#!/usr/bin/perl

# Dateiname      : gendat-to-arff.pl
# Letzte Änderung: 20. März 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm konvertiert eine gendat-Datei, in jede Spalten eine Überschrift
# hat und in der die Daten der Tests schon konvertiert wurden, in eine
# arff-Datei. Die gendat-Datei wird über STDIN erwartet und die konvertierte
# Datei über STDOUT ausgegeben.
#
# Aufruf: gendat-to-arff.pl relationsname
#
# "relationsname" ist der Name der die Bezeichnung, die in der arff-Datei
# angegeben wird.

use strict;

# Konstanten zur Konfiguration:

# Die Typen der Attribute. Alle nicht enthaltenen Attribute haben den Typ
# real.
my %attribWerte = ("S" => "string",
                   "W" => "integer",
                   "Probeset" => "string",
                   "GeneID" => "string",
                   "FV" => "integer",
                   "Func" => "{CAS,CD,CSK,CTR,CW,DNM,DNR,DR,EF,EX,HEF,HKR,HR,"
                             . "HSY,HTF,HY,IC,IPP,KO,MAK,MLS,MP,MS,MT,NM,NTP,"
                             . "OPT,OT,OTL,OTR,PA,PD,PHO,PK,PM,PTM,REE,REP,"
                             . "RK,ROS,SEF,SGP,SM,SMP,ST,TF,TL,TR,UPF,VT}");


if (@ARGV != 1) {
  die "Aufruf: gendat-to-arff.pl relationsname\n";
}

# Der Name der die Bezeichnung, die in der arff-Datei angegeben wird.
my $relationsname = $ARGV[0];

# Die Nummer der aktuell eingelesenen Zeile.
my $zeilenNr = 0;

while (<STDIN>) {

  if (!/^[\s]*$/) {
    chomp;
    $zeilenNr++;
    my @werte = split(/[ ]*\t[ ]*/);

    if ($zeilenNr == 1) {
      # Header der arff-Datei ausgaben.
      print "\@relation $relationsname\n\n";

      foreach my $attrib (@werte) {
        my $attribTyp = $attribWerte{$attrib};
        if ($attribTyp eq "") {
          print "\@attribute $attrib real\n";
        } else {
          print "\@attribute $attrib $attribTyp\n";
        }
      }

      print "\n\@data\n";

    } else {
      # Datenzeile ausgeben.
      print join(',', @werte) . "\n";
    }
  }
}

