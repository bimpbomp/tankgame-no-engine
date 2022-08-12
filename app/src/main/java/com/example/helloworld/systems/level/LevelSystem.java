package com.example.helloworld.systems.level;

import android.graphics.Color;
import android.util.Log;
import com.example.helloworld.components.PhysicsComponent;
import com.example.helloworld.components.TankInput;
import com.example.helloworld.components.Viewport;
import com.example.helloworld.components.renderable.Renderable;
import com.example.helloworld.core.CoordinateSystem;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.observer.*;
import com.example.helloworld.systems.physics.PhysicsBodyGenerator;
import com.example.helloworld.systems.render.PolygonFactory;
import com.example.helloworld.systems.render.ViewportChangeEvent;
import org.jbox2d.common.Vec2;

public class LevelSystem extends GameSystem implements ISubscriber {
    private boolean needToLoad;
    private Publisher viewportEventPublisher;

    public LevelSystem(Coordinator coordinator) {
        super(coordinator);
        PublisherHub.getInstance().subscribe(GameEventType.LEVEL_CHANGE, this);
        needToLoad = true;
        this.viewportEventPublisher = PublisherHub.getInstance().createNewPublisher(GameEventType.VIEWPORT_CHANGE);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
        if (needToLoad){
            unloadCurrentLevel();
            loadNewLevel();
            needToLoad = false;
        }
    }

    @Override
    public void onNotify(GameEvent event) {
        if (event.getEventType() == GameEventType.LEVEL_CHANGE)
            needToLoad = true;
    }

    private void loadNewLevel(){
        // player
        Entity player = WorldObjectGenerator.generateTank(coordinator, new Vec2(10, 10), 1, 1, 99, Color.GREEN);
        coordinator.setPlayer(player);

        // generate walls
        WorldObjectGenerator.generateWall(coordinator, new Vec2(10, 13), 4, 1, 99);
        WorldObjectGenerator.generateWall(coordinator, new Vec2(10, 7), 4, 1, 99);
        WorldObjectGenerator.generateWall(coordinator, new Vec2(7, 10), 1, 4, 99);
        WorldObjectGenerator.generateWall(coordinator, new Vec2(13, 10), 1, 4, 99);

        Log.d("Loading", "New level loaded");
    }

    private void unloadCurrentLevel(){
        Log.d("Loading", "Unloading current level...");
    }
}
