/*
 * Dateiname      : Konstanten.java
 * Letzte �nderung: 18. M�rz 2007
 * Autoren        : Dietmar Lippold, Rene Berleong
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
     * Initialisierungswert f�r die Zufallsgeneratoren. F�r den regul�ren
     * Betrieb sollte dieser den Wert 0 haben, bei dem dann jedes Mal ein
     * anderer Initialisierungswert f�r den Zufallsgenerator erzeugt wird.
     * Zum Test eines Verfahrens kann der Initialisierungswert aber fest
     * vorgegeben werden.
     */
    public static final int SEED = 1;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>ProbabilityRatings</CODE>
     * bei einem unicost-Problem benutzt werden soll. Der Wert mu� kleiner
     * oder gleich 1 sein.
     */
    public static final float U_PROBABILITY_PORTION = 0.2f;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>ProbabilityRatings</CODE>
     * bei einem multicost-Problem benutzt werden soll. Der Wert mu� kleiner
     * oder gleich 1 sein.
     */
    public static final float M_PROBABILITY_PORTION = 0.25f;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>ChvatalRatings</CODE>
     * bei einem unicost-Problem benutzt werden soll. Der Wert mu� kleiner
     * oder gleich 1 sein.
     */
    public static final float U_CHVATAL_PORTION = 0.4f;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>ChvatalRatings</CODE>
     * bei einem multicost-Problem benutzt werden soll. Der Wert mu� kleiner
     * oder gleich 1 sein.
     */
    public static final float M_CHVATAL_PORTION = 0.75f;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>FrequencyRatings</CODE>
     * bei einem unicost-Problem benutzt werden soll. Die Summe aus
     * <CODE>U_CHVATAL_PORTION</CODE>, <CODE>U_PROBABILITY_PORTION</CODE>
     * und aus diesem Wert mu� kleiner oder gleich 1 sein.
     */
    public static final float U_FREQUENCY_PORTION = 0.0f;

    /**
     * Anteil der Iterationen, in denen die Klasse <CODE>FrequencyRatings</CODE>
     * bei einem multicost-Problem benutzt werden soll. Die Summe aus
     * <CODE>M_CHVATAL_PORTION</CODE>, <CODE>M_PROBABILITY_PORTION</CODE>
     * und aus diesem Wert mu� kleiner oder gleich 1 sein.
     */
    public static final float M_FREQUENCY_PORTION = 0.0f;

    /**
     * Anteil der F�lle, in denen bei Verwendung der Klasse
     * <CODE>FrequencyRatings</CODE> die absolute Bewertung verwendet wird. In
     * den anderen F�llen wird die relative Bewertung verwendet. Bei multicost
     * Problemen wird immer die absolute Bewertung verwendet.
     */
    public static final float FREQUENCY_ABS_PORTION = 0.5f;

    /**
     * Anteil der F�lle, in denen bei Verwendung der Klasse
     * <CODE>MarchSteenRatings</CODE> die Strategie 1 zum Hinzuf�gung und zum
     * Entfernen vewendet werden soll. In den anderen F�llen wird die
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
     * Gibt an, ob nach Ermittlung einer L�sung das Optimierungsverfahren
     * der Klasse <CODE>InferiorOptimization</CODE> angewendet werden soll.<p>
     *
     * Im urspr�nglichen Paper war dieser Wert implizit <CODE>true</CODE>.
     */
    public static final boolean USE_INFERIOR_OPT = true;

    /**
     * Gibt an, ob nach Ermittlung einer L�sung das Optimierungsverfahren
     * der Klasse <CODE>AddOneOptimization</CODE> angewendet werden soll.<p>
     *
     * Im urspr�nglichen Paper war dieser Wert implizit <CODE>false</CODE>.
     */
    public static final boolean USE_ADD_ONE_OPT = true;

    /**
     * Gibt an, ob nach Ermittlung einer L�sung das Optimierungsverfahren
     * der Klasse <CODE>AddTwoOptimization</CODE> angewendet werden soll.<p>
     *
     * Im urspr�nglichen Paper war dieser Wert implizit <CODE>false</CODE>.
     */
    public static final boolean USE_ADD_TWO_OPT = false;

    /**
     * Gr��e der Menge von m�glichen Teilmengen, die zur Auswahl zur Verf�gung
     * stehen.<p>
     *
     * Urspr�nglicher Wert: 400.<br>
     * Siehe Unterlagen Seite 3, letzter Abschnitt, Zeile 1.
     */
    public static final int MAX_CANDIDATES = 400;

    /**
     * Gibt den Faktor bzw. Quotienten an, mit dem sich die
     * Wahrscheinlichkeiten zur Auswahl der einzelnen Rating-Verfahren in der
     * Klasse <CODE>DynamicCandidateSelectCreation</CODE> augrund ihres
     * Erfolgs �ndern.
     */
    public static final float PORTION_CHANGE = 1.5f;
}

