#!/bin/sh

# Dateiname      : evalSpamJRip.sh
# Letzte �nderung: 02. Juni 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Evaluation von JRip.

echo Verfahren: JRip
date
echo

echo "------------------------------------------------------------------------"

echo Name: Spam/spam-1-oi.arff
./evalScripts/runJRip.sh arffDaten/Spam/spam-1-oi.arff -o
echo "------------------------------------------------------------------------"

echo Name: Spam/spam-1-mi.arff
./evalScripts/runJRip.sh arffDaten/Spam/spam-1-mi.arff -o
echo "------------------------------------------------------------------------"

echo
date
echo

