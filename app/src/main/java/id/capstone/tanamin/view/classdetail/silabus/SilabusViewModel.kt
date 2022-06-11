package id.capstone.tanamin.view.classdetail.silabus

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class SilabusViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    fun getAllModule(classMap: String) = tanaminRepository.getAllModule(classMap)
}