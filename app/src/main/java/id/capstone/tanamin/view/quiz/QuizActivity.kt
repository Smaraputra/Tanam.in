package id.capstone.tanamin.view.quiz

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.data.remote.response.QuizModule
import id.capstone.tanamin.data.remote.response.SoalData
import id.capstone.tanamin.databinding.ActivityQuizBinding
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.databinding.CustomAlertScoreBinding
import id.capstone.tanamin.view.ViewModelFactory
import id.capstone.tanamin.view.classfinished.ClassFinishedActivity

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private var modulId: Int = 0
    private var quizId: Int = 0
    private var classId: Int = 0
    private var classTitle: String=""
    private lateinit var quizQuestionAdapter: QuizQuestionAdapter
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var answerQuiz: MutableList<String>

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()
        modulId=intent.getIntExtra(ID_MODULE_EXTRA,0)
        classId=intent.getIntExtra(ID_CLASS_EXTRA,0)
        classTitle= intent.getStringExtra(CLASS_TITLE_EXTRA).toString()
        answerQuiz = mutableListOf("-","-","-","-","-")
        setupView()
        setupViewModel()
    }

    private fun setupView(){
        binding.ivBackButton.setOnClickListener{
            onBackPressed()
        }
        binding.tvClassTitle.text=classTitle
    }

    private fun setupViewModel(){
        val pref= LoginPreferences.getInstance(dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this, "")
        val quizViewModel: QuizViewModel by viewModels {
            factory
        }
        this.quizViewModel=quizViewModel
        binding.button5.setOnClickListener{
            getQuiz()
            binding.textView8.visibility = View.GONE
            binding.button5.visibility = View.GONE
            binding.button5.isClickable = false
            binding.textView7.visibility = View.VISIBLE
            binding.btnNext.visibility = View.VISIBLE
            binding.scrollView2.visibility = View.VISIBLE
        }
        binding.btnNext.setOnClickListener{
            sendAnswer()
        }
    }

    private fun getQuiz(){
        val moduleHashMap: HashMap<String, String> = HashMap()
        val liveDataPref=preferencesViewModel.getIDUser()
        liveDataPref.observe(this){ userId->
            moduleHashMap["classid"]=classId.toString()
            moduleHashMap["modulid"]=modulId.toString()
            moduleHashMap["userid"]=userId.toString()
            val liveDataDetailModule=quizViewModel.getQuiz(moduleHashMap)
            liveDataDetailModule.observe(this){ result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.loadingModule.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.loadingModule.visibility = View.GONE
                            setupAdapter(result.data.data.module)
                            quizId = result.data.data.quizid
                            timerStart()
                            liveDataPref.removeObservers(this)
                            liveDataDetailModule.removeObservers(this)
                        }
                        is Result.Error -> {
                            binding.loadingModule.visibility = View.GONE
                            ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                                ?.let { (showDialog(result.error, it)) }
                            liveDataPref.removeObservers(this)
                            liveDataDetailModule.removeObservers(this)
                        }
                    }
                }
            }
        }
    }

    private fun timerStart(){
        val timer = object: CountDownTimer(1000*60*10, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                val timeRemaining = "Sisa waktu: $minutes:$seconds"
                binding.textView7.text = timeRemaining
            }
            override fun onFinish() {
                sendAnswer()
            }
        }
        timer.start()
    }

    private fun sendAnswer(){
        val moduleHashMap: HashMap<String, String> = HashMap()
        val liveDataPref=preferencesViewModel.getIDUser()
        liveDataPref.observe(this){ userId->
            moduleHashMap["quizid"]=quizId.toString()
            moduleHashMap["userid"]=userId.toString()
            moduleHashMap["classid"]=classId.toString()
            moduleHashMap["moduleid"]=modulId.toString()
            val liveDataDetailModule=quizViewModel.sendAnswer(answerQuiz,moduleHashMap)
            liveDataDetailModule.observe(this){ result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.loadingModule.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.loadingModule.visibility = View.GONE
                            liveDataPref.removeObservers(this)
                            liveDataDetailModule.removeObservers(this)
                            if(result.data.data.score<60){
                                ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                                    ?.let { (showDialogScore(result.data.data.score.toString(), it, false)) }
                            }else{
                                ContextCompat.getDrawable(this, R.drawable.ic_baseline_check_circle_24)
                                    ?.let { (showDialogScore(result.data.data.score.toString(), it, true)) }
                            }
                        }
                        is Result.Error -> {
                            binding.loadingModule.visibility = View.GONE
                            ContextCompat.getDrawable(this, R.drawable.ic_baseline_error_24)
                                ?.let { (showDialog(result.error, it)) }
                            liveDataPref.removeObservers(this)
                            liveDataDetailModule.removeObservers(this)
                        }
                    }
                }
            }
        }
    }

    fun showDialog(text: String, icon: Drawable) {
        val builder = AlertDialog.Builder(this).create()
        val bindAlert: CustomAlertApiBinding = CustomAlertApiBinding.inflate(LayoutInflater.from(this))
        builder.setView(bindAlert.root)
        bindAlert.infoDialog.text = text
        bindAlert.imageView5.setImageDrawable(icon)
        bindAlert.closeButton.setOnClickListener {
            builder.dismiss()
            finish()
        }
        builder.show()
    }

    private fun showDialogScore(text: String, icon: Drawable, status: Boolean) {
        val builder = AlertDialog.Builder(this).create()
        val bindAlert: CustomAlertScoreBinding = CustomAlertScoreBinding.inflate(LayoutInflater.from(this))
        builder.setView(bindAlert.root)
        val nilai = "Nilai anda: $text/100"
        bindAlert.infoDialog.text = nilai
        bindAlert.imageView5.setImageDrawable(icon)
        if(status){
            bindAlert.score.text = getString(R.string.congratulation_quiz)
            bindAlert.closeButton.setOnClickListener {
                builder.dismiss()
                val intent = Intent(this@QuizActivity, ClassFinishedActivity::class.java)
                intent.putExtra(CLASS_FINISH, classTitle)
                finish()
                startActivity(intent)
            }
        }else{
            bindAlert.score.text = getString(R.string.not_pass_quiz)
            bindAlert.closeButton.setOnClickListener {
                builder.dismiss()
                finish()
            }
        }

        builder.show()
    }

    private fun setupAdapter(quiz: QuizModule){
        val questionList: MutableList<SoalData>?
        questionList = mutableListOf()
        questionList.add(quiz.soal1)
        questionList.add(quiz.soal2)
        questionList.add(quiz.soal3)
        questionList.add(quiz.soal4)
        questionList.add(quiz.soal5)
        binding.rvQuizQuestion.setHasFixedSize(true)
        binding.rvQuizQuestion.layoutManager = LinearLayoutManager(this)
        quizQuestionAdapter = QuizQuestionAdapter(this, questionList)
        binding.rvQuizQuestion.adapter = quizQuestionAdapter
        quizQuestionAdapter.setOnItemClickCallback(object : QuizQuestionAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String, position: Int) {
                answerQuiz[position] = data
            }
        })
    }

    companion object{
        const val CLASS_FINISH="class_finish"
        const val ID_MODULE_EXTRA="id_modul"
        const val ID_CLASS_EXTRA="id_class"
        const val CLASS_TITLE_EXTRA="class_title"
    }
}