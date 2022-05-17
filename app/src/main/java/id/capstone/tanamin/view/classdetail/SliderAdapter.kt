package id.capstone.tanamin.view.classdetail

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.smarteist.autoimageslider.SliderViewAdapter
import id.capstone.tanamin.databinding.SliderItemBinding
import id.capstone.tanamin.databinding.ViewholderClassDetailSilabusBinding

class SliderAdapter(private val context: Context) :
    SliderViewAdapter<SliderAdapter.SliderViewholder>() {
//    private var mSliderItems: MutableList<String> = mutableListOf()
    private var mSliderItems= mutableListOf<String>("1","2","3","4")
    fun renewItems(sliderItems: MutableList<String>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: String) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderViewholder {
        val inflate = SliderItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return SliderViewholder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderViewholder, position: Int) {
//        val sliderItem: String = mSliderItems[position]
//        Glide.with(viewHolder.imageSliderItem)
//            .load(sliderItem)
//            .fitCenter()
//            .into(viewHolder.imageSliderItem)
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    class SliderViewholder(binding: SliderItemBinding) : ViewHolder(binding.root){
        val imageSliderItem=binding.sliderItemImage
    }
}