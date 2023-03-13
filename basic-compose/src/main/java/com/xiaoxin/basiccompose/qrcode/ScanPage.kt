package com.xiaoxin.basiccompose.qrcode

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.xiaoxin.basiccompose.widgets.TopAppBarSimple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanPage(
    type:Int,
    onBack: () -> Unit

) {
    var isShow by remember {
        mutableStateOf(ImageAnalyzerConfig.showScanBox)
    }
    var scanCode by remember {
        mutableStateOf("")
    }
    var cameraState by remember {
        mutableStateOf(false)
    }
    val permissionCamera = rememberPermissionState(permission = Manifest.permission.CAMERA)
    LaunchedEffect(Unit) {
        permissionCamera.launchPermissionRequest()
        when (permissionCamera.status) {
            is PermissionStatus.Denied -> {

            }
            PermissionStatus.Granted -> {
                cameraState = true
            }
        }

    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val cameraProviderFuture = remember {
            ProcessCameraProvider.getInstance(context)
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBarSimple(titleContent = "掃一掃") {
                onBack.invoke()
            }
            if (cameraState) {
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (cameraV, rightMenu, lineV, resultV, toolV) = createRefs()

                    AndroidView(
                        factory = { context ->
                            val previewView = PreviewView(context)
                            val preview: Preview = Preview.Builder()
                                .build()
                            val selector = CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                .build()
                            preview.setSurfaceProvider(previewView.surfaceProvider)

                            val imageAnalysis = ImageAnalysis.Builder()
//                                .setOutputImageRotationEnabled(true)
//                                .setTargetRotation(Surface.ROTATION_0)
//                                .setTargetResolution(android.util.Size(1080, 1920))
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                            imageAnalysis.setAnalyzer(
                                ContextCompat.getMainExecutor(context),
                                QRCodeAnalyzer() { result ->
                                    lifecycleOwner.lifecycleScope.launch {
                                        delay(timeMillis = ImageAnalyzerConfig.interval)
                                        ImageAnalyzerConfig.intervalScan = true
                                    }
                                    result.let { scanCode = it.toString() }
                                    // 进行操作

                                    /**
                                     * cameraProviderFuture
                                        .get()
                                        .unbindAll()
                                     */
                                }
                            )
                            try {
                                cameraProviderFuture.get().bindToLifecycle(
                                    lifecycleOwner,
                                    selector,
                                    preview,
                                    imageAnalysis
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            return@AndroidView previewView
                        },

                        modifier = Modifier
                            .fillMaxSize()
                            .constrainAs(cameraV) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                    )
                    if (isShow) {
                        DrawScanBorder(modifier = Modifier
                            .constrainAs(lineV) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            })
                    }
                }


                Text(
                    text = scanCode,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                )
            }
        }
    }
}

//掃描邊框
@Composable
fun DrawScanBorder(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val rollHeight by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing), //移动
            repeatMode = RepeatMode.Reverse
        )
    )
    // 扫描滚动
    Canvas(modifier = modifier, onDraw = {
        val canvasWidth = size.width
        var canvasHeight = size.height
        canvasHeight = (canvasHeight / 2 - 300) + rollHeight
        // 绘制线条
        drawLine(
            color = Color.Blue,
            start = Offset(x = (canvasWidth / 2 - 300), y = canvasHeight),
            end = Offset(x = (canvasWidth / 2 + 300), y = canvasHeight),
            strokeWidth = 6f
        )
        // 绘制边框长度
        val cornerHeight = 50f
        // 边角的宽度
        val cornerWidth = 8f
        val path = Path()
        path.apply {
            // 移动到左上角
            moveTo(x = canvasWidth / 2 - 300, y = (canvasWidth / 2 - 300 + cornerHeight))
            lineTo(x = canvasWidth / 2 - 300, y = (canvasWidth / 2 - 300))
            lineTo(x = canvasWidth / 2 - 300 + cornerHeight, y = (canvasWidth / 2 - 300))
            // 移动到右上角
            moveTo(x = canvasWidth / 2 + 300 - cornerHeight, y = (canvasWidth / 2 - 300))
            lineTo(x = canvasWidth / 2 + 300, y = (canvasWidth / 2 - 300))
            lineTo(x = canvasWidth / 2 + 300, y = (canvasWidth / 2 - 300 + cornerHeight))
            // 移动左下角
            moveTo(x = canvasWidth / 2 - 300, y = (canvasWidth / 2 + 300 - cornerHeight))
            lineTo(x = canvasWidth / 2 - 300, y = (canvasWidth / 2 + 300))
            lineTo(x = canvasWidth / 2 - 300 + cornerHeight, y = (canvasWidth / 2 + 300))
            // 移动到右下角
            moveTo(x = canvasWidth / 2 + 300, y = (canvasWidth / 2 + 300 - cornerHeight))
            lineTo(x = canvasWidth / 2 + 300, y = (canvasWidth / 2 + 300))
            lineTo(x = canvasWidth / 2 + 300 - cornerHeight, y = (canvasWidth / 2 + 300))
        }
        drawPath(
            path = path, color = Color.Blue, style = Stroke(
                width = cornerWidth,
            )
        )
    })
}