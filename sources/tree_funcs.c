#include "..\headers\tree_funcs.h"
#include "..\headers\tree_structs.h"
#include "..\headers\parser.tab.h"
/*
Инициализация пула памяти
(временное решение)
*/
void init_safeAlloc()
{
    for (int i=0; i<POOL_ARRAY_SZ; i++)
    {
        safeAllocArray[i] = malloc(ALLOC_SZ);
    }

}

/*
Окружает входную строку двойными кавычками и возвращает новую строку
*/
char* surroundQuotes(char* in_str)
{

    char* result;

    result = (char*)safeAlloc(strlen(in_str)+2);

    strcpy(result, "\"");
    strcat(result,in_str);
    strcat(result, "\"");

    return result;

}

/*
Берёт из пула новый адрес, использовать вместо malloc! (временное решение)
*/
void * safeAlloc(unsigned int bytes)
{
// Не удалять, в будущем надо что-то сделать с выделятором памяти
// появились некоторые проблемы с safeAlloc - придётся брать выделенную
// память из заранее подготовленного пула
    /*  void* result = NULL;

      if (bytes == 0)
      {
          return NULL;
      }

      result = malloc(bytes);

      if (!result)
      {
          printf("\nFailed to malloc: %d blocks has been allocated", mallocPointer);
          garbageCollector();
          assert(result);
      }
      printf("\n %d block: %x", mallocPointer,result );
      safeAllocArray[safeAllocPointer++] = result;
    */

    return  safeAllocArray[safeAllocPointer++];

}

/*
Вырезает двойные кавычки из строки и возвращает новую строку
*/
char * cutQuotes(char* str)
{
    char* result = NULL;
    if (str && str != (char*)0xbaadf00d)
    {
        char* buf = (char*)safeAlloc(ALLOC_SZ);
        result = (char*)safeAlloc(ALLOC_SZ);
        char* token;
        strcpy(result, "");
        strcpy(buf, str);
        token = strtok(buf, "\"");
        while(token)
        {
            strcat(result, token);
            token = strtok(NULL, "\"");
        }
    }
    return result;
}

/*
Проверяет, есть ли данный хэш в дереве
*/
int isHashExists(int num)
{
    int result = 0;
    int i;

    for (i=0; i<hash_pointer; i++)
    {
        if (num == hashes[i])
        {
            result = 1;
            break;
        }
    }


    return result;
}

/*
Создаёт строку из хэша (на данный момент всё примитивно - просто число)
и возвращает её
*/
char * makeHashString()
{
    char* result;


    result = (char*)safeAlloc(6);

    hashes[hash_pointer] = prevSalt++;
    hash_pointer++;

    itoa(prevSalt, result, 16);

    return result;

}

/*
Возвращает мнемоническое обозначение для кода операции
*/
char * getName(int type)
{


    char * result = (char*)safeAlloc(14);

    switch(type)
    {
    case ASSMNT:
        strcpy(result, ":=");
        break;
    case PLUS:
        strcpy(result, "+");
        break;
    case MINUS:
        strcpy(result, "-");
        break;
    case MUL:
        strcpy(result, "*");
        break;
    case DIV:
        strcpy(result, "/");
        break;
    case POW:
        strcpy(result, "**");
        break;
    case GT:
        strcpy(result, ">");
        break;
    case LT:
        strcpy(result, "<");
        break;
    case GTEQ:
        strcpy(result, ">=");
        break;
    case LTEQ:
        strcpy(result, "<=");
        break;
    case NEQ:
        strcpy(result, "<>");
        break;
    case EQ:
        strcpy(result, "=");
        break;
    default:
        strcpy(result, "Non_operation");
    }
    return result;
}


/*
Склеивает строки, наподобие strcat, НО, в отличие от последней,
присоединяет буфер к концу константной строки
*/
char* strcat_const(const char* const_str, char* buf)
{

    char* temp_p;
    char* backup_str = (char*)safeAlloc(strlen(buf));

    strcpy(backup_str, buf);

    temp_p = (char*)safeAlloc(strlen(buf)+strlen(const_str));

    strcpy(temp_p, const_str);

    strcpy(temp_p+strlen(const_str), backup_str);

    //buf = backup_str;

    return temp_p;

}

/*
Создаёт связь на графе от from до to
*/
void makeNode(char* from, char* to)
{

    if (from && to)
    {
        char* temp = (char*)safeAlloc(ALLOC_SZ);
        char* fromWithoutQuotes = cutQuotes(from);
        char* toWithoutQuotes = cutQuotes(to);
        strcpy(temp, "\"");
        strcat(temp, fromWithoutQuotes);
        strcat(temp, "\" -> \"");
        strcat(temp, toWithoutQuotes);
        strcat(temp, "\";");

        fprintf(dotfile, "\n%s", temp);

        fprintf(locsfile,fromWithoutQuotes);
        char _0x1F[2];
        _0x1F[0] = '\x1F';
        fprintf(locsfile,_0x1F);
        char buf[10]= {};
        sprintf(buf, "%d", yylloc.first_line);
        fprintf(locsfile, buf);
        fprintf(locsfile, "\n");

    }

}

/*
Создаёт уникальный идентификатор узла, исходя из переданной строки
и сгенерированного внутри хэша (вид ID - "большойИстрашный!f977d")
*/
char* makeUniqueID(const char* name_of_proc)
{


    char* temp_p = (char*)safeAlloc(strlen(name_of_proc));
    strcpy(temp_p, name_of_proc);


    strcat(temp_p, "!");
    strcat(temp_p, makeHashString());

    return temp_p;
}




