package id.capstone.tanamin.view.classdetail.forum

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.capstone.tanamin.data.remote.response.ListForumItem
import id.capstone.tanamin.databinding.ViewholderClassDetailForumBinding

class ForumListAdapter(private val ctx: Context, private val listModule: List<ListForumItem>) : RecyclerView.Adapter<ForumListAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ViewholderClassDetailForumBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listModule[position]
        holder.binding.tvForumTitle.text = data.title
    }

    override fun getItemCount(): Int = listModule.size

    class ListViewHolder(val binding: ViewholderClassDetailForumBinding) : RecyclerView.ViewHolder(binding.root)
}