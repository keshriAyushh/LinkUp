package com.ayush.linkup.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.linkup.data.model.Comment
import com.ayush.linkup.data.model.Post
import com.ayush.linkup.data.model.User
import com.ayush.linkup.data.repository.CommentRepository
import com.ayush.linkup.data.repository.PostRepository
import com.ayush.linkup.data.repository.UserRepository
import com.ayush.linkup.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<State<User>>(State.None)
    val userState get() = _userState.asStateFlow()

    private val _postsState = MutableStateFlow<State<List<Post>>>(State.None)
    val postsState get() = _postsState.asStateFlow()

    private val _getCommentsState = MutableStateFlow<State<List<Comment>>>(State.None)
    val getCommentsState get() = _getCommentsState.asStateFlow()

    private val _addPostState = MutableStateFlow<State<Boolean>>(State.None)
    val addPostState get() = _addPostState.asStateFlow()

    private val _addCommentsState = MutableStateFlow<State<Boolean>>(State.None)
    val addCommentsState get() = _addCommentsState.asStateFlow()

    private val _deleteCommentState = MutableStateFlow<State<Boolean>>(State.None)
    val deleteCommentState get() = _deleteCommentState.asStateFlow()

    private val _deleteState = MutableStateFlow<State<Boolean>>(State.None)
    val deleteState get() = _deleteState.asStateFlow()

    fun getUser(userId: String) {
        viewModelScope.launch {
            userRepository.getUser(userId)
                .collect {
                    _userState.value = it
                }
        }
    }

    fun getPostsByUserId(userId: String) {
        viewModelScope.launch {
            postRepository.getAllPostsByUserId(userId)
                .collect {
                    _postsState.value = it
                }
        }
    }

    fun addComment(comment: Comment) {
        viewModelScope.launch {
            commentRepository.addComment(comment).collect {
                _addCommentsState.value = it
            }
        }
    }

    fun getAllComments(postId: String) {
        viewModelScope.launch {
            commentRepository.getAllComments(postId).collect {
                _getCommentsState.value = it
            }
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            commentRepository.deleteComment(comment).collect {
                _deleteCommentState.value = it
            }
        }
    }

    fun updateComments(value: Int, post: Post) {
        commentRepository.updateComments(value, post)
    }

    fun updateLike(post: Post, liked: Boolean) {
        postRepository.updateLike(post, liked)
    }
}