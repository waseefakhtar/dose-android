package com.waseefakhtar.doseapp.util

import androidx.navigation.NavHostController

fun NavHostController.navigateSingleTop(destination:String){
    this.navigate(destination){
        popUpTo(destination)
        launchSingleTop = true
    }
}