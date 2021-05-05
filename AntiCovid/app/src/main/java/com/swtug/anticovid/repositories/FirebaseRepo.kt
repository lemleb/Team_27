package com.swtug.anticovid.repositories

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.swtug.anticovid.models.User

object FirebaseRepo {

    private const val COLLECTION_USER = "User"


    fun saveUser(user: User, firebaseListener: FirebaseListener) {
        firebaseListener.onStart()
        val firebaseFireStore = FirebaseFirestore.getInstance()

        firebaseFireStore.collection(COLLECTION_USER).document(user.email).get()
            .addOnSuccessListener { document ->
                if(document == null || !document.exists()) {
                    firebaseFireStore.collection(COLLECTION_USER).document(user.email).set(user)
                    .addOnSuccessListener {
                        firebaseListener.onSuccess(user)
                    }
                    .addOnFailureListener {
                        firebaseListener.onFailure()
                    }
                } else {
                    firebaseListener.onSuccess(null)
                }
        }.addOnFailureListener {
            firebaseListener.onFailure()
        }
    }

    fun getUser(email: String, firebaseListener: FirebaseListener) {
        firebaseListener.onStart()
        var user: User? = null
        val firebaseStore = FirebaseFirestore.getInstance()

        firebaseStore.collection(COLLECTION_USER).document(email).get()
            .addOnSuccessListener { document ->

                if(document != null && document.exists()) {
                    user = User(
                        document["id"] as Long,
                        document["name"] as String,
                        document["surname"] as String,
                        document["email"] as String,
                        document["address"] as String,
                        document["secid"] as String,
                        document["phonenumber"] as String,
                        document["password"] as String
                    )
                }

                firebaseListener.onSuccess(user)
            }
            .addOnFailureListener {
                firebaseListener.onFailure()
            }
    }
}