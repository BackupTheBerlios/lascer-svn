#!/usr/bin/perl

# Dateiname      : isDrawn4.pl
# Letzte �nderung: 03. Oktober 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Programm konvertiert eine Endspiel-Datei mit vier Steinen in eine Datei,
# in der die Stellungen danach klassifiziert sind, ob sie remis sind. Dazu
# entfernt das Programm aus einer arff-Datei mit der Angabe von Halbz�gen zu
# Stellungen einige Attribute.
#
# Die Ausgangsdatei ist z.B. f�r das KPKR-Endspiel durch folgenden Aufruf zu
# erzeugen:
# java egtb/Conversion -black -legal -nocaptures -noconversions KPKR.lpd


# Keine Konfig-Parameter.


# Gibt die Anzahl der vorhandenen Attuibute an.
my $attributAnz = 12;

# Eine Liste der Nummern der zu entfernenden Attribute. Das erste Attribut
# hat die Nummer Null.
# Zu entfernende Attribute: sideToMove, illegal, plies
my @entfernenNummern = (8, 9, 11);


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
      # Es sind alle Attribute eingelesen.
      # Anzahl der eingelesenen Attribute erh�hen, damit der Vergleich
      # beim n�chsten Test nicht erf�llt ist.
      $attributGelesen++;
    }

  } elsif (/^\@/) {

    # Header-Zeile unver�ndert ausgeben.
    print $_;

  } else {

    # Datenzeile aufspalten.
    chomp;
    @werte = split(/[ ]*,[ ]*/);

    # Die n�chste Ausgabezeile.
    $ausgabe = "";

    # Vorhandene, nicht zu entfernende Werte an die Ausgabe anh�ngen.
    for (my $nr = 0; $nr < $attributAnz; $nr++) {
      if (!$entfernen[$nr]) {
        $ausgabe .= $werte[$nr] . ",";
      }
    }

    chop $ausgabe;
    print $ausgabe . "\n";
  }
}

