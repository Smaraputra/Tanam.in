package id.capstone.tanamin.view.onboarding

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.databinding.ActivityOnBoardingBinding
import id.capstone.tanamin.view.login.LoginActivity

class OnBoardingActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupView()
        setupViewModel()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val pref = LoginPreferences.getInstance(dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        binding.button.setOnClickListener {
            preferencesViewModel.saveStatusOnBoard(true)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}