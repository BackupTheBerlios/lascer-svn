/*
 * Dateiname      : SofortigeAuswahl.java
 * Letzte �nderung: 28. November 2007
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut f�r Intelligente Systeme Universit�t Stuttgart,
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


package mengenUeberdeckung.reduktion;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.LinkedList;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.SizeMismatchException;
import mengenUeberdeckung.heuristiken.utility.ReductionUtility;

/**
 * Die Klasse dient der Auswahl von IndexTeilmengen aus einer Familie von
 * vorgegebenen IndexTeilmengen, wobei die Auswahl mittels einer
 * vorgegebenen Nutzenfunktion erfolgt. Es werden dabei immer nur die
 * jeweils ausgew�hlten Teilmengen gespeichert.<P>
 *
 * Die Anzahl der zu speichernden Teilmengen wird im Konstruktor �bergeben.
 * Auf jeden Fall gespeichert werden Teilmengen mit Kosten von Null und
 * notwendige Teilmengen.
 *
 * @author  Dietmar Lippold
 */
public class SofortigeAuswahl extends ItmAuswahlErzeugung {

    /**
     * Eine erg�nzte Auswahl, der ebenfalls alle Teilmengen �bergeben werden,
     * die diesem Objekt hinzugef�gt werden, oder der Wert <CODE>null</CODE>,
     * wenn keine erg�nzte Auswahl vorhanden ist. Wenn sie vorhanden ist,
     * enth�lt sie die eigentlich ausgew�hlten Teilmengen und dieses Objekt,
     * bzw. konkret die Familie <CODE>ausgewaehlteTeilmengen</CODE>, enth�lt
     * nur die notwendigen Teilmengen und Teilmengen mit Kosten von Null.
     */
    private SofortigeAuswahl ergaenzteAuswahl = null;

    /**
     * Die zur Auswahl der Teilmengen zu benutzte Bewertungsfunktion.
     */
    private ReductionUtility reductionUtility;

    /**
     * Die Familie, die die ausgew�hlten Teilmengen enth�lt.
     */
    private ItmFamilie ausgewaehlteTeilmengen;

    /**
     * Tripel aus den ausgew�hlten Teilmengen zusammen mit ihren relativen
     * Nutzen und Kosten aufsteigend bez�glich der Kosten sortiert. Die
     * relativen Nutzen und Kosten sind die, um die sich der Nutzen bzw. die
     * Kosten der Familie bei Entfernung der Teilmenge verringern. Die Objekte
     * sind vom Typ <CODE>NutzenKostenItm</CODE>.
     */
    private TreeSet aufsteigendeTeilmengen = null;

    /**
     * Tripel aus den ausgew�hlten Teilmengen zusammen mit ihren relativen
     * Nutzen und Kosten absteigend bez�glich der Kosten sortiert. Die
     * relativen Nutzen und Kosten sind die, um die sich der Nutzen bzw. die
     * Kosten der Familie bei Entfernung der Teilmenge verringern. Die Objekte
     * sind vom Typ <CODE>NutzenKostenItm</CODE>.
     */
    private TreeSet absteigendeTeilmengen = null;

    /**
     * Speichert zu jedem Hash-Code einer in dieser Auswahl enthaltenen
     * Teilmenge eine <CODE>LinkedList</CODE> aller enthaltenen Teilmengen,
     * die diesen Hash-Code besitzen. Der Hash-Code ist als Key ist als
     * <CODE>Integer</CODE> gespeichert.
     */
    private HashMap hashCodeTeilmengen = new HashMap();

    /**
     * Die Elemente, durch die eine hinzuzunehmende Teilmenge zu einer
     * erforderlichen Teilmenge wird. Das sind die Elemente, die weniger als
     * <CODE>minUeberdeckAnz</CODE> mal �berdeckt sind.
     */
    private IndexTeilmenge hinzuErforderlichElem;

    /**
     * Die Elemente, durch die eine enthaltene Teilmenge zu einer
     * erforderlichen Teilmenge wird. Das sind die Elemente, die maximal
     * <CODE>minUeberdeckAnz</CODE> mal �berdeckt sind.
     */
    private IndexTeilmenge enthaltenErforderlichElem;

    /**
     * Das Verh�ltnis der Anzahl der Teilmengen in <CODE>ergaenzteAuswahl</CODE>
     * zur Anzahl der Teilmengen in <CODE>ausgewaehlteTeilmengen</CODE>. Wenn
     * keine erg�nzte Ausahl vorhanden ist, ist der Wert ohne Bedeutung.
     */
    private float faktorErgAnz = 0;

    /**
     * Der Nutzenwert der aktuellen Familie.
     */
    private float aktuellerNutzen = 0;

    /**
     * Der Kostenwert der aktuellen Familie.
     */
    private float aktuelleKosten = 0;

    /**
     * Der minimale Quotient aus dem negierten Nutzen bei der Entfernung und
     * den Kosten der in der aktuellen Familie enthaltenen Teilmengen. Der
     * Quotient ist positiv.
     */
    private float minQuotient;

    /**
     * Die minimale Anzahl der Teilmengen, die in der Familie der erg�nzenden
     * Auswahl enthalten sein soll.
     */
    private int ergaenzteMinItmAnz = 0;

    /**
     * Die minimale Anzahl der Teilmengen, die in der Familie enthalten sein
     * soll. Bei einem negativen Wert werden alle Teilmengen aufgenommen und
     * es findet keine Auswahl statt.
     */
    private int minItmAnzahl;

    /**
     * Die minimale H�ufigkeit, mit der jedes Element �berdeckt sein soll.
     * Ein Wert von Null bedeutet, da� keine minimale H�uigkeit gefordert
     * wird.
     */
    private int minUeberdeckAnz;

    /**
     * Gibt an, ob seit der Existens dieser Instanz die maximale Anzahl der
     * auszuw�hlenden Teilmengen schon einmal erreicht wurde
     */
    private boolean maxAuswahlWurdeErreicht = false;

