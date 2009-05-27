#!/usr/bin/perl

# Dateiname      : formelStatistik.pl
# Letzte �nderung: 30. Mai 2006 durch Dietmar Lippold 
# Autor          : Dietmar Lippold 
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Programm ermittelt eine Statistik f�r eine im Unterprogramm anzugebende
# Formel in Bezug auf eine arff-Datei. Dabei wird die Anzahl der richtig und
# falsch klassifizierten positiven und negativen Beispiele ermittelt. Die
# arff-Datei ist �ber STDIN anzugeben.
#
# Aufruf: cat arff-datei | ./formelStatistik.pl

use strict;

# Keine Konfig-Parameter


# Beginn des Programms.

# Gibt an, ob als n�chste eine Zeile des Datenbereichs eingelesen wird.
my $datenbereich = 0;

# Die Nummer des als n�chsten Attributs. Das erste hat die Nummer Null.
my $naechsteAttribNr = 0;

# Zu dem Namen jedes Attributs dessen Nummer.
my %attribNummern;

# Die Anzahl der entsprechenden Beispiele.
my $richtigPositiv = 0;
my $falschPositiv = 0;
my $richtigNegativ = 0;
my $falschNegativ = 0;

while (<>) {

  if (!$datenbereich) {

    if (/^\@attribute/i) {

      # Zum Namen die Nummer speichern.
      /^\@attribute[\s]+(\S*)/i;
      $attribNummern{$1} = $naechsteAttribNr;
      $naechsteAttribNr++;

    }

    if (/^\@data/i) {
      if ($attribNummern{"class"} eq "") {
        die "Kein class Attribut vorhanden\n";
      }

      $datenbereich = 1;
    }

  } else {

    # Leerzeilen und Kommentarzeilen weglassen.
    if ((!/^[\s]*$/) && (!/^\%/)) {

      # Zeile aus dem Datenbereich.
      chomp;

      # Die Werte der Attribute ermitteln.
      my @werte = split(/[\s]*,[\s]*/);

      # Die Klassifikation des neuen Beispiels ermitteln.
      my $positiv = (lc($werte[$attribNummern{"class"}]) eq "true");

      # Ermitteln, ob die Formel f�r das Beispiel erf�llt ist.
      my $formelTrifftZu = formelTrifftZu(@werte);

      if ($positiv && $formelTrifftZu) {
        $richtigPositiv++;
      } elsif (!$positiv && $formelTrifftZu) {
        $falschPositiv++;
      } elsif ($positiv && !$formelTrifftZu) {
        $falschNegativ++;
      } else {
        $richtigNegativ++;
      }

    }
  }
}

print "Statistik:\n";
print "Anz. richtiger pos. Beispiel  : $richtigPositiv\n";
print "Anz. f�lschlich pos. Beispiel : $falschPositiv\n";
print "Anz. richtiger neg. Beispiel  : $richtigNegativ\n";
print "Anz. f�lschlich neg. Beispiel : $falschNegativ\n";

my $anzFalsch = $falschPositiv + $falschNegativ;
my $anzAlle = $anzFalsch + $richtigPositiv + $richtigNegativ;
print "Fehleranteil in Prozent       : " . (100.0 * $anzFalsch / $anzAlle);
print "\n";

my $posBspPrecision = $richtigPositiv / ($richtigPositiv + $falschPositiv);
my $posBspRecall = $richtigPositiv / ($richtigPositiv + $falschNegativ);
my $posBspFMeasure = 2 * $posBspPrecision * $posBspRecall
                       / ($posBspPrecision + $posBspRecall);
print "precision f�r pos. Beispiele  : $posBspPrecision\n"; 
print "recall f�r pos. Beispiele     : $posBspRecall\n";
print "f-measure f�r pos. Beispiele  : $posBspFMeasure\n";

my $negBspPrecision = $richtigNegativ / ($richtigNegativ + $falschNegativ);
my $negBspRecall = $richtigNegativ / ($richtigNegativ + $falschPositiv);
my $negBspFMeasure = 2 * $negBspPrecision * $negBspRecall
                       / ($negBspPrecision + $negBspRecall);
print "precision f�r neg. Beispiele  : $negBspPrecision\n"; 
print "recall f�r neg. Beispiele     : $negBspRecall\n";
print "f-measure f�r neg. Beispiele  : $negBspFMeasure\n";


sub formelTrifftZu {
  my @werte = @_;

  return 1;
}

