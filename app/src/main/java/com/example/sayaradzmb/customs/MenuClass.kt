package com.example.sayaradzmb.customs

import android.R
import android.widget.Toast
import android.view.MenuItem
import android.widget.PopupMenu

class MenuClass(private val position: Int) : PopupMenu.OnMenuItemClickListener {

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {

          // R.id.edit_annonce -> return true
        }
        return false
    }
}