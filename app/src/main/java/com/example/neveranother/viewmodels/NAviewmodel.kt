package com.example.neveranother.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Init af Elliot, Measurement class, var´s og function af victor, data class serialized af Elliot.

@Serializable
data class Measurement(
    val upperCircumference: String,
    val lowerCircumference: String,
    val chestVolume: String,
    val chestWidth: String,
    val chestHeight: String,
    val date: String
)

class NAViewModel: ViewModel() {
    val userName = "Anette"

    // Measurements
    var upperCircumference by mutableStateOf("")
    var lowerCircumference by mutableStateOf("")
    var chestVolume by mutableStateOf("")
    var chestWidth by mutableStateOf("")
    var chestHeight by mutableStateOf("")

    // List to save completed measurements
    val savedMeasurements = mutableStateListOf<Measurement>()

    fun saveCurrentMeasurements() {
        val measurement = Measurement(
            upperCircumference = upperCircumference,
            lowerCircumference = lowerCircumference,
            chestVolume = chestVolume,
            chestWidth = chestWidth,
            chestHeight = chestHeight,
            date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        )
        savedMeasurements.add(measurement)
    }
}
