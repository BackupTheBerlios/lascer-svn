/*
 * Dateiname      : AlgVollErzKonzepte.java
 * Letzte Änderung: 21. November 2007
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2007
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


package lascer.konzepte.mengen;

import java.util.Random;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import mathCollection.BitMathIntSet;

import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.konvertierung.BegrenzteUeberdeckung;
import mengenUeberdeckung.optimierung.InferiorOptimization;
import mengenUeberdeckung.optimierung.AddOneOptimization;
import mengenUeberdeckung.optimierung.SequenceOptimization;
import mengenUeberdeckung.iteratedGreedyCovering.AddOneSolutionSet;
import mengenUeberdeckung.iteratedGreedyCovering.IterEnhancedGreedyHeuristic;
import mengenUeberdeckung.reduktion.ItmAuswahlErzeugung;
import mengenUeberdeckung.reduktion.SofortigeAuswahl;
import mengenUeberdeckung.doppelstruktur.DoppelItmFamilie;
import mengenUeberdeckung.doppelstruktur.SumDoppelItmFamilie;
import mengenUeberdeckung.doppelstruktur.QuotDoppelItmFamilie;
import mengenUeberdeckung.heuristiken.costs.FrequencyCosts;
import mengenUeberdeckung.heuristiken.utility.FrequencyUtility;

import lascer.konzepte.Konzept;
import lascer.konzepte.einzelne.Disjunktion;
import lascer.problemdaten.Beispieldaten;

/**
 * Repräsentiert eine Menge allgemeiner Konzepte zur Erzeugung vollständiger
 * Konzepte. Die Menge stellt eine Auswahl der zur Aufnahme übergebenen
 * Konzepte dar.
 *
 * @author  Dietmar Lippold
 */
public class AlgVollErzKonzepte {

    /**
     * Der Logger dieser Klasse.
     */
    private static final Logger LOGGER
        = Logger.getLogger(AlgVollErzKonzepte.class.getName());

    /**
     * Der zu verwendende Zufallsgenerator.
     */
    private final Random random;

    /**
     * Die der Konzeptbildung zugrunde liegenden Beispieldaten.
     */
    private final Beispieldaten beispieldaten;

    /**
     * Das Verfahren zur Auswahl und Verwaltung der Teilmengen zu den
     * Konzepten.
     */
    private ItmAuswahlErzeugung konzeptItmAuswahl;

    /**
     * Die Indices der noch nicht ausgeschlossenen negativen Beispiele.
     */
    private BitMathIntSet nichtAusgeschlosenBsp;

    /**
     * Der Faktor, mit dem die Summe der Kosten der ausgeschlossenen Indices
     * multipliziert wird, um die Kosten für einen nicht ausgeschlossenen
     * Index zu errechnen.
     */
    private float kostenSummenFaktor;

    /**
     * Die Anzahl der Nachkomma-Stellen, die bei der Addition in der Klasse
     * <CODE>FixedExistentCosts</CODE> berücksichtigt werden sollen.
     */
    private int nachkommaAnz;

    /**
     * Die Anzahl der Literale, die maximal in einer Disjunktion bzw. in einer
     * Konjunktion enthalten sein sollen. Der Wert Null steht für eine
     * unbegrenzte Anzahl.
     */
    private int maxLiteralAnz;

