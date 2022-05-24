package id.capstone.tanamin.view.home

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class HomeViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    fun getHomeData(homeMap: HashMap<String, String>) = tanaminRepository.getHomeData(homeMap)
}