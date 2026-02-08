package ru.sicampus.bootcamp2026.ui.theme.screens.TimeTable

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    when (val currState = state) {
        is TTState.Error -> TimetableError(currState, onRefresh = { viewModel.getData() })
        is TTState.Loading -> TimetableLoading()
        is TTState.Content -> TimeTableContent(
            appViewModel = appViewModel,
            toInvitations = { viewModel.toInvitations() },
            toProfile = { viewModel.toProfile() },
            userPreferences = userPreferences
        )
    }
}


@Composable
fun TimetableError(
    state: TTState.Error,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
fun TimetableLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
) {
    var selectedItem by remember { mutableStateOf("Расписание") }
    var selectedIndex by remember { mutableIntStateOf(0) }

    TimeTableLayout(
        selectedIndex = selectedIndex,
        onOptionSelected = { selectedIndex = it },
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                appViewModel = appViewModel
            )
        },
        content = {
            when (selectedIndex) {
                0 -> WeekView(appViewModel = appViewModel, userPreferences = userPreferences)
                //1 -> MonthView()
            }
        }
    )
}

@Composable
fun TimeTableLayout(
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val options = listOf("Неделя", "Месяц")

    Scaffold(
        bottomBar = bottomBar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
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
                        onClick = { onOptionSelected(index) },
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
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimetableScreenPreview() {
    TimeTableLayout(
        selectedIndex = 0,
        onOptionSelected = {},
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                color = Color.LightGray
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("Bottom Bar")
                }
            }
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Week View Placeholder")
            }
        }
    )
}