#!/usr/bin/perl

# Dateiname      : hasPly.pl
# Letzte �nderung: 18. M�rz 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Programm konvertiert eine Endspiel-Datei in eine Datei, in der die
# Stellungen danach klassifiziert sind, ob sie in einer bestimmten Anzahl
# von Z�gen gewonnen oder verloren sind. Dazu entfernt das Programm aus einer
# arff-Datei mit der Angabe von Halbz�gen zu Stellungen einige Attribute und
# erg�nzt das class-Attribut.
#
# Die Ausgangsdatei ist z.B. f�r das KQK-Endspiel durch folgenden Aufruf zu
# erzeugen:
# java egtb/Conversion -black -legal -nocaptures -noconversions -nodraws KQK.lpd


# Konfig-Parameter:

# Gibt die Anzahl der vorhandenen Attuibute an.
my $attributAnz = 10;

# Eine Liste der Nummern der zu entfernenden Attribute. Das erste Attribut
# hat die Nummer Null.
# Zu entfernende Attribute: sideToMove, illegal, draw, plies
my @entfernenNummern = (6, 7, 8, 9);

# Eine Hash-Liste mit den Namen und Wertebereichen neuer Attribute.
my %neueAttribute = (class => '{false, true}');

# Gibt die Anzahl der Halbz�ge bis zum Gewinn an, deren Stellungen f�r das
# neue Attribut class Wert true erhalten sollen.
my $ply = 0;


# Gibt die Anzahl schon eingelesener Attribute an.
my $attributeGelesen = 0;

# Gibt zu jeder Attributnummer an, ob das Attribut entfernt werden soll.
my @entfernen;
for (my $nr = 0; $nr < $attributAnz; $nr++) {
  $entfernen[$nr] = 0;
}
foreach my $nummer (@entfernenNummern) {
  $entfernen[$nummer] = 1;
}


while (<>) {

  if (/^[\s]*$/) {

    # Leerzeile unver�ndert ausgeben.
    print $_;

  } elsif (/^\%/) {

    # Kommentarzeile unver�ndert ausgeben.
    print $_;

  } elsif (/^\@attribute/i) {

    $attributeGelesen++;

    if (!$entfernen[$attributeGelesen - 1]) {
      # Zeile mit Attribut ausgeben.
      print $_;
    }

    if ($attributeGelesen == $attributAnz) {
      # Es sind alle Attribute eingelesen. Neue Attribute ausgeben.
      foreach my $name (keys %neueAttribute) {
        print "\@attribute " . $name . " " . $neueAttribute{$name} . "\n";
      }
      # Anzahl der eingelesenen Attribute erh�hen, damit der Vergleich
      # beim n�chsten Test nicht erf�llt ist.
      $attributGelesen++;
    }

  } elsif (/^\@/) {

    # Header-Zeile unver�ndert ausgeben.
    print $_;

  } else {

    # Datenzeile.
    chomp;
    @werte = split(/[ ]*,[ ]*/);

    # Die Liste f�r die Werte der neuen Attribute.
    my @neueWerte;

    ###
    # Beginn der Berechnung der Werte der neuen Attribute.
    ###

    if ($werte[$attributAnz - 1] == $ply) {
      $neueWerte[0] = "true";
    } else {
      $neueWerte[0] = "false";
    }

    ###
    # Ende der Berechnung der Werte der neuen Attribute.
    ###

    # Die n�chste Ausgabezeile.
    $ausgabe = "";

    # Vorhandene, nicht zu entfernende Werte an die Ausgabe anh�ngen.
    for (my $nr = 0; $nr < $attributAnz; $nr++) {
      if (!$entfernen[$nr]) {
        $ausgabe .= $werte[$nr] . ",";
      }
    }

    # Neue Werte an die Ausgabe anh�ngen.
    foreach my $wert (@neueWerte) {
      $ausgabe .= $wert . ",";
    }

    chop $ausgabe;
    print $ausgabe . "\n";
  }
}

