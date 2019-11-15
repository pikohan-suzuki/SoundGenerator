package com.amebaownd.pikohan_nwiatori.soundgenerator

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.amebaownd.pikohan_nwiatori.soundgenerator.util.MyContext
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val RECORD_AUDIO_REQUEST_CODE =101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        setupWithNavController(bottomNavigationView,navController)

        MyContext.onCreateApplication(applicationContext)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)){

            }else{
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.RECORD_AUDIO),RECORD_AUDIO_REQUEST_CODE)
            }
        }
    }
}
