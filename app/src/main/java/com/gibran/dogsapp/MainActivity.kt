package com.gibran.dogsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gibran.dogsapp.presentation.navigation.DogsNavigation
import com.gibran.dogsapp.presentation.ui.theme.DogsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogsAppTheme {
                DogsNavigation()
            }
        }
    }
}
