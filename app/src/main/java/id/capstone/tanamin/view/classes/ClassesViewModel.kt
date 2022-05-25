package id.capstone.tanamin.view.classes

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class ClassesViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    fun getAllClass(classMap: String) = tanaminRepository.getAllClass(classMap)
    fun searchWord(word: String) = tanaminRepository.getSearchClass(word)
}