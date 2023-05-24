package id.wikosac.storyapp.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.wikosac.storyapp.MainActivity
import id.wikosac.storyapp.R
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.databinding.FragmentHomeBinding
import id.wikosac.storyapp.ui.custom.DividerItemDecorator
import id.wikosac.storyapp.ui.detail.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences = requireActivity().getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val tokenPref = sharedPreferences.getString("TOKEN", "").toString()
        Log.d("Login", "onCreate: token -> $tokenPref")
        homeViewModel.getStory(tokenPref)

        homeViewModel.storyList.observe(viewLifecycleOwner) {
            Log.d("Login", "onCreateView: ${homeViewModel.storyList.value}")
            if (homeViewModel.storyList.value != null) {
                setItemData(it)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setItemData(stories: List<Story>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemDecorator = DividerItemDecorator()
        binding.recyclerView.addItemDecoration(itemDecorator)
        val adapter = HomeAdapter(stories)
        binding.recyclerView.adapter = adapter
    }
}