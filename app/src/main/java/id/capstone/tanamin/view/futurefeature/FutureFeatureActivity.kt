package id.capstone.tanamin.view.futurefeature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.capstone.tanamin.R
import id.capstone.tanamin.databinding.ActivityFutureFeatureBinding
import id.capstone.tanamin.databinding.ActivityLoginBinding

class FutureFeatureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFutureFeatureBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFutureFeatureBinding.inflate(layoutInflater)
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