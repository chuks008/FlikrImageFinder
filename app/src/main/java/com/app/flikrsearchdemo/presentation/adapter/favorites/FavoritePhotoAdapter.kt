package com.app.flikrsearchdemo.presentation.adapter.favorites

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.flikrsearchdemo.R
import com.app.flikrsearchdemo.presentation.adapter.BasePhotoViewHolder
import com.app.flikrsearchdemo.presentation.adapter.photos.PhotoConnector

/**
 * Created by Your name on 2019-11-07.
 */
class FavoritePhotoListAdapter(private val photoConnector: PhotoConnector):
    RecyclerView.Adapter<FavoritePhotoListAdapter.FavoritePhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePhotoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.favorite_gallery_item, parent, false)
        return FavoritePhotoViewHolder(itemView, photoConnector, parent.context)
    }

    override fun getItemCount(): Int {
        return photoConnector.getItemCount()
    }

    override fun onBindViewHolder(holder: FavoritePhotoViewHolder, position: Int) {
        photoConnector.bind(holder, position)
    }

    class FavoritePhotoViewHolder(itemView: View, photoConnector: PhotoConnector, context: Context):
        BasePhotoViewHolder(itemView, photoConnector, context) {

        init {

            photoActionBtn.setOnClickListener {
                AlertDialog.Builder(context).run {
                    setTitle("Deleting photo from favorites")
                    setMessage("Would you like to delete this photo from your favorites?")
                    setPositiveButton("yes") {
                            dialogInterface, i ->

                        photoConnector.onActionPerformed(currentPosition)
                        dialogInterface.dismiss()
                    }

                    setNegativeButton("cancel") { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }

                    show()
                }
            }
        }
    }

}
