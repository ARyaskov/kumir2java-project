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
     ALG = 264,
     ARG = 265,
     VVOD = 266,
     VESCH = 267,
     VESCHTAB = 268,
     VYVOD = 269,
     DA = 270,
     ZNACH = 271,
     KON = 272,
     LIT = 273,
     LITTAB = 274,
     LOG = 275,
     LOGTAB = 276,
     NACH = 277,
     NET = 278,
     NS = 279,
     REZ = 280,
     SIM = 281,
     SIMTAB = 282,
     CEL = 283,
     CELTAB = 284,
     ENDL = 285,
     SPACE = 286,
     BOGUS = 287,
     EOF = 288,
     ASSMNT = 289,
     EQ = 290,
     NEQ = 291,
     LTEQ = 292,
     GTEQ = 293,
     LT = 294,
     GT = 295,
     MINUS = 296,
     PLUS = 297,
     DIV = 298,
     MUL = 299,
     POW = 300,
     UMINUS = 301
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

