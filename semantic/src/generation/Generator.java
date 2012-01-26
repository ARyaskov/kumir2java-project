package generation;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import semantic.*;

public class Generator {

    private DataOutputStream m_output;
    private short OPERAND_STACK_SIZE = 2048;

    public Generator(String filename) {
        try {
            m_output = new DataOutputStream(
                    new BufferedOutputStream(
                    new FileOutputStream(filename)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void writeProlog() throws IOException {

        byte cafebabe[] = new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
        byte version[] = new byte[]{0x00, 0x00, 0x00, 0x33, 0x00, 0x14, 0x0a, 0x00};
        m_output.write(cafebabe);
        m_output.write(version);

    }

    public byte[] getBytes(Integer _inte) {
        byte result[] = null;
        if (_inte.intValue() < 256) {
            result = new byte[]{0x00, _inte.byteValue()};
        } else {
            result = _inte.toString().getBytes();
        }
        return result;

    }

    public void writeConstantsTable(ConstantsTable ct) throws IOException {


        // Пишем размер таблицы констант + 1
        Integer numToWrite = Integer.valueOf(ct.getCount() + 1);

        m_output.writeChar(numToWrite.intValue());

        Iterator it = ct.getIterator();
        while (it.hasNext()) {
            ConstantsTableRow row = (ConstantsTableRow) it.next();
            String type = row.getType();

            if (type.equals("UTF-8")) {
                byte _type = 0x01;

                // тип константы u1
                m_output.writeByte(_type);

                Integer _inte = Integer.valueOf(((String) row.getValue()).length());

                // длина строки u2
                m_output.writeShort(_inte.shortValue());

                // Символы константы u1[]
                m_output.writeBytes(row.getValue().toString());

            } else if (type.equals("String")) {
                byte _type = 0x08;

                // тип константы u1
                m_output.writeByte(_type);


                Integer _inte = Integer.valueOf((String) row.getValue());
                // номер константы UTF-8 u2
                m_output.writeShort(_inte.shortValue());


            } else if (type.equals("NameAndType")) {
                byte _type = 0x12;

                // тип константы u1
                m_output.writeByte(_type);

                Integer _inte1 = Integer.valueOf(((String) row.getValue()).split(",")[0]);
                Integer _inte2 = Integer.valueOf(((String) row.getValue()).split(",")[1]);

                // номер константы UTF-8 с названием u2
                m_output.writeShort(_inte1.shortValue());
                // номер константы UTF-8 с дескриптором u2
                m_output.writeShort(_inte2.shortValue());


            } else if (type.equals("MethodRef")) {
                byte _type = 0x10;

                // тип константы u1
                m_output.writeByte(_type);

                Integer _inte1 = Integer.valueOf(((String) row.getValue()).split(",")[0]);
                Integer _inte2 = Integer.valueOf(((String) row.getValue()).split(",")[1]);

                // номер константы UTF-8 с именем класса u2
                m_output.writeShort(_inte1.shortValue());
                // номер константы UTF-8 с NameAndType u2
                m_output.writeShort(_inte2.shortValue());


            } else if (type.equals("Class")) {
                byte _type = 0x07;

                // тип константы u1
                m_output.writeByte(_type);

                Integer _inte = Integer.valueOf(row.getValue().toString());

                // номер константы UTF-8 u2
                m_output.writeShort(_inte.shortValue());

            } else if (type.equals("INT")) {
                byte _type = 0x03;
                // тип константы u1
                m_output.writeByte(_type);

                Integer _inte = Integer.valueOf((String) row.getValue());

                // целое число со знаком s4
                m_output.writeInt(_inte.intValue());
            }
        }



    }

    public void writeAccessFlags() throws IOException {

        byte result[] = new byte[]{0x00, 0x02};

        m_output.write(result);

    }

    public void writeCurrentClass(int id) throws IOException {
        // текущий класс в u2
        m_output.writeShort(Integer.valueOf(id).shortValue());

    }

    public void writeParentClass(int parentID) throws IOException {
        // родительский класс в u2
        m_output.writeShort(Integer.valueOf(parentID).shortValue());

    }

    public void writeInterfaces() throws IOException {

        // интерфейсы в u2
        m_output.writeShort(Integer.valueOf(0).shortValue());

    }

    public void writeFields() throws IOException {

        // поля в u2
        m_output.writeShort(Integer.valueOf(0).shortValue());
    }

     public void writeInit() throws IOException {
        HashMap<String, ArrayList<Byte>> buf = Semantic.bytecodeBuffer;
        ArrayList<Byte> code = buf.get("<init>");
        
        // ACC_PUBLIC
        m_output.writeShort((short)0x0001);
        // имя метода в u2 (ссылка на UTF-8)
        m_output.writeShort(Integer.valueOf(idName).shortValue());
     }
    
    public void writeMethods() throws IOException {
        short size = (short) Semantic.fpTable.getSize();
        // количество методов в u2
        m_output.writeShort(size);
        // ACC_PUBLIC | ACC_STATIC
        short total = 0x0001 | 0x0008;
        
        writeInit();
        Iterator it = Semantic.fpTable.getIterator();
        while (it.hasNext()) {
            FPTableRow row = (FPTableRow) it.next();
// флаги доступа в u2
            m_output.writeShort(total);

            ConstantsTableRow crow = Semantic.constantsTable.getRowById(row.getConstTableID());
            String idName = ((String) crow.getValue()).split(",")[0];
            String idDescr = ((String) crow.getValue()).split(",")[1];

            // имя метода в u2 (ссылка на UTF-8)
            m_output.writeShort(Integer.valueOf(idName).shortValue());
            // дескриптор метода в u2 (ссылка на UTF-8)
            m_output.writeShort(Integer.valueOf(idDescr).shortValue());

            // Количество атрибутов метода в u2
            m_output.writeShort((short) 1);


            // Атрибут Code
            // Номер константы UTF-8 c Code в таблице констант
            m_output.writeShort((short) 1);

            // Длина атрибута без первых 6 байт на длину и имя
            int len = 0;
            m_output.writeInt(len);

            // Размер стека операндов для методов в u2
            m_output.writeShort(OPERAND_STACK_SIZE);

            short countLocals = (short) Semantic.localsTable.getLocalsFor(row.getID()).size();
            // Количество локальных переменных в u2
            m_output.writeShort(countLocals);


            int lenBytecode = 0;
            m_output.writeInt(lenBytecode);
            /*
             * ЗДЕСЬ ДЛИНА БАЙТ-КОДА U4 И САМ БАЙТКОД
             * Semantic.bytecodeBuffer.get(methodName)
             */
            
            

            // Таблица исключений в u2
            m_output.writeShort((short) 0);

            // Таблица атрибутов в u2
            m_output.writeShort((short) 0);

        }







    }

    public void writeAttributes() throws IOException {

        // атрибуты в u2
        m_output.writeShort(Integer.valueOf(0).shortValue());

    }

    public void close() throws IOException {

        m_output.close();

    }
}
