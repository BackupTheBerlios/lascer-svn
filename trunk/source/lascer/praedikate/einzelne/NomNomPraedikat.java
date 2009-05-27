/*
 * Dateiname      : NomNomPraedikat.java
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


package lascer.praedikate.einzelne;

import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.attribute.NominalAttribut;
import lascer.praedikate.Praedikat;

/**
 * Definiert ein Prädikat, das die Werte zweier nominaler Attribute
 * miteinander vergleicht.
 *
 * @author  Dietmar Lippold
 */
public class NomNomPraedikat implements Praedikat {

    /**
     * Das erste Attribut, dessen Wert verglichen wird.
     */
    private NominalAttribut testAttribut1;

    /**
     * Das zweite Attribut, dessen Wert verglichen wird.
     */
    private NominalAttribut testAttribut2;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param attribut1  Das erste Attribut, dessen Wert verglichen werden
     *                   soll.
     * @param attribut2  Das zweite Attribut, dessen Wert verglichen werden
     *                   soll.
     */
    public NomNomPraedikat(NominalAttribut attribut1, NominalAttribut attribut2) {
        this.testAttribut1 = attribut1;
        this.testAttribut2 = attribut2;
    }

    /**
     * Liefert die Komplexität des Prädikats.
     *
     * @return  Die Komplexität des Prädikats.
     */
    public float komplexitaet() {
        return Konstanten.NOM_NOM_PRAED_KOMPLEX;
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
        String wert1, wert2;

        wert1 = beispiel.getNominalWert(testAttribut1);
        wert2 = beispiel.getNominalWert(testAttribut2);

        if (wert1.equals(Konstanten.UNBEKANNT_NOMINAL)
                || wert2.equals(Konstanten.UNBEKANNT_NOMINAL)) {
            return false;
        } else if (invertiert) {
            return !wert1.equals(wert2);
        } else {
            return wert1.equals(wert2);
        }
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Prädikats auf die übergeben Attribut-Werte der Beispiele ergeben.
     *
     * @param attribWerte1   Die einen Attribut-Werte der Beispiele.
     * @param attribWerte2   Die anderen Attribut-Werte der Beispiele.
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
    private boolean[] praedikatWerte(String[] attribWerte1,
                                     String[] attribWerte2,
                                     boolean invertiert,
                                     boolean wertUnbekannt, boolean unbekWert) {
        boolean[] praedWerte;

        praedWerte = new boolean[attribWerte1.length];
        for (int bspNr = 0; bspNr < attribWerte1.length; bspNr++) {
            if (wertUnbekannt
                && (attribWerte1[bspNr].equals(Konstanten.UNBEKANNT_NOMINAL)
                    || attribWerte2[bspNr].equals(Konstanten.UNBEKANNT_NOMINAL))) {
                praedWerte[bspNr] = unbekWert;
            } else if (invertiert) {
                praedWerte[bspNr] = !attribWerte1[bspNr].equals(attribWerte2[bspNr]);
            } else {
                praedWerte[bspNr] = attribWerte1[bspNr].equals(attribWerte2[bspNr]);
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
        String[]  attribWerte1;
        String[]  attribWerte2;
        boolean[] bspWerte;

        attribWerte1 = beispieldaten.getNominalWerte(testAttribut1, true);
        attribWerte2 = beispieldaten.getNominalWerte(testAttribut2, true);

        return praedikatWerte(attribWerte1, attribWerte2, invertiert,
                              beispieldaten.nominalWertUnbekannt(), false);
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
        String[]  attribWerte1;
        String[]  attribWerte2;
        boolean[] bspWerte;

        attribWerte1 = beispieldaten.getNominalWerte(testAttribut1, false);
        attribWerte2 = beispieldaten.getNominalWerte(testAttribut2, false);

        return praedikatWerte(attribWerte1, attribWerte2, invertiert,
                              beispieldaten.nominalWertUnbekannt(), true);
    }

    /**
     * Liefert eine Beschreibung des Prädikats.
     *
     * @return  Eine Beschreibung des Prädikats.
     */
    public String toString() {
        return ("v(" + testAttribut1.name() + ") = "
                + "v(" + testAttribut2.name() + ")");
    }
}

