package id.capstone.tanamin.view.classdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.capstone.tanamin.databinding.FragmentForumBinding

class ForumFragment : Fragment() {
    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentForumBinding.inflate(inflater,container,false)
        return binding.root
    }

}