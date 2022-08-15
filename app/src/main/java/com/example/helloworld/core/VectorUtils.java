package com.example.helloworld.core;

import org.jbox2d.common.Vec2;

public class VectorUtils {

    public static float angle(Vec2 a, Vec2 b){
        //return (float) Math.acos(dot(a, b) / (a.length() * b.length()));
        Vec2 na = normalise(a);
        Vec2 nb = normalise(b);
        float dot = Vec2.dot(na, nb);
        float det = Vec2.cross(na, nb);
        return (float) (Math.atan2(det, dot) * 180 / Math.PI);
    }

    public static Vec2 normalise(Vec2 vec){
        Vec2 clone = new Vec2(vec);
        clone.normalize();
        return clone;
    }

    public static Vec2 rotate(Vec2 vec, float angle){
        angle *= Math.PI / 180;
        return new Vec2(
                (float) (Math.cos(angle) * vec.x - Math.sin(angle) * vec.y),
                (float) (Math.sin(angle) * vec.x + Math.cos(angle) * vec.y)
        );
    }
}
