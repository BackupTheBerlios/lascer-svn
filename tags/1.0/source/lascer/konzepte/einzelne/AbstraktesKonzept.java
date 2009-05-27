/*
 * Dateiname      : AbstraktesKonzept.java
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;

import mathCollection.BitMathIntSet;

import lascer.konzepte.Konzept;

/**
 * Implementiert ein abstraktes Konzept.
 *
 * @author  Dietmar Lippold
 */
public abstract class AbstraktesKonzept implements Konzept {

    /**
     * Die zuletzt verwendetet Menge aller positiven Beispiele.
     */
    private static BitMathIntSet allePosBeispiele = null;

    /**
     * Die zuletzt verwendetet Menge aller negativen Beispiele.
     */
    private static BitMathIntSet alleNegBeispiele = null;

    /**
     * Die Menge der Indices der vom Konzept erf�llten positiven Beispiele
     * der zugrunde liegenden Daten.
     */
    private BitMathIntSet posErfuelltBsp = null;

    /**
     * Die Menge der Indices der vom Konzept erf�llten negativen Beispiele
     * der zugrunde liegenden Daten.
     */
    private BitMathIntSet negErfuelltBsp = null;

    /**
     * Die Gesamtanzahl der insgesamt vorhandenen positiven Beispiele.
     */
    private int posGesamtAnz;

    /**
     * Die Gesamtanzahl der insgesamt vorhandenen negativen Beispiele.
     */
    private int negGesamtAnz;

    /**
     * Liefert eine absteigend nach Qualit�t sortierte Liste der �bergebenen
     * Konzepte.
     *
     * @param konzepte  Die zusortierenden Konzepte.
     *
     * @return  Eine absteigend nach Qualit�t sortierte Liste der �bergebenen
     *          Konzepte.
     */
    public static ArrayList sortierteKonzepte(Collection konzepte) {
        ArrayList         konzeptListe;
        KonzeptComparator comparator;

        comparator = new KonzeptComparator();
        konzeptListe = new ArrayList(konzepte);
        Collections.sort(konzeptListe, comparator);

        return konzeptListe;
    }

    /**
     * Liefert die invertierte Menge der �bergebenen positiven Beispiele.
     *
     * @param gesamtAnz    Die Gesmtanzahl aller positiven Beispiele.
     * @param erfuelltBsp  Die Indices der positiven Beispiele, die nicht in
     *                     der zu erzeugenden Menge enthalten sein sollen.
     *
     * @return  Die invertierte Menge der �bergebenen positiven Beispielen.
     */
    protected static BitMathIntSet posBspInvertiert(int gesamtAnz,
                                                    BitMathIntSet erfuelltBsp) {

        if ((allePosBeispiele == null)
            || (allePosBeispiele.size() != gesamtAnz)) {

            // Die Menge der positiven Beispiele neu erzeugen.
            // Wegen einer ung�nstigen Implementierung von java.util.BitSet in
            // JDK 1.4 den Wert 64 zum gr��ten Index addieren.
            allePosBeispiele = new BitMathIntSet(gesamtAnz + 64);
            for (int i = 0; i < gesamtAnz; i++) {
                allePosBeispiele.add(i);
            }
        }

        return allePosBeispiele.difference(erfuelltBsp);
    }

