package id.capstone.tanamin.view.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.capstone.tanamin.R
import id.capstone.tanamin.view.classdetail.ClassDetailActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val i = Intent(this, ClassDetailActivity::class.java)
        startActivity(i)
    }
}