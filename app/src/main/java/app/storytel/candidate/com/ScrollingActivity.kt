package app.storytel.candidate.com

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.storytel.candidate.com.network.models.Photo
import app.storytel.candidate.com.network.models.Post
import app.storytel.candidate.com.network.models.Result
import com.bumptech.glide.Glide

class ScrollingActivity : AppCompatActivity() {

    private val viewModel: ScrollingActivityViewModel by lazy{
        ViewModelProvider(this).get(ScrollingActivityViewModel::class.java)
    }

    private val toolbar: Toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar)
    }

    private val mRecyclerView: RecyclerView by lazy{
        findViewById<RecyclerView>(R.id.recycler_view)
    }

    private val mPostAdapter: PostAdapter by lazy {
        PostAdapter(onItemClicked = {post, photo ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("postId", post.id)
            intent.putExtra("postTitle", post.title)
            intent.putExtra("postBody", post.body)
            intent.putExtra("photoUrl", photo.url)
            startActivity(intent)
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = manager
        mRecyclerView.adapter = mPostAdapter

        viewModel.getPostsAndImages().observe(this, Observer {
            if (it.loading){
                //TODO("show loading indicator")
            }

            it.result?.let { result ->
                when (result){
                    is Result.Failure -> {
                        displayError(result.error)
                    }
                    is Result.Success -> {
                        mPostAdapter.setData(result.data)
                    }
                }
            }
        })

        viewModel.loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun displayError (error: Throwable?){
        val builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.posts_error_message))
        builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.retry)){ _, _ ->
            viewModel.loadData()
        }

        builder.create().show()
    }
}