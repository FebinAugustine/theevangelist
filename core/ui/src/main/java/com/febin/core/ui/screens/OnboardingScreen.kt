package com.febin.core.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.febin.core.ui.R // Changed import
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onOnboardingFinished: () -> Unit) {
    val pageCount = 3 // Total number of onboarding pages
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val scope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingPageData(
            imageRes = R.drawable.ic_launcher_playstore, // Using the playstore icon
            title = "Welcome to The Evangelist",
            description = "Let There Be Light"
        ),
        OnboardingPageData(
            imageRes = R.drawable.theevangelist_logo, // Changed to theevangelist_logo
            title = "Pave The Way",
            description = "Make The Way Straight For Our Lord."
        ),
        OnboardingPageData(
            imageRes = R.drawable.theevangelist_logo, // Changed to theevangelist_logo
            title = "Get Started",
            description = "Multitude Proclaims This Sathvartha"
        )
    )

    Column(Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            pageIndex -> // pageIndex is the current page
            OnboardingPage(pages[pageIndex])
        }

         Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
         ) {
            repeat(pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(10.dp)
                        .background(color, CircleShape)
                )
            }
         }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pagerState.currentPage < pageCount - 1) {
                TextButton(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSurface),
                    onClick = { onOnboardingFinished() }) {
                    Text("Skip"
                        , color = MaterialTheme.colorScheme.primary
                        , style = MaterialTheme.typography.titleSmall
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSurface),
                    onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }

                }) {
                    Text("Next"
                        , color = MaterialTheme.colorScheme.primary
                        , style = MaterialTheme.typography.titleSmall
                    )
                }
            } else {
                Button(
                    onClick = { onOnboardingFinished() },
                    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.onSurface)
                ) {
                    Text("Get Started"
                        , color = MaterialTheme.colorScheme.primary
                        , style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

data class OnboardingPageData(
    val imageRes: Int,
    val title: String,
    val description: String
)

@Composable
fun OnboardingPage(pageData: OnboardingPageData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = pageData.imageRes),
            contentDescription = pageData.title,
            modifier = Modifier.size(200.dp), // Adjust size as needed
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = pageData.title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = pageData.description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

