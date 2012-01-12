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




enum ETypeDecl
{
    FROM_ATOMIC_TYPE,
    FROM_ARRAY_TYPE
};

union Const_values
{
    int    Int;
    char*  Bool;
    double Double;
    char   Char;
    char*  String;
    char*  Id;
};


enum Const_type
{
    Int,
    Double,
    Char,
    String,
    Id,
    Bool
};

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


enum Additional_Expr_types
{
    FUNCTION_CALL,
    EXPRLIST,
    ID_WITH_INDEXES
};

struct NProgram
{
    struct Stmt_list* stmt_list;
#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NIdentifier
{
    char* name;
#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NStmt
{
    enum Stmt_type  type;
    struct NExpr*   expr;
    struct NExpr_list* expr_list;
    struct NFunc_stmt* func_stmt;
    struct NProc_stmt* proc_stmt;
    struct NPrint_stmt* print_stmt;
    struct NRead_stmt* read_stmt;
    struct NZnach_value* znach_value;
    struct NDecl* decl;
    struct NStmt*  next;




#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NType
{

    enum ETypeDecl type;

    char* name;

#ifdef OUT2DOT
    char* print_val;
#endif

};

struct NAtomicType
{

    char* name;

#ifdef OUT2DOT
    char* print_val;
#endif

};

struct NArrayType
{

    char* name;

#ifdef OUT2DOT
    char* print_val;
#endif

};


struct NStmt_list
{
    struct NStmt* first;
    struct NStmt* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NExpr_list
{
    struct NExpr* first;
    struct NExpr* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};


struct NExpr
{
    enum yytokentype       expr_type;
    enum Const_type      const_type;
    union Const_values         val;

    struct NExpr*        left;
    struct NExpr*        right;
    struct NExpr_list*   list;
    struct NExpr*        next;

    struct NFunction_call* func_call;
    struct NIdentifier*  id;
#ifdef OUT2DOT
    char* print_val;
#endif

};



struct NDecl
{
    enum ETypeDecl type_decl;
    struct NType* type;
    struct NEnum_atomic_identifiers* atomic_ids;
    struct NEnum_array_identifiers* array_ids;

#ifdef OUT2DOT
    char* print_val;
#endif

};


struct NFunc_stmt
{
    struct NType* ret_type;
    struct NParam_list* param_list;
    struct NStmt_list* stmt_list;
    struct NIdentifier* id;

#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NProc_stmt
{
    struct NParam_list* param_list;
    struct NStmt_list* stmt_list;
    struct NIdentifier* id;

#ifdef OUT2DOT
    char* print_val;
#endif
};


struct NParam_list
{
    struct NParam* first;
    struct NParam* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};




struct NParam
{
    int type;
    struct NArg_value* arg_value;
    struct NRez_value* rez_value;

#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NZnach_value
{
    struct NExpr* expr;

#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NArg_value
{
    enum ETypeDecl type_of_value;
    struct NAtomic_type* atomic_type;
    struct NArray_type* array_type;

    struct NEnum_atomic_identifier_list* atomic_list;
    struct NEnum_array_identifier_list* array_list;

#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NRez_value
{
    enum ETypeDecl type_of_value;
    struct NAtomic_type* atomic_type;
    struct NArray_type* array_type;

    struct NEnum_atomic_identifier_list* atomic_list;
    struct NEnum_array_identifier_list* array_list;

#ifdef OUT2DOT
    char* print_val;
#endif
};

enum EDimensionType
{
    INT_INT,
    INT_ID,
    ID_INT,
    ID_ID
};

struct NDim
{
    enum EDimensionType type;

    struct NIdentifier* firstID;
    struct NIdentifier* secondID;

    int firstINT;
    int secondINT;

    struct NDim* next;

#ifdef OUT2DOT
    char* print_val;
#endif

};

struct NDimensions
{
    struct NDim* first;
    struct NDim* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};



struct NRead_stmt
{
    char* var;
    struct NRead_stmt* next;
struct NExpr_list* list;
#ifdef OUT2DOT
    char* print_val;
#endif
};


struct NRead_stmt_list
{
    struct NRead_stmt* first;
    struct NRead_stmt* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};
struct NPrint_stmt_list
{
    struct NPrint_stmt* first;
    struct NPrint_stmt* last;

#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NPrint_stmt
{
    char* var;
    struct NExpr_list* list;

    struct NPrint_stmt* next;

#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NFunction_call
{
    struct NIdentifier* id;
    struct NExpr_list* expr_list;

#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NZnach_stmt
{

    struct NExpr* expr;

#ifdef OUT2DOT
    char* print_val;
#endif
};

struct NAtomic_type
{
    char * name;

#ifdef OUT2DOT
    char* print_val;
#endif

};

struct NArray_type
{
    char * name;

#ifdef OUT2DOT
    char* print_val;
#endif

};


struct NEnum_atomic_identifier
{

    struct NIdentifier* id;
    struct NEnum_atomic_identifier * next;

#ifdef OUT2DOT
    char* print_val;
#endif


};

struct NEnum_array_identifier
{

    struct NIdentifier* id;
    struct NDimensions* dimensions;
    struct NEnum_array_identifier * next;

#ifdef OUT2DOT
    char* print_val;
#endif


};

struct NEnum_atomic_identifier_list
{


    struct NEnum_atomic_identifier * first;
    struct NEnum_atomic_identifier * last;

#ifdef OUT2DOT
    char* print_val;
#endif


};

struct NEnum_array_identifier_list
{

    struct NEnum_array_identifier * first;
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
struct NStmt*                        create_stmt_znach (struct NZnach_stmt* znach_stmt);
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
struct NDim*                         create_id_int_dim(struct NIdentifier* first,int second);
struct NDim*                         create_id_id_dim(struct NIdentifier* first,struct NIdentifier* second);
struct NDimensions*                  append_int_int_dim(struct NDimensions* list, int first, int second);
struct NDimensions*                  append_int_id_dim(struct NDimensions* list,int first,struct NIdentifier* second);
struct NDimensions*                  append_id_int_dim(struct NDimensions* list, struct NIdentifier* first,int second);
struct NDimensions*                  append_id_id_dim(struct NDimensions* list,struct NIdentifier* first,struct NIdentifier* second);
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
