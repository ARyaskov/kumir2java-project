package semantic;

import java.util.*;

public class FPTable {

    private ArrayList<FPTableRow> nodes;

    public FPTable() {
        nodes = new ArrayList();
    }

    public void add(FPTableRow row) {
        nodes.add(row);
    }

    public Iterator getIterator() {
        return nodes.iterator();
    }

    public boolean isIdentifierExists(String str) {
        boolean result = false;
        Iterator it = nodes.iterator();
        while (it.hasNext()) {
            FPTableRow row = (FPTableRow) it.next();
            if (row.getName().equals(str)) {
                result = true;
                break;
            }
        }

        return result;
    }

    public FPTableRow getRowByName(String name) {
        FPTableRow result = null;

        Iterator it = nodes.iterator();
        while (it.hasNext()) {
            FPTableRow row = (FPTableRow) it.next();
            if (row.getName().equals(name)) {
                result = row;
                break;
            }
        }
        return result;
    }

    public void printTable(){
        Iterator it = nodes.iterator();
        while(it.hasNext()){
            ((FPTableRow)it.next()).printRow();

        }
    }

    public int getSize(){
        return nodes.size();
    }

}
