package com.tjtjrb.fullscreenintent

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.tjtjrb.fullscreenintent.Constants.CHANNEL_ID
import kotlinx.android.synthetic.main.fragment_root.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNotificationChannel()

        Log.d("FULL_SCREEN", "Intent in main activity was: $intent")
        if (intent != null && intent.dataString?.contains("From-Service") == true) {
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .add(R.id.container, FromServiceFragment(), "fromService")
                .commit()
        } else {
            addRootFragment()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("FULL_SCREEN", "onNewIntent - Intent in main activity was: $intent")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            return
        }
        getSystemService<NotificationManager>()!!.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "I Will Full Screen", NotificationManager.IMPORTANCE_HIGH)
        )
    }

    fun returnToRoot() {
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }

        addRootFragment()
    }

    private fun addRootFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, RootFragment(), "root")
            .commit()
    }
}
