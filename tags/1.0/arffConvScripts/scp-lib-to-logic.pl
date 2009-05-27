#!/usr/bin/perl

# Dateiname      : scp-lib-to-logic.pl
# Letzte Änderung: 23. Aug 2005 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Konvertiert eine Datei im scp-lib-Format (wie es von der Methode
# DoppelItmFamilie.toLibFormat()) in das logic-Format (eine vereinfachte
# Version vom espresso-Format).

my @posBeispiele;
my @negBeispiele;
my $zeilennummer = 0;
my $spaltenanz = 0;

while (<>) {

    chomp();
    $eingabe = $_;
    $zeilennummer++;

    if ($zeilennummer == 1) {

        ($posBspAnz, $negBspAnz) = split(' ', $eingabe);

    } elsif (index($eingabe, ":") > -1) {

        $posStringEnde = index($eingabe, ":");
        $negStringAnfang = index($eingabe, ";") + 1;

        $posString = substr($eingabe, 1, $posStringEnde - 2);
        $negString = substr($eingabe, $negStringAnfang + 1, length($eingabe) - $negStringAnfang - 2);

        @posWerte = split(/, /, $posString);
        @negWerte = split(/, /, $negString);

        my %posMenge;
        foreach $wert (@posWerte) {
            $posMenge{$wert} = $wert;
        }

        my %negMenge;
        foreach $wert (@negWerte) {
            $negMenge{$wert} = $wert;
        }

        for (my $i = 0; $i < $posBspAnz; $i++) {
            if (exists $posMenge{$i}) {
                $posBeispiele[$i] .= "1";
            } else {
                $posBeispiele[$i] .= "0";
            }
        }

        for (my $i = 0; $i < $negBspAnz; $i++) {
            if (exists $negMenge{$i}) {
                $negBeispiele[$i] .= "1";
            } else {
                $negBeispiele[$i] .= "0";
            }
        }

        $spaltenanz++;
    }
}

# Ausgabe der ersten Zeile.
print $spaltenanz . " 1 " . ($posBspAnz + $negBspAnz) . "\n";

# Ausgabe der positiven Beispiele
for (my $i = 0; $i < $posBspAnz; $i++) {
    print $posBeispiele[$i] . " 1\n";
}

# Ausgabe der negativen Beispiele
for (my $i = 0; $i < $negBspAnz; $i++) {
    print $negBeispiele[$i] . " 0\n";
}

