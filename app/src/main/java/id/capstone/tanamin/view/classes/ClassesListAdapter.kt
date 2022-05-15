package id.capstone.tanamin.view.classes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.capstone.tanamin.databinding.RvClassBinding
import id.capstone.tanamin.view.classdetail.ClassDetailActivity

class ClassesListAdapter(private val ctx: Context) : RecyclerView.Adapter<ClassesListAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = RvClassBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ClassDetailActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int = 5

    class ListViewHolder(binding: RvClassBinding) : RecyclerView.ViewHolder(binding.root)
}