/*
Очищает пул памяти
*/
void garbageCollector()
{
    int i;
    for (i=0; i < POOL_ARRAY_SZ; i++)
    {
        if (!safeAllocArray[i])
        {
            free(safeAllocArray[i]);
            safeAllocArray[i] = NULL;
        }
    }
}





/** =============================================================== */




/*
Программа <- список операторов
*/
struct NProgram* create_program (struct NStmt_list* list)
{
    struct NProgram* program = (struct NProgram*)safeAlloc(sizeof(struct NStmt_list));
    char* uniqID;
    program->stmt_list = list;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_program");
    program->print_val = uniqID;
    makeNode(uniqID, list->print_val);
#endif

    return program;
}


/*
Список операторов <- оператор
*/
struct NStmt_list* create_stmt_list (struct NStmt* stmt)
{
    struct NStmt_list* list = NULL;
    char* uniqID;
    if (stmt)
    {

        list = (struct NStmt_list*)safeAlloc(sizeof(struct NStmt_list));
        list->first = stmt;
        list->last = stmt;


#ifdef OUT2DOT
        uniqID = makeUniqueID("create_stmt_list");
        list->print_val = uniqID;
        makeNode(uniqID, stmt->print_val);
#endif


    }
    return list;
}


/*
   Добавляет оператор stmt в список операторов list
*/
struct NStmt_list* append_stmt_to_list (struct NStmt_list* list, struct NStmt* stmt)
{
    char* uniqID;
    if (stmt)
    {
        if (list)
        {
            list->last->next = stmt;
            list->last = stmt;
#ifdef OUT2DOT
            uniqID = makeUniqueID("append_stmt_to_list");
            makeNode(list->print_val, uniqID);
            //list->print_val = uniqID;
            makeNode(uniqID, stmt->print_val);

#endif
        }
        else
        {
            list = create_stmt_list(stmt);
        }




    }

    return list;
}


/*
   Оператор <- выражение
*/
struct NStmt* create_stmt_expr (struct NExpr* expr)
{
    struct NStmt* stmt = NULL;
    char* uniqID;

    if (expr)
    {
        stmt = (struct NStmt*)safeAlloc(sizeof(struct NStmt));
        stmt->expr = expr;
        stmt->type = EXPR;


#ifdef OUT2DOT
        uniqID = makeUniqueID("create_stmt_expr");
        stmt->print_val = uniqID;
        makeNode(uniqID, expr->print_val);
#endif
    }

    return stmt;
}

/*
Оператор <- список выражений
*/
struct NStmt* create_stmt_expr_list(struct NExpr_list* list)
{
    struct NStmt* stmt = NULL;
    char* uniqID;
    if (list)
    {
        stmt = (struct NStmt*)safeAlloc(sizeof(struct NStmt));
        stmt->type   = EXPR_LIST;
        stmt->expr_list   = list;


#ifdef OUT2DOT
        uniqID = makeUniqueID("create_stmt_expr_list");
        stmt->print_val = uniqID;
        makeNode(uniqID, list->print_val);
#endif

    }

    return stmt;
}

/*
Оператор <- функция как оператор
*/
struct NStmt* create_stmt_func (struct NFunc_stmt* func_stmt)
{
    struct NStmt* stmt = NULL;
    char* uniqID;
    if (func_stmt)
    {
        stmt = (struct NStmt*)safeAlloc(sizeof(struct NStmt));
        stmt->func_stmt   = func_stmt;
        stmt->type   = FUNC_STMT;

#ifdef OUT2DOT
        uniqID = makeUniqueID("create_stmt_func");
        stmt->print_val = uniqID;
        makeNode(uniqID, func_stmt->print_val);
#endif

    }

    return stmt;
}

/*
Оператор <- процедура как оператор
*/
struct NStmt* create_stmt_proc (struct NProc_stmt* proc_stmt)
{
    struct NStmt* stmt = NULL;
    char* uniqID;
    if (proc_stmt)
    {
        stmt = (struct NStmt*)safeAlloc(sizeof(struct NStmt));
        stmt->proc_stmt   = proc_stmt;
        stmt->type   = PROC_STMT;


#ifdef OUT2DOT
        uniqID = makeUniqueID("create_stmt_proc");
        stmt->print_val = uniqID;
        makeNode(uniqID, proc_stmt->print_val);
#endif
    }
    return stmt;
}

/*
Оператор <- оператор вывода
*/
struct NStmt* create_stmt_print (struct NPrint_stmt* print_stmt)
{
    struct NStmt* stmt = NULL;
    char* uniqID;
    if (print_stmt)
    {
        stmt = (struct NStmt*)safeAlloc(sizeof(struct NStmt));
        stmt->print_stmt  = print_stmt;
        stmt->type   = PRINT_STMT;


#ifdef OUT2DOT
        uniqID = makeUniqueID("create_stmt_print");
        stmt->print_val = uniqID;
        makeNode(uniqID, print_stmt->print_val);
#endif
    }

    return stmt;
}

/*
Оператор <- оператор ввода
*/
struct NStmt* create_stmt_read (struct NRead_stmt* read_stmt)
{
    struct NStmt* stmt = NULL;
    char* uniqID;

    if (read_stmt)
    {
        stmt = (struct NStmt*)safeAlloc(sizeof(struct NStmt));
        stmt->read_stmt   = read_stmt;
        stmt->type   = READ_STMT;

#ifdef OUT2DOT
        uniqID = makeUniqueID("create_stmt_read");
        stmt->print_val = uniqID;
        makeNode(uniqID, read_stmt->print_val);
#endif

    }

    return stmt;
}

