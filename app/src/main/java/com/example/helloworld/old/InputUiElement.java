package com.example.helloworld.old;

import android.graphics.Canvas;

public interface InputUiElement {
    boolean containsPoint(Point eventPosition);

    void setPointerId(int id);

    boolean hasId(int id);

    boolean isActive();

    boolean isNotActive();

    int getId();

    void cancel();

    void draw(Canvas canvas);
}
