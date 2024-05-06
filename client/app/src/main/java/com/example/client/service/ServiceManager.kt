package com.example.client.service

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.server.IADLStudent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) : ServiceConnection {
    private var service: IADLStudent? = null
    val serviceConnected = MutableLiveData<Boolean>(false)

    init {
        connect()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val processId = service?.hashCode() ?: "Unknown"
        Log.d("ServiceConnection", "onServiceConnected: $service")
        Toast.makeText(context, "Process ID of server : $processId", Toast.LENGTH_SHORT).show()
        this.service = IADLStudent.Stub.asInterface(service)
        serviceConnected.value = true
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        serviceConnected.value = false
        service = null
    }

    fun getStudentService(): IADLStudent? = service
    private fun connect() {
        context.bindService(
            Intent().apply {
                setClassName(
                    "com.example.server",
                    "com.example.server.StudentService"
                )
            }, this, Context.BIND_AUTO_CREATE
        )
    }
}