/** ==================================Функции========================================= */
#include <string.h>
#include "..\headers\parser.tab.h"
#include "..\headers\tree_structs.h"


void                                 init_safeAlloc();
struct NExpr*                        create_brackets_expr(struct NExpr* expr_in);

void*                                safeAlloc(unsigned int bytes);
char*                                cutQuotes(char* str);
char*                                makeHashString();
char*                                getName(int);
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
struct NStmt*                        create_stmt_if (struct NIf_stmt* if_stmt);
struct NStmt*                        create_stmt_cycle(struct NCycle_stmt* cycle_stmt);
struct NStmt*                        create_stmt_switch (struct NSwitch_stmt* switch_stmt);
struct NStmt*                        create_stmt_utv(struct NExpr* expr);

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
struct NPrint_stmt*             append_str_print(struct NPrint_stmt_list* list, char* str);
struct NPrint_stmt*             append_int_print(struct NPrint_stmt_list* list, int value);

struct NAtomic_type*                 create_atomic_type(char* name);
struct NArray_type*                  create_array_type(char* name);
struct NType*                        create_type_from_atomic(struct NAtomic_type* atomic_type);
struct NType*                        create_type_from_array(struct NArray_type* arr_type);
struct NEnum_atomic_identifier_list* create_enum_atomic_identifier_list(struct NIdentifier* id);
struct NEnum_array_identifier_list*  create_enum_array_identifier_list(struct NIdentifier* id, struct NDimensions* dimensions);
struct NEnum_atomic_identifier_list* append_enum_atomic_identifier_list(struct NEnum_atomic_identifier_list* list, struct NIdentifier* id);
struct NEnum_array_identifier_list*  append_enum_array_identifier_list(struct NEnum_array_identifier_list* list, struct NIdentifier* id, struct NDimensions * dimensions);


struct NArg_value* create_arg_from_atomic(struct NAtomic_type* type, struct NEnum_atomic_identifier_list* list);
struct NArg_value* create_arg_from_array(struct NArray_type* type, struct NEnum_array_identifier_list* list);
struct NRez_value* create_rez_from_array(struct NArray_type* type, struct NEnum_array_identifier_list* list);
struct NRez_value* create_rez_from_atomic(struct NAtomic_type* type, struct NEnum_atomic_identifier_list* list);
struct NPrint_stmt* create_expr_list_print(struct NExpr_list* list);
struct NRead_stmt* create_expr_list_read(struct NExpr_list* list);

// развилки и циклы
struct NNc_expr* 					create_nc(struct NExpr* expr, enum NC_type type);
struct NIf_stmt* 					create_if(struct NExpr* expr, struct NStmt_list* list_to, struct NStmt_list* list_inache);
struct NCycle_stmt* 				create_cycle(struct NNc_expr* nc_expr, struct NStmt_list* list, struct NExpr* expr);
struct NSwitch_stmt* 				create_switch(struct NCase_stmt_list* case_stmt_list, struct NStmt_list* list);
struct NUtv_stmt* 					create_utv(struct NStmt_list* list);
struct NCase_stmt_list* 			create_stmt_case_list(struct NCase_stmt* stmt_case);
struct NCase_stmt_list* 			append_case_stmt_to_list(struct NCase_stmt_list* case_stmt_list ,struct NCase_stmt* stmt_case);
struct NCase_stmt* 					create_stmt_case(struct NExpr* expr, struct NStmt_list* list);


