/*
 * Dateiname      : KonzeptErzeugungTeilprob.java
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
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.rmi.RemoteException;

import de.unistuttgart.architeuthis.userinterfaces.ProblemComputeException;
import de.unistuttgart.architeuthis.userinterfaces.develop.RemoteStore;
import de.unistuttgart.architeuthis.userinterfaces.develop.PartialSolution;
import de.unistuttgart.architeuthis.userinterfaces.develop.CommunicationPartialProblem;
import de.unistuttgart.architeuthis.abstractproblems.ContainerPartialSolution;
import de.unistuttgart.architeuthis.remotestore.hashsetnew.UserRemoteHashSetNew;

import lascer.KonzErzParameter;
import lascer.praedikate.Praedikat;
import lascer.praedikate.PraedikatErzeugung;
import lascer.problemdaten.Beispieldaten;

/**
 * Erzeugt aus Prädikaten der verschiedenen Arten Konzepte. Die verschiedenen
 * Phasen werden mehrmals durchlaufen.
 *
 * @author  Dietmar Lippold
 */
public class KonzeptErzeugungTeilprob implements CommunicationPartialProblem {

    /**
     * Der Logger dieser Klasse.
     */
    private static final Logger LOGGER
        = Logger.getLogger(KonzeptErzeugungTeilprob.class.getName());

    /**
     * Ein Zufallsgenerator.
     */
    private Random rand;

    /**
     * Die Erzeuger von Prädikaten, die als Parameter in der Methode
     * <CODE>compute</CODE> an die Methode <CODE>erzPraedSpezKonzepte</CODE>
     * übergeben werden.
     */
    private ArrayList paramPraedikatErzeuger;

    /**
     * Die Verwaltung korrekter oder vollständiger Konzepte, die als Parameter
     * in der Methode <CODE>compute</CODE> an die Methode
     * <CODE>erzPraedSpezKonzepte</CODE> übergeben wird.
     */
    private KonzeptVerwaltung paramKonzeptVerwaltung;

    /**
     * Der verteilte Speicher oder <CODE>null</CODE>, wenn es keinen gibt.
     */
    private UserRemoteHashSetNew remoteHashSet;

    /**
     * Die der Konzeptbildung zugrunde liegenden Beispieldaten.
     */
    private Beispieldaten beispieldaten;

    /**
     * Das Verfahren zur Optimierung von Konzepten.
     */
    private KonzeptOptimierung konzOpt;

    /**
     * Die in der äußeren Iteration zuletzt ermittelte beste Formel, die als
     * Parameter in der Methode <CODE>compute</CODE> an die Methode
     * <CODE>erzPraedSpezKonzepte</CODE> übergeben wird.
     */
    private KombiKonzept paramAeussereBesteFormel;

    /**
     * Die beste bisher insgesamt gefundene korrekte und möglichst
     * vollständige Formel.
     */
    private KombiKonzept besteKorrFormelGesamt;

    /**
     * Die beste bisher insgesamt gefundene vollständige und möglichst
     * korrekte Formel.
     */
    private KombiKonzept besteVollFormelGesamt;

    /**
     * Die Wahrscheinlichkeit, mit der ein nicht überdecktes bzw. nicht
     * ausgeschlossenes Beispiel als überdeckt bzw. ausgeschlossen behandelt
     * werden soll.
     */
    private float aenderWkt;

    /**
     * Die Anzahl der Interationen der inneren Phase der Konzepterzeugung.
     * Sie gibt an, wie häufig aus den bereits aufgenommenen allgemeinen
     * Konzepten korrekte und vollständige Konzepte erzeugt werden sollen.
     */
    private int innereSollAnz;

    /**
     * Die Anzahl der Interationen der mittleren Phase der Konzepterzeugung.
     * Sie gibt an, wie häufig allgemeine Konzepte aufgenommen und
     * anschließend die innere Iteration durchgeführt werden soll.
     */
    private int mittlereSollAnz;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen von allgemeinen
     * Konzepten zur Erzeugung spezieller Konzepte. Ein negativer Wert steht
     * für eine unbeschränkte Anzahl und es findet keine Auswahl statt.
     */
    private int algSpezItmAnz;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * allgemeinen Konzepte bei der Optimierung eines speziellen Konzepts.
     * Ein negativer Wert steht für eine unbeschränkte Anzahl. Der Wert Null
     * bedeutet, daß keine Optimierung stattfindet.
     */
    private int optSpezItmAnz;

