package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import ru.skillbranch.devintensive.R
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr:Int = 0
) : ImageView(context , attrs , defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH
    private var bitmap : Bitmap? = null
    private var text: String? = null

    init {
        val a = context.obtainStyledAttributes(attrs , R.styleable.CircleImageView)
        borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor , DEFAULT_BORDER_COLOR)
        borderWidth = a.getInt(R.styleable.CircleImageView_cv_borderWidth , DEFAULT_BORDER_WIDTH)
        a.recycle()

        bitmap = getBitmapFromDrawable(drawable)
        bitmap = getCircleBitmap(bitmap!!)
    }


    override fun onDraw(canvas: Canvas) {
        if (bitmap == null) return
        if (width == 0 || height == 0) return

        bitmap = getScaledBitmap(bitmap!!, width)
        bitmap = getCenterCroppedBitmap(bitmap!!, width)
        bitmap = getCircleBitmap(bitmap!!)

        if (borderWidth > 0)
            bitmap = getStrokedBitmap(bitmap!!, toPx(borderWidth)  , borderColor)

        canvas.drawBitmap(bitmap!!, 0F, 0F, null)

    }

    private fun getStrokedBitmap(squareBmp: Bitmap, strokeWidth: Float, color: Int): Bitmap {
        val inCircle = RectF()
        val strokeStart = strokeWidth / 2F
        val strokeEnd = squareBmp.width - strokeWidth / 2F

        inCircle.set(strokeStart , strokeStart, strokeEnd, strokeEnd)

        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        strokePaint.color = color
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = strokeWidth.toFloat()

        val canvas = Canvas(squareBmp)
        canvas.drawOval(inCircle, strokePaint)

        return squareBmp
    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        val smallest = min(bitmap.width, bitmap.height)
        val outputBmp = Bitmap.createBitmap(smallest, smallest, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBmp)

        val paint = Paint()
        val rect = Rect(0, 0, smallest, smallest)

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(smallest / 2F, smallest / 2F, smallest / 2F, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return outputBmp
    }

    private fun getCenterCroppedBitmap(bitmap: Bitmap, size: Int): Bitmap {
        val cropStartX = (bitmap.width - size) / 2
        val cropStartY = (bitmap.height - size) / 2

        return Bitmap.createBitmap(bitmap, cropStartX, cropStartY, size, size)
    }

    private fun getScaledBitmap(bitmap: Bitmap, side: Int): Bitmap {
        return if (bitmap.width != side || bitmap.height != side) {
            val min = min(bitmap.height , bitmap.width).toFloat()
            val kof = min / side
            Bitmap.createScaledBitmap(bitmap, (bitmap.width / kof).toInt(), (bitmap.height / kof).toInt(), false)
        } else {
            bitmap
        }
    }

    @Dimension
    fun getBorderWidth(): Int = borderWidth
        
    fun getBorderColor(): Int = borderColor

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = dp
        this.invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = context.getColor(colorId)
        this.invalidate()
    }

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        this.invalidate()
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if(drawable == null) {
            return null
        }

        if(drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth , drawable.intrinsicHeight , Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width ,canvas.height)
        drawable.draw(canvas)

        return bitmap
    }


    private fun toPx(dp: Int): Float = dp * resources.displayMetrics.density

    private fun toDp(px: Float): Int = (px / resources.displayMetrics.density).toInt()

}
