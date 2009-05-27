#!/bin/sh

# Dateiname      : evalSpamPrism.sh
# Letzte �nderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Evaluation von Prism.

echo Verfahren: Prism
date
echo

# Maximale Gr��e des Stack erh�hen.
JVM_OPTS="-Xss1m"
export JVM_OPTS

echo "------------------------------------------------------------------------"

echo Name: Spam/spam-1-oi.arff
./evalScripts/runPrism.sh arffDaten/Spam/spam-1-oi.arff -o
echo "------------------------------------------------------------------------"

echo Name: Spam/spam-1-mi.arff
./evalScripts/runPrism.sh arffDaten/Spam/spam-1-mi.arff -o
echo "------------------------------------------------------------------------"

echo
date
echo

