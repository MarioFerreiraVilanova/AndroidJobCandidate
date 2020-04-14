package app.storytel.candidate.com.network.models

data class NetworkResource<out T: Any> (
        val loading: Boolean,
        val result: Result<T>?
)