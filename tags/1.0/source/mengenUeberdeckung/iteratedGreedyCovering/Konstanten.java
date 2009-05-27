/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 18. März 2007
 * Autoren        : Dietmar Lippold, Rene Berleong
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


package mengenUeberdeckung.iteratedGreedyCovering;

/**
 * Enthaelt alle im Algorithmus enthaltene Konstanten (nach einer
 * Beschreibung von Elena Marchiori und Adri Steenbeek ("An Iterated
 * Heuristic Algorithm for the Set Covering Problem", 1998).
 *
 * @author  Dietmar Lippold, Rene Berleong
 */
public final class Konstanten {

    /**
     * Initialisierungswert für die Zufallsgeneratoren. Für den regulären
     * Betrieb sollte dieser den Wert 0 haben, bei dem dann jedes Mal ein
     * anderer Initialisierungswert für den Zufallsgenerator erzeugt wird.
     * Zum Test eines Verfahrens kann der Initialisierungswert aber fest
     * vorgegeben werden.
     */
    public static final int SEED = 1;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>ProbabilityRatings</CODE>
     * bei einem unicost-Problem benutzt werden soll. Der Wert muß kleiner
     * oder gleich 1 sein.
     */
    public static final float U_PROBABILITY_PORTION = 0.2f;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>ProbabilityRatings</CODE>
     * bei einem multicost-Problem benutzt werden soll. Der Wert muß kleiner
     * oder gleich 1 sein.
     */
    public static final float M_PROBABILITY_PORTION = 0.25f;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>ChvatalRatings</CODE>
     * bei einem unicost-Problem benutzt werden soll. Der Wert muß kleiner
     * oder gleich 1 sein.
     */
    public static final float U_CHVATAL_PORTION = 0.4f;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>ChvatalRatings</CODE>
     * bei einem multicost-Problem benutzt werden soll. Der Wert muß kleiner
     * oder gleich 1 sein.
     */
    public static final float M_CHVATAL_PORTION = 0.75f;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>FrequencyRatings</CODE>
     * bei einem unicost-Problem benutzt werden soll. Die Summe aus
     * <CODE>U_CHVATAL_PORTION</CODE>, <CODE>U_PROBABILITY_PORTION</CODE>
     * und aus diesem Wert muß kleiner oder gleich 1 sein.
     */
    public static final float U_FREQUENCY_PORTION = 0.0f;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>FrequencyRatings</CODE>
     * bei einem multicost-Problem benutzt werden soll. Die Summe aus
     * <CODE>M_CHVATAL_PORTION</CODE>, <CODE>M_PROBABILITY_PORTION</CODE>
     * und aus diesem Wert muß kleiner oder gleich 1 sein.
     */
    public static final float M_FREQUENCY_PORTION = 0.0f;

    /**
     * Anteil der Fälle, in denen bei Verwendung der Klasse
     * <CODE>FrequencyRatings</CODE> die absolute Bewertung verwendet wird. In
     * den anderen Fällen wird die relative Bewertung verwendet. Bei multicost
     * Problemen wird immer die absolute Bewertung verwendet.
     */
    public static final float FREQUENCY_ABS_PORTION = 0.5f;

    /**
     * Anteil der Fälle, in denen bei Verwendung der Klasse
     * <CODE>MarchSteenRatings</CODE> die Strategie 1 zum Hinzufügung und zum
     * Entfernen vewendet werden soll. In den anderen Fällen wird die
     * Strategie 2 verwendet. Bei multicost Problemen wird als add-Strategie
     * immer Strategie 1 verwendet.
     */
    public static final float STRATEGY_1_PORTION = 0.5f;

    /**
     * Anzahl der Iterationen in der Hauptprozedur.<p>
     *
     * Kein urspruenglicher Wert.<br>
     * Siehe Unterlagen Seite 5, pseudo-code, Zeile 5.
     */
    public static final int NUMBER_OF_ITERATIONS = 10;

    /**
     * Gibt an, ob nach Ermittlung einer Lösung das Optimierungsverfahren
     * der Klasse <CODE>InferiorOptimization</CODE> angewendet werden soll.<p>
     *
     * Im ursprünglichen Paper war dieser Wert implizit <CODE>true</CODE>.
     */
    public static final boolean USE_INFERIOR_OPT = true;

    /**
     * Gibt an, ob nach Ermittlung einer Lösung das Optimierungsverfahren
     * der Klasse <CODE>AddOneOptimization</CODE> angewendet werden soll.<p>
     *
     * Im ursprünglichen Paper war dieser Wert implizit <CODE>false</CODE>.
     */
    public static final boolean USE_ADD_ONE_OPT = true;

    /**
     * Gibt an, ob nach Ermittlung einer Lösung das Optimierungsverfahren
     * der Klasse <CODE>AddTwoOptimization</CODE> angewendet werden soll.<p>
     *
     * Im ursprünglichen Paper war dieser Wert implizit <CODE>false</CODE>.
     */
    public static final boolean USE_ADD_TWO_OPT = false;

    /**
     * Größe der Menge von möglichen Teilmengen, die zur Auswahl zur Verfügung
     * stehen.<p>
     *
     * Ursprünglicher Wert: 400.<br>
     * Siehe Unterlagen Seite 3, letzter Abschnitt, Zeile 1.
     */
    public static final int MAX_CANDIDATES = 400;

    /**
     * Gibt den Faktor bzw. Quotienten an, mit dem sich die
     * Wahrscheinlichkeiten zur Auswahl der einzelnen Rating-Verfahren in der
     * Klasse <CODE>DynamicCandidateSelectCreation</CODE> augrund ihres
     * Erfolgs ändern.
     */
    public static final float PORTION_CHANGE = 1.5f;
}

