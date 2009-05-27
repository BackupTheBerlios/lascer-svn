#!/usr/bin/perl

# Dateiname      : gendatKonvDaten.pl
# Letzte Änderung: 24. März 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm konvertiert die Überschriften und die Daten der zusätzlichen Tests
# in einer gendat-Datei. Dabei wird der vorhanende Test-Signal-Wert und der
# ration-Wert entfernt und es wird der Signal-Mittelwert, der log2-Wert von
# ration und der relative log2-Wert von ration aufgenommen. Die vorhandene
# gendat-Datei wird über STDIN erwartet und die konvertierte Datei über
# STDOUT ausgegeben.
#
# Aufruf: gendatKonvDaten.pl spaltennummer medianBreite
#
# "spaltennummer" ist die Nummer der ersten Splate eines zusätzlichen Tests.
# "medianBreite" ist die Anzahl der Werte, die für die Berechnung des Medians
# der kleinsten und größten ration-Werte verwendet werden soll.

use strict;

# Konstanten zur Konfiguration:

# Die Ergänzung für den Namen der Spalte eines Tests mit dem Mittelwert der
# Signal-Werte aus Tests und Kontrolle. 
my $signalMwErg = "-SignalMw";

# Die Ergänzung für den Namen der Spalte eines Tests mit dem log2-Ration-Wert.
my $log2RationErgaenz = "-log2-Ration";

# Die Ergänzung für den Namen der Spalte eines Tests mit dem relativen
# log2-Ration-Wert.
my $relLog2RationErgaenz = "-rlog2-Ration";

# Die Anzahl der Stellen, auf die gerundet werden soll.
my $stellenAnz = 3;


if (@ARGV != 2) {
  die "Aufruf: gendatKonvDaten.pl spaltennummer medianBreite\n";
}

# Die Nummer der Spalte, ab der die Tests beginnen.
my $ersteTestSpalte = $ARGV[0];

# Die die Anzahl der Werte, die für die Berechnung des Medians der kleinsten
# und größten ration-Werte verwendet werden soll.
my $medianBreite = $ARGV[1];

# Die Nummer der aktuell eingelesenen Zeile.
my $zeilenNr = 0;

# Der konstante Wert für den Logarithmus von 2.
my $log2 = log(2.0);

# Der Faktor, der beim Runden verwendet wird.
my $rundenFaktor = int(exp($stellenAnz * log(10)) + 0.5);

