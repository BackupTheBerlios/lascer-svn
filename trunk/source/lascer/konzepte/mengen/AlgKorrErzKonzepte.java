/*
 * Dateiname      : AlgKorrErzKonzepte.java
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
import lascer.konzepte.einzelne.Konjunktion;
import lascer.problemdaten.Beispieldaten;

/**
 * Repräsentiert eine Menge allgemeiner Konzepte zur Erzeugung korrekter
 * Konzepte. Die Menge stellt eine Auswahl der zur Aufnahme übergebenen
 * Konzepte dar.
 *
 * @author  Dietmar Lippold
 */
public class AlgKorrErzKonzepte {

    /**
     * Der Logger dieser Klasse.
     */
    private static final Logger LOGGER
        = Logger.getLogger(AlgKorrErzKonzepte.class.getName());

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
     * Die Indices der noch nicht überdeckten positiven Beispiele.
     */
    private BitMathIntSet nichtUeberdecktBsp;

    /**
     * Der Faktor, mit dem die Summe der Kosten der überdeckten Indices
     * multipliziert wird, um die Kosten für einen nicht überdeckten Index zu
     * errechnen.
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
     * @param random             Der zu verwendende Zufallsgenerator.
     * @param beispieldaten      Die dem Konzept zugrunde gelegten
     *                           Beispieldaten.
     * @param posUeberdeckGuete  Güte, mit der die einzelnen positiven
     *                           Elemente von den vorhandenen korrekten
     *                           Konzepten überdeckt werden.
     * @param kostenFaktor       Faktor, mit dem die Summe der Kosten der
     *                           überdeckten Indices multipliziert wird, um
     *                           die Kosten für einen nicht überdeckten Index
     *                           zu errechnen.
     * @param algMinItmAnz       Die minimale Anzahl der zu speichernden
     *                           Teilmengen zur Auswahl der allgemeinen
     *                           Konzeptmengen. Ein negativer Wert steht für
     *                           eine unbeschränkte Anzahl.
     * @param maxLiteralAnz      Die Anzahl der Literale, die maximal in einer
     *                           Disjunktion bzw. Konjunktion enthalten sein
     *                           sollen. Der Wert Null steht für eine
     *                           unbegrenzte Anzahl.
     * @param speicherEffizienz  Gibt an, in welchem Ausmaß die Teilmengen
     *                           besonders Speicher-effizient aber dadurch
     *                           weniger Laufzeit-effizient verwaltet werden
     *                           sollen. Der Wert muß zwischen Null und Zwei
     *                           liegen.
     */
    public AlgKorrErzKonzepte(Random random, Beispieldaten beispieldaten,
                              float[] posUeberdeckGuete, float kostenFaktor,
                              int algMinItmAnz, int maxLiteralAnz,
                              int speicherEffizienz) {
        DoppelItmFamilie konzeptFamilie;
        float[] indexKosten;

        this.random = random;
        this.beispieldaten = beispieldaten;
        this.nichtUeberdecktBsp = wertNullIndices(posUeberdeckGuete);
        this.kostenSummenFaktor = kostenFaktor;
        this.maxLiteralAnz = maxLiteralAnz;
        this.speicherEffizienz = speicherEffizienz;

        indexKosten = indexKosten(posUeberdeckGuete);
        konzeptFamilie = new SumDoppelItmFamilie(beispieldaten.negBspAnz(),
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
     * Fall, wenn es noch nicht überdeckte positive Beispiele gibt aber das
     * Konzept von diesen keines überdeckt.
     *
     * @param konzept          Das Konzept, von dem ermittelt werden soll, ob
     *                         es nutzlos ist.
     * @param nichtUeberdeckt  Die Indices der positiven Beispiele, die bisher
     *                         noch nicht überdeckt sind.
     *
     * @return  <CODE>true</CODE>, wenn das Konzept nutzlos ist, sonst
     *          <CODE>false</CODE>.
     */
    private boolean konzeptIstNutzlos(Konzept konzept,
                                      BitMathIntSet nichtUeberdeckt) {

        if (nichtUeberdeckt.isEmpty()) {
            return false;
        } else {
            return konzept.posErfuelltBsp().isDisjoint(nichtUeberdeckt);
        }
    }

    /**
     * Liefert die Kosten für das Nicht-Enthaltensein eines einzelnen
     * positiven Beispiels in einem allgemeinen Konzept.
     *
     * @param ueberdeckGuete  Für jedes positive Beispiel die Güte, mit der
     *                        es schon überdeckt wird. Dieser Wert muß größer
     *                        als Null sein.
     * @param exponent        Der Exponent, der bei der Berechnung der Kosten
     *                        verwendet werden soll.
     *
     * @return  Ein Array, in dem für jedes positive Beispiel die Kosten für
     *          das Nicht-Enthaltensein des Beispiels in einem allgemeinen
     *          Konzept angegeben sind.
     */
    private float einzelIndexKosten(float ueberdeckGuete, float exponent) {

        if (exponent == 0) {
            return 1;
        } else if (exponent == 1.0f) {
            return (1.0f / ueberdeckGuete);
        } else if (exponent == 2.0f) {
            return (1.0f / (ueberdeckGuete * ueberdeckGuete));
        } else {
            return (1.0f / ((float) Math.pow(ueberdeckGuete, exponent)));
        }
    }

    /**
     * Liefert die einzelnen Kosten für das Nicht-Enthaltensein der positiven
     * Beispiele in einem allgemeinen Konzept und setzt den Wert für das
     * Attribut <CODE>nachkommaAnz</CODE>. Dabei sind die Kosten für einen
     * Index, dessen Güte Null ist, größer als die Summe der Kosten der
     * Indices mit einer Güte größer als Null.
     *
     * @param posUeberdeckGuete  Für jedes positive Beispiel die Güte, mit der
     *                           es schon überdeckt wird.
     *
     * @return  Ein Array, in dem für jedes positive Beispiel die Kosten für
     *          das Nicht-Enthaltensein des Beispiels in einem allgemeinen
     *          Konzept angegeben sind.
     */
    private float[] indexKosten(float[] posUeberdeckGuete) {
        float[] indexKosten = new float[posUeberdeckGuete.length];
        float   maxIndexKosten;
        float   kostenSumme;
        float   maxZehnerPotenz;
        int     nullGueteAnz;
        int     posElementAnz;

        nullGueteAnz = 0;
        posElementAnz = posUeberdeckGuete.length;
        for (int i = 0; i < posElementAnz; i++) {
            if (posUeberdeckGuete[i] > 0) {
                indexKosten[i] = einzelIndexKosten(posUeberdeckGuete[i],
                                                   Konstanten.HAEUFIGKEITS_EXPONENT);
            } else {
                indexKosten[i] = 0;
                nullGueteAnz++;
            }
        }

        if (nullGueteAnz == posElementAnz) {
            // Es ist noch gar kein Index überdeckt.
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
                throw new RuntimeException("AlgKorrErzKonzepte: Werte sind zu groß");
            }

            maxIndexKosten = (FrequencyCosts.festkommaSumme(indexKosten, nachkommaAnz)
                              * kostenSummenFaktor);
        }

        for (int i = 0; i < posElementAnz; i++) {
            if (posUeberdeckGuete[i] == 0) {
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
        konzeptDoppelTeilmenge = new KonzeptDoppelTeilmenge(konzept.negGesamtAnz(),
                                                            konzept.posFalschBsp(),
                                                            konzept,
                                                            speicherEffizient);
        konzeptDoppelTeilmenge.indicesAufnehmen(konzept.negRichtigBsp());
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

        if (konzeptIstNutzlos(konzept, nichtUeberdecktBsp)) {
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
     * @param teilmengen       Die Teilmengen, von denen die nutzlosen
     *                         entfernt werden sollen.
     * @param nichtUeberdeckt  Die Indices der positiven Beispiele, die bisher
     *                         noch nicht überdeckt sind.
     */
    private void entferneNutzloseKonzeptTeilmengen(HashSet teilmengen,
                                                   BitMathIntSet nichtUeberdeckt) {
        KonzeptDoppelTeilmenge konzeptTeilmenge;
        Iterator               iter;

        iter = teilmengen.iterator();
        while (iter.hasNext()) {
            konzeptTeilmenge = (KonzeptDoppelTeilmenge) iter.next();
            if (konzeptIstNutzlos(konzeptTeilmenge.konzept(), nichtUeberdeckt)) {
                // Die Konzept-Teilmenge enthält nur positive Beispiel, die
                // bisher schon überdeckt sind.
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
     * Liefert eine Menge mit korrekten und sinnvollen Konzepten, die aus den
     * enthaltenen allgemeinen Konzepten erzeugt wurden.
     *
     * @param posUeberdeckGuete  Für jedes positive Beispiel die Güte, mit der
     *                           es schon überdeckt wird.
     * @param scpIterAnz         Die Anzahl der Iterationen im SCP-Verfahren.
     * @param nurEinKonzept      Gibt an, ob nur ein Konzept erzeugt werden
     *                           soll. Falls nicht, werden mehrere Konzepte
     *                           erzeugt.
     * @param bekanntesKonzept   Ein bekanntes korrektes Konzept oder der Wert
     *                           <CODE>null</CODE>. Wenn kein besseres Konzept
     *                           erzeugt werden kann, wird dieses zur
     *                           Erzeugung der Menge der Konzepte verwendet.
     *
     * @return  Ein <CODE>HashSet</CODE> mit korrekten Konzepten vom Typ
     *          <CODE>KombiKonzept</CODE>.
     *
     * @throws IllegalArgumentException  Wenn die Anzahl der übergebenen Werte
     *                                   nicht mit der Anzahl der positiven
     *                                   Beispiele übereinstimmt.
     */
    public HashSet korrekteKonzepte(float[] posUeberdeckGuete, int scpIterAnz,
                                    boolean nurEinKonzept,
                                    Konzept bekanntesKonzept) {
        UeberdeckungsOptimierung scpVerfahren;
        SequenceOptimization     optVerfahren;
        AddOneSolutionSet        solutionExtention;
        HashSet                  ausgewItm;
        HashSet                  loesungsMengen;
        HashSet                  korrekteKonzepte;
        HashSet                  naechsteLoesungsMenge;
        DoppelItmFamilie         auswahl;
        QuotDoppelItmFamilie     neueFamilie;
        QuotDoppelItmFamilie     bekannnteLoesung;
        QuotDoppelItmFamilie     neueLoesung;
        Konjunktion              korrKonzept;
        BitMathIntSet            nichtUeberdeckt;
        Iterator                 loesIter;
        float[]                  indexKosten;

        if (posUeberdeckGuete.length != beispieldaten.posBspAnz()) {
            throw new IllegalArgumentException("Anzahl der positiven Beispiele"
                                               + " stimmt nicht überein");
        }

        korrekteKonzepte = new HashSet();
        auswahl = (DoppelItmFamilie) konzeptItmAuswahl.auswahl();
        LOGGER.log(Level.CONFIG,
                   "Anzahl der Teilmengen der Auswahl = "
                   + auswahl.groesseFamilie());

        nichtUeberdeckt = wertNullIndices(posUeberdeckGuete);
        ausgewItm = auswahl.toHashSet();

        // Wenn es noch Beispiele gibt, die nicht überdeckt sind, dann die zu
        // deren Verringerung nutzlosen Teilmengen entfernen.
        if (!nichtUeberdeckt.isEmpty()) {
            // Es gibt noch nicht überdeckte positive Beispiele.
            entferneNutzloseKonzeptTeilmengen(ausgewItm, nichtUeberdeckt);
        }

        // Bei speicherEffizienz 1 für das SCP-Verfahren neue
        // nicht-speicher-effizient Teilmengen erzeugen.
        if (speicherEffizienz == 1) {
            ausgewItm = KonzeptDoppelTeilmenge.konvTeilmengen(ausgewItm, false);
        }

        // Beste Familie als korrektes Konzept erzeugen.
        indexKosten = indexKosten(posUeberdeckGuete);
        neueFamilie = new QuotDoppelItmFamilie(beispieldaten.negBspAnz(),
                                               indexKosten, nachkommaAnz);
        neueFamilie.teilmengenHinzufuegen(ausgewItm);
        LOGGER.log(Level.CONFIG,
                   "Statistik der (evtl. reduzierten) Auswahl:"
                   + neueFamilie.statistik(2));

        if (neueFamilie.anzNichtUeberdeckt() == 0) {
            // Es können korrekte Konzepte erzeugt werden.

            if (bekanntesKonzept == null) {
                bekannnteLoesung = null;
            } else {
                bekannnteLoesung = new QuotDoppelItmFamilie(beispieldaten.negBspAnz(),
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
                // Weitere Familien als korrekte Konzepte erzeugen.
                solutionExtention = new AddOneSolutionSet(false);
                loesungsMengen = solutionExtention.generatedSet(neueFamilie.toHashSet(),
                                                                neueLoesung);
            } else {
                loesungsMengen = new HashSet();
            }
            loesungsMengen.add(neueLoesung.toHashSet());

            // Aus den Familien tatsächliche korrekte Konzepte erzeugen.
            loesIter = loesungsMengen.iterator();
            while (loesIter.hasNext()) {
                naechsteLoesungsMenge = (HashSet) loesIter.next();
                korrKonzept = new Konjunktion(beispieldaten);
                korrKonzept.aufnehmen(KonzeptDoppelTeilmenge.konzepte(naechsteLoesungsMenge));
                if (korrKonzept.istSinnvoll()) {
                    korrekteKonzepte.add(korrKonzept);
                }
            }
        } else {
            LOGGER.log(Level.CONFIG, "Kein korrektes Konzept erzeugbar");
        }

        return korrekteKonzepte;
    }
}

