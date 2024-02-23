package com.ayush.linkup.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.linkup.data.model.Post
import com.ayush.linkup.data.model.User
import com.ayush.linkup.data.repository.PostRepository
import com.ayush.linkup.data.repository.UserRepository
import com.ayush.linkup.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _allPostsState = MutableStateFlow<State<List<Post>>>(State.None)
    val allPostsState get() = _allPostsState

    private val _postState = MutableStateFlow<State<Post>>(State.None)
    val postState get() = _postState

    private val _userState = MutableStateFlow<State<User>>(State.None)
    val userState get() = _userState

    private val _addPostState = MutableStateFlow<State<Boolean>>(State.None)
    val addPostState get() = _addPostState

    private val _deleteState = MutableStateFlow<State<Boolean>>(State.None)
    val deleteState get() = _deleteState

    var currentUserId: String? = null

    init {
        getCurrentUserId()
    }


    fun getAllPosts() {
        viewModelScope.launch {
            postsRepository.getAllPosts().collect {
                _allPostsState.value = it
            }
        }
    }

    fun getPost(postId: String) {
        viewModelScope.launch {
            postsRepository.getPost(postId).collect {
                _postState.value = it
            }
        }
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            postsRepository.addPost(post).collect {
                _addPostState.value = it
            }
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postsRepository.deletePost(post).collect {
                _deleteState.value = it
            }
        }
    }

    private fun getCurrentUserId() {
        currentUserId = userRepository.getCurrentUserId()
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            userRepository.getUser(userId).collect {
                _userState.value = it
            }
        }
    }
}