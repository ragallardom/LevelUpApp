package cl.duoc.levelupapp.model

data class UserResponse(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String
)