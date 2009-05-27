#!/usr/bin/perl

# Dateiname      : attribAenderung.pl
# Letzte �nderung: 02. Juli 2007 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Programm entfernt aus einer arff-Datei vorgegebene Attribute und f�gt neue
# vorgegebene Attribute hinzu. Die Werte f�r die neuen Attribute m�ssen unten
# im markierten Bereich ermittelt werden. Die vorhandene arff-Datei wird �ber
# STDIN eingelesen und die ge�nderte arff-Datei �ber STDOUT ausgegeben.
#
# HINWEIS: Die Namen der Attribute in der arff-Datei d�rfen nicht in
#          Anf�hrungszeichen (Quotes) stehen.
#
# Aufruf: cat arff-Datei | ./attribAenderung.pl


use strict;

# Konfig-Parameter:

# Eine Liste der Namen der Attribute, die entfernt werden sollen.
my @entfernenAttribute = ('attribName1');

# Eine Liste mit den Namen neuer Attribute.
my @neueAttribNamen = ('attribName2', 'class');

# Eine Hash-Liste mit den Namen und Wertebereichen neuer Attribute.
my %neueAttribute = ('attribName2' => 'integer',
                     'class' => '{false, true}');


# Gibt die Anzahl schon eingelesener Attribute an.
my $attributeGelesen = 0;

# Gibt die Anzahl der zu entfernenden Attuibute an.
my $attributAnzEntfern = 0;

# Gibt die Anzahl der vorhandenen Attuibute an.
my $attributAnzAlt = -1;

# Gibt die Anzahl der neuen Attuibute an.
my $attributAnzNeu = -1;

# Gibt zu jeder Attributnummer an, ob das Attribut entfernt werden soll.
my @entfernen;

# Gibt zu dem Namen jedes Attributs dessen Nummer an.
my %attribNummern;

while (<>) {

  if (/^[\s]*$/) {

    # Leerzeile unver�ndert ausgeben.
    print $_;

  } elsif (/^\%/) {

    # Kommentarzeile unver�ndert ausgeben.
    print $_;

  } elsif (/^\@attribute/i) {

    my $eingabezeile = $_;

    # Den Namen des Attributs ermitteln.
    /^\@attribute[\s]*(\S+)/i;

    # Zum Namen die Nummer speichern.
    $attribNummern{$1} = $attributeGelesen;

    # Pr�fen, ob das Attribut und dessen Wert entfernt werden soll.
    if (scalar(grep {$_ eq $1} @entfernenAttribute) > 0) {
      $entfernen[$attributeGelesen] = 1;
      $attributAnzEntfern++;
    } else {
      $entfernen[$attributeGelesen] = 0;
    }

    if (!$entfernen[$attributeGelesen]) {
      # Zeile mit Attribut ausgeben.
      print $eingabezeile;
    }

    $attributeGelesen++;

  } elsif (/^\@data/i) {

    # Beginn des Datenbereichs.

    my $eingabezeile = $_;

    $attributAnzAlt = $attributeGelesen;
    $attributAnzNeu = $attributeGelesen - $attributAnzEntfern
                                        + scalar(@neueAttribNamen);

    # Neue Attribute ausgeben.
    foreach my $name (@neueAttribNamen) {
      print "\@attribute " . $name . " " . $neueAttribute{$name} . "\n";
    }

    # Die eingelesene Zeile ausgeben.
    print "\n";
    print $eingabezeile;

  } elsif (/^\@/) {

    # Header-Zeile unver�ndert ausgeben.
    print $_;

  } else {

    # Datenzeile.
    chomp;
    my @werte = split(/[\s]*,[\s]*/);

    if (scalar(@werte) != $attributAnzAlt) {
      die "\n" . "Fehler: Die Anzahl der gelesenen Attributwerte stimmt"
          . " mit der Anzahl der\n"
          . " vorhandenen Attribute nicht �berein:\n" . $_ . "\n";
    }

    # Die Liste f�r die Werte der neuen Attribute.
    my @neueWerte;

    ###
    # Beginn der Berechnung der Werte der neuen Attribute.
    ###

    push(@neueWerte, ($werte[$attribNummern{'attribName1'}] + 1));

    if ($werte[$attribNummern{'attribName1'}] > 0) {
      push(@neueWerte, ("true"));
    } else {
      push(@neueWerte, ("false"));
    }

    ###
    # Ende der Berechnung der Werte der neuen Attribute.
    ###

    # Die n�chste Ausgabezeile erzeugen.
    my $ausgabe = "";
    my $ausgabeAttribAnz = 0;

    # Vorhandene, nicht zu entfernende Werte an die Ausgabe anh�ngen.
    for (my $nr = 0; $nr < $attributAnzAlt; $nr++) {
      if (!$entfernen[$nr]) {
        $ausgabe .= $werte[$nr] . ",";
        $ausgabeAttribAnz++;
      }
    }

    # Neue Werte an die Ausgabe anh�ngen.
    foreach my $wert (@neueWerte) {
      $ausgabe .= $wert . ",";
      $ausgabeAttribAnz++;
    }

    chop $ausgabe;
    print $ausgabe . "\n";

    if (($ausgabeAttribAnz != $attributAnzNeu)
        || (scalar(@neueWerte) != scalar(@neueAttribNamen))) {
      die "\n" . "Fehler: Die Anzahl der neuen Attributwerte stimmt"
          . " mit der Anzahl der\n"
          . " neuen Attribute nicht �berein.\n";
    }
  }
}

