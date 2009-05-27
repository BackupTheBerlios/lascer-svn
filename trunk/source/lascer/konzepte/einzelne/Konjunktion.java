/*
 * Dateiname      : Konjunktion.java
 * Letzte Änderung: 21. September 2006
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

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;

import mathCollection.BitMathIntSet;

import lascer.konzepte.Konzept;
import lascer.konzepte.KombiKonzept;
import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;

/**
 * Repräsentiert eine Konjunktion, also eine konjunktiv verknüpfte Menge von
 * Konzepten.
 *
 * @author  Dietmar Lippold
 */
public class Konjunktion extends AbstraktesKonzept implements KombiKonzept {

    /**
     * Die in der Konjunktion enthaltenen Teilkonzepte.
     */
    private HashSet konzepte = new HashSet();

    /**
     * Die Komplexität des Konzepts.
     */
    private float komplexitaet = -1;

    /**
     * Erzeugt eine neue Instanz, bei der die Attributen nur die default-Werte
     * besitzen.
     *
     * @param posGesamtAnz  Die Anzahl der insgesamt vorhandenen positiven
     *                      Beispiele.
     * @param negGesamtAnz  Die Anzahl der insgesamt vorhandenen negativen
     *                      Beispiele.
     */
    private Konjunktion(int posGesamtAnz, int negGesamtAnz) {
        super(posGesamtAnz, negGesamtAnz);
    }

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param bspDaten  Die dem Konzept zugrunde gelegten Beispieldaten.
     */
    public Konjunktion(Beispieldaten bspDaten) {

        super(bspDaten.posBspAnz(), bspDaten.negBspAnz());

        // Die initiale Konjunktion umfaßt alle Beispiele.
        setzeErfuellteBsp(posBspInvertiert(bspDaten.posBspAnz(), new BitMathIntSet()),
                          negBspInvertiert(bspDaten.negBspAnz(), new BitMathIntSet()));
        komplexitaet = Konstanten.INIT_KON_KOMPLEX;
    }

    /**
     * Liefert eine flache Kopie dieser Konjunktion.
     *
     * @return  Eine flache Kopie dieser Konjunktion.
     */
    public Object clone() {
        Konjunktion kopie;

        kopie = new Konjunktion(posGesamtAnz(), negGesamtAnz());
        kopie.setzeErfuellteBsp((BitMathIntSet) posErfuelltBsp().clone(),
                                (BitMathIntSet) negErfuelltBsp().clone());
        kopie.konzepte.addAll(konzepte);
        kopie.komplexitaet = komplexitaet;

        return kopie;
    }

    /**
     * Liefert den hashCode dieses Objekts.
     *
     * @return  Den hashCode dieses Objekts.
     */
    public int hashCode() {
        return konzepte.hashCode();
    }

    /**
     * Ermittelt, ob das übergebene Objekt eine Konjunktion ist und zu
     * <CODE>this</CODE> gleich ist.
     *
     * @param anderesObjekt  Ein anderes Objekt, in der Regel eine zu
     *                       vergleichende <CODE>Konjunktion</CODE>.
     *
     * @return  <CODE>true</CODE>, wenn das übergebene Objekt eine
     *          <CODE>Konjunktion</CODE> ist und die gleichen Teilkonzepte
     *          besitzt, anderenfalls <CODE>false</CODE>.
     */
    public boolean equals(Object anderesObjekt) {

        if (anderesObjekt == this) {
            return true;
        }

        if (!(anderesObjekt instanceof Konjunktion)) {
            return false;
        }

        Konjunktion andereKonjunktion = (Konjunktion) anderesObjekt;
        if (!posErfuelltBsp().equals(andereKonjunktion.posErfuelltBsp())
            || !negErfuelltBsp().equals(andereKonjunktion.negErfuelltBsp())) {

            return false;
        }

        return konzepte.equals(andereKonjunktion.konzepte);
    }

