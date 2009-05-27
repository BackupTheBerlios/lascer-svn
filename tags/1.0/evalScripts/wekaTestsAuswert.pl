#!/usr/bin/perl

# Dateiname      : wekaTestsAuswert.pl
# Letzte Änderung: 05. Januar 2009 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm wertet mehrere Dateien aus, die mit einem eval-Script für
# Test-Dateien für Weka erzeugt wurden. Es gibt ein LaTeX-Dokument aus, in
# dem Tabellen mit errechneten Werten zu den Tests enthalten sind.
#
# Aufruf: cat datei1 [datei2 ...] | ./wekaTestsAuswert.pl


use strict;

# Parameter zur Konfiguration.

# Die Anzahl der Nachkommastellen, die ausgegeben werden sollen.
my $stellenAnz = 1;


# Gibt an, ob die aktuelle Zeile zum Bereich der Ausgabe vom cross-validation
# gehört.
my $validationParsing = 0;

# Gibt an, ob die aktuelle Zeile zum Bereich der Confusion Matrix der
# Ausgabe vom cross-validation gehört.
my $matrixParsing = 0;

# Das verwendete Klassifikationsverfahren.
my $verfahren = "";

# Die Tabellen zu den Dateien.
my %tabellen = ();

# Der Name der eingelesenen Datei.
my $datei = "";

# Die Anzahl der richtig klassifizierten negativen Beispiele.
my $negRichtigAnz = -1;

# Die Anzahl der faslch klassifizierten negativen Beispiele.
my $negFalschAnz = -1;

# Die Anzahl der falsch klassifizierten positiven Beispiele.
my $posFalschAnz = -1;

# Die Anzahl der richtig klassifizierten positiven Beispiele.
my $posRichtigAnz = -1;

# Der Faktor, der beim Runden verwendet wird.
my $rundenFaktor = int(exp($stellenAnz * log(10)) + 0.5);

# Parsen der Werte.
while (<>) {

  # Die eingelesene Zeile vergleichen.
  if (/Verfahren: (.*)/) {

    $verfahren = $1;

  } elsif (/Name: (.*)/) {

    $datei = $1;

  } elsif ((/Stratified cross-validation/) || (/Error on test data/)) {

    $validationParsing = 1;

  } elsif ($validationParsing && (/Confusion Matrix/)) {

    $matrixParsing = 1;

  } elsif (/-----/) {

    if ($datei ne "") {
      my $tabelle = $tabellen{$datei};
      $tabelle .= tabellenZeile();
      $tabellen{$datei} = $tabelle;
#      datenAusgeben();
    }

    $datei = "";
    $matrixParsing = 0;
    $validationParsing = 0;
    $negRichtigAnz = -1;

  } elsif ($matrixParsing) {

    if (/(\d+) +(\d+) +\|/) {
      if ($negRichtigAnz < 0) {
        # Die erste Zeile der Matrix wurde noch nicht eingelesen.
        $negRichtigAnz = $1;
        $negFalschAnz = $2;
      } else {
        # Die erste Zeile der Matrix wurde schon eingelesen.
        $posFalschAnz = $1;
        $posRichtigAnz = $2;
      }
    }
  }
}

# Ausgeben vom Dokument-Anfang.
printFileHeader();

# Ausgeben der Tabellen.
foreach my $tabDatei (sort(keys(%tabellen))) {
  my $tabelle = $tabellen{$tabDatei};

  my $problem = $tabDatei;
  $problem =~ s/\.arff//;
  $problem =~ s/_/\\_/g;
  printTableHeader($problem);
  print $tabelle;
  printTableFooter($problem);
  print "\n";
}

# Ausgeben vom Dokument-Ende.
printFileFooter();


# Funktion gibt folgende Werte aus:
#  + Name der Testdatei
#  + np-Fehler-Anteil
#  + pn-Fehler-Anteil
#  + arithm. Mittelwert der Fehler-Anteile
#  + gesamt-Fehler-Anteil
#  + pos. precision
#  + pos. recall
#  + pos. f-score
sub datenAusgeben {
  my $negAnz = $negRichtigAnz + $negFalschAnz;
  my $posAnz = $posFalschAnz + $posRichtigAnz;
  my $gesAnz = $negAnz + $posAnz;
  my $npFehlerAnteil = $negFalschAnz / $negAnz;
  my $pnFehlerAnteil = $posFalschAnz / $posAnz;
  my $gesFehlerAnteil = ($negFalschAnz + $posFalschAnz) / $gesAnz;
  my $posRecall = $posRichtigAnz / $posAnz;

  my $posPrecision;
  if ($posRichtigAnz + $negFalschAnz == 0) {
    $posPrecision = 1;
  } else {
    $posPrecision = $posRichtigAnz / ($posRichtigAnz + $negFalschAnz);
  }

  my $posFScore;
  if ($posPrecision + $posRecall == 0) {
    $posFScore = 0;
  } else {
    $posFScore = 2 * $posPrecision * $posRecall / ($posPrecision + $posRecall);
  }

  print "$datei : ";
  print runden($npFehlerAnteil * 100) . ", ";
  print runden($pnFehlerAnteil * 100) . ", ";
  print runden(($npFehlerAnteil + $pnFehlerAnteil) * 50) . ", ";
  print runden($gesFehlerAnteil * 100) . ", ";
  print runden($posPrecision * 100) . ", ";
  print runden($posRecall * 100) . ", ";
  print runden($posFScore * 100) . ", ";
  print "\n";
}

