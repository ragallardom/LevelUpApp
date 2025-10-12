package cl.duoc.levelupapp.repository.auth

import cl.duoc.levelupapp.model.User
import cl.duoc.levelupapp.repository.auth.FirebaseAuthDataSource

class AuthRepository(
    private val ds: FirebaseAuthDataSource = FirebaseAuthDataSource()
) {
    suspend fun login(email: String, pass: String): User? {
        val fu = ds.signIn(email, pass) ?: return null
        return User(uid = fu.uid, email = fu.email)
    }

    suspend fun signUp(email: String, pass: String): User? {
        val fu = ds.signUp(email, pass) ?: return null
        return User(uid = fu.uid, email = fu.email)
    }

    suspend fun sendPasswordReset(email: String): Boolean {
        return ds.sendPasswordReset(email)
    }

    fun logout() = ds.signOut()
    fun currentUser(): User? = ds.currentUser()?.let { User(it.uid, it.email) }
}