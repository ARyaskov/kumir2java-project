/* A Bison parser, made by GNU Bison 2.4.2.  */

/* Skeleton implementation for Bison's Yacc-like parsers in C
   
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

/* C LALR(1) parser skeleton written by Richard Stallman, by
   simplifying the original so-called "semantic" parser.  */

/* All symbols defined below should begin with yy or YY, to avoid
   infringing on user name space.  This should be done even for local
   variables, as they might otherwise be expanded by user macros.
   There are some unavoidable exceptions within include files to
   define necessary library symbols; they are noted "INFRINGES ON
   USER NAME SPACE" below.  */

/* Identify Bison output.  */
#define YYBISON 1

/* Bison version.  */
#define YYBISON_VERSION "2.4.2"

/* Skeleton name.  */
#define YYSKELETON_NAME "yacc.c"

/* Pure parsers.  */
#define YYPURE 0

/* Push parsers.  */
#define YYPUSH 0

/* Pull parsers.  */
#define YYPULL 1

/* Using locations.  */
#define YYLSP_NEEDED 1



/* Copy the first part of user declarations.  */




#include "..\headers\parser.tab.h"
#include "..\headers\tree_structs.h"

    extern int yylex(void);

#define YYERROR_VERBOSE

    void yyerror (char const* s);

    extern FILE * yyin;

#define YYDEBUG 1

    FILE * errfile = NULL;





/* Enabling traces.  */
#ifndef YYDEBUG
# define YYDEBUG 1
#endif

/* Enabling verbose error messages.  */
#ifdef YYERROR_VERBOSE
# undef YYERROR_VERBOSE
# define YYERROR_VERBOSE 1
#else
# define YYERROR_VERBOSE 0
#endif

/* Enabling the token table.  */
#ifndef YYTOKEN_TABLE
# define YYTOKEN_TABLE 0
#endif


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
     ASSMNT = 288,
     EQ = 289,
     NEQ = 290,
     LTEQ = 291,
     GTEQ = 292,
     LT = 293,
     GT = 294,
     MINUS = 295,
     PLUS = 296,
     DIV = 297,
     MUL = 298,
     POW = 299
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
    struct NEnum_atomic_identifiers* Enum_atomic_identifiers;
    struct NEnum_array_identifiers*  Enum_array_identifiers;
    struct NParam_list*  Param_list;
    struct NParam*       Param;
    struct NArg_value*   Arg_value;
    struct NRez_value*   Rez_value;
    struct NDimensions*  Dimensions;
    struct NAtomic_type* Atomic_type;
    struct NArray_type*  Array_type;
    struct NRead_stmt_list*   Read_list;
    struct NPrint_stmt_list*  Print_list;
    struct NFunction_call*    Function_Call;



} YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif

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


/* Copy the second part of user declarations.  */



#ifdef short
# undef short
#endif

#ifdef YYTYPE_UINT8
typedef YYTYPE_UINT8 yytype_uint8;
#else
typedef unsigned char yytype_uint8;
#endif

#ifdef YYTYPE_INT8
typedef YYTYPE_INT8 yytype_int8;
#elif (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
typedef signed char yytype_int8;
#else
typedef short int yytype_int8;
#endif

#ifdef YYTYPE_UINT16
typedef YYTYPE_UINT16 yytype_uint16;
#else
typedef unsigned short int yytype_uint16;
#endif

#ifdef YYTYPE_INT16
typedef YYTYPE_INT16 yytype_int16;
#else
typedef short int yytype_int16;
#endif

#ifndef YYSIZE_T
# ifdef __SIZE_TYPE__
#  define YYSIZE_T __SIZE_TYPE__
# elif defined size_t
#  define YYSIZE_T size_t
# elif ! defined YYSIZE_T && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#  include <stddef.h> /* INFRINGES ON USER NAME SPACE */
#  define YYSIZE_T size_t
# else
#  define YYSIZE_T unsigned int
# endif
#endif

#define YYSIZE_MAXIMUM ((YYSIZE_T) -1)

#ifndef YY_
# if defined YYENABLE_NLS && YYENABLE_NLS
#  if ENABLE_NLS
#   include <libintl.h> /* INFRINGES ON USER NAME SPACE */
#   define YY_(msgid) dgettext ("bison-runtime", msgid)
#  endif
# endif
# ifndef YY_
#  define YY_(msgid) msgid
# endif
#endif

/* Suppress unused-variable warnings by "using" E.  */
#if ! defined lint || defined __GNUC__
# define YYUSE(e) ((void) (e))
#else
# define YYUSE(e) /* empty */
#endif

/* Identity function, used to suppress warnings about constant conditions.  */
#ifndef lint
# define YYID(n) (n)
#else
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static int
YYID (int yyi)
#else
static int
YYID (yyi)
    int yyi;
#endif
{
  return yyi;
}
#endif

#if ! defined yyoverflow || YYERROR_VERBOSE

/* The parser invokes alloca or malloc; define the necessary symbols.  */

# ifdef YYSTACK_USE_ALLOCA
#  if YYSTACK_USE_ALLOCA
#   ifdef __GNUC__
#    define YYSTACK_ALLOC __builtin_alloca
#   elif defined __BUILTIN_VA_ARG_INCR
#    include <alloca.h> /* INFRINGES ON USER NAME SPACE */
#   elif defined _AIX
#    define YYSTACK_ALLOC __alloca
#   elif defined _MSC_VER
#    include <malloc.h> /* INFRINGES ON USER NAME SPACE */
#    define alloca _alloca
#   else
#    define YYSTACK_ALLOC alloca
#    if ! defined _ALLOCA_H && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#     include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#     ifndef _STDLIB_H
#      define _STDLIB_H 1
#     endif
#    endif
#   endif
#  endif
# endif

# ifdef YYSTACK_ALLOC
   /* Pacify GCC's `empty if-body' warning.  */
#  define YYSTACK_FREE(Ptr) do { /* empty */; } while (YYID (0))
#  ifndef YYSTACK_ALLOC_MAXIMUM
    /* The OS might guarantee only one guard page at the bottom of the stack,
       and a page size can be as small as 4096 bytes.  So we cannot safely
       invoke alloca (N) if N exceeds 4096.  Use a slightly smaller number
       to allow for a few compiler-allocated temporary stack slots.  */
#   define YYSTACK_ALLOC_MAXIMUM 4032 /* reasonable circa 2006 */
#  endif
# else
#  define YYSTACK_ALLOC YYMALLOC
#  define YYSTACK_FREE YYFREE
#  ifndef YYSTACK_ALLOC_MAXIMUM
#   define YYSTACK_ALLOC_MAXIMUM YYSIZE_MAXIMUM
#  endif
#  if (defined __cplusplus && ! defined _STDLIB_H \
       && ! ((defined YYMALLOC || defined malloc) \
	     && (defined YYFREE || defined free)))
#   include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#   ifndef _STDLIB_H
#    define _STDLIB_H 1
#   endif
#  endif
#  ifndef YYMALLOC
#   define YYMALLOC malloc
#   if ! defined malloc && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void *malloc (YYSIZE_T); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
#  ifndef YYFREE
#   define YYFREE free
#   if ! defined free && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void free (void *); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
# endif
#endif /* ! defined yyoverflow || YYERROR_VERBOSE */


#if (! defined yyoverflow \
     && (! defined __cplusplus \
	 || (defined YYLTYPE_IS_TRIVIAL && YYLTYPE_IS_TRIVIAL \
	     && defined YYSTYPE_IS_TRIVIAL && YYSTYPE_IS_TRIVIAL)))

/* A type that is properly aligned for any stack member.  */
union yyalloc
{
  yytype_int16 yyss_alloc;
  YYSTYPE yyvs_alloc;
  YYLTYPE yyls_alloc;
};

/* The size of the maximum gap between one aligned stack and the next.  */
# define YYSTACK_GAP_MAXIMUM (sizeof (union yyalloc) - 1)

/* The size of an array large to enough to hold all stacks, each with
   N elements.  */
# define YYSTACK_BYTES(N) \
     ((N) * (sizeof (yytype_int16) + sizeof (YYSTYPE) + sizeof (YYLTYPE)) \
      + 2 * YYSTACK_GAP_MAXIMUM)

/* Copy COUNT objects from FROM to TO.  The source and destination do
   not overlap.  */
# ifndef YYCOPY
#  if defined __GNUC__ && 1 < __GNUC__
#   define YYCOPY(To, From, Count) \
      __builtin_memcpy (To, From, (Count) * sizeof (*(From)))
#  else
#   define YYCOPY(To, From, Count)		\
      do					\
	{					\
	  YYSIZE_T yyi;				\
	  for (yyi = 0; yyi < (Count); yyi++)	\
	    (To)[yyi] = (From)[yyi];		\
	}					\
      while (YYID (0))
#  endif
# endif

/* Relocate STACK from its old location to the new one.  The
   local variables YYSIZE and YYSTACKSIZE give the old and new number of
   elements in the stack, and YYPTR gives the new location of the
   stack.  Advance YYPTR to a properly aligned location for the next
   stack.  */
# define YYSTACK_RELOCATE(Stack_alloc, Stack)				\
    do									\
      {									\
	YYSIZE_T yynewbytes;						\
	YYCOPY (&yyptr->Stack_alloc, Stack, yysize);			\
	Stack = &yyptr->Stack_alloc;					\
	yynewbytes = yystacksize * sizeof (*Stack) + YYSTACK_GAP_MAXIMUM; \
	yyptr += yynewbytes / sizeof (*yyptr);				\
      }									\
    while (YYID (0))

