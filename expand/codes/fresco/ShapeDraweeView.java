package com.liangmayong.base.widget.shapeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.facebook.drawee.view.SimpleDraweeView;
import com.liangmayong.base.R;

/**
 * Created by LiangMaYong on 2016/12/23.
 */
public class ShapeDraweeView extends SimpleDraweeView {


    private static final PorterDuffXfermode PORTER_DUFF_DST_IN = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private static final PorterDuffXfermode PORTER_DUFF_SRC_ATOP = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);


    private Canvas maskCanvas;
    private Bitmap maskBitmap;
    private Paint maskPaint;
    private Drawable shape;
    private Drawable cover;

    private Canvas coverCanvas;
    private Bitmap coverBitmap;
    private Paint coverPaint;

    private Canvas drawableCanvas;
    private Bitmap drawableBitmap;
    private Paint drawablePaint;

    private boolean invalidated = true;
    private boolean square = false;

    private int pressedColor = 0x20333333;

    private boolean pressed = false;

    public ShapeDraweeView(Context context) {
        super(context);
        setup(context, null, 0);
    }

    public ShapeDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs, 0);
    }

    public ShapeDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context, attrs, defStyle);
    }

    /**
     * setup
     *
     * @param context  context
     * @param attrs    attrs
     * @param defStyle defStyle
     */
    private void setup(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeViewStyleable, defStyle, 0);
            this.square = typedArray.getBoolean(R.styleable.ShapeViewStyleable_shapeSquare, false);
            this.shape = typedArray.getDrawable(R.styleable.ShapeViewStyleable_shape);
            this.cover = typedArray.getDrawable(R.styleable.ShapeViewStyleable_shapeCover);
            this.pressedColor = typedArray.getColor(R.styleable.ShapeViewStyleable_shapePressed, pressedColor);
            typedArray.recycle();
        }

        if (getScaleType() == ScaleType.FIT_CENTER) {
            setScaleType(ScaleType.CENTER_CROP);
        }

        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskPaint.setColor(Color.BLACK);

        coverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coverPaint.setColor(Color.BLACK);

        if (isInEditMode()) {
            if (cover != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    setBackground(cover);
                } else {
                    setBackgroundDrawable(cover);
                }
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    public void setSquare(boolean square) {
        this.square = square;
        invalidate();
    }

    public void setCover(Drawable cover) {
        this.cover = cover;
        invalidate();
    }

    public void setShape(Drawable shape) {
        this.shape = shape;
        invalidate();
    }

    @Override
    public void invalidate() {
        invalidated = true;
        super.invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createShapeCanvas(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInEditMode()) {
            drawDrawable(canvas, getDrawable());
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClickable()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressed = true;
                    invalidate();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    pressed = false;
                    invalidate();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * drawDrawable
     *
     * @param canvas   canvas
     * @param drawable drawable
     */
    private void drawDrawable(Canvas canvas, Drawable drawable) {
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        try {
            if (invalidated) {
                if (drawable != null) {
                    invalidated = false;
                    Matrix imageMatrix = getImageMatrix();
                    if (imageMatrix == null) {
                        drawable.draw(drawableCanvas);
                    } else {
                        int drawableSaveCount = drawableCanvas.getSaveCount();
                        drawableCanvas.save();
                        drawableCanvas.concat(imageMatrix);
                        drawable.draw(drawableCanvas);
                        drawableCanvas.restoreToCount(drawableSaveCount);
                    }

                    if (pressed) {
                        drawableCanvas.drawColor(pressedColor);
                    }
                    if (maskBitmap != null) {
                        drawablePaint.reset();
                        drawablePaint.setFilterBitmap(false);
                        drawablePaint.setXfermode(PORTER_DUFF_DST_IN);
                        drawableCanvas.drawBitmap(maskBitmap, 0.0f, 0.0f, drawablePaint);
                    }

                    if (maskBitmap != null) {
                        drawablePaint.reset();
                        drawablePaint.setFilterBitmap(false);
                        drawablePaint.setXfermode(PORTER_DUFF_SRC_ATOP);
                        drawableCanvas.drawBitmap(coverBitmap, 0.0f, 0.0f, drawablePaint);
                    }
                }
            }
            if (!invalidated) {
                drawablePaint.setXfermode(null);
                canvas.drawBitmap(drawableBitmap, 0.0f, 0.0f, drawablePaint);
            }
        } catch (Exception e) {
        } finally {
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (square) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            int dimen = Math.min(width, height);
            setMeasuredDimension(dimen, dimen);
        }
    }

    /**
     * createShapeCanvas
     *
     * @param width  width
     * @param height height
     * @param oldw   oldw
     * @param oldh   oldh
     */
    private void createShapeCanvas(int width, int height, int oldw, int oldh) {
        if (maskBitmap != null) {
            maskBitmap.recycle();
            maskBitmap = null;
        }
        if (coverBitmap != null) {
            coverBitmap.recycle();
            coverBitmap = null;
        }
        if (drawableBitmap != null) {
            drawableBitmap.recycle();
            drawableBitmap = null;
        }
        if (shape != null) {
            maskCanvas = new Canvas();
            maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            maskCanvas.setBitmap(maskBitmap);

            maskPaint.reset();
            paintMaskCanvas(maskCanvas, maskPaint, width, height);
        }
        if (cover != null) {
            coverCanvas = new Canvas();
            coverBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            coverCanvas.setBitmap(coverBitmap);

            coverPaint.reset();
            paintCoverCanvas(coverCanvas, coverPaint, width, height);
        }
        drawableCanvas = new Canvas();
        drawableBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        drawableCanvas.setBitmap(drawableBitmap);
        drawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        invalidated = true;
    }

    /**
     * paintMaskCanvas
     *
     * @param maskCanvas maskCanvas
     * @param maskPaint  maskPaint
     * @param width      width
     * @param height     height
     */
    private void paintMaskCanvas(Canvas maskCanvas, Paint maskPaint, int width, int height) {
        if (shape != null) {
            shape.setBounds(0, 0, width, height);
            shape.draw(maskCanvas);
        }
    }

    /**
     * paintCoverCanvas
     *
     * @param topCanvas topCanvas
     * @param topPaint  topPaint
     * @param width     width
     * @param height    height
     */
    private void paintCoverCanvas(Canvas topCanvas, Paint topPaint, int width, int height) {
        if (cover != null) {
            cover.setBounds(0, 0, width, height);
            cover.draw(topCanvas);
        }
    }
}
