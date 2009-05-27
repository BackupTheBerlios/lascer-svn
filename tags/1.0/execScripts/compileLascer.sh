#!/bin/sh

# Dateiname      : compileLascer.sh
# Letzte Änderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Compiliert Lascer.
#
# Aufruf:
# ./execScripts/compileLascer.sh

# Alle relativen Pfade beziehen sich auf das Oberverzeichnis von execScripts.


# Benötigte Umgebungsvariablen (BUILD, JAVAC, CLASSPATH, CONFIG_DIR)
# werden gesetzt.
. execScripts/setup.sh

# Verzeichnis für die compilierten Dateien anlegen, falls es noch nicht
# existiert.
if [ ! -e $BUILD_DIR ] ; then
  mkdir $BUILD_DIR
fi

$JAVAC -classpath $CLASSPATH -d $BUILD_DIR source/lascer/*/*/*/*.java
$JAVAC -classpath $CLASSPATH -d $BUILD_DIR source/lascer/*/*/*.java
$JAVAC -classpath $CLASSPATH -d $BUILD_DIR source/lascer/*/*.java
$JAVAC -classpath $CLASSPATH -d $BUILD_DIR source/lascer/*.java

