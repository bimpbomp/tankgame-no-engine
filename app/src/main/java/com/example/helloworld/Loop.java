package com.example.helloworld;

import android.graphics.Color;
import android.util.Log;

// Loop taken from: https://dewitters.com/dewitters-gameloop/
public class Loop implements Runnable {
    private GameSurface gameSurface;
    private InputManager inputManager;
    private boolean gameIsRunning;

    private BoundingBox levelBoundary;
    public GameObject gameObject;
    public GameObject gameObject2;

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

            this.gameSurface.draw(interpolation);
        }
    }

    public void initialiseGame(){
        inputManager = new InputManager();
        gameObject = new GameObject(100, 100, 100, 100, Color.GREEN);
        gameObject2 = new GameObject(0, 0, 100, 100, Color.RED);
        levelBoundary = new BoundingBox(0, 0, gameSurface.getWidth(), gameSurface.getHeight());
    }

    public void updateGame(){
        Point inputUnitVector = VectorUtils.getUnitVector(inputManager.pollDpadInput());
        Point gameObjectVelocity = new Point(
                inputUnitVector.getX() * gameObject.getMaxSpeed(),
                inputUnitVector.getY() * gameObject.getMaxSpeed()
        );

        gameObject.setVelocity(gameObjectVelocity);
        gameObject.setXPos(gameObject.getXPos() + gameObjectVelocity.getX());
        gameObject.setYPos(gameObject.getYPos() + gameObjectVelocity.getY());

        //PhysicsManager.applyPhysics(gameObject, levelBoundary);
    }

    public void setGameIsRunning(boolean isRunning){
        this.gameIsRunning = isRunning;
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}
