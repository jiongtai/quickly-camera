package com.bayee.cameras.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bayee.cameras.R;

public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {

    private int startColor;
    private int endColor;

    public GradientTextView(Context context) {
        this(context, null);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        startColor = a.getColor(R.styleable.GradientTextView_startColor, Color.TRANSPARENT);
        endColor = a.getColor(R.styleable.GradientTextView_endColor, Color.TRANSPARENT);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        Shader shader = new LinearGradient(0, 0, getWidth(), getHeight(),
                new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        super.onDraw(canvas);
    }
}
