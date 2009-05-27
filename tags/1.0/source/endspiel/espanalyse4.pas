{*
   Dateiname      : espanalyse4.pas
   Letzte Änderung: April 1997 durch Dietmar Lippold
   Autoren        : Dietmar Lippold
   Copyright (C)  : Dietmar Lippold, 1996
  
   This file is part of Lascer (http://lascer.berlios.de/).
  
   Lascer is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
  
   Lascer is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
  
   You should have received a copy of the GNU General Public License
   along with Lascer; if not, see <http://www.gnu.org/licenses/>.
*}


{ Programm analysiert 4-Steine-Schachendspiele.

  Zu jeder moeglichen Stellung wird ermittelt, ob sie gewinnbar, patt oder
  remis ist oder wie viele Zuege bis zum Gewinn gebraucht werden.
  Dazu werden zuerst alle moeglichen Endstellungen erzeugt und von diesen
  ausgehend die jeweils moeglichen Ursprungsstellungen. }

{* Version ohne Unterverwandlung *}



PROGRAM EndspielAnalyse4(INPUT,OUTPUT);

CONST
  maxzuegeanz = 60;
  {* maxzuegeanz gibt eine Obergrenze fuer die Anzahl der moeglichen Zuege
     einer Figur an *}
  maxbhzuganz = 70;
  {* maxbhzuganz gibt eine Obergrenze fuer die Anzahl der Zuege bis zum
     Gewinn von bewerteten Stellungen aus geladenen Endspielen an *}
  maxendstanz = 1400000;   {* fuer Endspiel ohne x-Bauer reicht 250000,
                              mit max. einem x-Bauer reicht 1000000    *}
  {* maxendstanz gibt eine Obergrenze fuer die Anzahl von bewerteten
     Stellungen an, die geladen und bei der Analyse als Endstellungen
     benutzt werden *}
  maxbewstanz = 370000;    {* fuer Endspiel ohne x-Bauer und ohne
                              Hauptfigur mit Bauer der gleichen Farbe
                              reicht 20000, mit max. einem x-Bauer ohne
                              Hauptfigur der gleichen Farbe oder mit
                              einer Hauptfigur und einem nicht-x-Bauern
                              der gleichen Farbe reicht 140000, fuer
                              zwei x-Bauern reicht 260000              *}
  {* maxbewstanz gibt eine Obergrenze fuer die Anzahl von bewerteten
     Stellungen an, die geladen und nicht als Endstellungen bei der
     Analyse benutzt werden *}
  maxghzuganz = 90;
  {* maxghzuganz gibt eine Obergrenze fuer die Anzahl der Zuege bis zum
     Gewinn bei den analysierten Endspielen an *}
  maxgewstanz = 1800000;   {* fuer Endspiel ohne x-Bauer reicht 800000 *}
  {* maxgewstanz gibt eine Obergrenze fuer die Anzahl der in einem Halbzug
     erzeugten gewinnbaren Stellungen an *}

TYPE
  SZBEREICH = 1..8;
  {* SZBEREICH beschreibt den Bereich fuer Spalten und Zeilen *}
  ZGBEREICH = 0..maxzuegeanz;
  {* ZGBEREICH beschreibt den Bereich fuer die Anzahl moeglicher Zuege
     einer Figur *}
  BSTWERT = 0..maxbhzuganz;
  {* BSTWERT beschreibt den Bereich fuer die Anzahl der Zuege bis zum
     Gewinn von bewerteten Stellungen aus geladenen Endspielen *}
  ESTBEREICH = 0..maxendstanz;
  {* ESTBEREICH beschreibt den Bereich fuer die Anzahl von bewerteten
     Stellungen, die geladen und bei der Analyse als Endstellungen
     benutzt werden *}
  BSTBEREICH = 0..maxbewstanz;
  {* BSTBEREICH beschreibt den Bereich fuer die Anzahl von bewerteten
     Stellungen, die geladen und nicht als Endstellungen bei der Analyse
     benutzt werden *}
  GSTWERT = 0..maxghzuganz;
  {* GSTWERT beschreibt den Bereich fuer die Anzahl der Zuege bis zum
     Gewinn bei den analysierten Endspielen *}
  GSTBEREICH = 0..maxgewstanz;
  {* GSTBEREICH beschreibt den Bereich fuer die Anzahl der in einem Halbzug
     erzeugten gewinnbaren Stellungen *}
  DATBEREICH = 0..120;   {* 0..Max(maxghzuganz,120), 120 = ORD('x') *}
  {* DATBEREICH beschreibt den Bereich von Werten in geladenen und
     gespeicherten Dateien *}
  FARBEN = (weiss,schwarz);
  {* FARBEN benennt die moeglichen Farben der Figuren *}
  STELLART = (illegal,nachesp,nichtbew,matt,patt,tremis,wgewinn,sgewinn,remis);
  {* STELLART benennt die moeglichen Stellungsarten *}
  BSTELLUNGSWERTE = ARRAY[1..8,1..8,1..8,1..8,1..8,1..8] OF BSTWERT;
  {* BSTELLUNGSWERTE beschreibt das Feld, in dem fuer jede geladene
     Stellung eines 3-Steine-Endspiels ihr Wert gespeichert ist *}
  BSTELLUNGSARTEN = ARRAY[1..8,1..8,1..8,1..8,1..8,1..8] OF STELLART;
  {* BSTELLUNGSARTEN beschreibt das Feld, in dem fuer jede geladene
     Stellung eines 3-Steine-Endspiels ihre Art gespeichert ist *}
  STELLUNGSZANZ = ARRAY[1..8,1..8,1..8,1..8,1..8,1..8,1..8,1..8] OF ZGBEREICH;
  {* STELLUNGSZANZ beschreibt das Feld, in dem fuer jede Stellung die
     aktuelle Anzahl der nicht zum Verlust fuehrenden Zuege gespeichert
     ist *}
  STELLUNGSWERTE = ARRAY[1..8,1..8,1..8,1..8,1..8,1..8,1..8,1..8] OF GSTWERT;
  {* STELLUNGSWERTE beschreibt das Feld, in dem fuer jede Stellung ihr
     Wert gespeichert ist *}
  STELLUNGSARTEN = ARRAY[1..8,1..8,1..8,1..8,1..8,1..8,1..8,1..8] OF STELLART;
  {* STELLUNGSARTEN beschreibt das Feld, in dem fuer jede Stellung ihre
     Art gespeichert ist *}
  POSITION = RECORD
               spalte,zeile : SZBEREICH;
             END;
  {* POSITION beschreibt die Position einer Figur durch ihre Spalte
     und Zeile *}
  POSLIST = ARRAY[1..maxzuegeanz] OF POSITION;
  {* POSLIST beschreibt eine Liste von Positionen *}
  BSTELLUNG = RECORD
                sks,skz,wks,wkz,f3s,f3z : SZBEREICH;
              END;
  {* BSTELLUNG beschreibt eine geladene Stellung eines 3-Steine-Endspiels
     durch die Spalten und Zeilen der Figuren, ohne Angabe der Farbe, die
     am Zug ist.
     'sk' steht fuer den schwarzen Koenig, 'wk' fuer den weissen Koenig
     und 'f3' fuer die dritte Figur. Das angehaengte 's' steht fuer Spalte,
     das 'z' fuer "Zeile". *}
  STELLUNG = RECORD
               sks,skz,wks,wkz,f3s,f3z,f4s,f4z : SZBEREICH;
             END;
  {* STELLUNG beschreibt eine Stellung durch die Spalten und Zeilen der
     Figuren, ohne Angabe der Farbe, die am Zug ist.
     'sk' steht fuer den schwarzen Koenig, 'wk' fuer den weissen Koenig,
     'f3' fuer die dritte Figur und 'f4' fuer die vierte Figur. Das
     angehaengte 's' steht fuer Spalte, das 'z' fuer Zeile. *}
  GEWSTELLUNGEN = ARRAY[1..maxgewstanz] OF STELLUNG;
  {* GEWSTELLUNGEN beschreibt die Menge der gewinnbaren Stellungen, die
     in einem Halbzug ermittelt werden *}
  ENDSTELLUNGEN = ARRAY[1..maxendstanz] OF STELLUNG;
  {* ENDSTELLUNGEN beschreibt die Menge der mittels der geladenen Daten
     bewerteten Stellungen, die bei der Analyse als Endstellungen benutzt
     werden. *}
  BHZUGSTELLUNGEN = ARRAY[1..maxbhzuganz,1..maxbewstanz] OF STELLUNG;
  {* BHZUGSTELLUNGEN beschreibt fuer jeden Halbzug, ausser fuer den
     nullten, die Menge der mittels der geladenen Daten bewerteten
     Stellungen, die bei der Analyse nicht als Endstellungen benutzt
     werden. *}
  BHZSTELLUNGSANZ = ARRAY[1..maxbhzuganz] OF BSTBEREICH;
  {* BHZSTELLUNGSANZ beschreibt fuer jeden Halbzug, ausser fuer den
     nullten, die Anzahl der mittels der geladenen Daten bewerteten
     Stellungen, die bei der Analyse nicht als Endstellungen benutzt
     werden. *}
  SPSTELLUNGSANZ = ARRAY[weiss..schwarz] OF INTEGER;
  {* SPSTELLUNGSANZ beschreibt fuer jede Farbe die gesamte Anzahl von
     Stellungen einer Art nach Spiegelung *}
  HZSPSTELLUNGSANZ = ARRAY[0..maxghzuganz] OF SPSTELLUNGSANZ;
  {* HZSPSTELLUNGSANZ beschreibt fuer jeden Halbzug die Anzahl von
     Stellungen einer Art und einer Farbe nach Spiegelung *}

VAR
  brett : ARRAY[-1..10,-1..10] OF BOOLEAN;   { global }
  {* brett wird beim Test eines Zuges auf Legalitaet benutzt und gibt fuer
     jedes Feld eines erweiterten Brettes an, ob die jeweilige Figur darauf
     ziehen darf *}
  o3stwerte,o4stwerte : BSTELLUNGSWERTE;   { global }
  {* Variablen enthalten die Stellungswerte geladener 3-Steine-Endspiele
     zur Bewertung von Stellungen, in denen eine Figur geschlagen wurde.
     o3stwerte enthaelt die Daten des Endspiels ohne die dritte Figur,
     o4stwerte die Daten des Endspiels ohne die vierte Figur. *}
  wdstwerte,sdstwerte : STELLUNGSWERTE;   { global }
  {* Variablen enthalten die Stellungswerte geladener Damenendspiele zur
     zur Bewertung von Stellungen, in denen ein Bauer umgewandelt wird *}
  moegzuganz : ARRAY[weiss..schwarz] OF STELLUNGSZANZ;   { global }
  {* moegzuganz enthaelt die Zahl der jeweils aktuell nicht zum Verlust
     fuehrenden Zuege fuer beide Farben. *}
  stellwerte : ARRAY[weiss..schwarz] OF STELLUNGSWERTE;   { global }
  {* stellwerte enthaelt die Stellungswerte aller Stellungen des zu
     analysierenden Endspiels fuer beide Farben. *}
  o3starten,o4starten : BSTELLUNGSARTEN;   { global }
  {* Variablen enthalten die Stellungsarten geladener 3-Steine-Endspiele
     zur Bewertung von Stellungen, in denen eine Figur geschlagen wurde.
     o3starten enthaelt die Daten des Endspiels ohne die dritten Figur,
     o4starten die Daten des Endspiels ohne die vierten Figur. *}
  wdstarten,sdstarten : STELLUNGSARTEN;   { global }
  {* Variablen enthalten die Stellungsarten geladener Damenendspiele als
     Umwandlungsendspiele, wenn ein Bauernendspiel analysiert werden
     soll. *}
  stellarten : ARRAY[weiss..schwarz] OF STELLUNGSARTEN;   { global }
  {* stellarten enthaelt die Stellungsarten aller Stellungen des zu
     analysierenden Endspiels fuer beide Farben. *}
  gstellungen : ARRAY[0..1,weiss..schwarz] OF GEWSTELLUNGEN;   { global }
  {* gstellungen stellt Listen der am Anfang auf Illegalitaet zu
     pruefenden oder der im jeweils letzten Halbzug als gewinnbar
     erkannten Stellungen fuer beide Farben dar *}
  estellungen :  ARRAY[weiss..schwarz] OF ENDSTELLUNGEN;   { global }
  {* estellungen enthaelt die Menge der mittels der geladenen Daten
     bewerteten Stellungen, die bei der Analyse als Endstellungen
     benutzt werden *}
  bhzstellungen : ARRAY[weiss..schwarz] OF BHZUGSTELLUNGEN;   { global }
  {* bhzstellungen enthaelt fuer jeden Halbzug, ausser dem nullten,
     die Menge der mittels der geladenen Daten bewerteten Stellungen,
     die bei der Analyse nicht als Endstellungen benutzt werden *}
  gstellanz : ARRAY[0..1,weiss..schwarz] OF GSTBEREICH;   { global }
  {* gstellanz gibt die Anzahl der am Anfang auf Illegalitaet zu
     pruefenden oder der im jeweils letzten Halbzug als gewinnbar
     erkannten Stellungen fuer beide Farben dar *}
  estellanz : ARRAY[weiss..schwarz] OF ESTBEREICH;   { global }
  {* estellanz enthaelt die Anzahl der mittels der geladenen Daten
     bewerteten Stellungen, die bei der Analyse als Endstellungen
     benutzt werden *}
  bhzstellanz : ARRAY[weiss..schwarz] OF BHZSTELLUNGSANZ;   { global }
  {* bhzstellanz enthaelt fuer jeden Halbzug, ausser dem nullten, die
     Anzahl der mittels der geladenen Daten bewerteten Stellungen, die
     bei der Analyse nicht als Endstellungen benutzt werden *}
  wgewstanz,sgewstanz : HZSPSTELLUNGSANZ;   { global }
  {* Variablen geben die Anzahl der gewinnbaren Stellungen fuer jeden
     Halbzug fuer die jeweilige Farbe an *}
  legalanz,mattanz,pattanz,tremisanz,remisanz : SPSTELLUNGSANZ;   { global }
  {* Variablen geben die Anzahl der Stellungen der jeweiligen Art an *}
  wgewinanz,sgewinanz : SPSTELLUNGSANZ;   { global }
  {* Variablen geben die Anzahl der insgesamt gewinnbaren Stellungen fuer
     die jeweilige Farbe an *}
  farbe3,farbe4 : FARBEN;   { global }
  {* Variablen geben die Farben der zu ladenden dritten und vierten Figur
     an *}
  grhzamin : BSTWERT;   { global }
  {* grhzamin gibt den groessten Stellungswert einer geladenen und fuer
     die Analyse auch benoetigten Stellung an *}
  wgrhzanz,sgrhzanz : BSTWERT;   { global }
  {* Variablen geben den groessten Stellungswert einer geladenen und bei
     die Analyse auch benutzten Stellung an, die fuer Weiss bzw. fuer
     Schwarz gewonnen ist *}
  stindex : 0..1;   { global }
  {* stindex schaltet nach jedem analysierten Halbzug zwischen zwei Listen
     von als illegal erkannten oder gewinnbaren Stellungen hin und her, so
     dass die neue Liste geschrieben werden kann waehrend die alte noch
     benutzt wird *}
  hzanz : GSTWERT;
  farbe : FARBEN;
  abbrechen : BOOLEAN;   { global }
  {* abbrechen gibt an, ob die Analyse, z.B. wegen Ueberschreiten einer
     Konstanten, abgebrochen werden muss *}
  vorstellpruefen : BOOLEAN;   { global }
  {* vorstellpruefen gibt an, ob geprueft werden soll, ob eine Stellung
     eine legale Vorgaengerstellung hat, wobei sie, wenn eine solche nicht
     existiert, illegal waere *}
  schnellstmatt : BOOLEAN;   { global }
  {* schnellstmatt gibt an, ob die Analyse unter der Vorgabe des schnellsten
     Gewinns und nicht des schnellsten Matts erfolgen soll *}
  bvorhanden : BOOLEAN;   { global }
  {* bvorhanden gibt an, ob im zu analysierenden Endspiel ein Bauer
     vorhanden ist, d.h. die dritte oder vierte Figur ein Bauer ist *}
  fig34gleich : BOOLEAN;   { global }
  {* fig34gleich gibt an, ob die beiden vorgegebenen Figuren gleich sind,
     d.h. in Art und Farbe uebereinstimen *}
  untersuchung : BOOLEAN;
  figur3,figur4 : CHAR;   { global }
  {* Variablen geben die Arten dritten und vierten Figur an *}
  chin : CHAR;


FUNCTION StellartTyp(stlartwert:INTEGER):STELLART;
{*
Beschreibung:
  Funktion wandelt den uebergebenen INTEGER-Wert, der eine Stellungsart
  repraesentiert, in die zugehoerige Stellungsart um.
Parameter:
  stlartwert = INTEGER-Wert, der einen STELLART-Wert repraesentiert
Benutzte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
VAR
  stlart:STELLART;
  stlartnr:INTEGER;
BEGIN
  stlart:=illegal;
  FOR stlartnr:=ORD(illegal) TO stlartwert-1 DO
    stlart:=SUCC(stlart);
  StellartTyp:=stlart;
END;   { StellartTyp }

PROCEDURE InitDaten;
{*
Beschreibung:
  Prozedur initialisiert einige globale Variablen.
Parameter:
  Keine
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  brett, wgewstanz, sgewstanz, bhzstellanz, gstellanz, estellanz,
  legalanz, mattanz, pattanz, tremisanz, remisanz, grhzamin, wgrhzanz,
  sgrhzanz, abbrechen
Vorbedingungen:
  Keine
*}
VAR
  bhzanz:BSTWERT;
  ghzanz:GSTWERT;
  spalte,zeile:SZBEREICH;
  farbe:FARBEN;
  feldnr:INTEGER;
BEGIN
  FOR feldnr:=-1 TO 10 DO
    BEGIN
    brett[-1,feldnr]:=FALSE;
    brett[0,feldnr]:=FALSE;
    brett[9,feldnr]:=FALSE;
    brett[10,feldnr]:=FALSE;
    brett[feldnr,-1]:=FALSE;
    brett[feldnr,0]:=FALSE;
    brett[feldnr,9]:=FALSE;
    brett[feldnr,10]:=FALSE;
    END;  { FOR }
  FOR spalte:=1 TO 8 DO
    FOR zeile:=1 TO 8 DO
      brett[spalte,zeile]:=TRUE;
  FOR farbe:=weiss TO schwarz DO
    BEGIN
    FOR ghzanz:=0 TO maxghzuganz DO
      BEGIN
      wgewstanz[ghzanz,farbe]:=0;
      sgewstanz[ghzanz,farbe]:=0;
      END;  { FOR }
    FOR bhzanz:=1 TO maxbhzuganz DO
      bhzstellanz[farbe,bhzanz]:=0;
    gstellanz[0,farbe]:=0;
    gstellanz[1,farbe]:=0;
    estellanz[farbe]:=0;
    legalanz[farbe]:=0;
    mattanz[farbe]:=0;
    pattanz[farbe]:=0;
    tremisanz[farbe]:=0;
    remisanz[farbe]:=0;
    END;  { FOR }
  grhzamin:=0;
  wgrhzanz:=0;
  sgrhzanz:=0;
  abbrechen:=FALSE;
END;   { InitDaten }

PROCEDURE FigurenEinlesen;
{*
Beschreibung:
  Prozedur liest Farbe und Art der vorzugebenden dritten und vierten
  Figur ein.
Parameter:
  Keine
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  farbe3, figur3, farbe4, figur4, bvorhanden
Vorbedingungen:
  Keine
*}
VAR
  gelesen:BOOLEAN;
  chin:CHAR;
BEGIN
  WRITELN;
  WRITELN;
  WRITELN('Bestimmen der Figuren');
  REPEAT
    WRITELN;
    WRITE('  Farbe der 3. Figur (w/s) : ');
    READLN(chin);
  UNTIL (chin='w') OR (chin='s');
  IF chin='w' THEN
      farbe3:=weiss
    ELSE
      farbe3:=schwarz;
  REPEAT
    WRITELN;
    WRITE('  Art der 3. Figur (T/S/L/D/a..h/x) : ');
    READLN(chin);
    IF (chin='T') OR (chin='S') OR (chin='L') OR (chin='D')
     OR (chin>='a') AND (chin<='h') OR (chin='x') THEN
        gelesen:=TRUE
      ELSE
        gelesen:=FALSE;
  UNTIL gelesen;
  figur3:=chin;
  REPEAT
    WRITELN;
    WRITE('  Farbe der 4. Figur (w/s) : ');
    READLN(chin);
  UNTIL (chin='w') OR (chin='s');
  IF chin='w' THEN
      farbe4:=weiss
    ELSE
      farbe4:=schwarz;
  REPEAT
    WRITELN;
    WRITE('  Art der 4. Figur (T/S/L/D/a..h/x) : ');
    READLN(chin);
    IF (chin='T') OR (chin='S') OR (chin='L') OR (chin='D')
     OR (chin>='a') AND (chin<='h') OR (chin='x') THEN
        gelesen:=TRUE
      ELSE
        gelesen:=FALSE;
  UNTIL gelesen;
  figur4:=chin;
  bvorhanden:=(figur3>='a') OR (figur4>='a');
  fig34gleich:=(figur3=figur4) AND (farbe3=farbe4);
END;   { FigurenEinlesen }

PROCEDURE BStellToPwerte(bstell:BSTELLUNG;
                         VAR sks,skz,wks,wkz,f3s,f3z:SZBEREICH);
{*
Beschreibung:
  Prozedur ermitteln aus der Darstellung der uebergebenen Stellung
  eines 3-Steine-Endspiels die Zeilen und Spalten der Figuren.
Parameter:
  bstell = Die zu wandelnde Stellung
  sks,skz,wks,wkz,f3s,f3z = Variablen fuer die Zeilen und Spalten der
                            Figuren
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
BEGIN
  sks:=bstell.sks;
  skz:=bstell.skz;
  wks:=bstell.wks;
  wkz:=bstell.wkz;
  f3s:=bstell.f3s;
  f3z:=bstell.f3z;
END;   { BStellToPwerte }

PROCEDURE PwerteToBStell(sks,skz,wks,wkz,f3s,f3z:SZBEREICH;
                         VAR bstell:BSTELLUNG);
{*
Beschreibung:
  Prozedur ermitteln aus den uebergebenen Zeilen und Spalten der Figuren
  eines 3-Steine-Endspiels die Darstellung zugehoerigen Stellung.
Parameter:
  sks,skz,wks,wkz,f3s,f3z = Zeilen und Spalten der Figuren
  bstell = Variable fuer die ermittelte Stellung
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
BEGIN
  bstell.sks:=sks;
  bstell.skz:=skz;
  bstell.wks:=wks;
  bstell.wkz:=wkz;
  bstell.f3s:=f3s;
  bstell.f3z:=f3z;
END;   { PwerteToBStell }

PROCEDURE StellToPwerte(stell:STELLUNG;
                        VAR sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH);
{*
Beschreibung:
  Prozedur ermitteln aus der Darstellung der uebergebenen Stellung die
  Zeilen und Spalten der Figuren.
Parameter:
  stell = Die zu wandelnde Stellung
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z = Variablen fuer die Zeilen und Spalten
                                    der Figuren
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
BEGIN
  sks:=stell.sks;
  skz:=stell.skz;
  wks:=stell.wks;
  wkz:=stell.wkz;
  f3s:=stell.f3s;
  f3z:=stell.f3z;
  f4s:=stell.f4s;
  f4z:=stell.f4z;
END;   { StellToPwerte }

PROCEDURE PwerteToStell(sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
                        VAR stell:STELLUNG);
{*
Beschreibung:
  Prozedur ermitteln aus den uebergebenen Zeilen und Spalten der Figuren
  die Darstellung der zugehoerigen Stellung.
Parameter:
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z = Zeilen und Spalten der Figuren
  stell = Variable fuer die ermittelte Stellung
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
BEGIN
  stell.sks:=sks;
  stell.skz:=skz;
  stell.wks:=wks;
  stell.wkz:=wkz;
  stell.f3s:=f3s;
  stell.f3z:=f3z;
  stell.f4s:=f4s;
  stell.f4z:=f4z;
END;   { PwerteToStell }

PROCEDURE SpiegeleBStellArt(fig3:CHAR;bstell:BSTELLUNG;art:STELLART;
                            VAR bstlarten:BSTELLUNGSARTEN);
{*
Beschreibung:
  Prozedur weist in dem uebergebenen Feld von Stellungsarten eines
  3-Steine-Endspiels allen gespiegelten Vorkommen der uebergebenen
  Stellung die uebergebene Stellungsart zu.
Parameter:
  fig3 = Die Art der dritten Figur der Stellung
  bstell = Die Stellung, deren Art zugewiesen wird
  art = Die Stellungsart, die zugewiesen wird
  bstlarten = Variable des Feldes der Stellungsarten, an das die
              Zuweisungen erfolgen
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
VAR
  sks,skz,wks,wkz,f3s,f3z:SZBEREICH;
BEGIN
  BStellToPwerte(bstell,sks,skz,wks,wkz,f3s,f3z);
  bstlarten[sks,skz,wks,wkz,f3s,f3z]:=art;
  bstlarten[9-sks,skz,9-wks,wkz,9-f3s,f3z]:=art;
  IF fig3<'a' THEN
    BEGIN
    bstlarten[sks,9-skz,wks,9-wkz,f3s,9-f3z]:=art;
    bstlarten[9-sks,9-skz,9-wks,9-wkz,9-f3s,9-f3z]:=art;
    bstlarten[skz,sks,wkz,wks,f3z,f3s]:=art;
    bstlarten[9-skz,sks,9-wkz,wks,9-f3z,f3s]:=art;
    bstlarten[skz,9-sks,wkz,9-wks,f3z,9-f3s]:=art;
    bstlarten[9-skz,9-sks,9-wkz,9-wks,9-f3z,9-f3s]:=art;
    END;  { IF }
END;   { SpiegeleBStellArt }

PROCEDURE SpiegeleBStellWert(fig3:CHAR;bstell:BSTELLUNG;wert:GSTWERT;
                             VAR bstlwerte:BSTELLUNGSWERTE);
{*
Beschreibung:
  Prozedur weist in dem uebergebenen Feld von Stellungswerten eines
  3-Steine-Endspiels allen gespiegelten Vorkommen der uebergebenen
  Stellung den uebergebenen Stellungswert zu.
Parameter:
  fig3 = Die Art der dritten Figur der Stellung
  bstell = Die Stellung, deren Wert zugewiesen wird
  wert = Der Stellungswert, der zugewiesen wird
  bstlwerte = Variable des Feldes der Stellungswerte, an das die
              Zuweisungen erfolgen
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
VAR
  sks,skz,wks,wkz,f3s,f3z:SZBEREICH;
BEGIN
  BStellToPwerte(bstell,sks,skz,wks,wkz,f3s,f3z);
  bstlwerte[sks,skz,wks,wkz,f3s,f3z]:=wert;
  bstlwerte[9-sks,skz,9-wks,wkz,9-f3s,f3z]:=wert;
  IF fig3<'a' THEN
    BEGIN
    bstlwerte[sks,9-skz,wks,9-wkz,f3s,9-f3z]:=wert;
    bstlwerte[9-sks,9-skz,9-wks,9-wkz,9-f3s,9-f3z]:=wert;
    bstlwerte[skz,sks,wkz,wks,f3z,f3s]:=wert;
    bstlwerte[9-skz,sks,9-wkz,wks,9-f3z,f3s]:=wert;
    bstlwerte[skz,9-sks,wkz,9-wks,f3z,9-f3s]:=wert;
    bstlwerte[9-skz,9-sks,9-wkz,9-wks,9-f3z,9-f3s]:=wert;
    END;  { IF }
END;   { SpiegeleBStellWert }

PROCEDURE SpiegeleStellArt(fig3,fig4:CHAR;stell:STELLUNG;art:STELLART;
                           VAR stlarten:STELLUNGSARTEN);
{*
Beschreibung:
  Prozedur weist in dem uebergebenen Feld von Stellungsarten allen
  gespiegelten Vorkommen der uebergebenen Stellung die uebergebene
  Stellungsart zu.
Parameter:
  fig3,fig4 = Die Arten der dritten und vierten Figur der Stellung
  stell = Die Stellung, deren Art zugewiesen wird
  art = Die Stellungsart, die zugewiesen wird
  stlarten = Variable des Feldes der Stellungsarten, an das die
             Zuweisungen erfolgen
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
VAR
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
BEGIN
  StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  stlarten[sks,skz,wks,wkz,f3s,f3z,f4s,f4z]:=art;
  stlarten[9-sks,skz,9-wks,wkz,9-f3s,f3z,9-f4s,f4z]:=art;
  IF (fig3<'a') AND (fig4<'a') THEN
    BEGIN
    stlarten[sks,9-skz,wks,9-wkz,f3s,9-f3z,f4s,9-f4z]:=art;
    stlarten[9-sks,9-skz,9-wks,9-wkz,9-f3s,9-f3z,9-f4s,9-f4z]:=art;
    stlarten[skz,sks,wkz,wks,f3z,f3s,f4z,f4s]:=art;
    stlarten[9-skz,sks,9-wkz,wks,9-f3z,f3s,9-f4z,f4s]:=art;
    stlarten[skz,9-sks,wkz,9-wks,f3z,9-f3s,f4z,9-f4s]:=art;
    stlarten[9-skz,9-sks,9-wkz,9-wks,9-f3z,9-f3s,9-f4z,9-f4s]:=art;
    END;  { IF }
END;   { SpiegeleStellArt }

PROCEDURE SpiegeleStellWert(fig3,fig4:CHAR;stell:STELLUNG;wert:GSTWERT;
                            VAR stlwerte:STELLUNGSWERTE);
{*
Beschreibung:
  Prozedur weist in dem uebergebenen Feld von Stellungswerten allen
  gespiegelten Vorkommen der uebergebenen Stellung den uebergebenen
  Stellungswert zu.
Parameter:
  fig3,fig4 = Die Arten der dritten und vierten Figur der Stellung
  stell = Die Stellung, deren Wert zugewiesen wird
  wert = Der Stellungswert, der zugewiesen wird
  stlwerte = Variable des Feldes der Stellungswerte, an das die
             Zuweisungen erfolgen
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
VAR
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
BEGIN
  StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  stlwerte[sks,skz,wks,wkz,f3s,f3z,f4s,f4z]:=wert;
  stlwerte[9-sks,skz,9-wks,wkz,9-f3s,f3z,9-f4s,f4z]:=wert;
  IF (fig3<'a') AND (fig4<'a') THEN
    BEGIN
    stlwerte[sks,9-skz,wks,9-wkz,f3s,9-f3z,f4s,9-f4z]:=wert;
    stlwerte[9-sks,9-skz,9-wks,9-wkz,9-f3s,9-f3z,9-f4s,9-f4z]:=wert;
    stlwerte[skz,sks,wkz,wks,f3z,f3s,f4z,f4s]:=wert;
    stlwerte[9-skz,sks,9-wkz,wks,9-f3z,f3s,9-f4z,f4s]:=wert;
    stlwerte[skz,9-sks,wkz,9-wks,f3z,9-f3s,f4z,9-f4s]:=wert;
    stlwerte[9-skz,9-sks,9-wkz,9-wks,9-f3z,9-f3s,9-f4z,9-f4s]:=wert;
    END;  { IF }
END;   { SpiegeleStellWert }

PROCEDURE FigWegDatenLaden(farbe:FARBEN;figur:CHAR;ofarbe:FARBEN;
                           VAR ostarten:BSTELLUNGSARTEN;
                           VAR ostwerte:BSTELLUNGSWERTE);
{*
Beschreibung:
  Prozedur laed die Daten (analysierte Bewertungen) des Endspiels, in
  dem gegenueber dem vorgegebenen Endspiel die uebergebene Figur
  geschlagen wurde.
Parameter:
  farbe = Die Farbe der noch vorhandenen Figur
  figur = Die Art der noch vorhandenen Figur
  ofarbe = Die Farbe der Figur, die geschlagen wurde
  ostarten = Variable des Feldes, an das die geladenen Stellungsarten
             zugewiesen werden
  ostwerte = Variable des Feldes, an das die geladenen Stellungswerte
             zugewiesen werden
Benutzte globale Variablen:
  schnellstmatt
Veraenderte globale Variablen:
  abbrechen
Vorbedingungen:
  Keine
*}
VAR
  bstell:BSTELLUNG;
  stlart:STELLART;
  sks,wks,wkz,f3s,f3z:SZBEREICH;
  skz:INTEGER;
  figanz,figcode,farbcode,vspcode,smcode:INTEGER;
  stlcode,stlwert:INTEGER;
  odatname:STRING(31);
  oanlydaten:FILE OF DATBEREICH;
BEGIN
  WRITELN;
  WRITE('  Laden der Daten vom Endspiel mit');
  IF farbe=weiss THEN
      WRITE('  weisse')
    ELSE
      WRITE('  schwarze');
  IF figur>='a' THEN
      WRITELN('m ',figur,'-Bauer')
    ELSE
      CASE figur OF
        'D' : WRITELN('r Dame');
        'T' : WRITELN('m Turm');
        'L' : WRITELN('m Laeufer');
        'S' : WRITELN('m Springer');
      END;  { CASE }
  WRITELN;
  WRITE('    Name der entsprechenden Datei = ');
  READLN(odatname);
  RESET(oanlydaten,odatname);
  READ(oanlydaten,figanz);
  IF figanz<>3 THEN
      BEGIN
      WRITELN;
      WRITELN('    Daten von falschem Endspiel mit ',figanz:1,' Steinen');
      abbrechen:=TRUE;
      END
    ELSE
      BEGIN
      READ(oanlydaten,farbcode);
      READ(oanlydaten,figcode);
      IF (farbcode<>ORD(farbe)) OR (figcode<>ORD(figur)) THEN
          BEGIN
          WRITELN;
          WRITELN('    Daten fuer falsche Steine');
          abbrechen:=TRUE;
          END;  { IF }
      END;  { ELSE }
  IF NOT(abbrechen) THEN
    BEGIN
    READ(oanlydaten,vspcode);
    IF vspcode=ORD(TRUE) THEN
      BEGIN
      WRITELN;
      WRITELN('    Daten nicht zum Laden in anderen Analysen geeignet');
      abbrechen:=TRUE;
      END;  { IF }
    END;  { IF }
  IF NOT(abbrechen) THEN
    BEGIN
    READ(oanlydaten,smcode);
    IF schnellstmatt AND (smcode=ORD(FALSE)) THEN
      BEGIN
      WRITELN;
      WRITELN('    Daten nicht aus Analyse fuer schnellstes Matt');
      abbrechen:=TRUE;
      END;  { IF }
    END;  { IF }
  IF NOT(abbrechen) THEN
    BEGIN
    FOR sks:=1 TO 4 DO
      BEGIN
      skz:=0;
      WHILE (skz<sks) OR (figur>='a') AND (skz<8) DO
        BEGIN
        skz:=skz+1;
        FOR wks:=1 TO 8 DO
         FOR wkz:=1 TO 8 DO
          FOR f3s:=1 TO 8 DO
           FOR f3z:=1 TO 8 DO
             IF NOT(abbrechen) THEN
               BEGIN
               PwerteToBStell(sks,skz,wks,wkz,f3s,f3z,bstell);
               READ(oanlydaten,stlcode);
               READ(oanlydaten,stlwert);
               IF ofarbe=schwarz THEN
                 BEGIN
                 READ(oanlydaten,stlcode);
                 READ(oanlydaten,stlwert);
                 END;  { IF }
               IF stlwert>maxbhzuganz THEN
                   BEGIN
                   WRITELN;
                   WRITELN;
                   WRITELN('Konstante  maxbhzuganz  zu klein');
                   abbrechen:=TRUE;
                   END
                 ELSE
                   BEGIN
                   stlart:=StellartTyp(stlcode);
                   SpiegeleBStellArt(figur,bstell,stlart,ostarten);
                   SpiegeleBStellWert(figur,bstell,stlwert,ostwerte);
                   IF ofarbe=weiss THEN
                     BEGIN
                     READ(oanlydaten,stlcode);
                     READ(oanlydaten,stlwert);
                     END;  { IF }
                   END;  { ELSE }
               END;  { IF }
        END;  { WHILE }
      END;  { FOR }
    END;  { IF }
  WRITELN;
  IF abbrechen THEN
      WRITELN('    Daten nicht geladen')
    ELSE
      WRITELN('    Daten geladen');
END;   { FigWegDatenLaden }

PROCEDURE SchlagDatenLaden;
{*
Beschreibung:
  Prozedur laed die Daten (analysierte Bewertungen) der Endspiele, in
  denen gegenueber dem vorgegebenen Endspiel eine Figur geschlagen wurde.
Parameter:
  Keine
Benutzte globale Variablen:
  farbe3, farbe4, figur3, figur4, schnellstmatt (indirekt)
Veraenderte globale Variablen:
  o3starten, o4starten, o3stwerte, o4stwerte, abbrechen (indirekt)
Vorbedingungen:
  Keine
*}
BEGIN
  WRITELN;
  WRITELN;
  WRITELN('Laden der Analysedaten der zugehoerigen 3-Steine-Endspiele');
  IF figur3>='a' THEN
      FigWegDatenLaden(farbe3,'x',farbe4,o4starten,o4stwerte)
    ELSE
      FigWegDatenLaden(farbe3,figur3,farbe4,o4starten,o4stwerte);
  IF NOT(abbrechen) THEN
    IF ((figur4=figur3) OR (figur4>='a') AND (figur3>='a'))
     AND (farbe4=farbe3) THEN
        BEGIN
        o3starten:=o4starten;
        o3stwerte:=o4stwerte;
        END
      ELSE
        IF figur4>='a' THEN
            FigWegDatenLaden(farbe4,'x',farbe3,o3starten,o3stwerte)
          ELSE
            FigWegDatenLaden(farbe4,figur4,farbe3,o3starten,o3stwerte);
END;   { SchlagDatenLaden }

PROCEDURE UmwandelDatenLaden;
{*
Beschreibung:
  Prozedur laed die Daten (analysierte Bewertungen) der zum vorgegebenen
  Endspiel gehoerenden Umwandelungsendspiele.
Parameter:
  Keine
Benutzte globale Variablen:
  farbe3, farbe4, figur3, figur4, schnellstmatt
Veraenderte globale Variablen:
  wdstarten, wdstwerte, abbrechen
Vorbedingungen:
  Keine
*}
VAR
  stell:STELLUNG;
  stlart:STELLART;
  uwfarbe,farbe:FARBEN;
  figur:CHAR;
  sks,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  skz:INTEGER;
  figanz,figcode3,farbcode3,figcode4,farbcode4,vspcode,smcode:INTEGER;
  stlcode,stlwert:INTEGER;
  ddatname:STRING(31);
  danlydaten:FILE OF DATBEREICH;
BEGIN
  WRITELN;
  WRITELN;
  WRITELN('Laden der Analysedaten der Umwandlungs-Endspiele');
  FOR uwfarbe:=weiss TO schwarz DO
    BEGIN
    IF (farbe3=uwfarbe) AND (figur3>='a') THEN
        BEGIN
        farbe:=farbe4;
        IF figur4>='a' THEN
            figur:='x'
          ELSE
            figur:=figur4;
        END
      ELSE  IF (farbe4=uwfarbe) AND (figur4>='a') THEN
        BEGIN
        farbe:=farbe3;
        IF figur3>='a' THEN
            figur:='x'
          ELSE
            figur:=figur3;
        END
      ELSE
        figur:='X';
    IF (figur<>'X') AND NOT(abbrechen) THEN
      BEGIN
      WRITELN;
      WRITE('  Laden der Daten vom Endspiel mit');
      IF uwfarbe=weiss THEN
          WRITE('  weisser Dame  und')
        ELSE 
          WRITE('  schwarzer Dame  und');
      IF farbe=weiss THEN
          WRITE('  weisse')
        ELSE
          WRITE('  schwarze');
      IF figur>='a' THEN
          WRITELN('m ',figur,'-Bauer')
        ELSE
          CASE figur OF
            'D' : WRITELN('r Dame');
            'T' : WRITELN('m Turm');
            'L' : WRITELN('m Laeufer');
            'S' : WRITELN('m Springer');
          END;  { CASE }
      WRITELN;
      WRITE('    Name der entsprechenden Datei = ');
      READLN(ddatname);
      RESET(danlydaten,ddatname);
      READ(danlydaten,figanz);
      IF figanz<>4 THEN
          BEGIN
          WRITELN;
          WRITELN('    Daten von falschem Endspiel mit ',figanz:1,' Steinen');
          abbrechen:=TRUE;
          END
        ELSE
          BEGIN
          READ(danlydaten,farbcode3);
          READ(danlydaten,figcode3);
          READ(danlydaten,farbcode4);
          READ(danlydaten,figcode4);
          IF ((farbcode3<>ORD(uwfarbe)) OR (figcode3<>ORD('D'))
           OR (farbcode4<>ORD(farbe)) OR (figcode4<>ORD(figur)))
           AND ((farbcode3<>ORD(farbe)) OR (figcode3<>ORD(figur))
           OR (farbcode4<>ORD(uwfarbe)) OR (figcode4<>ORD('D'))) THEN
              BEGIN
              WRITELN;
              WRITELN('    Daten fuer falsche Steine');
              abbrechen:=TRUE;
              END;  { IF }
          END;  { ELSE }
      IF NOT(abbrechen) THEN
        BEGIN
        READ(danlydaten,vspcode);
        IF vspcode=ORD(TRUE) THEN
          BEGIN
          WRITELN;
          WRITELN('    Daten nicht zum Laden in anderen Analysen geeignet');
          abbrechen:=TRUE;
          END;  { IF }
        END;  { IF }
      IF NOT(abbrechen) THEN
        BEGIN
        READ(danlydaten,smcode);
        IF schnellstmatt AND (smcode=ORD(FALSE)) THEN
          BEGIN
          WRITELN;
          WRITELN('    Daten nicht aus Analyse fuer schnellstes Matt');
          abbrechen:=TRUE;
          END;  { IF }
        END;  { IF }
      IF NOT(abbrechen) THEN
        BEGIN
        FOR sks:=1 TO 4 DO
          BEGIN
          skz:=0;
          WHILE (skz<sks) OR (figur>='a') AND (skz<8) DO
            BEGIN
            skz:=skz+1;
            FOR wks:=1 TO 8 DO
             FOR wkz:=1 TO 8 DO
              FOR f3s:=1 TO 8 DO
               FOR f3z:=1 TO 8 DO
                FOR f4s:=1 TO 8 DO
                 FOR f4z:=1 TO 8 DO
                   BEGIN
                   IF NOT(abbrechen) THEN
                     BEGIN
                     READ(danlydaten,stlcode);
                     READ(danlydaten,stlwert);
                     IF uwfarbe=weiss THEN
                       BEGIN
                       READ(danlydaten,stlcode);
                       READ(danlydaten,stlwert);
                       END;  { IF }
                     IF stlwert>maxbhzuganz THEN
                       BEGIN
                       WRITELN;
                       WRITELN;
                       WRITELN('Konstante  maxbhzuganz  zu klein');
                       abbrechen:=TRUE;
                       END;  { IF }
                     END;  { IF }
                   IF NOT(abbrechen) THEN
                     BEGIN
                     stlart:=StellartTyp(stlcode);
                     IF (farbcode3=ORD(farbe)) AND (figcode3=ORD(figur)) THEN
                         PwerteToStell(sks,skz,wks,wkz,f3s,f3z,f4s,f4z,stell)
                       ELSE
                         PwerteToStell(sks,skz,wks,wkz,f4s,f4z,f3s,f3z,stell);
                     IF uwfarbe=weiss THEN
                         BEGIN
                         SpiegeleStellArt('D',figur,stell,stlart,wdstarten);
                         SpiegeleStellWert('D',figur,stell,stlwert,wdstwerte);
                         END
                       ELSE
                         BEGIN
                         SpiegeleStellArt('D',figur,stell,stlart,sdstarten);
                         SpiegeleStellWert('D',figur,stell,stlwert,sdstwerte);
                         END;  { ELSE }
                     IF uwfarbe=schwarz THEN
                       BEGIN
                       READ(danlydaten,stlcode);
                       READ(danlydaten,stlwert);
                       END;  { IF }
                     END;  { IF }
                   END;  { FOR }
            END;  { WHILE }
          END;  { FOR }
        END;  { IF }
      WRITELN;
      IF abbrechen THEN
          WRITELN('    Daten nicht geladen')
        ELSE
          WRITELN('    Daten geladen');
      END;  { IF }
    END;  { FOR }
END;   { UmwandelDatenLaden }

FUNCTION FigVertauschbar(farbex,farbey:FARBEN;figurx,figury:CHAR;
                         fxs,fxz,fys,fyz:SZBEREICH):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn die beiden uebergebenen
  Figuren vertauscht werden koennen, ohne dass eine neue Stellung
  entsteht. Bauern, die keine x-Bauern sind, muessen dabei weiterhin
  auf einem fuer sie legalen Feld stehen.
Parameter:
  farbex, farbey = Die Farben der beiden Figuren, fuer die ueberprueft
                   wird, ob sie vertauschbar sind
  figurx, figury = Die Art der beiden Figuren, fuer die ueberprueft
                   wird, ob sie vertauschbar sind
  fxs,fxz,fys,fyz = Die Spalten und Zeilen der Figuren, fuer die
                    ueberprueft wird, ob sie vertauschbar sind
Benutzte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
BEGIN
  IF farbex<>farbey THEN
      FigVertauschbar:=FALSE
    ELSE  IF figurx=figury THEN
      FigVertauschbar:=TRUE
    ELSE
      IF (figurx>='a') AND (figury>='a') THEN
          FigVertauschbar:=(fxs=fys)
        ELSE
          FigVertauschbar:=FALSE;
END;   { FigVertauschbar }

FUNCTION Symetriestellung(stell:STELLUNG):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn die uebergebene Stellung
  bezueglich einer auf sie angewandten Spiegelung symetrisch ist, d.h.
  wenn kein Bauer vorhanden ist und alle Figuren auf der gleichen
  langen Brettdiagonalen stehen oder nur die beiden Koenige auf der
  Diagonalen stehen und die beiden anderen Figuren gleich sind und
  spieglesymetrisch stehen.
Parameter:
  stell = Die Stellung, fuer die der Test erfolgt
Benutzte globale Variablen:
  bvorhanden, fig34gleich
Vorbedingungen:
  Keine
*}
VAR
  diagonalsym,spigelsym:BOOLEAN;
BEGIN
  IF bvorhanden THEN
      Symetriestellung:=FALSE
    ELSE
      BEGIN
      WITH stell DO
        BEGIN
        IF (sks=skz) AND (wks=wkz) AND (f3s=f3z) AND (f4s=f4z)
         OR (sks=9-skz) AND (wks=9-wkz) AND (f3s=9-f3z) AND (f4s=9-f4z) THEN
            diagonalsym:=TRUE
          ELSE
            diagonalsym:=FALSE;
        IF NOT(fig34gleich) THEN
            spigelsym:=FALSE
          ELSE
            IF (sks=skz) AND (wks=wkz) AND (f3s=f4z) AND (f3z=f4s)
             OR (sks=9-skz) AND (wks=9-wkz)
             AND (f3s=9-f4z) AND (f3z=9-f4s) THEN
              spigelsym:=TRUE
            ELSE
              spigelsym:=FALSE;
        END;  { WITH }
      Symetriestellung:=(diagonalsym OR spigelsym);
      END;  { ELSE }
END;   { Symetriestellung }

PROCEDURE SetzeStellungsArt(farbe:FARBEN;stell:STELLUNG;art:STELLART;
                            VAR fstanz:INTEGER);
{*
Beschreibung:
  Prozedur weist allen gespiegelten Vorkommen der uebergebenen Stellung
  mit der uebergebenen Farbe am Zug die uebergebene Stellungsart zu und
  liefert die Anzahl ihrer Vorkommen (Anzahl der gespiegelten Stellungen).
Parameter:
  farbe = Farbe, die in der Stellung am Zug ist
  stell = Stellung, deren gespiegelten Vorkommen die Art zugewiesen wird
  art = Stellungsart, die zugewiesen wird
  fstanz = Variable fuer die Anzahl der Stellungsvorkommen
Benutzte globale Variablen:
  figur3, figur4, figur3, figur4, bvorhanden, fig34gleich (indirekt)
Veraenderte globale Variablen:
  stellarten
Vorbedingungen:
  Keine
*}
VAR
  vstell:STELLUNG;
BEGIN
  SpiegeleStellArt(figur3,figur4,stell,art,stellarten[farbe]);
  WITH stell DO
    IF FigVertauschbar(farbe3,farbe4,figur3,figur4,f3s,f3z,f4s,f4z) THEN
      BEGIN
      vstell:=stell;
      vstell.f3s:=stell.f4s;
      vstell.f3z:=stell.f4z;
      vstell.f4s:=stell.f3s;
      vstell.f4z:=stell.f3z;
      SpiegeleStellArt(figur3,figur4,vstell,art,stellarten[farbe]);
      END;  { IF }
  IF (figur3>='a') AND (figur3<>'x') OR (figur4>='a') AND (figur4<>'x') THEN
      fstanz:=1
    ELSE  IF bvorhanden THEN
      fstanz:=2
    ELSE  IF Symetriestellung(stell) THEN
      fstanz:=4
    ELSE
      fstanz:=8;
END;   { SetzeStellungsArt }

PROCEDURE SetzeStellungsWert(farbe:FARBEN;stell:STELLUNG;wert:GSTWERT);
{*
Beschreibung:
  Prozedur weist allen gespiegelten Vorkommen der uebergebenen Stellung
  mit der uebergebenen Farbe am Zug den uebergebenen Stellungswert zu.
Parameter:
  farbe = Farbe, die in der Stellung am Zug ist
  stell = Stellung, deren gespiegelten Vorkommen der Wert zugewiesen wird
  wert = Stellungswert, der zugewiesen wird
Benutzte globale Variablen:
  farbe3, farbe4, figur3, figur4
Veraenderte globale Variablen:
  stellwerte
Vorbedingungen:
  Keine
*}
VAR
  vstell:STELLUNG;
BEGIN
  SpiegeleStellWert(figur3,figur4,stell,wert,stellwerte[farbe]);
  WITH stell DO
    IF FigVertauschbar(farbe3,farbe4,figur3,figur4,f3s,f3z,f4s,f4z) THEN
      BEGIN
      vstell:=stell;
      vstell.f3s:=stell.f4s;
      vstell.f3z:=stell.f4z;
      vstell.f4s:=stell.f3s;
      vstell.f4z:=stell.f3z;
      SpiegeleStellWert(figur3,figur4,vstell,wert,stellwerte[farbe]);
      END;  { IF }
END;   { SetzeStellungsWert }

PROCEDURE SpiegeleZuegeAnz(fig3,fig4:CHAR;stell:STELLUNG;zganz:ZGBEREICH;
                           VAR mgzuganz:STELLUNGSZANZ);
{*
Beschreibung:
  Prozedur weist in dem uebergebenen Feld von Anzahlen moeglicher
  Zuege allen gespiegelten Vorkommen der uebergebenen Stellungen die
  uebergebene Anzahl moeglicher Zuege zu.
Parameter:
  fig3,fig4 = Die Arten der dritten und vierten Figur der Stellung
  stell = Die Stellung, deren Wert zugewiesen wird
  zganz = Die Anzahl moeglicher Zuege, die zugewiesen wird
  mgzuganz = Variable des Feldes der moeglichen Zuegeanzahlen, an das
             die Zuweisungen erfolgt
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
VAR
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
BEGIN
  StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  mgzuganz[sks,skz,wks,wkz,f3s,f3z,f4s,f4z]:=zganz;
  mgzuganz[9-sks,skz,9-wks,wkz,9-f3s,f3z,9-f4s,f4z]:=zganz;
  IF (fig3<'a') AND (fig4<'a') THEN
    BEGIN
    mgzuganz[sks,9-skz,wks,9-wkz,f3s,9-f3z,f4s,9-f4z]:=zganz;
    mgzuganz[9-sks,9-skz,9-wks,9-wkz,9-f3s,9-f3z,9-f4s,9-f4z]:=zganz;
    mgzuganz[skz,sks,wkz,wks,f3z,f3s,f4z,f4s]:=zganz;
    mgzuganz[9-skz,sks,9-wkz,wks,9-f3z,f3s,9-f4z,f4s]:=zganz;
    mgzuganz[skz,9-sks,wkz,9-wks,f3z,9-f3s,f4z,9-f4s]:=zganz;
    mgzuganz[9-skz,9-sks,9-wkz,9-wks,9-f3z,9-f3s,9-f4z,9-f4s]:=zganz;
    END;  { IF }
END;   { SpiegeleZuegeAnz }

PROCEDURE SetzeZuegeAnz(farbe:FARBEN;stell:STELLUNG;zganz:ZGBEREICH);
{*
Beschreibung:
  Prozedur weist allen gespiegelten Vorkommen der uebergebenen Stellung
  mit der uebergebenen Farbe am Zug die uebergebene Anzahl nicht zum
  Verlust fuehrender Zuege zu.
Parameter:
  farbe = Farbe, die in der Stellung am Zug ist
  stell = Stellung, der die Anzahl der Zugmoeglichkeiten zugewiesen wird
  zganz = Anzahl der Zugmoeglichkeiten, die zugewiesen wird
Benutzte globale Variablen:
  farbe3, farbe4, figur3, figur4
Veraenderte globale Variablen:
  moegzuganz
Vorbedingungen:
  Keine
*}
VAR
  vstell:STELLUNG;
BEGIN
  SpiegeleZuegeAnz(figur3,figur4,stell,zganz,moegzuganz[farbe]);
  WITH stell DO
    IF FigVertauschbar(farbe3,farbe4,figur3,figur4,f3s,f3z,f4s,f4z) THEN
      BEGIN
      vstell:=stell;
      vstell.f3s:=stell.f4s;
      vstell.f3z:=stell.f4z;
      vstell.f4s:=stell.f3s;
      vstell.f4z:=stell.f3z;
      SpiegeleZuegeAnz(figur3,figur4,vstell,zganz,moegzuganz[farbe]);
      END;  { IF }
END;   { SetzeZuegeAnz }

FUNCTION SchachB(farbe:FARBEN;
                 tks,tkz,fbs,fbz,xks,xkz,fxs,fxz:SZBEREICH):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn der angegebenen Farbe durch die
  Figur, deren Spalte und Zeile als zweites uebergeben wird und die ein
  Bauer ist, Schach geboten wird.
Parameter:
  farbe = Die Farbe, fuer die getestet wird, ob ihr Schach geboten wird
  tks,tkz = Spalte und Zeile des Koenigs, fuer den getestet wird, ob er
            im Schach steht
  fbs,fbz = Spalte und Zeile des Bauerns, fuer den getestet wird, ob er
            Schach bietet
  xks,xkz = Spalte und Zeile des anderen Koenigs, fuer den nicht getestet
            wird, ob er im Schach steht
  fxs,fxz = Spalte und Zeile der vierten Figur
Benutzte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
BEGIN
  IF (fbs=fxs) AND (fbz=fxz) THEN
      SchachB:=FALSE
    ELSE
      IF farbe=weiss THEN
          SchachB:=(ABS(tks-fbs)=1) AND (tkz-fbz=-1)
        ELSE
          SchachB:=(ABS(tks-fbs)=1) AND (tkz-fbz=1);
END;   { SchachB }

FUNCTION SchachS(tks,tkz,fss,fsz,xks,xkz,fxs,fxz:SZBEREICH):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn dem Koenig, dessen Spalte und
  Zeile zuerst uebergeben wird, durch die Figur, deren Spalte und Zeile
  als zweites uebergeben wird und die ein Springer ist, Schach geboten
  wird.
Parameter:
  tks,tkz = Spalte und Zeile des Koenigs, fuer den getestet wird, ob er
            im Schach steht
  fss,fsz = Spalte und Zeile des Springers, fuer den getestet wird, ob er
            Schach bietet
  xks,xkz = Spalte und Zeile des anderen Koenigs, fuer den nicht getestet
            wird, ob er im Schach steht
  fxs,fxz = Spalte und Zeile der vierten Figur
Benutzte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
VAR
  sdiff,zdiff:INTEGER;
BEGIN
  IF (fss=fxs) AND (fsz=fxz) THEN
      SchachS:=FALSE
    ELSE
      BEGIN
      sdiff:=ABS(tks-fss);
      zdiff:=ABS(tkz-fsz);
      SchachS:=(sdiff=1) AND (zdiff=2) OR (sdiff=2) AND (zdiff=1);
      END;  { ELSE }
END;   { SchachS }

FUNCTION WertDazwischen(zwert,wert1,wert2:SZBEREICH):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn der Wert zwert zwischen wert1 und
  wert2 liegt, d.h. einer der beiden Werte wert1 und wert2 groesser und
  der andere kleiner ist als zwert.
Parameter:
  zwert = Wert, fuer den geteste wird, ob er zwischen den beiden anderen
          liegt
  wert1,wert2 = Werte, fuer die getestet wird, ob zwert zwischen ihnen
                liegt
Benutzte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
BEGIN
  IF (wert1<zwert) AND (zwert<wert2) OR (wert2<zwert) AND (zwert<wert1) THEN
      WertDazwischen:=TRUE
    ELSE
      WertDazwischen:=FALSE;
END;   { WertDazwischen }

FUNCTION SchachL(tks,tkz,fls,flz,xks,xkz,fxs,fxz:SZBEREICH):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn dem Koenig, dessen Spalte und
  Zeile zuerst uebergeben wird, durch die Figur, deren Spalte und Zeile
  als zweites uebergeben wird und die wie ein Laeufer zieht, Schach
  geboten wird.
Parameter:
  tks,tkz = Spalte und Zeile des Koenigs, fuer den getestet wird, ob er
            im Schach steht
  fss,fsz = Spalte und Zeile der Figur (Laeufer oder Dame), fuer die
            getestet wird, ob sie Schach bietet
  xks,xkz = Spalte und Zeile des anderen Koenigs, fuer den nicht getestet
            wird, ob er im Schach steht
  fxs,fxz = Spalte und Zeile der vierten Figur
Benutzte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
BEGIN
  IF (fls=tks) AND (flz=tkz) OR (fls=fxs) AND (flz=fxz) THEN
      SchachL:=FALSE
    ELSE  IF tks-fls=tkz-flz THEN
      IF (xks-fls=xkz-flz) AND WertDazwischen(xkz,flz,tkz) THEN
          SchachL:=FALSE
        ELSE  IF (fxs-fls=fxz-flz) AND WertDazwischen(fxz,flz,tkz) THEN
          SchachL:=FALSE
        ELSE
          SchachL:=TRUE
    ELSE  IF tks-fls=flz-tkz THEN
      IF (xks-fls=flz-xkz) AND WertDazwischen(xks,fls,tks) THEN
          SchachL:=FALSE
        ELSE  IF (fxs-fls=flz-fxz) AND WertDazwischen(fxs,fls,tks) THEN
          SchachL:=FALSE
        ELSE
          SchachL:=TRUE
    ELSE
      SchachL:=FALSE;
END;   { SchachL }

FUNCTION SchachT(tks,tkz,fts,ftz,xks,xkz,fxs,fxz:SZBEREICH):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn dem Koenig, dessen Spalte und
  Zeile zuerst uebergeben wird, durch die Figur, deren Spalte und Zeile
  als zweites uebergeben wird und die wie ein Turm zieht, Schach
  geboten wird.
Parameter:
  tks,tkz = Spalte und Zeile des Koenigs, fuer den getestet wird, ob er
            im Schach steht
  fss,fsz = Spalte und Zeile der Figur (Turm oder Dame), fuer die
            getestet wird, ob sie Schach bietet
  xks,xkz = Spalte und Zeile des anderen Koenigs, fuer den nicht getestet
            wird, ob er im Schach steht
  fxs,fxz = Spalte und Zeile der vierten Figur
Benutzte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
BEGIN
  IF (fts=tks) AND (ftz=tkz) OR (fts=fxs) AND (ftz=fxz) THEN
      SchachT:=FALSE
    ELSE  IF tks=fts THEN
      IF (xks=fts) AND WertDazwischen(xkz,ftz,tkz) THEN
          SchachT:=FALSE
        ELSE  IF (fxs=fts) AND WertDazwischen(fxz,ftz,tkz) THEN
          SchachT:=FALSE
        ELSE
          SchachT:=TRUE
    ELSE  IF tkz=ftz THEN
      IF (xkz=ftz) AND WertDazwischen(xks,fts,tks) THEN
          SchachT:=FALSE
        ELSE  IF (fxz=ftz) AND WertDazwischen(fxs,fts,tks) THEN
          SchachT:=FALSE
        ELSE
          SchachT:=TRUE
    ELSE
      SchachT:=FALSE;
END;   { SchachT }

FUNCTION Schach(farbe:FARBEN;stell:STELLUNG):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn die angegebene Farbe im Schach
  steht. Dabei wird ein Bauer auf seiner Umwandlungsreihe als Dame 
  betrachtet.
Parameter:
  farbe = Die Farbe, fuer die getestet wird, ob sie im Schach steht
  stell = Die Stellung, fuer die getestet wird, ob der angegebenen Farbe
          darin Schach geboten wird
Benutzte globale Variablen:
  farbe3, farbe4, figur3, figur4
Vorbedingungen:
  Keine
*}
VAR
  tks,tkz,xks,xkz,f3s,f3z,f4s,f4z:SZBEREICH;
  sgeboten:BOOLEAN;
BEGIN
  IF (farbe=farbe3) AND (farbe=farbe4) THEN
      Schach:=FALSE
    ELSE
      BEGIN
      IF farbe=weiss THEN
          StellToPwerte(stell,xks,xkz,tks,tkz,f3s,f3z,f4s,f4z)
        ELSE
          StellToPwerte(stell,tks,tkz,xks,xkz,f3s,f3z,f4s,f4z);
      sgeboten:=FALSE;
      IF farbe<>farbe3 THEN
        IF figur3>='a' THEN
            IF (farbe3=weiss) AND (f3z=8)
             OR (farbe3=schwarz) AND (f3z=1) THEN
                BEGIN
                IF SchachT(tks,tkz,f3s,f3z,xks,xkz,f4s,f4z)
                 OR SchachL(tks,tkz,f3s,f3z,xks,xkz,f4s,f4z) THEN
                  sgeboten:=TRUE;
                END
              ELSE
                sgeboten:=SchachB(farbe,tks,tkz,f3s,f3z,xks,xkz,f4s,f4z)
          ELSE
            CASE figur3 OF
              'T' : sgeboten:=SchachT(tks,tkz,f3s,f3z,xks,xkz,f4s,f4z);
              'S' : sgeboten:=SchachS(tks,tkz,f3s,f3z,xks,xkz,f4s,f4z);
              'L' : sgeboten:=SchachL(tks,tkz,f3s,f3z,xks,xkz,f4s,f4z);
              'D' : IF SchachT(tks,tkz,f3s,f3z,xks,xkz,f4s,f4z)
                     OR SchachL(tks,tkz,f3s,f3z,xks,xkz,f4s,f4z) THEN
                      sgeboten:=TRUE;
            END;  { CASE }
      IF NOT(sgeboten) THEN
        IF farbe<>farbe4 THEN
        IF figur4>='a' THEN
            IF (farbe4=weiss) AND (f4z=8)
             OR (farbe4=schwarz) AND (f4z=1) THEN
                BEGIN
                IF SchachT(tks,tkz,f4s,f4z,xks,xkz,f3s,f3z)
                 OR SchachL(tks,tkz,f4s,f4z,xks,xkz,f3s,f3z) THEN
                  sgeboten:=TRUE;
                END
              ELSE
                sgeboten:=SchachB(farbe,tks,tkz,f4s,f4z,xks,xkz,f3s,f3z)
            ELSE
              CASE figur4 OF
                'T' : sgeboten:=SchachT(tks,tkz,f4s,f4z,xks,xkz,f3s,f3z);
                'S' : sgeboten:=SchachS(tks,tkz,f4s,f4z,xks,xkz,f3s,f3z);
                'L' : sgeboten:=SchachL(tks,tkz,f4s,f4z,xks,xkz,f3s,f3z);
                'D' : IF SchachT(tks,tkz,f4s,f4z,xks,xkz,f3s,f3z)
                       OR SchachL(tks,tkz,f4s,f4z,xks,xkz,f3s,f3z) THEN
                        sgeboten:=TRUE;
              END;  { CASE }
      Schach:=sgeboten;
      END;  { ELSE }
END;  { Schach }

PROCEDURE ErzeugeKPoslist(stein:CHAR;stell:STELLUNG;back,nespstell:BOOLEAN;
                          VAR posliste:POSLIST;VAR posanz:ZGBEREICH);
{*
Beschreibung:
  Prozedur erzeugt fuer den angegebenen Koenig aus der uebergebenen
  Stellung heraus alle Positionen, auf die er legal ziehen kann
  (Nachfolgepositionen) oder von denen aus er auf seine Position in
  der Stellung legal ziehen konnte (Vorgaengerpositionen), wobei
  beruecksichtigt wird, ob die Position auch einer Nachendspielstellung
  angehoeren darf.
Parameter:
  stein = Die Angabe des Steins des Koenigs, fuer den die Positionen
          erzeugt werden, wobei 's' fuer den schwarzen und 'w' fuer den
          weissen Koenig steht
  stell = Die Stellung, fuer die die Nachfolge- bzw. Vorgaengerpositionen
          erzeugt werden
  back = Wenn back TRUE ist, werden die Vorgaengerpositionen erzeugt, wenn
         back FALSE ist, die Nachfolgepositionen
  nespstell = Genau dann, wenn nespstell TRUE ist, werden auch die
              Positionen aus Nachendspielstellungen erzeugt
  posliste = Variable fuer die Liste der erzeugten Positionen
  posanz = Variable fuer die Anzahl der erzeugten Positionen
Benutzte globale Variablen:
  stellarten
Veraenderte globale Variablen:
  brett, abbrechen
Vorbedingungen:
  Die Stellung stell ist initial legal
*}
VAR
  stlart:STELLART;
  posnr:ZGBEREICH;
  nzfarbe:FARBEN;
  eks,ekz,xks,xkz,f3s,f3z,f4s,f4z:SZBEREICH;
  neks,nekz:SZBEREICH;
  sadd,zadd:INTEGER;
BEGIN
  IF stein='w' THEN
      StellToPwerte(stell,xks,xkz,eks,ekz,f3s,f3z,f4s,f4z)
    ELSE
      StellToPwerte(stell,eks,ekz,xks,xkz,f3s,f3z,f4s,f4z);
  posnr:=0;
  IF ((f3s<>xks) OR (f3z<>xkz)) AND ((f4s<>xks) OR (f4z<>xkz))
   AND ((f3s<>f4s) OR (f3z<>f4z)) THEN
    IF back OR ((eks<>f3s) OR (ekz<>f3z)) AND ((eks<>f4s) OR (ekz<>f4z)) THEN
      BEGIN
      IF back THEN
        BEGIN
        brett[f3s,f3z]:=FALSE;
        brett[f4s,f4z]:=FALSE;
        END;  { IF }
      IF back THEN
          IF stein='w' THEN
              nzfarbe:=weiss
            ELSE
              nzfarbe:=schwarz
        ELSE
          IF stein='w' THEN
              nzfarbe:=schwarz
            ELSE
              nzfarbe:=weiss;
      FOR sadd:=-1 TO 1 DO
       FOR zadd:=-1 TO 1 DO
        IF (sadd<>0) OR (zadd<>0) THEN
          IF brett[eks+sadd,ekz+zadd] THEN
            BEGIN
            neks:=eks+sadd;
            nekz:=ekz+zadd;
            IF stein='w' THEN
                stlart:=stellarten[nzfarbe,xks,xkz,neks,nekz,f3s,f3z,f4s,f4z]
              ELSE
                stlart:=stellarten[nzfarbe,neks,nekz,xks,xkz,f3s,f3z,f4s,f4z];
            IF (stlart<>illegal) AND (stlart<>nachesp)
             OR (stlart=nachesp) AND nespstell THEN
              IF posnr<maxzuegeanz THEN
                  BEGIN
                  posnr:=posnr+1;
                  posliste[posnr].spalte:=neks;
                  posliste[posnr].zeile:=nekz;
                  END
                ELSE
                  BEGIN
                  WRITELN;
                  WRITELN;
                  WRITELN('Konstante  maxzuegeanz  zu klein');
                  abbrechen:=TRUE;
                  END;  { ELSE }
            END;  { IF }
      brett[f3s,f3z]:=TRUE;
      brett[f4s,f4z]:=TRUE;
      END;  { IF }
  posanz:=posnr;
END;   { ErzeugeKPoslist }

PROCEDURE ErzeugeBPoslist(stein:CHAR;stell:STELLUNG;back,nespstell:BOOLEAN;
                          VAR posliste:POSLIST;VAR posanz:ZGBEREICH);
{*
Beschreibung:
  Prozedur erzeugt fuer den angegebenen Bauern aus der uebergebenen
  Stellung heraus alle Positionen, auf die er legal ziehen kann
  (Nachfolgepositionen) oder von denen aus er auf seine Position in
  der Stellung legal ziehen konnte (Vorgaengerpositionen), wobei
  beruecksichtigt wird, ob die Position auch einer Nachendspielstellung
  angehoeren darf.
Parameter:
  stein = Die Angabe des Steins des Bauern, wobei der Wert '3' fuer den
          dritten Stein und Wert '4' fuer den vierten Stein steht
  stell = Die Stellung, fuer die die Nachfolge- bzw. Vorgaengerpositionen
          erzeugt werden
  back = Wenn back TRUE ist, werden die Vorgaengerpositionen erzeugt, wenn
         es FALSE ist, die Nachfolgepositionen
  nespstell = Genau dann, wenn nespstell TRUE ist, werden auch die
              Positionen aus Nachendspielstellungen erzeugt
  posliste = Variable fuer die Liste der erzeugten Positionen
  posanz = Variable fuer die Anzahl der erzeugten Positionen
Benutzte globale Variablen:
  farbe3, farbe4, stellarten
Veraenderte globale Variablen:
  brett
Vorbedingungen:
  Die Stellung stell ist initial legal
*}
VAR
  stlart:STELLART;
  bfarbe,nzfarbe:FARBEN;
  fbs,fbz,wks,wkz,sks,skz,fxs,fxz:SZBEREICH;
  nfbz,nfbs:SZBEREICH;
  zadd,sadd:INTEGER;
  schrittanz,schritt:INTEGER;
BEGIN
  IF stein='3' THEN
      StellToPwerte(stell,sks,skz,wks,wkz,fbs,fbz,fxs,fxz)
    ELSE
      StellToPwerte(stell,sks,skz,wks,wkz,fxs,fxz,fbs,fbz);
  posanz:=0;
  IF ((fxs<>sks) OR (fxz<>skz)) AND ((fxs<>wks) OR (fxz<>wkz)) THEN
    BEGIN
    brett[sks,skz]:=FALSE;
    brett[wks,wkz]:=FALSE;
    brett[fxs,fxz]:=FALSE;
    IF stein='3' THEN
        bfarbe:=farbe3
      ELSE
        bfarbe:=farbe4;
    IF back THEN
        nzfarbe:=bfarbe
      ELSE
        IF bfarbe=weiss THEN
            nzfarbe:=schwarz
          ELSE
            nzfarbe:=weiss;
    IF (bfarbe=weiss) AND back OR (bfarbe=schwarz) AND NOT(back) THEN
        zadd:=-1
      ELSE
        zadd:=1;
    IF brett[fbs,fbz] THEN
      BEGIN
      IF (bfarbe=weiss) AND (back AND (fbz=4) OR NOT(back) AND (fbz=2))
       OR (bfarbe=schwarz) AND (back AND (fbz=5) OR NOT(back) AND (fbz=7)) THEN
          schrittanz:=2
        ELSE
          schrittanz:=1;
      schritt:=0;
      nfbz:=fbz;
      WHILE (schritt<schrittanz) AND brett[fbs,nfbz+zadd] DO
        BEGIN
        schritt:=schritt+1;
        nfbz:=nfbz+zadd;
        IF stein='3' THEN
            stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,fbs,nfbz,fxs,fxz]
          ELSE
            stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,fxs,fxz,fbs,nfbz];
        IF (stlart<>illegal) AND (stlart<>nachesp)
         OR (stlart=nachesp) AND nespstell THEN
          BEGIN
          posanz:=posanz+1;
          posliste[posanz].spalte:=fbs;
          posliste[posanz].zeile:=nfbz;
          END;  { IF }
        END;  { WHILE }
      END;  { IF }
    IF back OR brett[fbs,fbz] THEN
      BEGIN
      IF NOT(back) THEN
        brett[fxs,fxz]:=TRUE;
      FOR sadd:=-1 TO 1 DO
       IF sadd<>0 THEN
         IF brett[fbs+sadd,fbz+zadd] THEN
           BEGIN
           nfbs:=fbs+sadd;
           nfbz:=fbz+zadd;
           IF back AND (fxs=fbs) AND (fxz=fbz) 
            OR NOT(back) AND (fxs=nfbs) AND (fxz=nfbz) THEN
             BEGIN
             IF stein='3' THEN
                 stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,nfbs,nfbz,fxs,fxz]
               ELSE
                 stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,fxs,fxz,nfbs,nfbz];
             IF (stlart<>illegal) AND (stlart<>nachesp)
              OR (stlart=nachesp) AND nespstell THEN
               BEGIN
               posanz:=posanz+1;
               posliste[posanz].spalte:=nfbs;
               posliste[posanz].zeile:=nfbz;
               END;  { IF }
             END;  { IF }
           END;  { IF }
      END;  { IF }
    brett[sks,skz]:=TRUE;
    brett[wks,wkz]:=TRUE;
    brett[fxs,fxz]:=TRUE;
    END;  { IF }
END;   { ErzeugeBPoslist }

PROCEDURE ErzeugeSPoslist(stein:CHAR;stell:STELLUNG;back,nespstell:BOOLEAN;
                          VAR posliste:POSLIST;VAR posanz:ZGBEREICH);
{*
Beschreibung:
  Prozedur erzeugt fuer den angegebenen Springer aus der uebergebenen
  Stellung heraus alle Positionen, auf die er legal ziehen kann
  (Nachfolgepositionen) oder von denen aus er auf seine Position in
  der Stellung legal ziehen konnte (Vorgaengerpositionen), wobei
  beruecksichtigt wird, ob die Position auch einer Nachendspielstellung
  angehoeren darf.
Parameter:
  stein = Die Angabe des Steins des Springer,  wobei der Wert '3' fuer den
          dritten Stein und Wert '4' fuer den vierten Stein steht
  stell = Die Stellung, fuer die die Nachfolge- bzw. Vorgaengerpositionen
          erzeugt werden
  back = Wenn back TRUE ist, werden die Vorgaengerpositionen erzeugt, wenn
         es FALSE ist, die Nachfolgepositionen
  nespstell = Genau dann, wenn nespstell TRUE ist, werden auch die
              Positionen aus Nachendspielstellungen erzeugt
  posliste = Variable fuer die Liste der erzeugten Positionen
  posanz = Variable fuer die Anzahl der erzeugten Positionen
Benutzte globale Variablen:
  farbe3, farbe4, stellarten
Veraenderte globale Variablen:
  brett, abbrechen
Vorbedingungen:
  Die Stellung stell ist initial legal
*}
VAR
  stlart:STELLART;
  posnr:ZGBEREICH;
  nzfarbe:FARBEN;
  fss,fsz,wks,wkz,sks,skz,fxs,fxz:SZBEREICH;
  nfss,nfsz:SZBEREICH;
  sadd,zadd:INTEGER;
BEGIN
  IF stein='3' THEN
      StellToPwerte(stell,sks,skz,wks,wkz,fss,fsz,fxs,fxz)
    ELSE
      StellToPwerte(stell,sks,skz,wks,wkz,fxs,fxz,fss,fsz);
  posnr:=0;
  IF ((fxs<>sks) OR (fxz<>skz)) AND ((fxs<>wks) OR (fxz<>wkz)) THEN
    BEGIN
    brett[sks,skz]:=FALSE;
    brett[wks,wkz]:=FALSE;
    IF (farbe3=farbe4) OR back THEN
      brett[fxs,fxz]:=FALSE;
    IF back OR brett[fss,fsz] AND ((fss<>fxs) OR (fsz<>fxz)) THEN
      BEGIN
      IF back THEN
          IF stein='3' THEN
              nzfarbe:=farbe3
            ELSE
              nzfarbe:=farbe4
        ELSE
          IF (stein='3') AND (farbe3=weiss)
           OR (stein='4') AND (farbe4=weiss) THEN
              nzfarbe:=schwarz
            ELSE
              nzfarbe:=weiss;
      FOR sadd:=-2 TO 2 DO
       FOR zadd:=-2 TO 2 DO
        IF ABS(sadd)+ABS(zadd)=3 THEN
          IF brett[fss+sadd,fsz+zadd] THEN
            BEGIN
            nfss:=fss+sadd;
            nfsz:=fsz+zadd;
            IF stein='3' THEN
                stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,nfss,nfsz,fxs,fxz]
              ELSE
                stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,fxs,fxz,nfss,nfsz];
            IF (stlart<>illegal) AND (stlart<>nachesp)
             OR (stlart=nachesp) AND nespstell THEN
              IF posnr<maxzuegeanz THEN
                  BEGIN
                  posnr:=posnr+1;
                  posliste[posnr].spalte:=nfss;
                  posliste[posnr].zeile:=nfsz;
                  END
                ELSE
                  BEGIN
                  WRITELN;
                  WRITELN;
                  WRITELN('Konstante  maxzuegeanz  zu klein');
                  abbrechen:=TRUE;
                  END;  { ELSE }
            END;  { IF }
      END;  { IF }
    brett[sks,skz]:=TRUE;
    brett[wks,wkz]:=TRUE;
    brett[fxs,fxz]:=TRUE;
    END;  { IF }
  posanz:=posnr;
END;   { ErzeugeSPoslist }

PROCEDURE ErzeugeDTLPoslist(stein:CHAR;stell:STELLUNG;back,nespstell:BOOLEAN;
                            VAR posliste:POSLIST;VAR posanz:ZGBEREICH);
{*
Beschreibung:
  Prozedur erzeugt fuer die angegebene Figur, die eine Dame, ein Turm
  oder ein Laeufer ist, aus der uebergebenen Stellung heraus alle
  Positionen, auf die sie legal ziehen kann (Nachfolgepositionen) oder
  von denen aus sie auf ihre Position in der Stellung legal ziehen konnte
  (Vorgaengerpositionen), wobei beruecksichtigt wird, ob die Position
  auch einer Nachendspielstellung angehoeren darf.
Parameter:
  stein = Die Angabe des Steins der Dame, des Turms oder des Laeufers, wobei
          der Wert '3' fuer den dritten Stein und Wert '4' fuer den vierten
          Stein steht
  stell = Die Stellung, fuer die die Nachfolge- bzw. Vorgaengerpositionen
          erzeugt werden
  back = Wenn back TRUE ist, werden die Vorgaengerpositionen erzeugt, wenn
         es FALSE ist, die Nachfolgepositionen
  nespstell = Genau dann, wenn nespstell TRUE ist, werden auch die
              Positionen aus Nachendspielstellungen erzeugt
  posliste = Variable fuer die Liste der erzeugten Positionen
  posanz = Variable fuer die Anzahl der erzeugten Positionen
Benutzte globale Variablen:
  farbe3, farbe4, figur3, figur4, stellarten
Veraenderte globale Variablen:
  brett, abbrechen
Vorbedingungen:
  Die Stellung stell ist initial legal
*}
VAR
  stlart:STELLART;
  posnr:ZGBEREICH;
  nzfarbe:FARBEN;
  fes,fez,sks,skz,wks,wkz,fxs,fxz:SZBEREICH;
  nfes,nfez:SZBEREICH;
  sadd,zadd:INTEGER;
  figur:CHAR;
BEGIN
  IF stein='3' THEN
      StellToPwerte(stell,sks,skz,wks,wkz,fes,fez,fxs,fxz)
    ELSE
      StellToPwerte(stell,sks,skz,wks,wkz,fxs,fxz,fes,fez);
  posnr:=0;
  IF ((fxs<>sks) OR (fxz<>skz)) AND ((fxs<>wks) OR (fxz<>wkz)) THEN
    BEGIN
    brett[sks,skz]:=FALSE;
    brett[wks,wkz]:=FALSE;
    IF (farbe3=farbe4) OR back THEN
      brett[fxs,fxz]:=FALSE;
    IF back OR brett[fes,fez] AND ((fes<>fxs) OR (fez<>fxz)) THEN
      BEGIN
      IF stein='3' THEN
          figur:=figur3
        ELSE
          figur:=figur4;
      IF back THEN
          IF stein='3' THEN
              nzfarbe:=farbe3
            ELSE
              nzfarbe:=farbe4
        ELSE
          IF (stein='3') AND (farbe3=weiss)
           OR (stein='4') AND (farbe4=weiss) THEN
              nzfarbe:=schwarz
            ELSE
              nzfarbe:=weiss;
      FOR sadd:=-1 TO 1 DO
       FOR zadd:=-1 TO 1 DO
        IF (sadd<>0) OR (zadd<>0) THEN
         IF (figur='D') OR (figur='T') AND ((sadd=0) OR (zadd=0))
          OR (figur='L') AND (sadd<>0) AND (zadd<>0) THEN
           BEGIN
           nfes:=fes;
           nfez:=fez;
           WHILE ((nfes<>fxs) OR (nfez<>fxz) OR (nfes=fes) AND (nfez=fez))
            AND brett[nfes+sadd,nfez+zadd] DO
             BEGIN
             nfes:=nfes+sadd;
             nfez:=nfez+zadd;
             IF stein='3' THEN
                 stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,nfes,nfez,fxs,fxz]
               ELSE
                 stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,fxs,fxz,nfes,nfez];
             IF (stlart<>illegal) AND (stlart<>nachesp)
              OR (stlart=nachesp) AND nespstell THEN
               IF posnr<maxzuegeanz THEN
                   BEGIN
                   posnr:=posnr+1;
                   posliste[posnr].spalte:=nfes;
                   posliste[posnr].zeile:=nfez;
                   END
                 ELSE
                   BEGIN
                   WRITELN;
                   WRITELN;
                   WRITELN('Konstante  maxzuegeanz  zu klein');
                   abbrechen:=TRUE;
                   END;  { ELSE }
             END;  { WHILE }
           END;  { IF }
      END;  { IF }
    brett[sks,skz]:=TRUE;
    brett[wks,wkz]:=TRUE;
    brett[fxs,fxz]:=TRUE;
    END;  { IF }
  posanz:=posnr;
END;   { ErzeugeDTLPoslist }

PROCEDURE ErzeugePoslist(zfarbe:FARBEN;stein:CHAR;stell:STELLUNG;
                         back,nespstell:BOOLEAN;
                         VAR posliste:POSLIST;VAR posanz:ZGBEREICH);
{*
Beschreibung:
  Prozedur erzeugt fuer den angegebenen Stein der angegebenen Farbe in
  der uebergebenen Stellung alle Positionen, auf die die entsprechende
  Figur legal ziehen kann (Nachfolgepositionen) oder von denen aus sie
  auf ihre Position in der Stellung legal ziehen konnte
  (Vorgaengerpositionen), wobei beruecksichtigt wird, ob die Position
  auch einer Nachendspielstellung angehoeren darf. Gehoert der
  angegebene Stein nicht der angegebenen Farbe an, werden null Zuege
  erzeugt.
Parameter:
  zfarbe = Die Farbe, fuer deren Stein die Positionen erzeugt werden
  stein = Der Stein, fuer den die Positionen erzeugt werden sollen, wobei
          der Wert '3' fuer den dritten Stein und Wert '4' fuer den vierten
          Stein steht
  stell = Die Stellung, fuer die die Nachfolge- bzw. Vorgaengerpositionen
          erzeugt werden
  back = Wenn back TRUE ist, werden die Vorgaengerpositionen erzeugt, wenn
         es FALSE ist, die Nachfolgepositionen
  nespstell = Genau dann, wenn nespstell TRUE ist, werden auch die
              Positionen aus Nachendspielstellungen erzeugt
  posliste = Variable fuer die Liste der erzeugten Positionen
  posanz = Variable fuer die Anzahl der erzeugten Positionen
Benutzte globale Variablen:
  farbe3, farbe4, figur3, figur4, stellarten (indirekt)
Veraenderte globale Variablen:
  brett (indirekt), abbrechen (indirekt)
Vorbedingungen:
  Die Stellung stell ist initial legal
*}
VAR
  figur:CHAR;
BEGIN
  posanz:=0;
  IF (stein='w') AND (zfarbe=weiss) OR (stein='s') AND (zfarbe=schwarz) THEN
      ErzeugeKPoslist(stein,stell,back,nespstell,posliste,posanz)
    ELSE
      IF (stein='3') AND (zfarbe=farbe3)
       OR (stein='4') AND (zfarbe=farbe4) THEN
        BEGIN
        IF stein='3' THEN
            figur:=figur3
          ELSE
            figur:=figur4;
        IF (figur='D') OR (figur='T') OR (figur='L') THEN
            ErzeugeDTLPoslist(stein,stell,back,nespstell,posliste,posanz)
          ELSE  IF figur='S' THEN
            ErzeugeSPoslist(stein,stell,back,nespstell,posliste,posanz)
          ELSE
            ErzeugeBPoslist(stein,stell,back,nespstell,posliste,posanz);
        END;  { IF }
END;   { ErzeugePoslist }

PROCEDURE StellungHinzu(farbe:FARBEN;stell:STELLUNG);
{*
Beschreibung:
  Prozedur fuegt die uebergebene Stellung der Liste der am Anfang als
  illegal oder im letzten Halbzug als gewinnbaren erkannten Stellungen
  hinzu.
Parameter:
  farbe = Die Farbe, die in der Stellung am Zug ist
  stell = Die Stellung, die der Liste hinzugefuegt wird
Benutzte globale Variablen:
  stindex
Veraenderte globale Variablen:
  gstellanz, gstellungen, abbrechen
Vorbedingungen:
  Keine
*}
BEGIN
  IF gstellanz[stindex,farbe]<maxgewstanz THEN
      BEGIN
      gstellanz[stindex,farbe]:=gstellanz[stindex,farbe]+1;
      gstellungen[stindex,farbe,gstellanz[stindex,farbe]]:=stell;
      END
    ELSE
      BEGIN
      WRITELN;
      WRITELN;
      WRITELN('Konstante  maxgewstanz  zu klein');
      abbrechen:=TRUE;
      END;  { ELSE }
END;   { StellungHinzu }

FUNCTION VtRepraesentant(f3s,f3z,f4s,f4z:SZBEREICH):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn die Stellung, in der die
  zusaetzlich zu den Koenigen vorhandenen Figuren auf den durch die
  uebergebenen Spalten und Zeilen beschrieben Feldern stehen, der
  Repraesentant ihrer Aequivalenzklasse ist, die alle Stellungen
  umfasst, die durch moegliche Vertauschungen der gleichen Figuren
  aus der Stellung erzeugbar sind. Die Funktion liefert fuer genau
  eine Stellung der Aequivalenzklasse, fuer den Repraesentanten,
  den Wert TRUE.
Parameter:
  f3s,f3z,f4s,f4z = Die Spalten und Zeilen der Figuren ausser den
                    Koenigen von der Stellung, die getestet wird,
                    ob sie ein Repraesentant ist
Benutzte globale Variablen:
  figur3, figur4, fig34gleich
Vorbedingungen:
  (sks<=4) AND ((skz<=sks) OR bvorhanden AND (skz<=8))
*}
BEGIN
  IF (figur3<'a') AND (figur4<'a') THEN
      IF fig34gleich THEN
          IF f3s+f3z<f4s+f4z THEN
              VtRepraesentant:=TRUE
            ELSE  IF f3s+f3z=f4s+f4z THEN
              IF f3s*f3s+f3z*f3z<f4s*f4s+f4z*f4z THEN
                  VtRepraesentant:=TRUE
                ELSE  IF f3s*f3s+f3z*f3z=f4s*f4s+f4z*f4z THEN
                  VtRepraesentant:=(f3z<=f4z)
                ELSE
                  VtRepraesentant:=FALSE
            ELSE
              VtRepraesentant:=FALSE
        ELSE
          VtRepraesentant:=TRUE
    ELSE  IF (figur3>='a') AND (figur4>='a') AND (farbe3=farbe4) THEN
      IF fig34gleich THEN
          VtRepraesentant:=(f3s<f4s) OR (f3s=f4s) AND (f3z<=f4z)
        ELSE  IF (f3s=f4s) THEN
          VtRepraesentant:=(f3z<=f4z)
        ELSE
          VtRepraesentant:=TRUE
    ELSE
      VtRepraesentant:=TRUE;
