package com.ayush.linkup.presentation.screen.app.posts

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ayush.linkup.R
import com.ayush.linkup.data.model.Post
import com.ayush.linkup.presentation.LocalSnackbarState
import com.ayush.linkup.presentation.component.Space
import com.ayush.linkup.presentation.navigation.LocalAppNavigator
import com.ayush.linkup.presentation.viewmodels.PostsViewModel
import com.ayush.linkup.utils.BtmRoute
import com.ayush.linkup.utils.State
import kotlinx.coroutines.launch

@Composable
fun AddPostScreen(
    viewModel: PostsViewModel = hiltViewModel()
) {

    val postText = rememberSaveable {
        mutableStateOf("")
    }

    val mediaUri = rememberSaveable {
        mutableStateOf<Uri?>(null)
    }

    val isUploading = rememberSaveable {
        mutableStateOf(false)
    }

    val mediaPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            mediaUri.value = uri
        }
    )

    val snackbarState = LocalSnackbarState.current
    val scope = rememberCoroutineScope()
    val navigator = LocalAppNavigator.current

    if (isUploading.value) {
        Uploading()
    }

    viewModel.addPostState.collectAsState().value.let {
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
                isUploading.value = true
            }

            State.None -> {
                isUploading.value = false
            }

            is State.Success -> {
                isUploading.value = false
                if (it.data) {
                    scope.launch {
                        snackbarState
                            .showSnackbar(
                                message = "Post added successfully!",
                                duration = SnackbarDuration.Short
                            )
                    }
                    postText.value = ""
                    navigator.navigate(BtmRoute.PostsScreen.route) {
                        popUpTo(BtmRoute.PostsScreen.route) {
                            inclusive = true
                        }
                    }
                } else {
                    scope.launch {
                        snackbarState
                            .showSnackbar(
                                message = "Post creation failed!",
                                duration = SnackbarDuration.Short
                            )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "Create Post",
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 25.sp
            )

            Space(20.dp)

            OutlinedTextField(
                value = postText.value,
                onValueChange = {
                    postText.value = it
                },
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Enter text",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_regular))
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
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                minLines = 1,
                maxLines = 10,
                shape = RoundedCornerShape(10.dp)
            )
            Space(10.dp)

            mediaUri.value?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = mediaUri.value,
                        contentDescription = "selected_media",
                        modifier = Modifier.size(360.dp)
                    )
                }

            }

            Space(10.dp)
            Row(
                modifier = Modifier.fillMaxWidth(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        mediaPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Add Media",
                        fontFamily = FontFamily(Font(R.font.nunito_medium))
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        isUploading.value = true
                        if ((postText.value.isNotBlank() || postText.value.isNotEmpty())) {
                            if (mediaUri.value != null) {
                                viewModel.addPost(
                                    Post(
                                        text = postText.value.trim(),
                                        media = mediaUri.value.toString()
                                    )
                                )
                            } else {
                                viewModel.addPost(
                                    Post(
                                        text = postText.value.trim(),
                                        media = null
                                    )
                                )
                            }
                        } else {
                            scope.launch {
                                snackbarState
                                    .showSnackbar(
                                        message = "You must enter some text or select a picture to add a post!",
                                        duration = SnackbarDuration.Short
                                    )
                            }
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Post",
                        fontFamily = FontFamily(Font(R.font.nunito_medium))
                    )
                }
            }
        }
    }
}

@Composable
fun Uploading() {
    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(Color.White)
            .alpha(0.4f),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            strokeCap = StrokeCap.Round,
            strokeWidth = 10.dp,
            modifier = Modifier.size(100.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Uploading",
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = FontFamily(Font(R.font.nunito_medium)),
            fontSize = 18.sp
        )
    }
}
