@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package ru.sicampus.bootcamp2026.ui.theme.screens.Profile

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ViewModelState
import ru.sicampus.bootcamp2026.data.source.AuthLocalDataSource
import ru.sicampus.bootcamp2026.data.source.UserPreferences
import ru.sicampus.bootcamp2026.ui.theme.AndroidBootcamp2026FrontendTheme
import ru.sicampus.bootcamp2026.ui.theme.Blue
import ru.sicampus.bootcamp2026.ui.theme.Surface
import ru.sicampus.bootcamp2026.ui.theme.Typography
import ru.sicampus.bootcamp2026.ui.theme.components.BottomNavBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    appViewModel: AppViewModel,
    userPreferences: UserPreferences
){
    val viewModel: ProfileViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(appViewModel, userPreferences) as T
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    val state by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    when(val currentState = state){
        is ProfileState.Error -> ProfileError(currentState, onRefresh = {viewModel.getData()})
        is ProfileState.Loading -> ProfileLoading()
        is ProfileState.EdContent -> ProfileEditScreen(viewModel = viewModel)
        is ProfileState.NoEdContent -> ProfileNoEdContent(
            appViewModel = appViewModel,
            viewModel = viewModel,
            currentState = currentState,
            onLogout = {
                scope.launch {
                    AuthLocalDataSource.clearToken()
                    userPreferences.clear()
                    viewModel.getData()
                    appViewModel.NavigateTo(ViewModelState.Login)
                }
            }
        )
    }
}

@Composable
fun MainProfileViewContent(
    viewModel: ProfileViewModel,
    data: ProfileState.NoEdContent,
    onLogout: () -> Unit
){
    Column(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxWidth()
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
                    modifier = Modifier.paddingFromBaseline(top = 10.dp, bottom = 5.dp)
                )
                Text(
                    text = data.fullName,
                    style = Typography.bodyLarge
                )
                Text(
                    text = "Должность",
                    style = Typography.labelSmall,
                    modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                )
                Text(
                    text = data.jobTitle,
                    style = Typography.bodyLarge
                )
                Text(
                    text = "Email",
                    style = Typography.labelSmall,
                    modifier = Modifier.paddingFromBaseline(top = 30.dp, bottom = 5.dp)
                )
                Text(
                    text = data.email,
                    style = Typography.bodyLarge
                )
            }
        }
        OutlinedButton(
            onClick = {
                viewModel.switchToEditMode(
                    fullName = data.fullName,
                    jobTitle = data.jobTitle,
                    email = data.email,
                    password = "",
                    avatarUrl = data.avatarUrl,
                )
            },
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),
            border = BorderStroke(2.dp, Blue)
        ){
            Text(
                "Изменить профиль",
                color = Blue
            )
        }

        Button(
            onClick = onLogout,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue)
        ) {
            Text(
                "Выйти",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProfileError(
    state: ProfileState.Error,
    onRefresh: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.reason)
            Button(onClick = onRefresh) { Text("refresh") }
        }
    }
}

@Composable
fun ProfileLoading(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun ProfileNoEdContent(
    appViewModel: AppViewModel,
    viewModel: ProfileViewModel,
    currentState: ProfileState.NoEdContent,
    onLogout: () -> Unit
){
    var selectedItemState by remember { mutableStateOf("Профиль") }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedItemState,
                onItemSelected = { selectedItemState = it },
                appViewModel = appViewModel
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
        ) {
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
                    modifier = Modifier.padding(20.dp),
                    style = Typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentState.avatarUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.sample_avatar),
                    error = painterResource(R.drawable.sample_avatar),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(144.dp)
                        .clip(CircleShape)
                )
            }

            Column(
                modifier = Modifier.padding(top = 220.dp)
            ) {
                MainProfileViewContent(
                    viewModel = viewModel,
                    data = currentState,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    AndroidBootcamp2026FrontendTheme {
        Scaffold(
            bottomBar = {
                Surface(
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {}
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Image(painter = painterResource(R.drawable.profile_wave2), contentScale = ContentScale.FillWidth, contentDescription = "", modifier = Modifier.fillMaxWidth())
                Image(painter = painterResource(R.drawable.profile_wave1), contentScale = ContentScale.FillWidth, contentDescription = "", modifier = Modifier.fillMaxWidth())

                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Профиль", modifier = Modifier.padding(20.dp), style = Typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
                    AsyncImage(
                        model = painterResource(R.drawable.sample_avatar),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(144.dp).clip(CircleShape)
                    )
                }

                Column(modifier = Modifier.padding(32.dp).fillMaxWidth().padding(top = 220.dp)) {
                    Card(colors = CardDefaults.cardColors(containerColor = Surface), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(22.dp)) {
                            Text("ФИО", style = Typography.labelSmall)
                            Text("Иванов Иван", style = Typography.bodyLarge)
                            Text("Должность", style = Typography.labelSmall)
                            Text("Android Dev", style = Typography.bodyLarge)
                            Text("Email", style = Typography.labelSmall)
                            Text("ivan@ya.ru", style = Typography.bodyLarge)
                        }
                    }
                    OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth().padding(top = 30.dp)) { Text("Изменить") }
                    Button(onClick = {}, modifier = Modifier.fillMaxWidth().padding(top = 10.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue)) { Text("Выйти") }
                }
            }
        }
    }
}