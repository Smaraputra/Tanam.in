package id.capstone.tanamin.view.detection

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository
import okhttp3.MultipartBody

class DetectionViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    fun detectImage(profilePictureMultipart: MultipartBody.Part) = tanaminRepository.detectImage(profilePictureMultipart)
}