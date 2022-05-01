package com.kapilv.githubtrending.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import androidx.annotation.Nullable;

import com.kapilv.githubtrending.R;

/**
 *  Custom layout to generate Shadows around Containers
 *
 *  Attributes -
 *
 *         -> enableShadow      : Enable/Disable Shadow around view
 *         -> shadowTopOffset   : Offset of Shadow Start from Top (Positive value shifts shadow outwards)
 *         -> shadowBottomOffset: Offset of Shadow Start from Bottom (Positive value shifts shadow outwards)
 *         -> shadowStartOffset : Offset of Shadow Start from Start (Positive value shifts shadow outwards)
 *         -> shadowEndOffset   : Offset of Shadow Start from End (Positive value shifts shadow outwards)
 *         -> shadowStartY      : Start Location of Shadow along y-axis, relative to the default location
 *         -> shadowColor       : Start Color of the Shadow
 *         -> shadowStrokeWidth : Stroke Width for painting the shadow on canvas
 *         -> blurRadius        : Shadow blur radius
 *         -> backgroundColor   : Color to fill in Container
 *         -> cornerRadius      : Container Corner Radius
 *
 *
 *
 */
public class ShadowView extends ConstraintLayout {

    private final Paint rectPaint = new Paint();
    private final Paint shadowPaint = new Paint();

    private final Path shadowPath = new Path();
    protected Path clipPath = new Path();
    private final Path rectBackgroundPath = new Path();

    protected RectF clipRectF = new RectF();
    private final RectF backgroundRectF = new RectF();

    private final PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);

    private int shadowColor = Color.BLACK;
    private int backgroundColor = Color.WHITE;

    private float shadowStrokeWidth = 15f;

    protected float blurRadius = 50f;
    private float cornerRadius = 8f;
    private float shadowStartOffset = 0f;
    private float shadowEndOffset = 0f;
    private float shadowTopOffset = 0f;
    private float shadowBottomOffset = 0f;
    private float shadowStartY = java.lang.Float.MIN_VALUE;
    private boolean enableShadow = false;

    private BlurMaskFilter blurMaskFilter;

    public ShadowView(Context context) {
        super(context);
        init(null);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    /**
     * Initialize the View with parameters from Supplied Attributes.
     *
     * @param attrs Attributes Supplied through View's xml
     */
    private void init(@Nullable AttributeSet attrs) {
        readAttrs(attrs);
        blurMaskFilter = new BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL);
    }

    /**
     * Read the Supplied Attributes and assign them to view parameters.
     *
     * @param attrs Attributes Supplied through View's xml
     */
    private void readAttrs(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            Context context = this.getContext();

            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShadowView, 0, 0);
            shadowTopOffset = typedArray.getDimension(R.styleable.ShadowView_shadowTopOffset, dpToPx(context, 0));
            shadowBottomOffset = typedArray.getDimension(R.styleable.ShadowView_shadowBottomOffset, dpToPx(context, 0));
            shadowStartOffset = typedArray.getDimension(R.styleable.ShadowView_shadowStartOffset, dpToPx(context, 0));
            shadowEndOffset = typedArray.getDimension(R.styleable.ShadowView_shadowEndOffset, dpToPx(context, 0));

            shadowStartY = typedArray.getDimension(R.styleable.ShadowView_shadowStartY, java.lang.Float.MIN_VALUE);

            shadowColor = typedArray.getColor(R.styleable.ShadowView_shadowColor, Color.BLACK);
            backgroundColor = typedArray.getColor(R.styleable.ShadowView_backgroundColor, context.getResources().getColor(R.color.colorPrimary));

            shadowStrokeWidth = typedArray.getDimension(R.styleable.ShadowView_shadowStrokeWidth, dpToPx(context, 1));
            cornerRadius = typedArray.getDimension(R.styleable.ShadowView_cornerRadius, dpToPx(context, 4));
            blurRadius = typedArray.getDimension(R.styleable.ShadowView_blurRadius, dpToPx(context, 8));

            enableShadow = typedArray.getBoolean(R.styleable.ShadowView_enableShadow, true);

            typedArray.recycle();
        }
    }

    /**
     * Clips the View Background to emulate corners.
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        clipPath.reset();

        clipRectF.top = 0f;
        clipRectF.left = 0f;
        clipRectF.right = canvas.getWidth();
        clipRectF.bottom = canvas.getHeight();
        clipPath.addRoundRect(clipRectF, cornerRadius, cornerRadius, Path.Direction.CW);

        canvas.clipPath(clipPath);
        super.dispatchDraw(canvas);
    }

    /**
     * Construct the final View by reconstructing the provided background canvas.
     * First applies outer shadows by making shadow paths from Top-right to Top-left to Bottom-left to Bottom-right to Top-right according to given parameters,
     * Then paints the path to the background canvas,
     * Finally add a rectangle with solid filled paint to finalize the view.
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (enableShadow) {
            drawShadow(canvas);
        }
        drawRectBackground(canvas);
        super.onDraw(canvas);
    }

    /**
     * First, forms background paint with background color fill,
     * Then, creates background rectangle with diagonal corners - (0, 0) and (canvas's width, canvas's height),
     * Finally, paints the path to the background canvas.
     *
     * @param canvas
     */
    private void drawRectBackground(Canvas canvas) {
        float height = canvas.getHeight();
        float width = canvas.getWidth();

        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setColor(backgroundColor);
        rectPaint.setXfermode(porterDuffXfermode);

        backgroundRectF.top = 0f;
        backgroundRectF.left = 0f;
        backgroundRectF.right = width;
        backgroundRectF.bottom = height;
        rectBackgroundPath.reset();
        rectBackgroundPath.addRect(backgroundRectF, Path.Direction.CW);

        canvas.drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, rectPaint);
    }

    /**
     * First, forms shadow paint with stroke according to shadow parameters (color, stroke width),
     * Then, Applies outer shadows by making shadow paths from Top-right to Top-left to Bottom-left to Bottom-right to Top-right with shadow paint,
     * Finally, paints the path to the background canvas.
     *
     * @param canvas
     */
    private void drawShadow(Canvas canvas) {
        canvas.save();

        float height = canvas.getHeight();
        float width = canvas.getWidth();

        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.STROKE);
        shadowPaint.setColor(shadowColor);
        shadowPaint.setStrokeWidth(shadowStrokeWidth);
        shadowPaint.setXfermode(porterDuffXfermode);
        shadowPaint.setMaskFilter(blurMaskFilter);

        shadowPath.reset();
        shadowPath.moveTo((width + (shadowEndOffset)), shadowStartY + shadowTopOffset);              //Top Right
        shadowPath.lineTo((shadowStartOffset), shadowStartY+shadowTopOffset);                        // TR -> TL
        shadowPath.lineTo((shadowStartOffset), (height + shadowBottomOffset));                          // TL -> BL
        shadowPath.lineTo((width + shadowEndOffset), (height + shadowBottomOffset));                    // BL -> BR
        shadowPath.lineTo((width + shadowEndOffset), shadowStartY + shadowTopOffset);                // BR -> TR

        canvas.drawPath(shadowPath, shadowPaint);
        canvas.restore();
    }

    /**
     * Converts size in dp to px.
     *
     * @param context
     * @param dp Size in dp
     * @return
     */
    private float dpToPx(Context context, int dp) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
