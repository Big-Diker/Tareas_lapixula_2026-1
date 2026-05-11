JC = javac
JVM = java
JFLAGS = -g -Xlint -deprecation
MAIN = Main

SOURCES = Main.java \
          componentes/*.java \
          mapa/*.java \
          entidades/*.java

all:
	$(JC) $(JFLAGS) $(SOURCES)
	@echo yea
run:
	$(JVM) $(MAIN)

clean:
	rm -f *.class
	rm -f componentes/*.class
	rm -f mapa/*.class
	rm -f entidades/*.class
	@echo "Archivos .class eliminados."