    /**
     * Gibt die Anzahl der durchzuführenden Iterationen beim SCP-Verfahren
     * bei der Erzeugung eines speziellen Konzepts aus den allgemeinen
     * Konzepten an.
     */
    private int erzSpezScpIterAnz;

    /**
     * Gibt die Anzahl der durchzuführenden Iterationen beim SCP-Verfahren
     * bei der Erzeugung einer Formel aus den speziellen, d.h. aus den
     * korrekten oder vollständigen, Konzepten an.
     */
    private int erzFormScpIterAnz;

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
     * Gibt an, ob nach Erzeugung einer korrekten und vollständigen Formel
     * zusätzlich zum jeweils optimalen Konzept weitere spezielle Konzepte
     * erzeugt und in die Auswahl der speziellen Konzepte aufgenommen werden
     * sollen.
     */
    private boolean zusatzKonzErz;

    /**
     * Die Angabe, ob die allgemeinen Konzepte der Erzeugung korrekter
     * Konzepte dienen, die als Parameter in der Methode <CODE>compute</CODE>
     * an die Methode <CODE>erzPraedSpezKonzepte</CODE> übergeben wird. Falls
     * nicht, dienen sie der Erzeugung vollständiger Konzepte.
     */
    private boolean paramKorrKonzErzeugung;

    /**
     * Erzeugt eine neue Instanz. Die Anzahl der Interationen der einzelnen
     * Phasen wird angegeben. Eine negative Anzahl bedeutet eine unbedingte
     * Anzahl, Null bedeutet eine Iteration, solange sich eine Verbesserung
     * ergibt, und eine positive Anzahl bedeutet eine Iteration, bis die
     * Anzahl der Iterationen erreicht ist oder sich keine Verbesserung mehr
     * ergibt.
     *
     * @param rand                      Ein Zufallsgenerator.
     * @param beispieldaten             Die der Konzeptbildung zugrunde
     *                                  liegenden Beispieldaten.
     * @param parameter                 Die Parameter, die die Erzeugung der
     *                                  Prädikat-Erzeuger konfigurieren.
     * @param paramPraedikatErzeuger    Die Erzeuger von Prädikaten als
     *                                  Parameter für die Methode
     *                                  <CODE>erzPraedSpezKonzepte</CODE>.
     * @param paramKonzeptVerwaltung    Die Verwaltung spezieller Konzepte als
     *                                  Parameter für die Methode
     *                                  <CODE>erzPraedSpezKonzepte</CODE>.
     * @param besteKorrFormelGesamt     Die beste bisher insgesamt gefundene
     *                                  korrekte und möglichst vollständige
     *                                  Formel.
     * @param besteVollFormelGesamt     Die beste bisher insgesamt gefundene
     *                                  vollständige und möglichst korrekte
     *                                  Formel.
     * @param paramAeussereBesteFormel  Die bisher beste Formel der äußeren
     *                                  Iteration als Parameter für die
     *                                  Methode
     *                                  <CODE>erzPraedSpezKonzepte</CODE>.
     * @param aenderWkt                 Die Wahrscheinlichkeit, mit der ein
     *                                  nicht überdecktes bzw. nicht
     *                                  ausgeschlossenes Beispiel als
     *                                  überdeckt bzw. ausgeschlossen
     *                                  behandelt werden soll.
     * @param paramKorrKonzErzeugung    Die Angabe, ob korrekter Konzepte
     *                                  erzeugt werden sollen, als Parameter
     *                                  für die Methode
     *                                  <CODE>erzPraedSpezKonzepte</CODE>.
     */
    public KonzeptErzeugungTeilprob(Random rand, Beispieldaten beispieldaten,
                                    KonzErzParameter parameter,
                                    ArrayList paramPraedikatErzeuger,
                                    KonzeptVerwaltung paramKonzeptVerwaltung,
                                    KombiKonzept besteKorrFormelGesamt,
                                    KombiKonzept besteVollFormelGesamt,
                                    KombiKonzept paramAeussereBesteFormel,
                                    float aenderWkt,
                                    boolean paramKorrKonzErzeugung) {

        this.rand = rand;
        this.beispieldaten = beispieldaten;
        this.konzOpt = new KonzeptOptimierung(rand, beispieldaten, parameter);
        this.algSpezItmAnz = parameter.getAlgSpezItmAnz();
        this.speicherEffizienz = parameter.getSpeicherEffizienz();
        this.negBoolPraedErz = parameter.getNegBoolPraedErz();
        this.mittlereSollAnz = parameter.getMittlereIterAnz();
        this.innereSollAnz = parameter.getInnereIterAnz();
        this.optSpezItmAnz = parameter.getOptSpezItmAnz();
        this.zusatzKonzErz = parameter.getZusatzKonzErz();
        this.erzSpezScpIterAnz = parameter.getErzSpezScpIterAnz();
        this.erzFormScpIterAnz = parameter.getErzFormScpIterAnz();
        this.maxLiteralAnz = parameter.getMaxLitAnz();
        this.paramPraedikatErzeuger = paramPraedikatErzeuger;
        this.paramKonzeptVerwaltung = paramKonzeptVerwaltung;
        this.besteKorrFormelGesamt = besteKorrFormelGesamt;
        this.besteVollFormelGesamt = besteVollFormelGesamt;
        this.paramAeussereBesteFormel = paramAeussereBesteFormel;
        this.aenderWkt = aenderWkt;
        this.paramKorrKonzErzeugung = paramKorrKonzErzeugung;
    }

