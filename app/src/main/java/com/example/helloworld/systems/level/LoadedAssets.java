package com.example.helloworld.systems.level;

import android.content.res.Resources;
import android.graphics.Bitmap;
import com.example.helloworld.systems.render.Sprite;
import org.jbox2d.common.Vec2;

import java.util.HashMap;
import java.util.Map;

public class LoadedAssets {
    private static LoadedAssets instance;
    private final Map<String, Sprite> loadedAssets;
    private static Resources resources;

    public LoadedAssets() {
        loadedAssets = new HashMap<>();
    }

    public void add(String name, Sprite sprite){
        loadedAssets.put(name, sprite);
    }

    public Sprite get(String name){
        if (loadedAssets.containsKey(name))
            return loadedAssets.get(name);
        return null;
    }

    public void clear(){
        loadedAssets.clear();
    }

    public static LoadedAssets getInstance(){
        if (instance == null)
            instance = new LoadedAssets();
        return instance;
    }

    public static Resources getResources() {
        return resources;
    }

    public static void setResources(Resources resources) {
        LoadedAssets.resources = resources;
    }
}
