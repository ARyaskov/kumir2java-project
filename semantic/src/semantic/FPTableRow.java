package semantic;

import java.util.*;

public class FPTableRow {

    private static int idCount = 0;
    private String m_name;
    private int m_ID;
    private int m_constTableID;
    private String m_returnType;
    private int m_parCount;
    private ArrayList<String> m_parTypes;

    public FPTableRow(int idConstTable, String name, String returnType, int countPar, ArrayList<String> pars) {
        m_constTableID = idConstTable;
        m_ID = idCount++;
        m_name = name;
        m_returnType = returnType;
        m_parCount = countPar;
        m_parTypes = pars;
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
            System.out.printf("|%d|%d|%s|%s|%d|%s|\n", m_ID, m_constTableID, m_returnType, m_name, m_parCount, pars);
        }
    }
}
