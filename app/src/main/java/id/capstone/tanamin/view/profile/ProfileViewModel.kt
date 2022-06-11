package id.capstone.tanamin.view.profile

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class ProfileViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    fun getProfileUser(profileMap: HashMap<String, String>) = tanaminRepository.getProfileUser(profileMap)
}