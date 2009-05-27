/*
 * Dateiname      : ChvatalGreedyAuswahlStatistic.java
 * Letzte Änderung: 26. Juli 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2006
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


package mengenUeberdeckung.tests.bib;

import java.util.Random;
import java.io.IOException;

import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.heuristiken.selection.CandidateSelection;
import mengenUeberdeckung.heuristiken.selection.BestRatingSelection;
import mengenUeberdeckung.greedyCovering.EnhancedGreedyHeuristic;
import mengenUeberdeckung.heuristiken.ratings.CandidateRatings;
import mengenUeberdeckung.heuristiken.ratings.ChvatalRating;
import mengenUeberdeckung.heuristiken.utility.FrequencyUtility;
import mengenUeberdeckung.reduktion.UeberdeckNachSofortAuswahl;
import mengenUeberdeckung.tests.bib.parser.ParseException;

/**
 * Diese Klasse erzeugt eine Statistik mit dem Greedy-Verfahren unter
 * Verwendung der Chvatal-Heuristik zur Mengenüberdeckung zu einem Verzeichnis
 * mit Problemdateien, wobei die Familie der Teilmengen zuerst reduziert wird.
 * Der Name des Verzeichnisses, in dem die Problemdateien liegen, wird
 * zusammen mit dem Typ der Dateien und dem Namen der Ausgabedatei auf der
 * Komandozeile übergeben. Außerdem ist ein Faktor anzugeben, der die Größe
 * der Auswahl bestimmt.
 *
 * @author  Dietmar Lippold
 */
public class ChvatalGreedyAuswahlStatistic {

    /**
     * Initialisierungswert für den Zufallsgenerator. Für den regulären
     * Betrieb sollte dieser den Wert 0 sein, bei dem dann jedes Mal ein
     * anderer Initialisierungswert für den Zufallsgenerator erzeugt wird.
     * Zum Test eines Verfahrens kann der Initialisierungswert aber fest
     * vorgegeben werden.
     */
    public static final int SEED = 1;

    /**
     * Zufallsgenerator.
     */
    private static Random rand = new Random();

    /**
     * Liefert ein Verfahren, das eine Familie von Teilmengen zuerst reduziert
     * und dann das Greedy-Verfahren mit der Chvatal-Heuristik zur
     * Mengenüberdeckung darauf anwendet.
     *
     * @param minUeberdeckAnz  Die minimale Häufigkeit, mit der jedes Element
     *                         überdeckt werden soll.
     * @param faktorErgAnz     Das Verhältnis der Anzahl der Teilmengen der
     *                         ergänzten Auswahl und der primären Auswahl.
     *
     * @return  Ein Verfahren zur Erzeugung einer Mengennüberdeckung.
     */
    private static UeberdeckungsOptimierung scpVerfahren(int minUeberdeckAnz,
                                                         float faktorErgAnz) {
        UeberdeckungsOptimierung  uerberdeckVerfahren;
        CandidateRatings          candidateRatings;
        CandidateSelection        candidateSelection;
        FrequencyUtility          freqUtil;

        if (SEED != 0) {
            rand.setSeed(SEED);
        }

        candidateRatings = new ChvatalRating();
        candidateSelection = new BestRatingSelection(candidateRatings, rand);
        uerberdeckVerfahren = new EnhancedGreedyHeuristic(null, candidateSelection,
                                                          rand);

        if (faktorErgAnz < 0) {
            return uerberdeckVerfahren;
        } else {
            freqUtil = new FrequencyUtility(false);
            return (new UeberdeckNachSofortAuswahl(0, minUeberdeckAnz, faktorErgAnz,
                                                   freqUtil, uerberdeckVerfahren));
        }
    }

    /**
     * Entnimmt die Parameter aus dem übergebenen Array von Strings und
     * erzeugt eine Excel-Datei mit den Ergebnissen.<P>
     *
     * Auf der Komandozeile sind nacheinander folgende Parameter anzugeben:
     *
     * <OL>
     * <LI>Der Name des Verzeichnisses mit den Problemdateien.
     * <LI>Der Typ der Problemdateien.
     * <LI>Der Name der Datei, in die die Excel-Tabelle geschrieben werden
     *     soll, oder <EM>stdout</EM>, wenn das Ergebnis auf der
     *     Standard-Ausgabe ausgegeben werden soll.
     * <LI>Die minimale Häufigkeit, mit der jedes Element überdeckt werden
     *     soll.
     * <LI>Der Faktor, um den die Anzahl der Teilmengen in der ergänzten
     *     Auswahl größer ist als die Anzahl der Teilmengen in der primären
     *     Auswahl.
     * </OL>
     *
     * Als Typ sind folgende Angaben möglich:
     *
     * <DL>
     * <DT>or-bib</DT>
     * <DD>Die Dateien haben das Format der OR-Bibliothek.</DD>
     * <DT>rail-bib</DT>
     * <DD>Die Dateien haben das Format des Wettbewerbs mit den Daten der
     *     Eisenbahngesellschaft.</DD>
     * <DT>triple</DT>
     * <DD>Die Dateien haben das Format eines Steiner triple systems.</DD>
     * <DT>xu-bench</DT>
     * <DD>Die Dateien haben das Format der Benchmarks von Xu.</DD>
     * <DT>setOfSets</DT>
     * <DD>Die Dateien habe das Format, in dem eine Familie von der Methode
     *     <CODE>ItmFamilie.toLibFormat()</CODE> ausgegeben wird.
     * </DL>
     *
     * @param args  Ein Array von Strings, das den Namen des Verzeichnisses
     *              mit den Problemdateien, den Typ der Problemdateien, den
     *              Namen der Ausgabedatei, die minimale Häufigkeit der
     *              Überdeckung eines Elements und einen Faktor enthält. Der
     *              Faktor gibt an, wie viele mal mehr Teilmengen in der
     *              sekundären gegenüber der primären Auswahl enthalten sind.
     *              Bei einem negativen Wert erfolgt keine Auswahl.
     */
    public static void main(String[] args) {
        UeberdeckungsOptimierung scpVerfahren;
        float                    faktor;
        int                      minUeberdeckAnz;

        if (args.length != 5) {
            System.err.println("Keine vier Parameter angegeben");
            System.exit(-1);
        }

        minUeberdeckAnz = Integer.parseInt(args[3]);
        faktor = Float.parseFloat(args[4]);

        try {
            scpVerfahren = scpVerfahren(minUeberdeckAnz, faktor);
            ResultStatisticCreation.createStatistic(args[0], args[1],
                                                    args[2], scpVerfahren);
        } catch (IllegalArgumentException e) {
            System.err.println("Einer der Komandozeilenparameter war nicht"
                               + "korrekt.");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Eine der Dateien hatte nicht das angegebene"
                               + " Format.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Es ist ein Fehler beim Schreiben der Datei"
                               + " aufgetreten.");
            e.printStackTrace();
        }
    }
}

