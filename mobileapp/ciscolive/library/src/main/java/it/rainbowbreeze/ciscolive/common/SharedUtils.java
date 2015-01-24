package it.rainbowbreeze.ciscolive.common;

import java.nio.ByteBuffer;

/**
 * Created by alfredomorresi on 23/11/14.
 */
public class SharedUtils {
    public static byte[] longToBytes(long x) {
        byte[] payload = ByteBuffer.allocate(8).putLong(x).array();
        return payload;
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes);
        buffer.flip();  //need flip
        return buffer.getLong();
    }
}
