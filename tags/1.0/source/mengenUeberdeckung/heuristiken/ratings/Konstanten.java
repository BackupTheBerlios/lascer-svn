/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 23. Juli 2006
 * Autoren        : Rene Berleong, Dietmar Lippold
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


package mengenUeberdeckung.heuristiken.ratings;

/**
 * Enthält einige Konstanten (zum Teil nach einer Beschreibung von Elena
 * Marchiori und Adri Steenbeek ("An Iterated Heuristic Algorithm for the Set
 * Covering Problem", 1998).
 *
 * @author  Rene Berleong, Dietmar Lippold
 */
public final class Konstanten {

    /**
     * Konstante aus der den Formeln zu <code>wAdd</code> und
     * <code>wRmv</code>. Dieser Wert gilt für unicost Probleme.<p>
     *
     * Ursprünglicher Wert: 0.01.<br>
     * Siehe Unterlagen Seite 5, Kasten 1 und Kasten 2.
     */
    public static final float UNICOST_ADD_CONSTANT = 0.01f;

    /**
     * Konstante aus der den Formeln zu <code>wAdd</code> und
     * <code>wRmv</code>, angepaßt auf multicost-Probleme.<p>
     *
     * Ursprünglicher Wert: keiner
     */
    public static final float MULTICOST_ADD_CONSTANT = 1.0f;

    /**
     * Default-Wert für den Exponenten, der bei der Berechnung der Potenz
     * der relativen Kosten einer Teilüberdeckung der Elemente in der Klasse
     * <CODE>FrequencyRating</CODE> verwendet wird.
     */
    public static final float DEFAULT_REL_COST_EXPONENT = 1.0f;

    /**
     * Anzahl der Iterationen in der Klasse <CODE>ProbabilityRatings</CODE>.
     */
    public static final int PROB_ITER_NUMBER = 10;

    /**
     * Der Faktor, mit dem die Veränderungen der Wahrscheinlichkeiten der
     * Teilmengen in der Klasse <CODE>ProbabilityRatings</CODE> gedämpft
     * werden.
     */
    public static final float DAEMPFUNGS_FAKTOR = 0;

    /**
     * Der Wert, mit dem bei unicost-Problemen die Relation aus der
     * allgemeinen Wahrscheinlichkeit und den Kosten einer Teilmenge zur
     * Ermittlung ihrer speziellen (elementbezogenen) Wahrscheinlichkeit in
     * der Klasse <CODE>ProbabilityRatings</CODE> potenziert wird.
     */
    public static final float U_REL_EXPONENT = 1.0f;

    /**
     * Der Wert, mit dem bei multicost-Problemen die Relation aus der
     * allgemeinen Wahrscheinlichkeit und den Kosten einer Teilmenge zur
     * Ermittlung ihrer speziellen (elementbezogenen) Wahrscheinlichkeit in
     * der Klasse <CODE>ProbabilityRatings</CODE> potenziert wird.
     */
    public static final float M_REL_EXPONENT = 2.0f;

    /**
     * Gibt an, ob die wt-Werte in der Klasse <CODE>ProbabilityRatings</CODE>
     * standardmäßig mit einem einheitlichen Wert initialisiert werden sollen.
     * Anderenfalls hängt die Wahrscheinlichkeit von der Anzahl der jeweils
     * enthaltenen Elemente ab.
     */
    public static final boolean WT_INIT_EINHEITLICH = true;

    /**
     * Gibt an, ob in der Klasse <CODE>ProbabilityRatings</CODE> standardmäßig
     * das Produkt-Verfahren verwendet werden soll. Falls nicht, wird
     * standardmäßig das Summen-Verfahren verwendet.
     */
    public static final boolean PROB_PROD_VERFAHREN = true;
}

