#!/usr/bin/perl

# Dateiname      : logic-n-to-arff.pl
# Letzte Änderung: 26. Februar 2008 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Konvertiert eine Datei im logic-Format (eine vereinfachte Version vom
# espresso-Format) in das arff-Format. Dabei stehen die Anzahl der
# Eingabewerte, der Ausgabewerte und der Terme am Anfang der Datei in
# eigenen Zeilen. Die Anzahl der Ausgabewerte kann insbesondere größer als
# Eins sein. Die erzeugte arff-Datei wird über STDOUT ausgegeben.
#
# Aufruf: logic-to-arff.pl <logic-datei>

use strict;

# Konstanten zur Konfiguration:

# Der Basis-Name der neuen Eingabe-Attribute.
my $einBasisName = "varAttrib";

# Der Basis-Name der neuen Ausgabe-Attribute.
my $ausBasisName = "funkAttrib";


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

# Zu jeder Funktion (Ausgabewert) der logik-Datei ist eine Referenz auf einen
# Hash vorhanden, in dem zu jeder arff-Darstellung der Eingabewerte der
# zugehörige Ausgabewert angegeben ist.
my @funkHashArray;

if (@ARGV != 1) {
    print STDERR "Aufuf: logic-to-arff.pl <logic-datei>\n";
    exit();
}

# Einlesen der logik-Datei.
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

        } elsif (/^\.p\s*([\d]+)/) {

            $termSollAnz = $1;

        } elsif (!/^\./) {

            # Ausgabe einer Datenzeile.
            $termIstAnz++;

            # Prüfen, ob alle Informationen vom Header angegeben waren.
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

                $datenbereich = 1;
            }

            my $arffEingabe = "";
            my $pos = -1;

            # Einlesen der Eingabe-Werte (der Variablen).
            my $attribNr = 0;
            while ($attribNr < $eingabeAnz) {
                $pos++;
                my $zeichen = substr($eingabe, $pos, 1);

                if ($zeichen eq "") {
                    print "\n";
                    print "\n";
                    print STDERR "Fehler: Zu wenige Werte in Zeile ";
                    print $zeilennummer . ":\n";
                    print "$eingabe\n";
                    exit(-1);
                }

                if (($zeichen ne " ") && ($zeichen ne ",")) {
                    if ($zeichen eq "0") {
                        $arffEingabe .= "false,";
                    } elsif ($zeichen eq "1") {
                        $arffEingabe .= "true,";
                    } elsif (($zeichen eq "-") || ($zeichen eq "~")) {
                        $arffEingabe .= "?,";
                    } else {
                        print "\n";
                        print "\n";
                        print STDERR "Fehler: Ungültiges Zeichen in Zeile ";
                        print $zeilennummer . ":\n";
                        print "$eingabe\n";
                        exit(-1);
                    }
                    $attribNr++;
                }
            }

            # Einlesen der Ausgabe-Werte (der Funktionen).
            my $funkNr = 0;
            while ($funkNr < $ausgabeAnz) {
                $pos++;
                my $zeichen = substr($eingabe, $pos, 1);

                if ($zeichen eq "") {
                    print "\n";
                    print "\n";
                    print STDERR "Fehler: Zu wenige Werte in Zeile ";
                    print $zeilennummer . ":\n";
                    print "$eingabe\n";
                    exit(-1);
                }

                if (($zeichen ne " ") && ($zeichen ne ",")) {
                    if ($zeichen eq "0") {
                        $funkHashArray[$funkNr]->{$arffEingabe} = 0;
                    } elsif ($zeichen eq "1") {
                        $funkHashArray[$funkNr]->{$arffEingabe} = 1;
                    } elsif (($zeichen ne "-") && ($zeichen ne "~")) {
                        print "\n";
                        print "\n";
                        print STDERR "Fehler: Ungültiges Zeichen in Zeile ";
                        print $zeilennummer . ":\n";
                        print "$eingabe\n";
                        exit(-1);
                    }
                    $funkNr++;
                }

            }

            if (length($eingabe) > $pos + 1) {
                print "\n";
                print "\n";
                print STDERR "Fehler: Zu viele Werte in Zeile ";
                print $zeilennummer . ":\n";
                print "$eingabe\n";
                exit(-1);
            }
        }
    }
}
close(DATEI);

if ($termIstAnz != $termSollAnz) {
    print STDERR "Warnung: Anzahl der vorhandenen Terme stimmt nicht mit Anzahl\n";
    print STDERR "         der vorgegebenen Terme überein.\n";
    exit(-1);
}

# Ausgabe des Beginns des Headers der arff-Datei.
print "\@relation " . $ARGV[0] . "\n";
print "\n";

# Ausgabe der Eingabe-Attribute.
for (my $i = 1; $i <= $eingabeAnz; $i++) {
    print "\@attribute $einBasisName" . $i . " {false, true}\n";
}

# Ausgabe der Ausgabe-Attribute.
for (my $i = 1; $i <= $ausgabeAnz; $i++) {
    print "\@attribute $ausBasisName" . $i . " {false, true}\n";
}

# Ausgabe des Klassen-Attributs.
print "\@attribute class {false, true}\n";
print "\n";

# Ausgabe des Zeile zum Beginn des Body der arff-Datei.
print "\@data\n";

# Ausgabe der Datenzeilen.
for (my $funkNr = 0; $funkNr <= $ausgabeAnz; $funkNr++) {
    foreach my $eingabe (keys(%{$funkHashArray[$funkNr]})) {
        my $funkWert = @funkHashArray[$funkNr]->{$eingabe};
        print $eingabe . &funkKodierung($funkNr, $ausgabeAnz);
        if ($funkWert == 0) {
            print "false\n";
        } else {
            print "true\n";
        }
    }
}
print "\n";


# Funktion liefert eine Folge von Strings "false" mit einem String "true",
# dessen Stelle als erster Parameter übergeben wird. Die erste Stelle hat
# die Nummer Null. Der zweite Parameter liefert die Anzahl der Strings. Die
# einzelnen Strings sind durch Kommata getrennt.
sub funkKodierung {
    my $funkNr = shift;
    my $funkAnz = shift;
    my $kodierung = "";

    for (my $nr = 0; $nr < $funkNr; $nr++) {
        $kodierung .= "false,";
    }

    $kodierung .= "true,";

    for (my $nr = $funkNr + 1; $nr < $funkAnz; $nr++) {
        $kodierung .= "false,";
    }

    return $kodierung;
}