#endif

/* YYFINAL -- State number of the termination state.  */
#define YYFINAL  45
/* YYLAST -- Last index in YYTABLE.  */
#define YYLAST   347

/* YYNTOKENS -- Number of terminals.  */
#define YYNTOKENS  52
/* YYNNTS -- Number of nonterminals.  */
#define YYNNTS  23
/* YYNRULES -- Number of rules.  */
#define YYNRULES  81
/* YYNRULES -- Number of states.  */
#define YYNSTATES  182

/* YYTRANSLATE(YYLEX) -- Bison symbol number corresponding to YYLEX.  */
#define YYUNDEFTOK  2
#define YYMAXUTOK   299

#define YYTRANSLATE(YYX)						\
  ((unsigned int) (YYX) <= YYMAXUTOK ? yytranslate[YYX] : YYUNDEFTOK)

/* YYTRANSLATE[YYLEX] -- Bison symbol number corresponding to YYLEX.  */
static const yytype_uint8 yytranslate[] =
{
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
      48,    45,     2,     2,    49,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,    51,    50,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,    46,     2,    47,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,    34,
      35,    36,    37,    38,    39,    40,    41,    42,    43,    44
};

#if YYDEBUG
/* YYPRHS[YYN] -- Index of the first RHS symbol of rule number YYN in
   YYRHS.  */
static const yytype_uint16 yyprhs[] =
{
       0,     0,     3,     5,     7,    10,    12,    14,    16,    18,
      21,    24,    26,    28,    30,    35,    37,    41,    43,    45,
      47,    49,    51,    55,    59,    63,    67,    71,    75,    79,
      83,    87,    91,    95,    99,   101,   103,   108,   110,   114,
     116,   121,   126,   128,   132,   137,   142,   148,   153,   160,
     173,   187,   197,   210,   212,   216,   220,   222,   224,   230,
     236,   242,   248,   252,   256,   260,   264,   270,   276,   282,
     288,   290,   292,   294,   296,   298,   300,   302,   304,   306,
     308,   312
};

/* YYRHS -- A `-1'-separated list of the rules' RHS.  */
static const yytype_int8 yyrhs[] =
{
      53,     0,    -1,    54,    -1,    55,    -1,    54,    55,    -1,
      30,    -1,    58,    -1,    63,    -1,    64,    -1,    73,    30,
      -1,    74,    30,    -1,    66,    -1,    60,    -1,    57,    -1,
      59,    46,    58,    47,    -1,    59,    -1,    48,    56,    45,
      -1,     4,    -1,     7,    -1,     5,    -1,     6,    -1,     3,
      -1,    56,    33,    56,    -1,    56,    41,    56,    -1,    56,
      40,    56,    -1,    56,    43,    56,    -1,    56,    42,    56,
      -1,    56,    44,    56,    -1,    56,    39,    56,    -1,    56,
      38,    56,    -1,    56,    37,    56,    -1,    56,    36,    56,
      -1,    56,    35,    56,    -1,    56,    34,    56,    -1,    23,
      -1,    15,    -1,    59,    48,    58,    45,    -1,    56,    -1,
      58,    49,    56,    -1,     8,    -1,    72,    31,    61,    30,
      -1,    71,    31,    62,    30,    -1,    59,    -1,    61,    49,
      59,    -1,    61,    31,    49,    59,    -1,    61,    49,    31,
      59,    -1,    61,    31,    49,    31,    59,    -1,    59,    46,
      70,    47,    -1,    62,    49,    59,    46,    70,    47,    -1,
       9,    31,    72,    31,    59,    30,    22,    30,    54,    30,
      17,    30,    -1,     9,    31,    72,    31,    59,    48,    65,
      45,    30,    22,    54,    17,    30,    -1,     9,    31,    59,
      30,    22,    30,    54,    17,    30,    -1,     9,    31,    59,
      48,    65,    45,    30,    22,    30,    54,    17,    30,    -1,
      67,    -1,    65,    50,    67,    -1,    16,    33,    56,    -1,
      68,    -1,    69,    -1,    10,    31,    72,    31,    61,    -1,
      10,    31,    71,    31,    62,    -1,    25,    31,    71,    31,
      62,    -1,    25,    31,    72,    31,    61,    -1,     4,    51,
       4,    -1,    59,    51,    59,    -1,     4,    51,    59,    -1,
      59,    51,     4,    -1,    70,    49,     4,    51,     4,    -1,
      70,    49,    59,    51,    59,    -1,    70,    49,     4,    51,
      59,    -1,    70,    49,    59,    51,     4,    -1,    29,    -1,
      13,    -1,    27,    -1,    19,    -1,    21,    -1,    28,    -1,
      12,    -1,    26,    -1,    18,    -1,    20,    -1,    14,    31,
      58,    -1,    11,    31,    58,    -1
};

/* YYRLINE[YYN] -- source line where rule number YYN was defined.  */
static const yytype_uint16 yyrline[] =
{
       0,   112,   112,   117,   119,   124,   126,   128,   130,   132,
     134,   136,   138,   143,   145,   147,   149,   151,   155,   159,
     163,   167,   171,   175,   177,   179,   181,   183,   185,   187,
     189,   191,   193,   195,   197,   202,   211,   216,   218,   222,
     229,   231,   237,   239,   241,   243,   245,   250,   252,   257,
     259,   263,   265,   270,   272,   277,   282,   284,   288,   290,
     295,   297,   303,   305,   307,   309,   311,   313,   315,   317,
     324,   326,   328,   330,   332,   337,   339,   341,   343,   345,
     350,   354
};
#endif

#if YYDEBUG || YYERROR_VERBOSE || YYTOKEN_TABLE
/* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
   First, the terminals, then, starting at YYNTOKENS, nonterminals.  */
static const char *const yytname[] =
{
  "$end", "error", "$undefined", "BOOL_CONST", "INT_CONST",
  "STRING_CONST", "DOUBLE_CONST", "CHAR_CONST", "ID", "ALG", "ARG", "VVOD",
  "VESCH", "VESCHTAB", "VYVOD", "DA", "ZNACH", "KON", "LIT", "LITTAB",
  "LOG", "LOGTAB", "NACH", "NET", "NS", "REZ", "SIM", "SIMTAB", "CEL",
  "CELTAB", "ENDL", "SPACE", "BOGUS", "ASSMNT", "EQ", "NEQ", "LTEQ",
  "GTEQ", "LT", "GT", "MINUS", "PLUS", "DIV", "MUL", "POW", "')'", "'['",
  "']'", "'('", "','", "';'", "':'", "$accept", "program", "stmt_list",
  "stmt", "expr", "function_call", "expr_list", "identifier", "decl",
  "enum_atomic_identifiers", "enum_array_identifiers", "func_stmt",
  "proc_stmt", "param_list", "znach_value", "param", "arg_value",
  "rez_value", "dimensions", "array_type", "atomic_type", "print_list",
  "read_list", 0
};
#endif

# ifdef YYPRINT
/* YYTOKNUM[YYLEX-NUM] -- Internal token number corresponding to
   token YYLEX-NUM.  */
static const yytype_uint16 yytoknum[] =
{
       0,   256,   257,   258,   259,   260,   261,   262,   263,   264,
     265,   266,   267,   268,   269,   270,   271,   272,   273,   274,
     275,   276,   277,   278,   279,   280,   281,   282,   283,   284,
     285,   286,   287,   288,   289,   290,   291,   292,   293,   294,
     295,   296,   297,   298,   299,    41,    91,    93,    40,    44,
      59,    58
};
# endif

/* YYR1[YYN] -- Symbol number of symbol that rule YYN derives.  */
static const yytype_uint8 yyr1[] =
{
       0,    52,    53,    54,    54,    55,    55,    55,    55,    55,
      55,    55,    55,    56,    56,    56,    56,    56,    56,    56,
      56,    56,    56,    56,    56,    56,    56,    56,    56,    56,
      56,    56,    56,    56,    56,    56,    57,    58,    58,    59,
      60,    60,    61,    61,    61,    61,    61,    62,    62,    63,
      63,    64,    64,    65,    65,    66,    67,    67,    68,    68,
      69,    69,    70,    70,    70,    70,    70,    70,    70,    70,
      71,    71,    71,    71,    71,    72,    72,    72,    72,    72,
      73,    74
};

/* YYR2[YYN] -- Number of symbols composing right hand side of rule YYN.  */
static const yytype_uint8 yyr2[] =
{
       0,     2,     1,     1,     2,     1,     1,     1,     1,     2,
       2,     1,     1,     1,     4,     1,     3,     1,     1,     1,
       1,     1,     3,     3,     3,     3,     3,     3,     3,     3,
       3,     3,     3,     3,     1,     1,     4,     1,     3,     1,
       4,     4,     1,     3,     4,     4,     5,     4,     6,    12,
      13,     9,    12,     1,     3,     3,     1,     1,     5,     5,
       5,     5,     3,     3,     3,     3,     5,     5,     5,     5,
       1,     1,     1,     1,     1,     1,     1,     1,     1,     1,
       3,     3
};

/* YYDEFACT[STATE-NAME] -- Default rule to reduce with in state
   STATE-NUM when YYTABLE doesn't specify something else to do.  Zero
   means the default is an error.  */
