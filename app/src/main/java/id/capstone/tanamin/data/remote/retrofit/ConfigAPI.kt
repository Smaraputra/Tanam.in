package id.capstone.tanamin.data.remote.retrofit

import id.capstone.tanamin.BuildConfig
import id.capstone.tanamin.BuildConfig.BASE_URL_TANAMIN
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigAPI {
    companion object {
        private var urlBase: String = BASE_URL_TANAMIN
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
                        requestBuilder.header("Authorization", "Bearer $token")
                        chain.proceed(requestBuilder.build())
                    }
                    .build()
            }else{
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(urlBase)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ServicesAPI::class.java)
        }
    }
}