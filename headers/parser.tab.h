/* A Bison parser, made by GNU Bison 2.4.2.  */

/* Skeleton interface for Bison's Yacc-like parsers in C
   
      Copyright (C) 1984, 1989-1990, 2000-2006, 2009-2010 Free Software
   Foundation, Inc.
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.
   
   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */


/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     BOOL_CONST = 258,
     INT_CONST = 259,
     STRING_CONST = 260,
     DOUBLE_CONST = 261,
     CHAR_CONST = 262,
     ID = 263,
     ESLI = 264,
     TO = 265,
     INACHE = 266,
     VSE = 267,
     NC = 268,
     KC_PRI = 269,
     POKA = 270,
     KC = 271,
     VYBOR = 272,
     PRI = 273,
     UTV = 274,
     RAZ = 275,
     ALG = 276,
     ARG = 277,
     VVOD = 278,
     VESCH = 279,
     VESCHTAB = 280,
     VYVOD = 281,
     DA = 282,
     ZNACH = 283,
     KON = 284,
     LIT = 285,
     LITTAB = 286,
     LOG = 287,
     LOGTAB = 288,
     NACH = 289,
     NET = 290,
     NS = 291,
     REZ = 292,
     SIM = 293,
     SIMTAB = 294,
     CEL = 295,
     CELTAB = 296,
     ENDL = 297,
     ASSMNT = 298,
     EQ = 299,
     NEQ = 300,
     LTEQ = 301,
     GTEQ = 302,
     LT = 303,
     GT = 304,
     MINUS = 305,
     PLUS = 306,
     DIV = 307,
     MUL = 308,
     POW = 309,
     ILI = 310,
     I = 311,
     NE = 312,
     UMINUS = 313
   };
#endif



#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE
{


    char*    bool_const;
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
    struct NEnum_atomic_identifier_list* Enum_atomic_identifier_list;
    struct NEnum_array_identifier_list*  Enum_array_identifier_list;
    struct NParam_list*  Param_list;
    struct NParam*       Param;
    struct NArg_value*   Arg_value;
    struct NRez_value*   Rez_value;
    struct NDimensions*  Dimensions;
    struct NAtomic_type* Atomic_type;
    struct NArray_type*  Array_type;
    struct NRead_stmt*   Read;
    struct NPrint_stmt*  Print;
    struct NFunction_call*    Function_Call;

	struct NNc_expr*	  Nc_expr;
	struct NIf_stmt*	  If_stmt;
	struct NCycle_stmt* Cycle_stmt;
	struct NSwitch_stmt* Switch_stmt;
	struct NCase_stmt_list* Case_stmt_list;
	struct NCase_stmt* Case_stmt;



} YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif

extern YYSTYPE yylval;

#if ! defined YYLTYPE && ! defined YYLTYPE_IS_DECLARED
typedef struct YYLTYPE
{
  int first_line;
  int first_column;
  int last_line;
  int last_column;
} YYLTYPE;
# define yyltype YYLTYPE /* obsolescent; will be withdrawn */
# define YYLTYPE_IS_DECLARED 1
# define YYLTYPE_IS_TRIVIAL 1
#endif

extern YYLTYPE yylloc;

