#!/bin/sh

# Dateiname      : runLascer.sh
# Letzte �nderung: 26. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

./execScripts/runWekaClassi.sh -configParam config/lascerParamEinfach -formelKlasse pos -t $@

