/*
 * Dateiname      : ParseException.java
 * Letzte Änderung: 1. Januar 2005
 * Autoren        : Michael Wohlfart
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2005
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


package mengenUeberdeckung.tests.bib.parser;

import java.io.IOException;

/**
 * Exception for any kind of parse problems.
 *
 * @author Michael Wohlfart
 */
public class ParseException extends IOException {

    /**
     * parameterless constructor
     *
     */
    public ParseException() {
        super();
    }

    /**
     * constructor with error message
     *
     * @param string erros message
     */
    public ParseException(String string) {
        super(string);
    }
}

