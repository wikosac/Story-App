package id.wikosac.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.wikosac.storyapp.R
import id.wikosac.storyapp.api.Story
import id.wikosac.storyapp.databinding.StoryListBinding
import id.wikosac.storyapp.ui.detail.DetailActivity

class HomeAdapter()
    : PagingDataAdapter<Story, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.story_list, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(listStory[position])
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
        Log.d("testo", "onBindViewHolder: $data")
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgStory: ImageView = view.findViewById(R.id.img_story)
        private val nameStory: TextView = view.findViewById(R.id.name_story)
        private val descStory: TextView = view.findViewById(R.id.desc_story)

        fun bind(story: Story) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(imgStory)
            nameStory.text = story.name
            descStory.text = story.description

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("Story", story)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgStory, "imgStory"),
                        Pair(nameStory, "nameStory"),
                        Pair(descStory, "descStory")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}