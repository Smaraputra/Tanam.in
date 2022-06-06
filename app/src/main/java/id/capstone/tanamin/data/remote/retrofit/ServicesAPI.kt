package id.capstone.tanamin.data.remote.retrofit

import id.capstone.tanamin.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @FormUrlEncoded
    @POST("class")
    fun getAllClass(
        @FieldMap hashMap: Map<String, String>
    ):Call<AllClassesResponse>

    @GET("module/{classId}")
    fun getAllModule(
        @Path("classId") classId: String
    ): Call<ListModulesResponse>

    @GET("module/{classId}/forum")
    fun getAllForum(
        @Path("classId") classId: String
    ): Call<ListForumResponse>

    @Multipart
    @POST("editProfile")
    fun editProfile(
        @Part profile_picture: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("age") age: RequestBody,
        @Part("address") address: RequestBody,
        @Part("userid") userid: RequestBody
    ): Call<EditProfileResponse>

    @Multipart
    @POST("editProfile")
    fun editProfileWithoutPhoto(
        @Part("name") name: RequestBody,
        @Part("age") age: RequestBody,
        @Part("address") address: RequestBody,
        @Part("userid") userid: RequestBody
    ): Call<EditProfileResponse>

    @FormUrlEncoded
    @POST("createForum")
    fun createForum(
        @FieldMap hashMap: Map<String, String>
    ):Call<CreateForumResponse>

    @GET("ForumMassage/{forumId}")
    fun getAllMessage(
        @Path("forumId") forumId: String
    ): Call<AllMessageResponse>

    @FormUrlEncoded
    @POST("sendMassage")
    fun sendMessage(
        @FieldMap hashMap: Map<String, String>
    ):Call<SendMessageResponse>

    @FormUrlEncoded
    @POST("module")
    fun getDetailModule(
        @FieldMap hashMap: Map<String, String>
    ):Call<DetailModuleResponse>

    @FormUrlEncoded
    @POST("module")
    fun getQuizModule(
        @FieldMap hashMap: Map<String, String>
    ):Call<QuizResponse>

    @FormUrlEncoded
    @POST("quizCheck")
    fun sendAnswer(
        @Field("answer") answer: List<String>,
        @FieldMap hashMap: Map<String, String>
    ):Call<QuizAnswerResponse>

    @Multipart
    @POST("classProgress")
    fun uploadProgress(
        @Part picture: MultipartBody.Part,
        @Part("userid") userId: RequestBody,
        @Part("classid") classId: RequestBody
    ): Call<UploadProgressResponse>

    @Multipart
    @POST("predict")
    fun detectImage(
        @Part file: MultipartBody.Part
    ):Call<DetectionResponse>

    @GET("deteksi/{id}/informations")
    fun getResultDetected(
        @Path("id") id: String
    ): Call<DetectionInformationResponse>
}