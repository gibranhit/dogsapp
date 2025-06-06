package com.gibran.dogsapp.presentation.ui

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.gibran.dogsapp.R
import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.presentation.event.DogListUiEvent
import com.gibran.dogsapp.presentation.state.DogListUiState
import com.gibran.dogsapp.presentation.ui.components.DogCard
import com.gibran.dogsapp.presentation.ui.components.ShimmerDogCard
import com.gibran.dogsapp.presentation.theme.BackgroundColor
import com.gibran.dogsapp.presentation.theme.DarkGrayText
import com.gibran.dogsapp.presentation.theme.Dimens.spacingMedium
import com.gibran.dogsapp.presentation.theme.Dimens.spacingXl
import com.gibran.dogsapp.presentation.viewmodel.DogListViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DogListScreen(
    viewModel: DogListViewModel = hiltViewModel(),
    onDogClick: (Dog) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedContentScope: AnimatedContentScope ? = null
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
    onEvent: (DogListUiEvent) -> Unit = {},
    onDogClick: (Dog) -> Unit = {},
    onBackPressed: () -> Unit = {},
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedContentScope: AnimatedContentScope? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.dogs_we_love),
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
                        contentPadding = PaddingValues(vertical = spacingMedium)
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
                        Spacer(modifier = Modifier.height(spacingXl))
                        Button(
                            onClick = { onEvent(DogListUiEvent.RetryLoadDogs) }
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = spacingMedium)
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun DogListContentLoadingPreview() {
    DogListContent(
        uiState = DogListUiState(isLoading = true),
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun DogListContentErrorPreview() {
    DogListContent(
        uiState = DogListUiState(
            isLoading = false,
            errorMessage = "Failed to load dogs. Please check your internet connection."
        )
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun DogListContentSuccessPreview() {
    DogListContent(
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
    )
}
