/*
 * Dateiname      : IParser.java
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

import java.io.InputStream;

import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * This interface must be implemented by all Parsers.
 *
 * @author  Michael Wohlfart
 */
public interface IParser {

    /**
     *
     * @param input iput stream to be parsed by this implementation
     *
     * @return an instance of a SCP Problem
     *
     *
     * @throws ParseException if there is something wrong with the syntax
     */
    ItmFamilie doParse(InputStream input) throws ParseException;

    /**
     * @return the number of identical sets
     */
    int getDoubleSets();
}

