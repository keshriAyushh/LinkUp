package com.ayush.linkup.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.ayush.linkup.presentation.navigation.AppNavigation
import com.ayush.linkup.presentation.ui.theme.LinkUpTheme
import dagger.hilt.android.AndroidEntryPoint

val LocalSnackbarState = compositionLocalOf<SnackbarHostState> { error("error") }
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LinkUpTheme {
                CompositionLocalProvider(LocalSnackbarState provides remember { SnackbarHostState() }) {
                    AppNavigation()
                }
            }
        }
    }
}
