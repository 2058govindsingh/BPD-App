package com.example.bpdapp.dao

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
    private val db = FirebaseFirestore.getInstance()
    private val policeCollection = db.collection("polices")
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
}