while (<STDIN>) {

  if (!/^[\s]*$/) {
    chomp;
    $zeilenNr++;
    my @werte = split(/[ ]*\t[ ]*/);
    my $spaltenAnz = scalar(@werte);

    if ($zeilenNr == 1) {

      # Die Namen der Spalten ausgeben.
      my @namen;
      my $nameNr = 0;

      # Die Namen der ersten Spalten übernehmen.
      for (my $sNr = 0; $sNr < $ersteTestSpalte; $sNr++) {
        $namen[$nameNr] = $werte[$sNr];
        $nameNr++;
      }

      # Die Namen der Test-Spalten erzeugen.
      for (my $sNr = $ersteTestSpalte; $sNr < $spaltenAnz; $sNr += 2) {
        # Geklammerten Text ersetzen.
        $werte[$sNr] =~ s/[\s]*\([^\)]*\)//g;

        # Leerzeichen und Kommata im Namen ersetzen.
        $werte[$sNr] =~ s/[ ,]/_/g;

        $namen[$nameNr] = $werte[$sNr] . $signalMwErg;
        $nameNr++;
        $namen[$nameNr] = $werte[$sNr] . $log2RationErgaenz;
        $nameNr++;
        $namen[$nameNr] = $werte[$sNr] . $relLog2RationErgaenz;
        $nameNr++;
      }

      print join("\t", @namen) . "\n";

    } else {

      # Die Daten der Spalten einer Zeile ausgeben.
      my $wertNr = 0;
      my @daten;

      my @signalWerte;
      my @rationWerte;
      my @sigMittelWerte;
      my @log2RationWerte;
      my @relLog2RationWerte;

      # Die Daten der ersten Spalten übernehmen.
      for (my $sNr = 0; $sNr < $ersteTestSpalte; $sNr++) {
        $daten[$wertNr] = $werte[$sNr];
        $wertNr++;
      }

      # Die Werte der weiteren Spalten nach Typ einlesen.
      # Die Werte der weiteren Spalten nach Typ einlesen.
      for (my $sNr = $ersteTestSpalte; $sNr < $spaltenAnz; $sNr += 2) {
        my $testNr = ($sNr - $ersteTestSpalte) / 2;

        if ($werte[$sNr + 1] ne 'n/a') {
          $signalWerte[$testNr] = $werte[$sNr];
          $rationWerte[$testNr] = $werte[$sNr + 1];
        }
      }

      my $minWert = median(minima(@rationWerte));
      my $minLog2 = log($minWert) / $log2;

      my $maxWert = median(maxima(@rationWerte));
      my $maxLog2 = log($maxWert) / $log2;

      # Die neuen Werte für die Tests errechnen.
      for (my $sNr = $ersteTestSpalte; $sNr < $spaltenAnz; $sNr += 2) {
        my $testNr = ($sNr - $ersteTestSpalte) / 2;

        if ($werte[$sNr + 1] eq 'n/a') {

          $sigMittelWerte[$testNr] = '?';
          $log2RationWerte[$testNr] = '?';
          $relLog2RationWerte[$testNr] = '?';

        } else {

          my $signalWert = $werte[$sNr];
          my $rationWert = $werte[$sNr + 1];

          my $sMW = sigMittelwert($signalWert, $rationWert);
          $sigMittelWerte[$testNr] = runden($sMW);

          my $lRW = log($rationWert) / $log2;
          $log2RationWerte[$testNr] = runden($lRW);

          my $rLRW;
          if ($log2RationWerte[$testNr] > 0) {
            $rLRW = $log2RationWerte[$testNr] / abs($maxLog2);
          } elsif ($log2RationWerte[$testNr] == 0) {
            $rLRW = 0;
          } else {
            $rLRW = $log2RationWerte[$testNr] / abs($minLog2);
          }
          $relLog2RationWerte[$testNr] = runden($rLRW);

        }
      }

      for (my $tNr = 0; $tNr < scalar(@sigMittelWerte); $tNr++) {
        $daten[$wertNr] = $sigMittelWerte[$tNr];
        $wertNr++;

        $daten[$wertNr] = $log2RationWerte[$tNr];
        $wertNr++;

        $daten[$wertNr] = $relLog2RationWerte[$tNr];
        $wertNr++;
      }

      print join("\t", @daten) . "\n";

    }
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

sub minima {
  my @werte = @_;
  my @minWerte;

  my @werte = sort({$a <=> $b} @werte);

  for (my $wNr = 0; $wNr < $medianBreite; $wNr++) {
    $minWerte[$wNr] = $werte[$wNr];
  }
  return @minWerte;
}

sub maxima {
  my @werte = @_;
  my @maxWerte;

  my @werte = sort({$a <=> $b} @werte);

  for (my $wNr = 0; $wNr < $medianBreite; $wNr++) {
    $maxWerte[$wNr] = $werte[scalar(@werte) - $medianBreite + $wNr];
  }

  return @maxWerte;
}

sub median {
  my @werte = @_;
  my $wertAnz = scalar(@werte);

  my @werte = sort({$a <=> $b} @werte);

  if ($wertAnz % 2 == 1) {
    # Es gibt eine ungerade Anzahl von Werten.
    return $werte[($wertAnz - 1) / 2];
  } else {
    # Es gibt eine gerade Anzahl von Werten.
    my $klWert = $werte[$wertAnz / 2 - 1];
    my $grWert = $werte[$wertAnz / 2];
    return ($klWert + $grWert) / 2;
  }
}

