package com.gibran.dogsapp.presentation.ui

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.presentation.event.DogListUiEvent
import com.gibran.dogsapp.presentation.state.DogListUiState
import com.gibran.dogsapp.presentation.ui.components.DogCard
import com.gibran.dogsapp.presentation.ui.components.ShimmerDogCard
import com.gibran.dogsapp.presentation.ui.theme.BackgroundColor
import com.gibran.dogsapp.presentation.ui.theme.DarkGrayText
import com.gibran.dogsapp.presentation.viewmodel.DogListViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DogListScreen(
    viewModel: DogListViewModel = hiltViewModel(),
    onDogClick: (Dog) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.dogs.isEmpty()) {
        if (uiState.dogs.isEmpty()){
            viewModel.handleEvent(DogListUiEvent.LoadDogs)
        }
    }

    DogListContent(
        uiState = uiState,
        onEvent = viewModel::handleEvent,
        onDogClick = onDogClick,
        onBackPressed = {
            (context as? ComponentActivity)?.finish()
        },
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun DogListContent(
    uiState: DogListUiState,
    onEvent: (DogListUiEvent) -> Unit,
    onDogClick: (Dog) -> Unit = {},
    onBackPressed: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dogs We Love",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrayText
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor),
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkGrayText
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .background(BackgroundColor)
            .fillMaxSize()) {

            when {
                uiState.isLoading -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(5) {
                            ShimmerDogCard()
                        }
                    }
                }

                uiState.errorMessage != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onEvent(DogListUiEvent.RetryLoadDogs) }
                        ) {
                            Text("Retry")
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.dogs) { dog ->
                            DogCard(
                                dog = dog,
                                onClick = onDogClick,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedContentScope = animatedContentScope
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DogListContentLoadingPreview() {
    DogListContentPreview(
        uiState = DogListUiState(isLoading = true),
        onEvent = {},
        onBackPressed = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DogListContentErrorPreview() {
    DogListContentPreview(
        uiState = DogListUiState(
            isLoading = false,
            errorMessage = "Failed to load dogs. Please check your internet connection."
        ),
        onEvent = {},
        onBackPressed = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DogListContentSuccessPreview() {
    DogListContentPreview(
        uiState = DogListUiState(
            dogs = listOf(
                Dog(
                    id = 1,
                    name = "Buddy",
                    description = "A friendly golden retriever who loves to play fetch",
                    age = 3,
                    imageUrl = "https://example.com/dog1.jpg"
                ),
                Dog(
                    id = 2,
                    name = "Luna",
                    description = "A playful husky with beautiful blue eyes",
                    age = 2,
                    imageUrl = "https://example.com/dog2.jpg"
                ),
                Dog(
                    id = 3,
                    name = "Max",
                    description = "A loyal german shepherd, great with kids",
                    age = 5,
                    imageUrl = "https://example.com/dog3.jpg"
                )
            ),
            isLoading = false,
            errorMessage = null
        ),
        onEvent = {},
        onBackPressed = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun DogListContentPreview(
    uiState: DogListUiState,
    onEvent: (DogListUiEvent) -> Unit,
    onBackPressed: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dogs We Love",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrayText
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(BackgroundColor)
                .fillMaxSize()
        ) {

            when {
                uiState.isLoading -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(5) {
                            ShimmerDogCard()
                        }
                    }
                }

                uiState.errorMessage != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onEvent(DogListUiEvent.RetryLoadDogs) }
                        ) {
                            Text("Retry")
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.dogs) { dog ->
                            DogCard(
                                dog = dog,
                            )
                        }
                    }
                }
            }
        }
    }
}
