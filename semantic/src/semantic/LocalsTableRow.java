package semantic;

import java.util.*;

public class LocalsTableRow {

    private static int m_idCount = 0;
    private int m_id;
    private int m_trueID;
    private ArrayList<Integer> m_locs;
    private String m_constantType;
    private String m_name;
    private Object m_value;
    private boolean m_isParameter;
    private int m_idFunc;

    public LocalsTableRow(ArrayList<Integer> locs, String constType, Object value, int idFunc, boolean isParam) {
        m_id = m_idCount++;
        m_locs = locs;
        m_constantType = constType;

        m_value = value;
        m_idFunc = idFunc;
        m_isParameter = isParam;
        
        m_trueID = calculateTrueID(idFunc);
        
    }

    private int calculateTrueID(int funID){
        int result=0;
        
        Iterator it = Semantic.localsTable.getIterator();
        while(it.hasNext()){
           LocalsTableRow row = (LocalsTableRow)it.next();
           if (row.getFuncID() == funID){
               result++;
           }
        }
        
        return result;
    }
    public Object getValue() {
        return m_value;
    }

    public void print() {
        String numLocs = "";
        Iterator it = m_locs.iterator();
        while (it.hasNext()) {
            numLocs += it.next() + ",";
        }
        numLocs = numLocs.substring(0, numLocs.length() - 1);
        System.out.printf("|%d|%s|%s|%s|%d|\n", m_id, numLocs, m_constantType, m_value.toString(), m_idFunc);
    }

    public ArrayList<Integer> getLocs() {
        return m_locs;
    }

    public int getID() {
        return m_trueID;
    }

    public int getFuncID() {
        return m_idFunc;
    }
    public String getType(){
        return m_constantType;
    }
}
