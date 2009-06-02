#!/bin/sh

# Dateiname      : evalRidor.sh
# Letzte Änderung: 02. Juni 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Evaluation von Ridor.

echo Verfahren: Ridor
date
echo

echo "------------------------------------------------------------------------"

echo Name: HeartDisease/heart-statlog-present.arff
./evalScripts/runRidor.sh arffDaten/HeartDisease/heart-statlog-present.arff -o
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-aver.arff
./evalScripts/runRidor.sh arffDaten/Agricultural/eucalyptus-aver.arff -o
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-best.arff
./evalScripts/runRidor.sh arffDaten/Agricultural/eucalyptus-best.arff -o
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-good.arff
./evalScripts/runRidor.sh arffDaten/Agricultural/eucalyptus-good.arff -o
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-low.arff
./evalScripts/runRidor.sh arffDaten/Agricultural/eucalyptus-low.arff -o
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-none.arff
./evalScripts/runRidor.sh arffDaten/Agricultural/eucalyptus-none.arff -o
echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-13.arff
#./evalScripts/runRidor.sh arffDaten/Soybean/soybean-13.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-14.arff
#./evalScripts/runRidor.sh arffDaten/Soybean/soybean-14.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-15.arff
#./evalScripts/runRidor.sh arffDaten/Soybean/soybean-15.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-13-red.arff
#./evalScripts/runRidor.sh arffDaten/Soybean/soybean-13-red.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-14-red.arff
#./evalScripts/runRidor.sh arffDaten/Soybean/soybean-14-red.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-15-red.arff
#./evalScripts/runRidor.sh arffDaten/Soybean/soybean-15-red.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-EI.arff
#./evalScripts/runRidor.sh arffDaten/Splice/splice-EI.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-IE.arff
#./evalScripts/runRidor.sh arffDaten/Splice/splice-IE.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-N.arff
#./evalScripts/runRidor.sh arffDaten/Splice/splice-N.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Letter/letter-A.arff
#./evalScripts/runRidor.sh arffDaten/Letter/letter-A.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Letter/letter-B.arff
#./evalScripts/runRidor.sh arffDaten/Letter/letter-B.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Letter/letter-C.arff
#./evalScripts/runRidor.sh arffDaten/Letter/letter-C.arff -o
#echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-0.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Factors/mfeat-factors-0.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-1.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Factors/mfeat-factors-1.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-2.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Factors/mfeat-factors-2.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-3.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Factors/mfeat-factors-3.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-4.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Factors/mfeat-factors-4.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-5.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Factors/mfeat-factors-5.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-6.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Factors/mfeat-factors-6.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-7.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Factors/mfeat-factors-7.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-8.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Factors/mfeat-factors-8.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-9.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Factors/mfeat-factors-9.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-0.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Pixel/mfeat-pixel-0.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-1.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Pixel/mfeat-pixel-1.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-2.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Pixel/mfeat-pixel-2.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-3.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Pixel/mfeat-pixel-3.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-4.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Pixel/mfeat-pixel-4.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-5.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Pixel/mfeat-pixel-5.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-6.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Pixel/mfeat-pixel-6.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-7.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Pixel/mfeat-pixel-7.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-8.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Pixel/mfeat-pixel-8.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-9.arff
./evalScripts/runRidor.sh arffDaten/Mfeat-Pixel/mfeat-pixel-9.arff -o
echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-0.arff
#./evalScripts/runRidor.sh arffDaten/OptDigits/optdigits-0.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-1.arff
#./evalScripts/runRidor.sh arffDaten/OptDigits/optdigits-1.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-2.arff
#./evalScripts/runRidor.sh arffDaten/OptDigits/optdigits-2.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-3.arff
#./evalScripts/runRidor.sh arffDaten/OptDigits/optdigits-3.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-4.arff
#./evalScripts/runRidor.sh arffDaten/OptDigits/optdigits-4.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-5.arff
#./evalScripts/runRidor.sh arffDaten/OptDigits/optdigits-5.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-6.arff
#./evalScripts/runRidor.sh arffDaten/OptDigits/optdigits-6.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-7.arff
#./evalScripts/runRidor.sh arffDaten/OptDigits/optdigits-7.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-8.arff
#./evalScripts/runRidor.sh arffDaten/OptDigits/optdigits-8.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-9.arff
#./evalScripts/runRidor.sh arffDaten/OptDigits/optdigits-9.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-not_recom.arff
#./evalScripts/runRidor.sh arffDaten/Nursery/nursery-not_recom.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-recommend.arff
#./evalScripts/runRidor.sh arffDaten/Nursery/nursery-recommend.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-very_recom.arff
#./evalScripts/runRidor.sh arffDaten/Nursery/nursery-very_recom.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-priority.arff
#./evalScripts/runRidor.sh arffDaten/Nursery/nursery-priority.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-spec_prior.arff
#./evalScripts/runRidor.sh arffDaten/Nursery/nursery-spec_prior.arff -o
#echo "------------------------------------------------------------------------"

echo Name: Monks/monks-problems-1
./evalScripts/runRidor.sh arffDaten/Monks/monks-problems-1_train-mod.arff -T arffDaten/Monks/monks-problems-1_test-mod.arff -o
echo "------------------------------------------------------------------------"

echo Name: Monks/monks-problems-3
./evalScripts/runRidor.sh arffDaten/Monks/monks-problems-3_train-mod.arff -T arffDaten/Monks/monks-problems-3_test-mod.arff -o
echo "------------------------------------------------------------------------"

echo
date
echo

