/* == �������� �������� ��� �������� ����� ��������������� ������ == */
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

/* ���, �� �������� �������� ���������� */
enum ETypeDecl
{
    FROM_ATOMIC_TYPE,
    FROM_ARRAY_TYPE
};

/* �����������, �������� �������� ��������� */
union Const_values
{
    int    Int;
    char*  Bool;
    double Double;
    char   Char;
    char*  String;
    char*  Id;
};

/* ��� ������������ ��������� */
enum Const_type
{
    Int,
    Double,
    Char,
    String,
    Id,
    Bool
};

/* ��� ��������� */
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

/* �������������� ��� ��������� */
enum Additional_Expr_types
{
    FUNCTION_CALL,
    EXPRLIST,
    ID_WITH_INDEXES,
	BRAKETS,
	UNARY
};

/* ����� �������� � ���������� ������� */
enum EDimensionType
{
    /* ��� ������� - ����� ����� (������[1:10]) */
    INT_INT,
    /* ������ ������ - ���������� (������[1:N])*/
    INT_ID
};


/* ������ program - ��������� (������) */
struct NProgram
{
    // ������ ���������� stmt_list
    struct NStmt_list* stmt_list;
#ifdef OUT2DOT
// ����� � ����� - �������� ��� ������ � ���� ������
    char* print_val;
#endif
};

/* ������ identifier - ������������� */
struct NIdentifier
{
    // ������, �� ������� �������� �������������
    char* name;
#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ������ stmt - ��������*/
struct NStmt
{
    // ��� ���������
    enum Stmt_type  type;
    // ��������� �� ���������
    struct NExpr*   expr;
    // ��������� �� ������ ���������
    struct NExpr_list* expr_list;
    // ��������� �� �������
    struct NFunc_stmt* func_stmt;
    // ��������� �� ���������
    struct NProc_stmt* proc_stmt;
    // ��������� �� �������� ������
    struct NPrint_stmt* print_stmt;
    // ��������� �� �������� �����
    struct NRead_stmt* read_stmt;
    // ��������� �� ������������ �������� �������
    struct NZnach_value* znach_value;
    // ��������� �� ���������� ����������
    struct NDecl* decl;
    // ��������� �� ��������� �������� � ������
    struct NStmt*  next;

#ifdef OUT2DOT
    char* print_val;
#endif
};



/* ������ stmt_list - ������ ����������*/
struct NStmt_list
{
    /* ������ � ������ �������� */
    struct NStmt* first;
    /* ��������� � ������ �������� */
    struct NStmt* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ������ expr_list - ������ ��������� */
struct NExpr_list
{
    /* ������ ��������� � ������*/
    struct NExpr* first;
    /* ��������� ��������� � ������*/
    struct NExpr* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ������ expr - ��������� */
struct NExpr
{
    /* ��� ��������� */
    int      expr_type;
    /* ��� ��������� (���� �������� ���������) */
    enum Const_type      const_type;
    /* �������� ��������� (���� �������� ���������) */
    union Const_values         val;

    /* ��������� �� ����� ��������� (���� ����)*/
    struct NExpr*        left;
    /* ��������� �� ������ ��������� (���� ����)*/
    struct NExpr*        right;
    /* ��������� �� ������ ��������� (���� �������� ������)*/
    struct NExpr_list*   list;
    /* ��������� �� ��������� ���������*/
    struct NExpr*        next;
    /* ��������� �� �������� ������������ (��������, ����� ������ � ������)*/
    struct NExpr*        subexpr;
    /* ��������� �� ����� ������� (���� �������� ����� �������) */
    struct NFunction_call* func_call;
    /* ��������� �� ������������� (���� �������� �������������)*/
    struct NIdentifier*  id;
#ifdef OUT2DOT
    char* print_val;
#endif

};


/* ������ decl - ���������� */
struct NDecl
{
    /* ��� ���������� - �� ���������� ��� ���������� ����*/
    enum ETypeDecl type_decl;
    /* ������ ��������� ���������������, ���� ��������� ���*/
    struct NEnum_atomic_identifier_list* atomic_ids;
    /* ������ ��������� ���������������, ���� ��������� ���*/
    struct NEnum_array_identifier_list* array_ids;

#ifdef OUT2DOT
    char* print_val;
#endif

};

/* ������ func_stmt - ����������� ������� ��� �������� */
struct NFunc_stmt
{
    /* ������������ �������� (atomic_type) */
    struct NAtomic_type* ret_type;
    /* ������ ���������� (param_list) */
    struct NParam_list* param_list;
    /* ������ ���������� ���� ������� (stmt_list) */
    struct NStmt_list* stmt_list;
    /* ������������� ������� */
    struct NIdentifier* id;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ������ proc_stmt - ����������� ��������� ��� �������� */
struct NProc_stmt
{
    /* ������ ���������� ��������� (param_list)*/
    struct NParam_list* param_list;
    /* ������ ���������� ���� ��������� (stmt_list)*/
    struct NStmt_list* stmt_list;
    /* ������������� ���������*/
    struct NIdentifier* id;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ������ param_list - ������ ����������*/
struct NParam_list
{
    /* ������ �������� � ������ */
    struct NParam* first;
    /* ��������� �������� � ������ */
    struct NParam* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};


/* ������ param - �������� */
struct NParam
{
    /*��� ��������� - ARG (������� ��������)
      ��� REZ (�������� �� ������)*/
    int type;
    /* ��������� �� ���-�������� � ������ ������*/
    struct NArg_value* arg_value;
    /* ��������� �� ���-�������� �� ������ ������*/
    struct NRez_value* rez_value;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ������ znach_value - ���������� ������������� �������� �������*/
struct NZnach_value
{
    /* ���������, ������������� "����" */
    struct NExpr* expr;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ������ arg_value - ���-�������� (������� ��������)*/
struct NArg_value
{
    /* ��� ���-��������� - ��������� ��� ���������*/
    enum ETypeDecl type_of_value;
    /* ��������� ��� */
    struct NAtomic_type* atomic_type;
    /* ��������� ��� */
    struct NArray_type* array_type;

