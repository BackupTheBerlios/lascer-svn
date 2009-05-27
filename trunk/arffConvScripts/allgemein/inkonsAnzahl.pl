#!/usr/bin/perl

# Dateiname      : inkonsAnzahl.pl
# Letzte Änderung: 16.06.2006 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm ermittelt zu einer arff-Datei die Anzahl der positiven Beispiele,
# deren Attribut-Werte gleich sind zu einem negativen Beispiel und die
# Anzahl der negativen Beispiele, deren deren Attribut-Werte gleich sind zu
# einem positiven Beispiel.
#
# Aufruf: inkonsAnzahl.pl <arff-Datei>

if (scalar(@ARGV) != 1) {
  die "Aufruf: inkonsAnzahl.pl <arff-Datei>\n";
}

$arffdatei = $ARGV[0];

# Gibt an, ob als nächstes eine Zeile des Datenbereichs eingelesen wird.
my $datenbereich = 0;

# Die Nummer des class Attributs.
my $classAttribNr = -1;

# Die Nummer des letzten eingelesenen Attributs.
my $attributnr = -1;

# Einlesen und speichern der Werte der vorhandenen Beispiele.
open(DATEI, $arffdatei);
while (<DATEI>) {

  if ((length($_) > 1) && !(/^\%/)) {

    if (!$datenbereich) {

      if (/^\@attribute/i) {
          $attributnr++;

          if ((/class *{false, true}/i) || (/class *{true, false}/i)) {
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

      @attributWerte = split(/ *, */);
      $posBeispiel = $attributWerte[$classAttribNr] eq "true";
      $attributWerte[$classAttribNr] = "";
      $attributString = join(',', @attributWerte);

      if ($posBeispiel) {
        $posWerte{$attributString} = 1;
      } else {
        $negWerte{$attributString} = 1;
      }
    }
  }
}
close(DATEI);

# Die Anzahl der Beispiele mit unbekannter Klasse.
$klasseUnbekanntAnz = 0;

# Die Gesamtanzahl positiver Beispiele.
$posGesamtAnz = 0;

# Die Anzahl der positiven inkonsistenten Beispiele.
$posInkonsistentAnz = 0;

# Die Gesamtanzahl negativer Beispiele.
$negGesamtAnz = 0;

# Die Anzahl der negativen inkonsistenten Beispiele.
$negInkonsistentAnz = 0;

$datenbereich = 0;

# Zählen der vorhandenen inkonsistenten Beispiele.
open(DATEI, $arffdatei);
while (<DATEI>) {

  if ((length($_) > 1) && !(/\%/)) {

    if (!$datenbereich) {

      if (/^\@data/i) {
        $datenbereich = 1;
      }

    } else {

      chomp;

      @attributWerte = split(/ *, */);
      $klasseUnbekannt = $attributWerte[$classAttribNr] eq "?";
      $posBeispiel = $attributWerte[$classAttribNr] eq "true";
      $attributWerte[$classAttribNr] = "";
      $attributString = join(',', @attributWerte);

      if ($klasseUnbekannt) {
        $klasseUnbekanntAnz++;
      } elsif ($posBeispiel) {
        $posGesamtAnz++;
      } else {
        $negGesamtAnz++;
      }

      if ($posBeispiel && ($negWerte{$attributString} == 1)) {
        $posInkonsistentAnz++
      }
      if (!$posBeispiel && ($posWerte{$attributString} == 1)) {
        $negInkonsistentAnz++
      }
    }
  }
}

print "Gesamtanzahl pos. Beispiele  = $posGesamtAnz\n";
print " davon inkonsistent          = $posInkonsistentAnz\n";
print "Gesamtanzahl neg. Beispiele  = $negGesamtAnz\n";
print " davon inkonsistent          = $negInkonsistentAnz\n";
print "Anzahl Beispiele unb. Klasse = $klasseUnbekanntAnz\n";

