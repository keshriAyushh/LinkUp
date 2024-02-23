package com.ayush.linkup.presentation.screen.auth

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.linkup.R
import com.ayush.linkup.presentation.LocalSnackbarState
import com.ayush.linkup.presentation.component.Space
import com.ayush.linkup.presentation.navigation.LocalAuthNavigator
import com.ayush.linkup.presentation.viewmodels.AuthViewModel
import com.ayush.linkup.utils.State
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForgotPassScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {

    val navigator = LocalAuthNavigator.current
    val snackbarState = LocalSnackbarState.current

    val scope = rememberCoroutineScope()


    val email = rememberSaveable {
        mutableStateOf("")
    }

    viewModel.forgotPassState.collectAsState().value.let {
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

            }

            State.None -> {

            }

            is State.Success -> {
                scope.launch {
                    snackbarState
                        .showSnackbar(
                            message = "Email sent!",
                            duration = SnackbarDuration.Short
                        )
                }
                Handler(Looper.getMainLooper())
                    .postDelayed(
                        { navigator.popBackStack() },
                        500
                    )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start

        ) {
            Space(100.dp)

            Text(
                text = "Please enter your email id to receive the password reset email!",
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurface
            )

            Space(20.dp)

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


            Space(30.dp)

            Button(
                onClick = {
                    if (email.value.isValidEmail()) {
                        viewModel.forgotPassword(email.value.trim())
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
                    text = "Send",
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 18.sp
                )
            }

            Space(10.dp)

            TextButton(
                onClick = {
                    navigator.popBackStack()
                },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(1.dp)),
                contentPadding = PaddingValues(all = 10.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Cancel",
                    fontFamily = FontFamily(Font(R.font.nunito_bold)),
                    fontSize = 18.sp
                )
            }
        }
    }


}

fun String.isValidEmail(): Boolean {
    var isValid = false
    trim()
    isValid = if (isNotBlank() && isNotEmpty()) {
        contains("@")
    } else {
        false
    }

    return isValid
}