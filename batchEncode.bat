@echo "Encoding begin..." &
@move unittests\procsAndFuncs.dot temp\procsAndFuncs_now.dot &
@unix_tools\bin\iconv.exe -f WINDOWS-1251 -t UTF-8 "temp/procsAndFuncs_now.dot" > "unittests/procsAndFuncs.dot" &
@move unittests\decls1.dot temp\decls1_now.dot &
@unix_tools\bin\iconv.exe -f WINDOWS-1251 -t UTF-8 "temp/decls1_now.dot" > "unittests/decls1.dot" &
@move unittests\decls2.dot temp\decls2_now.dot &
@unix_tools\bin\iconv.exe -f WINDOWS-1251 -t UTF-8 "temp/decls2_now.dot" > "unittests/decls2.dot" &
@echo "Encoding end..."