package com.ayush.linkup.presentation.component

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ayush.linkup.R
import com.ayush.linkup.data.model.Post
import com.ayush.linkup.data.model.User


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PostItem(
    post: Post,
    user: User,
) {
    val isLiked = rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.pfp)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = user.name,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Space(10.dp)

            Text(
                text = post.text,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                color = MaterialTheme.colorScheme.onSurface
            )
            Space(20.dp)
            Row(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.heart),
                    contentDescription = "heart",
                    modifier = Modifier.clickable {
                        isLiked.value = !isLiked.value
                    },
                    tint = if (isLiked.value) Color.Red else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(20.dp))
                Icon(
                    painter = painterResource(id = R.drawable.comments),
                    contentDescription = "comment",
                    modifier = Modifier.clickable {

                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                Icon(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = "share",
                    modifier = Modifier.clickable {

                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Space(10.dp)
        }
    }
}