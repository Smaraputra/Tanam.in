package id.capstone.tanamin.view.classes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.capstone.tanamin.R
import id.capstone.tanamin.data.local.database.Classes
import id.capstone.tanamin.databinding.RvClassBinding
import id.capstone.tanamin.view.classdetail.ClassDetailActivity

class ClassesListAdapter(private val ctx: Context, private val classes: List<Classes>) : RecyclerView.Adapter<ClassesListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = RvClassBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = classes[position]
        Glide.with(ctx)
            .asBitmap()
            .load(data.picture)
            .placeholder(R.drawable.ic_background_logo)
            .error(R.drawable.ic_background_logo)
            .into(holder.binding.classImage)
        holder.binding.classTitle.text = if(data.title.isNotEmpty()) data.title else holder.itemView.context.getString(R.string.no_data)
        holder.binding.continueContent.text = if(data.modul_title.isNotEmpty()) data.modul_title else holder.itemView.context.getString(R.string.no_data)
        holder.binding.percentage.text = if(!data.progress.isNaN() && data.progress>0.0) "${data.progress} %" else "0.0 %"
        holder.binding.button3.text = if(!data.progress.isNaN() && data.progress<=0.0) "Mulai" else "Lanjut"
        holder.itemView.setOnClickListener {
            val intent = Intent(ctx, ClassDetailActivity::class.java)
            intent.putExtra(DETAIL_CLASS, data)
            ctx.startActivity(intent)
        }
    }

    class ListViewHolder(val binding: RvClassBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = classes.size

    companion object {
        const val DETAIL_CLASS = "detail_class"
    }
}