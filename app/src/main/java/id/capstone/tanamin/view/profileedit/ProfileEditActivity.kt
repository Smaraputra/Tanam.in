package id.capstone.tanamin.view.profileedit

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.data.remote.response.User
import id.capstone.tanamin.databinding.ActivityProfileEditBinding
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.databinding.CustomAlertLogoutBinding
import id.capstone.tanamin.utils.uriToFile
import id.capstone.tanamin.view.ViewModelFactory
import id.capstone.tanamin.view.profile.ProfileFragment
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var profileEditViewModel: ProfileEditViewModel
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var user: User
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")
    private var profilePicture: File?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        user=intent.getParcelableExtra<User>(ProfileFragment.PROFILE_USER_EXTRA) as User
        setupView()
        setupViewModel()
        profileEditViewModel.setIsProfileEdited(false)
        binding.etFullName.addTextChangedListener(textWatcher)
        binding.etAge.addTextChangedListener(textWatcher)
        binding.etAddress.addTextChangedListener(textWatcher)

        profileEditViewModel.filePhoto.observe(this){
            profilePicture=it
            val fileBitmap: Bitmap = BitmapFactory.decodeFile(it.path)
            Glide.with(this).load(fileBitmap).into(binding.changeImage)
        }
        binding.editPhotoBtn.setOnClickListener{
            startGallery()
        }
        binding.btnSave.setOnClickListener{
           updateProfile()
        }
        profileEditViewModel.isProfileEdited.observe(this){
            binding.btnSave.isEnabled = it
        }
    }

    private fun setupView(){
        binding.etFullName.setText(user.name)
        if(user.address != "dikosongkan"){
            binding.etAddress.setText(user.address)
        }
        if(user.age != null && user.age !=0){
            binding.etAge.setText(user.age.toString())
        }
        Glide.with(this).load(user.profilePicture).placeholder(R.drawable.ic_profileuser_illustration)
            .error(R.drawable.ic_profileuser_illustration).into(binding.changeImage)
        binding.ivBackButton.setOnClickListener{
            onBackPressed()
        }

    }
    private fun setupViewModel(){
        val pref= LoginPreferences.getInstance(dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, "")
        val profileEditViewModel: ProfileEditViewModel by viewModels {
            factory
        }
        this.profileEditViewModel=profileEditViewModel
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val selectedFile = uriToFile(selectedImg, this)
            profileEditViewModel.setFilePhoto(selectedFile)
        }
    }

    private fun updateProfile(){
        val userid=user.idUser.toString().toRequestBody("text/plain".toMediaType())
        val name=if(binding.etFullName.text.toString().isNotEmpty())binding.etFullName.text.toString().toRequestBody("text/plain".toMediaType())
            else "dikosongkan".toRequestBody("text/plain".toMediaType())
        val age = if(binding.etAge.text.toString().isNotEmpty())binding.etAge.text.toString().toRequestBody("text/plain".toMediaType())
            else "0".toRequestBody("text/plain".toMediaType())
        val address = if(binding.etAddress.text.toString().isNotEmpty())binding.etAddress.text.toString().toRequestBody("text/plain".toMediaType())
            else "dikosongkan".toRequestBody("text/plain".toMediaType())
        var liveData=profileEditViewModel.editProfile(null,userid,name,age,address)
        if(profilePicture!=null){
            val requestProfilePictureFile = profilePicture?.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val profilePictureMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "profile_picture",
                profilePicture?.name,
                requestProfilePictureFile!!
            )
            liveData = profileEditViewModel.editProfile(profilePictureMultipart,userid,name,age,address)
        }
        liveData.observe(this){ result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loadingModule.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingModule.visibility = View.GONE
                        ContextCompat.getDrawable(this, R.drawable.ic_baseline_check_circle_24)
                            ?.let { showDialog(result.data.message, it, true) }
                        preferencesViewModel.saveNameUser(binding.etFullName.text.toString())
                        liveData.removeObservers(this)
                    }
                    is Result.Error -> {
                        binding.loadingModule.visibility = View.GONE
                        ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                            ?.let { showDialog(result.error, it, false) }
                        liveData.removeObservers(this)
                    }
                }
            }
        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Foto Profil")
        launcherIntentGallery.launch(chooser)
    }
    private fun showDialog(text: String, icon: Drawable, status: Boolean) {
        val builder = AlertDialog.Builder(this).create()
        val bindAlert: CustomAlertApiBinding = CustomAlertApiBinding.inflate(LayoutInflater.from(this))
        builder.setView(bindAlert.root)
        bindAlert.infoDialog.text = text
        bindAlert.imageView5.setImageDrawable(icon)
        if(status){
            bindAlert.closeButton.setOnClickListener {
                builder.dismiss()
                builder.setCancelable(false)
                finish()
            }
        }else{
            bindAlert.closeButton.setOnClickListener {
                builder.dismiss()
            }
        }
        builder.show()
    }

    private fun showDialogLogout() {
        val builder = AlertDialog.Builder(this).create()
        val bindAlert: CustomAlertLogoutBinding = CustomAlertLogoutBinding.inflate(LayoutInflater.from(this))
        builder.setView(bindAlert.root)
        bindAlert.imageView5.visibility=View.GONE
        bindAlert.infoDialog.text = getString(R.string.confirm_edit_profile)
        bindAlert.logoutConfirm.text=getString(R.string.change_button)
        bindAlert.logoutConfirm.setOnClickListener {
            updateProfile()
            builder.dismiss()
        }
        bindAlert.cancelButton.setOnClickListener {
            super.onBackPressed()
            builder.dismiss()
        }
        builder.show()
    }

    private val textWatcher: TextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }
        override fun afterTextChanged(s: Editable) {
            val newName=binding.etFullName.text.toString()
            val newAge=binding.etAge.text.toString()
            val newAddress=binding.etAddress.text.toString()
            if(newName != user.name || newAddress != user.address || newAge != user.age.toString() || profilePicture!=null){
                profileEditViewModel.setIsProfileEdited(true)
            }else{
                profileEditViewModel.setIsProfileEdited(false)
            }
        }
    }

    override fun onBackPressed() {
        val liveData=profileEditViewModel.isProfileEdited
        liveData.observe(this){
            if(it){
                showDialogLogout()
            }else{
                super.onBackPressed()
            }
            liveData.removeObservers(this)
        }

    }
}