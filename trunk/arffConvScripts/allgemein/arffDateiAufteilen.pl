#!/usr/bin/perl

# Dateiname      : arffDateiAufteilen.pl
# Letzte Änderung: 09. September 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm teilt die Beispiele einer arff-Datei zufällig auf zwei neue
# arff-Dateien auf. Der Kopf der arff-Datei wird in beide neuen arff-Dateien
# übernommen. Der Name der vorhandenen arff-Datei ist in der Kommandozeile
# anzugeben und die beiden neuen arff-Dateien werden unter Namen erzeugt, die
# vom angegebenen Namen abgeleitet sind.
#
# Aufruf: arffDateiAufteilen.pl Dateiname anteil
#
# "Dateiname" gibt den Namen der aufzuteilenden arff-Daten an.
# "anteil" ist ein Wert zwischen Null und Eins und gibt den Anteil der
# Beispiele für die eine der beiden neuen Daten an.

use strict;

# Konfig-Parameter:

# Der Anhang an den Namen vorhandenen arff-Datei für die erste neu zu
# erzeugende Datei.
my $anhang1 = "-1";

# Der Anhang an den Namen vorhandenen arff-Datei für die zweite neu zu
# erzeugende Datei.
my $anhang2 = "-2";


# Beginn des Programms.

# Parameter testen.
if (@ARGV != 2) {
    die "Aufruf: arffDateiAufteilen.pl Dateiname anteil\n";
}

# Der Name der vorhandenen arff-Datei.
my $vorhandeneDatei = $ARGV[0];
my $neueDatei1 = $vorhandeneDatei . $anhang1;
my $neueDatei2 = $vorhandeneDatei . $anhang2;

# Der Anteil der Beispiele für die eine der beiden neuen Dateien.
my $anteil = $ARGV[1];

if (($anteil < 0.0) || ($anteil > 1.0)) {
  die "Der Anteil muß zwischen Null und Eins liegen.\n";
}

# Gibt an, ob als nächste eine Zeile des Datenbereichs eingelesen wird.
my $datenbereich = 0;

open(VORH_DATEI, $vorhandeneDatei);
open(NEU_DATEI1, "> " . $neueDatei1) || die("Datei kann nicht geöffnet werden: $!");
open(NEU_DATEI2, "> " . $neueDatei2) || die("Datei kann nicht geöffnet werden: $!");

while (<VORH_DATEI>) {

  if (!$datenbereich) {

    if (/^\@data/i) {
      $datenbereich = 1;
    }

    # Header-Zeile in beiden neuen Dateien ausgeben.
    print NEU_DATEI1 $_;
    print NEU_DATEI2 $_;

  } else {

    # Leerzeilen, Kommentarzeilen und Zeilen mit unbekannten Werten, d.h. mit
    # einem Fragezeichen, weglassen.
    if ((!/^[\s]*$/) && (!/^\%/) && (!/\?/)) {

      if (rand() < $anteil) {
        print NEU_DATEI1 $_;
      } else {
        print NEU_DATEI2 $_;
      }

    }
  }
}
print NEU_DATEI1 "\n";
print NEU_DATEI2 "\n";

