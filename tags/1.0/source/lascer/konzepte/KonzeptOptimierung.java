/*
 * Dateiname      : KonzeptOptimierung.java
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


package lascer.konzepte;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.Serializable;

import mathCollection.BitMathIntSet;

import lascer.KonzErzParameter;
import lascer.praedikate.Praedikat;
import lascer.praedikate.PraedikatErzeugung;
import lascer.problemdaten.Beispieldaten;

/**
 * Bietet Methoden zur Optimierung eines speziellen Konzepts oder einer
 * Formel, d.h. eines finalen Konzepts.
 *
 * @author  Dietmar Lippold
 */
public class KonzeptOptimierung implements Serializable {

    /**
     * Der Logger dieser Klasse.
     */
    private static final Logger LOGGER
        = Logger.getLogger(KonzeptOptimierung.class.getName());

    /**
     * Ein Zufallsgenerator.
     */
    private Random rand;

    /**
     * Die der Konzeptbildung zugrunde liegenden Beispieldaten.
     */
    private Beispieldaten beispieldaten;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen von speziellen, d.h.
     * korrekten oder vollständigen, Konzepten zur Erzeugung von Formeln. Ein
     * negativer Wert steht für eine unbeschränkte Anzahl und es findet keine
     * Auswahl statt.
     */
    private int spezFormItmAnz;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * allgemeinen Konzepte bei der Optimierung eines speziellen Konzepts.
     * Ein negativer Wert steht für eine unbeschränkte Anzahl. Der Wert Null
     * bedeutet, daß keine Optimierung stattfindet.
     */
    private int optSpezItmAnz;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * speziellen Konzepte bei der Optimierung eines finalen Konzepts. Ein
     * negativer Wert steht für eine unbeschränkte Anzahl. Der Wert Null
     * bedeutet, daß keine Optimierung stattfindet.
     */
    private int optFinItmAnz;

    /**
     * Gibt die Anzahl der durchzuführenden Iterationen beim SCP-Verfahren
     * bei der Optimierung eines speziellen Konzepts an.
     */
    private int optSpezScpIterAnz;

    /**
     * Gibt die Anzahl der durchzuführenden Iterationen beim SCP-Verfahren
     * bei der Optimierung einer finalen Formel an.
     */
    private int optFinScpIterAnz;

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
     * Gibt an, ob boolsche Attribute negiert werden sollen, d.h. invertierte
     * Literale dazu erzeugt werden sollen.
     */
    private boolean negBoolPraedErz;

    /**
     * Erzeugt eine neue Instanz. Die Anzahl der Interationen der einzelnen
     * Phasen wird angegeben. Eine negative Anzahl bedeutet eine unbedingte
     * Anzahl, Null bedeutet eine Iteration, solange sich eine Verbesserung
     * ergibt, und eine positive Anzahl bedeutet eine Iteration, bis die
     * Anzahl der Iterationen erreicht ist oder sich keine Verbesserung mehr
     * ergibt.
     *
     * @param rand           Ein Zufallsgenerator.
     * @param beispieldaten  Die der Konzeptbildung zugrunde liegenden
     *                       Beispieldaten.
     * @param parameter      Die Parameter, die die Erzeugung der
     *                       Prädikat-Erzeuger konfigurieren.
     */
    public KonzeptOptimierung(Random rand, Beispieldaten beispieldaten,
                              KonzErzParameter parameter) {

        this.rand = rand;
        this.beispieldaten = beispieldaten;
        this.spezFormItmAnz = parameter.getSpezFormItmAnz();
        this.optSpezItmAnz = parameter.getOptSpezItmAnz();
        this.optFinItmAnz = parameter.getOptFinItmAnz();
        this.optSpezScpIterAnz = parameter.getOptSpezScpIterAnz();
        this.optFinScpIterAnz = parameter.getOptFinScpIterAnz();
        this.maxLiteralAnz = parameter.getMaxLitAnz();
        this.speicherEffizienz = parameter.getSpeicherEffizienz();
        this.negBoolPraedErz = parameter.getNegBoolPraedErz();
    }

