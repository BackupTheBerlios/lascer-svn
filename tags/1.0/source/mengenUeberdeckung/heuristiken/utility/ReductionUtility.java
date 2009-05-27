/*
 * Dateiname      : ReductionUtility.java
 * Letzte �nderung: 08. Juli 2005
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


package mengenUeberdeckung.heuristiken.utility;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Diese Klassen implementiert Nutzenfunktionen f�r Instanzen von
 * <CODE>ItmFamilie</CODE> sowie f�r das Hinzuf�gen und Entfernen von
 * Instanzen von <CODE>IndexTeilmenge</CODE>. Die Bewertungen m�ssen dabei
 * kompatibel sein, d.h. es mu� gelten: Die Bewertung einer partiellen
 * �berdeckung plus der Bewertung einer Teilmenge bei Hinzunahme oder
 * Entfernung mu� der Bewertung der resultierenden partiellen �berdeckung
 * entsprechen. Ausgenommen davon ist die Bewertung einer partiellen
 * �berdeckung oder einer Teilmenge mit Kosten von Null.
 *
 * @author  Dietmar Lippold
 */
public interface ReductionUtility {

    /**
     * Berechnet den Nutzen (die Qualit�t) einer partiellen �berdeckung.
     *
     * @param currentCover  Die partielle �berdeckung, deren Nutzen berechnet
     *                      werden soll.
     *
     * @return  Den Nutzen der partiellen �berdeckung.
     */
    public float familyUtility(ItmFamilie currentCover);

    /**
     * Berechnet den potentiellen Nutzen einer Teilmenge bei deren Hinzuf�gen,
     * d.h. wenn die Teilmenge schon in der Familie enthalten ist. Dies ist
     * ein positiver Wert.
     *
     * @param teilmenge     Teilmenge deren Nutzen berechnet wird.
     * @param currentCover  Akutelle �berdeckung des SCP.
     *
     * @return  Nutzen der Teilmenge.
     */
    public float addUtilityPot(IndexTeilmenge teilmenge, ItmFamilie currentCover);

    /**
     * Berechnet den Nutzen einer Teilmenge bei deren Hinzuf�gen. Dies ist ein
     * positiver Wert.
     *
     * @param teilmenge     Teilmenge deren Nutzen berechnet wird.
     * @param currentCover  Akutelle �berdeckung des SCP.
     *
     * @return  Nutzen der Teilmenge.
     *
     * @throws IllegalArgumentException  Die �bergebene Teilmenge ist schon
     *                                   in <code>currentCover</code>
     *                                   enthalten.
     */
    public float addUtility(IndexTeilmenge teilmenge, ItmFamilie currentCover);

    /**
     * Berechnet den Nutzen einer Teilmenge bei deren Entfernung. Dies ist ein
     * negativer Wert.
     *
     * @param teilmenge     Teilmenge, deren Nutzen berechnet wird.
     * @param currentCover  Akutelle �berdeckung des SCP.
     *
     * @return  Nutzen der Teilmenge.
     *
     * @throws IllegalArgumentException  Die �bergebene Teilmenge ist nicht
     *                                   in <code>currentCover</code>
     *                                   enthalten.
     */
    public float rmvUtility(IndexTeilmenge teilmenge, ItmFamilie currentCover);
}

