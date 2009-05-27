#!/usr/bin/perl

# Dateiname      : stellungsfilter.pl
# Letzte Änderung: 18. März 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm gibt nur Stellungen aus, auf die ein anzugebender Test zutrifft.
# Der Test ist unten im gekennzeichneten Bereich anzugeben.


# Keine Konfig-Parameter vorhanden.


# Gibt an, ob die Eingabezeile ausgegeben werden soll.
my $ausgeben = 0;

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

    # Zuweisung der Positionen der Steine an Variablen.
    my $whiteKingFile = $werte[0];
    my $whiteKingRank = $werte[1];
    my $whitePieceFile = $werte[2];
    my $whitePieceRank = $werte[3];
    my $blackKingFile = $werte[4];
    my $blackKingRank = $werte[5];

    ###
    # Beginn des Tests, ob die aktuelle Zeile ausgegeben werden soll.
    # Es ist die Variable $ausgeben entsprechend zu setzen.
    ###

    if (($blackKingFile == 1)
        && ($whitePieceFile >= 1) && ($whitePieceFile <= 2)
        && ($whitePieceRank >= 1) && ($whitePieceRank <= 2)
        && ($whiteKingFile >= 1) && ($whiteKingFile <= 3)) {

      $ausgeben = 1;
    } else {
      $ausgeben = 0;
    }

    ###
    # Ende des Tests, ob die aktuelle Zeile ausgegeben werden soll.
    ###

    if ($ausgeben) {
      print $eingabezeile;
    }
  }
}

