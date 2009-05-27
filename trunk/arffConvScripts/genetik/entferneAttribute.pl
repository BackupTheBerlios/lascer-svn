#!/usr/bin/perl

# Dateiname      : entferneAttribute.pl
# Letzte Änderung: 28. März 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm entfernt aus einer arff-Datei alle unrelevanten Attribute. Welche
# Attribute relevant sind, ist unter zu anzugeben. Die vorhandene arff-Datei
# wird über STDIN eingelesen und die geänderte arff-Datei über STDOUT
# ausgegeben.
#
# Aufruf: cat arff-Datei | ./entferneAttribute.pl [vnr] [zsm] [fk]
#
# "vnr" ist anzugeben, wenn die vorgegebenen nicht relevanten Attribute
# entfernt werden sollen. "zsm" ist anzugeben, wenn die Attribute zu den
# Signal-Mittelwerten entfernt werden sollen. "fk" ist anzugeben, wenn das
# Attribute "Func" zur Funktionsklasse entfernt werden soll.
# Mindestens eine der beiden Angaben ist erforderlich.

use strict;

# Konfig-Parameter:

# Eine Liste der Namen der relevanten Attribute, die nicht entfernt werden
# sollen.
my @relevanteAttribute = ('class', 'Signal1', 'Signal2',
                          'Signal1-Mw', 'Signal2-Mw', 'Probeset', 'GeneID',
                          'FV', 'Func', 'log2_1', 'tval1', 'pcmin1',
                          'log2_2', 'tval2', 'pcmin2',
                          'Biotic: A. brassiciola (+)',
                          'Biotic: A. tumefaciens (+)',
                          'Biotic: B. cinerea (+)',
                          'Biotic: E. cichoracearum (+)',
                          'Biotic: E. orontii (+)',
                          'Biotic: F. occidentalis (+)',
                          'Biotic: M. persicae 1 (+)',
                          'Biotic: M. persicae 2 (+)',
                          'Biotic: mycorrhiza (+)',
                          'Biotic: nematode (+)',
                          'Biotic: P. infestans (+)',
                          'Biotic: P. rapae (+)',
                          'Biotic: P. syringae 1 (+)',
                          'Biotic: P. syringae 2 (+)',
                          'Chemical: chitin (+)',
                          'Chemical: hydrogen peroxide (+)',
                          'Chemical: ibuprofen (+)',
                          'Chemical: ozone (+)',
                          'Chemical: syringolin (+)',
                          'Hormone: ethylene (+)',
                          'Hormone: MJ (+)',
                          'Hormone: salicylic acid (+)',
                          'Stress: oxidative',
                          'Stress: wounding');

# Eine Liste der Strings, um die die ursprünglichen Namen der Attribute der
# arff-Datei ergänzt wurden.
my @namensErgaenzungen = ("-SignalMw", "-log2-Ration", "-rlog2-Ration");

# Die Ergänzung der Namen der Attribute der zusätzlichen Tests zum
# Signal-Mittelwert.
my $zusatzMwErgaenz = "Mw";

# Der Name des Attributs der Funktionsklasse.
my $funcName = "Func";


# Verarbeitung unabhängig von der arff-Datei:

if ((@ARGV == 0) || (@ARGV > 3)
    || ($ARGV[0] ne "vnr") && ($ARGV[0] ne "zsm") && ($ARGV[0] ne "fk")
    || (@ARGV >= 2) && ($ARGV[1] ne "vnr") && ($ARGV[1] ne "zsm")
                                           && ($ARGV[1] ne "fk")
    || (@ARGV == 3) && ($ARGV[2] ne "vnr") && ($ARGV[2] ne "zsm")
                                           && ($ARGV[2] ne "fk")) {

    die "Aufruf: cat arff-Datei | ./entferneAttribute.pl [vnr] [zsm] [fk]\n";
}

# Gibt an, ob die nicht relevanten Attribute entfernt werden sollen.
my $entferneNichtRelev = 0;

# Gibt an, ob die Attribute der zusätzlichen Test zu den Signal-Mittelwerten
# entfernt werden sollen.
my $entferneZusatzMw = 0;

# Gibt an, ob das Attribut zur Funktionsklasse entfernt werden sollen.
my $entferneFunc = 0;

if (@ARGV == 1) {
    $entferneNichtRelev = ($ARGV[0] eq "vnr");
    $entferneZusatzMw = ($ARGV[0] eq "zsm");
    $entferneFunc = ($ARGV[0] eq "fk");
} elsif (@ARGV == 2) {
    $entferneNichtRelev = (($ARGV[0] eq "vnr") || ($ARGV[1] eq "vnr"));
    $entferneZusatzMw = (($ARGV[0] eq "zsm") || ($ARGV[1] eq "zsm"));
    $entferneFunc = (($ARGV[0] eq "fk") || ($ARGV[1] eq "fk"));
} else {
    $entferneNichtRelev = (($ARGV[0] eq "vnr") || ($ARGV[1] eq "vnr")
                                               || ($ARGV[2] eq "vnr"));
    $entferneZusatzMw = (($ARGV[0] eq "zsm") || ($ARGV[1] eq "zsm")
                                             || ($ARGV[2] eq "zsm"));
    $entferneFunc = (($ARGV[0] eq "fk") || ($ARGV[1] eq "fk")
                                        || ($ARGV[2] eq "fk"));
}

# Eine Liste der modifizierten Namen der relevanten Attribute.
my @modRelevAttribute;

# Die Namen der Attribute in der Weise modifizieren, wie sie auch für die
# arff-Datei modifiziert wurden.
foreach my $name (@relevanteAttribute) {

  # Geklammerten Text ersetzen.
  $name =~ s/[\s]*\([^\)]*\)//g;

  # Leerzeichen und Kommata im Namen ersetzen.
  $name =~ s/[ ,]/_/g;

  push(@modRelevAttribute, ($name));
}


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
    my $attribName = $1;

    if ($entferneZusatzMw && ($attribName =~ /$zusatzMwErgaenz$/)) {

      $entfernen[$attributeGelesen] = 1;

    } elsif ($entferneFunc && ($attribName eq $funcName)) {

      $entfernen[$attributeGelesen] = 1;

    } elsif ($entferneNichtRelev) {

      # Den Namen des Attributs auf den Stamm reduzieren.
      foreach my $ergaenzung (@namensErgaenzungen) {
        $attribName =~ s/$ergaenzung$//;
      }

      # Prüfen, ob das Attribut und dessen Wert entfernt werden soll.
      if (scalar(grep(/$attribName/, @modRelevAttribute)) > 0) {
        $entfernen[$attributeGelesen] = 0;
      } else {
        $entfernen[$attributeGelesen] = 1;
      }

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

    print $eingabezeile;

  } elsif (/^\@/) {

    # Andere Header-Zeile unverändert ausgeben.
    print $_;

  } else {

    # Datenzeile.
    chomp;
    my @werte = split(/[\s]*,[\s]*/);

    if (scalar(@werte) != $attributAnz) {
      die "Die Anzahl der Attributwerte stimmt mit der Anzahl der Attribute\n"
          . " nicht überein:\n" . $_ . "\n";
    }

    # Die nächste Ausgabezeile.
    my $ausgabe = "";

    # Vorhandene, nicht zu entfernende Werte an die Ausgabe anhängen.
    for (my $nr = 0; $nr < $attributAnz; $nr++) {
      if (!$entfernen[$nr]) {
        $ausgabe .= $werte[$nr] . ",";
      }
    }

    chop $ausgabe;
    print $ausgabe . "\n";

  }
}

