package id.wikosac.storyapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.wikosac.storyapp.MainActivity
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        )

        val sharedPreferences = requireActivity().getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val tokenPref = sharedPreferences.getString("TOKEN", "")
        homeViewModel.getStory(tokenPref!!)

        homeViewModel.storyList.observe(viewLifecycleOwner) {
            Log.d(MainActivity.TAG, "onCreateView: ${homeViewModel.storyList.value}")
            if (homeViewModel.storyList.value != null) {
                setItemData(it)
            }
        }

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setItemData(stories: List<Story>) {
        val adapter = HomeAdapter(stories)
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickCallback(object : HomeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
//                showSelected(data.id, data.login, data.avatarUrl)
                Toast.makeText(requireContext(), data.name, Toast.LENGTH_SHORT).show()
            }
        })
    }

//    private fun showSelected(id: Int?, nickname: String, avatarUrl: String) {
//        val moveWithObjectIntent = Intent(requireActivity(), DetailActivity::class.java)
//        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_ID, id)
//        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_ITEM, nickname)
//        moveWithObjectIntent.putExtra(DetailActivity.EXTRA_AVA, avatarUrl)
//        startActivity(moveWithObjectIntent)
//    }
}