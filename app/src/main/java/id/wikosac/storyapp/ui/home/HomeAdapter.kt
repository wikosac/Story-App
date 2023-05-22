package id.wikosac.storyapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.wikosac.storyapp.R
import id.wikosac.storyapp.api.Story

class HomeAdapter(
    private val listStory: List<Story>
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.img_story)
        val name: TextView = view.findViewById(R.id.name_story)
        val desc: TextView = view.findViewById(R.id.desc_story)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.story_list, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = listStory[position].name
        val desc = listStory[position].description
        val img = listStory[position].photoUrl
        Glide.with(holder.itemView.context)
            .load(img)
            .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher).override(200,200))
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(holder.avatar)
        holder.name.text = name
        holder.desc.text = desc

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listStory[holder.adapterPosition])
        }
    }

    override fun getItemCount() = listStory.size
}