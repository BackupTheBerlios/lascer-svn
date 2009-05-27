/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 06. November 2005
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2005
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

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold
 */
public class Konstanten {

    /**
     * Initialisierungswert für den Zufallsgenerator.
     */
    public static final int SEED = 1;

    /**
     * Initialer Wert für den <EM>Kostenfaktor</EM>, also den Faktor, mit dem
     * die Summe der Kosten der überdeckten Indices multipliziert wird, um die
     * Kosten für einen nicht überdeckten Index zu errechnen.
     */
    public static final float KOSTEN_FAKTOR_INIT = 1.0f;

    /**
     * Faktor, mit dem der Kostenfaktor, bei jeder Erhöhung multipliziert
     * wird.
     */
    public static final float KOSTEN_FAKTOR_FAKTOR = 2.0f;

    /**
     * Grenzwert, bis zu dem der Kostenfaktor erhöht wird.
     */
    public static final float KOSTEN_FAKTOR_GRENZE = 30.0f;
}

