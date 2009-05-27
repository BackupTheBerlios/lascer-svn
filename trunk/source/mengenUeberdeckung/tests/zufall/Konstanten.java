/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 13. März 2004
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2004
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


package mengenUeberdeckung.tests.zufall;

/**
 * Enthält Konstanten für die Testprogramme.
 *
 * @author  Dietmar Lippold
 */
public final class Konstanten {

    /**
     * Minimale Anzahl der Teilmengen bei der Reduktion. Ein negativer Wert
     * bedeutet, daß keine Reduktion stattfindet. Bei einem Wert von Null
     * werden nur die notwendigen Teilmengen in die primäre Auswahl
     * aufgenommen. Die Größe der ergänzten Auswahl wird durch
     * <CODE>FAKTOR_ERG_ANZ</CODE> festgelegt.
     */
    public static final int MIN_ITM_ANZ = 0;

    /**
     * Die minimale Häufigkeit, mit der jedes Element überdeckt sein soll.
     * Ein Wert von Null bedeutet, daß keine minimale Häuigkeit gefordert
     * wird.
     */
    public static final int MIN_UEBERDECK_ANZ = 0;

    /**
     * Bei der Reduktion das Verhältnis der Anzahl der Teilmengen in der
     * ergänzten Auswahl zur Anzahl der Teilmengen in der Basis-Auswahl. Bei
     * einem Wert von Null wird keine ergänzende Auswahl erstellt. Ansonsten
     * muß der Wert größer oder gleich Eins sein.
     */
    public static final float FAKTOR_ERG_ANZ = 5.0f;
}

