/*
 * Dateiname      : RealWertPraedikat.java
 * Letzte Änderung: 09. September 2006
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
import lascer.problemdaten.attribute.RealAttribut;
import lascer.praedikate.Praedikat;

/**
 * Definiert ein Prädikat, das den Wert eines real-Attributs mit einem
 * vorgegebenen Wert vergleicht.
 *
 * @author  Dietmar Lippold
 */
public class RealWertPraedikat implements Praedikat {

    /**
     * Das Attribut, dessen Wert verglichen wird.
     */
    private RealAttribut testAttribut;

    /**
     * Der Wert, gegen den das Attribut verglichen wird.
     */
    private float testWert;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param attribut  Das Attribut, dessen Wert verglichen werden soll.
     * @param wert      Der Wert, gegen den das Attribut verglichen werden
     *                  soll.
     */
    public RealWertPraedikat(RealAttribut attribut, float wert) {
        this.testAttribut = attribut;
        this.testWert = wert;
    }

    /**
     * Liefert die Komplexität des Prädikats.
     *
     * @return  Die Komplexität des Prädikats.
     */
    public float komplexitaet() {
        return Konstanten.REAL_WERT_PRAED_KOMPLEX;
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
        float attribWert;

        attribWert = beispiel.getRealWert(testAttribut);
        if (attribWert == Konstanten.UNBEKANNT_WERT) {
            return false;
        } else {
            if (invertiert) {
                return (attribWert != testWert);
            } else {
                return (attribWert == testWert);
            }
        }
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Prädikats auf die übergeben Attribut-Werte der Beispiele ergeben.
     *
     * @param attribWerte    Die Attribut-Werte der Beispiele.
     * @param invertiert     Die Angabe, ob die Werte des Prädikats invertiert
     *                       werden sollen.
     * @param wertUnbekannt  Die Angabe, ob mindestens einer der
     *                       Attribut-Werte unbekannt ist.
     * @param unbekWert      Der Wert, der einem Beispiel mit unbekanntem Wert
     *                       zugewiesen werden soll.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Prädikats auf die Attribut-Werte ergeben, gegebenenfalls
     *          invertiert.
     */
    private boolean[] praedikatWerte(float[] attribWerte, boolean invertiert,
                                     boolean wertUnbekannt, boolean unbekWert) {
        boolean[] praedWerte;

        praedWerte = new boolean[attribWerte.length];
        for (int bspNr = 0; bspNr < attribWerte.length; bspNr++) {
            if (wertUnbekannt
                    && (attribWerte[bspNr] == Konstanten.UNBEKANNT_WERT)) {
                praedWerte[bspNr] = unbekWert;
            } else if (invertiert) {
                praedWerte[bspNr] = (attribWerte[bspNr] != testWert);
            } else {
                praedWerte[bspNr] = (attribWerte[bspNr] == testWert);
            }
        }
        return praedWerte;
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Prädikats auf die positiven Beispiele der übergebenen Beispieldaten
     * ergeben.
     *
     * @param beispieldaten  Die Beispieldaten, für deren positive Beispiele
     *                       die Werte des Prädikats ermittelt werden sollen.
     * @param invertiert     Die Angabe, ob die Werte des Prädikats invertiert
     *                       werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Prädikats auf die positiven Beispiele ergeben.
     */
    public boolean[] posBspWerte(Beispieldaten beispieldaten, boolean invertiert) {

        return praedikatWerte(beispieldaten.getRealWerte(testAttribut, true),
                              invertiert, beispieldaten.realWertUnbekannt(), false);
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Prädikats auf die negativen Beispiele der übergebenen Beispieldaten
     * ergeben.
     *
     * @param beispieldaten  Die Beispieldaten, für deren negativen Beispiele
     *                       die Werte des Prädikats ermittelt werden sollen.
     * @param invertiert     Die Angabe, ob die Werte des Prädikats invertiert
     *                       werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Prädikats auf die negativen Beispiele ergeben.
     */
    public boolean[] negBspWerte(Beispieldaten beispieldaten, boolean invertiert) {

        return praedikatWerte(beispieldaten.getRealWerte(testAttribut, false),
                              invertiert, beispieldaten.realWertUnbekannt(), true);
    }

    /**
     * Liefert eine Beschreibung des Prädikats.
     *
     * @return  Eine Beschreibung des Prädikats.
     */
    public String toString() {
        return "v(" + testAttribut.name() + ") = " + testWert;
    }
}

