package ru.sicampus.bootcamp2026

import android.annotation.SuppressLint
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
import ru.sicampus.bootcamp2026.ui.theme.screens.Invitation.InvitationListScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.Profile.ProfileScreen
import ru.sicampus.bootcamp2026.ui.theme.screens.TimeTable.TimetableScreen

class MainActivity() : ComponentActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidBootcamp2026FrontendTheme{
                val viewModel: AppViewModel = viewModel()

                val state by viewModel.appState.collectAsState()
                when(val currState = state) {
                    is ViewModelState.Login -> LoginScreen(viewModel)
                    is ViewModelState.Invitations-> InvitationListScreen(viewModel)
                    is ViewModelState.TimeTable -> TimetableScreen(viewModel)
                    is ViewModelState.Profile -> ProfileScreen(viewModel)
                    //is ViewModelState.Loading ->
                    //is ViewModelState.Error ->
                    else -> LoginScreen(viewModel)
                }
            }
        }

    }
}



@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    AndroidBootcamp2026FrontendTheme {
    }
}