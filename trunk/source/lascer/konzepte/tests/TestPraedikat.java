/*
 * Dateiname      : TestPraedikat.java
 * Letzte Änderung: 09. Juni 2006
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


package lascer.konzepte.tests;

import lascer.praedikate.Praedikat;
import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;

/**
 * Implementiert ein Test-Prädikat.
 *
 * @author  Dietmar Lippold
 */
public class TestPraedikat implements Praedikat {

    /**
     * Die Komplexität eines Prädikats.
     */
    private static final float KOMPLEXITAET = 0.5f;

    /**
     * Die Werte dieses Prädikats für die positiven Beispiele.
     */
    private boolean[] posBspWerte;

    /**
     * Die Werte dieses Prädikats für die negativen Beispiele.
     */
    private boolean[] negBspWerte;

    /**
     * Die Bezeichnung dieses Prädikats.
     */
    private String bezeichnung;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param posBspWerte  Die Werte, die das Prädikat für die positiven
     *                     Beispiele liefern soll.
     * @param negBspWerte  Die Werte, die das Prädikat für die negativen
     *                     Beispiele liefern soll.
     * @param bezeichnung  Die Bezeichnung des Prädikats.
     */
    public TestPraedikat(boolean[] posBspWerte, boolean[] negBspWerte,
                         String bezeichnung) {
        this.posBspWerte = posBspWerte;
        this.negBspWerte = negBspWerte;
        this.bezeichnung = bezeichnung;
    }

    /**
     * Liefert die Komplexität des Prädikats.
     *
     * @return  Die Komplexität des Prädikats.
     */
    public float komplexitaet() {
        return KOMPLEXITAET;
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

        if (invertiert) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Prädikats auf die positiven Beispiel der übergebenen Beispieldaten
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
        boolean[] rueckgabe;

        rueckgabe = (boolean[]) posBspWerte.clone();
        if (invertiert) {
            for (int i = 0; i < rueckgabe.length; i++) {
                rueckgabe[i] = !rueckgabe[i];
            }
        }

        return rueckgabe;
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Prädikats auf die negativen Beispiel der übergebenen Beispieldaten
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
        boolean[] rueckgabe;

        rueckgabe = (boolean[]) negBspWerte.clone();
        if (invertiert) {
            for (int i = 0; i < rueckgabe.length; i++) {
                rueckgabe[i] = !rueckgabe[i];
            }
        }

        return rueckgabe;
    }

    /**
     * Liefert eine Beschreibung des Prädikats.
     *
     * @return  Eine Beschreibung des Prädikats.
     */
    public String toString() {
        return bezeichnung;
    }
}

