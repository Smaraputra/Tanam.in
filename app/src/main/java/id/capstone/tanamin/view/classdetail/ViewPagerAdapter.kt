package id.capstone.tanamin.view.classdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.capstone.tanamin.view.classdetail.forum.ForumFragment
import id.capstone.tanamin.view.classdetail.silabus.SilabusFragment

class ViewPagerAdapter(activity:AppCompatActivity):FragmentStateAdapter(activity) {
    var classId: String = ""
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
        fragment?.arguments = Bundle().apply {
            putString(SilabusFragment.ARG_CLASS, classId)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}