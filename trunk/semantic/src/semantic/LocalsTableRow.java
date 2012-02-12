package semantic;

import java.util.ArrayList;
import java.util.Iterator;

public class LocalsTableRow {

    /*
     * ID локальной переменной в данной функции
     */
    private int m_trueID;
    /*
     * Местоположения данного символа (но не конкретной переменной!)
     */
    private ArrayList<Integer> m_locs;
    /*
     * Тип переменной
     */
    private String m_constantType;
    /*
     * Имя переменной
     */
    private Object m_value;
    /*
     * Является ли переменная параметром
     */
    private boolean m_isParameter;
    /*
     * ID функции в таблице функций, которой пренадлежит данная переменная
     */
    private int m_idFunc;

    /*
     * Конструктор, который создаёт запись в таблице локальных переменных,
     * исходя из массива местоположений, типа переменной, имени переменной, id
     * функции, в которой переменная существует, признака, является ли
     * переменная параметром.
     */
    public LocalsTableRow(ArrayList<Integer> locs, String constType, Object value, int idFunc, boolean isParam) {
        m_locs = locs;
        m_constantType = constType;

        m_value = value;
        m_idFunc = idFunc;
        m_isParameter = isParam;

        m_trueID = calculateTrueID(idFunc);

    }

    /*
     * Вычисляет истинный ID локальной переменной. Функция гарантирует, что если
     * начата новая процедура/функция, то ID переменных начнутся с 0, а не с
     * того числа, которым закончились IDs прошлых функций (что закономерно, так
     * как здесь одна таблица на все функции)
     */
    private int calculateTrueID(int funID) {
        int result = 0;

        Iterator it = Semantic.localsTable.getIterator();
        while (it.hasNext()) {
            LocalsTableRow row = (LocalsTableRow) it.next();
            if (row.getFuncID() == funID) {
                result++;
            }
        }

        return result;
    }
    /*
     * Возвращает название переменной
     */

    public Object getValue() {
        return m_value;
    }
    /*
     * Печатает строку в stdout
     */

    public void print() {
        String numLocs = "";
        Iterator it = m_locs.iterator();
        while (it.hasNext()) {
            numLocs += it.next() + ",";
        }
        numLocs = numLocs.substring(0, numLocs.length() - 1);
        System.out.printf("|%d|%s|%s|%s|%d|\n", m_trueID, numLocs, m_constantType, m_value.toString(), m_idFunc);
    }
    /*
     * Возвращает местоположения символа (местоположения вообще в программе)
     * Если требуется отследить в конкретной функции, нужно модифицировать
     */

    public ArrayList<Integer> getLocs() {
        return m_locs;
    }

    /*
     * Возвращает ID переменной в таблице локальных переменных
     */
    public int getID() {
        return m_trueID;
    }
    /*
     * Возвращает ID функции, которой пренадлежит данная переменная
     */

    public int getFuncID() {
        return m_idFunc;
    }
    /*
     * Возвращает тип переменной
     */

    public String getType() {
        return m_constantType;
    }
}
