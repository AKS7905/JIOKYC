package com.example.jiokyc

import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker
import com.example.jiokyc.MyService
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.work.Worker
import com.example.jiokyc.api.*
import com.example.jiokyc.api.kycLinkResponce.AuthResponceModel
import com.example.jiokyc.utlis.Constants
import retrofit2.Call

class MyWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {
    private val TAG = "MyWorker"
    override fun doWork(): Result {
        Log.d(TAG, "doWork called for: " + this.id)
        Log.d(TAG, "Service Running: " + MyService.isServiceRunning)
        if (!MyService.isServiceRunning) {
            Log.d(TAG, "starting service from doWork")
            val intent = Intent(context, MyService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }
        return Result.success()
    }

    override fun onStopped() {
        Log.d(TAG, "onStopped called for: " + this.id)
        super.onStopped()
    }


}