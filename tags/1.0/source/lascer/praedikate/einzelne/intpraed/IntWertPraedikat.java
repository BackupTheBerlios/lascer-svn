/*
 * Dateiname      : IntWertPraedikat.java
 * Letzte �nderung: 09. September 2006
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


package lascer.praedikate.einzelne.intpraed;

import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.attribute.IntAttribut;
import lascer.praedikate.Praedikat;

/**
 * Definiert ein Pr�dikat, das den Wert eines int-Attributs mit einem
 * vorgegebenen Wert vergleicht.
 *
 * @author  Dietmar Lippold
 */
public class IntWertPraedikat implements Praedikat {

    /**
     * Das Attribut, dessen Wert verglichen wird.
     */
    private IntAttribut testAttribut;

    /**
     * Der Wert, gegen den das Attribut verglichen wird.
     */
    private int testWert;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param attribut  Das Attribut, dessen Wert verglichen werden soll.
     * @param wert      Der Wert, gegen den das Attribut verglichen werden
     *                  soll.
     */
    public IntWertPraedikat(IntAttribut attribut, int wert) {
        this.testAttribut = attribut;
        this.testWert = wert;
    }

    /**
     * Liefert die Komplexit�t des Pr�dikats.
     *
     * @return  Die Komplexit�t des Pr�dikats.
     */
    public float komplexitaet() {
        return Konstanten.INT_WERT_PRAED_KOMPLEX;
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
        int attribWert;

        attribWert = beispiel.getIntWert(testAttribut);
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
     * des Pr�dikats auf die �bergeben Attribut-Werte der Beispiele ergeben.
     *
     * @param attribWerte    Die Attribut-Werte der Beispiele.
     * @param invertiert     Die Angabe, ob die Werte des Pr�dikats invertiert
     *                       werden sollen.
     * @param wertUnbekannt  Die Angabe, ob mindestens einer der
     *                       Attribut-Werte unbekannt ist.
     * @param unbekWert      Der Wert, der einem Beispiel mit unbekanntem Wert
     *                       zugewiesen werden soll.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Pr�dikats auf die Attribut-Werte ergeben, gegebenenfalls
     *          invertiert.
     */
    private boolean[] praedikatWerte(int[] attribWerte, boolean invertiert,
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
     * des Pr�dikats auf die positiven Beispiele der �bergebenen Beispieldaten
     * ergeben.
     *
     * @param beispieldaten  Die Beispieldaten, f�r deren positive Beispiele
     *                       die Werte des Pr�dikats ermittelt werden sollen.
     * @param invertiert     Die Angabe, ob die Werte des Pr�dikats invertiert
     *                       werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Pr�dikats auf die positiven Beispiele ergeben.
     */
    public boolean[] posBspWerte(Beispieldaten beispieldaten, boolean invertiert) {

        return praedikatWerte(beispieldaten.getIntWerte(testAttribut, true),
                              invertiert, beispieldaten.intWertUnbekannt(), false);
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Pr�dikats auf die negativen Beispiele der �bergebenen Beispieldaten
     * ergeben.
     *
     * @param beispieldaten  Die Beispieldaten, f�r deren negativen Beispiele
     *                       die Werte des Pr�dikats ermittelt werden sollen.
     * @param invertiert     Die Angabe, ob die Werte des Pr�dikats invertiert
     *                       werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Pr�dikats auf die negativen Beispiele ergeben.
     */
    public boolean[] negBspWerte(Beispieldaten beispieldaten, boolean invertiert) {

        return praedikatWerte(beispieldaten.getIntWerte(testAttribut, false),
                              invertiert, beispieldaten.intWertUnbekannt(), true);
    }

    /**
     * Liefert eine Beschreibung des Pr�dikats.
     *
     * @return  Eine Beschreibung des Pr�dikats.
     */
    public String toString() {
        return "v(" + testAttribut.name() + ") = " + testWert;
    }
}

