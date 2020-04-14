package app.storytel.candidate.com.network.models

data class Post (
        val userId: Int,
        val id: Int,
        val title: String = "",
        val body: String = ""
)