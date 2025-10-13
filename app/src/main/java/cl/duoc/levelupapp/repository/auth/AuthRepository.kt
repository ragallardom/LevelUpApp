package cl.duoc.levelupapp.repository.auth

import cl.duoc.levelupapp.model.User

class AuthRepository(
    private val ds: FirebaseAuthDataSource = FirebaseAuthDataSource()
) {
    suspend fun login(email: String, pass: String): Result<User> =
        ds.signIn(email, pass).map { firebaseUser ->
            User(uid = firebaseUser.uid, email = firebaseUser.email ?: email)
        }

    suspend fun signUp(email: String, pass: String): Result<User> =
        ds.signUp(email, pass).map { firebaseUser ->
            User(uid = firebaseUser.uid, email = firebaseUser.email ?: email)
        }

    suspend fun sendPasswordReset(email: String): Result<Unit> =
        ds.sendPasswordReset(email)

    fun logout() = ds.signOut()
    fun currentUser(): User? = ds.currentUser()?.let { User(it.uid, it.email) }
}