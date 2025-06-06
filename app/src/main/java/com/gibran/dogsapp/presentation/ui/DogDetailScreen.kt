package com.gibran.dogsapp.presentation.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.gibran.dogsapp.R
import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.presentation.ui.components.shimmerEffect
import com.gibran.dogsapp.presentation.theme.BackgroundColor
import com.gibran.dogsapp.presentation.theme.CardBackground
import com.gibran.dogsapp.presentation.theme.DarkGrayText
import com.gibran.dogsapp.presentation.theme.Dimens
import com.gibran.dogsapp.presentation.theme.Dimens.dogCardDetailHeight
import com.gibran.dogsapp.presentation.theme.Dimens.spacingMedium
import com.gibran.dogsapp.presentation.theme.Dimens.spacingXs
import com.gibran.dogsapp.presentation.theme.Dimens.spacingXxs
import com.gibran.dogsapp.presentation.theme.DogsAppTheme
import com.gibran.dogsapp.presentation.theme.GrayText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun DogDetailScreen(
    dog: Dog,
    onBackClick: () -> Unit,
    animatedContentScope: AnimatedContentScope? = null,
    sharedContentState: SharedTransitionScope? = null
) {
    var imageState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = dog.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrayText,
                        modifier = Modifier.testTag("dogNameText")
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = DarkGrayText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dogCardDetailHeight)
                    .padding(Dimens.dogCardHorizontalPadding)
                    ,
                shape = RoundedCornerShape(Dimens.cornerRadiusLarge),
                elevation = CardDefaults.cardElevation(defaultElevation = spacingMedium)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (imageState is AsyncImagePainter.State.Loading || imageState is AsyncImagePainter.State.Empty) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmerEffect()
                        )
                    }
                    if(animatedContentScope != null && sharedContentState != null) {
                        with(sharedContentState) {
                            AsyncImage(
                                model = dog.imageUrl,
                                contentDescription = dog.name,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .sharedElement(
                                        sharedContentState = rememberSharedContentState(key = "dog_image_${dog.id}"),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                                    .clip(RoundedCornerShape(Dimens.cornerRadiusLarge)),
                                contentScale = ContentScale.Crop,
                                onState = { imageState = it }
                            )
                        }
                    } else {
                        AsyncImage(
                            model = dog.imageUrl,
                            contentDescription = dog.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(Dimens.cornerRadiusLarge)),
                            contentScale = ContentScale.Crop,
                            onState = { imageState = it }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f)
                                    ),
                                    startY = 200f
                                )
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.spacingXl))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.dogCardHorizontalPadding),
                shape = RoundedCornerShape(Dimens.cornerRadiusLarge),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = spacingXs)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.spacingXxl)
                ) {
                    Text(
                        text = dog.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrayText
                    )

                    Spacer(modifier = Modifier.height(spacingMedium))

                    Surface(
                        shape = RoundedCornerShape(Dimens.spacingXl),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = stringResource(id = R.string.almost_years_old, dog.age),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(
                                horizontal = Dimens.spacingLarge,
                                vertical = spacingMedium
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimens.spacingXxl))

                    Text(
                        text = stringResource(id = R.string.about_dog, dog.name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkGrayText
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingLarge))

                    Text(
                        text = dog.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = GrayText,
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingXxl))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingLarge)
                    ) {
                        InfoCard(
                            title = stringResource(id = R.string.personality),
                            content = getPersonalityFromDescription(dog.description),
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            title = stringResource(id = R.string.age_group),
                            content = getAgeGroup(dog.age),
                            modifier = Modifier.weight(1f).testTag("ageGroupText")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.spacingXxl))
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.spacingLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = spacingXxs)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.spacingXl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(Dimens.spacingSmall))
            Text(
                text = content,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun getPersonalityFromDescription(description: String): String {
    return when {
        description.contains("friendly", ignoreCase = true) -> "Friendly"
        description.contains("playful", ignoreCase = true) -> "Playful"
        description.contains("loyal", ignoreCase = true) -> "Loyal"
        description.contains("trust", ignoreCase = true) -> "Cautious"
        description.contains("bodyguard", ignoreCase = true) -> "Protective"
        description.contains("democracy", ignoreCase = true) -> "Leader"
        else -> "Unique"
    }
}

private fun getAgeGroup(age: Int): String {
    return when {
        age < 2 -> "Puppy"
        age < 7 -> "Adult"
        else -> "Senior"
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun DogDetailScreenPreview() {
    DogsAppTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                DogDetailScreen(
                    dog = Dog(
                        id = 1,
                        name = "Chief",
                        description = "Black (formerly) White with black spots, he don't trust anyone. This loyal companion has been through many adventures and has learned to be cautious around strangers. Despite his wariness, he forms deep bonds with those he trusts.",
                        age = 2,
                        imageUrl = "https://example.com/chief.jpg"
                    ),
                    onBackClick = {}
                )
            }
        }
    }
}
