package com.example.sayaradzmb.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.sayaradzmb.R
import com.example.sayaradzmb.helper.SharedPreferenceInterface
import kotlinx.android.synthetic.main.activity_user_command.*

class UserOffersActivity : AppCompatActivity(), SharedPreferenceInterface{

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_command)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp)

        //pour retourner vers l'activité précédente
        toolbar.setNavigationOnClickListener {
            finish()
        }

        toolbar.title = "Mes Offres"
    }

}