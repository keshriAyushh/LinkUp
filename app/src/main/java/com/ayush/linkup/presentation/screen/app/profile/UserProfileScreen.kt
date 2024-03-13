package com.ayush.linkup.presentation.screen.app.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ayush.linkup.R
import com.ayush.linkup.data.model.Comment
import com.ayush.linkup.presentation.LocalSnackbarState
import com.ayush.linkup.presentation.component.CommentItem
import com.ayush.linkup.presentation.component.Loading
import com.ayush.linkup.presentation.component.PostItem
import com.ayush.linkup.presentation.component.Space
import com.ayush.linkup.presentation.navigation.LocalAppNavigator
import com.ayush.linkup.presentation.screen.app.posts.Post
import com.ayush.linkup.presentation.viewmodels.UserProfileViewModel
import com.ayush.linkup.utils.State
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    currentUserId: String,
    userId: String,
    viewModel: UserProfileViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val snackbarState = LocalSnackbarState.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getUser(userId)
        viewModel.getPostsByUserId(userId)
    }

    val showBottomSheet = rememberSaveable {
        mutableStateOf(false)
    }

    val navigator = LocalAppNavigator.current

    val commentText = rememberSaveable {
        mutableStateOf("")
    }

    val postId = rememberSaveable {
        mutableStateOf("")
    }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
            },
            sheetState = bottomSheetState,
        ) {
            LaunchedEffect(key1 = showBottomSheet.value) {
                if (postId.value != "") {
                    Log.d("postId", postId.value)
                    viewModel.getAllComments(postId.value)
                }
            }

            viewModel.getCommentsState.collectAsState().value.let {
                when (it) {
                    is State.Error -> {
                        scope.launch {
                            snackbarState.showSnackbar(
                                message = it.message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }

                    State.Loading -> {
                        Loading()
                    }

                    State.None -> {}
                    is State.Success -> {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(MaterialTheme.colorScheme.surface),
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .align(Alignment.TopCenter),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start
                            ) {
                                items(it.data) { comment ->
                                    CommentItem(
                                        comment,
                                        currentUserId,
                                        userId
                                    ) { deleteComment ->
                                        viewModel.deleteComment(deleteComment)
                                        viewModel.updateComments(value = -1, post = Post)
                                    }
                                }
                                item { Space(100.dp) }

                            }
                            OutlinedTextField(
                                value = commentText.value,
                                onValueChange = {
                                    commentText.value = it
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .align(Alignment.BottomCenter)
                                    .padding(vertical = 30.dp),
                                placeholder = {
                                    Text(
                                        text = "Add a comment",
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.nunito_regular))
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Send,
                                        contentDescription = "email_icon",
                                        modifier = Modifier.clickable {
                                            if (commentText.value.isNotEmpty()) {
                                                viewModel.addComment(
                                                    Comment(
                                                        comment = commentText.value.trim(),
                                                        commentedBy = currentUserId!!,
                                                        onPost = postId.value,
                                                    )
                                                )
                                                viewModel.updateComments(value = 1, post = Post)
                                                commentText.value = ""
                                            }
                                        }
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    cursorColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    focusedTrailingIconColor = MaterialTheme.colorScheme.secondaryContainer,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                                    unfocusedIndicatorColor = Color.Gray,
                                    unfocusedTextColor = Color.LightGray,
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                )
                            )

                        }
                    }
                }
            }
        }
    }

    viewModel.userState.collectAsState().value.let {
        when (it) {
            is State.Error -> {
                scope.launch {
                    snackbarState
                        .showSnackbar(
                            message = it.message,
                            duration = SnackbarDuration.Short
                        )
                }
            }

            State.Loading -> {
                Loading()
            }

            State.None -> {}

            is State.Success -> {
                val user = it.data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(horizontal = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                navigator.popBackStack()
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "back_button",
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                //Do something, idk
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_friend),
                                contentDescription = "back_button",
                            )
                        }
                    }

                    Space(50.dp)

                    if (user.pfp != "") {
                        AsyncImage(
                            model = user.pfp,
                            contentDescription = "profile_photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(70.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = "dp",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(70.dp)
                        )
                    }

                    Space(10.dp)
                    Text(
                        text = user.name,
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Space(20.dp)
                    Text(
                        text = "All Posts",
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                    )

                    viewModel.postsState.collectAsState().value.let {
                        when (it) {
                            is State.Error -> {
                                scope.launch {
                                    snackbarState.showSnackbar(
                                        message = it.message,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }

                            State.Loading -> {
                                Loading()
                            }

                            State.None -> {

                            }

                            is State.Success -> {
                                val posts = it.data
                                LazyColumn {
                                    items(posts) { post ->
                                        PostItem(
                                            post = post,
                                            currentUserId = currentUserId,
                                            onDeleteClick = { deletePost ->
                                                if (currentUserId == post.postedBy) {

                                                }
                                            },
                                            onCommentsClick = { commentPost ->
                                                postId.value = commentPost.postId
                                                showBottomSheet.value = true
                                            },
                                            onShareClick = {

                                            },
                                            onLikeClick = { likedPost, isLiked ->
                                                viewModel.updateLike(likedPost, isLiked)
                                            },
                                        ) { posterId ->

                                        }

                                        Space(10.dp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

var post = com.ayush.linkup.data.model.Post()