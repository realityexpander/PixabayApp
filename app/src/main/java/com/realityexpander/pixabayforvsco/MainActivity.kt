package com.realityexpander.pixabayforvsco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.realityexpander.pixabayforvsco.presentation.NavGraphs
import com.realityexpander.pixabayforvsco.ui.theme.PixabayAppTheme
import dagger.hilt.android.AndroidEntryPoint

// App based on this document:
// https://docs.google.com/document/d/1GrW-Ucykm09ZHBEaNRSLeL4grhRoXTscksg6n-OPA2Y/edit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            PixabayAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }
}