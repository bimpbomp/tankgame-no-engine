package com.example.helloworld.systems.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import com.example.helloworld.components.*;
import com.example.helloworld.components.renderable.*;
import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.android.GameSurface;
import org.jbox2d.common.Vec2;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class RenderSystem extends GameSystem {
    private final Set<SortableRenderable> sortedEntities;
    private final Set<Entity> newEntities;
    private final GameSurface gameSurface;
    private final Paint paint;
    private boolean entitiesAdded;
    private Canvas canvas;
    private Camera camera;

    public RenderSystem(Coordinator coordinator, GameSurface gameSurface) {
        super(coordinator);
        this.sortedEntities = new TreeSet<>();
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
                int entityType = sortableRenderable.renderable.getType();
                Vec2 screenCoordinates = convertToScreenCoordinates(sortableRenderable.entity, sortableRenderable.renderable);

                if (entityType == Component.getType(RenderablePolygon.class)){
                    renderPolygon(screenCoordinates, (RenderablePolygon) sortableRenderable.renderable);
                } else if (entityType == Component.getType(RenderableCircle.class)) {
                    renderCircle(screenCoordinates, (RenderableCircle) sortableRenderable.renderable);
                } else if (entityType == Component.getType(RenderableText.class)) {
                    renderText(screenCoordinates, (RenderableText) sortableRenderable.renderable);
                } else if (entityType == Component.getType(RenderableSprite.class)) {
                    renderSprite(screenCoordinates, (RenderableSprite) sortableRenderable.renderable);
                }
            }
        }
        try {
            gameSurface.getHolder().unlockCanvasAndPost(canvas);
        } catch (IllegalArgumentException e){
            //canvas is destroyed already
        }
    }

    public void renderPolygon(Vec2 screenCoordinates, RenderablePolygon renderablePolygon){
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

    public void renderText(Vec2 screenCoordinates, RenderableText renderableText){

    }

    public void renderCircle(Vec2 screenCoordinates, RenderableCircle renderableCircle){

    }

    public void renderSprite(Vec2 screenCoordinates, RenderableSprite renderableSprite){

    }

    private Vec2 convertToScreenCoordinates(Entity entity, Renderable renderable){
        if (renderable.isScreenElement)
            return entity.position;
        else {
            return new Vec2(
                    entity.position.x - (camera.entity.position.x - camera.viewport.width / 2f),
                    entity.position.y - (camera.entity.position.y - camera.viewport.height / 2f)
            );
        }
    }

    public void processNewEntities(){
        for (Entity entity : newEntities){
            Renderable renderable;

            if (entity.signature.get(Component.getType(RenderablePolygon.class))){
                renderable = (Renderable) coordinator.getComponent(entity, RenderablePolygon.class);
            } else if (entity.signature.get(Component.getType(RenderableCircle.class))) {
                renderable = (Renderable) coordinator.getComponent(entity, RenderableCircle.class);
            } else if (entity.signature.get(Component.getType(RenderableText.class))) {
                renderable = (Renderable) coordinator.getComponent(entity, RenderableText.class);
            } else if (entity.signature.get(Component.getType(RenderableSprite.class))) {
                renderable = (Renderable) coordinator.getComponent(entity, RenderableSprite.class);
            } else {
                renderable = null;
            }

            if (renderable != null){
                sortedEntities.add(new SortableRenderable(entity, renderable));
            }
        }
        newEntities.clear();
        entitiesAdded = false;
    }

    public void setViewport(Entity entity) {
        this.camera = new Camera();
        this.camera.entity = entity;
        this.camera.viewport = (Viewport) coordinator.getComponent(entity, Viewport.class);
    }

    @Override
    public void removeEntity(Entity entity) {
        super.removeEntity(entity);
        for (SortableRenderable sortableRenderable : this.sortedEntities){
            if (sortableRenderable.entity.equals(entity)){
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
