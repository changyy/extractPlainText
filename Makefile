# == MAKEFILE INIT ==
EMPTY=
SPACE=$(EMPTY) $(EMPTY)
COMMA=,
LIB_SEPARATOR=:

# == LIBS ==
LIB_POI_DIR=./libs/
LIB_POI=poi-3.8-20120326.jar poi-ooxml-3.8-20120326.jar poi-excelant-3.8-20120326.jar poi-ooxml-schemas-3.8-20120326.jar poi-scratchpad-3.8-20120326.jar
LIB_POI_INCLUDE=$(subst $(SPACE),$(LIB_SEPARATOR)$(LIB_POI_DIR),$(SPACE)$(LIB_POI))
LIB_OTHERS_DIR=./libs/
LIB_OTHERS=commons-logging-1.1.jar dom4j-1.6.1.jar junit-3.8.1.jar log4j-1.2.13.jar stax-api-1.0.1.jar xmlbeans-2.3.0.jar
LIB_OTHERS_INCLUDE=$(subst $(SPACE),$(LIB_SEPARATOR)$(LIB_OTHERS_DIR),$(SPACE)$(LIB_OTHERS))
LIB_INCLUDE=.$(LIB_POI_INCLUDE)$(LIB_OTHERS_INCLUDE) # .:./libs/x.jar:./libs/y.jar
LIB_INCLUDE_WITH_PATH=$(subst $(SPACE),$(SPACE)$(LIB_POI_DIR),$(SPACE)$(LIB_POI))$(SPACE)$(subst $(SPACE),$(SPACE)$(LIB_OTHERS_DIR),$(SPACE)$(LIB_OTHERS))

# == 
OUTPUT=./archive
JAR=extractPlainText.jar
#LIBS=.:libs/commons-logging-1.1.jar:libs/poi-3.8-20120326.jar:libs/poi-ooxml-schemas-3.8-20120326.jar:libs/dom4j-1.6.1.jar:libs/poi-examples-3.8-20120326.jar:libs/poi-scratchpad-3.8-20120326.jar:libs/junit-3.8.1.jar:libs/poi-excelant-3.8-20120326.jar:libs/stax-api-1.0.1.jar:libs/log4j-1.2.13.jar:libs/poi-ooxml-3.8-20120326.jar:libs/xmlbeans-2.3.0.jar

#$(OUTPUT)/%.class: %.java
#	javac -Xlint:deprecation -cp $(LIBS) -d $(OUTPUT) $<

untar:
	mkdir -p $(OUTPUT);
	@for sublib in $(LIB_INCLUDE_WITH_PATH) ; do \
		unzip -o $$sublib -d $(OUTPUT) ;\
	done
	rm -rf $(OUTPUT)/*.txt $(OUTPUT)/META-INF
	mkdir $(OUTPUT)/META-INF

jar: untar ExtractPlainText.class
	rm -f ./MANIFEST.MF out.jar
	echo "Manifest-Version: 1.0\nMain-Class: ExtractPlainText" > $(OUTPUT)/META-INF/MANIFEST.MF
	cd $(OUTPUT) && jar cvmf META-INF/MANIFEST.MF ../$(JAR) *

ExtractPlainText.class:
	mkdir -p $(OUTPUT);
	javac -Xlint:deprecation -cp $(LIB_INCLUDE) ExtractPlainText.java -d $(OUTPUT)

run: clean ExtractPlainText.class
	java -cp $(LIBS) ExtractPlainText $(ARG)
clean:
	rm -rf ./ExtractPlainText.class $(OUTPUT) $(JAR)
