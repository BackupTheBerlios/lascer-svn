#!/bin/sh

# Dateiname      : evalJ48.sh
# Letzte �nderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Evaluation von J48.

echo Verfahren: J48
date
echo

echo "------------------------------------------------------------------------"

echo Name: HeartDisease/heart-statlog-present.arff
./evalScripts/runJ48.sh arffDaten/HeartDisease/heart-statlog-present.arff -o
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-aver.arff
./evalScripts/runJ48.sh arffDaten/Agricultural/eucalyptus-aver.arff -o
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-best.arff
./evalScripts/runJ48.sh arffDaten/Agricultural/eucalyptus-best.arff -o
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-good.arff
./evalScripts/runJ48.sh arffDaten/Agricultural/eucalyptus-good.arff -o
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-low.arff
./evalScripts/runJ48.sh arffDaten/Agricultural/eucalyptus-low.arff -o
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-none.arff
./evalScripts/runJ48.sh arffDaten/Agricultural/eucalyptus-none.arff -o
echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-13.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-13.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-14.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-14.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-15.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-15.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-13-red.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-13-red.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-14-red.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-14-red.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-15-red.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-15-red.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-EI.arff
#./evalScripts/runJ48.sh arffDaten/Splice/splice-EI.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-IE.arff
#./evalScripts/runJ48.sh arffDaten/Splice/splice-IE.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Splice/splice-N.arff
#./evalScripts/runJ48.sh arffDaten/Splice/splice-N.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Letter/letter-A.arff
#./evalScripts/runJ48.sh arffDaten/Letter/letter-A.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Letter/letter-B.arff
#./evalScripts/runJ48.sh arffDaten/Letter/letter-B.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Letter/letter-C.arff
#./evalScripts/runJ48.sh arffDaten/Letter/letter-C.arff -o
#echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-0.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Factors/mfeat-factors-0.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-1.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Factors/mfeat-factors-1.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-2.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Factors/mfeat-factors-2.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-3.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Factors/mfeat-factors-3.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-4.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Factors/mfeat-factors-4.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-5.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Factors/mfeat-factors-5.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-6.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Factors/mfeat-factors-6.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-7.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Factors/mfeat-factors-7.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-8.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Factors/mfeat-factors-8.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Factors/mfeat-factors-9.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Factors/mfeat-factors-9.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-0.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Pixel/mfeat-pixel-0.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-1.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Pixel/mfeat-pixel-1.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-2.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Pixel/mfeat-pixel-2.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-3.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Pixel/mfeat-pixel-3.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-4.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Pixel/mfeat-pixel-4.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-5.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Pixel/mfeat-pixel-5.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-6.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Pixel/mfeat-pixel-6.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-7.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Pixel/mfeat-pixel-7.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-8.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Pixel/mfeat-pixel-8.arff -o
echo "------------------------------------------------------------------------"

echo Name: Mfeat-Pixel/mfeat-pixel-9.arff
./evalScripts/runJ48.sh arffDaten/Mfeat-Pixel/mfeat-pixel-9.arff -o
echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-0.arff
#./evalScripts/runJ48.sh arffDaten/OptDigits/optdigits-0.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-1.arff
#./evalScripts/runJ48.sh arffDaten/OptDigits/optdigits-1.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-2.arff
#./evalScripts/runJ48.sh arffDaten/OptDigits/optdigits-2.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-3.arff
#./evalScripts/runJ48.sh arffDaten/OptDigits/optdigits-3.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-4.arff
#./evalScripts/runJ48.sh arffDaten/OptDigits/optdigits-4.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-5.arff
#./evalScripts/runJ48.sh arffDaten/OptDigits/optdigits-5.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-6.arff
#./evalScripts/runJ48.sh arffDaten/OptDigits/optdigits-6.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-7.arff
#./evalScripts/runJ48.sh arffDaten/OptDigits/optdigits-7.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-8.arff
#./evalScripts/runJ48.sh arffDaten/OptDigits/optdigits-8.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: OptDigits/optdigits-9.arff
#./evalScripts/runJ48.sh arffDaten/OptDigits/optdigits-9.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-not_recom.arff
#./evalScripts/runJ48.sh arffDaten/Nursery/nursery-not_recom.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-recommend.arff
#./evalScripts/runJ48.sh arffDaten/Nursery/nursery-recommend.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-very_recom.arff
#./evalScripts/runJ48.sh arffDaten/Nursery/nursery-very_recom.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-priority.arff
#./evalScripts/runJ48.sh arffDaten/Nursery/nursery-priority.arff -o
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-spec_prior.arff
#./evalScripts/runJ48.sh arffDaten/Nursery/nursery-spec_prior.arff -o
#echo "------------------------------------------------------------------------"

echo Name: Monks/monks-problems-1
./evalScripts/runJ48.sh arffDaten/Monks/monks-problems-1_train-mod.arff -T arffDaten/Monks/monks-problems-1_test-mod.arff -o
echo "------------------------------------------------------------------------"

echo Name: Monks/monks-problems-3
./evalScripts/runJ48.sh arffDaten/Monks/monks-problems-3_train-mod.arff -T arffDaten/Monks/monks-problems-3_test-mod.arff -o
echo "------------------------------------------------------------------------"

echo
date
echo

