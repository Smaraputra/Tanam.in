package id.capstone.tanamin.view.classmodule

import android.content.Context
import android.content.Intent
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
import androidx.core.text.HtmlCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.data.remote.response.DataDetailModule
import id.capstone.tanamin.databinding.ActivityClassModuleBinding
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.utils.uriToFile
import id.capstone.tanamin.view.ViewModelFactory
import id.capstone.tanamin.view.quiz.QuizActivity
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ClassModuleActivity : AppCompatActivity() {
    private var _binding: ActivityClassModuleBinding?=null
    private val binding get():ActivityClassModuleBinding =_binding!!
    private var userId:Int=0
    private var modulId: Int = 0
    private var classId:Int=0
    private var classTitle: String=""
    private lateinit var classModuleViewModel: ClassModuleViewModel
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var statusViewModel : LiveData<Boolean>
    private lateinit var liveDataToken : LiveData<String>
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityClassModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.tvModuleDesc.loadSkeleton()
        binding.tvModuleTitle.loadSkeleton()
        binding.ivClassModule.loadSkeleton()
        binding.tvClassTitle.loadSkeleton()
        modulId=intent.getIntExtra(ID_MODULE_EXTRA,0)
        classId=intent.getIntExtra(ID_CLASS_EXTRA,0)
        classTitle= intent.getStringExtra(CLASS_TITLE_EXTRA).toString()
        binding.ivBackButton.setOnClickListener{
            finish()
        }
        setupViewModel()
        getDetailModule()
    }

    private fun setupView(dataDetailModule: DataDetailModule){
        binding.tvClassTitle.text = classTitle
        binding.tvModuleTitle.text = dataDetailModule.module[0].title
        binding.tvModuleDesc.text =
            HtmlCompat.fromHtml(dataDetailModule.module[0].content, HtmlCompat.FROM_HTML_MODE_LEGACY)
        if(dataDetailModule.module[0].idModuls!=1){
            binding.btnPrev.visibility=View.VISIBLE
            binding.btnPrev.setOnClickListener {
                val intent = Intent(this, ClassModuleActivity::class.java)
                intent.putExtra(ID_CLASS_EXTRA, dataDetailModule.classId.toInt())
                intent.putExtra(ID_MODULE_EXTRA,dataDetailModule.module[0].idModuls - 1)
                intent.putExtra(CLASS_TITLE_EXTRA,classTitle)
                finish()
                startActivity(intent)
            }
        }

        if(dataDetailModule.module[0].idModuls+1==dataDetailModule.maxId) {
            binding.btnUpProgress.visibility=View.VISIBLE
            binding.btnUpProgress.setOnClickListener {
                startGallery()
            }
        }
        binding.btnNext.setOnClickListener {
            if(dataDetailModule.module[0].idModuls+1!=dataDetailModule.maxId){
                val intent = Intent(this, ClassModuleActivity::class.java)
                intent.putExtra(ID_CLASS_EXTRA, dataDetailModule.classId.toInt())
                intent.putExtra(ID_MODULE_EXTRA,dataDetailModule.module[0].idModuls + 1)
                intent.putExtra(CLASS_TITLE_EXTRA,classTitle)
                finish()
                startActivity(intent)
            }else{
                val liveData=classModuleViewModel.isProgressPictureUploaded
                liveData.observe(this){ status->
                    if(status){
                        val intent = Intent(this, QuizActivity::class.java)
                        intent.putExtra(ID_CLASS_EXTRA, dataDetailModule.classId.toInt())
                        intent.putExtra(ID_MODULE_EXTRA,dataDetailModule.module[0].idModuls + 1)
                        intent.putExtra(CLASS_TITLE_EXTRA,classTitle)
                        finish()
                        startActivity(intent)
                    }else{
                        ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                            ?.let { showDialog("Anda belum mengunggah foto progress !", it,status) }
                    }
                    liveData.removeObservers(this)
                }
            }
        }
    }

    private fun setupViewModel(){
        val pref= LoginPreferences.getInstance(dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        liveDataToken=preferencesViewModel.getTokenUser()
        liveDataToken.observe(this){ token ->
            val factory: ViewModelFactory = ViewModelFactory.getInstance(this, token)
            val classModuleViewModel: ClassModuleViewModel by viewModels {
                factory
            }
            this.classModuleViewModel=classModuleViewModel
            preferencesViewModel.saveViewModelStatus(true)
            classModuleViewModel.setProgressPictureUploadedStatus(false)
            liveDataToken.removeObservers(this)
        }
    }

    private fun getDetailModule(){
        statusViewModel = preferencesViewModel.getViewModelStatus()
        statusViewModel.observe(this){ status ->
            if(status) {
                val moduleHashMap: HashMap<String, String> = HashMap()
                val liveDataPref=preferencesViewModel.getIDUser()
                liveDataPref.observe(this){ userId->
                    this.userId=userId
                    moduleHashMap["classid"]=classId.toString()
                    moduleHashMap["modulid"]=modulId.toString()
                    moduleHashMap["userid"]= userId.toString()
                    val liveDataDetailModule=classModuleViewModel.getDetailModule(moduleHashMap)
                    liveDataDetailModule.observe(this){ result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    binding.loadingModule.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    binding.loadingModule.visibility = View.GONE
                                    setupView(result.data.data)
                                    liveDataPref.removeObservers(this)
                                    liveDataDetailModule.removeObservers(this)
                                    binding.tvModuleDesc.hideSkeleton()
                                    binding.tvModuleTitle.hideSkeleton()
                                    binding.ivClassModule.hideSkeleton()
                                    binding.tvClassTitle.hideSkeleton()
                                }
                                is Result.Error -> {
                                    binding.loadingModule.visibility = View.GONE
                                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                                        ?.let { (showDialog(result.error, it, false)) }
                                    liveDataPref.removeObservers(this)
                                    liveDataDetailModule.removeObservers(this)
                                    binding.tvModuleDesc.hideSkeleton()
                                    binding.tvModuleTitle.hideSkeleton()
                                    binding.ivClassModule.hideSkeleton()
                                    binding.tvClassTitle.hideSkeleton()
                                }
                            }
                        }
                    }
                }
                statusViewModel.removeObservers(this)
            }
        }
    }

    fun showDialog(text: String, icon: Drawable,isFinishAct:Boolean) {
        val builder = AlertDialog.Builder(this).create()
        val bindAlert: CustomAlertApiBinding = CustomAlertApiBinding.inflate(LayoutInflater.from(this))
        builder.setView(bindAlert.root)
        bindAlert.infoDialog.text = text
        bindAlert.imageView5.setImageDrawable(icon)
        bindAlert.closeButton.setOnClickListener {
            builder.dismiss()
            if(isFinishAct){
                finish()
            }
        }
        builder.show()
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Foto Profil")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val selectedFile = uriToFile(selectedImg, this)
            uploadProgress(selectedFile)
        }
    }

    private fun uploadProgress(progressPicture: File){
        val userIdRequest=userId.toString().toRequestBody("text/plain".toMediaType())
        val classIdRequest=classId.toString().toRequestBody("text/plain".toMediaType())
        val requestProgressPicture = progressPicture.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val progressPictureMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "picture",
            progressPicture.name,
            requestProgressPicture
        )
        val liveData = classModuleViewModel.uploadProgress(progressPictureMultipart,userIdRequest,classIdRequest)
        liveData.observe(this){ result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loadingModule.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingModule.visibility = View.GONE
                        ContextCompat.getDrawable(this, R.drawable.ic_baseline_check_circle_24)
                            ?.let { showDialog(result.data.message, it,false) }
                        classModuleViewModel.setProgressPictureUploadedStatus(true)
                        liveData.removeObservers(this)
                    }
                    is Result.Error -> {
                        binding.loadingModule.visibility = View.GONE
                        ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                            ?.let { showDialog(result.error, it,false) }
                        liveData.removeObservers(this)
                    }
                }
            }
        }
    }
    companion object{
        const val ID_MODULE_EXTRA="id_modul"
        const val ID_CLASS_EXTRA="id_class"
        const val CLASS_TITLE_EXTRA="class_title"
    }
}