package id.capstone.tanamin.view.classmodule

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class ClassModuleViewModel(private val tanaminRepository: TanaminRepository): ViewModel(){
    fun getDetailModule(moduleData:HashMap<String,String>)=tanaminRepository.getDetailModule(moduleData)
}