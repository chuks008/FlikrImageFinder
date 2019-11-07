package com.app.flikrsearchdemo.presentation.adapter.photos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.flikrsearchdemo.R
import com.app.flikrsearchdemo.presentation.adapter.BasePhotoViewHolder
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
        BasePhotoViewHolder(itemView, photoConnector, context) {

        init {
            photoActionBtn.setOnClickListener {
                AlertDialog.Builder(context).run {
                    setTitle("Adding photo to favorites")
                    setMessage("Would you like to add this photo to your favorites?")
                    setPositiveButton("yes", DialogInterface.OnClickListener {
                            dialogInterface, i ->

                        photoConnector.onActionPerformed(currentPosition)
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
    }

}