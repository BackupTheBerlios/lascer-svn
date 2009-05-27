/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 10. Juni 2006
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


package lascer.praedikate.erzeugung.realpraed;

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold
 */
public class Konstanten {

    /**
     * Der bezüglich der normalen Vergleichsfunktionen kleinste float-Wert.
     * Dies ist der betragsmäßig größte negative float-Wert.
     */
    public static final float MIN_WERT = -Float.MAX_VALUE;

    /**
     * Der bezüglich der normalen Vergleichsfunktionen kleinste float-Wert.
     * Dies ist der betragsmäßig größte positive float-Wert.
     */
    public static final float MAX_WERT = Float.MAX_VALUE;

    /**
     * Die Repräsentation eines unbekannten Wertes eines real-Attributs.
     */
    public static final float
        UNBEKANNT_WERT = lascer.problemdaten.Konstanten.UNBEKANNT_REAL;
}

