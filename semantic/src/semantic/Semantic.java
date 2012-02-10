package semantic;

import java.io.*;
import java.util.ArrayList;

import java.util.regex.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.*;
import generation.*;

public class Semantic {

    public static String fileContent;
    public static Graph g;
    public static HashMap<String, String> locals;
    public static ArrayList<String> consts;
    public static ArrayList<String> ids;
    public static ArrayList<String> types;
    public static int vertexId = 0;
    public static Vertex root;
    public static ArrayList<Vertex> procs;
    public static ArrayList<Vertex> funcs;
    public static HashMap<String, Integer> locations;
    public static HashMap<String, ArrayList<Integer>> multipleLocations;
    public static String filename;
    public static String filenameKum;
    public static String filenameLoc;
    public static HashMap<String, String> decls;
    public static ConstantsTable constantsTable;
    public static LocalsTable localsTable;
    public static FPTable fpTable;
    public static boolean allRight = true;
    public static int m_currentClassID;
    public static int m_objectClassID;
    public static short m_initMethodRefID;
    // имя метода : список инструкций
    public static HashMap<String, ByteBuffer> bytecodeBuffer;
    public static int m_order = 0;
    public static int m_mainNameAndType;
    public static short m_mainClassID;
    /*
     * public static boolean m_isPrevIsType = false; public static String
     * m_prevType = "";
     */

