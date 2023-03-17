package com.example.bpdapp

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.bpdapp.dao.PoliceDao
import com.example.bpdapp.databinding.FragmentLoginOtpVerificationBinding
import com.example.bpdapp.models.EmailFinder
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
//import com.google.firebase.auth.R
//import io.grpc.InternalChannelz.id
import java.util.concurrent.TimeUnit
// SHA256 12:C3:AB:66:14:47:CA:60:03:E3:37:F9:30:87:EF:D8:2F:9D:D9:31:43:C9:D0:1E:D6:1F:FE:1B:80:78:5F:A0
// 17:4A:16:CC:D9:2C:10:BE:67:73:24:8C:48:78:84:A7:C7:29:85:CE
class LoginOtpVerificationFragment : Fragment() {
    val TAG = "Login OTP Tag"
    private var _binding : FragmentLoginOtpVerificationBinding? = null
    private val binding get() =_binding!!
    private lateinit var auth : FirebaseAuth
    private lateinit var verifyButton : Button
    private lateinit var resendTV : TextView
    private lateinit var inputOTP1 : EditText
    private lateinit var inputOTP2 : EditText
    private lateinit var inputOTP3 : EditText
    private lateinit var inputOTP4 : EditText
    private lateinit var inputOTP5 : EditText
    private lateinit var inputOTP6 : EditText
    private lateinit var progressBar : ProgressBar
    private lateinit var OTP : String
    private lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneNumber : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            resendToken= it.getParcelable("resendToken")!!
            Log.e(TAG, "this is resendtoken:$resendToken" )
            OTP= it.getString("otp").toString()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginOtpVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        init()
        auth = Firebase.auth
//        if(auth.currentUser ==null) Log.e(TAG, "auth.currentUser null he")
//        else if(auth.currentUser!!.phoneNumber ==null)
//            Log.e(TAG, "auth.currentUser.phoneNumber null he")
//        else
//            Log.e(TAG, "sab shi he")
        inputOTP1.requestFocus()
        progressBar.visibility = View.INVISIBLE
        addTextChangeListener()
        resendOTPTvVisibility()
        resendTV.setOnClickListener {
            resendVerificationCode()
            resendOTPTvVisibility()
        }
        verifyButton.setOnClickListener {
            val typedOTP = ( inputOTP1.text.toString()+ inputOTP2.text.toString()+inputOTP3.text.toString()+
                    inputOTP4.text.toString() + inputOTP5.text.toString()+inputOTP6.text.toString() )
            if(typedOTP.isNotEmpty())
            {
                if(typedOTP.length==6) {
                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        OTP, typedOTP
                    )
                    progressBar.visibility = View.VISIBLE
                    signInWithPhoneAuthCredential(credential)
                }else {
                    Toast.makeText(this.requireContext(), "Please enter the OTP", Toast.LENGTH_LONG).show()

                }
            } else {
                Toast.makeText(this.requireContext(), "Please enter the OTP", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun resendOTPTvVisibility() {
        inputOTP1.setText("")
        inputOTP2.setText("")
        inputOTP3.setText("")
        inputOTP4.setText("")
        inputOTP5.setText("")
        inputOTP6.setText("")
        resendTV.visibility = View.INVISIBLE
        resendTV.isEnabled = false

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            resendTV.visibility = View.VISIBLE
            resendTV.isEnabled = true
        }, 60000)
    }
    private fun resendVerificationCode(){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this.requireActivity())                 // Activity (for callback binding)
            .setCallbacks(mCallbacks)
            .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
            .build()
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
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
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
            OTP =verificationId
            resendToken= token

        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this.requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success1")
                    GlobalScope.launch {
                        val policeDao = PoliceDao()
                        withContext(Dispatchers.Main) {
                            policeDao.linkWithEmail()
                        }
                    }
                    Toast.makeText(activity, "Authenticated Successfully! ", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.INVISIBLE
                    sendToMain()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithPhoneAuthCredential:failure :${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
    private fun sendToMain() {
        startActivity(Intent(this.requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }
    private fun addTextChangeListener() {
        inputOTP1.addTextChangedListener(EditTextWatcher(inputOTP1))
        inputOTP2.addTextChangedListener(EditTextWatcher(inputOTP2))
        inputOTP3.addTextChangedListener(EditTextWatcher(inputOTP3))
        inputOTP4.addTextChangedListener(EditTextWatcher(inputOTP4))
        inputOTP5.addTextChangedListener(EditTextWatcher(inputOTP5))
        inputOTP6.addTextChangedListener(EditTextWatcher(inputOTP6))
    }

    private fun init() {
        auth =FirebaseAuth.getInstance()
        verifyButton = binding.verifyOtpButton
        resendTV= binding.resendTextView
        progressBar = binding.otpProgressBar
        inputOTP1= binding.otpEditText1
        inputOTP2= binding.otpEditText2
        inputOTP3= binding.otpEditText3
        inputOTP4= binding.otpEditText4
        inputOTP5= binding.otpEditText5
        inputOTP6= binding.otpEditText6

    }


    inner class EditTextWatcher(private val view : View ) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when(view.id)
            {
                R.id.otpEditText1 ->if(text.length == 1) inputOTP2.requestFocus()
                R.id.otpEditText2 ->if(text.length == 1) inputOTP3.requestFocus() else if(text.isEmpty()) inputOTP1.requestFocus()
                R.id.otpEditText3 ->if(text.length == 1) inputOTP4.requestFocus() else if(text.isEmpty()) inputOTP2.requestFocus()
                R.id.otpEditText4 ->if(text.length == 1) inputOTP5.requestFocus() else if(text.isEmpty()) inputOTP3.requestFocus()
                R.id.otpEditText5 ->if(text.length == 1) inputOTP6.requestFocus() else if(text.isEmpty()) inputOTP4.requestFocus()
                R.id.otpEditText6 ->if(text.isEmpty()) inputOTP5.requestFocus()

            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}