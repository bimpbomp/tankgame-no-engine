package com.example.helloworld.core.ecs;

import androidx.annotation.Nullable;
import org.jbox2d.common.Vec2;

public class Entity {
    public int id;
    public Signature signature;
    public Vec2 position = new Vec2();
    public float angle = 0f;
    public float scale = 1f;

    public Entity(int id){
        this.id = id;
        this.signature = new Signature();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Entity)) {
            return false;
        }

        return ((Entity) obj).id == id;
    }
}