    /**
     * Erzeugt eine neue Instanz zur Auswahl von Teilmengen. In der Auswahl
     * ist mindestens die �bergebene Anzahl von Teilmengen enthalten. Au�erdem
     * sind Teilmengen mit Kosten von Null und notwendige Teilmengen
     * enthalten.
     *
     * @param ausgangsFamilie   Eine Familie, in deren Kopie die ausgew�hlten
     *                          Teilmengen aufgenommen werden. In der Regel
     *                          enth�lt diese noch keine Teilmenge.
     * @param minItmAnzahl      Die minimale Anzahl der Teilmengen, die in
     *                          der Familie enthalten sein soll. Bei einem
     *                          negativen Wert werden alle Teilmengen
     *                          aufgenommen und es findet keine Auswahl
     *                          statt.
     * @param minUeberdeckAnz   Die minimale H�ufigkeit, mit der jedes
     *                          Element �berdeckt sein soll. Ein Wert von
     *                          Null bedeutet, da� keine minimale H�uigkeit
     *                          gefordert wird.
     * @param reductionUtility  Das Verfahren, das zur Berechnung des
     *                          Nutzens der Teilmengen und der Familie
     *                          verwendet werden soll.
     */
    public SofortigeAuswahl(ItmFamilie ausgangsFamilie, int minItmAnzahl,
                            int minUeberdeckAnz, ReductionUtility reductionUtility) {
        super(ausgangsFamilie.groesseGesamtmenge());

        int groesseGesamtmenge = ausgangsFamilie.groesseGesamtmenge();

        this.ausgewaehlteTeilmengen = (ItmFamilie) ausgangsFamilie.clone();
        this.minItmAnzahl = minItmAnzahl;
        if (minUeberdeckAnz <= 1) {
            this.minUeberdeckAnz = 0;
        } else {
            this.minUeberdeckAnz = minUeberdeckAnz;
        }
        this.reductionUtility = reductionUtility;
        this.hinzuErforderlichElem = new IndexTeilmenge(groesseGesamtmenge);
        this.enthaltenErforderlichElem = new IndexTeilmenge(groesseGesamtmenge);
    }

    /**
     * Erzeugt eine neue Instanz zur Auswahl von Teilmengen zusammmen mit
     * einer Instanz zur erg�nzten Auswahl. Diese Auswahl, also nicht die
     * erg�nzte Auswahl, ist eine minimale Auswahl, in der nur Teilmengen
     * enthalten sind, die notwendig sind oder deren Kosten Null sind. Die
     * Anzahl der Teilmengen in der erg�nzten Auswahl ist mindestens so gro�
     * wie das Produkt aus der Anzahl der Teilmengen dieser Auswahl und aus
     * dem �bergebenen Faktor. Sie ist au�erdem mindestens so gro� wie die
     * �bergebene Anzahl. Wenn die �bergebene Anzahl von Mengen negativ ist,
     * also keine Auswahl erfolgt, wird keine erg�nzte Auswahl erzeugt.
     *
     * @param ausgangsFamilie   Eine Familie, in deren Kopie die ausgew�hlten
     *                          Teilmengen aufgenommen werden. In der Regel
     *                          enth�lt diese noch keine Teilmenge.
     * @param minItmAnzahl      Die minimale Anzahl der Teilmengen, die in
     *                          der Familie enthalten sein soll. Bei einem
     *                          negativen Wert werden alle Teilmengen
     *                          aufgenommen und es findet keine Auswahl
     *                          statt.
     * @param minUeberdeckAnz   Die minimale H�ufigkeit, mit der jedes
     *                          Element �berdeckt sein soll. Ein Wert von
     *                          Null bedeutet, da� keine minimale H�uigkeit
     *                          gefordert wird.
     * @param faktorErgAnz      Das Verh�ltnis der Anzahl der Teilmengen
     *                          der erg�nzten Auswahl und dieser Auswahl.
     * @param reductionUtility  Das Verfahren, das zur Berechnung des
     *                          Nutzens der Teilmengen und der Familie
     *                          dieser und der erg�nzten Auswahl verwendet
     *                          werden soll.
     *
     * @throws IllegalArgumentException  <CODE>faktorErgAnz</CODE> hat einen
     *                                   negativen Wert.
     */
    public SofortigeAuswahl(ItmFamilie ausgangsFamilie, int minItmAnzahl,
                            int minUeberdeckAnz, float faktorErgAnz,
                            ReductionUtility reductionUtility) {
        this(ausgangsFamilie, minItmAnzahl, minUeberdeckAnz, reductionUtility);

        if (faktorErgAnz < 0) {
            throw new IllegalArgumentException("Parameter faktorErgAnz ist"
                                               + " negativ");
        }

        if (minItmAnzahl >= 0) {
            this.minItmAnzahl = 0;
            this.minUeberdeckAnz = 0;
            this.faktorErgAnz = faktorErgAnz;
            this.ergaenzteMinItmAnz = minItmAnzahl;

            int groesseGesamtmenge = ausgangsFamilie.groesseGesamtmenge();
            int ergAnz = Math.max(minItmAnzahl,
                                  Math.round(groesseGesamtmenge * faktorErgAnz));
            if (ergAnz > 0) {
                ergaenzteAuswahl = new SofortigeAuswahl(ausgangsFamilie, ergAnz,
                                                        minUeberdeckAnz,
                                                        reductionUtility);
            }
        }
    }

    /**
     * Ermittelt, ob eine Verbesserung der Familie durch Hinzunahme einer
     * neuen Teilmenge m�glich ist. Dazu wird die Bewertung der Familie nach
     * Addition des mittels <CODE>minQuotient</CODE> abgesch�tzten Nutzens und
     * Subtraktion der angegebenen Kosten mit der derzeitigen Bewertung der
     * Familie verglichen.
     *
     * @param ergaenzterNutzen  Der Nutzenwert der Familie nach der Aufnahme
     *                          der neuen Teilmenge.
     * @param ergaenzteKosten   Der Kostenwert der Familie nach der Aufnahme
     *                          der neuen Teilmenge.
     * @param wegKosten         Die gesch�tzten Kosten, die bei der Entfernung
     *                          der enthaltenen Teilmenge wegfallen.
     *
     * @return  Die Beurteilung, ob nach der Hinzunahme der neuen Teilmenge
     *          die Entfernung der enthaltenen Teilmenge zu einer besseren
     *          Bewertung der Familie f�hren kann als derzeit.
     */
    private boolean verbesserungMoeglich(float ergaenzterNutzen,
                                         float ergaenzteKosten,
                                         float wegKosten) {
        float nutzenSumme, kostenSumme;

        nutzenSumme = ergaenzterNutzen - minQuotient * wegKosten;
        kostenSumme = ergaenzteKosten - wegKosten;
        return (nutzenSumme / kostenSumme > aktuellerNutzen / aktuelleKosten);
    }

