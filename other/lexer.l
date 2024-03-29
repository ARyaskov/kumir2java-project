
%option noyywrap
%option never-interactive

%{

/* �������� ��� ������������ ����������� */
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


"|"						{ fprintf(logfile,"\n�����������:\n");  BEGIN(COMMENT); }


<COMMENT>\n				{
						  fprintf(logfile,"\n����� �����������\n");
						  BEGIN(INITIAL);
						}


"����"                  {fprintf(logfile,"\n����������� �������� ����� \"����\"!");return ESLI;}
"��"                    {fprintf(logfile,"\n����������� �������� ����� \"��\"!");return TO;}
"��"                    {fprintf(logfile,"\n����������� �������� ����� \"��\"!");return NC;}
"��"                    {fprintf(logfile,"\n����������� �������� ����� \"��\"!");return KC;}
"��_���"                {fprintf(logfile,"\n����������� �������� ����� \"��_���\"!");return KC_PRI;}
"�� ���"                {fprintf(logfile,"\n����������� �������� ����� \"�� ���\"!");return KC_PRI;}
"�����"                 {fprintf(logfile,"\n����������� �������� ����� \"�����\"!");return VYBOR;}
"���"                   {fprintf(logfile,"\n����������� �������� ����� \"���\"!");return UTV;}
"�����"                 {fprintf(logfile,"\n����������� �������� ����� \"�����\"!");return INACHE;}
"���"                   {fprintf(logfile,"\n����������� �������� ����� \"���\"!");return VSE;}
"���"                   {fprintf(logfile,"\n����������� �������� ����� \"���\"!");return RAZ;}
"����"                   {fprintf(logfile,"\n����������� �������� ����� \"����\"!");return POKA;}
"���"                   {fprintf(logfile,"\n����������� �������� ����� \"���\"!");return PRI;}

"���"					{fprintf(logfile,"\n����������� �������� ����� \"���\"!");return ALG;}
"���"					{fprintf(logfile,"\n����������� �������� ����� \"���\"!");return ARG;}
"����"					{fprintf(logfile,"\n����������� �������� ����� \"����\"!");return VVOD;}
"���"					{fprintf(logfile,"\n����������� �������� ����� \"���\"!");return VESCH;}
"������"				{fprintf(logfile,"\n����������� �������� ����� \"������\"!");return VESCHTAB;}
"�����"					{fprintf(logfile,"\n����������� �������� ����� \"�����\"!");return VYVOD;}
"��"					{fprintf(logfile,"\n����������� �������� ����� \"��\"!");return DA;}
"����"					{fprintf(logfile,"\n����������� �������� ����� \"����\"!");return ZNACH;}
"���"					{fprintf(logfile,"\n����������� �������� ����� \"���\"!");return KON;}
"���"					{fprintf(logfile,"\n����������� �������� ����� \"���\"!");return LIT;}
"������"				{fprintf(logfile,"\n����������� �������� ����� \"������\"!");return LITTAB;}
"���"					{fprintf(logfile,"\n����������� �������� ����� \"���\"!");return LOG;}
"������"				{fprintf(logfile,"\n����������� �������� ����� \"������\"!");return LOGTAB;}
"���"					{fprintf(logfile,"\n����������� �������� ����� \"���\"!");return NACH;}
"���"					{fprintf(logfile,"\n����������� �������� ����� \"���\"!");return NET;}
"��"					{fprintf(logfile,"\n����������� �������� ����� \"��\"!");return NS;}
"���"					{fprintf(logfile,"\n����������� �������� ����� \"���\"!");return REZ;}
"���"					{fprintf(logfile,"\n����������� �������� ����� \"���\"!");return SIM;}
"������"				{fprintf(logfile,"\n����������� �������� ����� \"������\"!");return SIMTAB;}
"���"					{fprintf(logfile,"\n����������� �������� ����� \"���\"!");return CEL;}
"������"				{fprintf(logfile,"\n����������� �������� ����� \"������\"!");return CELTAB;}



"+"						{fprintf(logfile,"\n����������� �������� \"��������\"!");return PLUS;}
"-"						{fprintf(logfile,"\n����������� �������� \"���������\"!");return MINUS;}
"*"						{fprintf(logfile,"\n����������� �������� \"���������\"!");return MUL;}
"/"						{fprintf(logfile,"\n����������� �������� \"�������\"!");return DIV;}
"**"					{fprintf(logfile,"\n����������� �������� \"���������� � �������\"!");return POW;}

":="					{fprintf(logfile,"\n���������� �������� \"������������\"!");return ASSMNT;}

"="						{fprintf(logfile,"\n����������� �������� \"�����\"!");return EQ;}
"<>"					{fprintf(logfile,"\n���������� �������� \"�� �����\"!");return NEQ;}
"<"						{fprintf(logfile,"\n���������� �������� \"������\"!");return LT;}
">"						{fprintf(logfile,"\n���������� �������� \"������\"!");return GT;}
"<="					{fprintf(logfile,"\n���������� �������� \"������ ��� �����\"!");return LTEQ;}
">="					{fprintf(logfile,"\n���������� �������� \"������ ��� �����\"!");return GTEQ;}

"\["					{fprintf(logfile,"\n����������� ����������� ���������� ������!");return '[';}
"]"				    	{fprintf(logfile,"\n����������� ����������� ���������� ������!");return ']';}

"\("					{fprintf(logfile,"\n����������� ����������� ������� ������!");return '(';}
")"					    {fprintf(logfile,"\n����������� ����������� ������� ������!");return ')';}

":"						{fprintf(logfile,"\n����������� ���������!");return ':';}
","						{fprintf(logfile,"\n����������� �������!");return ',';}
";"                     {fprintf(logfile,"\n����������� �������!");return ';';}

[\n]+                   {
                          fprintf(logfile,"\n���������� ������� (�������������) ������!");
                          return ENDL;
                          yylineno++;
                          }



[0-9]+					{fprintf(logfile,"\n����������� ���������� �����: %d",atoi(yytext));
                        yylval.int_const = atoi(yytext);
						return INT_CONST;}

[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?  {

                                           yylval.double_const = atof(yytext);
				                           fprintf(logfile,"\n����������� ����� � ��������� ������: %f",yylval.double_const);
                                           return DOUBLE_CONST;
				                   }

��(_|[[:SPACE:]]+)���	{ fprintf(logfile,"\n���������� c���������� �������� ���������� �����!");
                                           return KC_PRI;
						}

[�-��-�A-Za-z_�@]+[0-9�-��-�A-Za-z_�@]*  {
if (!strcmp(yytext, "��") || !strcmp(yytext, "���"))
{
  fprintf(logfile,"\n���������� ���������� �������������: %s",yytext);
  strcpy(yylval.bool_const, yytext);
  res = BOOL_CONST;
}else
{
          fprintf(logfile,"\n���������� �������������: %s",yytext);
          yylval.id = (char*)malloc(strlen(yytext));
		  strcpy(yylval.id, yytext);
		  res = ID;
}
return res;
		 }






\$[0-9A-F]+			{
						  sscanf(yytext+1,"%x", &yylval.int_const);
					      fprintf(logfile,"\n����������� ����������������� �����: %x",yylval.int_const);
						  return INT_CONST;
						}



\"						{ strcpy(literal,""); BEGIN(STRING1);}
<STRING1>[^\"]+			{ strcat(literal, yytext);}
<STRING1>\"				{

 if ( strlen(literal) != 1 )
{
  fprintf(logfile,  "\n����������� ������: \"%s\"", literal);
  yylval.string_const = (char*)malloc(strlen(literal));
  strcpy(yylval.string_const, literal);
  res = STRING_CONST;
}else
{
  fprintf(logfile,  "\n����������� ���������� ���������: \"%c\"", literal[0]);
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
  fprintf(logfile,  "\n����������� ������: \"%s\"", literal);
  yylval.string_const = (char*)malloc(strlen(literal));
  strcpy(yylval.string_const, literal);
  res = STRING_CONST;
}else
{
  fprintf(logfile,  "\n����������� ���������� ���������: \'%c\'", literal[0]);
  yylval.char_const = literal[0] ;
  res = CHAR_CONST;
}
  BEGIN(INITIAL);
  return res;
        }

. {}

%%


