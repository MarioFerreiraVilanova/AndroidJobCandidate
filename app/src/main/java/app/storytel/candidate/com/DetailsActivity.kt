package app.storytel.candidate.com

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.storytel.candidate.com.network.models.Comment
import app.storytel.candidate.com.network.models.Result
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_details.*
import java.lang.Exception

class DetailsActivity : AppCompatActivity() {
    private val mImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.backdrop)
    }

    private val mTextView: TextView by lazy {
        findViewById<TextView>(R.id.details)
    }

    private val mViewModel: DetailsActivityViewModel by lazy {
        ViewModelProvider(this).get(DetailsActivityViewModel::class.java)
    }

    //I would normaly make the comments a RecyclerView and populate them as a list instead of having
    // all these variables here.

    private val mCommentTitle1: TextView by lazy {
        findViewById<TextView>(R.id.title1)
    }

    private val mCommentTitle2: TextView by lazy {
        findViewById<TextView>(R.id.title2)
    }

    private val mCommentTitle3: TextView by lazy {
        findViewById<TextView>(R.id.title3)
    }

    private val mDescription1: TextView by lazy {
        findViewById<TextView>(R.id.description1)
    }

    private val mDescription2: TextView by lazy {
        findViewById<TextView>(R.id.description2)
    }

    private val mDescription3: TextView by lazy {
        findViewById<TextView>(R.id.description3)
    }

    private val mComment1: View by lazy {
        findViewById<View>(R.id.comment1)
    }

    private val mComment2: View by lazy {
        findViewById<View>(R.id.comment2)
    }

    private val mComment3: View by lazy {
        findViewById<View>(R.id.comment3)
    }

    private val mLoadingCommentsView: View by lazy {
        findViewById<View>(R.id.loading_comments_text)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //TODO display the selected post from ScrollingActivity. Use mImageView and mTextView for image and body text. Change the title to use the post title
        //TODO load top 3 comments from COMMENTS_URL into the 3 card views

        mViewModel.init(intent)

        Glide.with(this)
                //.load(mViewModel.photoUrl) // This should be the line to load images. The following line is just for the purpose of this task
                .load(mViewModel.photoUrl?.replace("via.placeholder.com", "dummyimage.com")) //This is just for the purpose of showing images in this task since "via.placeholder" returns 410 error codes. This would never go to production.
                .listener(object : RequestListener<String, GlideDrawable> {
            override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                Log.e("PostViewHolder", "Error loading image: ${e?.message}")
                return true
            }

            override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                return false
            }

        }).into(mImageView)
        mTextView.text = mViewModel.postBody
        supportActionBar?.title = mViewModel.postTitle

        mViewModel.getComments().observe(this, Observer {
            showLoadingIndicator (it.loading)

            when (val result = it.result){
                is Result.Failure -> {displayError(result.error)}
                is Result.Success -> {showComments(result.data)}
            }
        })

    }

    private fun showComments (comments: List<Comment>){
        mComment1.visibility = if (comments.isNotEmpty()) VISIBLE else GONE
        mComment2.visibility = if (comments.size > 1) VISIBLE else GONE
        mComment3.visibility = if (comments.size > 2) VISIBLE else GONE

        if (comments.isNotEmpty()){
            mCommentTitle1.text = comments[0].name
            mDescription1.text = comments[0].body
            if (comments.size > 1){
                mCommentTitle2.text = comments[1].name
                mDescription2.text = comments[1].name
                if (comments.size > 2){
                    mCommentTitle3.text = comments[2].name
                    mDescription3.text = comments[2].name
                }
            }
        }else{
            //TODO There are no comments. As it is right now, no comment cards will be shown. Maybe we should display some kind of view indicating that there are no comments available
        }
    }

    private fun showLoadingIndicator (show: Boolean){
        if (show){
            comment1.visibility = GONE
            comment2.visibility = GONE
            comment3.visibility = GONE

            mLoadingCommentsView.visibility = VISIBLE
        }else{
            mLoadingCommentsView.visibility = GONE
        }
    }

    private fun displayError (error: Throwable?){
        val builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.comments_error_message))
        builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.retry)){ _, _ ->
            mViewModel.postId?.let {
                mViewModel.fetchComments(it)
            } ?: run {
                Log.e("DetailsActivity", "Details activity is running without a post id. This should never happen")
            }
        }

        builder.create().show()
    }
}