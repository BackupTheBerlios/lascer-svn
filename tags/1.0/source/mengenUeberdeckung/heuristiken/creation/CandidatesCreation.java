/*
 * Dateiname      : CandidatesCreation.java
 * Letzte �nderung: 23. August 2005
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut f�r Intelligente Systeme Universit�t Stuttgart,
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


package mengenUeberdeckung.heuristiken.creation;

import java.util.HashSet;
import java.util.Collection;

import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Liefert Mengen von Kandidaten von Teilmengen zur Aufnahme in oder zur
 * Entfernung aus einer partiellen �berdeckung.
 *
 * @author  Dietmar Lippold
 */
public interface CandidatesCreation {

    /**
     * Liefert die m�glichen Kandidaten aus einer gegebenen Familie von
     * Teilmengen zur Erg�nzung einer gegebenen partielle �berdeckung.
     *
     * @param problemItms        Die Kandidaten der Teilmengen.
     * @param partialCover       Die schon vorhandene partielle �berdeckung.
     * @param bestKnownSolution  Die bisher beste L�sung des Gesamtproblems.
     *                           Der Wert kann <CODE>null</CODE> sein, wenn
     *                           z.B. noch keine L�sung bekannt ist.
     *
     * @return  Familie, welche die Kandidaten enth�lt.
     */
    public HashSet candidatesForAdd(Collection problemItms,
                                    ItmFamilie partialCover,
                                    ItmFamilie bestKnownSolution);

    /**
     * Liefert die m�glichen Kandidaten aus einer partiellen �berdeckung mit
     * Bezug auf eine gegebene Familie von Teilmengen zur Entfernung aus der
     * partiellen �berdeckung.
     *
     * @param problemItms        Die Kandidaten der Teilmengen.
     * @param partialCover       Die schon vorhandene partielle �berdeckung.
     * @param bestKnownSolution  Die bisher beste L�sung des Gesamtproblems.
     *                           Der Wert kann <CODE>null</CODE> sein, wenn
     *                           z.B. noch keine L�sung bekannt ist.
     *
     * @return  Familie, welche die Kandidaten enth�lt.
     */
    public HashSet candidatesForRmv(Collection problemItms,
                                    ItmFamilie partialCover,
                                    ItmFamilie bestKnownSolution);
}

