package com.example.neveranother.navigator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neveranother.Temppage
import com.example.neveranother.cart.Cart
import com.example.neveranother.choosemeassurement.Choosemeasurement
import com.example.neveranother.frontpage.Frontpage
import com.example.neveranother.manual.Manual
import com.example.neveranother.productpage.Productpage
import com.example.neveranother.scanner.Scanner
import com.example.neveranother.viewmodels.NAviewmodel

@Composable
fun Navigatior(
    NAviewmodel: NAviewmodel,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "temppage") {
        composable("temppage") {
            Temppage(
                NAviewmodel,
                goToFrontpage = {navController.navigate("frontpage")},
                goToProductpage = {navController.navigate("productpage")},
                goToChoosemeasurement = {navController.navigate("choosemeasurement")},
                goToManual = {navController.navigate("manual")},
                goToScanner = {navController.navigate("scanner")},
                goToCart = {navController.navigate("cart")}
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
            Manual()
        }
        composable("scanner"){
            Scanner()
        }
        composable("cart"){
            Cart()
        }
    }

}