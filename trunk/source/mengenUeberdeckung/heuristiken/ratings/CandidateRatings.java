/*
 * Dateiname      : CandidateRatings.java
 * Letzte Änderung: 07. September 2005
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


package mengenUeberdeckung.heuristiken.ratings;

import java.util.List;

import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Dient der Bewertung von Teilmengen zur Aufnahme in oder zur Entfernung aus
 * einer partiellen Überdeckung.
 *
 * @author  Dietmar Lippold
 */
public interface CandidateRatings {

    /**
     * Liefert die Bewertungen von Teilmengen zur Ergänzung der übergegebenen
     * partielle Überdeckung. Umso größer die Werte sind, umso besser sind die
     * Teilmengen bewertet, d.h. umso eher sollten sie zu partialCover
     * hinzugenommen werden.
     *
     * @param  teilmengen    Die Teilmengen, die bewertet werden sollen.
     * @param  partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                       Teilmengen bewertet werden sollen.
     *
     * @return  Die Bewertungen der Teilmengen. Diese können positiv, Null
     *          oder negativ sein.
     *
     * @throws IllegalArgumentException  Eine der übergebenen Teilmengen ist
     *                                   schon in <code>partialCover</code>
     *                                   enthalten.
     */
    public float[] wAdd(List teilmengen, ItmFamilie partialCover);

    /**
     * Liefert die Bewertungen von Teilmengen zur Entfernung aus der
     * übergegebenen partiellen Überdeckung. Umso größer die Werte sind, umso
     * schlechter sind die Teilmengen bewertet, d.h. umso eher sollten sie aus
     * partialCover entfernt werden.
     *
     * @param  teilmengen    Die Teilmengen, die bewertet werden sollen.
     * @param  partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                       Teilmengen bewertet werden sollen.
     *
     * @return  Die Bewertungen der Teilmengen. Diese können positiv, Null
     *          oder negativ sein.
     *
     * @throws IllegalArgumentException  Eine der übergebenen Teilmengen ist
     *                                   schon in <code>partialCover</code>
     *                                   enthalten.
     */
    public float[] wRmv(List teilmengen, ItmFamilie partialCover);
}

