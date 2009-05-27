#!/usr/bin/perl

# Dateiname      : inkonsFilter.pl
# Letzte Änderung: 16.06.2006 durch Dietmar Lippod
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm filtert zu einer arff-Datei inkonsistente Beispiele raus. Die
# anderen Zeilen werden über STDOUT ausgegeben.
#
# Aufruf: inkonsFilter.pl <arff-Datei> [ja|nein] [ja|nein]
#
# Der zweite Kommandozeilen-Parameter gibt an, ob die inkonsistenten
# positiven Beispiele ausgefiltert werden sollen, der dritte, ob die
# inkonsistenten negativen ausgefiltert werden sollen.

if ((scalar(@ARGV) != 3)
    || ($ARGV[1] ne "ja") && ($ARGV[1] ne "nein")
    || ($ARGV[2] ne "ja") && ($ARGV[2] ne "nein")) {
  die "Aufruf: inkonsFilter.pl <arff-Datei> [ja|nein] [ja|nein]\n";
}

$arffdatei = $ARGV[0];
$filterInkonsPos = ($ARGV[1] eq "ja");
$filterInkonsNeg = ($ARGV[2] eq "ja");

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

# Die Gesamtanzahl positiver Beispiele.
$posGesamtAnz = 0;

# Die Anzahl der positiven inkonsistenten Beispiele.
$posInkonsistentAnz = 0;

# Die Gesamtanzahl negativer Beispiele.
$negGesamtAnz = 0;

# Die Anzahl der negativen inkonsistenten Beispiele.
$negInkonsistentAnz = 0;

$datenbereich = 0;

# Alle Zeilen bis auf die inkonsistenten Zeilen ausgeben.
open(DATEI, $arffdatei);
while (<DATEI>) {

  if ((length($_) <= 1) || (/\%/)) {

    # Leere Zeile oder Kommentar-Zeile ausgeben.
    print;

  } else {

    if (!$datenbereich) {

      # Header-Zeile ausgeben.
      print;

      if (/^\@data/i) {
        $datenbereich = 1;
      }

    } else {

      chomp;

      @attributWerte = split(/ *, */);
      $posBeispiel = $attributWerte[$classAttribNr] eq "true";
      $attributWerte[$classAttribNr] = "";
      $attributString = join(',', @attributWerte);

      if ($posBeispiel
          && (!$filterInkonsPos || ($negWerte{$attributString} != 1))) {
        # Zeile mit positivem Beispiel ausgeben.
        print "$_\n";
      }

      if (!$posBeispiel
          && (!$filterInkonsNeg || ($posWerte{$attributString} != 1))) {
        # Zeile mit negativem Beispiel ausgeben.
        print "$_\n";
      }
    }
  }
}

