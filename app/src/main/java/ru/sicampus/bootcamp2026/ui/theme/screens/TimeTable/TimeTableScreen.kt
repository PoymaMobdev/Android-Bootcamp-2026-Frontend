package ru.sicampus.bootcamp2026.ui.theme.screens.TimeTable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.ui.theme.components.BottomNavBar
import ru.sicampus.bootcamp2026.ui.theme.components.WeekView





@SuppressLint("NewApi")
@Composable
fun TimetableScreen(
    appViewModel: AppViewModel,
    userPreferences: UserPreferences
    ) {
    val viewModel: TTViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TTViewModel(appViewModel) as T
            }
        }
    )

    val state by viewModel.uiState.collectAsState()

    when(val currState = state){
        is TTState.Error -> TimetableError(currState, onRefresh = {viewModel.getData()})
        is TTState.Loading -> TimetableLoading()
        is TTState.Content -> TimeTableContent(appViewModel,toInvitations = {viewModel.toInvitations()}, toProfile = {viewModel.toProfile()}, userPreferences )
    }
}


@Composable
fun TimetableError(
    state: TTState.Error,
    onRefresh: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason)
            Button(
                onClick = onRefresh
            ) {
                Text("refresh")
            }
        }
    }
}


@Composable
fun TimetableLoading(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp)
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun TimeTableContent(
    appViewModel: AppViewModel,
    toInvitations: () -> Unit,
    toProfile: () -> Unit,
    userPreferences: UserPreferences
){
    val viewModel: TTViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TTViewModel(appViewModel) as T
            }
        }
    )

    var selectedItem by remember { mutableStateOf("Расписание") }

    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Неделя", "Месяц")



    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                appViewModel = appViewModel
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 50.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                options.forEachIndexed { index, label ->
                    Button(
                        onClick = { selectedIndex = index },
                        colors = if (index == selectedIndex)
                            ButtonDefaults.buttonColors()
                        else
                            ButtonDefaults.outlinedButtonColors(),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(text = label)
                    }
                }
            }

            when (selectedIndex) {
                0 -> WeekView(appViewModel = appViewModel, userPreferences )
                //1 -> MonthView()
            }
    }


    }
}







