
%option noyywrap
%option never-interactive

%{

/* Исходный код лексического анализатора */
#include "..\headers\parser.tab.h"
#include "..\headers\tree_structs.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <locale.h>
#include <math.h>

#define YY_NO_UNISTD_H

char commentBuf[2000];
char literal[1000];
int integer;
double doubl;
FILE * logfile;
int res;
FILE * err;
#define YYDEBUG 1

 extern int yyparse(void);




#define YY_USER_ACTION {int colnum = 0; \
                        yylloc.first_line = yylineno; \
                        yylloc.first_column = colnum;\
                        colnum=colnum+yyleng; \
                        yylloc.last_column=colnum; \
                        yylloc.last_line = yylineno; \
                       }




%}
%option yylineno
%x COMMENT
%x STRING1
%x STRING2
%%


"|"						{ fprintf(logfile,"\nКомментарий:\n");  BEGIN(COMMENT); }


<COMMENT>\n				{
						  fprintf(logfile,"\nКонец комментария\n");
						  BEGIN(INITIAL);
						}


"алг"					{fprintf(logfile,"\nВстретилось ключевое слово \"алг\"!");return ALG;}
"арг"					{fprintf(logfile,"\nВстретилось ключевое слово \"арг\"!");return ARG;}
"ввод"					{fprintf(logfile,"\nВстретилось ключевое слово \"дано\"!");return VVOD;}
"вещ"					{fprintf(logfile,"\nВстретилось ключевое слово \"вещ\"!");return VESCH;}
"вещтаб"				{fprintf(logfile,"\nВстретилось ключевое слово \"вещтаб\"!");return VESCHTAB;}
"вывод"					{fprintf(logfile,"\nВстретилось ключевое слово \"вывод\"!");return VYVOD;}
"да"					{fprintf(logfile,"\nВстретилось ключевое слово \"да\"!");return DA;}
"знач"					{fprintf(logfile,"\nВстретилось ключевое слово \"знач\"!");return ZNACH;}
"кон"					{fprintf(logfile,"\nВстретилось ключевое слово \"кон\"!");return KON;}
"лит"					{fprintf(logfile,"\nВстретилось ключевое слово \"лит\"!");return LIT;}
"литтаб"				{fprintf(logfile,"\nВстретилось ключевое слово \"литтаб\"!");return LITTAB;}
"лог"					{fprintf(logfile,"\nВстретилось ключевое слово \"лог\"!");return LOG;}
"логтаб"				{fprintf(logfile,"\nВстретилось ключевое слово \"логтаб\"!");return LOGTAB;}
"нач"					{fprintf(logfile,"\nВстретилось ключевое слово \"нач\"!");return NACH;}
"нет"					{fprintf(logfile,"\nВстретилось ключевое слово \"нет\"!");return NET;}
"нс"					{fprintf(logfile,"\nВстретилось ключевое слово \"нс\"!");return NS;}
"рез"					{fprintf(logfile,"\nВстретилось ключевое слово \"рез\"!");return REZ;}
"сим"					{fprintf(logfile,"\nВстретилось ключевое слово \"сим\"!");return SIM;}
"симтаб"				{fprintf(logfile,"\nВстретилось ключевое слово \"симтаб\"!");return SIMTAB;}
"цел"					{fprintf(logfile,"\nВстретилось ключевое слово \"цел\"!");return CEL;}
"целтаб"				{fprintf(logfile,"\nВстретилось ключевое слово \"целтаб\"!");return CELTAB;}



"+"						{fprintf(logfile,"\nВстретилась операция \"Сложение\"!");return PLUS;}
"-"						{fprintf(logfile,"\nВстретилась операция \"Вычитание\"!");return MINUS;}
"*"						{fprintf(logfile,"\nВстретилась операция \"Умножение\"!");return MUL;}
"/"						{fprintf(logfile,"\nВстретилась операция \"Деление\"!");return DIV;}
"**"					{fprintf(logfile,"\nВстретилась операция \"Возведение в степень\"!");return POW;}

":="					{fprintf(logfile,"\nВстретился оператор \"Присваивание\"!");return ASSMNT;}

