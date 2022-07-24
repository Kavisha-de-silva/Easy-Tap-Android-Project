package com.example.easytap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Splash_Screen : AppCompatActivity() {


    private lateinit var  firebaseAuth:FirebaseAuth

    private  val Splash_time:Long=3000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        firebaseAuth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            checkUser()
        }, Splash_time)
    }

    private fun checkUser() {
       val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser== null)
        {
          startActivity(Intent(this,MainActivity::class.java))
          finish()
        }
        else{


            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot){

                        val userType = snapshot.child("userType").value
                        if(userType == "user"){
                            startActivity(Intent(this@Splash_Screen,DashboardUserActivity::class.java))
                            finish()
                        }
                        else if(userType=="admin")
                        {
                            startActivity(Intent(this@Splash_Screen,DashboardAdminActivity::class.java))
                            finish()
                        }
                    }
                    override fun onCancelled(error: DatabaseError){

                    }

                })
        }
    }
}