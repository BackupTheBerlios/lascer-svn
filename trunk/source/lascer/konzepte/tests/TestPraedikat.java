/*
 * Dateiname      : TestPraedikat.java
 * Letzte �nderung: 09. Juni 2006
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
 * Implementiert ein Test-Pr�dikat.
 *
 * @author  Dietmar Lippold
 */
public class TestPraedikat implements Praedikat {

    /**
     * Die Komplexit�t eines Pr�dikats.
     */
    private static final float KOMPLEXITAET = 0.5f;

    /**
     * Die Werte dieses Pr�dikats f�r die positiven Beispiele.
     */
    private boolean[] posBspWerte;

    /**
     * Die Werte dieses Pr�dikats f�r die negativen Beispiele.
     */
    private boolean[] negBspWerte;

    /**
     * Die Bezeichnung dieses Pr�dikats.
     */
    private String bezeichnung;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param posBspWerte  Die Werte, die das Pr�dikat f�r die positiven
     *                     Beispiele liefern soll.
     * @param negBspWerte  Die Werte, die das Pr�dikat f�r die negativen
     *                     Beispiele liefern soll.
     * @param bezeichnung  Die Bezeichnung des Pr�dikats.
     */
    public TestPraedikat(boolean[] posBspWerte, boolean[] negBspWerte,
                         String bezeichnung) {
        this.posBspWerte = posBspWerte;
        this.negBspWerte = negBspWerte;
        this.bezeichnung = bezeichnung;
    }

    /**
     * Liefert die Komplexit�t des Pr�dikats.
     *
     * @return  Die Komplexit�t des Pr�dikats.
     */
    public float komplexitaet() {
        return KOMPLEXITAET;
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

        if (invertiert) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Pr�dikats auf die positiven Beispiel der �bergebenen Beispieldaten
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
     * des Pr�dikats auf die negativen Beispiel der �bergebenen Beispieldaten
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
     * Liefert eine Beschreibung des Pr�dikats.
     *
     * @return  Eine Beschreibung des Pr�dikats.
     */
    public String toString() {
        return bezeichnung;
    }
}

