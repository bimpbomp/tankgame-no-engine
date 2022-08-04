package com.example.helloworld.ecs;

import java.util.BitSet;

public class Signature {
    private final BitSet sig;

    public Signature(){
        sig = new BitSet();
    }

    public void set(int bit){
        sig.set(bit);
    }

    public void clear(int bit){
        sig.clear(bit);
    }

    public void clear(){
        sig.clear();
    }

    public boolean matches(Signature signature){
        return sig.equals(signature.sig);
    }
}
