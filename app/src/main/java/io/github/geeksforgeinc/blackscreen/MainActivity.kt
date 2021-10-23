package io.github.geeksforgeinc.blackscreen

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import io.github.geeksforgeinc.blackscreen.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE = 100
    }
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.button.setOnClickListener{
            if (requestPermission(REQUEST_CODE)) {
                return@setOnClickListener
            }
            showServiceFloat()
        }
    }

    private fun showServiceFloat() {
        ContextCompat.startForegroundService(this, Intent(this, FloatService::class.java))
    }

    private fun requestPermission(requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, requestCode)
                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requestCode == REQUEST_CODE) {
                Handler().postDelayed({
                    if (Settings.canDrawOverlays(this)) {
                        when (requestCode) {
                            REQUEST_CODE -> showServiceFloat()
                        }
                    }
                }, 500)
            }
        }
    }
}