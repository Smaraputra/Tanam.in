package id.capstone.tanamin.view.profileedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileEditViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    private val _filePhoto = MutableLiveData<File>()
    val filePhoto: LiveData<File> = _filePhoto

    fun editProfile(profilePictureMultipart: MultipartBody.Part, userid: RequestBody, name: RequestBody,age:RequestBody, address: RequestBody) = tanaminRepository.editProfile(profilePictureMultipart,name,age,address,userid)

    fun setFilePhoto(filePhoto: File){
        _filePhoto.value=filePhoto
    }
}