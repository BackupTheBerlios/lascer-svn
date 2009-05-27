{*
   Dateiname      : ableitung4.pas
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


{ Programm untersucht die Stellungen von 4-Steine-Schachendspielen auf
  Ableitbarkeit aus einer Menge von Ausgangsstellungen heraus.

  Zu jeder moeglichen Stellung wird ermittelt, ob sie aus einer Reihe
  von Ausgangsstellungen ableitbar ist und wieviele Zuege bis zu ihr
  erforderlich sind.
  Dazu werden von allen Ausgangsstellungen aus die jeweils moeglichen
  Folgestellugen erzeugt bis alle ableitbaren Stellungen als solche
  erkannt sind. }

{* Version ohne Unterverwandlung *}



PROGRAM Ableitung4(INPUT,OUTPUT);

CONST
  maxzuegeanz = 60;
  {* maxzuegeanz gibt eine Obergrenze fuer die Anzahl der moeglichen Zuege
     einer Figur an *}
  maxagstanz = 70;
  {* maxagstanz gibt eine Obergrenze fuer die Anzahl der vorgegebenen
     Ausgangsstellungen an, aus denen die anderen Stellungen abgeleitet
     werden *}
  maxahzuganz = 40;
  {* maxahzuganz gibt eine Obergrenze fuer die Anzahl der Zuege bis zu
     einer abgeleiteten Stellung bei den analysierten Endspielen an *}
  maxablstanz = 1200000;   {* fuer Endspiel ohne x-Bauer mit Hauptfigur
                              reicht 600000 *}
  {* maxablstanz gibt eine Obergrenze fuer die Anzahl der in einem Halbzug
     abgeleiteten Stellungen an *}

TYPE
  SZBEREICH = 1..8;
  {* SZBEREICH beschreibt den Bereich fuer Spalten und Zeilen *}
  ZGBEREICH = 0..maxzuegeanz;
  {* ZGBEREICH beschreibt den Bereich fuer die Anzahl moeglicher Zuege
     einer Figur *}
  AGSTBEREICH = 0..maxagstanz;
  {* AGSTBEREICH beschreibt den Bereich fuer die Anzahl der
     Ausgangsstellungen *}
  ASTWERT = 0..maxahzuganz;
  {* ASTWERT beschreibt den Bereich der Stellungswerte, d.h. den Bereich
     fuer die Anzahl der Zuege bis zu den abgeleiteten Stellungen in den
     untersuchten Endspielen *}
  ASTBEREICH = 0..maxablstanz;
  {* ASTBEREICH beschreibt den Bereich fuer die Anzahl der in einem Halbzug
     ableitbare Stellungen *}
  DATBEREICH = 0..120;   {* 0..Max(maxahzuganz,120), 120 = ORD('x') *}
  {* DATBEREICH beschreibt den Bereich von Werten bei den zu speichernden
     Dateien *}
  FARBEN = (weiss,schwarz);
  {* FARBEN benennt die moeglichen Farben der Figuren *}
  ASTELLART = (illegal,nachesp,nichtbew,ableitbar,isoliert);
  {* ASTELLART benennt die moeglichen Stellungsarten *}
  STELLUNGSZANZ = ARRAY[1..8,1..8,1..8,1..8,1..8,1..8,1..8,1..8] OF ZGBEREICH;
  {* STELLUNGSZANZ beschreibt das Feld, in dem fuer jede Stellung die
     aktuelle Anzahl der nicht zum Verlust fuehrenden Zuege gespeichert
     ist *}
  ASTELLUNGSWERTE = ARRAY[1..8,1..8,1..8,1..8,1..8,1..8,1..8,1..8] OF ASTWERT;
  {* ASTELLUNGSWERTE beschreibt das Feld der Stellungswerte der Stellungen,
     d.h. fuer jede Stellung die jeweils notwenige Anzahl von Halbzuegen
     aus einer Ausgangsstellung bis zur jeweiligen Stellung *}
  ASTELLUNGSARTEN = ARRAY[1..8,1..8,1..8,1..8,1..8,1..8,1..8,1..8] OF ASTELLART;
  {* ASTELLUNGSARTEN beschreibt das Feld, in dem fuer jede Stellung ihre
     Art gespeichert ist *}
  POSITION = RECORD
               spalte,zeile : SZBEREICH;
             END;
  {* POSITION beschreibt die Position einer Figur durch ihre Spalte
     und Zeile *}
  POSLIST = ARRAY[1..maxzuegeanz] OF POSITION;
  {* POSLIST beschreibt eine Liste von Positionen *}
  STELLUNG = RECORD
               sks,skz,wks,wkz,f3s,f3z,f4s,f4z : SZBEREICH;
             END;
  {* STELLUNG beschreibt eine Stellung durch die Spalten und Zeilen der
     Figuren, ohne Angabe der Farbe, die am Zug ist.
     'sk' steht fuer den schwarzen Koenig, 'wk' fuer den weissen Koenig,
     'f3' fuer die dritte Figur und 'f4' fuer die vierte Figur. Das
     angehaengte 's' steht fuer Spalte, das 'z' fuer Zeile. *}
  ABLSTELLUNGEN = ARRAY[1..maxablstanz] OF STELLUNG;
  {* ABLSTELLUNGEN beschreibt die Menge der ableitbaren Stellungen, die
     in einem Halbzug ermittelt werden *}
  AUGSTELLUNGEN = ARRAY[1..maxagstanz] OF STELLUNG;
  {* AUGSTELLUNGEN beschreibt die Menge der Ausgangsstellungen *}
  SPSTELLUNGSANZ = ARRAY[weiss..schwarz] OF INTEGER;
  {* SPSTELLUNGSANZ beschreibt fuer jede Farbe die gesamte Anzahl der
     ableitbaren Stellungen nach Spiegelung *}
  HZSPSTELLUNGSANZ = ARRAY[0..maxahzuganz] OF SPSTELLUNGSANZ;
  {* HZSPSTELLUNGSANZ beschreibt fuer jeden Halbzug die Anzahl von
     ableitbaren Stellungen einer Farbe nach Spiegelung *}

VAR
  brett : ARRAY[-1..10,-1..10] OF BOOLEAN;   { global }
  {* brett wird beim Test eines Zuges auf Legalitaet benutzt und gibt fuer
     jedes Feld eines erweiterten Brettes an, ob die jeweilige Figur darauf
     ziehen darf *}
  stellwerte : ARRAY[weiss..schwarz] OF ASTELLUNGSWERTE;   { global }
  {* stellwerte enthaelt fuer alle Stellungen des zu untersuchenden
     Endspiels ihre Stellungswerte, d.h die jeweils notwenige Anzahl
     von Halbzuegen aus einer Ausgangsstellung bis zur jeweiligen
     Stellung *}
  stellarten : ARRAY[weiss..schwarz] OF ASTELLUNGSARTEN;   { global }
  {* stellarten enthaelt die Stellungsarten aller Stellungen des zu
     untersuchenden Endspiels fuer beide Farben *}
  astellungen : ARRAY[0..1,weiss..schwarz] OF ABLSTELLUNGEN;   { global }
  {* astellungen stellt Listen der am Anfang auf Illegalitaet zu
     pruefenden oder der im jeweils letzten Halbzug als ableitbar
     erkannten Stellungen fuer beide Farben dar *}
  agstellungen :  ARRAY[weiss..schwarz] OF AUGSTELLUNGEN;   { global }
  {* agstellungen enthaelt die Menge der Ausgangsstellungen *}
  astellanz : ARRAY[0..1,weiss..schwarz] OF ASTBEREICH;   { global }
  {* astellanz gibt die Anzahl der am Anfang auf Illegalitaet zu
     pruefenden oder der im jeweils letzten Halbzug als ableitbar
     erkannten Stellungen fuer beide Farben dar *}
  agstellanz : ARRAY[weiss..schwarz] OF AGSTBEREICH;   { global }
  {* agstellanz enthaelt die Anzahl der Ausgangsstellungen *}
  albstanz : HZSPSTELLUNGSANZ;   { global }
  {* Variable gibt die Anzahl der ableitbaren Stellungen fuer jeden
     Halbzug fuer die jeweilige Farbe an *}
  legalanz,ableitanz,isoliertanz : SPSTELLUNGSANZ;   { global }
  {* Variablen geben die Anzahl der Stellungen der jeweiligen Art an *}
  farbe3,farbe4 : FARBEN;   { global }
  {* Variablen geben die Farben der zu ladenden dritten und vierten Figur
     an *}
  stindex : 0..1;   { global }
  {* stindex schaltet nach jedem untersuchten Halbzug zwischen zwei
     Listen von als illegal erkannten oder ableitbaren Stellungen hin
     und her, so dass die neue Liste geschrieben werden kann waehrend
     die alte noch benutzt wird *}
  hzanz : ASTWERT;
  farbe : FARBEN;
  abbrechen : BOOLEAN;   { global }
  {* abbrechen gibt an, ob die Analyse, z.B. wegen Ueberschreiten einer
     Konstanten, abgebrochen werden muss *}
  vorstellpruefen : BOOLEAN;   { global }
  {* vorstellpruefen gibt an, ob geprueft werden soll, ob eine Stellung
     eine legale Vorgaengerstellung hat, wobei sie, wenn eine solche nicht
     existiert, illegal waere *}
  bvorhanden : BOOLEAN;   { global }
  {* bvorhanden gibt an, ob im zu analysierenden Endspiel ein Bauer
     vorhanden ist, d.h. die dritte oder vierte Figur ein Bauer ist *}
  fig34gleich : BOOLEAN;   { global }
  {* fig34gleich gibt an, ob die beiden vorgegebenen Figuren gleich sind,
     d.h. in Art und Farbe uebereinstimen *}
  figur3,figur4 : CHAR;   { global }
  {* Variablen geben die Arten dritten und vierten Figur an *}
  chin : CHAR;


FUNCTION StellartTyp(stlartwert:INTEGER):ASTELLART;
{*
Beschreibung:
  Funktion wandelt den uebergebenen INTEGER-Wert, der eine Stellungsart
  repraesentiert, in die zugehoerige Stellungsart um.
Parameter:
  stlartwert = INTEGER-Wert, der einen ASTELLART-Wert repraesentiert
Benutzte globale Variablen:
  Keine
Vorbedingungen:
  Keine
*}
VAR
  stlart:ASTELLART;
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
  brett, albstanz, astellanz, agstellanz, legalanz, ableitanz,
  isoliertanz, abbrechen
Vorbedingungen:
  Keine
*}
VAR
  ahzanz:ASTWERT;
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
    FOR ahzanz:=0 TO maxahzuganz DO
      albstanz[ahzanz,farbe]:=0;
    astellanz[0,farbe]:=0;
    astellanz[1,farbe]:=0;
    agstellanz[farbe]:=0;
    legalanz[farbe]:=0;
    ableitanz[farbe]:=0;
    isoliertanz[farbe]:=0;
    END;  { FOR }
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

