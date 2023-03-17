package com.example.bpdapp

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.bpdapp.dao.PoliceDao
import com.example.bpdapp.databinding.FragmentLoginEmailBinding
import com.example.bpdapp.databinding.FragmentLoginPhoneNumberBinding
import com.example.bpdapp.models.EmailFinder
import com.example.bpdapp.models.Police
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit


class LoginPhoneNumberFragment : Fragment() {
    val TAG = "Login Phone Tag"
    private var _binding : FragmentLoginPhoneNumberBinding? = null
    private val binding get() =_binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var countryCodePicker : CountryCodePicker
    private lateinit var phoneNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = Firebase.auth
        phoneNumberSignIn()
        binding.button2.setOnClickListener{
            val action = LoginPhoneNumberFragmentDirections.actionLoginPhoneNumberFragment2ToLoginEmailFragment()
            binding.root.findNavController().navigate(action)
        }
    }

    private fun phoneNumberSignIn() {
        binding.button.setOnClickListener {
            val ccp = binding.countryCodeSelector
            //adding phoneNumber to country code. Now "+91" becomes "+91 9876543210"
            ccp.registerPhoneNumberTextView(binding.phoneNumber)
            phoneNumber =  ccp.fullNumberWithPlus.replace(" ","")
            Log.e(TAG, phoneNumber)
            val dao = PoliceDao()
            if (phoneNumber.length<5) return@setOnClickListener
            GlobalScope.launch(Dispatchers.IO) {
                if (dao.isValidPhoneNumber(phoneNumber)) {
                    withContext(Dispatchers.Main) {
                        Log.e(TAG, "Given phone number is validd$phoneNumber")
                        binding.getOtpProgressBar.visibility = View.VISIBLE
                        initiateOtp(phoneNumber)

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e(TAG, "Given phone number is not valid")
                        Toast.makeText(activity, "Enter a registered phone number", Toast.LENGTH_LONG ).show()
                    }
                }
            }
        }
    }
    private fun initiateOtp(phoneNumber :String) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this.requireActivity())                 // Activity (for callback binding)
            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.e(TAG, "onVerificationCompleted:$credential")
            binding.getOtpProgressBar.visibility= View.INVISIBLE
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.e(TAG, "onVerificationFailed", e)
            binding.getOtpProgressBar.visibility= View.INVISIBLE

            if (e is FirebaseAuthInvalidCredentialsException) {
                Log.e(TAG, "acha phone number galat he")
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
//            Log.e(TAG, "Phone Number kya he:${auth.currentUser!!.phoneNumber!!}")
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "onCodeSent :$verificationId")

                    val action =
                        LoginPhoneNumberFragmentDirections.actionLoginPhoneNumberFragment2ToLoginOtpVerificationFragment(
                            resendToken = token, otp =
                            verificationId
                        )
                    binding.getOtpProgressBar.visibility = View.INVISIBLE
                    binding.root.findNavController().navigate(action)
                }
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this.requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e(TAG, "signInWithCredential:success")
                    GlobalScope.launch {
                        val policeDao = PoliceDao()
                        withContext(Dispatchers.Main) {
                            policeDao.linkWithEmail()
                        }
                    }

                    Toast.makeText(requireContext(), "Authenticated Successfully! ", Toast.LENGTH_LONG).show()
                    updateUI(auth.currentUser)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.e(TAG, "signInWithPhoneAuthCredential:failure :${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
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
    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
//            Toast.makeText(this.requireContext(),"Already Signed in.. Redirecting..${currentUser.phoneNumber}", Toast.LENGTH_SHORT).show()
            updateUI(currentUser)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
