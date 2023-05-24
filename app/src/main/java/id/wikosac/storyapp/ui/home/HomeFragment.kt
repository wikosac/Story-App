package id.wikosac.storyapp.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.databinding.FragmentHomeBinding
import id.wikosac.storyapp.ui.custom.DividerItemDecorator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences = requireActivity().getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val tokenPref = sharedPreferences.getString("TOKEN", "").toString()
        homeViewModel.getStory(tokenPref)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.storyList.observe(viewLifecycleOwner) {
            if (homeViewModel.storyList.value != null) {
                setItemData(it)
            }
        }
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