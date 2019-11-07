package com.app.flikrsearchdemo.presentation.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.flikrsearchdemo.R
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoConnector
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoRow
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Your name on 2019-11-07.
 */
abstract class BasePhotoViewHolder(itemView: View,
                                   private val photoConnector: PhotoConnector,
                                   private val context: Context): RecyclerView.ViewHolder(itemView), PhotoRow {

    private val imageTitle: TextView = itemView.findViewById(R.id.imageTitleText)
    protected val photoImage: ImageView = itemView.findViewById(R.id.photoImageView)
    protected val photoActionBtn: ImageButton = itemView.findViewById(R.id.bookmarkImageBtn)
    protected var currentPosition: Int = 0

    init {
        itemView.setOnClickListener {
            photoConnector.onSelectItem(currentPosition)
        }
    }

    override fun setTitle(title: String) {
        imageTitle.text = title
    }

    override fun setImage(imageUrl: String) {
        Glide.with(photoImage.context)
            .load(imageUrl)
            .apply(RequestOptions().placeholder(R.drawable.placeholder_img))
            .centerCrop()
            .into(photoImage)
    }

    override fun setPosition(position: Int) {
        currentPosition = position
    }
}