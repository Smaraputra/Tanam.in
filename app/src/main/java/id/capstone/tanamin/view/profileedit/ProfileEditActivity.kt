package id.capstone.tanamin.view.profileedit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.capstone.tanamin.databinding.ActivityProfileEditBinding

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        setupView()
    }

    private fun setupView(){
        binding.ivBackButton.setOnClickListener{
            onBackPressed()
        }
    }
}