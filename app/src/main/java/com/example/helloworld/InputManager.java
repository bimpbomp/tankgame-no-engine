package com.example.helloworld;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class InputManager {
    private Dpad dpad;

    public boolean onTouchEvent(MotionEvent event){
        boolean eventHandled;
        int eventIndex = event.getActionIndex();
        Point eventPosition = new Point(event.getX(eventIndex), event.getY(eventIndex));

        eventHandled = dpad.handleInputEvent(event, eventPosition);

        return eventHandled;
    }

    public void draw(Canvas canvas){
        dpad.draw(canvas);
    }

    public Point pollDpadInput(){
        return dpad.pollInputVector();
    }

    public void initialise(Point dpadCenter, int dpadSize){
        dpad = new Dpad(dpadCenter, dpadSize);
    }
}
