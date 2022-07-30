package com.example.helloworld;

public class GameObject {
    private int xPos;
    private int yPos;
    private int width;
    private int height;
    private int color;

    public GameObject(int xPos, int yPos, int width, int height, int color) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
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

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
