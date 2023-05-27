package id.wikosac.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.wikosac.storyapp.databinding.ActivityMain2Binding
import id.wikosac.storyapp.databinding.ActivityMainBinding
import id.wikosac.storyapp.ui.custom.DividerItemDecorator
import id.wikosac.storyapp.ui.home.HomeAdapter
import id.wikosac.storyapp.ui.home.HomeViewModel
import id.wikosac.storyapp.ui.home.LoadingStateAdapter
import id.wikosac.storyapp.ui.home.ViewModelFactory

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setListItem()
    }

    private fun setListItem() {
        binding.rv.layoutManager = LinearLayoutManager(this)
        val itemDecorator = DividerItemDecorator()
        binding.rv.addItemDecoration(itemDecorator)
        val adapter = QuoteListAdapter()
//        binding.recyclerView.adapter = adapter
        binding.rv.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        homeViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}