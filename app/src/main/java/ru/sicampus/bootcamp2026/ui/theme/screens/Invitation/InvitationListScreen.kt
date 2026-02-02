package ru.sicampus.bootcamp2026.ui.theme.screens.Invitation


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.Typography
import ru.sicampus.bootcamp2026.ui.theme.components.InvitationList

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InvitationListScreen() {
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(R.drawable.invitation_wave2),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                Image(
                    painter = painterResource(R.drawable.invitation_wave1),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Приглашения",
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.Center),
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Surface,
                ),
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                InvitationList(
                    meetingNames = listOf(
                        "Название встречи",
                        "Название встречи",
                        "Название встречи",
                        "Название встречи"
                    ),
                    datesAndTimes = listOf(
                        "01.02.2026  17:00",
                        "01.02.2026  18:00",
                        "01.02.2026  19:00",
                        "01.02.2026  20:00",
                    )
                )
            }
        }
    }

}

@Preview
@Composable
fun InvitationListScreenPreview() {
    AndroidBootcamp2026FrontendTheme() {
        InvitationListScreen()
    }
}