package cl.duoc.levelupapp.repository.auth

import cl.duoc.levelupapp.model.User

class AuthRepository(
    private val ds: FirebaseAuthDataSource = FirebaseAuthDataSource()
) {
    suspend fun login(email: String, pass: String): Result<User> =
        runCatching {
            val fu = ds.signIn(email, pass)
            User(uid = fu.uid, email = fu.email)
        }

    suspend fun signUp(email: String, pass: String): Result<User> =
        runCatching {
            val fu = ds.signUp(email, pass)
            User(uid = fu.uid, email = fu.email)
        }

    suspend fun sendPasswordReset(email: String): Boolean {
        return ds.sendPasswordReset(email)
    }

    fun logout() = ds.signOut()
    fun currentUser(): User? = ds.currentUser()?.let { User(it.uid, it.email) }
}
