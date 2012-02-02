@rem @echo "Lexical and syntax analisys..." &
@rem @..\parser.exe semantic\source.kum semantic\source.dot semantic\source.loc &
@rem @echo "Semantic analisys..." &
@rem @java.exe -jar .\dist\semantic.jar source.kum
@rem @echo "Code generation..." &
@java.exe MainClass