    /**
     * Gibt an, in welchem Ausmaß die Teilmengen besonders Speicher-effizient
     * aber dadurch weniger Laufzeit-effizient verwaltet werden sollen. Der
     * Wert ist gleich oder größer Null (maximale Laufzeit-Effizienz) und
     * kleiner oder gleich Zwei (maximale Speicher-Effizienz).
     */
    private int speicherEffizienz;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param random              Der zu verwendende Zufallsgenerator.
     * @param beispieldaten       Die dem Konzept zugrunde gelegten
     *                            Beispieldaten.
     * @param negAusschlussGuete  Güte, mit der die einzelnen negativen
     *                            Beispiele von den vorhandenen vollständigen
     *                            Konzepten ausgeschlossen werden.
     * @param kostenFaktor        Faktor, mit dem die Summe der Kosten der
     *                            ausgeschlossenen Indices multipliziert wird,
     *                            um die Kosten für einen nicht
     *                            ausgeschlossenen Index zu errechnen.
     * @param algMinItmAnz        Die minimale Anzahl der zu speichernden
     *                            Teilmengen zur Auswahl der allgemeinen
     *                            Konzeptmengen. Ein negativer Wert steht für
     *                            eine unbeschränkte Anzahl.
     * @param maxLiteralAnz       Die Anzahl der Literale, die maximal in
     *                            einer Disjunktion bzw. Konjunktion enthalten
     *                            sein sollen. Der Wert Null steht für eine
     *                            unbegrenzte Anzahl.
     * @param speicherEffizienz   Gibt an, in welchem Ausmaß die Teilmengen
     *                            besonders Speicher-effizient aber dadurch
     *                            weniger Laufzeit-effizient verwaltet werden
     *                            sollen. Der Wert muß zwischen Null und Zwei
     *                            liegen.
     */
    public AlgVollErzKonzepte(Random random, Beispieldaten beispieldaten,
                              float[] negAusschlussGuete, float kostenFaktor,
                              int algMinItmAnz, int maxLiteralAnz,
                              int speicherEffizienz) {
        DoppelItmFamilie konzeptFamilie;
        float[] indexKosten;

        this.random = random;
        this.beispieldaten = beispieldaten;
        this.nichtAusgeschlosenBsp = wertNullIndices(negAusschlussGuete);
        this.kostenSummenFaktor = kostenFaktor;
        this.maxLiteralAnz = maxLiteralAnz;
        this.speicherEffizienz = speicherEffizienz;

        indexKosten = indexKosten(negAusschlussGuete);
        konzeptFamilie = new SumDoppelItmFamilie(beispieldaten.posBspAnz(),
                                                 new FrequencyCosts(indexKosten));
        konzeptItmAuswahl = new SofortigeAuswahl(konzeptFamilie, algMinItmAnz,
                                                 Konstanten.ALG_MIN_UEBERDECK_ANZ,
                                                 Konstanten.ALG_FAKTOR_ERG_ANZ,
                                                 new FrequencyUtility(false));
    }

    /**
     * Liefert die Indices der Werte, die nicht Null sind.
     *
     * @param werte  Die Werte, zu denen die Indices geliefert werden.
     *
     * @return  Die Indices der übergebenen Werte, die nicht Null sind.
     */
    private BitMathIntSet wertNullIndices(float[] werte) {
        BitMathIntSet wertNullIndices = new BitMathIntSet();

        for (int i = 0; i < werte.length; i++) {
            if (werte[i] == 0) {
                wertNullIndices.add(i);
            }
        }
        return wertNullIndices;
    }

    /**
     * Ermittelt, ob das übergebene Konzept nutzlos ist. Dies ist dann der
     * Fall, wenn es noch nicht ausgeschlossene negative Beispiele gibt aber
     * das Konzept von diesen keines ausschließt.
     *
     * @param konzept             Das Konzept, von dem ermittelt werden soll,
     *                            ob es nutzlos ist.
     * @param nichtAusgeschlosen  Die Indices der negativen Beispiele, die
     *                            bisher noch nicht ausgeschlossen sind.
     *
     * @return  <CODE>true</CODE>, wenn das Konzept nutzlos ist, sonst
     *          <CODE>false</CODE>.
     */
    private boolean konzeptIstNutzlos(Konzept konzept,
                                      BitMathIntSet nichtAusgeschlosen) {

        if (nichtAusgeschlosen.isEmpty()) {
            return false;
        } else {
            return konzept.negErfuelltBsp().isSuperset(nichtAusgeschlosen);
        }
    }

