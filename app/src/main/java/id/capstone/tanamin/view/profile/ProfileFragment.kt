package id.capstone.tanamin.view.profile

import android.content.Context
import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.capstone.tanamin.R
import id.capstone.tanamin.data.Result
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.databinding.CustomAlertLogoutBinding
import id.capstone.tanamin.databinding.FragmentProfileBinding
import id.capstone.tanamin.view.MainActivity
import id.capstone.tanamin.view.ViewModelFactory
import id.capstone.tanamin.view.login.LoginActivity
import id.capstone.tanamin.view.profileedit.ProfileEditActivity

class ProfileFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")
    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

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
        setupViewModel()
        getProfileUser()
        binding.logoutButton.setOnClickListener{
            showDialogLogout()
        }

        binding.editDataButton .setOnClickListener{
            val intent = Intent(requireContext(), ProfileEditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel(){
        val pref=LoginPreferences.getInstance(requireContext().dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity(), "")
        val profileViewModel: ProfileViewModel by viewModels {
            factory
        }
        this.profileViewModel=profileViewModel
    }



    private fun showDialogLogout() {
        val builder = AlertDialog.Builder(requireContext()).create()
        val bindAlert: CustomAlertLogoutBinding = CustomAlertLogoutBinding.inflate(LayoutInflater.from(requireContext()))
        builder.setView(bindAlert.root)
        bindAlert.logoutConfirm.setOnClickListener {
            builder.dismiss()
            preferencesViewModel.saveNameUser("DEFAULT_VALUE")
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
        val profileMap: HashMap<String, String> = HashMap()
        preferencesViewModel.getIDUser().observe(requireActivity()) {
            profileMap["userid"] = it.toString()
            profileViewModel.getProfileUser(profileMap).observe(requireActivity()) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
//                            binding.loadingList.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
//                            binding.loadingList.visibility = View.GONE
                            binding.textView3.text=result.data.data.name
                            binding.textView4.text=result.data.data.email
                            binding.textView5.text=result.data.data.age?.toString() ?: "Tidak ada data"
                            binding.textView6.text=result.data.data.address ?: "Tidak ada data"
                            binding.successCount.text="${result.data.data.finish?.toString() ?: "0"} Selesai"
                            binding.processCount.text="${result.data.data.progress?.toString() ?: "0"} Proses"
                            Glide.with(this).load(result.data.data.profilePicture?:this.getResources().getIdentifier("ic_profileuser_illustration", "drawable", requireActivity().getPackageName())).into(binding.classImage2)
                            profileViewModel.getProfileUser(profileMap)
                                .removeObservers(requireActivity())
                        }
                        is Result.Error -> {
//                            binding.loadingList.visibility = View.GONE
                            ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.ic_baseline_error_24
                            )
                                ?.let {
                                    (requireActivity() as MainActivity).showDialog(
                                        result.error,
                                        it
                                    )
                                }
                            profileViewModel.getProfileUser(profileMap)
                                .removeObservers(requireActivity())
                        }
                    }
                }
            }
        }
    }
}