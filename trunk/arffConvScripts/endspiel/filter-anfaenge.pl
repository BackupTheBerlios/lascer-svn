#!/usr/bin/perl

# Dateiname      : filter-anfaenge.pl
# Letzte Änderung: 28. Juli 2007 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm entfernt aus einer Datei alle Zeilen, die mit einer Zeile aus
# einer anderen Datei anfangen. Die verkürzte Datei wird auf STDOUT
# ausgegeben.
#
# Aufruf: filter-anfaenge.pl <anfaenge> <datei>
#
# Dabei ist <anfaenge> die Datei mit den Zeilenanfängen und <datei> die
# zu filternde Datei.

use strict;


if (scalar(@ARGV) != 2) {
  die "Aufruf: filter-anfaenge.pl <anfaenge> <datei>\n";
}

# Die Länge der Zeilenanfänge.
my $anfangsLaenge = -1;

# Die Liste mit den Zeilenanfängen.
my %anfaenge = ();

# Die Datei mit den Zeilenanfängen einlesen.
open(ANFAENGE, $ARGV[0]) or die "Datei $ARGV[0] konnte nicht geöffnet werden";
while (<ANFAENGE>) {
  chomp;

  my $neueLaenge = length($_);

  if ($anfangsLaenge < 0) {
    $anfangsLaenge = $neueLaenge;
  } elsif ($neueLaenge != $anfangsLaenge) {
    die "Längen der Anfänge sind unterschiedlich\n";
  }

  $anfaenge{$_} = 1;
}

# Die zu filternde Datei einlesen und filtern.
open(DATEI, $ARGV[1]) or die "Datei $ARGV[1] konnte nicht geöffnet werden";
while (<DATEI>) {
  my $zeilenanfang = substr($_, 0, $anfangsLaenge);
  if (!$anfaenge{$zeilenanfang}) {
    print $_;
  }
}

