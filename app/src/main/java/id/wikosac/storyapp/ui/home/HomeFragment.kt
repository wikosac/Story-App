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

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemDecorator = DividerItemDecorator()
        binding.recyclerView.addItemDecoration(
            itemDecorator
//            DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        )

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
        val adapter = HomeAdapter(stories)
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickCallback(object : HomeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                detail(data.photoUrl, data.name, data.description, data.createdAt)
//                Toast.makeText(requireContext(), data.name, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun detail(img: String?, name: String?, desc: String?, time: String?) {
        val imgStory: ImageView = requireActivity().findViewById(R.id.img_story)
        val nameStory: TextView = requireActivity().findViewById(R.id.name_story)
        val descStory: TextView = requireActivity().findViewById(R.id.desc_story)

        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra("IMG", img)
        intent.putExtra("NAME", name)
        intent.putExtra("DESC", desc)
        intent.putExtra("TIME", time)

        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                Pair(imgStory, "imgStory"),
                Pair(nameStory, "nameStory"),
                Pair(descStory, "descStory")
            )
        startActivity(intent, optionsCompat.toBundle())
        Log.d("anim", "detail: ${optionsCompat.toBundle().toString()}")
    }
}