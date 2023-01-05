package com.example.bpdapp.dao

import android.util.Log
import com.example.bpdapp.models.Criminal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CriminalDao {
    private val db = FirebaseFirestore.getInstance()
    private val criminalCollection = db.collection("criminals")

    suspend fun checkCriminal(name: String, phone: String, aadhaar: String): MutableList<Criminal> {
        var result: MutableSet<Criminal> = mutableSetOf()
        val result1: MutableSet<Criminal> = mutableSetOf()
        val result2: MutableSet<Criminal> = mutableSetOf()
        val result3: MutableSet<Criminal> = mutableSetOf()

        criminalCollection
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { it ->
                for (criminal in it.documents) {
                    criminal.toObject(Criminal::class.java)?.let { it1 -> result1.add(it1) }
                }
                if (result1.isNotEmpty())
                    result = result1
            }
            .addOnFailureListener { exception ->
                Log.w("Firebase", "Error getting documents: $exception")
            }
            .await()


        criminalCollection
            .whereEqualTo("phone", phone)
            .get()
            .addOnSuccessListener { it ->
                for (criminal in it.documents) {
                    criminal.toObject(Criminal::class.java)?.let { it1 -> result2.add(it1) }
                }
                if (result2.isNotEmpty()) {
                    result = if (result.isEmpty())
                        result2.toMutableSet()
                    else {
                        val temp: MutableSet<Criminal> = mutableSetOf()
                        for (criminal in result) {
                            if (result2.contains(criminal)) {
                                temp.add(criminal)
                            }
                        }
                        temp
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firebase", "Error getting documents: $exception")
            }
            .await()

        criminalCollection
            .whereEqualTo("aadhaar", aadhaar)
            .get()
            .addOnSuccessListener { it ->
                for (criminal in it.documents) {
                    criminal.toObject(Criminal::class.java)?.let { it1 -> result3.add(it1) }
                }
                if (result3.isNotEmpty()) {
                    result = if (result.isEmpty())
                        result3.toMutableSet()
                    else {
                        val temp: MutableSet<Criminal> = mutableSetOf()
                        for (criminal in result) {
                            if (result3.contains(criminal)) {
                                temp.add(criminal)
                            }
                        }
                        temp
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firebase", "Error getting documents: $exception")
            }
            .await()

        return result.toMutableList()
    }
}