PROCEDURE SpiegeleStellArt(fig3,fig4:CHAR;stell:STELLUNG;art:ASTELLART;
                           VAR stlarten:ASTELLUNGSARTEN);
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

PROCEDURE SpiegeleStellWert(fig3,fig4:CHAR;stell:STELLUNG;wert:ASTWERT;
                            VAR stlwerte:ASTELLUNGSWERTE);
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

PROCEDURE SetzeStellungsArt(farbe:FARBEN;stell:STELLUNG;art:ASTELLART;
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

PROCEDURE SetzeStellungsWert(farbe:FARBEN;stell:STELLUNG;wert:ASTWERT);
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
  stlart:ASTELLART;
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
  stlart:ASTELLART;
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
  stlart:ASTELLART;
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
  stlart:ASTELLART;
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
  illegal oder im letzten Halbzug als ableitbar erkannten Stellungen
  hinzu.
Parameter:
  farbe = Die Farbe, die in der Stellung am Zug ist
  stell = Die Stellung, die der Liste hinzugefuegt wird
Benutzte globale Variablen:
  stindex
Veraenderte globale Variablen:
  astellanz, astellungen, abbrechen
Vorbedingungen:
  Keine
*}
BEGIN
  IF astellanz[stindex,farbe]<maxablstanz THEN
      BEGIN
      astellanz[stindex,farbe]:=astellanz[stindex,farbe]+1;
      astellungen[stindex,farbe,astellanz[stindex,farbe]]:=stell;
      END
    ELSE
      BEGIN
      WRITELN;
      WRITELN;
      WRITELN('Konstante  maxablstanz  zu klein');
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
    ELSE  IF (figur3>='a') AND (figur4='L') AND (farbe3=farbe4) THEN
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

