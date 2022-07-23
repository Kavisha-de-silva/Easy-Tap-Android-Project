package com.example.easytap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.result.contract.ActivityResultContracts

class Splash_Screen : AppCompatActivity() {


    private  val Splash_time:Long=3000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            startActivity(Intent(this,Share_Image_Text::class.java))
            finish()
        }, Splash_time)
    }
}