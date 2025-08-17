package com.example.movies.auth.ui

import android.net.Uri
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.movies_details.navigation.AUTH_ROUTE
import com.example.movies_details.navigation.NOW_PLAYING_ROUTE
import com.example.state.State

@Composable
fun AuthScreen(factory: ViewModelProvider.Factory, navController: NavController) {
    val viewModel: MoviesAuthViewModel = viewModel(factory = factory)
    val tokenState = viewModel.tokenState
    val sessionState = viewModel.sessionState
    val isLoading = viewModel.isLoading

    if (sessionState is State.Success) {
        LaunchedEffect(Unit) {
            navController.navigate(NOW_PLAYING_ROUTE) {
                popUpTo(AUTH_ROUTE) { inclusive = true }
            }
        }
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        tokenState is State.Empty && sessionState is State.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { viewModel.startAuth() }) {
                    Text("Log in")
                }
            }
        }

        tokenState is State.Success -> {
            val token = tokenState.data
            val url = "https://www.themoviedb.org/authenticate/$token?redirect_to=moviesapp://auth"

            AndroidView(factory = { context ->
                CustomTabsIntent.Builder().build().also {
                    it.launchUrl(context, Uri.parse(url))
                }
                View(context) // пустая заглушка
            })

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Confirm login in browser")
            }

        }

        sessionState is State.Success -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = (Alignment.Center)
            ) {
                Text("Authorization successful! Session ID: ${sessionState.data}")
            }

            // 👇 сразу навигируем дальше
            LaunchedEffect(sessionState) {
                navController.navigate(NOW_PLAYING_ROUTE) {
                    popUpTo(AUTH_ROUTE) { inclusive = true }
                }
            }
        }

        tokenState is State.Failure -> {
            Text("Error getting token: ${tokenState.message.message}")
        }

        sessionState is State.Failure -> {
            Text("Error getting session: ${sessionState.message.message}")
        }
    }
}
