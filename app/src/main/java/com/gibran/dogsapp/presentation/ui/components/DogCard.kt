package com.gibran.dogsapp.presentation.ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.gibran.dogsapp.R
import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.presentation.ui.theme.CardBackground
import com.gibran.dogsapp.presentation.ui.theme.DarkGrayText
import com.gibran.dogsapp.presentation.ui.theme.Dimens
import com.gibran.dogsapp.presentation.ui.theme.GrayText
import com.gibran.dogsapp.presentation.ui.theme.ShimmerLight
import com.gibran.dogsapp.presentation.ui.theme.ShimmerMedium

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "shimmer"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                ShimmerLight,
                ShimmerMedium,
                ShimmerLight
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Composable
fun ShimmerDogCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.dogCardHeight)
            .padding(
                horizontal = Dimens.dogCardHorizontalPadding,
                vertical = Dimens.dogCardVerticalPadding
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .width(Dimens.dogCardImageWidth)
                    .clip(
                        RoundedCornerShape(
                            topEnd = Dimens.cornerRadiusLarge,
                            topStart = Dimens.cornerRadiusLarge,
                            bottomStart = Dimens.cornerRadiusLarge
                        )
                    )
                    .background(CardBackground)
                    .height(Dimens.dogCardHeight),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(Dimens.cornerRadiusLarge))
                        .shimmerEffect()
                )
            }

            Column {
                Spacer(modifier = Modifier.height(Dimens.dogCardTextTopOffset))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.dogCardHeight)
                        .clip(
                            RoundedCornerShape(
                                topEnd = Dimens.cornerRadiusLarge,
                                bottomEnd = Dimens.cornerRadiusLarge
                            )
                        )
                        .background(CardBackground)
                        .padding(
                            horizontal = Dimens.dogCardContentPadding,
                            vertical = Dimens.spacingMedium
                        ),
                ) {
                    Box(
                        modifier = Modifier
                            .height(Dimens.shimmerTitleHeight)
                            .fillMaxWidth(0.6f)
                            .clip(RoundedCornerShape(Dimens.cornerRadiusSmall))
                            .shimmerEffect()
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                    Box(
                        modifier = Modifier
                            .height(Dimens.shimmerTextHeight)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(Dimens.cornerRadiusSmall))
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                    Box(
                        modifier = Modifier
                            .height(Dimens.shimmerTextHeight)
                            .fillMaxWidth(0.8f)
                            .clip(RoundedCornerShape(Dimens.cornerRadiusSmall))
                            .shimmerEffect()
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingLarge))

                    // Age shimmer
                    Box(
                        modifier = Modifier
                            .height(Dimens.shimmerTextHeight)
                            .fillMaxWidth(0.4f)
                            .clip(RoundedCornerShape(Dimens.cornerRadiusSmall))
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DogCard(
    modifier: Modifier = Modifier,
    dog: Dog,
    onClick: (Dog) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedContentScope: AnimatedContentScope? = null
) {
    var imageState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.dogCardHeight)
            .padding(
                horizontal = Dimens.dogCardHorizontalPadding,
                vertical = Dimens.dogCardVerticalPadding
            )
            .clickable { onClick(dog) },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Dog Image
            Column(
                modifier = Modifier
                    .width(Dimens.dogCardImageWidth)
                    .clip(
                        RoundedCornerShape(
                            topEnd = Dimens.cornerRadiusLarge,
                            topStart = Dimens.cornerRadiusLarge,
                            bottomStart = Dimens.cornerRadiusLarge
                        )
                    )
                    .background(CardBackground)
                    .height(Dimens.dogCardHeight),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(Dimens.cornerRadiusLarge))
                ) {
                    if (imageState is AsyncImagePainter.State.Loading || imageState is AsyncImagePainter.State.Empty) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmerEffect()
                        )
                    }
                    if(animatedContentScope != null && sharedTransitionScope != null) {
                        with(sharedTransitionScope) {
                            AsyncImage(
                                model = dog.imageUrl,
                                contentDescription = dog.name,
                                modifier =Modifier.fillMaxSize().sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "dog_image_${dog.id}"),
                                    animatedVisibilityScope = animatedContentScope
                                ),
                                contentScale = ContentScale.Crop,
                                onState = { imageState = it }
                            )
                        }
                    } else{
                        AsyncImage(
                            model = dog.imageUrl,
                            contentDescription = dog.name,
                            modifier =Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            onState = { imageState = it }
                        )
                    }
                }
            }

            // Dog Info
            Column {
                Spacer(modifier = Modifier.height(Dimens.dogCardTextTopOffset))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.dogCardHeight)
                        .clip(
                            RoundedCornerShape(
                                topEnd = Dimens.cornerRadiusLarge,
                                bottomEnd = Dimens.cornerRadiusLarge
                            )
                        )
                        .background(CardBackground)
                        .padding(
                            horizontal = Dimens.dogCardContentPadding,
                            vertical = Dimens.spacingMedium
                        ),
                ) {
                    Text(
                        text = dog.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkGrayText
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingMedium))

                    Text(
                        text = dog.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = GrayText,
                    )

                    Spacer(modifier = Modifier.height(Dimens.spacingLarge))

                    Text(
                        text = stringResource(id = R.string.almost_years, dog.age),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = DarkGrayText
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun DogCardPreview() {
    DogCard(
        dog = Dog(
            id = 1,
            name = "Chief",
            description = "Black (formerly) White with black spots, he don't trust anyone",
            age = 2,
            imageUrl = "https://example.com/chief.jpg"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ShimmerDogCardPreview() {
    ShimmerDogCard()
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true, name = "Multiple Cards")
@Composable
private fun DogCardsPreview() {
    Column {
        DogCard(
            dog = Dog(
                id = 1,
                name = "Chief",
                description = "Black (formerly) White with black spots, he don't trust anyone",
                age = 2,
                imageUrl = "https://example.com/chief.jpg"
            )
        )
        DogCard(
            dog = Dog(
                id = 2,
                name = "Spots",
                description = "White with black spots, he is a bodyguard",
                age = 2,
                imageUrl = "https://example.com/spots.jpg"
            )
        )
        DogCard(
            dog = Dog(
                id = 3,
                name = "King",
                description = "Red, Brown, White, he believes in democracy",
                age = 2,
                imageUrl = "https://example.com/king.jpg"
            )
        )
    }
}
