#!/usr/bin/perl

# Dateiname      : attribWertEntfern.pl
# Letzte Änderung: 18. März 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm entfernt vorgegebene Werte von Attributen und entfernt die
# Beispiele, die diese Attributwerte besitzen.


# Konfig-Parameter:

# Eine Hash-Liste mit den Nummern von Attributen und dem jeweils zu
# entfernenden Wert. Das erste Attribut hat die Nummer Null. Jedes Attribut
# darf maximal einmal vorkommen.
my %zuEntfernen = (0 => 'april', 2 => '\?');


# Gibt die Anzahl schon eingelesener Attribute an.
my $attributeGelesen = 0;

while (<>) {

  if (/^[\s]*$/) {

    # Leerzeile unverändert ausgeben.
    print $_;

  } elsif (/^\%/) {

    # Kommentarzeile unverändert ausgeben.
    print $_;

  } elsif (/^\@attribute/i) {

    $attributeGelesen++;

    if (!$zuEntfernen{$attributeGelesen - 1}) {

      # Zeile mit Attribut ausgeben.
      print $_;

    } else {

      # Ein Wert des Attributs muß entfernt werden.
      chomp;
      (my $tag, my $name, my $werte) = split(' ', $_, 3);

      my $substWert = $zuEntfernen{$attributeGelesen - 1};
      if ($werte =~ /,[ ]*$substWert/) {
        $werte =~ s/,[ ]*$substWert//o;
      } elsif ($werte =~ /$substWert[ ]*,/) {
        $werte =~ s/$substWert[ ]*,//o;
      } elsif ($werte =~ /$substWert/) {
        $werte =~ s/$substWert//o;
      }
      print $tag . " " . $name . " " . $werte . "\n";

    }

  } elsif (/^\@/) {

    # Header-Zeile unverändert ausgeben.
    print $_;

  } else {

    # Datenzeile.
    chomp;
    @werte = split(/[ ]*,[ ]*/);

    # Variable die angibt, ob die Zeile ausgegeben werden soll.
    my $ausgeben = 1;

    # Werte prüfen, ob sie im Beispiel vorkommen.
    foreach my $nr (keys %zuEntfernen) {
      my $substWert = $zuEntfernen{$nr};
      if ($werte[$nr] =~ /$substWert/) {
        $ausgeben = 0;
      }
    }

    # Zeile gegebenenfalls ausgeben.
    if ($ausgeben) {
      print $_ . "\n";
    }
  }
}

