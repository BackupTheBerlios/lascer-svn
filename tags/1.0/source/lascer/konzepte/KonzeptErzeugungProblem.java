/*
 * Dateiname      : KonzeptErzeugungProblem.java
 * Letzte Änderung: 09. Juli 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2006
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

import java.util.HashSet;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.Random;
import java.io.Serializable;
import de.unistuttgart.architeuthis.userinterfaces.develop.PartialProblem;
import de.unistuttgart.architeuthis.userinterfaces.develop.PartialSolution;
import de.unistuttgart.architeuthis.abstractproblems.AbstractFixedSizeProblem;
import de.unistuttgart.architeuthis.abstractproblems.ContainerPartialSolution;

import lascer.KonzErzParameter;
import lascer.problemdaten.Beispieldaten;

/**
 * Erzeugt aus Prädikaten der verschiedenen Arten Konzepte. Die verschiedenen
 * Phasen werden mehrmals durchlaufen.
 *
 * @author  Dietmar Lippold
 */
public class KonzeptErzeugungProblem extends AbstractFixedSizeProblem {

    /**
     * Der Logger dieser Klasse.
     */
    private static final Logger LOGGER
        = Logger.getLogger(KonzeptErzeugungProblem.class.getName());

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
     * Die Wahrscheinlichkeit, mit der bei einer parallelen Verarbeitung
     * mehrerer Teilprobleme ein nicht überdecktes bzw. nicht ausgeschlossenes
     * Beispiel als überdeckt bzw. ausgeschlossen behandelt werden soll.
     */
    private float parallelAenderWkt;

    /**
     * Die Angabe, ob die allgemeinen Konzepte der Erzeugung korrekter
     * Konzepte dienen, die als Parameter in der Methode <CODE>compute</CODE>
     * an die Methode <CODE>erzPraedSpezKonzepte</CODE> übergeben wird.
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
     * @param parallelAenderWkt         Die Wahrscheinlichkeit, mit der bei
     *                                  einer parallelen Verarbeitung mehrerer
     *                                  Teilprobleme ein nicht überdecktes
     *                                  bzw. nicht ausgeschlossenes Beispiel
     *                                  als überdeckt bzw. ausgeschlossen
     *                                  behandelt werden soll.
     * @param paramKorrKonzErzeugung    Die Angabe, ob korrekter Konzepte
     *                                  erzeugt werden sollen, als Parameter
     *                                  für die Methode
     *                                  <CODE>erzPraedSpezKonzepte</CODE>.
     */
    public KonzeptErzeugungProblem(Random rand, Beispieldaten beispieldaten,
                                   KonzErzParameter parameter,
                                   ArrayList paramPraedikatErzeuger,
                                   KonzeptVerwaltung paramKonzeptVerwaltung,
                                   KombiKonzept besteKorrFormelGesamt,
                                   KombiKonzept besteVollFormelGesamt,
                                   KombiKonzept paramAeussereBesteFormel,
                                   float parallelAenderWkt,
                                   boolean paramKorrKonzErzeugung) {

        this.rand = rand;
        this.beispieldaten = beispieldaten;
        this.parameter = parameter;
        this.paramPraedikatErzeuger = paramPraedikatErzeuger;
        this.paramKonzeptVerwaltung = paramKonzeptVerwaltung;
        this.besteKorrFormelGesamt = besteKorrFormelGesamt;
        this.besteVollFormelGesamt = besteVollFormelGesamt;
        this.paramAeussereBesteFormel = paramAeussereBesteFormel;
        this.parallelAenderWkt = parallelAenderWkt;
        this.paramKorrKonzErzeugung = paramKorrKonzErzeugung;
    }

    /**
     * Liefert ein Array von Teilproblemen. Die im Array enthaltenen Werte
     * <code>null</code> bleiben unberücksichtigt.
     *
     * @param parProbsSuggested  Die vorgeschlagene Anzahl von Teilproblemen.
     *                           Diese ist grösser oder gleich Eins.
     *
     * @return  Array von Teilproblemen.
     */
    protected PartialProblem[] createPartialProblems(int parProbsSuggested) {
        PartialProblem[]  teilprobleme;
        KonzeptVerwaltung neueKonzeptVerwaltung;
        Random            randParProb;
        float             realAenderWkt;

        if (parProbsSuggested == 1) {
            realAenderWkt = 0;
        } else {
            realAenderWkt = parallelAenderWkt;
        }
        teilprobleme = new PartialProblem[parProbsSuggested];
        for (int i = 0; i < parProbsSuggested; i++) {
            randParProb = new Random(rand.nextLong());
            neueKonzeptVerwaltung = paramKonzeptVerwaltung.flacheKopie(randParProb);
            teilprobleme[i] = new KonzeptErzeugungTeilprob(randParProb,
                                                           beispieldaten, parameter,
                                                           paramPraedikatErzeuger,
                                                           neueKonzeptVerwaltung,
                                                           besteKorrFormelGesamt,
                                                           besteVollFormelGesamt,
                                                           paramAeussereBesteFormel,
                                                           realAenderWkt,
                                                           paramKorrKonzErzeugung);
        }
        return teilprobleme;
    }

    /**
     * Erstellt eine Gesamtlösung aus den übergebenen Teillösungen
     *
     * @param partialSolutions  Die Teillösungen zu allen Teilproblemen.
     *
     * @return  Die Gesamtlösung.
     */
    protected Serializable createSolution(PartialSolution[] partialSolutions) {
        ContainerPartialSolution teilloesungsContainer;
        HashSet                  erzeugteKonzepte;
        ArchiLoesung             teilloesung;
        KombiKonzept             besteFormelGesamt, neueFormel;
        boolean                  weiterVerbesserMoeglich;

        besteFormelGesamt = null;
        erzeugteKonzepte = new HashSet();
        weiterVerbesserMoeglich = false;
        for (int i = 0; i < partialSolutions.length; i++) {
            teilloesungsContainer = (ContainerPartialSolution) partialSolutions[i];
            teilloesung = (ArchiLoesung) teilloesungsContainer.getPartialSolution();

            neueFormel = teilloesung.besteFormel();
            if ((besteFormelGesamt == null)
                    || neueFormel.istBesser(besteFormelGesamt)) {
                besteFormelGesamt = neueFormel;
            }
            erzeugteKonzepte.addAll(teilloesung.erzeugteKonzepte());
            weiterVerbesserMoeglich |= teilloesung.weiterVerbesserMoeglich();
        }
        return new ArchiLoesung(erzeugteKonzepte, besteFormelGesamt,
                                weiterVerbesserMoeglich);
    }
}

