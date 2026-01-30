package ru.sicampus.bootcamp2026.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.Typography

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(){
    Scaffold() {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {

            MainProfileViewContent()

            Image(
                painter = painterResource(R.drawable.profile_wave2),
                contentScale = ContentScale.FillWidth,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
            Image(
                painter = painterResource(R.drawable.profile_wave1),
                contentScale = ContentScale.FillWidth,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Профиль",
                    modifier = Modifier
                        .padding(20.dp),
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Image(
                    painter = painterResource(id = R.drawable.sample_avatar),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(144.dp)
                        .clip(CircleShape)

                )
            }
        }
    }
}

@Composable
fun MainProfileViewContent(){
    Column(
        modifier = Modifier.padding(32.dp).fillMaxWidth()
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Surface,
            ),
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.padding(22.dp)
            ){
                Text(
                    text = "ФИО",
                    style = Typography.labelSmall,
                    modifier = Modifier.paddingFromBaseline(top = 270.dp, bottom = 5.dp)
                )
                Text(
                    text = "Иванов Иван Иванович",
                    style = Typography.bodyLarge
                )
                Text(
                    text = "Должность",
                    style = Typography.labelSmall,
                    modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                )
                Text(
                    text = "Должность сотрудника",
                    style = Typography.bodyLarge
                )
                Text(
                    text = "Email",
                    style = Typography.labelSmall,
                    modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                )
                Text(
                    text = "ivan@mail.com",
                    style = Typography.bodyLarge
                )
            }
        }
        OutlinedButton(
            onClick = {},
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
            border = BorderStroke(3.dp, Blue)
        ){
            Text(
                "Изменить профиль",
                color = Blue
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview(){
    AndroidBootcamp2026FrontendTheme {
        ProfileScreen()
    }
}