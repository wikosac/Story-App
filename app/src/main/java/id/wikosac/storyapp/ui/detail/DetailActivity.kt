package id.wikosac.storyapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.wikosac.storyapp.R
import id.wikosac.storyapp.databinding.ActivityDetailBinding
import id.wikosac.storyapp.databinding.ActivityMainBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val img = intent.getStringExtra("IMG")
        val name = intent.getStringExtra("NAME")
        val desc = intent.getStringExtra("DESC")
        val time = intent.getStringExtra("TIME")

        with(binding) {
            Glide.with(imgStory.context)
                .load(img)
                .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher).override(200,200))
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(imgStory)
            nameStory.text = name
            descStory.text = desc
            timeStory.text = time
        }
    }
}