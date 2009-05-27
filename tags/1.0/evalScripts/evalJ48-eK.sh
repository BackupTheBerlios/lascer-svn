#!/bin/sh

# Dateiname      : evalJ48-eK.sh
# Letzte Änderung: 25. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Evaluation von J48 mit exakter Klassifikation.

echo Verfahren: J48-eK
date
echo

echo "------------------------------------------------------------------------"

echo Name: HeartDisease/heart-statlog-present.arff
./evalScripts/runJ48.sh arffDaten/HeartDisease/heart-statlog-present.arff -o -U -M 1
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-aver.arff
./evalScripts/runJ48.sh arffDaten/Agricultural/eucalyptus-aver.arff -o -U -M 1
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-best.arff
./evalScripts/runJ48.sh arffDaten/Agricultural/eucalyptus-best.arff -o -U -M 1
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-good.arff
./evalScripts/runJ48.sh arffDaten/Agricultural/eucalyptus-good.arff -o -U -M 1
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-low.arff
./evalScripts/runJ48.sh arffDaten/Agricultural/eucalyptus-low.arff -o -U -M 1
echo "------------------------------------------------------------------------"

echo Name: Agricultural/eucalyptus-none.arff
./evalScripts/runJ48.sh arffDaten/Agricultural/eucalyptus-none.arff -o -U -M 1
echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-13.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-13.arff -o -U -M 1
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-14.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-14.arff -o -U -M 1
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-15.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-15.arff -o -U -M 1
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-13-red.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-13-red.arff -o -U -M 1
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-14-red.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-14-red.arff -o -U -M 1
#echo "------------------------------------------------------------------------"

#echo Name: Soybean/soybean-15-red.arff
#./evalScripts/runJ48.sh arffDaten/Soybean/soybean-15-red.arff -o -U -M 1
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-not_recom.arff
#./evalScripts/runJ48.sh arffDaten/Nursery/nursery-not_recom.arff -o -U -M 1
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-recommend.arff
#./evalScripts/runJ48.sh arffDaten/Nursery/nursery-recommend.arff -o -U -M 1
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-very_recom.arff
#./evalScripts/runJ48.sh arffDaten/Nursery/nursery-very_recom.arff -o -U -M 1
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-priority.arff
#./evalScripts/runJ48.sh arffDaten/Nursery/nursery-priority.arff -o -U -M 1
#echo "------------------------------------------------------------------------"

#echo Name: Nursery/nursery-spec_prior.arff
#./evalScripts/runJ48.sh arffDaten/Nursery/nursery-spec_prior.arff -o -U -M 1
#echo "------------------------------------------------------------------------"

echo Name: Monks/monks-problems-1
./evalScripts/runJ48.sh arffDaten/Monks/monks-problems-1_train-mod.arff -T arffDaten/Monks/monks-problems-1_test-mod.arff -o -U -M 1
echo "------------------------------------------------------------------------"

echo Name: Monks/monks-problems-3
./evalScripts/runJ48.sh arffDaten/Monks/monks-problems-3_train-mod.arff -T arffDaten/Monks/monks-problems-3_test-mod.arff -o -U -M 1
echo "------------------------------------------------------------------------"

echo
date
echo

