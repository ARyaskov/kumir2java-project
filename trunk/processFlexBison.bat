@del sources\lex.yy.c
@del sources\parserMV.tab.c
@unix_tools\bin\flex.exe -L other/lexer.l
@unix_tools\bin\bison.exe -d --no-lines --verbose other/parserMV.y
@move lex.yy.c sources\lex.yy.c
@move parser.tab.c sources\parser.tab.c
@move parser.tab.h headers\parser.tab.h
@move parser.output output\parser.output