PROCEDURE AugStellungHinzu(farbe:FARBEN;stell:STELLUNG);
{*
Beschreibung:
  Prozedur fuegt die uebergebene Stellung der Liste von Ausgangsstellungen
  hinzu.
Parameter:
  farbe = Die Farbe, die in der Stellung am Zug ist
  stell = Die Stellung, die der Liste hinzugefuegt wird
Benutzte globale Variablen:
  Keine
Veraenderte globale Variablen:
  agstellanz, agstellungen, abbrechen
Vorbedingungen:
  Keine
*}
BEGIN
  IF agstellanz[farbe]<maxagstanz THEN
      BEGIN
      agstellanz[farbe]:=agstellanz[farbe]+1;
      agstellungen[farbe,agstellanz[farbe]]:=stell;
      END
    ELSE
      BEGIN
      WRITELN;
      WRITELN;
      WRITELN('Konstante  maxagstanz  zu klein');
      abbrechen:=TRUE;
      END;  { ELSE }
END;   { AugStellungHinzu }

FUNCTION Ausgangsstellung(amzug:FARBEN;stell:STELLUNG):BOOLEAN;
{*
Beschreibung:
  Funktion liefert genau dann TRUE, wenn die uebergebene Stellung eine
  Ausgangsstellung ist.
Parameter:
  amzug = Die Farbe, die in der Stellung am Zug ist
  stell = Die Stellung, die getestet werden soll, ob sie eine
          Ausgangsstellung ist.
Benutzte globale Variablen:
  farbe3, figur3, farbe4, figur4, bvorhanden
Veraenderte globale Variablen:
  Keine.
Vorbedingungen:
  (sks<=4) AND ((skz<=sks) OR bvorhanden AND (skz<=8))
*}
VAR
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  spdif1,spdif2:INTEGER;
  agstellung3,agstellung4:BOOLEAN;
