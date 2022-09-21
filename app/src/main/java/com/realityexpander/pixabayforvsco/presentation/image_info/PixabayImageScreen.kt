package com.realityexpander.pixabayforvsco.presentation.image_info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.ramcosta.composedestinations.annotation.Destination
import com.realityexpander.pixabayforvsco.R
import com.realityexpander.pixabayforvsco.ui.theme.DarkBlue

@Composable
@Destination
fun PixabayImageScreen(
    id: String,
    viewModel: PixabayImageInfoViewModel = hiltViewModel()
) {
    val state = viewModel.state

    if (state.errorMessage == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue)
                .padding(16.dp)
        ) {
            state.pixabayImage?.let { image ->
                SubcomposeAsyncImage(
                    model = image.webformatURL,
                    contentDescription = image.tags,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 300.dp),
//                    error = painterResource(R.drawable.ic_android_black_24dp),
//                    loading = {
//                        CircularProgressIndicator()
//                    },
                    filterQuality = FilterQuality.High,
                ) {
                    if (this.painter.state is AsyncImagePainter.State.Loading) {
                        Column(
                            modifier = Modifier
                                .size(50.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else if (this.painter.state is AsyncImagePainter.State.Empty) {
                        Text("No Image Available")
                    } else if (this.painter.state is AsyncImagePainter.State.Error) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("Error Loading Image")
                            Icon(
                                painter = painterResource(id = R.drawable.ic_android_black_24dp),
                                contentDescription = null // decorative element
                            )
                        }
                    } else {
                        SubcomposeAsyncImageContent()
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = image.user,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tags: " + image.tags,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Comments: ${image.comments}",
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Likes: " + image.likes,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))


                Text(
                    text = "Downloads: " + image.downloads,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Favorites: " + image.favorites,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Show error message if any
        if (state.errorMessage != null && !state.isLoading) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colors.error
            )
        }
    }
}

// Make first char uppercase and rest lowercase
private fun String.toTitlecase(): String {
    return this.lowercase()
        .split(" ")
        .joinToString("") { string ->
            string.replaceFirstChar { firstChar ->
                firstChar.uppercaseChar()
            }
        }
}
