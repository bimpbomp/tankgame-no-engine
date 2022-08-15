package com.example.helloworld.systems.level;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import com.example.helloworld.R;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.observer.*;
import com.example.helloworld.systems.render.Sprite;
import org.jbox2d.common.Vec2;

public class LevelSystem extends GameSystem implements ISubscriber {
    private boolean needToLoad;

    public LevelSystem(Coordinator coordinator) {
        super(coordinator);
        PublisherHub.getInstance().subscribe(GameEventType.LEVEL_CHANGE, this);
        needToLoad = true;
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
        // asset management
        loadAssets();

        // player
        Entity player = WorldObjectGenerator.generateTank(coordinator, new Vec2(10, 10), 1, 1, 99);
        coordinator.setPlayer(player);

        // generate walls
        WorldObjectGenerator.generateWall(coordinator, new Vec2(10, 13), 4, 1, 99);
        WorldObjectGenerator.generateWall(coordinator, new Vec2(10, 7), 4, 1, 99);
        WorldObjectGenerator.generateWall(coordinator, new Vec2(7, 10), 1, 4, 99);
        WorldObjectGenerator.generateWall(coordinator, new Vec2(13, 10), 1, 4, 99);

        Log.d("Loading", "New level loaded");
    }

    private void loadAssets(){
        LoadedAssets.getInstance().clear();

        Bitmap bitmap = BitmapFactory.decodeResource(LoadedAssets.getResources(), R.raw.body_tracks);
        int scaleToUse = 10; // this will be our percentage
        int sizeY = coordinator.getPlayerViewportComponent().height * scaleToUse / 100;
        int sizeX = bitmap.getWidth() * sizeY / bitmap.getHeight();
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, sizeX, sizeY, false);

        Log.d("Loading", "Sprite wxh: " + scaled.getWidth() + "x" + scaled.getHeight());
        Sprite sprite = new Sprite();
        sprite.centerXOffset = scaled.getWidth() / 2f;
        sprite.centerYOffset = scaled.getHeight() / 2f;
        sprite.bitmap = scaled;

        LoadedAssets.getInstance().add(R.raw.body_tracks + "", sprite);
    }

    private void unloadCurrentLevel(){
        Log.d("Loading", "Unloading current level...");
    }
}
