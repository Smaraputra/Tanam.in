package id.capstone.tanamin.view.classdetail.silabus

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.data.remote.response.ListModuleData
import id.capstone.tanamin.databinding.FragmentSilabusBinding
import id.capstone.tanamin.view.ViewModelFactory
import id.capstone.tanamin.view.classdetail.ClassDetailActivity
import id.capstone.tanamin.view.classmodule.ClassModuleViewModel

class SilabusFragment : Fragment() {
    private var _binding: FragmentSilabusBinding? = null
    private val binding get() = _binding!!
    private var classID : String = ""
    private lateinit var silabusViewModel: SilabusViewModel
    private lateinit var liveDataStore : LiveData<String>
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var statusViewModel : LiveData<Boolean>
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")

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
        val pref= LoginPreferences.getInstance(requireContext().dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        preferencesViewModel.getTokenUser().observe(requireActivity()){ token ->
            val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), token)
            val silabusViewModel: SilabusViewModel by viewModels {
                factory
            }
            this.silabusViewModel=silabusViewModel
            preferencesViewModel.saveViewModelStatus(true)
        }
    }

    private fun getClassList(){
        statusViewModel = preferencesViewModel.getViewModelStatus()
        statusViewModel.observe(requireActivity()) { status ->
            if (status) {
                val liveData = silabusViewModel.getAllModule(classID)
                liveData.observe(requireActivity()) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.loadingForum.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.loadingForum.visibility = View.GONE
                                binding.cardViewNoInternet.visibility = View.GONE
                                if (result.data.data != null) {
                                    binding.cardViewNoModuleFound.visibility = View.GONE
                                    setupAdapter(result.data.data)
                                } else {
                                    binding.cardViewNoModuleFound.visibility = View.VISIBLE
                                }
                                liveData.removeObservers(requireActivity())
                            }
                            is Result.Error -> {
                                binding.loadingForum.visibility = View.GONE
                                binding.cardViewNoModuleFound.visibility = View.GONE
                                binding.cardViewNoInternet.visibility = View.VISIBLE
                                ContextCompat.getDrawable(
                                    requireActivity(),
                                    R.drawable.ic_baseline_error_24
                                )
                                    ?.let {
                                        (requireActivity() as ClassDetailActivity).showDialog(
                                            result.error,
                                            it
                                        )
                                    }
                                liveData.removeObservers(requireActivity())
                            }
                        }
                    }
                }
            }
            statusViewModel.removeObservers(requireActivity())
        }
    }

    private fun setupAdapter(listModuleData : ListModuleData){
        val silabusListAdapter = SilabusListAdapter(requireActivity(), listModuleData)
        binding.rvListSilabus.setHasFixedSize(true)
        binding.rvListSilabus.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvListSilabus.adapter = silabusListAdapter
    }

    companion object {
        const val ARG_CLASS = "class"
    }
}