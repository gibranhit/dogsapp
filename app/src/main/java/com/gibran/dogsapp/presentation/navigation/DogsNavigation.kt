package com.gibran.dogsapp.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.gibran.dogsapp.domain.model.Dog
import com.gibran.dogsapp.presentation.ui.DogDetailScreen
import com.gibran.dogsapp.presentation.ui.DogListScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DogsNavigation(
    navController: NavHostController = rememberNavController()
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = DogListRoute
        ) {
            composable<DogListRoute> {
                DogListScreen(
                    onDogClick = { dog ->
                        navController.navigate(
                            DogDetailRoute(
                                dogId = dog.id,
                                dogName = dog.name,
                                dogDescription = dog.description,
                                dogAge = dog.age,
                                dogImageUrl = dog.imageUrl
                            )
                        )
                    },
                    animatedContentScope = this@composable,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }

            composable<DogDetailRoute>{ backStackEntry ->
                val dogInfo = backStackEntry.toRoute<DogDetailRoute>()

                val dog = Dog(
                    id = dogInfo.dogId,
                    name = dogInfo.dogName,
                    description = dogInfo.dogDescription,
                    age = dogInfo.dogAge,
                    imageUrl = dogInfo.dogImageUrl
                )

                DogDetailScreen(
                    dog = dog,
                    onBackClick = { navController.navigateUp() },
                    animatedContentScope = this@composable,
                    sharedContentState = this@SharedTransitionLayout
                )
            }
        }
    }
}
