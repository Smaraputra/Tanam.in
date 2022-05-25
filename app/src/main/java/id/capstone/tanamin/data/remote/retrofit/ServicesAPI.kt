package id.capstone.tanamin.data.remote.retrofit

import id.capstone.tanamin.data.remote.response.*
import retrofit2.Call
import retrofit2.http.*

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

    @FormUrlEncoded
    @POST("home")
    fun getHomeData(
        @FieldMap hashMap: Map<String, String>
    ):Call<HomeResponse>

    @FormUrlEncoded
    @POST("profile")
    fun getProfileUser(
        @FieldMap hashMap: Map<String, String>
    ):Call<ProfileResponse>

    @GET("class/{userid}")
    fun getAllClass(
        @Path("userid") userid: String
    ): Call<AllClassesResponse>
}