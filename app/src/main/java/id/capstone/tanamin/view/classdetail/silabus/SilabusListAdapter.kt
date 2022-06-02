package id.capstone.tanamin.view.classdetail.silabus

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.capstone.tanamin.data.remote.response.ListModuleData
import id.capstone.tanamin.databinding.ViewholderClassDetailSilabusBinding
import id.capstone.tanamin.view.classmodule.ClassModuleActivity
import id.capstone.tanamin.view.classmodule.ClassModuleActivity.Companion.CLASS_TITLE_EXTRA
import id.capstone.tanamin.view.classmodule.ClassModuleActivity.Companion.ID_CLASS_EXTRA
import id.capstone.tanamin.view.classmodule.ClassModuleActivity.Companion.ID_MODULE_EXTRA

class SilabusListAdapter(private val ctx: Context, private val listModuleData: ListModuleData) : RecyclerView.Adapter<SilabusListAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding = ViewholderClassDetailSilabusBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listModuleData.listmodul[position]
        holder.binding.tvSilabusTitle.text = data.title
        holder.binding.cardView.setOnClickListener {
            if(data.idModuls==listModuleData.listmodul.size){
                //intent to quiz activity goes here
            }else{
                val intent = Intent(ctx, ClassModuleActivity::class.java)
                intent.putExtra(ID_MODULE_EXTRA, data.idModuls)
                intent.putExtra(ID_CLASS_EXTRA,listModuleData.detailKelas[0].idClass)
                intent.putExtra(CLASS_TITLE_EXTRA,listModuleData.detailKelas[0].title)
                ctx.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listModuleData.listmodul.size

    class ListViewHolder(val binding: ViewholderClassDetailSilabusBinding) : RecyclerView.ViewHolder(binding.root)

}