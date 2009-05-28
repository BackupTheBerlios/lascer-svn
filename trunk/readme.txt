                                     Lascer

   Lascer ist ein Software-System zum Bereich Data-Mining. Informationen
   zum Projekt und Downloadmöglichkeiten sind verfügbar unter
   [1]http://developer.berlios.de/projects/lascer/ .

Funktion

   Lascer ist ein Software-System, das Charakterisierungen zu vorgegebenen
   klassifizierten Daten (Beispielen) erzeugt. Die Beispiele sind dabei
   als Attribut-Vektoren im [2]ARFF-Format anzugeben und die
   Charakterisierung, d.h. die Beschreibung der Klassen, erfolgt als Menge
   von Regeln, die möglichst klein (einfach) sein soll. Die Regeln können
   insbesondere mathematische Funktionen enthalten, wodurch eine Menge von
   Regeln als analog zu einem menschlichen Begriff betrachtet werden kann
   (Problem Begriffserwerb). Wenn die Beispiele nur boolesche Werte
   enthalten und boolesch (zweiwertig) klassifiziert sind, entsprechen die
   erzeugten Regeln booleschen Formeln (spezielleres Problem zweistufige
   logische Minimierung).

   Standardmäßig erzeugt Lascer Regeln, die die gegebenen Beispiele
   korrekt und vollständig beschreiben, wobei die Beispiele aber auch
   unbekannte (nicht gegebene) Werte enthalten können. Es kann aber auch
   mit Beispielmengen umgehen, die unvollständig sind oder in denen
   Beispiele fehlerhafte Werte besitzen (durch Verwendung von pruning).
   Eine wichtige Eigenschaft von Lascer besteht außerdem darin, dass es
   sowohl in Bezug auf die Laufzeit wie in Bezug auf den Speicherbedarf
   auf die Verarbeitung von großen Mengen von Beispielen ausgelegt ist.
   Lascer wurde insbesondere für die Erzeugung von Begriffen zu einfachen
   Schachendspielen eingesetzt, wobei die möglichen Stellungen oder Züge
   eines Endspiels die Beispiele darstellen.

   Zur Erzeugung einer Lösung transformiert Lascer ein gegebenes Problem
   in das Set Covering and Exclusion Problem (SCEP), einer
   Verallgemeinerung des Set Covering Problem (SCP). Dieses wird dann
   näherungsweise gelöst mit einem Verfahren für das allgemeine SCP (mit
   nicht-linearer Kostenfunktion).

Komponenten

   Das Projekt umfasst derzeit folgende Komponenten, die auch eigenständig
   von Bedeutung sind:

   endspiel
          Analysiert Endspiele mit drei oder vier Steinen und kann die
          analysierten Daten speichern. Diese können dann interaktiv
          untersucht werden.

   egtb (end game table bases)
          Erzeugt aus einer Datei mit den analysierten Daten eines
          Endspiels eine Datei im ARFF-Format.

   weka/coreExtended
          Eine Erweiterung des Frameworks [3]Weka um Meta-Attribute und
          Meta-Instanzen, wobei letztere Beschreibungen der normalen
          Attribute darstellen.

   geometrischeClusterung
          Erzeugt zu einer Menge von Punkten im n-dimensionalen Raum deren
          konvexe Hülle. Die Facetten der konvexen Hüllen können in Lascer
          als Prädikate (zur Differenzierung zwischen Punkten, die
          Beispiele darstellen) verwendet werden.

   mengenUeberdeckung
          Realisiert das Verfahren zur Erzeugung einer Lösung zum SCP und
          zum SCEP.

   lascer
          Erzeugt zu gegebenen Daten Funktionen und Prädikate und daraus
          dann ein SCEP. Wandelt eine Lösung des SCEP anschließend in eine
          Menge von Regeln um.

   In Zukunft werden einzelne dieser Komponenten eventuell in eigene
   Projekte ausgegliedert. Bis auf die Komponente endspiel, die in Pascal
   implementiert wurde, wurden alle anderen Komponenten in Java
   implementiert. Zur Konvertierung von und zu ARFF-Dateien sind
   perl-Scripte vorhanden, zur Ausführung der Programme sh-Scripte.

   Darüber hinaus nutzt Lascer einige Bibliotheken, von denen
   [4]Architeuthis und [5]MathCollection zusammen mit Lascer entwickelt
   wurden.

