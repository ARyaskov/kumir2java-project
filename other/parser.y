%{


        #include "..\headers\parser.tab.h"
        #include "..\headers\tree_structs.h"

        extern int yylex(void);

		#define YYERROR_VERBOSE

       void yyerror (char const* s);

       extern FILE * yyin;

#define YYDEBUG 1

  FILE * errfile = NULL;

%}

%debug
%locations
%start program

%union
{
      /*  BOOL    bool_const;*/
        int     int_const;
        char    char_const;
        char*   string_const;
        double  double_const;
        char*   id;

		struct NProgram*     Program;
		struct NStmt*        Stmt;
		struct NStmt_list*   Stmt_list;
		struct NExpr*        Expr;
		struct NExpr_list*   Expr_list;
		struct NFunc_stmt*   Func_stmt;
		struct NProc_stmt*   Proc_stmt;
		struct NIdentifier*  Identifier;
		struct NZnach_value* Znach_value;
		struct NDecl*        Decl;
		struct NDecl_list*   Decl_list;
		struct NEnum_identifiers*        Enum_identifiers;
		struct NEnum_atomic_identifiers* Enum_atomic_identifiers;
		struct NEnum_array_identifiers*  Enum_array_identifiers;
		struct NParam_list*  Param_list;
		struct NParam*       Param;
		struct NArg_value*   Arg_value;
		struct NRez_value*   Rez_value;
		struct NDimensions*  Dimensions;
		struct NType*        Type;
		struct NAtomic_type* Atomic_type;
		struct NArray_type*  Array_type;
		/*struct NIndexes*     Indexes;*/
		struct NRead_stmt_list*   Read_list;
		struct NPrint_stmt_list*  Print_list;
		struct NFunction_call*    Function_Call;
};


%type <Program>         program
%type <Stmt>            stmt
%type <Stmt_list>       stmt_list
%type <Expr>            expr
%type <Expr_list>       expr_list
%type <Func_stmt>       func_stmt
%type <Proc_stmt>       proc_stmt
%type <Identifier>      identifier
%type <Znach_value>     znach_value
%type <Decl>            decl
%type <Decl_list>       decl_list
%type <Enum_identifiers>              enum_identifiers
%type <Enum_atomic_identifiers>       enum_atomic_identifiers
%type <Enum_array_identifiers>        enum_array_identifiers
%type <Param_list>      param_list
%type <Arg_value>       arg_value
%type <Rez_value>       rez_value
%type <Dimensions>      dimensions
%type <Type>            type
%type <Atomic_type>     atomic_type
%type <Array_type>      array_type
/*%type <Indexes>         indexes*/
%type <Param>           param;
%type <Read_list>       read_list;
%type <Print_list>      print_list;
%type <Function_Call>   function_call;


/*%token <bool_const>   BOOL_CONST*/
%token <int_const>    INT_CONST
%token <string_const> STRING_CONST
%token <double_const> DOUBLE_CONST
%token <char_const>   CHAR_CONST
%token <id>           ID



%token ALG ARG VVOD VESCH VESCHTAB VYVOD
%token DA  ZNACH KON
%token LIT LITTAB LOG LOGTAB NACH NET NS
%token REZ SIM SIMTAB CEL CELTAB
%token ENDL SPACE
%token BOGUS

%right ASSMNT
%left GT LT GTEQ LTEQ NEQ EQ
%left PLUS MINUS
%left MUL DIV
%left POW
%nonassoc ')'


