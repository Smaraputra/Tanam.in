package id.capstone.tanamin.view.quiz

import androidx.lifecycle.ViewModel
import id.capstone.tanamin.data.TanaminRepository

class QuizViewModel(private val tanaminRepository: TanaminRepository): ViewModel(){
    fun getQuiz(moduleData:HashMap<String,String>)=tanaminRepository.getQuiz(moduleData)
    fun sendAnswer(listAnswer: List<String>, moduleData:HashMap<String,String>)=tanaminRepository.sendAnswer(listAnswer, moduleData)
}