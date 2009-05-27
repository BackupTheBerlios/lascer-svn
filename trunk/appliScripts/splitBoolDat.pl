#!/usr/bin/perl

# Dateiname      : splitBoolDat.pl
# Letzte Änderung: 21. September 2006 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Spaltet eine boolsche Tabelle im PLA-Format nach den einzelnen
# Werte der Ausgabe-Variablen auf.
#
# Aufruf: cat datei.pla | ./splitBoolDat.pl <nr>
#
# Dabei ist <nr> die Nummer des Wertes der Ausgabe-Variablen, zu dem die
# relevanten Zeilen ausgegeben werden sollen.

use strict;

# Keine Konfig-Parameter.


# Anzahl der Parameter testen.
if (@ARGV != 1) {
    die "Aufruf: ./split.pl <Nummer des Ausgabewertes>\n";
}

# Die Nummer des auszugebenden Ausgabe-Wertes.
my $ausgabeWertNr = $ARGV[0];

# Die Zeile mit der Angabe der Eingabe-Variablen.
my $eingabeVarText;

# Die Anzahl der auszugebenden Terme.
my $termAnz = 0;

# Der auszugebende Text.
my $ausgabe = "";

while (<STDIN>) {

    if (/^\.i /) {

        $eingabeVarText = $_;

    } elsif ((/^\.type /) && (!/^\.type +fr/)) {

        print STDERR "Datei hat nicht das PLA-Format fr.\n";

    } elsif ((!/^\s*$/) && (!/^\./)) {

        chomp();
        (my $inputs, my $outputs) = split(/ /);

        my $ausgabeWert = substr($outputs, $ausgabeWertNr, 1);
        if ($ausgabeWert ne "~") {
            $ausgabe .= "$inputs $ausgabeWert\n";
            $termAnz++;
        }
    }

}

print $eingabeVarText;
print ".o 1\n";
print ".p $termAnz\n";
print ".type fr\n";
print $ausgabe;

