package app.storytel.candidate.com

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        PostAdapter(Glide.with(this), this)
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
                        TODO("show error")
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
}