    /**
     * Liefert die Kosten für das Enthaltensein eines einzelnen negativen
     * Beispiels in einem allgemeinen Konzept.
     *
     * @param ausschlussGuete  Für jedes negativen Beispiel die Güte, mit der
     *                         es schon ausgeschlossen wird. Dieser Wert muß
     *                         größer als Null sein.
     * @param exponent         Der Exponent, der bei der Berechnung der Kosten
     *                         verwendet werden soll.
     *
     * @return  Ein Array, in dem für jedes negativen Beispiel die Kosten für
     *          das Enthaltensein des Beispiels in einem allgemeinen Konzept
     *          angegeben sind.
     */
    private float einzelIndexKosten(float ausschlussGuete, float exponent) {

        if (exponent == 0) {
            return 1;
        } else if (exponent == 1.0f) {
            return (1.0f / ausschlussGuete);
        } else if (exponent == 2.0f) {
            return (1.0f / (ausschlussGuete * ausschlussGuete));
        } else {
            return (1.0f / ((float) Math.pow(ausschlussGuete, exponent)));
        }
    }

    /**
     * Liefert die einzelnen Kosten für das Enthaltensein der negativen
     * Beispiele in einem allgemeinen Konzept und setzt den Wert für das
     * Attribut <CODE>nachkommaAnz</CODE>. Dabei sind die Kosten für einen
     * Index, dessen Güte Null ist, größer als die Summe der Kosten der
     * Indices mit einer Güte größer als Null.
     *
     * @param negAusschlussGuete  Für jedes negativen Beispiel die Güte, mit
     *                            der es schon ausgeschlossen wird.
     *
     * @return  Ein Array, in dem für jedes negativen Beispiel die Kosten für
     *          das Enthaltensein des Beispiels in einem allgemeinen Konzept
     *          angegeben sind.
     */
    private float[] indexKosten(float[] negAusschlussGuete) {
        float[] indexKosten = new float[negAusschlussGuete.length];
        float   maxIndexKosten;
        float   kostenSumme;
        float   maxZehnerPotenz;
        int     nullGueteAnz;
        int     negElementAnz;

        nullGueteAnz = 0;
        negElementAnz = negAusschlussGuete.length;
        for (int i = 0; i < negElementAnz; i++) {
            if (negAusschlussGuete[i] > 0) {
                indexKosten[i] = einzelIndexKosten(negAusschlussGuete[i],
                                                   Konstanten.HAEUFIGKEITS_EXPONENT);
            } else {
                indexKosten[i] = 0;
                nullGueteAnz++;
            }
        }

        if (nullGueteAnz == negElementAnz) {
            // Es ist noch gar kein Index ausgeschlossen.
            nachkommaAnz = 0;
            maxIndexKosten = 1.0f;
        } else {
            kostenSumme = (FrequencyCosts.fliesskommaSumme(indexKosten)
                           * kostenSummenFaktor * (nullGueteAnz + 1));
            maxZehnerPotenz = ((float) Long.MAX_VALUE) / kostenSumme;
            nachkommaAnz = (int) Math.floor(Math.log(maxZehnerPotenz) / Math.log(10.0));

            // Wegen möglicher Rechenungenauigkeiten wird die Anzahl der
            // Nachkommastellen und damit der größte mögliche Wert um eine
            // Stelle verringert.
            nachkommaAnz--;

            if (nachkommaAnz < 0) {
                // Dieser Fall sollte nicht auftreten.
                // Anstatt eine Exception zu werfen, könnten auch alle Werte in
                // indexKosten durch Zehn hoch -nachkommaAnz dividiert werden.
                throw new RuntimeException("AlgVollErzKonzepte: Werte sind zu groß");
            }

            maxIndexKosten = (FrequencyCosts.festkommaSumme(indexKosten, nachkommaAnz)
                              * kostenSummenFaktor);
        }

        for (int i = 0; i < negElementAnz; i++) {
            if (negAusschlussGuete[i] == 0) {
                indexKosten[i] = maxIndexKosten;
            }
        }

        return indexKosten;
    }

    /**
     * Liefert zu einem Konzept eine daraus erzeugte Teilmenge.
     *
     * @param konzept  Das Konzept, zu dem eine Teilmenge erzeugt werden soll.
     *
     * @return  Die aus dem Konzept erzeugte Teilmenge.
     */
    private KonzeptDoppelTeilmenge konzeptTeilmenge(Konzept konzept) {
        KonzeptDoppelTeilmenge konzeptDoppelTeilmenge;
        boolean                speicherEffizient;

        speicherEffizient = (speicherEffizienz >= 1);
        konzeptDoppelTeilmenge = new KonzeptDoppelTeilmenge(konzept.posGesamtAnz(),
                                                            konzept.negFalschBsp(),
                                                            konzept,
                                                            speicherEffizient);
        konzeptDoppelTeilmenge.indicesAufnehmen(konzept.posRichtigBsp());
        return konzeptDoppelTeilmenge;
    }

