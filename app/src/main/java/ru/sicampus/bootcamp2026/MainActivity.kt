package ru.sicampus.bootcamp2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.screens.Login.LoginScreen
import androidx.compose.runtime.getValue
import ru.sicampus.bootcamp2026.ui.theme.screens.Profile.ProfileScreen

class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme{
                val viewModel: ViewModel = viewModel()

                val state by viewModel.appState.collectAsState()

                when(val currState = state) {
                    //is ViewModelState.Login -> LoginScreen()
                    //is ViewModelState.Invitations->
                    //is ViewModelState.TimeTable ->
                    is ViewModelState.Profile -> ProfileScreen()
                    //is ViewModelState.Loading ->
                    //is ViewModelState.Error ->
                    else -> LoginScreen()
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    AndroidBootcamp2026FrontendTheme {
        LoginScreen(
            /* onLoginClick = {email, password ->
                println("Превью: Вход с email=$email, password=$password")
            }

             */

        )
    }
}