    /**
     * Liefert die invertierte Menge der �bergebenen negativen Beispiele.
     *
     * @param gesamtAnz    Die Gesmtanzahl aller negativen Beispiele.
     * @param erfuelltBsp  Die Indices der negativen Beispiele, die nicht in
     *                     der zu erzeugenden Menge enthalten sein sollen.
     *
     * @return  Die invertierte Menge der �bergebenen negativen Beispielen.
     */
    protected static BitMathIntSet negBspInvertiert(int gesamtAnz,
                                                    BitMathIntSet erfuelltBsp) {

        if ((alleNegBeispiele == null)
            || (alleNegBeispiele.size() != gesamtAnz)) {

            // Die Menge der positiven Beispiele neu erzeugen.
            // Wegen einer ung�nstigen Implementierung von java.util.BitSet in
            // JDK 1.4 den Wert 64 zum gr��ten Index addieren.
            alleNegBeispiele = new BitMathIntSet(gesamtAnz + 64);
            for (int i = 0; i < gesamtAnz; i++) {
                alleNegBeispiele.add(i);
            }
        }

        return alleNegBeispiele.difference(erfuelltBsp);
    }

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param posGesamtAnz  Die Anzahl der insgesamt vorhandenen positiven
     *                      Beispiele.
     * @param negGesamtAnz  Die Anzahl der insgesamt vorhandenen negativen
     *                      Beispiele.
     */
    public AbstraktesKonzept(int posGesamtAnz, int negGesamtAnz) {
        this.posGesamtAnz = posGesamtAnz;
        this.negGesamtAnz = negGesamtAnz;
    }

    /**
     * Weist den Attributen f�r die erf�llten Beispiele neue Werte zu.
     *
     * @param posErfuellteBsp  Die vom Konzept erf�llten positiven Beispiele.
     * @param negErfuellteBsp  Die vom Konzept erf�llten negativen Beispiele.
     */
    protected void setzeErfuellteBsp(BitMathIntSet posErfuellteBsp,
                                     BitMathIntSet negErfuellteBsp) {
        posErfuelltBsp = posErfuellteBsp;
        negErfuelltBsp = negErfuellteBsp;
    }

    /**
     * Liefert die Menge der Indices der vom Konzept erf�llten positiven
     * Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept erf�llten positiven
     *          Beispiele.
     */
    public BitMathIntSet posErfuelltBsp() {
        return posErfuelltBsp;
    }

    /**
     * Liefert die Menge der Indices der vom Konzept erf�llten negativen
     * Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept erf�llten negativen
     *          Beispiele.
     */
    public BitMathIntSet negErfuelltBsp() {
        return negErfuelltBsp;
    }

    /**
     * Liefert die Menge der Indices der vom Konzept richtiger Weise
     * abgedeckten positiven Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept abgedeckten
     *          positiven Beispiele.
     */
    public BitMathIntSet posRichtigBsp() {
        return posErfuelltBsp();
    }

    /**
     * Liefert die Menge der Indices der vom Konzept richtiger Weise nicht
     * abgedeckten negativen Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept nicht abgedeckten
     *          negativen Beispiele.
     */
    public BitMathIntSet negRichtigBsp() {
        return negBspInvertiert(negGesamtAnz, negErfuelltBsp);
    }

    /**
     * Liefert die Menge der Indices der vom Konzept f�lschlicher Weise nicht
     * abgedeckten positiven Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept nicht abgedeckten
     *          positiven Beispiele.
     */
    public BitMathIntSet posFalschBsp() {
        return posBspInvertiert(posGesamtAnz, posErfuelltBsp);
    }

    /**
     * Liefert die Menge der Indices der vom Konzept f�lschlicher Weise
     * abgedeckten negativen Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept abgedeckten negativen
     *          Beispiele.
     */
    public BitMathIntSet negFalschBsp() {
        return negErfuelltBsp();
    }

    /**
     * Liefert die Anzahl der positiven Beispiele der zugrunde liegenden
     * Daten.
     *
     * @return  Die Gesamtanzahl der positiven Beispiele.
     */
    public int posGesamtAnz() {
        return posGesamtAnz;
    }

    /**
     * Liefert die Anzahl der negativen Beispiele der zugrunde liegenden
     * Daten.
     *
     * @return  Die Gesamtanzahl der negativen Beispiele.
     */
    public int negGesamtAnz() {
        return negGesamtAnz;
    }

    /**
     * Liefert die Anzahl der positiven Beispiele, f�r die das Konzept erf�llt
     * ist.
     *
     * @return  Die Anzahl der positiven Beispiele, f�r die das Konzept
     *          erf�llt ist.
     */
    public int posErfuelltAnz() {
        return posErfuelltBsp.size();
    }

