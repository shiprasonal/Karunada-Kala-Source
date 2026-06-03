package com.karunadakala.source.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.karunadakala.source.model.MarkerKind

fun MarkerKind.toMarkerDescriptor(context: Context): BitmapDescriptor =
    when (this) {
        MarkerKind.WORKSHOP -> MapMarkerBitmaps.workshopPin(context)
        MarkerKind.PERFORMANCE -> MapMarkerBitmaps.performancePin(context)
    }

private object MapMarkerBitmaps {

    private val cache = mutableMapOf<String, BitmapDescriptor>()

    fun workshopPin(context: Context): BitmapDescriptor =
        cache.getOrPut("workshop") {
            circlePin(context, AndroidColor.parseColor("#FFD700"))
        }

    fun performancePin(context: Context): BitmapDescriptor =
        cache.getOrPut("performance") {
            circlePin(context, AndroidColor.parseColor("#C8102E"))
        }

    private fun circlePin(context: Context, fillArgb: Int): BitmapDescriptor {
        val density = context.resources.displayMetrics.density
        val sizePx = (40f * density).toInt().coerceIn(28, 96)
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = fillArgb }
        val cx = sizePx / 2f
        val cy = sizePx / 2f
        val radius = sizePx * 0.34f
        canvas.drawCircle(cx, cy, radius, fill)

        val stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2.5f * density
            color = AndroidColor.WHITE
        }
        canvas.drawCircle(cx, cy, radius, stroke)

        val inner = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1.2f * density
            color = AndroidColor.argb(90, 0, 0, 0)
        }
        canvas.drawCircle(cx, cy, radius - stroke.strokeWidth / 2f, inner)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

@Composable
fun rememberMarkerDescriptor(kind: MarkerKind): BitmapDescriptor {
    val context = LocalContext.current
    return remember(kind) { kind.toMarkerDescriptor(context) }
}
