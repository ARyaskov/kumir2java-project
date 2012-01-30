@parser.exe unittests\procsAndFuncs1.kum unittests\procsAndFuncs1.dot unittests\procsAndFuncs1.loc &
@move unittests\procsAndFuncs1.dot temp\procsAndFuncs1_now.dot &
@unix_tools\bin\iconv.exe -f WINDOWS-1251 -t UTF-8 "temp/procsAndFuncs1_now.dot" > "unittests/procsAndFuncs1.dot" &
@unix_tools\bin\dot.exe  -Tpng "unittests/procsAndFuncs1.dot" -o"unittests/images/procsAndFuncs1.png" &
@echo "Ok!" 
