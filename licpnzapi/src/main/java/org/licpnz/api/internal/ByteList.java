package org.licpnz.api.internal;

import java.util.Arrays;
/**
 * Created by Ilya on 17.11.2016.
 */

public class ByteList {
    byte[] value;

    public ByteList(){
        super();
        value = new byte[1];
    }

    public void add(byte b){
        value = Arrays.copyOf(value,value.length+1);
        value[value.length - 1] = b;
    }

    public void add(byte[] b){
        if (b==null) return;
        if (b.length==0) return;
        final int v = value.length-1;
        value = Arrays.copyOf(value,v+1+b.length);
        System.arraycopy(b,0,value,v,b.length);
    }

    public byte[] toArray(){
        return value;
    }
}