Entstehungsgeschichte und Entwickler

   Lascer entstand (bis auf die Komponente endspiel, die früher entwickelt
   wurde) als System der [6]Dissertation von Dietmar Lippold in der
   Informatik an der Universität Stuttgart, hauptsächlich am Institut für
   Intelligente Systeme. Außer Dietmar Lippold haben im Rahmen von Diplom-
   und Studienarbeiten, Sofware-Praktika und Hiwi-Tätigkeiten folgende
   Personen wesentliche Teile mehrerer der Komponenten entwickelt und
   implementiert:
     * Edgar Binder (egtb, mengenUeberdeckung)
     * Haiyi Peng (geometrischeClusterung)
     * Jing Jing Wei (geometrischeClusterung)
     * Michael Wohlfart (mengenUeberdeckung)
     * Natalia Sevcenko (mengenUeberdeckung)
     * Rene Berleong (mengenUeberdeckung)
     * Wolfgang Tischer (mengenUeberdeckung)
     * Yang Zhou (geometrischeClusterung, weka/coreExtended)

   Ihre Namen sind auch in den jeweiligen Quelltext-Dateien genannt.

   Darüber hinaus haben die Personen, die an der Entwicklung der oben
   genannten Komponenten Architeuthis und MathCollection beteiligt waren,
   durch die beiden Komponenten ebenfalls wichtige Beiträge zum
   Gesamtsystem geleistet. Ihre Namen sind auf den beiden Projekt-Seiten
   genannt.

Download und Kontakt

   Die Releases, insbesondere die aktuelle stabile Version, sind abrufbar
   unter
   [7]http://developer.berlios.de/project/showfiles.php?group_id=10520.
   Einige ARFF-Dateien (diejenigen, die in der oben genannten Dissertation
   verwendet wurden) sind abrufbar unter
   [8]ftp://ftp.berlios.de/pub/lascer/arffDaten/, eine Anleitung zur
   Benutzung von Lascer ist abrufbar unter
   [9]https://developer.berlios.de/docman/?group_id=10520.

   Die aktuelle Entwicklerversion ist aus dem Subversion-Repository
   abrufbar unter dem URL svn://svn.berlios.de/lascer. Man kann diese z.B.
   mit folgendem Befehl downloaden: svn checkout
   svn://svn.berlios.de/lascer
   Das Repository kann kann man sich auch mittels Web-Browser anschauen
   unter [10]http://svn.berlios.de/viewcvs/lascer/

   Anregungen und Fragen bitte senden an Dietmar Lippold: lippold at
   users.berlios.de
     __________________________________________________________________

   [Letzte Änderung: 27.05.2009 durch Dietmar Lippold (lippold at
   users.berlios.de)]

   [11]BerliOS Developer Logo

Verweise

   1. http://developer.berlios.de/projects/lascer/
   2. http://www.cs.waikato.ac.nz/~ml/weka/arff.html
   3. http://www.cs.waikato.ac.nz/~ml/weka/
   4. http://architeuthis.berlios.de/
   5. http://mathcollection.berlios.de/
   6. http://www.cuvillier.de/flycms/de/html/30/-UickI3zKPS7yd0Y=/Buchdetails.html
   7. http://developer.berlios.de/project/showfiles.php?group_id=10520
   8. ftp://ftp.berlios.de/pub/lascer/arffDaten/
   9. https://developer.berlios.de/docman/?group_id=10520
  10. http://svn.berlios.de/viewcvs/lascer/
  11. http://developer.berlios.de/