static const yytype_uint8 yydefact[] =
{
       0,    21,    17,    19,    20,    18,    39,     0,     0,    76,
      71,     0,    35,     0,    78,    73,    79,    74,    34,    77,
      72,    75,    70,     5,     0,     0,     2,     3,    37,    13,
       6,    15,    12,     7,     8,    11,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     1,     4,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     9,    10,     0,     0,    81,    80,
      55,    16,    22,    33,    32,    31,    30,    29,    28,    24,
      23,    26,    25,    27,    38,     0,     0,     0,     0,    42,
       0,     0,     0,     0,    14,    36,     0,    41,     0,    40,
       0,     0,     0,     0,     0,     0,    53,    56,    57,     0,
       0,     0,     0,     0,     0,     0,    43,     0,     0,     0,
       0,     0,     0,     0,     0,     0,    47,     0,     0,     0,
      44,    45,     0,     0,     0,     0,     0,     0,    54,     0,
       0,    62,    64,    65,    63,     0,     0,     0,    46,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,    48,
      51,    59,    58,    60,    61,     0,     0,     0,    66,    68,
      69,    67,     0,     5,     0,     0,     0,     0,    52,    49,
       0,    50
};

/* YYDEFGOTO[NTERM-NUM].  */
static const yytype_int8 yydefgoto[] =
{
      -1,    25,    26,    27,    28,    29,    30,    31,    32,    90,
      88,    33,    34,   105,    35,   106,   107,   108,   112,    36,
      37,    38,    39
};

/* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
   STATE-NUM.  */
#define YYPACT_NINF -115
static const yytype_int16 yypact[] =
{
     256,  -115,  -115,  -115,  -115,  -115,  -115,    15,    22,  -115,
    -115,    27,  -115,   -13,  -115,  -115,  -115,  -115,  -115,  -115,
    -115,  -115,  -115,  -115,     2,    29,   256,  -115,   303,  -115,
      12,    59,  -115,  -115,  -115,  -115,    46,    53,    79,    80,
      10,     2,     2,     2,   285,  -115,  -115,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,   109,   109,  -115,  -115,   -11,    89,    12,    12,
     303,  -115,   303,    23,    23,    23,    23,    23,    23,    83,
      83,    84,    84,  -115,   303,    86,     0,    92,   -14,  -115,
     -16,   123,    30,   109,  -115,  -115,    48,  -115,   109,  -115,
      98,    -4,   118,   119,   128,    -3,  -115,  -115,  -115,    -9,
     120,   122,    87,   134,     3,   109,  -115,   256,   103,   103,
     144,    30,   127,    30,    65,    66,  -115,    74,    48,   109,
    -115,  -115,   149,   150,   151,   152,   153,   163,  -115,   156,
      -2,  -115,  -115,  -115,  -115,   136,   137,    90,  -115,   159,
     109,   109,   109,   109,   179,   256,   181,    75,   100,  -115,
    -115,   169,    -5,   169,    -5,   256,   287,   190,  -115,  -115,
    -115,  -115,   187,   202,   256,   191,   192,   225,  -115,  -115,
     193,  -115
};

/* YYPGOTO[NTERM-NUM].  */
static const yytype_int8 yypgoto[] =
{
    -115,  -115,  -114,   -26,    44,  -115,   -29,   -39,  -115,   -10,
      -8,  -115,  -115,    97,  -115,   104,  -115,  -115,    96,   -47,
     -38,  -115,  -115
};

/* YYTABLE[YYPACT[STATE-NUM]].  What to do in state STATE-NUM.  If
   positive, shift that token.  If negative, reduce the rule which
   number is the opposite.  If zero, do what YYDEFACT says.
   If YYTABLE_NINF, syntax error.  */
#define YYTABLE_NINF -1
static const yytype_uint8 yytable[] =
{
      46,    66,    67,   132,     6,     1,     2,     3,     4,     5,
       6,     6,    68,    69,    99,   100,    97,    12,     6,    91,
      43,   122,     9,    87,    89,    18,   100,   115,    14,    45,
      16,    85,    86,   101,   129,    98,    19,    92,    21,   123,
     103,   166,   120,   156,   101,    95,    40,   121,   121,    59,
      24,   172,   110,    41,   109,   104,     6,   111,    42,   113,
     177,    59,   116,    54,    55,    56,    57,    58,    44,   141,
     143,   133,   135,     6,     6,   130,   131,    62,   145,   168,
     134,   136,     6,     6,    63,   142,   144,    70,   146,   111,
     148,    72,    73,    74,    75,    76,    77,    78,    79,    80,
      81,    82,    83,    84,   170,    60,    46,    61,     6,    64,
      65,    87,    89,    87,    89,     9,    10,     6,   169,   171,
      93,    14,    15,    16,    17,    56,    57,    58,    58,    19,
      20,    21,    22,    94,   126,    59,   127,   159,    96,   127,
      46,   162,   161,   164,   163,   102,    46,   114,   117,   139,
     118,    46,     1,     2,     3,     4,     5,     6,     7,   119,
       8,     9,    10,    11,    12,    13,   149,    14,    15,    16,
      17,   124,    18,   125,   137,    19,    20,    21,    22,    23,
     128,   150,   151,   152,   153,   154,   155,   157,   158,   160,
       1,     2,     3,     4,     5,     6,     7,    24,     8,     9,
      10,    11,    12,    13,   175,    14,    15,    16,    17,   165,
      18,   167,   174,    19,    20,    21,    22,    23,    98,   176,
     140,   178,   179,   181,   147,   138,     0,     0,     1,     2,
       3,     4,     5,     6,     7,    24,     8,     9,    10,    11,
      12,    13,   180,    14,    15,    16,    17,     0,    18,     0,
       0,    19,    20,    21,    22,    23,     0,     0,     0,     1,
       2,     3,     4,     5,     6,     7,     0,     8,     9,    10,
      11,    12,    13,    24,    14,    15,    16,    17,     0,    18,
       0,     0,    19,    20,    21,    22,    23,     0,     0,     0,
       1,     2,     3,     4,     5,     6,     7,     0,     8,     9,
      10,    11,    12,    13,    24,    14,    15,    16,    17,     0,
      18,     0,     0,    19,    20,    21,    22,   173,    47,    48,
      49,    50,    51,    52,    53,    54,    55,    56,    57,    58,
      71,     0,     0,     0,     0,    24,    47,    48,    49,    50,
      51,    52,    53,    54,    55,    56,    57,    58
};

static const yytype_int16 yycheck[] =
{
      26,    40,    40,   117,     8,     3,     4,     5,     6,     7,
       8,     8,    41,    42,    30,    31,    30,    15,     8,    30,
      33,    30,    12,    62,    63,    23,    31,    31,    18,     0,
      20,    60,    61,    49,    31,    49,    26,    48,    28,    48,
      10,   155,    45,    45,    49,    45,    31,    50,    50,    49,
      48,   165,     4,    31,    93,    25,     8,    96,    31,    98,
     174,    49,   101,    40,    41,    42,    43,    44,    24,     4,
       4,   118,   119,     8,     8,   114,   115,    31,     4,     4,
     118,   119,     8,     8,    31,   124,   125,    43,   127,   128,
     129,    47,    48,    49,    50,    51,    52,    53,    54,    55,
      56,    57,    58,    59,     4,    46,   132,    48,     8,    30,
      30,   150,   151,   152,   153,    12,    13,     8,   157,   158,
      31,    18,    19,    20,    21,    42,    43,    44,    44,    26,
      27,    28,    29,    47,    47,    49,    49,    47,    46,    49,
     166,   151,   150,   153,   152,    22,   172,    49,    30,    22,
      31,   177,     3,     4,     5,     6,     7,     8,     9,    31,
      11,    12,    13,    14,    15,    16,    17,    18,    19,    20,
      21,    51,    23,    51,    30,    26,    27,    28,    29,    30,
      46,    31,    31,    31,    31,    22,    30,    51,    51,    30,
       3,     4,     5,     6,     7,     8,     9,    48,    11,    12,
      13,    14,    15,    16,    17,    18,    19,    20,    21,    30,
      23,    30,    22,    26,    27,    28,    29,    30,    49,    17,
     123,    30,    30,    30,   128,   121,    -1,    -1,     3,     4,
       5,     6,     7,     8,     9,    48,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    -1,    23,    -1,
      -1,    26,    27,    28,    29,    30,    -1,    -1,    -1,     3,
       4,     5,     6,     7,     8,     9,    -1,    11,    12,    13,
      14,    15,    16,    48,    18,    19,    20,    21,    -1,    23,
      -1,    -1,    26,    27,    28,    29,    30,    -1,    -1,    -1,
       3,     4,     5,     6,     7,     8,     9,    -1,    11,    12,
      13,    14,    15,    16,    48,    18,    19,    20,    21,    -1,
      23,    -1,    -1,    26,    27,    28,    29,    30,    33,    34,
      35,    36,    37,    38,    39,    40,    41,    42,    43,    44,
      45,    -1,    -1,    -1,    -1,    48,    33,    34,    35,    36,
      37,    38,    39,    40,    41,    42,    43,    44
};

/* YYSTOS[STATE-NUM] -- The (internal number of the) accessing
   symbol of state STATE-NUM.  */
