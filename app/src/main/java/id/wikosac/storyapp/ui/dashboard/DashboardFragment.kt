package id.wikosac.storyapp.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.wikosac.storyapp.MainActivity
import id.wikosac.storyapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val intent = requireActivity().intent
        val token = intent.getStringExtra("token").toString()
        dashboardViewModel.getStory(token)

        val textView: TextView = binding.textDashboard
        dashboardViewModel.storyList.observe(viewLifecycleOwner) {
            Log.d(MainActivity.TAG, "onCreateView: ${dashboardViewModel.storyList.value}")
            if (dashboardViewModel.storyList.value != null) {
                textView.text = it[0].toString()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}