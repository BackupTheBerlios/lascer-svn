/*
 * Dateiname      : SizeMismatchException.java
 * Letzte Änderung: 17. Dezember 2004
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


package mengenUeberdeckung.allgemein;

/**
 * Diese Exception wird ausgelöst, wenn die Größe des übergebenen Objekts
 * unterschiedlich ist zur Größe des Objekts <CODE>this</CODE>.
 *
 * @author  Dietmar Lippold
 */
public class SizeMismatchException extends IllegalArgumentException {

    /**
     * Erzeugt eine SizeMismatchException ohne weitere Angaben.
     */
    public SizeMismatchException() {
        super();
    }

    /**
     * Erzeugt eine SizeMismatchException mit dem angegebenen Text.
     *
     * @param msg  Eine Beschreibung des Fehlers.
     */
    public SizeMismatchException(String msg) {
        super(msg);
    }
}

