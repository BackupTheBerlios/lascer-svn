Dateiname      : ReadmeCoreExtended.txt
Letzte �nderung: 31. Mai 2009 durch Dietmar Lippold
Autoren        : Dietmar Lippold

Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
Siehe Datei "license.txt" f�r Hinweise zur Lizenz.


Funktion vom Paket weka/coreExtended
------------------------------------

Das Paket weka/coreExtended ist eine Erweiterung des Frameworks Weka
(http://www.cs.waikato.ac.nz/~ml/weka/) um Meta-Attribute und
Meta-Instanzen, wobei letztere Beschreibungen der normalen Attribute
darstellen.


Verwendung vom Paket weka/coreExtended
--------------------------------------

Die Klassen k�nnen analog zu den Klassen aus Weka verwendet werden, wobei
BasicAttribute der Klasse Attribute aus Weka und BasicInstance der Klasse
Instance aus Weka entspricht.

Zur Ausf�hrung wird die Bibliothek Weka ben�tigt.

Beispiel-Datens�tze mit Meta-Attributen und -Instanzen finden sich im
Verzeichnis weka/coreExtended/tests/. Ein Beispiel-Aufruf kann wie folgt
erfolgen:

  java -classpath weka.jar:. weka.coreExtended.Instances <arff-file>

Dabei ist weka.jar das Paket mit den class-Dateien aus Weka.