BEGIN
  StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  agstellung3:=FALSE;
  agstellung4:=FALSE;
  IF (bvorhanden) AND (sks=4) AND (skz=8) AND (wks=4) AND (wkz=1) THEN
      BEGIN
      IF (figur3>='a') THEN
          BEGIN
          IF (farbe3=weiss) AND (f3z=2) OR (farbe3=schwarz) AND (f3z=7) THEN
              IF figur3='x' THEN
                  agstellung3:=TRUE
                ELSE
                  BEGIN
                  spdif1:=ABS(f3s-1-(ORD(figur3)-ORD('a')));
                  spdif2:=ABS(8-f3s-(ORD(figur3)-ORD('a')));
                  agstellung3:=(spdif1=0) OR (spdif2=0);
                  END
            ELSE  IF (farbe3=farbe4) AND (figur3=figur4) THEN
              IF farbe3=weiss THEN
                  agstellung3:=(f3s=f4s) AND (f3z=3)
                ELSE
                  agstellung3:=(f3s=f4s) AND (f3z=6);
          END
        ELSE
          IF (farbe3=weiss) AND (f3z=1) OR (farbe3=schwarz) AND (f3z=8) THEN
            CASE figur3 OF
              'T' : agstellung3:=(f3s=1) OR (f3s=8);
              'S' : agstellung3:=(f3s=2) OR (f3s=7);
              'L' : agstellung3:=(f3s=3) OR (f3s=6);
              'D' : agstellung3:=(f3s=5);
            END;  { CASE }
      IF (figur4>='a') THEN
          BEGIN
          IF (farbe4=weiss) AND (f4z=2) OR (farbe4=schwarz) AND (f4z=7) THEN
              IF figur4='x' THEN
                  agstellung4:=TRUE
                ELSE
                  BEGIN
                  spdif1:=ABS(f4s-1-(ORD(figur4)-ORD('a')));
                  spdif2:=ABS(8-f4s-(ORD(figur4)-ORD('a')));
                  agstellung4:=(spdif1=0) OR (spdif2=0);
                  END
            ELSE  IF (farbe4=farbe3) AND (figur4=figur3) THEN
              IF farbe4=weiss THEN
                  agstellung4:=(f4s=f3s) AND (f4z=3)
                ELSE
                  agstellung4:=(f4s=f3s) AND (f4z=6);
          END
        ELSE
          IF (farbe4=weiss) AND (f4z=1) OR (farbe4=schwarz) AND (f4z=8) THEN
            CASE figur4 OF
              'T' : agstellung4:=(f4s=1) OR (f4s=8);
              'S' : agstellung4:=(f4s=2) OR (f4s=7);
              'L' : agstellung4:=(f4s=3) OR (f4s=6);
              'D' : agstellung4:=(f4s=5);
            END;  { CASE }
      END
    ELSE  IF NOT(bvorhanden)
     AND (sks=4) AND (skz=1) AND (wks=4) AND (wkz=8) THEN
      BEGIN
      IF (farbe3=weiss) AND (f3z=8) OR (farbe3=schwarz) AND (f3z=1) THEN
          CASE figur3 OF
            'T' : agstellung3:=(f3s=1) OR (f3s=8);
            'S' : agstellung3:=(f3s=2) OR (f3s=7);
            'L' : agstellung3:=(f3s=3) OR (f3s=6);
            'D' : agstellung3:=(f3s=5);
          END  { CASE }
        ELSE
          IF (farbe3=farbe4) AND (figur3=figur4)
           AND ((figur3='D') OR (figur3='L')) THEN
            IF farbe3=weiss THEN
                agstellung3:=(f3s=f4s) AND (f3z=6)
              ELSE
                agstellung3:=(f3s=f4s) AND (f3z=3);
      IF (farbe4=weiss) AND (f4z=8) OR (farbe4=schwarz) AND (f4z=1) THEN
          CASE figur4 OF
            'T' : agstellung4:=(f4s=1) OR (f4s=8);
            'S' : agstellung4:=(f4s=2) OR (f4s=7);
            'L' : agstellung4:=(f4s=3) OR (f4s=6);
            'D' : agstellung4:=(f4s=5);
          END  { CASE }
        ELSE
          IF (farbe4=farbe3) AND (figur4=figur3)
           AND ((figur4='D') OR (figur4='L')) THEN
            IF farbe4=weiss THEN
                agstellung4:=(f4s=f3s) AND (f4z=6)
              ELSE
                agstellung4:=(f4s=f3s) AND (f4z=3);
      END;  { ELSE IF }
  Ausgangsstellung:=agstellung3 AND agstellung4;
END;   { Ausgangsstellung }

