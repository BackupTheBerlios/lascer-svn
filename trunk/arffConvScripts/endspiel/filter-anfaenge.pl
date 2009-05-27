#!/usr/bin/perl

# Dateiname      : filter-anfaenge.pl
# Letzte �nderung: 28. Juli 2007 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Programm entfernt aus einer Datei alle Zeilen, die mit einer Zeile aus
# einer anderen Datei anfangen. Die verk�rzte Datei wird auf STDOUT
# ausgegeben.
#
# Aufruf: filter-anfaenge.pl <anfaenge> <datei>
#
# Dabei ist <anfaenge> die Datei mit den Zeilenanf�ngen und <datei> die
# zu filternde Datei.

use strict;


if (scalar(@ARGV) != 2) {
  die "Aufruf: filter-anfaenge.pl <anfaenge> <datei>\n";
}

# Die L�nge der Zeilenanf�nge.
my $anfangsLaenge = -1;

# Die Liste mit den Zeilenanf�ngen.
my %anfaenge = ();

# Die Datei mit den Zeilenanf�ngen einlesen.
open(ANFAENGE, $ARGV[0]) or die "Datei $ARGV[0] konnte nicht ge�ffnet werden";
while (<ANFAENGE>) {
  chomp;

  my $neueLaenge = length($_);

  if ($anfangsLaenge < 0) {
    $anfangsLaenge = $neueLaenge;
  } elsif ($neueLaenge != $anfangsLaenge) {
    die "L�ngen der Anf�nge sind unterschiedlich\n";
  }

  $anfaenge{$_} = 1;
}

# Die zu filternde Datei einlesen und filtern.
open(DATEI, $ARGV[1]) or die "Datei $ARGV[1] konnte nicht ge�ffnet werden";
while (<DATEI>) {
  my $zeilenanfang = substr($_, 0, $anfangsLaenge);
  if (!$anfaenge{$zeilenanfang}) {
    print $_;
  }
}