    /**
     * Erzeugt zum übergegeben Konzept ein optimiertes Konzept, das möglichst
     * besser ist in Bezug auf eine Ergänzung einer vorhandenen Menge anderer
     * spezieller Konzepte. Bei der Erzeugung des optimierten Konzept werden
     * alle erzeugbaren Prädikate berücksichtigt.
     *
     * @param praedikatErzeuger   Erzeuger von Prädikaten oder <CODE>null</CODE>,
     *                            wenn die vorhandenen allgemeinen Konzepte
     *                            verwendet werden sollen.
     * @param vorhandeneAlgKonz   Die schon vorhandenen allgemeinen Konzepte.
     * @param vorhandeneSpezKonz  Die schon vorhandenen speziellen Konzepte.
     * @param bekanntesKonzept    Ein bekanntes spezielles Konzept, zu dem ein
     *                            optimiertes Konzept erzeugt werden soll.
     * @param kostenFaktor        Faktor, mit dem die Summe der Kosten der
     *                            überdeckten Indices multipliziert wird.
     * @param korrKonzErzeugung   Gibt an, ob ein korrektes Konzepts erzeugt
     *                            werden soll. Falls nicht, wird ein
     *                            vollständiges Konzept erzeugt.
     *
     * @return  Das optimierte Konzept oder das übergebene Konzept, wenn kein
     *          besseres Konzept erzeugt werden konnte.
     */
    public KombiKonzept optimiertesSpezKonzept(ArrayList praedikatErzeuger,
                                               Collection vorhandeneAlgKonz,
                                               Collection vorhandeneSpezKonz,
                                               KombiKonzept bekanntesKonzept,
                                               float kostenFaktor,
                                               boolean korrKonzErzeugung) {
        KonzeptVerwaltung  konzeptVerwaltung;
        PraedikatErzeugung erzeuger;
        HashSet            teilkonzepte;
        Konzept            teilkonzept, allgemeinesKonzept;
        KombiKonzept       reduziertesKonzept, neuesKonzept;
        Praedikat          praedikat;
        BitMathIntSet      redKonzPosFehler, redKonzNegFehler;
        Iterator           teilKonzIter, vhKonzIter, praedIter;
        float              teilKonzKomplex;
        int                teilKonzPosFehlAnz, teilKonzNegFehlAnz;

        // Prüfen, ob eine Optimierung stattfinden soll.
        if (optSpezItmAnz == 0) {
            return bekanntesKonzept;
        }

        LOGGER.log(Level.CONFIG,
                   "Beginn der Optimierung eines speziellen Konzepts");
        teilkonzepte = bekanntesKonzept.teilkonzepte();
        if (korrKonzErzeugung) {
            // Als Formelkomplexität wird der Wert Null angegeben, da diese
            // in den Kosten der vorhandenen speziellen Konzepte schon
            // berücksichtigt wurde.
            konzeptVerwaltung = new KorrKonzeptVerwaltung(rand, beispieldaten,
                                                          kostenFaktor, 0,
                                                          optSpezItmAnz,
                                                          spezFormItmAnz,
                                                          maxLiteralAnz,
                                                          speicherEffizienz,
                                                          negBoolPraedErz);
        } else {
            // Als Formelkomplexität wird der Wert Null angegeben, da diese
            // in den Kosten der vorhandenen speziellen Konzepte schon
            // berücksichtigt wurde.
            konzeptVerwaltung = new VollKonzeptVerwaltung(rand, beispieldaten,
                                                          kostenFaktor, 0,
                                                          optSpezItmAnz,
                                                          spezFormItmAnz,
                                                          maxLiteralAnz,
                                                          speicherEffizienz,
                                                          negBoolPraedErz);
        }
        konzeptVerwaltung.konzepteAufnehmen(vorhandeneSpezKonz);
        konzeptVerwaltung.erzAlgKonzeptMenge(true);
        konzeptVerwaltung.konzepteAufnehmen(teilkonzepte);

        // Für jedes Teilkonzept versuchen, ein besseres Teilkonzept zu
        // erzeugen.
        teilKonzIter = teilkonzepte.iterator();
        while (teilKonzIter.hasNext()) {
            teilkonzept = (Konzept) teilKonzIter.next();

            teilKonzKomplex = teilkonzept.komplexitaet();
            teilKonzPosFehlAnz = teilkonzept.posFalschAnz();
            teilKonzNegFehlAnz = teilkonzept.negFalschAnz();

            reduziertesKonzept = (KombiKonzept) bekanntesKonzept.clone();
            reduziertesKonzept.entfernen(teilkonzept);
            redKonzPosFehler = reduziertesKonzept.posFalschBsp();
            redKonzNegFehler = reduziertesKonzept.negFalschBsp();

            // Aufnahme aller Prädikate oder Konzepte, die das aktuelle
            // Teilkonzept ersetzen können.
            if (praedikatErzeuger != null) {
                // Es sollen die allgemeinen Konzepte neu aus den Prädikaten
                // erzeugen werden.
                for (int praedErzNr = 0;
                     praedErzNr < praedikatErzeuger.size();
                     praedErzNr++) {
                    erzeuger = (PraedikatErzeugung) praedikatErzeuger.get(praedErzNr);

                    if (korrKonzErzeugung) {
                        praedIter = erzeuger.posVollPraedIter();
                    } else {
                        praedIter = erzeuger.posKorrPraedIter();
                    }
                    while (praedIter.hasNext()) {
                        praedikat = (Praedikat) praedIter.next();
                        konzeptVerwaltung.praedikatAufnehmen(praedikat, false,
                                                             redKonzPosFehler,
                                                             redKonzNegFehler,
                                                             teilKonzKomplex,
                                                             teilKonzPosFehlAnz,
                                                             teilKonzNegFehlAnz);
                    }

                    if (korrKonzErzeugung) {
                        praedIter = erzeuger.negKorrPraedIter();
                    } else {
                        praedIter = erzeuger.negVollPraedIter();
                    }
                    while (praedIter.hasNext()) {
                        praedikat = (Praedikat) praedIter.next();
                        konzeptVerwaltung.praedikatAufnehmen(praedikat, true,
                                                             redKonzPosFehler,
                                                             redKonzNegFehler,
                                                             teilKonzKomplex,
                                                             teilKonzPosFehlAnz,
                                                             teilKonzNegFehlAnz);
                    }

                    praedIter = erzeuger.posAlgPraedIter();
                    while (praedIter.hasNext()) {
                        praedikat = (Praedikat) praedIter.next();
                        konzeptVerwaltung.praedikatAufnehmen(praedikat, false,
                                                             redKonzPosFehler,
                                                             redKonzNegFehler,
                                                             teilKonzKomplex,
                                                             teilKonzPosFehlAnz,
                                                             teilKonzNegFehlAnz);
                    }

                    praedIter = erzeuger.negAlgPraedIter();
                    while (praedIter.hasNext()) {
                        praedikat = (Praedikat) praedIter.next();
                        konzeptVerwaltung.praedikatAufnehmen(praedikat, true,
                                                             redKonzPosFehler,
                                                             redKonzNegFehler,
                                                             teilKonzKomplex,
                                                             teilKonzPosFehlAnz,
                                                             teilKonzNegFehlAnz);
                    }
                }
            } else {
                // Es sollen die vorhandenen allgemeinen Konzepte verwendet
                // werden.
                vhKonzIter = vorhandeneAlgKonz.iterator();
                while (vhKonzIter.hasNext()) {
                    allgemeinesKonzept = (Konzept) vhKonzIter.next();
                    konzeptVerwaltung.konzeptAufnehmen(allgemeinesKonzept,
                                                       redKonzPosFehler,
                                                       redKonzNegFehler,
                                                       teilKonzKomplex,
                                                       teilKonzPosFehlAnz,
                                                       teilKonzNegFehlAnz);
                }
            }
        }

        // Neues Konzept erzeugen.
        neuesKonzept = konzeptVerwaltung.erzeugtesKonzept(optSpezScpIterAnz,
                                                          bekanntesKonzept);

        // Ergänzen:
        // Prüfen, ob neuesKonzept relativ besser ist als bekanntesKonzept.
        // Evtl. iterieren, wenn neuesKonzept relativ besser ist als
        // bekanntesKonzept.

        LOGGER.log(Level.CONFIG,
                   "Ende der Optimierung eines speziellen Konzepts");

        if (neuesKonzept == null) {
            return bekanntesKonzept;
        } else {
            return neuesKonzept;
        }
    }

