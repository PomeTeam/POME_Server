package com.example.pomeserver.global.util.supplier;

import java.util.function.LongSupplier;

public class MyLongSupplier implements LongSupplier {
    private long value;

    public MyLongSupplier(Object obj) {
        try{
            this.value = (Long) obj;
        }catch (ClassCastException e){
            this.value = 20;
        }
    }

    @Override
    public long getAsLong() {
        return this.value;
    }
}