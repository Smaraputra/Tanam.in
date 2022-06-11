package id.capstone.tanamin.view.futurefeature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.capstone.tanamin.databinding.ActivityFutureFeatureBinding

class FutureFeatureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFutureFeatureBinding
    private var title = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFutureFeatureBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        title= intent.getStringExtra(FEATURE_FUTURE).toString()
        setupView()
    }

    private fun setupView(){
        binding.ivBackButton.setOnClickListener{
            finish()
        }
        binding.tvFutureFeature.text = title
    }

    companion object {
        const val FEATURE_FUTURE = "feature_future"
    }
}