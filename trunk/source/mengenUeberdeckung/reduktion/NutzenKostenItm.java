/*
 * Dateiname      : NutzenKostenItm.java
 * Letzte Änderung: 08. Juli 2005
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2005
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


package mengenUeberdeckung.reduktion;

import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Repräsentiert ein Tripel aus einer Teilmenge, einem Nutzenwert und einem
 * Kostenwert. Im Konstruktor kann die Sortierreihenfolge festgelegt werden.
 *
 * @author  Dietmar Lippold
 */
public class NutzenKostenItm implements Comparable {

    /**
     * Liefert die Teilmenge des Tripels.
     */
    private IndexTeilmenge teilmenge;

    /**
     * Liefert den Nutzenwert des Tripels.
     */
    private float nutzen;

    /**
     * Liefert den Kostenwert des Tripels.
     */
    private float kosten;

    /**
     * Gibt an, ob die Tripel in einer <CODE>SortedSet</CODE> aufsteigend
     * oder absteigend bezüglich der Kostenwerte sortiert werden sollen.
     */
    private boolean aufsteigend;

    /**
     * Erzeugt eine Instanz zu den übergebenen Daten mit der angegebenen
     * Sortierreihenfolge.
     *
     * @param teilmenge    Die zu verwaltende Teilmenge.
     * @param nutzen       Der zu verwaltende Nutzenwert.
     * @param kosten       Der zu verwaltende Kostenwert.
     * @param aufsteigend  Die Angabe, ob die Instanzen in einem
     *                     <CODE>SortedSet</CODE> aufsteigend bezüglich der
     *                     Kostenwerte sortiert werden sollen. Falls nicht,
     *                     werden sie absteigend sortiert.
     */
    public NutzenKostenItm(IndexTeilmenge teilmenge, float nutzen, float kosten,
                           boolean aufsteigend) {
        this.teilmenge = teilmenge;
        this.nutzen = nutzen;
        this.kosten = kosten;
        this.aufsteigend = aufsteigend;
    }

    /**
     * Weißt der Instanz neue Werte für Nutzen und Kosten zu.
     *
     * @param neuerNutzen  Der neue Nutzenwert.
     * @param neueKosten   Der neue Kostenwert.
     */
    public void setzeNutzenKosten(float neuerNutzen, float neueKosten) {
        this.nutzen = neuerNutzen;
        this.kosten = neueKosten;
    }

    /**
     * Liefert die Teilmenge des Tripels.
     *
     * @return  Die Teilmenge des Tripels.
     */
    public IndexTeilmenge teilmenge() {
        return teilmenge;
    }

    /**
     * Liefert den Nutzenwert des Tripels.
     *
     * @return  Den Nutzenwert des Tripels.
     */
    public float nutzen() {
        return nutzen;
    }

    /**
     * Liefert den Kostenwert des Tripels.
     *
     * @return  Den Kostenwert des Tripels.
     */
    public float kosten() {
        return kosten;
    }

    /**
     * Vergleicht das übergebene Objekt mit diesem Objekt anhand des
     * Kostenwerts in Bezug auf die im Konstruktor vorgegebene Reihenfolge.
     *
     * @param o  Das Objekt, das mit diesem Objekt verglichen werden soll.
     *
     * @return  Den Wert 0, wenn das übergebene Objekt gleich diesem Objekt
     *          ist, den Wert -1 wenn dieses Objekt in Bezug auf die
     *          vorgegebene Reihenfolge kleiner ist als das übergebene Objekt
     *          und den Wert 1 wenn es größer ist.
     */
    public int compareTo(Object o) {
        NutzenKostenItm anderesTripel = (NutzenKostenItm) o;

        if (anderesTripel == this) {
            return 0;
        } else if (aufsteigend) {
            if ((kosten < anderesTripel.kosten)
                || (kosten == anderesTripel.kosten)
                   && (teilmenge.hashCode() < anderesTripel.teilmenge.hashCode())) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if ((kosten > anderesTripel.kosten)
                || (kosten == anderesTripel.kosten)
                   && (teilmenge.hashCode() > anderesTripel.teilmenge.hashCode())) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /**
     * Liefert die Daten des Tripels als String.
     *
     * @return  Einen String, der <CODE>this</CODE> beschreibt.
     */
    public String toString() {
        StringBuffer ausgabe = new StringBuffer();

        ausgabe.append(teilmenge.toString() + "\n");
        ausgabe.append("Relativer Nutzer = " + nutzen + "\n");
        ausgabe.append("Relative Kosten  = " + kosten + "\n");
        return ausgabe.toString();
    }
}