END;   { VtRepraesentant }

FUNCTION Repraesentant(sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn die Stellung, die durch die
  uebergebenen Spalten und Zeilen beschrieben wird, der Repraesentant
  ihrer Aequivalenzklasse ist, die alle Stellungen umfasst, die durch
  Spiegelung oder durch Vertauschung gleicher Figuren aus der Stellung
  erzeugbar sind. Die Funktion liefert fuer genau eine Stellung der
  Aequivalenzklasse, fuer den Repraesentanten, den Wert TRUE.
Parameter:
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z = Die Spalten und Zeilen der Figuren
                                    der Stellung, die getestet wird, ob
                                    sie ein Repraesentant ist
Benutzte globale Variablen:
  bvorhanden, figur3 (indirekt), figur4 (indirekt),
  fig34gleich (indirekt)
Vorbedingungen:
  (sks<=4) AND ((skz<=sks) OR bvorhanden AND (skz<=8))
*}
BEGIN
  IF bvorhanden OR (skz<sks) OR (wkz<wks) OR (wkz=wks) AND (f3z<f3s)
   OR (wkz=wks) AND (f3z=f3s) AND (f4z<=f4s) THEN
      Repraesentant:=VtRepraesentant(f3s,f3z,f4s,f4z)
    ELSE
      Repraesentant:=FALSE;
