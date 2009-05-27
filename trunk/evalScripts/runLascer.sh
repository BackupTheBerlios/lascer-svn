#!/bin/sh

# Dateiname      : runLascer.sh
# Letzte Änderung: 26. Mai 2009 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

./execScripts/runWekaClassi.sh -configParam config/lascerParamEinfach -formelKlasse pos -t $@

