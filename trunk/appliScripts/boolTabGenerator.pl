#!/usr/bin/perl

# Dateiname      : boolTabGenerator.pl
# Letzte Änderung: 21. September 2006 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Erzeugt eine boolschen Wertetabelle mit einem einzelnen Ausgabewert. Die
# Tabelle wird im PLA-Format "fr", wie sie auch vom Programm ESPRESSO benutzt
# wird, auf STDOUT ausgegeben.
#
# Aufruf: ./boolTabGenerator.pl <Anz. Eingaben> <Anz. Terme> <Ant. Ausg-Eins>
#                               <Ant. Eing-Eins> <Ant. Eing-DC> <Z-Zahl>
#
# Dabei ist:
#
#  * Anz. Eingaben  : Anzahl der Eingabewerte (Spalten).
#  * Anz. Terme     : Anzahl der Terme (Zeilen).
#  * Ant. Ausg-Eins : Anteil der Terme mit Ausgabewert Eins in Prozent.
#  * Ant. Eing-Eins : Anteil der Einsen unter den Eingabewerten in Prozent.
#  * Ant. Eing-DC   : Anteil der don't care Werte unter den Eingabewerten
#                     in Prozent.
#  * Z-Zahl         : Ganzzahlige Zufallszahl.

use strict;

# Keine Konfig-Parameter.


# Anzahl der Parameter testen.
if (@ARGV != 6) {
    die "Aufruf: ./boolTabGenerator.pl <Anz. Eingaben> <Anz. Terme>"
        . " <Ant. Ausg-Eins> <Ant. Eing-Eins> <Ant. Eing-DC> <Z-Zahl>\n";
}

# Anzahl der Eingabewerte (Spalten).
my $anzEingWerte = $ARGV[0];

# Anzahl der Terme (Zeilen).
my $anzTerme = $ARGV[1];

# Anteil der Terme mit Ausgabewert Eins in Prozent.
my $antAusgEins = $ARGV[2] / 100.0;

# Anteil der Einsen unter den Eingabewerten in Prozent.
my $antEingEins = $ARGV[3] / 100.0;

# Anteil der don't care Werte unter den Eingabewerten in Prozent.
my $antEingDc = $ARGV[4] / 100.0;

# Eine HashMap der schon erzeugten Terme. Jedem schon erzeuten Term ist
# der Wert 1 zugewiesen.
my %erzTerme;


# Prüfen der Anteile an den Eingabewerten.
if ($antEingEins + $antEingDc > 1.0) {
    die "Die Werte <Ant. Eing-Eins> <Ant. Eing-DC> sind zu groß.\n";
}

# Setzen des Start-Wertes des Zufallsgenerators.
srand($ARGV[5]);


# Hauptprogramm
printHeader();

for (my $termNr = 0; $termNr < $anzTerme; $termNr++) {
    print neuerTerm();
    if (rand() < $antAusgEins) {
        print " 1\n";
    } else {
        print " 0\n";
    }
}


# Gibt den Header der PLA-Datei aus.
sub printHeader {
    print ".i $anzEingWerte\n";
    print ".o 1\n";
    print ".p $anzTerme\n";
    print ".type fr\n";
}

# Liefert einen neuen Term, der noch nicht erzeugt wurde.
sub neuerTerm {
    my $term;

    do {
        $term = "";

        for (my $i = 0; $i < $anzEingWerte; $i++) {
            my $zufallsert = rand();
            if ($zufallsert < $antEingDc) {
                $term .= "-";
            } elsif ($zufallsert < $antEingDc + $antEingEins) {
                $term .= "1";
            } else {
                $term .= "0";
            }
        }
    } while ($erzTerme{$term});
    $erzTerme{$term} = 1;

    return $term;
}

