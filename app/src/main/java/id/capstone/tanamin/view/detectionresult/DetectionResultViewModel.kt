package id.capstone.tanamin.view.detectionresult

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class DetectionResultViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    fun getResultDetected(informationId: String) = tanaminRepository.getResultDetected(informationId)
}