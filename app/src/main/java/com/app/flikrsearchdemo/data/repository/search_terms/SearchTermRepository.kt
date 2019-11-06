package com.app.flikrsearchdemo.data.repository.search_terms

interface SearchTermRepository {

    fun getSearchTerms(): List<String>
    fun addNewSearchTerm(searchTerm: String)
    fun saveSearchTerms()
}