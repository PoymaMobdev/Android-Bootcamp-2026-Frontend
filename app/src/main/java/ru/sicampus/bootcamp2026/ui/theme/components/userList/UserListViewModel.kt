package ru.sicampus.bootcamp2026.ui.theme.components.userList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.sicampus.bootcamp2026.data.UserRepository
import ru.sicampus.bootcamp2026.data.source.UserInfoDataSource
import ru.sicampus.bootcamp2026.domain.GetUsersUseCase

class UserListViewModel: ViewModel() {

    private val _uiState: MutableStateFlow<UserListState> = MutableStateFlow(UserListState.Content)

    val uiState = _uiState.asStateFlow()

    val getUsersUseCase = GetUsersUseCase(UserRepository(UserInfoDataSource()))


}