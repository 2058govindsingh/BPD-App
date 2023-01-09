package com.example.bpdapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
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

        emailPasswordSignIn()
        forgotPassword()

    }

    private fun emailPasswordSignIn() {
        auth = Firebase.auth

        binding.layoutLoginFile.loginButton.setOnClickListener {
            val email = binding.layoutLoginFile.loginEmail.text.toString().trim()
            val password = binding.layoutLoginFile.loginPassword.text.toString()

            var fieldEmpty = false

            if (email == "") {
                fieldEmpty = true
                binding.layoutLoginFile.textInputLoginEmail.error = "Email field can't be empty."
            } else {
                binding.layoutLoginFile.textInputLoginEmail.error = null
            }

            if (password == "") {
                fieldEmpty = true
                binding.layoutLoginFile.textInputLoginPassword.error = "Password field can't be empty."
            } else {
                binding.layoutLoginFile.textInputLoginPassword.error = null
            }

            if (fieldEmpty)
                return@setOnClickListener

            binding.layoutLogin.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

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
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }

    }

    private fun forgotPassword() {
        binding.layoutLoginFile.forgotPasswordTV.setOnClickListener {
            val email = binding.layoutLoginFile.loginEmail.text.toString()

            binding.layoutLoginFile.textInputLoginPassword.error = null

            if (email == "") {
                binding.layoutLoginFile.textInputLoginEmail.error = "Email field can't be empty."
                return@setOnClickListener
            } else {
                binding.layoutLoginFile.textInputLoginEmail.error = null
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                        Toast.makeText(this, "Password reset link has been sent to email.",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Please check your email address.",
                            Toast.LENGTH_SHORT).show()
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
            binding.layoutLogin.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }
}