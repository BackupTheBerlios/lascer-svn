/*
 * Dateiname      : SofortigeAuswahl.java
 * Letzte Änderung: 28. November 2007
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
 * jeweils ausgewählten Teilmengen gespeichert.<P>
 *
 * Die Anzahl der zu speichernden Teilmengen wird im Konstruktor übergeben.
 * Auf jeden Fall gespeichert werden Teilmengen mit Kosten von Null und
 * notwendige Teilmengen.
 *
 * @author  Dietmar Lippold
 */
public class SofortigeAuswahl extends ItmAuswahlErzeugung {

    /**
     * Eine ergänzte Auswahl, der ebenfalls alle Teilmengen übergeben werden,
     * die diesem Objekt hinzugefügt werden, oder der Wert <CODE>null</CODE>,
     * wenn keine ergänzte Auswahl vorhanden ist. Wenn sie vorhanden ist,
     * enthält sie die eigentlich ausgewählten Teilmengen und dieses Objekt,
     * bzw. konkret die Familie <CODE>ausgewaehlteTeilmengen</CODE>, enthält
     * nur die notwendigen Teilmengen und Teilmengen mit Kosten von Null.
     */
    private SofortigeAuswahl ergaenzteAuswahl = null;

    /**
     * Die zur Auswahl der Teilmengen zu benutzte Bewertungsfunktion.
     */
    private ReductionUtility reductionUtility;

    /**
     * Die Familie, die die ausgewählten Teilmengen enthält.
     */
    private ItmFamilie ausgewaehlteTeilmengen;

    /**
     * Tripel aus den ausgewählten Teilmengen zusammen mit ihren relativen
     * Nutzen und Kosten aufsteigend bezüglich der Kosten sortiert. Die
     * relativen Nutzen und Kosten sind die, um die sich der Nutzen bzw. die
     * Kosten der Familie bei Entfernung der Teilmenge verringern. Die Objekte
     * sind vom Typ <CODE>NutzenKostenItm</CODE>.
     */
    private TreeSet aufsteigendeTeilmengen = null;

    /**
     * Tripel aus den ausgewählten Teilmengen zusammen mit ihren relativen
     * Nutzen und Kosten absteigend bezüglich der Kosten sortiert. Die
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
     * <CODE>minUeberdeckAnz</CODE> mal überdeckt sind.
     */
    private IndexTeilmenge hinzuErforderlichElem;

    /**
     * Die Elemente, durch die eine enthaltene Teilmenge zu einer
     * erforderlichen Teilmenge wird. Das sind die Elemente, die maximal
     * <CODE>minUeberdeckAnz</CODE> mal überdeckt sind.
     */
    private IndexTeilmenge enthaltenErforderlichElem;

    /**
     * Das Verhältnis der Anzahl der Teilmengen in <CODE>ergaenzteAuswahl</CODE>
     * zur Anzahl der Teilmengen in <CODE>ausgewaehlteTeilmengen</CODE>. Wenn
     * keine ergänzte Ausahl vorhanden ist, ist der Wert ohne Bedeutung.
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
     * Die minimale Anzahl der Teilmengen, die in der Familie der ergänzenden
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
     * Die minimale Häufigkeit, mit der jedes Element überdeckt sein soll.
     * Ein Wert von Null bedeutet, daß keine minimale Häuigkeit gefordert
     * wird.
     */
    private int minUeberdeckAnz;

    /**
     * Gibt an, ob seit der Existens dieser Instanz die maximale Anzahl der
     * auszuwählenden Teilmengen schon einmal erreicht wurde
     */
    private boolean maxAuswahlWurdeErreicht = false;