static const yytype_uint8 yystos[] =
{
       0,     3,     4,     5,     6,     7,     8,     9,    11,    12,
      13,    14,    15,    16,    18,    19,    20,    21,    23,    26,
      27,    28,    29,    30,    48,    53,    54,    55,    56,    57,
      58,    59,    60,    63,    64,    66,    71,    72,    73,    74,
      31,    31,    31,    33,    56,     0,    55,    33,    34,    35,
      36,    37,    38,    39,    40,    41,    42,    43,    44,    49,
      46,    48,    31,    31,    30,    30,    59,    72,    58,    58,
      56,    45,    56,    56,    56,    56,    56,    56,    56,    56,
      56,    56,    56,    56,    56,    58,    58,    59,    62,    59,
      61,    30,    48,    31,    47,    45,    46,    30,    49,    30,
      31,    49,    22,    10,    25,    65,    67,    68,    69,    59,
       4,    59,    70,    59,    49,    31,    59,    30,    31,    31,
      45,    50,    30,    48,    51,    51,    47,    49,    46,    31,
      59,    59,    54,    71,    72,    71,    72,    30,    67,    22,
      65,     4,    59,     4,    59,     4,    59,    70,    59,    17,
      31,    31,    31,    31,    22,    30,    45,    51,    51,    47,
      30,    62,    61,    62,    61,    30,    54,    30,     4,    59,
       4,    59,    54,    30,    22,    17,    17,    54,    30,    30,
      17,    30
};

#define yyerrok		(yyerrstatus = 0)
#define yyclearin	(yychar = YYEMPTY)
#define YYEMPTY		(-2)
#define YYEOF		0

#define YYACCEPT	goto yyacceptlab
#define YYABORT		goto yyabortlab
#define YYERROR		goto yyerrorlab


/* Like YYERROR except do call yyerror.  This remains here temporarily
   to ease the transition to the new meaning of YYERROR, for GCC.
   Once GCC version 2 has supplanted version 1, this can go.  However,
   YYFAIL appears to be in use.  Nevertheless, it is formally deprecated
   in Bison 2.4.2's NEWS entry, where a plan to phase it out is
   discussed.  */

#define YYFAIL		goto yyerrlab
#if defined YYFAIL
  /* This is here to suppress warnings from the GCC cpp's
     -Wunused-macros.  Normally we don't worry about that warning, but
     some users do, and we want to make it easy for users to remove
     YYFAIL uses, which will produce warnings from Bison 2.5.  */
#endif

#define YYRECOVERING()  (!!yyerrstatus)

#define YYBACKUP(Token, Value)					\
do								\
  if (yychar == YYEMPTY && yylen == 1)				\
    {								\
      yychar = (Token);						\
      yylval = (Value);						\
      yytoken = YYTRANSLATE (yychar);				\
      YYPOPSTACK (1);						\
      goto yybackup;						\
    }								\
  else								\
    {								\
      yyerror (YY_("syntax error: cannot back up")); \
      YYERROR;							\
    }								\
while (YYID (0))


#define YYTERROR	1
#define YYERRCODE	256


/* YYLLOC_DEFAULT -- Set CURRENT to span from RHS[1] to RHS[N].
   If N is 0, then set CURRENT to the empty location which ends
   the previous symbol: RHS[0] (always defined).  */

#define YYRHSLOC(Rhs, K) ((Rhs)[K])
#ifndef YYLLOC_DEFAULT
# define YYLLOC_DEFAULT(Current, Rhs, N)				\
    do									\
      if (YYID (N))                                                    \
	{								\
	  (Current).first_line   = YYRHSLOC (Rhs, 1).first_line;	\
	  (Current).first_column = YYRHSLOC (Rhs, 1).first_column;	\
	  (Current).last_line    = YYRHSLOC (Rhs, N).last_line;		\
	  (Current).last_column  = YYRHSLOC (Rhs, N).last_column;	\
	}								\
      else								\
	{								\
	  (Current).first_line   = (Current).last_line   =		\
	    YYRHSLOC (Rhs, 0).last_line;				\
	  (Current).first_column = (Current).last_column =		\
	    YYRHSLOC (Rhs, 0).last_column;				\
	}								\
    while (YYID (0))
#endif


/* YY_LOCATION_PRINT -- Print the location on the stream.
   This macro was not mandated originally: define only if we know
   we won't break user code: when these are the locations we know.  */

#ifndef YY_LOCATION_PRINT
# if defined YYLTYPE_IS_TRIVIAL && YYLTYPE_IS_TRIVIAL
#  define YY_LOCATION_PRINT(File, Loc)			\
     fprintf (File, "%d.%d-%d.%d",			\
	      (Loc).first_line, (Loc).first_column,	\
	      (Loc).last_line,  (Loc).last_column)
# else
#  define YY_LOCATION_PRINT(File, Loc) ((void) 0)
# endif
#endif


/* YYLEX -- calling `yylex' with the right arguments.  */

#ifdef YYLEX_PARAM
# define YYLEX yylex (YYLEX_PARAM)
#else
# define YYLEX yylex ()
#endif

/* Enable debugging if requested.  */
#if YYDEBUG

# ifndef YYFPRINTF
#  include <stdio.h> /* INFRINGES ON USER NAME SPACE */
#  define YYFPRINTF fprintf
# endif

# define YYDPRINTF(Args)			\
do {						\
  if (yydebug)					\
    YYFPRINTF Args;				\
} while (YYID (0))

# define YY_SYMBOL_PRINT(Title, Type, Value, Location)			  \
do {									  \
  if (yydebug)								  \
    {									  \
      YYFPRINTF (stderr, "%s ", Title);					  \
      yy_symbol_print (stderr,						  \
		  Type, Value, Location); \
      YYFPRINTF (stderr, "\n");						  \
    }									  \
} while (YYID (0))


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_value_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep, YYLTYPE const * const yylocationp)
#else
static void
yy_symbol_value_print (yyoutput, yytype, yyvaluep, yylocationp)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
    YYLTYPE const * const yylocationp;
#endif
{
  if (!yyvaluep)
    return;
  YYUSE (yylocationp);
# ifdef YYPRINT
  if (yytype < YYNTOKENS)
    YYPRINT (yyoutput, yytoknum[yytype], *yyvaluep);
# else
  YYUSE (yyoutput);
# endif
  switch (yytype)
    {
      default:
	break;
    }
}


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep, YYLTYPE const * const yylocationp)
#else
static void
yy_symbol_print (yyoutput, yytype, yyvaluep, yylocationp)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
    YYLTYPE const * const yylocationp;
#endif
{
  if (yytype < YYNTOKENS)
    YYFPRINTF (yyoutput, "token %s (", yytname[yytype]);
  else
    YYFPRINTF (yyoutput, "nterm %s (", yytname[yytype]);

  YY_LOCATION_PRINT (yyoutput, *yylocationp);
  YYFPRINTF (yyoutput, ": ");
  yy_symbol_value_print (yyoutput, yytype, yyvaluep, yylocationp);
  YYFPRINTF (yyoutput, ")");
}

/*------------------------------------------------------------------.
| yy_stack_print -- Print the state stack from its BOTTOM up to its |
| TOP (included).                                                   |
`------------------------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_stack_print (yytype_int16 *yybottom, yytype_int16 *yytop)
#else
static void
yy_stack_print (yybottom, yytop)
    yytype_int16 *yybottom;
    yytype_int16 *yytop;
#endif
{
  YYFPRINTF (stderr, "Stack now");
  for (; yybottom <= yytop; yybottom++)
    {
      int yybot = *yybottom;
      YYFPRINTF (stderr, " %d", yybot);
    }
  YYFPRINTF (stderr, "\n");
}

# define YY_STACK_PRINT(Bottom, Top)				\
do {								\
  if (yydebug)							\
    yy_stack_print ((Bottom), (Top));				\
} while (YYID (0))


/*------------------------------------------------.
| Report that the YYRULE is going to be reduced.  |
`------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_reduce_print (YYSTYPE *yyvsp, YYLTYPE *yylsp, int yyrule)
#else
static void
yy_reduce_print (yyvsp, yylsp, yyrule)
    YYSTYPE *yyvsp;
    YYLTYPE *yylsp;
    int yyrule;
#endif
{
  int yynrhs = yyr2[yyrule];
  int yyi;
  unsigned long int yylno = yyrline[yyrule];
  YYFPRINTF (stderr, "Reducing stack by rule %d (line %lu):\n",
	     yyrule - 1, yylno);
  /* The symbols being reduced.  */
  for (yyi = 0; yyi < yynrhs; yyi++)
    {
      YYFPRINTF (stderr, "   $%d = ", yyi + 1);
      yy_symbol_print (stderr, yyrhs[yyprhs[yyrule] + yyi],
		       &(yyvsp[(yyi + 1) - (yynrhs)])
		       , &(yylsp[(yyi + 1) - (yynrhs)])		       );
      YYFPRINTF (stderr, "\n");
    }
}

# define YY_REDUCE_PRINT(Rule)		\
do {					\
  if (yydebug)				\
    yy_reduce_print (yyvsp, yylsp, Rule); \
} while (YYID (0))

/* Nonzero means print parse trace.  It is left uninitialized so that
   multiple parsers can coexist.  */
int yydebug;
#else /* !YYDEBUG */
# define YYDPRINTF(Args)
# define YY_SYMBOL_PRINT(Title, Type, Value, Location)
# define YY_STACK_PRINT(Bottom, Top)
# define YY_REDUCE_PRINT(Rule)
#endif /* !YYDEBUG */


/* YYINITDEPTH -- initial size of the parser's stacks.  */
#ifndef	YYINITDEPTH
# define YYINITDEPTH 200
#endif

