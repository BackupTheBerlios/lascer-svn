#!/usr/bin/perl

# Dateiname      : arff-to-logic.pl
# Letzte Änderung: 16.06.2006 durch Dietmar Lippod
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Konvertiert eine Datei im arff-Format in das logic-Format (eine
# vereinfachte Version vom espresso-Format). Die erzeugte Datei wird über
# STDOUT ausgegeben.
#
# Aufruf: arff-to-logic.pl <datei.arff> [val|esp]
#
# Der letzte Parameter gibt an, ob die zu erzeugende Datei mit dem Header für
# das Format von Valentin (bei "val") oder für das Format von Espresso (bei
# "esp") erzeugt werden soll.

use strict;

# Es sind keine Konstanten zur Konfiguration vorhanden.


# Die Anzahl der Attribute
my $attribAnz = 0;

# Zu jeder Nummer eines Attributs ein Hash mit dessen Werten.
my @attribWertHashs;

# Gibt an, ob als nächstes eine Zeile des Datenbereichs eingelesen wird.
my $datenbereich = 0;

# Die Nummer des class Attributs.
my $classAttribNr = -1;

# Die Nummer des letzten eingelesenen Attributs.
my $attributnr = -1;

# Die Anzahl der Daten-Zeilen.
my $datenzeilen = 0;

if ((@ARGV != 2) || ($ARGV[1] ne "val") && ($ARGV[1] ne "esp")) {
    die "Aufruf: arff-to-logic.pl <arff-Datei> [val|esp]\n";
}

# Gibt an, ob die zu erzeugende Datei das Format von Valentin haben soll.
my $formatValentin = ($ARGV[1] eq "val");

# Attribute und Anzahl der Daten-Zeilen ermitteln.
open(DATEI, $ARGV[0]) or die "Datei konnte nicht geöffnet werden";
while (<DATEI>) {

    # Leerzeilen und Kommentar-Zeilen nicht verarbeiten.
    if ((!/(^[\s]*$)/) && (!/\%/)) {

        chomp;

        if (!$datenbereich) {

            if (/^\@attribute/) {
                $attributnr++;

                my $kennzeichnung;
                my $name;
                my $werteString;
                my @werte;

                if ((/class *{false, true}/) || (/class *{true, false}/)) {
                    $classAttribNr = $attributnr;
                } else {
                    # Werte des Attributs einlesen.
                    ($kennzeichnung, $name, $werteString) = split(/ +/, $_, 3);
                    @werte = split(/[ {,}]+/, $werteString);
                    if (scalar(@werte) != 3) {
                        die "Attribut $name ist nicht boolsch\n";
                    } else {
                        my %hash;
                        for (my $nr = 1; $nr < scalar(@werte); $nr++) {
                            $hash{$werte[$nr]} = $nr;
                        }
                        $attribWertHashs[$attributnr] = \%hash;
                    }
                }
            }

            if (/^\@data/) {
                if ($classAttribNr == -1) {
                    die "Kein class Attribut vorhanden";
                }
                $datenbereich = 1;
            }

        } else {

            $datenzeilen++;

        }
    }
}
close(DATEI);

# Die Header-Zeilen der logic-Datei ausgeben.
if ($formatValentin) {
    print $attributnr . "\n";
    print "1 \n";
    print $datenzeilen . "\n";
} else {
    print "# $ARGV[0]\n";
    print ".i $attributnr\n";
    print ".o 1\n";
    print ".p $datenzeilen\n";
    print ".type fr\n";

}

$datenbereich = 0;

# Die Daten-Zeilen einlesen und im logic-Format ausgeben.
open(DATEI, $ARGV[0]) or die "Datei konnte nicht geöffnet werden";
while (<DATEI>) {

    # Leerzeilen und Kommentar-Zeilen nicht verarbeiten.
    if ((!/(^[ ]*$)/) && (!/\%/)) {

        if (!$datenbereich) {

            if (/^\@data/) {
                $datenbereich = 1;
            }

        } else {

            chomp;

            my @attributWerte = split(/ *, */);
            my $posBeispiel = $attributWerte[$classAttribNr] eq "true";

            # Die Werte der Attribute ausgeben.
            for (my $nr = 0; $nr < scalar(@attributWerte); $nr++) {
                if ($nr != $classAttribNr) {
                    my %hash = %{$attribWertHashs[$nr]};
                    my $wertNr = $hash{$attributWerte[$nr]} - 1;
                    print $wertNr;
                }
            }

            # Den Wert der Klasse ausgeben.
            if ($posBeispiel) {
                print " 1\n";
            } else {
                print " 0\n";
            }
        }
    }
}
close(DATEI);

