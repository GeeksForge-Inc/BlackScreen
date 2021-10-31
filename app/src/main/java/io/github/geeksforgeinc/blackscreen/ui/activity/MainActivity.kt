package io.github.geeksforgeinc.blackscreen.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import io.github.geeksforgeinc.blackscreen.R
import io.github.geeksforgeinc.blackscreen.databinding.ActivityMainBinding
import io.github.geeksforgeinc.blackscreen.service.FloatService
import io.github.geeksforgeinc.blackscreen.utils.isSilent
import io.github.geeksforgeinc.blackscreen.utils.setChecked
import io.github.geeksforgeinc.blackscreen.viewmodel.BlackScreenViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE = 100
    }
    private lateinit var binding : ActivityMainBinding
    private val viewModel : BlackScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.floatServiceEnabledLiveData.observe(this) {
            binding.button.setChecked(it, true)
        }
        binding.button.setOnCheckedChangeListener { toggleButton, isChecked ->
            if (!toggleButton.isSilent) {
                viewModel.setFloatServiceEnabled(isChecked)
                if (isChecked) {
                    if (requestPermission(REQUEST_CODE)) {
                        return@setOnCheckedChangeListener
                    }
                    FloatService.startService(this)
                } else {
                    FloatService.stopService(this)
                }

            }
        }
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
                            REQUEST_CODE -> FloatService.startService(this)
                        }
                    }
                }, 500)
            }
        }
    }
}