#!/bin/sh

# Dateiname      : evalLascer-NomNom.sh
# Letzte Änderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Evaluation von Lascer mit Vergleich nominaler Attribute.

echo Verfahren: Lascer-NomNom
date
echo

echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-0.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-0.arff -o -nomNomPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-1.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-1.arff -o -nomNomPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-2.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-2.arff -o -nomNomPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-3.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-3.arff -o -nomNomPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-4.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-4.arff -o -nomNomPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-5.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-5.arff -o -nomNomPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-6.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-6.arff -o -nomNomPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-7.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-7.arff -o -nomNomPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-8.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-8.arff -o -nomNomPraedErz ja
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-9.arff
./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-9.arff -o -nomNomPraedErz ja
echo "------------------------------------------------------------------------"

echo
date
echo