    /**
     * Pr�ft, ob eine Teilmenge zus�tzlich zu den vorhandenen Teilmengen in
     * die Familie aufgenommen werden kann.
     *
     * @return  <CODE>true</CODE>, wenn eine Teilmenge zus�tzlich aufgenommen
     *          werden kann, sonst <CODE>false</CODE>.
     */
    private boolean zusaetzlicheAufnahmeMoeglich() {
        return ((minItmAnzahl < 0)
                || (ausgewaehlteTeilmengen.groesseFamilie() < minItmAnzahl));
    }

    /**
     * Pr�ft, ob die �bergebene Teilmenge unabh�ngig von den Bewertungen der
     * vorhandenen Teilmengen aufgenommen werden soll.
     *
     * @param teilmenge    Die Teilmenge, von der gepr�ft werden soll, ob sie
     *                     unabh�ngig von den Bewertungen der vorhandenen
     *                     Teilmengen aufgenommen werden soll.
     * @param hinzuKosten  Die bei der Aufnahme der Teilmenge hinzukommenden
     *                     Kosten.
     *
     * @return  <CODE>true</CODE>, wenn die �bergebene Teilmenge unabh�ngig
     *          von den Bewertungen der vorhandenen Teilmengen aufgenommen
     *          werden soll, sonst <CODE>false</CODE>.
     */
    private boolean unabhaengigeAufnahmeMoeglich(IndexTeilmenge teilmenge,
                                                 float hinzuKosten) {
        return ((hinzuKosten == 0)
                || ausgewaehlteTeilmengen.teilmengeIstNotwendig(teilmenge)
                || !teilmenge.istDisjunkt(hinzuErforderlichElem));
    }

    /**
     * Ermittelt, ob aus der aktuellen Familie eine Teilmenge entfernt werden
     * kann. Daf�r m�ssen mehr Teilmengen als die vorgegebene Mindestanzahl
     * vorhanden sein und au�erdem mu� mindestens eine nicht notwendige
     * Teilmengen und eine Teilmenge mit Kosten gr��er als Null vorhanden
     * sein.
     *
     * @return  <CODE>true</CODE>, wenn eine Teilmenge entfernt werden kann,
     *          sonst <CODE>false</CODE>.
     */
    private boolean teilmengeEntfernenMoeglich() {
        return ((aktuelleKosten > 0)
                && (ausgewaehlteTeilmengen.groesseFamilie() > minItmAnzahl)
                && (ausgewaehlteTeilmengen.anzNichtNotwendigeTeilmengen() > 0));
    }

    /**
     * Ermittelt erstmalig die Daten der Attribute auf Grundlage der Belegung
     * von <CODE>ausgewaehlteTeilmengen</CODE>.
     */
    private void datenErzeugen() {
        NutzenKostenItm tripel;
        IndexTeilmenge  nextTeilmenge;
        Iterator        itmIter;
        float           nutzen, kosten;
        int             groesseGesamtmenge, ueberdeckAnz;
        int             teilmengenAnz, nichtUeberdecktAnz, prodAnz;

        aufsteigendeTeilmengen = new TreeSet();
        absteigendeTeilmengen = new TreeSet();
        minQuotient = Float.MAX_VALUE;

        itmIter = ausgewaehlteTeilmengen.iterator();
        while (itmIter.hasNext()) {
            nextTeilmenge = (IndexTeilmenge) itmIter.next();
            nutzen = reductionUtility.addUtilityPot(nextTeilmenge,
                                                    ausgewaehlteTeilmengen);
            kosten = ausgewaehlteTeilmengen.kostenEntfernen(nextTeilmenge);

            tripel = new NutzenKostenItm(nextTeilmenge, nutzen, kosten, true);
            aufsteigendeTeilmengen.add(tripel);

            tripel = new NutzenKostenItm(nextTeilmenge, nutzen, kosten, false);
            absteigendeTeilmengen.add(tripel);

            if (nutzen / kosten < minQuotient) {
                minQuotient = -nutzen / kosten;
            }
        }

        // Die erforderlichen Elemente ermitteln.
        if (minUeberdeckAnz > 0) {
            groesseGesamtmenge = groesseGesamtmenge();
            for (int i = 0; i < groesseGesamtmenge; i++) {
                ueberdeckAnz = ausgewaehlteTeilmengen.ueberdeckungsHaeufigkeit(i);
                if (ueberdeckAnz <= minUeberdeckAnz) {
                    enthaltenErforderlichElem.indexAufnehmen(i);
                    if (ueberdeckAnz < minUeberdeckAnz) {
                        hinzuErforderlichElem.indexAufnehmen(i);
                    }
                }
            }
        }

        aktuellerNutzen = reductionUtility.familyUtility(ausgewaehlteTeilmengen);
        aktuelleKosten = ausgewaehlteTeilmengen.kostenFamilie();

        if (ergaenzteAuswahl != null) {
            // Die Anzahl der Teilmengen in der erg�nzten Auswahl neu setzen.
            // Diese ergibt sich aus der Multiplikation der f�r eine vollst�ndige
            // �berdeckung maximal erforderlichen Anzahl von Teilmengen mit
            // dem vorgegebenen Faktor. Au�erdem ist die minimale Anzahl der
            // Teilmengen zur ber�cksichtigen.
            teilmengenAnz = ausgewaehlteTeilmengen.groesseFamilie();
            nichtUeberdecktAnz = ausgewaehlteTeilmengen.anzNichtUeberdeckt();
            prodAnz = Math.round((teilmengenAnz + nichtUeberdecktAnz) * faktorErgAnz);
            ergaenzteAuswahl.setzeMinItmAnz(Math.max(prodAnz, ergaenzteMinItmAnz));
        }
    }

