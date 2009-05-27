#!/usr/bin/perl

# Dateiname      : ergaenzeGruppenAttrib.pl
# Letzte �nderung: 13. Februar 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Programm erg�nzt eine Datei mit klassifizierten Z�gen um ein
# Gruppen-Attribut. Jedem positiven Beispiel wird als Wert der String der
# Ausgangsstellung zugewiesen, jedem negativen Beispiel der String zum
# Ignorieren der Gruppe.
#
# Aufruf: ergaenzeGruppenAttrib.pl <arff-Datei>
# Die erg�nzte Datei wird auf der Standard-Ausgabe ausgegeben.

# Der Name des zu erg�nzenden Attributs.
my $attribname = "or_grp";

# Die Anzahl der Steine auf dem Brett. Die Attribute der Ausgangsstellung
# m�ssen als erste Attribute angegeben sein.
my $steinanz = 3;

# Der Wert, der angibt, da� die Gruppe des Beispiels ignoriert werden soll.
my $ignorwert = "_";


# Die Nummer des als n�chstes einzulesenden Attributs im Header.
$attribnr = 0;

# Die Nummer des Attributs, das die Klasse repr�sentiert.
$classattribnr = -1;

# Gibt an, ob als n�chstes eine Zeile des Datenbereichs eingelesen wird.
my $datenbereich = 0;

while (<>) {

  if (!$datenbereich) {

    if (/^\@attribute class/) {
      $classattribnr = $attribnr;
    }

    if (/^\@attribute/) {
      $attribnr++;
    }

    if (/^\@data/) {
      # Neues Attribut ausgaben.
      print "\@attribute " .  $attribname . " string\n";
      print "\n";
      $datenbereich = 1;
    }

    # Header-Zeile ausgeben
    print;

  } else {

    chomp;
    my $eingabezeile = $_;
    @werte = split(/[ ]*,[ ]*/);

    # Ermittlung der Klasse des Beispiels.
    my $klasse = $werte[$classattribnr];

    if ($klasse eq "false") {
      print $eingabezeile . "," . $ignorwert . "\n";
    } else {
      # Ermittlung des Strings der Ausgangsstellung.
      my $stellung = "";
      for (my $nr = 0; $nr < 2 * $steinanz; $nr++) {
        $stellung .= $werte[$nr];
      }
      print $eingabezeile . "," . $stellung . "\n";
    }
  }
}

