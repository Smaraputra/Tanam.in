package id.capstone.tanamin.data.remote.retrofit

import id.capstone.tanamin.BuildConfig
import id.capstone.tanamin.BuildConfig.BASE_URL_DETECTION_TANAMIN
import id.capstone.tanamin.BuildConfig.BASE_URL_TANAMIN
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ConfigAPI {
    companion object {
        private var urlBase: String = BASE_URL_TANAMIN
        private var urlDetectionBase: String = BASE_URL_DETECTION_TANAMIN
        fun getApiService(token: String): ServicesAPI {
            lateinit var client: OkHttpClient
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            client = if(token.isNotBlank()){
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor).addNetworkInterceptor { chain ->
                        val requestBuilder = chain.request().newBuilder()
                        requestBuilder.header("key", token)
                        chain.proceed(requestBuilder.build())
                    }
                    .callTimeout(15, TimeUnit.SECONDS)
                    .build()
            }else{
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .callTimeout(15, TimeUnit.SECONDS)
                    .build()
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ServicesAPI::class.java)
        }

        fun getApiDetectionService(token: String): ServicesAPI {
            lateinit var client: OkHttpClient
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            client = if(token.isNotBlank()){
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor).addNetworkInterceptor { chain ->
                        val requestBuilder = chain.request().newBuilder()
                        requestBuilder.header("key", token)
                        chain.proceed(requestBuilder.build())
                    }
                    .callTimeout(15, TimeUnit.SECONDS)
                    .build()
            }else{
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .callTimeout(15, TimeUnit.SECONDS)
                    .build()
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(urlDetectionBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ServicesAPI::class.java)
        }
    }
}