END;   { Repraesentant }

FUNCTION LPositionIllegal(stell:STELLUNG):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn in der uebergebenen Stellung
  die Position eines Laeufers alleine oder in Bezug auf die anderen
  Figuren unmittelbar illegal ist.
Parameter:
  stell = Die Stellung, fuer die die Laeuferposition ueberprueft wird
Benutzte globale Variablen:
  figur3, figur4, farbe3, farbe4
Vorbedingungen:
  Keine
*}
VAR
  fls,flz,fbs,fbz:SZBEREICH;
  bfarbe:FARBEN;
  stillegal:BOOLEAN;
BEGIN
  stillegal:=TRUE;
  IF (figur3='L') AND (figur4>='a') AND (farbe3=farbe4) THEN
      BEGIN
      fls:=stell.f3s;
      flz:=stell.f3z;
      fbs:=stell.f4s;
      fbz:=stell.f4z;
      bfarbe:=farbe4;
      END
    ELSE  IF (figur3>='a') AND (figur4='L')  AND (farbe3=farbe4) THEN
      BEGIN
      fls:=stell.f4s;
      flz:=stell.f4z;
      fbs:=stell.f3s;
      fbz:=stell.f3z;
      bfarbe:=farbe3;
      END
    ELSE
      stillegal:=FALSE;
  IF NOT(stillegal) THEN
      LPositionIllegal:=FALSE
    ELSE  IF (bfarbe=weiss) AND (fbz>2) OR (bfarbe=schwarz) AND (fbz<7) THEN
      LPositionIllegal:=FALSE
    ELSE
      IF (fls=1) AND (flz=1) AND (fbs=2) AND (fbz=2) THEN
          LPositionIllegal:=TRUE
        ELSE  IF (fls=1) AND (flz=8) AND (fbs=2) AND (fbz=7) THEN
          LPositionIllegal:=TRUE
        ELSE  IF (fls=8) AND (flz=1) AND (fbs=7) AND (fbz=2) THEN
          LPositionIllegal:=TRUE
        ELSE  IF (fls=8) AND (flz=8) AND (fbs=7) AND (fbz=7) THEN
          LPositionIllegal:=TRUE
        ELSE
          LPositionIllegal:=FALSE;
