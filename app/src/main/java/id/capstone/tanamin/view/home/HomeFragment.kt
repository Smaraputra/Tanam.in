package id.capstone.tanamin.view.home

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.bumptech.glide.Glide
import id.capstone.tanamin.BuildConfig.BASE_URL_IMAGE
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.databinding.FragmentHomeBinding
import id.capstone.tanamin.view.ViewModelFactory
import id.capstone.tanamin.view.classdetail.ClassDetailActivity
import id.capstone.tanamin.view.futurefeature.FutureFeatureActivity
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var liveDataStore : LiveData<Int>
    private lateinit var liveDataToken : LiveData<String>
    private lateinit var statusViewModel : LiveData<Boolean>
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
        binding.textView.loadSkeleton()
        binding.layoutCard.loadSkeleton()
        setupView()
        setupViewModel()
    }

    override fun onStart() {
        super.onStart()
        getHomeData()
    }

    private fun isAttachedToActivity(): Boolean {
        return isAdded && activity != null
    }

    private fun setupView(){
        binding.marketplace.setOnClickListener{
            val intent = Intent(requireContext(), FutureFeatureActivity::class.java)
            intent.putExtra(FEATURE_FUTURE,"Marketplace")
            startActivity(intent)
        }

        binding.encyclopedia.setOnClickListener{
            val intent = Intent(requireContext(), FutureFeatureActivity::class.java)
            intent.putExtra(FEATURE_FUTURE,"Encyclopedia")
            startActivity(intent)
        }
    }

    private fun setupViewModel(){
        val pref=LoginPreferences.getInstance(requireContext().dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        liveDataToken=preferencesViewModel.getTokenUser()
        liveDataToken.observe(this) { token ->
            val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), token)
            val homeViewModel: HomeViewModel by viewModels {
                factory
            }
            this.homeViewModel = homeViewModel
            preferencesViewModel.saveViewModelStatus(true)
            liveDataToken.removeObservers(this)
        }
    }

    private fun getHomeData(){
        statusViewModel = preferencesViewModel.getViewModelStatus()
        statusViewModel.observe(this){ status ->
            if(status) {
                val homeMap: HashMap<String, String> = HashMap()
                liveDataStore = preferencesViewModel.getIDUser()
                liveDataStore.observe(requireActivity()) { userId ->
                    if (isAttachedToActivity()) {
                        homeMap["userid"] = userId.toString()
                        val liveData = homeViewModel.getHomeData(homeMap)
                        liveData.observe(requireActivity()) { result ->
                            if (result != null) {
                                when (result) {
                                    is Result.Loading -> {
                                        binding.loadingList2.visibility = View.VISIBLE
                                    }
                                    is Result.Success -> {
                                        binding.loadingList2.visibility = View.GONE
                                        if (result.data.data != null) {
                                            binding.cardViewClass.visibility = View.VISIBLE
                                            binding.cardViewNoClass.visibility = View.GONE
                                            binding.cardViewNoInternet.visibility = View.GONE
                                            val percentage = "${String.format("%.2f", result.data.data.kelas[0].progress)} %"
                                            Glide.with(requireActivity())
                                                .asBitmap()
                                                .load(BASE_URL_IMAGE + result.data.data.kelas[0].picture)
                                                .placeholder(R.drawable.ic_background_logo)
                                                .error(R.drawable.ic_background_logo)
                                                .into(binding.classImage)
                                            binding.classTitle.text = result.data.data.kelas[0].title
                                            binding.continueContent.text =
                                                result.data.data.kelas[0].modul_title
                                            binding.percentage.text = percentage
                                            binding.cardViewClass.setOnClickListener {
                                                val data = result.data.data.kelas[0]
                                                val intent = Intent(
                                                    requireActivity(),
                                                    ClassDetailActivity::class.java
                                                )
                                                intent.putExtra(DETAIL_CLASS, data)
                                                startActivity(intent)
                                            }
                                        } else {
                                            binding.loadingList2.visibility = View.GONE
                                            binding.cardViewNoClass.visibility = View.VISIBLE
                                            binding.cardViewClass.visibility = View.INVISIBLE
                                            binding.cardViewNoInternet.visibility = View.GONE
                                        }
                                        binding.layoutCard.hideSkeleton()
                                        liveData.removeObservers(requireActivity())
                                        liveDataStore.removeObservers(requireActivity())
                                    }
                                    is Result.Error -> {
                                        binding.loadingList2.visibility = View.GONE
                                        binding.cardViewNoInternet.visibility = View.VISIBLE
                                        binding.cardViewNoClass.visibility = View.INVISIBLE
                                        binding.cardViewClass.visibility = View.INVISIBLE
                                        ContextCompat.getDrawable(
                                            requireActivity(),
                                            R.drawable.ic_baseline_error_24
                                        )
                                            ?.let { showDialog(result.error, it) }
                                        binding.layoutCard.hideSkeleton()
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
        val liveDataName = preferencesViewModel.getNameUser()
        liveDataName.observe(requireActivity()){ username->
            val nameHome = "Hai, ${username.substring(username.lastIndexOf(" ")+1)}"
            binding.textView.text=nameHome
            binding.textView.hideSkeleton()
            liveDataName.removeObservers(requireActivity())
        }
    }

    fun showDialog(text: String, icon: Drawable) {
        val builder = AlertDialog.Builder(requireActivity()).create()
        val bindAlert: CustomAlertApiBinding = CustomAlertApiBinding.inflate(LayoutInflater.from(requireActivity()))
        builder.setView(bindAlert.root)
        bindAlert.infoDialog.text = text
        bindAlert.imageView5.setImageDrawable(icon)
        bindAlert.closeButton.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    companion object {
        const val DETAIL_CLASS = "detail_class"
        const val FEATURE_FUTURE = "feature_future"
    }
}