package com.ayush.linkup.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.linkup.data.model.User
import com.ayush.linkup.data.repository.DiscoverRepository
import com.ayush.linkup.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverRepository: DiscoverRepository
) : ViewModel() {

    private val _allUsersState = MutableStateFlow<State<List<User>>>(State.None)
    val allUsersState get() = _allUsersState.asStateFlow()

    private val _searchState = MutableStateFlow<State<List<User>>>(State.None)
    val searchState get() = _searchState.asStateFlow()

    lateinit var currentUserId: String

    fun getAllUsers() {
        viewModelScope.launch {
            discoverRepository.getAllUsers().collect {
                _allUsersState.value = it
            }
        }
    }

    fun searchUser(user: String) {
        viewModelScope.launch {
            discoverRepository.searchUsers(user).collect {
                _searchState.value = it
            }
        }
    }

    fun getCurrentUserId() {
        currentUserId = discoverRepository.getCurrentUserId()
    }

    init {
        getCurrentUserId()
        getAllUsers()
    }
}