    /**
     * Nimmt das übergebene Konzept in diese Menge auf.
     *
     * @param konzept  Das in diese Menge aufzunehmende Konzept.
     *
     * @return  <CODE>true</CODE>, wenn das Konzept in die Auswahl aufgenommen
     *          wurde, sonst <CODE>false</CODE>.
     *
     * @throws IllegalArgumentException  Wenn die Anzahlen der Beispiele, die
     *                                   diesem Konzept und dem übergebenen
     *                                   Konzept zugrunde liegen, nicht
     *                                   übereinstimmt.
     */
    public boolean aufnehmen(Konzept konzept) {
        KonzeptDoppelTeilmenge konzeptDoppelTeilmenge;

        if ((konzept.posGesamtAnz() !=  beispieldaten.posBspAnz())
            || (konzept.negGesamtAnz() !=  beispieldaten.negBspAnz())) {

            throw new IllegalArgumentException("Die Anzahl der zugrunde liegenden"
                                               + " Beispiele stimmt nicht überein");
        }

        if (konzeptIstNutzlos(konzept, nichtAusgeschlosenBsp)) {
            return false;
        }

        konzeptDoppelTeilmenge = konzeptTeilmenge(konzept);
        return konzeptItmAuswahl.teilmengeHinzufuegen(konzeptDoppelTeilmenge);
    }

    /**
     * Nimmt die Konzepte der übergebene Collection in diese Menge auf.
     *
     * @param konzepte  Eine Collection mit Konzepten, die in diese Menge
     *                  aufgenommen werden sollen.
     *
     * @return  <CODE>true</CODE>, wenn mindestens ein Konzept in die Auswahl
     *          aufgenommen wurde, sonst <CODE>false</CODE>.
     *
     * @throws IllegalArgumentException  Wenn die Anzahlen der Beispiele, die
     *                                   diesem Konzept und einem Konzept der
     *                                   übergebenen Menge zugrunde liegen,
     *                                   nicht übereinstimmt.
     */
    public boolean aufnehmen(Collection konzepte) {
        Iterator konzIter = konzepte.iterator();
        Konzept  konzept;
        boolean  aufgenommen = false;

        while (konzIter.hasNext()) {
            konzept = (Konzept)konzIter.next();
            aufgenommen |= aufnehmen(konzept);
        }
        return aufgenommen;
    }

    /**
     * Gibt an, ob mit Sicherheit alle hinzugefügten Konzepte in der Auswahl
     * enthalten sind.
     *
     * @return  Die Angabe, ob mit Sicherheit alle hinzugefügten Konzepte in
     *          der Auswahl enthalten sind.
     */
    public boolean alleKonzepteEnthalten() {
        return !konzeptItmAuswahl.maxAuswahlWurdeErreicht();
    }

    /**
     * Liefert die Anzahl der in dieser Menge enthaltenen Konzepte.
     *
     * @return  Die Anzahl der in dieser Menge enthaltenen Konzepte.
     */
    public int enthalteneKonzeptAnz() {
        return konzeptItmAuswahl.groesseAuswahl();
    }

    /**
     * Liefert eine Menge der enthaltenen Konzepte.
     *
     * @return  Eine Menge der enthaltenen Konzepte.
     */
    public HashSet enthalteneKonzepte() {
        return KonzeptDoppelTeilmenge.konzepte(konzeptItmAuswahl.auswahl().toHashSet());
    }

