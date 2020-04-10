package app.storytel.candidate.com;

import java.util.List;

import app.storytel.candidate.com.network.models.Photo;
import app.storytel.candidate.com.network.models.Post;

public class PostAndImages {
    public List<Post> mPosts;
    public List<Photo> mPhotos;

    public PostAndImages(List<Post> post, List<Photo> photos) {
        mPosts = post;
        mPhotos = photos;
    }
}
