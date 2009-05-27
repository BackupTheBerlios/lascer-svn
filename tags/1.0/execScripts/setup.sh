# Dateiname      : setup.sh
# Letzte Änderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Diese Datei dient zum Setzen der Umgebungsvariablen von Lascer in
# den anderen Scripten. Alle relativen Pfade beziehen sich auf das
# Oberverzeichnis von execScripts.


# Der Pfad zum JDK. Kann als äußere Umgebungsvariable gesetzt werden.
# Kann hier aber auch explizit gesetzt werden.
# JAVA_HOME=/usr/java/IBMJava2-142/

# Die Pfade zu den Java-Programmen setzen.
if [ "$JAVA_HOME" == "" ]; then
  JAVA="java"
  JAVAC="javac"
else
  JAVA="$JAVA_HOME/bin/java"
  JAVAC="$JAVA_HOME/bin/javac"
fi

# Das build-Verzeichnis für die compilierten Dateien.
BUILD_DIR="build"

# Die jar-Dateien für den classpath.
CLASSPATH="libs/mathCollection.jar"
CLASSPATH="$CLASSPATH:libs/commandline.jar"
CLASSPATH="$CLASSPATH:libs/weka.jar"
CLASSPATH="$CLASSPATH:libs/coreExtended.jar"
CLASSPATH="$CLASSPATH:libs/geomClusterung.jar"
CLASSPATH="$CLASSPATH:libs/mengenUeberdeckung.jar"
CLASSPATH="$CLASSPATH:libs/archiUser.jar"
CLASSPATH="$CLASSPATH:source/"
CLASSPATH="$CLASSPATH:$BUILD_DIR/"

# Das Verzeichnis mit den properties- und config-Dateien.
CONFIG_DIR="config"

