/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 29. August 2006
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


package lascer.praedikate.einzelne.realpraed;

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold
 */
public class Konstanten {

    /**
     * Gibt die Komplexität eines Prädikats für den Vergleich eines
     * real-Attribute mit einem Wert an.
     */
    public static final float REAL_WERT_PRAED_KOMPLEX = 2.0f;

    /**
     * Gibt die Komplexität eines Prädikats für den Vergleich zweier
     * real-Attribute in Bezug auf Gleichheit an.
     */
    public static final float REAL_REAL_GL_PRAED_KOMPLEX = 2.5f;

    /**
     * Gibt die Komplexität eines Prädikats für den Vergleich zweier
     * real-Attribute in Bezug auf die Beschränkung des einen Attributs durch
     * das andere Attribut oder deren Gleichheit an.
     */
    public static final float REAL_REAL_BG_PRAED_KOMPLEX = 2.5f;

    /**
     * Gibt die Komplexität eines Prädikats für den Vergleich zweier
     * real-Attribute in Bezug auf die Beschränkung des einen Attributs durch
     * das andere Attribut bei Umgleichheit an.
     */
    public static final float REAL_REAL_BU_PRAED_KOMPLEX = 2.5f;

    /**
     * Gibt die grundlegende Komplexität eines Prädikats zu einer Funktion für
     * real-Attribute an, bei dem geprüft wird, ob der Funktionswert gleich
     * einem gegebenen Wert ist. Bei der tatsächlichen Komplexität eines
     * Prädikats kommt noch die Komplexität der jeweiligen Funktion hinzu.
     */
    public static final float REAL_FUNK_WERT_PRAED_KOMPLEX = 1.0f;

    /**
     * Gibt die grundlegende Komplexität eines Prädikats zu einer Funktion für
     * real-Attribute an, bei dem geprüft wird, ob der Funktionswert in einem
     * gegebenen Halb-Intervall liegt. Bei der tatsächlichen Komplexität eines
     * Prädikats kommt noch die Komplexität der jeweiligen Funktion hinzu.
     */
    public static final float REAL_FUNK_HALB_ITV_PRAED_KOMPLEX = 1.5f;

    /**
     * Gibt die grundlegende Komplexität eines Prädikats zu einer Funktion für
     * real-Attribute an, bei dem geprüft wird, ob der Funktionswert in einem
     * gegebenen Voll-Intervall liegt. Bei der tatsächlichen Komplexität eines
     * Prädikats kommt noch die Komplexität der jeweiligen Funktion hinzu.
     */
    public static final float REAL_FUNK_VOLL_ITV_PRAED_KOMPLEX = 3.0f;

    /**
     * Die Repräsentation eines unbekannten Wertes eines real-Attributs.
     */
    public static final float
        UNBEKANNT_WERT = lascer.problemdaten.Konstanten.UNBEKANNT_REAL;
}

