package semantic;

import java.io.*;
import java.util.ArrayList;

import java.util.regex.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            in = new Scanner(new File(path), "Windows-1251");
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
            String str1 = rows[i].split(" ")[0];
            String str2 = rows[i].split(" ")[1];
            str1 = str1.trim();
            str2 = str2.trim();
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

    public static boolean isOperation(String str) {
        if (str.contains("+") || str.contains("-")
                || str.contains("*") || str.contains("/")
                || str.contains("**") || str.equals("UnaryMinus")
                || str.contains("<") || str.equals(">")
                || str.contains("=") || str.equals("<>")
                || str.contains(":=") || str.equals(">=")
                || str.equals("<=")) {
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
        }



        return result;

    }

    public static Vertex getChildsByAttName(Vertex in_vx, String att_name) {
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
            result = getChildsByAttName(vx, att_name);
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
                name = value;
                id = "";
                type = "ID";
                hasType = true;
                isInheritesType = true;
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

            } else {
                String value;
                value = str_in.substring(tempInt + 1, str_in.length());
                value = value.trim();
                if (!consts.contains(value)) {
                    consts.add(value);
                }
                if (describe.equals("Целая константа") || describe.equals("Целое число")) {
                    result.addAttribute("TYPE", "цел");

                } else if (describe.equals("Вещественная константа")) {
                    result.addAttribute("TYPE", "вещ");
                } else if (describe.equals("Логическая константа")) {
                    result.addAttribute("TYPE", "лог");
                } else if (describe.equals("Строковая константа")) {
                    result.addAttribute("TYPE", "лит");
                } else if (describe.equals("Символьная константа")) {
                    result.addAttribute("TYPE", "сим");
                }

                result.addAttribute("NAME", value);
                name = describe;
                id = String.valueOf(ids.indexOf(value));
                type = "SIMPLE";
                hasType = true;
            }
        }



        // System.out.printf("Create vertex: %s\n", str_in);
        result.addAttribute("NAME", name);



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

            Vertex znach_value = getChildsByAttName(vx, "create_znachvalue");
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
        ArrayList<Vertex> list = new ArrayList();
        HashMap<String, String> tempLocals = new HashMap();
        return tempLocals;
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
        //Теперь собираем из параметров
        ArrayList<Vertex> tempLst = new ArrayList();
        ArrayList<Vertex> tempLst2 = new ArrayList();
        Vertex vxNow2 = new Vertex();
        findPureNameInChilds2List(in_vx, "create_from_arg_rezvalue", tempLst);

        Iterator it3 = tempLst.iterator();
        while (it3.hasNext()) {
        Vertex vx3 = (Vertex) it3.next();
        findTypeInChilds2List(vx3, "ID", tempLst2);
        }*/




        return tempLocals;


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
                        isFail = true;

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
                isFail = true;
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
                if (vxCh.getAttribute("TYPE").equals("цел") == false) {
                    Integer loc = locations.get(vxNow.getVirginName());
                    String erMess = "Ошибка C0002:  Индексы массива должны быть целыми числами!";
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
    /*
     * Проверки:
     *
     * Функция возвращает значения Процедура не возвращала значения Переменные
     * были объявлены в данной области видимости в процессе приведение типов к
     * более точному
     *
     */

    /*
     * Поднимается наверх, пока не достигнет вершины, для которой понятие типа
     * бесполезно Попутно устанавливает
     */
    /*
     * public static applyType(){
     *
     *
     * Iterator it = g.getIterator(); while(it.hasNext()){ Vertex vx =
     * (Vertex)it.next(); if (vx.getIsInheritesType() &&
     * vx.getTypeOfSymbol().equals("OPERATION")){ Iterator it2 =
     * vx.getChildList().iterator(); while(it2.hasNext()){ Vertex vx2 =
     * (Vertex)it2.next();
     *
     * }
     * }
     * }
     *
     * }
     */
    public static void getParameterList(Vertex in_vx, ArrayList<String> list) {
        String str = null;
        ArrayList<Vertex> list2 = new ArrayList();
        findPureNameInChilds2List(in_vx, "create_arg", list2);
        Iterator it = list2.iterator();
        int countInArgRez = 0;
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            countInArgRez = vx.getChildList().get(1).getChildList().size();
            str = vx.getChildList().get(0).getChildList().get(0).getAttribute("NAME");
            for (int i = 0; i < countInArgRez; i++) {
                list.add(str);
            }
        }

        list2 = new ArrayList();
        findPureNameInChilds2List(in_vx, "create_rez", list2);
        it = list2.iterator();
        str = null;
        while (it.hasNext()) {
            Vertex vx = (Vertex) it.next();
            countInArgRez = vx.getChildList().get(1).getChildList().size();
            str = vx.getChildList().get(0).getChildList().get(0).getAttribute("NAME");
            for (int i = 0; i < countInArgRez; i++) {
                list.add(str);
            }
        }



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

            getParameterList(vx, plist);

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

            getParameterList(vx, plist);
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
                if (vx.getParentList().get(0).getAttribute("NAME").equals("create_func")) {
                    tempVx = vx;
                }
            }
            tempVx = tempVx.getParentList().get(0);
            decls = semanticWork_acquireDeclsFP(tempVx);
            params = semanticWork_acquireParamsFP(tempVx);
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
                    type = "FLOAT";
                } else if (typeDeclNow.equals("цел")) {
                    type = "INT";
                } else {
                    type = "UTF-8";
                }
                FPTableRow row = fpTable.getRowByName(curFun.getName());
                int idFunc = row.getID();
                LocalsTableRow ltr = new LocalsTableRow(tempLocs, type, declNow, idFunc, false);
                localsTable.add(ltr);


            }



        }

    }

    public static boolean isNumeric(String aStringValue) {
        Pattern pattern = Pattern.compile("\\d+");

        Matcher matcher = pattern.matcher(aStringValue);
        return matcher.matches();
    }

    public static String findClassName(String methodRef) {
        String result;
        result = "1";

        return result;

    }

    public static String findNameAndType(String methodRef) {
        String result = null;

        Vertex vx = g.getVertexByVirginName(methodRef);
        String name = vx.getChildList().get(0).getChildList().get(0).getAttribute("NAME");
        FPTableRow fpr = fpTable.getRowByName(name);
        if (fpr != null) {
            result = String.valueOf(fpr.getID());
        }
        return result;

    }

    public static void fillConstantsTable() {
        ConstantsTableRow row;
        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "Code");
        constantsTable.addRow(row);
        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "<init>");
        constantsTable.addRow(row);
        row = new ConstantsTableRow(new ArrayList(), "UTF-8", "MainClass");
        constantsTable.addRow(row);
        row = new ConstantsTableRow(new ArrayList(), "Class", ConstantsTableRow.m_constantIDCount - 1);
        constantsTable.addRow(row);


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

                String className = findClassName(str);
                String nameAndType = findNameAndType(str);

                String value = className + "," + nameAndType;
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
                if (nowRow != null && listTypes.containsAll(nowRow.getParTypes()) == false) {
                    String errMess = "Вызов функции/процедуры \"" + name + "\" не соответствует определению по типам параметров";
                    Integer loc = locations.get(vx.getVirginName());
                    String err = makeErrorMessage(filename, loc, errMess);
                    System.err.print(err);
                    allRight = false;

                }



            }
        }
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
        Iterator it2 = tempList.iterator();
        while (it2.hasNext()) {
            Vertex vx = (Vertex) it2.next();
            // скопируем детей []
            ArrayList<Vertex> childs = vx.getChildList();
            // скопируем родителя
            Vertex parent = vx.getParentList().get(0);
            // удалим []
            g.removeByVirginName(vx.getVirginName());
            //Переименуем := в []:= 
            String newName = "[]" + parent.getAttribute("NAME");
            parent.addAttribute("NAME", newName);
            // присвоим детей [] списку детей :=, притом первыми

            // запасной массив
            ArrayList<Vertex> temp = parent.getChildList();
            parent.getChildList().clear();

            for (Vertex v : childs) {
                parent.getChildList().add(v);
            }
            for (Vertex v : temp) {
                parent.getChildList().add(v);
            }
            //вуаля
        }

    }

    public static void cutLocalsFromConstants() {
        Iterator it = localsTable.getIterator();
        while (it.hasNext()) {
            LocalsTableRow row = (LocalsTableRow) it.next();
            ArrayList<Integer> tmpList = row.getLocs();
            Iterator it2 = constantsTable.getIterator();
            while (it2.hasNext()) {
                ConstantsTableRow row2 = (ConstantsTableRow) it2.next();
                ArrayList<Integer> tmptmp = row2.getLocation();
                if (tmptmp.containsAll(tmpList)) {
                    System.out.printf("Константа %d в таблице констант - локальная переменная %d\n",
                            row2.getID(), row.getID());
                }
                /////
            }

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
                    if (row2.getID() != result && row2.getLocation().get(0).equals(result)) {
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
        //  filename = "..\\unittests\\procsAndFuncs1.dot";
        filename = "..\\unittests\\procsAndFuncs1.dot";
        filenameLoc = filename.substring(0, filename.length() - 4).concat(".loc");
        filenameKum = filename.substring(0, filename.length() - 4).concat(".kum");

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

    }

    public static void checks() {
        semanticWork_checkFP();
        semanticWork_checkCallingFunctionExists();
        semanticWork_checkParamCountMatch();
        semanticWork_checkParamTypesMatch();


        //  semanticWorks_checkIntegerIndexes();

    }

    public static void main(String[] args) {

        init();
        checks();
      // >>   cutLocalsFromConstants();
        
        // >> processParamListOnTables();
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Semantic.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.print("Таблица констант:\n");
        constantsTable.printTable();
        System.out.print("\nТаблица функций:\n");
        fpTable.printTable();
        System.out.print("\nТаблица локальных переменных:\n");
        localsTable.printTable();

        //transformTree();
        // g.printInfo();

        /*
         * Iterator it = fpTable.getIterator(); while (it.hasNext()) {
         * ((FPTableRow) it.next()).printRow(); }
         */
        //    Vertex tmp = g.getByVertexName("N");
        // Vertex whi = whichBlock(tmp);
        //  semanticWorks_attributeSymbolsByType();

        // constantsTable.printTable();

        //g.printInfo();
     /*
         * ArrayList<Vertex> list = new ArrayList();
         * semanticWork_findExprs(root, list);
         * semanticWorks_attributeIdentByType(); /* if (procs != null &&
         * procs.size() > 0) { semanticWork_isReturnExistsP(procs.get(0)); } if
         * (funcs != null && funcs.size() > 0) {
         * semanticWork_isReturnExistsF(funcs.get(0)); }
         *
         * boolean bnkl2 = true;
         */


        if (allRight == true) {
            // System.err.print("\nСкомпилилось.");
        }



    }
}
