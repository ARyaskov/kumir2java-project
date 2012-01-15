/* == Описание структур для хранения узлов синтаксического дерева == */
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
FILE * locsfile;

int hashes[HASH_ARRAY_SZ];
int hash_pointer;


#define MAX_NODE_NUM 8192
#define MAX_ALLOCS_NUM 65536


void * safeAllocArray[POOL_ARRAY_SZ];

int safeAllocPointer;

int prevSalt;

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
    ID_WITH_INDEXES,
	BRAKETS,
	UNARY
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
    int      expr_type;
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
    /* Указатель на хранимое подвыражение (например, после взятия в скобки)*/
    struct NExpr*        subexpr;
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
    struct NEnum_atomic_identifier_list* atomic_ids;
    /* Список табличных идентификаторов, если табличный тип*/
    struct NEnum_array_identifier_list* array_ids;

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


#endif
