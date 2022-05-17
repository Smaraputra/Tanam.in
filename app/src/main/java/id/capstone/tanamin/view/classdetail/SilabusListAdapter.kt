package id.capstone.tanamin.view.classdetail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.capstone.tanamin.databinding.ViewholderClassDetailSilabusBinding
import id.capstone.tanamin.view.classmodule.ClassModuleActivity

class SilabusListAdapter(private val ctx: Context) : RecyclerView.Adapter<SilabusListAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ViewholderClassDetailSilabusBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ClassModuleActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int = 5

    class ListViewHolder(binding: ViewholderClassDetailSilabusBinding) : RecyclerView.ViewHolder(binding.root)
}