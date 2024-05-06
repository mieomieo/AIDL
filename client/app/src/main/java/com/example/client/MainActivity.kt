package com.example.client

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.client.databinding.ActivityMainBinding
import com.example.client.viewmodel.StudentViewModel
import com.example.server.IADLStudent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel: StudentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onSupportNavigateUp(): Boolean {
        var navigateUpNeeded = true

        viewModel.checkService.observeForever { isConnected ->
            val dialog = android.app.AlertDialog.Builder(this)
                .setTitle("Service not connected")
                .setMessage("Service đã sập. Thử lại sau !!")
                .create()

            if (!isConnected) {
                dialog.show()
                navigateUpNeeded = false
            } else {
                dialog.dismiss()
                navigateUpNeeded = true
            }
        }

        if (navigateUpNeeded) {
            navController = findNavController(R.id.fragmentContainerView)
            return navController.navigateUp() || super.onSupportNavigateUp()
        }

        return false
    }
}