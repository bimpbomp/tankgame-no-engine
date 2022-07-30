package com.example.helloworld;

import android.graphics.Color;
import android.util.Log;

// Loop taken from: https://dewitters.com/dewitters-gameloop/
public class Loop implements Runnable {
    private GameSurface gameSurface;
    private boolean gameIsRunning;

    public GameObject gameObject;
    private int xDirection = -1;
    private int yDirection = -1;

    public Loop(GameSurface gameSurface) {
        this.gameSurface = gameSurface;
    }

    @Override
    public void run() {
        int TICKS_PER_SECOND = 25;
        int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
        int MAX_FRAMESKIP = 5;

        long nextGameTick = System.currentTimeMillis();
        int loops;
        float interpolation;

        initialiseGame();

        Log.d("Loading", "Game loop starting");
        gameIsRunning = true;
        while (gameIsRunning) {
            loops = 0;

            while (System.currentTimeMillis() > nextGameTick && loops < MAX_FRAMESKIP) {
                updateGame();

                nextGameTick += SKIP_TICKS;
                loops++;
            }

            interpolation = (float) (System.currentTimeMillis() + SKIP_TICKS - nextGameTick) / (float) SKIP_TICKS;

            // use interpolation like this: view_position = position + (speed * interpolation)
            this.gameSurface.draw(interpolation);
        }
    }

    public void initialiseGame(){
        gameObject = new GameObject(0, 0, 100, 100, Color.GREEN);
    }

    public void updateGame(){
        int screenWidth = gameSurface.width;
        int newX = gameObject.getxPos() + (20 * xDirection);
        boolean xBoundaryReached = false;

        // check if new position means object leaves screen x boundary
        if (newX > screenWidth - gameObject.getWidth()){
            newX = screenWidth - gameObject.getWidth();
            gameObject.setColor(Color.RED);
            xDirection *= -1;
            xBoundaryReached = true;
        } else if (newX < 0) {
            newX = 0;
            gameObject.setColor(Color.GREEN);
            xDirection *= -1;
            xBoundaryReached = true;
        }
        gameObject.setxPos(newX);

        // move the y position if x boundary hit
        if (xBoundaryReached){
            int screenHeight = gameSurface.height;
            int newY = gameObject.getyPos() + (screenHeight / 4 * yDirection);

            // check if new position means object leaves screen y boundary
            if (newY > screenHeight - gameObject.getHeight()){
                newY = screenHeight - gameObject.getHeight();
                yDirection *= -1;
            } else if (newY <= 0){
                newY = 0;
                yDirection *= -1;
            }

            gameObject.setyPos(newY);
        }
    }

    public void setGameIsRunning(boolean isRunning){
        this.gameIsRunning = isRunning;
    }
}