/*
Оператор <- значение функции как оператор (ключевое слово знач)
*/
struct NStmt* create_stmt_znach (struct NZnach_value* znach_val)
{
    struct NStmt* stmt = NULL;
    char* uniqID;
    if (znach_val)
    {
        stmt = (struct NStmt*)safeAlloc(sizeof(struct NStmt));
        stmt->znach_value = znach_val;
        stmt->type   = ZNACH_VALUE;


#ifdef OUT2DOT
        uniqID = makeUniqueID("create_stmt_znach");
        stmt->print_val = uniqID;
        makeNode(uniqID, znach_val->print_val);
#endif
    }

    return stmt;
}

/*
Оператор <- объявление переменной
*/
struct NStmt* create_stmt_decl(struct NDecl* decl)
{
    struct NStmt* stmt = NULL;
    char* uniqID;
    if (decl)
    {
        stmt = (struct NStmt*)safeAlloc(sizeof(struct NStmt));
        stmt->decl        = decl;
        stmt->type        = DECL;


#ifdef OUT2DOT
        uniqID = makeUniqueID("create_stmt_decl");
        stmt->print_val = uniqID;
        makeNode(uniqID, decl->print_val);
#endif

    }

    return stmt;
}

/*
Вызов функции в коде <- идентификатор функции + аргументы в виде списка выражений
*/
struct NFunction_call* create_function_call(struct NIdentifier* ident, struct NExpr_list* expr_list)
{
    struct NFunction_call* call = NULL;
    char* uniqID;
    call = (struct NFunction_call*)safeAlloc(sizeof(struct NFunction_call));

    call->id = (struct NIdentifier*)safeAlloc(sizeof(struct NIdentifier));
    call->id = ident;

    call->expr_list = expr_list;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_function_call");
    call->print_val = uniqID;
    makeNode(uniqID, ident->print_val);
    makeNode(uniqID, expr_list->print_val);
#endif


    return call;

}

/*
Выражение <- Идентификатор
*/
struct NExpr* create_expr_id(struct NIdentifier* ident)
{
    struct NExpr* expr = NULL;
    char* uniqID;
    expr = (struct NExpr*)safeAlloc(sizeof(struct NExpr));

    expr->left = NULL;
    expr->right = NULL;

    expr->expr_type = ID;
    expr->val.Id = (char*)safeAlloc(strlen(ident->name));
    strcpy(expr->val.Id, ident->name);

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_expr_id");
    expr->print_val = uniqID;
    makeNode(uniqID, ident->print_val);
#endif

    return expr;

}

/*
Выражение <- вызов функции в коде
*/
struct NExpr* create_function_call_expr(struct NFunction_call* function_call)
{
    struct NExpr* expr = NULL;
    char* uniqID;
    expr = (struct NExpr*)safeAlloc(sizeof(struct NExpr));

    expr->expr_type = FUNCTION_CALL;
    expr->func_call = function_call;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_function_call_expr");
    expr->print_val = uniqID;
    makeNode(uniqID, function_call->print_val);
#endif

    return expr;

}

/*
Выражение <- список выражений
*/
struct NExpr* create_brackets_expr(struct NExpr* expr_in)
{
    struct NExpr* expr = NULL;
    char* uniqID;
    expr = (struct NExpr*)safeAlloc(sizeof(struct NExpr));
    expr->subexpr = expr_in;
    expr->expr_type = BRAKETS;


#ifdef OUT2DOT
    uniqID = makeUniqueID("()");
    expr->print_val = uniqID;
    makeNode(uniqID, expr->subexpr->print_val);
#endif

    return expr;
}

/*
Выражение <- тип выражения и его значение
*/
struct NExpr* create_const_expr (enum Const_type type, union Const_values value)
{
    struct NExpr* expr = NULL;
    char* temp = (char*)safeAlloc(ALLOC_SZ);
    char* preudoString;
    char buf[10]= {0};
    expr = (struct NExpr*)safeAlloc(sizeof(struct NExpr));

