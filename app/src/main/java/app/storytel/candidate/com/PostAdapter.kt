package app.storytel.candidate.com

import android.util.Log
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.lang.Exception
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
            Glide.with(itemView.context)
                    //.load(photo.thumbnailUrl)
                    .load(photo.thumbnailUrl.replace("via.placeholder.com", "dummyimage.com")) //This is just for the purpose of showing images in this task since "via.placeholder" returns 410 error codes. This would never go to production.
                    .listener(object : RequestListener<String, GlideDrawable>{
                override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                    Log.e("PostViewHolder", "Error loading image: ${e?.message}")
                    return true
                }

                override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    return false
                }

            }).into(image)
        }
    }

}