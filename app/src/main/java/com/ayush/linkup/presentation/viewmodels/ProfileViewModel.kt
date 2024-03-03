package com.ayush.linkup.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.linkup.data.model.Post
import com.ayush.linkup.data.model.User
import com.ayush.linkup.data.repository.AuthRepository
import com.ayush.linkup.data.repository.PostRepository
import com.ayush.linkup.data.repository.UserRepository
import com.ayush.linkup.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    var userId: String? = null

    private val _userState = MutableStateFlow<State<User>>(State.None)
    val userState get() = _userState.asStateFlow()

    private val _postState = MutableStateFlow<State<List<Post>>>(State.None)
    val postState get() = _postState.asStateFlow()

    private val _deletePostState = MutableStateFlow<State<Boolean>>(State.None)
    val deletePostState get() = _deletePostState.asStateFlow()

    fun getUserData() {
        viewModelScope.launch {
            userRepository.getUser(userId!!)
                .collect {
                    _userState.value = it
                }
        }
    }

    fun getAllPosts() {
        viewModelScope.launch {
            postRepository.getAllPostsByUserId(userId!!)
                .collect {
                    _postState.value = it
                }
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postRepository.deletePost(post)
                .collect {
                    _deletePostState.value = it
                }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            userRepository.deleteUser(userId!!)
        }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun getCurrentUserId() {
        viewModelScope.launch {
            userId = userRepository.getCurrentUserId()
        }
    }

    init {
        getCurrentUserId()
        getAllPosts()
    }
}