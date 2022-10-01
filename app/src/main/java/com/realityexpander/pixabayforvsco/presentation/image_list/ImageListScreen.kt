package com.realityexpander.pixabayforvsco.presentation.image_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.realityexpander.pixabayforvsco.presentation.destinations.PixabayImageScreenDestination

@Composable
@Destination(start = true)
fun ImageListScreen(
    navigator: DestinationsNavigator,
    viewModel: ImageListViewModel = hiltViewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )
    val state = viewModel.state

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(ImageListEvent.OnSearchQueryChanged(it))
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text("Search…")
            },
            maxLines = 1,
            singleLine = true,
        )

        // Show error message
        if (state.errorMessage != null) {
            Text(
                "Error: ${state.errorMessage}",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(ImageListEvent.OnRefresh)
            }
        ) {
            if (state.pixabayImageList.isEmpty()) {
                Text(
                    "No images found",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.pixabayImageList.size) { i ->
                    // Kick off the loading (why not in a launched effect?)
                    if (i >= state.pixabayImageList.size - 1
                        && !state.endReached
                        && !state.isLoading
                    ) {
                        // yes its a side effect but its fine in this case because of the checks above
                        viewModel.getNextPixabayImagePageList()
                    }

                    ImageItem(
                        pixabayImage = state.pixabayImageList[i],
                        index = i,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(
                                    PixabayImageScreenDestination(
                                        id = state.pixabayImageList[i].id
                                    )
                                )
                            },
                    )

                    if (i < state.pixabayImageList.size) { // don't show divider for last item
                        Divider(
                            modifier = Modifier.padding(horizontal = 0.dp)
                        )
                    }
                }


                if (state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .height(55.dp)
                                    .padding(bottom = 16.dp),
                                color = Color.White
                            )
                        }
                    }
                }

            }
        }
    }

}
