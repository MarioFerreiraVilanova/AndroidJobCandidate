package app.storytel.candidate.com.network

import app.storytel.candidate.com.network.models.Comment
import app.storytel.candidate.com.network.models.Photo
import app.storytel.candidate.com.network.models.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SampleApi {

    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("photos")
    fun getPhotos(): Call<List<Photo>>

    @GET ("posts/{id}/comments")
    fun getComments(@Path("id") id: Int): Call<List<Comment>>
}