    /**
     * Erzeugt zum übergegeben Konzept ein optimiertes Konzept, das möglichst
     * besser ist in Bezug auf eine Ergänzung der anderen Konzepte der finalen
     * Formel. Bei der Erzeugung des optimierten Konzept werden alle
     * erzeugbaren Prädikate berücksichtigt.
     *
     * @param praedikatErzeuger   Erzeuger von Prädikaten oder <CODE>null</CODE>,
     *                            wenn nur die vorhandenen speziellen Konzepte
     *                            verwendet werden sollen.
     * @param vorhandeneKonzepte  Die schon vorhandenen speziellen Konzepte.
     * @param bekanntesKonzept    Ein bekanntes finales Konzept, zu dem ein
     *                            optimiertes Konzept erzeugt werden soll.
     * @param formelKomplex       Die geschätzte Komplexität einer Formel.
     * @param kostenFaktor        Faktor, mit dem die Summe der Kosten der
     *                            überdeckten Indices multipliziert wird.
     * @param korrKonzErzeugung   Gibt an, ob ein korrektes Konzepts erzeugt
     *                            werden soll. Falls nicht, wird ein
     *                            vollständiges Konzept erzeugt.
     *
     * @return  Das optimierte Konzept oder das übergebene Konzept, wenn kein
     *          besseres Konzept erzeugt werden konnte.
     */
    public KombiKonzept optimiertesFinKonzept(ArrayList praedikatErzeuger,
                                              Collection vorhandeneKonzepte,
                                              KombiKonzept bekanntesKonzept,
                                              float kostenFaktor,
                                              float formelKomplex,
                                              boolean korrKonzErzeugung) {
        KonzeptVerwaltung  konzeptVerwaltung;
        PraedikatErzeugung erzeuger;
        HashSet            konzepte;
        Konzept            naechstesKonzept, vorhandenesKonzept;
        KombiKonzept       letzteFormel, besteFormel;
        KombiKonzept       redKonzept;
        Praedikat          praedikat;
        BitMathIntSet      bestePosFehler, besteNegFehler;
        BitMathIntSet      redKonzPosFehler, redKonzNegFehler;
        Iterator           bfKonzIter, vhKonzIter, praedIter;

        // Prüfen, ob eine Optimierung stattfinden soll.
        if (optFinItmAnz == 0) {
            return bekanntesKonzept;
        }

        LOGGER.log(Level.CONFIG,
                   "Beginn der Optimierung eines finalen Konzepts");
        letzteFormel = bekanntesKonzept;
        do {
            LOGGER.log(Level.CONFIG,
                       "Neue Optimierung eines finalen Konzepts");
            besteFormel = letzteFormel;
            konzepte = besteFormel.teilkonzepte();
            bestePosFehler = besteFormel.posFalschBsp();
            besteNegFehler = besteFormel.negFalschBsp();

            // Die Konzeptverwaltung erzeugen, in die nur spezielle und keine
            // allgemeinen Konzepte aufgenommen werden.
            if (korrKonzErzeugung) {
                konzeptVerwaltung = new KorrKonzeptVerwaltung(rand, beispieldaten,
                                                              kostenFaktor,
                                                              formelKomplex,
                                                              0, optFinItmAnz,
                                                              maxLiteralAnz,
                                                              speicherEffizienz,
                                                              negBoolPraedErz);
            } else {
                konzeptVerwaltung = new VollKonzeptVerwaltung(rand, beispieldaten,
                                                              kostenFaktor,
                                                              formelKomplex,
                                                              0, optFinItmAnz,
                                                              maxLiteralAnz,
                                                              speicherEffizienz,
                                                              negBoolPraedErz);
            }
            konzeptVerwaltung.konzepteAufnehmen(konzepte);

            // Für jedes Teilkonzept versuchen, ein besseres Teilkonzept zu
            // erzeugen.
            bfKonzIter = konzepte.iterator();
            while (bfKonzIter.hasNext()) {
                naechstesKonzept = (Konzept) bfKonzIter.next();

                redKonzept = (KombiKonzept) besteFormel.clone();
                redKonzept.entfernen(naechstesKonzept);
                redKonzPosFehler = redKonzept.posFalschBsp().difference(bestePosFehler);
                redKonzNegFehler = redKonzept.negFalschBsp().difference(besteNegFehler);

                // Aufnahme aller erzeugbaren Prädikate, die das aktuelle
                // Teilkonzept ersetzen können, falls diese nicht schon alle
                // in der Menge der vorhandenen Konzepte enthalten sind.
                if (praedikatErzeuger != null) {
                    // Die speziellen Konzepte sollen aus den Prädikaten neu
                    // erzeugt werden.
                    for (int praedErzNr = 0;
                         praedErzNr < praedikatErzeuger.size();
                         praedErzNr++) {
                        erzeuger = (PraedikatErzeugung) praedikatErzeuger.get(praedErzNr);

                        if (korrKonzErzeugung) {
                            praedIter = erzeuger.posKorrPraedIter();
                        } else {
                            praedIter = erzeuger.posVollPraedIter();
                        }
                        while (praedIter.hasNext()) {
                            praedikat = (Praedikat) praedIter.next();
                            konzeptVerwaltung.praedikatAufnehmen(praedikat, false,
                                                                 redKonzPosFehler,
                                                                 redKonzNegFehler);
                        }

                        if (korrKonzErzeugung) {
                            praedIter = erzeuger.negVollPraedIter();
                        } else {
                            praedIter = erzeuger.negKorrPraedIter();
                        }
                        while (praedIter.hasNext()) {
                            praedikat = (Praedikat) praedIter.next();
                            konzeptVerwaltung.praedikatAufnehmen(praedikat, true,
                                                                 redKonzPosFehler,
                                                                 redKonzNegFehler);
                        }
                    }
                }

                // Aufnahme aller vorhandenen speziellen Konzepte, die das
                // aktuelle Teilkonzept ersetzen können.
                vhKonzIter = vorhandeneKonzepte.iterator();
                while (vhKonzIter.hasNext()) {
                    vorhandenesKonzept = (Konzept) vhKonzIter.next();
                    konzeptVerwaltung.konzeptAufnehmen(vorhandenesKonzept,
                                                       redKonzPosFehler,
                                                       redKonzNegFehler);
                }
            }
            letzteFormel = konzeptVerwaltung.besteFormel(optFinScpIterAnz,
                                                         bekanntesKonzept);

        } while (letzteFormel.istBesser(besteFormel));

        LOGGER.log(Level.CONFIG,
                   "Ende der Optimierung eines finalen Konzepts");

        return besteFormel;
    }
}

