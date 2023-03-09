package com.xiaoxin.basiccompose.wiegets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.xiaoxin.basiccompose.ui.theme.AppBarColors

@Composable
fun TopAppBarCustom(
    statusBarHeight: Int? = null,
    appBarColors: List<Color> = AppBarColors,
    context: @Composable () -> Unit,
) {
    // 系统状态栏高度
    val statusBarHeightTemp: Int = if (statusBarHeight != null) {
        statusBarHeight
    } else {
        val resourceId = LocalContext.current.resources.getIdentifier(
            "status_bar_height", "dimen", "android",
        )
        LocalContext.current.resources.getDimensionPixelSize(resourceId)
    }
    val barHeight = with(LocalDensity.current) {
        remember {
            mutableStateOf(statusBarHeightTemp.toDp())
        }
    }
    // 标题高度
    val appBarHeight = 50.dp
    Row(
        modifier = Modifier
            .background(
                Brush.linearGradient(
                    appBarColors
                ),
            )
            .fillMaxWidth()
            .padding(top = barHeight.value)
            .height(appBarHeight),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        context()
    }

}

@Composable
fun TopAppBarSimple(
    statusBarHeight: Int? = null,
    titleContent: String = "",
    appBarColors: List<Color> = AppBarColors,
    arrowBackIcon: ImageVector? = Icons.Default.ArrowBack,
    arrowBackClick: (() -> Unit)?
) {// 系统状态栏高度
    val statusBarHeightTemp: Int = if (statusBarHeight != null) {
        statusBarHeight
    } else {
        val resourceId = LocalContext.current.resources.getIdentifier(
            "status_bar_height", "dimen", "android",
        )
        LocalContext.current.resources.getDimensionPixelSize(resourceId)
    }
    val barHeight = with(LocalDensity.current) {
        remember {
            mutableStateOf(statusBarHeightTemp.toDp())
        }
    }
    // 标题高度
    val appBarHeight = 50.dp
    ConstraintLayout(
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    appBarColors
                )
            )
            .fillMaxWidth()
            .padding(top = barHeight.value)
            .height(appBarHeight),
    ) {
        val (arrowBack, title) = createRefs()
        arrowBackIcon?.let {
            Icon(imageVector = arrowBackIcon,
                tint = Color.White,
                contentDescription = "返回",
                modifier = Modifier
                    .constrainAs(arrowBack) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        onClick = { arrowBackClick?.invoke() },
                    )
                    .padding(start = 10.dp)
            )
        }
        Text(text = titleContent,
            fontSize = 14.sp,
            modifier = Modifier
                .constrainAs(title) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clickable { }, color = Color.White
        )
    }
}

@Composable
@Preview(name = "自定义标题")
private fun TopAppBarPreviewCustom() {
    TopAppBarCustom(
        statusBarHeight = 80,
        appBarColors = listOf(Color.Red, Color.Yellow, Color.Cyan)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (iconArrowBack, title, rightMenu) = createRefs()
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "返回",
                tint = Color(0xFFFFFFFF),
                modifier = Modifier
                    .constrainAs(iconArrowBack) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 10.dp)
                    .clickable {

                    }
            )
            Text(
                text = "首页", color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.constrainAs(title) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
            Row(
                modifier = Modifier
                    .constrainAs(rightMenu) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(end = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "菜单1", color = Color.White, modifier = Modifier.padding(end = 3.dp))
                Text(text = "菜单2", color = Color.White)
            }

        }
    }
}

@Composable
@Preview(name = "简单标题")
private fun TopAppBarPreviewSimple() {
    TopAppBarSimple(
        statusBarHeight = 80,
        titleContent = "首页",
        arrowBackIcon = Icons.Default.ArrowBack,
        arrowBackClick = {
            print("返回")
        })
}