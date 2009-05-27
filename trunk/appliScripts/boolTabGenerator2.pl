#!/usr/bin/perl

# Dateiname      : boolTabGenerator2.pl
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
#                               <Ant. Eing-DC> <Z-Zahl>
#
# Dabei ist:
#
#  * Anz. Eingaben  : Anzahl der Eingabewerte (Spalten).
#  * Anz. Terme     : Anzahl der Terme (Zeilen).
#  * Ant. Ausg-Eins : Anteil der Terme mit Ausgabewert Eins in Prozent.
#  * Ant. Eing-DC   : Anteil der don't care Werte unter den Eingabewerten
#                     in Prozent.
#  * Z-Zahl         : Ganzzahlige Zufallszahl.

use strict;

# Keine Konfig-Parameter.


# Anzahl der Parameter testen.
if (@ARGV != 5) {
    die "Aufruf: ./boolTabGenerator.pl <Anz. Eingaben> <Anz. Terme>"
        . " <Ant. Ausg-Eins> <Ant. Eing-DC> <Z-Zahl>\n";
}

# Anzahl der Eingabewerte (Spalten).
my $anzEingWerte = $ARGV[0];

# Anzahl der Terme (Zeilen).
my $anzTerme = $ARGV[1];

# Anteil der Terme mit Ausgabewert Eins in Prozent.
my $antAusgEins = $ARGV[2] / 100.0;

# Anteil der don't care Werte unter den Eingabewerten in Prozent.
my $antEingDc = $ARGV[3] / 100.0;

# Die Anzahl der zu erzeugenden Zeilen der Tabelle.
my $zeilenAnz = 2 * $anzTerme;

# Die Tabelle mit den Spalten.
my @tabelle;

# Eine HashMap der schon erzeugten Terme. Jedem schon erzeuten Term ist
# der Wert 1 zugewiesen.
my %erzTerme;


# Setzen des Start-Wertes des Zufallsgenerators.
srand($ARGV[4]);


# Hauptprogramm
printHeader();

# Erzeugung der Tabelle.
for (my $sNr = 0; $sNr < $anzEingWerte; $sNr++) {
    my @spalte;

    for (my $zNr = 0; $zNr < $zeilenAnz; $zNr++) {
        $spalte[$zNr] = 0;
    }

    my $einsAnz = rand() * $zeilenAnz;
    for (my $einsNr = 0; $einsNr < $einsAnz; $einsNr++) {
        my $zeilenNr = rand() * $zeilenAnz;
        $spalte[$zeilenNr] = 1;
    }

    push(@tabelle, [ @spalte ]);
}

# Ausgabe der Tabelle, wobei don't care Werte eingefügt werden.
my $zeilenNr = 0;
for (my $termNr = 0; $termNr < $anzTerme; $termNr++) {
    my $term;

    do {
        $term = "";

        for (my $sNr = 0; $sNr < $anzEingWerte; $sNr++) {
            if (rand() < $antEingDc) {
                $term .= "-";
            } else {
                $term .= $tabelle[$sNr][$zeilenNr];
            }
        }

        $zeilenNr++;
        if ($zeilenNr >= $zeilenAnz) {
            die "Zu wenige Zeilen vorhanden.";
        }
    } while ($erzTerme{$term});
    $erzTerme{$term} = 1;

    if (rand() < $antAusgEins) {
        print "$term 1\n";
    } else {
        print "$term 0\n";
    }
}


# Gibt den Header der PLA-Datei aus.
sub printHeader {
    print ".i $anzEingWerte\n";
    print ".o 1\n";
    print ".p $anzTerme\n";
    print ".type fr\n";
}

