package com.example.sayaradzmb.ui.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.example.sayaradzmb.R

import kotlinx.android.synthetic.main.activity_annonce_offers.*
import kotlinx.android.synthetic.main.content_annonce_offers.*

class AnnonceOffersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annonce_offers)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        expand_btn.setOnClickListener {
            extraInfoLoader()
        }

    }

    fun extraInfoLoader(){
        if (expandable_layout.isExpanded) expandable_layout.collapse()
        else expandable_layout.expand()
    }

}
