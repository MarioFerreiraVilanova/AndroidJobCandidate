package app.storytel.candidate.com.network.models

import app.storytel.candidate.com.network.models.Photo
import app.storytel.candidate.com.network.models.Post

data class PostAndImages(
        var mPosts: List<Post>,
        var mPhotos: List<Photo>
)