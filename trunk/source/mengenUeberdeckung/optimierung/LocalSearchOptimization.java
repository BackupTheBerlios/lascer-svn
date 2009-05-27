/*
 * Dateiname      : LocalSearchOptimization.java
 * Letzte Änderung: 19. März 2007
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


package mengenUeberdeckung.optimierung;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.heuristiken.creation.CandidatesCreation;
import mengenUeberdeckung.heuristiken.selection.CandidateSelection;

/**
 * Optimiert eine Lösung zu einem unicost-Überdeckungsproblem durch eine
 * lokale Suche mittels Hinzufügen und Entfernen von Teilmengen.
 * <P>
 * Es handelt sich um Algorithmus aus folgendem Paper: <CITE>Nysret Musliu.
 * Local Search Algorithm for unicost Set Covering Problem. 2006.</CITE>.
 *
 * @author  Dietmar Lippold
 */
public class LocalSearchOptimization implements Optimization {

    /**
     * Ein Zufallsgenerator.
     */
    private Random rand;

    /**
     * Der Faktor, mit dem der Reziprok-Wert der Dichte multipliziert wird,
     * um die Anzahl der Änderungen der Familie zu jeder zu testenden Länge
     * der Taboo-Liste zu erhalten.
     */
    private int tabooAenderFaktor;

    /**
     * Gibt an, ob eine Speicher-effiziente und damit aber nicht
     * Laufzeit-effiziente Ausführung erfolgen soll.
     */
    private boolean speicherEffizient;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param rand               Ein Zufallsgenerator.
     * @param speicherEffizient  Gibt an, ob eine Speicher-effiziente und
     *                           damit aber nicht Laufzeit-effiziente
     *                           Ausführung erfolgen soll.
     * @param tabooAenderFaktor  Der Faktor, mit dem der Reziprok-Wert der
     *                           Dichte multipliziert wird, um die Anzahl der
     *                           Änderungen der Familie zu jeder zu testenden
     *                           Länge der Taboo-Liste zu erhalten.
     */
    public LocalSearchOptimization(Random rand, boolean speicherEffizient,
                                   int tabooAenderFaktor) {

        this.rand = rand;
        this.tabooAenderFaktor = tabooAenderFaktor;
        this.speicherEffizient = speicherEffizient;
    }

    /**
     * Erzeugt eine neue Instanz. Für den Faktor zur Ermittlung der Anzahl der
     * Änderungen einer zu optimierenden Familie wird ein Standard-Wert
     * verwendet.
     *
     * @param rand               Ein Zufallsgenerator.
     * @param speicherEffizient  Gibt an, ob eine Speicher-effiziente und
     *                           damit aber nicht Laufzeit-effiziente
     *                           Ausführung erfolgen soll.
     */
    public LocalSearchOptimization(Random rand, boolean speicherEffizient) {
        this(rand, speicherEffizient, Konstanten.TABOO_AENDER_FAKTOR);
    }

