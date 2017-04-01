package com.justaudio.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.net.Uri;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class GifImageView extends View {

    private static final int DEFAULT_MOVIEW_DURATION = 1000;

    private Movie mMovie;

    private long mMovieStart = 0;
    private int mCurrentAnimationTime = 0;

    public GifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setImageResource(int mvId) {
        mMovie = Movie.decodeStream(getResources().openRawResource(mvId));
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMovie != null) {
            setMeasuredDimension(mMovie.width(), mMovie.height());
        } else {
            setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMovie != null) {
            updateAnimationTime();
            drawGif(canvas);
            invalidate();
        } else {
            drawGif(canvas);
        }
    }

    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }
        int dur = mMovie.duration();
        if (dur == 0) {
            dur = DEFAULT_MOVIEW_DURATION;
        }
        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }

    private void drawGif(Canvas canvas) {
        mMovie.setTime(mCurrentAnimationTime);
        mMovie.draw(canvas, 0, 0);
        canvas.restore();
    }

}
