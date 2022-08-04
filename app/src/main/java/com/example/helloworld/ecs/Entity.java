package com.example.helloworld.ecs;

import androidx.annotation.Nullable;

public class Entity {
    public int id;
    public Signature signature;

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
