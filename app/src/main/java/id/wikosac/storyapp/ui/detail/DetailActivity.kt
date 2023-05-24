package id.wikosac.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.wikosac.storyapp.R
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.databinding.ActivityDetailBinding
import id.wikosac.storyapp.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<Story>("Story") as Story

        with(binding) {
            Glide.with(imgStory.context)
                .load(story.photoUrl)
                .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher).override(200,200))
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(imgStory)
            nameStory.text = story.name
            descStory.text = story.description
            timeStory.text = story.createdAt
        }
    }
}