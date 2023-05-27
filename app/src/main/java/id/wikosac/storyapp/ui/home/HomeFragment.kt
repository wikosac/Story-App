package id.wikosac.storyapp.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.wikosac.storyapp.MainActivity
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.databinding.FragmentHomeBinding
import id.wikosac.storyapp.ui.auth.LoginActivity
import id.wikosac.storyapp.ui.custom.DividerItemDecorator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getStory()
            setListItem()
        homeViewModel.storyList.observe(viewLifecycleOwner) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListItem() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemDecorator = DividerItemDecorator()
        binding.recyclerView.addItemDecoration(itemDecorator)
        val adapter = HomeAdapter()
//        binding.recyclerView.adapter = adapter
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        homeViewModel.story.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
            Log.d("testo", "setListItem: $adapter")
    }
}