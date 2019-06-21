package com.example.sayaradzmb.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.sayaradzmb.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /**
         *  le code qui permet de passer a login activity
         */
        val handler = Handler()
        handler.postDelayed({
            // do something
            val intent = Intent(this@MainActivity, AcuilleActivity::class.java)
            // If you just use this that is not a valid context. Use ActivityName.this
            startActivity(intent)
            this@MainActivity.finish()
        }, 3000)


    }
}