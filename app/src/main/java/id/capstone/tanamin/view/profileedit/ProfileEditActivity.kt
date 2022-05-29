package id.capstone.tanamin.view.profileedit

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
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
import id.capstone.tanamin.utils.uriToFile
import id.capstone.tanamin.view.ViewModelFactory
import id.capstone.tanamin.view.login.LoginActivity
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
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")
    private var profilePicture: File?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        setupView()
        setupViewModel()

        val user=intent.getParcelableExtra<User>(ProfileFragment.PROFILE_USER_EXTRA) as User
        binding.etFullName.setText(user.name)
        binding.etAddress.setText(user.address)
        binding.etAge.setText(user.age.toString())

        Glide.with(this).load(user.profilePicture).placeholder(R.drawable.ic_profileuser_illustration)
            .error(R.drawable.ic_profileuser_illustration).into(binding.changeImage)
        binding.editPhotoBtn.setOnClickListener{
            startGallery()
        }
        binding.btnSave.setOnClickListener{
            val userid=user.idUser.toString().toRequestBody("text/plain".toMediaType())
            val name=binding.etFullName.text.toString().toRequestBody("text/plain".toMediaType())
            val age = binding.etAge.text.toString().toRequestBody("text/plain".toMediaType())
            val address = binding.etAddress.text.toString().toRequestBody("text/plain".toMediaType())
            val requestProfilePictureFile = profilePicture?.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val profilePictureMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "profile_picture",
                    profilePicture?.name,
                    requestProfilePictureFile!!
                )
            val liveData = profileEditViewModel.editProfile(profilePictureMultipart,userid,name,age,address)
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

        profileEditViewModel.filePhoto.observe(this){
            profilePicture=it
            val fileBitmap: Bitmap = BitmapFactory.decodeFile(it.path)
            binding.changeImage.setImageBitmap(fileBitmap)
        }
    }

    private fun setupView(){
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
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }else{
            bindAlert.closeButton.setOnClickListener {
                builder.dismiss()
            }
        }
        builder.show()
    }
}