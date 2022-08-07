package com.example.helloworld.ecs;

import androidx.annotation.NonNull;

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

    public boolean get(int bit){
        return sig.get(bit);
    }

    public boolean contains(Signature signature){
        byte[] thisBytes = this.sig.toByteArray();
        BitSet temp = BitSet.valueOf(thisBytes);
        temp.and(signature.sig);

        return temp.equals(this.sig);
    }

    @NonNull
    @Override
    public String toString() {
        return sig.toString();
    }
}
