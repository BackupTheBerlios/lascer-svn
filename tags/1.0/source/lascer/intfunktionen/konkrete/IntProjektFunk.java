/*
 * Dateiname      : IntProjektFunk.java
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


package lascer.intfunktionen.konkrete;

import java.util.List;
import java.util.LinkedList;
import java.io.Serializable;

import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.attribute.IntAttribut;
import lascer.intfunktionen.ErzeugbareIntFunk;

/**
 * Eine Funktion, die ein Beispiel auf den Wert eines int-Attributs abbildet.
 *
 * @author  Dietmar Lippold
 */
public class IntProjektFunk implements ErzeugbareIntFunk, Serializable {

    /**
     * Das Attribut, dessen Wert für ein Beispiel die Funktion liefern soll.
     */
    private IntAttribut attribut;

    /**
     * Die Nummer dieser Funktion unter allen Projektions-Funktionen.
     */
    private int nummer;

    /**
     * Erzeugt eine neue Instanz, die zu einem Beispiel den Wert des
     * übergebenen Attributs liefert.
     *
     * @param nummer    Die Nummer dieser Funktion.
     * @param attribut  Das Attribut, dessen Wert für ein Beispiel die
     *                  Funktion liefern soll.
     */
    public IntProjektFunk(int nummer, IntAttribut attribut) {
        this.nummer = nummer;
        this.attribut = attribut;
    }

    /**
     * Gibt an, daß die Funktion immer sinnvoll ist.
     *
     * @return  Den Wert <CODE>true</CODE>.
     */
    public boolean istSinnvoll() {
        return true;
    }

    /**
     * Liefert die Nummer der Funktion unter allen Projektionsfunktionen.
     *
     * @return  Die Nummer der Funktion.
     */
    public int nummer() {
        return nummer;
    }

    /**
     * Liefert die Anzahl der elementaren Funktionen in der Gesamtfunktion.
     *
     * @return  Die Anzahl der elementaren Funktionen.
     */
    public int elementFunkAnz() {
        return 1;
    }

    /**
     * Liefert eine neu erzeugte Liste der von dieser Funktion verwendeten
     * Attribute.
     *
     * @return  Eine neu erzeugte Liste der von dieser Funktion verwendeten
     *          Attribute.
     */
    public List verwendeteAttribute() {
        LinkedList verwendeteAttribute;

        verwendeteAttribute = new LinkedList();
        verwendeteAttribute.add(attribut);
        return verwendeteAttribute;
    }

    /**
     * Liefert die Komplexität der Funktion.
     *
     * @return  Die Komplexität der Funktion.
     */
    public float komplexitaet() {
        return Konstanten.KOMPLEX_PROJEKTION;
    }

    /**
     * Liefert den Wert der Funktion für das übergebene Beispiel.
     *
     * @param beispiel  Ein Beispiel, für das der Wert der Funktion ermittelt
     *                  werden soll.
     *
     * @return  Den Wert der Funktion für das übergebene Beispiel.
     */
    public int wert(Beispiel beispiel) {
        return beispiel.getIntWert(attribut);
    }

    /**
     * Liefert ein Array mit den Werten der Funktion für die positiven
     * Beispiele der übergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, für deren positive Beispiele
     *                       die Werte der Funktion ermittelt werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          der Funktion auf die positiven Beispiele ergeben.
     */
    public int[] posBspWerte(Beispieldaten beispieldaten) {
        return beispieldaten.getIntWerte(attribut, true);
    }

    /**
     * Liefert ein Array mit den Werten der Funktion für die negativen
     * Beispiele der übergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, für deren negativen Beispiele
     *                       die Werte der Funktion ermittelt werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          der Funktion auf die negativen Beispiele ergeben.
     */
    public int[] negBspWerte(Beispieldaten beispieldaten) {
        return beispieldaten.getIntWerte(attribut, false);
    }

    /**
     * Liefert eine Beschreibung der Funktion.
     *
     * @return  Eine Beschreibung der Funktion.
     */
    public String toString() {
        return ("v(" + attribut.name() + ")");
    }
}

