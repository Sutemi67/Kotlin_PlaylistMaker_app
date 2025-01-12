package com.example.playlistmaker.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import com.example.playlistmaker.player.data.PlaybackStatus

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var imageBitmap: Bitmap? = null
    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var playBitmap: Bitmap? = null
    private var pauseBitmap: Bitmap? = null
//    private var onClickListener: (() -> Unit)? = null
//
//    fun setOnPlaybackClickListener(listener: () -> Unit) {
//        Log.d("clicks", "лисенер нажатия создан")
//        onClickListener = listener
//    }

    fun setPlaybackIcon(status: PlaybackStatus) {
        imageBitmap = when (status) {
            PlaybackStatus.Playing -> pauseBitmap
            else -> playBitmap
        }
        invalidate()
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                val playIconResId = getResourceId(R.styleable.PlaybackButtonView_playIcon, 0)
                val pauseIconResId = getResourceId(R.styleable.PlaybackButtonView_pauseIcon, 0)
                playBitmap = if (playIconResId != 0) {
                    AppCompatResources.getDrawable(context, playIconResId)?.toBitmap()
                } else null

                pauseBitmap = if (pauseIconResId != 0) {
                    AppCompatResources.getDrawable(context, pauseIconResId)?.toBitmap()
                } else null
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        imageBitmap?.let {
            canvas.drawBitmap(imageBitmap!!, null, imageRect, null)
        }
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                Log.d("clicks", "Action Down нажатие")
//                return true
//            }
//
//            MotionEvent.ACTION_UP -> {
//                performClick()
//                Log.d("clicks", "Action Up нажатие")
//                return true
//            }
//        }
//        return super.onTouchEvent(event)
//    }
//
//    override fun performClick(): Boolean {
//        Log.d("clicks", "нажатие на кнопку обработано в performClick")
//        onClickListener?.invoke()
//        return super.performClick()
//    }
}