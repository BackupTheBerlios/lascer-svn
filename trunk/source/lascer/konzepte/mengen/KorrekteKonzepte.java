/*
 * Dateiname      : KorrekteKonzepte.java
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

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.konvertierung.PartielleUeberdeckung;
import mengenUeberdeckung.optimierung.InferiorOptimization;
import mengenUeberdeckung.optimierung.AddOneOptimization;
import mengenUeberdeckung.optimierung.SequenceOptimization;
import mengenUeberdeckung.reduktion.ItmAuswahlErzeugung;
import mengenUeberdeckung.reduktion.SofortigeAuswahl;
import mengenUeberdeckung.heuristiken.utility.FrequencyUtility;
import mengenUeberdeckung.iteratedGreedyCovering.IterEnhancedGreedyHeuristic;

import lascer.konzepte.Konzept;
import lascer.konzepte.KombiKonzept;
import lascer.konzepte.einzelne.Disjunktion;
import lascer.problemdaten.Beispieldaten;

/**
 * Repräsentiert eine Menge korrekter Konzepte, aus denen ein möglichst gutes
 * vollständiges Konzept erzeugt werden soll. Die Menge stellt eine Auswahl
 * der zur Aufnahme übergebenen Konzepte dar.
 *
 * @author  Dietmar Lippold
 */
public class KorrekteKonzepte {

    /**
     * Der Logger dieser Klasse.
     */
    private static final Logger LOGGER
        = Logger.getLogger(KorrekteKonzepte.class.getName());

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
     * Die geschätzte maximale Gesamt-Komplexität einer Formel. Dieser Wert
     * ist nur bei Vorgabe der Anzahl an Literalen, die maximal in einer
     * Disjunktion bzw. Konjunktion enthalten sein sollen, (Attribut
     * <CODE>maxLiteralAnz</CODE>) von Bedeutung.
     */
    private float gesamtKomplexitaet;

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
     * @param beispieldaten       Die der Konzeptbildung aus dieser Menge
     *                            zugrunde gelegten Beispieldaten.
     * @param gesamtKomplexitaet  Die geschätzte maximale Gesamt-Komplexität
     *                            einer Formel.
     * @param maxLiteralAnz       Die Anzahl der Literale, die maximal in
     *                            einer Disjunktion bzw. Konjunktion enthalten
     *                            sein sollen. Der Wert Null steht für eine
     *                            unbegrenzte Anzahl.
     * @param spezMinItmAnz       Die minimale Anzahl der zu speichernden
     *                            Teilmengen zur Auswahl der speziellen
     *                            Konzeptmengen. Ein negativer Wert steht für
     *                            eine unbeschränkte Anzahl.
     * @param speicherEffizienz   Gibt an, in welchem Ausmaß die Teilmengen
     *                            besonders Speicher-effizient aber dadurch
     *                            weniger Laufzeit-effizient verwaltet werden
     *                            sollen. Der Wert muß zwischen Null und Zwei
     *                            liegen.
     */
    public KorrekteKonzepte(Random random, Beispieldaten beispieldaten,
                            float gesamtKomplexitaet, int maxLiteralAnz,
                            int spezMinItmAnz, int speicherEffizienz) {
        int posBspAnz;

        this.random = random;
        this.beispieldaten = beispieldaten;
        this.gesamtKomplexitaet = gesamtKomplexitaet;
        this.maxLiteralAnz = maxLiteralAnz;
        this.speicherEffizienz = speicherEffizienz;

        posBspAnz = beispieldaten.posBspAnz();
        konzeptItmAuswahl = new SofortigeAuswahl(new ItmFamilie(posBspAnz),
                                                 spezMinItmAnz,
                                                 Konstanten.SPEZ_MIN_UEBERDECK_ANZ,
                                                 Konstanten.SPEZ_FAKTOR_ERG_ANZ,
                                                 new FrequencyUtility(false));
    }

