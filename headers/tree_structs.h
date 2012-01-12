#pragma once

#ifndef _TREE_STRUCTS
#define _TREE_STRUCTS

#include <stdlib.h>
#include <stdio.h>

#include <locale.h>
#include <string.h>
#include <time.h>
#include <assert.h>


#define OUT2DOT 1
#define ALLOC_SZ 512
#define POOL_ARRAY_SZ 2000
#define HASH_ARRAY_SZ 1000

char* before_val;
FILE * dotfile;
FILE * logfile;

int hashes[HASH_ARRAY_SZ];
int hash_pointer;

int prevSalt;

#define MAX_NODE_NUM 8192
#define MAX_ALLOCS_NUM 65536


void * safeAllocArray[POOL_ARRAY_SZ];
int safeAllocPointer;



/* Тип, из которого создаётся объявление */
enum ETypeDecl
{
    FROM_ATOMIC_TYPE,
    FROM_ARRAY_TYPE
};

/* Объединение, хранящее значение выражения */
union Const_values
{
    int    Int;
    char*  Bool;
    double Double;
    char   Char;
    char*  String;
    char*  Id;
};

/* Тип константного выражения */
enum Const_type
{
    Int,
    Double,
    Char,
    String,
    Id,
    Bool
};

/* Тип оператора */
enum Stmt_type
{
    ENDLINE,
    EXPR,
    EXPR_LIST,
    FUNC_STMT,
    PROC_STMT,
    PRINT_STMT,
    READ_STMT,
    ZNACH_VALUE,
    DECL
};

/* Дополнительный тип выражения */
enum Additional_Expr_types
{
    FUNCTION_CALL,
    EXPRLIST,
    ID_WITH_INDEXES
};

/* Режим индексов в объявлении массива */
enum EDimensionType
{
    /* Оба индекса - целые числа (массив[1:10]) */
    INT_INT,
    /* Второй индекс - переменная (массив[1:N])*/
    INT_ID
};


/* Символ program - программа (корень) */
struct NProgram
{
    // Список операторов stmt_list
    struct NStmt_list* stmt_list;
#ifdef OUT2DOT
// Здесь и далее - значение для печати в узле дерева
    char* print_val;
#endif
};

/* Символ identifier - идентификатор */
struct NIdentifier
{
    // Строка, из которой создаётся идентификатор
    char* name;
#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Символ stmt - оператор*/
struct NStmt
{
    // Тип оператора
    enum Stmt_type  type;
    // Указатель на выражение
    struct NExpr*   expr;
    // Указатель на список выражений
    struct NExpr_list* expr_list;
    // Указатель на функцию
    struct NFunc_stmt* func_stmt;
    // Указатель на процедуру
    struct NProc_stmt* proc_stmt;
    // Указатель на оператор печати
    struct NPrint_stmt* print_stmt;
    // Указатель на оператор ввода
    struct NRead_stmt* read_stmt;
    // Указатель на возвращаемое значение функции
    struct NZnach_value* znach_value;
    // Указатель на объявление переменной
    struct NDecl* decl;
    // Указатель на следующий оператор в списке
    struct NStmt*  next;

#ifdef OUT2DOT
    char* print_val;
#endif
};



/* Символ stmt_list - список операторов*/
struct NStmt_list
{
    /* Первый в списке оператор */
    struct NStmt* first;
    /* Последний в списке оператор */
    struct NStmt* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Символ expr_list - список выражений */
struct NExpr_list
{
    /* Первое выражение в списке*/
    struct NExpr* first;
    /* Последнее выражение в списке*/
    struct NExpr* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Символ expr - выражение */
struct NExpr
{
    /* Тип выражения */
    enum yytokentype       expr_type;
    /* Тип константы (если хранится константа) */
    enum Const_type      const_type;
    /* Значение константы (если хранится константа) */
    union Const_values         val;

