package id.capstone.tanamin.data.remote.retrofit

import id.capstone.tanamin.data.remote.response.LoginResponse
import id.capstone.tanamin.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ServicesAPI {
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @FieldMap hashMap: Map<String, String>
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @FieldMap hashMap: Map<String, String>
    ): Call<LoginResponse>
}