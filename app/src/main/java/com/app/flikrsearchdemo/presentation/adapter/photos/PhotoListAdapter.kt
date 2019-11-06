package com.app.flikrsearchdemo.presentation.adapter.photos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
        return PhotoListViewHolder(itemView, photoConnector, parent.context)
    }

    override fun getItemCount(): Int {
        return photoConnector.getItemCount()
    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        photoConnector.bind(holder, position)
    }

    class PhotoListViewHolder(itemView: View, photoConnector: PhotoConnector, context: Context):
        RecyclerView.ViewHolder(itemView), PhotoRow {

        private val imageTitle: TextView = itemView.findViewById(R.id.imageTitleText)
        private val photoImage: ImageView = itemView.findViewById(R.id.photoImageView)
        private val photoLikeBtn: ImageButton = itemView.findViewById(R.id.bookmarkImageBtn)
        private var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                photoConnector.onSelectItem(currentPosition)
            }

            photoLikeBtn.setOnClickListener {
                AlertDialog.Builder(context).run {
                    setTitle("Adding photo to favorites")
                    setMessage("Would you like to add this photo to your favorites?")
                    setPositiveButton("yes", DialogInterface.OnClickListener {
                            dialogInterface, i ->

                        photoConnector.onBookmarkPhoto(currentPosition)
                        dialogInterface.dismiss()
                    })

                    setNegativeButton("cancel", DialogInterface.OnClickListener {
                            dialogInterface, i ->
                        dialogInterface.dismiss()
                    })

                    show()
                }

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
}