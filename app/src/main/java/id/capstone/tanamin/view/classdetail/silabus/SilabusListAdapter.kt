package id.capstone.tanamin.view.classdetail.silabus

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.capstone.tanamin.data.remote.response.ListmodulItem
import id.capstone.tanamin.databinding.ViewholderClassDetailSilabusBinding

class SilabusListAdapter(private val ctx: Context, private val listModule: List<ListmodulItem>) : RecyclerView.Adapter<SilabusListAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ViewholderClassDetailSilabusBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listModule[position]
        holder.binding.tvSilabusTitle.text = data.title
    }

    override fun getItemCount(): Int = listModule.size

    class ListViewHolder(val binding: ViewholderClassDetailSilabusBinding) : RecyclerView.ViewHolder(binding.root)
}