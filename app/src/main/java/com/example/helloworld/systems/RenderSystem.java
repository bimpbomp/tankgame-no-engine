package com.example.helloworld.systems;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import com.example.helloworld.components.Render;
import com.example.helloworld.components.Transform;
import com.example.helloworld.ecs.Coordinator;
import com.example.helloworld.ecs.Entity;
import com.example.helloworld.ecs.GameSystem;
import com.example.helloworld.old.BoundingBox;
import com.example.helloworld.old.GameObject;
import com.example.helloworld.old.GameSurface;
import org.jbox2d.common.Vec2;

public class RenderSystem extends GameSystem {
    private GameSurface gameSurface;
    private BoundingBox viewport;
    private Paint paint;

    public RenderSystem(Coordinator coordinator, GameSurface gameSurface) {
        super(coordinator);
        this.gameSurface = gameSurface;
        this.paint = new Paint();
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

        Canvas canvas = gameSurface.getHolder().lockCanvas();

        if (canvas != null){
            gameSurface.draw(canvas);
            // draw background color
            canvas.drawColor(Color.DKGRAY);

            for (Entity entity : entities){
                Render render = (Render) coordinator.getComponent(entity, Render.class);
                Transform transform = (Transform) coordinator.getComponent(entity, Transform.class);
                Vec2 position = transform.position;

                paint.setColor(render.color);
                canvas.drawRect(position.x - transform.scale/2f,
                        position.y - transform.scale/2f,
                        position.x + transform.scale/2f,
                        position.y + transform.scale/2f,
                        paint);
            }

            //todo Draw UI
            //loop.getInputManager().draw(canvas);
        }
        try {
            gameSurface.getHolder().unlockCanvasAndPost(canvas);
        } catch (IllegalArgumentException e){
            //canvas is destroyed already
        }
    }
}