%%

        program: stmt_list   {$$=create_program($1);}
                ;

        stmt_list: stmt           {$$=create_stmt_list($1);}
                 | stmt_list stmt {$$=append_stmt_to_list($1,$2);}
                 ;

        stmt:   ENDL               {$$=create_stmt_expr(NULL);}
				| expr_list        {$$=create_stmt_expr_list($1);}
                | func_stmt        {$$=create_stmt_func($1);}
				| proc_stmt        {$$=create_stmt_proc($1);}
                | print_list       {$$=create_stmt_print($1);}
                | read_list        {$$=create_stmt_read($1);}
				| znach_value      {$$=create_stmt_znach($1);}
				| decl             {$$=create_stmt_decl($1);}
                ;

        expr:   function_call                {$$=create_function_call_expr($1);}
           		| identifier '[' expr_list ']' {$$=create_array_expr($1,$3);}
				| identifier                 {$$=create_expr_id($1);}
				| '(' expr ')'               {$$=create_exprlist_expr($2);}
                | INT_CONST                  {union Const_values value; value.Int=$1;$$=create_const_expr(Int, value);}
                | CHAR_CONST                 {union Const_values value; value.Char=$1;$$=create_const_expr(Char, value);}
                | STRING_CONST               {union Const_values value; value.String=$1;$$=create_const_expr(String, value);}
				| DOUBLE_CONST               {union Const_values value; value.Double=$1;$$=create_const_expr(Double, value);}
                /*| BOOL_CONST               {union Const_values value; value.Bool=$1;$$=create_const_expr(Bool, value);}*/
                | expr ASSMNT expr           {
                                            /*  @$.first_line = @1.first_line;
                                              @$.first_column = @1.first_column;
                                              @$.last_column = @3.last_column;
                                              @$.last_line = @3.last_line;

                                              printf("Making assignment %d.%d:%d.%d\n",
                                                  @1.first_line, @1.first_column,
                                                  @3.last_column, @3.last_line
                                                  );*/

                                              $$=create_op_expr(ASSMNT,$1,$3);

                                              }
                | expr PLUS expr             {$$=create_op_expr(PLUS,$1,$3);}
                | expr MINUS expr            {$$=create_op_expr(MINUS,$1,$3);}
                | expr MUL expr              {$$=create_op_expr(MUL,$1,$3);}
                | expr DIV expr              {$$=create_op_expr(DIV,$1,$3);}
                | expr POW expr              {$$=create_op_expr(POW,$1,$3);}
                | expr GT expr               {$$=create_op_expr(GT,$1,$3);}
                | expr LT expr               {$$=create_op_expr(LT,$1,$3);}
                | expr GTEQ expr             {$$=create_op_expr(GTEQ,$1,$3);}
                | expr LTEQ expr             {$$=create_op_expr(LTEQ,$1,$3);}
                | expr NEQ expr              {$$=create_op_expr(NEQ,$1,$3);}
                | expr EQ expr               {$$=create_op_expr(EQ,$1,$3);}
                ;


	    function_call :  identifier '(' expr_list ')' {$$=create_function_call($1, $3);}
                        ;

        expr_list : expr                                  {$$=create_expr_list($1);}
				   | expr_list ',' expr                   {$$=append_expr_to_list($1,$3);}
                ;
		identifier: ID                                    {
		                                   /*        @$.first_line = @1.first_line;
                                              @$.first_column = @1.first_column;
                                              @$.last_column = @1.last_column;
                                              @$.last_line = @1.last_line;

                                              printf("Making identifier! %d.%d:%d.%d\n",
                                                  @$.first_line, @$.first_column,
                                                  @$.last_line, @$.last_column
                                                  );*/
		$$=create_ident($1); }
				;

		decl: atomic_type SPACE enum_atomic_identifiers ENDL           {$$=create_from_atomic_decl($1,$3);}
	         |  array_type SPACE enum_array_identifiers ENDL           {$$=create_from_array_decl($1,$3);}

		decl_list: decl
		          | decl_list ENDL decl
		          | decl_list
				  ;

		enum_identifiers: enum_atomic_identifiers
		                 | enum_array_identifiers
						 ;

		enum_atomic_identifiers : 	identifier  {$$=create_enum_atomic_identifier_list($1);}
                          | enum_atomic_identifiers ',' identifier 	 {$$=append_enum_atomic_identifier_list($1,$3);}
                          | enum_atomic_identifiers SPACE ',' identifier 	 {$$=append_enum_atomic_identifier_list($1,$4);}
                          | enum_atomic_identifiers ',' SPACE identifier 	 {$$=append_enum_atomic_identifier_list($1,$4);}
                          | enum_atomic_identifiers SPACE ',' SPACE identifier 	 {$$=append_enum_atomic_identifier_list($1,$5);}
						  ;

		enum_array_identifiers : identifier '[' dimensions ']'  {$$=create_enum_array_identifier_list($1,$3);}
                          | enum_array_identifiers ',' identifier '[' dimensions ']' {$$=append_enum_array_identifier_list($1,$3,$5);}
                          | enum_array_identifiers ',' identifier '[' dimensions ']' '\n' {$$=append_enum_array_identifier_list($1,$3,$5);}
						  ;

        func_stmt: ALG SPACE atomic_type SPACE identifier ENDL NACH ENDL stmt_list ENDL KON ENDL      {
/*
        				                                           @$.first_line = @1.first_line;
                                              @$.first_column = @1.first_column;
                                              @$.last_column = @11.last_column;
                                              @$.last_line = @11.last_line;
YYLLOC_DEFAULT(@$, Rhs, N) */
                                              printf("Making func %d.%d:%d.%d\n",
                                                  @$.first_line, @$.first_column,
                                                  @$.last_column, @$.last_line
                                                  );

        $$=create_func($3, $5,NULL, $9);}
                         | ALG SPACE atomic_type SPACE identifier '(' param_list ')' ENDL NACH stmt_list KON ENDL {$$=create_func($3, $5,$7, $11);}
                         ;
		proc_stmt: ALG SPACE identifier ENDL NACH ENDL stmt_list KON ENDL       {

				                                           @$.first_line = @1.first_line;
                                              @$.first_column = @1.first_column;
                                              @$.last_column = @8.last_column;
                                              @$.last_line = @8.last_line;

                                              printf("Making proc %d.%d:%d.%d\n",
                                                  @$.first_line, @$.first_column,
                                                  @$.last_column, @$.last_line
                                                  );

                                                  $$=create_proc($3, NULL , $7);
                                                  }
                  | ALG SPACE identifier '(' param_list ')' ENDL NACH ENDL stmt_list KON ENDL  {

                          				                                           @$.first_line = @1.first_line;
                                              @$.first_column = @1.first_column;
                                              @$.last_column = @12.last_column;
                                              @$.last_line = @12.last_line;

                                              printf("Making proc %d.%d:%d.%d\n",
                                                  @$.first_line, @$.first_column,
                                                  @$.last_column, @$.last_line
                                                  );


                  $$=create_proc($3,$5,$10);}
                  ;

        param_list: param                                        {$$=create_param_list($1);}
				   | param_list ',' param                        {$$=append_param_to_list($1, $3);}
				   ;

		znach_value: ZNACH ASSMNT expr                           {$$=create_znachvalue($3);}
		            ;

		param: arg_value                                         {$$=create_from_arg_rezvalue($1);}
		        | rez_value                                      {$$=create_from_rez_rezvalue($1);}
		;
		arg_value: ARG SPACE atomic_type SPACE identifier                    {$$=create_arg($3, $5, NULL);}
		          | ARG SPACE array_type SPACE identifier '[' dimensions ']' {$$=create_arg($3, $5, $7);}
				  ;

		rez_value: REZ SPACE array_type identifier '[' dimensions ']' {$$=create_rez($3, $4, $6);}
		          | REZ SPACE atomic_type identifier                  {$$=create_rez($3, $4, NULL);}
				  ;


		dimensions: INT_CONST ':' INT_CONST                           {$$=create_int_int_dim($1,$3);}
		           | identifier ':' identifier                        {$$=create_id_id_dim($1,$3);}
				   | INT_CONST ':' identifier                         {$$=create_int_id_dim($1,$3);}
				   | identifier ':' INT_CONST                         {$$=create_int_id_dim($1, $3);}
		           | dimensions ',' INT_CONST ':' INT_CONST           {$$=append_int_int_dim($1, $3, $5);}
				   | dimensions ',' identifier ':' identifier         {$$=append_id_id_dim($1, $3, $5);}
				   | dimensions ',' INT_CONST ':' identifier          {$$=append_int_id_dim($1, $3, $5);}
				   | dimensions ',' identifier ':' INT_CONST          {$$=append_id_int_dim($1, $3, $5);}
				   ;

		/*indexes: INT_CONST  {$$=create_index_int($1);}
		        | identifier {$$=create_index_id($1);}
				| indexes ',' identifier {$$=create_index_indexes_id($1, $3);}
			    | indexes ',' INT_CONST {$$=create_indexes_int($1, $3);}
			;		*/

        type: atomic_type {$$=create_type_from_atomic($1);}
            | array_type  {$$=create_type_from_array($1);}
			;

		array_type:   CELTAB {$$=create_atomic_type("целтаб");}
                    | VESCHTAB {$$=create_atomic_type("вещтаб");}
                    | SIMTAB {$$=create_atomic_type("симтаб");}
                    | LITTAB {$$=create_atomic_type("литтаб");}
                    | LOGTAB {$$=create_atomic_type("логтаб");}
                  ;

		atomic_type:  CEL  {$$=create_atomic_type("цел");}
			 | VESCH {$$=create_atomic_type("вещ");}
			 | SIM   {$$=create_atomic_type("сим");}
			 | LIT   {$$=create_atomic_type("лит");}
			 | LOG  {$$=create_atomic_type("лог");}
			 ;

        print_list:  VYVOD STRING_CONST                   {$$=create_str_print($2);}
		                   | VYVOD INT_CONST              {$$=create_int_print($2);}
						   | VYVOD CHAR_CONST             {$$=create_str_print($2);}
						 /*  | VYVOD BOOL_CONST           {$$=create_bool_print($2);}*/
						   | VYVOD identifier             {$$=create_str_print($2);}
		                   | print_list ',' NS            {$$=append_str_print($1, "нс");}
						   | print_list ',' STRING_CONST  {$$=append_str_print($1, $3);}
						   | print_list ',' INT_CONST     {$$=append_int_print($1, $3);}
						   | print_list ',' CHAR_CONST    {$$=append_str_print($1, $3);}
						   /*| print_list ',' BOOL_CONST  {$$=append_bool_print($1, $3);}*/
						   | print_list ',' identifier    {$$=append_str_print($1, $3);}
                           ;

        read_list:      VVOD identifier               {/*$$=create_read($2);*/}
		                   | read_list ',' identifier {/*$$=append_read($1,$3);*/}
                           ;

