package com.example.bpdapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.bpdapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

val TAG = "Login Activity Tag"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth


        binding.loginButton.setOnClickListener {

<<<<<<< HEAD
            binding.appBarLayout.visibility = View.GONE
=======
            binding.loginAppBarLayout.visibility = View.GONE
>>>>>>> test1
            binding.loginTphLogo.visibility = View.GONE
            binding.textFieldEmail.visibility = View.GONE
            binding.textFieldPassword.visibility = View.GONE
            binding.loginForgetPassword.visibility = View.GONE
            binding.loginButton.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            val email = binding.inputFieldEmail.text.toString().trim()
            val password = binding.inputFieldPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null)
        {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        }
        else
        {
<<<<<<< HEAD
            binding.appBarLayout.visibility = View.VISIBLE
=======
            binding.loginAppBarLayout.visibility = View.VISIBLE
>>>>>>> test1
            binding.loginTphLogo.visibility = View.VISIBLE
            binding.textFieldEmail.visibility = View.VISIBLE
            binding.textFieldPassword.visibility = View.VISIBLE
            binding.loginForgetPassword.visibility = View.VISIBLE
            binding.loginButton.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }
}