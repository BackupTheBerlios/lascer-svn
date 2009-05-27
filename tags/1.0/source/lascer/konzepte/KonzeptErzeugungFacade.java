/*
 * Dateiname      : KonzeptErzeugungFacade.java
 * Letzte �nderung: 21. November 2007
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
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.Serializable;

import mathCollection.BitMathIntSet;

import de.unistuttgart.architeuthis.facade.ProblemComputation;
import de.unistuttgart.architeuthis.remotestore.hashsetnew.RemoteHashSetNewGenerator;

import lascer.KonzErzParameter;
import lascer.praedikate.Praedikat;
import lascer.praedikate.PraedikatErzeugung;
import lascer.problemdaten.Beispieldaten;
import lascer.konzepte.einzelne.Disjunktion;
import lascer.konzepte.einzelne.Konjunktion;

/**
 * Erzeugt aus Pr�dikaten der verschiedenen Arten Konzepte. Die verschiedenen
 * Phasen werden mehrmals durchlaufen.
 *
 * @author  Dietmar Lippold
 */
public class KonzeptErzeugungFacade {

    /**
     * Der Logger dieser Klasse.
     */
    private static final Logger LOGGER
        = Logger.getLogger(KonzeptErzeugungFacade.class.getName());

    /**
     * Ein Zufallsgenerator.
     */
    private Random rand;

    /**
     * Die der Konzeptbildung zugrunde liegenden Beispieldaten.
     */
    private Beispieldaten beispieldaten;

    /**
     * Die Parameter zur Konfiguration der Erzeugung der Konzepte.
     */
    private KonzErzParameter parameter;

    /**
     * Die beste bisher insgesamt gefundene korrekte und m�glichst
     * vollst�ndige Formel.
     */
    private KombiKonzept besteKorrFormelGesamt;

    /**
     * Die beste bisher insgesamt gefundene vollst�ndige und m�glichst
     * korrekte Formel.
     */
    private KombiKonzept besteVollFormelGesamt;

    /**
     * Der Name des Dispatchers des ComputeSystems, das zur Berechnung
     * verwendet werden soll.
     */
    private String dispatcher;

    /**
     * Der URL (in der Regel auf einem WWW-Server), unter dem die Klassen
     * dieses Packages bereitgestellt werden, um f�r das ComputeSystem
     * verf�gbar zu sein.
     */
    private String classUrl;

    /**
     * Die gesch�tzte maximale Gesamt-Komplexit�t einer Formel. Dieser Wert
     * ist nur bei Vorgabe der Anzahl an Literalen, die maximal in einer
     * Disjunktion bzw. Konjunktion enthalten sein sollen (Attribut
     * <CODE>maxLiteralAnz</CODE>), von Bedeutung.
     */
    private float initGesamtKomplex;

    /**
     * Die Wahrscheinlichkeit, mit der bei einer parallelen Verarbeitung
     * mehrerer Teilprobleme ein nicht �berdecktes bzw. nicht ausgeschlossenes
     * Beispiel als �berdeckt bzw. ausgeschlossen behandelt werden soll.
     */
    private float parallelAenderWkt;

    /**
     * F�r eine lokale Berechnung die Anzahl der Teilprobleme, die dem Problem
     * vorgeschlagen werden soll. Bei einer verteilten Berechnung hat dieser
     * Wert keine Bedeutung.
     */
    private int lokaleTeilprobAnz;

    /**
     * Die Anzahl der Literale, die maximal in einer Disjunktion bzw. in einer
     * Konjunktion enthalten sein sollen. Der Wert Null steht f�r eine
     * unbegrenzte Anzahl.
     */
    private int maxLiteralAnz;

    /**
     * Die Anzahl der Interationen der inneren Phase der Konzepterzeugung.
     * Sie gibt an, wie h�ufig aus den bereits aufgenommenen allgemeinen
     * Konzepten korrekte und vollst�ndige Konzepte erzeugt werden sollen.
     */
    private int innereSollAnz;

    /**
     * Die Anzahl der Interationen der mittleren Phase der Konzepterzeugung.
     * Sie gibt an, wie h�ufig allgemeine Konzepte aufgenommen und
     * anschlie�end die innere Iteration durchgef�hrt werden soll.
     */
    private int mittlereSollAnz;

    /**
     * Die Anzahl der Interationen der �u�eren Phase der Konzepterzeugung.
     * Sie gibt an, wie h�ufig korrekte und vollst�ndige Konzepte aufgenommen
     * und anschlie�end die mittlere Iteration durchgef�hrt werden soll.
     */
    private int aeussereSollAnz;

    /**
     * Die Anzahl der Interationen der gesamten Konzepterzeugung, also die
     * Anzahl, wie oft nacheinander ein bestes Konzept erzeugt werden soll.
     */
    private int gesamtIterAnz;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen von allgemeinen
     * Konzepten zur Erzeugung spezieller Konzepte. Ein negativer Wert steht
     * f�r eine unbeschr�nkte Anzahl und es findet keine Auswahl statt.
     */
    private int algSpezItmAnz;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen von speziellen, d.h.
     * korrekten oder vollst�ndigen, Konzepten zur Erzeugung von Formeln. Ein
     * negativer Wert steht f�r eine unbeschr�nkte Anzahl und es findet keine
     * Auswahl statt.
     */
    private int spezFormItmAnz;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * allgemeinen Konzepte bei der Optimierung eines speziellen Konzepts.
     * Ein negativer Wert steht f�r eine unbeschr�nkte Anzahl. Der Wert Null
     * bedeutet, da� keine Optimierung stattfindet.
     */
    private int optSpezItmAnz;

    /**
     * Gibt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     * bei der Erzeugung eines speziellen Konzepts aus den allgemeinen
     * Konzepten an.
     */
    private int erzSpezScpIterAnz;

    /**
     * Gibt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     * bei der Erzeugung einer Formel aus den speziellen, d.h. aus den
     * korrekten oder vollst�ndigen, Konzepten an.
     */
    private int erzFormScpIterAnz;

    /**
     * Gibt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     * bei der Erzeugung einer finalen Formel aus den speziellen, d.h. aus
     * den korrekten oder vollst�ndigen, Konzepten an.
     */
    private int finFormScpIterAnz;

    /**
     * Gibt an, in welchem Ausma� die Teilmengen besonders Speicher-effizient
     * aber dadurch weniger Laufzeit-effizient verwaltet werden sollen. Der
     * Wert ist gleich oder gr��er Null (maximale Laufzeit-Effizienz) und
     * kleiner oder gleich Zwei (maximale Speicher-Effizienz).
     */
    private int speicherEffizienz;

    /**
     * Gibt an, ob die Berechnung der Teilprobleme auf dem Compute-System
     * verteilt erfolgen soll. Falls nicht, erfolgt sie lokal.
     */
    private boolean verteilteBerechnung;

    /**
     * Gibt an, ob boolsche Attribute negiert werden sollen, d.h. invertierte
     * Literale dazu erzeugt werden sollen.
     */
    private boolean negBoolPraedErz;

    /**
     * Liefert einen Wert f�r die Effizienz des Konzepts, unabh�ngig von
     * anderen Konzepten. Dies ist der Quotient aus der Anzahl der �berdeckten
     * positiven und der ausgeschlossenen negativen Beispiele und der
     * Komplexit�t des Konzepts.
     *
     * @param konzept  Das Konzept, dessen Effizienz geliefert werden soll.
     *
     * @return  Einen Wert f�r die Effizienz des Konzepts. Wenn der Wert des
     *          �bergebenen Konzepts <CODE>null</CODE> ist, wird der Wert Null
     *          geliefert.
     */
    public static float konzeptEffizienz(Konzept konzept) {

        if (konzept == null) {
            return 0;
        } else {
            return ((konzept.posRichtigAnz() + konzept.negRichtigAnz())
                    / konzept.komplexitaet());
        }
    }

