package semantic;

import java.util.ArrayList;
import java.util.Iterator;

public class FPTableRow {

    /*
     * Статический счетчик для ID
     */
    static int idCount = 0;
    /*
     * Имя функции
     */
    private String m_name;
    /*
     * ID в таблице функций
     */
    private int m_ID;
    /*
     * ID в таблице констант (NameAndType_info для функции)
     */
    private int m_constTableID;
    /*
     * Возвращаемый тип
     */
    private String m_returnType;
    /*
     * Количество параметров
     */
    private int m_parCount;
    /*
     * Массив типов параметров
     */
    private ArrayList<String> m_parTypes;
    /*
     * Дескриптор функции в формате NameAndType_info
     */
    private String m_descriptor;

    /*
     * Конструктор создаёт строку таблицы функций из ID в таблице констант
     * (NameAndType_info), имени функции, возвращаемого значения, количества
     * параметров, массива типов параметров
     *
     */
    public FPTableRow(int idConstTable, String name, String returnType, int countPar, ArrayList<String> pars) {
        m_constTableID = idConstTable;
        m_ID = idCount++;
        m_name = name;
        if (returnType != null) {
            m_returnType = returnType;
        } else {
            m_returnType = "V";
        }
        m_parCount = countPar;
        m_parTypes = pars;
        m_descriptor = makeDescriptor(pars, m_returnType);

        translateTypes();
    }

    /*
     * Переводит типы из вида "цел" и "симтаб[10" в вид "I" и "[C"
     */
    private void translateTypes() {
        ArrayList<String> newPars = new ArrayList();
        String temp = "";
        for (String s : m_parTypes) {
            String[] _sArr = s.split("\\[");
            for (int i = 1; i < _sArr.length; i++) {
                temp += "[";
            }
            if (s.equals("цел")) {
                temp += "I";
            } else if (s.equals("сим")) {
                temp += "C";
            } else if (s.equals("лит")) {
                temp += "Ljava/lang/String;";
            } else if (s.equals("лог")) {
                temp += "Z";
            } else if (s.equals("вещ")) {
                temp += "D";
            } else if (s.indexOf("целтаб") != -1) {
                temp += "I";
            } else if (s.indexOf("симтаб") != -1) {
                temp += "C";
            } else if (s.indexOf("литтаб") != -1) {
                temp += "Ljava/lang/String;";
            } else if (s.indexOf("логтаб") != -1) {
                temp += "Z";
            } else if (s.indexOf("вещтаб") != -1) {
                temp += "D";
            }
            newPars.add(temp);
            temp = "";
        }
        m_parTypes = newPars;
    }

    /*
     * Создаёт дескриптор функции из массива параметров и возвращаемого значения
     * в формате NameAndType_info
     */
    public static String makeDescriptor(ArrayList<String> parTypes, String ret) {
        String temp = "";
        temp += "(";
        for (String s : parTypes) {
            String[] _sArr = s.split("\\[");
            for (int i = 1; i < _sArr.length; i++) {
                temp += "[";
            }

            if (s.equals("цел")) {
                temp += "I";
            } else if (s.equals("сим")) {
                temp += "C";
            } else if (s.equals("вещ")) {
                temp += "D";
            } else if (s.equals("лит")) {
                temp += "Ljava/lang/String;";
            } else if (s.equals("лог")) {
                temp += "Z";
            } else if (s.indexOf("целтаб") != -1) {

                temp += "I";
            } else if (s.indexOf("вещтаб") != -1) {

                temp += "D";
            } else if (s.indexOf("симтаб") != -1) {

                temp += "C";
            } else if (s.indexOf("литтаб") != -1) {

                temp += "Ljava/lang/String;";
            } else if (s.indexOf("логтаб") != -1) {

                temp += "Z";
            }
        }
        temp += ")";
        temp += ret;
        return temp;

    }
    /*
     * Устанавливает имя функции
     */

    public void setName(String newName) {
        m_name = newName;
    }

    /*
     * Устанавливает тип возращаемого значения
     */
    public void setReturnType(String type) {
        m_returnType = type;
    }
    /*
     * Устанавливает ID записи
     */

    public void setID(int id) {
        m_ID = id;
    }
    /*
     * Устанавливает число параметров
     */

    public void setParCount(int count) {
        m_parCount = count;
    }
    /*
     * Устанавливает типы параметров
     */

    public void setParTypes(ArrayList<String> types) {
        m_parTypes = types;
    }
    /*
     * Возвращает имя записи
     */

    public String getName() {
        return m_name;
    }
    /*
     * Возвращает ID записи
     */

    public int getID() {
        return m_ID;
    }
    /*
     * Возвращает тип возвращаемого значения
     */

    public String getReturnType() {
        return m_returnType;
    }
    /*
     * Возвращает количество параметров
     */

    public int getParCount() {
        return m_parCount;
    }

    /*
     * Возвращает массив с типами параметров
     */
    public ArrayList<String> getParTypes() {
        return m_parTypes;
    }
    /*
     * Возвращает индекс в таблице констант
     */

    public int getConstTableID() {
        return m_constTableID;
    }
    /*
     * Устанавливает индекс в таблице констант
     */

    public void setConstTableID(int id) {
        m_constTableID = id;
    }
    /*
     * Печатает строку в stdout
     */

    public void printRow() {
        System.out.printf("|%d|%d|%s|%s|%d|%s|\n", m_ID, m_constTableID, m_returnType, m_name, m_parCount, m_descriptor);

    }
}
