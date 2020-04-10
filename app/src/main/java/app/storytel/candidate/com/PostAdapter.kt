package app.storytel.candidate.com

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.PostAdapter.PostViewHolder
import app.storytel.candidate.com.network.models.Photo
import app.storytel.candidate.com.network.models.Post
import app.storytel.candidate.com.network.models.PostAndImages
import com.bumptech.glide.Glide
import kotlin.random.Random

class PostAdapter(
        private val onItemClicked: ((Post, Photo) -> Unit)
) : RecyclerView.Adapter<PostViewHolder>() {
    private var mData: PostAndImages = PostAndImages(ArrayList(), ArrayList())

    override fun getItemCount() = mData.mPosts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = mData.mPosts[position]
        val photo = mData.mPhotos[Random.nextInt(mData.mPhotos.size - 1)]
        holder.bind(post, photo)
        holder.itemView.setOnClickListener {
            onItemClicked(post, photo)
        }
    }

    fun setData(data: PostAndImages) {
        mData = data
        notifyDataSetChanged()
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val body: TextView = itemView.findViewById(R.id.body)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind (post: Post, photo: Photo){
            title.text = post.title
            body.text = post.body
            Glide.with(itemView.context).load(photo.thumbnailUrl).into(image)
        }
    }

}