    /**
     * Aktualisiert die Menge der f�r eine neue Teilmenge erforderlichen
     * Elemente nach Hinzunahme oder Entfernung einer Teilmenge zu bzw. aus
     * <CODE>ausgewaehlteTeilmengen</CODE>.
     *
     * @param teilmenge  Eine Teilmenge, die in <CODE>ausgewaehlteTeilmengen</CODE>
     *                   aufgenommen oder daraus entfernt wurde.
     * @param hinzu      Gibt an, ob die �bergebene Teilmenge aufgenommen wurde.
     *                   Anderenfalls wurde sie entfernt.
     */
    private void hinzuErforderElemAktualisieren(IndexTeilmenge teilmenge,
                                                boolean hinzu) {
        int ueberdeckAnz;

        if (minUeberdeckAnz > 0) {
                // Die Elemente ermitteln, die neu erforderlich sind.
            for (int index = teilmenge.kleinsterEnthaltenerIndex();
                 index >= 0;
                 index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {
                ueberdeckAnz = ausgewaehlteTeilmengen.ueberdeckungsHaeufigkeit(index);
                if (hinzu) {
                    // Pr�fen, ob das Element nicht mehr erforderlich ist.
                    if (ueberdeckAnz == minUeberdeckAnz) {
                        hinzuErforderlichElem.indexLoeschen(index);
                    }
                } else {
                    // Pr�fen, ob das Element neu erforderlich ist.
                    if (ueberdeckAnz == minUeberdeckAnz - 1) {
                        hinzuErforderlichElem.indexAufnehmen(index);
                    }
                }
            }
        }
    }

    /**
     * Aktualisiert die Menge der f�r eine vorhandene Teilmenge erforderlichen
     * Elemente nach Hinzunahme oder Entfernung einer Teilmenge zu bzw. aus
     * <CODE>ausgewaehlteTeilmengen</CODE>.
     *
     * @param teilmenge  Eine Teilmenge, die in <CODE>ausgewaehlteTeilmengen</CODE>
     *                   aufgenommen oder daraus entfernt wurde.
     * @param hinzu      Gibt an, ob die �bergebene Teilmenge aufgenommen wurde.
     *                   Anderenfalls wurde sie entfernt.
     */
    private void enthaltErforderElemAktualisieren(IndexTeilmenge teilmenge,
                                                  boolean hinzu) {
        int ueberdeckAnz;

        if (minUeberdeckAnz > 0) {
                // Die Elemente ermitteln, die neu erforderlich sind.
            for (int index = teilmenge.kleinsterEnthaltenerIndex();
                 index >= 0;
                 index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {
                ueberdeckAnz = ausgewaehlteTeilmengen.ueberdeckungsHaeufigkeit(index);
                if (hinzu) {
                    // Pr�fen, ob das Element nicht mehr erforderlich ist.
                    if (ueberdeckAnz == minUeberdeckAnz + 1) {
                        enthaltenErforderlichElem.indexLoeschen(index);
                    }
                } else {
                    // Pr�fen, ob das Element neu erforderlich ist.
                    if (ueberdeckAnz == minUeberdeckAnz) {
                        enthaltenErforderlichElem.indexAufnehmen(index);
                    }
                }
            }
        }
    }

    /**
     * Ermittelt die Daten der Attribute auf Grundlage der neuen Belegung
     * von <CODE>ausgewaehlteTeilmengen</CODE>. Dabei werden den Elementen
     * von <CODE>aufsteigendeTeilmengen</CODE> und <CODE>absteigendeTeilmengen</CODE>
     * nach Aufnahme eines neuen Tripels der hinzuzunehmenden Teilmenge und
     * Entfernung des Tripels der zu entfernenden Teilmenge neue Werte f�r
     * Nutzen und Kosten zugewiesen.<P>
     *
     * Aufrufe dieser Methode k�nnen auch durch Aufrufe der Methode
     * <CODE>datenErzeugen</CODE> ersetzt werden. Gegen�ber der Methode
     * verbraucht diese Methode aber weniger Objekte.
     *
     * @param neueTeilmenge  Die Teilmenge, die in <CODE>ausgewaehlteTeilmengen</CODE>
     *                       neu aufgenommen wurde. Wenn keine aufgenommen wurde,
     *                       wird der Wert <CODE>null</CODE> �bergeben.
     * @param wegTeilmenge   Die Teilmenge, die aus <CODE>ausgewaehlteTeilmengen</CODE>
     *                       entfernt wurde. Wenn keine entfernt wurde, wird der
     *                       Wert <CODE>null</CODE> �bergeben.
     */
    private void datenAktualisieren(IndexTeilmenge neueTeilmenge,
                                    IndexTeilmenge wegTeilmenge) {
        TreeSet         alteTripel;
        NutzenKostenItm nextTripel;
        IndexTeilmenge  nextTeilmenge;
        Iterator        tripelIter;
        float           nutzen, kosten;
        int             teilmengenAnz, nichtUeberdecktAnz, prodAnz;

        if (neueTeilmenge != null) {
            // Die neue Teilmenge als Tripel aufnehmen
            aufsteigendeTeilmengen.add(new NutzenKostenItm(neueTeilmenge, 0, 0, true));
            absteigendeTeilmengen.add(new NutzenKostenItm(neueTeilmenge, 0, 0, false));
        }

        // aufsteigendeTeilmengen neu erzeugen
        minQuotient = Float.MAX_VALUE;
        alteTripel = aufsteigendeTeilmengen;
        tripelIter = alteTripel.iterator();
        aufsteigendeTeilmengen = new TreeSet();
        while (tripelIter.hasNext()) {
            nextTripel = (NutzenKostenItm) tripelIter.next();
            nextTeilmenge = nextTripel.teilmenge();
            if (!nextTeilmenge.equals(wegTeilmenge)) {
                nutzen = reductionUtility.addUtilityPot(nextTeilmenge,
                                                        ausgewaehlteTeilmengen);
                kosten = ausgewaehlteTeilmengen.kostenEntfernen(nextTeilmenge);
                nextTripel.setzeNutzenKosten(nutzen, kosten);
                aufsteigendeTeilmengen.add(nextTripel);

                if (nutzen / kosten < minQuotient) {
                    minQuotient = -nutzen / kosten;
                }
            }
        }

        // absteigendeTeilmengen neu erzeugen
        alteTripel = absteigendeTeilmengen;
        tripelIter = alteTripel.iterator();
        absteigendeTeilmengen = new TreeSet();
        while (tripelIter.hasNext()) {
            nextTripel = (NutzenKostenItm) tripelIter.next();
            nextTeilmenge = nextTripel.teilmenge();
            if (!nextTeilmenge.equals(wegTeilmenge)) {
                nutzen = reductionUtility.addUtilityPot(nextTeilmenge,
                                                        ausgewaehlteTeilmengen);
                kosten = ausgewaehlteTeilmengen.kostenEntfernen(nextTeilmenge);
                nextTripel.setzeNutzenKosten(nutzen, kosten);
                absteigendeTeilmengen.add(nextTripel);
            }
        }

        aktuellerNutzen = reductionUtility.familyUtility(ausgewaehlteTeilmengen);
        aktuelleKosten = ausgewaehlteTeilmengen.kostenFamilie();

        if (ergaenzteAuswahl != null) {
            // Die Anzahl der Teilmengen in der erg�nzten Auswahl neu setzen.
            // Diese ergibt sich aus der Multiplikation der f�r eine vollst�ndige
            // �berdeckung maximal erforderlichen Anzahl von Teilmengen mit
            // dem vorgegebenen Faktor. Au�erdem ist die minimale Anzahl der
            // Teilmengen zur ber�cksichtigen.
            teilmengenAnz = ausgewaehlteTeilmengen.groesseFamilie();
            nichtUeberdecktAnz = ausgewaehlteTeilmengen.anzNichtUeberdeckt();
            prodAnz = Math.round((teilmengenAnz + nichtUeberdecktAnz) * faktorErgAnz);
            ergaenzteAuswahl.setzeMinItmAnz(Math.max(prodAnz, ergaenzteMinItmAnz));
        }
    }

    /**
     * Liefert die schlechteste Teilmenge, durch deren Entfernung also die
     * gr��te Bewertung entsteht, wenn diese Bewertung gr��er ist als die
     * urspr�ngliche Bewertung. Wenn die Bewertung nicht gr��er ist, wird
     * der Wert <CODE>null</CODE> geliefert. Die Vorbedingung ist, da� die
     * Kosten der bisherigen Familie gr��er als Null waren.
     *
     * @param ergaenzterNutzen  Der Nutzen der Familie nach Hinzunahme der
     *                          neuen Teilmenge.
     * @param ergaenzteKosten   Die Kosten der Familie nach Hinzunahme der
     *                          neuen Teilmenge.
     *
     * @return  Die schlechteste Teilmenge, durch deren Entfernung eine
     *          gr��ere Bewertung als urspr�nglich vorhanden entsteht, oder
     *          <CODE>null</CODE>, wenn es so eine Teilmenge nicht gibt.
     */
    private IndexTeilmenge bessereSchlechtesteTeilmenge(float ergaenzterNutzen,
                                                        float ergaenzteKosten) {
        NutzenKostenItm vglTripel;
        IndexTeilmenge  vglTeilmenge, schlechtesteTeilmenge;
        Iterator        tripelIter;
        float           geschNutzen, geschKosten;
        float           wegNutzen, wegKosten;
        float           groessteBewertung, neueBewertung;
        boolean         abbrechen;

        // Den Iterator f�r die Tripel ermitteln.
        if (ergaenzterNutzen - minQuotient * ergaenzteKosten > 0) {
            tripelIter = absteigendeTeilmengen.iterator();
        } else {
            tripelIter = aufsteigendeTeilmengen.iterator();
        }

        // Unter allen vorhandenen Teilmengen, gegen�ber denen die �bergebene
        // Teilmenge besser bewertet sein kann, diejenige ermitteln, nach
        // deren Entfernung sich die gr��te Bewertung der Familie ergibt.
        // Diese Bewertung mu� jedoch gr��ere sein als die der urspr�nglichen
        // Familie.
        groessteBewertung = 0;
        schlechtesteTeilmenge = null;
        abbrechen = false;
        while (tripelIter.hasNext() && (!abbrechen)) {
            vglTripel = (NutzenKostenItm) tripelIter.next();
            vglTeilmenge = vglTripel.teilmenge();

            // Pr�fen, ob die Bewertung nach Entfernung von vglTeilmenge
            // schlechter ist als die urspr�nglichen Bewertung. Damit w�re
            // die Bewertung auch f�r alle folgenden Tripel schlechter.
            abbrechen = !verbesserungMoeglich(ergaenzterNutzen, ergaenzteKosten,
                                              vglTripel.kosten());
            if (!abbrechen) {
                // Eine Entfernung von vglTeilmenge kann nur dann zu einer
                // gr��eren Bewertung als bisher f�hren, wenn der konkrete
                // Sch�tzwert gr��er als die aktuelle Bewertung ist.
                geschNutzen = ergaenzterNutzen - vglTripel.nutzen();
                geschKosten = ergaenzteKosten - vglTripel.kosten();
                if (geschNutzen / geschKosten > aktuellerNutzen / aktuelleKosten) {

                    // Es kommen nur nicht notwendige und nicht erforderliche
                    // Teilmengen zur Entfernung in Frage.
                    if (!ausgewaehlteTeilmengen.teilmengeIstNotwendig(vglTeilmenge)
                        && vglTeilmenge.istDisjunkt(enthaltenErforderlichElem)) {

                        // Vergleich der Bewertung nach Entfernung von
                        // vglTeilmenge mit der bisher gr��ten Bewertung.
                        wegNutzen = reductionUtility.rmvUtility(vglTeilmenge,
                                                                ausgewaehlteTeilmengen);
                        wegKosten = ausgewaehlteTeilmengen.kostenEntfernen(vglTeilmenge);
                        neueBewertung = (ergaenzterNutzen + wegNutzen)
                                        / (ergaenzteKosten - wegKosten);
                        if (neueBewertung > groessteBewertung) {
                            schlechtesteTeilmenge = vglTeilmenge;
                            groessteBewertung = neueBewertung;
                        }
                    }
                }
            }
        }

        // Die schlechteste Teilmenge nur liefern, wenn die durch ihre
        // Entfernung entstehende Bewertung besser ist als die urspr�ngliche
        // Bewertung. Ansonsten wird null geliefert.
        if (groessteBewertung > (aktuellerNutzen / aktuelleKosten)) {
            return schlechtesteTeilmenge;
        } else {
            return null;
        }
    }

    /**
     * Liefert die schlechteste nicht notwendige Teilmenge, durch deren
     * Entfernung also die gr��te Bewertung entsteht. Die Vorbedingung ist,
     * da� die Kosten der Familie gr��er als Null sind.
     *
     * @return  Die schlechteste nicht notwendige und nicht erforderliche
     *          Teilmenge.
     */
    private IndexTeilmenge gesamtSchlechtesteTeilmenge() {
        IndexTeilmenge vglTeilmenge, schlechtesteTeilmenge;
        Iterator       itmIter;
        float          groessteBewertung, neueBewertung;
        float          wegNutzen, wegKosten;

        // Einen Iterator �ber alle Teilmengen, die entfernt werden k�nnen,
        // erzeugen.
        itmIter = ausgewaehlteTeilmengen.nichtNotwendigeTeilmengen().iterator();

        // Unter allen nicht notwendigen Teilmengen die ermitteln, nach deren
        // Entfernung sich die gr��te Bewertung der Familie ergibt.
        groessteBewertung = 0;
        schlechtesteTeilmenge = null;
        while (itmIter.hasNext()) {
            vglTeilmenge = (IndexTeilmenge) itmIter.next();

            // Es sind nur Teilmengen zu ber�cksichtigen, die nicht
            // erforderlich sind.
            if (vglTeilmenge.istDisjunkt(enthaltenErforderlichElem)) {
                // Ermitteln der Bewertung nach Entfernung von vglTeilmenge.
                wegNutzen = reductionUtility.rmvUtility(vglTeilmenge,
                                                        ausgewaehlteTeilmengen);
                wegKosten = ausgewaehlteTeilmengen.kostenEntfernen(vglTeilmenge);
                if (aktuelleKosten - wegKosten == 0) {
                    // vglTeilmenge hat als einzige Kosten gr��er als Null.
                    return vglTeilmenge;
                }
                neueBewertung = (aktuellerNutzen + wegNutzen)
                                / (aktuelleKosten - wegKosten);
                if (neueBewertung > groessteBewertung) {
                    schlechtesteTeilmenge = vglTeilmenge;
                    groessteBewertung = neueBewertung;
                }
            }
        }

        return schlechtesteTeilmenge;
    }

    /**
     * Nimmt die �bergebene Teilmenge in die Liste der Teilmengen mit gleichem
     * Hash-Code auf. Wenn es noch keine solche Teilmenge gibt, wird eine neue
     * Liste erzeugt.
     *
     * @param teilmenge  Die Teilmenge, die in die Liste der Teilmengen mit
     *                   gleichem Hash-Code aufgenommen werden soll.
     */
    private void hashCodeItmAufnehmen(IndexTeilmenge teilmenge) {
        LinkedList aehnlicheTeilmengen;
        Integer    hashCodeInt;

        hashCodeInt = new Integer(teilmenge.hashCode());
        aehnlicheTeilmengen = (LinkedList) hashCodeTeilmengen.get(hashCodeInt);
        if (aehnlicheTeilmengen == null) {
            aehnlicheTeilmengen = new LinkedList();
            aehnlicheTeilmengen.add(teilmenge);
            hashCodeTeilmengen.put(hashCodeInt, aehnlicheTeilmengen);
        } else {
            aehnlicheTeilmengen.add(teilmenge);
        }
    }

    /**
     * Entfernt die �bergebene Teilmenge aus der Liste der Teilmengen mit
     * gleichem Hash-Code.
     *
     * @param teilmenge  Die Teilmenge, die aus der Liste der Teilmengen mit
     *                   gleichem Hash-Code entfernt werden soll.
     */
    private void hashCodeItmEntfernen(IndexTeilmenge teilmenge) {
        LinkedList aehnlicheTeilmengen;
        Integer    hashCodeInt;

        hashCodeInt = new Integer(teilmenge.hashCode());
        aehnlicheTeilmengen = (LinkedList) hashCodeTeilmengen.get(hashCodeInt);
        aehnlicheTeilmengen.remove(teilmenge);
        if (aehnlicheTeilmengen.isEmpty()) {
            hashCodeTeilmengen.remove(hashCodeInt);
        }
    }

    /**
     * Reduziert die Familie der ausgew�hlten Teilmengen wenn n�tig.
     */
    private void auswahlReduzieren() {
        IndexTeilmenge schlechtesteTeilmenge;
        boolean        abbrechen;

        abbrechen = false;
        while (teilmengeEntfernenMoeglich() && !abbrechen) {
            schlechtesteTeilmenge = gesamtSchlechtesteTeilmenge();
            if (schlechtesteTeilmenge == null) {
                // Es gibt keine Teilmenge, die entfernt werden k�nnte.
                abbrechen = true;
            } else {
                ausgewaehlteTeilmengen.teilmengeEntfernen(schlechtesteTeilmenge);
                hashCodeItmEntfernen(schlechtesteTeilmenge);
                hinzuErforderElemAktualisieren(schlechtesteTeilmenge, false);
                enthaltErforderElemAktualisieren(schlechtesteTeilmenge, false);
                datenAktualisieren(null, schlechtesteTeilmenge);
            }
        }
    }

    /**
     * F�gt die �bergebene Teilmenge einer Familie hinzu, aus der die
     * Auswahl erzeugt wird. Die �bergebene Teilmenge und die enthaltenen
     * Teilmengen m�ssen zu einer Gesamtmenge gleicher Gr��e geh�ren.
     *
     * @param teilmenge  Die der Familie hinzuzuf�gende Teilmenge.
     *
     * @return  <CODE>true</CODE>, wenn die �bergebene Teilmenge in die
     *          Auswahl aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     *
     * @throws SizeMismatchException  Die zur Teilmenge geh�rende Gesamtmenge
     *                                hat eine andere Gr��e als die
     *                                Gesamtmenge von <CODE>this</CODE>.
     *
     * @see mengenUeberdeckung.allgemein.SizeMismatchException
     */
    public boolean teilmengeHinzufuegen(IndexTeilmenge teilmenge) {
        LinkedList      aehnlicheTeilmengen;
        NutzenKostenItm vglTripel;
        IndexTeilmenge  vorhandeneTeilmenge;
        IndexTeilmenge  schlechtesteTeilmenge;
        Iterator        itmIter;
        Integer         hashCodeInt;
        float           hinzuNutzen, hinzuKosten;
        float           ergaenzterNutzen, ergaenzteKosten;
        boolean         ergaenztHinzu = false;
        boolean         selbstHinzu = false;

        if (teilmenge.groesseGesamtmenge() != groesseGesamtmenge()) {
            throw new SizeMismatchException("Die Gr��e der Gesamtmenge der"
                                            + " �bergebenen Teilmenge ist"
                                            + " unzul�ssig");
        }

        // Die Teilmenge wird zuerst der erg�nzten Auswahl �bergeben.
        if (ergaenzteAuswahl != null) {
            ergaenztHinzu = ergaenzteAuswahl.teilmengeHinzufuegen(teilmenge);
        }

        // Wenn die Teilmenge schon in der Familie enthalten ist, ist nichts
        // zu tun.
        if (ausgewaehlteTeilmengen.enthaelt(teilmenge)) {
            if (ergaenzteAuswahl != null) {
                return ergaenztHinzu;
            } else {
                return false;
            }
        }

        // Pr�fen, ob die �bergebene Teilmenge nur gleich gut oder schlechter
        // ist als eine enthaltene Teilmenge.
        hashCodeInt = new Integer(teilmenge.hashCode());
        aehnlicheTeilmengen = (LinkedList) hashCodeTeilmengen.get(hashCodeInt);
        if (aehnlicheTeilmengen != null) {
            // Es gibt vorhandene Teilmengen mit gleichem Hash-Code.
            itmIter = aehnlicheTeilmengen.iterator();
            while (itmIter.hasNext()) {
                vorhandeneTeilmenge = (IndexTeilmenge) itmIter.next();
                if (vorhandeneTeilmenge.istGleichOderBesser(teilmenge)) {
                    if (ergaenzteAuswahl != null) {
                        return ergaenztHinzu;
                    } else {
                        return false;
                    }
                }
            }
        }

        // Pr�fen, ob die �bergebene Teilmenge zus�tzlich aufgenommen werden
        // soll.
        if (zusaetzlicheAufnahmeMoeglich()) {
            ausgewaehlteTeilmengen.teilmengeHinzufuegen(teilmenge);
            hashCodeItmAufnehmen(teilmenge);
            if (ergaenzteAuswahl != null) {
                return ergaenztHinzu;
            } else {
                return true;
            }
        }

        // Falls die Werte der Attribute bisher noch nicht berechnet wurden,
        // sie jetzt erstmalig erzeugen.
        if (aufsteigendeTeilmengen == null) {
            maxAuswahlWurdeErreicht = true;
            datenErzeugen();
            auswahlReduzieren();
        }

        hinzuNutzen = reductionUtility.addUtility(teilmenge, ausgewaehlteTeilmengen);
        hinzuKosten = ausgewaehlteTeilmengen.kostenHinzufuegen(teilmenge);
        ergaenzterNutzen = aktuellerNutzen + hinzuNutzen;
        ergaenzteKosten = aktuelleKosten + hinzuKosten;

        // Pr�fen, ob die �bergebene Teilmenge aufgenommen werden soll.
        if (unabhaengigeAufnahmeMoeglich(teilmenge, hinzuKosten)) {
            // Die neue Teilmenge kann unabh�ngig von den Bewertungen der
            // vorhandenen Teilmengen aufgenommen werden.
            ausgewaehlteTeilmengen.teilmengeHinzufuegen(teilmenge);
            hashCodeItmAufnehmen(teilmenge);
            hinzuErforderElemAktualisieren(teilmenge, true);
            enthaltErforderElemAktualisieren(teilmenge, true);
            datenAktualisieren(teilmenge, null);
            selbstHinzu = true;
        }  else if (aktuelleKosten == 0) {
            // Die Kosten der neuen Teilmenge sind gr��er als Null, die
            // Kosten aller bisherigen Teilmengen sind aber gleich Null.
            // Dann wird die neue Teilmenge nicht aufgenommen, da keine
            // schlechtere Teilmenge entfernt werden kann.
            if (ergaenzteAuswahl != null) {
                return ergaenztHinzu;
            } else {
                return false;
            }
        } else {
            // Pr�fen, ob die neue Teilmenge zu einer besserer Bewertung
            // f�hren kann als eine vorhandene Bewertung.
            if (ergaenzterNutzen - minQuotient * ergaenzteKosten > 0) {
                // Die gr��te Bewertung der neuen Teilmenge ergibt sich
                // zusammen mit der vorhandenen Teilmenge mit den gr��ten
                // Kosten.
                vglTripel = (NutzenKostenItm) absteigendeTeilmengen.first();
            } else {
                // Die gr��te Bewertung der neuen Teilmenge ergibt sich
                // zusammen mit der vorhandenen Teilmenge mit den kleinsten
                // Kosten.
                vglTripel = (NutzenKostenItm) aufsteigendeTeilmengen.first();
            }
            if (!verbesserungMoeglich(ergaenzterNutzen, ergaenzteKosten,
                                      vglTripel.kosten())) {
                // Die �bergebene Teilmenge kann zu keiner Verbesserung
                // f�hren.
                if (ergaenzteAuswahl != null) {
                    return ergaenztHinzu;
                } else {
                    return false;
                }
            }

            // Es gibt also mindestens eine vorhandene Teilmenge, gegen�ber
            // der die �bergebene Teilmenge besser bewertet sein kann. Daher
            // die �bergebene Teilmenge zur Familie hinzunehmen und anhand
            // der tats�chlichen Bewertungen pr�fen, ob es eine schlechtere
            // Teilmenge gibt.
            ausgewaehlteTeilmengen.teilmengeHinzufuegen(teilmenge);
            enthaltErforderElemAktualisieren(teilmenge, true);
            schlechtesteTeilmenge = bessereSchlechtesteTeilmenge(ergaenzterNutzen,
                                                                 ergaenzteKosten);

            if (schlechtesteTeilmenge == null) {
                // Es gibt keine schlechtere Teilmenge als die �bergebene
                // Teilmenge. Diese wird daher wieder entfernt.
                ausgewaehlteTeilmengen.teilmengeEntfernen(teilmenge);
                enthaltErforderElemAktualisieren(teilmenge, false);
                if (ergaenzteAuswahl != null) {
                    return ergaenztHinzu;
                } else {
                    return false;
                }
            } else {
                // Die �bergebene Teilmenge bleibt in der Familie und die
                // schlechteste bisher enthaltene Teilmenge wird entfernt.
                hashCodeItmAufnehmen(teilmenge);
                hinzuErforderElemAktualisieren(teilmenge, true);
                ausgewaehlteTeilmengen.teilmengeEntfernen(schlechtesteTeilmenge);
                hashCodeItmEntfernen(schlechtesteTeilmenge);
                enthaltErforderElemAktualisieren(schlechtesteTeilmenge, false);
                hinzuErforderElemAktualisieren(schlechtesteTeilmenge, false);
                datenAktualisieren(teilmenge, schlechtesteTeilmenge);
                selbstHinzu = true;
            }
        }

        // Die Familie der ausgew�hlten Teilmengen reduzieren, wenn durch das
        // Aufnehmen der neuen Teilmenge andere Teilmengen entfernt werden
        // k�nnen und m�ssen.
        auswahlReduzieren();

        if (ergaenzteAuswahl != null) {
            return ergaenztHinzu;
        } else {
            return selbstHinzu;
        }
    }

    /**
     * Setzt die minimale Anzahl der Teilmengen, die in der Familie enthalten
     * sein kann. Bei einem negativen Wert werden alle Teilmengen aufgenommen
     * und es findet keine Auswahl statt. Wenn die Anzahl gegen�ber der
     * bisherigen Anzahl verringert wird oder die bisherigen Anzahl
     * unbeschr�nkt war und die neue beschr�nkt ist, werden gegebenenfalls zu
     * viel enthaltene Teilmengen entfernt.
     *
     * @param minItmAnzahl  Die neue minimale Anzahl der Teilmengen, die in
     *                      der Familie enthalten sein soll.
     */
    public void setzeMinItmAnz(int minItmAnzahl) {

        // Die m�gliche Anzahl der Teilmengen ver�ndern.
        this.minItmAnzahl = minItmAnzahl;

        // Wenn eine zus�tzliche Aufnahme weiterer Teilmengen nicht m�glich
        // ist und die Werte der Attribute bisher noch nicht berechnet wurden,
        // sie jetzt erstmalig erzeugen.
        if (!zusaetzlicheAufnahmeMoeglich()) {
            maxAuswahlWurdeErreicht = true;
            if (aufsteigendeTeilmengen == null) {
                datenErzeugen();
            }
            auswahlReduzieren();
        }
    }

    /**
     * Gibt an, ob seit der Existens dieser Instanz die maximale Anzahl der
     * auszuw�hlenden Teilmengen schon einmal erreicht wurde, so da� eine
     * neue Teilmenge nicht aufgenommen oder eine vorhandene entfernt werden
     * mu�te.
     *
     * @return  Die Angabe, ob seit der Existens dieser Instanz die maximale
     *          Anzahl der auszuw�hlenden Teilmengen schon einmal erreicht
     *          wurde.
     */
    public boolean maxAuswahlWurdeErreicht() {

        if (ergaenzteAuswahl != null) {
            return ergaenzteAuswahl.maxAuswahlWurdeErreicht();
        } else {
            return maxAuswahlWurdeErreicht;
        }
    }

    /**
     * Liefert die Gr��e der Familie der ausgew�hlten Teilmengen.
     *
     * @return  Die Gr��e der Familie der ausgew�hlten Teilmengen.
     */
    public int groesseAuswahl() {

        if (ergaenzteAuswahl != null) {
            return ergaenzteAuswahl.groesseAuswahl();
        } else {
            return ausgewaehlteTeilmengen.groesseFamilie();
        }
    }

    /**
     * Liefert eine neue Familie, die aus den ausgew�hlten IndexTeilmengen
     * besteht (die konkret Objekte von Unterklassen von IndexTeilmenge
     * sein k�nnen). Dabei soll die Auswahl weiterhin die IndexTeilmengen
     * enthalten, aus denen eine m�glichst umfassende �berdeckung der
     * Gesamtmenge mit m�glichst geringen Kosten erzeugt werden kann. Wenn
     * eine erg�nzte Auswahl erstellt wurde, werden deren Teilmengen
     * geliefert. Das gelieferte Objekt hat den gleichen Typ wie das Objekt,
     * das dem Konstruktor als erster Parameter �bergeben wurde.
     *
     * @return  Eine neu erzeugte Familie, die eine Auswahl der hinzugef�gten
     *          Teilmengen (oder von Unterklassen davon) enth�lt.
     */
    public ItmFamilie auswahl() {

        if (ergaenzteAuswahl != null) {
            return ergaenzteAuswahl.auswahl();
        } else {
            return ((ItmFamilie) ausgewaehlteTeilmengen.clone());
        }
    }
}

