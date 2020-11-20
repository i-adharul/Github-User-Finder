package com.adharul.i.githubusers.model.helper

import android.graphics.*
import com.squareup.picasso.Transformation
import kotlin.math.min

class CircleTransformation (circleSeparator: Boolean) : Transformation {
    private var mCircleSeparator = circleSeparator

    companion object{
        private const val COLOR = "#FFFFFF"
        private const val KEY = "circle"
    }

    override fun transform(source: Bitmap): Bitmap {
        val size = min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }
        val bitmap = Bitmap.createBitmap(size, size, source.config)
        val canvas = Canvas(bitmap)
        val shader =
            BitmapShader(squaredBitmap, Shader.TileMode.CLAMP , Shader.TileMode.CLAMP)
        val paint =
            Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
        paint.shader = shader
        val r = size / 2f
        canvas.drawCircle(r, r, r - 1, paint)
        // Make the thin border:
        val paintBorder = Paint()
        paintBorder.style = Paint.Style.STROKE
        paintBorder.color = Color.argb(84, 0, 0, 0)
        paintBorder.isAntiAlias = true
        paintBorder.strokeWidth = 1F
        canvas.drawCircle(r, r, r - 1, paintBorder)
        // Optional separator for stacking:
        if (mCircleSeparator) {
            val paintBorderSeparator = Paint()
            paintBorderSeparator.style = Paint.Style.STROKE
            paintBorderSeparator.color = Color.parseColor(COLOR)
            paintBorderSeparator.isAntiAlias = true
            paintBorderSeparator.strokeWidth = 4F
            canvas.drawCircle(r, r, r + 1, paintBorderSeparator)
        }
        squaredBitmap.recycle()
        return bitmap
    }

    override fun key(): String {
        return KEY
    }
}