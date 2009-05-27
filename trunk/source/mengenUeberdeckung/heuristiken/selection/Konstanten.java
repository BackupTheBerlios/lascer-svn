/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 23. Juli 2005
 * Autoren        : Dietmar Lippold, Rene Berleong
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2005
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


package mengenUeberdeckung.heuristiken.selection;

/**
 * Enthält alle im Algorithmus enthaltene Konstanten (nach einer
 * Beschreibung von Elena Marchiori und Adri Steenbeek ("An Iterated
 * Heuristic Algorithm for the Set Covering Problem", 1998) der Klassen von
 * diesem Package.
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
     * Wahrscheinlichkeit, mit der der fuer die Aufnahme einer Teilmenge
     * entscheidende "add_value" nicht berechnet wird und die
     * hinzuzufuegende Teilmenge unter den "Candidates" zufaellig
     * ausgwaehlt wird.<p>
     *
     * Urspruenglicher Wert: 0.05.<br>
     * Siehe Unterlagen Seite 3, vorletzter Abschnitt, Zeile 6.
     */
    public static final float ADD_RAND = 0.05f;

    /**
     * Wahrscheinlichkeit, mit der der fuer das Entfernen einer Teilmenge
     * entscheidende "rmv_value" nicht berechnet wird und die
     * zu entfernende Teilmenge unter den "Candidates" zufaellig
     * ausgwaehlt wird.<p>
     *
     * Urspruenglicher Wert: 0.05.<br>
     * Siehe Unterlagen Seite 4, zweiter Abschnitt, Zeile 6.
     */
    public static final float RMV_RAND = 0.05f;

    /**
     * Konstante, die ein Intervall angibt, das beim Vergleich der Bewertungen
     * der Teilmengen mit dem Maximum der Bewertungen in den Methoden
     * <code>selectAdd</code> und <code>selectRmv</code> von
     * <code>EnhancedGreedyHeuristic</code> verwendet wird. Werte, die beim
     * Vergleich maximal um den angegebenen Anteil kleiner sind als das
     * gesuchte Maximum werden als gleich zum Maximum angesehen. Durch dieses
     * Vorgehen werden Probleme mit dem Vergleich von Fließkommazahlen
     * vermieden.<p>
     *
     * Die Konstante wurde neu eingeführt und ist im angegebenen Paper nicht
     * enthalten.
     */
    public static final float CMP_INTERVAL = 0.001f;
}