    /**
     * Erzeugt aus den vorhandenen allgemeinen Konzepte spezielle, d.h.
     * korrekte oder vollständige Konzepte. Dies entspricht der inneren Phase
     * der Konzepterzeugung.
     *
     * @param praedikatErzeuger    Erzeuger von Prädikaten.
     * @param konzeptVerwaltung    Die Verwaltung korrekter oder vollständiger
     *                             Konzepte.
     * @param mittlereBesteFormel  Die in der mittleren Iteration zuletzt
     *                             ermittelte beste Formel.
     * @param korrKonzErzeugung    Gibt an, ob die allgemeinen Konzepte der
     *                             Erzeugung korrekter Konzepte dienen. Falls
     *                             nicht, dienen sie der Erzeugung
     *                             vollständiger Konzepte.
     *
     * @return  Die Angabe, ob die Menge der speziellen Konzepte verändert
     *          wurde.
     *
     * @throws RemoteException  Bei einem Problem mit dem Remote-Store.
     */
    private boolean erzeugeSpezKonzepte(ArrayList praedikatErzeuger,
                                        KonzeptVerwaltung konzeptVerwaltung,
                                        KombiKonzept mittlereBesteFormel,
                                        boolean korrKonzErzeugung)
        throws RemoteException {

        HashSet      vorhandeneAlgKonz, vorhandeneSpezKonz, erzeugteKonzepte;
        ArrayList    optPraedErzeuger;
        HashSet      weitereKonzepte;
        KombiKonzept besteFormel, letzteFormel;
        KombiKonzept erzeugtesKonzept, optimiertesKonzept;
        float        kostenFaktor;
        int          innereNochAnz;
        int          innereIstAnz;
        boolean      spezKonzVeraendert;
        boolean      verbesserung;

        LOGGER.log(Level.CONFIG, "Beginn der inneren Iterationen");
        spezKonzVeraendert = false;
        besteFormel = mittlereBesteFormel;
        innereNochAnz = Math.abs(innereSollAnz);
        innereIstAnz = 0;
        verbesserung = true;
        while (verbesserung && ((innereSollAnz == 0) || (innereNochAnz > 0))) {

            innereIstAnz++;
            LOGGER.log(Level.CONFIG, innereIstAnz + ". innere Iteration");

            // Erzeugung eines korrekten oder vollständigen Konzepts aus den
            // allgemeinen Prädikaten und dessen Optimierung.
            LOGGER.log(Level.CONFIG, "Ermittlung eines speziellen Konzepts");
            verbesserung = false;
            erzeugtesKonzept = konzeptVerwaltung.erzeugtesKonzept(erzSpezScpIterAnz,
                                                                  aenderWkt);
            if (erzeugtesKonzept != null) {
                // Es konnte ein spezielles Konzept erzeugt werden.
                if (optSpezItmAnz == 0) {
                    // Es können keine optimierten Konzepte erzeugt werden.
                    verbesserung |= konzeptVerwaltung.konzeptAufnehmen(erzeugtesKonzept);
                    if (remoteHashSet != null) {
                        remoteHashSet.add(erzeugtesKonzept);
                    }
                } else {
                    // Es können optimierte Konzepte erzeugt werden.
                    if (konzeptVerwaltung.alleAlgKonzepteEnthalten()) {
                        optPraedErzeuger = null;
                    } else {
                        optPraedErzeuger = praedikatErzeuger;
                    }
                    vorhandeneAlgKonz = konzeptVerwaltung.enthalteneAlgKonzepte();
                    vorhandeneSpezKonz = konzeptVerwaltung.enthalteneSpezKonzepte();
                    kostenFaktor = konzeptVerwaltung.gibKostenFaktor();
                    optimiertesKonzept = konzOpt.optimiertesSpezKonzept(optPraedErzeuger,
                                                                        vorhandeneAlgKonz,
                                                                        vorhandeneSpezKonz,
                                                                        erzeugtesKonzept,
                                                                        kostenFaktor,
                                                                        korrKonzErzeugung);
                    verbesserung |= konzeptVerwaltung.konzeptAufnehmen(optimiertesKonzept);
                    if (remoteHashSet != null) {
                        remoteHashSet.add(optimiertesKonzept);
                    }
                }
            }

            // Aufnahme der Konzepte, die von den anderen Teilproblemen
            // erzeugt wurden.
            if (remoteHashSet != null) {
                weitereKonzepte = remoteHashSet.newElements();
                verbesserung |= konzeptVerwaltung.konzepteAufnehmen(weitereKonzepte);
            }

            // Wenn schon eine korrekte und vollständige Formel erzeugt
            // werden kann, Erzeugung einer Menge weiterer korrekter oder
            // vollständiger Konzepte ohne Optimierung.
            if (zusatzKonzErz && (besteFormel != null)
                && besteFormel.istKorrekt() && besteFormel.istVollstaendig()) {

                LOGGER.log(Level.CONFIG,
                           "Ermittlung weiterer nicht optimierter Konzepte");
                erzeugteKonzepte = konzeptVerwaltung.erzeugteKonzepte(erzSpezScpIterAnz);
                LOGGER.log(Level.CONFIG,
                           "Aufnahme der erzeugten " + erzeugteKonzepte.size()
                           + " Konzepte");
                verbesserung |= konzeptVerwaltung.konzepteAufnehmen(erzeugteKonzepte);
            }

            LOGGER.log(Level.CONFIG,
                       "Neue spezielle Konzepte aufgenommen: " + verbesserung);
            spezKonzVeraendert |= verbesserung;

            // Anzahl der durchzuführenden Iterationen verringern.
            if (innereNochAnz > 0) {
                innereNochAnz--;
            }

            // Wenn keine feste Anzahl von Iterationen vorgegeben wurde, ob
            // tatsächlich eine Verbesserung stattgefunden hat.
            if (verbesserung
                && ((innereSollAnz == 0)
                    || (innereSollAnz > 0) && (innereNochAnz > 0))) {

                // Ermitteln, ob eine Verbesserung stattgefunden hat.
                letzteFormel =  konzeptVerwaltung.besteFormel(erzFormScpIterAnz);
                if ((besteFormel == null)
                        || letzteFormel.istBesser(besteFormel)) {
                    verbesserung = true;
                    besteFormel = letzteFormel;
                } else {
                    verbesserung = false;
                }
                LOGGER.log(Level.CONFIG,
                           "Tatsächliche Verbesserung: " + verbesserung);

                // Wenn die neue beste Formel besser ist als die bisher
                // insgesamt beste Formel, die neue speichern.
                if (korrKonzErzeugung) {
                    if (besteFormel.istBesser(besteKorrFormelGesamt)) {
                        besteKorrFormelGesamt = besteFormel;
                    }
                } else {
                    if (besteFormel.istBesser(besteVollFormelGesamt)) {
                        besteVollFormelGesamt = besteFormel;
                    }
                }
            }
        }
        LOGGER.log(Level.CONFIG, "Ende der inneren Iterationen");

        return spezKonzVeraendert;
    }

