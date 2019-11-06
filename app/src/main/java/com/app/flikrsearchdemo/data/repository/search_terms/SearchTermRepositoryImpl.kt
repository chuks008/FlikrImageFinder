package com.app.flikrsearchdemo.data.repository.search_terms

import android.content.SharedPreferences
import android.util.ArraySet
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

class SearchTermRepositoryImpl @Inject constructor(private val sharedPreferences: SharedPreferences,
                                                   private val gson: Gson): SearchTermRepository {

    private val TAG = SearchTermRepositoryImpl::class.java.simpleName
    private var searchTerms = LinkedList<String>()

    init {
        Log.e(TAG, "Initializing terms")
        val searchTermsString = sharedPreferences.getString("search_term_string", null)
        if(searchTermsString != null) {
            searchTerms = gson.fromJson(searchTermsString, object: TypeToken<LinkedList<String>>(){}.type)
        }
    }

    override fun getSearchTerms(): List<String> {
        return searchTerms.toList()
    }

    override fun addNewSearchTerm(searchTerm: String) {

        Log.e(TAG, "Adding term to list: $searchTerm")

        if(!searchTerms.contains(searchTerm)) {
            if(searchTerms.size == 30) {
                searchTerms.pollLast()
            }

            searchTerms.addFirst(searchTerm)
        }
    }

    override fun saveSearchTerms() {

        Log.e(TAG, "Saving terms to shared pref: Total = ${searchTerms.size}")
        sharedPreferences.edit().run {
            putString("search_term_string", gson.toJson(searchTerms))
            apply()
        }
    }
}