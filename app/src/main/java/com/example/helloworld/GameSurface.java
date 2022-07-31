package com.example.helloworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    Paint paint;
    int width;
    int height;
    Context context;
    Loop loop;
    Thread loopThread;

    public GameSurface(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        this.context = context;
        this.paint = new Paint();
    }

    public void draw(float interpolationDelta){
        Canvas canvas = getHolder().lockCanvas();

        if (canvas != null){
            super.draw(canvas);
            // draw background color
            canvas.drawColor(Color.DKGRAY);

            GameObject gO = loop.gameObject;
            paint.setColor(gO.getColor());
            paint.setStrokeWidth(3);
            float interpolatedXPosition = gO.getXPos() + (gO.getVelocity().getX() * interpolationDelta);
            float interpolatedYPosition = gO.getYPos() + (gO.getVelocity().getY() * interpolationDelta);
            canvas.drawRect(interpolatedXPosition, interpolatedYPosition, interpolatedXPosition + gO.getWidth(), interpolatedYPosition + gO.getHeight(), paint);

            // Draw UI
            loop.getInputManager().draw(canvas);
        }
        try {
            getHolder().unlockCanvasAndPost(canvas);
        } catch (IllegalArgumentException e){
            //canvas is destroyed already
        }
    }

    public void startLoopThread(){
        if (this.loop == null) {
            this.loop = new Loop(this);
            loop.initialiseGame();
            int dpadSize = 400;
            loop.getInputManager().initialise(new Point(dpadSize / 2f, getHeight() - dpadSize / 2f), dpadSize);
        }

        if (this.loopThread == null)
            this.loopThread = new Thread(this.loop);

        this.loopThread.start();
    }

    public void endLoopThread(){
        boolean retry = true;

        while(retry){
            try {
                this.loop.setGameIsRunning(false);
                this.loopThread.join();
                this.loopThread = null;
            } catch (InterruptedException e){
                Log.d("LOADING", "INTERRUPTED EXCEPTION JOINING loop thread");
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        startLoopThread();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        width = i1;
        height = i2;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        endLoopThread();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (loop.getInputManager() != null){
            return loop.getInputManager().onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}
