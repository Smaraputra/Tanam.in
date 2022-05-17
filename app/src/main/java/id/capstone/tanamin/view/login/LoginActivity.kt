package id.capstone.tanamin.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.capstone.tanamin.databinding.ActivityLoginBinding
import id.capstone.tanamin.view.MainActivity
import id.capstone.tanamin.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        binding.loginButton.isEnabled = true
        binding.loginButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.registerButton.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}