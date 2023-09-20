package com.hamidrezabashiri.ezcard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


object MainDestinations {
    const val HOME_ROUTE = "home"
    const val WALLET_ROUTE = "wallet"
    const val SETTINGS_ROUTE = "settings"
    const val LOGIN_ROUTE = "login"
    const val SIGNUP_ROUTE = "signup"
    const val SHARE_CARD_ROUTE = "shareCard"
    const val ADD_CARD_ROUTE = "addCard"
    const val WELCOME_ROUTE = "welcome_screen"
}

/**
 * Remembers and creates an instance of [EzCardNavController]
 */
@Composable
fun rememberEzCardNavController(
    navController: NavHostController = rememberNavController()
): EzCardNavController = remember(navController) {
    EzCardNavController(navController)
}

/**
 * Responsible for holding UI Navigation logic.
 */
@Stable
class EzCardNavController(
    val navController: NavHostController,
) {

    // ----------------------------------------------------------
    // Navigation state source of truth
    // ----------------------------------------------------------

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun upPress() {
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {

        if (route != currentRoute) {

            navController.navigate(route) {
                anim {
                    enter = 0
                    exit = 0
                    popEnter = 0
                    popExit = 0
                }
                launchSingleTop = true
                restoreState = true
                // Pop up backstack to the first destination and save state. This makes going back
                // to the start destination when pressing back in any other bottom tab.
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToSubScreen(route: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(route)
        }
    }

    fun navigateAndPopAllBackStackEntries(route: String, from: NavBackStackEntry) {
        navController.navigate(route) {
            popUpTo(from.id) {
                inclusive = true
            }
        }
    }

    fun navigateWithParam(param: Long, from: NavBackStackEntry, route: String) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("$route/$param")
        }
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

/**
 * Copied from similar function in NavigationUI.kt
 *
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
 */
private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}