END;   { LPositionIllegal }

FUNCTION PositionIllegal(stell:STELLUNG):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn die Position einer Figur oder
  mehrerer Figuren in der uebergebenen Stellung zueinander unmittelbar
  illegal ist bzw. sind.
Parameter:
  stell = Die Stellung, fuer die die Figurenpositionen ueberprueft werden
Benutzte globale Variablen:
  figur3, farbe3, figur4, farbe4
Vorbedingungen:
  Keine
*}
VAR
  f3s,f3z,f4s,f4z:SZBEREICH;
BEGIN
  f3s:=stell.f3s;
  f3z:=stell.f3z;
  f4s:=stell.f4s;
  f4z:=stell.f4z;
  IF (figur3>='a')
   AND ((farbe3=weiss) AND (f3z=1) OR (farbe3=schwarz) AND (f3z=8)) THEN
      PositionIllegal:=TRUE
    ELSE  IF (figur4>='a')
     AND ((farbe4=weiss) AND (f4z=1) OR (farbe4=schwarz) AND (f4z=8)) THEN
      PositionIllegal:=TRUE
    ELSE  IF (ABS(stell.sks-stell.wks)<=1)
     AND (ABS(stell.skz-stell.wkz)<=1) THEN
      PositionIllegal:=TRUE
    ELSE  IF (farbe3=farbe4) AND (f3s=f4s) AND (f3z=f4z) THEN
      PositionIllegal:=TRUE
    ELSE  IF (farbe3=weiss) AND (f3s=stell.wks) AND (f3z=stell.wkz) THEN
      PositionIllegal:=TRUE
    ELSE  IF (farbe3=schwarz) AND (f3s=stell.sks) AND (f3z=stell.skz) THEN
      PositionIllegal:=TRUE
    ELSE  IF (farbe4=weiss) AND (f4s=stell.wks) AND (f4z=stell.wkz) THEN
      PositionIllegal:=TRUE
    ELSE  IF (farbe4=schwarz) AND (f4s=stell.sks) AND (f4z=stell.skz) THEN
      PositionIllegal:=TRUE
    ELSE  IF (f3s=stell.wks) AND (f3z=stell.wkz)
     AND (f4s=stell.sks) AND (f4z=stell.skz) THEN
      PositionIllegal:=TRUE
    ELSE  IF (f3s=stell.sks) AND (f3z=stell.skz)
     AND (f4s=stell.wks) AND (f4z=stell.wkz) THEN
      PositionIllegal:=TRUE
    ELSE  IF LPositionIllegal(stell) THEN
      PositionIllegal:=TRUE
    ELSE
      PositionIllegal:=FALSE;
