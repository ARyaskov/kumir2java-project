package generation;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.nio.ByteBuffer;

public class CG {

    public static final byte INVOKESPECIAL = (byte) 0xB7;
    public static final byte ALOAD = (byte) 0x19;
    public static final byte ALOAD0 = (byte) 0x2a;
    public static final byte ALOAD1 = (byte) 0x2b;
    public static final byte ALOAD2 = (byte) 0x2c;
    public static final byte ALOAD3 = (byte) 0x2d;
    public static final byte RETURN = (byte) 0xB1;

    public static void putShortIntoByteArray(short two_bytes, ArrayList<Byte> bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(two_bytes);
        buffer.flip();
        for (byte b : buffer.array()) {
            bytes.add(Byte.valueOf(b));
        }


    }
}
