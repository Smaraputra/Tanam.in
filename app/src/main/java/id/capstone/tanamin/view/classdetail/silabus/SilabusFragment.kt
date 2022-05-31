package id.capstone.tanamin.view.classdetail.silabus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.remote.response.ListmodulItem
import id.capstone.tanamin.databinding.FragmentSilabusBinding
import id.capstone.tanamin.view.ViewModelFactory
import id.capstone.tanamin.view.classdetail.ClassDetailActivity

class SilabusFragment : Fragment() {
    private var _binding: FragmentSilabusBinding? = null
    private val binding get() = _binding!!
    private var classID : String = ""
    private lateinit var silabusViewModel: SilabusViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentSilabusBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        classID = arguments?.getString(ARG_CLASS)!!
        setupViewModel()
        getClassList()
    }

    override fun onResume() {
        super.onResume()
        getClassList()
    }

    private fun setupViewModel(){
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), "")
        val silabusViewModel: SilabusViewModel by viewModels {
            factory
        }
        this.silabusViewModel=silabusViewModel
    }

    private fun getClassList(){
        val liveData = silabusViewModel.getAllModule(classID)
        liveData.observe(requireActivity()){ result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loadingForum.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.loadingForum.visibility = View.GONE
                        binding.cardViewNoInternet.visibility = View.GONE
                        if(result.data.data != null){
                            binding.cardViewNoModuleFound.visibility = View.GONE
                            setupAdapter(result.data.data.listmodul)
                            liveData.removeObservers(requireActivity())
                        }else{
                            binding.cardViewNoModuleFound.visibility = View.VISIBLE
                        }
                    }
                    is Result.Error -> {
                        binding.loadingForum.visibility = View.GONE
                        binding.cardViewNoModuleFound.visibility = View.GONE
                        binding.cardViewNoInternet.visibility = View.VISIBLE
                        ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_error_24)
                            ?.let { (requireActivity() as ClassDetailActivity).showDialog(result.error, it) }
                        liveData.removeObservers(requireActivity())
                    }
                }
            }
        }
    }

    private fun setupAdapter(listModule : List<ListmodulItem>){
        val silabusListAdapter = SilabusListAdapter(requireActivity(), listModule)
        binding.rvListSilabus.setHasFixedSize(true)
        binding.rvListSilabus.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvListSilabus.adapter = silabusListAdapter
    }

    companion object {
        const val ARG_CLASS = "class"
    }
}