    /* Указатель на левое выражение (если есть)*/
    struct NExpr*        left;
    /* Указатель на правое выражение (если есть)*/
    struct NExpr*        right;
    /* Указатель на список выражений (если хранится список)*/
    struct NExpr_list*   list;
    /* Указатель на следующее выражение*/
    struct NExpr*        next;
    /* Указатель на вызов функции (если хранится вызов функции) */
    struct NFunction_call* func_call;
    /* Указатель на идентификатор (если хранится идентификатор)*/
    struct NIdentifier*  id;
#ifdef OUT2DOT
    char* print_val;
#endif

};


/* Символ decl - объявление */
struct NDecl
{
    /* Тип объявления - из атомарного или табличного типа*/
    enum ETypeDecl type_decl;
    /* Список атомарных идентификаторов, если атомарный тип*/
    struct NEnum_atomic_identifiers* atomic_ids;
    /* Список табличных идентификаторов, если табличный тип*/
    struct NEnum_array_identifiers* array_ids;

#ifdef OUT2DOT
    char* print_val;
#endif

};

/* Символ func_stmt - определение функции как оператор */
struct NFunc_stmt
{
    /* Возвращаемое значение (atomic_type) */
    struct NAtomic_type* ret_type;
    /* Список параметров (param_list) */
    struct NParam_list* param_list;
    /* Список операторов тела функции (stmt_list) */
    struct NStmt_list* stmt_list;
    /* Идентификатор функции */
    struct NIdentifier* id;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Символ proc_stmt - определение процедуры как оператор */
struct NProc_stmt
{
    /* Список параметров процедуры (param_list)*/
    struct NParam_list* param_list;
    /* Список операторов тела процедуры (stmt_list)*/
    struct NStmt_list* stmt_list;
    /* Идентификатор процедуры*/
    struct NIdentifier* id;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Символ param_list - список параметров*/
struct NParam_list
{
    /* Первый параметр в списке */
    struct NParam* first;
    /* Последний параметр в списке */
    struct NParam* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};


/* Символ param - параметр */
struct NParam
{
    /*Тип параметра - ARG (обычный аргумент)
      или REZ (параметр по ссылке)*/
    int type;
    /* Указатель на арг-параметр в первом случае*/
    struct NArg_value* arg_value;
    /* Указатель на рез-параметр во втором случае*/
    struct NRez_value* rez_value;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Символ znach_value - присвоение возвращаемого значения функции*/
struct NZnach_value
{
    /* Выражение, присваиваемое "знач" */
    struct NExpr* expr;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Символ arg_value - арг-параметр (обычный аргумент)*/
struct NArg_value
{
    /* Тип арг-параметра - атомарный или табличный*/
    enum ETypeDecl type_of_value;
    /* Атомарный тип */
    struct NAtomic_type* atomic_type;
    /* Табличный тип */
    struct NArray_type* array_type;

    /* Список идентификаторов атомарного типа*/
    struct NEnum_atomic_identifier_list* atomic_list;
    /* Список идентификаторов табличного типа*/
    struct NEnum_array_identifier_list* array_list;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Символ rez_value - рез-параметр (выходной параметр, параметр по ссылке)*/
struct NRez_value
{
    /* Тип рез-параметра - атомарный или табличный*/
    enum ETypeDecl type_of_value;
    /* Атомарный тип */
    struct NAtomic_type* atomic_type;
    /* Табличный тип */
    struct NArray_type* array_type;
    /* Список идентификаторов атомарного типа*/
    struct NEnum_atomic_identifier_list* atomic_list;
    /* Список идентификаторов табличного типа*/
    struct NEnum_array_identifier_list* array_list;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Вспомогательный тип - одно измерение массива */
struct NDim
{
    /* Режим индексов */
    enum EDimensionType type;

    /* При INT_ID - второй индекс-переменная*/
    struct NIdentifier* secondID;

    /* При INT_INT - первый индекс*/
    int firstINT;
    /* При INT_INT - второй индекс*/
    int secondINT;

    /* Следующее измерение*/
    struct NDim* next;

#ifdef OUT2DOT
    char* print_val;
#endif

};

/* Символ dimensions - измерения массива */
struct NDimensions
{
    /* Первое измерение */
    struct NDim* first;
    /* Последнее измерение*/
    struct NDim* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};


/* Символ read_stmt - оператор ввода*/
struct NRead_stmt
{
    /* Выражение, в результат которого следует ввести значение*/
    struct NExpr_list* list;
#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Символ print_stmt - оператор вывода*/
struct NPrint_stmt
{
    /* Выражение, результат которого следует вывести */
    struct NExpr_list* list;
#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Символ function_call - вызов функции */
struct NFunction_call
{
    /* Идентификатор функции*/
    struct NIdentifier* id;
    /* Список передаваемых аргументов */
    struct NExpr_list* expr_list;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* Символ atomic_type - атомарный тип*/
struct NAtomic_type
{
    /* Имя типа */
    char * name;

#ifdef OUT2DOT
    char* print_val;
#endif

};

/* Символ array_type - табличный тип*/
struct NArray_type
{
    /* Имя типа*/
    char * name;

#ifdef OUT2DOT
    char* print_val;
#endif

};

/* Атомарный идентификатор*/
struct NEnum_atomic_identifier
{
    /* Идентификатор*/
    struct NIdentifier* id;
    /* Следующий атомарный идентификатор */
    struct NEnum_atomic_identifier * next;

#ifdef OUT2DOT
    char* print_val;
#endif

};
/* Табличный идентификатор*/
struct NEnum_array_identifier
{

