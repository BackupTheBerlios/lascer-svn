#!/usr/bin/perl

# Dateiname      : spamLog-to-arff.pl
# Letzte Änderung: 25.01.2006 durch Dietmar Lippod
# Autoren        : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm konvertiert ein log-Dateien von SpamAssassin in einer arff-Datei,
# bei der Wert des class-Attributs, ob es sich bei einer Mail um eine
# Spam-Mail handelt.
# Die log-Datei kann aus einer Datei oder einem Ordner mit Spam- oder
# Ham-Mail erzeugt werden mit dem Programm mass-check von SpamAssassin.
# Die Namen der Spam- und die Ham-Datei sind als Kommandozeilen-Parameter
# anzugeben. Die erzeugte arff-Datei wird über STDOUT ausgegeben.
#
# Aufruf: spamLog-to-arff.pl spam-Datei ham-Datei kennung [ja|nein]
#
# Der letzte Wert gibt an, ob auch indirekte Regeln, deren Namen mit einem
# Unterstrich beginnen, berücksichtigt werden sollen.


if (scalar(@ARGV) != 4) {
  die "Aufruf: spamLog-to-arff.pl <spam-Datei> <ham-Datei> <kennung> [ja|nein]\n";
} elsif (($ARGV[3] ne "ja") && ($ARGV[3] ne "nein")) {
  die "Das vierte Argument muß 'ja' oder 'nein' sein.\n";
}

$spamdatei = $ARGV[0];
$hamdatei = $ARGV[1];
$kennung = $ARGV[2];
$indirekt = ($ARGV[3] eq "ja");

# Ein hash, bei denen alle in $hamdatei und $spamdatei enthaltenen Regeln
# als key vorhanden sind. Der zugehörige value ist jeweils der Wert 1.
my %alleRegelnHash;

# Die Liste aller Regeln.
my @alleRegelnListe;

# Alle in $spamdatei und $hamdatei vorkommenden Regeln in %alleRegeln
# aufnehmen. Indirekte Regeln werden nur aufgenommen, wenn $indirekt wahr
# ist.
foreach $datei ($spamdatei,$hamdatei) {
  open(DATEI, $datei);

  while (<DATEI>) {
    if ((length($_) > 1) && !(/#/)) {
      ($erkannt, $nummer, $mailId, $regeln, @rest) = split;
      @einzelRegelnListe = split(/,/, $regeln);

      foreach my $regel (@einzelRegelnListe) {
        if ($indirekt || (substr($regel, 0, 1) ne "_")) {
          $alleRegelnHash{$regel} = 1;
        }
      }
    }
  }
  close(DATEI);
}

# Umwandeln von %alleRegelnHash in eine sortierte Liste.
@alleRegelnListe = sort(keys(%alleRegelnHash));

# Ausgeben des Headers der arff-Datei.
print "\@relation $kennung\n\n";
foreach my $regel (@alleRegelnListe) {
  print "\@attribute $regel {false, true}\n";
}
print "\@attribute class {false, true}\n";
print "\n\@data\n";

# Ausgeben der Attributwerte zu den einzelnen Mails.
foreach $datei ($spamdatei,$hamdatei) {
  open(DATEI, $datei);

  while (<DATEI>) {
    if ((length($_) > 1) && !(/#/)) {
      ($erkannt, $nummer, $mailId, $regeln, @rest) = split;
      @einzelRegelnListe = split(/,/, $regeln);

      %einzelneRegelnHash = ();
      foreach my $regel (@einzelRegelnListe) {
        $einzelneRegelnHash{$regel} = 1;
      }

      foreach my $regel (@alleRegelnListe) {
        if ($einzelneRegelnHash{$regel} == 1) {
          print "true,";
        } else {
          print "false,";
        }
      }
      if ($datei eq $spamdatei) {
        print "true\n";
      } else {
        print "false\n";
      }
    }
  }
  close(DATEI);
}

