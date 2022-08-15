package com.example.helloworld.core.android;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import com.example.helloworld.core.Loop;
import com.example.helloworld.systems.level.LoadedAssets;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    Context context;
    Loop loop;
    Thread loopThread;
    private AndroidInputSystem inputSystem;

    public GameSurface(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        this.context = context;
        this.inputSystem = new AndroidInputSystem();
        LoadedAssets.setResources(getResources());
    }

    public void startLoopThread(){
        if (this.loop == null) {
            this.loop = new Loop();
            loop.initialiseGame(this);
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

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        endLoopThread();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = inputSystem.onTouchEvent(event);
        if (handled){
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }
}
