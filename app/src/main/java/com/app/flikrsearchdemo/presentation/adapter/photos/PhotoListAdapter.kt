package com.app.flikrsearchdemo.presentation.adapter.photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.flikrsearchdemo.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Your name on 2019-11-06.
 */
class PhotoListAdapter(private val photoConnector: PhotoConnector):
    RecyclerView.Adapter<PhotoListAdapter.PhotoListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.search_gallery_item, parent, false)
        return PhotoListViewHolder(itemView, photoConnector)
    }

    override fun getItemCount(): Int {
        return photoConnector.getItemCount()
    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        photoConnector.bind(holder, position)
    }

    class PhotoListViewHolder(itemView: View, listener: PhotoConnector):
        RecyclerView.ViewHolder(itemView), PhotoRow {

        val imageTitle = itemView.findViewById<TextView>(R.id.imageTitleText)
        val photoImage = itemView.findViewById<ImageView>(R.id.photoImageView)
        val photoLikeBtn = itemView.findViewById<ImageButton>(R.id.bookmarkImageBtn)
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                listener.onSelectItem(currentPosition)
            }
        }

        override fun setTitle(title: String) {
            imageTitle.text = title
        }

        override fun setImage(imageUrl: String) {
            Glide.with(photoImage.context)
                .load(imageUrl)
                .apply(RequestOptions().placeholder(R.drawable.placeholder_img))
                .into(photoImage)
        }

        override fun setPosition(position: Int) {
            currentPosition = position
        }
    }
}