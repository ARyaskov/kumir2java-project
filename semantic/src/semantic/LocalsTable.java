package semantic;

import java.util.*;

public class LocalsTable {

    private ArrayList<LocalsTableRow> nodes;

    public LocalsTable() {
        nodes = new ArrayList();
    }

    public void add(LocalsTableRow row) {
        nodes.add(row);
    }

    public LocalsTableRow getRowByName(String name) {
        LocalsTableRow result = null;

        Iterator it = nodes.iterator();
        while (it.hasNext()) {
            LocalsTableRow row = (LocalsTableRow) it.next();
            if (row.getValue().equals(name)) {
                result = row;
                break;
            }
        }
        return result;
    }


    public void printTable(){
        Iterator it = nodes.iterator();
        while(it.hasNext()){
            ((LocalsTableRow)it.next()).print();
        }
    }
}
