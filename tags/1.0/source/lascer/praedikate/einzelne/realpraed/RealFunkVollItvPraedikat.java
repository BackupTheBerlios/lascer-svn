/*
 * Dateiname      : RealFunkVollItvPraedikat.java
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
 * Definiert ein Pr�dikat, das f�r die Flie�komma-Werte einer Funktion
 * pr�ft, ob sie in einem vorgegebenen Voll-Intervall enthalten sind.
 *
 * @author  Dietmar Lippold
 */
public class RealFunkVollItvPraedikat extends RealFunkPraedikat
    implements Praedikat {

    /**
     * Die Untergrenze des Intervalls (inklusive).
     */
    private final float minWert;

    /**
     * Die Obergrenze des Intervalls (inklusive).
     */
    private final float maxWert;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param funktion         Die Funktion, deren Wert f�r ein Beispiel in
     *                         Bezug auf das vorgegebene Intervall gepr�ft
     *                         wird.
     * @param minWert          Die Untergrenze des Intervalls (inklusive).
     * @param maxWert          Die Obergrenze des Intervalls (inklusive).
     * @param beispieldaten    Daten, zu deren Attribut-Sammlung das Pr�dikat
     *                         erzeugt wird und zu deren Beispielen die
     *                         �bergeben Funktionswerte geh�ren.
     * @param posBspFunkWerte  Die Werte der Funktion f�r die positiven
     *                         Beispiele.
     * @param negBspFunkWerte  Die Werte der Funktion f�r die negativen
     *                         Beispiele.
     */
    public RealFunkVollItvPraedikat(RealFunktion funktion, float minWert,
                                    float maxWert, Beispieldaten beispieldaten,
                                    float[] posBspFunkWerte, float[] negBspFunkWerte) {
        super(funktion, beispieldaten, posBspFunkWerte, negBspFunkWerte,
              Konstanten.REAL_FUNK_VOLL_ITV_PRAED_KOMPLEX);

        this.minWert = minWert;
        this.maxWert = maxWert;
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
        float   funkWert;
        boolean praedWert;

        funkWert = funktion().wert(beispiel);
        if (funkWert == Konstanten.UNBEKANNT_WERT) {
            return false;
        } else {
            praedWert = ((minWert <= funkWert) && (funkWert <= maxWert));
            if (invertiert) {
                return !praedWert;
            } else {
                return praedWert;
            }
        }
    }

    /**
     * Ermittelt f�r jeden der �bergebenen Werte, ob dieser im Voll-Intervall
     * des Pr�dikats liegt.
     *
     * @param funkWerte  Die Werte, f�r die ermittelt wird, ob sie im
     *                   Intervall des Pr�dikats liegen.
     *
     * @return  Ein neu erzeugtes Array mit Werten die angeben, ob die
     *          �bergebenen Werte im Intervall des Pr�dikats liegen.
     */
    protected boolean[] bspWerte(float[] funkWerte) {
        boolean[] bspWerte;
        float     funkWert;

        bspWerte = new boolean[funkWerte.length];
        for (int bspNr = 0; bspNr < funkWerte.length; bspNr++) {
            funkWert = funkWerte[bspNr];
            bspWerte[bspNr] = (minWert <= funkWert) && (funkWert <= maxWert);
        }
        return bspWerte;
    }

    /**
     * Liefert eine Beschreibung des Pr�dikats.
     *
     * @return  Eine Beschreibung des Pr�dikats.
     */
    public String toString() {
        return funktion().toString() + " in [" + minWert + ", " + maxWert + "]";
    }
}

