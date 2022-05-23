package id.capstone.tanamin.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.capstone.tanamin.databinding.FragmentHomeBinding
import id.capstone.tanamin.view.classdetail.ClassDetailActivity
import id.capstone.tanamin.view.futurefeature.FutureFeatureActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
    }

    private fun setupView(){
        binding.cardView.setOnClickListener{
            val intent = Intent(requireContext(), ClassDetailActivity::class.java)
            startActivity(intent)
        }

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

    }
}