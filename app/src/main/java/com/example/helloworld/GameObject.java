package com.example.helloworld;

public class GameObject {
    private float xPos;
    private float yPos;
    private int width;
    private int height;
    private int color;
    private Point velocity;
    private final float maxSpeed = 50f;

    public GameObject(float xPos, float yPos, int width, int height, int color) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.color = color;
        this.velocity = new Point(0,0);
    }

    public float getXPos() {
        return xPos;
    }

    public float getYPos() {
        return yPos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getColor() {
        return color;
    }

    public Point getVelocity() {
        return velocity;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

    public void setXPos(float xPos) {
        this.xPos = xPos;
    }

    public void setYPos(float yPos) {
        this.yPos = yPos;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
