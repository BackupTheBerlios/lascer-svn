#!/bin/sh

# Dateiname      : packageLascer.sh
# Letzte �nderung: 27. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Erzeugt aus den class-Dateien von Lascer eine jar-Datei.
#
# Aufruf:
# ./execScripts/compileLascer.sh

# Alle relativen Pfade beziehen sich auf das Oberverzeichnis von execScripts.


# Ben�tigte Umgebungsvariablen (BUILD_DIR, DEPLOY_DIR) werden gesetzt.
. execScripts/setup.sh

# Pr�fen ob das Verzeichnis mit den compilierten Dateien existiert.
if [ ! -e $BUILD_DIR ] ; then
  echo "Bitte erst das Script execScripts/compileLascer.sh ausf�hren."
  exit -1
fi

# Verzeichnis f�r die jar-Datei anlegen, falls es noch nicht existiert.
if [ ! -e $DEPLOY_DIR ] ; then
  mkdir $DEPLOY_DIR
fi

# jar-Datei erzeugen.
jar -cf $DEPLOY_DIR/lascer.jar -C $BUILD_DIR lascer