# Funktion liefert eine Zeile für eine LaTeX-Tabellen mit folgenden Werten:
#  + Name der Testdatei
#  + np-Fehler-Anteil
#  + pn-Fehler-Anteil
#  + arithm. Mittelwert der Fehler-Anteile
#  + gesamt-Fehler-Anteil
#  + pos. precision
#  + pos. recall
#  + pos. f-score
sub tabellenZeile {
  my $zeile = "";

  my $negAnz = $negRichtigAnz + $negFalschAnz;
  my $posAnz = $posFalschAnz + $posRichtigAnz;
  my $gesAnz = $negAnz + $posAnz;
  my $npFehlerAnteil = $negFalschAnz / $negAnz;
  my $pnFehlerAnteil = $posFalschAnz / $posAnz;
  my $gesFehlerAnteil = ($negFalschAnz + $posFalschAnz) / $gesAnz;
  my $posRecall = $posRichtigAnz / $posAnz;

  my $posPrecision;
  if ($posRichtigAnz + $negFalschAnz == 0) {
    $posPrecision = 1;
  } else {
    $posPrecision = $posRichtigAnz / ($posRichtigAnz + $negFalschAnz);
  }

  my $posFScore;
  if ($posPrecision + $posRecall == 0) {
    $posFScore = 0;
  } else {
    $posFScore = 2 * $posPrecision * $posRecall / ($posPrecision + $posRecall);
  }

  $zeile .= $verfahren . " \& ";
  $zeile =~ s/_/\\_/g;
  $zeile .= runden($npFehlerAnteil * 100) . " \& ";
  $zeile .= runden($pnFehlerAnteil * 100) . " \& ";
  $zeile .= runden(($npFehlerAnteil + $pnFehlerAnteil) * 50) . " \& ";
  $zeile .= runden($gesFehlerAnteil * 100) . " \& ";
  $zeile .= runden($posPrecision * 100) . " \& ";
  $zeile .= runden($posRecall * 100) . " \& ";
  $zeile .= runden($posFScore * 100) . " \\\\";
  $zeile .= "\n";

  return $zeile;
}

sub runden {
  my $wert = shift;

  my $gerundet =(int($wert * $rundenFaktor + 0.5) / $rundenFaktor);

  if (index($gerundet, ".") > -1) {
    return $gerundet;
  } else {
    return $gerundet . substr(".000000000", 0, $stellenAnz + 1);
  }
}

sub printFileHeader {

  print "\\documentclass[12pt, a4paper]{article}\n";
  print "\\usepackage{a4}\n";
  print "\\usepackage[german]{babel}\n";
  print "\\usepackage[latin1]{inputenc}\n";
  print "\\usepackage{parskip}\n";
  print "\n";
  print "\\frenchspacing\n";
  print "\n";
  print "\\title{Tests von Weka-Verfahren}\n";
  print "\\author{Dietmar Lippold}\n";
  print "\\date{\\today}\n";
  print "\n";
  print "\\begin{document}\n";
  print "\n";
  print "\\maketitle\n";
  print "\n";
  print "Abkürzungen:\n";
  print "\n";
  print "\\begin{tabular}{l\@{ : }l}\n";
  print "np--F.   & np--Fehler in Prozent\\\\ \n";
  print "pn--F.   & pn--Fehler in Prozent\\\\ \n";
  print "d.\ F.   & Durchschnitt, d.h.\ arithm.\ Mittel, der prozentualen Fehler\\\\ \n";
  print "ges.\ F. & Gesamt--Fehler in Prozent\\\\ \n";
  print "prec.    & Precision\\\\ \n";
  print "rec.     & Recall\\\\ \n";
  print "F--Sc.   & F--Score\\\\ \n";
  print "\\end{tabular}\n";
  print "\n";
}

sub printTableHeader {
  my $problem = shift;

  print "\\section{Tabelle $problem}\n";
  print "\n";
  print "\\begin{center}\n";
  print "\\begin{tabular}{l|r|r|r|r|r|r|r}\n";
  print " Verfahren & np--F. & pn--F. & d.\ F. & ges.\ F.";
  print " & pres. & rec. & F-Sc. \\\\ \n";
  print "\\hline\n";
}

sub printTableFooter {
  my $problem = shift;

  print "\\end{tabular}\n";
  print "\\end{center}\n";
}

sub printFileFooter {

  print "\\end{document}\n";
  print "\n";
}

