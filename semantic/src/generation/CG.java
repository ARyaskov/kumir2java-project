package generation;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.nio.ByteBuffer;

public class CG {

    // Сложение
    public static final byte IADD = (byte) 0x60;
    public static final byte DADD = (byte) 0x63;
    // Вычитание
    public static final byte ISUB = (byte) 0x64;
    public static final byte DSUB = (byte) 0x67;
    // Умножение
    public static final byte IMUL = (byte) 0x68;
    public static final byte DMUL = (byte) 0x6b;
    // Деление
    public static final byte IDIV = (byte) 0x6c;
    public static final byte DDIV = (byte) 0x6f;
    // Нумерованные STORE
    public static final byte ASTORE_0 = (byte) 0x4b;
    public static final byte ASTORE_1 = (byte) 0x4c;
    public static final byte ASTORE_2 = (byte) 0x4d;
    public static final byte ASTORE_3 = (byte) 0x4e;
    public static final byte ISTORE_0 = (byte) 0x3b;
    public static final byte ISTORE_1 = (byte) 0x3c;
    public static final byte ISTORE_2 = (byte) 0x3d;
    public static final byte ISTORE_3 = (byte) 0x3e;
    public static final byte DSTORE_0 = (byte) 0x47;
    public static final byte DSTORE_1 = (byte) 0x48;
    public static final byte DSTORE_2 = (byte) 0x49;
    public static final byte DSTORE_3 = (byte) 0x4a;
    // Нумерованные LOAD
    public static final byte ILOAD_0 = (byte) 0x1a;
    public static final byte ILOAD_1 = (byte) 0x1b;
    public static final byte ILOAD_2 = (byte) 0x1c;
    public static final byte ILOAD_3 = (byte) 0x1d;
    public static final byte ALOAD_0 = (byte) 0x2a;
    public static final byte ALOAD_1 = (byte) 0x2b;
    public static final byte ALOAD_2 = (byte) 0x2c;
    public static final byte ALOAD_3 = (byte) 0x2d;
    public static final byte DLOAD_0 = (byte) 0x26;
    public static final byte DLOAD_1 = (byte) 0x27;
    public static final byte DLOAD_2 = (byte) 0x28;
    public static final byte DLOAD_3 = (byte) 0x29;
    // Операции с локальными переменными
    public static final byte ISTORE = (byte) 0x36;
    public static final byte ASTORE = (byte) 0x3a;
    public static final byte DSTORE = (byte) 0x39;
    public static final byte ALOAD = (byte) 0x19;
    public static final byte ILOAD = (byte) 0x15;
    public static final byte DLOAD = (byte) 0x18;
    // Вызов метода
    public static final byte INVOKESPECIAL = (byte) 0xB7;
    public static final byte INVOKESTATIC = (byte) 0xB8;
    // Действия с константами
    public static final byte ICONST_m1 = (byte) 0x02;
    public static final byte ICONST_0 = (byte) 0x03;
    public static final byte ICONST_1 = (byte) 0x04;
     public static final byte ICONST_2 = (byte) 0x05;
    public static final byte BIPUSH = (byte) 0x10;
    public static final byte SIPUSH = (byte) 0x11;
    public static final byte LDC = (byte) 0x12;
    public static final byte LDC_W = (byte) 0x13;
    // Массивы
    public static final byte IALOAD = (byte) 0x2e;
    public static final byte AALOAD = (byte) 0x32;
    public static final byte BALOAD = (byte) 0x33;
    public static final byte CALOAD = (byte) 0x34;
    public static final byte DALOAD = (byte) 0x31;
    public static final byte IASTORE = (byte) 0x4f;
    public static final byte AASTORE = (byte) 0x53;
    public static final byte DASTORE = (byte) 0x52;
    public static final byte CASTORE = (byte) 0x55;
    public static final byte BASTORE = (byte) 0x54;
    public static final byte MULTIANEWARRAY = (byte) 0xc5;
    public static final byte ANEWARRAY = (byte) 0xbd;
    public static final byte NEWARRAY = (byte) 0xbc;
    // Преобразования типов
    public static final byte I2B = (byte) 0x91;
    public static final byte I2C = (byte) 0x92;
    public static final byte I2D = (byte) 0x87;
    public static final byte D2I = (byte) 0x8e;
    // Остальное
    public static final byte ARETURN = (byte) 0xB0;
    public static final byte DRETURN = (byte) 0xAF;
    public static final byte IRETURN = (byte) 0xAC;
    public static final byte RETURN = (byte) 0xB1;
    public static final byte DUP = (byte) 0x59;
    public static final byte INEG = (byte) 0x74;
    public static final byte DNEG = (byte) 0x77;

    public static void putShortIntoByteArray(short two_bytes, ArrayList<Byte> bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(two_bytes);
        buffer.flip();
        for (byte b : buffer.array()) {
            bytes.add(Byte.valueOf(b));
        }


    }
}
