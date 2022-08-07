package com.example.helloworld.components.renderable;

import org.jbox2d.common.Vec2;

import java.util.List;

public class RenderablePolygon extends Renderable {
    public int color;
    public List<Vec2> vertices;
    public List<Vec2> vertexVectors;
    public Vec2 center;
}
