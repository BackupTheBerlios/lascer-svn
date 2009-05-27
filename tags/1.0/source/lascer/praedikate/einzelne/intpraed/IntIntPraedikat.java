/*
 * Dateiname      : IntIntPraedikat.java
 * Letzte �nderung: 12. September 2006
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
 * Definiert ein Pr�dikat, das die ganzzahligen Werte zweier Attribute
 * miteinander vergleicht.
 *
 * @author  Dietmar Lippold
 */
public abstract class IntIntPraedikat implements Praedikat {

    /**
     * Das erste Attribut, dessen Wert verglichen wird.
     */
    private IntAttribut testAttribut1;

    /**
     * Das zweite Attribut, dessen Wert verglichen wird.
     */
    private IntAttribut testAttribut2;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param attribut1  Das erste Attribut, dessen Wert verglichen werden
     *                   soll.
     * @param attribut2  Das zweite Attribut, dessen Wert verglichen werden
     *                   soll.
     */
    public IntIntPraedikat(IntAttribut attribut1, IntAttribut attribut2) {
        this.testAttribut1 = attribut1;
        this.testAttribut2 = attribut2;
    }

    /**
     * Ermittelt, ob das Pr�dikat f�r die beiden �bergebenen Werte erf�llt
     * ist.
     *
     * @param wert1  Der erste Wert, der verglichen werden soll.
     * @param wert2  Der zweite Wert, der verglichen werden soll.
     *
     * @return  Die Angabe, ob das Pr�dikat f�r die beiden �bergebenen Werte
     *          erf�llt ist.
     */
    protected abstract boolean erfuellt(int wert1, int wert2);

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
        int wert1, wert2;

        wert1 = beispiel.getIntWert(testAttribut1);
        wert2 = beispiel.getIntWert(testAttribut2);

        if ((wert1 == Konstanten.UNBEKANNT_WERT)
                || (wert2 == Konstanten.UNBEKANNT_WERT)) {
            return false;
        } else {
            if (invertiert) {
                return !erfuellt(wert1, wert2);
            } else {
                return erfuellt(wert1, wert2);
            }
        }
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Pr�dikats auf die �bergeben Attribut-Werte der Beispiele ergeben.
     *
     * @param attribWerte1   Die einen Attribut-Werte der Beispiele.
     * @param attribWerte2   Die anderen Attribut-Werte der Beispiele.
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
    private boolean[] praedikatWerte(int[] attribWerte1, int[] attribWerte2,
                                     boolean invertiert, boolean wertUnbekannt,
                                     boolean unbekWert) {
        boolean[] praedWerte;

        praedWerte = new boolean[attribWerte1.length];
        for (int bspNr = 0; bspNr < attribWerte1.length; bspNr++) {
            if (wertUnbekannt
                && ((attribWerte1[bspNr] == Konstanten.UNBEKANNT_WERT)
                    || (attribWerte2[bspNr] == Konstanten.UNBEKANNT_WERT))) {
                praedWerte[bspNr] = unbekWert;
            } else if (invertiert) {
                praedWerte[bspNr] = !erfuellt(attribWerte1[bspNr],
                                              attribWerte2[bspNr]);
            } else {
                praedWerte[bspNr] = erfuellt(attribWerte1[bspNr],
                                             attribWerte2[bspNr]);
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
        int[] attribWerte1;
        int[] attribWerte2;

        attribWerte1 = beispieldaten.getIntWerte(testAttribut1, true);
        attribWerte2 = beispieldaten.getIntWerte(testAttribut2, true);

        return praedikatWerte(attribWerte1, attribWerte2, invertiert,
                              beispieldaten.intWertUnbekannt(), false);
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
        int[] attribWerte1;
        int[] attribWerte2;

        attribWerte1 = beispieldaten.getIntWerte(testAttribut1, false);
        attribWerte2 = beispieldaten.getIntWerte(testAttribut2, false);

        return praedikatWerte(attribWerte1, attribWerte2, invertiert,
                              beispieldaten.intWertUnbekannt(), true);
    }

    /**
     * Liefert eine Beschreibung der zum Vergleich benutzten Relation des
     * Pr�dikats.
     *
     * @return  Eine Beschreibung der zum Vergleich benutzten Relation des
     *          Pr�dikats.
     */
    protected abstract String relation();

    /**
     * Liefert eine Beschreibung des Pr�dikats.
     *
     * @return  Eine Beschreibung des Pr�dikats.
     */
    public String toString() {
        return ("v(" + testAttribut1.name() + ") " + relation()
                + " v(" + testAttribut2.name() + ")");
    }
}