PROCEDURE StellungsAusgabe(amzug:FARBEN;stell:STELLUNG);
{*
Beschreibung:
  Prozedur gibt die uebergebene Stellung aus.
Parameter:
  amzug = Die Farbe, die in der Stellung am Zug ist
  stell = Die Stellung, die ausgegeben werden soll
Benutzte globale Variablen:
  Keine.
Veraenderte globale Variablen:
  Keine.
Vorbedingungen:
  Keine.
*}
VAR
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
BEGIN
  StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  WRITE('  sK : ');
  WRITE(CHR(sks+96));
  WRITE(CHR(skz+48));
  WRITE(',  wK : ');
  WRITE(CHR(wks+96));
  WRITE(CHR(wkz+48));
  IF (farbe3=weiss) THEN
      WRITE(',  w')
    ELSE
      WRITE(',  s');
  WRITE(figur3,' : ');
  WRITE(CHR(f3s+96));
  WRITE(CHR(f3z+48));
  IF (farbe4=weiss) THEN
      WRITE(',  w')
    ELSE
      WRITE(',  s');
  WRITE(figur4,' : ');
  WRITE(CHR(f4s+96));
  WRITE(CHR(f4z+48));
  IF (amzug=weiss) THEN
      WRITELN(';  Weiss am Zug')
    ELSE
      WRITELN(';  Schwarz am Zug');
END;   { StellungsAusgabe }

PROCEDURE ErmittelAgStellungen;
{*
Beschreibung:
  Prozedur ermittelt fuer alle Stellungen ihre initiale Art, d.h.
  insbesondere, ob es sich bei ihnen um eine Ausgangsstellung handelt,
  und initialisiert ihren Stellungswert mit dem Wert 0.
Parameter:
  Keine
Benutzte globale Variablen:
  bvorhanden, abbrechen, figur3 (indirekt), figur4 (indirekt),
  farbe3 (indirekt), farbe4 (indirekt), fig34gleich (indirekt)
Veraenderte globale Variablen:
  legalanz, stellwerte (indirekt), stellarten (indirekt),
  agstellanz (indirekt), agstellungen (indirekt)
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
                       ELSE  IF Ausgangsstellung(schwarz,anfstell) THEN
                         BEGIN
                         SetzeStellungsArt(weiss,anfstell,ableitbar,fstanz);
                         legalanz[weiss]:=legalanz[weiss]+fstanz;
                         StellungsAusgabe(weiss,anfstell);
                         AugStellungHinzu(weiss,anfstell);
                         END
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
                       ELSE  IF Ausgangsstellung(schwarz,anfstell) THEN
                         BEGIN
                         SetzeStellungsArt(schwarz,anfstell,ableitbar,fstanz);
                         legalanz[schwarz]:=legalanz[schwarz]+fstanz;
                         StellungsAusgabe(schwarz,anfstell);
                         AugStellungHinzu(schwarz,anfstell);
                         END
                       ELSE
                         BEGIN
                         SetzeStellungsArt(schwarz,anfstell,nichtbew,fstanz);
                         legalanz[schwarz]:=legalanz[schwarz]+fstanz;
                         END;  { ELSE }
                     END;  { ELSE }
                 END;  { IF }
      END;  { WHILE }
    END;  { FOR }
END;   { ErmittelAgStellungen }

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
  stlart:ASTELLART;
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
  legalanz, brett (indirekt), stellarten (indirekt), astellanz (indirekt),
  astellungen (indirekt), abbrechen (indirekt)
Vorbedingungen:
  Die Stellung stell ist bezueglich initialer Legalitaet bewertet
*}
VAR
  neustell:STELLUNG;
  posliste:POSLIST;
  posanz,posnr:ZGBEREICH;
  stlart:ASTELLART;
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
  astellungen, bvorhanden, abbrechen, farbe3 (indirekt), figur3 (indirekt),
  farbe4 (indirekt), figur4 (indirekt), fig34gleich (indirekt)
Veraenderte globale Variablen:
  stindex, astellanz, legalanz (indirekt), brett (indirekt),
  stellarten (indirekt), astellungen (indirekt), abbrechen (indirekt)
Vorbedingungen:
  Die Stellungen sind bezueglich initialer Legalitaet bewertet
*}
VAR
  teststell:STELLUNG;
  stnr:ASTBEREICH;
  farbe:FARBEN;
  sks,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  skz:INTEGER;
BEGIN
  stindex:=0;
  astellanz[stindex,weiss]:=0;
  astellanz[stindex,schwarz]:=0;
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
  WHILE astellanz[stindex,weiss]+astellanz[stindex,schwarz]>0 DO
    BEGIN
    stindex:=1-stindex;
    astellanz[stindex,weiss]:=0;
    astellanz[stindex,schwarz]:=0;
    FOR farbe:=weiss TO schwarz DO
      FOR stnr:=1 TO astellanz[1-stindex,farbe] DO
        IF NOT(abbrechen) THEN
          StellIllegalTest(farbe,astellungen[1-stindex,farbe,stnr]);
    END;  { WHILE }