    expr->left = NULL;
    expr->right = NULL;
    switch (type)
    {
    case Int:
    {
        expr->expr_type = INT_CONST;
        expr->const_type = Int;
        expr->val.Int = value.Int;

        char buf[10]= {};
        sprintf(buf, "%d", value.Int);

        fprintf(locsfile, buf);
        char _0x1F[2];
        _0x1F[0] = '\x1F';
        fprintf(locsfile,_0x1F);
        char buf2[10]= {};
        sprintf(buf2, "%d", yylloc.first_line);
        fprintf(locsfile, buf2);
        fprintf(locsfile, "\n");

#ifdef OUT2DOT
        expr->print_val = (char*)safeAlloc(ALLOC_SZ);
        strcpy(expr->print_val, "Целая константа: ");
        strcat(expr->print_val,itoa(expr->val.Int,buf,10) );
#endif

    }
    break;
    case Bool:
    {
        expr->expr_type = BOOL_CONST;
        expr->const_type = Bool;
        expr->val.Bool = (char*)safeAlloc(4);
        strcpy(expr->val.Bool, value.Bool);

        fprintf(locsfile, value.Bool);
        char _0x1F[2];
        _0x1F[0] = '\x1F';
        fprintf(locsfile,_0x1F);
        char buf[10]= {};
        sprintf(buf, "%d", yylloc.first_line);
        fprintf(locsfile, buf);
        fprintf(locsfile, "\n");
#ifdef OUT2DOT
        expr->print_val = (char*)safeAlloc(ALLOC_SZ);
        strcpy(expr->print_val, strcat_const("Логическая константа: ",expr->val.Bool) );

#endif
    }
    break;
    case Double:
    {
        expr->expr_type = DOUBLE_CONST;
        expr->const_type = Double;
        expr->val.Double = value.Double;

        char buf[10]= {};
        sscanf(buf, "%f", value.Double);

        fprintf(locsfile, buf);
        char _0x1F[2];
        _0x1F[0] = '\x1F';
        fprintf(locsfile,_0x1F);
        char buf2[10]= {};
        sprintf(buf2, "%d", yylloc.first_line);
        fprintf(locsfile, buf2);
        fprintf(locsfile, "\n");

#ifdef OUT2DOT
        expr->print_val = (char*)safeAlloc(ALLOC_SZ);
        sprintf(temp, "%f",expr->val.Double);
        realloc(expr->print_val,strlen(temp));
        strcpy(expr->print_val, "Дробная константа: ");
        strcat(expr->print_val, temp );
#endif

    }
    break;
    case Char:
    {
        expr->expr_type = CHAR_CONST;
        expr->const_type = Char;
        expr->val.Char = value.Char;


        char buf0[10]= {};
        sprintf(buf0, "\"%s\"", value.Char);
        fprintf(locsfile, buf0);

        char _0x1F[2];
        _0x1F[0] = '\x1F';
        fprintf(locsfile,_0x1F);
        char buf[10]= {};
        sprintf(buf, "%d", yylloc.first_line);
        fprintf(locsfile, buf);
        fprintf(locsfile, "\n");

#ifdef OUT2DOT
        expr->print_val = (char*)safeAlloc(ALLOC_SZ);

        preudoString= (char*)safeAlloc(2);
        preudoString[0]=expr->val.Char;
        preudoString[1]=0;
        strcpy(expr->print_val, "Символьная константа: ");
        strcat(expr->print_val, preudoString );

        free(preudoString);
#endif

    }
    break;
    case String:
    {
        expr->expr_type = STRING_CONST;
        expr->const_type = String;
        expr->val.String = value.String;

        char buf0[1024]= {};
        sprintf(buf0, "\"%s\"", value.String);
        fprintf(locsfile, buf0);
        char _0x1F[2];
        _0x1F[0] = '\x1F';
        fprintf(locsfile,_0x1F);
        char buf[10]= {};
        sprintf(buf, "%d", yylloc.first_line);
        fprintf(locsfile, buf);
        fprintf(locsfile, "\n");
#ifdef OUT2DOT
        expr->print_val = (char*)safeAlloc(ALLOC_SZ);
        strcpy(expr->print_val, "Строковая константа: ");
        strcat(expr->print_val, expr->val.String );
#endif
    }
    break;
    case Id:
    {
        expr->expr_type = ID;
        expr->const_type = Id;
        expr->val.Id = value.Id;

#ifdef OUT2DOT
        expr->print_val = (char*)safeAlloc(ALLOC_SZ);

        fprintf(locsfile, value.Id);
        char _0x1F[2];
        /*_0x1F[0] = '\x1F';
        fprintf(locsfile,_0x1F);*/
        char buf[10]= {};
        sprintf(buf, "%d", yylloc.first_line);
        fprintf(locsfile, buf);
        fprintf(locsfile, "\n");

        strcpy(expr->print_val, "Идентификатор: ");
        strcat(expr->print_val, expr->val.Id  );
#endif
    }
    break;
    default:
        ; //fprintf(log, "\n(create_const_expr)Unknown type of expression Type: %d", type);
    }


    return expr;
}


/*
Выражение <- тип выражения, выражение первое, выражение второе
*/
struct NExpr* create_op_expr (enum yytokentype type, struct NExpr* expr1, struct NExpr* expr2)
{
    struct NExpr* expr = NULL;

    expr = (struct NExpr*)safeAlloc(sizeof(struct NExpr));

    expr->left = expr1;
    expr->right = expr2;
    expr->expr_type = type;

#ifdef OUT2DOT
    char* uniqID = makeUniqueID(getName(expr->expr_type));
    expr->print_val = uniqID;
    makeNode(uniqID, expr1->print_val);
    makeNode(uniqID, expr2->print_val);
#endif

    return expr;
}

/*
Выражение <- тип выражения, выражение
*/
struct NExpr* create_unary(int type, struct NExpr* expr)
{
    struct NExpr* result = NULL;
    char * temp;
    result = (struct NExpr*)safeAlloc(sizeof(struct NExpr));

    result->left = NULL;
    result->right = expr;
    result->expr_type = type;


#ifdef OUT2DOT
    result->print_val = (char*)safeAlloc(ALLOC_SZ);
    strcpy(temp, "UnaryMinus");
    char* uniqID =makeUniqueID(temp);
    result->print_val = uniqID;
    makeNode(uniqID, expr->print_val);


#endif

    return result;
}

/*
Создание выражения из обращения к массиву
Выражение <- идентификатор и список выражений
*/
struct NExpr* create_array_expr (struct NIdentifier* id, struct NExpr_list* list)
{
    struct NExpr* result = NULL;
    char* uniqID;
    result = (struct NExpr*)safeAlloc(sizeof(struct NExpr));

    result->expr_type = ID_WITH_INDEXES;
    result->id = id;
    result->list = list;


#ifdef OUT2DOT
    uniqID = makeUniqueID("create_array_expr");
    result->print_val = uniqID;
    makeNode(uniqID, id->print_val);
    makeNode(uniqID, list->print_val);
#endif

    return result;
}



/*
Список выражений <- выражение
*/
struct NExpr_list* create_expr_list(struct NExpr* expr)
{
    struct NExpr_list* list = NULL;
    char* uniqID;
    if (expr)
    {
        list = (struct NExpr_list*)safeAlloc(sizeof(struct NExpr_list));
        list->first = expr;
        list->last = expr;

#ifdef OUT2DOT
        uniqID = makeUniqueID("create_expr_list");
        list->print_val = uniqID;
        makeNode(uniqID, expr->print_val);
#endif
    }
    return list;
}

