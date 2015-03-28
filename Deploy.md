

## Общее ##
Следует скачать и установить CodeBlocks - http://www.codeblocks.org/downloads/26, версию codeblocks-10.05mingw-setup.exe
Проект построителя дерева - parser.cbp, находится в trunk. Причина - компилятор в MS Visual Studio не поддерживает даже С99, что означает кучу неудобных вещей, таких как объявление всех переменных в начале функции и подобных. А в CodeBlocks для этого достаточно прописать в настройках компилятора _-std=c99_. И вообще среда хорошая :)

Позже сделаю Makefile, чтобы собирать из батника.

### Внимание! ###
Не следует менять кодировку отображения в редакторе! При смене кодировки скажем на UTF-8 c 1251 получается, что подобные вещи

```с

#ifdef OUT2DOT
uniqID = makeUniqueID("create_ident");
ident->print_val = uniqID;
temp_str = strcat_const("\"Идентификатор: ", id);
strcat(temp_str, "\"");
makeNode(uniqID, temp_str);
assert(ident->print_val);
#endif
```

сохраняются в UTF-8, а некоторые нужные участки в 1251, и iconv не знает как ему сконвертировать.
Одним словом - **стоит убедиться**, что в CodeBlocks в меню Settings->Editor->поле Encoding стоит Windows-1251!

## Быстрый старт ##

Запустите файл process.bat в корне.

```bat

@call batchUnitTests.bat &
@call batchEncode.bat &
@call batchDOT2PNG.bat &
@call batchDiff.bat
```
В результате этого появится несколько файлов:
  * Cинтаксическое дерево в формате dot - в папке unittests

  * Изображение синтаксического дерева в формате PNG - в папке unittests/images


## Добавление новых программ для модульного тестирования ##
Всё просто - в файл batchUnitTests.bat (желательно в конец, но обязательно до строки @echo "Unittesting end...") стоит добавить строку вида
```bat

@parser.exe unittests\procsAndFuncs.kum unittests\procsAndFuncs.dot unittests\procsAndFuncs.loc &
```
  * @ - подавляет вывод данной строки на консоль
  * unittests\procsAndFuncs.kum - где находится испытуемая программа
  * unittests\procsAndFuncs.dot - куда стоит положить результат
  * unittests\procsAndFuncs.loc - информация о положениях в тексте для семантического анализатора
  * & - соединяет команду со следующей (если Вы добавили её в конец, перед @echo "Unittesting end...", то & позволит echo выполниться, иначе обработчик посчитает эти две строки за одну команду).

Теперь в файл batchEncode.bat так же, до @echo "Encoding end..." добавляем строки вида
```bat

@move unittests\decls2.dot temp\decls2_now.dot &
@unix_tools\bin\iconv.exe -f WINDOWS-1251 -t UTF-8 "temp/decls2_now.dot" > "unittests/decls2.dot" &
```
  * > - это знак перенаправления в файл - вывод программы записывается в файл "unittests/decls2.dot"
Команда move перемещает исходный файл во временную папку, iconv проводит необходимые преобразования с файлом в temp, записывая результат обратно в требуемую папку.

**Зачем нужно было переносить файл? Почему нельзя написать сразу чтение и запись в один и тот же файл?** Если прописать iconv одинаковые пути, то на выходе получим просто пустой файл, так как iconv при открытии ставит блокировку на файл, успешно читает и конвертирует, но не может записать назад текст.

Следующий шаг - добавление в batchDOT2PNG.bat так же, до @echo "Dot2PNG end..." строчки вида
```bat

@dot.exe -Tpng "unittests/decls2.dot" -o"unittests/images/decls2.png" &
```
  * Tpng - формат вывода
  * unittests/decls2.dot - исходный файл для преобрахования
  * -o"unittests/images/decls2.png" - куда положить полученный png.

Последний шаг - добавление в batchDiff.bat так же, до @echo "Diff end..." строчки вида
```bat

@unix_tools\bin\diff.exe -a -B -w -b -E unittests/read2.dot unittests/ideals/read2.dot &
```
  * a -B -w -b -E - ключи, пропускающие незначительные детали при сравнении (изменение числа пробелов, табуляция и т.д.)
На данном шаге производится сравнение файла с идеалом. (Разумеется, при первом запуске стоит вручную  проверить **dot**-файл и, если он верен, то положить его в папку unittests\ideals\).


## Сборка генераторов ##

Сборка генераторов осуществляется запуском файла processFlexBison.bat

Вот он:

