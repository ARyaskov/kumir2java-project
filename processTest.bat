@parser.exe unittests\procsAndFuncs2.kum unittests\procsAndFuncs2.dot unittests\procsAndFuncs2.loc &
@move unittests\procsAndFuncs1.dot temp\procsAndFuncs1_now.dot &
@unix_tools\bin\iconv.exe -f WINDOWS-1251 -t UTF-8 "temp/procsAndFuncs2_now.dot" > "unittests/procsAndFuncs2.dot" &
@unix_tools\bin\dot.exe  -Tpng "unittests/procsAndFuncs2.dot" -o"unittests/images/procsAndFuncs2.png" &
@echo "Ok!" 