    public static void readFile(String path) {
        fileContent = "";
        Scanner in = null;
        try {
            in = new Scanner(new File(path));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Semantic.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (in.hasNext()) {
            fileContent += in.nextLine() + "\r\n";
        }
        in.close();
    }

    public static void readLocFile(String path) {
        String fileContentLoc = "";
        Scanner in = null;
        try {
            in = new Scanner(new File(path), "windows-1251");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Semantic.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (in.hasNext()) {
            fileContentLoc += in.nextLine() + "\r\n";
        }
        in.close();

        locations = new HashMap();
        multipleLocations = new HashMap();
        String[] rows = fileContentLoc.split("\r\n");
        for (int i = 0; i < rows.length; i++) {
            String str1 = rows[i].split("\\x{1F}")[0];
            String str2 = rows[i].split("\\x{1F}")[1];
            str1 = str1.trim();
            str2 = str2.trim();
            boolean isNum = Semantic.isNumeric(str2);
            if (isNum) {
                if (str1.length() > 0) {

                    locations.put(str1, Integer.valueOf(str2));

                    if (multipleLocations.containsKey(str1)) {
                        ArrayList<Integer> int0 = multipleLocations.get(str1);
                        int0.add(Integer.valueOf(str2));
                        multipleLocations.put(str1, int0);
                    } else {
                        ArrayList<Integer> int0 = new ArrayList();
                        int0.add(Integer.valueOf(str2));
                        multipleLocations.put(str1, int0);
                    }
                }
            }
        }
    }

    public static boolean isOperation(String str) {
        if (str.contains("+") || str.contains("-")
                || str.contains("*") || str.contains("/")
                || str.contains("**") || str.equals("UnaryMinus")
                || str.contains("<") || str.equals(">")
                || str.contains("=") || str.equals("<>")
                || str.contains(":=") || str.equals(">=")
                || str.equals("<=") || str.equals("()")) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean inChildsExistsAttName(Vertex in_vx, String att_name) {
        Iterator it = in_vx.getChildList().iterator();

        boolean result = false;

        if (att_name == null) {
            return result;
        }

        Vertex vx = null;


        while (it.hasNext()) {
            vx = (Vertex) it.next();
            if (vx.getAttribute("NAME").equals(att_name)) {
                result = true;
                break;
            }
            result = inChildsExistsAttName(vx, att_name);
            if (result == true) {
                break;
            }
        }



        return result;

    }

    public static String inChildsExistsAttType(Vertex in_vx) {
        Iterator it = in_vx.getChildList().iterator();

        String result = "";



        Vertex vx = null;


        while (it.hasNext()) {
            vx = (Vertex) it.next();
            if (vx.containsAttribute("TYPE")) {
                result = vx.getAttribute("TYPE");
                break;
            }
            result = inChildsExistsAttType(vx);
            if (result.length() > 0) {
                break;
            }
        }



        return result;

    }

    public static Vertex getChildByAttName(Vertex in_vx, String att_name) {
        Iterator it = in_vx.getChildList().iterator();

        Vertex result = null;

        if (att_name == null) {
            return result;
        }

        Vertex vx = null;


        while (it.hasNext()) {
            vx = (Vertex) it.next();
            if (vx.getAttribute("NAME").equals(att_name)) {
                result = vx;
                break;
            }
            result = getChildByAttName(vx, att_name);
            if (result != null) {
                break;
            }
        }



        return result;

    }

    public static Vertex createVertex(String str_in) {

        Vertex result = new Vertex(str_in);
        int index;
        String name, id, type = null;
        String describe;
        String vertexName = str_in;
        boolean hasType = false;
        boolean isInheritesType = false;

        if (str_in.contains("!")) {// Если встретила не константа
            index = str_in.indexOf("!");
            name = str_in.substring(0, index);
            id = str_in.substring(index + 1, str_in.length());
            if (isOperation(name)) {
                type = "OPERATION";
                hasType = true;
                isInheritesType = true;
            } else {
                type = "SIMPLE";
            }
        } else {
            int tempInt = str_in.indexOf(":");
            describe = str_in.substring(0, tempInt);
            if ("Идентификатор".equals(describe)) {
                String value;
                value = str_in.substring(tempInt + 1, str_in.length());
                value = value.trim();
                if (!ids.contains(value)) {
                    ids.add(value);
                }
                /*
                 * if (m_isPrevIsType == true){ result.addAttribute("TYPE",
                 * m_prevType); }
                 */
                name = value;
                id = "";
                type = "ID";
                hasType = true;
                isInheritesType = true;
                /*
                 * m_isPrevIsType = false; m_prevType = null;
                 */
            } else if ("Тип".equals(describe)) {
                String value;
                value = str_in.substring(tempInt + 1, str_in.length());
                value = value.trim();
                if (!types.contains(value)) {
                    types.add(value);
                }
                name = value;
                id = "";
                type = "TYPE";
                /*
                 * m_isPrevIsType = true; m_prevType = name;
                 */
            } else {
                String value;
                value = str_in.substring(tempInt + 2, str_in.length());
                //
                if (!consts.contains(value)) {
                    consts.add(value);
                }
                if (describe.equals("Целая константа") || describe.equals("Целое число")) {
                    result.addAttribute("TYPE", "цел");
                    value = value.trim();
                } else if (describe.equals("Вещественная константа")) {
                    result.addAttribute("TYPE", "вещ");
                    value = value.trim();
                } else if (describe.equals("Логическая константа")) {
                    result.addAttribute("TYPE", "лог");
                } else if (describe.equals("Строковая константа")) {
                    result.addAttribute("TYPE", "лит");
                } else if (describe.equals("Символьная константа")) {
                    result.addAttribute("TYPE", "сим");
                }

                // проверка - если данная строка - нс, то обрезать лишние знаки
                // по бокам
                String testNS = value.replaceAll("нс", " ");
                boolean testNSBool = true;
                for (int i = 0; i < testNS.length(); i++) {
                    if (testNS.charAt(i) != ' ') {
                        testNSBool = false;
                    }
                }
                if (testNSBool) {
                    value = value.trim();
                }

                //result.addAttribute("NAME", value);
                //result.addAttribute("NAME", name);
                name = value;
                id = String.valueOf(ids.indexOf(value));
                type = "SIMPLE";
                hasType = true;
                /*
                 * m_isPrevIsType = false; m_prevType = null;
                 */
            }
        }



        // System.out.printf("Create vertex: %s\n", str_in);
        result.addAttribute("NAME", name);
        result.addAttribute("ORDER", String.valueOf(m_order++));


        if (!id.isEmpty()) {
            result.addAttribute("ID", id);
        }

        result.setTypeOfSymbol(type);
        result.setVirginName(vertexName);
        result.setHasType(hasType);
        result.setIsInheritesType(isInheritesType);

        return result;
    }

    public static void createGraph() {
        /*
         * Обрежем объявление графа
         */

        ArrayList<Integer> tempList = new ArrayList();
        tempList.add(Integer.valueOf(1));

        ConstantsTableRow firstRow =
                new ConstantsTableRow(
                tempList,
                "UTF-8",
                "Code");

        constantsTable.addRow(firstRow);

        int i1, i2;
        i1 = fileContent.indexOf("{");
        i2 = fileContent.indexOf("}");
        fileContent = fileContent.substring(i1 + 3, i2 - 3);

        String[] rows = fileContent.split(";");


        for (int i = 0; i
                < rows.length; i++) {
            Vertex v1;
            Vertex v2;

            String str1 = rows[i].split(" -> ")[0];
            String str2 = rows[i].split(" -> ")[1];
            str1 = str1.trim();
            str2 = str2.trim();
            str1 = str1.substring(1, str1.length() - 1);
            str2 = str2.substring(1, str2.length() - 1);

            Vertex temp = g.getVertexByVirginName(str1);

            if (temp == null) {
                v1 = createVertex(str1);
            } else {
                v1 = temp;
            }

            temp = g.getVertexByVirginName(str2);

            if (temp == null) {
                v2 = createVertex(str2);
            } else {
                v2 = temp;
            }

            g.addAsParentOf(v1, v2);


        }




        // System.out.printf(fileContent);
    }

    public static Vertex getByVertexName(Vertex start_vx, String str_to_find) {

        Iterator it = start_vx.getChildList().iterator();
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

    public static Vertex findPureNameInChilds(Vertex in_vx, String str_to_find) {
        Iterator it = in_vx.getChildList().iterator();
        Vertex result = null;
        Vertex vx = null;


        while (it.hasNext()) {
            vx = (Vertex) it.next();


            if (vx.getAttribute("NAME").equals(str_to_find)) {
                result = vx;

                break;


            }
            result = findPureNameInChilds(vx, str_to_find);


        }

        return result;


    }

    public static ArrayList<Vertex> getAllEndings() {
        ArrayList<Vertex> result = new ArrayList();

        Iterator it = g.getIterator();

        Vertex vx = null;


        while (it.hasNext()) {

            vx = (Vertex) it.next();

            if (vx.getChildList().isEmpty()) {
                result.add(vx);
            }
        }

        return result;


    }

    public static ArrayList<Vertex> getAllCreateIdents() {
        ArrayList<Vertex> result = new ArrayList();

        Iterator it = g.getIterator();

        Vertex vx = null;


        while (it.hasNext()) {

            vx = (Vertex) it.next();

            if (vx.getAttribute("NAME").equals("create_ident")) {
                result.add(vx);
            }
        }

        return result;


    }

    public static ArrayList<Vertex> getAllFunctionCalls() {
        ArrayList<Vertex> result = new ArrayList();

        Iterator it = g.getIterator();

        Vertex vx = null;


        while (it.hasNext()) {

            vx = (Vertex) it.next();

            if (vx.getAttribute("NAME").equals("create_function_call_expr")) {
                result.add(vx);
            }
        }

        return result;


    }

    public static void semanticWork_isReturnExistsF(Vertex vx) {
        boolean isExits = false;
        isExits = inChildsExistsAttName(vx, "create_stmt_znach");

        if (!isExits) {
            Vertex vx_out = getByVertexName(vx, "create_ident");
            String get = vx_out.getChildList().get(0).getAttribute("NAME");
            String mesg = String.format("Отсутствует ЗНАЧ в функции <%s>", get);
            String errorMesg = makeErrorMessage(filename, -1, mesg);

            System.err.printf(errorMesg);


        }
    }

    public static void semanticWork_isReturnExistsP(Vertex vx) {
        boolean isExits = false;
        isExits = inChildsExistsAttName(vx, "create_stmt_znach");

        if (isExits) {

            Vertex znach_value = getChildByAttName(vx, "create_znachvalue");
            Vertex vx_out = getByVertexName(vx, "create_ident");

            String erMess = "Ошибка C0001:  Процедура не может возвращать значения!"
                    + " Слово \"знач\" - лишнее.";
            String messOut = makeErrorMessage(filenameKum, locations.get(znach_value.getVirginName()),
                    erMess);
            System.err.print(messOut);
            allRight = false;

        }
    }

    public static String makeErrorMessage(String file, Integer pos, String message) {
        String result = null;

        result = String.format("<%s:%d> | %s \n", file, pos.intValue(), message);



        return result;
    }

    public static String makeErrorMessage(String file, int pos, String message) {
        String result = null;

        result = String.format("<%s:%d> | %s \n", file, pos, message);



        return result;
    }

    public static ArrayList<Vertex> semanticWork_getFunctions(Vertex in_vx, ArrayList<Vertex> list) {

        Iterator it = in_vx.getChildList().iterator();

        Vertex vx = null;
        while (it.hasNext()) {
            vx = (Vertex) it.next();
            if (vx.getAttribute("NAME").equals("create_func")) {
                list.add(vx);
            }
            semanticWork_getFunctions(vx, list);
        }
        return list;
    }

    public static ArrayList<Vertex> semanticWork_getProcs(Vertex in_vx, ArrayList<Vertex> list) {


        Iterator it = in_vx.getChildList().iterator();

        Vertex vx = null;



        while (it.hasNext()) {
            vx = (Vertex) it.next();


            if (vx.getAttribute("NAME").equals("create_proc")) {
                list.add(vx);


            }
            semanticWork_getProcs(vx, list);


        }

        return list;


    }

    /*
     * Узнает в какой процедуре или функции находится вершина
     */
    public static Vertex whichBlock(Vertex in_vx) {
        Vertex result = null;
        if (in_vx != null) {
            Iterator it = procs.iterator();
            while (it.hasNext()) {
                Vertex vx = (Vertex) it.next();




            }
        }
        return result;
    }

    public static void findPureNameInChilds2List(Vertex in_vx, String str_to_find, ArrayList<Vertex> list) {
        Iterator it = in_vx.getChildList().iterator();
        Vertex result = null;
        Vertex vx = null;

        while (it.hasNext()) {
            vx = (Vertex) it.next();

            if (vx.getAttribute("NAME").equals(str_to_find)) {
                result = vx;
                list.add(vx);

            }
            findPureNameInChilds2List(vx, str_to_find, list);


        }


    }

    public static void findTypeInChilds2List(Vertex in_vx, String str_to_find, ArrayList<Vertex> list) {
        Iterator it = in_vx.getChildList().iterator();
        Vertex result = null;
        Vertex vx = null;

        while (it.hasNext()) {
            vx = (Vertex) it.next();

            if (vx.getAttribute("TYPE") == null) {
            } else if (vx.getAttribute("TYPE").equals(str_to_find)) {
                result = vx;
                list.add(vx);

            }
            findPureNameInChilds2List(vx, str_to_find, list);


        }


    }

    public static HashMap<String, String> semanticWork_acquireParamsFP(Vertex in_vx) {


        HashMap<String, String> result = new HashMap();
        Vertex startVx = null;
        Iterator it0 = in_vx.getChildList().iterator();
        while (it0.hasNext()) {
            Vertex vxNow = (Vertex) it0.next();
            if (vxNow.getAttribute("NAME").equals("create_param_list")) {
                startVx = vxNow.getChildByOrder(0).getChildByOrder(0);
                break;
            }

        }
        String found_type = "";
        String found_name = "";
        if (startVx != null) {
            Iterator it = startVx.getChildList().iterator();
            String str = "";
            String prefixType = "";
            while (it.hasNext()) {
                Vertex _vxNow = (Vertex) it.next();

                str = _vxNow.getAttribute("NAME");

                if (str.equals("create_array_type")) {
                    prefixType = _vxNow.getChildByOrder(0).getAttribute("NAME");
                } else if (str.equals("create_enum_array_identifier_list")) {
                    for (Vertex _v0 : _vxNow.getChildList()) {
                        String str0 = _v0.getAttribute("NAME");
                        if (str0.equals("create_int_int_dim")) {
                            prefixType += getDimensions(_v0);
                        } else if (str0.equals("create_int_id_dim")) {
                            prefixType += getDimensions(_v0);
                        } else if (str0.equals("create_ident")) {
                            found_name = _v0.getChildByOrder(0).getAttribute("NAME");
                        } else if (str0.equals("append_enum_array_identifier_list")) {
                            result.put(found_name, prefixType);
                            found_name = "";
                            prefixType = "";
                            for (Vertex _v1 : _v0.getChildList()) {
                                String str1 = _v1.getAttribute("NAME");
                                if (str1.equals("create_int_int_dim")) {
                                    prefixType += getDimensions(_v1);
                                } else if (str1.equals("create_int_id_dim")) {
                                    prefixType += getDimensions(_v1);
                                } else if (str1.equals("create_ident")) {
                                    found_name = _v1.getChildByOrder(0).getAttribute("NAME");
                                }

                            }
                            result.put(found_name, prefixType);
                            found_name = "";
                            prefixType = "";
                        }
                    }
                }
                if (found_name.isEmpty() != true && prefixType.isEmpty() != true) {
                    result.put(found_name, prefixType);
                }
            }
        }



        return result;
    }

    /*
     * Получает объявления в Function или в Procedure
     */
    public static HashMap<String, String> semanticWork_acquireDeclsFP(Vertex in_vx) {

        /*
         * Атомарные объявления
         */
        ArrayList<Vertex> list = new ArrayList();
        findPureNameInChilds2List(in_vx, "create_from_atomic_decl", list);

        HashMap<String, String> tempLocals = new HashMap();

        Iterator it = list.iterator();
        while (it.hasNext()) {
            Vertex vxNow = (Vertex) it.next();

            String found_type;


            vxNow = getByVertexName(vxNow, "create_atomic_type");
            found_type = vxNow.getChildList().get(0).getAttribute("NAME");

            vxNow = vxNow.getParentList().get(0);
            vxNow = getByVertexName(vxNow, "create_enum_atomic_identifier_list");
            Iterator it2 = vxNow.getChildList().iterator();

            vxNow.addAttribute("TYPE", found_type);

            while (it2.hasNext()) {
                Vertex vert = (Vertex) it2.next();
                String tempStr = vert.getAttribute("NAME");


                if (tempStr.equals("create_ident")) {
                    tempLocals.put(vert.getChildList().get(0).getAttribute("NAME"),
                            found_type);
                    vert.getChildList().get(0).addAttribute("TYPE", found_type);

                } else if (tempStr.equals("append_enum_atomic_identifier_list")) {
                    tempLocals.put(vert.getChildList().get(0).getChildList().get(0).getAttribute("NAME"),
                            found_type);
                    vert.getChildList().get(0).addAttribute("TYPE", found_type);
                    vert.getChildList().get(0).getChildList().get(0).addAttribute("TYPE", found_type);

                }
            }

        }



        /*
         * Табличные объявления
         */
        list = new ArrayList();
        findPureNameInChilds2List(in_vx, "create_from_array_decl", list);

        it = list.iterator();
        while (it.hasNext()) {
            Vertex _vxNow = (Vertex) it.next();

            String found_type;


            Vertex vxNow = getByVertexName(_vxNow, "create_array_type");
            found_type = vxNow.getChildList().get(0).getAttribute("NAME");

            vxNow = vxNow.getParentList().get(0);
            vxNow = getByVertexName(vxNow, "create_enum_array_identifier_list");
            Iterator it2 = vxNow.getChildList().iterator();

            vxNow.addAttribute("TYPE", found_type);
            _vxNow.addAttribute("TYPE", found_type);

            while (it2.hasNext()) {
                Vertex vert = (Vertex) it2.next();
                String tempStr = vert.getAttribute("NAME");
                switch (tempStr) {
                    case "create_int_int_dim": {
                        String _decls = getDimensions(vert);
                        found_type += _decls;
                        tempLocals.put(vert.getParentList().get(0).getChildByOrder(0).getChildByOrder(0).getAttribute("NAME"),
                                found_type);
                        vert.getChildList().get(0).addAttribute("TYPE", found_type);
                        break;
                    }
                    case "create_int_id_dim": {
                        String _decls = getDimensions(vert);
                        found_type += _decls;
                        tempLocals.put(vert.getChildList().get(0).getAttribute("NAME"),
                                found_type);
                        vert.getChildList().get(0).addAttribute("TYPE", found_type);
                        break;
                    }
                    case "create_ident":
                        tempLocals.put(vert.getChildList().get(0).getAttribute("NAME"),
                                found_type);
                        vert.getChildList().get(0).addAttribute("TYPE", found_type);
                        vert.addAttribute("TYPE", found_type);
                        break;
                    case "append_enum_array_identifier_list": {
                        Vertex _vx = vert.getChildList().get(0);
                        String _decls = getDimensions(vert.getChildList().get(1));
                        found_type += _decls;
                        tempLocals.put(_vx.getChildList().get(0).getAttribute("NAME"),
                                found_type);
                        _vx.addAttribute("TYPE", found_type);
                        _vx.getChildList().get(0).addAttribute("TYPE", found_type);
                        break;
                    }
                }
            }

        }



        return tempLocals;


    }

    /*
     * Возвращает измерения массива в виде "1:10,1:23" Функция принимает узел
     * create_int_int_dim или create_int_id_dim или append_int_int_dim или
     * append_int_id_dim Выход в виде ([1:10, 1:d, 1:12] -> [10[d[12)
     */
    public static String getDimensions(Vertex vx) {
        String result = "";


        Iterator it = vx.getChildList().iterator();
        while (it.hasNext()) {
            Vertex vxNow = (Vertex) it.next();
            String nowName = vxNow.getAttribute("NAME");
            String typeOfSymbol = vxNow.getTypeOfSymbol();
            String type = vxNow.getAttribute("TYPE");


            if (nowName.equals("append_int_id_dim")) {
                String name = vxNow.getChildByOrder(1).getAttribute("NAME");
                result += "[" + name;

            } else if (nowName.equals("append_int_int_dim")) {
                String name = vxNow.getChildByOrder(1).getAttribute("NAME");
                result += "[" + name;

            } else if (typeOfSymbol.equals("SIMPLE") && !nowName.equals("1")) {
                result += "[" + nowName;
            }


        }

        return result;
    }

    public static String getTypeOfIdentifier(Vertex in_vx) {
        String result = null;


        Iterator it = in_vx.getParentList().iterator();
        while (it.hasNext()) {

            Vertex vxNow = (Vertex) it.next();
            if (vxNow.getAttribute("NAME").equals("create_rez") || vxNow.getAttribute("NAME").equals("create_arg")) {
                result = vxNow.getChildList().get(0).getChildList().get(0).getAttribute("NAME");
                break;
            }
            result = getTypeOfIdentifier(vxNow);
        }


        return result;
    }

    public static String getTypeOfIdentifierByName(String name) {
        String result = null;

        return result;
    }

    public static ArrayList<Vertex> semanticWork_acquireIdents(Vertex in_vx, ArrayList<Vertex> list) {
        Iterator it = in_vx.getChildList().iterator();
// Vertex vx1 = g.getVertexByVirginName(virginName);
        Vertex vx = null;

        while (it.hasNext()) {
            vx = (Vertex) it.next();
            if (vx.getAttribute("NAME").equals("create_ident")) {
                list.add(vx);
            }
            semanticWork_acquireIdents(vx, list);


        }

        return list;
    }

    // получить все узлы с данным именем начиная с in_vx и вниз
    public static ArrayList<Vertex> semanticWork_acquire(String acquireWhat, Vertex in_vx, ArrayList<Vertex> list) {
        Iterator it = in_vx.getChildList().iterator();

        Vertex vx = null;

        while (it.hasNext()) {
            vx = (Vertex) it.next();
            if (vx.getAttribute("NAME").equals(acquireWhat)) {
                list.add(vx);
            }
            semanticWork_acquire(acquireWhat, vx, list);


        }

        return list;
    }

    public static ArrayList<Vertex> semanticWork_checkUndeclaredVariable(Vertex in_vx) {

        ArrayList<Vertex> tempList = new ArrayList();

        ArrayList<Vertex> listDecls = new ArrayList();
        ArrayList<Vertex> listIdents = new ArrayList();

        // хэш - имя=>тип
        HashMap<String, String> hash = new HashMap();
        hash = semanticWork_acquireDeclsFP(in_vx);
        listIdents = semanticWork_acquireIdents(in_vx, listIdents);

        Iterator it = listIdents.iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            String var_name = vx.getChildList().get(0).getAttribute("NAME");
            String var_id = vx.getChildList().get(0).getAttribute("ID");

            String tmpType = getTypeOfIdentifier(vx);
            String tmptmp = vx.getVirginName();
            String tmp1;
            if (tmpType != null) {
                tmp1 = vx.getChildList().get(0).getAttribute("NAME");
                hash.put(tmp1, tmpType);
            }
            if (fpTable.isIdentifierExists(var_name) != true
                    && vx.getParentList().get(0).getAttribute("NAME").equals("create_enum_atomic_identifier_list") != true
                    && vx.getParentList().get(0).getAttribute("NAME").equals("append_enum_atomic_identifier_list") != true) {
                if (hash.containsKey(var_name)) {// Переменная объявлена, теперь узнаем до использования ли
                } else {
                    tempList.add(vx.getChildList().get(0));
                }
            }
        }






        return tempList;
    }

    public static Integer location_after(String in_what, String in_afterWhat) {
        Integer result = null;
        ArrayList<Integer> twhat = multipleLocations.get(in_what);
        ArrayList<Integer> temp = new ArrayList();
        ArrayList<Integer> tafterWhat = multipleLocations.get(in_afterWhat);
        Integer aft = locations.get(in_afterWhat);
        Iterator it = twhat.iterator();
        while (it.hasNext()) {
            Integer inte = (Integer) it.next();
            if (inte.intValue() >= aft.intValue()) {
                result = inte;
                break;
            }
        }



        return result;
    }

    public static void semanticWork_checkFP() {
        ArrayList<Vertex> list = new ArrayList();
        ArrayList<Vertex> list2 = new ArrayList();
        ArrayList<Vertex> funcs = semanticWork_getFunctions(root, list);

        boolean isFail = false;
        ArrayList<Vertex> procs = semanticWork_getProcs(root, list2);


        Iterator it = funcs.iterator();
        while (it.hasNext()) {
            Vertex vxNow = (Vertex) it.next();
            semanticWork_isReturnExistsF(vxNow);
            ArrayList<Vertex> listVars1 = semanticWork_checkUndeclaredVariable(vxNow);
            if (listVars1.size() > 0) {

                Iterator it2 = listVars1.iterator();
                while (it2.hasNext()) {
                    Vertex vxNow2 = (Vertex) it2.next();
                    String str = vxNow2.getAttribute("NAME");
                    if (inChildsExistsAttName(vxNow, str) == false) {
                        Vertex temp = findPureNameInChilds(vxNow, "create_ident");

                        String errMess = String.format("Необъявленная переменная <%s> в теле функции!"
                                + " Ошибка в функции <%s>", vxNow2.getAttribute("NAME"), temp.getChildList().get(0).getAttribute("NAME"));


                        Integer inte = location_after(vxNow2.getParentList().get(0).getVirginName(), temp.getChildList().get(0).getAttribute("NAME"));
                        if (inte == null) {
                            inte = locations.get(vxNow2.getParentList().get(0).getVirginName());
                        }
                        errMess = makeErrorMessage(filename, inte, errMess);

                        System.err.print(errMess);
                        allRight = false;

                    }
                }


            }
            /*
             * HashMap<String, String> ar = semanticWork_acquireDeclsFP(vxNow);
             * localsTable.add(vxNow, ar);
             */
        }

        it = procs.iterator();
        while (it.hasNext()) {
            Vertex vxNow = (Vertex) it.next();
            semanticWork_isReturnExistsP(vxNow);
            ArrayList<Vertex> listVars2 = semanticWork_checkUndeclaredVariable(vxNow);
            if (listVars2.size() > 0) {
                Vertex temp = findPureNameInChilds(vxNow, "create_ident");
                System.err.printf("Error: необъявленные переменные в теле процедуры!"
                        + " Ошибка в процедуре <%s>\n", temp.getChildList().get(0).getAttribute("NAME"));
                allRight = false;
            }
            /*
             * HashMap<String, String> ar = semanticWork_acquireDeclsFP(vxNow);
             * localsTable.put(vxNow, ar);
             */
        }


    }

    /*
     * public static ArrayList<String> typesOfNext(){ ArrayList<String> list =
     * new ArrayList<String>(); }
     */

    /*
     * Находит верхушки выражений
     */
    public static ArrayList<Vertex> semanticWork_findExprs(Vertex in_vx, ArrayList<Vertex> list) {

        Iterator it = in_vx.getChildList().iterator();

        Vertex vx = null;

        while (it.hasNext()) {
            vx = (Vertex) it.next();
            if (isOperation(vx.getAttribute("NAME"))) {
                list.add(vx);

            }
            semanticWork_findExprs(vx, list);
        }

        return list;

    }

    public static String markAllNodesByType(Vertex rec_vertex) {

        String result;

        String str = null;

        rec_vertex.inheriteType();
        Iterator it = rec_vertex.getChildList().iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();

            str = markAllNodesByType(vx);
        }

        if (str == null) {
            result = rec_vertex.getAttribute("TYPE");
        } else {
            result = str;
        }
        return result;

    }

    public static void typeCastStartsWith(Vertex in_vx) {

        int size = in_vx.getChildList().size();
        for (int i = 0; i < size - 1; i++) {
        }

    }

    /*
     * Присваивает каждому идентификатору атрибут типа
     */
    public static void semanticWorks_attributeIdentByType() {
        HashMap<String, String> decls = null;
        if (procs != null && funcs != null) {
            /*
             * Проходим все функции
             */
            Iterator it = funcs.iterator();
            while (it.hasNext()) {
                Vertex vxNow = (Vertex) it.next();
                decls = semanticWork_acquireDeclsFP(vxNow);
                Iterator it2 = decls.keySet().iterator();
                while (it2.hasNext()) {// Отбираем идентификатор с таким именем 
                    String curStr = (String) it2.next();
                    Vertex idVx = g.getVertexByParams(curStr, "ID");
                    if (idVx != null) {
                        idVx.setHasType(true);
                        idVx.setIsInheritesType(true);
                        idVx.addAttribute("TYPE", decls.get(curStr));
                    }
                }
            }
            /*
             * Проходим все процедуры
             */
            it = procs.iterator();
            while (it.hasNext()) {
                Vertex vxNow = (Vertex) it.next();
                decls = semanticWork_acquireDeclsFP(vxNow);
                Iterator it2 = decls.keySet().iterator();
                while (it2.hasNext()) {// Отбираем идентификатор с таким именем 
                    String curStr = (String) it2.next();
                    Vertex idVx = g.getVertexByParams(curStr, "ID");
                    if (idVx != null) {
                        idVx.setHasType(true);
                        idVx.setIsInheritesType(true);
                        idVx.addAttribute("TYPE", decls.get(curStr));

                    }
                }
            }
        }
        /*
         * Хэшмеп - Имя:Тип
         */

    }

    public static void semanticWorks_attributeFPByType() {
    }

    public static void _attributeSymbolsByType(Vertex in_vx) {
        Vertex vxPar = null;
        Iterator it = in_vx.getParentList().iterator();

        while (it.hasNext()) {

            vxPar = (Vertex) it.next();
            if (vxPar.getTypeOfSymbol().equals("OPERATION")) {
                if (in_vx.haveSameParent().size() > 0) {//бинарная или тернарная
                    ArrayList<Vertex> sameParentList = in_vx.haveSameParent();

                    if (vxPar.getHasType() && vxPar.getIsInheritesType()) {
                        String newType = in_vx.castTypeList(sameParentList);
                        vxPar.addAttribute("TYPE", newType);
                    } else {
                        break;
                    }

                    _attributeSymbolsByType(vxPar);
                } else {// унарная операция
                    String newType = in_vx.getAttribute("TYPE");
                    vxPar.addAttribute("TYPE", newType);
                }
            }
        }

        // _attributeSymbolsByType()
    }

    public static void semanticWorks_attributeSymbolsByType() {
        ArrayList<Vertex> endings = getAllEndings();
        Iterator it = endings.iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            _attributeSymbolsByType(vx);

        }



    }

    public static void provideTypeFor_create_array_expr() {

        Iterator it = funcs.iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();

        }

        it = procs.iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();

        }
    }

    public static void semanticWorks_findFP() {
        funcs = new ArrayList();
        semanticWork_getFunctions(g.getRoot(), funcs);
        procs = new ArrayList();
        semanticWork_getProcs(g.getRoot(), procs);
    }

    public static void semanticWorks_checkIntegerIndexes() {
        // получить все масссивы точнее create_array_expr
        // если типы child!=цел, то ошибка
        ArrayList<Vertex> list = new ArrayList();
        semanticWork_acquire("create_array_expr", g.getRoot(), list);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Vertex vxNow = (Vertex) it.next();
            ArrayList<Object> listTypes;
            Iterator childIter = vxNow.getChildList().iterator();
            while (childIter.hasNext()) {
                Vertex vxCh = (Vertex) childIter.next();
                String _str = vxCh.getAttribute("NAME");
                if (vxCh.getAttribute("TYPE") == null) {
                    while (vxCh.getChildList().isEmpty()!=true) {
                        if (vxCh.getAttribute("TYPE") == null) {
                            vxCh = vxCh.getChildList().get(0);
                        }
                    }
                }
                String _strAfter = vxCh.getAttribute("NAME");
           /*     if (vxCh.getAttribute("TYPE").equals("цел") == false
                        && vxCh.getAttribute("NAME").equals("create_expr_list"))*/
                if (constantsTable.getRowByTypeAndName("String", _strAfter) != null &&
                        constantsTable.getRowByTypeAndName("INT", _strAfter) == null){
                    Integer loc = locations.get(vxNow.getVirginName());
                    String erMess = "Индексы массива должны быть целыми числами!";
                    Vertex result = new Vertex();
                    g.getCloserParentByName(vxNow, "create_proc", result);
                    if ("".equals(result.getVirginName())) {
                        g.getCloserParentByName(vxNow, "create_func", result);
                    }
                    String messOut = makeErrorMessage(filenameKum, loc, erMess);
                    System.err.print(messOut);
                    allRight = false;
                }
            }

        }

    }

    public static HashMap<String, String> getParameterHash(Vertex in_vx) {
        HashMap<String, String> result = new HashMap();
        String str = null;
        ArrayList<Vertex> list2 = new ArrayList();
        findPureNameInChilds2List(in_vx, "create_arg", list2);
        Iterator it = list2.iterator();
        int countInArgRez = 0;
        String curName = "";
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            if (vx.getChildList().get(1).getAttribute("NAME").equals("create_enum_array_identifier_list")) {
                // особый режим поиска в массиве
                str = vx.getChildList().get(0).getChildList().get(0).getAttribute("NAME");
                boolean flag = true;
                String dimHere = "";
                for (Vertex _v1 : vx.getChildList().get(1).getChildList()) {

                    String _name = _v1.getAttribute("NAME");
                    if (_name.equals("append_enum_array_identifier_list")) {
                        for (Vertex _v2 : _v1.getChildList()) {
                            String _name2 = _v2.getAttribute("NAME");
                            if (_name2.equals("create_int_int_dim")) {
                                String dimensions = getDimensions(_v2);
                                result.put(curName, str + dimensions);
                            } else if (_name2.equals("create_ident")) {
                                curName = _v2.getChildByOrder(0).getAttribute("NAME");
                            }

                        }

                    } else if (_name.equals("create_int_int_dim")) {
                        String dimensions = getDimensions(_v1);

                        result.put(curName, str + dimensions);
                    } else if (_name.equals("create_ident")) {
                        curName = _v1.getChildByOrder(0).getAttribute("NAME");
                    }
                }

            } else {
                countInArgRez = vx.getChildList().get(1).getChildList().size();
                str = vx.getChildList().get(0).getChildList().get(0).getAttribute("NAME");
                curName = vx.getChildByOrder(1).getChildByOrder(0).getChildByOrder(0).getAttribute("NAME");
                for (int i = 0; i < countInArgRez; i++) {
                    result.put(curName, str);
                }
            }
        }



        return result;

    }

    public static ArrayList<String> getParameterList(Vertex in_vx) {
        ArrayList<String> result = new ArrayList();
        String str = null;
        ArrayList<Vertex> list2 = new ArrayList();
        findPureNameInChilds2List(in_vx, "create_arg", list2);
        Iterator it = list2.iterator();
        int countInArgRez = 0;
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            if (vx.getChildList().get(1).getAttribute("NAME").equals("create_enum_array_identifier_list")) {
                // особый режим поиска в массиве
                str = vx.getChildList().get(0).getChildList().get(0).getAttribute("NAME");
                boolean flag = true;
                String dimHere = "";
                for (Vertex _v1 : vx.getChildList().get(1).getChildList()) {

                    String _name = _v1.getAttribute("NAME");
                    if (_name.equals("append_enum_array_identifier_list")) {
                        for (Vertex _v2 : _v1.getChildList()) {
                            String _name2 = _v2.getAttribute("NAME");
                            if (_name2.equals("create_int_int_dim")) {
                                String dimensions = getDimensions(_v2);
                                result.add(str + dimensions);
                            }
                        }

                    } else if (_name.equals("create_int_int_dim")) {
                        String dimensions = getDimensions(_v1);

                        result.add(str + dimensions);
                    }
                }

            } else {
                countInArgRez = vx.getChildList().get(1).getChildList().size();
                str = vx.getChildList().get(0).getChildList().get(0).getAttribute("NAME");
                for (int i = 0; i < countInArgRez; i++) {
                    result.add(str);
                }
            }
        }


        return result;

    }

    public static int findConstTableID(Vertex vx) {
        return 0;
    }

    public static String getRetVal(Vertex vx) {
        String result = null;

        result = findPureNameInChilds(vx, "create_atomic_type").getChildList().get(0).getAttribute("NAME");

        if (result.equals("цел") == true) {
            result = "I";
        } else if (result.equals("сим")) {
            result = "C";
        } else if (result.equals("лог")) {
            result = "Z";
        } else if (result.equals("лит")) {
            result = "Ljava/lang/String;";
        } else if (result.equals("вещ")) {
            result = "D";
        } else {
            result = "V";
        }

        return result;
    }

    public static void fillFPTable() {
        int countPar = 0;
        ArrayList<String> plist = new ArrayList();
        String retVal = null;
        FPTableRow fprow = null;
        Iterator it = procs.iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();


            int constsTableID = findConstTableID(vx);
            String name = findPureNameInChilds(vx, "create_ident").getChildList().get(0).getAttribute("NAME");
            // constsTableID = constantsTable.getRowByName(name).getID();

            /*
             * Заполняем NameAndType - сначала Name, кладем, потом дескриптор,
             * кладем, потом NameAndType,кладем и так получаем ID NameAndType в
             * таблицу функций
             */
            ConstantsTableRow CTRowName = new ConstantsTableRow(new ArrayList(), "UTF-8", name);
            constantsTable.addRow(CTRowName);

            plist = getParameterList(vx);

            String desc = FPTableRow.makeDescriptor(plist, "V");
            ConstantsTableRow CTRowType = new ConstantsTableRow(new ArrayList(), "UTF-8", desc);
            constantsTable.addRow(CTRowType);
            String nameAndTypeContain = CTRowName.getID() + "," + CTRowType.getID();
            ConstantsTableRow CTRowNameAndType = new ConstantsTableRow(new ArrayList(), "NameAndType", nameAndTypeContain);
            constantsTable.addRow(CTRowNameAndType);
            constsTableID = CTRowNameAndType.getID();
            if (findPureNameInChilds(vx, "create_ident") == null) {
                countPar = 0;
                fprow = new FPTableRow(constsTableID, name, null, 0, null);
            } else {


                fprow = new FPTableRow(constsTableID, name, null, plist.size(), plist);

            }
            // Занесем имя функции в таблицу констант

            fpTable.add(fprow);

            plist = new ArrayList();

        }

        // функции
        countPar = 0;
        plist = new ArrayList();
        retVal = null;
        fprow = null;
        it = funcs.iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();


            int constsTableID = findConstTableID(vx);
            String name = findPureNameInChilds(vx, "create_ident").getChildList().get(0).getAttribute("NAME");
            // constsTableID = constantsTable.getRowByName(name).getID();

            /*
             * Заполняем NameAndType - сначала Name, кладем, потом дескриптор,
             * кладем, потом NameAndType,кладем и так получаем ID NameAndType в
             * таблицу функций
             */
            Integer inte = locations.get(name);

            ConstantsTableRow CTRowName = new ConstantsTableRow(inte.intValue(), "UTF-8", name);
            constantsTable.addRow(CTRowName);

            plist = getParameterList(vx);
            retVal = getRetVal(vx);

            String desc = FPTableRow.makeDescriptor(plist, retVal);
            ConstantsTableRow CTRowType = new ConstantsTableRow(inte.intValue(), "UTF-8", desc);
            constantsTable.addRow(CTRowType);
            String nameAndTypeContain = CTRowName.getID() + "," + CTRowType.getID();
            ConstantsTableRow CTRowNameAndType = new ConstantsTableRow(new ArrayList(), "NameAndType", nameAndTypeContain);
            constantsTable.addRow(CTRowNameAndType);
            constsTableID = CTRowNameAndType.getID();
            if (findPureNameInChilds(vx, "create_ident") == null) {
                countPar = 0;
                fprow = new FPTableRow(constsTableID, name, retVal, 0, null);
            } else {
                fprow = new FPTableRow(constsTableID, name, retVal, plist.size(), plist);

            }
            // Занесем имя функции в таблицу констант

            fpTable.add(fprow);

            plist = new ArrayList();

        }
    }

    public static ArrayList<Integer> getAllByPureNameInLocations(String in_str) {
        ArrayList<Integer> result = new ArrayList();

        Iterator it = locations.keySet().iterator();
        while (it.hasNext()) {
            String curStr = (String) it.next();
            if (curStr.indexOf(in_str) != -1) {
                result.add(locations.get(curStr));
            }

        }

        return result;
    }

    public static Vertex getFPByName(String str) {
        Vertex result = null;

        Iterator it = g.getIterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            if (vx.getAttribute("NAME").equals(str)) {
                result = vx;
                break;
            }
        }

        return result;
    }

    public static void fillLocalsTable() {
// Сделать для процедур
        ArrayList<Vertex> list = new ArrayList();
        decls = new HashMap();
        HashMap<String, String> params = new HashMap();
        // функции decls = new HashMap(); list = new ArrayList(); it =

        int identCount = 0;
        int curFuncID = -1;
        Iterator it = fpTable.getIterator();
        while (it.hasNext()) {
            FPTableRow curFun = (FPTableRow) it.next();

            Vertex tempVx = getFPByName(curFun.getName());

            Iterator itParent = tempVx.getParentList().iterator();
            while (itParent.hasNext()) {
                Vertex vx = (Vertex) itParent.next();
                if (vx.getParentList().get(0).getAttribute("NAME").equals("create_func")
                        || vx.getParentList().get(0).getAttribute("NAME").equals("create_proc")) {
                    tempVx = vx;
                }
            }
            tempVx = tempVx.getParentList().get(0);
            decls = semanticWork_acquireDeclsFP(tempVx);
            params = getParameterHash(tempVx);
            curFuncID = curFun.getID();
            Iterator it2 = decls.keySet().iterator();
            while (it2.hasNext()) {
                String declNow = (String) it2.next();
                String typeDeclNow = decls.get(declNow);

                Iterator it3 = multipleLocations.keySet().iterator();
                ArrayList<Integer> tempLocs = new ArrayList();
                while (it3.hasNext()) {
                    /*
                     * String str = (String) it3.next(); System.out.print(str);
                     * System.out.print("-");
                     * System.out.print(multipleLocations.get(str));
                     * System.out.print("\n");
                     */
                    // System.out.printf("%s\n",curFun.getName());
                    String temp = (String) it3.next();

                    if (temp.equals(declNow)) {
                        tempLocs.addAll(multipleLocations.get(temp));
                    }
                }


                String type = null;
                if (typeDeclNow.equals("вещ")) {
                    type = "DOUBLE";
                } else if (typeDeclNow.equals("цел")) {
                    type = "INT";
                } else if (typeDeclNow.equals("лит")) {
                    type = "String";
                } else if (typeDeclNow.equals("сим")) {
                    type = "CHAR";
                } else if (typeDeclNow.equals("лог")) {
                    type = "BOOLEAN";
                } else if (typeDeclNow.indexOf("целтаб") != -1) {
                    type = typeDeclNow;
                } else if (typeDeclNow.indexOf("симтаб") != -1) {
                    type = typeDeclNow;
                } else if (typeDeclNow.indexOf("логтаб") != -1) {
                    type = typeDeclNow;
                } else if (typeDeclNow.indexOf("вещтаб") != -1) {
                    type = typeDeclNow;
                } else if (typeDeclNow.indexOf("литтаб") != -1) {
                    type = typeDeclNow;
                } else {
                    type = "UTF-8";
                }
                FPTableRow row = fpTable.getRowByName(curFun.getName());
                int idFunc = row.getID();
                LocalsTableRow ltr = new LocalsTableRow(tempLocs, type, declNow, idFunc, false);

                localsTable.add(ltr);
            }
//Теперь добавим параметры
            Iterator itPAR = params.keySet().iterator();

            while (itPAR.hasNext()) {
                String nowParName = (String) itPAR.next();

                Iterator itPAR2 = multipleLocations.keySet().iterator();
                ArrayList<Integer> tempLocs = new ArrayList();
                while (itPAR2.hasNext()) {
                    String temp = (String) itPAR2.next();

                    if (temp.equals(nowParName)) {
                        tempLocs.addAll(multipleLocations.get(temp));
                    }
                }
                String type = null;
                if (params.get(nowParName).equals("вещ")) {
                    type = "DOUBLE";
                } else if (params.get(nowParName).equals("цел")) {
                    type = "INT";
                } else if (params.get(nowParName).equals("лит")) {
                    type = "String";
                } else if (params.get(nowParName).equals("лог")) {
                    type = "BOOLEAN";
                } else if (params.get(nowParName).equals("сим")) {
                    type = "CHAR";
                } else {
                    type = "UTF-8";
                }
                FPTableRow row = fpTable.getRowByName(curFun.getName());
                int idFunc = row.getID();
                LocalsTableRow ltr = new LocalsTableRow(tempLocs, type, nowParName, idFunc, true);
                localsTable.add(ltr);
            }
        }
    }

    public static boolean isNumeric(String aStringValue) {
        Pattern pattern = Pattern.compile("\\d+");

        Matcher matcher = pattern.matcher(aStringValue);
        return matcher.matches();
    }

    public static int findClassID(String methodRef) {
        int result;
        result = constantsTable.getRowByTypeAndName("Class", "MainClass").getID();

        return result;

    }

    public static int findNameAndTypeID(String methodRef) {
        int result = -1;

        Vertex vx = g.getVertexByVirginName(methodRef);
        String name = vx.getChildList().get(0).getChildList().get(0).getAttribute("NAME");
        FPTableRow fpr = fpTable.getRowByName(name);
        if (fpr != null) {
            Iterator it = constantsTable.getIterator();
            while (it.hasNext()) {
                ConstantsTableRow row = (ConstantsTableRow) it.next();
                if (row.getType().equals("NameAndType")) {
                    String funID = ((String) row.getValue()).split(",")[0];
                    String val = (String) constantsTable.getRowById(Integer.valueOf(funID).intValue()).getValue();
                    if (val.equals(name)) {
                        result = row.getID();
                        break;
                    }

                }
            }


        }
        return result;

    }

    public static void makeMethodRef(String classID, String funName, String descriptor) {

        ConstantsTableRow row = new ConstantsTableRow(new ArrayList(), "UTF-8", descriptor);
        constantsTable.addRow(row);
        int descID = row.getID();
        row = new ConstantsTableRow(new ArrayList(), "UTF-8", funName);
        constantsTable.addRow(row);
        int nameID = row.getID();

        String id1 = String.valueOf(nameID);
        String id2 = String.valueOf(descID);
        row = new ConstantsTableRow(new ArrayList(), "NameAndType", id1 + "," + id2);
        constantsTable.addRow(row);
        int methodRefID = row.getID();

        row = new ConstantsTableRow(new ArrayList(), "MethodRef", classID + ","
                + String.valueOf(methodRefID));
        constantsTable.addRow(row);

    }

    public static void addRTLToConstantsTable() {
        ConstantsTableRow row;
        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "RTL");
        constantsTable.addRow(row);

        row = new ConstantsTableRow(new ArrayList(), "Class", ConstantsTableRow.m_constantIDCount - 1);
        constantsTable.addRow(row);

        String classID = String.valueOf(row.getID());


        makeMethodRef(classID, "ku_print", "(I)V");
        makeMethodRef(classID, "ku_print", "(D)V");
        makeMethodRef(classID, "ku_print", "(C)V");
        makeMethodRef(classID, "ku_print", "(Z)V");
        makeMethodRef(classID, "ku_print", "(Ljava/lang/String;)V");
        makeMethodRef(classID, "ku_println", "()V");
        makeMethodRef(classID, "ku_pow", "(II)I");

    }

    public static void addMethodRefForFunctions() {

        Iterator it = fpTable.getIterator();
        while (it.hasNext()) {
            FPTableRow frow = (FPTableRow) it.next();
            String name = frow.getName();
            String descr = "(";
            for (String every : frow.getParTypes()) {
                descr += every;
            }
            descr += ")";
            String retType = frow.getReturnType();
            descr += retType;

            name = String.valueOf(constantsTable.getRowByName(name).getID());
            descr = String.valueOf(constantsTable.getRowByName(descr).getID());
            String _nameAndType = name + "," + descr;
            ConstantsTableRow crow = new ConstantsTableRow(new ArrayList(), "NameAndType", _nameAndType);
            constantsTable.addRow(crow);

            String nameAndTypeID = String.valueOf(crow.getID());

            String _methodRef = String.valueOf(m_mainClassID) + "," + nameAndTypeID;

            crow = new ConstantsTableRow(new ArrayList(), "MethodRef", _methodRef);
            constantsTable.addRow(crow);
        }
    }

    public static void addMultiArraysToConstants() {
        Iterator it = localsTable.getIterator();
        while (it.hasNext()) {
            boolean existMArrays = false;
            String utf8Name = "";
            LocalsTableRow row = (LocalsTableRow) it.next();

            String _type = row.getType();
            String[] arrDims = _type.split("\\[");

            if (arrDims.length > 2) {
                for (int y = arrDims.length - 1; y > 0; y--) {
                    utf8Name += "[";
                    existMArrays = true;
                }
                switch (arrDims[0]) {
                    case "целтаб": {
                        utf8Name += "I";
                        existMArrays = true;
                    }
                    break;
                    case "вещтаб": {
                        utf8Name += "D";
                        existMArrays = true;
                    }
                    break;
                    case "логтаб": {
                        utf8Name += "Z";
                        existMArrays = true;
                    }
                    break;
                    case "симтаб": {
                        utf8Name += "C";
                        existMArrays = true;
                    }
                    break;
                    case "литтаб": {
                        utf8Name += "Ljava/lang/String;";
                        existMArrays = true;
                    }
                    break;
                }
                if (existMArrays == true) {
                    ConstantsTableRow crow;
                    crow = new ConstantsTableRow(new ArrayList(), "UTF-8", utf8Name);
                    constantsTable.addRow(crow);

                    crow = new ConstantsTableRow(new ArrayList(), "Class", String.valueOf(crow.getID()));
                    constantsTable.addRow(crow);
                }
            }
        }
    }

    public static void fillConstantsTable() {
        ConstantsTableRow row;
        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "Code");
        constantsTable.addRow(row);


        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "java/lang/Object");
        constantsTable.addRow(row);
        row = new ConstantsTableRow(new ArrayList(), "Class", row.getID());
        constantsTable.addRow(row);
        m_objectClassID = row.getID();

        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "java/lang/String");
        constantsTable.addRow(row);
        row = new ConstantsTableRow(new ArrayList(), "Class", row.getID());
        constantsTable.addRow(row);




        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "<init>");
        constantsTable.addRow(row);
        String _name = String.valueOf(row.getID());
        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "()V");
        constantsTable.addRow(row);
        String _descr = String.valueOf(row.getID());
        String _nameAndType = _name + "," + _descr;
        row = new ConstantsTableRow(new ArrayList(), "NameAndType", _nameAndType);
        constantsTable.addRow(row);


        String _methodRef = String.valueOf(m_objectClassID) + "," + String.valueOf(row.getID());
        row = new ConstantsTableRow(new ArrayList(), "MethodRef", _methodRef);
        constantsTable.addRow(row);
        m_initMethodRefID = (short) row.getID();


        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "MainClass");
        constantsTable.addRow(row);

        row = new ConstantsTableRow(new ArrayList(), "Class", row.getID());
        constantsTable.addRow(row);

        m_currentClassID = row.getID();
        m_mainClassID = (short) row.getID();



        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "this");
        constantsTable.addRow(row);
        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "LMainClass;");
        constantsTable.addRow(row);


        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "main");
        constantsTable.addRow(row);
        _name = String.valueOf(row.getID());
        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "([Ljava/lang/String;)V");
        constantsTable.addRow(row);
        _descr = String.valueOf(row.getID());
        _nameAndType = _name + "," + _descr;
        row = new ConstantsTableRow(new ArrayList(), "NameAndType", _nameAndType);
        constantsTable.addRow(row);
        m_mainNameAndType = row.getID();

        _methodRef = String.valueOf(m_mainClassID) + "," + String.valueOf(m_mainNameAndType);
        row = new ConstantsTableRow(new ArrayList(), "MethodRef", _methodRef);
        constantsTable.addRow(row);


        addRTLToConstantsTable();

        addMethodRefForFunctions();



        Iterator it = multipleLocations.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();

            if (str.indexOf("!") == -1) {

                if (isNumeric(str) == true) {
                    int parsed = Integer.parseInt(str, 10);
                    if (parsed < -32768 || parsed > 32767) {
                        String type = "INT";
                        ArrayList<Integer> locs = multipleLocations.get(str);
                        String value = str;
                        row = new ConstantsTableRow(locs, type, value);
                        constantsTable.addRow(row);
                    }
                } else if (isNumeric(str) == true) {
                    int parsed = Integer.parseInt(str, 10);
                    if (parsed != 0 && parsed >= -32768 && parsed <= 32767) {
                    }
                    // пропуск - переменные не обязательно вносить в список
                } else if (str.contains("\"")) {
                    String convertedString = str.substring(1, str.length() - 1);
                    if (convertedString.length() == 1) {
                        String type = "UTF-8";
                        ArrayList<Integer> locs = multipleLocations.get(str);
                        String value = convertedString;
                        row = new ConstantsTableRow(locs, "CHAR", value);
                        constantsTable.addRow(row);
                    } else {
                        String type = "UTF-8";
                        ArrayList<Integer> locs = multipleLocations.get(str);
                        String value = convertedString;
                        row = new ConstantsTableRow(locs, type, value);
                        constantsTable.addRow(row);

                        int indexForStringConst = row.getID();

                        type = "String";
                        value = String.valueOf(indexForStringConst);
                        row = new ConstantsTableRow(new ArrayList(), type, value);
                        constantsTable.addRow(row);
                    }
                } else {
                    String type = "UTF-8";
                    ArrayList<Integer> locs = multipleLocations.get(str);
                    String value = str;
                    row = new ConstantsTableRow(locs, type, value);
                    constantsTable.addRow(row);
                }
            } else if (str.indexOf("create_function_call") != -1
                    && str.indexOf("create_function_call_expr") == -1) {
                String type = "MethodRef";
                Integer loc = locations.get(str);

                int classID = findClassID(str);
                int nameAndTypeID = findNameAndTypeID(str);

                String value = Integer.toString(classID) + "," + Integer.toString(nameAndTypeID);
                row = new ConstantsTableRow(loc.intValue(), type, value);
                constantsTable.addRow(row);
            }
        }

    }

    public static void semanticWork_checkParamCountMatch() {
        ArrayList<Vertex> list = new ArrayList();
        findPureNameInChilds2List(g.getRoot(), "create_function_call", list);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            Vertex createIdVer = findPureNameInChilds(vx, "create_ident");
            String name = createIdVer.getChildList().get(0).getAttribute("NAME");
            ArrayList<Vertex> tempList = new ArrayList();
            Vertex create_expr_list = findPureNameInChilds(vx, "create_expr_list");
            int parCount = create_expr_list.getChildList().size();

            if (fpTable.getSize() > 0) {

                FPTableRow nowRow = fpTable.getRowByName(name);

                if (nowRow != null && parCount > nowRow.getParCount()) {
                    String errMess = "Количество параметров у \"" + name + "\"" + " больше, чем сказано в определении";
                    Integer loc = locations.get(vx.getVirginName());
                    String err = makeErrorMessage(filename, loc, errMess);
                    System.err.print(err);
                    allRight = false;
                } else if (nowRow != null && parCount < nowRow.getParCount()) {
                    String errMess = "Количество параметров у \"" + name + "\"" + " меньше, чем сказано в определении";
                    Integer loc = locations.get(vx.getVirginName());
                    String err = makeErrorMessage(filename, loc, errMess);
                    System.err.print(err);
                    allRight = false;
                }

            }
        }
    }

    public static void semanticWork_checkCallingFunctionExists() {
        ArrayList<Vertex> list = new ArrayList();
        findPureNameInChilds2List(g.getRoot(), "create_function_call", list);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            Vertex createIdVer = findPureNameInChilds(vx, "create_ident");
            String name = createIdVer.getChildList().get(0).getAttribute("NAME");

            if (fpTable.isIdentifierExists(name) == false) {
                String errMess = "Обращение к необъявленой функции/процедуре \"" + name + "\"";
                Integer loc = locations.get(vx.getVirginName());
                String err = makeErrorMessage(filename, loc, errMess);
                System.err.print(err);
                allRight = false;
            }


        }
    }

    public static void semanticWork_checkParamTypesMatch() {
        if (fpTable.getSize() > 0) {
            ArrayList<Vertex> list = new ArrayList();
            findPureNameInChilds2List(g.getRoot(), "create_function_call", list);
            Iterator it = list.iterator();
            while (it.hasNext()) {
                String name = null;
                ArrayList<String> listTypes = new ArrayList();
                Vertex vx = (Vertex) it.next();
                Vertex createIdVer = findPureNameInChilds(vx, "create_ident");
                name = createIdVer.getChildList().get(0).getAttribute("NAME");
                ArrayList<Vertex> tempList = new ArrayList();
                Vertex create_expr_list = findPureNameInChilds(vx, "create_expr_list");
                Iterator it2 = create_expr_list.getChildList().iterator();
                while (it2.hasNext()) {
                    Vertex vx2 = (Vertex) it2.next();
                    String type = vx2.getAttribute("TYPE");
                    if (type == null) {
                        type = vx2.getChildList().get(0).getAttribute("TYPE");
                    }

                    listTypes.add(type);


                }
                FPTableRow nowRow = fpTable.getRowByName(name);
                Iterator _it = listTypes.iterator();
                ArrayList<String> backupTypes = new ArrayList();
                while (_it.hasNext()) {
                    String typeNow = (String) _it.next();
                    String strNow = translateTypeTo1Char(typeNow);
                    backupTypes.add(strNow);
                }
                listTypes = backupTypes;
                for (int g = 0; g < listTypes.size(); g++) {
                    if (listTypes.get(g) == null) {
                        listTypes.remove(g);
                    } else if (listTypes.get(g).isEmpty()) {
                        listTypes.remove(g);
                    }
                }
                if (listTypes.isEmpty() != true && nowRow != null && listTypes.containsAll(nowRow.getParTypes()) == false) {
                    String errMess = "Вызов функции/процедуры \"" + name + "\" не соответствует определению по типам параметров";
                    Integer loc = locations.get(vx.getVirginName());
                    String err = makeErrorMessage(filename, loc, errMess);
                    System.err.print(err);
                    allRight = false;

                }



            }
        }
    }

    public static String translateTypeTo1Char(String str) {
        String result = "";
        if (str != null) {
            switch (str) {
                case "лит":
                    result = "Ljava/lang/String;";
                    break;
                case "цел":
                    result = "I";
                    break;
                case "вещ":
                    result = "D";
                    break;
                case "лог":
                    result = "Z";
                    break;
                case "сим":
                    result = "C";
                    break;


            }
        }

        return result;
    }

    public static void swap(ArrayList<Vertex> vxs, int index1, int index2) {
        ArrayList<Vertex> backup = (ArrayList) vxs.clone();

    }

    public static void transformTree() {
        // Трансформируем дерево - приводим узлы := к виду []:=
        ArrayList<Vertex> tempList = new ArrayList();
        Iterator it = g.getIterator();
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            if (vx.getAttribute("NAME").equals("create_array_expr")
                    && vx.getParentList().get(0).getAttribute("NAME").equals(":=")) {
                tempList.add(vx);
            }
        }

        // когда всё пройдено приступаем к обработке каждого


        for (int i = 0; i < tempList.size(); i++) {
            Vertex vx = tempList.get(i);

            // скопируем родителя
            Vertex parent = vx.getParentList().get(0);
            ArrayList<Vertex> tempPar = new ArrayList();
            ArrayList<Vertex> backupChilds = (ArrayList) vx.getChildList().clone();

            // parent.getChildList().clear();
            // скопируем детей []
            ArrayList<Vertex> childs = (ArrayList) vx.getChildList();

            int m = 0;
            for (Vertex temp : parent.getChildList()) {
                if (temp.getAttribute("NAME").equals("create_array_expr") == true) {
                    temp.addAttribute("NAME", "empty");
                    m++;

                }
            }

            int k = 0;
            for (Vertex every : childs) {

                every.getParentList().clear();
                if (every.getAttribute("NAME").equals("create_array_expr") != true) {
                    every.getParentList().add(parent);
                    parent.getChildList().add(k++, every);
                }
            }
            parent.getChildList().remove(m + k - 1);



            //Переименуем := в []:= 
            String newName = "[]" + parent.getAttribute("NAME");
            parent.addAttribute("NAME", newName);
            // присвоим детей [] списку детей :=, притом первыми

            // запасной массив
    /*
             * ArrayList<Vertex> temp = (ArrayList)
             * parent.getChildList().clone(); parent.getChildList().clear();
             *
             * for (Vertex v : childs) { v.getParentList().clear();
             * v.getParentList().add(parent); parent.getChildList().add(v); }
             * for (Vertex v : temp) {
             *
             * if (!v.getAttribute("NAME").equals("create_array_expr")) {
             * v.getParentList().clear(); v.getParentList().add(parent);
             * parent.getChildList().add(v); } }
             */
            //вуаля
        }

    }

    public static void cutLocalsFromConstants() {
        ArrayList<Integer> toDelete = new ArrayList();
        Iterator it = localsTable.getIterator();
        while (it.hasNext()) {
            LocalsTableRow row = (LocalsTableRow) it.next();
            ArrayList<Integer> tmpList = row.getLocs();
            String tmpName = (String) row.getValue();
            Iterator it2 = constantsTable.getIterator();
            while (it2.hasNext()) {
                ConstantsTableRow row2 = (ConstantsTableRow) it2.next();
                String tmptmpName = row2.getValue().toString();
                ArrayList<Integer> tmptmp = row2.getLocation();
                if (tmpName.equals(tmptmpName) && tmptmp.containsAll(tmpList)) {

                    /*
                     * System.out.printf("Константа %d в таблице констант -" +
                     * "локальная переменная %d\n", row2.getID(), row.getID());
                     */

                    toDelete.add(Integer.valueOf(row2.getID()));

                }

            }

        }


        for (Integer inte : toDelete) {
            constantsTable.emptyingRow(inte.intValue());
        }

    }

    public static void processParamListOnTables() {
        Iterator it = fpTable.getIterator();
        String resultName = null;
        while (it.hasNext()) {
            FPTableRow row = (FPTableRow) it.next();
            Integer addressNameAndType = row.getConstTableID();
            ConstantsTableRow crow = constantsTable.getRowById(addressNameAndType);
            Integer locOfDecriptor = Integer.valueOf(((String) crow.getValue()).split(",")[1].trim());
            Integer locOfName = Integer.valueOf(((String) crow.getValue()).split(",")[0].trim());
            Integer result = constantsTable.getRowById(locOfDecriptor).getLocation().get(0);
            resultName = constantsTable.getRowById(locOfName).getValue().toString();

            Iterator it2 = constantsTable.getIterator();
            while (it2.hasNext()) {
                ConstantsTableRow row2 = (ConstantsTableRow) it2.next();
                if (row2.getLocation().size() > 0) {
                    if (row2.getID() != result.intValue() && row2.getLowestLocation().equals(result)) {
                        //TODO - проитерировать для всех местоположений
                        ArrayList<Integer> tmpLocals = new ArrayList();
                        tmpLocals.add(row2.getLocation().get(0));
                        LocalsTableRow tmpRow = new LocalsTableRow(tmpLocals,
                                "UTF-8",
                                row2.getValue(),
                                fpTable.getRowByName(resultName).getID(),
                                false);
                        localsTable.add(tmpRow);
                    }
                }
            }

        }
    }

    public static void init() {

        g = new Graph();
        locals = new HashMap();
        consts = new ArrayList();
        ids = new ArrayList();
        types = new ArrayList();
        localsTable = new LocalsTable();
        decls = new HashMap();
        constantsTable = new ConstantsTable();
        fpTable = new FPTable();
        bytecodeBuffer = new HashMap();
        //  filename = "..\\unittests\\procsAndFuncs1.dot";

        readFile(filename);
        readLocFile(filenameLoc);

        //readFile("procsAndFuncs2.dot");
        // readFile("print1.dot");
        createGraph();
        // sortIt();
        root = g.findRoot();
        semanticWorks_findFP();


        fillFPTable();


        fillConstantsTable();

        fillLocalsTable();
        addMultiArraysToConstants();


    }

    public static void checks() {
        semanticWork_checkFP();
        semanticWork_checkCallingFunctionExists();
        semanticWork_checkParamCountMatch();
        semanticWork_checkParamTypesMatch();
        semanticWork_checkIncompatableTypesInExpr();
        semanticWorks_checkIntegerIndexes();

    }

    public static void _iterate_populate(ArrayList<Vertex> list, String type) {

        for (Vertex vx : list) {
            if (vx.getAttribute("NAME").equals("create_func")
                    || vx.getAttribute("NAME").equals("create_proc")
                    || vx.getTypeOfSymbol().equals("TYPE")
                   /* || vx.getTypeOfSymbol().equals("OPERATION")*/
                    || vx.containsAttribute("TYPE") == true
                    || vx.getAttribute("NAME").equals("create_stmt_list")
                    || vx.getAttribute("NAME").equals("append_stmt_to_list")
                    || vx.getAttribute("NAME").equals("create_from_arg_rezvalue")
                    || vx.getAttribute("NAME").equals("create_function_call")
                    || vx.getAttribute("NAME").equals("create_expr_list")) {
                continue;
            } else {

                vx.addAttribute("TYPE", type);

                _iterate_populate(vx.getParentList(), type);
                _iterate_populate(vx.getChildList(), type);
            }
        }
    }

    public static void populateTypes() {
        ArrayList<Vertex> endings = getAllEndings();

        ArrayList<Vertex> endingsType = new ArrayList();
        for (Vertex vx : endings) {
            if (vx.containsAttribute("TYPE")) {
                endingsType.add(vx);
            }
        }

        for (Vertex vx : endingsType) {
            _iterate_populate(vx.getParentList(), vx.getAttribute("TYPE"));
        }

        // Теперь ищем вызовы функций и присваиваем create_function_call_expr тип
        // тип находим по create_function_call_expr->create_function_call->create_ident->
        // ищем в таблице функций возвращаемое значение для данного ID и присваиваем
        // его create_function_call_expr
        ArrayList<Vertex> funCalls = getAllFunctionCalls();
        for (Vertex vx : funCalls) {
            Vertex create_function_call = getByVertexName(vx, "create_function_call");
            Vertex create_ident = getByVertexName(create_function_call, "create_ident");
            String funName = create_ident.getChildList().get(0).getAttribute("NAME");
            String retType = fpTable.getRowByName(funName).getReturnType();
            if (retType.equals("I")) {
                vx.addAttribute("TYPE", "цел");
            } else if (retType.equals("")) {
            }
        }



        ArrayList<Vertex> create_idents = getAllCreateIdents();
        for (Vertex vx : create_idents) {
            String type = vx.getChildList().get(0).getAttribute("TYPE");
            vx.addAttribute("TYPE", type);
            vx.getParentList().get(0).addAttribute("TYPE", type);
        }


        populate_type_operations();



    }

    public static ArrayList<Vertex> getAllExpr() {
        ArrayList<Vertex> result = new ArrayList();

        Iterator it = g.getIterator();

        Vertex vx = null;


        while (it.hasNext()) {

            vx = (Vertex) it.next();

            if (vx.getTypeOfSymbol().equals("OPERATION")) {
                result.add(vx);
            }
        }

        return result;

    }

    // случай когда операции вложенные и populate_types не достаёт
    public static void populate_type_operations() {
        ArrayList<Vertex> list = getAllExpr();
        for (Vertex vx : list) {
            String type = inChildsExistsAttType(vx);
            vx.addAttribute("TYPE", type);
        }
    }

    /*
     * public static void populate_type_params() { ArrayList<Vertex> list =
     * getAllExpr(); for (Vertex vx : list) { String type =
     * inChildsExistsAttType(vx); vx.addAttribute("TYPE", type); } }
     */
    public static void semanticWork_checkIncompatableTypesInExpr() {
        ArrayList<Vertex> list = getAllExpr();

        for (Vertex vx : list) {
            if (vx.getChildList().size() == 2) {
                String nameVx = vx.getAttribute("NAME");
                String nameVx1 = vx.getChildList().get(0).getAttribute("NAME");
                String nameVx2 = vx.getChildList().get(1).getAttribute("NAME");

                Vertex childVX1 = vx.getChildList().get(0);
                Vertex childVX2 = vx.getChildList().get(1);
                if (childVX1.getAttribute("TYPE") == null) {
                    while (childVX1.getChildList().isEmpty()!=true) {
                        if (childVX1.getAttribute("TYPE") == null) {
                            childVX1 = childVX1.getChildList().get(0);
                        }
                    }
                }
               if (childVX2.getAttribute("TYPE") == null) {
                    while (childVX2.getChildList().isEmpty()!=true) {
                        if (childVX2.getAttribute("TYPE") == null) {
                            childVX2 = childVX2.getChildList().get(0);
                        }
                    }
                }

                if (!childVX1.getAttribute("TYPE").equals(
                        childVX2.getAttribute("TYPE"))) {
                    String errMess = "Операнды у \"" + vx.getAttribute("NAME") + "\"" + " имеют разные типы";
                    Integer loc = locations.get(vx.getVirginName());
                    String err = makeErrorMessage(filename, loc, errMess);
                    System.err.print(err);
                    allRight = false;
                } else {
                    //тут проверка на унарный минус перед литерной константой
                }


            }

        }
    }

    public static void writeInitThings() {
        ByteBuffer commands = ByteBuffer.allocate(10);


        commands.put(CG.ALOAD_0);
        commands.put(CG.INVOKESPECIAL);
        commands.putShort(m_initMethodRefID);
        commands.put(CG.RETURN);


        bytecodeBuffer.put("<init>", commands);
    }

    public static void main(String[] args) {

        if (args.length == 1) {
            filename = args[0];
            filenameLoc = filename.substring(0, filename.length() - 4).concat(".loc");
            filenameKum = filename.substring(0, filename.length() - 4).concat(".kum");
        } else {
            filename = "..\\unittests\\procsAndFuncs1.dot";
            filenameLoc = filename.substring(0, filename.length() - 4).concat(".loc");
            filenameKum = filename.substring(0, filename.length() - 4).concat(".kum");

        }

        init();
        populateTypes();
     //   checks();
        cutLocalsFromConstants();


        try {
            Thread.sleep(100);


        } catch (InterruptedException ex) {
            Logger.getLogger(Semantic.class.getName()).log(Level.SEVERE, null, ex);
        }


        /*
         * System.out.print("Таблица констант:\n"); constantsTable.printTable();
         */
        // System.out.print("\nТаблица функций:\n"); fpTable.printTable();
     /*
         * System.out.print("\nТаблица локальных переменных:\n");
         * localsTable.printTable();
         */
        //processParamListOnTables();



        transformTree();
        //g.printInfo();


        if (allRight == true) {
            try {
                Generator generator = new Generator("MainClass.class");

                writeInitThings();
                generator.writeProlog();
                generator.writeConstantsTable(constantsTable);

                generator.writeAccessFlags();
                generator.writeCurrentClass(m_currentClassID);
                generator.writeParentClass(m_objectClassID);
                generator.writeInterfaces();
                generator.writeFields();

                generator.recursive(g.getRoot());

                generator.writeMethods();
                generator.writeAttributes();

                generator.close();








            } catch (IOException ex) {
                Logger.getLogger(Semantic.class.getName()).log(Level.SEVERE, null, ex);
            }





        }



    }
}
