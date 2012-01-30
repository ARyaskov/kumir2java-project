package semantic;

import java.util.*;

/*
 * Класс таблицы констант
 */
public class ConstantsTable {

    /*
     * Список для хранения таблицы
     */
    private ArrayList<ConstantsTableRow> rows = null;

    /*
     * Создаёт таблицу констант
     */
    public ConstantsTable() {
        rows = new ArrayList();
    }

    /*
     * Добавляет строку к таблице констант
     */
    public void addRow(ConstantsTableRow row) {
        if (isValueExists(row.getValue()) != null) {
            Iterator it = row.getLocation().iterator();
            while (it.hasNext()) {
                isValueExists(row.getValue()).addLocation(
                        (Integer) it.next());
            }
            ConstantsTableRow.m_constantIDCount--;
            row.setID(isValueExists(row.getValue()).getID());
        } else {
            rows.add(row);
        }
    }
    /*
     * Удаляет строку из таблицы констант
     */

    public void delRow(ConstantsTableRow row) {
        rows.remove(row);
    }
    /*
     * Удаляет строку по значению из таблицы констант
     */

    public void delRow(Object val) {
        rows.remove(getRowByVal(val));
    }

    /*
     * Проверяет нет ли в таблице объекта со схожим значением, если есть -
     * возвращает объект строки
     */
    public ConstantsTableRow isValueExists(Object val) {
        ConstantsTableRow result = null;
        Iterator it = rows.iterator();
        while (it.hasNext()) {
            ConstantsTableRow row = (ConstantsTableRow) it.next();
            if (row.getValue().equals(val)) {
                result = row;
                break;
            }
        }

        return result;

    }

    /*
     * Возвращает объект строки по ID записи в таблице
     */
    public ConstantsTableRow getRowById(int id) {
        ConstantsTableRow result = null;

        Iterator it = rows.iterator();
        while (it.hasNext()) {
            ConstantsTableRow curRow = (ConstantsTableRow) it.next();
            if (curRow.getID() == id) {
                result = curRow;
                break;
            }
        }

        return result;
    }

    /*
     * Возвращает объект строки по значению записи в таблице
     */
    public ConstantsTableRow getRowByVal(Object val) {
        ConstantsTableRow result = null;

        Iterator it = rows.iterator();
        while (it.hasNext()) {
            ConstantsTableRow curRow = (ConstantsTableRow) it.next();
            if (curRow.getValue() == val) {
                result = curRow;
                break;
            }
        }

        return result;
    }

    /*
     * Печатает таблицу в поток вывода
     */
    public void printTable() {
        Iterator it = rows.iterator();
        while (it.hasNext()) {
            ((ConstantsTableRow) it.next()).printRow();
        }
    }

    public ConstantsTableRow getRowByName(String name) {
        ConstantsTableRow result = null;

        Iterator it = rows.iterator();
        while (it.hasNext()) {
            ConstantsTableRow row = (ConstantsTableRow) it.next();
            if (row.getValue().equals(name)) {
                result = row;
                break;
            }
        }
        return result;
    }


    
    public Iterator getIterator() {
        return rows.iterator();
    }

    public void removeRow(ConstantsTableRow row) {
        rows.remove(row);

    }

    public void removeRow(int id) {
        rows.remove(this.getRowById(id));

    }
    public int getCount(){
        return rows.size();
    }
}
