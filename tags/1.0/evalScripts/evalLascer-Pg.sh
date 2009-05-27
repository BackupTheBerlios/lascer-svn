#!/bin/sh

# Dateiname      : evalLascer-Pg.sh
# Letzte Änderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Evaluation von Lascer mit Pruning mit geschätzten Fehler-Anteilen.

echo Verfahren: Lascer-Pg
date
echo

echo "------------------------------------------------------------------------"

echo Name: HeartDisease/heart-statlog-present.arff
./evalScripts/runLascer.sh arffDaten/HeartDisease/heart-statlog-present.arff -o -posPruneAnt 11.3 -negPruneAnt 12.0
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-aver.arff
./evalScripts/runLascer.sh arffDaten/Agricultural/eucalyptus-aver.arff -o -posPruneAnt 7.1 -negPruneAnt 10.7
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-best.arff
./evalScripts/runLascer.sh arffDaten/Agricultural/eucalyptus-best.arff -o -posPruneAnt 5.6 -negPruneAnt 7.1
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-good.arff
./evalScripts/runLascer.sh arffDaten/Agricultural/eucalyptus-good.arff -o -posPruneAnt 10.2 -negPruneAnt 14.1
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-low.arff
./evalScripts/runLascer.sh arffDaten/Agricultural/eucalyptus-low.arff -o -posPruneAnt 4.6 -negPruneAnt 8.3
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-none.arff
./evalScripts/runLascer.sh arffDaten/Agricultural/eucalyptus-none.arff -o -posPruneAnt 4.4 -negPruneAnt 2.8
echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-13.arff
#./evalScripts/runLascer.sh arffDaten/Soybean/soybean-13.arff -o -posPruneAnt 0.1 -negPruneAnt 0.1
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-14.arff
#./evalScripts/runLascer.sh arffDaten/Soybean/soybean-14.arff -o -posPruneAnt 1.9 -negPruneAnt 2.4
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-15.arff
#./evalScripts/runLascer.sh arffDaten/Soybean/soybean-15.arff -o -posPruneAnt 2.8 -negPruneAnt 3.5
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-13-red.arff
#./evalScripts/runLascer.sh arffDaten/Soybean/soybean-13-red.arff -o -posPruneAnt 0.3 -negPruneAnt 0.5
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-14-red.arff
#./evalScripts/runLascer.sh arffDaten/Soybean/soybean-14-red.arff -o -posPruneAnt 2.5 -negPruneAnt 3.2
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-15-red.arff
#./evalScripts/runLascer.sh arffDaten/Soybean/soybean-15-red.arff -o -posPruneAnt 3.0 -negPruneAnt 3.7
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-EI.arff
#./evalScripts/runLascer.sh arffDaten/Splice/splice-EI.arff -o -posPruneAnt 2.1 -negPruneAnt 1.8
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-IE.arff
#./evalScripts/runLascer.sh arffDaten/Splice/splice-IE.arff -o -posPruneAnt 2.4 -negPruneAnt 3.3
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-N.arff
#./evalScripts/runLascer.sh arffDaten/Splice/splice-N.arff -o -posPruneAnt 1.8 -negPruneAnt 3.5
#echo "------------------------------------------------------------------------"

#echo Name: Letter/letter-A.arff
#./evalScripts/runLascer.sh arffDaten/Letter/letter-A.arff -o -posPruneAnt 0.1 -negPruneAnt 0.1
#echo "------------------------------------------------------------------------"

#echo Name: Letter/letter-B.arff
#./evalScripts/runLascer.sh arffDaten/Letter/letter-B.arff -o -posPruneAnt 0.4 -negPruneAnt 0.5
#echo "------------------------------------------------------------------------"

#echo Name: Letter/letter-C.arff
#./evalScripts/runLascer.sh arffDaten/Letter/letter-C.arff -o -posPruneAnt 0.2 -negPruneAnt 0.3
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Factors/mfeat-factors-0.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-0.arff -o -posPruneAnt 0.3 -negPruneAnt 0.4
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Factors/mfeat-factors-1.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-1.arff -o -posPruneAnt 0.9 -negPruneAnt 1.0
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Factors/mfeat-factors-2.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-2.arff -o -posPruneAnt 0.6 -negPruneAnt 0.8
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Factors/mfeat-factors-3.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-3.arff -o -posPruneAnt 1.1 -negPruneAnt 1.5
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Factors/mfeat-factors-4.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-4.arff -o -posPruneAnt 0.4 -negPruneAnt 0.6
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Factors/mfeat-factors-5.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-5.arff -o -posPruneAnt 1.5 -negPruneAnt 1.7
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Factors/mfeat-factors-6.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-6.arff -o -posPruneAnt 0.6 -negPruneAnt 0.8
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Factors/mfeat-factors-7.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-7.arff -o -posPruneAnt 0.7 -negPruneAnt 0.7
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Factors/mfeat-factors-8.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-8.arff -o -posPruneAnt 0.7 -negPruneAnt 0.9
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Factors/mfeat-factors-9.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Factors/mfeat-factors-9.arff -o -posPruneAnt 0.8 -negPruneAnt 1.3
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Pixel/mfeat-pixel-0.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-0.arff -o -posPruneAnt 0.6 -negPruneAnt 0.6
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Pixel/mfeat-pixel-1.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-1.arff -o -posPruneAnt 1.1 -negPruneAnt 0.7
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Pixel/mfeat-pixel-2.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-2.arff -o -posPruneAnt 0.2 -negPruneAnt 0.4
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Pixel/mfeat-pixel-3.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-3.arff -o -posPruneAnt 1.1 -negPruneAnt 1.2
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Pixel/mfeat-pixel-4.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-4.arff -o -posPruneAnt 1.0 -negPruneAnt 0.6
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Pixel/mfeat-pixel-5.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-5.arff -o -posPruneAnt 1.1 -negPruneAnt 0.8
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Pixel/mfeat-pixel-6.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-6.arff -o -posPruneAnt 1.0 -negPruneAnt 0.8
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Pixel/mfeat-pixel-7.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-7.arff -o -posPruneAnt 1.0 -negPruneAnt 0.5
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Pixel/mfeat-pixel-8.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-8.arff -o -posPruneAnt 1.6 -negPruneAnt 1.4
#echo "------------------------------------------------------------------------"

