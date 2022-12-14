package com.realityexpander.pixabayforvsco.presentation.image_info

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment.TopRight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.github.chrisbanes.photoview.PhotoView
import com.ramcosta.composedestinations.annotation.Destination
import com.realityexpander.pixabayforvsco.R
import com.realityexpander.pixabayforvsco.ui.theme.DarkBlue


@GlideModule
class AppNameGlideModule : AppGlideModule()

@Composable
@Destination
fun PixabayImageScreen(
    id: String,
    viewModel: ImageInfoViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state = viewModel.state

    if (state.errorMessage == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())

        ) {
            state.pixabayImage?.let { image ->
                SubcomposeAsyncImage(
                    contentDescription = image.tags,
                    model = image.webformatURL,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 300.dp),
//                    error = painterResource(R.drawable.ic_android_black_24dp),
//                    loading = {
//                        CircularProgressIndicator()
//                    },
                    filterQuality = FilterQuality.High,
                ) {
                    when (this.painter.state) {
                        is AsyncImagePainter.State.Loading -> {
                            Column(
                                modifier = Modifier
                                    .size(50.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is AsyncImagePainter.State.Empty -> {
                            Text("No Image Available")
                        }
                        is AsyncImagePainter.State.Error -> {
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
                        }
                        else -> {
                            SubcomposeAsyncImageContent()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    text = image.user,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))

                AndroidView(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 300.dp),
                    factory = { context ->
                        PhotoView(context).apply {
                            //setImageResource(R.mipmap.ic_launcher_round) // for test

                            GlideApp.with(context)
                                .load(image.largeImageURL)
                                //.placeholder(R.mipmap.ic_launcher)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(this)

                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                            )
                            isZoomable = true
                            maximumScale = 10f

                        }
                    },
                    // Needed? For state updates - we don't update async, so no need.
//                    update = { view ->
//                        Glide.with(view)
//                            .load(image.largeImageURL)
//                            .placeholder(R.mipmap.ic_launcher)
//                            .into(view)
//                    }
                )
                Text(
                    text = "Double tap to zoom, pinch to zoom in/out, double tap to zoom out",
                    fontSize = 10.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

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
                    text = "Dimensions: " + image.imageWidth + " x " + image.imageHeight,
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(0.dp),
        contentAlignment = TopRight
    ) {
        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .padding(0.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_delete),
                contentDescription = "Back",
                modifier = Modifier
                    .width(45.dp)
                    .height(45.dp)
                    .align(CenterEnd)
                    .padding(top = 5.dp, end = 15.dp)
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
