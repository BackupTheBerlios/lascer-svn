{*
   Dateiname      : espfuehrung4.pp
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


{ Programm zum Fuehren eines analysierten 4-Steine-Endspiels. }

{ Angepasst auf einen Bildschirm von 80*24 mit reiner Textausgabe. }



PROGRAM EndspielFuehrung4(INPUT,OUTPUT);

CONST
  maxhzuganz = 90;
  maxzuegeanz = 60;
  maxagzuganz = 100;

TYPE
  SZBEREICH = 1..8;
  HZBEREICH = 0..maxhzuganz;
  ZGBEREICH = 0..maxzuegeanz;
  AZBEREICH = 0..maxagzuganz;
  STWERT = 0..maxhzuganz;
  DATBEREICH = 0..122;   {* 0..max(maxhzuganz,122) *}
  FARBEN = (weiss,schwarz);
  STELLART = (illegal,nachesp,nichtbew,matt,patt,tremis,wgewinn,sgewinn,remis);
  STELLUNGSWERTE = ARRAY[1..8,1..8,1..8,1..8,1..8,1..8,1..8,1..8] OF STWERT;
  STELLUNGSARTEN = ARRAY[1..8,1..8,1..8,1..8,1..8,1..8,1..8,1..8] OF STELLART;
  POSITION = RECORD
               spalte,zeile : SZBEREICH;
             END;
  POSLIST = ARRAY[1..maxzuegeanz] OF POSITION;
  ZUG = RECORD
          zugnr : LONGINT;
          stein : CHAR;
          altpos,neupos : POSITION;
          f3geschlagen,f4geschlagen,enpassant,umgewandelt : BOOLEAN;
        END;
  SPOSITION = PACKED ARRAY[1..2] OF CHAR;
  STELLUNG = RECORD
               sks,skz,wks,wkz,f3s,f3z,f4s,f4z : SZBEREICH;
             END;
  SPSTELLUNGSANZ = ARRAY[weiss..schwarz] OF LONGINT;

VAR
  brett : ARRAY[-1..10,-1..10] OF BOOLEAN;   { global }
  stellwerte : ARRAY[weiss..schwarz] OF STELLUNGSWERTE;   { global }
  stellarten : ARRAY[weiss..schwarz] OF STELLUNGSARTEN;   { global }
  ausgefzuege : ARRAY[1..maxagzuganz] OF ZUG;   { global }
  legalanz,mattanz,pattanz,tremisanz,remisanz : SPSTELLUNGSANZ;   { global }
  wgewinanz,sgewinanz : SPSTELLUNGSANZ;   { global }
  aktstell : STELLUNG;   { global }
  agzuganz,lagzuganz : AZBEREICH;   { global }
  wgrhzanz,sgrhzanz : STWERT;   { global }
  farbe3,farbe4 : FARBEN;   { global }
  amzug : FARBEN;   { global }
  zufallswert : LONGINT;   { global }
  abbrechen : BOOLEAN;   { global }
  vorstellpruefen : BOOLEAN;   { global }
  schnellstmatt : BOOLEAN;   { global }
  fig34gleich : BOOLEAN;   { global }
  stvorhanden,endstellung,vorgegeben,eingegeben : BOOLEAN;
  figur3,figur4 : CHAR;   { global }
  chin,ch : CHAR;


FUNCTION RandomReal:REAL;
BEGIN
  zufallswert:=(3*zufallswert+7227) MOD 2345;
  RandomReal:=zufallswert/2345;
END;   { RandomReal }

FUNCTION Min(wert1,wert2:LONGINT):LONGINT;
BEGIN
  IF wert1<=wert2 THEN
      Min:=wert1
    ELSE
      Min:=wert2;
END;   { Min }

FUNCTION Max(wert1,wert2:LONGINT):LONGINT;
BEGIN
  IF wert1>=wert2 THEN
      Max:=wert1
    ELSE
      Max:=wert2;
END;   { Max }

FUNCTION StellartTyp(stlartwert:INTEGER):STELLART;
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
VAR
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
    legalanz[farbe]:=0;
    mattanz[farbe]:=0;
    pattanz[farbe]:=0;
    tremisanz[farbe]:=0;
    remisanz[farbe]:=0;
    END;  { FOR }
  agzuganz:=0;
  lagzuganz:=0;
  wgrhzanz:=0;
  sgrhzanz:=0;
  abbrechen:=FALSE;
END;   { InitDaten }

PROCEDURE StellToPwerte(stell:STELLUNG;
                        VAR sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH);
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

FUNCTION BauerGespiegelt(zugfarbe:FARBEN;stell:STELLUNG):BOOLEAN;
VAR
  f3s,f3z,f4s,f4z:SZBEREICH;
  spdif:INTEGER;
BEGIN
  f3s:=stell.f3s;
  f3z:=stell.f3z;
  f4s:=stell.f4s;
  f4z:=stell.f4z;
  IF (figur3>='a') AND (figur3<>'x') THEN
      BEGIN
      spdif:=ABS(f3s-1-(ORD(figur3)-ORD('a')));
      IF (spdif=0)
       OR (spdif=1) AND (zugfarbe<>farbe3) AND (f3s=f4s) AND (f3z=f4z) THEN
          BauerGespiegelt:=FALSE
        ELSE
          BauerGespiegelt:=TRUE;
      END
    ELSE
      BEGIN
      spdif:=ABS(f4s-1-(ORD(figur4)-ORD('a')));
      IF (spdif=0)
       OR (spdif=1) AND (zugfarbe<>farbe4) AND (f4s=f3s) AND (f4z=f3z) THEN
          BauerGespiegelt:=FALSE
        ELSE
          BauerGespiegelt:=TRUE;
      END;  { ELSE }
END;   { BauerGespiegelt }

PROCEDURE SpiegeleStellArt(zugfarbe:FARBEN;stell:STELLUNG;art:STELLART;
                           VAR stlarten:STELLUNGSARTEN);
VAR
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
BEGIN
  StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  IF (figur3>='a') AND (figur3<>'x') OR (figur4>='a') AND (figur4<>'x') THEN
      IF BauerGespiegelt(zugfarbe,stell) THEN
          BEGIN
          stlarten[sks,skz,wks,wkz,f3s,f3z,f4s,f4z]:=illegal;
          stlarten[9-sks,skz,9-wks,wkz,9-f3s,f3z,9-f4s,f4z]:=art;
          END
        ELSE
          BEGIN
          stlarten[sks,skz,wks,wkz,f3s,f3z,f4s,f4z]:=art;
          stlarten[9-sks,skz,9-wks,wkz,9-f3s,f3z,9-f4s,f4z]:=illegal;
          END
    ELSE
      BEGIN
      stlarten[sks,skz,wks,wkz,f3s,f3z,f4s,f4z]:=art;
      stlarten[9-sks,skz,9-wks,wkz,9-f3s,f3z,9-f4s,f4z]:=art;
      IF (figur3<'a') AND (figur4<'a') THEN
        BEGIN
        stlarten[sks,9-skz,wks,9-wkz,f3s,9-f3z,f4s,9-f4z]:=art;
        stlarten[9-sks,9-skz,9-wks,9-wkz,9-f3s,9-f3z,9-f4s,9-f4z]:=art;
        stlarten[skz,sks,wkz,wks,f3z,f3s,f4z,f4s]:=art;
        stlarten[9-skz,sks,9-wkz,wks,9-f3z,f3s,9-f4z,f4s]:=art;
        stlarten[skz,9-sks,wkz,9-wks,f3z,9-f3s,f4z,9-f4s]:=art;
        stlarten[9-skz,9-sks,9-wkz,9-wks,9-f3z,9-f3s,9-f4z,9-f4s]:=art;
        END;  { IF }
      END;  { ELSE }
END;   { SpiegeleStellArt }

PROCEDURE SpiegeleStellWert(zugfarbe:FARBEN;stell:STELLUNG;wert:STWERT;
                            VAR stlwerte:STELLUNGSWERTE);
VAR
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
BEGIN
  StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  IF (figur3>='a') AND (figur3<>'x') OR (figur4>='a') AND (figur4<>'x') THEN
      IF BauerGespiegelt(zugfarbe,stell) THEN
          BEGIN
          stlwerte[sks,skz,wks,wkz,f3s,f3z,f4s,f4z]:=0;
          stlwerte[9-sks,skz,9-wks,wkz,9-f3s,f3z,9-f4s,f4z]:=wert;
          END
        ELSE
          BEGIN
          stlwerte[sks,skz,wks,wkz,f3s,f3z,f4s,f4z]:=wert;
          stlwerte[9-sks,skz,9-wks,wkz,9-f3s,f3z,9-f4s,f4z]:=0;
          END
    ELSE
      BEGIN
      stlwerte[sks,skz,wks,wkz,f3s,f3z,f4s,f4z]:=wert;
      stlwerte[9-sks,skz,9-wks,wkz,9-f3s,f3z,9-f4s,f4z]:=wert;
      IF (figur3<'a') AND (figur4<'a') THEN
        BEGIN
        stlwerte[sks,9-skz,wks,9-wkz,f3s,9-f3z,f4s,9-f4z]:=wert;
        stlwerte[9-sks,9-skz,9-wks,9-wkz,9-f3s,9-f3z,9-f4s,9-f4z]:=wert;
        stlwerte[skz,sks,wkz,wks,f3z,f3s,f4z,f4s]:=wert;
        stlwerte[9-skz,sks,9-wkz,wks,9-f3z,f3s,9-f4z,f4s]:=wert;
        stlwerte[skz,9-sks,wkz,9-wks,f3z,9-f3s,f4z,9-f4s]:=wert;
        stlwerte[9-skz,9-sks,9-wkz,9-wks,9-f3z,9-f3s,9-f4z,9-f4s]:=wert;
        END;  { IF }
      END;  { ELSE }
END;   { SpiegeleStellWert }

PROCEDURE Laden;
VAR
  stell:STELLUNG;
  stlart:STELLART;
  farbe:FARBEN;
  sks,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  skz:INTEGER;
  figanz,farbcode,figcode,vspcode,smcode:DATBEREICH;
  stlcode,stlwert:DATBEREICH;
  datname:STRING[31];
  anlydaten:FILE OF DATBEREICH;
BEGIN
  WRITELN;
  WRITELN;
  WRITELN('Laden der Daten einer Endspielanalyse fuer 4 Steine');
  WRITELN;
  WRITE('  Name fuer Datei = ');
  READLN(datname);
  ASSIGN(anlydaten,datname);
  RESET(anlydaten);
  READ(anlydaten,figanz);
  IF figanz<>4 THEN
      BEGIN
      WRITELN;
      WRITELN('  Daten von falschem Endspiel mit ',figanz:1,' Steinen.');
      abbrechen:=TRUE;
      END
    ELSE
      BEGIN
      READ(anlydaten,farbcode);
      IF farbcode=ORD(weiss) THEN
          farbe3:=weiss
        ELSE
          farbe3:=schwarz;
      READ(anlydaten,figcode);
      figur3:=CHR(figcode);
      READ(anlydaten,farbcode);
      IF farbcode=ORD(weiss) THEN
          farbe4:=weiss
        ELSE
          farbe4:=schwarz;
      READ(anlydaten,figcode);
      figur4:=CHR(figcode);
      fig34gleich:=(figur3=figur4) AND (farbe3=farbe4);
      END;  { ELSE }
  IF NOT(abbrechen) THEN
    BEGIN
    READ(anlydaten,vspcode);
    IF vspcode=ORD(TRUE) THEN
        vorstellpruefen:=TRUE
      ELSE
        vorstellpruefen:=FALSE;
    READ(anlydaten,smcode);
    IF smcode=ORD(TRUE) THEN
        schnellstmatt:=TRUE
      ELSE
        schnellstmatt:=FALSE;
    FOR sks:=1 TO 4 DO
      BEGIN
      skz:=0;
      WHILE (skz<sks) OR ((figur3>='a') OR (figur4>='a')) AND (skz<8) DO
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
                 IF NOT(abbrechen) THEN
                   BEGIN
                   PwerteToStell(sks,skz,wks,wkz,f3s,f3z,f4s,f4z,stell);
                   READ(anlydaten,stlcode);
                   READ(anlydaten,stlwert);
                   IF stlwert>maxhzuganz THEN
                     BEGIN
                     WRITELN;
                     WRITELN;
                     WRITELN('Konstante  maxhzuganz  zu klein');
                     abbrechen:=TRUE;
                     END;  { IF }
                   END;  { IF }
                 IF NOT(abbrechen) THEN
                   BEGIN
                   stlart:=StellartTyp(stlcode);
                   SpiegeleStellArt(farbe,stell,stlart,stellarten[farbe]);
                   SpiegeleStellWert(farbe,stell,stlwert,stellwerte[farbe]);
                   END;  { IF }
                 END;  { FOR }
        END;  { WHILE }
      END;  { FOR }
    END;  { IF }
  WRITELN;
  IF abbrechen THEN
      WRITELN('  Daten nicht geladen')
    ELSE
      WRITELN('  Daten geladen');
END;   { Laden }

FUNCTION VtRepraesentant(f3s,f3z,f4s,f4z:SZBEREICH):BOOLEAN;
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

PROCEDURE ErmittelStatistik;
VAR
  stlart:STELLART;
  stlwert:STWERT;
  farbe:FARBEN;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
BEGIN
  FOR sks:=1 TO 8 DO
   FOR skz:=1 TO 8 DO
    FOR wks:=1 TO 8 DO
     FOR wkz:=1 TO 8 DO
      FOR f3s:=1 TO 8 DO
       FOR f3z:=1 TO 8 DO
        FOR f4s:=1 TO 8 DO
         FOR f4z:=1 TO 8 DO
           IF VtRepraesentant(f3s,f3z,f4s,f4z) THEN
             FOR farbe:=weiss TO schwarz DO
               BEGIN
               stlart:=stellarten[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
               IF (stlart<>illegal) AND (stlart<>nachesp) THEN
                 BEGIN
                 legalanz[farbe]:=legalanz[farbe]+1;
                 CASE stlart OF
                   matt : mattanz[farbe]:=mattanz[farbe]+1;
                   patt : pattanz[farbe]:=pattanz[farbe]+1;
                   tremis : tremisanz[farbe]:=tremisanz[farbe]+1;
                   wgewinn : wgewinanz[farbe]:=wgewinanz[farbe]+1;
                   sgewinn : sgewinanz[farbe]:=sgewinanz[farbe]+1;
                   remis : remisanz[farbe]:=remisanz[farbe]+1;
                 END;  { CASE }
                 stlwert:=stellwerte[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
                 IF stlart=wgewinn THEN
                   IF stlwert>wgrhzanz THEN
                     wgrhzanz:=stlwert;
                 IF stlart=sgewinn THEN
                   IF stlwert>sgrhzanz THEN
                     sgrhzanz:=stlwert;
                 END;  { IF }
               END;  { FOR }
END;   { ErmittelStatistik }

PROCEDURE StatistikAusgeben;
BEGIN
  WRITELN;
  WRITELN;
  WRITE('Endspiel mit ');
  IF farbe3=weiss THEN
      WRITE('weisse')
    ELSE
      WRITE('schwarze');
  IF figur3>='a' THEN
      WRITE('m ',figur3,'-Bauer')
    ELSE
      CASE figur3 OF
        'D' : WRITE('r Dame');
        'T' : WRITE('m Turm');
        'L' : WRITE('m Laeufer');
        'S' : WRITE('m Springer');
      END;  { CASE }
  WRITE(' und ');
  IF farbe4=weiss THEN
      WRITE('weisse')
    ELSE
      WRITE('schwarze');
  IF figur4>='a' THEN
      WRITE('m ',figur4,'-Bauer.')
    ELSE
      CASE figur4 OF
        'D' : WRITE('r Dame.');
        'T' : WRITE('m Turm.');
        'L' : WRITE('m Laeufer.');
        'S' : WRITE('m Springer.');
      END;  { CASE }
  IF vorstellpruefen THEN
      WRITE(' Korrekte Def.,')
    ELSE
      WRITE(' Vollstaendige Def.,');
  WRITELN;
  IF schnellstmatt THEN
      WRITE('schnellstes Matt,')
    ELSE
      WRITE('schnellster Gewinn,');
  IF wgewinanz[weiss]+wgewinanz[schwarz]>0 THEN
    WRITE(' in max. ',wgrhzanz:2,' Hz. fuer Weiss');
  IF (wgewinanz[weiss]+wgewinanz[schwarz]>0)
   AND (sgewinanz[weiss]+sgewinanz[schwarz]>0) THEN
    WRITE(' und');
  IF sgewinanz[weiss]+sgewinanz[schwarz]>0 THEN
    WRITE(' in max. ',sgrhzanz:2,' Hz. fuer Schwarz');
  WRITELN;
  WRITELN;
  WRITELN('Die moeglichen Stellungen teilen sich folgendermassen auf :');
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
END;   { StatistikAusgeben }

PROCEDURE PosToSpos(posin:POSITION;VAR sposout:SPOSITION);
BEGIN
  sposout[1]:=CHR(posin.spalte+96);
  sposout[2]:=CHR(posin.zeile+48);
END;   { PosToSpos }

PROCEDURE SposToPos(sposin:SPOSITION;VAR posout:POSITION);
BEGIN
  IF sposin[1]>='a' THEN
      posout.spalte:=ORD(sposin[1])-96
    ELSE
      posout.spalte:=ORD(sposin[1])-64;
  posout.zeile:=ORD(sposin[2])-48;
END;   { SposToPos }

PROCEDURE EpStellungsBewertung(stell:STELLUNG;bstein:CHAR;bdoppelzug:BOOLEAN;
                               VAR epstlart:STELLART;VAR epstlwert:STWERT);
{ Bedeutung: Ermittelt Art und Wert einer Stellung, bei der der naechste Zug
             ein "en passant"-Schlagen ist. }
VAR
  stlart:STELLART;
  stlwert:STWERT;
  farbe:FARBEN;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  sspalte,szeile:SZBEREICH;
BEGIN
  epstlart:=illegal;
  epstlwert:=0;
  IF bdoppelzug AND (figur3>='a') AND (figur4>='a') AND (farbe3<>farbe4)
   AND (stell.f3z=stell.f4z) AND (ABS(stell.f3s-stell.f4s)=1) THEN
    BEGIN
    StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
    IF bstein='3' THEN
        BEGIN
        farbe:=farbe3;
        sspalte:=f3s;
        IF f3z=4 THEN
            szeile:=3
          ELSE
            szeile:=6;
        END
      ELSE
        BEGIN
        farbe:=farbe4;
        sspalte:=f4s;
        IF f4z=4 THEN
            szeile:=3
          ELSE
            szeile:=6;
        END;  { ELSE }
    stlart:=stellarten[farbe,sks,skz,wks,wkz,sspalte,szeile,sspalte,szeile];
    stlwert:=stellwerte[farbe,sks,skz,wks,wkz,sspalte,szeile,sspalte,szeile];
    IF (stlart=illegal) OR (stlart=nachesp) THEN
        BEGIN
        epstlart:=illegal;
        epstlwert:=0;
        END
      ELSE  IF (stlart=patt) OR (stlart=tremis) OR (stlart=remis) THEN
        BEGIN
        epstlart:=remis;
        epstlwert:=0;
        END
      ELSE  IF stlart=matt THEN
        BEGIN
        IF farbe=weiss THEN
            epstlart:=sgewinn
          ELSE
            epstlart:=wgewinn;
        epstlwert:=1;
        END
      ELSE
        BEGIN
        epstlart:=stlart;
        epstlwert:=stlwert+1;
        END;  { ELSE }
    END;  { IF }
END;   { EpStellungsBewertung }

PROCEDURE StellungsBewertung(stell:STELLUNG;lzstein:CHAR;bdoppelzug:BOOLEAN;
                             VAR stlart:STELLART;VAR stlwert:STWERT);
{ Bedeutung: Ermittelt Art und Wert einer Stellung, unter Einbeziehung der
             Moeglichkeit, dass der naechste Zug ein "en passent"-Schlagen
             ist.
  Parameter: lzstein: letzter gezogener Stein }
VAR
  epstlart,ustlart:STELLART;
  epstlwert,ustlwert:STWERT;
  farbe:FARBEN;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
BEGIN
  EpStellungsBewertung(stell,lzstein,bdoppelzug,epstlart,epstlwert);
  IF (lzstein='w') OR (lzstein='3') AND (farbe3=weiss)
   OR (lzstein='4') AND (farbe4=weiss) THEN
      farbe:=schwarz
    ELSE
      farbe:=weiss;
  StellToPwerte(stell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  ustlart:=stellarten[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
  ustlwert:=stellwerte[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
  IF (epstlart=illegal) OR (ustlart=illegal) THEN
      BEGIN
      stlart:=ustlart;
      stlwert:=ustlwert;
      END
    ELSE  IF (farbe=weiss) AND (ustlart=wgewinn)
     OR (farbe=schwarz) AND (ustlart=sgewinn) THEN
      IF epstlart<>ustlart THEN
          BEGIN
          stlart:=ustlart;
          stlwert:=ustlwert;
          END
        ELSE
          IF ustlwert<=epstlwert THEN
              BEGIN
              stlart:=ustlart;
              stlwert:=ustlwert;
              END
            ELSE
              BEGIN
              stlart:=epstlart;
              stlwert:=epstlwert;
              END
    ELSE  IF (ustlart=remis) AND (farbe=weiss) AND (epstlart=sgewinn)
     OR (ustlart=remis) AND (farbe=schwarz) AND (epstlart=wgewinn) THEN
      BEGIN
      stlart:=ustlart;
      stlwert:=ustlwert;
      END
    ELSE
      BEGIN
      stlart:=epstlart;
      stlwert:=epstlwert;
      END;  { ELSE }
END;   { StellungsBewertung }

PROCEDURE KurzZugAusgabe(ausgabzug:ZUG);
VAR
  altspos,neuspos:SPOSITION;
BEGIN
  IF (ausgabzug.stein='w') OR (ausgabzug.stein='s') THEN
      WRITE('K')
    ELSE  IF (ausgabzug.stein='3') AND (figur3<'a') THEN
      WRITE(figur3)
    ELSE  IF (ausgabzug.stein='4') AND (figur4<'a') THEN
      WRITE(figur4)
    ELSE
      WRITE(' ');
  PosToSpos(ausgabzug.altpos,altspos);
  PosToSpos(ausgabzug.neupos,neuspos);
  IF ausgabzug.f3geschlagen OR ausgabzug.f4geschlagen THEN
      WRITE(altspos,'x',neuspos)
    ELSE
      WRITE(altspos,'-',neuspos);
  IF ausgabzug.enpassant THEN
      WRITE(' ep')
    ELSE  IF ausgabzug.umgewandelt THEN
      WRITE(' D ')
    ELSE
      WRITE('   ');
END;   { KurzZugAusgabe }

PROCEDURE ZugDarstellen(VAR abschnitt:INTEGER;VAR agzugnr,lagzugnr:AZBEREICH);
BEGIN
  IF (abschnitt=1) AND (agzuganz>0)
   OR (abschnitt=5) AND (agzuganz=0) AND (lagzuganz>0) THEN
      BEGIN
      WRITELN('           Weiss :    Schwarz :');
      abschnitt:=abschnitt+1;
      END
    ELSE  IF (abschnitt=5) AND (lagzuganz>agzuganz) AND (agzuganz>0) THEN
      BEGIN
      WRITELN('       zurueckgenommene Zuege :');
      abschnitt:=6;
      END
    ELSE  IF (abschnitt>=3) AND (abschnitt<=4) AND (agzugnr<agzuganz) THEN
      BEGIN
      agzugnr:=agzugnr+1;
      WRITE('      ');
      WRITE(ausgefzuege[agzugnr].zugnr:2,'.  ');
      IF (abschnitt=3) AND (amzug=weiss) AND NOT(ODD(agzuganz-agzugnr))
       OR (abschnitt=3) AND (amzug=schwarz) AND ODD(agzuganz-agzugnr) THEN
          BEGIN
          WRITE('           ');
          KurzZugAusgabe(ausgefzuege[agzugnr]);
          WRITELN;
          END
        ELSE
          BEGIN
          KurzZugAusgabe(ausgefzuege[agzugnr]);
          IF agzugnr=agzuganz THEN
              WRITELN
            ELSE
              BEGIN
              agzugnr:=agzugnr+1;
              WRITE('  ');
              KurzZugAusgabe(ausgefzuege[agzugnr]);
              WRITELN;
              END;  { ELSE }
          END;  { ELSE }
      IF abschnitt=3 THEN
        abschnitt:=4;
      END
    ELSE  IF (abschnitt>=7) AND (abschnitt<=8) AND (lagzugnr<lagzuganz) THEN
      BEGIN
      lagzugnr:=lagzugnr+1;
      WRITE('      ');
      WRITE(ausgefzuege[lagzugnr].zugnr:2,'.  ');
      IF (abschnitt=7) AND (amzug=weiss) AND NOT(ODD(lagzugnr-agzuganz))
       OR (abschnitt=7) AND (amzug=schwarz) AND ODD(lagzugnr-agzuganz) THEN
          BEGIN
          WRITE('           ');
          KurzZugAusgabe(ausgefzuege[lagzugnr]);
          WRITELN;
          END
        ELSE
          BEGIN
          KurzZugAusgabe(ausgefzuege[lagzugnr]);
          IF lagzugnr=lagzuganz THEN
              WRITELN
            ELSE
              BEGIN
              lagzugnr:=lagzugnr+1;
              WRITE('  ');
              KurzZugAusgabe(ausgefzuege[lagzugnr]);
              WRITELN;
              END;  { ELSE }
          END;  { ELSE }
      IF abschnitt=7 THEN
        abschnitt:=8;
      END
    ELSE
      BEGIN
      WRITELN;
      abschnitt:=abschnitt+1;
      END;  { ELSE }
END;   { ZugDarstellen }

PROCEDURE StellungDarstellen(stell:STELLUNG;zfarbe:FARBEN;
                             neudarstell:BOOLEAN);
VAR
  letztzug:ZUG;
  stlart:STELLART;
  stlwert:STWERT;
  zeile,spalte:SZBEREICH;
  agzugnr,lagzugnr,zdif:AZBEREICH;
  abschnitt:INTEGER;
  bdpzug:BOOLEAN;
  zstein:CHAR;
  farb3,farb4:CHAR;
BEGIN
  WRITELN;
  WRITE('      ');
  FOR spalte:=1 TO 8 DO
    WRITE('  ',CHR(96+spalte):1,'  ');
  WRITELN;
  WRITE('     -');
  FOR spalte:=1 TO 8 DO
    WRITE('-----');
  IF neudarstell THEN
      WRITELN
    ELSE
      IF agzuganz>0 THEN
          BEGIN
          WRITELN('         ausgefuehrte Zuege :');
          abschnitt:=0;
          END
        ELSE  IF lagzuganz>0 THEN
          BEGIN
          WRITELN('       zurueckgenommene Zuege :');
          abschnitt:=4;
          END
        ELSE
          WRITELN;
  IF neudarstell THEN
        agzugnr:=0
      ELSE  IF lagzuganz=agzuganz THEN
        IF amzug=weiss THEN
            agzugnr:=Max(0,agzuganz-26)
          ELSE
            agzugnr:=Max(0,agzuganz-25)
      ELSE
        BEGIN
        zdif:=lagzuganz-agzuganz;
        IF (amzug=weiss) AND NOT(ODD(zdif)) THEN
            agzugnr:=Max(0,Min(agzuganz-10,agzuganz-2*(10-(zdif DIV 2))))
          ELSE  IF (amzug=schwarz) AND NOT(ODD(zdif)) THEN
            agzugnr:=Max(0,Min(agzuganz-9,agzuganz-2*(9-(zdif DIV 2))+1))
          ELSE  IF (amzug=weiss) AND ODD(zdif) THEN
            agzugnr:=Max(0,Min(agzuganz-10,agzuganz-2*(10-((zdif+1) DIV 2))))
          ELSE  IF (amzug=schwarz) AND ODD(zdif) THEN
            agzugnr:=Max(0,Min(agzuganz-9,agzuganz-2*(10-((zdif+1) DIV 2))+1));
        END;  { ELSE }
  lagzugnr:=agzuganz;
  IF farbe3=weiss THEN
      farb3:='w'
    ELSE
      farb3:='s';
  IF farbe4=weiss THEN
      farb4:='w'
    ELSE
      farb4:='s';
  FOR zeile:=8 DOWNTO 1 DO
    BEGIN
    WRITE('   ',zeile:1,' |');
    FOR spalte:=1 TO 8 DO
      IF (zeile=stell.skz) AND (spalte=stell.sks) THEN
          WRITE(' sK |')
        ELSE  IF (zeile=stell.wkz) AND (spalte=stell.wks) THEN
          WRITE(' wK |')
        ELSE  IF (zeile=stell.f3z) AND (spalte=stell.f3s)
         AND ((stell.f3z<>stell.f4z) OR (stell.f3s<>stell.f4s)
         OR (farbe3<>zfarbe)) THEN
          IF NOT(neudarstell) AND (figur3>='a')
           AND ((stell.f3z=8) OR (stell.f3z=1)) THEN
              WRITE(' ',farb3,'D |')
            ELSE
              WRITE(' ',farb3,figur3,' |')
        ELSE  IF (zeile=stell.f4z) AND (spalte=stell.f4s) THEN
          IF NOT(neudarstell) AND (figur4>='a')
           AND ((stell.f4z=8) OR (stell.f4z=1)) THEN
              WRITE(' ',farb4,'D |')
            ELSE
              WRITE(' ',farb4,figur4,' |')
        ELSE
          WRITE('    |');
    IF neudarstell THEN
        WRITELN
      ELSE
        ZugDarstellen(abschnitt,agzugnr,lagzugnr);
    WRITE('     -');
    FOR spalte:=1 TO 8 DO
      WRITE('-----');
    IF neudarstell THEN
        WRITELN
      ELSE
        ZugDarstellen(abschnitt,agzugnr,lagzugnr);
    END;  { FOR }
  WRITELN;
  IF zfarbe=weiss THEN
      WRITE('   Weiss')
    ELSE
      WRITE('   Schwarz');
  WRITE(' am Zug.');
  IF zfarbe=weiss THEN
      zstein:='s'
    ELSE
      zstein:='w';
  bdpzug:=FALSE;
  IF NOT(neudarstell) AND (agzuganz>0) THEN
    BEGIN
    zstein:=ausgefzuege[agzuganz].stein;
    IF (zstein='3') AND (figur3>='a') OR (zstein='4') AND (figur4>='a') THEN
      BEGIN
      letztzug:=ausgefzuege[agzuganz];
      bdpzug:=(ABS(letztzug.neupos.zeile-letztzug.altpos.zeile)=2);
      END;  { IF }
    END;  { IF }
  StellungsBewertung(stell,zstein,bdpzug,stlart,stlwert);
  CASE stlart OF
    matt : WRITELN('   Matt.');
    patt : WRITELN('   Patt.');
    tremis : WRITELN('   Technisches Remis.');
    remis : WRITELN('   Remis.');
    wgewinn : BEGIN
              IF schnellstmatt THEN
                  WRITE('   Schwarz ist matt in ',stlwert:1)
                ELSE
                  WRITE('   Gewonnen fuer Weiss in ',stlwert:1);
              IF stlwert=1 THEN
                  WRITELN(' Halbzug.')
                ELSE
                  WRITELN(' Halbzuegen.');
              END;
    sgewinn : BEGIN
              IF schnellstmatt THEN
                  WRITE('   Weiss ist matt in ',stlwert:1)
                ELSE
                  WRITE('   Gewonnen fuer Schwarz in ',stlwert:1);
              IF stlwert=1 THEN
                  WRITELN(' Halbzug.')
                ELSE
                  WRITELN(' Halbzuegen.');
              END;
  END;  { CASE }
END;   { StellungDarstellen }

PROCEDURE StellungVorgeben(VAR vorgegeben:BOOLEAN);
VAR
  stell:STELLUNG;
  spos:SPOSITION;
  stlart:STELLART;
  stlwert:STWERT;
  farbe:FARBEN;
  hzanzmin,hzanzmax:STWERT;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
  vsks,vskz,vwks,vwkz,vf3s,vf3z,vf4s,vf4z:INTEGER;
  wgsuch,sgsuch,msuch,psuch,trsuch,rsuch:BOOLEAN;
  gefunden,vorgegeb,ende:BOOLEAN;
  farb3,farb4:CHAR;
  chin:CHAR;
BEGIN
  WRITELN;
  WRITELN;
  WRITELN('Vorgeben einer (legalen) Stellung');
  REPEAT
    WRITELN;
    WRITE('Farbe am Zug (w/s) : ');
    READLN(chin);
  UNTIL (chin='w') OR (chin='s');
  IF chin='w' THEN
      farbe:=weiss
    ELSE
      farbe:=schwarz;
  WRITELN;
  WRITELN('Bitte Positionen eingeben ( . = egal,z.B. a. ):');
  WRITELN;
  WRITE('  Position des sK = ');
  READLN(spos[1],spos[2]);
  IF (spos[1]>='a') AND (spos[1]<='h') THEN
      vsks:=ORD(spos[1])-96
    ELSE  IF (spos[1]>='A') AND (spos[1]<='H') THEN
      vsks:=ORD(spos[1])-64
    ELSE
      vsks:=0;
  IF (spos[2]>='1') AND (spos[2]<='8') THEN
      vskz:=ORD(spos[2])-48
    ELSE
      vskz:=0;
  WRITE('  Position des wK = ');
  READLN(spos[1],spos[2]);
  IF (spos[1]>='a') AND (spos[1]<='h') THEN
      vwks:=ORD(spos[1])-96
    ELSE  IF (spos[1]>='A') AND (spos[1]<='H') THEN
      vwks:=ORD(spos[1])-64
    ELSE
      vwks:=0;
  IF (spos[2]>='1') AND (spos[2]<='8') THEN
      vwkz:=ORD(spos[2])-48
    ELSE
      vwkz:=0;
  IF farbe3=weiss THEN
      farb3:='w'
    ELSE
      farb3:='s';
  WRITE('  Position von ',farb3,figur3,' = ');
  READLN(spos[1],spos[2]);
  IF (spos[1]>='a') AND (spos[1]<='h') THEN
      vf3s:=ORD(spos[1])-96
    ELSE  IF (spos[1]>='A') AND (spos[1]<='H') THEN
      vf3s:=ORD(spos[1])-64
    ELSE
      vf3s:=0;
  IF (spos[2]>='1') AND (spos[2]<='8') THEN
      vf3z:=ORD(spos[2])-48
    ELSE
      vf3z:=0;
  IF farbe4=weiss THEN
      farb4:='w'
    ELSE
      farb4:='s';
  WRITE('  Position von ',farb4,figur4,' = ');
  READLN(spos[1],spos[2]);
  IF (spos[1]>='a') AND (spos[1]<='h') THEN
      vf4s:=ORD(spos[1])-96
    ELSE  IF (spos[1]>='A') AND (spos[1]<='H') THEN
      vf4s:=ORD(spos[1])-64
    ELSE
      vf4s:=0;
  IF (spos[2]>='1') AND (spos[2]<='8') THEN
      vf4z:=ORD(spos[2])-48
    ELSE
      vf4z:=0;
  IF (vsks>0) AND (vskz>0) AND (vwks>0) AND (vwkz>0)
   AND (vf3s>0) AND (vf3z>0) AND (vf4s>0) AND (vf4z>0) THEN
      BEGIN
      hzanzmin:=0;
      hzanzmax:=0;
      wgsuch:=TRUE;
      sgsuch:=TRUE;
      msuch:=TRUE;
      psuch:=TRUE;
      trsuch:=TRUE;
      rsuch:=TRUE;
      END
    ELSE
      BEGIN
      WRITELN;
      WRITELN('Bitte Mattzuganzahl bestimmen (0 = egal):');
      WRITELN;
      WRITE('  minimale Halbzuganzahl bis Matt = ');
      READLN(hzanzmin);
      WRITE('  maximale Halbzuganzahl bis Matt = ');
          READLN(hzanzmax);
      WRITELN;
      WRITELN('Bitte moegliche Art der Stellung bestimmen:');
      WRITELN;
      wgsuch:=FALSE;
      sgsuch:=FALSE;
      msuch:=FALSE;
      psuch:=FALSE;
      trsuch:=FALSE;
      rsuch:=FALSE;
      IF wgewinanz[farbe]>0 THEN
        BEGIN
        REPEAT
          WRITE('  mit Gewinn fuer Weiss (j/n) : ');
          READLN(chin);
        UNTIL (chin='j') OR (chin='n');
        wgsuch:=chin='j';
        END;  { IF }
      IF sgewinanz[farbe]>0 THEN
        BEGIN
        REPEAT
          WRITE('  mit Gewinn fuer Schwarz (j/n) : ');
          READLN(chin);
        UNTIL (chin='j') OR (chin='n');
        sgsuch:=chin='j';
        END;  { IF }
      WRITELN;
      REPEAT
        WRITE('  spezielle Stellung (j/n) : ');
        READLN(chin);
      UNTIL (chin='j') OR (chin='n');
      IF chin='j' THEN
        BEGIN
        WRITELN;
        IF mattanz[farbe]>0 THEN
          BEGIN
          REPEAT
            WRITE('  mit Matt (j/n) : ');
            READLN(chin);
          UNTIL (chin='j') OR (chin='n');
          msuch:=chin='j';
          END;  { IF }
        IF pattanz[farbe]>0 THEN
          BEGIN
          REPEAT
            WRITE('  mit Patt (j/n) : ');
            READLN(chin);
          UNTIL (chin='j') OR (chin='n');
          psuch:=chin='j';
          END;  { IF }
        IF tremisanz[farbe]>0 THEN
          BEGIN
          REPEAT
            WRITE('  mit techn. Remis (j/n) : ');
            READLN(chin);
          UNTIL (chin='j') OR (chin='n');
          trsuch:=chin='j';
          END;  { IF }
        IF remisanz[farbe]>0 THEN
          BEGIN
          REPEAT
            WRITE('  mit Remis (j/n) : ');
            READLN(chin);
          UNTIL (chin='j') OR (chin='n');
          rsuch:=chin='j';
          END;  { IF }
        END;  { IF }
      END;  { ELSE }
  gefunden:=FALSE;
  vorgegeb:=FALSE;
  ende:=NOT(wgsuch OR sgsuch OR msuch OR psuch OR trsuch OR rsuch);
  IF NOT(ende) THEN
    BEGIN
    sks:=vsks;
    REPEAT
     IF vsks=0 THEN
       sks:=sks+1;
     skz:=vskz;
     REPEAT
      IF vskz=0 THEN
        skz:=skz+1;
      wks:=vwks;
      REPEAT
       IF vwks=0 THEN
         wks:=wks+1;
       wkz:=vwkz;
       REPEAT
        IF vwkz=0 THEN
          wkz:=wkz+1;
        f3s:=vf3s;
        REPEAT
         IF vf3s=0 THEN
           f3s:=f3s+1;
         f3z:=vf3z;
         REPEAT
          IF vf3z=0 THEN
            f3z:=f3z+1;
          f4s:=vf4s;
          REPEAT
           IF vf4s=0 THEN
             f4s:=f4s+1;
           f4z:=vf4z;
           REPEAT
            IF vf4z=0 THEN
              f4z:=f4z+1;
            stlart:=stellarten[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
            stlwert:=stellwerte[farbe,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
            IF (stlart<>illegal) AND (stlart<>nachesp) THEN
             IF (stlart=wgewinn) AND wgsuch OR (stlart=sgewinn) AND sgsuch
              OR (stlart=matt) AND msuch OR (stlart=patt) AND psuch
              OR (stlart=tremis) AND trsuch
              OR (stlart=remis) AND rsuch THEN
               IF (stlart<>wgewinn) AND (stlart<>sgewinn)
                OR ((stlwert<=hzanzmax) OR (hzanzmax=0))
                AND (stlwert>=hzanzmin) THEN
                 BEGIN
                 gefunden:=TRUE;
                 PwerteToStell(sks,skz,wks,wkz,f3s,f3z,f4s,f4z,stell);
                 WRITELN;
                 StellungDarstellen(stell,farbe,TRUE);
                 WRITELN;
                 WRITELN;
                 REPEAT
                   WRITE('richtig  oder  falsch  oder  abbrechen');
                   WRITE('  (r/f/a) : ');
                   READLN(chin);
                 UNTIL (chin='r') OR (chin='f') OR (chin='a');
                 IF chin='a' THEN
                     ende:=TRUE
                   ELSE  IF chin<>'f' THEN
                     BEGIN
                     aktstell:=stell;
                     amzug:=farbe;
                     vorgegeb:=TRUE;
                     END;  { ELSE IF }
                 END;  { IF }
           UNTIL (f4z=8) OR (vf4z>0) OR vorgegeb OR ende;
          UNTIL (f4s=8) OR (vf4s>0) OR vorgegeb OR ende;
         UNTIL (f3z=8) OR (vf3z>0) OR vorgegeb OR ende;
        UNTIL (f3s=8) OR (vf3s>0) OR vorgegeb OR ende;
       UNTIL (wkz=8) OR (vwkz>0) OR vorgegeb OR ende;
      UNTIL (wks=8) OR (vwks>0) OR vorgegeb OR ende;
     UNTIL (skz=8) OR (vskz>0) OR vorgegeb OR ende;
    UNTIL (sks=8) OR (vsks>0) OR vorgegeb OR ende;
    END;  { IF }
  WRITELN;
  IF vorgegeb THEN
      BEGIN
      WRITELN('Stellung uebernommen.');
      agzuganz:=0;
      lagzuganz:=0;
      END
    ELSE
      BEGIN
      IF ende THEN
          WRITELN('Suche abgebrochen.')
        ELSE  IF gefunden THEN
          BEGIN
          WRITE('Es existiert keine weitere (legale) entsprechende');
          WRITELN(' Stellung.');
          END
        ELSE
          WRITELN('Es existiert keine (legale) entsprechende Stellung.');
      WRITELN(' Keine Stellung uebernommen.');
      END;  { ELSE }
  vorgegeben:=vorgegeb;
END;   { StellungVorgeben }

FUNCTION SchachB(farbe:FARBEN;
                 tks,tkz,fbs,fbz,xks,xkz,fxs,fxz:SZBEREICH):BOOLEAN;
BEGIN
  IF farbe=weiss THEN
      SchachB:=(ABS(tks-fbs)=1) AND (tkz-fbz=-1)
    ELSE
      SchachB:=(ABS(tks-fbs)=1) AND (tkz-fbz=1);
END;   { SchachB }

FUNCTION SchachS(tks,tkz,fss,fsz,xks,xkz,fxs,fxz:SZBEREICH):BOOLEAN;
VAR
  sdiff,zdiff:INTEGER;
BEGIN
  sdiff:=ABS(tks-fss);
  zdiff:=ABS(tkz-fsz);
  SchachS:=(sdiff=1) AND (zdiff=2) OR (sdiff=2) AND (zdiff=1);
END;   { SchachS }

FUNCTION WertDazwischen(zwert,wert1,wert2:SZBEREICH):BOOLEAN;
BEGIN
  IF (wert1<zwert) AND (zwert<wert2) OR (wert2<zwert) AND (zwert<wert1) THEN
      WertDazwischen:=TRUE
    ELSE
      WertDazwischen:=FALSE;
END;   { WertDazwischen }

FUNCTION SchachL(tks,tkz,fls,flz,xks,xkz,fxs,fxz:SZBEREICH):BOOLEAN;
BEGIN
  IF (fls=tks) AND (flz=tkz) THEN
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
BEGIN
  IF (fts=tks) AND (ftz=tkz) THEN
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

PROCEDURE ErzeugeKPoslist(stein:CHAR;stell:STELLUNG;
                          VAR posliste:POSLIST;VAR posanz:ZGBEREICH);
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
  IF stein='w' THEN
      nzfarbe:=schwarz
    ELSE
      nzfarbe:=weiss;
  posnr:=0;
  IF ((eks<>f3s) OR (ekz<>f3z)) AND ((eks<>f4s) OR (ekz<>f4z)) THEN
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
          IF (stlart<>illegal) AND (stlart<>nachesp) THEN
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
  posanz:=posnr;
END;   { ErzeugeKPoslist }

PROCEDURE ErzeugeBPoslist(stein:CHAR;stell:STELLUNG;
                          VAR posliste:POSLIST;VAR posanz:ZGBEREICH);
VAR
  stlart:STELLART;
  letztzug:ZUG;
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
  brett[sks,skz]:=FALSE;
  brett[wks,wkz]:=FALSE;
  brett[fxs,fxz]:=FALSE;
  IF stein='3' THEN
      bfarbe:=farbe3
    ELSE
      bfarbe:=farbe4;
  IF bfarbe=weiss THEN
      nzfarbe:=schwarz
    ELSE
      nzfarbe:=weiss;
  posanz:=0;
  IF bfarbe=schwarz THEN
      zadd:=-1
    ELSE
      zadd:=1;
  IF brett[fbs,fbz] THEN
    BEGIN
    IF (bfarbe=weiss) AND (fbz=2) OR (bfarbe=schwarz) AND (fbz=7) THEN
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
      IF (stlart<>illegal) AND (stlart<>nachesp) THEN
        BEGIN
        posanz:=posanz+1;
        posliste[posanz].spalte:=fbs;
        posliste[posanz].zeile:=nfbz;
        END;  { IF }
      END;  { WHILE }
    END;  { IF }
  IF brett[fbs,fbz] THEN
    BEGIN
    brett[fxs,fxz]:=TRUE;
    FOR sadd:=-1 TO 1 DO
      IF sadd<>0 THEN
        IF brett[fbs+sadd,fbz+zadd] THEN
          BEGIN
          nfbs:=fbs+sadd;
          nfbz:=fbz+zadd;
          IF (fxs=nfbs) AND (fxz=nfbz) THEN
            BEGIN
            IF stein='3' THEN
                stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,nfbs,nfbz,fxs,fxz]
              ELSE
                stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,fxs,fxz,nfbs,nfbz];
            IF (stlart<>illegal) AND (stlart<>nachesp) THEN
              BEGIN
              posanz:=posanz+1;
              posliste[posanz].spalte:=nfbs;
              posliste[posanz].zeile:=nfbz;
              END;  { IF }
            END;  { IF }
          END;  { IF }
    IF (figur3>='a') AND (figur4>='a') THEN
      IF agzuganz>0 THEN
        BEGIN
        letztzug:=ausgefzuege[agzuganz];
        IF ABS(letztzug.neupos.zeile-letztzug.altpos.zeile)=2 THEN
          IF (stell.f3z=stell.f4z) AND (ABS(stell.f3s-stell.f4s)=1) THEN
            BEGIN
            nfbz:=fbz+zadd;
            IF stein='3' THEN
                stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,fxs,nfbz,fxs,nfbz]
              ELSE
                stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,fxs,nfbz,fxs,nfbz];
            IF (stlart<>illegal) AND (stlart<>nachesp) THEN
              BEGIN
              posanz:=posanz+1;
              posliste[posanz].spalte:=fxs;
              posliste[posanz].zeile:=nfbz;
              END;  { IF }
            END;  { IF }
        END;  { IF }
    END;  { IF }
  brett[sks,skz]:=TRUE;
  brett[wks,wkz]:=TRUE;
  brett[fxs,fxz]:=TRUE;
END;   { ErzeugeBPoslist }

PROCEDURE ErzeugeSPoslist(stein:CHAR;stell:STELLUNG;
                          VAR posliste:POSLIST;VAR posanz:ZGBEREICH);
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
  brett[sks,skz]:=FALSE;
  brett[wks,wkz]:=FALSE;
  IF farbe3=farbe4 THEN
    brett[fxs,fxz]:=FALSE;
  IF (stein='3') AND (farbe3=weiss) OR (stein='4') AND (farbe4=weiss) THEN
      nzfarbe:=schwarz
    ELSE
      nzfarbe:=weiss;
  posnr:=0;
  IF brett[fss,fsz] AND ((fss<>fxs) OR (fsz<>fxz)) THEN
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
          IF (stlart<>illegal) AND (stlart<>nachesp) THEN
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
  posanz:=posnr;
  brett[sks,skz]:=TRUE;
  brett[wks,wkz]:=TRUE;
  brett[fxs,fxz]:=TRUE;
END;   { ErzeugeSPoslist }

PROCEDURE ErzeugeDTLPoslist(stein:CHAR;stell:STELLUNG;
                            VAR posliste:POSLIST;VAR posanz:ZGBEREICH);
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
  brett[sks,skz]:=FALSE;
  brett[wks,wkz]:=FALSE;
  IF farbe3=farbe4 THEN
    brett[fxs,fxz]:=FALSE;
  IF stein='3' THEN
      figur:=figur3
    ELSE
      figur:=figur4;
  IF (stein='3') AND (farbe3=weiss) OR (stein='4') AND (farbe4=weiss) THEN
      nzfarbe:=schwarz
    ELSE
      nzfarbe:=weiss;
  posnr:=0;
  IF brett[fes,fez] AND ((fes<>fxs) OR (fez<>fxz)) THEN
    FOR sadd:=-1 TO 1 DO
     FOR zadd:=-1 TO 1 DO
      IF (sadd<>0) OR (zadd<>0) THEN
        IF (figur='D') OR (figur='T') AND ((sadd=0) OR (zadd=0))
         OR (figur='L') AND (sadd<>0) AND (zadd<>0) THEN
          BEGIN
          nfes:=fes;
          nfez:=fez;
          WHILE ((nfes<>fxs) OR (nfez<>fxz)) AND brett[nfes+sadd,nfez+zadd] DO
            BEGIN
            nfes:=nfes+sadd;
            nfez:=nfez+zadd;
            IF stein='3' THEN
                stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,nfes,nfez,fxs,fxz]
              ELSE
                stlart:=stellarten[nzfarbe,sks,skz,wks,wkz,fxs,fxz,nfes,nfez];
            IF (stlart<>illegal) AND (stlart<>nachesp) THEN
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
  posanz:=posnr;
  brett[sks,skz]:=TRUE;
  brett[wks,wkz]:=TRUE;
  brett[fxs,fxz]:=TRUE;
END;   { ErzeugeDTLPoslist }

PROCEDURE ErzeugePoslist(zfarbe:FARBEN;stein:CHAR;stell:STELLUNG;
                         VAR posliste:POSLIST;VAR posanz:ZGBEREICH);
VAR
  figur:CHAR;
BEGIN
  posanz:=0;
  IF (zfarbe=weiss) AND (stein='w') OR (zfarbe=schwarz) AND (stein='s') THEN
      ErzeugeKPoslist(stein,stell,posliste,posanz)
    ELSE
      IF (stein='3') AND (zfarbe=farbe3)
       OR (stein='4') AND (zfarbe=farbe4) THEN
        BEGIN
        IF stein='3' THEN
            figur:=figur3
          ELSE
            figur:=figur4;
        IF (figur='D') OR (figur='T') OR (figur='L') THEN
            ErzeugeDTLPoslist(stein,stell,posliste,posanz)
          ELSE  IF figur='S' THEN
            ErzeugeSPoslist(stein,stell,posliste,posanz)
          ELSE
            ErzeugeBPoslist(stein,stell,posliste,posanz);
        END;  { IF }
END;   { ErzeugePoslist }

PROCEDURE ZugposToZug(altstell:STELLUNG;zstein:CHAR;zpos:POSITION;
                      VAR pzug:ZUG);
BEGIN
  pzug.zugnr:=0;
  pzug.stein:=zstein;
  IF zstein='w' THEN
      BEGIN
      pzug.altpos.spalte:=altstell.wks;
      pzug.altpos.zeile:=altstell.wkz;
      END
    ELSE  IF zstein='s' THEN
      BEGIN
      pzug.altpos.spalte:=altstell.sks;
      pzug.altpos.zeile:=altstell.skz;
      END
    ELSE  IF zstein='3' THEN
      BEGIN
      pzug.altpos.spalte:=altstell.f3s;
      pzug.altpos.zeile:=altstell.f3z;
      END
    ELSE
      BEGIN
      pzug.altpos.spalte:=altstell.f4s;
      pzug.altpos.zeile:=altstell.f4z;
      END;  { ELSE }
  pzug.neupos:=zpos;
  IF (zstein<>'3') AND (zpos.spalte=altstell.f3s)
   AND (zpos.zeile=altstell.f3z) THEN
      pzug.f3geschlagen:=TRUE
    ELSE
      pzug.f3geschlagen:=FALSE;
  IF (zstein<>'4') AND (zpos.spalte=altstell.f4s)
   AND (zpos.zeile=altstell.f4z) THEN
      pzug.f4geschlagen:=TRUE
    ELSE
      pzug.f4geschlagen:=FALSE;
  IF NOT(pzug.f3geschlagen) AND NOT(pzug.f4geschlagen)
   AND ((zstein='3') AND (figur3>='a') OR (zstein='4') AND (figur4>='a'))
   AND (pzug.neupos.spalte<>pzug.altpos.spalte) THEN
      pzug.enpassant:=TRUE
    ELSE
      pzug.enpassant:=FALSE;
  IF pzug.enpassant THEN
    IF zstein='3' THEN
        pzug.f4geschlagen:=TRUE
      ELSE
        pzug.f3geschlagen:=TRUE;
  IF ((zstein='3') AND (figur3>='a') OR (zstein='4') AND (figur4>='a'))
   AND ((zpos.zeile=1) OR (zpos.zeile=8)) THEN
      pzug.umgewandelt:=TRUE
    ELSE
      pzug.umgewandelt:=FALSE;
END;  { ZugposToZug }

PROCEDURE ZugAusgabe(ausgabezug:ZUG;neustell:STELLUNG;VAR zugausnr:LONGINT);
VAR
  altspos,neuspos:SPOSITION;
  stlart:STELLART;
  stlwert:STWERT;
  figur:CHAR;
  ch:CHAR;
BEGIN
  CASE ausgabezug.stein OF
    'w' : figur:='K';
    's' : figur:='K';
    '3' : figur:=figur3;
    '4' : figur:=figur4;
  END;  { CASE }
  IF figur>='a' THEN
      WRITE('  Bauer')
    ELSE
      CASE figur OF
        'K' : WRITE('  Koenig');
        'D' : WRITE('  Dame');
        'L' : WRITE('  Laeufer');
        'S' : WRITE('  Springer');
        'T' : WRITE('  Turm');
      END;  { CASE }
  PosToSpos(ausgabezug.altpos,altspos);
  PosToSpos(ausgabezug.neupos,neuspos);
  IF ausgabezug.f3geschlagen OR ausgabezug.f4geschlagen THEN
      WRITE(' ',altspos,' schlaegt ',neuspos)
    ELSE
      WRITE(' von ',altspos,' nach ',neuspos);
  IF ausgabezug.enpassant THEN
      WRITE(' ep ')
    ELSE  IF ausgabezug.umgewandelt THEN
      WRITE(' (D)')
    ELSE  IF (figur='D') OR (figur='T')  THEN
      WRITE('     ')
    ELSE  IF figur>='a' THEN
      WRITE('    ')
    ELSE  IF figur='K' THEN
      WRITE('   ')
    ELSE  IF figur='L' THEN
      WRITE('  ')
    ELSE  IF figur='S' THEN
      WRITE(' ');
  IF (figur>='a')
   AND (ABS(ausgabezug.neupos.zeile-ausgabezug.altpos.zeile)=2) THEN
      StellungsBewertung(neustell,ausgabezug.stein,TRUE,stlart,stlwert)
    ELSE
      StellungsBewertung(neustell,ausgabezug.stein,FALSE,stlart,stlwert);
  CASE stlart OF
    matt : WRITELN('   Matt.');
    patt : WRITELN('   Patt.');
    tremis : WRITELN('   Technisches Remis.');
    remis : WRITELN('   Remis.');
    wgewinn : IF schnellstmatt THEN
                  WRITELN('   Schwarz ist matt in ',stlwert:1,' Hz.')
                ELSE
                  WRITELN('   Gewonnen fuer Weiss in ',stlwert:1,' Hz.');
    sgewinn :  IF schnellstmatt THEN
                  WRITELN('   Weiss ist matt in ',stlwert:1,' Hz.')
                ELSE
                  WRITELN('   Gewonnen fuer Schwarz in ',stlwert:1,' Hz.');
  END;  { CASE }
  zugausnr:=zugausnr+1;
  IF zugausnr=20 THEN
    BEGIN
    WRITELN;
    WRITE('Bitte <Return> druecken ');
    READ(ch);
    WRITELN;
    zugausnr:=0;
    END;  { IF }
END;   { ZugAusgabe }

PROCEDURE MoegZuegeAusgeben;
VAR
  kposliste,f3posliste,f4posliste:POSLIST;
  neustell:STELLUNG;
  moegzug:ZUG;
  kzuganz,f3zuganz,f4zuganz:ZGBEREICH;
  zugnr:ZGBEREICH;
  zanr:LONGINT;
BEGIN
  WRITELN;
  WRITELN;
  IF amzug=weiss THEN
      WRITE('Weiss')
    ELSE
      WRITE('Schwarz');
  WRITELN(' ist am Zug.  Moegliche Zuege :');
  WRITELN;
  zanr:=0;
  IF amzug=weiss THEN
      ErzeugePoslist(amzug,'w',aktstell,kposliste,kzuganz)
    ELSE
      ErzeugePoslist(amzug,'s',aktstell,kposliste,kzuganz);
  IF NOT(abbrechen) THEN
    ErzeugePoslist(amzug,'3',aktstell,f3posliste,f3zuganz);
  IF NOT(abbrechen) THEN
    ErzeugePoslist(amzug,'4',aktstell,f4posliste,f4zuganz);
  IF NOT(abbrechen) THEN
    BEGIN
    IF kzuganz+f3zuganz+f4zuganz=0 THEN
        WRITELN(' Kein Zug moeglich')
      ELSE
        BEGIN
        neustell:=aktstell;
        FOR zugnr:=1 TO kzuganz DO
          IF amzug=weiss THEN
              BEGIN
              ZugposToZug(aktstell,'w',kposliste[zugnr],moegzug);
              neustell.wks:=kposliste[zugnr].spalte;
              neustell.wkz:=kposliste[zugnr].zeile;
              ZugAusgabe(moegzug,neustell,zanr);
              END
            ELSE
              BEGIN
              ZugposToZug(aktstell,'s',kposliste[zugnr],moegzug);
              neustell.sks:=kposliste[zugnr].spalte;
              neustell.skz:=kposliste[zugnr].zeile;
              ZugAusgabe(moegzug,neustell,zanr);
              END;  { ELSE }
        FOR zugnr:=1 TO f3zuganz DO
          BEGIN
          ZugposToZug(aktstell,'3',f3posliste[zugnr],moegzug);
          neustell:=aktstell;
          neustell.f3s:=f3posliste[zugnr].spalte;
          neustell.f3z:=f3posliste[zugnr].zeile;
          IF moegzug.enpassant THEN
            IF farbe4=weiss THEN
              neustell.f4z:=neustell.f4z-1
            ELSE
              neustell.f4z:=neustell.f4z+1;
          ZugAusgabe(moegzug,neustell,zanr);
          END;  { FOR }
        FOR zugnr:=1 TO f4zuganz DO
          BEGIN
          ZugposToZug(aktstell,'4',f4posliste[zugnr],moegzug);
          neustell:=aktstell;
          neustell.f4s:=f4posliste[zugnr].spalte;
          neustell.f4z:=f4posliste[zugnr].zeile;
          IF moegzug.enpassant THEN
            IF farbe3=weiss THEN
              neustell.f3z:=neustell.f3z-1
            ELSE
              neustell.f3z:=neustell.f3z+1;
          ZugAusgabe(moegzug,neustell,zanr);
          END;  { FOR }
        END;  { ELSE }
    END;  { IF }
END;   { MoegZuegeAusgeben }

PROCEDURE ZugSetzen(setzzug:ZUG);
VAR
  zugnr:AZBEREICH;
BEGIN
  IF agzuganz=maxagzuganz THEN
    BEGIN
    FOR zugnr:=1 TO agzuganz-1 DO
      ausgefzuege[zugnr]:=ausgefzuege[zugnr+1];
    agzuganz:=agzuganz-1;
    END;  { IF }
  agzuganz:=agzuganz+1;
  IF lagzuganz=0 THEN
      setzzug.zugnr:=1
    ELSE  IF agzuganz=1 THEN
      setzzug.zugnr:=ausgefzuege[agzuganz].zugnr
    ELSE
      IF amzug=schwarz THEN
          setzzug.zugnr:=ausgefzuege[agzuganz-1].zugnr
        ELSE
          setzzug.zugnr:=ausgefzuege[agzuganz-1].zugnr+1;
  ausgefzuege[agzuganz]:=setzzug;
  lagzuganz:=agzuganz;
  IF setzzug.stein='w' THEN
      BEGIN
      aktstell.wks:=setzzug.neupos.spalte;
      aktstell.wkz:=setzzug.neupos.zeile;
      END
    ELSE  IF setzzug.stein='s' THEN
      BEGIN
      aktstell.sks:=setzzug.neupos.spalte;
      aktstell.skz:=setzzug.neupos.zeile;
      END
    ELSE  IF setzzug.stein='3' THEN
      BEGIN
      aktstell.f3s:=setzzug.neupos.spalte;
      aktstell.f3z:=setzzug.neupos.zeile;
      END
    ELSE
      BEGIN
      aktstell.f4s:=setzzug.neupos.spalte;
      aktstell.f4z:=setzzug.neupos.zeile;
      END;  { ELSE }
  IF setzzug.enpassant THEN
    IF setzzug.f3geschlagen THEN
        IF farbe3=weiss THEN
          aktstell.f3z:=aktstell.f3z-1
        ELSE
          aktstell.f3z:=aktstell.f3z+1
      ELSE
        IF farbe4=weiss THEN
          aktstell.f4z:=aktstell.f4z-1
        ELSE  
          aktstell.f4z:=aktstell.f4z+1;
  IF amzug=weiss THEN
      amzug:=schwarz
    ELSE
      amzug:=weiss;
END;   { ZugSetzen }

PROCEDURE ZugEingeben(VAR eingegeben:BOOLEAN);
VAR
  zeingabe:PACKED ARRAY[1..5] OF CHAR;
  posliste:POSLIST;
  moegzug:ZUG;
  altpos,neupos:POSITION;
  spos:SPOSITION;
  zuegeanz,posnr:ZGBEREICH;
  zugmoegl:BOOLEAN;
  zugstein:CHAR;
BEGIN
  WRITELN;
  WRITELN;
  WRITELN('Aktuelle Stellung :');
  StellungDarstellen(aktstell,amzug,FALSE);
  WRITELN;
  WRITE('Einzugebender Zug (z.B. a2-a3) : ');
  READLN(zeingabe[1],zeingabe[2],zeingabe[3],zeingabe[4],zeingabe[5]);
  spos[1]:=zeingabe[1];
  spos[2]:=zeingabe[2];
  SposToPos(spos,altpos);
  spos[1]:=zeingabe[4];
  spos[2]:=zeingabe[5];
  SposToPos(spos,neupos);
  IF (altpos.spalte=aktstell.wks) AND (altpos.zeile=aktstell.wkz)
   AND (amzug=weiss) THEN
      BEGIN
      zugstein:='w';
      zugmoegl:=TRUE;
      END
    ELSE  IF (altpos.spalte=aktstell.sks) AND (altpos.zeile=aktstell.skz)
     AND (amzug=schwarz) THEN
      BEGIN
      zugstein:='s';
      zugmoegl:=TRUE;
      END
    ELSE  IF (altpos.spalte=aktstell.f3s) AND (altpos.zeile=aktstell.f3z)
     AND (amzug=farbe3) THEN
      BEGIN
      zugstein:='3';
      zugmoegl:=TRUE;
      END
    ELSE  IF (altpos.spalte=aktstell.f4s) AND (altpos.zeile=aktstell.f4z)
     AND (amzug=farbe4) THEN
      BEGIN
      zugstein:='4';
      zugmoegl:=TRUE;
      END
    ELSE
      zugmoegl:=FALSE;
  IF NOT(zugmoegl) THEN
      BEGIN
      WRITELN;
      WRITELN('Zug nicht moeglich');
      eingegeben:=FALSE;
      END
    ELSE
      BEGIN
      ErzeugePoslist(amzug,zugstein,aktstell,posliste,zuegeanz);
      IF abbrechen THEN
          eingegeben:=FALSE
        ELSE
          BEGIN
          zugmoegl:=FALSE;
          posnr:=0;
          WHILE (posnr<zuegeanz) AND NOT(zugmoegl) DO
            BEGIN
            posnr:=posnr+1;
            IF (posliste[posnr].spalte=neupos.spalte)
             AND (posliste[posnr].zeile=neupos.zeile) THEN
              zugmoegl:=TRUE;
            END;  { WHILE }
          IF NOT(zugmoegl) THEN
              BEGIN
              WRITELN;
              WRITELN('Zug nicht moeglich');
              eingegeben:=FALSE;
              END
            ELSE
              BEGIN
              ZugposToZug(aktstell,zugstein,posliste[posnr],moegzug);
              ZugSetzen(moegzug);
              eingegeben:=TRUE;
              END;  { ELSE }
          END;  { ELSE }
      END;  { ELSE }
END;   { ZugEingeben }

FUNCTION ZugWert(wzug:ZUG;neustell:STELLUNG):REAL;
VAR
  stlart:STELLART;
  stlwert:STWERT;
  farbe:FARBEN;
  zwert:REAL;
BEGIN
  IF (wzug.stein='w') OR (wzug.stein='3') AND (farbe3=weiss)
   OR (wzug.stein='4') AND (farbe4=weiss) THEN
      farbe:=schwarz
    ELSE
      farbe:=weiss;
  IF ((wzug.stein='3') AND (figur3>='a') OR (wzug.stein='4') AND (figur4>='a'))
   AND (ABS(wzug.neupos.zeile-wzug.altpos.zeile)=2) THEN
      StellungsBewertung(neustell,wzug.stein,TRUE,stlart,stlwert)
    ELSE
      StellungsBewertung(neustell,wzug.stein,FALSE,stlart,stlwert);
  CASE stlart OF
    matt : zwert:=-2*maxhzuganz;
    patt : zwert:=0;
    tremis : zwert:=0;
    remis : zwert:=0;
    wgewinn : IF farbe=weiss THEN
                  zwert:=maxhzuganz+1-stlwert
                ELSE
                  zwert:=-(maxhzuganz+1-stlwert);
    sgewinn : IF farbe=schwarz THEN
                  zwert:=maxhzuganz+1-stlwert
                ELSE
                  zwert:=-(maxhzuganz+1-stlwert);
  END;  { CASE }
  IF (stlart=patt) AND (stlart=tremis) THEN
      ZugWert:=zwert
    ELSE
      ZugWert:=zwert-RandomReal;
END;   { ZugWert }

PROCEDURE ZugAusfuehren;
VAR
  kposliste,f3posliste,f4posliste:POSLIST;
  neustell,bestnstell:STELLUNG;
  neuzug,bestzug:ZUG;
  kzuganz,f3zuganz,f4zuganz:ZGBEREICH;
  zugnr:ZGBEREICH;
  zwert,bestzwert:REAL;
  zanr:LONGINT;
BEGIN
  WRITELN;
  WRITELN;
  WRITE('Zug von');
  IF amzug=weiss THEN
      WRITE(' Weiss :')
    ELSE
      WRITE(' Schwarz :');
  IF amzug=weiss THEN
      ErzeugePoslist(amzug,'w',aktstell,kposliste,kzuganz)
    ELSE
      ErzeugePoslist(amzug,'s',aktstell,kposliste,kzuganz);
  IF NOT(abbrechen) THEN
    ErzeugePoslist(amzug,'3',aktstell,f3posliste,f3zuganz);
  IF NOT(abbrechen) THEN
    ErzeugePoslist(amzug,'4',aktstell,f4posliste,f4zuganz);
  IF NOT(abbrechen) THEN
    BEGIN
    bestzwert:=4*maxhzuganz;
    neustell:=aktstell;
    FOR zugnr:=1 TO kzuganz DO
      BEGIN
      IF amzug=weiss THEN
          BEGIN
          ZugposToZug(aktstell,'w',kposliste[zugnr],neuzug);
          neustell.wks:=kposliste[zugnr].spalte;
          neustell.wkz:=kposliste[zugnr].zeile;
          zwert:=ZugWert(neuzug,neustell);
          END
        ELSE
          BEGIN
          ZugposToZug(aktstell,'s',kposliste[zugnr],neuzug);
          neustell.sks:=kposliste[zugnr].spalte;
          neustell.skz:=kposliste[zugnr].zeile;
          zwert:=ZugWert(neuzug,neustell);
          END;  { ELSE }
      IF zwert<bestzwert THEN
        BEGIN
        bestzug:=neuzug;
        bestnstell:=neustell;
        bestzwert:=zwert;
        END;  { IF }
      END;  { FOR }
    FOR zugnr:=1 TO f3zuganz DO
      BEGIN
      ZugposToZug(aktstell,'3',f3posliste[zugnr],neuzug);
      neustell:=aktstell;
      neustell.f3s:=f3posliste[zugnr].spalte;
      neustell.f3z:=f3posliste[zugnr].zeile;
      IF neuzug.enpassant THEN
        IF farbe4=weiss THEN
          neustell.f4z:=neustell.f4z-1
        ELSE
          neustell.f4z:=neustell.f4z+1;
      zwert:=ZugWert(neuzug,neustell);
      IF zwert<bestzwert THEN
        BEGIN
        bestzug:=neuzug;
        bestnstell:=neustell;
        bestzwert:=zwert;
        END;  { IF }
      END;  { FOR }
    FOR zugnr:=1 TO f4zuganz DO
      BEGIN
      ZugposToZug(aktstell,'4',f4posliste[zugnr],neuzug);
      neustell:=aktstell;
      neustell.f4s:=f4posliste[zugnr].spalte;
      neustell.f4z:=f4posliste[zugnr].zeile;
      IF neuzug.enpassant THEN
        IF farbe3=weiss THEN
          neustell.f3z:=neustell.f3z-1
        ELSE
          neustell.f3z:=neustell.f3z+1;
      zwert:=ZugWert(neuzug,neustell);
      IF zwert<bestzwert THEN
        BEGIN
        bestzug:=neuzug;
        bestnstell:=neustell;
        bestzwert:=zwert;
        END;  { IF }
      END;  { FOR }
    zanr:=0;
    ZugAusgabe(bestzug,bestnstell,zanr);
    ZugSetzen(bestzug);
    END;  { IF }
END;   { ZugAusfuehren }

PROCEDURE ZugZuruecknehmen;
VAR
  zanr:LONGINT;
BEGIN
  WRITELN;
  WRITELN;
  WRITE('Zurueckgenommen :');
  zanr:=0;
  ZugAusgabe(ausgefzuege[agzuganz],aktstell,zanr);
  IF ausgefzuege[agzuganz].stein='w' THEN
      BEGIN
      aktstell.wks:=ausgefzuege[agzuganz].altpos.spalte;
      aktstell.wkz:=ausgefzuege[agzuganz].altpos.zeile;
      END
    ELSE  IF ausgefzuege[agzuganz].stein='s' THEN
      BEGIN
      aktstell.sks:=ausgefzuege[agzuganz].altpos.spalte;
      aktstell.skz:=ausgefzuege[agzuganz].altpos.zeile;
      END
    ELSE  IF ausgefzuege[agzuganz].stein='3' THEN
      BEGIN
      aktstell.f3s:=ausgefzuege[agzuganz].altpos.spalte;
      aktstell.f3z:=ausgefzuege[agzuganz].altpos.zeile;
      END
    ELSE
      BEGIN
      aktstell.f4s:=ausgefzuege[agzuganz].altpos.spalte;
      aktstell.f4z:=ausgefzuege[agzuganz].altpos.zeile;
      END;  { ELSE }
  IF ausgefzuege[agzuganz].enpassant THEN
    IF ausgefzuege[agzuganz].f3geschlagen THEN
        IF farbe3=weiss THEN
          aktstell.f3z:=aktstell.f3z+1
        ELSE
          aktstell.f3z:=aktstell.f3z-1
      ELSE
        IF farbe4=weiss THEN
          aktstell.f4z:=aktstell.f4z+1
        ELSE
          aktstell.f4z:=aktstell.f4z-1;
  agzuganz:=agzuganz-1;
  IF amzug=weiss THEN
      amzug:=schwarz
    ELSE
      amzug:=weiss;
END;   { ZugZuruecknehmen }

PROCEDURE ZugVorspielen;
VAR
  zanr:LONGINT;
BEGIN
  WRITELN;
  WRITELN;
  WRITE('Vorgespielt :');
  agzuganz:=agzuganz+1;
  IF ausgefzuege[agzuganz].stein='w' THEN
      BEGIN
      aktstell.wks:=ausgefzuege[agzuganz].neupos.spalte;
      aktstell.wkz:=ausgefzuege[agzuganz].neupos.zeile;
      END
    ELSE  IF ausgefzuege[agzuganz].stein='s' THEN
      BEGIN
      aktstell.sks:=ausgefzuege[agzuganz].neupos.spalte;
      aktstell.skz:=ausgefzuege[agzuganz].neupos.zeile;
      END
    ELSE  IF ausgefzuege[agzuganz].stein='3' THEN
      BEGIN
      aktstell.f3s:=ausgefzuege[agzuganz].neupos.spalte;
      aktstell.f3z:=ausgefzuege[agzuganz].neupos.zeile;
      END
    ELSE
      BEGIN
      aktstell.f4s:=ausgefzuege[agzuganz].neupos.spalte;
      aktstell.f4z:=ausgefzuege[agzuganz].neupos.zeile;
      END;  { ELSE }
  IF ausgefzuege[agzuganz].enpassant THEN
    IF ausgefzuege[agzuganz].f3geschlagen THEN
        IF farbe3=weiss THEN
          aktstell.f3z:=aktstell.f3z-1
        ELSE
          aktstell.f3z:=aktstell.f3z+1
      ELSE
        IF farbe4=weiss THEN
          aktstell.f4z:=aktstell.f4z-1
        ELSE
          aktstell.f4z:=aktstell.f4z+1;
  IF amzug=weiss THEN
      amzug:=schwarz
    ELSE
      amzug:=weiss;
  zanr:=0;
  ZugAusgabe(ausgefzuege[agzuganz],aktstell,zanr)
END;   { ZugVorspielen }

FUNCTION ZugMoeglich:BOOLEAN;
VAR
  stlart:STELLART;
  sks,skz,wks,wkz,f3s,f3z,f4s,f4z:SZBEREICH;
BEGIN
  StellToPwerte(aktstell,sks,skz,wks,wkz,f3s,f3z,f4s,f4z);
  stlart:=stellarten[amzug,sks,skz,wks,wkz,f3s,f3z,f4s,f4z];
  IF (stlart=matt) OR (stlart=patt) OR (stlart=tremis) THEN
      ZugMoeglich:=FALSE
    ELSE  IF (figur3>='a') AND ((f3z=1) OR (f3z=8))
     OR (figur4>='a') AND ((f4z=1) OR (f4z=8)) THEN
      ZugMoeglich:=FALSE
    ELSE  IF (f3s=sks) AND (f3z=skz) OR (f3s=wks) AND (f3z=wkz)
     OR (f3s=f4s) AND (f3z=f4z) OR (f4s=sks) AND (f4z=skz)
     OR (f4s=wks) AND (f4z=wkz) THEN
      ZugMoeglich:=FALSE
    ELSE
      ZugMoeglich:=TRUE;
END;   { ZugMoeglich }


BEGIN
  WRITELN;
  WRITELN('Fuehren eines vollstaendig analysierten 4-Steine-Endspiels');
  WRITELN;
  WRITELN;
  InitDaten;
  WRITE('Bitte Zufallszahl eingeben : ');
  READLN(zufallswert);
  Laden;
  IF NOT(abbrechen) THEN
    BEGIN
    ErmittelStatistik;
    StatistikAusgeben;
    WRITELN;
    WRITE('Bitte <Return> druecken ');
    READ(ch);
    stvorhanden:=FALSE;
    endstellung:=FALSE;
    REPEAT
      IF stvorhanden THEN
        endstellung:=NOT(ZugMoeglich);
      WRITELN;
      WRITELN;
      WRITELN('Bitte auswaehlen :');
      WRITELN;
      WRITELN(' 1. Endspieldaten ausgeben');
      WRITELN(' 2. Stellung vorgeben');
      IF stvorhanden THEN
        BEGIN
        WRITELN(' 3. Stellung ausgeben');
        IF agzuganz>0 THEN
          WRITELN(' 4. Zug zuruecknehmen');
        IF agzuganz<lagzuganz THEN
          WRITELN(' 5. Zug vorspielen');
        IF NOT(endstellung) THEN
          BEGIN
          WRITELN(' 6. Moegliche Zuege ausgeben');
          WRITELN(' 7. Zug eingeben');
          WRITELN(' 8. Zug ermitteln');
          WRITELN(' 9. Zug spielen');
          END;  { IF }
        END;  { IF }
      WRITELN('(E)nde');
      WRITELN;
      WRITE('Eingabe : ');
      READLN(chin);
      IF (chin>='1') AND (chin<='9') THEN
       IF (chin<='2') OR stvorhanden THEN
        IF (chin<='3') OR (chin='4') AND (agzuganz>0)
         OR (chin='5') AND (agzuganz<lagzuganz)
         OR (chin>='6') AND NOT(endstellung) THEN
          BEGIN
          CASE chin OF
            '1' : StatistikAusgeben;
            '2' : BEGIN
                  StellungVorgeben(vorgegeben);
                  IF vorgegeben THEN
                    stvorhanden:=TRUE;
                  END;
            '3' : BEGIN
                  WRITELN;
                  WRITELN;
                  WRITELN('Aktuelle Stellung :');
                  StellungDarstellen(aktstell,amzug,FALSE);
                  END;
            '4' : BEGIN
                  ZugZuruecknehmen;
                  StellungDarstellen(aktstell,amzug,FALSE);
                  END;
            '5' : BEGIN
                  ZugVorspielen;
                  StellungDarstellen(aktstell,amzug,FALSE);
                  END;
            '6' : MoegZuegeAusgeben;
            '7' : BEGIN
                  ZugEingeben(eingegeben);
                  IF eingegeben THEN
                    StellungDarstellen(aktstell,amzug,FALSE);
                  END;
            '8' : BEGIN
                  ZugAusfuehren;
                  IF NOT(abbrechen) THEN
                    StellungDarstellen(aktstell,amzug,FALSE);
                  END;
            '9' : BEGIN
                  ZugEingeben(eingegeben);
                  IF eingegeben THEN
                    BEGIN
                    IF ZugMoeglich THEN
                        ZugAusfuehren
                      ELSE
                        BEGIN
                        WRITELN;
                        WRITELN;
                        WRITELN('Kein Gegenzug mehr moeglich.');
                        END;  { ELSE }
                    IF NOT(abbrechen) THEN
                      StellungDarstellen(aktstell,amzug,FALSE);
                    END;  { IF }
                  END;
          END;  { CASE }
          IF NOT(abbrechen) THEN
            BEGIN
            WRITELN;
            WRITE('Bitte <Return> druecken ');
            READ(ch);
            END;  { IF }
          END;  { ELSE }
    UNTIL (chin='E') OR abbrechen;
    END;  { ELSE }
  IF abbrechen THEN
    BEGIN
    WRITELN;
    WRITELN;
    WRITELN('Programmlauf abgebrochen');
    END;  { IF }
  WRITELN;
END.

