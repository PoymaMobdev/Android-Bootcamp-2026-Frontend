package ru.sicampus.bootcamp2026.screens

import android.R.attr.text
import android.annotation.SuppressLint
import android.widget.ImageButton
import ru.sicampus.bootcamp2026.components.UserList
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.Typography

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MeetingInfoScreen() {
    Scaffold() {
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
                    Text(
                        text = "Название",
                        style = Typography.labelSmall,
                        modifier = Modifier.paddingFromBaseline(top = 150.dp, bottom = 5.dp)
                    )
                    Text(
                        text = "Название встречи",
                        style = Typography.bodyLarge
                    )
                    Text(
                        text = "Описание",
                        style = Typography.labelSmall,
                        modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                    )
                    Text(
                        text = "Очень длинное описание предстоящей встречи, которое придумал " +
                                "сотрудник, чтобы все поняли, для чего она нужна",
                        style = Typography.bodyLarge
                    )
                    Text(
                        text = "Дата и время",
                        style = Typography.labelSmall,
                        modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                    )
                    Text(
                        text = "08.02.2026   18:00-19:00",
                        style = Typography.bodyLarge
                    )
                    Text(
                        text = "Место",
                        style = Typography.labelSmall,
                        modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                    )
                    Text(
                        text = "Место встречи",
                        style = Typography.bodyLarge
                    )
                    Text(
                        text = "Список участников",
                        style = Typography.labelSmall,
                        modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                    )
                    UserList(
                        fios = listOf("Иванов Иван Иванович", "Иванов Иван Иванович",
                            "Иванов Иван Иванович", "Иванов Иван Иванович", "Иванов Иван Иванович"),
                        positions = listOf(
                            "Должность сотрудника",
                            "Должность сотрудника",
                            "Должность сотрудника",
                            "Должность сотрудника",
                            "Должность сотрудника"
                        ),
                        isOrganizers = listOf(true, false, false, false, false) // тоже 5 элементов
                    )
                }
            }
            Image(
                painter = painterResource(R.drawable.meeting_wave2),
                contentScale = ContentScale.FillWidth,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
            Image(
                painter = painterResource(R.drawable.meeting_wave1),
                contentScale = ContentScale.FillWidth,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
            IconButton(
                onClick = {},
                modifier = Modifier.align(Alignment.TopEnd).padding(7.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    tint = Color.White,
                    contentDescription = "закрыть"
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewMeetingInfoScreen() {
    AndroidBootcamp2026FrontendTheme() {
        MeetingInfoScreen()
    }
}