#echo Name: Mfeat-Pixel/mfeat-pixel-9.arff
#./evalScripts/runLascer.sh arffDaten/Mfeat-Pixel/mfeat-pixel-9.arff -o -posPruneAnt 1.5 -negPruneAnt 1.1
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-0.arff
#./evalScripts/runLascer.sh arffDaten/OptDigits/optdigits-0.arff -o -posPruneAnt 0.1 -negPruneAnt 0.2
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-1.arff
#./evalScripts/runLascer.sh arffDaten/OptDigits/optdigits-1.arff -o -posPruneAnt 0.6 -negPruneAnt 0.8
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-2.arff
#./evalScripts/runLascer.sh arffDaten/OptDigits/optdigits-2.arff -o -posPruneAnt 0.1 -negPruneAnt 0.5
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-3.arff
#./evalScripts/runLascer.sh arffDaten/OptDigits/optdigits-3.arff -o -posPruneAnt 0.4 -negPruneAnt 1.0
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-4.arff
#./evalScripts/runLascer.sh arffDaten/OptDigits/optdigits-4.arff -o -posPruneAnt 0.4 -negPruneAnt 0.8
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-5.arff
#./evalScripts/runLascer.sh arffDaten/OptDigits/optdigits-5.arff -o -posPruneAnt 0.4 -negPruneAnt 0.6
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-6.arff
#./evalScripts/runLascer.sh arffDaten/OptDigits/optdigits-6.arff -o -posPruneAnt 0.3 -negPruneAnt 0.3
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-7.arff
#./evalScripts/runLascer.sh arffDaten/OptDigits/optdigits-7.arff -o -posPruneAnt 0.3 -negPruneAnt 0.5
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-8.arff
#./evalScripts/runLascer.sh arffDaten/OptDigits/optdigits-8.arff -o -posPruneAnt 0.6 -negPruneAnt 1.3
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-9.arff
#./evalScripts/runLascer.sh arffDaten/OptDigits/optdigits-9.arff -o -posPruneAnt 0.7 -negPruneAnt 1.3
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-not_recom.arff
#./evalScripts/runLascer.sh arffDaten/Nursery/nursery-not_recom.arff -o -posPruneAnt 0.0 -negPruneAnt 0.0
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-recommend.arff
#./evalScripts/runLascer.sh arffDaten/Nursery/nursery-recommend.arff -o -posPruneAnt 0.0 -negPruneAnt 0.0
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-very_recom.arff
#./evalScripts/runLascer.sh arffDaten/Nursery/nursery-very_recom.arff -o -posPruneAnt 0.0 -negPruneAnt 0.0
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-priority.arff
#./evalScripts/runLascer.sh arffDaten/Nursery/nursery-priority.arff -o -posPruneAnt 0.0 -negPruneAnt 0.0
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-spec_prior.arff
#./evalScripts/runLascer.sh arffDaten/Nursery/nursery-spec_prior.arff -o -posPruneAnt 0.0 -negPruneAnt 0.0
#echo "------------------------------------------------------------------------"

#echo Name: Monks/monks-problems-1
#./evalScripts/runLascer.sh arffDaten/Monks/monks-problems-1_train-mod.arff -T arffDaten/Monks/monks-problems-1_test-mod.arff -o -posPruneAnt 0.0 -negPruneAnt 0.0
#echo "------------------------------------------------------------------------"

echo Name: Monks/monks-problems-3
./evalScripts/runLascer.sh arffDaten/Monks/monks-problems-3_train-mod.arff -T arffDaten/Monks/monks-problems-3_test-mod.arff -o -posPruneAnt 2.6 -negPruneAnt 6.8
echo "------------------------------------------------------------------------"

echo
date
echo

