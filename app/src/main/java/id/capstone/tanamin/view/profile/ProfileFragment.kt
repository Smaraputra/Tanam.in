package id.capstone.tanamin.view.profile

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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.data.remote.response.ProfileResponse
import id.capstone.tanamin.data.remote.response.User
import id.capstone.tanamin.databinding.CustomAlertApiBinding
import id.capstone.tanamin.databinding.CustomAlertLogoutBinding
import id.capstone.tanamin.databinding.FragmentProfileBinding
import id.capstone.tanamin.view.ViewModelFactory
import id.capstone.tanamin.view.login.LoginActivity
import id.capstone.tanamin.view.profileedit.ProfileEditActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var liveData : LiveData<Result<ProfileResponse>>
    private lateinit var liveDataStore : LiveData<Int>
    private var user:User? = null
    private lateinit var liveDataStoreToken : LiveData<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        binding.logoutButton.setOnClickListener{
            showDialogLogout()
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            setupViewModel()
        }
        getProfileUser()
    }

    private fun setupViewModel(){
        val pref=LoginPreferences.getInstance(requireContext().dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        liveDataStoreToken = preferencesViewModel.getTokenUser()
    }

    private fun showDialogLogout() {
        val builder = AlertDialog.Builder(requireContext()).create()
        val bindAlert: CustomAlertLogoutBinding = CustomAlertLogoutBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(bindAlert.root)
        bindAlert.logoutConfirm.setOnClickListener {
            builder.dismiss()
            liveData.removeObservers(requireActivity())
            preferencesViewModel.saveNameUser("DEFAULT_VALUE")
            preferencesViewModel.saveIDUser(0)
            preferencesViewModel.saveTokenUser("DEFAULT_VALUE")
            val intent = Intent(requireContext(), LoginActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }
        bindAlert.cancelButton.setOnClickListener {
            builder.dismiss()
        }
        builder.show()
    }

    private fun getProfileUser(){
        liveDataStoreToken.observe(this){
            val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), it)
            val profileViewModel: ProfileViewModel by viewModels {
                factory
            }
            this.profileViewModel=profileViewModel
            val profileMap: HashMap<String, String> = HashMap()
            liveDataStore = preferencesViewModel.getIDUser()
            liveDataStore.observe(requireActivity()) { userId ->
                profileMap["userid"] = userId.toString()
                liveData = profileViewModel.getProfileUser(profileMap)
                liveData.observe(requireActivity()) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.loadingList3.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                user=result.data.data.user
                                if(user!=null){
                                    binding.editDataButton .setOnClickListener{
                                        val intent = Intent(requireContext(), ProfileEditActivity::class.java)
                                        intent.putExtra(PROFILE_USER_EXTRA,user)
                                        startActivity(intent)
                                    }
                                }
                                val success = "${result.data.data.finish ?: "0"} Selesai"
                                val process = "${result.data.data.progress ?: "0"} Proses"
                                binding.loadingList3.visibility = View.GONE
                                binding.textView3.text=result.data.data.user.name
                                binding.textView4.text=result.data.data.user.email
                                binding.textView5.text= if(result.data.data.user.age != null && result.data.data.user.age != 0)
                                    result.data.data.user.age.toString() else "Tidak ada data"
                                binding.textView6.text=if(result.data.data.user.address != null && result.data.data.user.address != "dikosongkan")
                                    result.data.data.user.address else "Tidak ada data"
                                binding.successCount.text=success
                                binding.processCount.text=process
                                Glide.with(this)
                                    .load(result.data.data.user.profilePicture)
                                    .placeholder(R.drawable.ic_profileuser_illustration)
                                    .error(R.drawable.ic_profileuser_illustration)
                                    .into(binding.classImage2)
                                liveData.removeObservers(requireActivity())
                                liveDataStore.removeObservers(requireActivity())
                                liveDataStoreToken.removeObservers(requireActivity())
                            }
                            is Result.Error -> {
                                if(user==null){
                                    binding.editDataButton .setOnClickListener{
                                        ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_error_24)
                                            ?.let { showDialog(getString(R.string.no_connection), it) }
                                    }
                                }
                                binding.loadingList3.visibility = View.GONE
                                ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_error_24)
                                    ?.let { showDialog(result.error, it)
                                    }
                                liveData.removeObservers(requireActivity())
                                liveDataStore.removeObservers(requireActivity())
                                liveDataStoreToken.removeObservers(requireActivity())
                            }
                        }
                    }
                }
            }
        }
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

    companion object{
        const val PROFILE_USER_EXTRA = "profile_user"
    }
}