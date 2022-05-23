package id.capstone.tanamin.view.register

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class RegisterViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    fun registerUser(map: HashMap<String, String>) = tanaminRepository.registerUser(map)
}