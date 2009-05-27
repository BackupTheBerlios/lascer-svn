#!/bin/sh

# Dateiname      : runWekaClassi.sh
# Letzte �nderung: 26. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# F�hrt Lascer als Classifier von Weka aus. Der Name der Datensatz-Datei ist
# auf der Kommandozeile als Wert der Option "-t" anzugeben. Es k�nnen auch
# die anderen Parameter von Lascer angegeben werden.
#
# Aufruf z.B.:
# ./execScripts/runWekaClassi.sh -nomNomPraedErz ja -t datensatz.arff

# Alle relativen Pfade beziehen sich auf das Oberverzeichnis von execScripts.


# Ben�tigte Umgebungsvariablen (JAVA, CLASSPATH, CONFIG_DIR) werden
# gesetzt.
. execScripts/setup.sh


# Die Parameter f�r die JVM setzen. Die Werte der Umgebungsvariablen
# $JVM_OPTS werden �bernommen.
JVMPAR="$JVM_OPTS"
JVMPAR="$JVMPAR -Xmx1900m"
JVMPAR="$JVMPAR -Djava.security.policy=$CONFIG_DIR/transmitter.pol"

# Das Logging wird standardm��ig mittels der Kommandozeilen-Option -logging
# eingeschaltet. Soll es aber nur f�r bestimmte Klassen aktiviert werden,
# l��t man die Option weg, kommentiert die nachfolgende Zeile ein und pa�t
# die Datei lascerLoggingProps entsprechend an.
# JVMPAR="$JVMPAR -Djava.util.logging.config.file=$CONFIG_DIR/lascerLoggingProps"

# Die Main-Klasse setzen.
MAIN="lascer.WekaClassifier"

$JAVA -classpath $CLASSPATH $JVMPAR $MAIN $*

