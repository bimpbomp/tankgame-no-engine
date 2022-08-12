package com.example.helloworld.systems.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import com.example.helloworld.components.*;
import com.example.helloworld.components.renderable.Renderable;
import com.example.helloworld.components.renderable.RenderableType;
import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.android.GameSurface;
import com.example.helloworld.core.observer.GameEvent;
import com.example.helloworld.core.observer.GameEventType;
import com.example.helloworld.core.observer.ISubscriber;
import org.jbox2d.common.Vec2;

import java.util.*;

public class RenderSystem extends GameSystem implements ISubscriber {
    private final List<SortableRenderable> sortedEntities;
    private final Set<Entity> newEntities;
    private final GameSurface gameSurface;
    private final Paint paint;
    private boolean entitiesAdded;
    private Canvas canvas;
    private Camera camera;
    // zoom in/out
    public static final int WORLD_TO_SCREEN_SCALE_FACTOR = 100;

    public RenderSystem(Coordinator coordinator, GameSurface gameSurface) {
        super(coordinator);
        signature.set(Component.getType(Renderable.class));

        this.sortedEntities = new ArrayList<>();
        this.newEntities = new HashSet<>();
        this.gameSurface = gameSurface;
        this.paint = new Paint();
        this.entitiesAdded = false;
    }

    @Override
    public void update(float delta) {
        if (entitiesAdded){
            processNewEntities();
        }

        canvas = gameSurface.getHolder().lockCanvas();
        if (canvas != null){
            gameSurface.draw(canvas);

            // draw background color
            canvas.drawColor(Color.DKGRAY);

            for (SortableRenderable sortableRenderable : sortedEntities){
                // determine entity's renderable type and render appropriately
                RenderableType entityType = sortableRenderable.renderable.type;
                Vec2 screenCoordinates = convertToScreenCoordinates(sortableRenderable.entity, sortableRenderable.renderable);

                switch (entityType) {
                    case POLYGON:
                        renderPolygon(screenCoordinates, sortableRenderable.renderable);
                        break;
                    case CIRCLE:
                        renderCircle(screenCoordinates, sortableRenderable.renderable);
                        break;
                    case TEXT:
                        renderText(screenCoordinates, sortableRenderable.renderable);
                        break;
                    case SPRITE:
                        renderSprite(screenCoordinates, sortableRenderable.renderable);
                        break;
                }
            }
        }
        try {
            gameSurface.getHolder().unlockCanvasAndPost(canvas);
        } catch (IllegalArgumentException e){
            //canvas is destroyed already
        }
    }

    public void renderPolygon(Vec2 screenCoordinates, Renderable renderablePolygon){
        paint.setColor(renderablePolygon.color);

        if (renderablePolygon.vertexVectors.size() > 0){
            Path path = new Path();
            float x = screenCoordinates.x - renderablePolygon.vertexVectors.get(0).x;
            float y = screenCoordinates.y - renderablePolygon.vertexVectors.get(0).y;
            path.moveTo(x, y);

            for (int i = 1; i < renderablePolygon.vertexVectors.size(); i++){
                x = screenCoordinates.x - renderablePolygon.vertexVectors.get(i).x;
                y = screenCoordinates.y - renderablePolygon.vertexVectors.get(i).y;
                path.lineTo(x, y);
            }
            canvas.drawPath(path, paint);
        }
    }

    public void renderText(Vec2 screenCoordinates, Renderable renderableText){

    }

    public void renderCircle(Vec2 screenCoordinates, Renderable renderableCircle){

    }

    public void renderSprite(Vec2 screenCoordinates, Renderable renderableSprite){

    }

    private Vec2 convertToScreenCoordinates(Entity entity, Renderable renderable){
        if (renderable.isScreenElement) {
            return entity.position;
        }else {
            return new Vec2(
                    entity.position.x * WORLD_TO_SCREEN_SCALE_FACTOR - (camera.entity.position.x * WORLD_TO_SCREEN_SCALE_FACTOR - camera.viewport.width / 2f),
                    entity.position.y * WORLD_TO_SCREEN_SCALE_FACTOR - (camera.entity.position.y * WORLD_TO_SCREEN_SCALE_FACTOR - camera.viewport.height / 2f)
            );
        }
    }

    public void processNewEntities(){
        for (Entity entity : newEntities){
            Renderable renderable = (Renderable) coordinator.getComponent(entity, Renderable.class);

            if (renderable != null){
                Log.d("Loading", "new render entity: " + entity.id + ", type: " + renderable.type);
                sortedEntities.add(new SortableRenderable(entity, renderable));
            }
        }
        Collections.sort(sortedEntities);
        newEntities.clear();
        entitiesAdded = false;
    }

    private void setViewport(Entity entity) {
        this.camera = new Camera();
        this.camera.entity = entity;
        this.camera.viewport = (Viewport) coordinator.getComponent(entity, Viewport.class);
    }

    @Override
    public void removeEntity(Entity entity) {
        super.removeEntity(entity);
        for (SortableRenderable sortableRenderable : this.sortedEntities){
            if (sortableRenderable.entity.equals(entity)){
                Log.d("Loading", "removed render entity: " + entity.id);
                this.sortedEntities.remove(sortableRenderable);
                break;
            }
        }
    }

    @Override
    public void addEntity(Entity entity) {
        super.addEntity(entity);

        newEntities.add(entity);
        entitiesAdded = true;
    }

    @Override
    public void onNotify(GameEvent event) {
        if (event.getEventType() == GameEventType.VIEWPORT_CHANGE){
            this.setViewport(((ViewportChangeEvent) event).viewport);
        }
    }

    private static class SortableRenderable implements Comparable<SortableRenderable>{
        public Entity entity;
        public Renderable renderable;

        public SortableRenderable(Entity entity, Renderable renderable) {
            this.entity = entity;
            this.renderable = renderable;
        }

        @Override
        public int compareTo(SortableRenderable sortableRenderable) {
            return -1 * Integer.compare(renderable.zOrder, sortableRenderable.renderable.zOrder);
        }
    }

    private static class Camera {
        public Entity entity;
        public Viewport viewport;
    }
}
