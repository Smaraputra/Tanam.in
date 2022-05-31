package id.capstone.tanamin.view.forumcreate

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class ForumCreateViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    fun createForum(classMap: HashMap<String, String>) = tanaminRepository.createForum(classMap)
}