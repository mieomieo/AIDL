package com.example.client.service

import com.example.server.IADLStudent

object ServiceManager {
    private var studentManagerService: IADLStudent? = null

    fun setStudentManager(service: IADLStudent?) {
        studentManagerService = service
    }

    fun getStudentManager(): IADLStudent? {
        return studentManagerService
    }
}