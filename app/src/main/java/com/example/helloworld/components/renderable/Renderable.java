package com.example.helloworld.components.renderable;

import android.graphics.Bitmap;
import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.systems.render.Sprite;
import org.jbox2d.common.Vec2;

import java.util.List;

public class Renderable extends Component {
    public int zOrder;
    // maybe hacky, but allows UI elements to be defined relative to the viewport
    // defaulted to false so most game objects won't even care that this is here.
    public boolean isScreenElement = false;
    public RenderableType type;

    // used for polygon and circle
    public int color;

    // polygon only
    public List<Vec2> vertices;
    public List<Vec2> vertexVectors;

    // polygon and sprite
    public Vec2 center;

    // circle only
    public float radius;

    // sprite only
    public Sprite sprite;
}
