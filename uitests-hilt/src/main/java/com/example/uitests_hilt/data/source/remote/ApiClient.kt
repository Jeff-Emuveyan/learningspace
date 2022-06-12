package com.example.uitests_hilt.data.source.remote
import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiClient {

    fun getClient(appContext: Context): Retrofit{

        val httpClient =  OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder().build()
            chain.proceed(request)
        }

        val  client = httpClient.build()
        return Retrofit.Builder()
            .baseUrl("http://connect-demo.mobile1.io/square1/connect/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}