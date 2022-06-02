package id.capstone.tanamin.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.capstone.tanamin.data.TanaminRepository
import id.capstone.tanamin.data.di.Injection
import id.capstone.tanamin.view.classdetail.forum.ForumViewModel
import id.capstone.tanamin.view.classdetail.silabus.SilabusViewModel
import id.capstone.tanamin.view.classes.ClassesViewModel
import id.capstone.tanamin.view.classmodule.ClassModuleViewModel
import id.capstone.tanamin.view.forumcreate.ForumCreateViewModel
import id.capstone.tanamin.view.forumresult.ForumResultViewModel
import id.capstone.tanamin.view.home.HomeViewModel
import id.capstone.tanamin.view.login.LoginViewModel
import id.capstone.tanamin.view.profile.ProfileViewModel
import id.capstone.tanamin.view.profileedit.ProfileEditViewModel
import id.capstone.tanamin.view.register.RegisterViewModel

class ViewModelFactory private constructor(private val tanaminRepository: TanaminRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(tanaminRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                return RegisterViewModel(tanaminRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(tanaminRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                return ProfileViewModel(tanaminRepository) as T
            }
            modelClass.isAssignableFrom(ClassesViewModel::class.java) -> {
                return ClassesViewModel(tanaminRepository) as T
            }
            modelClass.isAssignableFrom(ProfileEditViewModel::class.java) -> {
                return ProfileEditViewModel(tanaminRepository) as T
            }
            modelClass.isAssignableFrom(SilabusViewModel::class.java) -> {
                return SilabusViewModel(tanaminRepository) as T
            }
            modelClass.isAssignableFrom(ForumViewModel::class.java) -> {
                return ForumViewModel(tanaminRepository) as T
            }
            modelClass.isAssignableFrom(ForumCreateViewModel::class.java) -> {
                return ForumCreateViewModel(tanaminRepository) as T
            }
            modelClass.isAssignableFrom(ForumResultViewModel::class.java) -> {
                return ForumResultViewModel(tanaminRepository) as T
            }
            modelClass.isAssignableFrom(ClassModuleViewModel::class.java)->{
                return ClassModuleViewModel(tanaminRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
 
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, token: String): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context, token))
            }.also { instance = it }
    }
}