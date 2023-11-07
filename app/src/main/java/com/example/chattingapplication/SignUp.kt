package com.example.chattingapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUp : AppCompatActivity() {
    private lateinit var edtuser: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtpassword: EditText
    private lateinit var signupbtn: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        mAuth= FirebaseAuth.getInstance()
        edtuser=findViewById(R.id.userid)
        edtEmail=findViewById(R.id.emailid)
        edtpassword=findViewById(R.id.passwordid)
        signupbtn=findViewById(R.id.signup_btn)

        signupbtn.setOnClickListener {
            val name=edtuser.text.toString()
            val email=edtEmail.text.toString()
            val password=edtpassword.text.toString()
            signup(name,email,password)
        }
    }
    private fun signup(name:String,email:String,password:String){
        // logic for creating user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for jumping to home page

                    //!! adding this for nullsafe
                    addUserToDatabase(name,email,mAuth.currentUser?.uid!!)
                    val intent= Intent(this@SignUp,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                }
                else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignUp,"Aunthentication failed.Please Try Again!", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUserToDatabase(name: String,email: String,uid:String){

        mDbRef= FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name, email,uid))

    }
}