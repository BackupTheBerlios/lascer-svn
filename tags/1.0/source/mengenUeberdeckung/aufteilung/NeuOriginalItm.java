/*
 * Dateiname      : NeuOriginalItm.java
 * Letzte �nderung: 18. Juni 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut f�r Intelligente Systeme Universit�t Stuttgart,
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
 * Repr�sentiert eine Teilmenge, zu der eine andere (die originale) Teilmenge
 * zus�tzlich gespeichert ist und abgerufen werden kann. F�r die neue
 * Teilmenge werden von der originalen Teilmenge entweder die Elemente oder
 * die Kosten �bernommen. Wenn die Kosten �bernommen werden, kann die neue
 * Teilmenge eine andere Gr��e der Gesamtmenge haben.
 *
 * @author  Dietmar Lippold
 */
public class NeuOriginalItm extends IndexTeilmenge implements Cloneable {

    /**
     * Die originale Teilmenge.
     */
    private IndexTeilmenge originalItm;

    /**
     * Erzeugt eine Instanz, bei der die Elemente von der �bergebenen Teilmenge
     * �bernommen und die Kosten f�r die neu erzeugte Teilmenge neu gesetzt
     * werden. Die Menge der Elemente der �bergebenen Teilmenge kann dabei der
     * neuen Teilmenge nur zugewiesen oder sie kann kopiert werden. Wenn sie
     * nur zugewiesen wird, ist eine Ver�nderung der einen Teilmenge mit einer
     * Ver�nderung der anderen verbunden.
     *
     * @param origTeilmenge  Die Teilmenge, die zus�tzlich gespeichert werden
     *                       soll und deren Elemente f�r die neu erzeugte
     *                       Teilmenge �bernommen werden.
     * @param kosten         Die Kosten, die der neu erzeugten Teilmenge
     *                       zugeordnet werden. Dieser Wert darf nicht negativ
     *                       sein.
     * @param cloneElements  Gibt an, ob f�r die Elemente ein neues Objekt
     *                       erzeugt werden soll. Falls nicht, ist eine
     *                       Ver�nderung der Elemente des neuen Objekts mit
     *                       einer Ver�nderung der Elemente des alten Objekts
     *                       verbunden.
     *
     * @throws IllegalArgumentException  Der Wert f�r <CODE>kosten</CODE> ist
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
     * Liefert die originale Teilmenge, die dem Konstruktor �bergeben wurde.
     *
     * @return  Die originale Teilmenge.
     */
    public IndexTeilmenge originalItm() {
        return originalItm;
    }
}

