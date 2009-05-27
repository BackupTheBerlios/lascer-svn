#!/bin/sh

# Dateiname      : evalLascer-RealReal.sh
# Letzte Änderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Evaluation von Lascer mit Vergleich nicht-ganzzahliger Attribute.

echo Verfahren: Lascer-RealReal
date
echo

echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-0.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-0.arff -o -realRealPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-1.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-1.arff -o -realRealPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-2.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-2.arff -o -realRealPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-3.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-3.arff -o -realRealPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-4.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-4.arff -o -realRealPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-5.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-5.arff -o -realRealPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-6.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-6.arff -o -realRealPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-7.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-7.arff -o -realRealPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-8.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-8.arff -o -realRealPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-9.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-9.arff -o -realRealPraedErz ja
echo "------------------------------------------------------------------------"

echo
date
echo

