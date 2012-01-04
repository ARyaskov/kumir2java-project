@del sources\lex.yy.c
@del sources\parser.tab.c
@unix_tools\bin\flex.exe -L other/lexer.l
@unix_tools\bin\bison.exe -d --no-lines --verbose other/parser.y
@move lex.yy.c sources\lex.yy.c
@move parser.tab.c sources\parser.tab.c
@move parser.tab.h headers\parser.tab.h
@move parser.output output\parser.output