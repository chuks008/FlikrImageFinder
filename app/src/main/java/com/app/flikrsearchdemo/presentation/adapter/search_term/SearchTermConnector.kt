package com.app.flikrsearchdemo.presentation.adapter.search_term

interface SearchTermConnector {

    fun bind(searchTermRow: SearchTermRow, position: Int)
    fun getCount(): Int
    fun onSelectSearchTerm(position: Int)
}