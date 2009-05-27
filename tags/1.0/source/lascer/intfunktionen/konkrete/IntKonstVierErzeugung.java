/*
 * Dateiname      : IntKonstVierErzeugung.java
 * Letzte Änderung: 25. Februar 2006
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


package lascer.intfunktionen.konkrete;

import lascer.intfunktionen.arten.NullStellIntFunkErz;
import lascer.intfunktionen.arten.NullStellIntFunk;

/**
 * Erzeugt eine Funktion, die den konstanten Wert Vier liefert.
 *
 * @author  Dietmar Lippold
 */
public class IntKonstVierErzeugung implements NullStellIntFunkErz {

    /**
     * Erzeugt eine nullstellige Funktion, die die Konstante Vier als Wert
     * liefert.
     *
     * @param nummer  Die Nummer der nullstelligen Funktion, die erzeugt
     *                werden soll.
     *
     * @return  Die erzeugte nullstellige Funktion, die immer der Wert Vier
     *          liefert.
     */
    public NullStellIntFunk nullStelligeFunktion(int nummer) {
        return new IntKonstVier(nummer);
    }
}

