package id.capstone.tanamin.view.detectionresult

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.databinding.ActivityDetectionResultBinding
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.view.ViewModelFactory

class DetectionResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectionResultBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")
    private lateinit var detectionResultViewModel: DetectionResultViewModel
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var statusViewModel : LiveData<Boolean>
    private lateinit var liveDataToken : LiveData<String>
    private var photo : Bitmap? = null
    private var informationId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        informationId = intent.getIntExtra(INFO_ID,0)+1
        val byteArray = intent.getByteArrayExtra(BITMAP_DETECTION)
        if (byteArray != null) {
            photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.ivDetectionImage.setImageBitmap(photo)
        }
        setupView()
        setupViewModel()
        getInformation()
    }

    private fun setupView(){
        binding.ivBackButton.setOnClickListener{
            onBackPressed()
        }
    }

    private fun setupViewModel(){
        val pref= LoginPreferences.getInstance(this.dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        liveDataToken=preferencesViewModel.getTokenUser()
        liveDataToken.observe(this){ token ->
            val factory: ViewModelFactory = ViewModelFactory.getInstance(this, token)
            val detectionResultViewModel: DetectionResultViewModel by viewModels {
                factory
            }
            this.detectionResultViewModel=detectionResultViewModel
            preferencesViewModel.saveViewModelStatus(true)
            liveDataToken.removeObservers(this)
        }
    }

    private fun getInformation(){
        statusViewModel = preferencesViewModel.getViewModelStatus()
        statusViewModel.observe(this){ status ->
            if(status) {
                val liveData = detectionResultViewModel.getResultDetected(informationId.toString())
                liveData.observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.loadingModule.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.loadingModule.visibility = View.GONE
                                binding.tvDetectionItemName.text = result.data.data.judul
                                binding.expandTextView.text = result.data.data.description
                                binding.tvContentKandunganDesc.text = result.data.data.kandungan
                                binding.tvBenefitDesc.text = result.data.data.manfaat
                                liveData.removeObservers(this)
                            }
                            is Result.Error -> {
                                binding.loadingModule.visibility = View.GONE
                                ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                                    ?.let { showDialog(result.error, it) }
                                liveData.removeObservers(this)
                            }
                        }
                    }
                }
                statusViewModel.removeObservers(this)
            }
        }
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
        const val INFO_ID = "info_id"
        const val BITMAP_DETECTION = "bitmap_detection"
    }
}