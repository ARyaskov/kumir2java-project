package semantic;

import java.util.*;

public class FPTableRow {

    static int idCount = 0;
    private String m_name;
    private int m_ID;
    private int m_constTableID;
    private String m_returnType;
    private int m_parCount;
    private ArrayList<String> m_parTypes;
    private String m_descriptor;

    public FPTableRow(int idConstTable, String name, String returnType, int countPar, ArrayList<String> pars) {
        m_constTableID = idConstTable;
        
        
        
        m_ID = idCount++;
        m_name = name;
        if (returnType!=null)
            m_returnType = returnType;
        else
            m_returnType = "V";
        m_parCount = countPar;
        m_parTypes = pars;
        m_descriptor = makeDescriptor(pars, m_returnType);
     /*   System.out.print("TEST\n");
        this.printRow();*/

    }

    public static String makeDescriptor(ArrayList<String> parTypes, String ret) {
        String temp = "";
        temp += "(";
        for (String s : parTypes) {
            if (s.equals("цел")) {
                temp += "I";
            } else if (s.equals("сим")) {
                temp += "C";
            } else if (s.equals("лит")) {
                temp += "Ljava/lang/String;";
            } else if (s.equals("лог")) {
                temp += "Z";
            } else if (s.equals("целтаб")) {
                temp += "[I";
            } else if (s.equals("симтаб")) {
                temp += "[C";
            } else if (s.equals("литтаб")) {
                temp += "[Ljava/lang/String;";
            } else if (s.equals("логтаб")) {
                temp += "[Z";
            }


        }
        temp += ")";
        /*
         * if (m_returnType.equals("цел")) { temp += "I"; } else if
         * (m_returnType.equals("сим")) { temp += "C"; } else if
         * (m_returnType.equals("лит")) { temp += "Ljava/lang/String;"; } else
         * if (m_returnType.equals("лог")) { temp += "B";
        }
         */
        temp += ret;
        return temp;
        
    }

    public void setName(String newName) {
        m_name = newName;
    }
    /*
     * public void setID(int id) { m_ID = id; }
     */

    public void setReturnType(String type) {
        m_returnType = type;
    }

       public void setID(int id) {
        m_ID = id;
    }
    public void setParCount(int count) {
        m_parCount = count;
    }

    public void setParTypes(ArrayList<String> types) {
        m_parTypes = types;
    }

    public String getName() {
        return m_name;
    }

    public int getID() {
        return m_ID;
    }

    public String getReturnType() {
        return m_returnType;
    }

    public int getParCount() {
        return m_parCount;
    }

    public ArrayList<String> getParTypes() {
        return m_parTypes;
    }

    public int getConstTable() {
        return m_constTableID;
    }

    public void setConstTableID(int id) {
        m_constTableID = id;
    }

    public void printRow() {
        String pars = "";
        if (m_parTypes != null) {
            Iterator it = m_parTypes.iterator();
            while (it.hasNext()) {
                pars += it.next() + ",";
            }
            pars = pars.substring(0, pars.length() - 1);
            System.out.printf("|%d|%d|%s|%s|%d|%s|\n", m_ID, m_constTableID, m_returnType, m_name, m_parCount, m_descriptor);
        }
    }
}