    /**
     * Liefert zu einem Konzept eine daraus erzeugte Teilmenge.
     *
     * @param konzept  Das Konzept, zu dem eine Teilmenge erzeugt werden soll.
     *
     * @return  Die aus dem Konzept erzeugte Teilmenge.
     */
    private KonzeptTeilmenge konzeptTeilmenge(Konzept konzept) {
        KonzeptTeilmenge konzeptTeilmenge;
        float            zusatzKomplex;
        int              teilkonzeptAnz;
        boolean          speicherEffizient;

        speicherEffizient = (speicherEffizienz >= 1);
        teilkonzeptAnz = konzept.teilkonzepte().size();
        if ((maxLiteralAnz == 0) || (teilkonzeptAnz <= maxLiteralAnz)) {
            zusatzKomplex = 0;
        } else {
            zusatzKomplex = gesamtKomplexitaet * (teilkonzeptAnz - maxLiteralAnz);
        }
        konzeptTeilmenge = new KonzeptTeilmenge(konzept.posGesamtAnz(), konzept,
                                                zusatzKomplex, speicherEffizient);
        konzeptTeilmenge.indicesAufnehmen(konzept.posRichtigBsp());
        return konzeptTeilmenge;
    }

    /**
     * Nimmt das übergebene Konzept in diese Menge auf.
     *
     * @param konzept  Das in diese Menge aufzunehmende Konzept.
     *
     * @return  <CODE>true</CODE>, wenn das Konzept in die Auswahl aufgenommen
     *          wurde, sonst <CODE>false</CODE>.
     *
     * @throws IllegalArgumentException  Wenn das übergebene Konzept nicht
     *                                   korrekt ist oder die Anzahlen der
     *                                   Beispiele, die diesem Konzept und dem
     *                                   übergebenen Konzept zugrunde liegen,
     *                                   nicht übereinstimmt.
     */
    public boolean aufnehmen(Konzept konzept) {
        KonzeptTeilmenge konzeptTeilmenge;

        if (!konzept.istKorrekt()) {
            throw new IllegalArgumentException("Konzept ist nicht korrekt");
        }

        if ((konzept.posGesamtAnz() !=  beispieldaten.posBspAnz())
            || (konzept.negGesamtAnz() !=  beispieldaten.negBspAnz())) {

            throw new IllegalArgumentException("Die Anzahl der zugrunde liegenden"
                                               + " Beispiele stimmt nicht überein");
        }

        konzeptTeilmenge = konzeptTeilmenge(konzept);
        return konzeptItmAuswahl.teilmengeHinzufuegen(konzeptTeilmenge);
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
     * @throws IllegalArgumentException  Wenn ein übergebenes Konzept nicht
     *                                   korrekt ist oder die Anzahlen der
     *                                   Beispiele, die diesem Konzept und
     *                                   einem übergebenen Konzept zugrunde
     *                                   liegen, nicht übereinstimmt.
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
     * Liefert ein Array mit der Güte für die Überdeckung jedes positiven
     * Elements durch die Konzepte dieser Menge. Der Wert zu einem Element
     * ist umso größer, je häufiger dieses von Konzepten überdeckt wird und
     * eventuell umso geringer deren Komplexität ist.<P>
     *
     * Die Güte für einen Index, der gar nicht überdeckt ist, ist Null. Für
     * einen Index, der überdeckt ist, ist der kleinste Wert Eins.
     *
     * @return  Ein Array, das für jedes positive Element die Güte von dessen
     *          Überdeckung angibt.
     */
    public float[] posUeberdeckGuete() {
        ItmFamilie       auswahl;
        Iterator         itmIter;
        KonzeptTeilmenge teilmenge;
        float[]          posUeberdeckGuete;
        float            maxKomplex;
        float            invKomplex;
        int              posBspAnz;

        posBspAnz = beispieldaten.posBspAnz();
        posUeberdeckGuete = new float[posBspAnz];
        auswahl = konzeptItmAuswahl.auswahl();

        if (Konstanten.GUETE_EINHEITLICH) {
            for (int i = 0; i < posBspAnz; i++) {
                posUeberdeckGuete[i] = auswahl.ueberdeckungsHaeufigkeit(i);
            }
        } else {
            for (int i = 0; i < posBspAnz; i++) {
                posUeberdeckGuete[i] = 0;
            }

            // Die größte Komplexität einer Teilmenge ermittelt.
            maxKomplex = 0;
            itmIter = auswahl.iterator();
            while (itmIter.hasNext()) {
                teilmenge = (KonzeptTeilmenge) itmIter.next();
                maxKomplex = Math.max(maxKomplex, teilmenge.konzept().komplexitaet());
            }

            itmIter = auswahl.iterator();
            while (itmIter.hasNext()) {
                teilmenge = (KonzeptTeilmenge) itmIter.next();
                invKomplex = maxKomplex / teilmenge.konzept().komplexitaet();
                for (int index = teilmenge.kleinsterEnthaltenerIndex();
                     index >= 0;
                     index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {
                    posUeberdeckGuete[index] += invKomplex;
                }
            }
        }
        return posUeberdeckGuete;
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
     * Gibt an, ob aus den enthaltenen Konzepten eine fehlerfreie Formel
     * erzeugt werden kann.
     *
     * @return  Die Angabe, ob aus den enthaltenen Konzepten eine fehlerfreie
     *          Formel erzeugt werden kann.
     */
    public boolean fehlerfreieFormelErzeugbar() {
        return (konzeptItmAuswahl.auswahl().anzNichtUeberdeckt() == 0);
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
     * Liefert eine neue Menge der enthaltenen Konzepte.
     *
     * @return  Eine neue Menge der enthaltenen Konzepte.
     */
    public HashSet enthalteneKonzepte() {
        return KonzeptTeilmenge.konzepte(konzeptItmAuswahl.auswahl().toHashSet());
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
        HashSet          konzeptTeilmengen;
        HashSet          teilkonzepte;
        KonzeptTeilmenge teilmenge;
        Konzept          teilkonzept;
        Iterator         iter;

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
     * Liefert diejenige korrekte Formel, die primär möglichst vollständig
     * ist und sekundär eine möglichst geringe Komplexität hat. Diese wird
     * aus den enthaltenen Konzepten erzeugt.
     *
     * @param scpIterAnz      Die Anzahl der Iterationen im SCP-Verfahren.
     * @param bekannteFormel  Eine bekannte korrekte Formel oder der Wert
     *                        <CODE>null</CODE>. Wenn keine bessere Formel
     *                        erzeugt werden kann, wird diese Formel
     *                        zurück geliefert.
     *
     * @return  Die beste korrekte Formel, die aus den enthaltenen Konzepten
     *          erzeugt werden kann.
     */
    public KombiKonzept besteFormel(int scpIterAnz, Konzept bekannteFormel) {
        IterEnhancedGreedyHeuristic scpBasicVerfahren;
        SequenceOptimization        optVerfahren;
        PartielleUeberdeckung       scpPartVerfahren;
        HashSet                     neueItms;
        ItmFamilie                  auswahl;
        ItmFamilie                  bekannnteLoesung;
        ItmFamilie                  problemFamilie;
        ItmFamilie                  neueLoesung;
        Disjunktion                 formel;

        if (bekannteFormel == null) {
            bekannnteLoesung = null;
        } else {
            bekannnteLoesung = new ItmFamilie(beispieldaten.posBspAnz());
            bekannnteLoesung.teilmengenHinzufuegen(konzeptTeilmengen(bekannteFormel));
        }

        optVerfahren = new SequenceOptimization();
        if (Konstanten.FORM_USE_INFERIOR_OPT) {
            optVerfahren.addOptimization(new InferiorOptimization());
        }
        if (Konstanten.FORM_USE_ADD_ONE_OPT) {
            optVerfahren.addOptimization(new AddOneOptimization());
        }
        scpBasicVerfahren = new IterEnhancedGreedyHeuristic(random, scpIterAnz,
                                                            optVerfahren);
        scpPartVerfahren = new PartielleUeberdeckung(scpBasicVerfahren);

        auswahl = konzeptItmAuswahl.auswahl();
        LOGGER.log(Level.CONFIG,
                   "Statistik der Auswahl:" + auswahl.statistik(2));

        // Bei speicherEffizienz 1 für das SCP-Verfahren eine Familie mit
        // nicht-speicher-effizienten Teilmengen erzeugen.
        if (speicherEffizienz == 1) {
            neueItms = KonzeptTeilmenge.konvTeilmengen(auswahl.toHashSet(), false);
            problemFamilie = new ItmFamilie(beispieldaten.posBspAnz());
            problemFamilie.teilmengenHinzufuegen(neueItms);
        } else {
            problemFamilie = auswahl;
        }

        neueLoesung = scpPartVerfahren.ueberdeckung(problemFamilie, bekannnteLoesung);
        LOGGER.log(Level.CONFIG,
                   "Statistik der Lösung:" + neueLoesung.statistik(2));

        formel = new Disjunktion(beispieldaten);
        formel.aufnehmen(KonzeptTeilmenge.konzepte(neueLoesung.toHashSet()));
        return formel;
    }
}

