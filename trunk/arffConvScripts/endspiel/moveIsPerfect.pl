#!/usr/bin/perl

# Dateiname      : moveIsPerfect.pl
# Letzte Änderung: 18. März 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm ersetzt in einer arff-Datei mit möglichen Zügen den Wert des
# Attributs class durch ein Attribut das angibt, ob der Zug perfekt ist.


# Konfig-Parameter:

# Gibt die Anzahl der vorhandenen Attuibute an.
my $attributAnz = 14;

# Eine Liste der Nummern der zu entfernenden Attribute. Das erste Attribut
# hat die Nummer Null.
my @entfernenNummern = (12, 13);

# Eine Hash-Liste mit den Namen und Wertebereichen neuer Attribute.
my %neueAttribute = (moveIsPerfect => '{false, true}');


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

    if ($attributeGelesen == $attributAnz) {
      # Es sind alle Attribute eingelesen. Neue Attribute ausgeben.
      foreach my $name (keys %neueAttribute) {
        print "\@attribute " . $name . " " . $neueAttribute{$name} . "\n";
      }
      # Anzahl der eingelesenen Attribute erhöhen, damit der Vergleich
      # beim nächsten Test nicht erfüllt ist.
      $attributGelesen++;
    }

  } elsif (/^\@/) {

    # Header-Zeile unverändert ausgeben.
    print $_;

  } else {

    # Datenzeile.
    chomp;
    @werte = split(/[ ]*,[ ]*/);

    # Die Liste für die Werte der neuen Attribute.
    my @neueWerte;

    ###
    # Beginn der Berechnung der Werte der neuen Attribute.
    ###

    if ($werte[$attributAnz - 1] eq "perfect") {
      $neueWerte[0] = "true";
    } else {
      $neueWerte[0] = "false";
    }

    ###
    # Ende der Berechnung der Werte der neuen Attribute.
    ###

    # Die nächste Ausgabezeile.
    $ausgabe = "";

    # Vorhandene, nicht zu entfernende Werte an die Ausgabe anhängen.
    for (my $nr = 0; $nr < $attributAnz; $nr++) {
      if (!$entfernen[$nr]) {
        $ausgabe .= $werte[$nr] . ",";
      }
    }

    # Neue Werte an die Ausgabe anhängen.
    foreach my $wert (@neueWerte) {
      $ausgabe .= $wert . ",";
    }

    chop $ausgabe;
    print $ausgabe . "\n";
  }
}