/*
Добавляет выражение expr к списку выражений expr_list
*/
struct NExpr_list* append_expr_to_list (struct NExpr_list* list, struct NExpr* expr)
{
    char* uniqID;
    if (expr)
    {
        if (list)
        {
            list->last->next = expr;
            list->last = expr;

#ifdef OUT2DOT
            uniqID = makeUniqueID("append_expr_to_list");
            makeNode(list->print_val, uniqID);
            // list->print_val = uniqID;
            makeNode(uniqID, expr->print_val);
#endif

        }
        else
        {
            list = create_expr_list(expr);
        }
    }

    return list;
}

/*
Идентификатор <- текстовое представление
*/
struct NIdentifier* create_ident(char* id)
{
    struct NIdentifier* ident = NULL;
    char* uniqID;
    char* temp_str;
    ident = (struct NIdentifier*)safeAlloc(sizeof(struct NIdentifier));

    ident->name = (char*)safeAlloc( strlen((const char*)id));
    strcpy(ident->name, id);

    fprintf(locsfile, ident->name);
    char _0x1F[2];
    _0x1F[0] = '\x1F';
    fprintf(locsfile,_0x1F);
    char buf[10]= {};
    sprintf(buf, "%d", yylloc.first_line);
    fprintf(locsfile, buf);
    fprintf(locsfile, "\n");


#ifdef OUT2DOT
    uniqID = makeUniqueID("create_ident");
    ident->print_val = uniqID;
    temp_str = strcat_const("\"Идентификатор: ", id);
    strcat(temp_str, "\"");
    makeNode(uniqID, temp_str);
    assert(ident->print_val);
#endif

    return ident;
}
/*
struct NIdentifier* append_ident (struct NIdentifier* complex_id, char* ident) {
if (!ident) {
if (!strlen(complex_id->name)) {
realloc(complex_id->name, strlen(complex_id->name)+strlen((const char*)ident));
strcat(complex_id, " ");
strcat(complex_id, ident);
} else {
complex_id = create_ident(ident);
}
}

return complex_id;
}*/

/*
Объявление <- атомарный тип и список атомарных идентификаторов
*/
struct NDecl* create_from_atomic_decl(struct NAtomic_type* type, struct NEnum_atomic_identifier_list* atomic_ids)
{
    struct NDecl* decl = NULL;
    char * uniqID;
    decl = (struct NDecl*)safeAlloc(sizeof(struct NDecl));
    decl->type_decl = FROM_ATOMIC_TYPE;
    decl->atomic_ids = atomic_ids;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_from_atomic_decl");
    decl->print_val = uniqID;
    makeNode(uniqID, type->print_val);
    makeNode(uniqID, atomic_ids->print_val);
#endif

    return decl;
}

/*
Объявление <- табличный тип и список табличных идентификаторов
*/
struct NDecl* create_from_array_decl(struct NArray_type* type, struct NEnum_array_identifier_list* array_ids)
{
    struct NDecl* decl = NULL;
    char * uniqID;
    decl = (struct NDecl*)safeAlloc(sizeof(struct NDecl));
    decl->type_decl = FROM_ARRAY_TYPE;
    decl->array_ids = array_ids;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_from_array_decl");
    decl->print_val = uniqID;
    makeNode(uniqID, type->print_val);
    makeNode(uniqID, array_ids->print_val);
#endif

    return decl;
}

/*
Функция как оператор <- тип возвращаемого значения (атомарный тип), идентификатор функции,
список параметров, список операторов в теле функции
*/
struct NFunc_stmt* create_func(struct NAtomic_type* type, struct NIdentifier* id, struct NParam_list* param_list, struct NStmt_list* stmt_list)
{

    struct NFunc_stmt* func_stmt = NULL;
    char* uniqID;
    func_stmt = (struct NFunc_stmt*)safeAlloc(sizeof(struct NFunc_stmt));

    func_stmt->ret_type = type;
    func_stmt->id = id;
    func_stmt->param_list = param_list;
    func_stmt->stmt_list = stmt_list;

#ifdef OUT2DOT

    uniqID = makeUniqueID("create_func");
    func_stmt->print_val = uniqID;
    makeNode(uniqID, type->print_val);
    makeNode(uniqID, id->print_val);

    if (param_list != NULL)
        makeNode(uniqID, param_list->print_val);

    if (stmt_list != NULL)
        makeNode(uniqID, stmt_list->print_val);

#endif


    return func_stmt;

}

/*
Процедура как оператор <- идентификатор процедуры, список параметров, список операторов в теле процедуры
*/
struct NProc_stmt* create_proc( struct NIdentifier* id, struct NParam_list* param_list, struct NStmt_list* stmt_list)
{
    struct NProc_stmt* proc_stmt = NULL;

    char * uniqID;

    proc_stmt = (struct NProc_stmt*)safeAlloc(sizeof(struct NProc_stmt));

    proc_stmt->id = id;
    proc_stmt->param_list = param_list;
    proc_stmt->stmt_list = stmt_list;


#ifdef OUT2DOT

    uniqID = makeUniqueID("create_proc");
    proc_stmt->print_val = uniqID;
    makeNode(uniqID, id->print_val);

    if (param_list != NULL)
        makeNode(uniqID, param_list->print_val);

    if (stmt_list != NULL)
        makeNode(uniqID, stmt_list->print_val);

#endif

    return proc_stmt;

}


