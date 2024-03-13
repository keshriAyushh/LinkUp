package com.ayush.linkup.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.linkup.data.model.Post
import com.ayush.linkup.data.repository.PostRepository
import com.ayush.linkup.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedPostsViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _likedPosts = MutableStateFlow<State<List<Post>>>(State.None)
    val likedPosts get() = _likedPosts.asStateFlow()

    private val _deletePost = MutableStateFlow<State<Boolean>>(State.None)
    val deletePost get() = _deletePost.asStateFlow()

    fun getAllLikedPosts(userId: String) {
        viewModelScope.launch {
            postRepository.getAllLikedPosts(userId)
                .collect {
                    _likedPosts.value = it
                }
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            postRepository.deletePost(post)
                .collect {
                    _deletePost.value = it
                }
        }
    }
}