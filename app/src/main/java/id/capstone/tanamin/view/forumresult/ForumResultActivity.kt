package id.capstone.tanamin.view.forumresult

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.capstone.tanamin.databinding.ActivityForumResultBinding

class ForumResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForumResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        setupView()
    }

    private fun setupView(){
        binding.ivBackButton2.setOnClickListener{
            onBackPressed()
        }
    }
}