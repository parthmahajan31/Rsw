package com.example.joining.Activity

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.joining.R
import kotlin.system.exitProcess

class SplashActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private var runnable: Runnable = Runnable { }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        permissions()
    }

    fun init() {
        handler = Handler()
        runnable = Runnable {
            kotlin.run {
                Intent(this@SplashActivity, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        }
        handler.postDelayed(runnable, 3000)

    }


    private fun permissions() {
        if (ContextCompat.checkSelfPermission(
                this@SplashActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 100
            )
        } else {
            init()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            100 -> {
                var fine: Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED

                if (grantResults.isNotEmpty()) {
//                    Log.i(tag, "Agree microphone permission")
                    init()
                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    OpenSetting()
                } else
//                    Log.i(tag,"Not agree microphone permission")
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ), 100
                    )
            }
        }
    }

    private fun OpenSetting() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@SplashActivity)
        builder.setTitle("Permission")
        builder.setMessage("Permissions are required")
        builder.setPositiveButton("Grant",
            DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(
                    this@SplashActivity,
                    "Go to Settings to Grant the Storage Permissions and restart application",
                    Toast.LENGTH_LONG
                ).show()
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", this@SplashActivity!!.packageName, null)
                intent.data = uri
                startActivity(intent)
                exitProcess(0)
            })
            .create()
            .show()
    }
}