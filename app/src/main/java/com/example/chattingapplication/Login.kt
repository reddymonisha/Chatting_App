package com.example.chattingapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtpassword: EditText
    private lateinit var loginbtn: Button
    private lateinit var signupbtn: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        mAuth= FirebaseAuth.getInstance()
        edtEmail=findViewById(R.id.emailid)
        edtpassword=findViewById(R.id.passwordid)
        loginbtn=findViewById(R.id.login_btn)
        signupbtn=findViewById(R.id.signup_btn)

        signupbtn.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        loginbtn.setOnClickListener {
            val email=edtEmail.text.toString()
            val password=edtpassword.text.toString()
            login(email,password)
        }
    }
    private fun login(email:String,password:String){

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for logging user
                    val intent=Intent(this@Login,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Login,"User Does not exist",Toast.LENGTH_SHORT).show()

                }
            }
    }
}