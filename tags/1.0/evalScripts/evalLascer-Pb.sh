#!/bin/sh

# Dateiname      : evalLascer-Pb.sh
# Letzte Änderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Evaluation von Lascer mit Pruning mit bekannten Fehler-Anteilen.

echo Verfahren: Lascer-Pb
date
echo

echo "------------------------------------------------------------------------"

echo Name: Monks/monks-problems-3
./evalScripts/runLascer.sh arffDaten/Monks/monks-problems-3_train-mod.arff -T arffDaten/Monks/monks-problems-3_test-mod.arff -o -posPruneAnt 1.7 -negPruneAnt 8.1
echo "------------------------------------------------------------------------"

echo
date
echo