END;   { PositionIllegal }

FUNCTION BauernSpalteIllegal(amzug:FARBEN;stell:STELLUNG):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn die vorgegebene dritte und/oder
  vierte Figur ein Bauer ist und einer oder beide in der uebergebenen
  Stellung auf einer falschen Spalte stehen.
Parameter:
  amzug = Die Farbe, die in der Stellung stell am Zug ist
  stell = Die Stellung, fuer die die Spalte der Bauern ueberprueft wird
Benutzte globale Variablen:
  figur3, farbe3, figur4, farbe4
Vorbedingungen:
  Keine
*}
VAR
  f3s,f3z,f4s,f4z:SZBEREICH;
  spdif1,spdif2:INTEGER;
  stlegal,f3gespiegelt,f4gespiegelt:BOOLEAN;
BEGIN
  f3s:=stell.f3s;
  f3z:=stell.f3z;
  f4s:=stell.f4s;
  f4z:=stell.f4z;
  stlegal:=TRUE;
  IF (figur3>='a') AND (figur3<>'x') THEN
    BEGIN
    spdif1:=ABS(f3s-1-(ORD(figur3)-ORD('a')));
    spdif2:=ABS(8-f3s-(ORD(figur3)-ORD('a')));
    IF (f3s=f4s) AND (f3z=f4z) AND (farbe3<>amzug) THEN
        IF spdif1=1 THEN
            f3gespiegelt:=FALSE
          ELSE  IF spdif2=1 THEN
            f3gespiegelt:=TRUE
          ELSE
            stlegal:=FALSE
      ELSE
        IF spdif1=0 THEN
            f3gespiegelt:=FALSE
          ELSE  IF spdif2=0 THEN
            f3gespiegelt:=TRUE
          ELSE
            stlegal:=FALSE;
    END;  { IF }
  IF stlegal AND (figur4>='a') AND (figur4<>'x') THEN
    BEGIN
    spdif1:=ABS(f4s-1-(ORD(figur4)-ORD('a')));
    spdif2:=ABS(8-f4s-(ORD(figur4)-ORD('a')));
    IF (f4s=f3s) AND (f4z=f3z) AND (farbe4<>amzug) THEN
        IF spdif1=1 THEN
            f4gespiegelt:=FALSE
          ELSE  IF spdif2=1 THEN
            f4gespiegelt:=TRUE
          ELSE
            stlegal:=FALSE
      ELSE
        IF spdif1=0 THEN
            f4gespiegelt:=FALSE
          ELSE  IF spdif2=0 THEN
            f4gespiegelt:=TRUE
          ELSE
            stlegal:=FALSE;
    END;  { IF }
  IF stlegal AND (figur3>='a') AND (figur3<>'x')
   AND (figur4>='a') AND (figur4<>'x') THEN
    IF f3gespiegelt AND NOT(f4gespiegelt)
     OR NOT(f3gespiegelt) AND f4gespiegelt THEN
      stlegal:=FALSE;
  BauernSpalteIllegal:=NOT(stlegal);