%%

/*
int main(int argc, char** argv){
  setlocale(LC_CTYPE, "rus");



  log = fopen("log.txt", "w+");
  freopen_s(&err,"err.txt","a+",stderr);

  if (argc > 1){
      fopen_s(&yyin,argv[1], "r");
  }else{
     yyin = fopen("input.kum", "r");
  }
  if (yyin != NULL)
   //  yylex();
  {
	  yyparse();
  }

  fclose(log);

  getchar();
  return 0;
}
*/

void yyerror (char const* s)
{
        printf("%s\n",s);
        getchar();
        exit(0);
}


int main (int argc, char* argv[])
{

init_safeAlloc();

char* buf = (char*)safeAlloc(100);
int i;

for (i=0;i<1000;i++)
    hashes[i]=i;
hash_pointer = 0;

       setlocale(LC_CTYPE, "russian");

if (argc == 1){
        argv[1] = (char*)malloc(ALLOC_SZ);
		strcpy(argv[1], "unittests\\procsAndFuncs.kum");
		        argv[2] = (char*)malloc(ALLOC_SZ);
		strcpy(argv[2], "output\\procsAndFuncs.dot");
}

       dotfile = fopen(argv[2], "w+");

       before_val = (char*)malloc(ALLOC_SZ);
       freopen("output\\err.txt","w",stderr);
       strcpy(buf, "digraph KumirTree { \n");
       fwrite(buf,1,strlen("graph KumirTree { \n"),dotfile);
       // FILE* file;

        logfile = fopen("output\\log.txt", "wt");
        //
        yyin = fopen(argv[1], "r");


        yyparse();


       strcpy(buf, "\n }");
       fwrite(buf,1,sizeof(buf),dotfile);

        fclose(dotfile);
        fclose(logfile);
        garbageCollector();
       // getchar();
        return 0;
}

