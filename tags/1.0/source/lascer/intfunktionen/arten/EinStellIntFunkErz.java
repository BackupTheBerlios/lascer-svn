/*
 * Dateiname      : EinStellIntFunkErz.java
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


package lascer.intfunktionen.arten;

import java.io.Serializable;

import lascer.intfunktionen.ErzeugbareIntFunk;

/**
 * Definiert die Erzeugung einer einstelligen int-Funktion.
 *
 * @author  Dietmar Lippold
 */
public interface EinStellIntFunkErz extends Serializable {

    /**
     * Erzeugt eine neue einstellige Funktion mit der angegebenen Nummer und
     * der übergebenen Funktion als Argument.
     *
     * @param nummer       Die Nummer der einstelligen Funktion, die erzeugt
     *                     werden soll.
     * @param argFunktion  Die Argumentfunktion der zu erzeugenden Funktion.
     *
     * @return  Die erzeugte einstellige Funktion.
     */
    public EinStellIntFunk einStelligeFunktion(int nummer,
                                               ErzeugbareIntFunk argFunktion);
}

