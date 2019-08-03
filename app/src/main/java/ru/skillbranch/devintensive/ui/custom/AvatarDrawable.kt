package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.TypedValue
import ru.skillbranch.devintensive.R


class AvatarDrawable(
        var initials: String ,
        var context: Context,
        var sizeTextSP: Float = 40f): Drawable() {

    private val paint: Paint = Paint()
    private var width: Float = 0f
    private var height: Float = 0f
    private var typedValue = TypedValue()

    override fun draw(canvas: Canvas) {
        context.theme.resolveAttribute(R.attr.colorAccent, typedValue, true )
        paint.color = typedValue.data
        canvas.drawCircle(width / 2 , height / 2 , width / 2 , paint)

        if (initials.isNotBlank()) {
            paint.textAlign = Paint.Align.CENTER
            val pixel = convertSpToPixels(sizeTextSP , context)
            paint.textSize = pixel
            paint.typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
            paint.color = Color.WHITE
            canvas.drawText(initials, width / 2, (height/2) + pixel /3 , paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int  {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }


    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        width = bounds?.width()?.toFloat() ?: 0f
        height = bounds?.height()?.toFloat() ?: 0f
    }

    fun convertSpToPixels(dp: Float, context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }

}