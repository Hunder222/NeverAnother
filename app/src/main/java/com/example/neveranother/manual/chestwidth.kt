package com.example.neveranother.manual

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neveranother.R
import com.example.neveranother.ui.theme.NAtextBlack
import com.example.neveranother.ui.theme.NAwarmGrey
import com.example.neveranother.ui.theme.NohemiFontFamily
import com.example.neveranother.viewmodels.NAviewmodel

@Composable
fun ChestWidth(
    viewModel: NAviewmodel,
    onBack: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("Visuel") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ManualProgressBar(currentStepIndex = 3)

        // Title
        Text(
            text = "Brystbredde",
            fontFamily = NohemiFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = NAtextBlack,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image/Video Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(NAwarmGrey),
                contentAlignment = Alignment.Center
            ) {
                if (selectedTab == "Visuel") {
                    Image(
                        painter = painterResource(id = R.drawable.chestwidth),
                        contentDescription = "Brystbredde Illustration",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    VideoPlayer(
                        videoResId = R.raw.video_3,
                        isMuted = viewModel.isVideoMuted,
                        onMuteChange = { viewModel.isVideoMuted = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            VisualVideoToggle(selectedTab = selectedTab, onTabSelected = { selectedTab = it })

            Spacer(modifier = Modifier.height(16.dp))

            // Description Text
            Text(
                text = "Find tre punkter: 1) bøjlens yderste ende, 2) brystets fyldigste punkt, 3) din midterfront. Mål vandret mellem dem. Hold båndet tætsiddende, men ikke stramt.",
                fontSize = 15.sp,
                color = NAtextBlack,
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            MeasurementInputSection(
                title = "Skriv Dit Mål",
                value = viewModel.chestWidth,
                onValueChange = { viewModel.chestWidth = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        ManualNavigationButtons(
            onBack = onBack,
            onNext = onNext,
            nextEnabled = viewModel.chestWidth.isNotEmpty()
        )
    }
}
