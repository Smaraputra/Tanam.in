package id.capstone.tanamin.view.forumcreate

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import id.capstone.tanamin.databinding.ActivityForumCreateBinding
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.view.ViewModelFactory

class ForumCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForumCreateBinding
    private lateinit var forumCreateViewModel: ForumCreateViewModel
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var liveDataStore : LiveData<Int>
    private lateinit var statusViewModel : LiveData<Boolean>
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")
    private var classID: Int = 0
    private var title = ""
    private var question = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumCreateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        classID = intent.getIntExtra(ID_CLASS,0)
        setupView()
        setupViewModel()
    }

    private fun setupView(){
        binding.etTitle.addTextChangedListener(textWatcher)
        binding.etQuestion.addTextChangedListener(textWatcher)
        binding.ivBackButton.setOnClickListener{
            onBackPressed()
        }
        binding.btnNext.setOnClickListener{
            sendData()
        }
    }

    private fun setupViewModel(){
        val pref= LoginPreferences.getInstance(this.dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        preferencesViewModel.getTokenUser().observe(this){ token ->
            val factory: ViewModelFactory = ViewModelFactory.getInstance(this, token)
            val forumCreateViewModel: ForumCreateViewModel by viewModels {
                factory
            }
            this.forumCreateViewModel=forumCreateViewModel
            preferencesViewModel.saveViewModelStatus(true)
        }
    }

    private fun sendData(){
        statusViewModel = preferencesViewModel.getViewModelStatus()
        statusViewModel.observe(this){ status ->
            if(status) {
                val homeMap: HashMap<String, String> = HashMap()
                liveDataStore = preferencesViewModel.getIDUser()
                liveDataStore.observe(this) { userId ->
                    homeMap["userid"] = userId.toString()
                    homeMap["classid"] = classID.toString()
                    homeMap["title"] = title
                    homeMap["question"] = question
                    val liveData = forumCreateViewModel.createForum(homeMap)
                    liveData.observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    binding.loadingModule.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    binding.loadingModule.visibility = View.GONE
                                    ContextCompat.getDrawable(
                                        this,
                                        R.drawable.ic_baseline_check_circle_24
                                    )
                                        ?.let { showDialog(result.data.message, it, true) }
                                    liveData.removeObservers(this)
                                    liveDataStore.removeObservers(this)
                                }
                                is Result.Error -> {
                                    binding.loadingModule.visibility = View.GONE
                                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                                        ?.let { showDialog(result.error, it, false) }
                                    liveData.removeObservers(this)
                                    liveDataStore.removeObservers(this)
                                }
                            }
                        }
                    }
                }
            }
            statusViewModel.removeObservers(this)
        }
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
                finish()
            }
        }else{
            bindAlert.closeButton.setOnClickListener {
                builder.dismiss()
            }
        }
        builder.show()
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            title = binding.etTitle.text.toString()
            question = binding.etQuestion.text.toString()
            binding.btnNext.isEnabled = title.length<=30 && question.length <=200 && title.isNotEmpty() && question.isNotEmpty()
        }
        override fun afterTextChanged(s: Editable) {

        }
    }

    companion object {
        const val ID_CLASS = "id_class"
    }
}