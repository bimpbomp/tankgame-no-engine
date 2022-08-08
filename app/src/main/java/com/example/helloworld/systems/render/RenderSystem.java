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
        // todo implement camera object that follows player
//        viewport = new BoundingBox(
//                playerX - gameSurface.getWidth()/2f,
//                playerY - gameSurface.getHeight()/2f,
//                playerX + gameSurface.getWidth()/2f,
//                playerY + gameSurface.getHeight()/2f
//        );

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
                if (entityType == Component.getType(RenderablePolygon.class)){
                    renderPolygon(sortableRenderable.entity, (RenderablePolygon) sortableRenderable.renderable);
                } else if (entityType == Component.getType(RenderableCircle.class)) {
                    renderCircle(sortableRenderable.entity, (RenderableCircle) sortableRenderable.renderable);
                } else if (entityType == Component.getType(RenderableText.class)) {
                    renderText(sortableRenderable.entity, (RenderableText) sortableRenderable.renderable);
                } else if (entityType == Component.getType(RenderableSprite.class)) {
                    renderSprite(sortableRenderable.entity, (RenderableSprite) sortableRenderable.renderable);
                }
            }
        }
        try {
            gameSurface.getHolder().unlockCanvasAndPost(canvas);
        } catch (IllegalArgumentException e){
            //canvas is destroyed already
        }
    }

    public void renderPolygon(Entity entity, RenderablePolygon renderablePolygon){
        paint.setColor(renderablePolygon.color);
        Transform transform = (Transform) coordinator.getComponent(entity, Component.getType(Transform.class));
        Vec2 centerScreenCoordinates = convertToScreenCoordinates(transform.position);

        if (renderablePolygon.vertexVectors.size() > 0){
            Path path = new Path();
            float x = centerScreenCoordinates.x - renderablePolygon.vertexVectors.get(0).x;
            float y = centerScreenCoordinates.y - renderablePolygon.vertexVectors.get(0).y;
            path.moveTo(x, y);

            for (int i = 1; i < renderablePolygon.vertexVectors.size(); i++){
                x = centerScreenCoordinates.x - renderablePolygon.vertexVectors.get(i).x;
                y = centerScreenCoordinates.y - renderablePolygon.vertexVectors.get(i).y;
                path.lineTo(x, y);
            }
            canvas.drawPath(path, paint);
        }
    }

    public void renderText(Entity entity, RenderableText renderableText){

    }

    public void renderCircle(Entity entity, RenderableCircle renderableCircle){

    }

    public void renderSprite(Entity entity, RenderableSprite renderableSprite){

    }

    private Vec2 convertToScreenCoordinates(Vec2 gamePosition){
        return new Vec2(
                gamePosition.x - (camera.transform.position.x - camera.viewport.width / 2f),
                gamePosition.y - (camera.transform.position.y - camera.viewport.height / 2f)
        );
    }

    public void processNewEntities(){
        for (Entity entity : newEntities){
            Renderable renderable;

            if (entity.signature.get(Component.getType(RenderablePolygon.class))){
                renderable = (Renderable) coordinator.getComponent(entity, RenderablePolygon.class);
            } else if (entity.signature.get(Component.getType(RenderableText.class))) {
                renderable = (Renderable) coordinator.getComponent(entity, RenderablePolygon.class);
            } else if (entity.signature.get(Component.getType(RenderableSprite.class))) {
                renderable = (Renderable) coordinator.getComponent(entity, RenderablePolygon.class);
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
        this.camera.transform = (Transform) coordinator.getComponent(entity, Transform.class);
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
        public Transform transform;
    }
}