/* YYMAXDEPTH -- maximum size the stacks can grow to (effective only
   if the built-in stack extension method is used).

   Do not make this value too large; the results are undefined if
   YYSTACK_ALLOC_MAXIMUM < YYSTACK_BYTES (YYMAXDEPTH)
   evaluated with infinite-precision integer arithmetic.  */

#ifndef YYMAXDEPTH
# define YYMAXDEPTH 10000
#endif



#if YYERROR_VERBOSE

# ifndef yystrlen
#  if defined __GLIBC__ && defined _STRING_H
#   define yystrlen strlen
#  else
/* Return the length of YYSTR.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static YYSIZE_T
yystrlen (const char *yystr)
#else
static YYSIZE_T
yystrlen (yystr)
    const char *yystr;
#endif
{
  YYSIZE_T yylen;
  for (yylen = 0; yystr[yylen]; yylen++)
    continue;
  return yylen;
}
#  endif
# endif

# ifndef yystpcpy
#  if defined __GLIBC__ && defined _STRING_H && defined _GNU_SOURCE
#   define yystpcpy stpcpy
#  else
/* Copy YYSRC to YYDEST, returning the address of the terminating '\0' in
   YYDEST.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static char *
yystpcpy (char *yydest, const char *yysrc)
#else
static char *
yystpcpy (yydest, yysrc)
    char *yydest;
    const char *yysrc;
#endif
{
  char *yyd = yydest;
  const char *yys = yysrc;

  while ((*yyd++ = *yys++) != '\0')
    continue;

  return yyd - 1;
}
#  endif
# endif

# ifndef yytnamerr
/* Copy to YYRES the contents of YYSTR after stripping away unnecessary
   quotes and backslashes, so that it's suitable for yyerror.  The
   heuristic is that double-quoting is unnecessary unless the string
   contains an apostrophe, a comma, or backslash (other than
   backslash-backslash).  YYSTR is taken from yytname.  If YYRES is
   null, do not copy; instead, return the length of what the result
   would have been.  */
static YYSIZE_T
yytnamerr (char *yyres, const char *yystr)
{
  if (*yystr == '"')
    {
      YYSIZE_T yyn = 0;
      char const *yyp = yystr;

      for (;;)
	switch (*++yyp)
	  {
	  case '\'':
	  case ',':
	    goto do_not_strip_quotes;

	  case '\\':
	    if (*++yyp != '\\')
	      goto do_not_strip_quotes;
	    /* Fall through.  */
	  default:
	    if (yyres)
	      yyres[yyn] = *yyp;
	    yyn++;
	    break;

	  case '"':
	    if (yyres)
	      yyres[yyn] = '\0';
	    return yyn;
	  }
    do_not_strip_quotes: ;
    }

  if (! yyres)
    return yystrlen (yystr);

  return yystpcpy (yyres, yystr) - yyres;
}
# endif

/* Copy into YYRESULT an error message about the unexpected token
   YYCHAR while in state YYSTATE.  Return the number of bytes copied,
   including the terminating null byte.  If YYRESULT is null, do not
   copy anything; just return the number of bytes that would be
   copied.  As a special case, return 0 if an ordinary "syntax error"
   message will do.  Return YYSIZE_MAXIMUM if overflow occurs during
   size calculation.  */
static YYSIZE_T
yysyntax_error (char *yyresult, int yystate, int yychar)
{
  int yyn = yypact[yystate];

  if (! (YYPACT_NINF < yyn && yyn <= YYLAST))
    return 0;
  else
    {
      int yytype = YYTRANSLATE (yychar);
      YYSIZE_T yysize0 = yytnamerr (0, yytname[yytype]);
      YYSIZE_T yysize = yysize0;
      YYSIZE_T yysize1;
      int yysize_overflow = 0;
      enum { YYERROR_VERBOSE_ARGS_MAXIMUM = 5 };
      char const *yyarg[YYERROR_VERBOSE_ARGS_MAXIMUM];
      int yyx;

# if 0
      /* This is so xgettext sees the translatable formats that are
	 constructed on the fly.  */
      YY_("syntax error, unexpected %s");
      YY_("syntax error, unexpected %s, expecting %s");
      YY_("syntax error, unexpected %s, expecting %s or %s");
      YY_("syntax error, unexpected %s, expecting %s or %s or %s");
      YY_("syntax error, unexpected %s, expecting %s or %s or %s or %s");
# endif
      char *yyfmt;
      char const *yyf;
      static char const yyunexpected[] = "syntax error, unexpected %s";
      static char const yyexpecting[] = ", expecting %s";
      static char const yyor[] = " or %s";
      char yyformat[sizeof yyunexpected
		    + sizeof yyexpecting - 1
		    + ((YYERROR_VERBOSE_ARGS_MAXIMUM - 2)
		       * (sizeof yyor - 1))];
      char const *yyprefix = yyexpecting;

      /* Start YYX at -YYN if negative to avoid negative indexes in
	 YYCHECK.  */
      int yyxbegin = yyn < 0 ? -yyn : 0;

      /* Stay within bounds of both yycheck and yytname.  */
      int yychecklim = YYLAST - yyn + 1;
      int yyxend = yychecklim < YYNTOKENS ? yychecklim : YYNTOKENS;
      int yycount = 1;

      yyarg[0] = yytname[yytype];
      yyfmt = yystpcpy (yyformat, yyunexpected);

      for (yyx = yyxbegin; yyx < yyxend; ++yyx)
	if (yycheck[yyx + yyn] == yyx && yyx != YYTERROR)
	  {
	    if (yycount == YYERROR_VERBOSE_ARGS_MAXIMUM)
	      {
		yycount = 1;
		yysize = yysize0;
		yyformat[sizeof yyunexpected - 1] = '\0';
		break;
	      }
	    yyarg[yycount++] = yytname[yyx];
	    yysize1 = yysize + yytnamerr (0, yytname[yyx]);
	    yysize_overflow |= (yysize1 < yysize);
	    yysize = yysize1;
	    yyfmt = yystpcpy (yyfmt, yyprefix);
	    yyprefix = yyor;
	  }

      yyf = YY_(yyformat);
      yysize1 = yysize + yystrlen (yyf);
      yysize_overflow |= (yysize1 < yysize);
      yysize = yysize1;

      if (yysize_overflow)
	return YYSIZE_MAXIMUM;

      if (yyresult)
	{
	  /* Avoid sprintf, as that infringes on the user's name space.
	     Don't have undefined behavior even if the translation
	     produced a string with the wrong number of "%s"s.  */
	  char *yyp = yyresult;
	  int yyi = 0;
	  while ((*yyp = *yyf) != '\0')
	    {
	      if (*yyp == '%' && yyf[1] == 's' && yyi < yycount)
		{
		  yyp += yytnamerr (yyp, yyarg[yyi++]);
		  yyf += 2;
		}
	      else
		{
		  yyp++;
		  yyf++;
		}
	    }
	}
      return yysize;
    }
}
#endif /* YYERROR_VERBOSE */


/*-----------------------------------------------.
| Release the memory associated to this symbol.  |
`-----------------------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yydestruct (const char *yymsg, int yytype, YYSTYPE *yyvaluep, YYLTYPE *yylocationp)
#else
static void
yydestruct (yymsg, yytype, yyvaluep, yylocationp)
    const char *yymsg;
    int yytype;
    YYSTYPE *yyvaluep;
    YYLTYPE *yylocationp;
#endif
{
  YYUSE (yyvaluep);
  YYUSE (yylocationp);

  if (!yymsg)
    yymsg = "Deleting";
  YY_SYMBOL_PRINT (yymsg, yytype, yyvaluep, yylocationp);

  switch (yytype)
    {

      default:
	break;
    }
}

/* Prevent warnings from -Wmissing-prototypes.  */
#ifdef YYPARSE_PARAM
#if defined __STDC__ || defined __cplusplus
int yyparse (void *YYPARSE_PARAM);
#else
int yyparse ();
#endif
#else /* ! YYPARSE_PARAM */
#if defined __STDC__ || defined __cplusplus
int yyparse (void);
#else
int yyparse ();
#endif
#endif /* ! YYPARSE_PARAM */


/* The lookahead symbol.  */
int yychar;

/* The semantic value of the lookahead symbol.  */
YYSTYPE yylval;

/* Location data for the lookahead symbol.  */
YYLTYPE yylloc;

/* Number of syntax errors so far.  */
int yynerrs;



/*-------------------------.
| yyparse or yypush_parse.  |
`-------------------------*/

#ifdef YYPARSE_PARAM
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void *YYPARSE_PARAM)
#else
int
yyparse (YYPARSE_PARAM)
    void *YYPARSE_PARAM;
#endif
#else /* ! YYPARSE_PARAM */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void)
#else
int
yyparse ()

