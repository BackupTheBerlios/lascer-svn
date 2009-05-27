/*
 * Dateiname      : RealFunkWertPraedikat.java
 * Letzte �nderung: 06. September 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2006
 *
 * This file is part of Lascer (http://lascer.berlios.de/).
 *
 * Lascer is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Lascer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Lascer; if not, see <http://www.gnu.org/licenses/>.
 */


package lascer.praedikate.einzelne.realpraed;

import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;
import lascer.realfunktionen.RealFunktion;
import lascer.praedikate.Praedikat;

/**
 * Definiert ein Pr�dikat, das f�r die Flie�komma-Werte einer Funktion pr�ft,
 * ob sie gleich sind zu einem vorgegebenen Wert.
 *
 * @author  Dietmar Lippold
 */
public class RealFunkWertPraedikat extends RealFunkPraedikat
    implements Praedikat {

    /**
     * Der Wert, gegen den der Funktionswert verglichen wird.
     */
    private final float testWert;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param funktion         Die Funktion, deren Wert f�r ein Beispiel in
     *                         Bezug auf das vorgegebene Intervall gepr�ft
     *                         wird.
     * @param testWert         Der Wert, gegen den der Funktionswert
     *                         verglichen wird.
     * @param beispieldaten    Daten, zu deren Attribut-Sammlung das Pr�dikat
     *                         erzeugt wird und zu deren Beispielen die
     *                         �bergeben Funktionswerte geh�ren.
     * @param posBspFunkWerte  Die Werte der Funktion f�r die positiven
     *                         Beispiele.
     * @param negBspFunkWerte  Die Werte der Funktion f�r die negativen
     *                         Beispiele.
     */
    public RealFunkWertPraedikat(RealFunktion funktion, float testWert,
                                 Beispieldaten beispieldaten,
                                 float[] posBspFunkWerte, float[] negBspFunkWerte) {
        super(funktion, beispieldaten, posBspFunkWerte, negBspFunkWerte,
              Konstanten.REAL_FUNK_WERT_PRAED_KOMPLEX);

        this.testWert = testWert;
    }

    /**
     * Ermitteln, ob das Pr�dikat f�r das �bergebene Beispiel erf�llt ist.
     *
     * @param beispiel    Ein Beispiel, f�r das ermittelt werden soll, ob das
     *                    Pr�dikat daf�r erf�llt ist.
     * @param invertiert  Die Angabe, ob der Wert des Pr�dikats invertiert
     *                    werden sollen.
     *
     * @return  Die Angabe, ob das Pr�dikat f�r  das �bergebene Beispiel
     *          erf�llt ist.
     */
    public boolean wert(Beispiel beispiel, boolean invertiert) {
        float funkWert;

        funkWert = funktion().wert(beispiel);
        if (funkWert == Konstanten.UNBEKANNT_WERT) {
            return false;
        } else {
            if (invertiert) {
                return (funkWert != testWert);
            } else {
                return (funkWert == testWert);
            }
        }
    }

    /**
     * Ermittelt f�r jeden der �bergebenen Werte, ob dieser gleich ist zum
     * vorgegebenen Wert des Pr�dikats.
     *
     * @param funkWerte  Die Werte, f�r die ermittelt wird, ob sie gleich sind
     *                   zum vorgegebenen Wert des Pr�dikats.
     *
     * @return  Ein neu erzeugtes Array mit Werten die angeben, ob die
     *          �bergebenen Werte gleich sind zum vorgegebenen Wert des
     *          Pr�dikats.
     */
    protected boolean[] bspWerte(float[] funkWerte) {
        boolean[] bspWerte;

        bspWerte = new boolean[funkWerte.length];
        for (int bspNr = 0; bspNr < funkWerte.length; bspNr++) {
            bspWerte[bspNr] = (funkWerte[bspNr] == testWert);
        }
        return bspWerte;
    }

    /**
     * Liefert eine Beschreibung des Pr�dikats.
     *
     * @return  Eine Beschreibung des Pr�dikats.
     */
    public String toString() {
        return funktion().toString() + " = " + testWert;
    }
}

