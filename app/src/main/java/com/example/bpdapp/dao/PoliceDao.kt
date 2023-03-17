package com.example.bpdapp.dao

import android.util.Log
import com.example.bpdapp.models.EmailFinder
import com.example.bpdapp.models.Police
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PoliceDao {
    private val TAG = "Police Dao"
    private val db = FirebaseFirestore.getInstance()
    private val policeCollection = db.collection("patrollers")
    private val auth = Firebase.auth

    fun getPoliceByEmail(email: String): Task<DocumentSnapshot>
    {
        return policeCollection.document(email).get()
    }

    fun updateCount() {
        GlobalScope.launch {

            val currentPoliceEmail = auth.currentUser!!.email!!
            val police = getPoliceByEmail(currentPoliceEmail).await()
                .toObject(Police::class.java)!!
            police.cases++
            policeCollection.document(currentPoliceEmail).set(police)
        }
    }
    suspend fun linkWithEmail() {
        var _email =auth.currentUser!!.email
        if(_email != "" && _email != null) return

        Log.e(TAG, "ck1")
        val objEmail= FirebaseFirestore.getInstance().collection("mapPhoneNumberToEmail")
            .document("email").get().await()
            .toObject(EmailFinder::class.java)
        if(objEmail == null)
        {
            Log.e(TAG, "obj Email null ha")
            return
        }
        _email = objEmail.email

        Log.e(TAG, "ck2")
        val email :String
        if(_email == "")
        {
            Log.e(TAG, "_email null he yha")
            return
        }
        else

            email =_email

        Log.e(TAG, "the email updated is:$email")
        auth.currentUser!!.updateEmail(email)
            .addOnFailureListener { exception ->
                Log.e("Home", "the exception arises: $exception")
            }
            .addOnSuccessListener {
                Log.e("Home", "User email address updated.")
            }
    }
    suspend fun isValidPhoneNumber(phoneNumber : String): Boolean {
        var flag = false
        FirebaseFirestore.getInstance()
            .collection("mapPhoneNumberToEmail")
            .document(phoneNumber)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        flag = true
                        Log.e("MainActivity","Document exists!")
                    } else {
                        Log.e("MainActivity", "Document does not exist!")
                    }
                } else {
                    Log.e("MainActivity", "Failed with : ", task.exception)
                }
            }
            .await()

        return flag
    }
}