package id.capstone.tanamin.view.classmodule

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.data.remote.response.DataDetailModule
import id.capstone.tanamin.databinding.ActivityClassModuleBinding
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.view.ViewModelFactory
import id.capstone.tanamin.view.classdetail.ClassDetailActivity
import id.capstone.tanamin.view.classdetail.silabus.SilabusListAdapter

class ClassModuleActivity : AppCompatActivity() {
    private var _binding: ActivityClassModuleBinding?=null
    private val binding get():ActivityClassModuleBinding =_binding!!
    private var modulId: Int = 0
    private var classId:Int=0
    private var classTitle: String=""
    private lateinit var classModuleViewModel: ClassModuleViewModel
    private lateinit var preferencesViewModel: PreferencesViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityClassModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        modulId=intent.getIntExtra(ID_MODULE_EXTRA,0)
        classId=intent.getIntExtra(ID_CLASS_EXTRA,0)
        classTitle= intent.getStringExtra(CLASS_TITLE_EXTRA).toString()
        setupViewModel()
        getDetailModule()
    }
    private fun getDetailModule(){
        val moduleHashMap: HashMap<String, String> = HashMap()
        val liveDataPref=preferencesViewModel.getIDUser()
        liveDataPref.observe(this){ userId->
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
                        }
                        is Result.Error -> {
                            binding.loadingModule.visibility = View.GONE
                            ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                                ?.let { (showDialog(result.error, it)) }
                            liveDataPref.removeObservers(this)
                            liveDataDetailModule.removeObservers(this)
                        }
                    }
                }
            }
        }
    }
    private fun setupView(dataDetailModule: DataDetailModule){
        binding.tvClassTitle.setText(classTitle)
        binding.tvModuleTitle.setText(dataDetailModule.module[0].title)
        binding.tvModuleDesc.setText(HtmlCompat.fromHtml(dataDetailModule.module[0].content, HtmlCompat.FROM_HTML_MODE_LEGACY))
        if(dataDetailModule.module[0].idModuls==1){
            binding.btnPrev.visibility=View.GONE
        }else{
            binding.btnPrev.setOnClickListener {
                val intent = Intent(this, ClassModuleActivity::class.java)
                intent.putExtra(ID_CLASS_EXTRA, dataDetailModule.classId.toInt())
                intent.putExtra(ID_MODULE_EXTRA,dataDetailModule.module[0].idModuls - 1)
                intent.putExtra(CLASS_TITLE_EXTRA,classTitle)
                finish()
                startActivity(intent)
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
                //intent to quiz activity goes here
            }


        }
    }
    private fun setupViewModel(){
        val pref= LoginPreferences.getInstance(dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, "")
        val classModuleViewModel: ClassModuleViewModel by viewModels {
            factory
        }
        this.classModuleViewModel=classModuleViewModel
    }
    fun showDialog(text: String, icon: Drawable) {
        val builder = AlertDialog.Builder(this).create()
        val bindAlert: CustomAlertApiBinding = CustomAlertApiBinding.inflate(LayoutInflater.from(this))
        builder.setView(bindAlert.root)
        bindAlert.infoDialog.text = text
        bindAlert.imageView5.setImageDrawable(icon)
        bindAlert.closeButton.setOnClickListener {
            builder.dismiss()
            finish()
        }
        builder.show()
    }
    companion object{
        const val ID_MODULE_EXTRA="id_modul"
        const val ID_CLASS_EXTRA="id_class"
        const val CLASS_TITLE_EXTRA="class_title"
    }
}