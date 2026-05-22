package com.example.neveranother

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.Navigator
import com.example.neveranother.ui.theme.NeverAnotherTheme
import com.example.neveranother.R
import com.example.neveranother.navigator.Navigatior
import com.example.neveranother.ui.theme.NAbackgroundColor
import com.example.neveranother.viewmodels.NAViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NeverAnotherTheme {
                // add background to entire app
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = NAbackgroundColor
                ) {
                    // init our never another viewmodel
                    val naViewModel: NAViewModel = viewModel()
                    Column(
                        // padding on app, so within status bar and android navigation bar
                        modifier = Modifier.systemBarsPadding()
                    ) {
                        Navigatior(naViewModel)
                    }
                }
            }
        }
    }
}