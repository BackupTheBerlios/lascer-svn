/*
 * Dateiname      : ChvatalRating.java
 * Letzte Änderung: 25. Juli 2006
 * Autoren        : Michael Wohlfart, Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
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


package mengenUeberdeckung.heuristiken.ratings;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Diese Klasse implementiert im Kern eine Bewertungsfunktion, die definiert
 * ist in<P>
 *
 * Vasek Chvatal :
 * <CITE>A greedy heuristic for set-covering problem</CITE>.
 * Mathematics of Operations Research, Vol 4, 1979<P>
 *
 * Die Anzahl der durch eine Teilmenge neu überdeckten Elemente und die Kosten
 * Teilmenge werden dividiert. Dies wird als <EM>Kosteneffektivität</EM>
 * bezeichnet. Abweichend vom Artikel werden zusätzlich noch die Kosten der
 * Teilmengen, die nach der Hinzunahme einer Teilmenge nicht mehr notwendig
 * sind, berücksichtigt.
 *
 * @author  Michael Wohlfart, Dietmar Lippold
 */
public class ChvatalRating extends SingleCandidateRatings {

    /**
     * Berechnet den Nutzen einer Teilmenge beim Hinzufügen oder beim
     * Entfernen. Das ist die Anzahl der Elemente, die nur in der übergebenen
     * Teilmenge enthalten sind.
     *
     * @param teilmenge     Teilmenge, deren rating berechnet wird.
     * @param currentCover  Akutelle Überdeckung des SCP.
     *
     * @return  Nutzen der Teilmenge.
     */
    private float nutzen(IndexTeilmenge teilmenge, ItmFamilie currentCover) {
        return currentCover.anzAlleineUeberdeckt(teilmenge);
    }

    /**
     * Berechnet das Maximum der Kosten der übergebenen Teilmengen beim
     * getrennten Entfernen aus der übergebenen Familie.
     *
     * @param teilmengen    Teilmengen, deren Kosten beim Entfernen berechnet
     *                      werden soll.
     * @param currentCover  Familie, zu der die Kosten des Entfernens der
     *                      einzelnen Teilmengen berechnet werden sollen.
     *
     * @return  Das Maximum der einzelnen Kosten beim Entfernen der
     *          Teilmengen.
     */
    private float entfernenKostenMax(Collection teilmengen,
                                     ItmFamilie currentCover) {
        IndexTeilmenge teilmenge;
        Iterator       iterator;
        float          kostenMax;

        kostenMax = 0;
        iterator = teilmengen.iterator();
        while (iterator.hasNext()) {
            teilmenge = (IndexTeilmenge) iterator.next();
            kostenMax = Math.max(kostenMax,
                                 currentCover.kostenEntfernen(teilmenge));
        }
        return kostenMax;
    }

    /**
     * Berechnet die Bewertung einer Teilmenge zum Hinzufügen.
     *
     * @param teilmenge     Teilmenge deren rating berechnet wird.
     * @param currentCover  Akutelle Überdeckung des SCP.
     *
     * @return  Bewertung der Teilmenge.
     *
     * @throws IllegalArgumentException  Die übergebene Teilmenge ist schon
     *                                   in <code>currentCover</code>
     *                                   enthalten.
     */
    public float wAdd(IndexTeilmenge teilmenge, ItmFamilie currentCover) {
        Set   redundanteTeilmengen;
        float hinzufuegenKosten;
        float redundanteKosten;
        float gesamtKosten;

        // Die Teilmenge darf noch nicht in der Überdeckung enthalten sein.
        if (currentCover.enthaelt(teilmenge)) {
            throw new IllegalArgumentException("Teilmenge schon enthalten");
        }

        hinzufuegenKosten = currentCover.kostenHinzufuegen(teilmenge);
        redundanteTeilmengen = currentCover.neuNichtNotwendigeTeilmengen(teilmenge);
        redundanteKosten = entfernenKostenMax(redundanteTeilmengen, currentCover);
        gesamtKosten = -hinzufuegenKosten + redundanteKosten;

        if (gesamtKosten < 0) {
            return (gesamtKosten / nutzen(teilmenge, currentCover));
        } else if (gesamtKosten == 0) {
            return (nutzen(teilmenge, currentCover) / teilmenge.groesseGesamtmenge());
	} else {
            return (gesamtKosten * nutzen(teilmenge, currentCover));
        }
    }

    /**
     * Berechnet die Bewertung einer Teilmenge zur Entfernung.
     *
     * @param teilmenge     Teilmenge, deren rating berechnet wird.
     * @param currentCover  Akutelle Überdeckung des SCP.
     *
     * @return  Bewertung für die Teilmenge.
     *
     * @throws IllegalArgumentException  Die übergebene Teilmenge ist nicht
     *                                   in <code>currentCover</code>
     *                                   enthalten.
     */
    public float wRmv(IndexTeilmenge teilmenge, ItmFamilie currentCover) {
        float entfernenKosten;
        float nutzen;

        // Die Teilmenge muss in der Überdeckung enthalten sein.
        if (!currentCover.enthaelt(teilmenge)) {
            throw new IllegalArgumentException("Teilmenge nicht enthalten");
        }

        entfernenKosten = currentCover.kostenEntfernen(teilmenge);
        nutzen = nutzen(teilmenge, currentCover);

        if (nutzen == 0) {
            return (entfernenKosten * ((float) Math.sqrt(Float.MAX_VALUE)));
        } else {
            return (entfernenKosten / nutzen);
        }
    }
}

