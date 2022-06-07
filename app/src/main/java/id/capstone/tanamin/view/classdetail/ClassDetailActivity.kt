package id.capstone.tanamin.view.classdetail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smarteist.autoimageslider.SliderView
import id.capstone.tanamin.R
import id.capstone.tanamin.data.local.database.Classes
import id.capstone.tanamin.databinding.ActivityClassDetailBinding
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.databinding.CustomAlertRequirementBinding
import id.capstone.tanamin.view.quiz.QuizActivity
import id.capstone.tanamin.view.classmodule.ClassModuleActivity
import id.capstone.tanamin.view.forumcreate.ForumCreateActivity

class ClassDetailActivity : AppCompatActivity() {
    private var _binding: ActivityClassDetailBinding?=null
    private val binding get():ActivityClassDetailBinding=_binding!!
    private lateinit var dataDetail: Classes
    private lateinit var viewPager: ViewPager2

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
        binding.expandTextView.text=dataDetail.detail
        binding.ivBackButton.setOnClickListener {
            onBackPressed()
        }
        binding.btnStartLearn2.setOnClickListener{
            showRequirementDialog()
        }
    }

    private fun setupPager(){
        val sectionsPagerAdapter = ViewPagerAdapter(this)
        sectionsPagerAdapter.classId = dataDetail.id_class.toString()
        viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if(position ==0){
                    if(dataDetail.progress.compareTo(0.0)!=0){
                        binding.btnStartLearn.text = getString(R.string.continue_study)
                    }else{
                        binding.btnStartLearn.text = getString(R.string.class_detail_silabus_button_text)
                    }
                    binding.btnStartLearn.setOnClickListener{
                        if(dataDetail.lastest_module < dataDetail.total_module){
                            val intent = Intent(this@ClassDetailActivity, ClassModuleActivity::class.java)
                            intent.putExtra(ID_CLASS, dataDetail.id_class)
                            intent.putExtra(ID_MODULE_EXTRA,dataDetail.lastest_module)
                            intent.putExtra(CLASS_TITLE_EXTRA,dataDetail.title)
                            startActivity(intent)
                        }else{
                            val intent = Intent(this@ClassDetailActivity, QuizActivity::class.java)
                            intent.putExtra(ID_CLASS, dataDetail.id_class)
                            intent.putExtra(ID_MODULE_EXTRA,dataDetail.lastest_module)
                            intent.putExtra(CLASS_TITLE_EXTRA,dataDetail.title)
                            startActivity(intent)
                        }
                    }
                }else{
                    binding.btnStartLearn.text = getString(R.string.new_forum)
                    binding.btnStartLearn.setOnClickListener{
                        val intent = Intent(this@ClassDetailActivity, ForumCreateActivity::class.java)
                        intent.putExtra(ID_CLASS, dataDetail.id_class)
                        startActivity(intent)
                    }
                }
                super.onPageSelected(position)
            }
        })
    }

    private fun setupCarousel(){
        val imageClass = dataDetail.picture.split(",").toTypedArray()
        val sliderView = findViewById<SliderView>(R.id.imageSlider)
        val adapter = SliderAdapter(this, imageClass)
        sliderView.setSliderAdapter(adapter)
        sliderView.startAutoCycle()
    }

    fun showDialog(text: String, icon: Drawable) {
        val builder = AlertDialog.Builder(this).create()
        val bindAlert: CustomAlertApiBinding = CustomAlertApiBinding.inflate(LayoutInflater.from(this))
        builder.setView(bindAlert.root)
        bindAlert.infoDialog.text = text
        bindAlert.imageView5.setImageDrawable(icon)
        bindAlert.closeButton.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    private fun showRequirementDialog() {
        val builder = AlertDialog.Builder(this).create()
        val bindAlert: CustomAlertRequirementBinding = CustomAlertRequirementBinding.inflate(LayoutInflater.from(this))
        builder.setView(bindAlert.root)
        bindAlert.closeButton.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.class_detail_tablayouttext1,
            R.string.class_detail_tablayouttext2
        )
        const val ID_MODULE_EXTRA="id_modul"
        const val CLASS_TITLE_EXTRA="class_title"
        const val DETAIL_CLASS = "detail_class"
        const val ID_CLASS = "id_class"
    }
}