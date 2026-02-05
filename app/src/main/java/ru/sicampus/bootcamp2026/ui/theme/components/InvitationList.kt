package ru.sicampus.bootcamp2026.ui.theme.components


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Typography
import ru.sicampus.bootcamp2026.ui.theme.screens.Invitation.InvitationViewModel
import kotlin.String

@Composable
fun InvitationListItem(
    meetingName: String,
    dateAndTime: String,
    appViewModel: AppViewModel
) {
    val viewModel: InvitationViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return InvitationViewModel(appViewModel) as T
            }
        }
    )

    Row(
        modifier = Modifier.padding(12.dp).fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.sample_avatar),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 8.dp, end = 6.dp)
                .size(39.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = Modifier.padding(start = 6.dp)
        ) {
            Text(
                meetingName,
                Modifier.padding(bottom = 5.dp),
                style = Typography.bodyLarge
            )
            Text(
                dateAndTime,
                style = Typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { viewModel.toMeetInfo() },
            modifier = Modifier.align(Alignment.CenterVertically),
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Рассмотреть запрос"
            )
        }
    }
    HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(bottom = 4.dp))
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun InvitationList(
    meetingNames: List<String>,
    datesAndTimes: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(meetingNames) { index, meetingName ->
            InvitationListItem(
                meetingName = meetingName,
                dateAndTime = datesAndTimes[index],
                appViewModel = AppViewModel()
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview(){
    AndroidBootcamp2026FrontendTheme {
        val meetingNames : List<String> = listOf("Анна", "Борис", "Василий")
        val datesAndTimes : List<String> = listOf("123124", "45745754", "465")
        InvitationList(
            meetingNames,
            datesAndTimes
        )
    }
}