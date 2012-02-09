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

    public LocalsTableRow getRowByName(String in_name) {
        LocalsTableRow result = null;

        Iterator it = nodes.iterator();
        while (it.hasNext()) {
            LocalsTableRow row = (LocalsTableRow) it.next();
            String name = (String) row.getValue();
            // name = name.split("\\[")[0];
            if (name.equals(in_name)) {
                result = row;
                break;
            }
        }
        return result;
    }

    public LocalsTableRow getRowByFunIDAndName(String funId, String in_name) {
        LocalsTableRow result = null;

        Iterator it = nodes.iterator();
        while (it.hasNext()) {
            LocalsTableRow row = (LocalsTableRow) it.next();
            String name = (String) row.getValue();
            int _funID = Integer.valueOf(funId).intValue();
            // name = name.split("\\[")[0];
            if (name.equals(in_name) && row.getFuncID() == _funID) {
                result = row;
                break;
            }
        }
        return result;
    }

    public LocalsTableRow getRowByFunIDAndName(int funId, String in_name) {
        LocalsTableRow result = null;

        Iterator it = nodes.iterator();
        while (it.hasNext()) {
            LocalsTableRow row = (LocalsTableRow) it.next();
            String name = (String) row.getValue();

            // name = name.split("\\[")[0];
            if (name.equals(in_name) && row.getFuncID() == funId) {
                result = row;
                break;
            }
        }
        return result;
    }

    public LocalsTableRow getRowByID(int id) {
        LocalsTableRow result = null;

        Iterator it = nodes.iterator();
        while (it.hasNext()) {
            LocalsTableRow row = (LocalsTableRow) it.next();
            if (row.getID() == id) {
                result = row;
                break;
            }
        }
        return result;
    }

    public void printTable() {
        Iterator it = nodes.iterator();
        while (it.hasNext()) {
            ((LocalsTableRow) it.next()).print();
        }
    }

    public Iterator getIterator() {
        return nodes.iterator();
    }

    public ArrayList<LocalsTableRow> getLocalsFor(int id) {
        ArrayList<LocalsTableRow> result = new ArrayList();

        Iterator it = nodes.iterator();
        while (it.hasNext()) {
            LocalsTableRow row = (LocalsTableRow) it.next();
            if (row.getFuncID() == id) {
                result.add(row);
            }
        }
        return result;
    }
}
