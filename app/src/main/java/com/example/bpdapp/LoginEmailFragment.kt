package com.example.bpdapp
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.bpdapp.databinding.FragmentLoginEmailBinding
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.bpdapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
class LoginEmailFragment : Fragment() {
    val TAG = "Login Email Tag"
    private var _binding : FragmentLoginEmailBinding? = null
    private val binding get() =_binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = Firebase.auth
        emailPasswordSignIn()
        forgotPassword()
        binding.button2.setOnClickListener{
            val action = LoginEmailFragmentDirections.actionLoginEmailFragmentToLoginPhoneNumberFragment2()
            binding.root.findNavController().navigate(action)
        }
    }
    private fun emailPasswordSignIn() {

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
                .addOnCompleteListener(this.requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this.requireContext(), "Authentication failed.",
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
                        Toast.makeText(this.requireContext(), "Password reset link has been sent to email.",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this.requireContext(), "Please check your email address.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null)
        {
            val mainActivityIntent = Intent(this.requireContext(), MainActivity::class.java)
            startActivity(mainActivityIntent)
            activity?.finish()
        }
        else
        {
            binding.layoutLogin.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
