package id.capstone.tanamin.view.classfinished

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.capstone.tanamin.databinding.ActivityClassFinishedBinding
import id.capstone.tanamin.view.MainActivity

class ClassFinishedActivity : AppCompatActivity() {
    private var classTitle: String = ""
    private lateinit var binding: ActivityClassFinishedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassFinishedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        classTitle = intent.getStringExtra(CLASS_FINISH).toString()
        setupView()
    }

    private fun setupView(){
        binding.tvFinishedClassName.text = classTitle
        binding.btnBack.setOnClickListener{
            val intent = Intent(this@ClassFinishedActivity, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    companion object{
        const val CLASS_FINISH="class_finish"
    }
}