package app.storytel.candidate.com

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.storytel.candidate.com.network.ApiFactory
import app.storytel.candidate.com.network.models.Comment
import app.storytel.candidate.com.network.models.NetworkResource
import app.storytel.candidate.com.network.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class DetailsActivityViewModel: ViewModel() {

    private val api = ApiFactory.getSampleApi()

    var postId: Int? = null
    var postTitle: String? = null
    var postBody: String? = null
    var photoUrl: String? = null
    private var initialized = false

    private val comments = MutableLiveData<NetworkResource<List<Comment>>>().apply {
        value = NetworkResource(false, null)
    }

    fun init (intent: Intent){
        if (initialized) return

        if (intent.hasExtra("postId")){
            postId = intent.getIntExtra("postId", 0)
        }

        if (intent.hasExtra("postTitle")){
            postTitle = intent.getStringExtra("postTitle")
        }

        if (intent.hasExtra("postBody")){
            postBody = intent.getStringExtra("postBody")
        }

        if (intent.hasExtra("photoUrl")){
            photoUrl = intent.getStringExtra("photoUrl")
        }

        postId?.let {
            fetchComments(it)
        }

        initialized = true
    }

    fun getComments(): LiveData<NetworkResource<List<Comment>>> {
        return comments
    }

    fun fetchComments (postId: Int){
        comments.postValue(NetworkResource(true, null))
        api.getComments(postId).enqueue(object : Callback<List<Comment>>{
            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                comments.postValue(NetworkResource(false, Result.Failure(t)))
            }

            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful){
                    comments.postValue((NetworkResource(false, Result.Success(response.body() ?: ArrayList()))))
                }else{
                    comments.postValue((NetworkResource(false, Result.Failure(HttpException(response)))))
                }
            }

        })
    }
}