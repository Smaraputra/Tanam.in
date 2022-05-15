package id.capstone.tanamin.view.classdetail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.capstone.tanamin.R
import id.capstone.tanamin.databinding.ActivityClassDetailBinding
import id.capstone.tanamin.view.classmodule.ClassModuleActivity

class ClassDetailActivity : AppCompatActivity() {
    private var _binding: ActivityClassDetailBinding?=null
    private val binding get():ActivityClassDetailBinding=_binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityClassDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val sectionsPagerAdapter = ViewPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.btnStartLearn.setOnClickListener {
            val intent = Intent(this, ClassModuleActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.class_detail_tablayouttext1,
            R.string.class_detail_tablayouttext2
        )
    }
}