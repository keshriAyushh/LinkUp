package com.ayush.linkup.presentation.screen.app.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.linkup.R
import com.ayush.linkup.presentation.LocalSnackbarState
import com.ayush.linkup.presentation.component.Loading
import com.ayush.linkup.presentation.component.PostItem
import com.ayush.linkup.presentation.component.Space
import com.ayush.linkup.presentation.navigation.LocalAppNavigator
import com.ayush.linkup.presentation.viewmodels.LikedPostsViewModel
import com.ayush.linkup.utils.State
import kotlinx.coroutines.launch

@Composable
fun LikedPostsScreen(
    currentUserId: String,
    viewModel: LikedPostsViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val snackbarState = LocalSnackbarState.current

    val navigator = LocalAppNavigator.current


    LaunchedEffect(key1 = Unit) {
        viewModel.getAllLikedPosts(currentUserId)
    }

    viewModel.likedPosts.collectAsState().value.let {
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

            State.None -> {

            }

            is State.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(horizontal = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
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
                                    modifier = Modifier
                                        .size(20.dp)

                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Your likes",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 25.sp,
                                fontFamily = FontFamily(Font(R.font.nunito_bold))
                            )
                        }
                        Space(10.dp)
                    }

                    items(it.data) { post ->
                        PostItem(
                            post = post,
                            currentUserId = currentUserId,
                            onDeleteClick = {},
                            onCommentsClick = {},
                            onShareClick = {},
                            onLikeClick = { likedPost, liked ->

                            }
                        ) {

                        }
                    }
                }
            }
        }
    }
}