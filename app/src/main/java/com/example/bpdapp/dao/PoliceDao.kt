package com.example.bpdapp.dao

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class PoliceDao {
    private val db = FirebaseFirestore.getInstance()
    private val policeCollection = db.collection("polices")

    fun getPoliceByEmail(email: String): Task<DocumentSnapshot>
    {
        return policeCollection.document(email).get()
    }
}