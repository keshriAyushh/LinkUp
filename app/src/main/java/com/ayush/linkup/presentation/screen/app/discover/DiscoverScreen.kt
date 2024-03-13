package com.ayush.linkup.presentation.screen.app.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.linkup.R
import com.ayush.linkup.presentation.LocalSnackbarState
import com.ayush.linkup.presentation.component.Loading
import com.ayush.linkup.presentation.component.Space
import com.ayush.linkup.presentation.component.UserItem
import com.ayush.linkup.presentation.viewmodels.DiscoverViewModel
import com.ayush.linkup.utils.State
import kotlinx.coroutines.launch

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel = hiltViewModel()
) {

    val search = rememberSaveable {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val snackbarState = LocalSnackbarState.current

    LaunchedEffect(key1 = search.value) {
        viewModel.searchUser(search.value)
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
            text = "Discover",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 25.sp,
            fontFamily = FontFamily(Font(R.font.nunito_bold))
        )

        Space(20.dp)

        OutlinedTextField(
            value = search.value,
            onValueChange = {
                search.value = it
            },
            modifier = Modifier
                .fillMaxWidth(1f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(10.dp)),
            placeholder = {
                Text(
                    text = "Search",
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    fontSize = 14.sp
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {

                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "search_icon"
                    )
                }
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
            )
        )
        Space(10.dp)
        viewModel.searchState.collectAsState().value.let {
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
                    LazyColumn {
                        items(it.data) { user ->
                            if (user.userId != viewModel.currentUserId) {
                                UserItem(user = user) {
                                    //Navigate to user profile
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
