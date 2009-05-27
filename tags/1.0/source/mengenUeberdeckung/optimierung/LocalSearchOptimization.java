/*
 * Dateiname      : LocalSearchOptimization.java
 * Letzte �nderung: 19. M�rz 2007
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
 * Optimiert eine L�sung zu einem unicost-�berdeckungsproblem durch eine
 * lokale Suche mittels Hinzuf�gen und Entfernen von Teilmengen.
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
     * um die Anzahl der �nderungen der Familie zu jeder zu testenden L�nge
     * der Taboo-Liste zu erhalten.
     */
    private int tabooAenderFaktor;

    /**
     * Gibt an, ob eine Speicher-effiziente und damit aber nicht
     * Laufzeit-effiziente Ausf�hrung erfolgen soll.
     */
    private boolean speicherEffizient;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param rand               Ein Zufallsgenerator.
     * @param speicherEffizient  Gibt an, ob eine Speicher-effiziente und
     *                           damit aber nicht Laufzeit-effiziente
     *                           Ausf�hrung erfolgen soll.
     * @param tabooAenderFaktor  Der Faktor, mit dem der Reziprok-Wert der
     *                           Dichte multipliziert wird, um die Anzahl der
     *                           �nderungen der Familie zu jeder zu testenden
     *                           L�nge der Taboo-Liste zu erhalten.
     */
    public LocalSearchOptimization(Random rand, boolean speicherEffizient,
                                   int tabooAenderFaktor) {

        this.rand = rand;
        this.tabooAenderFaktor = tabooAenderFaktor;
        this.speicherEffizient = speicherEffizient;
    }

    /**
     * Erzeugt eine neue Instanz. F�r den Faktor zur Ermittlung der Anzahl der
     * �nderungen einer zu optimierenden Familie wird ein Standard-Wert
     * verwendet.
     *
     * @param rand               Ein Zufallsgenerator.
     * @param speicherEffizient  Gibt an, ob eine Speicher-effiziente und
     *                           damit aber nicht Laufzeit-effiziente
     *                           Ausf�hrung erfolgen soll.
     */
    public LocalSearchOptimization(Random rand, boolean speicherEffizient) {
        this(rand, speicherEffizient, Konstanten.TABOO_AENDER_FAKTOR);
    }

    /**
     * Liefert eine optimierte L�sung, also eine L�sung mit geringeren
     * Kosten als die �bergebene aktuelle L�sung. Wenn eine solche nicht
     * ermittelt werden kann, wird die �bergebene Familie geliefert.
     *
     * @param problem             Das zu Grunde liegende SCP-Problem.
     * @param actualCover         Die aktuelle, zu optimierende L�sung. Diese
     *                            darf nur notwendige Teilmengen enthalten.
     * @param bestKnownSolution   Die bisher beste L�sung des Gesamtproblems.
     *                            Der Wert kann <CODE>null</CODE> sein, wenn
     *                            z.B. noch keine L�sung bekannt ist.
     * @param candidatesCreation  Das aktuelle Verfahren zur Erzeugung der
     *                            Kandidaten zur Aufnahme in und Entfernung
     *                            aus einer Familie.
     * @param candidateSelection  Das aktuelle Verfahren zur Auswahl der
     *                            Kandidaten zur Aufnahme in und zur
     *                            Entfernung aus einer Familie.
     *
     * @return  Eine Familie mit geringeren Kosten oder, wenn eine solche
     *          nicht ermittelt werden konnte, <CODE>actualCover</CODE>. Die
     *          gelieferte Familie enth�lt nur notwendige Teilmengen.
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

        // Wenn keine speichereffiziente Ausf�hrung erfolgen soll, zu jedem
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

                // Erzeugen der Kandidaten f�r die Hinzunahme.
                hinzuKandidaten.clear();
                if (groesseFamilie < bestTeilmengenAnz - 1) {
                    // Erzeugung des Iterators f�r die potentiellen
                    // Kandidaten.
                    if (speicherEffizient) {
                        itmIter = problem.iterator();
                    } else {
                        relevanteItm = new HashSet();
                        if (zuletztEntfernt == null) {
                            // Alle Teilmengen aufnehmen, die mindestens einen
                            // nicht �berdeckten Index enthalten.
                            for (int index = 0; index < groesseGesamtmenge; index++) {
                                if (!nextCover.indexIstUeberdeckt(index)) {
                                    relevanteItm.addAll(enthaltendeItm[index]);
                                }
                            }
                        } else {
                            // Alle Teilmengen aufnehmen, die einen Index der
                            // zuletzt entfernten Teilmenge enthalten, der
                            // nicht �berdeckt ist.
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

                // Erzeugen der Kandidaten f�r die Entfernung.
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

                // Einen Knadidaten unter den besten ausw�hlen und die neue
                // (Teil)�berdeckung erzeugen.
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

                // Pr�fen, ob die neue (Teil)�berdeckung eine vollst�ndige
                // �berdeckung ist und besser ist als die bisher beste. Dann
                // auch die Nummer des �nderungs-Durchlaufs zur�cksetzen.
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

