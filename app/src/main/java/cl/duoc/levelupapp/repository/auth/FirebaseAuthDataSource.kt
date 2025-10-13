package cl.duoc.levelupapp.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseAuthDataSource(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun signIn(email: String, pass: String): Result<FirebaseUser> =
        suspendCancellableCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        cont.resume(Result.success(user))
                    } else {
                        cont.resume(Result.failure(IllegalStateException("Usuario de Firebase nulo")))
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resume(Result.failure(exception))
                }
        }


    suspend fun signUp(email: String, pass: String): Result<FirebaseUser> =
        suspendCancellableCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        cont.resume(Result.success(user))
                    } else {
                        cont.resume(Result.failure(IllegalStateException("Usuario de Firebase nulo")))
                    }
                }
                .addOnFailureListener { exception ->
                    cont.resume(Result.failure(exception))
                }
        }

    suspend fun sendPasswordReset(email: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener { cont.resume(Result.success(Unit)) }
                .addOnFailureListener { exception ->
                    cont.resume(Result.failure(exception))
                }
        }


    fun currentUser(): FirebaseUser? = auth.currentUser
    fun signOut() = auth.signOut()
}