/*
Параметр(функции или процедуры) <- арг-значение (значение, предваряемое ключевым словом арг)
*/
struct NParam* create_from_arg_rezvalue(struct NArg_value* argvalue)
{
    struct NParam* param = NULL;
    char* uniqID;
    param = (struct NParam*)safeAlloc(sizeof(struct NParam));

    param->type = ARG;
    param->arg_value = argvalue;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_from_arg_rezvalue");
    param->print_val = uniqID;
    makeNode(uniqID, argvalue->print_val);
#endif

    return param;

}

/*
Параметр(функции или процедуры) <- рез-значение (значение, предваряемое ключевым словом рез)
*/
struct NParam* create_from_rez_rezvalue(struct NRez_value* rezvalue)
{
    struct NParam* param = NULL;
    char* uniqID;
    param = (struct NParam*)safeAlloc(sizeof(struct NParam));

    param->type = REZ;
    param->rez_value = rezvalue;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_from_rez_rezvalue");
    param->print_val = uniqID;
    makeNode(uniqID, rezvalue->print_val);
#endif

    return param;
}


/*
Список параметров <- параметр
*/
struct NParam_list* create_param_list (struct NParam* param)
{
    struct NParam_list* list = NULL;
    char* uniqID;
    list = (struct NParam_list*)safeAlloc(sizeof(struct NParam_list));

    list->first = param;
    list->last = param;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_param_list");
    list->print_val = uniqID;
    makeNode(uniqID, param->print_val);
#endif

    return list;
}

/*
Добавление в список параметров ещё одного параметра
*/
struct NParam_list* append_param_to_list (struct NParam_list* param_list, struct NParam* param)
{
    char* uniqID;
    if (param)
    {
        if (param_list)
        {
            //  param_list->last->next = param;
            param_list->last = param;

#ifdef OUT2DOT
            uniqID = makeUniqueID("append_param_to_list");
            makeNode(param_list->print_val, uniqID);
            // param_list->print_val = uniqID;
            makeNode(uniqID, param->print_val);
#endif
        }
        else
        {
            param_list = create_param_list(param);
        }


    }
    return param_list;
}

/*
Знач-значение <- выражение
Знач-значение - выражение вида "знач:=2*3", означающее, что функция должна вернуть "6"
Слово "знач" зарезервировано
*/
struct NZnach_value* create_znachvalue(struct NExpr* expr)
{
    struct NZnach_value* znach = (struct NZnach_value*)safeAlloc(sizeof(struct NZnach_value));
    char* uniqID;
    znach->expr = expr;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_znachvalue");
    znach->print_val = uniqID;
    makeNode(uniqID, expr->print_val);
#endif
    return znach;

}

/*
Арг-значение(параметр-аргумент вида "арг цел bm") <- тип аргумента, идентификатор аргумента[, измерения массива,
в случае передачи табличного типа как параметра (вида [1:10,1:20])]
Создаётся из значения атомарного типа
*/
struct NArg_value* create_arg_from_atomic(struct NAtomic_type* type, struct NEnum_atomic_identifier_list* list)
{
    struct NArg_value* result = NULL;
    char * uniqID;

    result = (struct NArg_value*)safeAlloc(sizeof(struct NArg_value));
    result->type_of_value = FROM_ATOMIC_TYPE;
    result->atomic_type = type;
    result->atomic_list = list;


#ifdef OUT2DOT
    uniqID = makeUniqueID("create_arg");
    result->print_val = uniqID;
    makeNode(uniqID, type->print_val);
    makeNode(uniqID, list->print_val);
#endif

    return result;
}

/*
Арг-значение(параметр-аргумент вида "арг цел bm") <- тип аргумента, идентификатор аргумента[, измерения массива,
в случае передачи табличного типа как параметра (вида [1:10,1:20])]
Создаётся из значения табличного типа
*/
struct NArg_value* create_arg_from_array(struct NArray_type* type, struct NEnum_array_identifier_list* list)
{
    struct NArg_value* result = NULL;
    char * uniqID;

    result = (struct NArg_value*)safeAlloc(sizeof(struct NArg_value));
    result->type_of_value = FROM_ARRAY_TYPE;
    result->array_type = type;
    result->array_list = list;


#ifdef OUT2DOT
    uniqID = makeUniqueID("create_arg");
    result->print_val = uniqID;
    makeNode(uniqID, type->print_val);
    makeNode(uniqID, list->print_val);
#endif

    return result;
}


/*
Рез-значение <- тип параметра,
Рез-значение - параметр, переданный по ссылке
Создаём из атомарного типа
*/
struct NRez_value* create_rez_from_atomic(struct NAtomic_type* type, struct NEnum_atomic_identifier_list* list)
{
    struct NRez_value* result = NULL;
    char * uniqID;

    result = (struct NRez_value*)safeAlloc(sizeof(struct NRez_value));
    result->type_of_value = FROM_ATOMIC_TYPE;
    result->atomic_type = type;
    result->atomic_list = list;


#ifdef OUT2DOT
    uniqID = makeUniqueID("create_rez");
    result->print_val = uniqID;
    makeNode(uniqID, type->print_val);
    makeNode(uniqID, list->print_val);
#endif
    return result;
}


/*
Рез-значение <- тип параметра,
Рез-значение - параметр, переданный по ссылке
Создаём из табличного типа
*/
struct NRez_value* create_rez_from_array(struct NArray_type* type, struct NEnum_array_identifier_list* list)
{

    struct NRez_value* result = NULL;
    char * uniqID;

    result = (struct NRez_value*)safeAlloc(sizeof(struct NRez_value));
    result->type_of_value = FROM_ARRAY_TYPE;
    result->array_type = type;
    result->array_list = list;


#ifdef OUT2DOT
    uniqID = makeUniqueID("create_rez");
    result->print_val = uniqID;
    makeNode(uniqID, type->print_val);
    makeNode(uniqID, list->print_val);
#endif
    return result;

}