END;   { BauernSpalteIllegal }

PROCEDURE EndStellungHinzu(farbe:FARBEN;stell:STELLUNG);
{*
Beschreibung:
  Prozedur fuegt die uebergebene Stellung der Liste der mittels der
  geladenen Daten bewerteten und fuer die Analyse benoetigten
  Endstellungen hinzu.
Parameter:
  farbe = Die Farbe, die in der Stellung am Zug ist
  stell = Die Stellung, die der Liste hinzugefuegt wird
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  estellanz, estellungen, abbrechen
Vorbedingungen:
  Keine
*}
BEGIN
  IF estellanz[farbe]<maxendstanz THEN
      BEGIN
      estellanz[farbe]:=estellanz[farbe]+1;
      estellungen[farbe,estellanz[farbe]]:=stell;
      END
    ELSE
      BEGIN
      WRITELN;
      WRITELN;
      WRITELN('Konstante  maxendstanz  zu klein');
      abbrechen:=TRUE;
      END;  { ELSE }
END;   { EndStellungHinzu }

PROCEDURE BewStellungHinzu(farbe:FARBEN;stell:STELLUNG;bhzwert:BSTWERT);
{*
Beschreibung:
  Prozedur fuegt die uebergebene Stellung der Liste der mittels der
  geladenen Daten bewerteten und fuer die Analyse benoetigten Stellungen,
  die keine Endstellungen sind, hinzu.
Parameter:
  farbe = Die Farbe, die in der Stellung am Zug ist
  stell = Die Stellung, die der Liste hinzugefuegt wird
  bhzwert = Der Stellungswert der der Liste hinzuzufuegenden Stellung
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  bhzstellanz, bhzstellungen, grhzamin, abbrechen
Vorbedingungen:
  Keine
*}
BEGIN
  IF bhzstellanz[farbe,bhzwert]<maxbewstanz THEN
      BEGIN
      bhzstellanz[farbe,bhzwert]:=bhzstellanz[farbe,bhzwert]+1;
      bhzstellungen[farbe,bhzwert,bhzstellanz[farbe,bhzwert]]:=stell;
      IF bhzwert>grhzamin THEN
        grhzamin:=bhzwert;
      END
    ELSE
      BEGIN
      WRITELN;
      WRITELN;
      WRITELN('Konstante  maxbewstanz  zu klein');
      abbrechen:=TRUE;
      END;  { ELSE }
END;   { BewStellungHinzu }

PROCEDURE BewerteFigWegStellung(amzug:FARBEN;wegstein:CHAR;stell:STELLUNG);
{*
Beschreibung:
  Prozedur bewertet die uebergebene Stellung, in der eine Figur geschlagen
  ist, anhand der geladenen Daten des zugehoerigen 3-Steine-Endspiels und
  fuegt sie, wenn es eine legale Stellung ist, zu der Liste der bewerteten
  Stelungen hinzu.
Parameter:
  amzug = Die Farbe, die in der Stellung am Zug ist
  wegstein = Der Stein, der geschlagen ist
  stell = Die Stellung, in der die Figur geschlagen ist und die bewertet
          wird
Benutzte globale Variablen:
  o3starten, o4starten, schnellstmatt, figur3 (indirekt),
  figur4 (indirekt), bvorhanden (indirekt), fig34gleich (indirekt)
Veraenderte globale Variablen:
  legalanz, stellarten (indirekt), estellanz (indirekt),
  estellungen (indirekt), bhzstellanz (indirekt),
  bhzstellungen (indirekt), grhzamin (indirekt),
Vorbedingungen:
  Die zu bewertende Stellung stell ist weder (initial) illegal noch eine
  Nachendspielstellung, d.h. sie hat weder den Stellungswert illegal
  noch nachesp.
*}
VAR
  stlart:STELLART;
  stlwert:BSTWERT;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  fstanz:INTEGER;
BEGIN
  StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  IF wegstein='3' THEN
      stlart:=o3starten[sks,skz,wks,wkz,f4s,f4z]
    ELSE
      stlart:=o4starten[sks,skz,wks,wkz,f3s,f3z];
  SetzeStellungsArt(amzug,stell,stlart,fstanz);
  legalanz[amzug]:=legalanz[amzug]+fstanz;
  IF NOT(schnellstmatt) THEN
      EndStellungHinzu(amzug,stell)
    ELSE
      BEGIN
      IF wegstein='3' THEN
          stlwert:=o3stwerte[sks,skz,wks,wkz,f4s,f4z]
        ELSE
          stlwert:=o4stwerte[sks,skz,wks,wkz,f3s,f3z];
      IF stlwert=0 THEN
          EndStellungHinzu(amzug,stell)
        ELSE
          BewStellungHinzu(amzug,stell,stlwert);
      END;  { ELSE }
END;   { BewerteFigWegStellung }

PROCEDURE BewerteUmwandelStellung(amzug:FARBEN;uwstein:CHAR;stell:STELLUNG);
{*
Beschreibung:
  Prozedur bewertet die uebergebene Stellung, die eine Umwandlungsstellung
  ist, anhand der geladenen Daten des entsprechenden Umwandlungsendspiels
  und fuegt sie, wenn es eine legale Stellung ist, zu der Liste der
  bewerteten Stelungen hinzu.
Parameter:
  amzug = Die Farbe, die in der Stellung am Zug ist
  uwstein = Der Stein, der umgewandelt wird
  stell = Die Stellung, in der der Bauer umgewandelt und die bewertet wird
Benutzte globale Variablen:
  sdstarten, sdstwerte, wdstarten, wdstwerte, schnellstmatt,
  figur3 (indirekt), figur4 (indirekt), bvorhanden (indirekt)
  fig34gleich (indirekt)
Veraenderte globale Variablen:
  legalanz, stellarten (indirekt), estellanz (indirekt),
  estellungen (indirekt), bhzstellanz (indirekt),
  bhzstellungen (indirekt), grhzamin (indirekt)
Vorbedingungen:
  Die zu amzug andere Farbe hat einen Bauern umgewandelt.
  Ausserdem ist die zu bewertende Stellung stell weder (initial) illegal
  noch eine Nachendspielstellung, d.h. sie hat weder den Stellungswert
  illegal noch nachesp.
*}
VAR
  stlart:STELLART;
  stlwert:BSTWERT;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  fstanz:INTEGER;
