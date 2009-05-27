/*
 * Dateiname      : CandidateSelection.java
 * Letzte Änderung: 17. Juli 2005
 * Autoren        : Dietmar Lippold
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


package mengenUeberdeckung.heuristiken.selection;

import java.util.Collection;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Dient der Auswahl der günstigsten Teilmenge aus einer Familie von
 * Kandidaten von Teilmengen zur Aufnahme in oder zur Entfernung aus einer
 * partiellen Überdeckung.
 *
 * @author  Dietmar Lippold
 */
public interface CandidateSelection {

    /**
     * Liefert den günstigsten Kandidaten aus einer Familie von Teilmengen
     * zur Ergänzung einer gegebenen partielle Überdeckung.
     *
     * @param candidates    Die Teilmengen, unter denen eine ausgewählt werden
     *                      soll.
     * @param partialCover  Die schon vorhandene partielle Überdeckung.
     *
     * @return  Die günstigste Teilmenge zur Ergänzung der übergegebenen
     *          partielle Überdeckung
     */
    public IndexTeilmenge selectAdd(Collection candidates, ItmFamilie partialCover);

    /**
     * Liefert den günstigsten Kandidaten aus einer Familie von Teilmengen
     * zur Entfernung aus der partiellen Überdeckung.
     *
     * @param candidates    Die Teilmengen, unter denen eine ausgewählt werden
     *                      soll.
     * @param partialCover  Die schon vorhandene partielle Überdeckung.
     *
     * @return  Die günstigste Teilmenge zur Entfernung aus der übergegebenen
     *          partielle Überdeckung
     */
    public IndexTeilmenge selectRmv(Collection candidates, ItmFamilie partialCover);
}

