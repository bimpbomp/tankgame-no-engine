package com.example.helloworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class MyCanvas extends View {
    Paint paint;
    Rect rect;
    int rectX = 20;
    int rectY = 20;
    int rectW = 100;
    int rectH = 100;

    int width;
    int height;

    public MyCanvas(Context context) {
        super(context);

        paint = new Paint();
        rect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(3);

        canvas.drawRect(rectX, rectY, rectW, rectH, paint);
    }
}
