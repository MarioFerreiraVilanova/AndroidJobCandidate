package app.storytel.candidate.com.network.models;

data class Comment (
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String?,
    val body: String
)
