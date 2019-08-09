package com.example.sayaradzmb.helper

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.example.sayaradzmb.R
import com.example.sayaradzmb.ui.activities.fragments.ListeVoitureCommande
import java.util.*

class FragmentHelper {
    companion object{
        fun changeFragment(data : Parcelable,activity : FragmentActivity,nextFragment : Fragment,tagData : String,tagFragment : String,idFragment : Int){
            val bundel = Bundle()
            bundel.putParcelable(tagData,data)
            nextFragment.arguments = bundel
            activity!!.supportFragmentManager.beginTransaction()
                .replace(idFragment,nextFragment,tagFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}