package com.example.sayaradzmb.customs

import android.view.MenuItem
import android.widget.PopupMenu


class MenuClass(private val position: Int) : PopupMenu.OnMenuItemClickListener {

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {

        }
        return false
    }
}