start cmd /k javac sink_node1.java
start cmd /k javac node1.java
start cmd /k java sink_node1 A
set "list=B C D E F G H I J K L M N O P Q R S T"

(for %%a in (%list%) do (
   start cmd /k java node1 %%a
))