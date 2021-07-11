package com.example.joining.Utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import com.example.joining.R

class Util {
    companion object{
        lateinit var dialogBox : AlertDialog
        fun isNetworkConnected(context: Context): Boolean{
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return cm.activeNetworkInfo !=null
        }

        fun showProgress(activity : Activity, message : String){
            val layoutInflater = LayoutInflater.from(activity)
            val confirmDialog : View = layoutInflater.inflate(R.layout.loading, null)

            dialogBox = AlertDialog.Builder(activity).create()
            dialogBox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBox.setView(confirmDialog)
            dialogBox.setCanceledOnTouchOutside(false)
            dialogBox.show()

        }
        fun dismissDialog(){
            if (dialogBox!=null && dialogBox!!.isShowing){
                dialogBox!!.dismiss()
            }
        }
    }
}