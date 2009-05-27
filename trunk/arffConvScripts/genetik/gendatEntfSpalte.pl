#!/usr/bin/perl

# Dateiname      : gendatEntfSpalte.pl
# Letzte Änderung: 19. März 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm entfernt aus eine gendat-Datei, die über STDIN eingelesen wird,
# eine Spalte und gibt die resultierende Datei auf STDOUT aus.
#
# Aufruf: gendatEntfSpalte.pl spaltennummer anfangs-spalten-anz
#   z.B.: gendatEntfSpalte.pl 4 15
#
# "spaltennummer" ist die Nummer der Splate, die entfernt werden soll. Ein
# negativer Wert wird ignoriert.
# "anfangs-spalten-anz" ist die Anzahl der Spalten am Anfang jeder Zeile,
# nach der jede zweite Spalte entfernt wird (ausgenommen die erste Zeile).
# Ein negativer Wert wird ignoriert.
# Die erste Spalte hat die Nummer Null.

# Es sind keine Konstanten zur Konfiguration vorhanden.


if (@ARGV != 2) {
    die "Aufruf: gendatEntfSpalte.pl spaltennummer anfangs-spalten-anz\n";
}

# Die zu entfernende Spalte.
my $entfSpalte = $ARGV[0];

# Die Nummer der Spalte, ab der jede zweite Spalte entfernt wird (ausgenommen
# die erste Zeile).
my $anfangsSpaltenAnz = $ARGV[1];

# Die Nummer der aktuell eingelesenen Zeile.
my $zeilenNr = 0;

while (<STDIN>) {

  chomp;
  $zeilenNr++;
  @werte = split(/[ ]*\t[ ]*/);

  my $wertAnz = scalar(@werte);
  my $ausgabe = "";
  for (my $nr = 0; $nr < $wertAnz; $nr++) {
    if (($nr != $entfSpalte)
        && (($zeilenNr == 1)
            || ($anfangsSpaltenAnz < 0)
            || ($nr < $anfangsSpaltenAnz)
            || (($nr - $anfangsSpaltenAnz) % 2 != 0))) {
      $ausgabe .= $werte[$nr] . "\t";
    }
  }

  chop $ausgabe;
  print $ausgabe . "\n";
}

