#!/usr/bin/perl

# Dateiname      : logic-to-arff.pl
# Letzte Änderung: 19. September 2006 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Konvertiert eine Datei im logic-Format (eine vereinfachte Version vom
# espresso-Format) in das arff-Format. Dabei stehen die Anzahl der
# Eingabewerte, der Ausgabewerte und der Terme am Anfang der Datei in
# eigenen Zeilen. Die erzeugte arff-Datei wird über STDOUT ausgegeben.
#
# Aufruf: logic-to-arff.pl <logic-datei>

use strict;

# Konstanten zur Konfiguration:

# Der Basis-Name der neuen Attribute.
my $basisName = "binAttrib";


# Die Nummer der als nächstes einzulesenden Zeile.
my $zeilennummer = 0;

# Gibt an, ob schon einen Datenzeile eingelesen wurden.
my $datenbereich = 0;

# Die Anzahl der vorhandenen Eingabezeilen (ohne Leer- und Kommentarzeilen).
my $eingabezeilen = 0;

# Die Anzahl der Eingabewerte.
my $eingabeAnz = -1;

# Die Anzahl der Ausgabewerte.
my $ausgabeAnz = -1;

# Die Anzahl der Terme, die vorhanden sein sollen.
my $termSollAnz = -1;

# Die Anzahl der Terme, die vorhanden sind.
my $termIstAnz = 0;

if (@ARGV != 1) {
    print STDERR "Aufuf: logic-to-arff.pl <logic-datei>\n";
    exit();
}

open(DATEI, $ARGV[0]) or die "Datei nicht vorhanden";
while (<DATEI>) {

    $zeilennummer++;

    # Prüfen, ob Leerzeile oder Kommentarzeile vorhanden.
    if ((!/(^[\s]*$)/) && (!/\#/)) {

        # Eventuelle Typ-Angabe prüfen.
        if ((/^.type/) && (!/.type fr/)) {
            print STDERR "Falsches Datei-Format.\n";
            exit(-1);
        }

        chomp();
        my $eingabe = $_;

        if (/^\.i\s*([\d]+)/) {

            $eingabeAnz = $1;

        } elsif (/^\.o\s*([\d]+)/) {

            $ausgabeAnz = $1;

            if ($ausgabeAnz != 1) {
                print STDERR "Fehler: Es muß genau ein Ausgabewert vorhanden sein.\n";
                exit(-1);
            }

        } elsif (/^\.p\s*([\d]+)/) {

            $termSollAnz = $1;

        } elsif (!/^\./) {

            # Ausgabe einer Datenzeile.
            $termIstAnz++;

            # Prüfen, ob der Header ausgegeben werden muß.
            if (!$datenbereich) {

                if ($eingabeAnz == -1) {
                    print STDERR "Es ist keine Anzahl der Eingabewerte angegeben.\n";
                    exit(-1);
                }

                if ($ausgabeAnz == -1) {
                    print STDERR "Es ist keine Anzahl der Ausgabewerte angegeben.\n";
                    exit(-1);
                }

                if ($termSollAnz == -1) {
                    print STDERR "Es ist keine Anzahl der Terme angegeben.\n";
                    exit(-1);
                }

                # Ausgabe des Beginns des Headers der arff-Datei.
                print "\@relation " . $ARGV[0] . "\n";
                print "\n";

                # Ausgabe der Attribute.
                for (my $i = 1; $i < $eingabeAnz + 1; $i++) {
                    print "\@attribute $basisName" . $i . " {false, true}\n";
                }

                # Ausgabe des Klassen-Attributs.
                print "\@attribute class {false, true}\n";
                print "\n";

                # Ausgabe des Zeile zum Beginn des Body der arff-Datei.
                print "\@data\n";

                $datenbereich = 1;
            }

            my $attribNr = 0;
            for (my $pos = 0; $pos < length($eingabe); $pos++) {
                my $zeichen = substr($eingabe, $pos, 1);

                if (($zeichen ne " ") && ($zeichen ne ",")) {

                    $attribNr++;
                    if ($attribNr <= $eingabeAnz + 1) {
                        if ($zeichen eq "0") {
                            print "false";
                        } elsif ($zeichen eq "1") {
                            print "true";
                        } elsif (($zeichen eq "-") || ($zeichen eq "~")) {
                            print "?";
                        } else {
                            print "\n";
                            print "\n";
                            print STDERR "Fehler: Ungültiges Zeichen in Zeile ";
                            print $zeilennummer . ":\n";
                            print "$eingabe\n";
                            exit(-1);
                        }
                    } else {
                        print "\n";
                        print "\n";
                        print STDERR "Fehler: Zu viele Werte in Zeile ";
                        print $zeilennummer . ":\n";
                        print "$eingabe\n";
                        exit(-1);
                    }

                    if ($attribNr <= $eingabeAnz) {
                        print ",";
                    }
                }
            }
            print "\n";

            if ($attribNr < $eingabeAnz) {
                print "\n";
                print STDERR "Fehler: Zu wenige Werte in Zeile ";
                print STDERR $zeilennummer . "\n";
                exit(-1);
            }
        }
    }
}
print "\n";

if ($termIstAnz != $termSollAnz) {
    print STDERR "Warnung: Anzahl der vorhandenen Terme stimmt nicht mit Anzahl\n";
    print STDERR "         der vorgegebenen Terme überein.\n";
}

