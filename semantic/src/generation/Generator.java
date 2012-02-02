package generation;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.nio.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import semantic.*;

public class Generator {

    private DataOutputStream m_output;
    private short OPERAND_STACK_SIZE = 2048;
    private ByteBuffer m_commands;
    private Stack m_idStack;

    public Generator(String filename) {
        try {
            m_output = new DataOutputStream(
                    new BufferedOutputStream(
                    new FileOutputStream(filename)));
            m_commands = ByteBuffer.allocate(100);
            m_idStack = new Stack();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void writeProlog() throws IOException {

        byte cafebabe[] = new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
        byte version[] = new byte[]{0x00, 0x00, 0x00, 0x33};
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

                String name = row.getValue().toString();
                // Integer _inte = Integer.valueOf(((String) row.getValue()).length());
                Integer _inte = Integer.valueOf(name.getBytes().length);
                // длина строки u2
                m_output.writeShort(_inte.shortValue());

                // Символы константы u1[]


                m_output.write(name.getBytes());


            } else if (type.equals("String")) {
                byte _type = 0x08;

                // тип константы u1
                m_output.writeByte(_type);


                Integer _inte = Integer.valueOf((String) row.getValue());
                // номер константы UTF-8 u2
                m_output.writeShort(_inte.shortValue());


            } else if (type.equals("NameAndType")) {
                byte _type = 0xC;

                // тип константы u1
                m_output.writeByte(_type);

                Integer _inte1 = Integer.valueOf(((String) row.getValue()).split(",")[0]);
                Integer _inte2 = Integer.valueOf(((String) row.getValue()).split(",")[1]);

                // номер константы UTF-8 с названием u2
                m_output.writeShort(_inte1.shortValue());
                // номер константы UTF-8 с дескриптором u2
                m_output.writeShort(_inte2.shortValue());


            } else if (type.equals("MethodRef")) {
                byte _type = 0xA;

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

        short flags = 0x0020;

        m_output.writeShort(flags);

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

    public void writeInitMethod() throws IOException {
        HashMap<String, ByteBuffer> buf = Semantic.bytecodeBuffer;
        ByteBuffer code = buf.get("<init>");

        // ACC_PUBLIC
        m_output.writeShort((short) 0x0001);
        // имя метода в u2 (ссылка на UTF-8)
        short nameID = (short) Semantic.constantsTable.getRowByName("<init>").getID();
        m_output.writeShort(nameID);
        // дескриптор
        short descrID = (short) Semantic.constantsTable.getRowByName("()V").getID();
        m_output.writeShort(descrID);
        // количество атрибутов в u2
        m_output.writeShort(Integer.valueOf(1).shortValue());
        // имя атрибута в u2
        short codeID = (short) Semantic.constantsTable.getRowByName("Code").getID();
        m_output.writeShort(codeID);
        // длина атрибута в u4
        int len = 12 + code.position();
        m_output.writeInt(len);
        // размер стека операндов в u2
        short stackSz = 1000;
        m_output.writeShort(stackSz);
        // количество переменных в u2
        short varCount = 1;
        m_output.writeShort(varCount);
        // длина байткода в u4
        int bcodeSize = code.position();
        m_output.writeInt(bcodeSize);


        // байткод
        byte barray[] = code.array();
        int breakpos = code.position();
        for (int i = 0; i < breakpos; i++) {
            m_output.writeByte(barray[i]);
        }

        // количество исключений в u2
        short exceptionCount = 0;
        m_output.writeShort(exceptionCount);

        // количество атрибутов в u2
        short attributesCount = 0;
        m_output.writeShort(attributesCount);
    }

    public void writeMain() throws IOException {
        // ACC_PUBLIC | ACC_STATIC
        short total = 0x0001 | 0x0008;

        ByteBuffer code = ByteBuffer.allocate(32);

        FPTableRow row = Semantic.fpTable.getRowByOrder(0);
        String name = row.getName();
        String descr = "(";
        for (String every : row.getParTypes()) {
            descr += every;
        }
        descr += ")";
        String retType = row.getReturnType();
        descr += retType;
        short id = findIdForMethodRef("MainClass", name, descr);


        code.put(CG.INVOKESTATIC);
        code.putShort(id);
        code.put(CG.RETURN);

        // флаги доступа
        m_output.writeShort(total);

        // имя метода
        short idName = (short) Semantic.constantsTable.getRowByName("main").getID();
        m_output.writeShort(idName);

        // дескриптор метода
        short idDescr = (short) Semantic.constantsTable.getRowByName("([Ljava/lang/String;)V").getID();
        m_output.writeShort(idDescr);

        // количество атрибутов
        m_output.writeShort((short) 1);

        short idCode = (short) Semantic.constantsTable.getRowByName("Code").getID();

        // id атрибута
        m_output.writeShort(idCode);

        // длина атрибута
        int len = 12 + code.position();
        m_output.writeInt(len);

        // размер стека операндов
        m_output.writeShort(OPERAND_STACK_SIZE);


        // количество локальных переменных
        m_output.writeShort((short) 1);

        // длина байт-кода
        m_output.writeInt(code.position());

        // байткод
        byte barray[] = code.array();
        int limit = code.position();
        for (int i = 0; i < limit; i++) {
            m_output.writeByte(barray[i]);
        }


        // Таблица исключений в u2
        m_output.writeShort((short) 0);
        // Таблица атрибутов в u2
        m_output.writeShort((short) 0);

    }

    public void writeMethods() throws IOException {
        short size = (short) Semantic.fpTable.getSize();
        // количество методов в u2 (методы + main(String[]) + <init>)
        m_output.writeShort((short) size + 2);
        // ACC_PUBLIC | ACC_STATIC
        short total = 0x0001 | 0x0008;

        writeInitMethod();


        Iterator it = Semantic.fpTable.getIterator();
        while (it.hasNext()) {
            FPTableRow row = (FPTableRow) it.next(); // флаги доступа в u2
            String nameOfMethod = row.getName();
            ByteBuffer code = Semantic.bytecodeBuffer.get(nameOfMethod);

            m_output.writeShort(total);

            ConstantsTableRow crow =
                    Semantic.constantsTable.getRowById(row.getConstTableID());
            String idName = ((String) crow.getValue()).split(",")[0];
            String idDescr =
                    ((String) crow.getValue()).split(",")[1];

            // имя метода в u2 (ссылка на UTF-8)
            m_output.writeShort(Integer.valueOf(idName).shortValue());
            //
            //дескриптор метода в u2 (ссылка на UTF - 8)

            m_output.writeShort(Integer.valueOf(idDescr).shortValue());

            // Количество атрибутов метода в u2 
            m_output.writeShort((short) 1);


            // Атрибут Code // Номер константы UTF-8 c Code в таблице констант
            short codeID = (short) Semantic.constantsTable.getRowByName("Code").getID();
            m_output.writeShort(codeID);

            // Длина атрибута без первых 6 байт на длину и имя 
            int len = 12 + code.position();
            m_output.writeInt(len);

            // Размер стека операндов для методов в u2
            m_output.writeShort(OPERAND_STACK_SIZE);

            short countLocals = (short) Semantic.localsTable.getLocalsFor(row.getID()).size();
            // Количество
            //локальных переменных в u2
            m_output.writeShort(countLocals);


            // * ЗДЕСЬ ДЛИНА БАЙТ-КОДА U4 И САМ БАЙТКОД // *

            int lenBytecode = code.position();
            m_output.writeInt(lenBytecode);

            // байткод
            byte barray[] = code.array();
            for (int i = 0; i < lenBytecode; i++) {
                m_output.writeByte(barray[i]);
            }


            // Таблица исключений в u2
            m_output.writeShort((short) 0);
            // Таблица атрибутов в u2
            m_output.writeShort((short) 0);

        }



        writeMain();



    }

    public void writeAttributes() throws IOException {

        // атрибуты в u2
        m_output.writeShort(Integer.valueOf(0).shortValue());

    }

    public void generateFunc(Vertex vx) {
        String nameOfFun = vx.getChildList().get(0).getChildList().get(0).getAttribute("NAME");
        if (Semantic.fpTable.getSize() > 1) {
        } else if (Semantic.fpTable.getSize() == 1) {
            nameOfFun = "main";



        } else if (Semantic.fpTable.getSize() == 0) {
        }

        m_commands.put(CG.RETURN);
        Semantic.bytecodeBuffer.put(nameOfFun, m_commands);

    }

    public void generatePrintExpr(Vertex vx) {
    }

    public void generateAssmntExpr(Vertex vx, String methodName) {
        Vertex right = vx.getChildByOrder(1);
        Vertex left = vx.getChildByOrder(0);

        m_commands = ByteBuffer.allocate(100);

        if (right.getAttribute("TYPE").equals("цел")) {
            int val = Integer.valueOf(right.getAttribute("NAME")).intValue();
            if (val >= -32768 && val < 32768) {
                m_commands.put(CG.SIPUSH);
                m_commands.putShort((short) val);
                m_commands.put(CG.ISTORE);
                String numOfLocal = left.getLastDescendant().getAttribute("LOCAL_" + methodName);
                m_commands.putShort(Integer.valueOf(numOfLocal).shortValue());

            } else {// ищем в таблице констант
                ConstantsTableRow row = Semantic.constantsTable.getRowByVal(val);
                int id = row.getID();
                m_commands.put(CG.LDC_W);
                m_commands.putShort((short) id);

                m_commands.put(CG.ISTORE);
                String numOfLocal = left.getLastDescendant().getAttribute("LOCAL_" + methodName);
                m_commands.putShort(Integer.valueOf(numOfLocal).shortValue());
            }

        }

    }

    public void generatePlusExpr(Vertex vx, String methodName) {
        /*
         * Vertex right = vx.getChildByOrder(1); Vertex left =
         * vx.getChildByOrder(0);
         *
         * if (left.getAttribute(methodName))
         */
    }

    public static short findIdForNameAndType(String name, String descr) {
        short result = -1;

        String id1 = String.valueOf(Semantic.constantsTable.getRowByName(name).getID());
        String id2 = String.valueOf(Semantic.constantsTable.getRowByName(descr).getID());

        result = (short) Semantic.constantsTable.getRowByName(id1 + "," + id2).getID();

        return result;
    }

    public static short findIdForMethodRef(String className, String name, String descr) {
        short result = -1;


        String id1 = String.valueOf(Semantic.constantsTable.getRowByName(name).getID());
        String id2 = String.valueOf(Semantic.constantsTable.getRowByName(descr).getID());

        String nameAndTypeID = String.valueOf(Semantic.constantsTable.getRowByName(id1 + "," + id2).getID());
        String classID = String.valueOf(Semantic.constantsTable.getRowByName(className).getID() + 1);


        Integer methodRefID = Integer.valueOf(
                Semantic.constantsTable.getRowByName(classID + "," + nameAndTypeID).getID());

        return methodRefID.shortValue();
    }

    public void giantSwitch() {

        m_commands = ByteBuffer.allocate(1000);
        String curMethod = null;
        boolean isPrepareForAssmnt = false;
        for (int m = 0; m < Semantic.g.getSize(); m++) {
            Vertex vx = (Vertex) Semantic.g.getVertexByOrder(m);
            String vxType = vx.getAttribute("NAME");
            switch (vxType) {
                case "create_ident": {
                    if (vx.getParentList().get(0).getAttribute("NAME").equals("create_proc")
                            || vx.getParentList().get(0).getAttribute("NAME").equals("create_func")) {
                        curMethod = vx.getChildList().get(0).getAttribute("NAME");
                    } else if (vx.getParentList().get(0).getAttribute("NAME").equals("create_enum_atomic_identifier_list")) {
                    } else {


                        String name =
                                vx.getChildByOrder(0).getAttribute("NAME");

                        int id =
                                Integer.valueOf(Semantic.localsTable.getRowByName(name).getID()).shortValue();
                        m_idStack.push(id);


                    }
                }
                break;
                case "+": {
                    for (int i = 0; i < 2; i++) {
                        Vertex tempVx = vx.getChildByOrder(i);
                        if (tempVx.getAttribute("NAME").equals("create_expr_id")) {
                            Vertex idVx = vx.getChildByOrder(i).getLastDescendant();
                            String name = idVx.getAttribute("NAME");
                            int idCT = Semantic.constantsTable.getRowByName(name).getID();
                            m_commands.put(CG.LDC_W);
                            m_commands.putShort((short) idCT);

                        } else if (vx.getChildByOrder(i).getAttribute("ID").equals("-1")) {
                            String val = vx.getChildByOrder(i).getAttribute("NAME");
                            int intVal = Integer.valueOf(val);
                            if (intVal >= -32768 && intVal < 32768) {
                                m_commands.put(CG.SIPUSH);
                                m_commands.putShort((short) intVal);
                            } else {// ищем в таблице констант
                                ConstantsTableRow row = Semantic.constantsTable.getRowByName(val);
                                int id = row.getID();
                                m_commands.put(CG.LDC_W);
                                m_commands.putShort((short) id);
                            }
                        }
                    }

                    m_commands.put(CG.IADD);
                }
                break;
                case "-": {
                    for (int i = 0; i < 2; i++) {
                        Vertex tempVx = vx.getChildByOrder(i);
                        if (tempVx.getAttribute("NAME").equals("create_expr_id")) {
                            Vertex idVx = vx.getChildByOrder(i).getLastDescendant();
                            String name = idVx.getAttribute("NAME");
                            int idCT = Semantic.constantsTable.getRowByName(name).getID();
                            m_commands.put(CG.LDC_W);
                            m_commands.putShort((short) idCT);

                        } else if (vx.getChildByOrder(i).getAttribute("ID").equals("-1")) {
                            String val = vx.getChildByOrder(i).getAttribute("NAME");
                            int intVal = Integer.valueOf(val);
                            if (intVal >= -32768 && intVal < 32768) {
                                m_commands.put(CG.SIPUSH);
                                m_commands.putShort((short) intVal);
                            } else {// ищем в таблице констант
                                ConstantsTableRow row = Semantic.constantsTable.getRowByName(val);
                                int id = row.getID();
                                m_commands.put(CG.LDC_W);
                                m_commands.putShort((short) id);
                            }
                        }
                    }

                    m_commands.put(CG.ISUB);
                }
                case "*": {
                    for (int i = 0; i < 2; i++) {
                        Vertex tempVx = vx.getChildByOrder(i);
                        if (tempVx.getAttribute("NAME").equals("create_expr_id")) {
                            Vertex idVx = vx.getChildByOrder(i).getLastDescendant();
                            String name = idVx.getAttribute("NAME");
                            int idCT = Semantic.constantsTable.getRowByName(name).getID();
                            m_commands.put(CG.LDC_W);
                            m_commands.putShort((short) idCT);

                        } else if (vx.getChildByOrder(i).getAttribute("ID").equals("-1")) {
                            String val = vx.getChildByOrder(i).getAttribute("NAME");
                            int intVal = Integer.valueOf(val);
                            if (intVal >= -32768 && intVal < 32768) {
                                m_commands.put(CG.SIPUSH);
                                m_commands.putShort((short) intVal);
                            } else {// ищем в таблице констант
                                ConstantsTableRow row = Semantic.constantsTable.getRowByName(val);
                                int id = row.getID();
                                m_commands.put(CG.LDC_W);
                                m_commands.putShort((short) id);
                            }
                        }
                    }

                    m_commands.put(CG.IMUL);
                }
                break;
                case "/": {
                    for (int i = 0; i < 2; i++) {
                        Vertex tempVx = vx.getChildByOrder(i);
                        if (tempVx.getAttribute("NAME").equals("create_expr_id")) {
                            Vertex idVx = vx.getChildByOrder(i).getLastDescendant();
                            String name = idVx.getAttribute("NAME");
                            int idCT = Semantic.constantsTable.getRowByName(name).getID();
                            m_commands.put(CG.LDC_W);
                            m_commands.putShort((short) idCT);

                        } else if (vx.getChildByOrder(i).getAttribute("ID").equals("-1")) {
                            String val = vx.getChildByOrder(i).getAttribute("NAME");
                            int intVal = Integer.valueOf(val);
                            if (intVal >= -32768 && intVal < 32768) {
                                m_commands.put(CG.SIPUSH);
                                m_commands.putShort((short) intVal);
                            } else {// ищем в таблице констант
                                ConstantsTableRow row = Semantic.constantsTable.getRowByName(val);
                                int id = row.getID();
                                m_commands.put(CG.LDC_W);
                                m_commands.putShort((short) id);
                            }
                        }
                    }

                    m_commands.put(CG.IDIV);
                }
                break;
                case "**": {
                    for (int i = 0; i < 2; i++) {
                        Vertex tempVx = vx.getChildByOrder(i);
                        if (tempVx.getAttribute("NAME").equals("create_expr_id")) {
                            Vertex idVx = vx.getChildByOrder(i).getLastDescendant();
                            String name = idVx.getAttribute("NAME");
                            int idCT = Semantic.constantsTable.getRowByName(name).getID();
                            m_commands.put(CG.LDC_W);
                            m_commands.putShort((short) idCT);

                        } else if (vx.getChildByOrder(i).getAttribute("ID").equals("-1")) {
                            String val = vx.getChildByOrder(i).getAttribute("NAME");
                            int intVal = Integer.valueOf(val);
                            if (intVal >= -32768 && intVal < 32768) {
                                m_commands.put(CG.SIPUSH);
                                m_commands.putShort((short) intVal);

                            } else {// ищем в таблице констант
                                ConstantsTableRow row = Semantic.constantsTable.getRowByName(val);
                                int id = row.getID();
                                m_commands.put(CG.LDC_W);
                                m_commands.putShort((short) id);

                            }
                        }
                    }
                    m_commands.put(CG.INVOKESTATIC);
                    int idMethodRef = findIdForMethodRef("RTL", "ku_pow", "(II)I");
                    m_commands.putShort((short) idMethodRef);

                }
                break;
                case ":=": {

                    if (vx.getParentList().get(0).getAttribute("NAME").equals(":=")) {
                        m_commands.put(CG.DUP);
                    }
                    m_commands.put(CG.ISTORE);
                    int id = (int) m_idStack.pop();
                    m_commands.put((byte) id);

                }
                break;

                case "create_expr_list_print": {
                    short idMethodRef = 0;
                    int childsCount = vx.getChildByOrder(0).getChildList().size();

                    for (int j = 0; j < childsCount; j++) {
                        Vertex vxNow = vx.getChildByOrder(0).getChildByOrder(j);

                        if (vxNow.getAttribute("NAME").equals("append_expr_to_list")) {
                            vxNow = vx.getChildByOrder(0).getChildByOrder(j).getChildByOrder(0);
                        }
                        int id = 0;
                        if (vxNow.getAttribute("NAME").equals("create_expr_id")) {
                            vxNow = vxNow.getChildByOrder(0).getChildByOrder(0);
                        }

                        String type = vxNow.getAttribute("TYPE");
                        String whatPrint = vxNow.getAttribute("NAME");
                        if (vxNow.getTypeOfSymbol().equals("ID")) {
                            id = Semantic.localsTable.getRowByName(whatPrint).getID();
                            m_commands.put(CG.ILOAD);
                            m_commands.put((byte) id);
                        }
                        if (type.equals("цел")) {
                            idMethodRef = findIdForMethodRef("RTL", "ku_print", "(I)V");
                        } else if (type.equals("лит") && whatPrint.equals("нс")) {
                            idMethodRef = findIdForMethodRef("RTL", "ku_println", "()V");
                        } else if (type.equals("лит")) {
                            short idWhatPrint = (short) Semantic.constantsTable.getRowByTypeAndName("String", whatPrint).getID();
                            m_commands.put(CG.LDC_W);
                            m_commands.putShort(idWhatPrint);
                            idMethodRef = findIdForMethodRef("RTL", "ku_print", "(Ljava/lang/String;)V");
                        }
                        m_commands.put(CG.INVOKESTATIC);
                        m_commands.putShort(idMethodRef);

                    }
                }
                break;
                case "create_proc": {
                    m_commands.put(CG.RETURN);
                    Semantic.bytecodeBuffer.put(curMethod, m_commands);
                }
                break;
                case "create_func": {
                    m_commands.put(CG.RETURN);
                    Semantic.bytecodeBuffer.put(curMethod, m_commands);
                }
                break;
                default: {

                    /*
                     * if (vx.containsAttribute("TYPE")) { if
                     * (vx.getAttribute("TYPE") != null &&
                     * vx.getAttribute("TYPE").equals("цел")) { String val =
                     * vx.getAttribute("NAME"); int intVal =
                     * Integer.valueOf(val); if (intVal >= -32768 && intVal <
                     * 32768) { m_commands.put(CG.SIPUSH);
                     * m_commands.putShort((short) intVal); } else {
                     * ConstantsTableRow row =
                     * Semantic.constantsTable.getRowByName(val); int id =
                     * row.getID(); m_commands.put(CG.LDC_W);
                     * m_commands.putShort((short) id); } } }
                     */
                }
            }
        }


    }

    public void close() throws IOException {

        m_output.close();

    }
}
