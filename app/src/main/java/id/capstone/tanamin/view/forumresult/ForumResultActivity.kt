package id.capstone.tanamin.view.forumresult

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.data.remote.response.DataItem
import id.capstone.tanamin.data.remote.response.ListForumItem
import id.capstone.tanamin.databinding.ActivityForumResultBinding
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.view.ViewModelFactory

class ForumResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForumResultBinding
    private lateinit var liveDataStore : LiveData<Int>
    private var userId: Int = 0
    private lateinit var forumResultViewModel: ForumResultViewModel
    private lateinit var preferencesViewModel: PreferencesViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")
    private lateinit var dataDetail: ListForumItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        dataDetail = intent.getParcelableExtra<ListForumItem>(ID_FORUM) as ListForumItem
        setupView()
        setupViewModel()
        getMessage()
    }

    private fun setupView(){
        binding.forumQuestion.text = dataDetail.question
        binding.titleQuestion.text = dataDetail.title
        binding.ivBackButton2.setOnClickListener{
            onBackPressed()
        }
    }

    private fun setupViewModel(){
        val pref= LoginPreferences.getInstance(this.dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, "")
        val forumResultViewModel: ForumResultViewModel by viewModels {
            factory
        }
        this.forumResultViewModel=forumResultViewModel
        liveDataStore = preferencesViewModel.getIDUser()
        liveDataStore.observe(this) { userId ->
            this.userId = userId
        }
        binding.buttonGchatSend.setOnClickListener{
            sendMessage()
        }
    }

    private fun sendMessage(){
        val messageData: HashMap<String, String> = HashMap()
        messageData["idforum"] = dataDetail.id_forum.toString()
        messageData["userid"] = userId.toString()
        messageData["massage"] = binding.editGchatMessage.text.toString()
        val liveData = forumResultViewModel.sendMessage(messageData)
        liveData.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loadingModule2.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingModule2.visibility = View.GONE
                        liveData.removeObservers(this)
                        getMessage()
                    }
                    is Result.Error -> {
                        binding.loadingModule2.visibility = View.GONE
                        ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                            ?.let { showDialog(result.error, it) }
                        liveData.removeObservers(this)
                    }
                }
            }
        }
    }

    private fun getMessage(){
        val liveData = forumResultViewModel.getAllMessage(dataDetail.id_forum.toString())
        liveData.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loadingModule2.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingModule2.visibility = View.GONE
                        binding.cardViewNoInternet.visibility = View.GONE
                        binding.cardViewNoChat.visibility = View.GONE
                        setupAdapter(result.data.data)
                        liveData.removeObservers(this)
                    }
                    is Result.Error -> {
                        binding.loadingModule2.visibility = View.GONE
                        binding.cardViewNoChat.visibility = View.GONE
                        binding.cardViewNoInternet.visibility = View.GONE
                        if(result.error=="maaf forum yang anda cari tidak ditemukan"){
                            binding.cardViewNoChat.visibility = View.VISIBLE
                        }else{
                            binding.cardViewNoInternet.visibility = View.VISIBLE
                            ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                                ?.let { showDialog(result.error, it) }
                            liveData.removeObservers(this)
                        }
                    }
                }
            }
        }
    }

    private fun setupAdapter(listModule : List<DataItem>){
        val forumListAdapter = ForumResultAdapter(listModule, userId)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = forumListAdapter
    }

    fun showDialog(text: String, icon: Drawable) {
        val builder = AlertDialog.Builder(this).create()
        val bindAlert: CustomAlertApiBinding = CustomAlertApiBinding.inflate(LayoutInflater.from(this))
        builder.setView(bindAlert.root)
        bindAlert.infoDialog.text = text
        bindAlert.imageView5.setImageDrawable(icon)
        bindAlert.closeButton.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    companion object {
        const val ID_FORUM = "id_forum"
    }
}