package com.catalin.instagramclone.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.catalin.instagramclone.DestinationScreen
import com.catalin.instagramclone.IgViewModel
import com.catalin.instagramclone.R
import com.catalin.instagramclone.data.PostData

@Composable
fun SinglePostScreen(navController: NavController, vm: IgViewModel, post: PostData) {

    val comments = vm.comments.value

    LaunchedEffect(key1 = Unit) {
        vm.getComments(post.postId)
    }

    post.userId?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
        ) {
            Text(text = "Back", modifier = Modifier.clickable { navController.popBackStack() })

            CommonDivider()

            SinglePostDisplay(
                navController = navController,
                vm = vm,
                post = post,
                nbComments = comments.size
            )
        }
    }
}

@Composable
fun SinglePostDisplay(
    navController: NavController,
    vm: IgViewModel,
    post: PostData,
    nbComments: Int
) {
    val userData = vm.userData.value
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = post.userImage),
                    contentDescription = null
                )
            }

            Text(text = post.username ?: "")
            Text(text = ".", modifier = Modifier.padding(8.dp))

            if (userData?.userId == post.userId) {
                // Current user's post. Don't show anything
            } else if (userData?.following?.contains(post.userId) == true) {
                Text(
                    text = "Folowing",
                    color = Color.Gray,
                    modifier = Modifier.clickable { vm.onFollowClick(post.userId!!) })
            } else {
                Text(
                    text = "Follow",
                    color = Color.Blue,
                    modifier = Modifier.clickable { vm.onFollowClick(post.userId!!) })
            }
        }
    }

    Box {
        val modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp)
        CommonImage(
            data = post.postImage,
            modifier = modifier,
            contentScale = ContentScale.FillWidth
        )
    }

    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_like),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.Red)
        )
        Text(text = " ${post.likes?.size ?: 0} likes", modifier = Modifier.padding(start = 0.dp))
    }

    Row(modifier = Modifier.padding(8.dp)) {
        Text(text = post.username ?: "", fontWeight = FontWeight.Bold)
        Text(text = post.postDescription ?: "", modifier = Modifier.padding(start = 8.dp))
    }

    Row(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "$nbComments comments",
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable {
                    post.postId?.let {
                        navController.navigate(DestinationScreen.CommentsScreen.createRoute(it))
                    }
                })
    }
}







