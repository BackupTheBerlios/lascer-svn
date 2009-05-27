/*
 * Dateiname      : Konstanten.java
 * Letzte �nderung: 22. August 2007
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2007
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


package lascer.praedikate.einzelne;

/**
 * Enth�lt default-Werte f�r die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold
 */
public class Konstanten {

    /**
     * Die Repr�sentation eines unbekannten Wertes eines nominalen Attributs.
     */
    public static final String
        UNBEKANNT_NOMINAL = lascer.problemdaten.Konstanten.UNBEKANNT_NOMINAL;

    /**
     * Gibt die Komplexit�t eines Pr�dikats f�r den Vergleich eines nominalen
     * Attribute mit einem Wert an.
     */
    public static final float NOM_WERT_PRAED_KOMPLEX = 2.0f;

    /**
     * Gibt die Komplexit�t eines Pr�dikats f�r den Vergleich zweier nominaler
     * Attribute an.
     */
    public static final float NOM_NOM_PRAED_KOMPLEX = 2.5f;

    /**
     * Gibt den Faktor an, mit dem die Dimension des Raums eines
     * Hyper-Ebenen-Pr�dikats multipliziert wird, um dessen Komplexit�t zu
     * errechnen.
     */
    public static final float HYP_EBEN_KOMPLEX_FAKTOR = 2.75f;
}