    /**
     * Liefert das effizienteste Konzept der �bergebenen Sammlung.
     *
     * @param konzepte  Die Sammlung der Konzepte, von der das effizienteste
     *                  geliefert werden soll.
     *
     * @return  Das effizienteste Konzept der �bergebenen Sammlung oder
     *          <CODE>null</CODE>, wenn die Sammlung kein Konzept enth�lt.
     */
    public static Konzept effizientesteKonzept(Collection konzepte) {
        Iterator konzIter;
        Konzept  bestKonzept, naechstKonzept;
        float    bestBewertung, naechstBewertung;

        bestKonzept = null;
        bestBewertung = -1;

        konzIter = konzepte.iterator();
        while (konzIter.hasNext()) {
            naechstKonzept = (Konzept) konzIter.next();
            naechstBewertung = konzeptEffizienz(naechstKonzept);
            if (naechstBewertung > bestBewertung) {
                bestKonzept = naechstKonzept;
                bestBewertung = naechstBewertung;
            }
        }

        return bestKonzept;
    }

    /**
     * Erzeugt eine neue Instanz und weist die �bergebenen Werte den
     * entsprechenden Attributen zu. Au�erdem werden ein Zufallsgenerator und
     * die Verwaltungen der korrekten und der vollst�ndigen Konzepte erzeugt.
     *
     * @param beispieldaten      Die der Konzeptbildung zugrunde liegenden
     *                           Beispieldaten.
     * @param gesamtIterAnz      Die Anzahl der Interationen der gesamten
     *                           Konzepterzeugung.
     * @param zufaellig          Gibt an, ob der Zufallsgenerator bei jedem
     *                           Aufruf mit einem anderen Wert initialisiert
     *                           werden soll. Anderenfalls wird er jedes Mal
     *                           mit dem gleichen Wert initialisiert.
     */
    private KonzeptErzeugungFacade(Beispieldaten beispieldaten,
                                   int gesamtIterAnz, boolean zufaellig) {

        this.beispieldaten = beispieldaten;
        this.gesamtIterAnz = gesamtIterAnz;

        // Erzeugung des Zufallsgenerators.
        rand = new Random();
        if (zufaellig) {
            rand.setSeed((new Date()).getTime());
        } else {
            rand.setSeed(Konstanten.SEED);
        }

        // Die initialen besten Formeln erzeugen. Diese enthalten noch die
        // maximale Anzahl von Fehlern.
        besteKorrFormelGesamt = new Disjunktion(beispieldaten);
        besteVollFormelGesamt = new Konjunktion(beispieldaten);
    }

    /**
     * Erzeugt eine neue Instanz. Die Anzahl der Interationen der einzelnen
     * Phasen wird angegeben. Eine negative Anzahl bedeutet eine unbedingte
     * Anzahl, Null bedeutet eine Iteration, solange sich eine Verbesserung
     * ergibt, und eine positive Anzahl bedeutet eine Iteration, bis die
     * Anzahl der Iterationen erreicht ist oder sich keine Verbesserung mehr
     * ergibt.
     *
     * @param beispieldaten  Die der Konzeptbildung zugrunde liegenden
     *                       Beispieldaten.
     * @param parameter      Die Parameter, die die Erzeugung der
     *                       Pr�dikat-Erzeuger konfigurieren.
     */
    public KonzeptErzeugungFacade(Beispieldaten beispieldaten,
                                  KonzErzParameter parameter) {

        this(beispieldaten, parameter.getGesamtIterAnz(), parameter.getZufall());

        this.parameter = parameter;
        this.dispatcher = parameter.getDispatcher();
        this.classUrl = "http://"
                        + parameter.getClassServerName()
                        + ":" + parameter.getClassServerPort() + "/";
        this.parallelAenderWkt = parameter.getParallelAenderWkt();
        this.lokaleTeilprobAnz = parameter.getLokaleTeilprobAnz();
        this.verteilteBerechnung = parameter.getVerteilt();
        this.initGesamtKomplex = parameter.getInitGesamtKomplex();
        this.maxLiteralAnz = parameter.getMaxLitAnz();
        this.algSpezItmAnz = parameter.getAlgSpezItmAnz();
        this.spezFormItmAnz = parameter.getSpezFormItmAnz();
        this.speicherEffizienz = parameter.getSpeicherEffizienz();
        this.negBoolPraedErz = parameter.getNegBoolPraedErz();
        this.aeussereSollAnz = parameter.getAeussereIterAnz();
        this.mittlereSollAnz = parameter.getMittlereIterAnz();
        this.innereSollAnz = parameter.getInnereIterAnz();
        this.optSpezItmAnz = parameter.getOptSpezItmAnz();
        this.erzSpezScpIterAnz = parameter.getErzSpezScpIterAnz();
        this.erzFormScpIterAnz = parameter.getErzFormScpIterAnz();
        this.finFormScpIterAnz = parameter.getFinFormScpIterAnz();
    }

    /**
     * Erzeugt eine neue Instanz. Die einzelnen Phasen werden so lange
     * durchlaufen, wie sich die Anzahl der Fehler vom besten Konzept
     * verringert. Die Anzahl der zu speichernden Teilmengen ist nicht
     * beschr�nkt. Die Teilmengen werden besonders Laufzeit-effizient
     * verwaltet und der Zufallsgenerator wird jedes Mal mit dem gleichen Wert
     * initialisiert.
     *
     * @param beispieldaten  Die der Konzeptbildung zugrunde liegenden
     *                       Beispieldaten.
     * @param gesamtIterAnz  Die Anzahl der Iterationen der gesamten
     *                       Konzepterzeugung.
     */
    public KonzeptErzeugungFacade(Beispieldaten beispieldaten, int gesamtIterAnz) {

        this(beispieldaten, gesamtIterAnz, false);

        this.parameter = null;
        this.maxLiteralAnz = 0;
        this.initGesamtKomplex = 0;
        this.aeussereSollAnz = 0;
        this.mittlereSollAnz = 0;
        this.innereSollAnz = 0;
        this.optSpezItmAnz = -1;
        this.algSpezItmAnz = -1;
        this.spezFormItmAnz = -1;
        this.speicherEffizienz = 0;
        this.dispatcher = lascer.Konstanten.DISPATCHER;
        this.classUrl = "http://"
                        + lascer.Konstanten.CLASS_SERVER_NAME
                        + ":" + lascer.Konstanten.CLASS_SERVER_PORT + "/";
        this.lokaleTeilprobAnz = lascer.Konstanten.LOKALE_TEILPROB_ANZ;
        this.verteilteBerechnung = lascer.Konstanten.VERTEILT;
        this.negBoolPraedErz = lascer.Konstanten.NEG_BOOL_PRAED_ERZ;
        this.erzSpezScpIterAnz = lascer.Konstanten.ERZ_SPEZ_SCP_ITER_ANZ;
        this.erzFormScpIterAnz = lascer.Konstanten.ERZ_FORM_SCP_ITER_ANZ;
        this.finFormScpIterAnz = lascer.Konstanten.FIN_FORM_SCP_ITER_ANZ;
    }

    /**
     * Liefert eine Menge von Konzepten, die zuf�llig aus den �bergebenen
     * Konzepten ausgew�hlt wurden. Wenn die �bergebene Menge nicht leer ist,
     * enth�lt die gelieferte Menge mindestens ein Konzepte weniger.
     *
     * @param konzepte  Eine Menge von Konzepten, aus denen zuf�llig welche
     *                  ausgew�hlt werden.
     *
     * @return  Eine Menge von zuf�llig ausgew�hlten Konzepten.
     */
    private HashSet auswahl(HashSet konzepte) {
        HashSet  auswahl = new HashSet();
        Iterator iterator;
        Konzept  konzept;
        float    auswahlWkt;

        if (konzepte.size() > 0) {
            do {
                // Auswahl aus dem m�glichen vorherigen Durchlauf l�schen.
                auswahl.clear();

                // Wahrscheinlichkeit der Auswahl der Konzepte festlegen.
                auswahlWkt = rand.nextFloat();

                // Konzepte ausw�hlen.
                iterator = konzepte.iterator();
                while (iterator.hasNext()) {
                    konzept = (Konzept) iterator.next();
                    if (rand.nextFloat() < auswahlWkt) {
                        auswahl.add(konzept);
                    }
                }
            } while (auswahl.size() == konzepte.size());
        }

        return auswahl;
    }

