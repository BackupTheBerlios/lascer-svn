#!/bin/sh

# Dateiname      : profileFormelErz.sh
# Letzte Änderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Erzeut Formeln zu einem Datensatz und erstellt eine Laufzeit-Datei. Der
# Name der Datensatz-Datei ist auf der Kommandozeile zusammen mit möglichen
# weiteren Parametern anzugeben.
#
# Aufruf z.B.:
# ./execScripts/profileFormelErz.sh -nomNomPraedErz ja datensatz.arff

# Alle relativen Pfade beziehen sich auf das Oberverzeichnis von execScripts.


# Benötigte Umgebungsvariablen (JAVA, CLASSPATH, CONFIG_DIR) werden
# gesetzt.
. execScripts/setup.sh


# Die Parameter für die JVM setzen.
JVMPAR="-server -Xmx1900m -Xrunhprof:cpu=samples,depth=20"
JVMPAR="$JVMPAR -Djava.security.policy=$CONFIG_DIR/transmitter.pol"

# Das Logging wird standardmäßig mittels der Kommandozeilen-Option -logging
# eingeschaltet. Soll es aber nur für bestimmte Klassen aktiviert werden,
# läßt man die Option weg, kommentiert die nachfolgende Zeile ein und paßt
# die Datei lascerLoggingProps entsprechend an.
# JVMPAR="$JVMPAR -Djava.util.logging.config.file=$CONFIG_DIR/lascerLoggingProps"

# Die Main-Klasse setzen.
MAIN="lascer.FormelErzeugung"

$JAVA -classpath $CLASSPATH $JVMPAR $MAIN $*

