package ru.sicampus.bootcamp2026.ui.theme.screens.MeetingInfo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.dto.MeetinCreateDTO
import ru.sicampus.bootcamp2026.data.source.MeetingCreateNetDataSource
import ru.sicampus.bootcamp2026.data.source.ScheduleNetworkDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.data.source.UsersInfoDataSource
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Blue
import java.util.Calendar

val HeaderBlue = Blue
val BgLightBlue = Color(0xFFF5F5F5)
val BorderGray = Color(0xFFE0E0E0)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("Выберите время") }
    var searchQuery by remember { mutableStateOf("") }

    val people = remember { mutableStateListOf<String>() }
    val selected = remember { mutableStateListOf<Long>() }

    val data = UsersInfoDataSource()
    val dataSource = MeetingCreateNetDataSource()
    val scope = rememberCoroutineScope()
    val idDataSource = ScheduleNetworkDataSource()
    val idValue = mutableStateOf<Long?>(null)
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            date = formattedDate
        },
        year, month, day
    )
    datePickerDialog.datePicker.minDate = calendar.timeInMillis

    LaunchedEffect(Unit) {
        val userid = idDataSource.loadAndReturnUserID(userPreferences.getUserEmail())
        idValue.value = userid
        data.getUsers(0, 100000).onSuccess {
            it.content?.forEach { user ->
                people.add("${user.id}) ${user.fullName}")
            }
        }
    }

    val filteredPeople = people.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    CreateMeetingContent(
        name = name,
        onNameChange = { name = it },
        date = date,
        onDateClick = { datePickerDialog.show() },
        time = time,
        onTimeChange = { time = it },
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        filteredPeople = filteredPeople,
        selectedIds = selected,
        onPersonToggle = { id ->
            if (id == idValue.value) {
                Toast.makeText(context, "Вы организатор", Toast.LENGTH_SHORT).show()
            } else {
                if (id in selected) selected.remove(id) else selected.add(id)
            }
        },
        onClose = { appViewModel.NavigateTo(ViewModelState.TimeTable) },
        onSave = {
            if (name.isEmpty() || date.isEmpty() || time == "Выберите время") {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
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
                    } else {
                        Toast.makeText(context, "Ошибка создания", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )
}

@Composable
fun CreateMeetingContent(
    name: String,
    onNameChange: (String) -> Unit,
    date: String,
    onDateClick: () -> Unit,
    time: String,
    onTimeChange: (String) -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    filteredPeople: List<String>,
    selectedIds: List<Long>,
    onPersonToggle: (Long) -> Unit,
    onClose: () -> Unit,
    onSave: () -> Unit
) {
    val timeConstantList = listOf("09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BgLightBlue
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.meeting_wave1),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )
                Image(
                    painter = painterResource(R.drawable.meeting_wave2),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                )

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Создание встречи",
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 15.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 140.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize()
                    ) {
                        CustomTextField(
                            value = name,
                            onValueChange = onNameChange,
                            placeholder = "Название встречи"
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier.clickable { onDateClick() }
                        ) {
                            OutlinedTextField(
                                value = if (date.isEmpty()) "Выберите дату" else date,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BorderGray,
                                    unfocusedBorderColor = BorderGray
                                ),
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 14.sp,
                                    color = if (date.isEmpty()) Color.Gray else Color.Black
                                )
                            )
                            Box(modifier = Modifier.matchParentSize().clickable { onDateClick() })
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Box {
                            OutlinedTextField(
                                value = time,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BorderGray,
                                    unfocusedBorderColor = BorderGray
                                ),
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 14.sp,
                                    color = if (time == "Выберите время") Color.Gray else Color.Black
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .clickable { expanded = true }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .background(Color.White)
                                    .fillMaxWidth(0.7f)
                            ) {
                                timeConstantList.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            onTimeChange(option)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Выберите участников",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, BorderGray),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = onSearchChange,
                                    placeholder = { Text("Поиск...", color = Color.Gray, fontSize = 14.sp) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                                    },
                                    trailingIcon = if (searchQuery.isNotEmpty()) {
                                        {
                                            IconButton(onClick = { onSearchChange("") }) {
                                                Icon(Icons.Outlined.Cancel, contentDescription = null, tint = Color.Gray)
                                            }
                                        }
                                    } else null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent
                                    ),
                                    singleLine = true
                                )

                                HorizontalDivider(color = BorderGray, thickness = 1.dp)

                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(4.dp)
                                ) {
                                    items(filteredPeople) { person ->
                                        val parts = person.split(") ")
                                        val id = parts[0].toLongOrNull() ?: 0L
                                        val namePart = parts.getOrElse(1) { person }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { onPersonToggle(id) }
                                                .padding(horizontal = 12.dp, vertical = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = namePart,
                                                fontSize = 14.sp,
                                                modifier = Modifier.weight(1f)
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .border(
                                                        1.dp,
                                                        if (id in selectedIds) HeaderBlue else Color.Gray,
                                                        RoundedCornerShape(4.dp)
                                                    )
                                                    .background(
                                                        if (id in selectedIds) HeaderBlue else Color.Transparent,
                                                        RoundedCornerShape(4.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (id in selectedIds) {
                                                    Icon(
                                                        painter = painterResource(R.drawable.ic_launcher_foreground),
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                        HorizontalDivider(color = BorderGray.copy(0.3f))
                                    }
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HeaderBlue)
                ) {
                    Text("Сохранить", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HeaderBlue,
            unfocusedBorderColor = BorderGray,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
    )
}

@Preview
@Composable
fun PreviewCreateScreen() {
    AndroidBootcamp2026FrontendTheme {
        CreateMeetingContent(
            name = "Встреча",
            onNameChange = {},
            date = "2026-02-12",
            onDateClick = {},
            time = "12:00",
            onTimeChange = {},
            searchQuery = "",
            onSearchChange = {},
            filteredPeople = listOf("1) Иван Иванов", "2) Петр Петров"),
            selectedIds = listOf(1L),
            onPersonToggle = {},
            onClose = {},
            onSave = {}
        )
    }
}