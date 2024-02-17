package com.ayush.linkup.presentation.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ayush.linkup.R
import com.ayush.linkup.data.model.User
import com.ayush.linkup.presentation.LocalSnackbarState
import com.ayush.linkup.presentation.component.Loading
import com.ayush.linkup.presentation.component.Space
import com.ayush.linkup.presentation.navigation.LocalAuthNavigator
import com.ayush.linkup.presentation.viewmodels.AuthViewModel
import com.ayush.linkup.utils.Route
import com.ayush.linkup.utils.State
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarState = LocalSnackbarState.current
    val navigator = LocalAuthNavigator.current

    viewModel.signUpState.collectAsState().value.let {
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
                val name = rememberSaveable {
                    mutableStateOf("")
                }
                val email = rememberSaveable {
                    mutableStateOf("")
                }
                val password = rememberSaveable {
                    mutableStateOf("")
                }

                val showPasswordToggled = rememberSaveable {
                    mutableStateOf(false)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Space(100.dp)
                    val annotatedString = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                                fontSize = 50.sp
                            )
                        ) {
                            append("Link")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                                fontSize = 60.sp
                            )
                        ) {
                            append("Up")
                        }
                    }
                    Text(text = annotatedString)
                    Space(10.dp)
                    Text(
                        text = "Create your account",
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Space(50.dp)

                    OutlinedTextField(
                        value = name.value,
                        onValueChange = {
                            name.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        placeholder = {
                            Text(
                                text = "Name",
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.nunito_regular))
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "email_icon",
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

                    Space(10.dp)

                    OutlinedTextField(
                        value = email.value,
                        onValueChange = {
                            email.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        placeholder = {
                            Text(
                                text = "Email",
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.nunito_regular))
                            )
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(
                                    R.drawable.email
                                ),
                                contentDescription = "email_icon",
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
                        )
                    )

                    Space(10.dp)

                    OutlinedTextField(
                        value = password.value,
                        onValueChange = {
                            password.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        placeholder = {
                            Text(
                                text = "Password",
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.nunito_regular))
                            )
                        },
                        trailingIcon = {
                            Icon(
                                painter = if (!showPasswordToggled.value) painterResource(
                                    R.drawable.password_hidden
                                ) else painterResource(
                                    id = R.drawable.password_visible
                                ),
                                contentDescription = "password_icon",
                                modifier = Modifier.clickable {
                                    showPasswordToggled.value = !showPasswordToggled.value
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
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        visualTransformation = if (!showPasswordToggled.value)
                            PasswordVisualTransformation()
                        else
                            VisualTransformation.None
                    )

                    Space(10.dp)

                    Text(
                        text = "Forgot Password?",
                        fontFamily = FontFamily(Font(R.font.nunito_light)),
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable {
                                navigator.navigate(Route.ForgotPassScreen.route) {

                                }
                            },
                        color = Color.Gray,
                    )

                    Space(30.dp)

                    Button(
                        onClick = {
                            if (email.value.isNotBlank() && password.value.isNotBlank() && name.value.isNotBlank()) {
                                viewModel.signUp(
                                    User(
                                        email = email.value.trim(),
                                        password = password.value.trim(),
                                        name = name.value.trim()
                                    )
                                )
                            } else {
                                scope.launch {
                                    snackbarState
                                        .showSnackbar(
                                            message = "Please enter your data correctly!",
                                            duration = SnackbarDuration.Short
                                        )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(horizontal = 10.dp)
                            .clip(RoundedCornerShape(1.dp)),
                        contentPadding = PaddingValues(all = 10.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Sign up",
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            fontSize = 18.sp
                        )
                    }
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier
                            .fillMaxSize(1f)
                            .background(Color.Transparent)
                            .padding(horizontal = 10.dp, vertical = 50.dp)
                    ) {
                        Text(
                            text = "Already have an account? Click here to log in!",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = FontFamily(Font(R.font.nunito_medium)),
                            fontSize = 16.sp,
                            modifier = Modifier.clickable {
                                navigator.navigate(Route.LoginScreen.route) {
                                    popUpTo(navigator.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }

            is State.Success -> {
                if (it.data) {
                    navigator.navigate(Route.HostScreen.route) {
                        popUpTo(Route.HostScreen.route) {
                            inclusive = true
                        }
                    }
                } else {
                    scope.launch {
                        snackbarState
                            .showSnackbar(
                                message = "Login Failed! Please try again.",
                                duration = SnackbarDuration.Short
                            )
                    }
                }
            }
        }
    }


}