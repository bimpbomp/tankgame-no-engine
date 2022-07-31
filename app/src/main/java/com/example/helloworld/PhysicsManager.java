package com.example.helloworld;

public class PhysicsManager {

    public static void applyPhysics(GameObject gameObject, BoundingBox levelBoundary){
        float levelWidth = Math.abs(levelBoundary.getRight() - levelBoundary.getLeft());
        float levelHeight = Math.abs(levelBoundary.getBottom() - levelBoundary.getTop());

        float newX = gameObject.getXPos();
        float newY = gameObject.getYPos();
        float newXVelocity = gameObject.getVelocity().getX();
        float newYVelocity = gameObject.getVelocity().getY();

        // check if new position means object leaves screen x boundary
        if (newX > levelWidth - gameObject.getWidth()){
            newX = levelWidth - gameObject.getWidth();
            newXVelocity = 0;
        } else if (newX < 0) {
            newX = 0;
            newXVelocity = 0;
        }

        // check if new position means object leaves screen y boundary
        if (newY > levelHeight - gameObject.getHeight()){
            newY = levelHeight - gameObject.getHeight();
            newYVelocity = 0;
        } else if (newY <= 0){
            newY = 0;
            newYVelocity = 0;
        }
        gameObject.setXPos(newX);
        gameObject.setYPos(newY);
        gameObject.setVelocity(new Point(newXVelocity, newYVelocity));
    }
}
