package id.capstone.tanamin.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.databinding.FragmentHomeBinding
import id.capstone.tanamin.view.MainActivity
import id.capstone.tanamin.view.ViewModelFactory
import id.capstone.tanamin.view.futurefeature.FutureFeatureActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var liveDataStore : LiveData<Int>
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        setupView()
        setupViewModel()
        getHomeData()
    }

    private fun setupView(){
        binding.marketplace.setOnClickListener{
            val intent = Intent(requireContext(), FutureFeatureActivity::class.java)
            startActivity(intent)
        }

        binding.encyclopedia.setOnClickListener{
            val intent = Intent(requireContext(), FutureFeatureActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel(){
        val pref=LoginPreferences.getInstance(requireContext().dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), "")
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }
        this.homeViewModel=homeViewModel
    }

    private fun getHomeData(){
        val homeMap: HashMap<String, String> = HashMap()
        liveDataStore = preferencesViewModel.getIDUser()
        liveDataStore.observe(requireActivity()) { userId ->
            homeMap["userid"] = userId.toString()
            val liveData = homeViewModel.getHomeData(homeMap)
            liveData.observe(requireActivity()){ result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.loadingList2.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.loadingList2.visibility = View.GONE
                            if(result.data.data !=null){
                                val percentage = "${result.data.data.progress} %"
                                binding.classTitle.text=result.data.data.kelas[0].title
                                binding.continueContent.text=result.data.data.recentModul.toString()
                                binding.percentage.text=percentage
                                binding.cardViewNoClass.visibility=View.GONE
                                binding.cardViewClass.setOnClickListener{

                                }
                            }else{
                                binding.loadingList2.visibility = View.GONE
                                binding.cardViewNoClass.visibility=View.VISIBLE
                                binding.cardViewClass.visibility=View.INVISIBLE
                            }
                            liveData.removeObservers(requireActivity())
                            liveDataStore.removeObservers(requireActivity())
                        }
                        is Result.Error -> {
                            binding.loadingList2.visibility = View.GONE
                            ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_error_24)
                                ?.let { (requireActivity() as MainActivity).showDialog(result.error, it) }
                            liveData.removeObservers(requireActivity())
                            liveDataStore.removeObservers(requireActivity())
                        }
                    }
                }
            }
        }
        preferencesViewModel.getNameUser().observe(requireActivity()){ username->
            val nameHome = "Hai, ${username.substring(username.lastIndexOf(" ")+1)}"
            binding.textView.text=nameHome
        }
    }
}