package com.example.neveranother.navigator

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neveranother.Temppage
import com.example.neveranother.cart.Cart
import com.example.neveranother.choosemeassurement.Choosemeasurement
import com.example.neveranother.frontpage.Frontpage
import com.example.neveranother.history.History
import com.example.neveranother.manual.ChestHeight
import com.example.neveranother.manual.ChestVolume
import com.example.neveranother.manual.Results
import com.example.neveranother.manual.ChestWidth
import com.example.neveranother.manual.LowerCircumference
import com.example.neveranother.manual.Manual
import com.example.neveranother.productpage.Productpage
import com.example.neveranother.scanner.Scanner
import com.example.neveranother.scanner.components.ChestVolume3D
import com.example.neveranother.scanner.components.HowToScan
import com.example.neveranother.viewmodels.NAViewModel

@Composable
fun Navigatior(
    naViewModel: NAViewModel,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "temppage",
        enterTransition = {
            slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
        }
    ) {
        composable("temppage") {
            Temppage(
                goToFrontpage = { navController.navigate("frontpage") },
                goToProductpage = { navController.navigate("productpage") },
                goToChoosemeasurement = { navController.navigate("choosemeasurement") },
                goToManual = { navController.navigate("manual") },
                goToScanner = { navController.navigate("scanner") },
                goToCart = { navController.navigate("cart") },
                goToHistory = { navController.navigate("history") }
            )
        }
        composable("frontpage") {
            Frontpage(
                { navController.navigate("productpage") }
            )
        }
        composable("productpage"){
            Productpage(
                naViewModel = naViewModel,
                onGoToMeasurement = { navController.navigate("choosemeasurement") },
                onCartClick = { navController.navigate("cart") },
                goToHome = {navController.navigate("frontpage")},
                goToCart = {navController.navigate("cart")}
            )
        }
        composable("choosemeasurement"){
            Choosemeasurement(naViewModel,
                {navController.navigate("manual")},
                {navController.navigate("howtoscan")},
                 { navController.popBackStack() })
        }
        composable("manual"){
            Manual(
                viewModel = naViewModel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("lowercircumference") }
            )
        }
        composable("lowercircumference"){
            LowerCircumference(
                viewModel = naViewModel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("chestvolume") }
            )
        }
        composable("chestvolume"){
            ChestVolume(
                viewModel = naViewModel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("chestwidth") }
            )
        }
        composable("chestwidth"){
            ChestWidth(
                viewModel = naViewModel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("chestheight") }
            )
        }
        composable("chestheight"){
            ChestHeight(
                viewModel = naViewModel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("results") }
            )
        }
        composable("results") {
            Results(
                naViewModel = naViewModel,
                onBack = { navController.popBackStack() },
                onGoToCart = {
                    naViewModel.saveCurrentMeasurements()
                    navController.navigate("productpage")
                }
            )
        }
        composable("howtoscan"){
            HowToScan(
                onNextClick ={ navController.navigate("chestvolume3d") }
            )
        }
        composable("chestvolume3d"){
            ChestVolume3D(
                viewModel = naViewModel,
                onBack = { navController.popBackStack() },
                onStartScanClick = { navController.navigate("scanner") }
            )
        }
        composable("scanner"){
            Scanner(
                NAviewmodel = naViewModel,
                onScanComplete = { navController.navigate("results") }
            )
        }
        composable("cart"){
            Cart(
                onBack = { navController.popBackStack() }
            )
        }
        composable("history"){
            History(
                viewModel = naViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

