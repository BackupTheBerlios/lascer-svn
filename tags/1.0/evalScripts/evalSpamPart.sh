#!/bin/sh

# Dateiname      : evalSpamPart.sh
# Letzte �nderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Evaluation von PART.

echo Verfahren: PART
date
echo

echo "------------------------------------------------------------------------"

echo Name: Spam/spam-1-oi.arff
./evalScripts/runPart.sh arffDaten/Spam/spam-1-oi.arff -o
echo "------------------------------------------------------------------------"

echo Name: Spam/spam-1-mi.arff
./evalScripts/runPart.sh arffDaten/Spam/spam-1-mi.arff -o
echo "------------------------------------------------------------------------"

echo
date
echo