    /**
     * Erzeugt eine neue Instanz zur Auswahl von Teilmengen. In der Auswahl
     * ist mindestens die übergebene Anzahl von Teilmengen enthalten. Außerdem
     * sind Teilmengen mit Kosten von Null und notwendige Teilmengen
     * enthalten.
     *
     * @param ausgangsFamilie   Eine Familie, in deren Kopie die ausgewählten
     *                          Teilmengen aufgenommen werden. In der Regel
     *                          enthält diese noch keine Teilmenge.
     * @param minItmAnzahl      Die minimale Anzahl der Teilmengen, die in
     *                          der Familie enthalten sein soll. Bei einem
     *                          negativen Wert werden alle Teilmengen
     *                          aufgenommen und es findet keine Auswahl
     *                          statt.
     * @param minUeberdeckAnz   Die minimale Häufigkeit, mit der jedes
     *                          Element überdeckt sein soll. Ein Wert von
     *                          Null bedeutet, daß keine minimale Häuigkeit
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
     * einer Instanz zur ergänzten Auswahl. Diese Auswahl, also nicht die
     * ergänzte Auswahl, ist eine minimale Auswahl, in der nur Teilmengen
     * enthalten sind, die notwendig sind oder deren Kosten Null sind. Die
     * Anzahl der Teilmengen in der ergänzten Auswahl ist mindestens so groß
     * wie das Produkt aus der Anzahl der Teilmengen dieser Auswahl und aus
     * dem übergebenen Faktor. Sie ist außerdem mindestens so groß wie die
     * übergebene Anzahl. Wenn die übergebene Anzahl von Mengen negativ ist,
     * also keine Auswahl erfolgt, wird keine ergänzte Auswahl erzeugt.
     *
     * @param ausgangsFamilie   Eine Familie, in deren Kopie die ausgewählten
     *                          Teilmengen aufgenommen werden. In der Regel
     *                          enthält diese noch keine Teilmenge.
     * @param minItmAnzahl      Die minimale Anzahl der Teilmengen, die in
     *                          der Familie enthalten sein soll. Bei einem
     *                          negativen Wert werden alle Teilmengen
     *                          aufgenommen und es findet keine Auswahl
     *                          statt.
     * @param minUeberdeckAnz   Die minimale Häufigkeit, mit der jedes
     *                          Element überdeckt sein soll. Ein Wert von
     *                          Null bedeutet, daß keine minimale Häuigkeit
     *                          gefordert wird.
     * @param faktorErgAnz      Das Verhältnis der Anzahl der Teilmengen
     *                          der ergänzten Auswahl und dieser Auswahl.
     * @param reductionUtility  Das Verfahren, das zur Berechnung des
     *                          Nutzens der Teilmengen und der Familie
     *                          dieser und der ergänzten Auswahl verwendet
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
     * neuen Teilmenge möglich ist. Dazu wird die Bewertung der Familie nach
     * Addition des mittels <CODE>minQuotient</CODE> abgeschätzten Nutzens und
     * Subtraktion der angegebenen Kosten mit der derzeitigen Bewertung der
     * Familie verglichen.
     *
     * @param ergaenzterNutzen  Der Nutzenwert der Familie nach der Aufnahme
     *                          der neuen Teilmenge.
     * @param ergaenzteKosten   Der Kostenwert der Familie nach der Aufnahme
     *                          der neuen Teilmenge.
     * @param wegKosten         Die geschätzten Kosten, die bei der Entfernung
     *                          der enthaltenen Teilmenge wegfallen.
     *
     * @return  Die Beurteilung, ob nach der Hinzunahme der neuen Teilmenge
     *          die Entfernung der enthaltenen Teilmenge zu einer besseren
     *          Bewertung der Familie führen kann als derzeit.
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
     * Prüft, ob eine Teilmenge zusätzlich zu den vorhandenen Teilmengen in
     * die Familie aufgenommen werden kann.
     *
     * @return  <CODE>true</CODE>, wenn eine Teilmenge zusätzlich aufgenommen
     *          werden kann, sonst <CODE>false</CODE>.
     */
    private boolean zusaetzlicheAufnahmeMoeglich() {
        return ((minItmAnzahl < 0)
                || (ausgewaehlteTeilmengen.groesseFamilie() < minItmAnzahl));
    }

