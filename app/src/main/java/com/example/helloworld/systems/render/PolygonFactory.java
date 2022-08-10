package com.example.helloworld.systems.render;

import com.example.helloworld.components.renderable.Renderable;
import com.example.helloworld.components.renderable.RenderableType;
import org.jbox2d.common.Vec2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolygonFactory {
    public static Renderable generateRectangle(int width, int height){
        Renderable renderablePolygon = new Renderable();
        renderablePolygon.vertices = new ArrayList<>(Arrays.asList(
                new Vec2(0, 0),
                new Vec2(width, 0),
                new Vec2(width, height),
                new Vec2(0, height)
        ));;
        renderablePolygon.center = new Vec2(width / 2f, height / 2f);
        renderablePolygon.type = RenderableType.POLYGON;
        generateVertexVectors(renderablePolygon);
        return renderablePolygon;
    }

    public static Renderable generateTriangle(int baseWidth, int height){
        Renderable renderablePolygon = new Renderable();
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
        renderablePolygon.type = RenderableType.POLYGON;
        generateVertexVectors(renderablePolygon);
        return renderablePolygon;
    }

    private static void generateVertexVectors(Renderable renderablePolygon){
        renderablePolygon.vertexVectors = new ArrayList<>();
        for (int i = 0; i < renderablePolygon.vertices.size(); i++){
            renderablePolygon.vertexVectors.add(renderablePolygon.vertices.get(i).add(renderablePolygon.center.mul(-1f)));
        }
    }
}
