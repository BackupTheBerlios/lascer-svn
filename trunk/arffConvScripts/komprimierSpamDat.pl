#!/usr/bin/perl

# Dateiname      : komprimierSpamDat.pl
# Letzte Änderung: 16. September 2006 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Komprimiert eine Spam-arff-Datei, indem es alle Werte "false" durch "f"
# und alle Werte "true" durch "t" ersetzt, außer für das Attribut class.
# Außerdem werden die Namen der Attribute ersetzt. Die komprimierte Datei
# wird auf der Standard-Ausgabe ausgegeben.
#
# Aufruf: cat datei.arff | ./komprimierSpamDat.pl


use strict;

# Konfig-Parameter:

# Der Basis-Name der neuen Attribute.
my $basisName = "attrib";


# Die Nummer des zuletzt eingelesenen Attributs.
my $attribNr = 0;

while (<>) {

  if (/\@attribute class/) {
    print $_;
  } elsif (/\@attribute ([^ ]+)/) {
    $attribNr++;
    my $naechstAttrib = $basisName . $attribNr;
    s/$1/$naechstAttrib/;
    s/false/f/;
    s/true/t/;

    print $_;
  } else {
    # false außer am Zeilenende ersetzen.
    s/false/f/g;
    s/([ ,])f$/$1false/;

    # true außer am Zeilenende ersetzen.
    s/true/t/g;
    s/([ ,])t$/$1true/;

    print $_;
  }
}