    /**
     * Erzeugt mittels der übergebenen Prädikat-Erzeuger nacheinander
     * Prädikate zu allgemeinen Konzepten und daraus spezielle, d.h. korrekte
     * oder vollständige Konzepte. Dies entspricht der mittleren und inneren
     * Phase der Konzepterzeugung.
     *
     * @param praedikatErzeuger    Erzeuger von Prädikaten.
     * @param konzeptVerwaltung    Die Verwaltung korrekter oder vollständiger
     *                             Konzepte.
     * @param aeussereBesteFormel  Die in der äußeren Iteration zuletzt
     *                             ermittelte beste Formel.
     * @param korrKonzErzeugung    Gibt an, ob die allgemeinen Konzepte der
     *                             Erzeugung korrekter Konzepte dienen. Falls
     *                             nicht, dienen sie der Erzeugung
     *                             vollständiger Konzepte.
     *
     * @return  Die Angabe, ob bei einem erneuten Aufruf mit gleichen Daten
     *          eine weitere Verbesserung möglich ist.
     *
     * @throws RemoteException  Bei einem Problem mit dem Remote-Store.
     */
    private boolean erzPraedSpezKonzepte(ArrayList praedikatErzeuger,
                                         KonzeptVerwaltung konzeptVerwaltung,
                                         KombiKonzept aeussereBesteFormel,
                                         boolean korrKonzErzeugung)
        throws RemoteException {

        PraedikatErzeugung erzeuger;
        Iterator     praedIter;
        Praedikat    praedikat;
        KombiKonzept besteFormel, letzteFormel;
        int          mittlereNochAnz;
        int          mittlereIstAnz;
        int          vorhandeneAlgKonzAnz;
        boolean      verbesserung;
        boolean      aufnahme;
        boolean      alleKonzEnthalten;
        boolean      fehlerVorhandenAlt, fehlerVorhandenNeu;

        // Prüfen, ob allgemeine Konzepte erzeugt werden sollen.
        if (algSpezItmAnz == 0) {
            LOGGER.log(Level.CONFIG,
                       "Es wird keine mittlere Iteration durchgeführt");
            return false;
        }

        LOGGER.log(Level.CONFIG, "Beginn der mittleren Iterationen");
        besteFormel = aeussereBesteFormel;
        mittlereNochAnz = Math.abs(mittlereSollAnz);
        mittlereIstAnz = 0;
        verbesserung = true;
        while (verbesserung && ((mittlereSollAnz == 0) || (mittlereNochAnz > 0))) {

            mittlereIstAnz++;
            LOGGER.log(Level.CONFIG, mittlereIstAnz + ". mittlere Iteration");
            aufnahme = false;

            // Allgemeine Prädikate erzeugen und aufnehmen, wenn diese nicht
            // schon alle vorhanden sind.
            vorhandeneAlgKonzAnz = konzeptVerwaltung.enthalteneAlgKonzeptAnz();
            alleKonzEnthalten = konzeptVerwaltung.alleAlgKonzepteEnthalten();
            fehlerVorhandenAlt = konzeptVerwaltung.zuletztFehlerVorhanden();
            konzeptVerwaltung.erzAlgKonzeptMenge(true);
            fehlerVorhandenNeu = konzeptVerwaltung.zuletztFehlerVorhanden();

            if ((vorhandeneAlgKonzAnz == 0)
                || !alleKonzEnthalten
                || (fehlerVorhandenNeu != fehlerVorhandenAlt)) {

                // Die Menge der vorhandenen allgemeinen Konzepte enthält
                // wahrscheinlich nicht alle erzeugbaren allgemeinen
                // Konzepte.
                LOGGER.log(Level.CONFIG, "Erzeugung allgemeiner Prädikate");

                // Wenn das erste Mal eine fehlerfreie Formel erzeugt werden
                // kann, die gespeicherten allgemeinen Konzepte löschen.
                if (fehlerVorhandenNeu != fehlerVorhandenAlt) {
                    konzeptVerwaltung.erzAlgKonzeptMenge(false);
                }

                // Neue Konzepte erzeugen.
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
                        aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, false);
                    }

                    if (korrKonzErzeugung) {
                        praedIter = erzeuger.negKorrPraedIter();
                    } else {
                        praedIter = erzeuger.negVollPraedIter();
                    }
                    while (praedIter.hasNext()) {
                        praedikat = (Praedikat) praedIter.next();
                        aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, true);
                    }

