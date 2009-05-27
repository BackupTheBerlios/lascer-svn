/*
 * Dateiname      : NeuOriginalItm.java
 * Letzte Änderung: 18. Juni 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2006
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


package mengenUeberdeckung.aufteilung;

import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Repräsentiert eine Teilmenge, zu der eine andere (die originale) Teilmenge
 * zusätzlich gespeichert ist und abgerufen werden kann. Für die neue
 * Teilmenge werden von der originalen Teilmenge entweder die Elemente oder
 * die Kosten übernommen. Wenn die Kosten übernommen werden, kann die neue
 * Teilmenge eine andere Größe der Gesamtmenge haben.
 *
 * @author  Dietmar Lippold
 */
public class NeuOriginalItm extends IndexTeilmenge implements Cloneable {

    /**
     * Die originale Teilmenge.
     */
    private IndexTeilmenge originalItm;

    /**
     * Erzeugt eine Instanz, bei der die Elemente von der übergebenen Teilmenge
     * übernommen und die Kosten für die neu erzeugte Teilmenge neu gesetzt
     * werden. Die Menge der Elemente der übergebenen Teilmenge kann dabei der
     * neuen Teilmenge nur zugewiesen oder sie kann kopiert werden. Wenn sie
     * nur zugewiesen wird, ist eine Veränderung der einen Teilmenge mit einer
     * Veränderung der anderen verbunden.
     *
     * @param origTeilmenge  Die Teilmenge, die zusätzlich gespeichert werden
     *                       soll und deren Elemente für die neu erzeugte
     *                       Teilmenge übernommen werden.
     * @param kosten         Die Kosten, die der neu erzeugten Teilmenge
     *                       zugeordnet werden. Dieser Wert darf nicht negativ
     *                       sein.
     * @param cloneElements  Gibt an, ob für die Elemente ein neues Objekt
     *                       erzeugt werden soll. Falls nicht, ist eine
     *                       Veränderung der Elemente des neuen Objekts mit
     *                       einer Veränderung der Elemente des alten Objekts
     *                       verbunden.
     *
     * @throws IllegalArgumentException  Der Wert für <CODE>kosten</CODE> ist
     *                                   negativ.
     */
    public NeuOriginalItm(IndexTeilmenge origTeilmenge, float kosten,
                          boolean cloneElements) {
        super(origTeilmenge, kosten, cloneElements);
        this.originalItm = origTeilmenge;
    }

    /**
     * Liefert ein neue Instanz dieser Klasse, die zu <CODE>this</CODE> gleich
     * ist. Die Original-Teilmenge des neuen und des alten Objekts sind dabei
     * identisch, d.h. diese wird nicht kopiert.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return new NeuOriginalItm(originalItm, kosten(), true);
    }

    /**
     * Liefert die originale Teilmenge, die dem Konstruktor übergeben wurde.
     *
     * @return  Die originale Teilmenge.
     */
    public IndexTeilmenge originalItm() {
        return originalItm;
    }
}

