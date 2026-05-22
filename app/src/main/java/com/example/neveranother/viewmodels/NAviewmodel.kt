package com.example.neveranother.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Measurement(
    val upperCircumference: String,
    val lowerCircumference: String,
    val chestVolume: String,
    val chestWidth: String,
    val chestHeight: String,
    val date: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
)

class NAviewmodel: ViewModel() {
    val userName = "Anette"

    // Measurements
    var upperCircumference by mutableStateOf("")
    var lowerCircumference by mutableStateOf("")
    var chestVolume by mutableStateOf("")
    var chestWidth by mutableStateOf("")
    var chestHeight by mutableStateOf("")

    // Video Sound State
    var isVideoMuted by mutableStateOf(true)

    // List to save completed measurements
    val savedMeasurements = mutableStateListOf<Measurement>()

    fun saveCurrentMeasurements() {
        val measurement = Measurement(
            upperCircumference = upperCircumference,
            lowerCircumference = lowerCircumference,
            chestVolume = chestVolume,
            chestWidth = chestWidth,
            chestHeight = chestHeight
        )
        savedMeasurements.add(measurement)
    }
}