                    praedIter = erzeuger.posAlgPraedIter();
                    while (praedIter.hasNext()) {
                        praedikat = (Praedikat) praedIter.next();
                        aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, false);
                    }

                    praedIter = erzeuger.negAlgPraedIter();
                    while (praedIter.hasNext()) {
                        praedikat = (Praedikat) praedIter.next();
                        aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, true);
                    }
                }
                LOGGER.log(Level.CONFIG,
                           "Allgemeine Prädikat aufgenommen: " + aufnahme);
            } else {
                LOGGER.log(Level.CONFIG,
                           "Allgemeine Prädikate brauchen nicht erzeugt zu werden");
            }
            verbesserung = aufnahme;

            // Ausführung der inneren Iteration.
            verbesserung |= erzeugeSpezKonzepte(praedikatErzeuger, konzeptVerwaltung,
                                                besteFormel, korrKonzErzeugung);

            // Anzahl der durchzuführenden Iterationen verringern.
            if (mittlereNochAnz > 0) {
                mittlereNochAnz--;
            }

            // Wenn keine feste Anzahl von Iterationen vorgegeben wurde,
            // prüfen, ob tatsächlich eine Verbesserung stattgefunden hat.
            // Auch wenn eine maximale Anzahl für die Literale vorgegeben ist,
            // eine neue Formel erzeugen.
            if (verbesserung
                && ((mittlereSollAnz == 0)
                    || (mittlereSollAnz > 0) && (mittlereNochAnz > 0)
                    || (maxLiteralAnz > 0) && (mittlereNochAnz > 0))) {

                // Ermitteln, ob eine Verbesserung stattgefunden hat.
                letzteFormel =  konzeptVerwaltung.besteFormel(erzFormScpIterAnz);
                if ((besteFormel == null)
                        || letzteFormel.istBesser(besteFormel)) {
                    verbesserung = true;
                    besteFormel = letzteFormel;
                } else {
                    verbesserung = false;
                }
                LOGGER.log(Level.CONFIG,
                           "Tatsächliche Verbesserung: " + verbesserung);

                // Wenn die neue beste Formel besser ist als die bisher
                // insgesamt beste Formel, die neue speichern.
                if (korrKonzErzeugung) {
                    if (besteFormel.istBesser(besteKorrFormelGesamt)) {
                        besteKorrFormelGesamt = besteFormel;
                    }
                } else {
                    if (besteFormel.istBesser(besteVollFormelGesamt)) {
                        besteVollFormelGesamt = besteFormel;
                    }
                }
            }
        }
        LOGGER.log(Level.CONFIG, "Ende der mittleren Iterationen");

        return verbesserung;
    }

    /**
     * Startet die Berechnung des Teilproblems.
     *
     * @param store  Der Verteilte Speicher (RemoteStore).
     *
     * @return  Die berechnete Teillösung.
     *
     * @throws ProblemComputeException  Bei beliebigen Fehlern bei der
     *                                  Berechnung.
     * @throws RemoteException          Bei Problemen bei der Kommunikation
     *                                  mit dem RemoteStore.
     */
    public PartialSolution compute(RemoteStore store) throws ProblemComputeException,
                                                             RemoteException {
        ArchiLoesung loesung;
        KombiKonzept besteFormelGesamt;
        boolean      weiterVerbesserMoeglich;

        remoteHashSet = (UserRemoteHashSetNew) store;
        weiterVerbesserMoeglich = erzPraedSpezKonzepte(paramPraedikatErzeuger,
                                                       paramKonzeptVerwaltung,
                                                       paramAeussereBesteFormel,
                                                       paramKorrKonzErzeugung);
        if (paramKorrKonzErzeugung) {
            besteFormelGesamt = besteKorrFormelGesamt;
        } else {
            besteFormelGesamt = besteVollFormelGesamt;
        }
        loesung = new ArchiLoesung(paramKonzeptVerwaltung.enthalteneSpezKonzepte(),
                                   besteFormelGesamt, weiterVerbesserMoeglich);
        return (new ContainerPartialSolution(loesung));
    }
}

