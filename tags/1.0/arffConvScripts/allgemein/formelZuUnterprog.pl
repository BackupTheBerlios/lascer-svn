#!/usr/bin/perl

# Dateiname      : formelZuUnterprog.pl
# Letzte Änderung: 10. September 2006 durch Dietmar Lippold
# Autor          : Dietmar Lippold
#
# Diese Datei gehört zum Projekt Lascer (http://lascer.berlios.de/).
# Siehe Datei "license.txt" für Hinweise zur Lizenz.

# Programm wandelt eine vorgegebene, von Lascer erzeugte Formel in ein
# perl-Unterprogramm. Damit kann für die Beispiele einer arff-Datei ermittelt
# werden, ob die Formel jeweils auf sie zutrifft. Das erzeugte Unterprogramm
# wird über STDOUT ausgegeben.
#
# Aufruf: ./formelZuUnterprog.pl

use strict;

# Konfig-Parameter:

# Vorzugebende Formel.
my $formel = <<EOF;
OR(v(a) = 0, NOT(v(b) >= 0), AND(v(c) <= 0, NOT(v(d) != 0)))
EOF


# Beginn des Programms.

my $boolAusdruck ="";

chomp $formel;

# Vergleiche gegen nominale Attributwerte ersetzen.
$formel =~ s/(=[ ]*)([^ -\d\),]+[^ \),]*)/eq '$2'/g;

erzeugeBoolAusdruck($formel);

print "sub formelTrifftZu {\n";
print "  my \@werte = \@_;\n";
print "\n";
print "  return $boolAusdruck;\n";
print "}\n";
print "\n";


sub erzeugeBoolAusdruck {
  my $text = shift;

  $text =~ s/^[\s]*//;
  if ($text =~ /^v/) {
    return posLiteral($text);
  } elsif ($text =~ /^NOT/) {
    return negLiteral($text);
  } elsif ($text =~ /^AND/) {
    return konjunktion($text);
  } elsif ($text =~ /^OR/) {
    return disjunktion($text);
  } else {
    die "Text ist keine Formel: $text\n";
  }
}

sub unbekanntTest {
  my $text = shift;

  if ($text !~ /^v\(([^\)]+)\)([^,\)]+)/) {
    die "Text ist kein pos. Literal: $text\n";
  }
  my $attribName = $1;

  return "(\$werte[\$attribNummern{\"" . $attribName . "\"}] ne \"?\")";
}

sub posLiteral {
  my $text = shift;

  if ($text !~ /^v\(([^\)]+)\)([^,\)]+)/) {
    die "Text ist kein pos. Literal: $text\n";
  }
  my $attribName = $1;
  my $bedingung = $2;
  my $bedingungNeu = $2;

  $bedingungNeu =~ s/ = / == /;
  $boolAusdruck .= unbekanntTest($text)
                   . " && (\$werte[\$attribNummern{\"" . $attribName . "\"}]"
                                                       . $bedingungNeu . ")";

  return "v($attribName)" . $bedingung;
}

sub posNegLiteral {
  my $text = shift;

  if ($text !~ /^v\(([^\)]+)\)([^,\)]+)/) {
    die "Text ist kein pos. Literal: $text\n";
  }
  my $attribName = $1;
  my $bedingung = $2;
  my $bedingungNeu = $2;

  $bedingungNeu =~ s/ = / == /;
  $boolAusdruck .= "(\$werte[\$attribNummern{\"" . $attribName . "\"}]"
                                                 . $bedingungNeu . ")";

  return "v($attribName)" . $bedingung;
}

sub negLiteral {
  my $text = shift;
  my $anfang;
  my $posLiteralText;
  my $posLiteral;
  my $ende;

  if ($text !~ /^NOT/) {
    die "Text ist kein neg. Literal: $text\n";
  }

  $text =~ /(^NOT[^v]+)/;
  $boolAusdruck .= "not";
  $anfang = $1;
  $posLiteralText = substr($text, length($anfang));
  $posLiteral = posNegLiteral($posLiteralText);
  $boolAusdruck .= " && " . unbekanntTest($posLiteralText);
  substr($text, length($anfang) + length($posLiteral)) =~ /([^\)]*[\)])/;
  $ende = $1;

  return ($anfang . $posLiteral . $ende);
}

sub konjunktion {
  my $text = shift;
  my $anfang;
  my $gelesen;
  my $rest;
  my $teil;

  if ($text !~ /^AND/) {
    die "Text ist keine Konjunktion: $text\n";
  }

  $text =~ /(^AND[^vNO]+)/;
  $boolAusdruck .= "(";
  $anfang = $1;
  $gelesen = $anfang;
  do {
    $rest = substr($text, length($gelesen));

    if ($rest =~ /^v/) {
      $teil = posLiteral($rest);
    } elsif ($rest =~ /^NOT/) {
      $teil = negLiteral($rest);
    } elsif ($rest =~ /^OR/) {
      $teil = disjunktion($rest);
    } else {
      die "Text ist keine Konjunktion: $text\n";
    }
    $gelesen .= $teil;
    substr($text, length($gelesen)) =~ /(^[^vNO\)]*[\)]*)/;
    $gelesen .= $1;
    if ($gelesen !~ /\)$/) {
      $boolAusdruck .= " && ";
    }

  } until ($gelesen =~ /\)$/);
  $boolAusdruck .= ")";

  return $gelesen;
}

sub disjunktion {
  my $text = shift;
  my $anfang;
  my $gelesen;
  my $rest;
  my $teil;

  if ($text !~ /^OR/) {
    die "Text ist keine Disjunktion: $text\n";
  }

  $text =~ /(^OR[^vNA]+)/;
  $boolAusdruck .= "(";
  $anfang = $1;
  $gelesen = $anfang;
  do {
    $rest = substr($text, length($gelesen));

    if ($rest =~ /^v/) {
      $teil = posLiteral($rest);
    } elsif ($rest =~ /^NOT/) {
      $teil = negLiteral($rest);
    } elsif ($rest =~ /^AND/) {
      $teil = konjunktion($rest);
    } else {
      die "Text ist keine Disjunktion: $text\n";
    }
    $gelesen .= $teil;
    substr($text, length($gelesen)) =~ /(^[^vNA\)]*[\)]*)/;
    $gelesen .= $1;
    if ($gelesen !~ /\)$/) {
      $boolAusdruck .= " || ";
    }

  } until ($gelesen =~ /\)$/);
  $boolAusdruck .= ")";

  return $gelesen;
}

