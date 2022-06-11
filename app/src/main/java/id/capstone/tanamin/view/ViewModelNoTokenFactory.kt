package id.capstone.tanamin.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.capstone.tanamin.data.TanaminRepository
import id.capstone.tanamin.data.di.Injection
import id.capstone.tanamin.view.login.LoginViewModel
import id.capstone.tanamin.view.register.RegisterViewModel

class ViewModelNoTokenFactory private constructor(private val tanaminRepository: TanaminRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(tanaminRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(tanaminRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
 
    companion object {
        @Volatile
        private var instance: ViewModelNoTokenFactory? = null
        fun getInstance(context: Context): ViewModelNoTokenFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelNoTokenFactory(Injection.provideRepository(context, ""))
            }.also { instance = it }
    }
}