/*
Измерение массива <- целое число, целое число
Создаёт измерение массива из целых чисел, то есть [1:13].
Это для объявления массива.
*/
struct NDimensions* create_int_int_dim(int first,int second)
{
    struct NDim* dim = NULL;
    char * uniqID;
    dim = (struct NDim*)safeAlloc(sizeof(struct NDim));
    dim->print_val = (char*)safeAlloc(32);
    dim->type = INT_INT;
    dim->firstINT = first;
    dim->secondINT = second;


    struct NDimensions* result = (struct NDimensions*)safeAlloc(sizeof(struct NDimensions));
    char temp[16];
    sprintf(temp, "[%d:%d]",first,second);

    //result->print_val = makeUniqueID(strcat_const("create_int_int_dim",temp));
    result->print_val = makeUniqueID("create_int_int_dim");
    result->first = dim;
    result->last = dim;

#ifdef OUT2DOT
    char* buf1[10]= {};
    char* buf2[10]= {};
    itoa(first, (char*)buf1, 10);
    itoa(second, (char*)buf2, 10);
    makeNode(result->print_val, strcat_const("Целое число: ", (char*)buf1));
    makeNode(result->print_val, strcat_const("Целое число: ", (char*)buf2));
#endif

    return result;
}

/*
Измерение массива <- целое число, идентификатор
Создаёт измерение массива из целого числа и идентификатора переменной, то есть [1:количество].
Это для объявления массива.
*/
struct NDimensions* create_int_id_dim(int first,struct NIdentifier* second)
{

    struct NDim* dim = NULL;
    char * uniqID;
    dim = (struct NDim*)safeAlloc(sizeof(struct NDim));
    dim->print_val = (char*)safeAlloc(32);
    dim->type = INT_ID;
    dim->firstINT = first;
    dim->secondID = second;


    struct NDimensions* result = (struct NDimensions*)safeAlloc(sizeof(struct NDimensions));
    char temp[16];
    sprintf(temp, "[%d:%s]",first,second->name);

    //result->print_val = makeUniqueID(strcat_const("create_int_int_dim",temp));
    result->print_val = makeUniqueID("create_int_id_dim");
    result->first = dim;
    result->last = dim;

#ifdef OUT2DOT
    char* buf1[10]= {};
    itoa(first, (char*)buf1, 10);
    makeNode(result->print_val, strcat_const("Целое число: ", (char*)buf1));
    makeNode(result->print_val, second->print_val);
#endif

    return result;
}


/*
Добавляет ещё одно измерение к массиву, было [1:10]->стало [1:10, 1:15]
*/
struct NDimensions* append_int_int_dim(struct NDimensions* list, int first, int second)
{
    struct NDim* dim = (struct NDim*)safeAlloc(sizeof(struct NDim));
    char * uniqID;
    dim->type = INT_INT;
    dim->firstINT = first;
    dim->secondINT = second;

    if (list)
    {
        list->last->next = dim;
        list->last = dim;

#ifdef OUT2DOT
        char buf1[8]= {};
        char buf2[8]= {};
        itoa(first, (char*)buf1, 10);
        itoa(second, (char*)buf2, 10);
        uniqID = makeUniqueID("append_int_int_dim");
        makeNode(list->print_val, uniqID);
        // dim->print_val = uniqID;
        makeNode(uniqID, strcat_const("Целое число: ", (char*)buf1));
        makeNode(uniqID, strcat_const("Целое число: ", (char*)buf2));
#endif
    }
    else
    {
        list = create_int_int_dim(first, second);
    }
    return list;
}

/*
Добавляет ещё одно измерение к массиву, было [1:10]->стало [1:10, 1:n]
*/
struct NDimensions* append_int_id_dim(struct NDimensions* list,int first, struct NIdentifier* second)
{

    char * uniqID;
    struct NDim* dim = (struct NDim*)safeAlloc(sizeof(struct NDim));
    dim->type = INT_ID;
    dim->firstINT = first;
    dim->secondID = second;

    if (list)
    {
        list->last->next = dim;
        list->last = dim;
#ifdef OUT2DOT
        char buf[10]= {};
        itoa(first, buf, 10);
        uniqID = makeUniqueID("append_int_id_dim");
        makeNode(list->print_val, uniqID);
        // dim->print_val = uniqID;
        makeNode(uniqID, strcat_const("Целое число: ", buf));
        makeNode(uniqID, second->print_val);

#endif
    }
    else
    {
        list = create_int_id_dim(first, second);
    }

    return list;
}



struct NPrint_stmt* create_expr_list_print(struct NExpr_list* list)
{
    struct NPrint_stmt* print_stmt = NULL;

    char* uniqID;
    print_stmt = (struct NPrint_stmt*) safeAlloc( sizeof(struct NPrint_stmt) );

    print_stmt->list = list;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_expr_list_print");
    print_stmt->print_val = uniqID;
    makeNode(uniqID, list->print_val);

#endif


    return print_stmt;
}


struct NRead_stmt* create_expr_list_read(struct NExpr_list* list)
{
    struct NRead_stmt* read_stmt = NULL;

    char* uniqID;
    read_stmt = (struct NRead_stmt*) safeAlloc( sizeof(struct NRead_stmt) );


    read_stmt->list = list;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_expr_list_read");
    read_stmt->print_val = uniqID;
    makeNode(uniqID, list->print_val);

#endif


    return read_stmt;
}