"="						{fprintf(logfile,"\nВстретилась операция \"Равно\"!");return EQ;}
"<>"					{fprintf(logfile,"\nВстретился оператор \"Не равно\"!");return NEQ;}
"<"						{fprintf(logfile,"\nВстретился оператор \"Меньше\"!");return LT;}
">"						{fprintf(logfile,"\nВстретился оператор \"Больше\"!");return GT;}
"<="					{fprintf(logfile,"\nВстретился оператор \"Меньше или равно\"!");return LTEQ;}
">="					{fprintf(logfile,"\nВстретился оператор \"Больше или равно\"!");return GTEQ;}

"\["					{fprintf(logfile,"\nВстретилась открывающая квадратная скобка!");return '[';}
"]"				    	{fprintf(logfile,"\nВстретилась закрывающая квадратная скобка!");return ']';}

"\("					{fprintf(logfile,"\nВстретилась открывающая круглая скобка!");return '(';}
")"					    {fprintf(logfile,"\nВстретилась закрывающая круглая скобка!");return ')';}

":"						{fprintf(logfile,"\nВстретилось двоеточие!");return ':';}
","						{fprintf(logfile,"\nВстретилась запятая!");return ',';}
";"                     {fprintf(logfile,"\nВстретилась запятая!");return ';';}

[\n]+                   {
                          fprintf(logfile,"\nВстретился перевод (множественный) строки!");
                          return ENDL;
                          yylineno++;
                          }



[0-9]+					{fprintf(logfile,"\nВстретилось десятичное число: %d",atoi(yytext));
                        yylval.int_const = atoi(yytext);
						return INT_CONST;}

[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?  {

                                           yylval.double_const = atof(yytext);
				                           fprintf(logfile,"\nВстретилось число с плавающей точкой: %f",yylval.double_const);
                                           return DOUBLE_CONST;
				                   }


[А-Яа-яA-Za-z_ё@]+[0-9А-Яа-яA-Za-z_ё@]*  {
if (!strcmp(yytext, "да") || !strcmp(yytext, "нет"))
{
  fprintf(logfile,"\nВстретился логический идентификатор: %s",yytext);
  strcpy(yylval.bool_const, yytext);
  res = BOOL_CONST;
}else
{
          fprintf(logfile,"\nВстретился идентификатор: %s",yytext);
          yylval.id = (char*)malloc(strlen(yytext));
		  strcpy(yylval.id, yytext);
		  res = ID;
}
return res;
		 }






\$[0-9A-F]+			{
						  sscanf(yytext+1,"%x", &yylval.int_const);
					      fprintf(logfile,"\nВстретилось шестнадцатиричное число: %x",yylval.int_const);
						  return INT_CONST;
						}



\"						{ strcpy(literal,""); BEGIN(STRING1);}
<STRING1>[^\"]+			{ strcat(literal, yytext);}
<STRING1>\"				{

 if ( strlen(literal) != 1 )
{
  fprintf(logfile,  "\nВстретилась строка: \"%s\"", literal);
  yylval.string_const = (char*)malloc(strlen(literal));
  strcpy(yylval.string_const, literal);
  res = STRING_CONST;
}else
{
  fprintf(logfile,  "\nВстретилась символьная константа: \"%c\"", literal[0]);
  yylval.char_const = literal[0] ;
  res = CHAR_CONST;
}
  BEGIN(INITIAL);
  return res;
        }



\'						{ strcpy(literal,""); BEGIN(STRING2);}
<STRING2>[^\']+			{ strcat(literal, yytext);}
<STRING2>\'				{

if ( strlen(literal) != 1 )
{
  fprintf(logfile,  "\nВстретилась строка: \"%s\"", literal);
  yylval.string_const = (char*)malloc(strlen(literal));
  strcpy(yylval.string_const, literal);
  res = STRING_CONST;
}else
{
  fprintf(logfile,  "\nВстретилась символьная константа: \'%c\'", literal[0]);
  yylval.char_const = literal[0] ;
  res = CHAR_CONST;
}
  BEGIN(INITIAL);
  return res;
        }



%%