    /**
     * Liefert die Anzahl der negativen Beispiele, f�r die das Konzept erf�llt
     * ist.
     *
     * @return  Die Anzahl der negativen Beispiele, f�r die das Konzept
     *          erf�llt ist.
     */
    public int negErfuelltAnz() {
        return negErfuelltBsp.size();
    }

    /**
     * Liefert die Anzahl der positiven Beispiele, f�r die dieses Konzept
     * richtiger Weise erf�llt ist.
     *
     * @return  Die Anzahl der positiven Beispiele, f�r die dieses Konzept
     *          richtiger Weise erf�llt ist.
     */
    public int posRichtigAnz() {
        return posErfuelltBsp.size();
    }

    /**
     * Liefert die Anzahl der negativen Beispiele, f�r die dieses Konzept
     * richtiger Weise nicht erf�llt ist.
     *
     * @return  Die Anzahl der negativen Beispiele, f�r die dieses Konzept
     *          richtiger Weise nicht erf�llt ist.
     */
    public int negRichtigAnz() {
        return (negGesamtAnz - negErfuelltBsp.size());
    }

    /**
     * Liefert die Anzahl der positiven Beispiele, f�r die dieses Konzept
     * f�lschlicher Weise nicht erf�llt ist.
     *
     * @return  Die Anzahl der positiven Beispiele, f�r die dieses Konzept
     *          f�lschlicher Weise nicht erf�llt ist.
     */
    public int posFalschAnz() {
        return (posGesamtAnz - posErfuelltBsp.size());
    }

    /**
     * Liefert die Anzahl der negativen Beispiele, f�r die dieses Konzept
     * f�lschlicher Weise erf�llt ist.
     *
     * @return  Die Anzahl der negativen Beispiele, f�r die dieses Konzept
     *          f�lschlicher Weise erf�llt ist.
     */
    public int negFalschAnz() {
        return negErfuelltBsp.size();
    }

    /**
     * Ermittelt, ob dieses Konzept sinnvoll ist f�r die Erzeugung eines
     * insgesamt korrekten und vollst�ndigen Konzepts. Dazu mu� dieses Konzept
     * mindestens ein positives Element der zugrunde liegenden Daten abdecken
     * und ein negatives Element der Daten ausschlie�en, soweit es ein
     * positives bzw. ein negatives Beispiel gibt.
     *
     * @return  Die Angabe, ob dieses Konzept sinnvoll ist.
     */
    public boolean istSinnvoll() {
        return (((posRichtigAnz() > 0) || (posGesamtAnz == 0))
                && ((negRichtigAnz() > 0) || (negGesamtAnz == 0)));
    }

    /**
     * Ermittelt, ob das Konzept korrekt ist, d.h. kein negatives Beispiel
     * der zugrunde liegenden Daten abdeckt.
     *
     * @return  Die Angabe, ob dieses Konzept korrekt ist.
     */
    public boolean istKorrekt() {
        return (negFalschAnz() == 0);
    }

    /**
     * Ermittelt, ob das Konzept vollst�ndig ist, d.h. alle positiven
     * Beispiele der zugrunde liegenden Daten abdeckt.
     *
     * @return  Die Angabe, ob dieses Konzept vollst�ndig ist.
     */
    public boolean istVollstaendig() {
        return (posFalschAnz() == 0);
    }

    /**
     * Ermittelt, ob dieses Konzept besser als das �bergebene Konzept ist. Das
     * ist der Fall, wann die Fehleranzahl dieses Konzepts geringer ist als
     * die des anderes Konzepts oder wenn die Fehleranzahl gleich ist und die
     * Komplexit�t dieses Konzepts geringer ist als die des anderen Konzepts.
     *
     * @param anderesKonzept  Ein anderes Konzept, mit dem dieses Konzept
     *                        verglichen wird.
     *
     * @return  Die Angabe, ob dieses Konzept besser als das �bergebene
     *          Konzept ist.
     */
    public boolean istBesser(Konzept anderesKonzept) {
        float dieseKomplexitaet, andereKomplexitaet;
        int dieseFehlerAnz, andereFehlerAnz;

        dieseFehlerAnz = posFalschAnz() + negFalschAnz();
        andereFehlerAnz = (anderesKonzept.posFalschAnz()
                           + anderesKonzept.negFalschAnz());
        dieseKomplexitaet = komplexitaet();
        andereKomplexitaet = anderesKonzept.komplexitaet();

        return ((dieseFehlerAnz < andereFehlerAnz)
                || ((dieseFehlerAnz == andereFehlerAnz)
                    && (dieseKomplexitaet < andereKomplexitaet)));
    }