    /* Идентификатор*/
    struct NIdentifier* id;
    /* Измерения*/
    struct NDimensions* dimensions;
    /* Следующий табличный идентификатор*/
    struct NEnum_array_identifier * next;

#ifdef OUT2DOT
    char* print_val;
#endif


};

/* Символ enum_atomic_identifiers - список атомарных идентификаторов*/
struct NEnum_atomic_identifier_list
{
    /* Первый идентификатор*/
    struct NEnum_atomic_identifier * first;
    /* Последний идентификатор*/
    struct NEnum_atomic_identifier * last;

#ifdef OUT2DOT
    char* print_val;
#endif


};
/* Символ enum_array_identifiers - список табличных идентификаторов*/
struct NEnum_array_identifier_list
{
    /*Первый идентификатор*/
    struct NEnum_array_identifier * first;
    /* Последний идентификатор*/
    struct NEnum_array_identifier * last;

#ifdef OUT2DOT
    char* print_val;
#endif

};

/** ==================================Функции========================================= */


void*                                safeAlloc(unsigned int bytes);
char*                                cutQuotes(char* str);
char*                                makeHashString();
char*                                getName(enum yytokentype type);
char*                                strcat_const(const char* const_str, char* buf);
void                                 makeNode(char* from, char* to);
char*                                makeUniqueID(const char* name_of_proc);
void                                 garbageCollector();
struct NProgram*                     create_program (struct NStmt_list* list);
struct NStmt_list*                   create_stmt_list (struct NStmt* stmt);
struct NStmt_list*                   append_stmt_to_list (struct NStmt_list* list, struct NStmt* stmt);
struct NStmt*                        create_stmt_expr (struct NExpr* expr);
struct NStmt*                        create_stmt_expr_list(struct NExpr_list* list);
struct NStmt*                        create_stmt_func (struct NFunc_stmt* func_stmt);
struct NStmt*                        create_stmt_proc (struct NProc_stmt* proc_stmt);
struct NStmt*                        create_stmt_print (struct NPrint_stmt* print_stmt);
struct NStmt*                        create_stmt_read (struct NRead_stmt* read_stmt);
struct NStmt*                        create_stmt_znach (struct NZnach_value* znach_stmt);
struct NStmt*                        create_stmt_decl(struct NDecl* decl);
struct NFunction_call*               create_function_call(struct NIdentifier* ident, struct NExpr_list* expr_list);
struct NExpr*                        create_expr_id(struct NIdentifier* ident);
struct NExpr*                        create_function_call_expr(struct NFunction_call* function_call);
struct NExpr*                        create_exprlist_expr(struct NExpr_list* list);
struct NExpr*                        create_const_expr (enum Const_type type, union Const_values value);
struct NExpr*                        create_op_expr (enum yytokentype type, struct NExpr* expr1, struct NExpr* expr2);
struct NExpr*                        create_unary(int type, struct NExpr* expr);
struct NExpr*                        create_array_expr (struct NIdentifier* id, struct NExpr_list* list);
struct NExpr_list*                   create_expr_list(struct NExpr* expr);
struct NExpr_list*                   append_expr_to_list (struct NExpr_list* list, struct NExpr* expr);
struct NIdentifier*                  create_ident(char* id);
struct NDecl*                        create_from_atomic_decl(struct NAtomic_type* type, struct NEnum_atomic_identifier_list* atomic_ids);
struct NDecl*                        create_from_array_decl(struct NArray_type* type, struct NEnum_array_identifier_list* array_ids);
struct NFunc_stmt*                   create_func(struct NAtomic_type* type, struct NIdentifier* id, struct NParam_list* param_list, struct NStmt_list* stmt_list);
struct NProc_stmt*                   create_proc( struct NIdentifier* id, struct NParam_list* param_list, struct NStmt_list* stmt_list);
struct NParam*                       create_from_arg_rezvalue(struct NArg_value* argvalue);
struct NParam*                       create_from_rez_rezvalue(struct NRez_value* rezvalue);
struct NParam_list*                  create_param_list (struct NParam* param);
struct NParam_list*                  append_param_to_list (struct NParam_list* param_list, struct NParam* param);
struct NZnach_value*                 create_znachvalue(struct NExpr* expr);
struct NArg_value*                   create_arg(struct NAtomic_type* type, struct NIdentifier* id, struct NDimensions* dims);
struct NRez_value*                   create_rez(struct NAtomic_type* type, struct NIdentifier* id, struct NDimensions* dims);
struct NDimensions*                         create_int_int_dim(int first,int second);
struct NDimensions*                         create_int_id_dim(int first,struct NIdentifier* second);
struct NDimensions*                  append_int_int_dim(struct NDimensions* list, int first, int second);
struct NDimensions*                  append_int_id_dim(struct NDimensions* list,int first,struct NIdentifier* second);
struct NPrint_stmt*                  create_str_print(char* str);
struct NPrint_stmt*                  create_int_print(int value);
struct NPrint_stmt_list*             append_str_print(struct NPrint_stmt_list* list, char* str);
struct NPrint_stmt_list*             append_int_print(struct NPrint_stmt_list* list, int value);
struct NAtomic_type*                 create_atomic_type(char* name);
struct NArray_type*                  create_array_type(char* name);
struct NType*                        create_type_from_atomic(struct NAtomic_type* atomic_type);
struct NType*                        create_type_from_array(struct NArray_type* arr_type);
struct NEnum_atomic_identifier_list* create_enum_atomic_identifier_list(struct NIdentifier* id);
struct NEnum_array_identifier_list*  create_enum_array_identifier_list(struct NIdentifier* id, struct NDimensions* dimensions);
struct NEnum_atomic_identifier_list* append_enum_atomic_identifier_list(struct NEnum_atomic_identifier_list* list, struct NIdentifier* id);
struct NEnum_array_identifier_list*  append_enum_array_identifier_list(struct NEnum_array_identifier_list* list, struct NIdentifier* id, struct NDimensions * dimensions);
#endif
