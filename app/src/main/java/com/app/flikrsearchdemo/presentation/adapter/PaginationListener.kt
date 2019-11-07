package com.app.flikrsearchdemo.presentation.adapter

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationListener(private val layoutManager: LinearLayoutManager,
                         private val pageSize: Int): RecyclerView.OnScrollListener() {

    private val TAG = PaginationListener::class.java.simpleName

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if(!isLoading() && !isLastPage()) {
            if((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount < pageSize) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun isLoading(): Boolean
    protected abstract fun isLastPage(): Boolean
    protected abstract fun loadMoreItems()



}