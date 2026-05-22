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
import com.example.neveranother.viewmodels.NAviewmodel

@Composable
fun Navigatior(
    NAviewmodel: NAviewmodel,
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
                NAviewmodel,
                goToFrontpage = {navController.navigate("frontpage")},
                goToProductpage = {navController.navigate("productpage")},
                goToChoosemeasurement = {navController.navigate("choosemeasurement")},
                goToManual = {navController.navigate("manual")},
                goToScanner = {navController.navigate("scanner")},
                goToCart = {navController.navigate("cart")},
                goToHistory = {navController.navigate("history")}
            )
        }
        composable("frontpage") {
            Frontpage()
        }
        composable("productpage"){
            Productpage()
        }
        composable("choosemeasurement"){
            Choosemeasurement(NAviewmodel,
                {navController.navigate("manual")},
                {navController.navigate("scanner")})
        }
        composable("manual"){
            Manual(
                viewModel = NAviewmodel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("lowercircumference") }
            )
        }
        composable("lowercircumference"){
            LowerCircumference(
                viewModel = NAviewmodel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("chestvolume") }
            )
        }
        composable("chestvolume"){
            ChestVolume(
                viewModel = NAviewmodel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("chestwidth") }
            )
        }
        composable("chestwidth"){
            ChestWidth(
                viewModel = NAviewmodel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("chestheight") }
            )
        }
        composable("chestheight"){
            ChestHeight(
                viewModel = NAviewmodel,
                onBack = { navController.popBackStack() },
                onNext = { navController.navigate("results") }
            )
        }
        composable("results") {
            Results(
                NAviewmodel = NAviewmodel,
                onBack = { navController.popBackStack() },
                onGoToCart = {
                    NAviewmodel.saveCurrentMeasurements()
                    navController.navigate("cart")
                }
            )
        }
        composable("scanner"){
            Scanner()
        }
        composable("cart"){
            Cart()
        }
        composable("history") {
            History(
                viewModel = NAviewmodel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
