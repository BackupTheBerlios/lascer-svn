#!/bin/sh

# Dateiname      : packageLascer.sh
# Letzte Änderung: 27. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Erzeugt aus den class-Dateien von Lascer eine jar-Datei.
#
# Aufruf:
# ./execScripts/compileLascer.sh

# Alle relativen Pfade beziehen sich auf das Oberverzeichnis von execScripts.


# Benötigte Umgebungsvariablen (BUILD_DIR, DEPLOY_DIR) werden gesetzt.
. execScripts/setup.sh

# Prüfen ob das Verzeichnis mit den compilierten Dateien existiert.
if [ ! -e $BUILD_DIR ] ; then
  echo "Bitte erst das Script execScripts/compileLascer.sh ausführen."
  exit -1
fi

# Verzeichnis für die jar-Datei anlegen, falls es noch nicht existiert.
if [ ! -e $DEPLOY_DIR ] ; then
  mkdir $DEPLOY_DIR
fi

# jar-Datei erzeugen.
jar -cf $DEPLOY_DIR/lascer.jar -C $BUILD_DIR lascer

