/*
 * Dateiname      : FamilyShrinking.java
 * Letzte �nderung: 27. Juli 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut f�r Intelligente Systeme Universit�t Stuttgart,
 *                  2006
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


package mengenUeberdeckung.heuristiken.shrinking;

import java.util.Collection;

import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Dient der Verkleinerung einer �berdeckung, also der Erzeugung einer
 * Teil-�berdeckung.
 *
 * @author  Dietmar Lippold
 */
public interface FamilyShrinking {

    /**
     * Liefert zu der �bergebenen �berdeckung eine neu erzeugte echte
     * Teil-�berdeckung, die also nur eine Auswahl der Teilmengen der
     * �bergebenen �berdeckung enth�lt.
     *
     * @param cover            Die �berdeckung, zu der eine Familie mit
     *                         weniger Teilmengen geliefert werden soll.
     * @param problem          Das Problem, zu dem die �berdeckung geh�rt.
     * @param fixedCandidates  Teilmengen, die aus der �berdeckung nicht
     *                         entfernt werden sollen.
     *
     * @return  Eine Familie, die eine echte Teil-Familie der �bergebenen
     *          �berdeckung ist.
     */
    public ItmFamilie partialCover(ItmFamilie cover, ItmFamilie problem,
                                   Collection fixedCandidates);
}

