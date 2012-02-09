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

    String curMethod = "";
    private DataOutputStream m_output;
    private short OPERAND_STACK_SIZE = 2048;
    private ByteBuffer m_commands;
    private Stack m_idStack;
    private Stack m_typeStack;
    private boolean reverseFlag = true;
    private Stack m_arraySizesStack;
    private String m_currentDeclType;
    private Stack m_idDeclStack;
    private byte T_BOOLEAN = 4;
    private byte T_CHAR = 5;
    private byte T_DOUBLE = 7;
    private byte T_BYTE = 8;
    private byte T_INT = 10;
    private Stack m_callingFunctionStack;
    private String curZnachType;

    public Generator(String filename) {
        try {
            m_output = new DataOutputStream(
                    new BufferedOutputStream(
                    new FileOutputStream(filename)));
            m_commands = ByteBuffer.allocate(2048);
            m_idStack = new Stack();
            m_typeStack = new Stack();
            m_arraySizesStack = new Stack();
            m_idDeclStack = new Stack();
            m_callingFunctionStack = new Stack();
            curZnachType = "";
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

    public void pushIntOnStack(String intInString) {
        int casted = Integer.valueOf(intInString).intValue();

        if (casted >= -32768 && casted <= 32767) {
            if (casted >= -128 && casted <= 127) {
                m_commands.put(CG.BIPUSH);
                m_commands.put((byte) casted);
            } else {
                m_commands.put(CG.SIPUSH);
                m_commands.putShort((short) casted);
            }
        } else {
            int _id = Semantic.constantsTable.getRowByTypeAndName("INT", intInString).getID();
            m_commands.put(CG.LDC_W);
            m_commands.putShort((short) _id);
        }

    }

    public void pushIntVarOnStack(String name) {

        int funId = Semantic.fpTable.getRowByName(curMethod).getID();
        int id = Semantic.localsTable.getRowByFunIDAndName(funId, name).getID();

        m_commands.put(CG.LDC_W);
        m_commands.putShort((short) id);



    }

    public void pushStringOnStack(String name) {

        int id = Semantic.constantsTable.getRowByTypeAndName("String", name).getID();

        m_commands.put(CG.LDC_W);
        m_commands.putShort((short) id);


    }

    public void pushCharOnStack(String name) {


        char code = name.charAt(0);

        m_commands.put(CG.SIPUSH);
        m_commands.putShort((short) code);
        //  m_commands.put(CG.I2C);
    }

    public void loadAFromLocalVar(byte id) {
        if (id == 0) {
            m_commands.put(CG.ALOAD_0);
        } else if (id == 1) {
            m_commands.put(CG.ALOAD_1);
        } else if (id == 2) {
            m_commands.put(CG.ALOAD_2);
        } else if (id == 3) {
            m_commands.put(CG.ALOAD_3);
        } else {
            m_commands.put(CG.ALOAD);
            m_commands.put(id);
        }
    }

    public String translateType(String inType) {
        String result = inType;

        if (inType.equals("I")) {
            result = "INT";
        } else if (inType.equals("D")) {
            result = "DOUBLE";
        } else if (inType.equals("C")) {
            result = "CHAR";
        } else if (inType.equals("Z")) {
            result = "BOOLEAN";
        } else if (inType.equals("Ljava/lang/String;")) {
            result = "String";
        } else {

            switch (inType) {
                case "лит":
                    result = "String";
                    break;
                case "цел":
                    result = "INT";
                    break;
                case "вещ":
                    result = "DOUBLE";
                    break;
                case "лог":
                    result = "BOOLEAN";
                    break;
                case "сим":
                    result = "CHAR";
                    break;
                default:
                    if (inType.equals("String") != true
                            && inType.equals("INT") != true
                            && inType.equals("DOUBLE") != true
                            && inType.equals("BOOLEAN") != true
                            && inType.equals("CHAR") != true) {
                        result = "unknown type";
                    }

            }
        }
        return result;
    }

    public Stack reverseStack(Stack stck) {
        Stack result = new Stack();
        while (stck.empty()) {
            result.push(stck.pop());
        }
        return result;


    }

    public void recursive(Vertex vx) {

        if (vx == null) {
            return;
        }
        /*
         * if (vx.getAttribute("NAME").equals("create_expr_list") &&
         * vx.getParentList().get(0).getAttribute("NAME").equals("create_expr_list_print"))
         * { for (int i = vx.getChildList().size()-1; i >= 0; i--) { Vertex
         * vxNow = vx.getChildByOrder(i); recursive(vxNow); } } else {
         */
        for (int i = 0; i < vx.getChildList().size(); i++) {
            Vertex vxNow = vx.getChildByOrder(i);
            recursive(vxNow);
        }
        /*
         * }
         */
        giantSwitch(vx);

    }
    int _glCount = 0;

    public void specialForPrintExprList() {
        short idMethodRef = 0;
        /*
         * System.out.printf("\n%d) %s\n", _glCount++, m_typeStack.toString());
         */
        String type = m_typeStack.pop().toString();

        if (type.equals("INT")) {
            idMethodRef = findIdForMethodRef("RTL", "ku_print", "(I)V");
        } else if (type.equals("CHAR")) {
            idMethodRef = findIdForMethodRef("RTL", "ku_print", "(C)V");
        } else if (type.equals("BOOLEAN")) {
            idMethodRef = findIdForMethodRef("RTL", "ku_print", "(Z)V");
        } else if (type.equals("String")) {
            idMethodRef = findIdForMethodRef("RTL", "ku_print", "(Ljava/lang/String;)V");
        } else if (type.equals("DOUBLE")) {
            idMethodRef = findIdForMethodRef("RTL", "ku_print", "(D)V");
        } else if (type.equals("NS")) {
            idMethodRef = findIdForMethodRef("RTL", "ku_println", "()V");
        }
        m_commands.put(CG.INVOKESTATIC);
        m_commands.putShort(idMethodRef);
    }

    public void loadValueFromLocalVar(byte id) {
        String type = m_typeStack.pop().toString();
        type = translateType(type);

        switch (type) {
            case "INT": {
                switch (id) {
                    case 0: {
                        m_commands.put(CG.ILOAD_0);
                    }
                    break;
                    case 1: {
                        m_commands.put(CG.ILOAD_1);
                    }
                    break;
                    case 2: {
                        m_commands.put(CG.ILOAD_2);
                    }
                    break;
                    case 3: {
                        m_commands.put(CG.ILOAD_3);
                    }
                    break;
                    default: {
                        m_commands.put(CG.ILOAD);
                        m_commands.put(id);
                    }

                }

            }
            break;

            case "String": {
                switch (id) {
                    case 0: {
                        m_commands.put(CG.ALOAD_0);
                    }
                    break;
                    case 1: {
                        m_commands.put(CG.ALOAD_1);
                    }
                    break;
                    case 2: {
                        m_commands.put(CG.ALOAD_2);
                    }
                    break;
                    case 3: {
                        m_commands.put(CG.ALOAD_3);
                    }
                    break;
                    default: {
                        m_commands.put(CG.ALOAD);
                        m_commands.put(id);
                    }

                }

            }
            break;
            case "DOUBLE": {
                switch (id) {
                    case 0: {
                        m_commands.put(CG.DLOAD_0);
                    }
                    break;
                    case 1: {
                        m_commands.put(CG.DLOAD_1);
                    }
                    break;
                    case 2: {
                        m_commands.put(CG.DLOAD_2);
                    }
                    break;
                    case 3: {
                        m_commands.put(CG.DLOAD_3);
                    }
                    break;
                    default: {
                        m_commands.put(CG.DLOAD);
                        m_commands.put(id);
                    }

                }

            }
            break;
            case "CHAR": {
                switch (id) {
                    case 0: {
                        m_commands.put(CG.ILOAD_0);
                        //  m_commands.put(CG.I2C);
                    }
                    break;
                    case 1: {
                        m_commands.put(CG.ILOAD_1);
                        //  m_commands.put(CG.I2C);
                    }
                    break;
                    case 2: {
                        m_commands.put(CG.ILOAD_2);
                        // m_commands.put(CG.I2C);
                    }
                    break;
                    case 3: {
                        m_commands.put(CG.ILOAD_3);
                        // m_commands.put(CG.I2C);

                    }
                    break;
                    default: {
                        m_commands.put(CG.ILOAD);
                        m_commands.put(id);
                        //  m_commands.put(CG.I2C);
                    }

                }

            }
            break;
        }
    }

    public boolean isArrayType(String type) {
        boolean result = false;
        if (type != null) {
            if (type.indexOf("целтаб") != -1) {
                result = true;
            } else if (type.indexOf("вещтаб") != -1) {
                result = true;
            } else if (type.indexOf("логтаб") != -1) {
                result = true;
            } else if (type.indexOf("литтаб") != -1) {
                result = true;
            } else if (type.indexOf("симтаб") != -1) {
                result = true;
            }
        }


        return result;
    }

    public void generateArray() {
    }

    public void giantSwitch(Vertex vx) {


        boolean isPrepareForAssmnt = false;


        String vxName = vx.getAttribute("NAME");
        String vxType = vx.getAttribute("TYPE");
        String vxTypeOfSymbol = vx.getTypeOfSymbol();
    
        System.out.printf("%s - %s\n", vxName, m_typeStack.toString());
        switch (vxName) {
            default: {
                if (vxName.equals("процедура")) {
                    byte i = 1;
                }
                if (vxTypeOfSymbol.equals("TYPE")) {
                    break;
                }
                if (vxType != null && vxTypeOfSymbol.equals("SIMPLE") && vxType.equals("цел")) {
                    if (Semantic.isNumeric(vxName)) {

                        String _parName = vx.getParentList().get(vx.enterNum).getAttribute("NAME");
                        if (_parName.equals("create_int_int_dim") != true && _parName.equals("append_int_int_dim") != true) {

                            pushIntOnStack(vxName);
                            m_typeStack.add("INT");


                        } else {
                            int val = Integer.valueOf(vxName).intValue();
                            if (val != 1) {
                                m_arraySizesStack.push(val);
                            }
                        }
                    }

                    vx.enterNum++;
                } else if (isArrayType(vxType) && vxTypeOfSymbol.equals("SIMPLE")) {
                } else if (vxType != null && vxTypeOfSymbol.equals("SIMPLE") && vxType.equals("лит")) {


                    if (vxName.equals("нс")) {
                        m_typeStack.add("NS");
                    } else {
                        pushStringOnStack(vxName);
                        m_typeStack.add("String");

                    }
                    String nameOf2Par = vx.generateNLevelParent(2).getAttribute("NAME");
                    if (nameOf2Par.equals("create_expr_list_print") || nameOf2Par.equals("create_stmt_print")) {
                        specialForPrintExprList();
                    }

                } else if (vxType != null && vxTypeOfSymbol.equals("SIMPLE") && vxType.equals("сим")) {



                    pushCharOnStack(vxName);
                    m_typeStack.add("CHAR");


                    String nameOf2Par = vx.generateNLevelParent(2).getAttribute("NAME");
                    if (nameOf2Par.equals("create_expr_list_print") || nameOf2Par.equals("create_stmt_print")) {
                        specialForPrintExprList();
                    }

                } else if (vxType != null && vxTypeOfSymbol.equals("ID")) {
                    m_idStack.push(vxName);
                    m_typeStack.push(vxType);
                } else if (vxTypeOfSymbol != null && vxTypeOfSymbol.equals("ID") && curMethod.isEmpty()) {
                    m_idStack.push(vxName);
                } else if (vxTypeOfSymbol != null && vxType != null && vxTypeOfSymbol.equals("ID") && vxType.equals("цел")) {
                    m_idStack.push(vxName);
                    m_typeStack.add("INT");
                } else if (vxTypeOfSymbol != null && vxType != null && vxTypeOfSymbol.equals("ID") && vxType.equals("лит")) {
                    m_idStack.push(vxName);
                    m_typeStack.add("String");
                } else if (vxTypeOfSymbol != null && vxType != null && vxTypeOfSymbol.equals("ID") && vxType.equals("сим")) {
                    m_idStack.push(vxName);
                    m_typeStack.add("CHAR");
                } else if (vxTypeOfSymbol.equals("ID")) {
                    m_idStack.push(vxName);
                    Vertex _par = vx.getParentList().get(vx.enterNum);
                    String _parName = _par.getParentList().get(0).getAttribute("NAME");
                    if (curMethod.isEmpty() != true) {
                        int funID = Semantic.fpTable.getRowByName(curMethod).getID();
                        if (Semantic.fpTable.isIdentifierExists(vxName) != true) {
                            String _type = Semantic.localsTable.getRowByFunIDAndName(funID, vxName).getType().toString();

                            _type = translateType(_type);
                            m_typeStack.push(_type);
                        }
                    }

                    vx.enterNum++;
                }


            }
            break;
            case "create_znachvalue": {
                curZnachType = m_typeStack.pop().toString();

            }
            break;
            case "create_stmt_znach": {
            }
            break;
            case "create_expr_list_print": {
                /*
                 * if (m_typeStack.empty() != true &&
                 * m_typeStack.peek().toString().equals("NS")) {
                 */
                // specialForPrintExprList();
                /*
                 * }
                 */
            }
            break;
            case "create_int_int_dim": {
            }
            break;
            case "create_stmt_decl": {
            }
            break;
            case "append_stmt_to_list": {
            }
            break;
            case "create_from_atomic_decl": {
            }
            break;
            case "create_atomic_type": {
            }
            break;
            case "create_enum_atomic_identifier_list": {
                m_idDeclStack.clear();
                m_typeStack.clear();
            }
            break;
            case "append_enum_atomic_identifier_list": {
            }
            break;
            case "create_expr_list": {
                String nameOf1Par = vx.getParentList().get(vx.enterNum).getAttribute("NAME");
                if (nameOf1Par.equals("[]:=")) {

                    /*
                     * m_commands.put(CG.ICONST_1); m_commands.put(CG.ISUB);
                     */
                    m_commands.put(CG.ICONST_1);
                    m_commands.put(CG.ISUB);
                    if (Semantic.getByVertexName(vx, "append_expr_to_list") != null) {
                        m_commands.put(CG.AALOAD);
                    } //else {
                    //    m_commands.put(CG.IASTORE);
                    //}
                    //

                } else if (nameOf1Par.equals("create_array_expr")) {
                    m_commands.put(CG.ICONST_1);
                    m_commands.put(CG.ISUB);
                } else if (nameOf1Par.equals("create_expr_list_print") && m_typeStack.empty() != true) {
                    specialForPrintExprList();
                } else if (nameOf1Par.equals("create_function_call")) {
                    // loadVarFromLocalVar();
                }


            }
            break;
            case "append_expr_to_list": {
                String nameOf2Par = vx.getParentList().get(0).getParentList().get(0).getAttribute("NAME");
                if (nameOf2Par.equals("create_expr_list_print") && m_typeStack.empty() != true) {
                    specialForPrintExprList();
                    m_typeStack.pop();
                }
                if (nameOf2Par.equals("create_function_call") && m_typeStack.empty() != true) {
                }
                if (nameOf2Par.equals("[]:=")) {
                    m_commands.put(CG.ICONST_1);
                    m_commands.put(CG.ISUB);
                }
            }
            break;
            case "create_stmt_print": {
                // specialForPrintExprList();
            }
            break;
            case "create_array_expr": {
                m_commands.put(CG.IALOAD);
                String nameOf2Par = vx.getParentList().get(0).getParentList().get(0).getAttribute("NAME");
                String nameOf3Par = vx.generateNLevelParent(2).getAttribute("NAME");
                if (nameOf2Par.equals("create_expr_list_print") || nameOf3Par.equals("create_expr_list_print")) {
                    specialForPrintExprList();
                    m_typeStack.pop();
                }
            }
            break;
            case "create_proc": {
                m_commands.put(CG.RETURN);
                Semantic.bytecodeBuffer.put(curMethod, m_commands);

                curMethod = "";
                m_commands = ByteBuffer.allocate(2048);


            }
            break;
            case "create_func": {
                if (curZnachType.equals("INT")) {
                    m_commands.put(CG.IRETURN);
                } else if (curZnachType.equals("DOUBLE")) {
                    m_commands.put(CG.DRETURN);
                } else if (curZnachType.equals("CHAR")) {
                    m_commands.put(CG.IRETURN);
                } else if (curZnachType.equals("BOOLEAN")) {
                    m_commands.put(CG.IRETURN);
                } else if (curZnachType.equals("String")) {
                    m_commands.put(CG.ARETURN);
                }

                Semantic.bytecodeBuffer.put(curMethod, m_commands);
                m_commands = ByteBuffer.allocate(2048);
                curZnachType = "";
            }
            break;
            case "create_function_call": {
                String _name = m_callingFunctionStack.pop().toString();
                FPTableRow fprow = Semantic.fpTable.getRowByName(_name);

                if (fprow != null) {// генерируем вызов процедуры
                    int idMethodRef = Semantic.constantsTable.getRowByTypeAndName("MethodRef", _name).getID();
                    m_commands.put(CG.INVOKESTATIC);
                    m_commands.putShort((short) idMethodRef);
                }
                for (int g = 0; g < fprow.getParCount(); g++) {
                    m_typeStack.pop();
                }
                if (fprow.getReturnType().isEmpty() != true) {
                    String _type = translateType(fprow.getReturnType());
                    m_typeStack.push(_type);
                }

            }
            break;
            case "create_ident": {
                boolean lock = false;
                String _par = vx.getParentList().get(vx.enterNum).getAttribute("NAME");
                if (_par.equals("create_proc")) {
                    int id = Semantic.constantsTable.getRowByName(m_idStack.pop().toString()).getID();
                    curMethod = Semantic.constantsTable.getRowById(id).getStringValue();
                    vx.enterNum++;
                } else if (_par.equals("create_func")) {
                    int id = Semantic.constantsTable.getRowByName(m_idStack.pop().toString()).getID();
                    curMethod = Semantic.constantsTable.getRowById(id).getStringValue();
                    vx.enterNum++;
                } else if (_par.equals("create_enum_array_identifier_list")) {
                    int funID = Semantic.fpTable.getRowByName(curMethod).getID();
                    String _name = m_idStack.pop().toString();
                    String _val = Semantic.localsTable.getRowByFunIDAndName(funID, _name).getValue().toString();
                    m_idDeclStack.push(_val);
                    vx.enterNum++;

                } else if (_par.equals("create_enum_atomic_identifier_list")) {
                    int funID = Semantic.fpTable.getRowByName(curMethod).getID();
                    String _name = m_idStack.pop().toString();
                    String _val = Semantic.localsTable.getRowByFunIDAndName(funID, _name).getValue().toString();
                    m_idDeclStack.push(_val);
                    vx.enterNum++;

                } else if (_par.equals("[]:=")) {
                    int funID = Semantic.fpTable.getRowByName(curMethod).getID();
                    String _name = m_idStack.pop().toString();
                    //  String _val = Semantic.localsTable.getRowByFunIDAndName(funID, _name).getValue().toString();
                    // m_id6Stack.push(_val);

                    int id = Integer.valueOf(Semantic.localsTable.getRowByFunIDAndName(funID, _name).getID()).intValue();
                    loadAFromLocalVar((byte) id);
                    vx.enterNum++;

                } else if (_par.equals("create_array_expr")) {
                    int funID = Semantic.fpTable.getRowByName(curMethod).getID();
                    String _name = m_idStack.pop().toString();
                    int id = Integer.valueOf(Semantic.localsTable.getRowByFunIDAndName(funID, _name).getID()).intValue();
                    loadAFromLocalVar((byte) id);
                    vx.enterNum++;
                } else if (_par.equals("create_expr_id") && vx.getParentList().get(vx.enterNum).getParentList().get(0).getAttribute("NAME").equals(":=") != true) {
                    // здесь генерация кода для вызова процедуры или значения rvalue-переменной
                    String _name = m_idStack.pop().toString();
                    FPTableRow fprow = Semantic.fpTable.getRowByName(_name);
                    String _type = "";
                    if (m_typeStack.empty() != true) {
                        _type = m_typeStack.peek().toString();
                    }

                    if (fprow != null) {// генерируем вызов процедуры/функции
                        int idMethodRef = Semantic.constantsTable.getRowByTypeAndName("MethodRef", _name).getID();
                        m_commands.put(CG.INVOKESTATIC);
                        m_commands.putShort((short) idMethodRef);
                        String ret = Semantic.fpTable.getRowByName(_name).getReturnType();
                        ret = translateType(ret);
                        m_typeStack.push(ret);
                        lock = true;
                    } else {// генерируем взятие значения переменной
                        int funID = Semantic.fpTable.getRowByName(curMethod).getID();
                        int id = Integer.valueOf(Semantic.localsTable.getRowByFunIDAndName(funID, _name).getID()).intValue();
                        loadValueFromLocalVar((byte) id);

                    }

                    if (lock == false && _type.isEmpty() != true) {
                        m_typeStack.push(_type);
                    }
                    lock = false;

                    vx.enterNum++;
                } else if (_par.equals("create_function_call")) {
                    // здесь генерация кода для вызова процедуры с параметрами

                    String _name = m_idStack.pop().toString();
                    m_callingFunctionStack.push(_name);

                    vx.enterNum++;
                }


            }
            break;
            case "create_enum_array_identifier_list": {

                while (!m_idDeclStack.empty()) {
                    String id = m_idDeclStack.pop().toString();
                    int funID = Semantic.fpTable.getRowByName(curMethod).getID();
                    LocalsTableRow row = Semantic.localsTable.getRowByFunIDAndName(funID, id);
                    int idLocVar = row.getID();
                    String _type = row.getType();
                    String[] arrDims = _type.split("\\[");
                    if (arrDims.length == 2) {
                        // Одномерный массив
                        byte _typecode = -1;
                        switch (arrDims[0]) {
                            case "целтаб": {
                                _typecode = T_INT;
                            }
                            break;
                            case "вещтаб": {
                                _typecode = T_DOUBLE;
                            }
                            break;
                            case "логтаб": {
                                _typecode = T_BOOLEAN;
                            }
                            break;
                            case "симтаб": {
                                _typecode = T_CHAR;
                            }
                            break;
                            case "литтаб": {
                                _typecode = 100;
                            }
                            break;
                        }
                        String size = String.valueOf(arrDims[1]);
                        pushIntOnStack(size);
                        if (_typecode == 100) {
                            m_commands.put(CG.ANEWARRAY);
                            short classID = (short) Semantic.constantsTable.getRowByTypeAndName("Class", "java/lang/String").getID();
                            m_commands.putShort(classID);

                        } else {

                            m_commands.put(CG.NEWARRAY);

                            if (_typecode == T_INT) {
                                m_commands.put(T_INT);
                                putAIntoLocalVar((byte) idLocVar);
                            } else if (_typecode == T_DOUBLE) {
                                m_commands.put(T_DOUBLE);
                                putAIntoLocalVar((byte) idLocVar);
                            } else if (_typecode == T_BOOLEAN) {
                                m_commands.put(T_BOOLEAN);
                                putAIntoLocalVar((byte) idLocVar);
                            } else if (_typecode == T_CHAR) {
                                m_commands.put(T_CHAR);
                                putAIntoLocalVar((byte) idLocVar);
                            } else if (_typecode == 100) {
                                putAIntoLocalVar((byte) idLocVar);
                            }
                        }
                    } else {// многомерный массив
                        int dims = arrDims.length - 1;
                        String searchType = "";
                        int classID = -1;
                        for (int y = 1; y <= dims; y++) {
                            String size = String.valueOf(arrDims[y]);
                            pushIntOnStack(size);
                            searchType += "[";
                        }
                        m_commands.put(CG.MULTIANEWARRAY);

                        switch (arrDims[0]) {
                            case "целтаб": {
                                searchType += "I";
                                classID = Semantic.constantsTable.getRowByTypeAndName("Class", searchType).getID();
                            }
                            break;
                            case "вещтаб": {
                                searchType += "D";
                                classID = Semantic.constantsTable.getRowByTypeAndName("Class", searchType).getID();
                            }
                            break;
                            case "логтаб": {
                                searchType += "Z";
                                classID = Semantic.constantsTable.getRowByTypeAndName("Class", searchType).getID();
                            }
                            break;
                            case "симтаб": {
                                searchType += "C";
                                classID = Semantic.constantsTable.getRowByTypeAndName("Class", searchType).getID();
                            }
                            break;
                            case "литтаб": {
                                searchType += "Ljava/lang/String;";
                                classID = Semantic.constantsTable.getRowByTypeAndName("Class", searchType).getID();
                            }
                            break;
                        }
                        m_commands.putShort((short) classID);
                        m_commands.put((byte) dims);
                        putAIntoLocalVar((byte) idLocVar);

                    }

                }
            }
            break;
            case "create_stmt_list": {
                m_idDeclStack.clear();
                m_typeStack.clear();
                m_idStack.clear();
            }
            break;
            case "append_to_stmt_list": {
                m_idDeclStack.clear();
                m_typeStack.clear();
                m_idStack.clear();
            }
            break;
            case "create_expr_id": {
                String _par = vx.generateNLevelParent(1).getAttribute("NAME");
                if (_par.equals("create_expr_list_print")) {
                    specialForPrintExprList();
                }
            }
            break;
            case "create_param_list": {
                m_idStack.clear();
                m_typeStack.clear();
            }
            break;

            case "+": {
                if (m_typeStack.get(m_typeStack.size() - 2).toString().equals("INT")
                        && m_typeStack.get(m_typeStack.size() - 1).toString().equals("INT")) {
                    m_commands.put(CG.IADD);
                    m_typeStack.pop();
                } else if (m_typeStack.get(0).toString().equals("DOUBLE")
                        && m_typeStack.get(1).toString().equals("DOUBLE")) {
                    m_commands.put(CG.DADD);
                    m_typeStack.pop();
                }
            }
            break;
            case "-": {
                if (m_typeStack.get(m_typeStack.size() - 2).toString().equals("INT")
                        && m_typeStack.get(m_typeStack.size() - 1).toString().equals("INT")) {
                    m_commands.put(CG.ISUB);
                    m_typeStack.pop();
                } else if (m_typeStack.get(0).toString().equals("DOUBLE")
                        && m_typeStack.get(1).toString().equals("DOUBLE")) {
                    m_commands.put(CG.DSUB);
                    m_typeStack.pop();
                }
            }
            break;
            case "*": {
                if (m_typeStack.get(m_typeStack.size() - 2).toString().equals("INT")
                        && m_typeStack.get(m_typeStack.size() - 1).toString().equals("INT")) {
                    m_commands.put(CG.IMUL);
                    m_typeStack.pop();
                } else if (m_typeStack.get(0).toString().equals("DOUBLE")
                        && m_typeStack.get(1).toString().equals("DOUBLE")) {
                    m_commands.put(CG.DMUL);
                    m_typeStack.pop();
                }

            }
            break;
            case "/": {

                if (m_typeStack.get(m_typeStack.size() - 2).toString().equals("INT")
                        && m_typeStack.get(m_typeStack.size() - 1).toString().equals("INT")) {
                    m_commands.put(CG.IDIV);
                    m_typeStack.pop();
                } else if (m_typeStack.get(0).toString().equals("DOUBLE")
                        && m_typeStack.get(1).toString().equals("DOUBLE")) {
                    m_commands.put(CG.DDIV);
                    m_typeStack.pop();
                }
            }
            break;
            case "**": {
                if (m_typeStack.get(m_typeStack.size() - 2).toString().equals("INT")
                        && m_typeStack.get(m_typeStack.size() - 1).toString().equals("INT")) {
                    m_commands.put(CG.INVOKESTATIC);
                    int idMethodRef = findIdForMethodRef("RTL", "ku_pow", "(II)I");
                    m_commands.putShort((short) idMethodRef);
                    m_typeStack.pop();
                } else if (m_typeStack.get(0).toString().equals("DOUBLE")
                        && m_typeStack.get(1).toString().equals("DOUBLE")) {
                    m_commands.put(CG.INVOKESTATIC);
                    int idMethodRef = findIdForMethodRef("RTL", "ku_pow", "(DD)D");
                    m_commands.putShort((short) idMethodRef);
                    m_typeStack.pop();
                }

            }
            break;
            case "[]:=": {
                byte i = 1;

                String type = m_typeStack.pop().toString();
                switch (type) {
                    case "INT":
                        m_commands.put(CG.IASTORE);
                        break;
                    case "CHAR":
                        m_commands.put(CG.CASTORE);
                        break;
                    case "String":
                        m_commands.put(CG.AASTORE);
                        break;
                    case "BOOLEAN":
                        m_commands.put(CG.BASTORE);
                        break;
                    case "DOUBLE":
                        m_commands.put(CG.DASTORE);
                        break;
                }
                m_typeStack.clear();



            }
            break;
            case ":=": {

                if (vx.getParentList().get(0).getAttribute("NAME").equals(":=")) {
                    m_commands.put(CG.DUP);
                }
                String nameOfVar = vx.getChildByOrder(0).getChildByOrder(0).getChildByOrder(0).getAttribute("NAME");
                int funID = Semantic.fpTable.getRowByName(curMethod).getID();
                int idLocalVal = Semantic.localsTable.getRowByFunIDAndName(funID, nameOfVar).getID();

                String type = m_typeStack.pop().toString();


                if (type.equals("String")) {
                    putAIntoLocalVar((byte) idLocalVal);
                } else if (type.equals("INT")) {
                    putIIntoLocalVar((byte) idLocalVal);
                } else if (type.equals("DOUBLE")) {
                    putDIntoLocalVar((byte) idLocalVal);
                } else if (type.equals("CHAR")) {
                    putCIntoLocalVar((byte) idLocalVal);
                }


                m_typeStack.pop();// осталось от сгенерированного lvalue

            }
            break;


        }
    }

    public void putDIntoLocalVar(byte numLocVar) {

        if (numLocVar == 0) {
            m_commands.put(CG.DSTORE_0);
        } else if (numLocVar == 1) {
            m_commands.put(CG.DSTORE_1);
        } else if (numLocVar == 2) {
            m_commands.put(CG.DSTORE_2);
        } else if (numLocVar == 3) {
            m_commands.put(CG.DSTORE_3);
        } else {
            m_commands.put(CG.DSTORE);
            m_commands.put(numLocVar);
        }
    }

    public void putAIntoLocalVar(byte numLocVar) {

        if (numLocVar == 0) {
            m_commands.put(CG.ASTORE_0);
        } else if (numLocVar == 1) {
            m_commands.put(CG.ASTORE_1);
        } else if (numLocVar == 2) {
            m_commands.put(CG.ASTORE_2);
        } else if (numLocVar == 3) {
            m_commands.put(CG.ASTORE_3);
        } else {
            m_commands.put(CG.ASTORE);
            m_commands.put(numLocVar);
        }
    }

    public void putIIntoLocalVar(byte numLocVar) {

        if (numLocVar == 0) {
            m_commands.put(CG.ISTORE_0);
        } else if (numLocVar == 1) {
            m_commands.put(CG.ISTORE_1);
        } else if (numLocVar == 2) {
            m_commands.put(CG.ISTORE_2);
        } else if (numLocVar == 3) {
            m_commands.put(CG.ISTORE_3);
        } else {
            m_commands.put(CG.ISTORE);
            m_commands.put(numLocVar);
        }
    }

    public void putCIntoLocalVar(byte numLocVar) {

        if (numLocVar == 0) {
            m_commands.put(CG.ISTORE_0);
        } else if (numLocVar == 1) {
            m_commands.put(CG.ISTORE_1);
        } else if (numLocVar == 2) {
            m_commands.put(CG.ISTORE_2);
        } else if (numLocVar == 3) {
            m_commands.put(CG.ISTORE_3);
        } else {
            m_commands.put(CG.ISTORE);
            m_commands.put(numLocVar);
        }
    }

    public void close() throws IOException {

        m_output.close();

    }
}
