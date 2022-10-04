package com.realityexpander.pixabayforvsco.presentation.image_list

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.realityexpander.pixabayforvsco.domain.model.PixabayImage


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun CompanyListingPreview() {
//    CompanyListingItem(
//        companyListing = CompanyListing(
//            companySymbol = "AAPL",
//            companyName = "Apple Inc.",
//            companyExchange = "NASDAQ",
//        )
//    )
//}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun TextPreview() {
//    Text(
//        text = "Hello World",
////        modifier = Modifier.fillMaxWidth()
//    )
//}


@Composable
fun ImageItem(
    pixabayImage: PixabayImage,
    modifier: Modifier = Modifier,
    index: Int = 0,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            //Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            ) {
                AsyncImage(
                    model = pixabayImage.previewURL,
                    contentDescription = pixabayImage.tags,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .height(50.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .align(Alignment.CenterVertically)
                ) {
                    // println("pixabayImage.tags: ${pixabayImage.tags}")
                    Text(
                        //text = pixabayImage.tags + "=>$index:${pixabayImage.page}",
                        text = "pg ${pixabayImage.page}=> item $index",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onBackground,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "(${pixabayImage.user})",
                        fontStyle = FontStyle.Italic,
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onBackground,
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

    }
}































