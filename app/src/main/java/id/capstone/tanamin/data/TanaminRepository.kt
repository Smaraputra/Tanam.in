package id.capstone.tanamin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import id.capstone.tanamin.data.local.database.Classes
import id.capstone.tanamin.data.local.database.TanaminRoomDatabase
import id.capstone.tanamin.data.remote.response.*
import id.capstone.tanamin.data.remote.retrofit.ServicesAPI
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TanaminRepository(
    private val tanaminRoomDatabase: TanaminRoomDatabase,
    private val apiService: ServicesAPI
    ) {
    private val resultLogin = MediatorLiveData<Result<LoginResponse>>()
    private val resultRegister = MediatorLiveData<Result<RegisterResponse>>()
    private val resultHome=MediatorLiveData<Result<HomeResponse>>()
    private val resultProfile=MediatorLiveData<Result<ProfileResponse>>()
    private val resultAllClasses=MediatorLiveData<Result<AllClassesResponse>>()

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

    fun getAllClass(profileMap: String): LiveData<Result<AllClassesResponse>>{
        resultAllClasses.value = Result.Loading
        val client = apiService.getAllClass(profileMap)
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
                                it.modul_id
                            )
                            classesList.add(news)
                        tanaminRoomDatabase.tanaminDao().deleteAllClasses()
                        tanaminRoomDatabase.tanaminDao().insertClasses(classesList)
                        }
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
}