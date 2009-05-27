#!/usr/bin/perl

# Dateiname      : beispielfilter.pl
# Letzte Änderung: 09. September 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm filtert aus einer arff-Datei alle Beispiele aus, die nicht einem
# vorgegebenen Test entsprechen. Der Test ist unten im gekennzeichneten
# Bereich anzugeben.
#
# Die vorhandene arff-Datei wird über STDIN erwartet und die neue Datei
# über STDOUT ausgegeben.
#
# Aufruf: cat datei.arff | ./beispielfilter.pl [arff|bsp]
#
# Der Parameter gibt an, ob eine komplette arff-Datei (mit Header) oder ob
# nur die Beispiele ausgegeben werden sollen.


# Keine Konfig-Parameter.


# Gibt an, ob als nächste eine Zeile des Datenbereichs eingelesen wird.
my $datenbereich = 0;

# Die Nummer des als nächsten Attributs. Das erste hat die Nummer Null.
my $naechsteAttribNr = 0;

# Zu dem Namen jedes Attributs dessen Nummer.
my %attribNummern;

# Parameter testen.
if ((@ARGV != 1) || ($ARGV[0] ne "arff") && ($ARGV[0] ne "bsp")) {
    die "Aufruf: beispielfilter.pl [arff|bsp]\n";
}

# Gibt an, ob eine komplette arff-Datei ausgegeben werden soll.
my $komplett = ($ARGV[0] eq "arff");

while (<STDIN>) {

  if (/^[\s]*$/) {

    # Leerzeile unverändert ausgeben.
    if ($komplett || $datenbereich) {
      print $_;
    }

  } elsif (/^\%/) {

    # Kommentarzeile unverändert ausgeben.
    if ($komplett || $datenbereich) {
      print $_;
    }

  } elsif (/^\@attribute/i) {

    # Zeile mit Attribut unverändert ausgeben.
    if ($komplett) {
      print $_;
    }

    # Zum Namen die Nummer speichern.
    /^\@attribute[\s]+(\S*)/i;
    $attribNummern{$1} = $naechsteAttribNr;
    $naechsteAttribNr++;

  } elsif (/^\@data/i) {

    # Beginn des Datenbereichs.
    if ($komplett) {
      print $_;
    }

    if ($attribNummern{"class"} eq "") {
      die "Kein class Attribut vorhanden\n";
    }
    $datenbereich = 1;

  } elsif (/^\@/) {

    # Andere Header-Zeile unverändert ausgeben.
    if ($komplett) {
      print $_;
    }

  } elsif ($datenbereich) {

    # Zeile aus dem Datenbereich.
    my $eingabezeile = $_;
    chomp;

    # Die Werte der Attribute.
    my @werte = split(/[\s]*,[\s]*/);

    # Flag, ob die Zeile ausgegeben werden soll.
    my $ausgeben;

    ###
    # Beginn des Tests, ob die aktuelle Zeile ausgegeben werden soll.
    # Es ist die Variable $ausgeben entsprechend zu setzen.
    ###

    if (($werte[$attribNummern{"whiteKingFile"}] eq "?")
        || ($werte[$attribNummern{"class"}] eq "?")) {

      $ausgeben = 0;

    } else {

      if (($werte[$attribNummern{"whiteKingFile"}] == 4)
          || ($werte[$attribNummern{"class"}] eq "true")) {

        $ausgeben = 1;
      } else {
        $ausgeben = 0;
      }

    }

    ###
    # Ende des Tests, ob die aktuelle Zeile ausgegeben werden soll.
    ###

    if ($ausgeben) {
      print $eingabezeile;
    }

  } else {

    die "Header-Zeile unbekannter Art\n";

  }
}

