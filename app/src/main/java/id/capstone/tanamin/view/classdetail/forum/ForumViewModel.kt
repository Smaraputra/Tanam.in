package id.capstone.tanamin.view.classdetail.forum

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class ForumViewModel(private val tanaminRepository: TanaminRepository): ViewModel() {
    fun getAllForum(classMap: String) = tanaminRepository.getAllForum(classMap)
}