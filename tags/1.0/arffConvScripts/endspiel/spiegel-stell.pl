#!/usr/bin/perl

# Dateiname      : spiegel-stell.pl
# Letzte Änderung: 18. März 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm konvertiert eine Endspiel-Datei, indem es alle Stellungen, in
# denen einer der Steine (in der Regel ein Bauer) entweder auf der linken
# oder auf der rechten Bretthälfte steht, an der Mittellinie spiegelt. Welche
# Stellungen gespiegelt werden sollen, ist durch die Parameter $steinnr und
# $linkehaelfte anzugeben.
#
# Die Ausgangsdatei ist für ein Bauern-Endspiel durch folgenden Aufruf zu
# erzeugen:
# java egtb/Conversion -black -legal -nocaptures -noconversions -nodraws KPK.lpd


# Konfig-Parameter:

# Die Anzahl der Steine auf dem Brett.
my $steinanz = 3;

# Die Nummer des Steins, der immer entweder auf der linken oder auf der
# rechten Bretthälfte stehen soll.
my $steinnr = 2;

# Gibt an, ob der zweite Stein nach der Konvertierung immer in der linken
# Bretthälft stehen soll. Falls er auf der rechten Bretthälfte stehen soll,
# ist die Variablen mit dem Wert Null zu initialisieren.
my $linkehaelfte = 1;


# Gibt an, ob als nächstes eine Zeile des Datenbereichs eingelesen wird.
my $datenbereich = 0;

while (<>) {

  if (!$datenbereich) {

    # Header-Zeile ausgeben
    print;

    if (/^\@data/) {
      $datenbereich = 1;
    }

  } else {

    my $eingabezeile = $_;
    chomp;
    @werte = split(/[ ]*,[ ]*/);

    # Testen, ob die Stellung zu spiegeln ist.
    if ($linkehaelfte && ($werte[2 * $steinnr - 2] > 4)
        || !$linkehaelfte && ($werte[2 * $steinnr - 2] <= 4)) {
      # Stellung spiegeln.
      my $ausgabe = "";
      my $attributAnz = scalar(@werte);

      # Die Werte der ersten Attribute spiegeln.
      for (my $nr = 0; $nr < 2 * $steinanz; $nr++) {
        if ($nr % 2 == 0) {
          $ausgabe .= (9 - $werte[$nr]) . ",";
        } else {
          $ausgabe .= $werte[$nr] . ",";
        }
      }

      # Die weiteren Werte unverändert ausgeben.
      for (my $nr = 2 * $steinanz; $nr < $attributAnz; $nr++) {
        $ausgabe .= $werte[$nr] . ",";
      }

      chop $ausgabe;
      print $ausgabe . "\n";
    } else {
      # Stellung unverändert ausgeben.
      print $eingabezeile;
    }
  }
}

