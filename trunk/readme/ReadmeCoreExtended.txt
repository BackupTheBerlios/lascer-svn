Dateiname      : ReadmeCoreExtended.txt
Letzte Änderung: 30. Mai 2009 durch Dietmar Lippold
Autoren        : Dietmar Lippold

Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
Siehe Datei "license.txt" für Hinweise zur Lizenz.


Funktion vom Paket weka/coreExtended
------------------------------------

Das Paket weka/coreExtended ist eine Erweiterung des Frameworks Weka um
Meta-Attribute und Meta-Instanzen, wobei letztere Beschreibungen der
normalen Attribute darstellen.


Verwendung vom Paket weka/coreExtended
--------------------------------------

Die Klassen können analog zu den Klassen aus Weka verwendet werden, wobei
BasicAttribute der Klasse Attribute aus Weka und BasicInstance der Klasse
Instance aus Weka entspricht.

Beispiel-Datensätze mit Meta-Attributen und -Instanzen finden sich im
Verzeichnis weka/coreExtended/tests/. Ein Beispiel-Aufruf kann wie folgt
erfolgen:

  java -classpath weka.jar:. weka.coreExtended.Instances <arff-file>

Dabei ist weka.jar das Paket aus Weka.

