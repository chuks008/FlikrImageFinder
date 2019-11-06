package com.app.flikrsearchdemo.presentation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.app.flikrsearchdemo.R
import com.app.flikrsearchdemo.presentation.photo_search.PhotoSearchActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.photo_detail_screen.*

class PhotoDetailActivity: AppCompatActivity() {

    private val TAG = PhotoDetailActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_detail_screen)

        intent.extras?.run {
            photoDetailTitle.text = getString("title")

            if(getBoolean("from_search")) {
                Glide.with(applicationContext)
                    .load(getString("image_url"))
                    .apply(RequestOptions()
                        .placeholder(resources.getDrawable(R.drawable.placeholder_img)))
                    .centerCrop()
                    .into(photoDetailImageView)
            } else {
                // load from local storage location
            }
        }

        setSupportActionBar(detailScreenToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Back"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}