    /**
     * Nimmt das übergebene Konzept in die Konjunktion auf. Wenn es schon
     * enthalten ist, ändert sich nichts.
     *
     * @param konzept  Das in die Konjunktion aufzunehmende Konzept.
     *
     * @throws IllegalArgumentException  Wenn die Anzahl der Beispiele, die
     *                                   dem übergebenen Konzept und die
     *                                   diesem Konzept zugrunde liegen,
     *                                   nicht übereinstimmt.
     */
    public void aufnehmen(Konzept konzept) {

        if ((konzept.posGesamtAnz() != posGesamtAnz())
            || (konzept.negGesamtAnz() != negGesamtAnz())) {

            throw new IllegalArgumentException("Die Anzahl der zugrunde liegenden"
                                               + " Beispiele stimmt nicht überein");
        }

        if (!konzepte.contains(konzept)) {
            setzeErfuellteBsp(posErfuelltBsp().intersection(konzept.posErfuelltBsp()),
                              negErfuelltBsp().intersection(konzept.negErfuelltBsp()));
            komplexitaet += konzept.komplexitaet();
            konzepte.add(konzept);
        }
    }

    /**
     * Nimmt die Konzepte der übergebene Collection in die Konjunktion auf,
     * soweit sie noch nicht enthalten sind.
     *
     * @param konzepte  Eine Collection mit Konzepten, die in die Konjunktion
     *                  aufgenommen werden sollen.
     *
     * @throws IllegalArgumentException  Wenn die Anzahl der Beispiele, die
     *                                   den übergebenen Konzepten und die
     *                                   diesem Konzept zugrunde liegen,
     *                                   nicht übereinstimmt.
     */
    public void aufnehmen(Collection konzepte) {

        Iterator konzeptIter = konzepte.iterator();
        while (konzeptIter.hasNext()) {
            aufnehmen((Konzept) konzeptIter.next());
        }
    }

    /**
     * Entfernt das übergebene Konzept als Teilkonzept aus der Konjunktion.
     * Wenn es nicht enthalten ist, ändert sich nichts.
     *
     * @param konzept  Das aus der Konjunktion zu entfernende Konzept.
     *
     * @throws IllegalArgumentException  Wenn die Anzahl der Beispiele, die
     *                                   dem übergebenen Konzept und die
     *                                   diesem Konzept zugrunde liegen,
     *                                   nicht übereinstimmt.
     */
    public void entfernen(Konzept konzept) {
        BitMathIntSet posErfuelltBsp, negErfuelltBsp;
        Konzept       naechstesKonzept;
        Iterator      konzIter;

        if ((konzept.posGesamtAnz() != posGesamtAnz())
            || (konzept.negGesamtAnz() != negGesamtAnz())) {

            throw new IllegalArgumentException("Die Anzahl der zugrunde liegenden"
                                               + " Beispiele stimmt nicht überein");
        }

        konzepte.remove(konzept);

        posErfuelltBsp = posBspInvertiert(posGesamtAnz(), new BitMathIntSet());
        negErfuelltBsp = negBspInvertiert(negGesamtAnz(), new BitMathIntSet());
        komplexitaet = Konstanten.INIT_KON_KOMPLEX;

        konzIter = konzepte.iterator();
        while (konzIter.hasNext()) {
            naechstesKonzept = (Konzept) konzIter.next();
            posErfuelltBsp = posErfuelltBsp.intersection(naechstesKonzept.posErfuelltBsp());
            negErfuelltBsp = negErfuelltBsp.intersection(naechstesKonzept.negErfuelltBsp());
            komplexitaet += naechstesKonzept.komplexitaet();
        }
        setzeErfuellteBsp(posErfuelltBsp, negErfuelltBsp);
    }

    /**
     * Liefert den Wert Eins als Anzahl der Konjunktionen des Konzepts.
     *
     * @return  Den Wert Eins.
     */
    public int konjunktionsAnz() {
        return 1;
    }

    /**
     * Liefert die Anzahl der Disjunktionen des Konzepts.
     *
     * @return  Die Anzahl der Disjunktionen des Konzepts.
     */
    public int disjunktionsAnz() {
        Iterator konzIter;
        int      anzahl;

        anzahl = 0;
        konzIter = konzepte.iterator();
        while (konzIter.hasNext()) {
            Konzept naechstesKonzept = (Konzept) konzIter.next();
            anzahl += naechstesKonzept.disjunktionsAnz();
        }
        return anzahl;
    }

