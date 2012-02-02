package generation;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.nio.ByteBuffer;

public class CG {

    public static final byte INVOKESPECIAL = (byte) 0xB7;
    public static final byte INVOKESTATIC = (byte) 0xB8;
    public static final byte ALOAD = (byte) 0x19;
    public static final byte ALOAD0 = (byte) 0x2a;
    public static final byte ALOAD1 = (byte) 0x2b;
    public static final byte ALOAD2 = (byte) 0x2c;
    public static final byte ALOAD3 = (byte) 0x2d;
    public static final byte RETURN = (byte) 0xB1;
    public static final byte BIPUSH = (byte) 0x10;
    public static final byte SIPUSH = (byte) 0x11;
    public static final byte ISTORE = (byte) 0x36;
    public static final byte ISTORE_0 = (byte) 0x3b;
    public static final byte ILOAD = (byte) 0x15;
    public static final byte LDC = (byte) 0x12;
    public static final byte LDC_W = (byte) 0x13;
    public static final byte IADD = (byte) 0x60;
    public static final byte ISUB = (byte) 0x64;
    public static final byte IMUL = (byte) 0x68;
    public static final byte IDIV = (byte) 0x6c;
    public static final byte I2D = (byte) 0x87;
    public static final byte DUP = (byte) 0x59;

    public static void putShortIntoByteArray(short two_bytes, ArrayList<Byte> bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(two_bytes);
        buffer.flip();
        for (byte b : buffer.array()) {
            bytes.add(Byte.valueOf(b));
        }


    }
}
