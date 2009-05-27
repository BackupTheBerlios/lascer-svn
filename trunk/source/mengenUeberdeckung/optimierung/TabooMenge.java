/*
 * Dateiname      : TabooMenge.java
 * Letzte Änderung: 13. März 2007
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2007
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


package mengenUeberdeckung.optimierung;

import java.util.LinkedList;
import java.util.HashSet;

/**
 * Eine Menge beschränkter Größe, aus der bei Überfüllung das jeweils älteste
 * Element entfernt wird.
 *
 * @author  Dietmar Lippold
 */
public class TabooMenge {

    /**
     * Eine Liste der Elemente dieser Menge.
     */
    private LinkedList elementListe;

    /**
     * Eine Menge der Elemente dieser Menge.
     */
    private HashSet elementMenge;

    /**
     * Die maximale Anzahl der Elemente dieser Menge.
     */
    private int maxElementAnz;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param maxElementAnz  Die maximale Anzahl der Elemente, die in dieser
     *                       Menge enthalten sein soll.
     */
    public TabooMenge(int maxElementAnz) {

        this.maxElementAnz = maxElementAnz;

        elementListe = new LinkedList();
        elementMenge = new HashSet();
    }

    /**
     * Nimmt ein Element in diese Menge auf.
     *
     * @param element  Das aufzunehmende Element.
     *
     * @return  Die Angabe, ob bei der Aufnahme des Elements ein vorhandenes
     *          Element entfernt wurde.
     */
    public boolean add(Object element) {

        elementListe.add(element);
        elementMenge.add(element);

        if (elementMenge.size() > maxElementAnz) {
            elementMenge.remove(elementListe.removeFirst());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Ermittelt, ob das übergebene Element in dieser Menge enthalten ist.
     *
     * @param element  Das Element, von dem ermittelt werden soll, ob es in
     *                 dieser Menge enthalten ist.
     *
     * @return  Die Angabe, ob das übergebene Element in dieser Menge
     *          enthalten ist.
     */
    public boolean contains(Object element) {
        return elementMenge.contains(element);
    }

    /**
     * Liefert die Anzahl der in dieser Menge enthaltenen Elemente.
     *
     * @return  Die Anzahl der in dieser Menge enthaltenen Elemente.
     */
    public int size() {
        return elementMenge.size();
    }
}