    /**
     * Erzeugt mittels der �bergebenen Pr�dikat-Erzeuger nacheinander
     * Pr�dikate zu allgemeinen Konzepten und daraus spezielle, d.h. korrekte
     * oder vollst�ndige Konzepte. Dies entspricht der mittleren und inneren
     * Phase der Konzepterzeugung.
     *
     * @param praedikatErzeuger    Erzeuger von Pr�dikaten.
     * @param konzeptVerwaltung    Die Verwaltung korrekter oder vollst�ndiger
     *                             Konzepte.
     * @param aeussereBesteFormel  Die in der �u�eren Iteration zuletzt
     *                             ermittelte beste Formel.
     * @param korrKonzErzeugung    Gibt an, ob die allgemeinen Konzepte der
     *                             Erzeugung korrekter Konzepte dienen. Falls
     *                             nicht, dienen sie der Erzeugung
     *                             vollst�ndiger Konzepte.
     *
     * @return  Die Angabe, ob bei einem erneuten Aufruf mit gleichen Daten
     *          eine weitere Verbesserung m�glich ist.
     */
    private boolean erzPraedSpezKonzepte(ArrayList praedikatErzeuger,
                                         KonzeptVerwaltung konzeptVerwaltung,
                                         KombiKonzept aeussereBesteFormel,
                                         boolean korrKonzErzeugung) {
        RemoteHashSetNewGenerator remoteStoreGen;
        ProblemComputation        problemComputation;
        KonzeptErzeugungProblem   problem;
        Serializable              serialLoesung;
        ArchiLoesung              archiLoesung;
        KombiKonzept              besteFormel;
        float                     kostenFaktor;

        // Pr�fen, ob allgemeine Konzepte erzeugt werden sollen.
        if (algSpezItmAnz == 0) {
            LOGGER.log(Level.CONFIG,
                       "Es wird keine mittlere Iteration durchgef�hrt");
            return false;
        }

        LOGGER.log(Level.CONFIG, "Vor Beginn der mittleren Iterationen");

        remoteStoreGen = new RemoteHashSetNewGenerator();
        problemComputation = new ProblemComputation();
        kostenFaktor = konzeptVerwaltung.gibKostenFaktor();
        problem = new KonzeptErzeugungProblem(rand, beispieldaten, parameter,
                                              praedikatErzeuger, konzeptVerwaltung,
                                              besteKorrFormelGesamt,
                                              besteVollFormelGesamt,
                                              aeussereBesteFormel,
                                              parallelAenderWkt,
                                              korrKonzErzeugung);
        try {
            if (verteilteBerechnung) {
                serialLoesung = problemComputation.transmitProblem(problem,
                                                                   remoteStoreGen,
                                                                   dispatcher,
                                                                   classUrl);
            } else {
                serialLoesung = problemComputation.computeProblem(problem,
                                                                  lokaleTeilprobAnz,
                                                                  remoteStoreGen);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        archiLoesung = (ArchiLoesung) serialLoesung;

        // Die erzeugten Konzepte aufnehmen.
        konzeptVerwaltung.konzepteAufnehmen(archiLoesung.erzeugteKonzepte());

        // Wenn die neue beste Formel besser ist als die bisher insgesamt
        // beste Formel, die neue speichern.
        besteFormel = archiLoesung.besteFormel();
        if (korrKonzErzeugung) {
            if (besteFormel.istBesser(besteKorrFormelGesamt)) {
                besteKorrFormelGesamt = besteFormel;
            }
        } else {
            if (besteFormel.istBesser(besteVollFormelGesamt)) {
                besteVollFormelGesamt = besteFormel;
            }
        }

        LOGGER.log(Level.CONFIG, "Nach Ende der mittleren Iterationen");

        return archiLoesung.weiterVerbesserMoeglich();
    }

    /**
     * Liefert ein spezielles Konzept, das einen �bergebenen Index eines
     * Beispiels enth�lt oder einen anderen �bergebenen Index nicht enth�lt.
     * Das Konzept wird mittels der �bergebenen Pr�dikat-Erzeuger aus allen
     * Pr�dikaten zu allgemeinen Konzepten erzeugt. Nach der Erzeugung wird
     * das Konzept gegebenenfalls noch optimiert.
     *
     * @param praedikatErzeuger   Die Erzeuger von Pr�dikaten.
     * @param konzeptVerwaltung   Die Verwaltung der speziellen Konzepte.
     * @param formelKomplexitaet  Die gesch�tzte Komplexit�t einer Formel.
     * @param posBspIndex         Der Index eines Beispiels, f�r das das
     *                            Konzept erf�llt sein soll, oder ein
     *                            negativer Wert.
     * @param negBspIndex         Der Index eines Beispiels, f�r das das
     *                            Konzept nicht erf�llt sein soll, oder ein
     *                            negativer Wert.
     * @param korrKonzErzeugung   Gibt an, ob ein korrektes Konzepts erzeugt
     *                            werden soll. Falls nicht, wird ein
     *                            vollst�ndiges Konzept erzeugt.
     *
     * @return  Das erzeugte spezielle Konzept oder den Wert <CODE>null</CODE>,
     *          wenn keines erzeugt werden konnte.
     */
    private KombiKonzept praedSpezKonzept(ArrayList praedikatErzeuger,
                                          KonzeptVerwaltung konzeptVerwaltung,
                                          float formelKomplexitaet,
                                          int posBspIndex, int negBspIndex,
                                          boolean korrKonzErzeugung) {
        KonzeptErzeugungTeilprob konzErz;
        KonzeptVerwaltung  neueKonzVerwalt;
        PraedikatErzeugung erzeuger;
        KonzeptOptimierung konzOpt;
        HashSet            vorhandeneSpezKonz;
        HashSet            vorhandeneAlgKonz;
        ArrayList          optPraedErzeuger;
        Iterator           praedIter;
        Iterator           konzIter;
        Praedikat          praedikat;
        KombiKonzept       erzeugtesKonzept;
        Konzept            allgemeinesKonzept;
        float              kostenFaktor;

        LOGGER.log(Level.CONFIG,
                   "Neue Erzeugung eines speziellen Konzepts zu einzelnen"
                   + " Indices");

        vorhandeneSpezKonz = konzeptVerwaltung.enthalteneSpezKonzepte();
        vorhandeneAlgKonz = konzeptVerwaltung.enthalteneAlgKonzepte();
        kostenFaktor = konzeptVerwaltung.gibKostenFaktor();

        if (korrKonzErzeugung) {
            neueKonzVerwalt = new KorrKonzeptVerwaltung(rand, beispieldaten,
                                                        kostenFaktor,
                                                        formelKomplexitaet,
                                                        algSpezItmAnz,
                                                        spezFormItmAnz,
                                                        maxLiteralAnz,
                                                        speicherEffizienz,
                                                        negBoolPraedErz);
        } else {
            neueKonzVerwalt = new VollKonzeptVerwaltung(rand, beispieldaten,
                                                        kostenFaktor,
                                                        formelKomplexitaet,
                                                        algSpezItmAnz,
                                                        spezFormItmAnz,
                                                        maxLiteralAnz,
                                                        speicherEffizienz,
                                                        negBoolPraedErz);
        }
        neueKonzVerwalt.konzepteAufnehmen(vorhandeneSpezKonz);
        neueKonzVerwalt.erzAlgKonzeptMenge(true);

        // Aufnahme aller allgemeinen Pr�dikate oder Konzepte, die die
        // Beispiel-Indices enthalten oder nicht enthalten.
        if ((vorhandeneAlgKonz.size() == 0)
            || !konzeptVerwaltung.alleAlgKonzepteEnthalten()) {

            // Die Menge der vorhandenen allgemeinen Konzepte ist leer oder
            // enth�lt wahrscheinlich nicht alle erzeugten allgemeinen
            // Konzepte. Daher die allgemeinen Konzepte neu aus den Pr�dikaten
            // erzeugen.
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
                    neueKonzVerwalt.praedikatAufnehmen(praedikat, false,
                                                       posBspIndex, negBspIndex);
                }

                if (korrKonzErzeugung) {
                    praedIter = erzeuger.negKorrPraedIter();
                } else {
                    praedIter = erzeuger.negVollPraedIter();
                }
                while (praedIter.hasNext()) {
                    praedikat = (Praedikat) praedIter.next();
                    neueKonzVerwalt.praedikatAufnehmen(praedikat, true,
                                                       posBspIndex, negBspIndex);
                }

                praedIter = erzeuger.posAlgPraedIter();
                while (praedIter.hasNext()) {
                    praedikat = (Praedikat) praedIter.next();
                    neueKonzVerwalt.praedikatAufnehmen(praedikat, false,
                                                       posBspIndex, negBspIndex);
                }

                praedIter = erzeuger.negAlgPraedIter();
                while (praedIter.hasNext()) {
                    praedikat = (Praedikat) praedIter.next();
                    neueKonzVerwalt.praedikatAufnehmen(praedikat, true,
                                                       posBspIndex, negBspIndex);
                }
            }
        } else {
            // Die Menge der vorhandenen allgemeinen Konzepte enth�lt alle
            // erzeugten allgemeinen Konzepte. Daher brauchen diese nur
            // aufgenommen und nicht neu erzeugt zu werden.
            konzIter = vorhandeneAlgKonz.iterator();
            while (konzIter.hasNext()) {
                allgemeinesKonzept = (Konzept) konzIter.next();
                neueKonzVerwalt.konzeptAufnehmen(allgemeinesKonzept,
                                                 posBspIndex, negBspIndex);
            }
        }

        // Neues spezielles Konzept erzeugen.
        erzeugtesKonzept = neueKonzVerwalt.erzeugtesKonzept(erzSpezScpIterAnz);

        // Erzeugtes Konzept gegebenenfalls optimieren.
        if ((erzeugtesKonzept != null) && (optSpezItmAnz != 0)) {
            // Es konnte ein spezielles Konzept erzeugt werden und es k�nnen
            // optimierte Konzepte erzeugt werden.
            konzOpt = new KonzeptOptimierung(rand, beispieldaten, parameter);
            if (neueKonzVerwalt.alleAlgKonzepteEnthalten()) {
                optPraedErzeuger = null;
            } else {
                optPraedErzeuger = praedikatErzeuger;
            }
            vorhandeneAlgKonz = neueKonzVerwalt.enthalteneAlgKonzepte();
            vorhandeneSpezKonz = neueKonzVerwalt.enthalteneSpezKonzepte();
            erzeugtesKonzept = konzOpt.optimiertesSpezKonzept(optPraedErzeuger,
                                                              vorhandeneAlgKonz,
                                                              vorhandeneSpezKonz,
                                                              erzeugtesKonzept,
                                                              kostenFaktor,
                                                              korrKonzErzeugung);
        }

        return erzeugtesKonzept;
    }

