package com.example.jiokyc.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.jiokyc.MyService
import com.example.jiokyc.MyWorker
import com.example.jiokyc.R
import com.example.jiokyc.utlis.Constants.Companion.KYC_LINK
import com.example.jiokyc.utlis.ShareUtil
import java.util.concurrent.TimeUnit


class KycSuccessActivity : AppCompatActivity() {
    var btn_kyc_done: Button? = null
    private val TAG = "KycSuccessActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kyc_success)

        btn_kyc_done = findViewById(R.id.btn_kyc_done)
        stopService()

        btn_kyc_done!!.setOnClickListener {
            intent=Intent(this,MainActivity::class.java)
            ShareUtil.saveToPrefs(
                this@KycSuccessActivity,
                ShareUtil.REQUEST_KEY,
                ""
            )

            finish()
        }
    }



    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        stopService()
        super.onDestroy()
    }

    fun startService() {
        Log.d(TAG, "startService called")
        if (!MyService.isServiceRunning) {
            val serviceIntent = Intent(this, MyService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)
        }
    }

    fun stopService() {
        Log.d(TAG, "stopService called")
        if (MyService.isServiceRunning) {
            val serviceIntent = Intent(this, MyService::class.java)
            stopService(serviceIntent)
        }
    }

    fun startServiceViaWorker() {
        Log.d(TAG, "startServiceViaWorker called")
        val UNIQUE_WORK_NAME = "StartMyServiceViaWorker"
        val workManager = WorkManager.getInstance(this)

        // As per Documentation: The minimum repeat interval that can be defined is 15 minutes
        // (same as the JobScheduler API), but in practice 15 doesn't work. Using 16 here
        val request = PeriodicWorkRequest.Builder(
            MyWorker::class.java,
            1,
            TimeUnit.MINUTES
        )
            .build()

        // to schedule a unique work, no matter how many times app is opened i.e. startServiceViaWorker gets called
        // do check for AutoStart permission
        workManager.enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

}