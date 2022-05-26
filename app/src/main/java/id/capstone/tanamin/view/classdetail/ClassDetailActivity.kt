package id.capstone.tanamin.view.classdetail

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smarteist.autoimageslider.SliderView
import id.capstone.tanamin.databinding.ActivityClassDetailBinding
import id.capstone.tanamin.view.classmodule.ClassModuleActivity
import id.capstone.tanamin.R
import id.capstone.tanamin.data.local.database.Classes

class ClassDetailActivity : AppCompatActivity() {
    private var _binding: ActivityClassDetailBinding?=null
    private val binding get():ActivityClassDetailBinding=_binding!!
    private lateinit var dataDetail: Classes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityClassDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        dataDetail = intent.getParcelableExtra<Classes>(DETAIL_CLASS) as Classes

        setupView()
    }

    private fun setupView(){
        setupPager()
        setupCarousel()
        binding.tvDetectionItemName.text=dataDetail.title
        binding.expandTextView.text="Jeruk atau limau adalah semua tumbuhan berbunga anggota marga Citrus dari suku Rutaceae (suku jeruk-jerukan). Anggotanya berbentuk pohon dengan buah yang berdaging dengan rasa masam yang segar, meskipun banyak di antara anggotanya yang memiliki rasa manis. Rasa masam berasal dari kandungan asam sitrat yang memang menjadi terkandung pada semua anggotanya."
        binding.btnStartLearn.setOnClickListener {
            val intent = Intent(this, ClassModuleActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupPager(){
        val sectionsPagerAdapter = ViewPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setupCarousel(){
        val imageClass = dataDetail.picture.split(",").toTypedArray()
        val sliderView = findViewById<SliderView>(R.id.imageSlider)
        val adapter = SliderAdapter(this, imageClass)
        sliderView.setSliderAdapter(adapter)
        sliderView.startAutoCycle()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.class_detail_tablayouttext1,
            R.string.class_detail_tablayouttext2
        )
        const val DETAIL_CLASS = "detail_class"
    }
}