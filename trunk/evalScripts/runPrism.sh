#!/bin/sh

# Dateiname      : runPrism.sh
# Letzte �nderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

if [ "$JAVA_HOME" == "" ]; then
  JAVA=java
else
  JAVA=$JAVA_HOME/bin/java
fi

$JAVA $JVM_OPTS -classpath libs/weka.jar weka.classifiers.rules.Prism -t $@

