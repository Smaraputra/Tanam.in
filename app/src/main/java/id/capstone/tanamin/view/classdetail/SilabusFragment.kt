package id.capstone.tanamin.view.classdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.capstone.tanamin.databinding.FragmentSilabusBinding

class SilabusFragment : Fragment() {
    private var _binding: FragmentSilabusBinding? = null
    private val binding get() = _binding!!
    private lateinit var silabusListAdapter: SilabusListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentSilabusBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        silabusListAdapter = SilabusListAdapter(requireContext())
        binding.rvListSilabus.setHasFixedSize(true)
        binding.rvListSilabus.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListSilabus.adapter = silabusListAdapter
    }
}