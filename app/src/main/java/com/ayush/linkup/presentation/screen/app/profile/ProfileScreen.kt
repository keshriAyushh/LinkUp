package com.ayush.linkup.presentation.screen.app.profile

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ayush.linkup.R
import com.ayush.linkup.presentation.LocalSnackbarState
import com.ayush.linkup.presentation.component.Loading
import com.ayush.linkup.presentation.component.ProfilePostItem
import com.ayush.linkup.presentation.component.Space
import com.ayush.linkup.presentation.navigation.LocalAppNavigator
import com.ayush.linkup.presentation.viewmodels.ProfileViewModel
import com.ayush.linkup.utils.Route
import com.ayush.linkup.utils.State
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileScreen(
    onSignOutClick: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val snackbarState = LocalSnackbarState.current
    val navigator = LocalAppNavigator.current
    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = rememberSaveable {
        mutableStateOf(false)
    }
    val rotateAnim = animateFloatAsState(
        targetValue = if (showBottomSheet.value) 270f else 0f,
        label = "rotation animation"
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserData()
    }

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Space(10.dp)
                Text(
                    text = "More options",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Space(10.dp)
                Button(
                    onClick = {
                        viewModel.signOut()
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 10.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Sign out",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    )
                }
                Space(10.dp)
                Button(
                    onClick = {
                        viewModel.deleteAccount()
                        navigator.navigate(Route.HostScreen.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onError,
                        containerColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 10.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Delete Account",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    )
                }
            }
        }
    }



    viewModel.userState.collectAsState().value.let {
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
                Column(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Your Profile",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 25.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                    )

                    Space(30.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.wrapContentSize()
                        ) {
                            if (it.data.pfp != "") {
                                AsyncImage(
                                    model = it.data.pfp,
                                    contentDescription = "profile_photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Rounded.AccountCircle,
                                    contentDescription = "dp",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .size(70.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(20.dp))

                            Text(
                                text = it.data.name,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            )
                        }

                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = "menu",
                            modifier = Modifier
                                .rotate(rotateAnim.value)
                                .clickable {
                                    showBottomSheet.value = true
                                }
                        )
                    }
                    Space(10.dp)
                    Text(
                        text = it.data.bio,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_medium))
                    )
                    Space(10.dp)


                    Text(
                        text = "All Posts",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_bold))
                    )
                    viewModel.postState.collectAsState().value.let {
                        when (it) {
                            is State.Error -> {

                            }

                            State.Loading -> {

                            }

                            State.None -> {

                            }

                            is State.Success -> {
                                LazyColumn {
                                    items(it.data) {
                                        ProfilePostItem(post = it) { post ->
                                            viewModel.deletePost(post)
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
}