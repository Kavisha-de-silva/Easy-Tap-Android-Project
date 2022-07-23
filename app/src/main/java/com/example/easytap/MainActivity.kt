package com.example.easytap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val workerButton=findViewById<Button>(R.id.btn_worker)
        val customerButton=findViewById<Button>(R.id.btn_customer)

        workerButton.setOnClickListener{
            val intent=Intent(this,Login_Register::class.java)

            startActivity(intent)
        }
    }
}