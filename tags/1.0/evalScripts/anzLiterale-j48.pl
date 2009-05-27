#!/usr/bin/perl

# Dateiname      : anzLiterale-j48.pl
# Letzte �nderung: 21. September 2006 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Liefert die Anzahl der Literale in Regel aus einem von J48 erzeugten
# Entscheidungsbaum.
#
# Aufruf: cat regeln | ./anzLiterale-j48.pl


my $gesuchterString = '\|';

while (<>) {
  if (!/^\s*$/) {
    @teile = split(/$gesuchterString/);
    $anzahl += scalar(@teile);
  }
}

print "Anzahl der Literale = $anzahl\n";

