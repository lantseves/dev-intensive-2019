package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
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
        private val SCALE_TYPE = ScaleType.CENTER_CROP
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH

    private var bitmap: Bitmap
    private var bitmapShader: BitmapShader? = null
    private var bitmapPaint: Paint = Paint()

    private var bmWidth:Int = 0
    private var bmHeight:Int = 0

    private var drawableRect: RectF = RectF()
    private var borderPaint: Paint = Paint()
    private var circleBackgroundPaint: Paint = Paint()
    private var drawableRadius: Float = 0f


    init {
        val a = context.obtainStyledAttributes(attrs , R.styleable.CircleImageView)
        borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor , DEFAULT_BORDER_COLOR)
        borderWidth = a.getInt(R.styleable.CircleImageView_cv_borderWidth , DEFAULT_BORDER_WIDTH)
        scaleType = ScaleType.CENTER_CROP
        a.recycle()

        bitmap = mBitmapFromDrawable(drawable)
        setup()
    }

    override fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)

        //canvas.clipPath()
        //context.theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
        canvas?.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius , bitmapPaint)
        if(borderWidth > 0) {
            borderPaint.color = borderColor
            borderPaint.strokeWidth = borderWidth * resources.displayMetrics.density
            borderPaint.style = Paint.Style.STROKE
            canvas?.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius - borderWidth * resources.displayMetrics.density, borderPaint)
        }


        //super.setImageDrawable(drawable)
    }

    override fun setImageDrawable(drwb: Drawable) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    private fun initializeBitmap() {
        bitmap = mBitmapFromDrawable(drawable)
    }

    private fun mBitmapFromDrawable(drawable: Drawable): Bitmap {

        if(drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        return Bitmap.createBitmap(drawable.intrinsicWidth , drawable.intrinsicHeight , Bitmap.Config.ARGB_8888)
    }

    private fun setup() {
        bitmapShader = BitmapShader(bitmap , Shader.TileMode.CLAMP , Shader.TileMode.CLAMP)

        bitmapPaint.isAntiAlias = true
        bitmapPaint.shader = bitmapShader

        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        borderPaint.strokeWidth = toPx(borderWidth)

        circleBackgroundPaint.style = Paint.Style.FILL
        circleBackgroundPaint.isAntiAlias = true
        circleBackgroundPaint.color = Color.BLACK

        bmWidth = bitmap.height
        bmHeight = bitmap.height

        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = min(availableWidth, availableHeight)
        drawableRadius = sideLength / 2f

        val left = (paddingLeft + (availableWidth - sideLength) / 2).toFloat()
        val top = (paddingTop + (availableWidth - sideLength)/ 2).toFloat()

        drawableRect.set(left , top , left + sideLength , top + sideLength)


    }








    @Dimension
    fun getBorderWidth():Int = borderWidth

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = dp
    }

    fun getBorderColor():Int = borderColor.dec()

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = context.getColor(colorId)
    }

    private fun toPx(dp: Int): Float = dp * resources.displayMetrics.density

    private fun toDp(px: Float): Int = (px / resources.displayMetrics.density).toInt()
}