package com.ayush.linkup.presentation.screen.app.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.linkup.presentation.viewmodels.AuthViewModel

@Composable
fun ProfileScreen(
    onSignOutClick: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                viewModel.signOut()
                onSignOutClick()
            }
        ) {
            Text("Sign out")
        }
    }
}