package app.storytel.candidate.com.network

import app.storytel.candidate.com.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private val client: OkHttpClient  = OkHttpClient()
            .newBuilder()
            .build()

    private val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.SAMPLE_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getSampleApi (): SampleApi{
        return retrofit.create(SampleApi::class.java)
    }
}