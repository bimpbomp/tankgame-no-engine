package com.example.helloworld;

import com.example.helloworld.components.renderable.RenderablePolygon;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolygonFactory {
    public static RenderablePolygon generateRectangle(int width, int height){
        RenderablePolygon renderablePolygon = new RenderablePolygon();
        renderablePolygon.vertices = new ArrayList<>(Arrays.asList(
                new Vec2(0, 0),
                new Vec2(width, 0),
                new Vec2(width, height),
                new Vec2(0, height)
        ));;
        renderablePolygon.center = new Vec2(width / 2f, height / 2f);
        generateVertexVectors(renderablePolygon);
        return renderablePolygon;
    }

    public static RenderablePolygon generateTriangle(int baseWidth, int height){
        RenderablePolygon renderablePolygon = new RenderablePolygon();
        List<Vec2> vertices = new ArrayList<>(Arrays.asList(
                new Vec2(baseWidth / 2f, 0),
                new Vec2(baseWidth, height),
                new Vec2(0, height)
        ));
        renderablePolygon.vertices = vertices;

        //calculating centroid center of triangle.
        Vec2 center = new Vec2(0f, 0f);
        for (Vec2 vertex : vertices){
            center.set(vertex.x + center.x, vertex.y + center.y);
        }
        center.set(center.x / 3f, center.y / 3f);

        renderablePolygon.center = center;
        generateVertexVectors(renderablePolygon);
        return renderablePolygon;
    }

    private static void generateVertexVectors(RenderablePolygon renderablePolygon){
        renderablePolygon.vertexVectors = new ArrayList<>();
        for (int i = 0; i < renderablePolygon.vertices.size(); i++){
            renderablePolygon.vertexVectors.add(renderablePolygon.vertices.get(i).add(renderablePolygon.center.mul(-1f)));
        }
    }
}
