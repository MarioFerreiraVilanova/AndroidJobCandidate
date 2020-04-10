package app.storytel.candidate.com.network

import app.storytel.candidate.com.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {

    val sampleApi: SampleApi by lazy {
        initializeSampleApi()
    }

    private fun initializeSampleApi (): SampleApi{
        val client = OkHttpClient()
                .newBuilder()
                .build()

        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.SAMPLE_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(SampleApi::class.java)
    }
}