END;   { ErmittelIllegalStellungen }

PROCEDURE AugStellungenAufnehmen;
{*
Beschreibung:
  Prozedur nimmt die Ausgangsstellungen in die Liste der als ableitbar
  erkannten Stellungen auf und erhoeht deren Anzahl.
Parameter:
  Keine
Benutzte globale Variablen:
  abbrechen, agstellanz, agstellungen, bvorhanden (indirekt),
  figur3 (indirekt), figur4 (indirekt), stindex (indirekt),
  fig34gleich (indirekt)
Veraenderte globale Variablen:
  albstanz, stellarten (indirekt), stellwerte (indirekt),
  astellanz (indirekt), astellungen (indirekt), abbrechen (indirekt)
Vorbedingungen:
  Keine
*}
VAR
  stell:STELLUNG;
  stlart:ASTELLART;
  farbe:FARBEN;
  agstnr:AGSTBEREICH;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  fstanz:INTEGER;
BEGIN
  FOR farbe:=weiss TO schwarz DO
    FOR agstnr:=1 TO agstellanz[farbe] DO
      IF NOT(abbrechen) THEN
        BEGIN
        stell:=agstellungen[farbe,agstnr];
        StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
        stlart:=stellarten[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
        IF stlart=ableitbar THEN
          BEGIN
          SetzeStellungsArt(farbe,stell,stlart,fstanz);
          SetzeStellungsWert(farbe,stell,0);
          albstanz[0,farbe]:=albstanz[0,farbe]+fstanz;
          StellungHinzu(farbe,stell);
          END;  { IF }
        END;  { IF }
END;   { AugStellungenAufnehmen }

PROCEDURE ErmittelNachStellungen(amzug:FARBEN;stein:CHAR;hzanz:ASTWERT);
{*
Beschreibung:
  Prozedur ermittelt alle Nachfolgestellungen zu den im letzten Halbzug
  als ableitbar erkannten Stellungen und weist ihnen ihre Stellungsart
  zu.
Parameter:
    amzug : Die Farbe, fuer die die Nachfolgestellungen ermittelt werden
    stein : Der Stein, aus dessen moeglichen Nachfolgepositionen die
            Nachfolgestellungen erzeugt werden
    hzanz : Die Anzahl der Halbzuege bis zum Erreichen der
            Nachfolgestellungen, die ihnen als Stellungswert zugewiesen
            wird
Benutzte globale Variablen:
  astellanz, astellungen, stellarten, farbe3 (indirekt),
  farbe4 (indirekt), figur3 (indirekt), figur4 (indirekt),
  stindex (indirekt), fig34gleich (indirekt)
Veraenderte globale Variablen:
  albstanz, brett (indirekt), stellarten (indirekt),
  stellwerte (indirekt), astellanz (indirekt), astellungen (indirekt),
  abbrechen (indirekt)
Vorbedingungen:
  Die Stellungen sind bzgl. Legalitaet bewertet
*}
VAR
  posliste:POSLIST;
  ablstell,nachstell:STELLUNG;
  stnr:ASTBEREICH;
  posanz,posnr:ZGBEREICH;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  nachs,nachz:SZBEREICH;
  ablstlart,nstlart:ASTELLART;
  nfarbe:FARBEN;
  fstanz:INTEGER;
BEGIN
  IF (stein='w') AND (amzug=weiss) OR (stein='s') AND (amzug=schwarz)
   OR (stein='3') AND (amzug=farbe3) OR (stein='4') AND (amzug=farbe4) THEN
    BEGIN
    IF amzug=weiss THEN
        nfarbe:=schwarz
      ELSE
        nfarbe:=weiss;
    FOR stnr:=1 TO astellanz[1-stindex,amzug] DO
      IF NOT(abbrechen) THEN
        BEGIN
        ablstell:=astellungen[1-stindex,amzug,stnr];
        nachstell:=ablstell;
        StellToPwerte(ablstell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
        ablstlart:=stellarten[amzug,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
        ErzeugePoslist(amzug,stein,ablstell,FALSE,FALSE,posliste,posanz);
        FOR posnr:=1 TO posanz DO
          BEGIN
          nachs:=posliste[posnr].spalte;
          nachz:=posliste[posnr].zeile;
          IF stein='w' THEN
              BEGIN
              nachstell.wks:=nachs;
              nachstell.wkz:=nachz;
              nstlart:=stellarten[nfarbe,sks,skz,nachs,nachz,f3s,f3z,f4s,f4z];
              END
            ELSE  IF stein='s' THEN
              BEGIN
              nachstell.sks:=nachs;
              nachstell.skz:=nachz;
              nstlart:=stellarten[nfarbe,nachs,nachz,wks,wkz,f3s,f3z,f4s,f4z];
              END
            ELSE  IF stein='3' THEN
              BEGIN
              nachstell.f3s:=nachs;
              nachstell.f3z:=nachz;
              nstlart:=stellarten[nfarbe,sks,skz,wks,wkz,nachs,nachz,f4s,f4z];
              END
            ELSE
              BEGIN
              nachstell.f4s:=nachs;
              nachstell.f4z:=nachz;
              nstlart:=stellarten[nfarbe,sks,skz,wks,wkz,f3s,f3z,nachs,nachz];
              END;  { ELSE }
          IF nstlart=nichtbew THEN
            BEGIN
            SetzeStellungsArt(nfarbe,nachstell,ableitbar,fstanz);
            albstanz[hzanz,nfarbe]:=albstanz[hzanz,nfarbe]+fstanz;
            SetzeStellungsWert(nfarbe,nachstell,hzanz);
            StellungHinzu(nfarbe,nachstell);
            END;  { IF }
          END;  { FOR }
        END;  { IF }
    END;  { IF }
END;   { ErmittelNachStellungen }

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
      WRITELN('Achtung : Untersuchung ohne "en passant"-Regel');
      END;  { IF }
END;   { EpKorrektur }

PROCEDURE ErmittelIsoliertStellungen;
{*
Beschreibung:
  Prozedur weist, nachdem alle ableitbaren Stellungen ermittelt wurden,
  allen als isoliert, d.h. als nicht ableitbar erkannten Stellungen ihre
  Stellungsart zu.
Parameter:
  Keine
Benutzte globale Variablen:
  bvorhanden, abbrechen, stellarten, figur3 (indirekt), figur4 (indirekt),
  fig34gleich (indirekt)
Veraenderte globale Variablen:
  isoliertanz, stellarten (indirekt)
Vorbedingungen:
  Alle Stellungen wurden bezueglich Legalitaet und Gewinnbarkeit bewertet
*}
VAR
  stell:STELLUNG;
  stlart:ASTELLART;
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
                     BEGIN
                     PwerteToStell(sks,skz,wks,wkz,f3s,f3z,f4s,f4z,stell);
                     SetzeStellungsArt(farbe,stell,isoliert,fstanz);
                     isoliertanz[farbe]:=isoliertanz[farbe]+fstanz;
                     END;  { IF }
                   END;  { IF }
      END;  { WHILE }
    END;  { FOR }
END;   { ErmittelIsoliertStellungen }

PROCEDURE AblStellAnzAusgabe;
{*
Beschreibung:
  Prozedur gibt fuer jede Anzahl von Halbzuegen die Anzahl der damit
  ableitbaren Stellungen aus, jeweils getrennt danach, welche Farbe am
  Zug ist. Ausserdem ermittelt sie in Abhaengigkeit von der Farbe, die
  am Zug ist, die Gesamtanzahl der ableitbaren Stellungen.
Parameter:
  Keine
Benutzte globale Variablen:
  albstanz
Veraenderte globale Variablen:
  ableitanz
Vorbedingungen:
  Keine
*}
VAR
  hznr:ASTWERT;
BEGIN
  WRITELN;
  WRITELN;
  WRITELN('Anzahl ableitbarer Stellungen :');
  WRITELN;
  ableitanz[weiss]:=0;
  ableitanz[schwarz]:=0;
  IF (albstanz[0,weiss]+albstanz[0,schwarz]=0)
   AND (albstanz[1,weiss]+albstanz[1,schwarz]=0) THEN
      WRITELN('Keine ableitbare Stellung')
    ELSE
      IF albstanz[0,weiss]+albstanz[0,schwarz]>0 THEN
        BEGIN
        WRITE('Ableit. in  0 Hz. :');
        IF albstanz[0,weiss]>0 THEN
          BEGIN
          WRITE('  mit Weiss am Zug   : ',albstanz[0,weiss]:7);
          ableitanz[weiss]:=ableitanz[weiss]+albstanz[0,weiss];
          END;  { IF }
        IF albstanz[0,schwarz]>0 THEN
          BEGIN
          WRITE('  mit Schwarz am Zug : ',albstanz[0,schwarz]:7);
          ableitanz[schwarz]:=ableitanz[schwarz]+albstanz[0,schwarz];
          END;  { IF }
        WRITELN;
        END;  { IF }
  hznr:=0;
  WHILE albstanz[hznr+1,weiss]+albstanz[hznr+1,schwarz]>0 DO
    BEGIN
    hznr:=hznr+1;
    WRITE('Ableit. in ',hznr:2,' Hz. :');
    IF albstanz[hznr,weiss]+albstanz[hznr,schwarz]=0 THEN
        WRITELN('  keine ableitbare Stellung')
      ELSE
        BEGIN
        IF albstanz[hznr,weiss]>0 THEN
          BEGIN
          WRITE('  mit Weiss am Zug   : ',albstanz[hznr,weiss]:7);
          ableitanz[weiss]:=ableitanz[weiss]+albstanz[hznr,weiss];
          END;  { IF }
        IF albstanz[hznr,schwarz]>0 THEN
          BEGIN
          WRITE('  mit Schwarz am Zug : ',albstanz[hznr,schwarz]:7);
          ableitanz[schwarz]:=ableitanz[schwarz]+albstanz[hznr,schwarz];
          END;  { IF }
        WRITELN;
        END;  { ELSE }
    END;  { WHILE }
END;   { AblStellAnzAusgabe }

PROCEDURE AGesStellAnzAusgabe;
{*
Beschreibung:
  Prozedur gibt fuer beide Farben am Zug die Gesamtanzahl der Stellungen
  jeder Stellungsart aus.
Parameter:
  Keine
Benutzte globale Variablen:
  legalanz, ableitanz, isoliertanz
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
  WRITELN('      Legal       : ',legalanz[weiss]:8);
  WRITELN('      Ableitbar   : ',ableitanz[weiss]:8);
  WRITELN('      Isoliert    : ',isoliertanz[weiss]:8);
  WRITELN;
  WRITELN('   mit Schwarz am Zug :');
  WRITELN('      Legal       : ',legalanz[schwarz]:8);
  WRITELN('      Ableitbar   : ',ableitanz[schwarz]:8);
  WRITELN('      Isoliert    : ',isoliertanz[schwarz]:8);
END;   { AGesStellAnzAusgabe }

PROCEDURE Speichern;
{*
Beschreibung:
  Prozedur speichert die Daten des untersuchten Endspiels als Datei.
  Dabei wird am Anfang eine Konstante TRUE gespeichert, um eine
  syntaktisch korrekte Verarbeitung durch dass Programm zum Fuehren
  der Endspiele zu ermoeglichen.
Parameter:
  Keine
Benutzte globale Variablen:
  farbe3, figur3, farbe4, figur4, vorstellpruefen, bvorhanden,
  stellarten, stellwerte
Veraenderte globale Variablen:
  Keine
Vorbedingungen:
  Das Endspiel wurde vollstaendig analysiert, d.h. alle Stellungen wurden
  vollstaendig bewertet
*}
VAR
  stlart:ASTELLART;
  stlwert:ASTWERT;
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
  WRITE(anlydaten,ORD(TRUE));
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
  WRITE('Untersuchung der Ableitbarkeit von Stellungen');
  WRITELN(' in 4-Steine-Schachendspielen');
  InitDaten;
  FigurenEinlesen;
  WRITELN;
  REPEAT
    WRITELN;
    WRITE('(k)orrekte oder (v)ollstaendige Definition');
    WRITE(' der Legalitaet (k/v) : ');
    READLN(chin);
  UNTIL (chin='k') OR (chin='v');
  vorstellpruefen:=(chin='k');
  IF NOT(abbrechen) THEN
    BEGIN
    WRITELN;
    WRITELN;
    WRITELN('Ausgangsstellungen (Repraesentanten):');
    WRITELN;
    ErmittelAgStellungen;
    END;  { IF }
  IF NOT(abbrechen) AND vorstellpruefen THEN
    ErmittelIllegalStellungen;
  stindex:=0;
  astellanz[stindex,weiss]:=0;
  astellanz[stindex,schwarz]:=0;
  IF NOT(abbrechen) THEN
    AugStellungenAufnehmen;
  hzanz:=0;
  WHILE (astellanz[stindex,weiss]+astellanz[stindex,schwarz]>0)
   AND NOT(abbrechen) DO
    BEGIN
    stindex:=1-stindex;
    astellanz[stindex,weiss]:=0;
    astellanz[stindex,schwarz]:=0;
    IF hzanz<maxahzuganz THEN
        hzanz:=hzanz+1
      ELSE
        BEGIN
        WRITELN;
        WRITELN;
        WRITELN('Konstante  maxahzuganz  zu klein');
        abbrechen:=TRUE;
        END;  { ELSE }
    FOR farbe:=weiss TO schwarz DO
      BEGIN
      IF NOT(abbrechen) THEN
        ErmittelNachStellungen(farbe,'3',hzanz);
      IF NOT(abbrechen) THEN
        ErmittelNachStellungen(farbe,'4',hzanz);
      IF NOT(abbrechen) THEN
        ErmittelNachStellungen(farbe,'s',hzanz);
      IF NOT(abbrechen) THEN
        ErmittelNachStellungen(farbe,'w',hzanz);
      END;  { FOR }
    END;  { WHILE }
  IF NOT(abbrechen) THEN
    EpKorrektur;
  IF NOT(abbrechen) THEN
    ErmittelIsoliertStellungen;
  IF NOT(abbrechen) THEN
    AblStellAnzAusgabe;
  IF abbrechen THEN
      BEGIN
      WRITELN;
      WRITELN;
      WRITELN('Analyse abgebrochen');
      END
    ELSE
      BEGIN
      AGesStellAnzAusgabe;
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

