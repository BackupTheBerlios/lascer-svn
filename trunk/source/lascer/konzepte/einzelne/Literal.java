/*
 * Dateiname      : Literal.java
 * Letzte �nderung: 21. September 2006
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


package lascer.konzepte.einzelne;

import java.util.HashSet;
import mathCollection.BitMathIntSet;

import lascer.konzepte.Konzept;
import lascer.praedikate.Praedikat;
import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;

/**
 * Repr�sentiert ein Literal.
 *
 * @author  Dietmar Lippold
 */
public class Literal extends AbstraktesKonzept implements Konzept {

    /**
     * Das dem Literal zugrunde liegende Pr�dikat.
     */
    private transient Praedikat praedikat;

    /**
     * Eine Beschreibung des Literals.
     */
    private String beschreibung;

    /**
     * Die Komplexit�t des Konzepts.
     */
    private float komplexitaet = -1;

    /**
     * Gibt an, ob es sich um ein invertiertes Literal handelt.
     */
    private boolean invertiert;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param praedikat   Das dem Literal zugrunde gelegte Pr�dikat.
     * @param bspDaten    Die dem Konzept zugrunde gelegten Beispieldaten.
     * @param invertiert  Die Angabe, ob das Pr�dikat f�r das Literal
     *                    invertiert werden soll.
     *
     * @throws IllegalArgumentException  Wenn die Anzahl der vom Pr�dikat
     *                                   gelieferten Werte nicht mit der
     *                                   Anzahl der positiven bzw. negativen
     *                                   Beispiele �bereinstimmt.
     */
    public Literal(Praedikat praedikat, Beispieldaten bspDaten, boolean invertiert) {

        super(bspDaten.posBspAnz(), bspDaten.negBspAnz());

        if (Konstanten.LIT_UNI_KOMPLEX) {
            this.komplexitaet = Konstanten.INIT_LIT_UNI_KOMPLEX;
        } else {
            this.komplexitaet = (Konstanten.INIT_LIT_ADD_KOMPLEX
                                 + praedikat.komplexitaet());
        }

        boolean[] posBspWerte = praedikat.posBspWerte(bspDaten, invertiert);
        boolean[] negBspWerte = praedikat.negBspWerte(bspDaten, invertiert);

        if ((posBspWerte.length != bspDaten.posBspAnz())
                || (negBspWerte.length != bspDaten.negBspAnz())) {
            throw new IllegalArgumentException("Anzahl der gelieferten"
                                               + " Werte stimmt nicht");
        }

        // Wegen einer ung�nstigen Implementierung von java.util.BitSet in
        // JDK 1.4 den Wert 64 zum gr��ten Index addieren.
        BitMathIntSet posErfuelltBsp = new BitMathIntSet(posBspWerte.length + 64);
        BitMathIntSet negErfuelltBsp = new BitMathIntSet(negBspWerte.length + 64);

        for (int i = 0; i < posBspWerte.length; i++) {
            if (posBspWerte[i]) {
                posErfuelltBsp.add(i);
            }
        }
        for (int i = 0; i < negBspWerte.length; i++) {
            if (negBspWerte[i]) {
                negErfuelltBsp.add(i);
            }
        }
        setzeErfuellteBsp(posErfuelltBsp, negErfuelltBsp);

        if (invertiert) {
            this.beschreibung = "NOT(" + praedikat.toString() + ")";
            this.komplexitaet += Konstanten.INV_LIT_KOMPLEX;
        } else {
            this.beschreibung = praedikat.toString();
        }

        this.praedikat = praedikat;
        this.invertiert = invertiert;
    }

    /**
     * Liefert den hashCode dieses Objekts.
     *
     * @return  Den hashCode dieses Objekts.
     */
    public int hashCode() {
        return beschreibung.hashCode();
    }

    /**
     * Ermittelt, ob das �bergebene Objekt ein Literal ist und zu
     * <CODE>this</CODE> gleich ist.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel ein zu
     *                       vergleichendes <CODE>Literal</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das �bergebene Objekt ein
     *          <CODE>Literal</CODE> ist und die gleiche Beschreibung besitzt,
     *          anderenfalls <CODE>false</CODE>.
     */
    public boolean equals(Object anderesObjekt) {

        if (anderesObjekt == this) {
            return true;
        }

        if (!(anderesObjekt instanceof Literal)) {
            return false;
        }

        return beschreibung.equals(((Literal) anderesObjekt).beschreibung);
    }

    /**
     * Liefert das dem Literal zugrunde liegende Pr�dikat.
     *
     * @return  Das dem Literal zugrunde liegende Pr�dikat.
     */
    public Praedikat praedikat() {
        return praedikat;
    }

    /**
     * Liefert die Angabe, ob es sich um ein invertiertes Literal handelt.
     *
     * @return  Die Angabe, ob es sich um ein invertiertes Literal handelt.
     */
    public boolean istInvertiert() {
        return invertiert;
    }

    /**
     * Liefert den Wert Null als Anzahl der Konjunktionen des Konzepts.
     *
     * @return  Den Wert Null.
     */
    public int konjunktionsAnz() {
        return 0;
    }

    /**
     * Liefert den Wert Null als Anzahl der Disjunktionen des Konzepts.
     *
     * @return  Den Wert Null.
     */
    public int disjunktionsAnz() {
        return 0;
    }

    /**
     * Liefert den Wert Null als Anzahl einzelnen Literale des Konzepts, die
     * also direkt in diesem Konzept enthalten sind.
     *
     * @return  Den Wert Null.
     */
    public int einzelLiteralAnz() {
        return 0;
    }

    /**
     * Liefert den Wert Eins, wenn das Literal nicht invertiert ist, sonst den
     * Wert Null.
     *
     * @return  Den Wert Eins oder Null, je nachdem, ob das Literal invertiert
     *          ist.
     */
    public int posLiteralAnz() {
        if (invertiert) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Liefert den Wert Eins, wenn das Literal invertiert ist, sonst den Wert
     * Wert Null.
     *
     * @return  Den Wert Null oder Eins, je nachdem, ob das Literal invertiert
     *          ist.
     */
    public int negLiteralAnz() {
        if (invertiert) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Liefert die Komplexit�t des Konzepts.
     *
     * @return  Die Komplexit�t des Konzepts.
     */
    public float komplexitaet() {
        return komplexitaet;
    }

    /**
     * Ermitteln, ob das Konzept auf das �bergebene Beispiel zutrifft bzw. f�r
     * dieses erf�llt ist.
     *
     * @param beispiel  Ein Beispiel, f�r das ermittelt werden soll, ob das
     *                  Konzept darauf zutrifft.
     *
     * @return  Die Angabe, ob das Konzept auf das �bergebene Beispiel
     *          zutrifft.
     */
    public boolean trifftZu(Beispiel beispiel) {
        return praedikat.wert(beispiel, invertiert);
    }

    /**
     * Liefert eine leere Menge der nicht vorhandenen Teilkonzepte dieses
     * Konzepts.
     *
     * @return  Eine leere Menge.
     */
    public HashSet teilkonzepte() {
        return (new HashSet(0));
    }

    /**
     * Liefert eine Beschreibung des Konzepts.
     *
     * @return  Eine Beschreibung des Konzepts.
     */
    public String toString() {
       return beschreibung;
    }
}