/*
Атомарный тип <- текстовое представление
*/
struct NAtomic_type*  create_atomic_type(char* name)
{
    char* uniqID;
    struct NAtomic_type* result = (struct NAtomic_type*)safeAlloc(sizeof(struct NAtomic_type));

    result->name = (char*)safeAlloc(ALLOC_SZ);

    strcpy(result->name, name);

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_atomic_type");
    result->print_val = uniqID;
    char* temp_p = strcat_const("Тип: ", name);

    makeNode(uniqID, surroundQuotes(temp_p));

#endif


    return result;
}

/*
Табличный тип <- текстовое представление
*/
struct NArray_type*  create_array_type(char* name)
{

    struct NArray_type* result = (struct NArray_type*)safeAlloc(sizeof(struct NArray_type));
    char* uniqID;
    result->name = (char*)safeAlloc(ALLOC_SZ);

    strcpy(result->name, name);

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_array_type");
    result->print_val = uniqID;
    makeNode(uniqID, strcat_const("Тип: ", name));
#endif

    return result;
}


/*
Список идентификаторов атомарного типа <- идентификатор
*/
struct NEnum_atomic_identifier_list* create_enum_atomic_identifier_list(struct NIdentifier* id)
{

    struct NEnum_atomic_identifier_list* list = (struct NEnum_atomic_identifier_list*)safeAlloc(sizeof(struct NEnum_atomic_identifier_list));
    char* uniqID;

    struct NEnum_atomic_identifier* firstId = (struct NEnum_atomic_identifier*)safeAlloc(sizeof(struct NEnum_atomic_identifier));


    firstId->id = id;

    firstId->print_val = (char*)safeAlloc(ALLOC_SZ);
    list->print_val = (char*)safeAlloc(ALLOC_SZ);

    strcpy(firstId->print_val, id->print_val);
    strcpy(list->print_val, firstId->print_val);

    list->first = firstId;
    list->last = firstId;
#ifdef OUT2DOT
    uniqID = makeUniqueID("create_enum_atomic_identifier_list");
    list->print_val = uniqID;
    makeNode(uniqID, id->print_val);
#endif

    return list;
}

/*
Список идентификаторов табличного типа <- идентификатор, измерения массива
*/
struct NEnum_array_identifier_list* create_enum_array_identifier_list(struct NIdentifier* id, struct NDimensions* dimensions)
{

    char* uniqID;
    struct NEnum_array_identifier_list* list = (struct NEnum_array_identifier_list*)safeAlloc(sizeof(struct NEnum_array_identifier_list));


    struct NEnum_array_identifier* firstId = (struct NEnum_array_identifier*)safeAlloc(sizeof(struct NEnum_array_identifier));


    firstId->id = id;
    firstId->dimensions = dimensions;

    firstId->print_val = (char*)safeAlloc(ALLOC_SZ);
    list->print_val = (char*)safeAlloc(ALLOC_SZ);

    list->first = firstId;
    list->last = firstId;

#ifdef OUT2DOT
    uniqID = makeUniqueID("create_enum_array_identifier_list");
    // firstId->print_val = uniqID;
    makeNode(uniqID, id->print_val);
    makeNode(uniqID, dimensions->print_val);

    strcpy(list->print_val,  uniqID);

    char* temp[32];
    strcpy((char*)temp,id->print_val);
    strcat((char*)temp,dimensions->print_val);

    strcpy(firstId->print_val, (char*)temp);
#endif



    return list;
}

/*
Добавление в список атомарных идентификаторов нового идентификатора
*/
struct NEnum_atomic_identifier_list* append_enum_atomic_identifier_list(struct NEnum_atomic_identifier_list* list, struct NIdentifier* id)
{
    char* uniqID;
    struct NEnum_atomic_identifier* tempAtomicId = (struct NEnum_atomic_identifier*)safeAlloc(sizeof(struct NEnum_atomic_identifier));
    tempAtomicId->print_val = (char*)safeAlloc(ALLOC_SZ);
    if (id)
    {
        if (list)
        {
            tempAtomicId->id = id;
            strcpy(tempAtomicId->print_val, id->print_val);
            list->last->next = tempAtomicId;
            list->last = tempAtomicId;

#ifdef OUT2DOT
            uniqID = makeUniqueID("append_enum_atomic_identifier_list");
            makeNode(list->print_val, uniqID);
            // list->print_val = uniqID;
            makeNode(uniqID, tempAtomicId->print_val);
#endif


        }
        else
        {
            list = create_enum_atomic_identifier_list(id);
        }
    }


    return list;
}

/*
Добавление в список табличных идентификаторов нового идентификатора
*/
struct NEnum_array_identifier_list* append_enum_array_identifier_list(struct NEnum_array_identifier_list* list,
        struct NIdentifier* id, struct NDimensions * dimensions)
{

    char* uniqID;
    struct NEnum_array_identifier* tempArrayId = (struct NEnum_array_identifier*)safeAlloc(sizeof(struct NEnum_array_identifier));

    if (id)
    {
        if (list)
        {
            tempArrayId->print_val = (char*)safeAlloc(ALLOC_SZ);
            tempArrayId->id = id;
            strcpy(tempArrayId->print_val, id->print_val);
            list->last->next = tempArrayId;
            list->last->dimensions = dimensions;
            list->last = tempArrayId;


#ifdef OUT2DOT
            uniqID = makeUniqueID("append_enum_array_identifier_list");
            // list->print_val = uniqID;
            makeNode(list->print_val, uniqID);
            makeNode(uniqID, tempArrayId->print_val);
#endif

        }
        else
        {
            list = create_enum_array_identifier_list(id, dimensions);
        }
    }

    return list;
}

