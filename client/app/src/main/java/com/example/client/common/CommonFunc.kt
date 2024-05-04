package com.example.client.common

import android.content.Context
import android.widget.EditText
import com.example.client.R

fun areAllFieldsFilled(vararg editTexts: EditText): Boolean {
    return editTexts.all { it.text.isNotBlank() }
}
 fun areScoresValid(context: Context, vararg scoreEditTexts: EditText): Boolean {
    var isValid = true
    for (editText in scoreEditTexts) {
        val score = editText.text.toString().toFloatOrNull()
        if (score == null || score !in 0.0f..10.0f) {
            isValid = false
            editText.error =context.getString(R.string.score_error)
        } else {
            editText.error = null
        }
    }
    return isValid
}