package semantic;

import java.util.*;

public class Graph {

    private ArrayList<Vertex> m_nodes;
    private Vertex m_root;

    public Graph() {
        m_nodes = new ArrayList();
        m_root = null;
    }

    public int getSize() {
        return m_nodes.size();
    }

    /**
     * Добавляет узел futureParentVertex как родителя для ofVertex
     */
    public void addAsParentOf(Vertex futureParentVertex, Vertex ofVertex) {
        if (!m_nodes.contains(futureParentVertex)) {
            m_nodes.add(futureParentVertex);
        }
        if (!m_nodes.contains(ofVertex)) {
            m_nodes.add(ofVertex);
        }

        ofVertex.getParentList().add(futureParentVertex);
        futureParentVertex.getChildList().add(ofVertex);


    }

    /**
     * Добавляет узел futureChildVertex как потомка для ofVertex
     */
    public void addAsChildOf(Vertex futureChildVertex, Vertex ofVertex) {
        if (!m_nodes.contains(futureChildVertex)) {
            m_nodes.add(futureChildVertex);
        }

        ofVertex.getChildList().add(futureChildVertex);
        futureChildVertex.getParentList().add(ofVertex);

    }

    public Vertex getVertexByVirginName(String virName) {
        Iterator it = m_nodes.iterator();
        Vertex result = null;
        Vertex vx = null;

        while (it.hasNext()) {
            vx = (Vertex) it.next();
            if (vx.getVirginName().equals(virName)) {
                result = vx;
                break;
            }
        }
        return result;
    }

    /*
     * Взять вершину с именем name и типом символа typeSym
     */
    public Vertex getVertexByParams(String name, String typeSim) {
        Iterator it = m_nodes.iterator();
        Vertex result = null;
        Vertex vx = null;

        while (it.hasNext()) {
            vx = (Vertex) it.next();
            if (vx.getTypeOfSymbol().equals(typeSim) && vx.getAttribute("NAME").equals(name)) {
                result = vx;
                break;
            }
        }
        return result;
    }

    public Vertex findRoot() {
        Vertex result = null;
        if (m_root == null) {
            Iterator it = this.m_nodes.iterator();
            Vertex vx = null;

            while (it.hasNext()) {
                vx = (Vertex) it.next();
                if (vx.getParentList().isEmpty()) {
                    result = vx;
                    break;
                }
            }
        } else {
            result = m_root;
        }
        return result;


    }

    public Iterator getIterator() {
        return m_nodes.iterator();
    }

    /*
     * Возвращает список значений атрибутов с именем whatAtt у детей vx-a
     */
    public ArrayList<Object> getAllAttValuesFromChilds(Vertex vx, String whatAtt) {
        ArrayList<Object> result = new ArrayList();
        Iterator it = vx.getChildList().iterator();
        while (it.hasNext()) {
            Vertex vxNow = (Vertex) it.next();

            result.add(vxNow.getAttribute(whatAtt));
        }

        return result;
    }

    public Vertex getCloserParentByName(Vertex in_vx, String parentName, Vertex result) {

        Iterator it = in_vx.getParentList().iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            if (vx.getAttribute("NAME").equals(parentName)) {
                result = vx;
            } else {
                result = getCloserParentByName(vx, parentName, result);
            }
        }
        return result;
    }

    public Vertex getRoot() {
        if (m_root == null) {
            m_root = findRoot();
        }

        return m_root;
    }

    public void printInfo() {
        Iterator it = this.m_nodes.iterator();
        while (it.hasNext()) {
            ((Vertex) it.next()).printVertex();
            System.out.print("\n");
        }
    }

    /*
     * Получить вершину по её имени в атрибутах
     */
    public Vertex getByVertexName(String str_to_find) {

        Iterator it = this.getIterator();
        Vertex result = null;
        Vertex vx = null;


        while (it.hasNext()) {
            vx = (Vertex) it.next();
            if (vx.getAttribute("NAME").equals(str_to_find)) {
                result = vx;
                break;
            }
        }

        return result;


    }
}
