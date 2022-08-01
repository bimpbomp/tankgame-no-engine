package com.example.helloworld;

import android.graphics.Color;
import android.util.Log;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

// Loop taken from: https://dewitters.com/dewitters-gameloop/
public class Loop implements Runnable {
    private GameSurface gameSurface;
    private InputManager inputManager;
    private boolean gameIsRunning;

    private BoundingBox levelBoundary;
    public GameObject gameObject;
    public GameObject gameObject2;

    public World world;
    public Body body;

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
        gameObject = new GameObject(0, 0, 100, 100, Color.GREEN);
        gameObject2 = new GameObject(0, 100, 100, 100, Color.RED);
        levelBoundary = new BoundingBox(0, 0, gameSurface.getWidth(), gameSurface.getHeight());

        Vec2 gravity = new Vec2(0f, -10f);
        world = new World(gravity);
        //body definition
        BodyDef bd = new BodyDef();
        bd.position.set(0, 0);
        bd.type = BodyType.DYNAMIC;

        //define shape of the body.
        CircleShape cs = new CircleShape();
        cs.m_radius = 0.5f;

        //define fixture of the body.
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.5f;
        fd.friction = 0.3f;
        fd.restitution = 0.5f;

        //create the body and add fixture to it
        body =  world.createBody(bd);
        body.createFixture(fd);
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
        float timeStep = 1.0f / 60.f;
        int velocityIterations = 6;
        int positionIterations = 2;
        world.step(timeStep, velocityIterations, positionIterations);
        Vec2 position = body.getPosition();
        float angle = body.getAngle();

        gameObject.setXPos(position.x);
        gameObject.setYPos(position.y);
    }

    public void setGameIsRunning(boolean isRunning){
        this.gameIsRunning = isRunning;
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}
