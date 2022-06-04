package id.capstone.tanamin.view.quiz

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import id.capstone.tanamin.data.remote.response.SoalData
import id.capstone.tanamin.databinding.RvQuizBinding

class QuizQuestionAdapter(private val ctx: Context, private val quizModule: MutableList<SoalData>) : RecyclerView.Adapter<QuizQuestionAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = RvQuizBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = quizModule[position]
        holder.binding.questionQuiz.text = data.soal
        holder.binding.numberQuiz.text = (position+1).toString()
        holder.binding.radioButtonA.text = data.pilihan.pilihan1
        holder.binding.radioButtonB.text = data.pilihan.pilihan2
        holder.binding.radioButtonC.text = data.pilihan.pilihan3
        holder.binding.radioGrup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            onItemClickCallback.onItemClicked(radio.text.toString(),position)
        }
    }

    class ListViewHolder(val binding: RvQuizBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = quizModule.size

    interface OnItemClickCallback {
        fun onItemClicked(data: String, position: Int)
    }
}