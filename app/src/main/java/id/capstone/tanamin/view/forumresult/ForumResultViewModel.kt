package id.capstone.tanamin.view.forumresult

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class ForumResultViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    fun getAllMessage(forumId: String) = tanaminRepository.getAllMessage(forumId)
    fun sendMessage(messageData: HashMap<String, String>) = tanaminRepository.sendMessage(messageData)
}