BEGIN
  StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  IF amzug=weiss THEN
      IF uwstein='3' THEN
          BEGIN
          stlart:=sdstarten[sks,skz,wks,wkz,f4s,f4z,f3s,f3z];
          stlwert:=sdstwerte[sks,skz,wks,wkz,f4s,f4z,f3s,f3z];
          END
        ELSE
          BEGIN
          stlart:=sdstarten[sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
          stlwert:=sdstwerte[sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
          END
    ELSE
      IF uwstein='3' THEN
          BEGIN
          stlart:=wdstarten[sks,skz,wks,wkz,f4s,f4z,f3s,f3z];
          stlwert:=wdstwerte[sks,skz,wks,wkz,f4s,f4z,f3s,f3z];
          END
        ELSE
          BEGIN
          stlart:=wdstarten[sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
          stlwert:=wdstwerte[sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
          END;  { ELSE }
  SetzeStellungsArt(amzug,stell,stlart,fstanz);
  legalanz[amzug]:=legalanz[amzug]+fstanz;
  IF NOT(schnellstmatt) OR (stlwert=0) THEN
      EndStellungHinzu(amzug,stell)
    ELSE
      BewStellungHinzu(amzug,stell,stlwert);
END;   { BewerteUmwandelStellung }

PROCEDURE ErmittelAnfStellungen;
{*
Beschreibung:
  Prozedur ermittelt fuer alle Stellungen ihre initiale Art und die Anzahl
  der initial legalen Stellungen und initialisiert ihren Stellungswert mit
  dem Wert 0.
Parameter:
  Keine
Benutzte globale Variablen:
  bvorhanden, abbrechen, figur3 (indirekt), figur4 (indirekt),
  farbe3 (indirekt), farbe4 (indirekt), sdstarten (indirekt),
  sdstwerte (indirekt), wdstarten (indirekt), wdstwerte (indirekt),
  schnellstmatt (indirekt), o3starten (indirekt), o4starten (indirekt),
  fig34gleich (indirekt)
Veraenderte globale Variablen:
  legalanz, stellwerte (indirekt), stellarten (indirekt),
  estellanz (indirekt), estellungen (indirekt), bhzstellanz (indirekt),
  bhzstellungen (indirekt), grhzamin (indirekt)
Vorbedingungen:
  Keine
*}
VAR
  anfstell:STELLUNG;
  sks,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  skz:INTEGER;
  fstanz:INTEGER;
BEGIN
  FOR sks:=1 TO 4 DO
    BEGIN
    skz:=0;
    WHILE (skz<sks) OR bvorhanden AND (skz<8) DO
      BEGIN
      skz:=skz+1;
      FOR wks:=1 TO 8 DO
       FOR wkz:=1 TO 8 DO
        FOR f3s:=1 TO 8 DO
         FOR f3z:=1 TO 8 DO
          FOR f4s:=1 TO 8 DO
           FOR f4z:=1 TO 8 DO
             IF Repraesentant(sks,skz,wks,wkz,f3s,f3z,f4s,f4z) THEN
               IF NOT(abbrechen) THEN
                 BEGIN
                 PwerteToStell(sks,skz,wks,wkz,f3s,f3z,f4s,f4z,anfstell);
                 SetzeStellungsWert(weiss,anfstell,0);
                 SetzeStellungsWert(schwarz,anfstell,0);
                 IF PositionIllegal(anfstell) THEN
                     BEGIN
                     SetzeStellungsArt(weiss,anfstell,illegal,fstanz);
                     SetzeStellungsArt(schwarz,anfstell,illegal,fstanz);
                     END
                   ELSE
                     BEGIN
                     IF BauernSpalteIllegal(weiss,anfstell) THEN
                         SetzeStellungsArt(weiss,anfstell,illegal,fstanz)
                       ELSE  IF Schach(schwarz,anfstell) THEN
                         SetzeStellungsArt(weiss,anfstell,illegal,fstanz)
                       ELSE  IF (wks=f3s) AND (wkz=f3z)
                        OR (wks=f4s) AND (wkz=f4z) THEN
                         SetzeStellungsArt(weiss,anfstell,illegal,fstanz)
                       ELSE  IF (figur3>='a') AND (f3z=8)
                        OR (figur4>='a') AND (f4z=8) THEN
                         SetzeStellungsArt(weiss,anfstell,nachesp,fstanz)
                       ELSE  IF (figur3>='a') AND (f3z=1) THEN
                         BewerteUmwandelStellung(weiss,'3',anfstell)
                       ELSE  IF (figur4>='a') AND (f4z=1) THEN
                         BewerteUmwandelStellung(weiss,'4',anfstell)
                       ELSE  IF (sks=f3s) AND (skz=f3z)
                        OR (f3s=f4s) AND (f3z=f4z) AND (farbe3=weiss) THEN
                         BewerteFigWegStellung(weiss,'3',anfstell)
                       ELSE  IF (sks=f4s) AND (skz=f4z)
                        OR (f3s=f4s) AND (f3z=f4z) AND (farbe4=weiss) THEN
                         BewerteFigWegStellung(weiss,'4',anfstell)
                       ELSE
                         BEGIN
                         SetzeStellungsArt(weiss,anfstell,nichtbew,fstanz);
                         legalanz[weiss]:=legalanz[weiss]+fstanz;
                         END;  { ELSE }
                     IF BauernSpalteIllegal(schwarz,anfstell) THEN
                         SetzeStellungsArt(schwarz,anfstell,illegal,fstanz)
                       ELSE  IF Schach(weiss,anfstell) THEN
                         SetzeStellungsArt(schwarz,anfstell,illegal,fstanz)
                       ELSE  IF (sks=f3s) AND (skz=f3z)
                        OR (sks=f4s) AND (skz=f4z) THEN
                         SetzeStellungsArt(schwarz,anfstell,illegal,fstanz)
                       ELSE  IF (figur3>='a') AND (f3z=1)
                        OR (figur4>='a') AND (f4z=1) THEN
                         SetzeStellungsArt(schwarz,anfstell,nachesp,fstanz)
                       ELSE  IF (figur3>='a') AND (f3z=8) THEN
                         BewerteUmwandelStellung(schwarz,'3',anfstell)
                       ELSE  IF (figur4>='a') AND (f4z=8) THEN
                         BewerteUmwandelStellung(schwarz,'4',anfstell)
                       ELSE  IF (wks=f3s) AND (wkz=f3z)
                        OR (f3s=f4s) AND (f3z=f4z) AND (farbe3=schwarz) THEN
                         BewerteFigWegStellung(schwarz,'3',anfstell)
                       ELSE  IF (wks=f4s) AND (wkz=f4z)
                        OR (f3s=f4s) AND (f3z=f4z) AND (farbe4=schwarz) THEN
                         BewerteFigWegStellung(schwarz,'4',anfstell)
                       ELSE
                         BEGIN
                         SetzeStellungsArt(schwarz,anfstell,nichtbew,fstanz);
                         legalanz[schwarz]:=legalanz[schwarz]+fstanz;
                         END;  { ELSE }
                     END;  { ELSE }
                 END;  { IF }
      END;  { WHILE }
    END;  { FOR }
END;   { ErmittelAnfStellungen }

FUNCTION LegalEpVorStellung(amzug:FARBEN;stell:STELLUNG):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn die uebergebene Stellung eine
  legale Vorgaengerstellung hat, aus der heraus "en passent"-Schlagen
  zur uebergebenen Stellung fuehren kann.
Parameter:
  amzug = Die Farbe, die in der Stellung am Zug ist
  stell = Die Stellung, fuer die geprueft wird, ob sie eine
          Vorgaengerstellung hat
Benutzte globale Variablen:
  figur3, figur4, farbe3, farbe4, stellarten
Vorbedingungen:
  Keine
*}
VAR
  stlart:STELLART;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
BEGIN
  stlart:=illegal;
  IF (figur3>='a') AND (figur4>='a') AND (farbe3<>farbe4)
   AND (stell.f3s=stell.f4s) AND (stell.f3z=stell.f4z) THEN
    BEGIN
    StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
    IF (f3z=3) AND (amzug=weiss) THEN
        IF farbe3=weiss THEN
            BEGIN
            IF ((sks<>f3s) OR (skz<>4)) AND ((wks<>f3s) OR (wkz<>4)) THEN
             IF ((sks<>f3s) OR (skz<>2)) AND ((wks<>f3s) OR (wkz<>2)) THEN
               BEGIN
               IF (f4s>=2) AND ((sks<>f4s-1) OR (skz<>4))
                AND ((wks<>f4s-1) OR (wkz<>4)) THEN
                 BEGIN
                 stlart:=stellarten[schwarz,sks,skz,wks,wkz,f3s,4,f4s-1,4];
                 IF (stlart<>illegal) AND (stlart<>nachesp) THEN
                   stlart:=stellarten[weiss,sks,skz,wks,wkz,f3s,2,f4s-1,4];
                 END;  { IF }
               IF (stlart=illegal) OR (stlart=nachesp) THEN
                 IF (f4s<=7) AND ((sks<>f4s+1) OR (skz<>4))
                  AND ((wks<>f4s+1) OR (wkz<>4)) THEN
                   BEGIN
                   stlart:=stellarten[schwarz,sks,skz,wks,wkz,f3s,4,f4s+1,4];
                   IF (stlart<>illegal) AND (stlart<>nachesp) THEN
                     stlart:=stellarten[weiss,sks,skz,wks,wkz,f3s,2,f4s+1,4];
                   END;  { IF }
               END;  { IF }
            END
          ELSE
            BEGIN
            IF ((sks<>f4s) OR (skz<>4)) AND ((wks<>f4s) OR (wkz<>4)) THEN
             IF ((sks<>f4s) OR (skz<>2)) AND ((wks<>f4s) OR (wkz<>2)) THEN
               BEGIN
               IF (f3s>=2) AND ((sks<>f3s-1) OR (skz<>4))
                AND ((wks<>f3s-1) OR (wkz<>4)) THEN
                 BEGIN
                 stlart:=stellarten[schwarz,sks,skz,wks,wkz,f3s-1,4,f4s,4];
                 IF (stlart<>illegal) AND (stlart<>nachesp) THEN
                   stlart:=stellarten[weiss,sks,skz,wks,wkz,f3s-1,2,f4s,4];
                 END;  { IF }
               IF (stlart=illegal) OR (stlart=nachesp) THEN
                 IF (f4s<=7) AND ((sks<>f3s+1) OR (skz<>4))
                  AND ((wks<>f3s+1) OR (wkz<>4)) THEN
                   BEGIN
                   stlart:=stellarten[schwarz,sks,skz,wks,wkz,f3s+1,4,f4s,4];
                   IF (stlart<>illegal) AND (stlart<>nachesp) THEN
                     stlart:=stellarten[weiss,sks,skz,wks,wkz,f3s+1,2,f4s,4];
                   END;  { IF }
               END;  { IF }
            END
      ELSE  IF (f3z=6) AND (amzug=schwarz) THEN
        IF farbe3=schwarz THEN
            BEGIN
            IF ((sks<>f3s) OR (skz<>5)) AND ((wks<>f3s) OR (wkz<>5)) THEN
             IF ((sks<>f3s) OR (skz<>7)) AND ((wks<>f3s) OR (wkz<>7)) THEN
               BEGIN
               IF (f4s>=2) AND ((sks<>f4s-1) OR (skz<>5))
                AND ((wks<>f4s-1) OR (wkz<>5)) THEN
                 BEGIN
                 stlart:=stellarten[weiss,sks,skz,wks,wkz,f3s,5,f4s-1,5];
                 IF (stlart<>illegal) AND (stlart<>nachesp) THEN
                   stlart:=stellarten[schwarz,sks,skz,wks,wkz,f3s,7,f4s-1,5];
                 END;  { IF }
               IF (stlart=illegal) OR (stlart=nachesp) THEN
                 IF (f4s<=7) AND ((sks<>f4s+1) OR (skz<>5))
                  AND ((wks<>f4s+1) OR (wkz<>5)) THEN
                   BEGIN
                   stlart:=stellarten[weiss,sks,skz,wks,wkz,f3s,5,f4s+1,5];
                   IF (stlart<>illegal) AND (stlart<>nachesp) THEN
                     stlart:=stellarten[schwarz,sks,skz,wks,wkz,f3s,7,f4s+1,5];
                   END;  { IF }
               END;  { IF }
            END
          ELSE
            BEGIN
            IF ((sks<>f4s) OR (skz<>5)) AND ((wks<>f4s) OR (wkz<>5)) THEN
             IF ((sks<>f4s) OR (skz<>7)) AND ((wks<>f4s) OR (wkz<>7)) THEN
               BEGIN
               IF (f3s>=2) AND ((sks<>f3s-1) OR (skz<>5))
                AND ((wks<>f3s-1) OR (wkz<>5)) THEN
                 BEGIN
                 stlart:=stellarten[weiss,sks,skz,wks,wkz,f3s-1,5,f4s,5];
                 IF (stlart<>illegal) AND (stlart<>nachesp) THEN
                   stlart:=stellarten[schwarz,sks,skz,wks,wkz,f3s-1,7,f4s,5];
                 END;  { IF }
               IF (stlart=illegal) OR (stlart=nachesp) THEN
                 IF (f4s<=7) AND ((sks<>f3s+1) OR (skz<>5))
                  AND ((wks<>f3s+1) OR (wkz<>5)) THEN
                   BEGIN
                   stlart:=stellarten[weiss,sks,skz,wks,wkz,f3s+1,5,f4s,5];
                   IF (stlart<>illegal) AND (stlart<>nachesp) THEN
                     stlart:=stellarten[schwarz,sks,skz,wks,wkz,f3s+1,7,f4s,5];
                   END;  { IF }
               END;  { IF }
            END;  { ELSE }
    END;  { IF }
  LegalEpVorStellung:=(stlart<>illegal) AND (stlart<>nachesp);
END;   { LegalEpVorStellung }

PROCEDURE StellIllegalTest(amzug:FARBEN;stell:STELLUNG);
{*
Beschreibung:
  Prozedur prueft, ob die uebergebene Stellung abgeleitet illegal ist
  und verringert, wenn dies so ist, die Anzahl der legalen Stellungen.
  Ausserdem erzeugt sie alle Vorgaengerstellungen, die in die Liste der
  auf Illegalitaet zu pruefenden Stellungen aufgenommen werden.
Parameter:
  amzug : Die Farbe, die in der Stellung am Zug ist
  stell : Die Stellung, die geprueft wird, ob sie abgeleitet illegal ist
Benutzte globale Variablen:
  stellarten, farbe3 (indirekt), farbe4 (indirekt), figur3 (indirekt),
  figur4 (indirekt), bvorhanden (indirekt), stindex (indirekt),
  fig34gleich (indirekt)
Veraenderte globale Variablen:
  legalanz, brett (indirekt), stellarten (indirekt), gstellanz (indirekt),
  gstellungen (indirekt), abbrechen (indirekt)
Vorbedingungen:
  Die Stellung stell ist bezueglich initialer Legalitaet bewertet
*}
VAR
  neustell:STELLUNG;
  posliste:POSLIST;
  posanz,posnr:ZGBEREICH;
  stlart:STELLART;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  afarbe:FARBEN;
  fstanz:INTEGER;
BEGIN
  IF amzug=weiss THEN
      afarbe:=schwarz
    ELSE
      afarbe:=weiss;
  StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  stlart:=stellarten[amzug,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
  IF stlart<>illegal THEN
    BEGIN
    ErzeugePoslist(afarbe,'s',stell,TRUE,FALSE,posliste,posanz);
    IF posanz=0 THEN
      ErzeugePoslist(afarbe,'w',stell,TRUE,FALSE,posliste,posanz);
    IF posanz=0 THEN
      ErzeugePoslist(afarbe,'3',stell,TRUE,FALSE,posliste,posanz);
    IF posanz=0 THEN
      ErzeugePoslist(afarbe,'4',stell,TRUE,FALSE,posliste,posanz);
    IF posanz=0 THEN
{ Fuer "en passent"-Regel folgenden Funktionsaufruf aufnehmen:
     IF NOT(LegalEpVorStellung(amzug,stell)) THEN
}
      BEGIN
      SetzeStellungsArt(amzug,stell,illegal,fstanz);
      IF stlart<>nachesp THEN
        legalanz[amzug]:=legalanz[amzug]-fstanz;
      neustell:=stell;
      ErzeugePoslist(amzug,'3',stell,FALSE,TRUE,posliste,posanz);
      FOR posnr:=1 TO posanz DO
        BEGIN
        neustell.f3s:=posliste[posnr].spalte;
        neustell.f3z:=posliste[posnr].zeile;
        StellungHinzu(afarbe,neustell);
        END;  { FOR }
      neustell:=stell;
      ErzeugePoslist(amzug,'4',stell,FALSE,TRUE,posliste,posanz);
      FOR posnr:=1 TO posanz DO
        BEGIN
        neustell.f4s:=posliste[posnr].spalte;
        neustell.f4z:=posliste[posnr].zeile;
        StellungHinzu(afarbe,neustell);
        END;  { FOR }
      neustell:=stell;
      IF amzug=weiss THEN
          ErzeugePoslist(amzug,'w',stell,FALSE,TRUE,posliste,posanz)
        ELSE
          ErzeugePoslist(amzug,'s',stell,FALSE,TRUE,posliste,posanz);
      FOR posnr:=1 TO posanz DO
        BEGIN
        IF amzug=weiss THEN
            BEGIN
            neustell.wks:=posliste[posnr].spalte;
            neustell.wkz:=posliste[posnr].zeile;
            END
          ELSE
            BEGIN
            neustell.sks:=posliste[posnr].spalte;
            neustell.skz:=posliste[posnr].zeile;
            END;  { ELSE }
        StellungHinzu(afarbe,neustell);
        END;  { FOR }
      END;  { IF }
    END;  { IF }
END;   { StellIllegalTest }

PROCEDURE ErmittelIllegalStellungen;
{*
Beschreibung:
  Prozedur ermittelt iterativ alle abgeleitet illegalen Stellungen.
Parameter:
  Keine
Benutzte globale Variablen:
  gstellungen, bvorhanden, abbrechen, farbe3 (indirekt), figur3 (indirekt),
  farbe4 (indirekt), figur4 (indirekt), fig34gleich (indirekt)
Veraenderte globale Variablen:
  stindex, gstellanz, legalanz (indirekt), brett (indirekt),
  stellarten (indirekt), gstellungen (indirekt), abbrechen (indirekt)
Vorbedingungen:
  Die Stellungen sind bezueglich initialer Legalitaet bewertet
*}
VAR
  teststell:STELLUNG;
  stnr:GSTBEREICH;
  farbe:FARBEN;
  sks,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  skz:INTEGER;
BEGIN
  stindex:=0;
  gstellanz[stindex,weiss]:=0;
  gstellanz[stindex,schwarz]:=0;
  FOR sks:=1 TO 4 DO
    BEGIN
    skz:=0;
    WHILE (skz<sks) OR bvorhanden AND (skz<8) DO
      BEGIN
      skz:=skz+1;
      FOR wks:=1 TO 8 DO
       FOR wkz:=1 TO 8 DO
        FOR f3s:=1 TO 8 DO
         FOR f3z:=1 TO 8 DO
          FOR f4s:=1 TO 8 DO
           FOR f4z:=1 TO 8 DO
             IF Repraesentant(sks,skz,wks,wkz,f3s,f3z,f4s,f4z) THEN
               IF NOT(abbrechen) THEN
                 BEGIN
                 PwerteToStell(sks,skz,wks,wkz,f3s,f3z,f4s,f4z,teststell);
                 StellIllegalTest(weiss,teststell);
                 StellIllegalTest(schwarz,teststell);
                 END;  { IF }
      END;  { WHILE }
    END;  { FOR }
  WHILE gstellanz[stindex,weiss]+gstellanz[stindex,schwarz]>0 DO
    BEGIN
    stindex:=1-stindex;
    gstellanz[stindex,weiss]:=0;
    gstellanz[stindex,schwarz]:=0;
    FOR farbe:=weiss TO schwarz DO
      FOR stnr:=1 TO gstellanz[1-stindex,farbe] DO
        IF NOT(abbrechen) THEN
          StellIllegalTest(farbe,gstellungen[1-stindex,farbe,stnr]);
    END;  { WHILE }
END;   { ErmittelIllegalStellungen }

PROCEDURE EndStellungenAufnehmen;
{*
Beschreibung:
  Prozedur erhoeht fuer alle Stellungen mit geladener Bewertung, die
  Endstellungen sind, die Anzahl der Stellungen der entsprechenden Art
  und nimmt die Stellungen, die matt oder gewonnen sind, in die Liste der
  als gewinnbar erkannten Stellungen auf.
Parameter:
  Keine
Benutzte globale Variablen:
  abbrechen, estellanz, estellungen, bvorhanden (indirekt),
  figur3 (indirekt), figur4 (indirekt), stindex (indirekt),
  fig34gleich (indirekt)
Veraenderte globale Variablen:
  mattanz, pattanz, tremisanz, wgewstanz, sgewstanz, stellarten (indirekt),
  stellwerte (indirekt), gstellanz (indirekt), gstellungen (indirekt),
  abbrechen (indirekt)
Vorbedingungen:
  Keine
*}
VAR
  stell:STELLUNG;
  stlart:STELLART;
  farbe:FARBEN;
  estnr:ESTBEREICH;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  fstanz:INTEGER;
BEGIN
  FOR farbe:=weiss TO schwarz DO
    FOR estnr:=1 TO estellanz[farbe] DO
      IF NOT(abbrechen) THEN
        BEGIN
        stell:=estellungen[farbe,estnr];
        StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
        stlart:=stellarten[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
        IF stlart<>illegal THEN
          BEGIN
          SetzeStellungsArt(farbe,stell,stlart,fstanz);
          SetzeStellungsWert(farbe,stell,0);
          IF stlart=matt THEN
              BEGIN
              mattanz[farbe]:=mattanz[farbe]+fstanz;
              StellungHinzu(farbe,stell);
              END
            ELSE  IF stlart=patt THEN
              pattanz[farbe]:=pattanz[farbe]+fstanz
            ELSE  IF stlart=tremis THEN
              tremisanz[farbe]:=tremisanz[farbe]+fstanz
            ELSE  IF stlart=remis THEN
              remisanz[farbe]:=remisanz[farbe]+fstanz
            ELSE  IF (stlart=wgewinn) OR (stlart=sgewinn) THEN
              BEGIN
              IF stlart=wgewinn THEN
                  wgewstanz[0,farbe]:=wgewstanz[0,farbe]+fstanz
                ELSE
                  sgewstanz[0,farbe]:=sgewstanz[0,farbe]+fstanz;
              StellungHinzu(farbe,stell);
              END;  { ELSE IF }
          END;  { IF }
        END;  { IF }
END;   { EndStellungenAufnehmen }

PROCEDURE ErmittelGewPattStellungen;
{*
Beschreibung:
  Prozedur ermittelt alle Stellungen, die matt oder patt sind und weist
  ihnen ihre Stellungsart zu. Die Matt-Stellungen werden in die Liste der
  gewinnbaren Stellungen aufgenommen. Stellungen, die weder matt noch patt
  und noch zu bewerten sind, wird die Anzahl der in ihnen moeglichen Zuege
  zugewiesen.
Parameter:
  Keine
Benutzte globale Variablen:
  bvorhanden, stellarten, farbe3 (indirekt), farbe4 (indirekt),
  figur3 (indirekt), figur4 (indirekt), stindex (indirekt),
  fig34gleich (indirekt)
Veraenderte globale Variablen:
  abbrechen, mattanz, pattanz, brett (indirekt), moegzuganz (indirekt),
  stellarten (indirekt), gstellanz (indirekt), gstellungen (indirekt)
Vorbedingungen:
  Die Stellungen sind bzgl. Legalitaet bewertet
*}
VAR
  zliste:POSLIST;
  zstell:STELLUNG;
  zanzs,zanzw,zanz3,zanz4:ZGBEREICH;
  zanz:INTEGER;
  sks,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  skz:INTEGER;
  stlart:STELLART;
  farbe:FARBEN;
  fstanz:INTEGER;
BEGIN
  FOR sks:=1 TO 4 DO
    BEGIN
    skz:=0;
    WHILE (skz<sks) OR bvorhanden AND (skz<8) DO
      BEGIN
      skz:=skz+1;
      FOR wks:=1 TO 8 DO
       FOR wkz:=1 TO 8 DO
        FOR f3s:=1 TO 8 DO
         FOR f3z:=1 TO 8 DO
          FOR f4s:=1 TO 8 DO
           FOR f4z:=1 TO 8 DO
             IF Repraesentant(sks,skz,wks,wkz,f3s,f3z,f4s,f4z) THEN
               IF NOT(abbrechen) THEN
                 BEGIN
                 PwerteToStell(sks,skz,wks,wkz,f3s,f3z,f4s,f4z,zstell);
                 FOR farbe:=weiss TO schwarz DO
                   BEGIN
                   stlart:=stellarten[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
                   IF stlart=nichtbew THEN
                     BEGIN
                     ErzeugePoslist(farbe,'s',zstell,FALSE,FALSE,zliste,zanzs);
                     ErzeugePoslist(farbe,'w',zstell,FALSE,FALSE,zliste,zanzw);
                     ErzeugePoslist(farbe,'3',zstell,FALSE,FALSE,zliste,zanz3);
                     ErzeugePoslist(farbe,'4',zstell,FALSE,FALSE,zliste,zanz4);
                     zanz:=zanzs+zanzw+zanz3+zanz4;
                     IF zanz>maxzuegeanz THEN
                         BEGIN
                         WRITELN;
                         WRITELN;
                         WRITELN('Konstante  maxzuegeanz  zu klein');
                         abbrechen:=TRUE;
                         END
                       ELSE  IF zanz>0 THEN
                         SetzeZuegeAnz(farbe,zstell,zanz)
                       ELSE
                         IF Schach(farbe,zstell) THEN
                             BEGIN
                             SetzeStellungsArt(farbe,zstell,matt,fstanz);
                             mattanz[farbe]:=mattanz[farbe]+fstanz;
                             StellungHinzu(farbe,zstell);
                             END
                           ELSE
                             BEGIN
                             SetzeStellungsArt(farbe,zstell,patt,fstanz);
                             pattanz[farbe]:=pattanz[farbe]+fstanz;
                             END;  { ELSE }
                     END;  { IF }
                   END;  { FOR }
                END;  { IF }
      END;  { WHILE }
    END;  { FOR }
END;   { ErmittelGewPattStellungen }

PROCEDURE BewStellungenAufnehmen(hzanz:GSTWERT);
{*
Beschreibung:
  Prozedur erhoeht fuer alle Stellungen mit geladener Bewertung, die
  keine Endstellungen sind und deren Wert einer bestimmter Anzahl von
  Zuegen bis zum Matt entspricht, die Anzahl der Stellungen der
  entsprechenden Art und nimmt sie in die Liste der als gewinnbar
  erkannten Stellungen des derzeitig bewerteten Halbzugs auf.
Parameter:
  hzanz : Halbzug, der untersucht wird, d.h. die Anzahl der Halbzuege bis
          zum Matt
Benutzte globale Variablen:
  abbrechen, bhzstellanz, bhzstellungen, bvorhanden (indirekt),
  figur3 (indirekt), figur4 (indirekt), stindex (indirekt),
  fig34gleich (indirekt)
Veraenderte globale Variablen:
  wgewstanz, sgewstanz, wgrhzanz, sgrhzanz, stellarten (indirekt),
  stellwerte (indirekt), gstellanz (indirekt), gstellungen (indirekt),
  abbrechen (indirekt)
Vorbedingungen:
  Keine
*}
VAR
  stell:STELLUNG;
  stlart:STELLART;
  farbe:FARBEN;
  hstnr:BSTBEREICH;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  fstanz:INTEGER;
BEGIN
  FOR farbe:=weiss TO schwarz DO
    FOR hstnr:=1 TO bhzstellanz[farbe,hzanz] DO
      IF NOT(abbrechen) THEN
        BEGIN
        stell:=bhzstellungen[farbe,hzanz,hstnr];
        StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
        stlart:=stellarten[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
        IF (stlart=wgewinn) OR (stlart=sgewinn) THEN
          BEGIN
          SetzeStellungsArt(farbe,stell,stlart,fstanz);
          SetzeStellungsWert(farbe,stell,hzanz);
          IF stlart=wgewinn THEN
              BEGIN
              wgewstanz[hzanz,farbe]:=wgewstanz[hzanz,farbe]+fstanz;
              wgrhzanz:=hzanz;
              END
            ELSE
              BEGIN
              sgewstanz[hzanz,farbe]:=sgewstanz[hzanz,farbe]+fstanz;
              sgrhzanz:=hzanz;
              END;  { ELSE }
          StellungHinzu(farbe,stell);
          END;  { IF }
        END;  { IF }
END;   { BewStellungenAufnehmen }

PROCEDURE ErmittelVorStellungen(amzug:FARBEN;stein:CHAR;hzanz:GSTWERT);
{*
Beschreibung:
  Prozedur ermittelt alle Vorgaengerstellungen zu den im letzten Halbzug
  als gewinnbar erkannten Stellungen. Dabei wird allen bisher unbewerteten
  Stellungen entweder ihre Stellungsbewertung zugewiesen, wenn die Farbe,
  die gewinnen kann, am Zug ist, oder andernfalls wird die Anzahl der in
  ihr nicht zum Verlust fuehrenden Zuege verringert. Hat die neue Anzahl
  den Wert 0, so wird der Stellung die Bewertung, die sie als gewinnbar
  fuer die Gegenfarbe kennzeichnet, zugewiesen.
Parameter:
  amzug : Die Farbe, fuer die die Vorgaengerstellungen ermittelt werden
  stein : Der Stein, aus dessen moeglichen Vorgaengerpositionen die
          Vorgaengerstellungen erzeugt werden
  hzanz : Die Anzahl der Halbzuege bis zum Gewinn, die den gewinnbaren
          Vorgaengerstellungen als Stellungswert zugewiesen wird
Benutzte globale Variablen:
  gstellanz, gstellungen, stellarten, moegzuganz, bvorhanden,
  farbe3 (indirekt), farbe4 (indirekt), figur3 (indirekt),
  figur4 (indirekt), stindex (indirekt), fig34gleich (indirekt)
Veraenderte globale Variablen:
  sgewstanz, wgewstanz, brett (indirekt), stellarten (indirekt),
  stellwerte (indirekt), gstellanz (indirekt), gstellungen (indirekt),
  abbrechen (indirekt), moegzuganz (indirekt)
Vorbedingungen:
  Die Stellungen sind bzgl. Legalitaet bewertet
*}
VAR
  posliste:POSLIST;
  gewstell,vorstell:STELLUNG;
  stnr:GSTBEREICH;
  posanz,posnr:ZGBEREICH;
  mgzanz,neuzuegeanz:ZGBEREICH;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  vors,vorz:SZBEREICH;
  gewstlart,vorstlart:STELLART;
  gfarbe:FARBEN;
  fstanz:INTEGER;
BEGIN
  IF (stein='w') AND (amzug=weiss) OR (stein='s') AND (amzug=schwarz)
   OR (stein='3') AND (amzug=farbe3) OR (stein='4') AND (amzug=farbe4) THEN
    BEGIN
    IF amzug=weiss THEN
        gfarbe:=schwarz
      ELSE
        gfarbe:=weiss;
    FOR stnr:=1 TO gstellanz[1-stindex,gfarbe] DO
      IF NOT(abbrechen) THEN
        BEGIN
        gewstell:=gstellungen[1-stindex,gfarbe,stnr];
        vorstell:=gewstell;
        StellToPwerte(gewstell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
        gewstlart:=stellarten[gfarbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
        ErzeugePoslist(amzug,stein,gewstell,TRUE,FALSE,posliste,posanz);
        FOR posnr:=1 TO posanz DO
          BEGIN
          vors:=posliste[posnr].spalte;
          vorz:=posliste[posnr].zeile;
          IF stein='w' THEN
              BEGIN
              vorstell.wks:=vors;
              vorstell.wkz:=vorz;
              vorstlart:=stellarten[amzug,sks,skz,vors,vorz,f3s,f3z,f4s,f4z];
              mgzanz:=moegzuganz[amzug,sks,skz,vors,vorz,f3s,f3z,f4s,f4z];
              END
            ELSE  IF stein='s' THEN
              BEGIN
              vorstell.sks:=vors;
              vorstell.skz:=vorz;
              vorstlart:=stellarten[amzug,vors,vorz,wks,wkz,f3s,f3z,f4s,f4z];
              mgzanz:=moegzuganz[amzug,vors,vorz,wks,wkz,f3s,f3z,f4s,f4z];
              END
            ELSE  IF stein='3' THEN
              BEGIN
              vorstell.f3s:=vors;
              vorstell.f3z:=vorz;
              vorstlart:=stellarten[amzug,sks,skz,wks,wkz,vors,vorz,f4s,f4z];
              mgzanz:=moegzuganz[amzug,sks,skz,wks,wkz,vors,vorz,f4s,f4z];
              END
            ELSE
              BEGIN
              vorstell.f4s:=vors;
              vorstell.f4z:=vorz;
              vorstlart:=stellarten[amzug,sks,skz,wks,wkz,f3s,f3z,vors,vorz];
              mgzanz:=moegzuganz[amzug,sks,skz,wks,wkz,f3s,f3z,vors,vorz];
              END;  { ELSE }
          IF vorstlart=nichtbew THEN
            IF (gewstlart=matt) OR (amzug=weiss) AND (gewstlart=wgewinn)
             OR (amzug=schwarz) AND (gewstlart=sgewinn) THEN
                BEGIN
                IF amzug=weiss THEN
                    BEGIN
                    SetzeStellungsArt(amzug,vorstell,wgewinn,fstanz);
                    wgewstanz[hzanz,amzug]:=wgewstanz[hzanz,amzug]+fstanz;
                    END
                  ELSE
                    BEGIN
                    SetzeStellungsArt(amzug,vorstell,sgewinn,fstanz);
                    sgewstanz[hzanz,amzug]:=sgewstanz[hzanz,amzug]+fstanz;
                    END;  { ELSE }
                SetzeStellungsWert(amzug,vorstell,hzanz);
                StellungHinzu(amzug,vorstell);
                END
              ELSE
                BEGIN
                IF bvorhanden THEN
                    neuzuegeanz:=mgzanz-1
                  ELSE  IF Symetriestellung(vorstell)
                   AND Symetriestellung(gewstell) THEN
                    neuzuegeanz:=mgzanz-1
                  ELSE  IF Symetriestellung(vorstell) THEN
                    neuzuegeanz:=mgzanz-2
                  ELSE  IF Symetriestellung(gewstell)
                   AND ((wks=wkz) AND (vors>vorz)
                   OR (wks=9-wkz) AND (vors>9-vorz)) THEN
                    neuzuegeanz:=mgzanz
                  ELSE
                    neuzuegeanz:=mgzanz-1;
                SetzeZuegeAnz(amzug,vorstell,neuzuegeanz);
                IF neuzuegeanz=0 THEN
                  BEGIN
                  IF amzug=weiss THEN
                      BEGIN
                      SetzeStellungsArt(amzug,vorstell,sgewinn,fstanz);
                      sgewstanz[hzanz,amzug]:=sgewstanz[hzanz,amzug]+fstanz;
                      END
                    ELSE
                      BEGIN
                      SetzeStellungsArt(amzug,vorstell,wgewinn,fstanz);
                      wgewstanz[hzanz,amzug]:=wgewstanz[hzanz,amzug]+fstanz;
                      END;  { ELSE }
                  SetzeStellungsWert(amzug,vorstell,hzanz);
                  StellungHinzu(amzug,vorstell);
                  END;  { IF }
                END;  { ELSE }
          END;  { FOR }
        END;  { IF }
    END;  { IF }
END;   { ErmittelVorStellungen }

PROCEDURE EpKorrektur;
{*
Beschreibung:
  Prozedur korrigiert die ermittelten Bewertungen aufgrund der Moeglichkeit
  des "en passent"-Schlagens.
  In der derzeitigen Version findet keine Korrektur statt sondern es wird
  nur eine Warnung ausgegeben, dass die e.p.-Regel nicht beruecksichtigt
  wird.
Parameter:
  Keine
Benutzte globale Variablen:
  figur3, figur4, farbe3, farbe4
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Die Stellungen wurden vollstaendig bewertet
*}
BEGIN
  IF (figur3>='a') AND (figur4>='a') AND (farbe3<>farbe4) THEN
    IF (ABS(ORD(figur3)-ORD(figur4))=1) OR (figur3='x') OR (figur4='x') THEN
      BEGIN
      WRITELN;
      WRITELN;
      WRITELN('Achtung : Analyse ohne "en passant"-Regel');
      END;  { IF }
END;   { EpKorrektur }

PROCEDURE ErmittelRemisStellungen;
{*
Beschreibung:
  Prozedur weist, nachdem alle gewinnbaren Stellungen bewertet wurden,
  allen noch nicht bewerteten Stellungen die Stellungsart remis bzw.
  technisch-remis zu.
Parameter:
  Keine
Benutzte globale Variablen:
  bvorhanden, abbrechen, stellarten, figur3 (indirekt), figur4 (indirekt),
  fig34gleich (indirekt)
Veraenderte globale Variablen:
  tremisanz, remisanz, stellarten (indirekt)
Vorbedingungen:
  Alle Stellungen wurden bezueglich Legalitaet und Gewinnbarkeit bewertet
*}
VAR
  stell:STELLUNG;
  stlart:STELLART;
  farbe:FARBEN;
  sks,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  skz:INTEGER;
  fstanz:INTEGER;
BEGIN
  FOR sks:=1 TO 4 DO
    BEGIN
    skz:=0;
    WHILE (skz<sks) OR bvorhanden AND (skz<8) DO
      BEGIN
      skz:=skz+1;
      FOR wks:=1 TO 8 DO
       FOR wkz:=1 TO 8 DO
        FOR f3s:=1 TO 8 DO
         FOR f3z:=1 TO 8 DO
          FOR f4s:=1 TO 8 DO
           FOR f4z:=1 TO 8 DO
             IF Repraesentant(sks,skz,wks,wkz,f3s,f3z,f4s,f4z) THEN
               FOR farbe:=weiss TO schwarz DO
                 IF NOT(abbrechen) THEN
                   BEGIN
                   stlart:=stellarten[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
                   IF stlart=nichtbew THEN
                     IF (figur3='L') AND (figur4='L')
                      AND (ODD(f3s+f3z)=ODD(f4s+f4z)) THEN
                         BEGIN
                         PwerteToStell(sks,skz,wks,wkz,f3s,f3z,f4s,f4z,stell);
                         SetzeStellungsArt(farbe,stell,tremis,fstanz);
                         tremisanz[farbe]:=tremisanz[farbe]+fstanz;
                         END
                       ELSE
                         BEGIN
                         PwerteToStell(sks,skz,wks,wkz,f3s,f3z,f4s,f4z,stell);
                         SetzeStellungsArt(farbe,stell,remis,fstanz);
                         remisanz[farbe]:=remisanz[farbe]+fstanz;
                         END;  { IF }
                   END;  { IF }
      END;  { WHILE }
    END;  { FOR }
END;   { ErmittelRemisStellungen }

PROCEDURE GewStellAnzAusgabe(farbe:FARBEN;gewstanz:HZSPSTELLUNGSANZ;
                             grhzanz:BSTWERT;
                             VAR gewinanz:SPSTELLUNGSANZ);
{*
Beschreibung:
  Prozedur gibt fuer jeden Halbzug die Anzahl der fuer die uebergebene
  Farbe gewinnbaren Stellungen aus, jeweils getrennt danach, welche
  Farbe am Zug ist. Ausserdem ermittelt sie die Gesamtanzahl der
  Gewinnstellungen der uebergebenen Farbe in Abhaengigkeit von der
  Farbe, die am Zug ist.
Parameter:
  farbe = Die Farbe, fuer die die Ausgaben erfolgen
  gewstanz = Die Anzahl der gewinnbaren Stellungen fuer jeden Halbzug
  grhzanz = Die Anzahl der Halbzuege, fuer die die Ausgabe erfolgen muss
  gewinanz = Variable fuer die Gesamtanzahl der Gewinnstellungen der
             uebergebenen Farbe in Abhaengigkeit von der Farbe, die am
             Zug ist
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
VAR
  hznr:GSTWERT;
BEGIN
  WRITELN;
  WRITELN;
  WRITE('Anzahl gewinnbarer Stellungen fuer');
  IF farbe=weiss THEN
      WRITELN(' Weiss :')
    ELSE
      WRITELN(' Schwarz :');
  WRITELN;
  gewinanz[weiss]:=0;
  gewinanz[schwarz]:=0;
  IF (gewstanz[0,weiss]+gewstanz[0,schwarz]=0)
   AND (gewstanz[1,weiss]+gewstanz[1,schwarz]=0) THEN
      WRITELN('Keine gewinnbaren Stellung')
    ELSE
      IF gewstanz[0,weiss]+gewstanz[0,schwarz]>0 THEN
        BEGIN
        WRITE('Gewinn in  0 Hz. :');
        IF gewstanz[0,weiss]>0 THEN
          BEGIN
          WRITE('  mit Weiss am Zug   : ',gewstanz[0,weiss]:7);
          gewinanz[weiss]:=gewinanz[weiss]+gewstanz[0,weiss];
          END;  { IF }
        IF gewstanz[0,schwarz]>0 THEN
          BEGIN
          WRITE('  mit Schwarz am Zug : ',gewstanz[0,schwarz]:7);
          gewinanz[schwarz]:=gewinanz[schwarz]+gewstanz[0,schwarz];
          END;  { IF }
        WRITELN;
        END;  { IF }
  hznr:=0;
  WHILE (hznr<grhzanz)
   OR (gewstanz[hznr+1,weiss]+gewstanz[hznr+1,schwarz]>0) DO
    BEGIN
    hznr:=hznr+1;
    WRITE('Gewinn in ',hznr:2,' Hz. :');
    IF gewstanz[hznr,weiss]+gewstanz[hznr,schwarz]=0 THEN
        WRITELN('  keine gewinnbare Stellung')
      ELSE
        BEGIN
        IF gewstanz[hznr,weiss]>0 THEN
          BEGIN
          WRITE('  mit Weiss am Zug   : ',gewstanz[hznr,weiss]:7);
          gewinanz[weiss]:=gewinanz[weiss]+gewstanz[hznr,weiss];
          END;  { IF }
        IF gewstanz[hznr,schwarz]>0 THEN
          BEGIN
          WRITE('  mit Schwarz am Zug : ',gewstanz[hznr,schwarz]:7);
          gewinanz[schwarz]:=gewinanz[schwarz]+gewstanz[hznr,schwarz];
          END;  { IF }
        WRITELN;
        END;  { ELSE }
    END;  { WHILE }
END;   { GewStellAnzAusgabe }

PROCEDURE GesStellAnzAusgabe;
{*
Beschreibung:
  Prozedur gibt fuer beide Farben die Gesamtanzahl der Stellungen jeder
  Stellungsart aus.
Parameter:
  Keine
Benutzte globale Variablen:
  legalanz, mattanz, tremisanz, wgewinanz, sgewinanz, remisanz
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Die Stellungen wurden vollstaendig bewertet
*}
BEGIN
  WRITELN;
  WRITELN;
  WRITELN('Anzahl der Stellungen :');
  WRITELN;
  WRITELN('   mit Weiss am Zug :');
  WRITELN('      Legal             : ',legalanz[weiss]:8);
  WRITELN('      Weiss matt        : ',mattanz[weiss]:8);
  WRITELN('      Weiss patt        : ',pattanz[weiss]:8);
  WRITELN('      Tech. remis       : ',tremisanz[weiss]:8);
  WRITELN('      Weiss wird gew.   : ',wgewinanz[weiss]:8);
  WRITELN('      Schwarz wird gew. : ',sgewinanz[weiss]:8);
  WRITELN('      Remis             : ',remisanz[weiss]:8);
  WRITELN;
  WRITELN('   mit Schwarz am Zug :');
  WRITELN('      Legal             : ',legalanz[schwarz]:8);
  WRITELN('      Schwarz matt      : ',mattanz[schwarz]:8);
  WRITELN('      Schwarz patt      : ',pattanz[schwarz]:8);
  WRITELN('      Tech. remis       : ',tremisanz[schwarz]:8);
  WRITELN('      Weiss wird gew.   : ',wgewinanz[schwarz]:8);
  WRITELN('      Schwarz wird gew. : ',sgewinanz[schwarz]:8);
  WRITELN('      Remis             : ',remisanz[schwarz]:8);
END;   { GesStellAnzAusgabe }

PROCEDURE Speichern;
{*
Beschreibung:
  Prozedur speichert die Daten des analysierten Endspiels als Datei.
Parameter:
  Keine
Benutzte globale Variablen:
  farbe3, figur3, farbe4, figur4, vorstellpruefen, schnellstmatt,
  bvorhanden, stellarten, stellwerte
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Das Endspiel wurde vollstaendig analysiert, d.h. alle Stellungen wurden
  vollstaendig bewertet
*}
VAR
  stlart:STELLART;
  stlwert:GSTWERT;
  farbe:FARBEN;
  sks,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  skz:INTEGER;
  datname:STRING(31);
  anlydaten:FILE OF DATBEREICH;
BEGIN
  WRITELN;
  WRITELN;
  WRITELN('Speichern der Daten');
  WRITELN;
  WRITE('Name fuer Datei = ');
  READLN(datname);
  REWRITE(anlydaten,datname);
  WRITE(anlydaten,4);
  WRITE(anlydaten,ORD(farbe3));
  WRITE(anlydaten,ORD(figur3));
  WRITE(anlydaten,ORD(farbe4));
  WRITE(anlydaten,ORD(figur4));
  WRITE(anlydaten,ORD(vorstellpruefen));
  WRITE(anlydaten,ORD(schnellstmatt));
  FOR sks:=1 TO 4 DO
    BEGIN
    skz:=0;
    WHILE (skz<sks) OR bvorhanden AND (skz<8) DO
      BEGIN
      skz:=skz+1;
      FOR wks:=1 TO 8 DO
       FOR wkz:=1 TO 8 DO
        FOR f3s:=1 TO 8 DO
         FOR f3z:=1 TO 8 DO
          FOR f4s:=1 TO 8 DO
           FOR f4z:=1 TO 8 DO
             FOR farbe:=weiss TO schwarz DO
               BEGIN
               stlart:=stellarten[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
               stlwert:=stellwerte[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
               WRITE(anlydaten,ORD(stlart));
               WRITE(anlydaten,stlwert);
               END;  { FOR }
      END;  { WHILE }
    END;  { FOR }
END;   { Speichern }


BEGIN
  WRITELN;
  WRITELN('Analysieren von 4-Steine-Schachendspielen');
  InitDaten;
  FigurenEinlesen;
  WRITELN;
  REPEAT
    WRITELN;
    WRITE('(U)ntersuchung des Endspiels oder (D)atenerzeugung : ');
    READLN(chin);
  UNTIL (chin='U') OR (chin='D');
  untersuchung:=(chin='U');
  IF NOT(untersuchung) THEN
      vorstellpruefen:=FALSE
    ELSE
      BEGIN
      WRITELN;
      REPEAT
        WRITELN;
        WRITE('(k)orrekte oder (v)ollstaendige Definition');
        WRITE(' der Legalitaet (k/v) : ');
        READLN(chin);
      UNTIL (chin='k') OR (chin='v');
      vorstellpruefen:=(chin='k');
      END;  { ELSE }
  IF NOT(untersuchung)
   OR (farbe3=farbe4) AND (figur3<'a') AND (figur4<'a') THEN
      schnellstmatt:=TRUE
    ELSE
      BEGIN
      WRITELN;
      REPEAT
        WRITELN;
        WRITE('schnellstes (M)att oder schnellster (G)ewinn : ');
        READLN(chin);
      UNTIL (chin='M') OR (chin='G');
      schnellstmatt:=(chin='M');
      END;  { ELSE }
  SchlagDatenLaden;
  IF NOT(abbrechen) THEN
    IF bvorhanden THEN
      UmwandelDatenLaden;
  IF NOT(abbrechen) THEN
    BEGIN
    WRITELN;
    WRITELN;
    WRITELN('Analyse laeuft ...');
    ErmittelAnfStellungen;
    END;  { IF }
  IF NOT(abbrechen) AND vorstellpruefen THEN
    ErmittelIllegalStellungen;
  stindex:=0;
  gstellanz[stindex,weiss]:=0;
  gstellanz[stindex,schwarz]:=0;
  IF NOT(abbrechen) THEN
    BEGIN
    EndStellungenAufnehmen;
    ErmittelGewPattStellungen;
    END;  { IF }
  hzanz:=0;
  WHILE ((hzanz<grhzamin)
   OR (gstellanz[stindex,weiss]+gstellanz[stindex,schwarz]>0))
   AND NOT(abbrechen) DO
    BEGIN
    stindex:=1-stindex;
    gstellanz[stindex,weiss]:=0;
    gstellanz[stindex,schwarz]:=0;
    IF hzanz<maxghzuganz THEN
        BEGIN
        hzanz:=hzanz+1;
        IF hzanz<=grhzamin THEN
          BewStellungenAufnehmen(hzanz);
        END
      ELSE
        BEGIN
        WRITELN;
        WRITELN;
        WRITELN('Konstante  maxghzuganz  zu klein');
        abbrechen:=TRUE;
        END;  { ELSE }
    FOR farbe:=weiss TO schwarz DO
      BEGIN
      IF NOT(abbrechen) THEN
        ErmittelVorStellungen(farbe,'3',hzanz);
      IF NOT(abbrechen) THEN
        ErmittelVorStellungen(farbe,'4',hzanz);
      IF NOT(abbrechen) THEN
        ErmittelVorStellungen(farbe,'s',hzanz);
      IF NOT(abbrechen) THEN
        ErmittelVorStellungen(farbe,'w',hzanz);
      END;  { FOR }
    END;  { WHILE }
  IF NOT(abbrechen) THEN
    EpKorrektur;
  IF NOT(abbrechen) THEN
    ErmittelRemisStellungen;
  IF NOT(abbrechen) AND untersuchung THEN
    BEGIN
    GewStellAnzAusgabe(weiss,wgewstanz,wgrhzanz,wgewinanz);
    GewStellAnzAusgabe(schwarz,sgewstanz,sgrhzanz,sgewinanz);
    END;  { IF }
  IF abbrechen THEN
      BEGIN
      WRITELN;
      WRITELN;
      WRITELN('Analyse abgebrochen');
      END
    ELSE  IF NOT(untersuchung) THEN
      Speichern
    ELSE
      BEGIN
      GesStellAnzAusgabe;
      WRITELN;
      REPEAT
        WRITELN;
        WRITE('(S)eichern oder (E)nde : ');
        READLN(chin);
      UNTIL (chin='E') OR (chin='S');
      IF chin='S' THEN
        Speichern;
      END;  { ELSE }
  WRITELN;
END.

