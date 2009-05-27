#!/usr/bin/perl

# Dateiname      : sojaConvert.pl
# Letzte �nderung: 19. M�rz 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Programm beschr�nkt in der Soja-Datei die Klasse auf einen der m�glichen
# Werte. Der Name der Klasse ist als Kommandozeilenparameter anzugeben.

# Konfig-Parameter:

# Gibt die Anzahl der vorhandenen Attuibute an.
my $attributAnz = 36;

# Eine Liste der Nummern der zu entfernenden Attribute.
my @entfernenNummern = (35);

# Eine Liste mit den Namen neuer Attribute.
my @neueAttribNamen = ('class');

# Eine Hash-Liste mit den Namen und Wertebereichen neuer Attribute.
my %neueAttribute = ('class' => '{false, true}');


# Der auf der Kommandozeile angegebene Name der Klasse.
$klassenname = $ARGV[0];

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


# �berpr�fen, ob ein Klassenname angegeben wurde.
if ($#ARGV != 0) {
  print "Bitte Name der Klasse angeben.\n";
  exit(-1);
}

while (<STDIN>) {

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
      foreach my $name (@neueAttribNamen) {
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
    @werte = split(/[\s]*,[\s]*/);

    # Die Liste f�r die Werte der neuen Attribute.
    my @neueWerte;

    ###
    # Beginn der Berechnung der Werte der neuen Attribute.
    ###

    if ($werte[35] eq "$klassenname" ) {
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

