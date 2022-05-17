package id.capstone.tanamin.view.classdetail

import id.capstone.tanamin.R
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(activity:AppCompatActivity):FragmentStateAdapter(activity) {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.class_detail_tablayouttext1,
            R.string.class_detail_tablayouttext2
        )
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> {
                fragment = SilabusFragment()
            }
            1 -> {
                fragment = ForumFragment()
            }
        }
        return fragment as Fragment
    }

}