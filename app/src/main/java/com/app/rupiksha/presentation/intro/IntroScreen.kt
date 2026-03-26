package com.app.rupiksha.presentation.intro

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app.rupiksha.R
import com.app.rupiksha.models.IntroModal
import com.app.rupiksha.presentation.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroScreen(navController: NavController) {
    val introList = listOf(
        IntroModal(stringResource(R.string.text_intro_1), R.drawable.intro_1),
        IntroModal(stringResource(R.string.text_intro_2), R.drawable.intro_2),
        IntroModal(stringResource(R.string.text_intro_3), R.drawable.intro_3)
    )
    
    val pagerState = rememberPagerState(pageCount = { introList.size })
    val scope = rememberCoroutineScope()
    
    val bgColors = listOf(
        Color(0xFF90CAF9),
        Color(0xFFE6EE9C),
        Color(0xFF81D4FA)
    )
    
    val currentColor = bgColors.getOrElse(pagerState.currentPage) { bgColors[0] }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(currentColor)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                }) {
                    Text(text = "Skip", color = Color.Black)
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                IntroPage(introModal = introList[page])
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(introList.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(10.dp)
                                .background(color, shape = MaterialTheme.shapes.small)
                        )
                    }
                }

                Button(
                    onClick = {
                        if (pagerState.currentPage < introList.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Intro.route) { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(
                        text = if (pagerState.currentPage == introList.size - 1) "FINISH" else "NEXT",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun IntroPage(introModal: IntroModal) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = introModal.introImage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = introModal.text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}