```bat

@del sources\lex.yy.c
@del sources\parser.tab.c
@unix_tools\bin\flex.exe -L other/lexer.l
@unix_tools\bin\bison.exe -d --no-lines --verbose other/parser.y
@move lex.yy.c sources\lex.yy.c
@move parser.tab.c sources\parser.tab.c
@move parser.tab.h headers\parser.tab.h
@move parser.output output\parser.output
```

**Почему используются unix-овые flex и bison?** Они более свежие, в них работает много директив, которые не работают в файлах с moodle, к примеру, %debug, позволяющая выводить более детальную отладочную информацию.

**Что за опция --verbose?** Указывает bison сгенерировать развёрнутое описание в parser.output

**Что за файл parser.output?** Очень полезный файл, в нём расписаны все состояния синтаксического анализатора, переходы между ними и много другой полезной информации, кроме того, с помощью него можно разрешить конфликты.

**Что за опция --no-lines?** Указывает bison'у не генерировать в коде парсера директивы #line XX - мне показалось это слегка неудобным в отладке, потому как стоит постоянно перескакивать с parser.tab.c на parser.y. Опция -L у Flex аналогична.


## Создание dot-файлов для модульных тестов ##

Создание dot-файлов для модульных тестов осуществляется с помощью созданного нами _parser.exe_ в корне, путём запуска файла _batchUnitTests.bat_ в trunk.

```bat

@echo "Unittesting begin..." &
@parser.exe unittests\procsAndFuncs.kum unittests\procsAndFuncs.dot &
@parser.exe unittests\decls1.kum unittests\decls1.dot &
@parser.exe unittests\decls2.kum unittests\decls2.dot &
@echo "Unittesting end..."
```

### Внимание! ###
Не стоит запускать этот файл в одиночку. Для цепочки создание-dot->перекодировка-в-UTF8->создание-PNG->сравнение есть более короткий путь - запуск файла **process.bat** в корне!

## Перекодировка ##

Перекодировка файлов dot осуществляется с помощью iconv, путём запуска файла _batchEncode.bat_ в trunk.
```bat

@echo "Encoding begin..." &
@move unittests\procsAndFuncs.dot temp\procsAndFuncs_now.dot &
@unix_tools\bin\iconv.exe -f WINDOWS-1251 -t UTF-8 "temp/procsAndFuncs_now.dot" > "unittests/procsAndFuncs.dot" &
@move unittests\decls1.dot temp\decls1_now.dot &
@unix_tools\bin\iconv.exe -f WINDOWS-1251 -t UTF-8 "temp/decls1_now.dot" > "unittests/decls1.dot" &
@move unittests\decls2.dot temp\decls2_now.dot &
@unix_tools\bin\iconv.exe -f WINDOWS-1251 -t UTF-8 "temp/decls2_now.dot" > "unittests/decls2.dot" &
@echo "Encoding end..."
```

### Внимание! ###
Не стоит запускать этот файл в одиночку. Для цепочки создание-dot->перекодировка-в-UTF8->создание-PNG->сравнение есть более короткий путь - запуск файла **process.bat** в корне!


## Создание PNG-файлов ##

Создание PNG-файлов осуществляется с помощью dot, путём запуска файла _batchDOT2PNG.bat_ в trunk. Установленный GraphViz (http://www.graphviz.org/) должен прописать в PATH путь до dot.exe, иначе **его нужно прописать самому** (обычно это `<GraphViz>`/bin/dot.exe).

```bat

@echo "Dot2PNG begin..." &
@dot.exe -Tpng "unittests/procsAndFuncs.dot" -o"unittests/images/procsAndFuncs.png" &
@dot.exe -Tpng "unittests/decls1.dot" -o"unittests/images/decls1.png" &
@dot.exe -Tpng "unittests/decls2.dot" -o"unittests/images/decls2.png" &
@echo "Dot2PNG end..."
```

### Внимание! ###
Не стоит запускать этот файл в одиночку. Для цепочки создание-dot->перекодировка-в-UTF8->создание-PNG->сравнение есть более короткий путь - запуск файла **process.bat** в корне!


## Сравнение файлов ##

Сравнение файлов осуществляется с помощью diff, путём запуска файла _batchDiff.bat_ в trunk.

```bat

@echo "Diff begin..." &
@unix_tools\bin\diff.exe -a -B -w -b -E unittests/decls1.dot unittests/ideals/decls1.dot &
@unix_tools\bin\diff.exe -a -B -w -b -E unittests/decls2.dot unittests/ideals/decls2.dot &
@echo "Diff end..."
```

### Внимание! ###
Не стоит запускать этот файл в одиночку. Для цепочки создание-dot->перекодировка-в-UTF8->создание-PNG->сравнение есть более короткий путь - запуск файла **process.bat** в корне!