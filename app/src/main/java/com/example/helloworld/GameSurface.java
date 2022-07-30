package com.example.helloworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
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

    public void draw(float interpolation){
        Canvas canvas = getHolder().lockCanvas();

        if (canvas != null){
            super.draw(canvas);
            // draw background color
            canvas.drawColor(Color.DKGRAY);

            GameObject gO = loop.gameObject;
            paint.setColor(gO.getColor());
            paint.setStrokeWidth(3);
            canvas.drawRect(gO.getxPos(), gO.getyPos(), gO.getxPos() + gO.getWidth(), gO.getyPos() + gO.getHeight(), paint);
        }
        try {
            getHolder().unlockCanvasAndPost(canvas);
        } catch (IllegalArgumentException e){
            //canvas is destroyed already
        }
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        width = getWidth();
//        height = getHeight();
//        paint.setColor(Color.GREEN);
//        paint.setStrokeWidth(3);
//
//        canvas.drawRect(rectX, rectY, rectW, rectH, paint);
//    }

    public void startLoopThread(){
        this.loop = new Loop(this);
        this.loopThread = new Thread(this.loop);
        this.loopThread.start();
    }

    public void endLoopThread(){
        boolean retry = true;

        while(retry){
            try {
                this.loop.setGameIsRunning(false);
                this.loopThread.join();
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
}
