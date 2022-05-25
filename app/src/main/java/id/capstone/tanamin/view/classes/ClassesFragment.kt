package id.capstone.tanamin.view.classes

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.database.Classes
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.databinding.FragmentClassesBinding
import id.capstone.tanamin.view.MainActivity
import id.capstone.tanamin.view.ViewModelFactory

class ClassesFragment : Fragment() {
    private var _binding: FragmentClassesBinding? = null
    private val binding get() = _binding!!
    private lateinit var classesListAdapter: ClassesListAdapter
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var classesViewModel: ClassesViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        setupView()
        setupViewModel()
        getClassList()
    }

    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        binding.swipeRefreshLayout.isRefreshing = false
        getClassList()
    }

    private fun setupView(){
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)
        binding.searchView.addTextChangedListener(textWatcher)
    }

    private fun setupViewModel(){
        val pref= LoginPreferences.getInstance(requireContext().dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), "")
        val classesViewModel: ClassesViewModel by viewModels {
            factory
        }
        this.classesViewModel=classesViewModel
    }

    private fun getClassList(){
        preferencesViewModel.getIDUser().observe(requireActivity()){ userid->
            val liveData = classesViewModel.getAllClass(userid.toString())
            liveData.observe(requireActivity()){ result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.loadingList4.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.loadingList4.visibility = View.GONE
                            setupAdapter(result.data.data.jsonMemberClass)
                            liveData.removeObservers(requireActivity())
                        }
                        is Result.Error -> {
                            binding.loadingList4.visibility = View.GONE
                            ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_error_24)
                                ?.let { (requireActivity() as MainActivity).showDialog(result.error, it) }
                            liveData.removeObservers(requireActivity())
                        }
                    }
                }
            }
        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }
        override fun afterTextChanged(s: Editable) {
            val liveData = classesViewModel.searchWord(binding.searchView.text.toString())
            liveData.observe(requireActivity()){
                setupAdapter(it)
                liveData.removeObservers(requireActivity())
            }
        }
    }

    private fun setupAdapter(classes: List<Classes>){
        binding.rvClassesList.setHasFixedSize(true)
        binding.rvClassesList.layoutManager = LinearLayoutManager(requireContext())
        classesListAdapter = ClassesListAdapter(requireContext(), classes)
        binding.rvClassesList.adapter = classesListAdapter
    }
}