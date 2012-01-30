package semantic;

import java.io.*;
import java.util.*;

/*
 * Класс узла в дереве
 */
public class Vertex {

    public static int vertexIDCount = 0;
    private int m_vertexID;
    private String m_idFromDOT = null;
    /*
     * Таблица атрибутов - имя:значение
     */
    private HashMap<String, String> m_ats;
    /*
     * Список потомков
     */
    private ArrayList<Vertex> m_childs;
    /*
     * Список родителей
     */
    private ArrayList<Vertex> m_parents;

    /*
     * Тип - одно из ID, CONSTANT, TYPE, SIMPLE
     */
    public String m_typeOfSymbol;
    /*
     * Первональное имя - то, что поступило от dot'а
     */
    private String m_virginName;
    /*
     * Наследовать ли тип от потомков
     */
    private boolean m_isInheritesType;
    /*
     * Есть ли тип вообще
     */
    private boolean m_hasType;

    public void inheriteType() {
        Iterator it = m_childs.iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            ArrayList<String> castedTypeList = new ArrayList();
            if (vx.getHasType()) {// Если у узла есть тип
                ArrayList<Vertex> comrades = vx.haveSameParent();
                Iterator it2 = comrades.iterator();
                while (it2.hasNext()) {
                    Vertex vx2 = (Vertex) it.next();
                    String castedType = null;
                    if (vx.isCastValid(vx2)) {
                        castedType = vx.castType(vx2);
                        castedTypeList.add(castedType);
                    }
                }

                this.addAttribute("Type", castType(castedTypeList));
            }
        }
    }

    /*
     * Возвращает тип, получившийся при смешении двух выражений
     */
    public String castType(Vertex in_vx) {
        String result = null;

        String at1 = this.getAttribute("Type");
        String at2 = in_vx.getAttribute("Type");


        if (at1.equals(at2)) {
            result = at1;
        } else if ((at1.equals("вещ") && at2.equals("цел"))
                || (at1.equals("цел") && at2.equals("вещ"))) {
            result = "вещ";
        }


        return result;
    }

    /*
     * Возвращает тип, получившийся при смешении массива типов Предполагается
     * что проверка на совместимость типов пройдена
     */
    public String castTypeList(ArrayList<Vertex> in_vx) {
        String result = null;
        Iterator it = in_vx.iterator();
        String str = null;
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            str = vx.getAttribute("TYPE");
            if (str.equals("вещ")) {
                result = "вещ";
                break;
            }
            result = in_vx.get(0).getAttribute("TYPE");
        }


        return result;
    }

    /*
     * Возвращает ID вершины, полученный из dot-файла
     */
    public String getIdFromDOT() {
        return m_idFromDOT;
    }

    /*
     * Возвращает ID вершины (в виде числа), полученный из dot-файла
     */
    public int getIntIdFromDOT() {
        return Integer.valueOf(m_idFromDOT).intValue();
    }

    /*
     * Устанавливает ID, полученный из dot-а для вершины
     */
    public void setIdFromDOT(String newId) {
        m_idFromDOT = newId;
    }

    /*
     * Преобразует тип, исходя из двух известных
     */
    public static String castType(String t1, String t2) {
        String result = null;

        if (t1.equals(t2)) {
            result = t1;
        } else if ((t1.equals("вещ") && t2.equals("цел"))
                || (t1.equals("цел") && t2.equals("вещ"))) {
            result = "вещ";
        }

        return result;
    }

    /*
     * Преобразует тип, исходя из массива типов
     */
    public static String castType(ArrayList<String> list) {
        String result = null;

        if (list.contains("вещ") && list.contains("цел")) {
            result = "вещ";
        } else {
            result = list.get(0);
        }

        return result;
    }

    /*
     * Возвращает список потомков
     */
    public ArrayList<Vertex> getChildList() {
        return m_childs;
    }

    /*
     * Возвращает список потомков
     */
    public ArrayList<Vertex> getParentList() {
        return m_parents;
    }

    /*
     * Возвращает справедливость преобразования типа, при операциях с vx
     */
    public boolean isCastValid(Vertex vx) {
        boolean result = false;
        String at1 = this.getAttribute("Type");
        String at2 = vx.getAttribute("Type");
        if (at1 != null && at2 != null) {
            if ((at1.equals("вещ") && at2.equals("цел"))
                    || (at1.equals("цел") && at2.equals("вещ"))
                    || at1.equals(at2)) {

                result = true;
            }
        }
        return result;
    }

    /*
     * Возвращает список вершин, имеющих такого же родителя. Работает в случае
     * одного родителя
     */
    public ArrayList<Vertex> haveSameParent() {
        ArrayList<Vertex> result = new ArrayList<Vertex>();
        Iterator it = getParentList().get(0).getChildList().iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            if (vx != this) {
                result.add(vx);
            }

        }
        return result;
    }

    /*
     * Добавить атрибут к вершине
     */
    public void addAttribute(String in_key, String in_value) {
        m_ats.put(in_key, in_value);
    }

    /*
     * Удалить атрибут
     */
    public void delAttribute(String in_key) {
        m_ats.remove(in_key);
    }

    /*
     * Возвращает атрибут вершины по заданному имени
     */
    public String getAttribute(String in_key) {
        return m_ats.get(in_key);
    }

    /*
     * Добавить вершину vx к потомкам
     */
    public void addToChilds(Vertex vx) {
        m_childs.add(vx);
    }
    /*
     * Добавить вершину vx к предкам
     */

    public void addToParents(Vertex vx) {
        m_parents.add(vx);
    }

    /*
     * Взять потомка по первоначальному имени, полученному из dot
     */
    public Vertex getChild(String virName) {
        Vertex result = null;
        Iterator it = m_childs.iterator();
        while (it.hasNext()) {
            Vertex vxNow = (Vertex) it.next();
            if (vxNow.getVirginName().equals(virName)) {
                result = vxNow;
                break;
            }
        }

        return result;
    }

    /*
     * Взять родителя по первоначальному имени, полученному из dot
     */
    public Vertex getParent(String virName) {
        Vertex result = null;
        Iterator it = m_parents.iterator();
        while (it.hasNext()) {
            Vertex vxNow = (Vertex) it.next();
            if (vxNow.getVirginName().equals(virName)) {
                result = vxNow;
                break;
            }
        }

        return result;
    }

    public Vertex getChildByOrder(int order) {
        Vertex result = null;

        result = m_childs.get(order);

        return result;
    }

    public Vertex getLastDescendant() {
        Vertex result = this;

        Iterator it = result.getChildList().iterator();
        while (it.hasNext()) {
            Vertex vxNow = (Vertex) it.next();
            result = vxNow.getLastDescendant();
        }

        return result;
    }

    /*
     * Возвращает признак - наследует ли данная вершина типы от потомков
     */
    public boolean getIsInheritesType() {
        return m_isInheritesType;
    }

    /*
     * Устанавливает факт того, наследует ли вершина тип от потомков
     */
    public void setIsInheritesType(boolean newState) {
        m_isInheritesType = newState;
    }

    /*
     * Возвращает признак - есть ли у вершины тип
     */
    public boolean getHasType() {
        return m_hasType;
    }

    /*
     * Устанавливает признак - есть ли у вершины тип
     */
    public void setHasType(boolean newState) {
        m_hasType = newState;
    }

    /*
     * Возвращает ID вершины, который был присвоен при создании (это не то же
     * самое, что ID в dot-файле)
     */
    public int getID() {
        return m_vertexID;
    }

    /*
     * Возвращает тип символа грамматики - одно из ID, TYPE, SIMPLE, OPERATION
     */
    public String getTypeOfSymbol() {
        return m_typeOfSymbol;
    }

    /*
     * Устанавливает тип символа грамматики
     */
    public void setTypeOfSymbol(String newType) {
        m_typeOfSymbol = newType;
    }

    /*
     * Возвращает первоначальное имя (как оно получено от dot-файла)
     */
    public String getVirginName() {
        return m_virginName;
    }

    public boolean containsAttribute(String att) {
        return m_ats.containsKey(att);
    }
    /*
     * Устанавливает первоначальное имя
     */

    public void setVirginName(String newName) {
        m_virginName = newName;
    }

    public void printVertex() {
        System.out.printf("%s\nAttributes:<NAME:%s, TYPE:%s>\n"
                + "Type of symbol: %s\n"
                + "Virgin Name: %s\n"
                + "ID: %s\n"
                + "Order: %s\n"
                + "Childs: %s\n"
                + "Parents: %s\n", this.toString(), getAttribute("NAME"), getAttribute("TYPE"),
                this.getTypeOfSymbol(),
                this.getVirginName(),
                getAttribute("ID"),
                this.getAttribute("ORDER"),
                this.getChildList().toString(),
                this.getParentList().toString());
    }

    public Vertex(String str) {
        m_ats = new HashMap();
        m_parents = new ArrayList<Vertex>();
        m_vertexID = Semantic.vertexId++;
        m_virginName = str;
        m_childs = new ArrayList();

    }

    public Vertex() {
        m_ats = new HashMap();
        m_parents = new ArrayList<Vertex>();
        m_vertexID = Semantic.vertexId++;
        m_childs = new ArrayList();
    }
}
