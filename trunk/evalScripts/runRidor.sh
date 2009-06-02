#!/bin/sh

# Dateiname      : runRidor.sh
# Letzte Änderung: 02. Juni 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

if [ "$JAVA_HOME" == "" ]; then
  JAVA=java
else
  JAVA=$JAVA_HOME/bin/java
fi

$JAVA $JVM_OPTS -classpath libs/weka.jar weka.classifiers.rules.Ridor -t $@

