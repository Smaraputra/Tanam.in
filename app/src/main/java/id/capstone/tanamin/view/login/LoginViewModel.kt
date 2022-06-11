package id.capstone.tanamin.view.login

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class LoginViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    fun loginUser(loginMap: HashMap<String, String>) = tanaminRepository.loginUser(loginMap)
}