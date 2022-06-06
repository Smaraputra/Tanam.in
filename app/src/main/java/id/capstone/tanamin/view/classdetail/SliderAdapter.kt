package id.capstone.tanamin.view.classdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import id.capstone.tanamin.R
import id.capstone.tanamin.databinding.SliderItemBinding

class SliderAdapter(private val context: Context, private val classImages: Array<String>) : SliderViewAdapter<SliderAdapter.SliderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): SliderViewHolder {
        val inflate = SliderItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return SliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder, position: Int) {
        val sliderItem: String = classImages[position]
        Glide.with(viewHolder.imageSliderItem)
            .load(sliderItem)
            .placeholder(R.drawable.ic_background_logo)
            .error(R.drawable.ic_background_logo)
            .fitCenter()
            .into(viewHolder.imageSliderItem)
    }

    override fun getCount(): Int {
        return classImages.size
    }

    class SliderViewHolder(binding: SliderItemBinding) : ViewHolder(binding.root){
        val imageSliderItem=binding.sliderItemImage
    }
}