    /**
     * Prüft, ob die übergebene Teilmenge unabhängig von den Bewertungen der
     * vorhandenen Teilmengen aufgenommen werden soll.
     *
     * @param teilmenge    Die Teilmenge, von der geprüft werden soll, ob sie
     *                     unabhängig von den Bewertungen der vorhandenen
     *                     Teilmengen aufgenommen werden soll.
     * @param hinzuKosten  Die bei der Aufnahme der Teilmenge hinzukommenden
     *                     Kosten.
     *
     * @return  <CODE>true</CODE>, wenn die übergebene Teilmenge unabhängig
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
     * kann. Dafür müssen mehr Teilmengen als die vorgegebene Mindestanzahl
     * vorhanden sein und außerdem muß mindestens eine nicht notwendige
     * Teilmengen und eine Teilmenge mit Kosten größer als Null vorhanden
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
            // Die Anzahl der Teilmengen in der ergänzten Auswahl neu setzen.
            // Diese ergibt sich aus der Multiplikation der für eine vollständige
            // Überdeckung maximal erforderlichen Anzahl von Teilmengen mit
            // dem vorgegebenen Faktor. Außerdem ist die minimale Anzahl der
            // Teilmengen zur berücksichtigen.
            teilmengenAnz = ausgewaehlteTeilmengen.groesseFamilie();
            nichtUeberdecktAnz = ausgewaehlteTeilmengen.anzNichtUeberdeckt();
            prodAnz = Math.round((teilmengenAnz + nichtUeberdecktAnz) * faktorErgAnz);
            ergaenzteAuswahl.setzeMinItmAnz(Math.max(prodAnz, ergaenzteMinItmAnz));
        }
    }

    /**
     * Aktualisiert die Menge der für eine neue Teilmenge erforderlichen
     * Elemente nach Hinzunahme oder Entfernung einer Teilmenge zu bzw. aus
     * <CODE>ausgewaehlteTeilmengen</CODE>.
     *
     * @param teilmenge  Eine Teilmenge, die in <CODE>ausgewaehlteTeilmengen</CODE>
     *                   aufgenommen oder daraus entfernt wurde.
     * @param hinzu      Gibt an, ob die übergebene Teilmenge aufgenommen wurde.
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
                    // Prüfen, ob das Element nicht mehr erforderlich ist.
                    if (ueberdeckAnz == minUeberdeckAnz) {
                        hinzuErforderlichElem.indexLoeschen(index);
                    }
                } else {
                    // Prüfen, ob das Element neu erforderlich ist.
                    if (ueberdeckAnz == minUeberdeckAnz - 1) {
                        hinzuErforderlichElem.indexAufnehmen(index);
                    }
                }
            }
        }
    }

    /**
     * Aktualisiert die Menge der für eine vorhandene Teilmenge erforderlichen
     * Elemente nach Hinzunahme oder Entfernung einer Teilmenge zu bzw. aus
     * <CODE>ausgewaehlteTeilmengen</CODE>.
     *
     * @param teilmenge  Eine Teilmenge, die in <CODE>ausgewaehlteTeilmengen</CODE>
     *                   aufgenommen oder daraus entfernt wurde.
     * @param hinzu      Gibt an, ob die übergebene Teilmenge aufgenommen wurde.
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
                    // Prüfen, ob das Element nicht mehr erforderlich ist.
                    if (ueberdeckAnz == minUeberdeckAnz + 1) {
                        enthaltenErforderlichElem.indexLoeschen(index);
                    }
                } else {
                    // Prüfen, ob das Element neu erforderlich ist.
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
     * Entfernung des Tripels der zu entfernenden Teilmenge neue Werte für
     * Nutzen und Kosten zugewiesen.<P>
     *
     * Aufrufe dieser Methode können auch durch Aufrufe der Methode
     * <CODE>datenErzeugen</CODE> ersetzt werden. Gegenüber der Methode
     * verbraucht diese Methode aber weniger Objekte.
     *
     * @param neueTeilmenge  Die Teilmenge, die in <CODE>ausgewaehlteTeilmengen</CODE>
     *                       neu aufgenommen wurde. Wenn keine aufgenommen wurde,
     *                       wird der Wert <CODE>null</CODE> übergeben.
     * @param wegTeilmenge   Die Teilmenge, die aus <CODE>ausgewaehlteTeilmengen</CODE>
     *                       entfernt wurde. Wenn keine entfernt wurde, wird der
     *                       Wert <CODE>null</CODE> übergeben.
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
            // Die Anzahl der Teilmengen in der ergänzten Auswahl neu setzen.
            // Diese ergibt sich aus der Multiplikation der für eine vollständige
            // Überdeckung maximal erforderlichen Anzahl von Teilmengen mit
            // dem vorgegebenen Faktor. Außerdem ist die minimale Anzahl der
            // Teilmengen zur berücksichtigen.
            teilmengenAnz = ausgewaehlteTeilmengen.groesseFamilie();
            nichtUeberdecktAnz = ausgewaehlteTeilmengen.anzNichtUeberdeckt();
            prodAnz = Math.round((teilmengenAnz + nichtUeberdecktAnz) * faktorErgAnz);
            ergaenzteAuswahl.setzeMinItmAnz(Math.max(prodAnz, ergaenzteMinItmAnz));
        }
    }

    /**
     * Liefert die schlechteste Teilmenge, durch deren Entfernung also die
     * größte Bewertung entsteht, wenn diese Bewertung größer ist als die
     * ursprüngliche Bewertung. Wenn die Bewertung nicht größer ist, wird
     * der Wert <CODE>null</CODE> geliefert. Die Vorbedingung ist, daß die
     * Kosten der bisherigen Familie größer als Null waren.
     *
     * @param ergaenzterNutzen  Der Nutzen der Familie nach Hinzunahme der
     *                          neuen Teilmenge.
     * @param ergaenzteKosten   Die Kosten der Familie nach Hinzunahme der
     *                          neuen Teilmenge.
     *
     * @return  Die schlechteste Teilmenge, durch deren Entfernung eine
     *          größere Bewertung als ursprünglich vorhanden entsteht, oder
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

        // Den Iterator für die Tripel ermitteln.
        if (ergaenzterNutzen - minQuotient * ergaenzteKosten > 0) {
            tripelIter = absteigendeTeilmengen.iterator();
        } else {
            tripelIter = aufsteigendeTeilmengen.iterator();
        }

        // Unter allen vorhandenen Teilmengen, gegenüber denen die übergebene
        // Teilmenge besser bewertet sein kann, diejenige ermitteln, nach
        // deren Entfernung sich die größte Bewertung der Familie ergibt.
        // Diese Bewertung muß jedoch größere sein als die der ursprünglichen
        // Familie.
        groessteBewertung = 0;
        schlechtesteTeilmenge = null;
        abbrechen = false;
        while (tripelIter.hasNext() && (!abbrechen)) {
            vglTripel = (NutzenKostenItm) tripelIter.next();
            vglTeilmenge = vglTripel.teilmenge();

            // Prüfen, ob die Bewertung nach Entfernung von vglTeilmenge
            // schlechter ist als die ursprünglichen Bewertung. Damit wäre
            // die Bewertung auch für alle folgenden Tripel schlechter.
            abbrechen = !verbesserungMoeglich(ergaenzterNutzen, ergaenzteKosten,
                                              vglTripel.kosten());
            if (!abbrechen) {
                // Eine Entfernung von vglTeilmenge kann nur dann zu einer
                // größeren Bewertung als bisher führen, wenn der konkrete
                // Schätzwert größer als die aktuelle Bewertung ist.
                geschNutzen = ergaenzterNutzen - vglTripel.nutzen();
                geschKosten = ergaenzteKosten - vglTripel.kosten();
                if (geschNutzen / geschKosten > aktuellerNutzen / aktuelleKosten) {

                    // Es kommen nur nicht notwendige und nicht erforderliche
                    // Teilmengen zur Entfernung in Frage.
                    if (!ausgewaehlteTeilmengen.teilmengeIstNotwendig(vglTeilmenge)
                        && vglTeilmenge.istDisjunkt(enthaltenErforderlichElem)) {

                        // Vergleich der Bewertung nach Entfernung von
                        // vglTeilmenge mit der bisher größten Bewertung.
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
        // Entfernung entstehende Bewertung besser ist als die ursprüngliche
        // Bewertung. Ansonsten wird null geliefert.
        if (groessteBewertung > (aktuellerNutzen / aktuelleKosten)) {
            return schlechtesteTeilmenge;
        } else {
            return null;
        }
    }

    /**
     * Liefert die schlechteste nicht notwendige Teilmenge, durch deren
     * Entfernung also die größte Bewertung entsteht. Die Vorbedingung ist,
     * daß die Kosten der Familie größer als Null sind.
     *
     * @return  Die schlechteste nicht notwendige und nicht erforderliche
     *          Teilmenge.
     */
    private IndexTeilmenge gesamtSchlechtesteTeilmenge() {
        IndexTeilmenge vglTeilmenge, schlechtesteTeilmenge;
        Iterator       itmIter;
        float          groessteBewertung, neueBewertung;
        float          wegNutzen, wegKosten;

        // Einen Iterator über alle Teilmengen, die entfernt werden können,
        // erzeugen.
        itmIter = ausgewaehlteTeilmengen.nichtNotwendigeTeilmengen().iterator();

        // Unter allen nicht notwendigen Teilmengen die ermitteln, nach deren
        // Entfernung sich die größte Bewertung der Familie ergibt.
        groessteBewertung = 0;
        schlechtesteTeilmenge = null;
        while (itmIter.hasNext()) {
            vglTeilmenge = (IndexTeilmenge) itmIter.next();

            // Es sind nur Teilmengen zu berücksichtigen, die nicht
            // erforderlich sind.
            if (vglTeilmenge.istDisjunkt(enthaltenErforderlichElem)) {
                // Ermitteln der Bewertung nach Entfernung von vglTeilmenge.
                wegNutzen = reductionUtility.rmvUtility(vglTeilmenge,
                                                        ausgewaehlteTeilmengen);
                wegKosten = ausgewaehlteTeilmengen.kostenEntfernen(vglTeilmenge);
                if (aktuelleKosten - wegKosten == 0) {
                    // vglTeilmenge hat als einzige Kosten größer als Null.
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
     * Nimmt die übergebene Teilmenge in die Liste der Teilmengen mit gleichem
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
     * Entfernt die übergebene Teilmenge aus der Liste der Teilmengen mit
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
     * Reduziert die Familie der ausgewählten Teilmengen wenn nötig.
     */
    private void auswahlReduzieren() {
        IndexTeilmenge schlechtesteTeilmenge;
        boolean        abbrechen;

        abbrechen = false;
        while (teilmengeEntfernenMoeglich() && !abbrechen) {
            schlechtesteTeilmenge = gesamtSchlechtesteTeilmenge();
            if (schlechtesteTeilmenge == null) {
                // Es gibt keine Teilmenge, die entfernt werden könnte.
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
     * Fügt die übergebene Teilmenge einer Familie hinzu, aus der die
     * Auswahl erzeugt wird. Die übergebene Teilmenge und die enthaltenen
     * Teilmengen müssen zu einer Gesamtmenge gleicher Größe gehören.
     *
     * @param teilmenge  Die der Familie hinzuzufügende Teilmenge.
     *
     * @return  <CODE>true</CODE>, wenn die übergebene Teilmenge in die
     *          Auswahl aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     *
     * @throws SizeMismatchException  Die zur Teilmenge gehörende Gesamtmenge
     *                                hat eine andere Größe als die
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
            throw new SizeMismatchException("Die Größe der Gesamtmenge der"
                                            + " übergebenen Teilmenge ist"
                                            + " unzulässig");
        }

        // Die Teilmenge wird zuerst der ergänzten Auswahl übergeben.
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

        // Prüfen, ob die übergebene Teilmenge nur gleich gut oder schlechter
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

        // Prüfen, ob die übergebene Teilmenge zusätzlich aufgenommen werden
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

        // Prüfen, ob die übergebene Teilmenge aufgenommen werden soll.
        if (unabhaengigeAufnahmeMoeglich(teilmenge, hinzuKosten)) {
            // Die neue Teilmenge kann unabhängig von den Bewertungen der
            // vorhandenen Teilmengen aufgenommen werden.
            ausgewaehlteTeilmengen.teilmengeHinzufuegen(teilmenge);
            hashCodeItmAufnehmen(teilmenge);
            hinzuErforderElemAktualisieren(teilmenge, true);
            enthaltErforderElemAktualisieren(teilmenge, true);
            datenAktualisieren(teilmenge, null);
            selbstHinzu = true;
        }  else if (aktuelleKosten == 0) {
            // Die Kosten der neuen Teilmenge sind größer als Null, die
            // Kosten aller bisherigen Teilmengen sind aber gleich Null.
            // Dann wird die neue Teilmenge nicht aufgenommen, da keine
            // schlechtere Teilmenge entfernt werden kann.
            if (ergaenzteAuswahl != null) {
                return ergaenztHinzu;
            } else {
                return false;
            }
        } else {
            // Prüfen, ob die neue Teilmenge zu einer besserer Bewertung
            // führen kann als eine vorhandene Bewertung.
            if (ergaenzterNutzen - minQuotient * ergaenzteKosten > 0) {
                // Die größte Bewertung der neuen Teilmenge ergibt sich
                // zusammen mit der vorhandenen Teilmenge mit den größten
                // Kosten.
                vglTripel = (NutzenKostenItm) absteigendeTeilmengen.first();
            } else {
                // Die größte Bewertung der neuen Teilmenge ergibt sich
                // zusammen mit der vorhandenen Teilmenge mit den kleinsten
                // Kosten.
                vglTripel = (NutzenKostenItm) aufsteigendeTeilmengen.first();
            }
            if (!verbesserungMoeglich(ergaenzterNutzen, ergaenzteKosten,
                                      vglTripel.kosten())) {
                // Die übergebene Teilmenge kann zu keiner Verbesserung
                // führen.
                if (ergaenzteAuswahl != null) {
                    return ergaenztHinzu;
                } else {
                    return false;
                }
            }

            // Es gibt also mindestens eine vorhandene Teilmenge, gegenüber
            // der die übergebene Teilmenge besser bewertet sein kann. Daher
            // die übergebene Teilmenge zur Familie hinzunehmen und anhand
            // der tatsächlichen Bewertungen prüfen, ob es eine schlechtere
            // Teilmenge gibt.
            ausgewaehlteTeilmengen.teilmengeHinzufuegen(teilmenge);
            enthaltErforderElemAktualisieren(teilmenge, true);
            schlechtesteTeilmenge = bessereSchlechtesteTeilmenge(ergaenzterNutzen,
                                                                 ergaenzteKosten);

            if (schlechtesteTeilmenge == null) {
                // Es gibt keine schlechtere Teilmenge als die übergebene
                // Teilmenge. Diese wird daher wieder entfernt.
                ausgewaehlteTeilmengen.teilmengeEntfernen(teilmenge);
                enthaltErforderElemAktualisieren(teilmenge, false);
                if (ergaenzteAuswahl != null) {
                    return ergaenztHinzu;
                } else {
                    return false;
                }
            } else {
                // Die übergebene Teilmenge bleibt in der Familie und die
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

        // Die Familie der ausgewählten Teilmengen reduzieren, wenn durch das
        // Aufnehmen der neuen Teilmenge andere Teilmengen entfernt werden
        // können und müssen.
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
     * und es findet keine Auswahl statt. Wenn die Anzahl gegenüber der
     * bisherigen Anzahl verringert wird oder die bisherigen Anzahl
     * unbeschränkt war und die neue beschränkt ist, werden gegebenenfalls zu
     * viel enthaltene Teilmengen entfernt.
     *
     * @param minItmAnzahl  Die neue minimale Anzahl der Teilmengen, die in
     *                      der Familie enthalten sein soll.
     */
    public void setzeMinItmAnz(int minItmAnzahl) {

        // Die mögliche Anzahl der Teilmengen verändern.
        this.minItmAnzahl = minItmAnzahl;

        // Wenn eine zusätzliche Aufnahme weiterer Teilmengen nicht möglich
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
     * auszuwählenden Teilmengen schon einmal erreicht wurde, so daß eine
     * neue Teilmenge nicht aufgenommen oder eine vorhandene entfernt werden
     * mußte.
     *
     * @return  Die Angabe, ob seit der Existens dieser Instanz die maximale
     *          Anzahl der auszuwählenden Teilmengen schon einmal erreicht
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
     * Liefert die Größe der Familie der ausgewählten Teilmengen.
     *
     * @return  Die Größe der Familie der ausgewählten Teilmengen.
     */
    public int groesseAuswahl() {

        if (ergaenzteAuswahl != null) {
            return ergaenzteAuswahl.groesseAuswahl();
        } else {
            return ausgewaehlteTeilmengen.groesseFamilie();
        }
    }

    /**
     * Liefert eine neue Familie, die aus den ausgewählten IndexTeilmengen
     * besteht (die konkret Objekte von Unterklassen von IndexTeilmenge
     * sein können). Dabei soll die Auswahl weiterhin die IndexTeilmengen
     * enthalten, aus denen eine möglichst umfassende Überdeckung der
     * Gesamtmenge mit möglichst geringen Kosten erzeugt werden kann. Wenn
     * eine ergänzte Auswahl erstellt wurde, werden deren Teilmengen
     * geliefert. Das gelieferte Objekt hat den gleichen Typ wie das Objekt,
     * das dem Konstruktor als erster Parameter übergeben wurde.
     *
     * @return  Eine neu erzeugte Familie, die eine Auswahl der hinzugefügten
     *          Teilmengen (oder von Unterklassen davon) enthält.
     */
    public ItmFamilie auswahl() {

        if (ergaenzteAuswahl != null) {
            return ergaenzteAuswahl.auswahl();
        } else {
            return ((ItmFamilie) ausgewaehlteTeilmengen.clone());
        }
    }
}

