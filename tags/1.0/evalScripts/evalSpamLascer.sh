#!/bin/sh

# Dateiname      : evalSpamLascer.sh
# Letzte �nderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Evaluation von Lascer ohne spezielle Parameter.

echo Verfahren: Lascer
date
echo

echo "------------------------------------------------------------------------"

echo Name: Spam/spam-1-oi.arff
./evalScripts/runLascer.sh arffDaten/Spam/spam-1-oi.arff -o
echo "------------------------------------------------------------------------"

echo Name: Spam/spam-1-mi.arff
./evalScripts/runLascer.sh arffDaten/Spam/spam-1-mi.arff -o
echo "------------------------------------------------------------------------"

echo
date
echo

