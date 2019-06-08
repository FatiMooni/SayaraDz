package com.example.sayaradzmb.helper

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Filterable
import android.widget.SearchView
import com.example.sayaradzmb.R

interface SearchViewInterface {

    public fun initSearchView(activity : FragmentActivity, v : View, adapter : Filterable, id : Int){
        //essai
        // Associate searchable configuration with the SearchView
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager?
        val searchView = v.findViewById<SearchView>(id)
        if (v == null) Log.i("id null","3Lacjjjjjjjjj")
        searchView!!.setSearchableInfo(
            searchManager!!
                .getSearchableInfo(activity!!.getComponentName())
        )
        searchView!!.setMaxWidth(Integer.MAX_VALUE)

        // listening to search query text change
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                adapter!!.getFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                adapter!!.getFilter().filter(query)
                return false
            }
        })
    }

}