    /**
     * Erzeugt mittels der �bergebenen Pr�dikat-Erzeuger nacheinander zuerst
     * Pr�dikate zu korrekten Konzepten und dann zu allgemeinen Konzepten und
     * daraus korrekte Konzepte. Dies entspricht der �u�eren Phase der
     * Konzepterzeugung f�r korrekte Konzepte.
     *
     * @param praedikatErzeuger  Erzeuger von Pr�dikaten.
     * @param konzeptVerwaltung  Die Verwaltung der korrekten Konzepte.
     */
    private void erzPraedKorrKonzepte(ArrayList praedikatErzeuger,
                                      KorrKonzeptVerwaltung konzeptVerwaltung) {
        PraedikatErzeugung erzeuger;
        Iterator     praedIter;
        Praedikat    praedikat;
        KombiKonzept besteFormel, letzteFormel;
        float        kostenFaktor;
        int          aeussereNochAnz;
        int          aeussereIstAnz;
        int          vorhandeneKorrKonzAnz;
        boolean      verbesserung;
        boolean      verbesserMoeglich;
        boolean      aufnahme;

        LOGGER.log(Level.CONFIG, "Beginn der korrekten �u�eren Iterationen");
        besteFormel = null;
        letzteFormel = null;
        aeussereNochAnz = Math.abs(aeussereSollAnz);
        aeussereIstAnz = 0;
        verbesserung = true;
        verbesserMoeglich = true;
        while (verbesserung && ((aeussereSollAnz == 0) || (aeussereNochAnz > 0))) {

            aeussereIstAnz++;
            LOGGER.log(Level.CONFIG,
                       aeussereIstAnz + ". Iteration der korrekten Konzepte");
            aufnahme = false;

            // Korrekte Pr�dikate erzeugen und aufnehmen, wenn diese nicht
            // schon alle vorhanden sind.
            vorhandeneKorrKonzAnz = konzeptVerwaltung.enthalteneSpezKonzeptAnz();
            if ((spezFormItmAnz != 0)
                && ((vorhandeneKorrKonzAnz == 0)
                    || !konzeptVerwaltung.alleSpezKonzepteEnthalten())) {

                // Die Menge der vorhandenen korrekten Konzepte enth�lt
                // wahrscheinlich nicht alle erzeugbaren korrekten Konzepte.
                LOGGER.log(Level.CONFIG, "Erzeugung korrekter Pr�dikate");
                for (int praedErzNr = 0;
                     praedErzNr < praedikatErzeuger.size();
                     praedErzNr++) {
                    erzeuger = (PraedikatErzeugung) praedikatErzeuger.get(praedErzNr);

                    praedIter = erzeuger.posKorrPraedIter();
                    while (praedIter.hasNext()) {
                        praedikat = (Praedikat) praedIter.next();
                        aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, false);
                    }

                    praedIter = erzeuger.negVollPraedIter();
                    while (praedIter.hasNext()) {
                        praedikat = (Praedikat) praedIter.next();
                        aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, true);
                    }
                }
                LOGGER.log(Level.CONFIG, "Pr�dikat aufgenommen: " + aufnahme);
            } else {
                LOGGER.log(Level.CONFIG,
                           "Korrekte Pr�dikate brauchen nicht erzeugt zu werden");
            }
            verbesserung = aufnahme;

            // Durchf�hrung der mittleren und inneren Phase der
            // Konzepterzeugung.
            if (verbesserMoeglich || aufnahme) {
                verbesserMoeglich = erzPraedSpezKonzepte(praedikatErzeuger,
                                                         konzeptVerwaltung,
                                                         besteFormel, true);
                verbesserung |= verbesserMoeglich;
            }

            // Anzahl der durchzuf�hrenden Iterationen verringern.
            if (aeussereNochAnz > 0) {
                aeussereNochAnz--;
            }

            // Wenn keine feste Anzahl von Iterationen vorgegeben wurde,
            // pr�fen, ob tats�chlich eine Verbesserung stattgefunden hat.
            // Ansonsten zumindest ein Mal die beste Formel ermitteln.
            if ((verbesserung
                 && ((aeussereSollAnz == 0)
                     || (aeussereSollAnz > 0) && (aeussereNochAnz > 0)))
                || ((aeussereNochAnz == 0) && (letzteFormel == null))) {

                letzteFormel =  konzeptVerwaltung.besteFormel(erzFormScpIterAnz);
                if ((besteFormel == null)
                        || letzteFormel.istBesser(besteFormel)) {
                    verbesserung = true;
                    besteFormel = letzteFormel;
                } else {
                    verbesserung = false;
                }
                LOGGER.log(Level.CONFIG,
                           "Tats�chliche Verbesserung: " + verbesserung);

                if (besteFormel.istBesser(besteKorrFormelGesamt)) {
                    besteKorrFormelGesamt = besteFormel;
                }
            }