    /**
     * Entfernt aus der übergebenen Menge die Konzept-Teilmengen, die nutzlos
     * sind.
     *
     * @param teilmengen          Die Teilmengen, von denen die nutzlosen
     *                            entfernt werden sollen.
     * @param nichtAusgeschlosen  Die Indices der negativen Beispiele, die
     *                            bisher noch nicht ausgeschlossen sind.
     */
    private void entferneNutzloseKonzeptTeilmengen(HashSet teilmengen,
                                                   BitMathIntSet nichtAusgeschlosen) {
        KonzeptDoppelTeilmenge konzeptTeilmenge;
        Iterator               iter;

        iter = teilmengen.iterator();
        while (iter.hasNext()) {
            konzeptTeilmenge = (KonzeptDoppelTeilmenge) iter.next();
            if (konzeptIstNutzlos(konzeptTeilmenge.konzept(), nichtAusgeschlosen)) {
                // Die Konzept-Teilmenge enthält alle negativen Beispiele, die
                // derzeit noch nicht ausgeschlossen sind.
                iter.remove();
            }
        }
    }

    /**
     * Liefert zu einem Konzept die aus den Teilkonzepten erzeugten
     * Teilmengen.
     *
     * @param konzept  Das Konzept, zu dem die Teilmengen erzeugt werden
     *                 sollen.
     *
     * @return  Die zu den Teilkonzepten erzeugten Teilmengen.
     */
    private HashSet konzeptTeilmengen(Konzept konzept) {
        HashSet                konzeptTeilmengen;
        HashSet                teilkonzepte;
        KonzeptDoppelTeilmenge teilmenge;
        Konzept                teilkonzept;
        Iterator               iter;

        teilkonzepte = konzept.teilkonzepte();
        konzeptTeilmengen = new HashSet(teilkonzepte.size());

        iter = teilkonzepte.iterator();
        while (iter.hasNext()) {
            teilkonzept = (Konzept) iter.next();
            teilmenge = konzeptTeilmenge(teilkonzept);
            konzeptTeilmengen.add(teilmenge);
        }

        return konzeptTeilmengen;
    }

