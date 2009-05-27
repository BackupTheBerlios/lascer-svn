#!/usr/bin/perl

# Dateiname      : isWon3.pl
# Letzte Änderung: 10. Oktober 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm konvertiert eine Endspiel-Datei mit drei Steinen in eine Datei,
# in der die Stellungen danach klassifiziert sind, ob sie für eine Seite
# gewonnen sind. Die konvertierte Datei für über STDOUT ausgegeben.
#
# Die Ausgangsdatei ist z.B. für das KQK-Endspiel durch folgenden Aufruf zu
# erzeugen:
# java egtb/Conversion -black -legal -nocaptures -noconversions KQK.lpd
#
# Aufruf: cat arff-Datei | ./isWon3.pl <ja|nein>
#
# Der Parameter gibt an, ob der Gewinn der Stellungen in Bezug auf die Seite
# am Zug beurteilt werden soll.

use strict;


# Keine Konfig-Parameter.


# Parameter testen.
if ((@ARGV != 1) || ($ARGV[0] ne "ja") && ($ARGV[0] ne "nein")) {
    die "Aufruf: cat arff-Datei | ./isWon3.pl <ja|nein>\n";
}

# Gibt an, ob die Stellungen in Bezug auf die Seite am Zug als gewonnen
# bewerte werden sollen.
my $gewonnenAmZug = ($ARGV[0] eq "ja");

# Eine Liste der Nummern der zu entfernenden Attribute. Das erste Attribut
# hat die Nummer Null.
# Zu entfernende Attribute: sideToMove, illegal, plies
my @entfernenNummern = (6, 7, 8, 9);


# Gibt die Anzahl schon eingelesener Attribute an.
my $attributeGelesen = 0;

# Gibt die Anzahl der vorhandenen Attuibute an.
my $attributAnz;

# Die Nummer des Attributs, das die Anzahl der Halbzüge bis zum Gewinn
# angibt.
my $zugAnzNr;

# Die Nummer des Attributs das angibt, ob die Stellung remis ist.
my $remisAttribNr;

# Gibt zu jeder Attributnummer an, ob das Attribut entfernt werden soll.
my @entfernen;
foreach my $nummer (@entfernenNummern) {
  $entfernen[$nummer] = 1;
}


while (<STDIN>) {

  if (/^[\s]*$/) {

    # Leerzeile unverändert ausgeben.
    print $_;

  } elsif (/^\%/) {

    # Kommentarzeile unverändert ausgeben.
    print $_;

  } elsif (/^\@attribute/i) {

    $attributeGelesen++;

    if (!$entfernen[$attributeGelesen - 1]) {
      # Zeile mit Attribut ausgeben.
      print $_;
    }

  } elsif (/^\@data/) {
    # Neues Attribut ausgeben.
    print "\@attribute won {false, true}\n\n";
    print $_;

    $attributAnz = $attributeGelesen;
    $zugAnzNr = $attributAnz - 1;
    $remisAttribNr = $zugAnzNr - 1;

  } elsif (/^\@/) {

    # Header-Zeile unverändert ausgeben.
    print $_;

  } else {

    # Datenzeile aufspalten.
    chomp;
    my @werte = split(/[ ]*,[ ]*/);

    # Die nächste Ausgabezeile.
    my $ausgabe = "";

    # Vorhandene, nicht zu entfernende Werte an die Ausgabe anhängen.
    for (my $nr = 0; $nr < $attributAnz; $nr++) {
      if (!$entfernen[$nr]) {
        $ausgabe .= $werte[$nr] . ",";
      }
    }

    if ($werte[$zugAnzNr] == 0) {
      if ($werte[$remisAttribNr] eq "true") {
        # Die Stellung ist remis.
        $ausgabe .= "false";
      } else {
        # Die Stellung ist matt.
        if ($gewonnenAmZug) {
          $ausgabe .= "false";
        } else {
          $ausgabe .= "true";
        }
      }
    } else {
      # Die Stellung ist weder remis noch matt.
      if ($gewonnenAmZug && ($werte[$zugAnzNr] > 0)
          || !$gewonnenAmZug && ($werte[$zugAnzNr] < 0)) {
        $ausgabe .= "true";
      } else {
        $ausgabe .= "false";
      }
    }

    print $ausgabe . "\n";
  }
}

