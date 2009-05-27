/*
 * Dateiname      : RealFunkHalbItvPraedikat.java
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
 * Definiert ein Prädikat, das für die Fließkomma-Werte einer Funktion prüft,
 * ob sie in einem Halb-Intervall liegen, also kleiner-gleich oder
 * größer-gleich sind zu einem vorgegebenen Wert sind.
 *
 * @author  Dietmar Lippold
 */
public class RealFunkHalbItvPraedikat extends RealFunkPraedikat
    implements Praedikat {

    /**
     * Die Ober- oder Untergrenze des Intervalls (inklusive).
     */
    private final float grenzWert;

    /**
     * Gibt an, ob das Halb-Intervall des Prädikats nach oben beschränkt ist.
     * Falls nicht, ist es nach unten beschränkt.
     */
    private final boolean nachObenBeschraenkt;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param funktion         Die Funktion, deren Wert für ein Beispiel in
     *                         Bezug auf das vorgegebene Intervall geprüft
     *                         wird.
     * @param grenzWert        Der Wert, gegen den der Funktionswert
     *                         verglichen wird.
     * @param obenBeschraenkt  Gibt an, ob das Halb-Intervall des Prädikats
     *                         nach oben beschränkt ist. Falls nicht, ist es
     *                         nach unten beschränkt.
     * @param beispieldaten    Daten, zu deren Attribut-Sammlung das Prädikat
     *                         erzeugt wird und zu deren Beispielen die
     *                         übergeben Funktionswerte gehören.
     * @param posBspFunkWerte  Die Werte der Funktion für die positiven
     *                         Beispiele.
     * @param negBspFunkWerte  Die Werte der Funktion für die negativen
     *                         Beispiele.
     */
    public RealFunkHalbItvPraedikat(RealFunktion funktion, float grenzWert,
                                    boolean obenBeschraenkt,
                                    Beispieldaten beispieldaten,
                                    float[] posBspFunkWerte, float[] negBspFunkWerte) {
        super(funktion, beispieldaten, posBspFunkWerte, negBspFunkWerte,
              Konstanten.REAL_FUNK_HALB_ITV_PRAED_KOMPLEX);

        this.grenzWert = grenzWert;
        this.nachObenBeschraenkt = obenBeschraenkt;
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
            if (nachObenBeschraenkt) {
                praedWert = (funkWert <= grenzWert);
            } else {
                praedWert = (funkWert >= grenzWert);
            }

            if (invertiert) {
                return !praedWert;
            } else {
                return praedWert;
            }
        }
    }

    /**
     * Ermittelt für jeden der übergebenen Werte, ob dieser im Halb-Intervall
     * des Prädikats liegt.
     *
     * @param funkWerte  Die Werte, für die ermittelt wird, ob sie gleich sind
     *                   zum vorgegebenen Wert des Prädikats.
     *
     * @return  Ein neu erzeugtes Array mit Werten die angeben, ob die
     *          übergebenen Werte gleich sind zum vorgegebenen Wert des
     *          Prädikats.
     */
    protected boolean[] bspWerte(float[] funkWerte) {
        boolean[] bspWerte;

        bspWerte = new boolean[funkWerte.length];
        if (nachObenBeschraenkt) {
            for (int bspNr = 0; bspNr < funkWerte.length; bspNr++) {
                bspWerte[bspNr] = (funkWerte[bspNr] <= grenzWert);
            }
        } else {
            for (int bspNr = 0; bspNr < funkWerte.length; bspNr++) {
                bspWerte[bspNr] = (funkWerte[bspNr] >= grenzWert);
            }
        }
        return bspWerte;
    }

    /**
     * Liefert eine Beschreibung des Prädikats.
     *
     * @return  Eine Beschreibung des Prädikats.
     */
    public String toString() {

        if (nachObenBeschraenkt) {
            return funktion().toString() + " <= " + grenzWert;
        } else {
            return funktion().toString() + " >= " + grenzWert;
        }
    }
}

