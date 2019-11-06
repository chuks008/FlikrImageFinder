package com.app.flikrsearchdemo.presentation.adapter.search_term

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.flikrsearchdemo.R

class SearchTermAdapter(private val presenter: SearchTermConnector):
    RecyclerView.Adapter<SearchTermAdapter.SearchTermViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTermViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.search_terms_view, parent, false)

        return SearchTermViewHolder(itemView, presenter)
    }

    override fun getItemCount(): Int {
        return presenter.getCount()
    }

    override fun onBindViewHolder(holder: SearchTermViewHolder, position: Int) {
        presenter.bind(holder, position)
    }

    class SearchTermViewHolder(itemView: View,
                               presenter: SearchTermConnector):
        RecyclerView.ViewHolder(itemView), SearchTermRow {

        val searchTermTextView = itemView.findViewById<TextView>(R.id.pastSearchTermText)
        var currentPosition = 0

        init {
            itemView.setOnClickListener {
                presenter.onSelectSearchTerm(currentPosition)
            }
        }

        override fun setSearchText(tag: String) {
            searchTermTextView.text = tag
        }

        override fun setPosition(position: Int) {
            currentPosition = position
        }
    }
}