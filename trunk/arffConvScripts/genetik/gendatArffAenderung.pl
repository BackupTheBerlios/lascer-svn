#!/usr/bin/perl

# Dateiname      : gendatArffAenderung.pl
# Letzte Änderung: 24. März 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm entfernt aus einer arff-Datei, die aus einer gendat-Datei erzeugt
# wurde, einige unnötige Attribute und ergänzt das Attribut 'class'. Die
# Werte für das Attribut class werden entsprechend dem anzugebenden
# Schwellwert ermittelt. Die vorhandene arff-Datei wird über STDIN eingelesen
# und die geänderte arff-Datei über STDOUT ausgegeben.
#
# Aufruf: cat arff-Datei | ./attribAenderung.pl schwellwert
#
# "schwellwert" ist der Wert für das Attribut 'Wert', ab dem ein Beispiel als
# positives Beispiel klassifiziert wird.


use strict;

# Konfig-Parameter:

# Eine Liste der Namen der Attribute, die entfernt werden sollen.
my @entfernenAttribute = ('Wert', 'Signal1', 'Ratio1', 'Signal2', 'Ratio2');

# Eine Liste mit den Namen neuer Attribute. Die Reihenfolge muß der
# Reihenfolge der unten neu errechneten Werte entsprechen.
my @neueAttribNamen = ('Signal1-Mw', 'Signal2-Mw', 'class');

# Eine Hash-Liste mit den Namen und Wertebereichen neuer Attribute.
my %neueAttribute = ('Signal1-Mw' => 'real', 'Signal2-Mw' => 'real',
                     'class' => '{false, true}');

# Die Anzahl der Stellen, auf die gerundet werden soll.
my $stellenAnz = 3;


if (@ARGV != 1) {
    die "Aufruf: cat arff-Datei | ./attribAenderung.pl schwellwert\n";
}

# Der für die Klassifikation zu verwendende Schwellwert.
my $schwellwert = $ARGV[0];

# Der Faktor, der beim Runden verwendet wird.
my $rundenFaktor = int(exp($stellenAnz * log(10)) + 0.5);

# Gibt die Anzahl schon eingelesener Attribute an.
my $attributeGelesen = 0;

# Gibt die Anzahl der vorhandenen Attuibute an.
my $attributAnz = -1;

# Gibt zu jeder Attributnummer an, ob das Attribut entfernt werden soll.
my @entfernen;

# Gibt zu dem Namen jedes Attributs dessen Nummer an.
my %attribNummern;

while (<STDIN>) {

  if (/^[\s]*$/) {

    # Leerzeile unverändert ausgeben.
    print $_;

  } elsif (/^\%/) {

    # Kommentarzeile unverändert ausgeben.
    print $_;

  } elsif (/^\@attribute/i) {

    my $eingabezeile = $_;

    # Den Namen des Attributs ermitteln.
    /^\@attribute[\s]*(\S+)/i;

    # Zum Namen die Nummer speichern.
    $attribNummern{$1} = $attributeGelesen;

    # Prüfen, ob das Attribut und dessen Wert entfernt werden soll.
    if (scalar(grep(/$1/, @entfernenAttribute)) > 0) {
      $entfernen[$attributeGelesen] = 1;
    } else {
      $entfernen[$attributeGelesen] = 0;
    }

    if (!$entfernen[$attributeGelesen]) {
      # Zeile mit Attribut ausgeben.
      print $eingabezeile;
    }

    $attributeGelesen++;

  } elsif (/^\@data/) {

    # Beginn des Datenbereichs.
    my $eingabezeile = $_;

    $attributAnz = $attributeGelesen;

    # Neue Attribute ausgeben.
    foreach my $name (@neueAttribNamen) {
      print "\@attribute " . $name . " " . $neueAttribute{$name} . "\n";
    }

    # Die eingelesene Zeile ausgeben.
    print "\n";
    print $eingabezeile;

  } elsif (/^\@/) {

    # Header-Zeile unverändert ausgeben.
    print $_;

  } else {

    # Datenzeile.
    chomp;
    my @werte = split(/[\s]*,[\s]*/);

    if (scalar(@werte) != $attributAnz) {
      die "Die Anzahl der Attributwerte stimmt mit der Anzahl der Attribute\n"
          . " nicht überein:\n" . $_ . "\n";
    }

    # Die Liste für die Werte der neuen Attribute.
    my @neueWerte;

    ###
    # Beginn der Berechnung der Werte der neuen Attribute.
    ###

    my $signalWert1 = $werte[$attribNummern{'Signal1'}];
    my $rationWert1 = $werte[$attribNummern{'Ratio1'}];

    my $signalWert2 = $werte[$attribNummern{'Signal2'}];
    my $rationWert2 = $werte[$attribNummern{'Ratio2'}];

    my $sigMittelwert1 = sigMittelwert($signalWert1, $rationWert1);
    push(@neueWerte, (runden($sigMittelwert1)));

    my $sigMittelwert2 = sigMittelwert($signalWert2, $rationWert2);
    push(@neueWerte, (runden($sigMittelwert2)));

    if ($werte[$attribNummern{'Wert'}] >= $schwellwert) {
      push(@neueWerte, ("true"));
    } else {
      push(@neueWerte, ("false"));
    }

    ###
    # Ende der Berechnung der Werte der neuen Attribute.
    ###

    # Die nächste Ausgabezeile.
    my $ausgabe = "";

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


sub sigMittelwert {
  my $signalWert = shift;
  my $rationWert = shift;

  return ($signalWert * ($rationWert + 1.0) / (2.0 * $rationWert));
}

sub runden {
  my $wert = shift;

  return (int($wert * $rundenFaktor + 0.5) / $rundenFaktor);
}

