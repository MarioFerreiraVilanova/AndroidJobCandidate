package app.storytel.candidate.com

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.storytel.candidate.com.network.ApiFactory
import app.storytel.candidate.com.network.models.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception

class ScrollingActivityViewModel: ViewModel() {

    private val api = ApiFactory.getSampleApi()

    private val postsAndImages = MutableLiveData<NetworkResource<PostAndImages>>().apply {
        this.value = NetworkResource(false, null)
    }

    fun getPostsAndImages(): LiveData<NetworkResource<PostAndImages>>{
        postsAndImages.value?.result ?: run {
            loadData()
        }
        return postsAndImages
    }

    fun loadData(){
        GlobalScope.launch {
            try {
                postsAndImages.postValue(NetworkResource(true, null))
                when (val posts = fetchPosts()){
                    is Result.Success -> {
                        when (val images = fetchPhotos()){
                            is Result.Success -> postsAndImages.postValue(NetworkResource(false, Result.Success(PostAndImages(posts.data, images.data))))
                            is Result.Failure -> postsAndImages.postValue(NetworkResource(false, images))
                        }
                    }
                    is Result.Failure -> postsAndImages.postValue(NetworkResource(false, posts))
                }
            }catch (e: Exception){
                postsAndImages.postValue(NetworkResource(false, Result.Failure(e)))
            }

        }
    }

    private fun fetchPosts(): Result<List<Post>>{
        val response = api.getPosts().execute()
        return if (response.isSuccessful){
            Result.Success(response.body() ?: ArrayList())
        }else{
            Result.Failure(HttpException(response))
        }
    }

    private fun fetchPhotos(): Result<List<Photo>>{
        val response = api.getPhotos().execute()
        return if (response.isSuccessful){
            Result.Success(response.body() ?: ArrayList())
        }else{
            Result.Failure(HttpException(response))
        }
    }
}