    /**
     * Liefert die Anzahl der einzelnen Literale des Konzepts, die also direkt
     * in diesem Konzept enthalten sind.
     *
     * @return  Die Anzahl der einzelnen Literale des Konzepts, die also
     *          direkt in diesem Konzept enthalten sind.
     */
    public int einzelLiteralAnz() {
        Iterator konzIter;
        int      anzahl;

        anzahl = 0;
        konzIter = konzepte.iterator();
        while (konzIter.hasNext()) {
            Konzept naechstesKonzept = (Konzept) konzIter.next();
            if (naechstesKonzept instanceof Literal) {
                anzahl++;
            }
        }
        return anzahl;
    }

    /**
     * Liefert die Anzahl der positiven Literale des Konzepts insgesamt.
     *
     * @return  Die Anzahl der positiven Literale des Konzepts insgesamt.
     */
    public int posLiteralAnz() {
        Iterator konzIter;
        int      anzahl;

        anzahl = 0;
        konzIter = konzepte.iterator();
        while (konzIter.hasNext()) {
            Konzept naechstesKonzept = (Konzept) konzIter.next();
            anzahl += naechstesKonzept.posLiteralAnz();
        }
        return anzahl;
    }

    /**
     * Liefert die Anzahl der negativen (invertierten) Literale des Konzepts
     * insgesamt.
     *
     * @return  Die Anzahl der negativen (invertierten) Literale des Konzepts
     *          insgesamt.
     */
    public int negLiteralAnz() {
        Iterator konzIter;
        int      anzahl;

        anzahl = 0;
        konzIter = konzepte.iterator();
        while (konzIter.hasNext()) {
            Konzept naechstesKonzept = (Konzept) konzIter.next();
            anzahl += naechstesKonzept.negLiteralAnz();
        }
        return anzahl;
    }

    /**
     * Liefert die Komplexität des Konzepts.
     *
     * @return  Die Komplexität des Konzepts.
     */
    public float komplexitaet() {
        return komplexitaet;
    }

    /**
     * Ermitteln, ob das Konzept auf das übergebene Beispiel zutrifft bzw. für
     * dieses erfüllt ist.
     *
     * @param beispiel  Ein Beispiel, für das ermittelt werden soll, ob das
     *                  Konzept darauf zutrifft.
     *
     * @return  Die Angabe, ob das Konzept auf das übergebene Beispiel
     *          zutrifft.
     */
    public boolean trifftZu(Beispiel beispiel) {
        Iterator konzIter;
        boolean  trifftZu;

        trifftZu = true;
        konzIter = konzepte.iterator();
        while (trifftZu && konzIter.hasNext()) {
            Konzept naechstesKonzept = (Konzept) konzIter.next();
            trifftZu = naechstesKonzept.trifftZu(beispiel);
        }
        return trifftZu;
    }

    /**
     * Liefert eine Menge der Teilkonzepte dieses Konzepts.
     *
     * @return  Eine Menge der Teilkonzepte dieses Konzepts.
     */
    public HashSet teilkonzepte() {
        return konzepte;
    }

    /**
     * Liefert eine Beschreibung des Konzepts.
     *
     * @return  Eine Beschreibung des Konzepts.
     */
    public String toString() {
        StringBuffer beschreibung = new StringBuffer();
        ArrayList    konzeptListe;
        Iterator     konzIter;

        if (konzepte.isEmpty()) {
            beschreibung.append("true");
        } else {
            konzeptListe = sortierteKonzepte(konzepte);

            beschreibung.append("AND(");
            konzIter = konzeptListe.iterator();
            do {
                Konzept naechstesKonzept = (Konzept) konzIter.next();
                beschreibung.append(naechstesKonzept.toString() + ", ");
            } while (konzIter.hasNext());
            beschreibung.deleteCharAt(beschreibung.length() - 1);
            beschreibung.deleteCharAt(beschreibung.length() - 1);
            beschreibung.append(")");
        }
        return beschreibung.toString();
    }
}

