package id.capstone.tanamin.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.capstone.tanamin.data.TanaminRepository
import id.capstone.tanamin.data.di.Injection
import id.capstone.tanamin.view.home.HomeViewModel
import id.capstone.tanamin.view.login.LoginViewModel
import id.capstone.tanamin.view.profile.ProfileViewModel
import id.capstone.tanamin.view.register.RegisterViewModel

class ViewModelFactory private constructor(private val tanaminRepository: TanaminRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(tanaminRepository) as T
        } else if(modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(tanaminRepository) as T
        }else if ((modelClass.isAssignableFrom(HomeViewModel::class.java))){
            return HomeViewModel(tanaminRepository) as T
        }else if(modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            return ProfileViewModel(tanaminRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
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