package com.ayush.linkup.presentation.screen.onboarding

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayush.linkup.R
import com.ayush.linkup.presentation.component.AnimatedButton
import com.ayush.linkup.presentation.component.Space
import com.ayush.linkup.presentation.navigation.LocalAuthNavigator
import com.ayush.linkup.presentation.viewmodels.OnboardingViewModel
import com.ayush.linkup.utils.Route
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {

    val navigator = LocalAuthNavigator.current

    val pages = listOf(
        OnboardingPage.First,
        OnboardingPage.Second,
        OnboardingPage.Third,
        OnboardingPage.Fourth,
    )

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val isFinishVisible = rememberSaveable {
        mutableStateOf(false)
    }

    val animatePb = animateFloatAsState(
        targetValue = when (pagerState.currentPage) {
            0 -> 0.25f
            1 -> 0.50f
            2 -> 0.75f
            else -> 0.25f
        },
        label = "progressBar animation",
        finishedListener = {
            isFinishVisible.value = true
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            count = 4,
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { position ->
            PagerScreen(onboardingPage = pages[position])
        }

        if (pagerState.currentPage != 3) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 50.dp),
                contentAlignment = Alignment.Center

            ) {
                CircularProgressIndicator(
                    progress = animatePb.value,
                    color = MaterialTheme.colorScheme.onSurface,
                    strokeWidth = 4.dp,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier.size(100.dp)
                )
                FilledIconButton(
                    onClick = {
                        if (pagerState.currentPage < 3) {
                            scope.launch {
                                pagerState.scrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .size(80.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                ) {
                    Icon(
                        imageVector = Icons.Rounded.NavigateNext,
                        contentDescription = "next button",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

        } else {
            AnimatedButton(
                pagerState = pagerState,
                text = "Finish",
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(horizontal = 20.dp)
            ) {
                onboardingViewModel.isOnboarded(completed = true)
                navigator.popBackStack()
                navigator.navigate(Route.SignupScreen.route)
            }
        }

    }
}

@Composable
fun PagerScreen(onboardingPage: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Space(100.dp)
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = onboardingPage.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            color = MaterialTheme.colorScheme.onSurface
        )
        Space(20.dp)
        if (onboardingPage.image != null) {
            Image(
                painter = painterResource(id = onboardingPage.image),
                contentDescription = "pager image",
                modifier = Modifier
                    .size(300.dp)
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = onboardingPage.description,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.nunito_medium)),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}