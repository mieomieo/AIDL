package com.example.client

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.client.databinding.ActivityMainBinding
import com.example.client.service.ServiceManager
import com.example.server.IADLStudent

class MainActivity : AppCompatActivity() {
    private var studentManagerService: IADLStudent? = null
    private var serviceBound = false
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val aidlConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.e("Service Status", "service connected")
            studentManagerService = IADLStudent.Stub.asInterface(p1)
            showServiceConnectedDialog(p1)
            ServiceManager.setStudentManager(studentManagerService)
        }
        override fun onServiceDisconnected(p0: ComponentName?) {
            studentManagerService = null
            serviceBound = false
        }
        private fun showServiceConnectedDialog(binder: IBinder?) {
            val dialog = AlertDialog.Builder(this@MainActivity)
                .setTitle("Service Connected")
                .setMessage("Process ID of server : ${binder?.hashCode()}")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .create()

            dialog.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent().apply {
            setClassName(
                "com.example.server",
                "com.example.server.StudentService"
            )
        }
        bindService(intent, aidlConnection, BIND_AUTO_CREATE)
        serviceBound = true

    }

    override fun onDestroy() {
        super.onDestroy()
        // unbindStudentManagerService
        if (serviceBound) {
            unbindService(aidlConnection)
            serviceBound = false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}