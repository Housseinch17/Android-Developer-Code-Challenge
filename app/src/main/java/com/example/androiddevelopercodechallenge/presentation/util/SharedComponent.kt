package com.example.androiddevelopercodechallenge.presentation.util

import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.presentation.theme.Label
import com.example.androiddevelopercodechallenge.presentation.theme.Typography

@Composable
fun PagingError(
    @StringRes id: Int,
    onPagingPerform: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(R.string.no_internet_connection),
            style = Typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier,
            onClick = {
                onPagingPerform()
            }
        ) {
            Text(
                modifier = Modifier,
                text = stringResource(id)
            )
        }
    }
}
@Composable
fun ShimmerEffect(
    modifier: Modifier,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
) {
    val shimmerColors = listOf(
        color.copy(alpha = 0.3f),  // Light grey
        color.copy(alpha = 0.5f),  // Slightly more opaque
        color.copy(alpha = 0.7f),  // Darker grey
        color.copy(alpha = 0.5f),
        color.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )

    Box(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }
}

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    imageUrl: String?){
    if (imageUrl != null) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl).crossfade(true)
                .placeholder(R.drawable.loading)
                .error(R.drawable.connectionerror)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}

@Composable
fun ShowDialog(
    modifier: Modifier,
    title: String,
    isProgressBar: Boolean,
    description: @Composable () -> Unit,
    confirmText: String,
    confirmButton: () -> Unit,
    onDismissButton: () -> Unit
) {
        AlertDialog(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface),
            onDismissRequest = {},
            confirmButton = {
                Button(
                    enabled = !isProgressBar,
                    onClick = confirmButton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )
                ) {
                    Text(
                        text = confirmText, color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                Button(
                    enabled = !isProgressBar,
                    onClick = onDismissButton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Label
                    )
                ) {
                    Text(
                        stringResource(R.string.dismiss),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            title = {
                Text(
                    text = title,
                    style = Typography.titleMedium
                )
            },
            text = {
                description()
            })
}