#endif
#endif
{


    int yystate;
    /* Number of tokens to shift before error messages enabled.  */
    int yyerrstatus;

    /* The stacks and their tools:
       `yyss': related to states.
       `yyvs': related to semantic values.
       `yyls': related to locations.

       Refer to the stacks thru separate pointers, to allow yyoverflow
       to reallocate them elsewhere.  */

    /* The state stack.  */
    yytype_int16 yyssa[YYINITDEPTH];
    yytype_int16 *yyss;
    yytype_int16 *yyssp;

    /* The semantic value stack.  */
    YYSTYPE yyvsa[YYINITDEPTH];
    YYSTYPE *yyvs;
    YYSTYPE *yyvsp;

    /* The location stack.  */
    YYLTYPE yylsa[YYINITDEPTH];
    YYLTYPE *yyls;
    YYLTYPE *yylsp;

    /* The locations where the error started and ended.  */
    YYLTYPE yyerror_range[2];

    YYSIZE_T yystacksize;

  int yyn;
  int yyresult;
  /* Lookahead token as an internal (translated) token number.  */
  int yytoken;
  /* The variables used to return semantic value and location from the
     action routines.  */
  YYSTYPE yyval;
  YYLTYPE yyloc;

#if YYERROR_VERBOSE
  /* Buffer for error messages, and its allocated size.  */
  char yymsgbuf[128];
  char *yymsg = yymsgbuf;
  YYSIZE_T yymsg_alloc = sizeof yymsgbuf;
#endif

#define YYPOPSTACK(N)   (yyvsp -= (N), yyssp -= (N), yylsp -= (N))

  /* The number of symbols on the RHS of the reduced rule.
     Keep to zero when no symbol should be popped.  */
  int yylen = 0;

  yytoken = 0;
  yyss = yyssa;
  yyvs = yyvsa;
  yyls = yylsa;
  yystacksize = YYINITDEPTH;

  YYDPRINTF ((stderr, "Starting parse\n"));

  yystate = 0;
  yyerrstatus = 0;
  yynerrs = 0;
  yychar = YYEMPTY; /* Cause a token to be read.  */

  /* Initialize stack pointers.
     Waste one element of value and location stack
     so that they stay on the same level as the state stack.
     The wasted elements are never initialized.  */
  yyssp = yyss;
  yyvsp = yyvs;
  yylsp = yyls;

#if defined YYLTYPE_IS_TRIVIAL && YYLTYPE_IS_TRIVIAL
  /* Initialize the default location before parsing starts.  */
  yylloc.first_line   = yylloc.last_line   = 1;
  yylloc.first_column = yylloc.last_column = 1;
#endif

  goto yysetstate;

/*------------------------------------------------------------.
| yynewstate -- Push a new state, which is found in yystate.  |
`------------------------------------------------------------*/
 yynewstate:
  /* In all cases, when you get here, the value and location stacks
     have just been pushed.  So pushing a state here evens the stacks.  */
  yyssp++;

 yysetstate:
  *yyssp = yystate;

  if (yyss + yystacksize - 1 <= yyssp)
    {
      /* Get the current used size of the three stacks, in elements.  */
      YYSIZE_T yysize = yyssp - yyss + 1;

#ifdef yyoverflow
      {
	/* Give user a chance to reallocate the stack.  Use copies of
	   these so that the &'s don't force the real ones into
	   memory.  */
	YYSTYPE *yyvs1 = yyvs;
	yytype_int16 *yyss1 = yyss;
	YYLTYPE *yyls1 = yyls;

	/* Each stack pointer address is followed by the size of the
	   data in use in that stack, in bytes.  This used to be a
	   conditional around just the two extra args, but that might
	   be undefined if yyoverflow is a macro.  */
	yyoverflow (YY_("memory exhausted"),
		    &yyss1, yysize * sizeof (*yyssp),
		    &yyvs1, yysize * sizeof (*yyvsp),
		    &yyls1, yysize * sizeof (*yylsp),
		    &yystacksize);

	yyls = yyls1;
	yyss = yyss1;
	yyvs = yyvs1;
      }
#else /* no yyoverflow */
# ifndef YYSTACK_RELOCATE
      goto yyexhaustedlab;
# else
      /* Extend the stack our own way.  */
      if (YYMAXDEPTH <= yystacksize)
	goto yyexhaustedlab;
      yystacksize *= 2;
      if (YYMAXDEPTH < yystacksize)
	yystacksize = YYMAXDEPTH;

      {
	yytype_int16 *yyss1 = yyss;
	union yyalloc *yyptr =
	  (union yyalloc *) YYSTACK_ALLOC (YYSTACK_BYTES (yystacksize));
	if (! yyptr)
	  goto yyexhaustedlab;
	YYSTACK_RELOCATE (yyss_alloc, yyss);
	YYSTACK_RELOCATE (yyvs_alloc, yyvs);
	YYSTACK_RELOCATE (yyls_alloc, yyls);
#  undef YYSTACK_RELOCATE
	if (yyss1 != yyssa)
	  YYSTACK_FREE (yyss1);
      }
# endif
#endif /* no yyoverflow */

      yyssp = yyss + yysize - 1;
      yyvsp = yyvs + yysize - 1;
      yylsp = yyls + yysize - 1;

      YYDPRINTF ((stderr, "Stack size increased to %lu\n",
		  (unsigned long int) yystacksize));

      if (yyss + yystacksize - 1 <= yyssp)
	YYABORT;
    }

  YYDPRINTF ((stderr, "Entering state %d\n", yystate));

  if (yystate == YYFINAL)
    YYACCEPT;

  goto yybackup;

/*-----------.
| yybackup.  |
`-----------*/
yybackup:

  /* Do appropriate processing given the current state.  Read a
     lookahead token if we need one and don't already have one.  */

  /* First try to decide what to do without reference to lookahead token.  */
  yyn = yypact[yystate];
  if (yyn == YYPACT_NINF)
    goto yydefault;

  /* Not known => get a lookahead token if don't already have one.  */

  /* YYCHAR is either YYEMPTY or YYEOF or a valid lookahead symbol.  */
  if (yychar == YYEMPTY)
    {
      YYDPRINTF ((stderr, "Reading a token: "));
      yychar = YYLEX;
    }

  if (yychar <= YYEOF)
    {
      yychar = yytoken = YYEOF;
      YYDPRINTF ((stderr, "Now at end of input.\n"));
    }
  else
    {
      yytoken = YYTRANSLATE (yychar);
      YY_SYMBOL_PRINT ("Next token is", yytoken, &yylval, &yylloc);
    }

  /* If the proper action on seeing token YYTOKEN is to reduce or to
     detect an error, take that action.  */
  yyn += yytoken;
  if (yyn < 0 || YYLAST < yyn || yycheck[yyn] != yytoken)
    goto yydefault;
  yyn = yytable[yyn];
  if (yyn <= 0)
    {
      if (yyn == 0 || yyn == YYTABLE_NINF)
	goto yyerrlab;
      yyn = -yyn;
      goto yyreduce;
    }

  /* Count tokens shifted since error; after three, turn off error
     status.  */
  if (yyerrstatus)
    yyerrstatus--;

  /* Shift the lookahead token.  */
  YY_SYMBOL_PRINT ("Shifting", yytoken, &yylval, &yylloc);

  /* Discard the shifted token.  */
  yychar = YYEMPTY;

  yystate = yyn;
  *++yyvsp = yylval;
  *++yylsp = yylloc;
  goto yynewstate;


/*-----------------------------------------------------------.
| yydefault -- do the default action for the current state.  |
`-----------------------------------------------------------*/
yydefault:
  yyn = yydefact[yystate];
  if (yyn == 0)
    goto yyerrlab;
  goto yyreduce;


/*-----------------------------.
| yyreduce -- Do a reduction.  |
`-----------------------------*/
yyreduce:
  /* yyn is the number of a rule to reduce with.  */
  yylen = yyr2[yyn];

  /* If YYLEN is nonzero, implement the default value of the action:
     `$$ = $1'.

     Otherwise, the following line sets YYVAL to garbage.
     This behavior is undocumented and Bison
     users should not rely upon it.  Assigning to YYVAL
     unconditionally makes the parser a bit smaller, and it avoids a
     GCC warning that YYVAL may be used uninitialized.  */
  yyval = yyvsp[1-yylen];

  /* Default location.  */
  YYLLOC_DEFAULT (yyloc, (yylsp - yylen), yylen);
  YY_REDUCE_PRINT (yyn);
  switch (yyn)
    {
        case 2:

    {(yyval.Program)=create_program((yyvsp[(1) - (1)].Stmt_list));
            ;}
    break;

  case 3:

    {(yyval.Stmt_list)=create_stmt_list((yyvsp[(1) - (1)].Stmt));
               ;}
    break;

  case 4:

    {(yyval.Stmt_list)=append_stmt_to_list((yyvsp[(1) - (2)].Stmt_list),(yyvsp[(2) - (2)].Stmt));
                 ;}
    break;

  case 5:

    {(yyval.Stmt)=create_stmt_expr(NULL);
                   ;}
    break;

  case 6:

    {(yyval.Stmt)=create_stmt_expr_list((yyvsp[(1) - (1)].Expr_list));
                   ;}
    break;

  case 7:

    {(yyval.Stmt)=create_stmt_func((yyvsp[(1) - (1)].Func_stmt));
                   ;}
    break;

  case 8:

    {(yyval.Stmt)=create_stmt_proc((yyvsp[(1) - (1)].Proc_stmt));
                   ;}
    break;

  case 9:

    {(yyval.Stmt)=create_stmt_print((yyvsp[(1) - (2)].Print_list));
                       ;}
    break;

  case 10:

    {(yyval.Stmt)=create_stmt_read((yyvsp[(1) - (2)].Read_list));
                      ;}
    break;

  case 11:

    {(yyval.Stmt)=create_stmt_znach((yyvsp[(1) - (1)].Znach_value));
                   ;}
    break;

  case 12:

    {(yyval.Stmt)=create_stmt_decl((yyvsp[(1) - (1)].Decl));
                   ;}
    break;

  case 13:

    {(yyval.Expr)=create_function_call_expr((yyvsp[(1) - (1)].Function_Call));
                             ;}
    break;

  case 14:

    {(yyval.Expr)=create_array_expr((yyvsp[(1) - (4)].Identifier),(yyvsp[(3) - (4)].Expr_list));
                               ;}
    break;

  case 15:

    {(yyval.Expr)=create_expr_id((yyvsp[(1) - (1)].Identifier));
                             ;}
    break;

  case 16:

    {(yyval.Expr)=create_exprlist_expr((yyvsp[(2) - (3)].Expr));
                             ;}
    break;

  case 17:

    {union Const_values value;
                              value.Int=(yyvsp[(1) - (1)].int_const);
                              (yyval.Expr)=create_const_expr(Int, value);
                             ;}
    break;

  case 18:

    {union Const_values value;
                              value.Char=(yyvsp[(1) - (1)].char_const);
                              (yyval.Expr)=create_const_expr(Char, value);
                             ;}
    break;

  case 19:

    {union Const_values value;
                              value.String=(yyvsp[(1) - (1)].string_const);
                              (yyval.Expr)=create_const_expr(String, value);
                             ;}
    break;

  case 20:

    {union Const_values value;
                              value.Double=(yyvsp[(1) - (1)].double_const);
                              (yyval.Expr)=create_const_expr(Double, value);
                             ;}
    break;

  case 21:

    {union Const_values value;
                              strcpy(value.Bool,(yyvsp[(1) - (1)].bool_const));
                              (yyval.Expr)=create_const_expr(Bool, value);
                             ;}
    break;

  case 22:

    {
    (yyval.Expr)=create_op_expr(ASSMNT,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
;}
    break;

  case 23:

    {(yyval.Expr)=create_op_expr(PLUS,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
                             ;}
    break;

  case 24:

    {(yyval.Expr)=create_op_expr(MINUS,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
                             ;}
    break;

  case 25:

    {(yyval.Expr)=create_op_expr(MUL,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
                             ;}
    break;

  case 26:

    {(yyval.Expr)=create_op_expr(DIV,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
                             ;}
    break;

  case 27:

    {(yyval.Expr)=create_op_expr(POW,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
                             ;}
    break;

  case 28:

    {(yyval.Expr)=create_op_expr(GT,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
                             ;}
    break;

  case 29:

    {(yyval.Expr)=create_op_expr(LT,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
                             ;}
    break;

  case 30:

    {(yyval.Expr)=create_op_expr(GTEQ,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
                             ;}
    break;

  case 31:

    {(yyval.Expr)=create_op_expr(LTEQ,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
                             ;}
    break;

  case 32:

    {(yyval.Expr)=create_op_expr(NEQ,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
                             ;}
    break;

  case 33:

    {(yyval.Expr)=create_op_expr(EQ,(yyvsp[(1) - (3)].Expr),(yyvsp[(3) - (3)].Expr));
                             ;}
    break;

  case 34:

    {union Const_values value;
                              value.Bool = (char*)safeAlloc(4);
                              strcpy(value.Bool,"нет");
                              (yyval.Expr)=create_const_expr(Bool, value);
                             ;}
    break;

  case 35:

    {union Const_values value;
                              value.Bool = (char*)safeAlloc(4);
                              strcpy(value.Bool,"да");
                              (yyval.Expr)=create_const_expr(Bool, value);
                             ;}
    break;

  case 36:

    {(yyval.Function_Call)=create_function_call((yyvsp[(1) - (4)].Identifier), (yyvsp[(3) - (4)].Expr_list));
                             ;}
    break;

  case 37:

    {(yyval.Expr_list)=create_expr_list((yyvsp[(1) - (1)].Expr));
                                      ;}
    break;

  case 38:

    {(yyval.Expr_list)=append_expr_to_list((yyvsp[(1) - (3)].Expr_list),(yyvsp[(3) - (3)].Expr));
                                       ;}
    break;

  case 39:

    {
    (yyval.Identifier)=create_ident((yyvsp[(1) - (1)].id));
;}
    break;

  case 40:

    {(yyval.Decl)=create_from_atomic_decl((yyvsp[(1) - (4)].Atomic_type),(yyvsp[(3) - (4)].Enum_atomic_identifiers));
                                                         ;}
    break;

  case 41:

    {(yyval.Decl)=create_from_array_decl((yyvsp[(1) - (4)].Array_type),(yyvsp[(3) - (4)].Enum_array_identifiers));
                                                          ;}
    break;

  case 42:

    {(yyval.Enum_atomic_identifiers)=create_enum_atomic_identifier_list((yyvsp[(1) - (1)].Identifier));
                                             ;}
    break;

  case 43:

    {(yyval.Enum_atomic_identifiers)=append_enum_atomic_identifier_list((yyvsp[(1) - (3)].Enum_atomic_identifiers),(yyvsp[(3) - (3)].Identifier));
                                                       ;}
    break;

  case 44:

    {(yyval.Enum_atomic_identifiers)=append_enum_atomic_identifier_list((yyvsp[(1) - (4)].Enum_atomic_identifiers),(yyvsp[(4) - (4)].Identifier));
                                                     ;}
    break;

  case 45:

    {(yyval.Enum_atomic_identifiers)=append_enum_atomic_identifier_list((yyvsp[(1) - (4)].Enum_atomic_identifiers),(yyvsp[(4) - (4)].Identifier));
                                                     ;}
    break;

  case 46:

    {(yyval.Enum_atomic_identifiers)=append_enum_atomic_identifier_list((yyvsp[(1) - (5)].Enum_atomic_identifiers),(yyvsp[(5) - (5)].Identifier));
                                                       ;}
    break;

  case 47:

    {(yyval.Enum_array_identifiers)=create_enum_array_identifier_list((yyvsp[(1) - (4)].Identifier),(yyvsp[(3) - (4)].Dimensions));
                                                    ;}
    break;

  case 48:

    {(yyval.Enum_array_identifiers)=append_enum_array_identifier_list((yyvsp[(1) - (6)].Enum_array_identifiers),(yyvsp[(3) - (6)].Identifier),(yyvsp[(5) - (6)].Dimensions));
                                                           ;}
    break;

  case 49:

    {(yyval.Func_stmt)=create_func((yyvsp[(3) - (12)].Atomic_type), (yyvsp[(5) - (12)].Identifier),NULL, (yyvsp[(9) - (12)].Stmt_list));
                                                                                       ;}
    break;

  case 50:

    {(yyval.Func_stmt)=create_func((yyvsp[(3) - (13)].Atomic_type), (yyvsp[(5) - (13)].Identifier),(yyvsp[(7) - (13)].Param_list), (yyvsp[(11) - (13)].Stmt_list));
                                                                                         ;}
    break;

  case 51:

    {(yyval.Proc_stmt)=create_proc((yyvsp[(3) - (9)].Identifier), NULL , (yyvsp[(7) - (9)].Stmt_list));
                                                                            ;}
    break;

  case 52:

    {(yyval.Proc_stmt)=create_proc((yyvsp[(3) - (12)].Identifier),(yyvsp[(5) - (12)].Param_list),(yyvsp[(10) - (12)].Stmt_list));
                                                                             ;}
    break;

  case 53:

    {(yyval.Param_list)=create_param_list((yyvsp[(1) - (1)].Param));
                                             ;}
    break;

  case 54:

    {(yyval.Param_list)=append_param_to_list((yyvsp[(1) - (3)].Param_list), (yyvsp[(3) - (3)].Param));
                                              ;}
    break;

  case 55:

    {(yyval.Znach_value)=create_znachvalue((yyvsp[(3) - (3)].Expr));
                                            ;}
    break;

  case 56:

    {(yyval.Param)=create_from_arg_rezvalue((yyvsp[(1) - (1)].Arg_value));
                                                  ;}
    break;

  case 57:

    {(yyval.Param)=create_from_rez_rezvalue((yyvsp[(1) - (1)].Rez_value));
                                                 ;}
    break;

  case 58:

    {(yyval.Arg_value)=create_arg_from_atomic((yyvsp[(3) - (5)].Atomic_type), (yyvsp[(5) - (5)].Enum_atomic_identifiers));
                                                        ;}
    break;

  case 59:

    {(yyval.Arg_value)=create_arg_from_array((yyvsp[(3) - (5)].Array_type), (yyvsp[(5) - (5)].Enum_array_identifiers));
                                                         ;}
    break;

  case 60:

    {(yyval.Rez_value)=create_rez_from_array((yyvsp[(3) - (5)].Array_type), (yyvsp[(5) - (5)].Enum_array_identifiers));
                                                       ;}
    break;

  case 61:

    {(yyval.Rez_value)=create_rez_from_atomic((yyvsp[(3) - (5)].Atomic_type), (yyvsp[(5) - (5)].Enum_atomic_identifiers));
                                                        ;}
    break;

  case 62:

    {(yyval.Dimensions)=create_int_int_dim((yyvsp[(1) - (3)].int_const),(yyvsp[(3) - (3)].int_const));
                                                  ;}
    break;

  case 63:

    {(yyval.Dimensions)=create_id_id_dim((yyvsp[(1) - (3)].Identifier),(yyvsp[(3) - (3)].Identifier));
                                                   ;}
    break;

  case 64:

    {(yyval.Dimensions)=create_int_id_dim((yyvsp[(1) - (3)].int_const),(yyvsp[(3) - (3)].Identifier));
                                                   ;}
    break;

  case 65:

    {(yyval.Dimensions)=create_int_id_dim((yyvsp[(1) - (3)].Identifier), (yyvsp[(3) - (3)].int_const));
                                                   ;}
    break;

  case 66:

    {(yyval.Dimensions)=append_int_int_dim((yyvsp[(1) - (5)].Dimensions), (yyvsp[(3) - (5)].int_const), (yyvsp[(5) - (5)].int_const));
                                                   ;}
    break;

  case 67:

    {(yyval.Dimensions)=append_id_id_dim((yyvsp[(1) - (5)].Dimensions), (yyvsp[(3) - (5)].Identifier), (yyvsp[(5) - (5)].Identifier));
                                                   ;}
    break;

  case 68:

    {(yyval.Dimensions)=append_int_id_dim((yyvsp[(1) - (5)].Dimensions), (yyvsp[(3) - (5)].int_const), (yyvsp[(5) - (5)].Identifier));
                                                   ;}
    break;

  case 69:

    {(yyval.Dimensions)=append_id_int_dim((yyvsp[(1) - (5)].Dimensions), (yyvsp[(3) - (5)].Identifier), (yyvsp[(5) - (5)].int_const));
                                                   ;}
    break;

  case 70:

    {(yyval.Array_type)=create_array_type("целтаб");
       ;}
    break;

  case 71:

    {(yyval.Array_type)=create_array_type("вещтаб");
           ;}
    break;

  case 72:

    {(yyval.Array_type)=create_array_type("симтаб");
         ;}
    break;

  case 73:

    {(yyval.Array_type)=create_array_type("литтаб");
         ;}
    break;

  case 74:

    {(yyval.Array_type)=create_array_type("логтаб");
         ;}
    break;

  case 75:

    {(yyval.Atomic_type)=create_atomic_type("цел");
     ;}
    break;

  case 76:

    {(yyval.Atomic_type)=create_atomic_type("вещ");
        ;}
    break;

  case 77:

    {(yyval.Atomic_type)=create_atomic_type("сим");
        ;}
    break;

  case 78:

    {(yyval.Atomic_type)=create_atomic_type("лит");
        ;}
    break;

  case 79:

    {(yyval.Atomic_type)=create_atomic_type("лог");
       ;}
    break;

  case 80:

    {(yyval.Print_list)=create_expr_list_print((yyvsp[(3) - (3)].Expr_list));
                        ;}
    break;

  case 81:

    {(yyval.Read_list)=create_expr_list_read((yyvsp[(3) - (3)].Expr_list));
                        ;}
    break;



      default: break;
    }
  YY_SYMBOL_PRINT ("-> $$ =", yyr1[yyn], &yyval, &yyloc);

  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);

  *++yyvsp = yyval;
  *++yylsp = yyloc;

  /* Now `shift' the result of the reduction.  Determine what state
     that goes to, based on the state we popped back to and the rule
     number reduced by.  */

  yyn = yyr1[yyn];

  yystate = yypgoto[yyn - YYNTOKENS] + *yyssp;
  if (0 <= yystate && yystate <= YYLAST && yycheck[yystate] == *yyssp)
    yystate = yytable[yystate];
  else
    yystate = yydefgoto[yyn - YYNTOKENS];

  goto yynewstate;


/*------------------------------------.
| yyerrlab -- here on detecting error |
`------------------------------------*/
yyerrlab:
  /* If not already recovering from an error, report this error.  */
  if (!yyerrstatus)
    {
      ++yynerrs;
#if ! YYERROR_VERBOSE
      yyerror (YY_("syntax error"));
#else
      {
	YYSIZE_T yysize = yysyntax_error (0, yystate, yychar);
	if (yymsg_alloc < yysize && yymsg_alloc < YYSTACK_ALLOC_MAXIMUM)
	  {
	    YYSIZE_T yyalloc = 2 * yysize;
	    if (! (yysize <= yyalloc && yyalloc <= YYSTACK_ALLOC_MAXIMUM))
	      yyalloc = YYSTACK_ALLOC_MAXIMUM;
	    if (yymsg != yymsgbuf)
	      YYSTACK_FREE (yymsg);
	    yymsg = (char *) YYSTACK_ALLOC (yyalloc);
	    if (yymsg)
	      yymsg_alloc = yyalloc;
	    else
	      {
		yymsg = yymsgbuf;
		yymsg_alloc = sizeof yymsgbuf;
	      }
	  }

	if (0 < yysize && yysize <= yymsg_alloc)
	  {
	    (void) yysyntax_error (yymsg, yystate, yychar);
	    yyerror (yymsg);
	  }
	else
	  {
	    yyerror (YY_("syntax error"));
	    if (yysize != 0)
	      goto yyexhaustedlab;
	  }
      }
#endif
    }

  yyerror_range[0] = yylloc;

  if (yyerrstatus == 3)
    {
      /* If just tried and failed to reuse lookahead token after an
	 error, discard it.  */

      if (yychar <= YYEOF)
	{
	  /* Return failure if at end of input.  */
	  if (yychar == YYEOF)
	    YYABORT;
	}
      else
	{
	  yydestruct ("Error: discarding",
		      yytoken, &yylval, &yylloc);
	  yychar = YYEMPTY;
	}
    }

  /* Else will try to reuse lookahead token after shifting the error
     token.  */
  goto yyerrlab1;


/*---------------------------------------------------.
| yyerrorlab -- error raised explicitly by YYERROR.  |
`---------------------------------------------------*/
yyerrorlab:

  /* Pacify compilers like GCC when the user code never invokes
     YYERROR and the label yyerrorlab therefore never appears in user
     code.  */
  if (/*CONSTCOND*/ 0)
     goto yyerrorlab;

  yyerror_range[0] = yylsp[1-yylen];
  /* Do not reclaim the symbols of the rule which action triggered
     this YYERROR.  */
  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);
  yystate = *yyssp;
  goto yyerrlab1;


/*-------------------------------------------------------------.
| yyerrlab1 -- common code for both syntax error and YYERROR.  |
`-------------------------------------------------------------*/
yyerrlab1:
  yyerrstatus = 3;	/* Each real token shifted decrements this.  */

  for (;;)
    {
      yyn = yypact[yystate];
      if (yyn != YYPACT_NINF)
	{
	  yyn += YYTERROR;
	  if (0 <= yyn && yyn <= YYLAST && yycheck[yyn] == YYTERROR)
	    {
	      yyn = yytable[yyn];
	      if (0 < yyn)
		break;
	    }
	}

      /* Pop the current state because it cannot handle the error token.  */
      if (yyssp == yyss)
	YYABORT;

      yyerror_range[0] = *yylsp;
      yydestruct ("Error: popping",
		  yystos[yystate], yyvsp, yylsp);
      YYPOPSTACK (1);
      yystate = *yyssp;
      YY_STACK_PRINT (yyss, yyssp);
    }

  *++yyvsp = yylval;

  yyerror_range[1] = yylloc;
  /* Using YYLLOC is tempting, but would change the location of
     the lookahead.  YYLOC is available though.  */
  YYLLOC_DEFAULT (yyloc, (yyerror_range - 1), 2);
  *++yylsp = yyloc;

  /* Shift the error token.  */
  YY_SYMBOL_PRINT ("Shifting", yystos[yyn], yyvsp, yylsp);

  yystate = yyn;
  goto yynewstate;


/*-------------------------------------.
| yyacceptlab -- YYACCEPT comes here.  |
`-------------------------------------*/
yyacceptlab:
  yyresult = 0;
  goto yyreturn;

/*-----------------------------------.
| yyabortlab -- YYABORT comes here.  |
`-----------------------------------*/
yyabortlab:
  yyresult = 1;
  goto yyreturn;

#if !defined(yyoverflow) || YYERROR_VERBOSE
/*-------------------------------------------------.
| yyexhaustedlab -- memory exhaustion comes here.  |
`-------------------------------------------------*/
yyexhaustedlab:
  yyerror (YY_("memory exhausted"));
  yyresult = 2;
  /* Fall through.  */
#endif

yyreturn:
  if (yychar != YYEMPTY)
     yydestruct ("Cleanup: discarding lookahead",
		 yytoken, &yylval, &yylloc);
  /* Do not reclaim the symbols of the rule which action triggered
     this YYABORT or YYACCEPT.  */
  YYPOPSTACK (yylen);
  YY_STACK_PRINT (yyss, yyssp);
  while (yyssp != yyss)
    {
      yydestruct ("Cleanup: popping",
		  yystos[*yyssp], yyvsp, yylsp);
      YYPOPSTACK (1);
    }
#ifndef yyoverflow
  if (yyss != yyssa)
    YYSTACK_FREE (yyss);
#endif
#if YYERROR_VERBOSE
  if (yymsg != yymsgbuf)
    YYSTACK_FREE (yymsg);
#endif
  /* Make sure YYID is used.  */
  return YYID (yyresult);
}






void yyerror (char const* s)
{
    printf("%s\n",s);
    printf("\nERROR on line: %d:%d\n", yylloc.first_line,yylloc.last_line);
    getchar();
    exit(0);
}

int main (int argc, char* argv[])
{

    init_safeAlloc();

    char* buf = (char*)safeAlloc(100);
    int i;

    for (i=0; i<HASH_ARRAY_SZ; i++)
        hashes[i]=i;
    hash_pointer = 0;

    setlocale(LC_CTYPE, "russian");

    if (argc == 1)
    {
        exit(1);
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




