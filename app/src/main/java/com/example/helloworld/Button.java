package com.example.helloworld;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class Button implements InputUiElement {
    private final Paint paint;
    private final int radius;
    private Point center;

    private int pointerId;

    private final int defaultColor;
    private final int pressedColor;

    private final Click buttonFunction;

    public Button(Point center, int radius, int color, Click buttonFunction){
        this.center = center;
        this.radius = radius;
        this.paint = new Paint();
        this.paint.setColor(color);
        this.pressedColor = Color.GRAY;
        this.defaultColor = color;
        this.pointerId = MotionEvent.INVALID_POINTER_ID;
        this.buttonFunction = buttonFunction;
    }

    public Button(Point center, int radius, int color){
        this.center = center;
        this.radius = radius;
        this.paint = new Paint();
        this.paint.setColor(color);
        this.pressedColor = Color.GRAY;
        this.defaultColor = color;
        this.pointerId = MotionEvent.INVALID_POINTER_ID;
        this.buttonFunction = null;
    }

    @Override
    public void setPointerId(int id) {
        if (id >= 0)
            this.paint.setColor(pressedColor);

        this.pointerId = id;
    }

    public void onClick() {
        this.pointerId = MotionEvent.INVALID_POINTER_ID;
        this.paint.setColor(defaultColor);

        if (this.buttonFunction != null)
            this.buttonFunction.click();
    }

    @Override
    public boolean hasId(int id) {
        return pointerId == id;
    }

    @Override
    public boolean isActive() {
        return pointerId >= 0;
    }

    @Override
    public boolean isNotActive() {
        return pointerId < 0;
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawCircle(center.getX(), center.getY(), radius, paint);
    }

    public boolean containsPoint(Point touchEventPosition){
        return VectorUtils.calculateDistance(center, touchEventPosition) <= radius;
    }

    @Override
    public void cancel() {
        this.pointerId = MotionEvent.INVALID_POINTER_ID;
        this.paint.setColor(defaultColor);
    }

    @Override
    public int getId() {
        return pointerId;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center){this.center = center;}
}