    /**
     * Liefert eine Menge mit vollständigen und sinnvollen Konzepten, die aus
     * den enthaltenen allgemeinen Konzepten erzeugt wurden.
     *
     * @param negAusschlussGuete  Für jedes negativen Beispiel die Güte, mit
     *                            der es schon ausgeschlossen wird.
     * @param scpIterAnz          Die Anzahl der Iterationen im SCP-Verfahren.
     * @param nurEinKonzept       Gibt an, ob nur ein Konzept erzeugt werden
     *                            soll. Falls nicht, werden mehrere Konzepte
     *                            erzeugt.
     * @param bekanntesKonzept    Ein bekanntes vollständiges Konzept oder der
     *                            Wert <CODE>null</CODE>. Wenn kein besseres
     *                            Konzept erzeugt werden kann, wird dieses zur
     *                            Erzeugung der Menge der Konzepte verwendet.
     *
     * @return  Ein <CODE>HashSet</CODE> mit vollständigen Konzepten vom Typ
     *          <CODE>KombiKonzept</CODE>.
     *
     * @throws IllegalArgumentException  Wenn die Anzahl der übergebenen Werte
     *                                   nicht mit der Anzahl der positiven
     *                                   Beispiele übereinstimmt.
     */
    public HashSet vollstaendigeKonzepte(float[] negAusschlussGuete,
                                         int scpIterAnz, boolean nurEinKonzept,
                                         Konzept bekanntesKonzept) {
        UeberdeckungsOptimierung scpVerfahren;
        SequenceOptimization     optVerfahren;
        AddOneSolutionSet        solutionExtention;
        HashSet                  ausgewItm;
        HashSet                  loesungsMengen;
        HashSet                  vollstaendigeKonzepte;
        HashSet                  naechsteLoesungsMenge;
        DoppelItmFamilie         auswahl;
        QuotDoppelItmFamilie     neueFamilie;
        QuotDoppelItmFamilie     bekannnteLoesung;
        QuotDoppelItmFamilie     neueLoesung;
        Disjunktion              vollKonzept;
        BitMathIntSet            nichtAusgeschlosen;
        Iterator                 loesIter;
        float[]                  indexKosten;

        if (negAusschlussGuete.length != beispieldaten.negBspAnz()) {
            throw new IllegalArgumentException("Anzahl der negativen Beispiele"
                                               + " stimmt nicht überein");
        }

        vollstaendigeKonzepte = new HashSet();
        auswahl = (DoppelItmFamilie) konzeptItmAuswahl.auswahl();
        LOGGER.log(Level.CONFIG,
                   "Anzahl der Teilmengen der Auswahl = "
                   + auswahl.groesseFamilie());

        nichtAusgeschlosen = wertNullIndices(negAusschlussGuete);
        ausgewItm = auswahl.toHashSet();

        // Wenn es noch Beispiele gibt, die nicht ausgeschlossen sind, dann zu
        // deren Verringerung nutzlose Teilmengen entfernen.
        if (!nichtAusgeschlosen.isEmpty()) {
            // Es gibt noch nicht ausgeschlossene negative Beispiele.
            entferneNutzloseKonzeptTeilmengen(ausgewItm, nichtAusgeschlosen);
        }

        // Bei speicherEffizienz 1 für das SCP-Verfahren neue
        // nicht-speicher-effizient Teilmengen erzeugen.
        if (speicherEffizienz == 1) {
            ausgewItm = KonzeptDoppelTeilmenge.konvTeilmengen(ausgewItm, false);
        }

        // Beste Familie als vollständiges Konzept erzeugen.
        indexKosten = indexKosten(negAusschlussGuete);
        neueFamilie = new QuotDoppelItmFamilie(beispieldaten.posBspAnz(),
                                               indexKosten, nachkommaAnz);
        neueFamilie.teilmengenHinzufuegen(ausgewItm);
        LOGGER.log(Level.CONFIG,
                   "Statistik der (evtl. reduzierten) Auswahl:"
                   + neueFamilie.statistik(2));

        if (neueFamilie.anzNichtUeberdeckt() == 0) {
            // Es können vollständigen Konzepte erzeugt werden.

            if (bekanntesKonzept == null) {
                bekannnteLoesung = null;
            } else {
                bekannnteLoesung = new QuotDoppelItmFamilie(beispieldaten.posBspAnz(),
                                                            indexKosten, nachkommaAnz);
                bekannnteLoesung.teilmengenHinzufuegen(konzeptTeilmengen(bekanntesKonzept));
            }

            optVerfahren = new SequenceOptimization();
            if (Konstanten.SPEZ_USE_INFERIOR_OPT) {
                optVerfahren.addOptimization(new InferiorOptimization());
            }
            if (Konstanten.SPEZ_USE_ADD_ONE_OPT) {
                optVerfahren.addOptimization(new AddOneOptimization());
            }
            scpVerfahren = new IterEnhancedGreedyHeuristic(random, scpIterAnz,
                                                           optVerfahren);

            if (maxLiteralAnz > 0) {
                scpVerfahren = new BegrenzteUeberdeckung(scpVerfahren, maxLiteralAnz);
            }

            neueLoesung = (QuotDoppelItmFamilie) scpVerfahren.ueberdeckung(neueFamilie,
                                                                           bekannnteLoesung);
            LOGGER.log(Level.CONFIG,
                       "Statistik der Lösung:" + neueLoesung.statistik(2));

            // Die Menge der Doppel-Teilmengen der erzeugten Lösung und evtl.
            // weiterer zu erzeugender Lösungen ermitteln.
            if (!nurEinKonzept) {
                // Weitere Familien als vollständige Konzepte erzeugen.
                solutionExtention = new AddOneSolutionSet(false);
                loesungsMengen = solutionExtention.generatedSet(neueFamilie.toHashSet(),
                                                                neueLoesung);
            } else {
                loesungsMengen = new HashSet();
            }
            loesungsMengen.add(neueLoesung.toHashSet());

            // Aus den Familien tatsächliche vollständige Konzepte erzeugen.
            loesIter = loesungsMengen.iterator();
            while (loesIter.hasNext()) {
                naechsteLoesungsMenge = (HashSet) loesIter.next();
                vollKonzept = new Disjunktion(beispieldaten);
                vollKonzept.aufnehmen(KonzeptDoppelTeilmenge.konzepte(naechsteLoesungsMenge));
                if (vollKonzept.istSinnvoll()) {
                    vollstaendigeKonzepte.add(vollKonzept);
                }
            }
        } else {
            LOGGER.log(Level.CONFIG, "Kein vollständiges Konzept erzeugbar");
        }

        return vollstaendigeKonzepte;
    }
}

