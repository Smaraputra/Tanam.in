package id.capstone.tanamin.view.classes

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.database.Classes
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.databinding.FragmentClassesBinding
import id.capstone.tanamin.view.ViewModelFactory
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

class ClassesFragment : Fragment() {
    private var _binding: FragmentClassesBinding? = null
    private val binding get() = _binding!!
    private lateinit var classesListAdapter: ClassesListAdapter
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var classesViewModel: ClassesViewModel
    private lateinit var liveDataStore : LiveData<Int>
    private lateinit var liveDataToken : LiveData<String>
    private lateinit var statusViewModel : LiveData<Boolean>
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
        binding.swipeRefreshLayout.loadSkeleton()
        setupViewModel()
    }

    override fun onStart() {
        super.onStart()
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
        val pref= LoginPreferences.getInstance(requireActivity().dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        liveDataToken = preferencesViewModel.getTokenUser()
        liveDataToken.observe(this){ token ->
            val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), token)
            val classesViewModel: ClassesViewModel by viewModels {
                factory
            }
            this.classesViewModel=classesViewModel
            preferencesViewModel.saveViewModelStatus(true)
            setupView()
            liveDataToken.removeObservers(this)
        }
    }

    private fun getClassList(){
        statusViewModel = preferencesViewModel.getViewModelStatus()
        statusViewModel.observe(this){ status->
            if(status){
                val classMap: HashMap<String, String> = HashMap()
                liveDataStore = preferencesViewModel.getIDUser()
                liveDataStore.observe(requireActivity()) { userId ->
                    classMap["userid"] = userId.toString()
                    val liveData = classesViewModel.getAllClass(classMap)
                    liveData.observe(requireActivity()){ result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    binding.loadingList4.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    binding.loadingList4.visibility = View.GONE
                                    binding.cardViewNoClassFound.visibility = View.GONE
                                    binding.cardViewNoInternet.visibility = View.GONE
                                    setupAdapter(result.data.data.jsonMemberClass)
                                    binding.swipeRefreshLayout.hideSkeleton()
                                    liveData.removeObservers(requireActivity())
                                    liveDataStore.removeObservers(requireActivity())
                                }
                                is Result.Error -> {
                                    binding.loadingList4.visibility = View.GONE
                                    val liveData2 = classesViewModel.searchWord("")
                                    liveData2.observe(requireActivity()){
                                        setupAdapter(it)
                                        if(it.isEmpty()){
                                            binding.cardViewNoInternet.visibility = View.VISIBLE
                                        }else{
                                            binding.cardViewNoInternet.visibility = View.GONE
                                        }
                                        liveData2.removeObservers(requireActivity())
                                    }
                                    ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_error_24)
                                        ?.let {showDialog(result.error, it) }
                                    binding.swipeRefreshLayout.hideSkeleton()
                                    liveData.removeObservers(requireActivity())
                                    liveDataStore.removeObservers(requireActivity())
                                }
                            }
                        }
                    }
                }
            }
            statusViewModel.removeObservers(requireActivity())
        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }
        override fun afterTextChanged(s: Editable) {
            binding.cardViewNoClassFound.visibility = View.GONE
            val liveData = classesViewModel.searchWord(binding.searchView.text.toString())
            liveData.observe(requireActivity()){
                setupAdapter(it)
                if(it.isEmpty()){
                    binding.cardViewNoClassFound.visibility = View.VISIBLE
                }else{
                    binding.cardViewNoClassFound.visibility = View.GONE
                }
                liveData.removeObservers(requireActivity())
            }
        }
    }

    private fun setupAdapter(classes: List<Classes>){
        binding.rvClassesList.setHasFixedSize(true)
        binding.rvClassesList.layoutManager = LinearLayoutManager(requireActivity())
        classesListAdapter = ClassesListAdapter(requireActivity(), classes)
        binding.rvClassesList.adapter = classesListAdapter
    }

    fun showDialog(text: String, icon: Drawable) {
        val builder = AlertDialog.Builder(requireContext()).create()
        val bindAlert: CustomAlertApiBinding = CustomAlertApiBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(bindAlert.root)
        bindAlert.infoDialog.text = text
        bindAlert.imageView5.setImageDrawable(icon)
        bindAlert.closeButton.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }
}