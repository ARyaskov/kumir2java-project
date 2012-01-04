@echo "Dot2PNG begin..." &
@dot.exe -Tpng "unittests/procsAndFuncs.dot" -o"unittests/images/procsAndFuncs.png" &
@dot.exe -Tpng "unittests/decls1.dot" -o"unittests/images/decls1.png" & 
@dot.exe -Tpng "unittests/decls2.dot" -o"unittests/images/decls2.png" &
@echo "Dot2PNG end..."