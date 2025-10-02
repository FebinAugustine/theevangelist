//package com.febin.features.userdashboard.ui.screens.screens
//
//import androidx.compose.foundation.Image
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CenterAlignedTopAppBar
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.rememberTopAppBarState
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.nestedscroll.nestedScroll
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import com.febin.core.ui.R
//
//// Example data class
//data class MyDummyObject(
//    val id: Int,
//    val title: String,
//    val no: Int,
//    val color: Color
//)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(
//    modifier: Modifier = Modifier
//) {
//
//
//// create the list with specific colors
//    val dummyObjects: List<MyDummyObject> = listOf(
//        MyDummyObject(1, "Positive", 7, Color(0xFFE3F2FD)),
//        MyDummyObject(2, "Neutral", 5,  Color(0xFFFFF9C4)),
//        MyDummyObject(3, "Negative", 8, Color(0xFFFFCDD2)),
//        MyDummyObject(4, "Total", 22,   Color(0xFFC8E6C9))
//    )
//    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
//    Scaffold(
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
//
//        topBar = {
//            CenterAlignedTopAppBar(
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.onPrimary,
//                    scrolledContainerColor = Color.Unspecified,
//                    navigationIconContentColor = Color.Unspecified,
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                    actionIconContentColor = Color.Unspecified
//                ),
//                title = {
//                    Text(
//                        "The Evangelist",
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                },
//
////                navigationIcon = {
////                    IconButton(onClick = { /* do something */ }) {
////                        Icon(
////                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
////                            contentDescription = "Localized description"
////                        )
////                    }
////                },
//                actions = {
//                    IconButton(onClick = { /* do something */ }) {
//                        Icon(
//                            imageVector = Icons.Filled.Person,
//                            contentDescription = "Localized description"
//                        )
//                    }
//                    IconButton(onClick = { /* do something */ }) {
//                        Icon(
//                            imageVector = Icons.Filled.Menu,
//                            contentDescription = "Localized description"
//                        )
//                    }
//                },
//
//                scrollBehavior = scrollBehavior,
//            )
//        },
//    ) {paddingValues ->
//        Column(
//            modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState()),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.Start
//        ) {
//            Text(
//                text = "Your Dashboard",
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.padding(16.dp),
//                color = MaterialTheme.colorScheme.primary
//            )
//            Card(
//                modifier = Modifier.fillMaxWidth().padding(8.dp),
//                shape = RoundedCornerShape(
//                    topStart = 15.dp,
//                    topEnd = 15.dp,
//                    bottomStart = 15.dp,
//                    bottomEnd = 15.dp
//                ),
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                ),
//                elevation = CardDefaults.cardElevation(2.dp)
//
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(2.dp),
//                    horizontalArrangement = Arrangement.Start,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.theevangelist_logo),
//                        contentDescription = "App Logo",
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .size(100.dp),
//                    )
//                    Column(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalAlignment = Alignment.Start
//                    ) {
//                        Text(
//                            text = "Yesu Febin",
//                            style = MaterialTheme.typography.headlineSmall,
//                            modifier = Modifier.padding(0.dp),
//                            color = MaterialTheme.colorScheme.onPrimary
//                        )
//                        Text(
//                            text = "febinaugustine7@gmail.com",
//                            style = MaterialTheme.typography.bodyLarge,
//                            modifier = Modifier.padding(0.dp),
//                            color = MaterialTheme.colorScheme.onPrimary
//                        )
//
//                    }
//
//
//                }
//            }
//            Column(
//                modifier = Modifier.fillMaxWidth().padding(16.dp),
//                horizontalAlignment = Alignment.Start
//            ) {
//
//
//                Text(
//                    text = "Your Reports",
//                    style = MaterialTheme.typography.headlineSmall,
//                    modifier = Modifier.padding(0.dp),
//                    color = MaterialTheme.colorScheme.primary
//                )
//
//                // Group list into chunks of 2
//                dummyObjects.chunked(2).forEach { rowItems ->
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        rowItems.forEach { dummy ->
//                            Card(
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .height(120.dp), // adjust height
//                                elevation = CardDefaults.cardElevation(4.dp),
//                                shape = RoundedCornerShape(12.dp),
//                                colors = CardDefaults.cardColors(containerColor = dummy.color)
//                            ) {
//                                Column(
//                                    modifier = Modifier
//                                        .padding(12.dp)
//                                        .fillMaxSize(),
//                                    verticalArrangement = Arrangement.Center
//                                ) {
//                                    Text(
//                                        text = dummy.title,
//                                        style = MaterialTheme.typography.titleMedium,
//                                        color = MaterialTheme.colorScheme.onSurface
//                                    )
//                                    Spacer(modifier = Modifier.height(4.dp))
//                                    Text(
//                                        text = dummy.no.toString(),
//                                        style = MaterialTheme.typography.headlineMedium,
//                                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                                    )
//                                }
//                            }
//                        }
//
//                        // If only 1 item in row, add spacer to balance layout
//                        if (rowItems.size < 2) {
//                            Spacer(modifier = Modifier.weight(1f))
//                        }
//                    }
//                }
//
//            }
//
//        }
//    }
//
//}