#!/bin/sh

# Dateiname      : evalSpamRidor.sh
# Letzte �nderung: 02. Juni 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Evaluation von Ridor.

echo Verfahren: Ridor
date
echo

echo "------------------------------------------------------------------------"

echo Name: Spam/spam-1-oi.arff
./evalScripts/runRidor.sh arffDaten/Spam/spam-1-oi.arff -o
echo "------------------------------------------------------------------------"

echo Name: Spam/spam-1-mi.arff
./evalScripts/runRidor.sh arffDaten/Spam/spam-1-mi.arff -o
echo "------------------------------------------------------------------------"

echo
date
echo

