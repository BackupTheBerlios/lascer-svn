#!/usr/bin/perl

# Dateiname      : ergaenzeGruppenAttrib.pl
# Letzte Änderung: 13. Februar 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm ergänzt eine Datei mit klassifizierten Zügen um ein
# Gruppen-Attribut. Jedem positiven Beispiel wird als Wert der String der
# Ausgangsstellung zugewiesen, jedem negativen Beispiel der String zum
# Ignorieren der Gruppe.
#
# Aufruf: ergaenzeGruppenAttrib.pl <arff-Datei>
# Die ergänzte Datei wird auf der Standard-Ausgabe ausgegeben.

# Der Name des zu ergänzenden Attributs.
my $attribname = "or_grp";

# Die Anzahl der Steine auf dem Brett. Die Attribute der Ausgangsstellung
# müssen als erste Attribute angegeben sein.
my $steinanz = 3;

# Der Wert, der angibt, daß die Gruppe des Beispiels ignoriert werden soll.
my $ignorwert = "_";


# Die Nummer des als nächstes einzulesenden Attributs im Header.
$attribnr = 0;

# Die Nummer des Attributs, das die Klasse repräsentiert.
$classattribnr = -1;

# Gibt an, ob als nächstes eine Zeile des Datenbereichs eingelesen wird.
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

