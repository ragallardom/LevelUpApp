package cl.duoc.levelupapp.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseAuthDataSource(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun signIn(email: String, pass: String): FirebaseUser =
        suspendCancellableCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        cont.resume(user)
                    } else {
                        cont.resumeWithException(IllegalStateException("Usuario sin datos"))
                    }
                }
                .addOnFailureListener { exception -> cont.resumeWithException(exception) }
        }


    suspend fun signUp(email: String, pass: String): FirebaseUser =
        suspendCancellableCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        cont.resume(user)
                    } else {
                        cont.resumeWithException(IllegalStateException("Usuario sin datos"))
                    }
                }
                .addOnFailureListener { exception -> cont.resumeWithException(exception) }
        }

    suspend fun sendPasswordReset(email: String): Boolean =
        suspendCancellableCoroutine { cont ->
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener { cont.resume(true) }
                .addOnFailureListener { cont.resume(false) }
        }


    fun currentUser(): FirebaseUser? = auth.currentUser
    fun signOut() = auth.signOut()
}
