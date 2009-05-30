Dateiname      : ReadmeEndspiel.txt
Letzte �nderung: 30. Mai 2009 durch Dietmar Lippold
Autoren        : Dietmar Lippold

Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
Siehe Datei "license.txt" f�r Hinweise zur Lizenz.


Funktion vom Paket Endspiel
---------------------------

Das Paket Endspiel enth�lt Programme zur Analyse und zum F�hren von
Schachendspielen mit 3 oder 4 Steinen.

Die Programme wurden entwickelt von Dietmar Lippold in seiner Projektarbeit
mit dem Titel "Vollst�ndige Analyse einfacher Schachendspiele", Universit�t
Kaiserslautern 1996, und in seinem Artikel "The Legitimacy of Positions in
Endgame Databases" im "ICCA Journal" (ISSN 0920-234X), volume 20 (1997),
number 1, Seiten 20-28 verwendet.

Die Programme liegen in zwei Pascal-Dialekten vor, wobei sich die jeweiligen
Quelltexte aber nur in wenigen Zeilen unterscheiden. Die Quelltexte mit der
Endung "pp" lassen sich zumindest mit "Free Pascal"
(http://www.freepascal.org/") und "GNU Pascal" (http://www.gnu-pascal.de/)
�bersetzen, die Quelltexte mit der Endung "pas" zumindest mit "GNU Pascal".
Bei Verwendung von GNU Pascal sind die Dateien, die gespeichert werden, aber
vier mal so gro� wie die Dateien bei Verwendung von Free Pascal, da die
ausf�hrbaren Programme von Free Pascal die Daten komprimiertert speichern.
Der Inhalt ist in beiden F�llen der gleiche und es k�nnen auch beide Arten
von Dateien vom Paket "egtb" weiter verwendet werden, mit den gleichen
Ergebnissen.


Verwendung vom Paket Endspiel
-----------------------------

Es sind folgende Programme vorhanden:

  espanalyse3, espanalyse4:

    Sie dienen der Analyse eines Endspiels mit 3 bzw. 4 Steinen. Die
    Daten der Analyse k�nnen gespeichert werden. Sie k�nnen zum einen
    f�r die anschlie�ende Untersuchung (mit espfuehrung3, espfuehrung4;
    Funktion "Untersuchung") oder f�r die Analyse eines anderen Endspiels
    (Funktion "Datenerzeugung") verwendet werden.

    Bei der Funktion "Untersuchung" bedeutet "vollstaendige Definition",
    dass die Analyse vollst�ndig ist in Bezug auf die legalen Stellungen,
    d.h. es wird eine Obermenge der legalen Stellungen verwendet.
    "Korrekte Definition" bedeutet entsprechend, dass keine nicht legale
    Stellung verwendet wird.

    Das Ziel des "schnellsten Matts" bedeutet, dass die geringste Anzahl
    von Halbz�gen bis zum Matt gesucht wird, unter Ber�cksichtigung eines
    m�glichen Nachfolge-Endspiels, das durch Umwandlung eines Bauern oder
    Schlagen eines Steins entsteht. "Schnellster Gewinn" bedeutet, dass die
    geringste Anzahl von Halbz�gen bis zum Matt, bis zur Umwandlung eines
    Bauern oder bis zum Schlagen eines Steins gesucht wird, wobei das
    Nachfolge-Endspiel dabei ebenfalls gewinnbar sein muss.

  espfuehrung3, espfuehrung4:

    Sie dienen dem F�hren eines Endspiels mittels zuvor erzeugter Daten
    einer Analyse (mittels espanalyse3 oder espanalyse4).

  ableitung3, ableitung4:

    Sie dienen der Ermittlung einer unteren und einer oberen Grenze f�r
    die Anzahl der legalen Stellungen in einem Endspiel. Eine Stellung
    ist dabei als legal definiert, wenn sie aus der Grundstellung heraus
    durch legale Z�ge erreichbar ist.

Bei der Verwendung der Programme wird eine Figur durch ihren ersten
Buchstaben (z.B. "T" f�r Turm) und ein Bauer durch seine Linie bezeichnet.
"x" steht f�r einen beliebigen Bauern.

Bei der Analyse der Endspiele entspricht die "Datenerzeugung" einer
"Untersuchung" mit "vollstaendiger Definition" und gegebenenfalls
"schnellstem Matt".