    /* ������ ��������������� ���������� ����*/
    struct NEnum_atomic_identifier_list* atomic_list;
    /* ������ ��������������� ���������� ����*/
    struct NEnum_array_identifier_list* array_list;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ������ rez_value - ���-�������� (�������� ��������, �������� �� ������)*/
struct NRez_value
{
    /* ��� ���-��������� - ��������� ��� ���������*/
    enum ETypeDecl type_of_value;
    /* ��������� ��� */
    struct NAtomic_type* atomic_type;
    /* ��������� ��� */
    struct NArray_type* array_type;
    /* ������ ��������������� ���������� ����*/
    struct NEnum_atomic_identifier_list* atomic_list;
    /* ������ ��������������� ���������� ����*/
    struct NEnum_array_identifier_list* array_list;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ��������������� ��� - ���� ��������� ������� */
struct NDim
{
    /* ����� �������� */
    enum EDimensionType type;

    /* ��� INT_ID - ������ ������-����������*/
    struct NIdentifier* secondID;

    /* ��� INT_INT - ������ ������*/
    int firstINT;
    /* ��� INT_INT - ������ ������*/
    int secondINT;

    /* ��������� ���������*/
    struct NDim* next;

#ifdef OUT2DOT
    char* print_val;
#endif

};

/* ������ dimensions - ��������� ������� */
struct NDimensions
{
    /* ������ ��������� */
    struct NDim* first;
    /* ��������� ���������*/
    struct NDim* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};


/* ������ read_stmt - �������� �����*/
struct NRead_stmt
{
    /* ���������, � ��������� �������� ������� ������ ��������*/
    struct NExpr_list* list;
#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ������ print_stmt - �������� ������*/
struct NPrint_stmt
{
    /* ���������, ��������� �������� ������� ������� */
    struct NExpr_list* list;
#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ������ function_call - ����� ������� */
struct NFunction_call
{
    /* ������������� �������*/
    struct NIdentifier* id;
    /* ������ ������������ ���������� */
    struct NExpr_list* expr_list;

#ifdef OUT2DOT
    char* print_val;
#endif
};

/* ������ atomic_type - ��������� ���*/
struct NAtomic_type
{
    /* ��� ���� */
    char * name;

#ifdef OUT2DOT
    char* print_val;
#endif

};

/* ������ array_type - ��������� ���*/
struct NArray_type
{
    /* ��� ����*/
    char * name;

#ifdef OUT2DOT
    char* print_val;
#endif

};

/* ��������� �������������*/
struct NEnum_atomic_identifier
{
    /* �������������*/
    struct NIdentifier* id;
    /* ��������� ��������� ������������� */
    struct NEnum_atomic_identifier * next;

#ifdef OUT2DOT
    char* print_val;
#endif

};
/* ��������� �������������*/
struct NEnum_array_identifier
{

    /* �������������*/
    struct NIdentifier* id;
    /* ���������*/
    struct NDimensions* dimensions;
    /* ��������� ��������� �������������*/
    struct NEnum_array_identifier * next;

#ifdef OUT2DOT
    char* print_val;
#endif


};

/* ������ enum_atomic_identifiers - ������ ��������� ���������������*/
struct NEnum_atomic_identifier_list
{
    /* ������ �������������*/
    struct NEnum_atomic_identifier * first;
    /* ��������� �������������*/
    struct NEnum_atomic_identifier * last;

#ifdef OUT2DOT
    char* print_val;
#endif


};
/* ������ enum_array_identifiers - ������ ��������� ���������������*/
struct NEnum_array_identifier_list
{
    /*������ �������������*/
    struct NEnum_array_identifier * first;
    /* ��������� �������������*/
    struct NEnum_array_identifier * last;

#ifdef OUT2DOT
    char* print_val;
#endif

};


#endif
