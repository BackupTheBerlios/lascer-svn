/*
 * Dateiname      : NullStellRealFunkErz.java
 * Letzte Änderung: 03. Mai 2006
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


package lascer.realfunktionen.arten;

import java.io.Serializable;

/**
 * Definiert die Erzeugung einer nullstelligen real-Funktion.
 *
 * @author  Dietmar Lippold
 */
public interface NullStellRealFunkErz extends Serializable {

    /**
     * Erzeugt eine neue nullstellige Funktion mit der angegebenen Nummer.
     *
     * @param nummer  Die Nummer der nullstelligen Funktion, die erzeugt
     *                werden soll.
     *
     * @return  Die erzeugte nullstelligen Funktion.
     */
    public NullStellRealFunk nullStelligeFunktion(int nummer);
}

