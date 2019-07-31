package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
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
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH

    private var drawableRect: RectF = RectF()

    private var bitmapPaint: Paint = Paint()
    private var borderPaint: Paint = Paint()
    private var backgroundPaint: Paint = Paint()


    init {
        val a = context.obtainStyledAttributes(attrs , R.styleable.CircleImageView)
        borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor , DEFAULT_BORDER_COLOR)
        borderWidth = a.getInt(R.styleable.CircleImageView_cv_borderWidth , DEFAULT_BORDER_WIDTH)
        scaleType = ScaleType.CENTER_CROP
        a.recycle()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        backgroundPaint.color = borderColor

        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = min(availableWidth, availableHeight)
        val drawableRadius = sideLength / resources.displayMetrics.density

        val left = (paddingLeft + (availableWidth - sideLength) / 2).toFloat() / resources.displayMetrics.density
        val top = (paddingTop + (availableWidth - sideLength)/ 2).toFloat() / resources.displayMetrics.density

        drawableRect.set(left , top , left + sideLength , top + sideLength)

        canvas?.drawCircle(drawableRect.centerX() , drawableRect.centerY() , drawableRadius  , backgroundPaint)

        canvas?.drawCircle(drawableRect.centerX(), drawableRect.centerY(), drawableRadius - borderWidth , borderPaint)


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




}