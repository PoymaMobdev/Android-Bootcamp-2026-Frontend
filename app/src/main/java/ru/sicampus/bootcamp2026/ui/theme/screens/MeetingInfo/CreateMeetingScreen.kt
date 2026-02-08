package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import android.annotation.SuppressLint
import android.widget.Toast
import ru.sicampus.bootcamp2026.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.data.dto.MeetinCreateDTO
import ru.sicampus.bootcamp2026.data.source.MeetingCreateNetDataSource
import ru.sicampus.bootcamp2026.data.source.UsersInfoDataSource
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Gray
import ru.sicampus.bootcamp2026.ui.theme.InverseSurface
import ru.sicampus.bootcamp2026.ui.theme.OpDeepBlue
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.fontFamily
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.source.ScheduleNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Typography


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun CreateMeetingScreen(
    appViewModel: AppViewModel,
    userPreferences: UserPreferences
) {


    val viewModel: MeetingInfoViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MeetingInfoViewModel(appViewModel) as T
            }
        }
    )
    val appState by appViewModel.appState.collectAsStateWithLifecycle()


    val data = UsersInfoDataSource()
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("09:00") }
    val people = remember { mutableStateListOf<String>() }
    val selected = remember { mutableStateListOf<Long>() }
    val dataSource = MeetingCreateNetDataSource()
    val scope = rememberCoroutineScope()
    val idDataSource = ScheduleNetworkDataSource()
    val idValue = mutableStateOf<Long?>(null)
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val timeConstantList = listOf(
        "09:00",
        "10:00",
        "11:00",
        "12:00",
        "13:00",
        "14:00",
        "15:00",
        "16:00",
        "17:00",
    )


    LaunchedEffect(Unit) {
        val userid = idDataSource.loadAndReturnUserID(userPreferences.getUserEmail())
        idValue.value = userid
        data.getUsers(0, 100000).onSuccess {
            it.content?.forEach { user ->
                people.add("${user.id}) ${user.fullName} ${user.jobTitle}")
            }
        }
    }


    Scaffold() {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Surface,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(40.dp).fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.height(120.dp))

                        MeetingField("Название встречи", name) { name = it }
                        MeetingField("Дата: yyyy-MM-dd", date) { date = it }

                        Card(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                            ) {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { expanded = !expanded },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                    ),
                                ) {
                                    Text(time, color = Color.DarkGray)
                                    Icon(
                                        Icons.Default.KeyboardArrowDown,
                                        contentDescription = "dropdown",
                                        tint = Color.DarkGray
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    timeConstantList.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = {
                                                time = option
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = "Список участников",
                            style = Typography.bodyLarge,
                            modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                        )
                        LazyColumn(
                            modifier = Modifier.height(350.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(people) { person ->
                                val parts = person.split(") ")
                                val Id = parts[0].toLong()

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (Id in selected && idValue.value != Id) OpDeepBlue else Gray,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(16.dp)
                                        .clickable {
                                            if (Id in selected) {
                                                selected.remove(Id)
                                            } else if(Id == idValue.value){
                                                Toast.makeText(context, "Вы - организатор, вы не можете себя добавить", Toast.LENGTH_LONG).show()
                                            }
                                            else {
                                                selected.add(Id)
                                            }
                                        },
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = person, fontFamily = fontFamily)
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(
                                                if (Id in selected) Blue else Surface,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .border(
                                                1.dp,
                                                InverseSurface.copy(0.3f),
                                                RoundedCornerShape(4.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (Id in selected) {
                                            Text(text = "", color = Surface, fontSize = 14.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Image(
                    painter = painterResource(R.drawable.meeting_wave1),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                Image(
                    painter = painterResource(R.drawable.meeting_wave2),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                IconButton(
                    onClick = {
                        //TODO: добавить переход на предыдущий экран
                    },
                    modifier = Modifier.padding(7.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = Color.White,
                        contentDescription = "закрыть"
                    )
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(30.dp),
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            val success = dataSource.createMeeting(
                                meeting = MeetinCreateDTO(
                                    name,
                                    dateTime = viewModel.convertToISO(date, time).toString(),
                                    selected,
                                ),
                                usersPreferences = userPreferences
                            )
                            if (success) {
                                appViewModel.NavigateTo(ViewModelState.TimeTable)
                            }
                        }
                    },
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        "Создать"
                    )
                }
            }
        }
    }
}

@Composable
fun MeetingField(label: String, value: String, onValueChange: (String) -> Unit) {

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
        value = value,
        textStyle = Typography.labelSmall,
        onValueChange = onValueChange,
        maxLines = 1,
        label = { Text(label) }
    )
}

@Preview
@Composable
fun CreateNewMeetingScreenPreview(
    appViewModel: AppViewModel,
    userPreferences: UserPreferences
){
    AndroidBootcamp2026FrontendTheme {
        CreateMeetingScreen(appViewModel, userPreferences)
    }
}