    /**
     * Liefert eine optimierte Lösung, also eine Lösung mit geringeren
     * Kosten als die übergebene aktuelle Lösung. Wenn eine solche nicht
     * ermittelt werden kann, wird die übergebene Familie geliefert.
     *
     * @param problem             Das zu Grunde liegende SCP-Problem.
     * @param actualCover         Die aktuelle, zu optimierende Lösung. Diese
     *                            darf nur notwendige Teilmengen enthalten.
     * @param bestKnownSolution   Die bisher beste Lösung des Gesamtproblems.
     *                            Der Wert kann <CODE>null</CODE> sein, wenn
     *                            z.B. noch keine Lösung bekannt ist.
     * @param candidatesCreation  Das aktuelle Verfahren zur Erzeugung der
     *                            Kandidaten zur Aufnahme in und Entfernung
     *                            aus einer Familie.
     * @param candidateSelection  Das aktuelle Verfahren zur Auswahl der
     *                            Kandidaten zur Aufnahme in und zur
     *                            Entfernung aus einer Familie.
     *
     * @return  Eine Familie mit geringeren Kosten oder, wenn eine solche
     *          nicht ermittelt werden konnte, <CODE>actualCover</CODE>. Die
     *          gelieferte Familie enthält nur notwendige Teilmengen.
     */
    public ItmFamilie optimize(ItmFamilie problem, ItmFamilie actualCover,
                               ItmFamilie bestKnownSolution,
                               CandidatesCreation candidatesCreation,
                               CandidateSelection candidateSelection) {
        ItmFamilie     optimizedCover, nextCover;
        ArrayList[]    enthaltendeItm;
        ArrayList      hinzuKandidaten, entfernKandidaten;
        HashSet        relevanteItm;
        TabooMenge     tabooMenge;
        IndexTeilmenge zuletztEntfernt;
        IndexTeilmenge naechsteItm, ausgewaehlt;
        Iterator       itmIter;
        int            laengenAnz, aenderAnz;
        int            bestTeilmengenAnz;
        int            groesseGesamtmenge, mittelAnzItm;
        int            groesseFamilie, tabooLaenge;
        int            besteBewertung, naechsteBewert;
        int            nichtUeberdecktAnz, alleineAnz;
        int            kandidatenAnz, auswahlNr;
        int            aenderNr;

        if (!problem.teilmengenKostenGleich()) {
            return actualCover;
        }

        laengenAnz = Konstanten.TABLOO_LAENGE_ANZ;
        aenderAnz = Math.round(tabooAenderFaktor / problem.dichteMittel());

        groesseGesamtmenge = problem.groesseGesamtmenge();

        // Wenn keine speichereffiziente Ausführung erfolgen soll, zu jedem
        // Index die Liste der diesen Index enthaltenden Teilmengen erzeugen.
        enthaltendeItm = null;
        if (!speicherEffizient) {
            enthaltendeItm = new ArrayList[groesseGesamtmenge];
            mittelAnzItm = Math.round(problem.ueberdeckungsHaeufigkeitMittel());
            for (int index = 0; index < groesseGesamtmenge; index++) {
                enthaltendeItm[index] = new ArrayList(mittelAnzItm);
            }

            itmIter = problem.iterator();
            while (itmIter.hasNext()) {
                naechsteItm = (IndexTeilmenge) itmIter.next();
                for (int index = naechsteItm.kleinsterEnthaltenerIndex();
                     index >= 0;
                     index = naechsteItm.naechsterEnthaltenerIndex(index + 1)) {
                    enthaltendeItm[index].add(naechsteItm);
                }
            }
        }

        bestTeilmengenAnz = actualCover.groesseFamilie();
        optimizedCover = actualCover;

        hinzuKandidaten = new ArrayList();
        entfernKandidaten = new ArrayList();
        zuletztEntfernt = null;

        for (int laengenNr = 0; laengenNr < laengenAnz; laengenNr++) {
            nextCover = (ItmFamilie) optimizedCover.clone();
            groesseFamilie = nextCover.groesseFamilie();
            tabooLaenge = 1 + rand.nextInt(Math.round(groesseFamilie
                                                      * Konstanten.TABOO_FAKTOR));
            tabooMenge = new TabooMenge(tabooLaenge);

            aenderNr = 0;
            while (aenderNr < aenderAnz) {
                aenderNr++;

                groesseFamilie = nextCover.groesseFamilie();
                nichtUeberdecktAnz = nextCover.anzNichtUeberdeckt();
                besteBewertung = Integer.MAX_VALUE;

                // Erzeugen der Kandidaten für die Hinzunahme.
                hinzuKandidaten.clear();
                if (groesseFamilie < bestTeilmengenAnz - 1) {
                    // Erzeugung des Iterators für die potentiellen
                    // Kandidaten.
                    if (speicherEffizient) {
                        itmIter = problem.iterator();
                    } else {
                        relevanteItm = new HashSet();
                        if (zuletztEntfernt == null) {
                            // Alle Teilmengen aufnehmen, die mindestens einen
                            // nicht überdeckten Index enthalten.
                            for (int index = 0; index < groesseGesamtmenge; index++) {
                                if (!nextCover.indexIstUeberdeckt(index)) {
                                    relevanteItm.addAll(enthaltendeItm[index]);
                                }
                            }
                        } else {
                            // Alle Teilmengen aufnehmen, die einen Index der
                            // zuletzt entfernten Teilmenge enthalten, der
                            // nicht überdeckt ist.
                            for (int index = zuletztEntfernt.kleinsterEnthaltenerIndex();
                                 index >= 0;
                                 index = zuletztEntfernt.naechsterEnthaltenerIndex(index + 1)) {
                                if (!nextCover.indexIstUeberdeckt(index)) {
                                    relevanteItm.addAll(enthaltendeItm[index]);
                                }
                            }
                        }
                        itmIter = relevanteItm.iterator();
                    }

                    // Erzeugung der eigentlichen Kandidaten.
                    while (itmIter.hasNext()) {
                        naechsteItm = (IndexTeilmenge) itmIter.next();
                        if (!nextCover.enthaelt(naechsteItm)) {
                            alleineAnz = nextCover.anzAlleineUeberdeckt(naechsteItm);
                            if ((alleineAnz == nichtUeberdecktAnz)
                                || (((zuletztEntfernt == null)
                                     || !naechsteItm.istDisjunkt(zuletztEntfernt))
                                    && !tabooMenge.contains(naechsteItm))) {
                                naechsteBewert = (groesseFamilie + 1
                                                  + nichtUeberdecktAnz - alleineAnz);
                                if (naechsteBewert <= besteBewertung) {
                                    if (naechsteBewert < besteBewertung) {
                                        hinzuKandidaten.clear();
                                        besteBewertung = naechsteBewert;
                                    }
                                    hinzuKandidaten.add(naechsteItm);
                                }
                            }
                        }
                    }
                }

                // Erzeugen der Kandidaten für die Entfernung.
                entfernKandidaten.clear();
                itmIter = nextCover.iterator();
                while (itmIter.hasNext()) {
                    naechsteItm = (IndexTeilmenge) itmIter.next();
                    alleineAnz = nextCover.anzAlleineUeberdeckt(naechsteItm);
                    if (!tabooMenge.contains(naechsteItm)) {
                        naechsteBewert = (groesseFamilie - 1
                                          + nichtUeberdecktAnz + alleineAnz);
                        if (naechsteBewert <= besteBewertung) {
                            if (naechsteBewert < besteBewertung) {
                                hinzuKandidaten.clear();
                                entfernKandidaten.clear();
                                besteBewertung = naechsteBewert;
                            }
                            entfernKandidaten.add(naechsteItm);
                        }
                    }
                }

                // Einen Knadidaten unter den besten auswählen und die neue
                // (Teil)Überdeckung erzeugen.
                kandidatenAnz = hinzuKandidaten.size() + entfernKandidaten.size();
                auswahlNr = rand.nextInt(kandidatenAnz);
                if (auswahlNr < hinzuKandidaten.size()) {
                    ausgewaehlt = (IndexTeilmenge) hinzuKandidaten.get(auswahlNr);
                    nextCover.teilmengeHinzufuegen(ausgewaehlt);
                    zuletztEntfernt = null;
                } else {
                    auswahlNr -= hinzuKandidaten.size();
                    ausgewaehlt = (IndexTeilmenge) entfernKandidaten.get(auswahlNr);
                    nextCover.teilmengeEntfernen(ausgewaehlt);
                    zuletztEntfernt = ausgewaehlt;
                }
                tabooMenge.add(ausgewaehlt);

                // Prüfen, ob die neue (Teil)Überdeckung eine vollständige
                // Überdeckung ist und besser ist als die bisher beste. Dann
                // auch die Nummer des Änderungs-Durchlaufs zurücksetzen.
                if ((nextCover.anzNichtUeberdeckt() == 0)
                        && (nextCover.kostenFamilie() < optimizedCover.kostenFamilie())) {
                    optimizedCover = (ItmFamilie) nextCover.clone();
                    aenderNr = 0;
                    if (nextCover.groesseFamilie() < bestTeilmengenAnz) {
                        bestTeilmengenAnz = nextCover.groesseFamilie();
                    }
                }
            }
        }

        return optimizedCover;
    }
}

