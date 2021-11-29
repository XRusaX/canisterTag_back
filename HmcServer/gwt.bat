set src=src\main\java
set entryPoint=com.ma.hmc.demo.demo
set gwt=C:\linux\gitrepos-aoit\hmc-server\common-java\Libs\gwt-2.8.1

java -cp %src%;%gwt%\* com.google.gwt.dev.Compiler -war target\classes\static -draftCompile -strict -incremental -setProperty user.agent="gecko1_8" -style PRETTY %entryPoint%