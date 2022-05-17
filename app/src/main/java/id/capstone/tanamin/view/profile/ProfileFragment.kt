package id.capstone.tanamin.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.capstone.tanamin.data.local.datastore.LoginPreferences
import id.capstone.tanamin.data.local.datastore.PreferencesViewModel
import id.capstone.tanamin.data.local.datastore.PreferencesViewModelFactory
import id.capstone.tanamin.databinding.CustomAlertLogoutBinding
import id.capstone.tanamin.databinding.FragmentProfileBinding
import id.capstone.tanamin.view.login.LoginActivity
import id.capstone.tanamin.view.profileedit.ProfileEditActivity

class ProfileFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userSession")
    private lateinit var preferencesViewModel: PreferencesViewModel
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
        binding.logoutButton.setOnClickListener{
            showDialogLogout()
        }

        binding.editDataButton .setOnClickListener{
            val intent = Intent(requireContext(), ProfileEditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewModel(){
        val pref = LoginPreferences.getInstance(requireContext().dataStore)
        preferencesViewModel = ViewModelProvider(this, PreferencesViewModelFactory(pref)).get(
            PreferencesViewModel::class.java
        )
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
}