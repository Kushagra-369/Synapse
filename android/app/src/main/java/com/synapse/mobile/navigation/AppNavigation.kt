package com.synapse.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.synapse.mobile.features.onboarding.FinishScreen
import com.synapse.mobile.features.onboarding.OnboardingScreen
import com.synapse.mobile.features.onboarding.PermissionScreen
import com.synapse.mobile.features.onboarding.SpecialAccessScreen
import com.synapse.mobile.features.home.HomeScreen
import com.synapse.mobile.core.engine.SynapseEngine
import com.synapse.mobile.features.developer.DeveloperScreen
@Composable
fun AppNavigation(
    engine: SynapseEngine
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "onboarding"
    ) {

        composable("onboarding") {
            OnboardingScreen(navController)
        }

        composable("permissions") {
            PermissionScreen(navController)
        }

        composable("special_access") {
            SpecialAccessScreen(navController)
        }

        composable("finish") {
            FinishScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("developer") {
            DeveloperScreen(navController, engine)
        }

    }

}