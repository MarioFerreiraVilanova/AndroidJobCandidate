package app.storytel.candidate.com

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DetailsActivity : AppCompatActivity() {
    private val mImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.backdrop)
    }

    private val mTextView: TextView by lazy {
        findViewById<TextView>(R.id.details)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //TODO display the selected post from ScrollingActivity. Use mImageView and mTextView for image and body text. Change the title to use the post title
        //TODO load top 3 comments from COMMENTS_URL into the 3 card views


    }
}