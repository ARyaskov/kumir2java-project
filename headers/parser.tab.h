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
     INT_CONST = 258,
     STRING_CONST = 259,
     DOUBLE_CONST = 260,
     CHAR_CONST = 261,
     ID = 262,
     ALG = 263,
     ARG = 264,
     VVOD = 265,
     VESCH = 266,
     VESCHTAB = 267,
     VYVOD = 268,
     DA = 269,
     ZNACH = 270,
     KON = 271,
     LIT = 272,
     LITTAB = 273,
     LOG = 274,
     LOGTAB = 275,
     NACH = 276,
     NET = 277,
     NS = 278,
     REZ = 279,
     SIM = 280,
     SIMTAB = 281,
     CEL = 282,
     CELTAB = 283,
     ENDL = 284,
     SPACE = 285,
     BOGUS = 286,
     ASSMNT = 287,
     EQ = 288,
     NEQ = 289,
     LTEQ = 290,
     GTEQ = 291,
     LT = 292,
     GT = 293,
     MINUS = 294,
     PLUS = 295,
     DIV = 296,
     MUL = 297,
     POW = 298
   };
#endif



#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE
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

