package com.example.jiokyc.utlis

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.provider.Settings

import java.io.File


object ShareUtil {
    val BasicAuth="BASIC_AUTH"
    val GOOGLE_PLUS_SHARE_REQUEST = 1
    val AccessToken = "AccessToken"
    val REQUEST_KEY = "REQUEST_KEY"
    val USER_NAME = "USER_NAME"
    val KEY_loginstatus = "loginstatus"
    val FIRST_OPEN="FIRST_OPEN"
    val USER_EMAILID = "USER_EMAILID"
    val USERMOBILE_NO = "USERMOBILE_NO"
    val isLogin= false

    val ADDRESS = "ADDRESS"
    val CITY = "CITY"


    val Declamer = "Declamer"
    val Campaigns_Count = "Campaigns_Count"


    fun saveToPrefs(context: Context, key: String, value: String): String {
        if (key.equals("customerMasterId", ignoreCase = true)) {
        }
        val prefs = PreferenceManager
            .getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
        return key
    }



    fun getFromPrefs(context: Context, key: String): String? {
        val sharedPrefs = PreferenceManager
            .getDefaultSharedPreferences(context)
        return try {
            sharedPrefs.getString(key, "")

        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

    }

    fun callDefaultShare(activity: Activity, sharingText: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sharingText)
        activity.startActivity(Intent.createChooser(sharingIntent, "Share using"))
    }




    @SuppressLint("HardwareIds")
    fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }


    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @JvmStatic
    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            return dir.delete()
        } else return if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }
}
