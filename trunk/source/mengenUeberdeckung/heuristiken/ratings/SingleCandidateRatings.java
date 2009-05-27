/*
 * Dateiname      : SingleCandidateRatings.java
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
import java.util.Iterator;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Dient der Bewertung einer einzelnen Teilmenge zur Aufnahme in oder zur
 * Entfernung aus einer partiellen Überdeckung.
 *
 * @author  Dietmar Lippold
 */
public abstract class SingleCandidateRatings implements CandidateRatings {

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
    public float[] wAdd(List teilmengen, ItmFamilie partialCover) {
        IndexTeilmenge teilmenge;
        float[]        addValues;
        Iterator       iterator;
        int            itmNr;

        addValues = new float[teilmengen.size()];
        itmNr = 0;
        iterator = teilmengen.iterator();
        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge) iterator.next();
            addValues[itmNr] = wAdd(teilmenge, partialCover);
            itmNr++;
        }
        return addValues;
    }

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
    public float[] wRmv(List teilmengen, ItmFamilie partialCover) {
        IndexTeilmenge teilmenge;
        float[]        rmvValues;
        Iterator       iterator;
        int            itmNr;

        rmvValues = new float[teilmengen.size()];
        itmNr = 0;
        iterator = teilmengen.iterator();
        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge) iterator.next();
            rmvValues[itmNr] = wRmv(teilmenge, partialCover);
            itmNr++;
        }
        return rmvValues;
    }

    /**
     * Liefert die Bewertung einer Teilmenge zur Ergänzung der übergegebenen
     * partielle Überdeckung. Umso größer der Wert ist, umso besser ist die
     * Teilmenge bewertet, d.h. umso eher sollte sie zu der partiellen
     * Überdeckung hinzugenommen werden.
     *
     * @param  teilmenge     Die Teilmenge, die bewertet werden soll.
     * @param  partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                       Teilmenge bewertet werden soll.
     *
     * @return  Die Bewertung der Teilmenge. Diese kann positiv, Null oder
     *          negativ sein.
     *
     * @throws IllegalArgumentException  Die übergebene Teilmenge ist schon
     *                                   in <code>partialCover</code>
     *                                   enthalten.
     */
    public abstract float wAdd(IndexTeilmenge teilmenge, ItmFamilie partialCover);

    /**
     * Liefert die Bewertung einer Teilmenge zur Entfernung aus der
     * übergegebenen partiellen Überdeckung. Umso größer der Wert ist, umso
     * schlechter ist die Teilmenge bewertet, d.h. umso eher sollte sie aus
     * der partiellen Überdeckung entfernt werden.
     *
     * @param  teilmenge     Die Teilmenge, die bewertet werden soll.
     * @param  partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                       Teilmenge bewertet werden soll.
     *
     * @return  Die Bewertung der Teilmenge. Diese kann positiv, Null oder
     *          negativ sein.
     *
     * @throws IllegalArgumentException  Die übergebene Teilmenge ist schon
     *                                   in <code>partialCover</code>
     *                                   enthalten.
     */
    public abstract float wRmv(IndexTeilmenge teilmenge, ItmFamilie partialCover);
}

