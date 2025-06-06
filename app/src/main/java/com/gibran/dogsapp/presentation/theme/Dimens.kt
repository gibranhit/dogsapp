package com.gibran.dogsapp.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Dimens {
    // DogCard dimensions
    val dogCardDetailHeight: Dp = 400.dp
    val dogCardHeight: Dp = 200.dp
    val dogCardImageWidth: Dp = 120.dp
    val dogCardHorizontalPadding: Dp = 16.dp
    val dogCardVerticalPadding: Dp = 8.dp
    val dogCardContentPadding: Dp = 16.dp
    val dogCardTextTopOffset: Dp = 24.dp

    // Corner radius
    val cornerRadiusLarge: Dp = 16.dp
    val cornerRadiusSmall: Dp = 4.dp

    // Spacing
    val spacingXxs: Dp = 2.dp
    val spacingXs: Dp = 4.dp
    val spacingSmall: Dp = 6.dp
    val spacingMedium: Dp = 8.dp
    val spacingLarge: Dp = 12.dp
    val spacingXl: Dp = 16.dp
    val spacingXxl: Dp = 20.dp
    val spacingHuge: Dp = 24.dp

    // Shimmer dimensions
    val shimmerTitleHeight: Dp = 24.dp
    val shimmerTextHeight: Dp = 16.dp
}

// Extension function to get dimension from resources (if needed for dynamic sizing)
@Composable
fun dimensionResource(id: Int): Dp {
    return LocalContext.current.resources.getDimension(id).dp
}