package semantic;

import java.util.*;

/**
 * Строка таблицы констант
 */
/*
 * |constantID|Locations|typeOfConstant|value|
 * 
*/
public class ConstantsTableRow {

    public static int m_constantIDCount = 1;
    /**
     * Статический счётчик для вычисления следующего ID
     */
    private int m_constantID;
    /**
     * Идентификатор константы
     */
    private ArrayList<Integer> m_locations;
    /**
     * Местоположения в коде
     */
    private String m_typeOfConstant;
    /**
     * Тип константы
     */
    private Object m_value;

    /**
     * Значение константы
     */
    /*
     * Конструктор, создающий строку таблицы констант с положениями в коде locs,
     * типом type и значением константы value
     */
    public ConstantsTableRow(ArrayList<Integer> locs, String type, Object value) {
        m_locations = locs;
        m_typeOfConstant = type;
        m_value = value;
        m_constantID = m_constantIDCount;
        m_constantIDCount++;
    }
    
        public ConstantsTableRow(int loc, String type, Object value) {
        m_locations = new ArrayList();
        m_locations.add(Integer.valueOf(loc));
        m_typeOfConstant = type;
        m_value = value;
        m_constantID = m_constantIDCount;
        m_constantIDCount++;
    }

    /*
     * Возвращает номера строк в коде, где встретилась константа
     */
    public ArrayList<Integer> getLocation() {
        return m_locations;
    }
    
    public Integer getLowestLocation(){
        Integer result = null;
        int lowest = m_locations.get(0);
        for (Integer inte : m_locations){
            if (inte.intValue() < lowest){
                lowest = inte.intValue();
            }
        }
        
        result = Integer.valueOf(lowest);
        
        return result;
    }
    /*
     * Возвращает тип константы в виде строки
     */

    public String getType() {
        return m_typeOfConstant;
    }
    /*
     * Возвращает значение константы (может быть преобразовано в любой тип)
     */

    public Object getValue() {
        return m_value;
    }
    /*
     * Возвращает ID константы в таблице констант
     */

          public void setID(int id) {
        m_constantID = id;
    }
    public int getID() {
        return m_constantID;
    }
    /*
     * Установить положения константы в коде
     */

    public void setLocations(ArrayList<Integer> in_locs) {
        m_locations = in_locs;
    }
    /*
     * Добавить положение константы в коде
     */

    public void addLocation(Integer in_loc) {
        m_locations.add(in_loc);
    }
    /*
     * Установить тип константы (например, "UTF-8")
     */

    public void setType(String in_type) {
        m_typeOfConstant = in_type;
    }
    /*
     * Установить значение константы
     */

    public void setValue(Object in_value) {
        m_value = in_value;
    }
    /*
     * Печать строки в stdout
     */

    public void printRow() {
        String numRows = "";
        Set set = new HashSet();


        if (m_locations != null) {
            Iterator it = m_locations.iterator();
            while (it.hasNext()) {
               Integer inte =(Integer) it.next();
                numRows += inte.toString() + ",";
                set.add(inte);
            }
            if (numRows.length()>0)
                numRows = numRows.substring(0, numRows.length() - 1);
        }
        
        System.out.printf("|%d|%s|%s|%s|\n", m_constantID, numRows, m_typeOfConstant, m_value.toString());
    }
   
    
    
}
