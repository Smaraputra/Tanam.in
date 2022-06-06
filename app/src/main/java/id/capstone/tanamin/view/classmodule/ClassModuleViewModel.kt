package id.capstone.tanamin.view.classmodule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ClassModuleViewModel(private val tanaminRepository: TanaminRepository): ViewModel(){

    private val _isProgressPictureUploaded = MutableLiveData<Boolean>()
    val isProgressPictureUploaded: LiveData<Boolean> = _isProgressPictureUploaded

    fun uploadProgress(progressPictureMultipart: MultipartBody.Part, userId: RequestBody,classId:RequestBody)=tanaminRepository.uploadProgress(progressPictureMultipart,userId,classId)
    fun getDetailModule(moduleData:HashMap<String,String>)=tanaminRepository.getDetailModule(moduleData)

    fun setProgressPictureUploadedStatus(boolean: Boolean){
        _isProgressPictureUploaded.value=boolean
    }
}