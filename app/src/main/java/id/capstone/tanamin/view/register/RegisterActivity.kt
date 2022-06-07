package id.capstone.tanamin.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
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
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.databinding.ActivityRegisterBinding
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.utils.encryptInput
import id.capstone.tanamin.view.ViewModelNoTokenFactory
import id.capstone.tanamin.view.login.LoginActivity
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var name: String
    private lateinit var password : String
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        setupView()
        setupViewModel()
        playAnimation()
    }

    private fun setupView() {
        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.nameEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)
        binding.registerButton.setOnClickListener {
            sendData()
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelNoTokenFactory = ViewModelNoTokenFactory.getInstance(this)
        val registerViewModel: RegisterViewModel by viewModels {
            factory
        }
        this.registerViewModel=registerViewModel
    }

    private fun playAnimation() {
        val titleName = ObjectAnimator.ofFloat(binding.nameTitle, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(500)
        val titleEmail = ObjectAnimator.ofFloat(binding.emailTitle, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val titlePass = ObjectAnimator.ofFloat(binding.passwordTitle, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)

        val togetherName = AnimatorSet().apply {
            playTogether(titleName, name)
        }

        val togetherEmail = AnimatorSet().apply {
            playTogether(titleEmail, email)
        }

        val togetherPass = AnimatorSet().apply {
            playTogether(titlePass, pass)
        }

        val togetherButton = AnimatorSet().apply {
            playTogether(login, register)
        }

        AnimatorSet().apply {
            playSequentially(togetherName, togetherEmail, togetherPass, togetherButton)
            start()
        }
    }

    private fun sendData(){
        val registerMap: HashMap<String, String> = HashMap()
        registerMap["name"] = name
        registerMap["email"] = email
        registerMap["password"] = encryptInput(password)
        val liveData = registerViewModel.registerUser(registerMap)
        liveData.observe(this){ result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loadingList.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingList.visibility = View.GONE
                        ContextCompat.getDrawable(this, R.drawable.ic_baseline_check_circle_24)
                            ?.let { showDialog(result.data.message, it, true) }
                        liveData.removeObservers(this)
                    }
                    is Result.Error -> {
                        binding.loadingList.visibility = View.GONE
                        ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                            ?.let { showDialog(result.error, it, false) }
                        liveData.removeObservers(this)
                    }
                }
            }
        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            name = binding.nameEditText.text.toString()
            email = binding.emailEditText.text.toString()
            password = binding.passwordEditText.text.toString()
            val pattern: Pattern = Pattern.compile(REGEX_EMAIL)
            val matcher: Matcher = pattern.matcher(email)
            binding.registerButton.isEnabled =
                !(!matcher.matches() || email.isEmpty() || password.length<6 || password.isEmpty() || name.length>31 || name.isEmpty())
        }
        override fun afterTextChanged(s: Editable) {

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
                builder.setCancelable(false)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }else{
            bindAlert.closeButton.setOnClickListener {
                builder.dismiss()
            }
        }
        builder.show()
    }

    companion object {
        const val REGEX_EMAIL = "^[\\w!#\$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#\$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$"
    }
}