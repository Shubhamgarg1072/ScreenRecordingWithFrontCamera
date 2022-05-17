package com.ibashkimi.screenrecorder

import android.Manifest
import android.app.PictureInPictureParams
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.Display
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.ibashkimi.screenrecorder.settings.PreferenceHelper
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            onFirstCreate()
        }
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.main_nav_host_fragment)

        findViewById<Toolbar?>(R.id.toolbar)?.let {
            setSupportActionBar(it)
            val appBarConfiguration = AppBarConfiguration(
                setOf(R.id.home, R.id.navigation_dialog, R.id.more_settings_dialog)
            )
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun openPipMode() {

        val d: Display = windowManager
            .defaultDisplay
        val p = Point()
        d.getSize(p)
        val width: Int = p.x
        val height: Int = p.y

        val ratio = Rational(width, height)
        val pip_Builder = PictureInPictureParams.Builder()
        pip_Builder.setAspectRatio(ratio).build()
        enterPictureInPictureMode(pip_Builder.build())
    }


    /**
     * Called the first time the activity is created.
     */
    private fun onFirstCreate() {
        PreferenceHelper(this).apply {
            // Apply theme before onCreate
            applyNightMode(nightMode)
            initIfFirstTimeAnd {
                createNotificationChannels()
            }
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    companion object {
        const val ACTION_TOGGLE_RECORDING = "com.ibashkimi.screenrecorder.TOGGLE_RECORDING"
    }


}