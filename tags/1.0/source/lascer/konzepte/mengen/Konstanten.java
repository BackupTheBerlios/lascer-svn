/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 24. Juli 2006
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


package lascer.konzepte.mengen;

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold
 */
public class Konstanten {

    /**
     * Gibt an, ob die Güte der Überdeckung und des Ausschlusses eines
     * Elements unabhängig von der Komplexität der überdeckenden bzw.
     * ausschließenden Konzepte sein soll. Eine Berücksichtigung der
     * Komplexität führt zu einer höheren Laufzeit aber evtl. auch zu einer
     * Formel mit geringerer Komplexität.
     */
    public static final boolean GUETE_EINHEITLICH = false;

    /**
     * Gibt den Exponenten für die Häufigkeit der Überdeckung bzw. des
     * Ausschlusses bei der Berechnung der Index-Kosten an. Der Wert muß
     * gleich oder größer als Null sein. Beim Wert Null wird bei den Kosten
     * eines Indices nur unterschieden, ob er mindestens einmal überdeckt
     * bzw. ausgeschlossen wird.
     */
    public static final float HAEUFIGKEITS_EXPONENT = 1.0f;

    /**
     * Bei der Auswahl der allgemeinen Konzeptmengen die minimale Häufigkeit,
     * mit der jedes Element überdeckt sein soll. Ein Wert von Null bedeutet,
     * daß keine minimale Häuigkeit gefordert wird.
     */
    public static final int ALG_MIN_UEBERDECK_ANZ = 0;

    /**
     * Bei der Auswahl der speziellen, d.h. korrekten oder vollständigen,
     * Konzeptmengen, die minimale Häufigkeit, mit der jedes Element überdeckt
     * sein soll. Ein Wert von Null bedeutet, daß keine minimale Häuigkeit
     * gefordert wird.
     */
    public static final int SPEZ_MIN_UEBERDECK_ANZ = 0;

    /**
     * Bei der Auswahl der allgemeinen Konzeptmengen das Verhältnis der Anzahl
     * der Teilmengen in der ergänzten Auswahl zur Anzahl der Teilmengen in
     * der Basis-Auswahl. Bei einem Wert von Null wird keine ergänzende
     * Auswahl erstellt. Ansonsten muß der Wert größer oder gleich Eins sein.
     */
    public static final float ALG_FAKTOR_ERG_ANZ = 5.0f;

    /**
     * Bei der Auswahl der speziellen, d.h. korrekten oder vollständigen,
     * Konzeptmengen das Verhältnis der Anzahl der Teilmengen in der ergänzten
     * Auswahl zur Anzahl der Teilmengen in der Basis-Auswahl. Bei einem Wert
     * von Null wird keine ergänzende Auswahl erstellt. Ansonsten muß der Wert
     * größer oder gleich Eins sein.
     */
    public static final float SPEZ_FAKTOR_ERG_ANZ = 5.0f;

    /**
     * Gibt an, ob bei der Erzeugung eines speziellen Konzepts aus den
     * allgemeinen Konzepten eine Optimierung mit dem Inferior Verfahren
     * erfolgen soll (Parameter vom Konstruktor
     * <CODE>IterEnhancedGreedyHeuristic</CODE>).
     */
    public static final boolean SPEZ_USE_INFERIOR_OPT = false;

    /**
     * Gibt an, ob bei der Erzeugung einer Formel aus den speziellen, d.h.
     * aus den korrekten oder vollständigen, Konzepten eine Optimierung mit
     * dem Inferior Verfahren erfolgen soll (Parameter vom Konstruktor
     * <CODE>IterEnhancedGreedyHeuristic</CODE>).
     */
    public static final boolean FORM_USE_INFERIOR_OPT = true;

    /**
     * Gibt an, ob bei der Erzeugung eines speziellen Konzepts aus den
     * allgemeinen Konzepten eine Optimierung mit dem Add-One Verfahren
     * erfolgen soll (Parameter vom Konstruktor
     * <CODE>IterEnhancedGreedyHeuristic</CODE>).
     */
    public static final boolean SPEZ_USE_ADD_ONE_OPT = false;

    /**
     * Gibt an, ob bei der Erzeugung einer Formel aus den speziellen, d.h.
     * aus den korrekten oder vollständigen, Konzepten eine Optimierung mit
     * dem Add-One Verfahren erfolgen soll (Parameter vom Konstruktor
     * <CODE>IterEnhancedGreedyHeuristic</CODE>).
     */
    public static final boolean FORM_USE_ADD_ONE_OPT = true;
}

