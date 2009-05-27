#!/bin/sh

# Dateiname      : evalPrism.sh
# Letzte Änderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Evaluation von PRISM.

echo Verfahren: PRISM
date
echo

echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-13-red.arff
#./evalScripts/runPrism.sh arffDaten/Soybean/soybean-13-red.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-14-red.arff
#./evalScripts/runPrism.sh arffDaten/Soybean/soybean-14-red.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-15-red.arff
#./evalScripts/runPrism.sh arffDaten/Soybean/soybean-15-red.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-EI.arff
#./evalScripts/runPrism.sh arffDaten/Splice/splice-EI.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-IE.arff
#./evalScripts/runPrism.sh arffDaten/Splice/splice-IE.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-N.arff
#./evalScripts/runPrism.sh arffDaten/Splice/splice-N.arff -o
#echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-0.arff
./evalScripts/runPrism.sh arffDaten/Mfeat-Pixel/mfeat-pixel-0.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-1.arff
./evalScripts/runPrism.sh arffDaten/Mfeat-Pixel/mfeat-pixel-1.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-2.arff
./evalScripts/runPrism.sh arffDaten/Mfeat-Pixel/mfeat-pixel-2.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-3.arff
./evalScripts/runPrism.sh arffDaten/Mfeat-Pixel/mfeat-pixel-3.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-4.arff
./evalScripts/runPrism.sh arffDaten/Mfeat-Pixel/mfeat-pixel-4.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-5.arff
./evalScripts/runPrism.sh arffDaten/Mfeat-Pixel/mfeat-pixel-5.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-6.arff
./evalScripts/runPrism.sh arffDaten/Mfeat-Pixel/mfeat-pixel-6.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-7.arff
./evalScripts/runPrism.sh arffDaten/Mfeat-Pixel/mfeat-pixel-7.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-8.arff
./evalScripts/runPrism.sh arffDaten/Mfeat-Pixel/mfeat-pixel-8.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-9.arff
./evalScripts/runPrism.sh arffDaten/Mfeat-Pixel/mfeat-pixel-9.arff -o
echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-not_recom.arff
#./evalScripts/runPrism.sh arffDaten/Nursery/nursery-not_recom.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-recommend.arff
#./evalScripts/runPrism.sh arffDaten/Nursery/nursery-recommend.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-very_recom.arff
#./evalScripts/runPrism.sh arffDaten/Nursery/nursery-very_recom.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-priority.arff
#./evalScripts/runPrism.sh arffDaten/Nursery/nursery-priority.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-spec_prior.arff
#./evalScripts/runPrism.sh arffDaten/Nursery/nursery-spec_prior.arff -o
#echo "------------------------------------------------------------------------"

echo Name: Monks/monks-problems-1
./evalScripts/runPrism.sh arffDaten/Monks/monks-problems-1_train-mod.arff -T arffDaten/Monks/monks-problems-1_test-mod.arff -o
echo "------------------------------------------------------------------------"

echo Name: Monks/monks-problems-3
./evalScripts/runPrism.sh arffDaten/Monks/monks-problems-3_train-mod.arff -T arffDaten/Monks/monks-problems-3_test-mod.arff -o
echo "------------------------------------------------------------------------"

echo
date
echo

