/*
 * Dateiname      : RealFunkVollItvPraedikat.java
 * Letzte Änderung: 06. September 2006
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
 * Definiert ein Prädikat, das für die Fließkomma-Werte einer Funktion
 * prüft, ob sie in einem vorgegebenen Voll-Intervall enthalten sind.
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
     * @param funktion         Die Funktion, deren Wert für ein Beispiel in
     *                         Bezug auf das vorgegebene Intervall geprüft
     *                         wird.
     * @param minWert          Die Untergrenze des Intervalls (inklusive).
     * @param maxWert          Die Obergrenze des Intervalls (inklusive).
     * @param beispieldaten    Daten, zu deren Attribut-Sammlung das Prädikat
     *                         erzeugt wird und zu deren Beispielen die
     *                         übergeben Funktionswerte gehören.
     * @param posBspFunkWerte  Die Werte der Funktion für die positiven
     *                         Beispiele.
     * @param negBspFunkWerte  Die Werte der Funktion für die negativen
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
     * Ermitteln, ob das Prädikat für das übergebene Beispiel erfüllt ist.
     *
     * @param beispiel    Ein Beispiel, für das ermittelt werden soll, ob das
     *                    Prädikat dafür erfüllt ist.
     * @param invertiert  Die Angabe, ob der Wert des Prädikats invertiert
     *                    werden sollen.
     *
     * @return  Die Angabe, ob das Prädikat für  das übergebene Beispiel
     *          erfüllt ist.
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
     * Ermittelt für jeden der übergebenen Werte, ob dieser im Voll-Intervall
     * des Prädikats liegt.
     *
     * @param funkWerte  Die Werte, für die ermittelt wird, ob sie im
     *                   Intervall des Prädikats liegen.
     *
     * @return  Ein neu erzeugtes Array mit Werten die angeben, ob die
     *          übergebenen Werte im Intervall des Prädikats liegen.
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
     * Liefert eine Beschreibung des Prädikats.
     *
     * @return  Eine Beschreibung des Prädikats.
     */
    public String toString() {
        return funktion().toString() + " in [" + minWert + ", " + maxWert + "]";
    }
}