    /**
     * Ermittelt, ob dieses Konzept gleich gut wie das �bergebene Konzept ist.
     * Das ist der Fall, wann die Fehleranzahl und die Komplexit�t dieses
     * Konzepts gleich sind zur Fehleranzahl und die Komplexit�t des anderes
     * Konzepts.
     *
     * @param anderesKonzept  Ein anderes Konzept, mit dem dieses Konzept
     *                        verglichen wird.
     *
     * @return  Die Angabe, ob dieses Konzept gleich gut ist wie das
     *          �bergebene Konzept.
     */
    public boolean istGleichGut(Konzept anderesKonzept) {
        float dieseKomplexitaet, andereKomplexitaet;
        int dieseFehlerAnz, andereFehlerAnz;

        dieseFehlerAnz = posFalschAnz() + negFalschAnz();
        andereFehlerAnz = (anderesKonzept.posFalschAnz()
                           + anderesKonzept.negFalschAnz());
        dieseKomplexitaet = komplexitaet();
        andereKomplexitaet = anderesKonzept.komplexitaet();

        return ((dieseFehlerAnz == andereFehlerAnz)
                && (dieseKomplexitaet == andereKomplexitaet));
    }

    /**
     * Liefert eine Liste der elementaren Teilkonzepte (Literale) dieses
     * Konzepts. In dieser Liste k�nnen einige Literale auch mehrfach
     * vorkommen.
     *
     * @return  Eine Liste der elementaren Teilkonzepte dieses Konzepts.
     */
    public ArrayList elementareTeilkonzepte() {
        ArrayList elemTeilkonzepte = new ArrayList();
        HashSet   teilkonzepte;
        Iterator  iter;
        Konzept   teilkonzept;

        teilkonzepte = teilkonzepte();
        if (teilkonzepte.isEmpty()) {
            elemTeilkonzepte.add(this);
        } else {
            iter = teilkonzepte.iterator();
            while (iter.hasNext()) {
                teilkonzept = (Konzept) iter.next();
                elemTeilkonzepte.addAll(teilkonzept.elementareTeilkonzepte());
            }
        }

        return elemTeilkonzepte;
    }

    /**
     * Liefert eine Statistik zum Konzept.
     *
     * @return  Eine Statistik des Konzepts.
     */
    public String statistik() {
        StringBuffer ausgabe = new StringBuffer();

        ausgabe.append("Anzahl der pos. Beispiele = " + posGesamtAnz() + "\n");
        ausgabe.append("Anzahl der pos. Fehler    = " + posFalschAnz() + "\n");
        ausgabe.append("Anzahl der neg. Beispiele = " + negGesamtAnz() + "\n");
        ausgabe.append("Anzahl der neg. Fehler    = " + negFalschAnz() + "\n");
        ausgabe.append("Anzahl der Konjunktionen  = " + konjunktionsAnz() + "\n");
        ausgabe.append("Anzahl der Disjunktionen  = " + disjunktionsAnz() + "\n");
        ausgabe.append("Anzahl der einz. Literale = " + einzelLiteralAnz() + "\n");
        ausgabe.append("Anzahl der pos. Literale  = " + posLiteralAnz() + "\n");
        ausgabe.append("Anzahl der neg. Literale  = " + negLiteralAnz() + "\n");
        ausgabe.append("Komplexit�t               = " + komplexitaet());

        return ausgabe.toString();
    }
}

