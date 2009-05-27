/*
 * Dateiname      : Konstanten.java
 * Letzte �nderung: 28. Juni 2005
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


package lascer.konzepte.einzelne;

/**
 * Enth�lt default-Werte f�r die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold
 */
public class Konstanten {

    /**
     * Gibt an, ob die Komplexit�t eines Literals unabh�ngig von der
     * Komplexit�t des enthaltenen Pr�dikats sein soll.
     */
    public static final boolean LIT_UNI_KOMPLEX = false;

    /**
     * Gibt die Komplexit�t eines Literals an, wenn diese unabh�ngig ist von
     * der Komplexit�t des enthaltenen Pr�dikats.
     */
    public static final float INIT_LIT_UNI_KOMPLEX = 2.0f;

    /**
     * Gibt die Komplexit�t eines Literals an, wenn diese abh�ngig ist von der
     * Komplexit�t des enthaltenen Pr�dikats und zu dieser addiert wird.
     */
    public static final float INIT_LIT_ADD_KOMPLEX = 0.0f;

    /**
     * Gibt den Wert an, um den sich die Komplexit�t eines Literals durch
     * Invertieren erh�ht.
     */
    public static final float INV_LIT_KOMPLEX = 0.5f;

    /**
     * Gibt die initiale Komplexit�t einer Disjunktion an.
     */
    public static final float INIT_DIS_KOMPLEX = 1.0f;

    /**
     * Gibt die initiale Komplexit�t einer Konjunktion an.
     */
    public static final float INIT_KON_KOMPLEX = 1.0f;
}

