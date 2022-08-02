package com.example.helloworld.ecs;

import java.util.BitSet;

public class Signature {
    private BitSet sig;

    public Signature(){
        sig = new BitSet();
    }

    public void reset(){
        sig.clear();
    }
}
