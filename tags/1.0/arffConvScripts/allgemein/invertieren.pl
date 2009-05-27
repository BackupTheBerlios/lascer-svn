#!/usr/bin/perl

# Dateiname      : invertieren.pl
# Letzte Änderung: 09.09.2006 durch Dietmar Lippod
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm invertiert die Klasse aller Beispiele, d.h. ändert die Klasse
# der positiven Beispiele auf "false" und die der negativen Beispiele auf
# "true". Optional kann auch ein Anteil der Beispiele in Prozent, die
# invertiert werden sollen, angegeben werden.
#
# Aufruf: invertieren.pl <arff-Datei> [pos. Anteil] [neg. Anteil] 

use strict;

# Keine Konfig-Parameter vorhanden.

if ((scalar(@ARGV) != 1) && (scalar(@ARGV) != 3)) {
  die "Aufruf: invertieren.pl <arff-Datei> [pos. Anteil] [neg. Anteil]\n";
}

# Den Namen der arff-Datei bestimmen.
my $arffdatei = $ARGV[0];

# Der Anteil der positiven Beispiele, die geändert werden sollen.
my $posAnteil = 1;

# Der Anteil der negativen Beispiele, die geändert werden sollen.
my $negAnteil = 1;

if (scalar(@ARGV) == 3) {
  $posAnteil = $ARGV[1] / 100.0;
  $negAnteil = $ARGV[2] / 100.0;
}

# Gibt an, ob als nächstes eine Zeile des Datenbereichs eingelesen wird.
my $datenbereich = 0;

# Die Nummer des class Attributs.
my $classAttribNr = -1;

# Die Nummer des letzten eingelesenen Attributs.
my $attributnr = -1;

# Einlesen und speichern der Werte der vorhandenen Beispiele.
open(DATEI, $arffdatei) || die "Fehler beim Öffnen der Datei $arffdatei\n";
while (<DATEI>) {

  if ((length($_) <= 1) || (/^\%/)) {

    # Leere Zeile oder Kommentarzeile ausgeben.
    print;

  } else {

    if (!$datenbereich) {

      # Header-Zeile ausgeben.
      print;

      if (/^\@attribute/i) {
          $attributnr++;

          if ((/class *{false, true}/) || (/class *{true, false}/)) {
            $classAttribNr = $attributnr;
          }
      }

      if (/^\@data/i) {
        if ($classAttribNr == -1) {
          die "Kein class Attribut vorhanden";
        }
        $datenbereich = 1;
      }

    } else {

      chomp;

      my @attributWerte = split(/ *, */);
      if ($attributWerte[$classAttribNr] eq "true") {
        if (rand() < $posAnteil) {
          $attributWerte[$classAttribNr] = "false";
        }
      } else {
        if (rand() < $negAnteil) {
          $attributWerte[$classAttribNr] = "true";
        }
      }
      my $attributString = join(',', @attributWerte);

      print "$attributString\n";
    }
  }
}
close(DATEI);

