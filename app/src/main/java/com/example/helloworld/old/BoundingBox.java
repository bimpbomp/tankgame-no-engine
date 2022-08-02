package com.example.helloworld.old;

public class BoundingBox {
    private float left;
    private float top;
    private float right;
    private float bottom;

    public BoundingBox(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean containsPoint(Point p){
        return p.getX() >= left && p.getX() <= right && p.getY() >= top && p.getY() <= bottom;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }
}
