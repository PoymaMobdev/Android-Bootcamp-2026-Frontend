package ru.sicampus.bootcamp2026.ui.theme.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.sicampus.bootcamp2026.AppViewModel
import ru.sicampus.bootcamp2026.R
import ru.sicampus.bootcamp2026.ViewModelState

@Composable
fun BottomNavBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    appViewModel: AppViewModel
) {
    val activeColor = Color.Black
    val inactiveColor = Color.Gray.copy(alpha = 0.6f)

    val navBarShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 15.dp, shape = navBarShape)
            .clip(navBarShape),
        containerColor = Color.White,
        tonalElevation = 0.dp,
    ) {
        val items = listOf("Приглашения", "Расписание", "Профиль")

        items.forEach { item ->
            val isSelected = selectedItem == item

            NavigationBarItem(
                modifier = Modifier.padding(top = 8.dp),
                selected = isSelected,
                onClick = {
                    onItemSelected(item)
                    when (item) {
                        "Приглашения" -> appViewModel.NavigateTo(ViewModelState.Invitations)
                        "Расписание" -> appViewModel.NavigateTo(ViewModelState.TimeTable)
                        "Профиль" -> appViewModel.NavigateTo(ViewModelState.Profile)
                    }
                },
                icon = {
                    when (item) {
                        "Приглашения" -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Email else Icons.Outlined.Email,
                            contentDescription = "Приглашения"
                        )
                        "Расписание" -> Icon(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = "Расписание"
                        )
                        "Профиль" -> Icon(
                            imageVector = if (isSelected) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Профиль"
                        )
                    }
                },
                label = {
                    Text(
                        text = item,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = activeColor,
                    selectedTextColor = activeColor,
                    unselectedIconColor = inactiveColor,
                    unselectedTextColor = inactiveColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}