            if (!verbesserung) {
                // Es ist keine Verbesserung erfolgt. Daher versuchen, wenn
                // noch Fehler vorhanden sind, den Kostenfaktor zu erh�hen.
                if (besteKorrFormelGesamt.posFalschAnz() > 0) {
                    kostenFaktor = konzeptVerwaltung.gibKostenFaktor();
                    if (kostenFaktor < Konstanten.KOSTEN_FAKTOR_GRENZE) {
                        kostenFaktor *= Konstanten.KOSTEN_FAKTOR_FAKTOR;
                        konzeptVerwaltung.setzeKostenFaktor(kostenFaktor);
                        LOGGER.log(Level.CONFIG,
                                   "Erh�hung vom Kostenfaktor auf: "
                                   + kostenFaktor);
                        verbesserung = true;
                        verbesserMoeglich = true;
                    } else {
                        LOGGER.log(Level.CONFIG,
                                   "Keine Erh�hung vom Kostenfaktor mehr"
                                   + " m�glich.");
                    }
                }
            }
        }
        LOGGER.log(Level.CONFIG, "Ende der korrekten �u�eren Iterationen");
    }

    /**
     * Erzeugt mittels der �bergebenen Pr�dikat-Erzeuger sequentiell zu jedem
     * noch nicht �berdeckten positiven Index alle Pr�dikate zu allgemeinen
     * Konzepten, die den jeweiligen Index enthalten, und daraus korrekte
     * Konzepte.
     *
     * @param praedikatErzeuger  Erzeuger von Pr�dikaten.
     * @param konzeptVerwaltung  Die Verwaltung der korrekten Konzepte.
     * @param formelKomplex      Die gesch�tzte Komplexit�t einer korrekten
     *                           Formel.
     */
    private void seqErzPraedKorrKonzepte(ArrayList praedikatErzeuger,
                                         KorrKonzeptVerwaltung konzeptVerwaltung,
                                         float formelKomplex) {
        KombiKonzept  erzeugtesKonzept;
        KombiKonzept  neueFormel;
        BitMathIntSet posFehler;

        // Pr�fen, ob noch Fehler vorhanden sind.
        if (besteKorrFormelGesamt.posFalschAnz() == 0) {
            return;
        }

        // Pr�fen, ob �berhaupt ein korrektes Konzept erzeugt werden kann.
        erzeugtesKonzept = praedSpezKonzept(praedikatErzeuger, konzeptVerwaltung,
                                            formelKomplex, -1, -1, true);
        if (erzeugtesKonzept == null) {
            return;
        }

        LOGGER.log(Level.CONFIG,
                   "Beginn der Erzeugung korrekter Konzepte zu einzelnen"
                   + " Indices");

        posFehler = besteKorrFormelGesamt.posFalschBsp();
        for (int fehlIndex = posFehler.getMinimum();
             fehlIndex >= 0;
             fehlIndex = posFehler.getNext(fehlIndex + 1)) {

            // Pr�fen, ob der Fehler-Index noch ein Fehler ist.
            if (besteKorrFormelGesamt.posFalschBsp().contains(fehlIndex)) {

                // Neues korrektes Konzept erzeugen und aufnehmen.
                erzeugtesKonzept = praedSpezKonzept(praedikatErzeuger,
                                                    konzeptVerwaltung,
                                                    formelKomplex,
                                                    fehlIndex, -1, true);
                if (erzeugtesKonzept != null) {
                    konzeptVerwaltung.konzeptAufnehmen(erzeugtesKonzept);
                    neueFormel = konzeptVerwaltung.besteFormel(erzFormScpIterAnz);
                    if (neueFormel.istBesser(besteKorrFormelGesamt)) {
                        besteKorrFormelGesamt = neueFormel;
                    }
                }
            }
        }

        LOGGER.log(Level.CONFIG,
                   "Ende der Erzeugung korrekter Konzepte zu einzelnen"
                   + " Indices");
    }

    /**
     * Erzeugt mittels der �bergebenen Pr�dikat-Erzeuger nacheinander
     * Pr�dikate der verschiedenen Arten und daraus korrekte Konzepte.
     *
     * @param praedikatErzeuger  Erzeuger von Pr�dikaten.
     */
    public void erzeugeKorrKonzepte(ArrayList praedikatErzeuger) {
        KorrKonzeptVerwaltung konzeptVerwaltung;
        KonzeptOptimierung    konzOpt;
        ArrayList             optPraedErzeuger;
        HashSet               teilkonzeptAuswahl;
        HashSet               vorhandeneKonzepte;
        KombiKonzept          besteFormel;
        float                 kostenFaktor;
        float                 formelKomplex;

        konzOpt = new KonzeptOptimierung(rand, beispieldaten, parameter);

        kostenFaktor = Konstanten.KOSTEN_FAKTOR_INIT;
        formelKomplex = initGesamtKomplex;

        for (int gesInterNr = 0; gesInterNr < gesamtIterAnz; gesInterNr++) {
            LOGGER.log(Level.CONFIG,
                       "Gesamtdurchlauf der korrekten Konzepte: " + gesInterNr);

            // Einige Teilkonzepte des bisher besten Konzepts aufnehmen.
            teilkonzeptAuswahl = auswahl(besteKorrFormelGesamt.teilkonzepte());
            konzeptVerwaltung = new KorrKonzeptVerwaltung(rand, beispieldaten,
                                                          kostenFaktor,
                                                          formelKomplex,
                                                          algSpezItmAnz,
                                                          spezFormItmAnz,
                                                          maxLiteralAnz,
                                                          speicherEffizienz,
                                                          negBoolPraedErz);
            konzeptVerwaltung.konzepteAufnehmen(teilkonzeptAuswahl);

            // Ausgabe der partiellen Ausgangsformel ab dem zweiten
            // Gesamtdurchlauf.
            if (LOGGER.isLoggable(Level.CONFIG)) {
                if (gesInterNr > 0) {
                    konzeptVerwaltung.besteFormel(1);
                }
            }

            // Ausf�hrung der �u�eren Iteration.
            erzPraedKorrKonzepte(praedikatErzeuger, konzeptVerwaltung);
            kostenFaktor = konzeptVerwaltung.gibKostenFaktor();

            // M�glicherweise noch vorhandene Fehler versuchen zu beseitigen.
            seqErzPraedKorrKonzepte(praedikatErzeuger, konzeptVerwaltung,
                                    formelKomplex);

            // Finale Erzeugung der besten Formel.
            LOGGER.log(Level.CONFIG,
                       "Erzeugung einer finalen korrekten Formel");
            besteFormel = konzeptVerwaltung.besteFormel(finFormScpIterAnz);
            if (besteFormel.istBesser(besteKorrFormelGesamt)) {
                besteKorrFormelGesamt = besteFormel;
            }

            // Optimierung der insgesamt besten Formel.
            if (konzeptVerwaltung.alleSpezKonzepteEnthalten()) {
                optPraedErzeuger = null;
            } else {
                optPraedErzeuger = praedikatErzeuger;
            }
            vorhandeneKonzepte = konzeptVerwaltung.enthalteneSpezKonzepte();
            besteKorrFormelGesamt = konzOpt.optimiertesFinKonzept(optPraedErzeuger,
                                                                  vorhandeneKonzepte,
                                                                  besteKorrFormelGesamt,
                                                                  kostenFaktor,
                                                                  formelKomplex,
                                                                  true);
            formelKomplex = besteKorrFormelGesamt.komplexitaet();
        }
    }

    /**
     * Erzeugt mittels der �bergebenen Pr�dikat-Erzeuger nacheinander zuerst
     * Pr�dikate zu vollst�ndigen Konzepten und dann zu allgemeinen Konzepten
     * und daraus vollst�ndige Konzepte. Dies entspricht der �u�eren Phase der
     * Konzepterzeugung f�r vollst�ndige Konzepte.
     *
     * @param praedikatErzeuger  Erzeuger von Pr�dikaten.
     * @param konzeptVerwaltung  Die Verwaltung der vollst�ndigen Konzepte.
     */
    private void erzPraedVollKonzepte(ArrayList praedikatErzeuger,
                                      VollKonzeptVerwaltung konzeptVerwaltung) {
        PraedikatErzeugung erzeuger;
        Iterator     praedIter;
        Praedikat    praedikat;
        KombiKonzept besteFormel, letzteFormel;
        float        kostenFaktor;
        int          aeussereNochAnz;
        int          aeussereIstAnz;
        int          vorhandeneVollKonzAnz;
        boolean      verbesserung;
        boolean      verbesserMoeglich;
        boolean      aufnahme;

        LOGGER.log(Level.CONFIG, "Beginn der vollst�ndigen �u�eren Iterationen");
        besteFormel = null;
        letzteFormel = null;
        aeussereNochAnz = Math.abs(aeussereSollAnz);
        aeussereIstAnz = 0;
        verbesserung = true;
        verbesserMoeglich = true;
        while (verbesserung && ((aeussereSollAnz == 0) || (aeussereNochAnz > 0))) {

            aeussereIstAnz++;
            LOGGER.log(Level.CONFIG,
                       aeussereIstAnz + ". Iteration der vollst�ndigen Konzepte");
            aufnahme = false;

            // Vollst�ndige Pr�dikate erzeugen und aufnehmen, wenn diese nicht
            // schon alle vorhanden sind.
            vorhandeneVollKonzAnz = konzeptVerwaltung.enthalteneSpezKonzeptAnz();
            if ((spezFormItmAnz != 0)
                && ((vorhandeneVollKonzAnz == 0)
                    || !konzeptVerwaltung.alleSpezKonzepteEnthalten())) {

                // Die Menge der vorhandenen vollst�ndigen Konzepte enth�lt
                // wahrscheinlich nicht alle erzeugbaren vollst�ndigen
                // Konzepte.
                LOGGER.log(Level.CONFIG, "Erzeugung vollst�ndiger Pr�dikate");
                for (int praedErzNr = 0;
                     praedErzNr < praedikatErzeuger.size();
                     praedErzNr++) {
                    erzeuger = (PraedikatErzeugung) praedikatErzeuger.get(praedErzNr);

                    praedIter = erzeuger.posVollPraedIter();
                    while (praedIter.hasNext()) {
                        praedikat = (Praedikat) praedIter.next();
                        aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, false);
                    }

                    praedIter = erzeuger.negKorrPraedIter();
                    while (praedIter.hasNext()) {
                        praedikat = (Praedikat) praedIter.next();
                        aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, true);
                    }
                }
                LOGGER.log(Level.CONFIG, "Pr�dikat aufgenommen: " + aufnahme);
            } else {
                LOGGER.log(Level.CONFIG,
                           "Vollst�ndige Pr�dikate brauchen nicht erzeugt zu werden");
            }
            verbesserung = aufnahme;

            // Durchf�hrung der mittleren und inneren Phase der
            // Konzepterzeugung.
            if (verbesserMoeglich || aufnahme) {
                verbesserMoeglich = erzPraedSpezKonzepte(praedikatErzeuger,
                                                         konzeptVerwaltung,
                                                         besteFormel, false);
                verbesserung |= verbesserMoeglich;
            }

            // Anzahl der durchzuf�hrenden Iterationen verringern.
            if (aeussereNochAnz > 0) {
                aeussereNochAnz--;
            }

            // Wenn keine feste Anzahl von Iterationen vorgegeben wurde,
            // pr�fen, ob tats�chlich eine Verbesserung stattgefunden hat.
            // Ansonsten zumindest ein Mal die beste Formel ermitteln.
            if ((verbesserung
                 && ((aeussereSollAnz == 0)
                     || (aeussereSollAnz > 0) && (aeussereNochAnz > 0)))
                || ((aeussereNochAnz == 0) && (letzteFormel == null))) {

                letzteFormel =  konzeptVerwaltung.besteFormel(erzFormScpIterAnz);
                if ((besteFormel == null)
                        || letzteFormel.istBesser(besteFormel)) {
                    verbesserung = true;
                    besteFormel = letzteFormel;
                } else {
                    verbesserung = false;
                }
                LOGGER.log(Level.CONFIG,
                           "Tats�chliche Verbesserung: " + verbesserung);

                if (besteFormel.istBesser(besteVollFormelGesamt)) {
                    besteVollFormelGesamt = besteFormel;
                }
            }

            if (!verbesserung) {
                // Es ist keine Verbesserung erfolgt. Daher versuchen, wenn
                // noch Fehler vorhanden sind, den Kostenfaktor zu erh�hen.
                if (besteVollFormelGesamt.negFalschAnz() > 0) {
                    kostenFaktor = konzeptVerwaltung.gibKostenFaktor();
                    if (kostenFaktor < Konstanten.KOSTEN_FAKTOR_GRENZE) {
                        kostenFaktor *= Konstanten.KOSTEN_FAKTOR_FAKTOR;
                        konzeptVerwaltung.setzeKostenFaktor(kostenFaktor);
                        LOGGER.log(Level.CONFIG,
                                   "Erh�hung vom Kostenfaktor auf: "
                                   + kostenFaktor);
                        verbesserung = true;
                        verbesserMoeglich = true;
                    } else {
                        LOGGER.log(Level.CONFIG,
                                   "Keine Erh�hung vom Kostenfaktor mehr"
                                   + " m�glich.");
                    }
                }
            }
        }
        LOGGER.log(Level.CONFIG, "Ende der vollst�ndigen �u�eren Iterationen");
    }

    /**
     * Erzeugt mittels der �bergebenen Pr�dikat-Erzeuger sequentiell zu jedem
     * noch nicht ausgeschlossenen negativen Index alle Pr�dikate zu
     * allgemeinen Konzepten, die den jeweiligen Index nicht enthalten, und
     * daraus vollst�ndige Konzepte.
     *
     * @param praedikatErzeuger  Erzeuger von Pr�dikaten.
     * @param konzeptVerwaltung  Die Verwaltung der vollst�ndigen Konzepte.
     * @param formelKomplex      Die gesch�tzte Komplexit�t einer
     *                           vollst�ndigen Formel.
     */
    private void seqErzPraedVollKonzepte(ArrayList praedikatErzeuger,
                                         VollKonzeptVerwaltung konzeptVerwaltung,
                                         float formelKomplex) {
        KombiKonzept  erzeugtesKonzept;
        KombiKonzept  neueFormel;
        BitMathIntSet negFehler;

        // Pr�fen, ob noch Fehler vorhanden sind.
        if (besteVollFormelGesamt.negFalschAnz() == 0) {
            return;
        }

        // Pr�fen, ob �berhaupt ein vollst�ndiges Konzept erzeugt werden kann.
        erzeugtesKonzept = praedSpezKonzept(praedikatErzeuger, konzeptVerwaltung,
                                            formelKomplex, -1, -1, false);
        if (erzeugtesKonzept == null) {
            return;
        }

        LOGGER.log(Level.CONFIG,
                   "Beginn der Erzeugung vollst�ndiger Konzepte zu"
                   + " einzelnen Indices");

        negFehler = besteVollFormelGesamt.negFalschBsp();
        for (int fehlIndex = negFehler.getMinimum();
             fehlIndex >= 0;
             fehlIndex = negFehler.getNext(fehlIndex + 1)) {

            // Pr�fen, ob der Fehler-Index noch ein Fehler ist.
            if (besteVollFormelGesamt.negFalschBsp().contains(fehlIndex)) {

                // Neues vollst�ndiges Konzept erzeugen und aufnehmen.
                erzeugtesKonzept = praedSpezKonzept(praedikatErzeuger,
                                                    konzeptVerwaltung,
                                                    formelKomplex,
                                                    -1, fehlIndex, false);
                if (erzeugtesKonzept != null) {
                    konzeptVerwaltung.konzeptAufnehmen(erzeugtesKonzept);
                    neueFormel = konzeptVerwaltung.besteFormel(erzFormScpIterAnz);
                    if (neueFormel.istBesser(besteVollFormelGesamt)) {
                        besteVollFormelGesamt = neueFormel;
                    }
                }
            }
        }

        LOGGER.log(Level.CONFIG,
                   "Ende der Erzeugung vollst�ndiger Konzepte zu"
                   + " einzelnen Indices");
    }

    /**
     * Erzeugt mittels der �bergebenen Pr�dikat-Erzeuger nacheinander
     * Pr�dikate der verschiedenen Arten und daraus vollst�ndige Konzepte.
     *
     * @param praedikatErzeuger  Erzeuger von Pr�dikaten.
     */
    public void erzeugeVollKonzepte(ArrayList praedikatErzeuger) {
        VollKonzeptVerwaltung konzeptVerwaltung;
        KonzeptOptimierung    konzOpt;
        ArrayList             optPraedErzeuger;
        HashSet               teilkonzeptAuswahl;
        HashSet               vorhandeneKonzepte;
        KombiKonzept          besteFormel;
        float                 kostenFaktor;
        float                 formelKomplex;

        konzOpt = new KonzeptOptimierung(rand, beispieldaten, parameter);

        kostenFaktor = Konstanten.KOSTEN_FAKTOR_INIT;
        formelKomplex = initGesamtKomplex;

        for (int gesInterNr = 0; gesInterNr < gesamtIterAnz; gesInterNr++) {
            LOGGER.log(Level.CONFIG,
                       "Gesamtdurchlauf der vollst�ndigen Konzepte: " + gesInterNr);

            // Einige Teilkonzepte des bisher besten Konzepts aufnehmen.
            teilkonzeptAuswahl = auswahl(besteVollFormelGesamt.teilkonzepte());
            konzeptVerwaltung = new VollKonzeptVerwaltung(rand, beispieldaten,
                                                          kostenFaktor,
                                                          formelKomplex,
                                                          algSpezItmAnz,
                                                          spezFormItmAnz,
                                                          maxLiteralAnz,
                                                          speicherEffizienz,
                                                          negBoolPraedErz);
            konzeptVerwaltung.konzepteAufnehmen(teilkonzeptAuswahl);

            // Ausgabe der partiellen Ausgangsformel ab dem zweiten
            // Gesamtdurchlauf.
            if (LOGGER.isLoggable(Level.CONFIG)) {
                if (gesInterNr > 0) {
                    konzeptVerwaltung.besteFormel(1);
                }
            }

            // Ausf�hrung der �u�eren Iteration.
            erzPraedVollKonzepte(praedikatErzeuger, konzeptVerwaltung);
            kostenFaktor = konzeptVerwaltung.gibKostenFaktor();

            // M�glicherweise noch vorhandene Fehler versuchen zu beseitigen.
            seqErzPraedVollKonzepte(praedikatErzeuger, konzeptVerwaltung,
                                    formelKomplex);

            // Finale Erzeugung der besten Formel.
            LOGGER.log(Level.CONFIG,
                       "Erzeugung einer finalen vollst�ndigen Formel");
            besteFormel = konzeptVerwaltung.besteFormel(finFormScpIterAnz);
            if (besteFormel.istBesser(besteVollFormelGesamt)) {
                besteVollFormelGesamt = besteFormel;
            }

            // Optimierung der insgesamt besten Formel.
            if (konzeptVerwaltung.alleSpezKonzepteEnthalten()) {
                optPraedErzeuger = null;
            } else {
                optPraedErzeuger = praedikatErzeuger;
            }
            vorhandeneKonzepte = konzeptVerwaltung.enthalteneSpezKonzepte();
            besteVollFormelGesamt = konzOpt.optimiertesFinKonzept(optPraedErzeuger,
                                                                  vorhandeneKonzepte,
                                                                  besteVollFormelGesamt,
                                                                  kostenFaktor,
                                                                  formelKomplex,
                                                                  false);
            formelKomplex = besteVollFormelGesamt.komplexitaet();
        }
    }

    /**
     * Erzeugt mittels der �bergebenen Pr�dikat-Erzeuger nacheinander
     * Pr�dikate der verschiedenen Arten und daraus korrekte und vollst�ndige
     * Konzepte.
     *
     * @param praedikatErzeuger  Erzeuger von Pr�dikaten.
     */
    public void erzeugeKonzepte(ArrayList praedikatErzeuger) {
        erzeugeKorrKonzepte(praedikatErzeuger);
        erzeugeVollKonzepte(praedikatErzeuger);
    }

    /**
     * Erzeugt mittels des �bergebenen Pr�dikat-Erzeugers nacheinander
     * Pr�dikate der verschiedenen Arten und daraus korrekte Konzepte.
     *
     * @param praedikatErzeuger  Ein einzelner Erzeuger von Pr�dikaten.
     */
    public void erzeugeKorrKonzepte(PraedikatErzeugung praedikatErzeuger) {
        ArrayList praedErzeuger = new ArrayList(1);
        praedErzeuger.add(praedikatErzeuger);
        erzeugeKorrKonzepte(praedErzeuger);
    }

    /**
     * Erzeugt mittels des �bergebenen Pr�dikat-Erzeugers nacheinander
     * Pr�dikate der verschiedenen Arten und daraus vollst�ndige Konzepte.
     *
     * @param praedikatErzeuger  Ein einzelner Erzeuger von Pr�dikaten.
     */
    public void erzeugeVollKonzepte(PraedikatErzeugung praedikatErzeuger) {
        ArrayList praedErzeuger = new ArrayList(1);
        praedErzeuger.add(praedikatErzeuger);
        erzeugeVollKonzepte(praedErzeuger);
    }

    /**
     * Erzeugt mittels des �bergebenen Pr�dikat-Erzeugers nacheinander
     * Pr�dikate der verschiedenen Arten und daraus korrekte und vollst�ndige
     * Konzepte.
     *
     * @param praedikatErzeuger  Ein einzelner Erzeuger von Pr�dikaten.
     */
    public void erzeugeKonzepte(PraedikatErzeugung praedikatErzeuger) {
        ArrayList praedErzeuger = new ArrayList(1);
        praedErzeuger.add(praedikatErzeuger);
        erzeugeKonzepte(praedErzeuger);
    }

    /**
     * Liefert die beste korrekte Formel. Das ist die, die prim�r die
     * geringste Anzahl positiver Fehler und sekund�r eine m�glichst geringe
     * Komplexit�t hat.
     *
     * @return  Die beste korrekte Formel.
     */
    public Konzept besteKorrFormel() {
        return besteKorrFormelGesamt;
    }

    /**
     * Liefert die beste vollst�ndige Formel. Das ist die, die prim�r die
     * geringste Anzahl negativer Fehler und sekund�r eine m�glichst geringe
     * Komplexit�t hat.
     *
     * @return  Die beste vollst�ndige Formel.
     */
    public Konzept besteVollFormel() {
        return besteVollFormelGesamt;
    }

    /**
     * Liefert die insgesamt beste Formel. Das ist die, die prim�r die
     * geringste Anzahl positiver oder negativer Fehler und sekund�r eine
     * m�glichst geringe Komplexit�t hat.
     *
     * @return  Die insgesamt beste Formel.
     */
    public Konzept besteFormel() {
        if (besteKorrFormelGesamt.istBesser(besteVollFormelGesamt)) {
            return besteKorrFormelGesamt;
        } else {
            return besteVollFormelGesamt;
        }
    }

    /**
     * Liefert das effizienteste korrekte Konzept, das f�r das positive
     * Beispiel mit dem �bergebenen Index zutrifft.
     *
     * @param praedikatErzeuger  Erzeuger von Pr�dikaten.
     * @param posBspIndex        Der Index eines positiven Beispiels, f�r das
     *                           das zu erzeugende Konzept zutreffen soll. Ein
     *                           negativer Wert wird ignoriert.
     *
     * @return  Das effizienteste korrekte Konzept, das f�r das positive
     *          Beispiel mit dem �bergebenen Index zutrifft, oder
     *          <CODE>null</CODE>, wenn es ein solches Konzept nicht gibt.
     */
    public Konzept effizientKorrKonzept(ArrayList praedikatErzeuger,
                                        int posBspIndex) {
        KorrKonzeptVerwaltung konzeptVerwaltung;
        PraedikatErzeugung    erzeuger;
        Iterator              praedIter;
        Praedikat             praedikat;
        KombiKonzept          erzeugtesKonzept;
        float                 kostenFaktor;
        boolean               aufnahme;

        kostenFaktor = Konstanten.KOSTEN_FAKTOR_INIT;
        konzeptVerwaltung = new KorrKonzeptVerwaltung(rand, beispieldaten,
                                                      kostenFaktor,
                                                      initGesamtKomplex,
                                                      algSpezItmAnz,
                                                      spezFormItmAnz,
                                                      maxLiteralAnz,
                                                      speicherEffizienz,
                                                      negBoolPraedErz);

        LOGGER.log(Level.CONFIG, "Erzeugung allgemeiner Pr�dikate");
        do {
            // Neues korrektes Konzept erzeugen.
            erzeugtesKonzept = praedSpezKonzept(praedikatErzeuger,
                                                konzeptVerwaltung,
                                                initGesamtKomplex,
                                                posBspIndex, -1, true);

            // Wenn ein Konzept erzeugt wurde, dieses aufnehmen.
            if (erzeugtesKonzept != null) {
                LOGGER.log(Level.CONFIG, "Neues korr. Konzept erzeugt.");
                konzeptVerwaltung.konzeptAufnehmen(erzeugtesKonzept);
            }

            if ((erzeugtesKonzept != null)
                && (erzeugtesKonzept.posRichtigAnz() == 0)) {

                // Das Konzpt trifft f�r kein positives Beispiel zu.
                kostenFaktor = konzeptVerwaltung.gibKostenFaktor();
                kostenFaktor *= Konstanten.KOSTEN_FAKTOR_FAKTOR;
                if (kostenFaktor <= Konstanten.KOSTEN_FAKTOR_GRENZE) {
                    konzeptVerwaltung.setzeKostenFaktor(kostenFaktor);
                    LOGGER.log(Level.CONFIG,
                               "Erh�hung vom Kostenfaktor auf: "
                               + kostenFaktor);
                } else {
                    LOGGER.log(Level.CONFIG,
                               "Keine Erh�hung vom Kostenfaktor mehr"
                               + " m�glich.");
                }
            }
        } while ((erzeugtesKonzept != null)
                 && (erzeugtesKonzept.posRichtigAnz() == 0)
                 && (kostenFaktor <= Konstanten.KOSTEN_FAKTOR_GRENZE));

        LOGGER.log(Level.CONFIG, "Erzeugung korrekter Pr�dikate");
        aufnahme = false;
        for (int praedErzNr = 0;
             praedErzNr < praedikatErzeuger.size();
             praedErzNr++) {
            erzeuger = (PraedikatErzeugung) praedikatErzeuger.get(praedErzNr);

            praedIter = erzeuger.posKorrPraedIter();
            while (praedIter.hasNext()) {
                praedikat = (Praedikat) praedIter.next();
                aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, false,
                                                                 posBspIndex, -1);
            }

            praedIter = erzeuger.negVollPraedIter();
            while (praedIter.hasNext()) {
                praedikat = (Praedikat) praedIter.next();
                aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, true,
                                                                 posBspIndex, -1);
            }
        }
        LOGGER.log(Level.CONFIG, "Korr. Pr�dikat aufgenommen: " + aufnahme);

        return effizientesteKonzept(konzeptVerwaltung.enthalteneSpezKonzepte());
    }

    /**
     * Liefert das effizienteste vollst�ndige Konzept, das f�r das negative
     * Beispiel mit dem �bergebenen Index nicht zutrifft.
     *
     * @param praedikatErzeuger  Erzeuger von Pr�dikaten.
     * @param negBspIndex        Der Index eines negativen Beispiels, f�r das
     *                           das zu erzeugende Konzept nicht zutreffen
     *                           soll. Ein negativer Wert wird ignoriert.
     *
     * @return  Das effizienteste korrekte Konzept, das f�r das negative
     *          Beispiel mit dem �bergebenen Index nicht zutrifft, oder
     *          <CODE>null</CODE>, wenn es ein solches Konzept nicht gibt.
     */
    public Konzept effizientVollKonzept(ArrayList praedikatErzeuger,
                                        int negBspIndex) {
        VollKonzeptVerwaltung konzeptVerwaltung;
        PraedikatErzeugung    erzeuger;
        Iterator              praedIter;
        Praedikat             praedikat;
        KombiKonzept          erzeugtesKonzept;
        float                 kostenFaktor;
        boolean               aufnahme;

        kostenFaktor = Konstanten.KOSTEN_FAKTOR_INIT;
        konzeptVerwaltung = new VollKonzeptVerwaltung(rand, beispieldaten,
                                                      kostenFaktor,
                                                      initGesamtKomplex,
                                                      algSpezItmAnz,
                                                      spezFormItmAnz,
                                                      maxLiteralAnz,
                                                      speicherEffizienz,
                                                      negBoolPraedErz);

        LOGGER.log(Level.CONFIG, "Erzeugung allgemeiner Pr�dikate");
        do {
            // Neues vollst�ndiges Konzept erzeugen.
            erzeugtesKonzept = praedSpezKonzept(praedikatErzeuger,
                                                konzeptVerwaltung,
                                                initGesamtKomplex,
                                                -1, negBspIndex, false);

            // Wenn ein Konzept erzeugt wurde, dieses aufnehmen.
            if (erzeugtesKonzept != null) {
                LOGGER.log(Level.CONFIG, "Neues voll. Konzept erzeugt.");
                konzeptVerwaltung.konzeptAufnehmen(erzeugtesKonzept);
            }

            if ((erzeugtesKonzept != null)
                && (erzeugtesKonzept.negRichtigAnz() == 0)) {

                // Das Konzpt trifft f�r alle negativen Beispiel zu.
                kostenFaktor = konzeptVerwaltung.gibKostenFaktor();
                kostenFaktor *= Konstanten.KOSTEN_FAKTOR_FAKTOR;
                if (kostenFaktor <= Konstanten.KOSTEN_FAKTOR_GRENZE) {
                    konzeptVerwaltung.setzeKostenFaktor(kostenFaktor);
                    LOGGER.log(Level.CONFIG,
                               "Erh�hung vom Kostenfaktor auf: "
                               + kostenFaktor);
                } else {
                    LOGGER.log(Level.CONFIG,
                               "Keine Erh�hung vom Kostenfaktor mehr"
                               + " m�glich.");
                }
            }
        } while ((erzeugtesKonzept != null)
                 && (erzeugtesKonzept.negRichtigAnz() == 0)
                 && (kostenFaktor <= Konstanten.KOSTEN_FAKTOR_GRENZE));

        LOGGER.log(Level.CONFIG, "Erzeugung vollst�ndiger Pr�dikate");
        aufnahme = false;
        for (int praedErzNr = 0;
             praedErzNr < praedikatErzeuger.size();
             praedErzNr++) {
            erzeuger = (PraedikatErzeugung) praedikatErzeuger.get(praedErzNr);

            praedIter = erzeuger.posVollPraedIter();
            while (praedIter.hasNext()) {
                praedikat = (Praedikat) praedIter.next();
                aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, false,
                                                                 -1, negBspIndex);
            }

            praedIter = erzeuger.negKorrPraedIter();
            while (praedIter.hasNext()) {
                praedikat = (Praedikat) praedIter.next();
                aufnahme |= konzeptVerwaltung.praedikatAufnehmen(praedikat, true,
                                                                 -1, negBspIndex);
            }
        }
        LOGGER.log(Level.CONFIG, "Voll. Pr�dikat aufgenommen: " + aufnahme);

        return effizientesteKonzept(konzeptVerwaltung.enthalteneSpezKonzepte());
    }

    /**
     * Liefert das insgesamt effizienteste Konzept, das f�r das positive
     * Beispiel mit dem �bergebenen Index zutrifft und das negative Beispiel
     * mit dem �bergebenen Index nicht zutrifft.
     *
     * @param praedikatErzeuger  Erzeuger von Pr�dikaten.
     * @param posBspIndex        Der Index eines positiven Beispiels, f�r das
     *                           das zu erzeugende Konzept zutreffen soll. Ein
     *                           negativer Wert wird ignoriert.
     * @param negBspIndex        Der Index eines negativen Beispiels, f�r das
     *                           das zu erzeugende Konzept nicht zutreffen
     *                           soll. Ein negativer Wert wird ignoriert.
     *
     * @return  Das insgesamt effizienteste Konzept, das f�r das positive
     *          Beispiel mit dem �bergebenen Index zutrifft und f�r das
     *          negative Beispiel mit dem �bergebenen Index nicht zutrifft,
     *          oder <CODE>null</CODE>, wenn es ein solches Konzept nicht
     *          gibt.
     */
    public Konzept effizientKonzept(ArrayList praedikatErzeuger,
                                    int posBspIndex, int negBspIndex) {
        Konzept korrKonzept, vollKonzept;
        float   korrEffizienz, vollEffizienz;

        korrKonzept = effizientKorrKonzept(praedikatErzeuger, posBspIndex);
        vollKonzept = effizientVollKonzept(praedikatErzeuger, negBspIndex);
        korrEffizienz = konzeptEffizienz(korrKonzept);
        vollEffizienz = konzeptEffizienz(vollKonzept);

        if (korrEffizienz >= vollEffizienz) {
            return korrKonzept;
        } else {
            return vollKonzept;
        }
    }
}

