#!/usr/bin/perl

# Dateiname      : anzLiterale-j48.pl
# Letzte Änderung: 21. September 2006 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

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

