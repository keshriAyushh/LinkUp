package com.ayush.linkup.presentation.screen.app.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.linkup.R
import com.ayush.linkup.data.model.User
import com.ayush.linkup.presentation.LocalSnackbarState
import com.ayush.linkup.presentation.component.Loading
import com.ayush.linkup.presentation.component.PostItem
import com.ayush.linkup.presentation.component.Space
import com.ayush.linkup.presentation.navigation.LocalAppNavigator
import com.ayush.linkup.presentation.viewmodels.PostsViewModel
import com.ayush.linkup.utils.State
import kotlinx.coroutines.launch

@Composable
fun PostsScreen(
    viewModel: PostsViewModel = hiltViewModel()
) {

    val navigator = LocalAppNavigator.current
    val scope = rememberCoroutineScope()
    val snackbarState = LocalSnackbarState.current
    var user: User = User()

    LaunchedEffect(true) {
        viewModel.getAllPosts()
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

            State.Loading -> {}
            State.None -> {}
            is State.Success -> {
                user = it.data
            }
        }
    }

    viewModel.allPostsState.collectAsState().value.let {
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
                        Text(
                            text = "All Posts",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 25.sp,
                            fontFamily = FontFamily(Font(R.font.nunito_bold))
                        )
                    }
                    items(it.data) { data ->
                        LaunchedEffect(data) {
                            viewModel.getUser(data.postedBy)
                        }
                        PostItem(
                            post = data,
                            user = user,
                            viewModel = viewModel
                        )
                        Space(10.dp)
                    }
                }
            }
        }
    }


}
