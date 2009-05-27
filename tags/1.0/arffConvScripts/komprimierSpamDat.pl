#!/usr/bin/perl

# Dateiname      : komprimierSpamDat.pl
# Letzte �nderung: 16. September 2006 durch Dietmar Lippold
# Autoren        : Dietmar Lippold
#
# Diese Datei geh�rt zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" f�r Hinweise zur Lizenz.

# Komprimiert eine Spam-arff-Datei, indem es alle Werte "false" durch "f"
# und alle Werte "true" durch "t" ersetzt, au�er f�r das Attribut class.
# Au�erdem werden die Namen der Attribute ersetzt. Die komprimierte Datei
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
    # false au�er am Zeilenende ersetzen.
    s/false/f/g;
    s/([ ,])f$/$1false/;

    # true au�er am Zeilenende ersetzen.
    s/true/t/g;
    s/([ ,])t$/$1true/;

    print $_;
  }
}

