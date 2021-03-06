package id.capstone.tanamin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import id.capstone.tanamin.data.local.database.Classes
import id.capstone.tanamin.data.local.database.TanaminRoomDatabase
import id.capstone.tanamin.data.remote.response.*
import id.capstone.tanamin.data.remote.retrofit.ServicesAPI
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TanaminRepository(
    private val tanaminRoomDatabase: TanaminRoomDatabase,
    private val apiService: ServicesAPI,
    private val detectionService: ServicesAPI
    ) {
    private val resultLogin = MediatorLiveData<Result<LoginResponse>>()
    private val resultRegister = MediatorLiveData<Result<RegisterResponse>>()
    private val resultHome=MediatorLiveData<Result<HomeResponse>>()
    private val resultProfile=MediatorLiveData<Result<ProfileResponse>>()
    private val resultAllClasses=MediatorLiveData<Result<AllClassesResponse>>()
    private val resultEditProfile=MediatorLiveData<Result<EditProfileResponse>>()
    private val resultListModule=MediatorLiveData<Result<ListModulesResponse>>()
    private val resultListForum=MediatorLiveData<Result<ListForumResponse>>()
    private val resultCreateForum=MediatorLiveData<Result<CreateForumResponse>>()
    private val resultGetMessage=MediatorLiveData<Result<AllMessageResponse>>()
    private val resultSendMessage=MediatorLiveData<Result<SendMessageResponse>>()
    private val resultDetailModule=MediatorLiveData<Result<DetailModuleResponse>>()
    private val resultQuizModule=MediatorLiveData<Result<QuizResponse>>()
    private val resultQuizAnswer=MediatorLiveData<Result<QuizAnswerResponse>>()
    private val resultUploadProgress=MediatorLiveData<Result<UploadProgressResponse>>()
    private val resultDetection=MediatorLiveData<Result<DetectionResponse>>()
    private val resultDetectionResult=MediatorLiveData<Result<DetectionInformationResponse>>()

    fun registerUser(registerMap: HashMap<String, String>): LiveData<Result<RegisterResponse>> {
        resultRegister.value = Result.Loading
        val client = apiService.registerUser(registerMap)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if(response.isSuccessful){
                    resultRegister.value = Result.Success(response.body() as RegisterResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultRegister.value = Result.Error(jsonObject.getString("message"))
                    }catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                resultRegister.value = Result.Error(t.message.toString())
            }
        })
        return resultRegister
    }

    fun loginUser(loginMap: HashMap<String, String>) : LiveData<Result<LoginResponse>> {
        resultLogin.value = Result.Loading
        val client = apiService.loginUser(loginMap)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    resultLogin.value = Result.Success(response.body() as LoginResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultLogin.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                resultLogin.value = Result.Error(t.message.toString())
            }
        })
        return resultLogin
    }

    fun getHomeData(homeMap:HashMap<String,String>): LiveData<Result<HomeResponse>>{
        resultHome.value = Result.Loading
        val client = apiService.getHomeData(homeMap)
        client.enqueue(object : Callback<HomeResponse> {
            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                if(response.isSuccessful){
                    resultHome.value = Result.Success(response.body() as HomeResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultHome.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                resultHome.value = Result.Error(t.message.toString())
            }
        })
        return resultHome
    }

    fun getProfileUser(profileMap:HashMap<String,String>): LiveData<Result<ProfileResponse>>{
        resultProfile.value = Result.Loading
        val client = apiService.getProfileUser(profileMap)
        client.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if(response.isSuccessful){
                    resultProfile.value = Result.Success(response.body() as ProfileResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultProfile.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                resultProfile.value = Result.Error(t.message.toString())
            }
        })
        return resultProfile
    }

    fun getAllClass(classMap: HashMap<String,String>): LiveData<Result<AllClassesResponse>>{
        resultAllClasses.value = Result.Loading
        val client = apiService.getAllClass(classMap)
        client.enqueue(object : Callback<AllClassesResponse> {
            override fun onResponse(call: Call<AllClassesResponse>, response: Response<AllClassesResponse>) {
                if(response.isSuccessful){
                    val classes = response.body()?.data?.jsonMemberClass
                    val classesList = ArrayList<Classes>()
                    runBlocking {
                        classes?.forEach { it ->
                            val news = Classes(
                                it.id_class,
                                it.title,
                                it.detail,
                                it.picture,
                                it.total_module,
                                it.progress,
                                it.modul_title,
                                it.lastest_module
                            )
                            classesList.add(news)
                        }
                        tanaminRoomDatabase.tanaminDao().deleteAllClasses()
                        tanaminRoomDatabase.tanaminDao().insertClasses(classesList)
                    }
                    resultAllClasses.value = Result.Success(response.body() as AllClassesResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultAllClasses.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<AllClassesResponse>, t: Throwable) {
                resultAllClasses.value = Result.Error(t.message.toString())
            }
        })
        return resultAllClasses
    }

    fun getSearchClass(word: String): LiveData<List<Classes>>{
        return tanaminRoomDatabase.tanaminDao().searchClasses(word)
    }

    fun editProfile(profilePictureMultipart: MultipartBody.Part?, name: RequestBody, age:RequestBody, address: RequestBody, userid: RequestBody):LiveData<Result<EditProfileResponse>>{
        resultEditProfile.value = Result.Loading
        val client = if(profilePictureMultipart!=null) apiService.editProfile(profilePictureMultipart,name,age,address,userid) else apiService.editProfileWithoutPhoto(name,age,address,userid)
        client.enqueue(object : Callback<EditProfileResponse> {
            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>
            ) {
                if (response.isSuccessful) {
                    resultEditProfile.value = Result.Success(response.body() as EditProfileResponse)
                } else {
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultEditProfile.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                resultEditProfile.value = Result.Error(t.message.toString())
            }
        })
        return resultEditProfile
    }


    fun getAllModule(classId : String): LiveData<Result<ListModulesResponse>>{
        resultListModule.value = Result.Loading
        val client = apiService.getAllModule(classId)
        client.enqueue(object : Callback<ListModulesResponse> {
            override fun onResponse(call: Call<ListModulesResponse>, response: Response<ListModulesResponse>) {
                if(response.isSuccessful){
                    resultListModule.value = Result.Success(response.body() as ListModulesResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultListModule.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ListModulesResponse>, t: Throwable) {
                resultListModule.value = Result.Error(t.message.toString())
            }
        })
        return resultListModule
    }

    fun getAllForum(classId : String): LiveData<Result<ListForumResponse>>{
        resultListForum.value = Result.Loading
        val client = apiService.getAllForum(classId)
        client.enqueue(object : Callback<ListForumResponse> {
            override fun onResponse(call: Call<ListForumResponse>, response: Response<ListForumResponse>) {
                if(response.isSuccessful){
                    resultListForum.value = Result.Success(response.body() as ListForumResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultListForum.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ListForumResponse>, t: Throwable) {
                resultListForum.value = Result.Error(t.message.toString())
            }
        })
        return resultListForum
    }

    fun createForum(loginMap: HashMap<String, String>) : LiveData<Result<CreateForumResponse>> {
        resultCreateForum.value = Result.Loading
        val client = apiService.createForum(loginMap)
        client.enqueue(object : Callback<CreateForumResponse> {
            override fun onResponse(call: Call<CreateForumResponse>, response: Response<CreateForumResponse>) {
                if(response.isSuccessful){
                    resultCreateForum.value = Result.Success(response.body() as CreateForumResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultCreateForum.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<CreateForumResponse>, t: Throwable) {
                resultCreateForum.value = Result.Error(t.message.toString())
            }
        })
        return resultCreateForum
    }

    fun getAllMessage(forumId: String) : LiveData<Result<AllMessageResponse>> {
        resultGetMessage.value = Result.Loading
        val client = apiService.getAllMessage(forumId)
        client.enqueue(object : Callback<AllMessageResponse> {
            override fun onResponse(call: Call<AllMessageResponse>, response: Response<AllMessageResponse>) {
                if(response.isSuccessful){
                    resultGetMessage.value = Result.Success(response.body() as AllMessageResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultGetMessage.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<AllMessageResponse>, t: Throwable) {
                resultGetMessage.value = Result.Error(t.message.toString())
            }
        })
        return resultGetMessage
    }

    fun sendMessage(messageData: HashMap<String, String>) : LiveData<Result<SendMessageResponse>> {
        resultSendMessage.value = Result.Loading
        val client = apiService.sendMessage(messageData)
        client.enqueue(object : Callback<SendMessageResponse> {
            override fun onResponse(call: Call<SendMessageResponse>, response: Response<SendMessageResponse>) {
                if(response.isSuccessful){
                    resultSendMessage.value = Result.Success(response.body() as SendMessageResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultSendMessage.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<SendMessageResponse>, t: Throwable) {
                resultSendMessage.value = Result.Error(t.message.toString())
            }
        })
        return resultSendMessage
    }

    fun getDetailModule(moduleData:HashMap<String,String>):LiveData<Result<DetailModuleResponse>>{
        resultDetailModule.value = Result.Loading
        val client = apiService.getDetailModule(moduleData)
        client.enqueue(object : Callback<DetailModuleResponse> {
            override fun onResponse(call: Call<DetailModuleResponse>, response: Response<DetailModuleResponse>) {
                if(response.isSuccessful){
                    resultDetailModule.value = Result.Success(response.body() as DetailModuleResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultDetailModule.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<DetailModuleResponse>, t: Throwable) {
                resultDetailModule.value = Result.Error(t.message.toString())
            }
        })
        return resultDetailModule
    }

    fun getQuiz(moduleData:HashMap<String,String>):LiveData<Result<QuizResponse>>{
        resultQuizModule.value = Result.Loading
        val client = apiService.getQuizModule(moduleData)
        client.enqueue(object : Callback<QuizResponse> {
            override fun onResponse(call: Call<QuizResponse>, response: Response<QuizResponse>) {
                if(response.isSuccessful){
                    resultQuizModule.value = Result.Success(response.body() as QuizResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultQuizModule.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<QuizResponse>, t: Throwable) {
                resultQuizModule.value = Result.Error(t.message.toString())
            }
        })
        return resultQuizModule
    }

    fun sendAnswer(listAnswer: List<String>, moduleData:HashMap<String,String>):LiveData<Result<QuizAnswerResponse>>{
        resultQuizAnswer.value = Result.Loading
        val client = apiService.sendAnswer(listAnswer,moduleData)
        client.enqueue(object : Callback<QuizAnswerResponse> {
            override fun onResponse(call: Call<QuizAnswerResponse>, response: Response<QuizAnswerResponse>) {
                if(response.isSuccessful){
                    resultQuizAnswer.value = Result.Success(response.body() as QuizAnswerResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultQuizAnswer.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<QuizAnswerResponse>, t: Throwable) {
                resultQuizAnswer.value = Result.Error(t.message.toString())
            }
        })
        return resultQuizAnswer
    }

    fun uploadProgress(progressPictureMultipart: MultipartBody.Part, userId: RequestBody, classId: RequestBody):LiveData<Result<UploadProgressResponse>>{
        resultUploadProgress.value = Result.Loading
        val client = apiService.uploadProgress(progressPictureMultipart,userId,classId)
        client.enqueue(object : Callback<UploadProgressResponse> {
            override fun onResponse(
                call: Call<UploadProgressResponse>,
                response: Response<UploadProgressResponse>
            ) {
                if (response.isSuccessful) {
                    resultUploadProgress.value = Result.Success(response.body() as UploadProgressResponse)
                } else {
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultUploadProgress.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<UploadProgressResponse>, t: Throwable) {
                resultUploadProgress.value = Result.Error(t.message.toString())
            }
        })
        return resultUploadProgress
    }

    fun detectImage(file: MultipartBody.Part): LiveData<Result<DetectionResponse>>{
        resultDetection.value = Result.Loading
        val client = detectionService.detectImage(file)
        client.enqueue(object : Callback<DetectionResponse> {
            override fun onResponse(call: Call<DetectionResponse>, response: Response<DetectionResponse>) {
                if(response.isSuccessful){
                    resultDetection.value = Result.Success(response.body() as DetectionResponse)
                }else{
                    resultDetection.value = Result.Error("Tidak dapat mendeteksi. Silahkan ulangi lagi.")
                }
            }

            override fun onFailure(call: Call<DetectionResponse>, t: Throwable) {
                resultDetection.value = Result.Error(t.message.toString())
            }
        })
        return resultDetection
    }

    fun getResultDetected(informationId: String): LiveData<Result<DetectionInformationResponse>>{
        resultDetectionResult.value = Result.Loading
        val client = apiService.getResultDetected(informationId)
        client.enqueue(object : Callback<DetectionInformationResponse> {
            override fun onResponse(call: Call<DetectionInformationResponse>, response: Response<DetectionInformationResponse>) {
                if(response.isSuccessful){
                    resultDetectionResult.value = Result.Success(response.body() as DetectionInformationResponse)
                }else{
                    lateinit var jsonObject: JSONObject
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        resultDetectionResult.value = Result.Error(jsonObject.getString("message"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<DetectionInformationResponse>, t: Throwable) {
                resultDetectionResult.value = Result.Error(t.message.toString())
            }
        })
        return resultDetectionResult
    }
}