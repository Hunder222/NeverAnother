package com.example.neveranother.supabase

import android.net.http.HttpResponseCache.install
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.util.CoilUtils.result
import com.example.neveranother.components.NAHeader1
import com.example.neveranother.components.NAHeader2
import com.example.neveranother.history.HistoryCard
import com.example.neveranother.ui.theme.NAbackgroundColor
import com.example.neveranother.ui.theme.NAboxColor
import com.example.neveranother.viewmodels.Measurement
import com.example.neveranother.viewmodels.NAViewModel
import com.google.common.math.LinearTransformation.vertical
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Supabase(
    NAViewModel: NAViewModel
){
    var localMeasurements = NAViewModel.savedMeasurements.lastOrNull()

    val coRutineScope = rememberCoroutineScope()

    var refreshTrigger by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 32.dp, vertical = 32.dp),
    ) {
        NAHeader1("Data")

        Spacer(Modifier.height(30.dp))

        NAHeader2("Local Measurements")

        if (localMeasurements != null){
            HistoryCard(localMeasurements)
            Button(
                onClick = {
                    coRutineScope.launch { saveDateToSupabase(localMeasurements) }
                }
            ) { Text("Gem mål i skyen") }
        } else {
            Spacer(Modifier.height(10.dp))
            Text("Tag mål med manuel eller 3D scanner for at gemme mål i skyen", fontWeight = FontWeight.Bold)
        }


        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {refreshTrigger++}
        ) { Text("Genindlæs") }

        NAHeader2("Supabase measurements")
        ShowDataFromSupabase(refreshTrigger)
    }
}



// supabaseKey er en "anon key". RLS er tilføjet og tillader kun select og insert på tabellen.
val supabase = createSupabaseClient(
    supabaseUrl = "https://okxsrjudyndvisgktoek.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im9reHNyanVkeW5kdmlzZ2t0b2VrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzk5Mzk2OTEsImV4cCI6MjA5NTUxNTY5MX0.DPbiJBjOTdMof4lv4mFDB0CnMJWf0vKsBV0u99SKPlI"
) {
    install(Auth)
    install(Postgrest)
}

@Composable
fun ShowDataFromSupabase( refreshTrigger: Int ){
    var supabaseMeasurements by remember { mutableStateOf<List<Measurement>>(emptyList()) }
    // LaunchedEffect tillader async kode som fetch, at kører i en Composable fun
    LaunchedEffect(refreshTrigger) {
        try {
            withContext(Dispatchers.IO) {
                val results = supabase
                    .from("Measurements")
                    .select{
                        order("id", Order.DESCENDING)
                    }
                    .decodeList<Measurement>()
                supabaseMeasurements = results
            }
        } catch (e: Exception) { e.printStackTrace() }
    }
    // Når dataen fra supabase er hentet, lav en lazyColumn hvor de alle renderes.
    if (supabaseMeasurements.isNotEmpty()) {
        Text("Antal gemte measurements: ${supabaseMeasurements.size}")
        // Laver bunden af skærmen til en scrollable column, og rendere et HistoryCard for hver measurement fetched
        LazyColumn() {
            items(supabaseMeasurements) { measurement ->
                HistoryCard(measurement)
            }
        }
    }
}

// async function, bliver kørt i en CoRutine scope, kørt af en Button på linje 73
suspend fun saveDateToSupabase( measurement: Measurement ){
    try {
        withContext(Dispatchers.IO) {
            supabase.from("Measurements").insert(measurement)
        }
    } catch (e: Exception) { e.printStackTrace() }
}