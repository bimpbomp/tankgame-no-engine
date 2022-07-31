package com.example.helloworld;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

public class Dpad{
    private Button up;
    private Button down;
    private Button left;
    private Button right;
    private BoundingBox boundingBox;

    public Dpad(Point center, int size){
        int buttonRadius = size / 6;
        int buttonSpacing = buttonRadius * 2;
        up = new Button(new Point(center.getX(), center.getY() - buttonSpacing), buttonRadius, Color.LTGRAY);
        down = new Button(new Point(center.getX(), center.getY() + buttonSpacing), buttonRadius, Color.LTGRAY);
        left = new Button(new Point(center.getX() - buttonSpacing, center.getY()), buttonRadius, Color.LTGRAY);
        right = new Button(new Point(center.getX() + buttonSpacing, center.getY()), buttonRadius, Color.LTGRAY);

        boundingBox = new BoundingBox(
                center.getX() - buttonRadius * 3,
                center.getY() - buttonRadius * 3,
                center.getX() + buttonRadius * 3,
                center.getY() + buttonRadius * 3
        );
    }

    public boolean handleInputEvent(MotionEvent event, Point eventPosition){
        int eventIndex = event.getActionIndex();
        int eventId = event.getPointerId(eventIndex);
        Button relevantButton;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (boundingBox.containsPoint(eventPosition)) {
                    if (up.containsPoint(eventPosition)) {
                        relevantButton = up;
                    } else if (down.containsPoint(eventPosition)) {
                        relevantButton = down;
                    } else if (left.containsPoint(eventPosition)) {
                        relevantButton = left;
                    } else if (right.containsPoint(eventPosition)) {
                        relevantButton = right;
                    } else {
                        return false;
                    }

                    if (relevantButton.isNotActive() && relevantButton.containsPoint(eventPosition)) {
                        relevantButton.setPointerId(eventId);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (up.hasId(eventId)){
                    relevantButton = up;
                } else if (down.hasId(eventId)) {
                    relevantButton = down;
                } else if (left.hasId(eventId)) {
                    relevantButton = left;
                } else if (right.hasId(eventId)) {
                    relevantButton = right;
                } else {
                    return false;
                }

                if (relevantButton.hasId(eventId)) {
                    relevantButton.onClick();
                    relevantButton.cancel();
                }
                break;
            default:
                return false;
        }

        return true;
    }

    public Point pollInputVector(){
        int x = 0;
        int y = 0;

        if (up.isActive()){
            y -= 1;
        }

        if (down.isActive()){
            y += 1;
        }

        if (left.isActive()){
            x -= 1;
        }

        if (right.isActive()){
            x += 1;
        }

        return new Point(x, y);
    }

    public void draw(Canvas canvas) {
        up.draw(canvas);
        down.draw(canvas);
        left.draw(canvas);
        right.draw(canvas);
    }
}
