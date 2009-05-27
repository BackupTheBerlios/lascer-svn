/*
 * Dateiname      : MarchSteenRating.java
 * Letzte Änderung: 25. Juli 2005
 * Autoren        : Dietmar Lippold, Rene Berleong
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

import java.util.Iterator;

import mengenUeberdeckung.allgemein.IndexTeilmenge;
import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Dient der Bewertung einer Teilmengen zur Aufnahme in oder zur Entfernung
 * aus einer partiellen Überdeckung entsprechend dem Paper von Elena Marchiori
 * und Adri Steenbeek (1998). Die dort beschriebenen Bewertungen wurden jedoch
 * zusätzlich durch die Kosten einer Teilmenge beim Hinzufügen oder Entfernen
 * dividiert, so daß auch non-unicost-Probleme behandelt werden können.
 *
 * @author  Dietmar Lippold, Rene Berleong
 */
public class MarchSteenRating extends SingleCandidateRatings {

    /**
     * Nummer der zur Wahl stehenden Add-Strategien
     * (<code>w_add1</code> = 1 oder <code>w_add2</code> = 2).
     * Bei Hinzufügen neuer Strategien müssen <code>wAdd</code> und der
     * Konstruktor angepasst werden.
     */
    private int addStrategyNr;

    /**
     * Nummer der zur Wahl stehenden Remove-Strategien
     * (<code>w_rmv1</code> = 1 oder <code>w_rmv2</code> = 2).
     * Bei Hinzufügen neuer Strategien müssen <code>wRmv</code> und der
     * Konstruktor angepasst werden.
     */
    private int rmvStrategyNr;

    /**
     * Gibt den Wert für die Konstante <code>add_constant</code> an.
     */
    private float addConstant;

    /**
     * Erstellt eine neue Instanz der Klasse.
     *
     * @param addStrategyNr  Nummer der Add-Strategie
     * @param rmvStrategyNr  Nummer der Remove-Strategie
     * @param unicost        Gibt an, ob das Verfahren für ein unicost-Problem
     *                       verwendet werden soll. Diese Angabe ist für die
     *                       erste Strategie ohne Bedeutung.
     */
    public MarchSteenRating(int addStrategyNr, int rmvStrategyNr,
                            boolean unicost) {

        if (addStrategyNr < 1 || addStrategyNr > 2) {
            throw new IllegalArgumentException("Unzulässiger Wert für"
                + " Parameter addStrategyNr: " + addStrategyNr);
        }
        if (rmvStrategyNr < 1 || rmvStrategyNr > 2) {
            throw new IllegalArgumentException("Unzulässiger Wert für"
                + " Parameter rmvStrategyNr: " + rmvStrategyNr);
        }

        this.addStrategyNr = addStrategyNr;
        this.rmvStrategyNr = rmvStrategyNr;
        if (unicost) {
            addConstant = Konstanten.UNICOST_ADD_CONSTANT;
        } else {
            addConstant = Konstanten.MULTICOST_ADD_CONSTANT;
        }
    }

    /**
     * Berechnet den "add_value" mit Verfahren 1.
     *
     * @param teilmenge     Teilmenge, für die der AddValue bestimmt werden
     *                      soll.  Darf nicht in <code>partialCover</code>
     *                      enthalten sein.
     * @param partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                      Teilmenge bewertet werden soll.
     *
     * @return  AddValue
     */
    private float wAdd1(IndexTeilmenge teilmenge, ItmFamilie partialCover) {
        float w  = 0.0f;
        int   xc = 0;

        for (int index = teilmenge.kleinsterEnthaltenerIndex();
             index >= 0;
             index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {
            xc = partialCover.ueberdeckungsHaeufigkeit(index) + 1;
            w = w + (1.0f / (xc * xc));
        }

        return w;
    }

    /**
     * Berechnet den "add_value" mit Verfahren 2.
     *
     * @param teilmenge     Teilmenge, für die der AddValue bestimmt werden
     *                      soll. Darf nicht in <code>partialCover</code>
     *                      enthalten sein.
     * @param partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                      Teilmenge bewertet werden soll.
     *
     * @return  AddValue
     */
    private float wAdd2(IndexTeilmenge teilmenge, ItmFamilie partialCover) {
        IndexTeilmenge pcTeilmenge;
        Iterator       iterator;
        float          w = 0.0f;

        partialCover.teilmengeHinzufuegen(teilmenge);
        iterator = partialCover.iterator();
        while (iterator.hasNext()) {
            pcTeilmenge = (IndexTeilmenge)iterator.next();
            w = w + (partialCover.kostenEntfernen(pcTeilmenge)
                     / (partialCover.anzAlleineUeberdeckt(pcTeilmenge)
                        + addConstant));
        }
        partialCover.teilmengeEntfernen(teilmenge);

        return w;
    }

    /**
     * Liefert die Bewertung einer Teilmenge zur Ergänzung der übergegebenen
     * partielle Überdeckung. Dazu wird zufällig eines von zwei
     * Bewertungsverfahren ausgewählt.
     *
     * @param teilmenge     Die Teilmenge, die bewertet werden soll.
     * @param partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                      Teilmenge bewertet werden soll.
     *
     * @return  Bewertung der Teilmenge, die größer oder gleich Null ist.
     *
     * @throws IllegalArgumentException  Die übergebene Teilmenge ist schon
     *                                   in <code>partialCover</code>
     *                                   enthalten.
     */
    public float wAdd(IndexTeilmenge teilmenge, ItmFamilie partialCover) {
        if (partialCover.enthaelt(teilmenge)) {
            throw new IllegalArgumentException("Teilmenge schon enthalten");
        }

        float kosten = partialCover.kostenHinzufuegen(teilmenge);
        if (kosten == 0) {
            return Float.MAX_VALUE;
        } else {
            if (addStrategyNr == 1) {
                return wAdd1(teilmenge, partialCover) / kosten;
            } else {
                return wAdd2(teilmenge, partialCover) / kosten;
            }
        }
    }

    /**
     * Berechnet den "rmv_value" mit Verfahren 1.
     *
     * @param teilmenge     Teilmenge, für die der RemoveValue bestimmt werden
     *                      soll. Muß in <code>partialCover</code> enthalten
     *                      sein.
     * @param partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                      Teilmenge bewertet werden soll.
     *
     * @return  RemoveValue
     */
    private float wRmv1(IndexTeilmenge teilmenge, ItmFamilie partialCover) {
        float w  = 0.0f;
        int   xc = 0;

        for (int index = teilmenge.kleinsterEnthaltenerIndex();
             index >= 0;
             index = teilmenge.naechsterEnthaltenerIndex(index + 1)) {
            xc = partialCover.ueberdeckungsHaeufigkeit(index);
            w = w + (1.0f / (xc * xc));
        }

        return -w;
    }

    /**
     * Berechnet den "rmv_value" mit Verfahren 2.
     *
     * @param teilmenge     Teilmenge, für die der RemoveValue bestimmt werden
     *                      soll. Muß in <code>partialCover</code> enthalten
     *                      sein.
     * @param partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                      Teilmenge bewertet werden soll.
     *
     * @return  RemoveValue
     */
    private float wRmv2(IndexTeilmenge teilmenge, ItmFamilie partialCover) {
        IndexTeilmenge pcTeilmenge;
        Iterator       iterator;
        float          w = 0.0f;

        partialCover.teilmengeEntfernen(teilmenge);
        iterator = partialCover.iterator();
        while (iterator.hasNext()) {
            pcTeilmenge = (IndexTeilmenge)iterator.next();
            w = w + (partialCover.kostenEntfernen(pcTeilmenge)
                     / (partialCover.anzAlleineUeberdeckt(pcTeilmenge)
                        + addConstant));
        }
        partialCover.teilmengeHinzufuegen(teilmenge);

        return -w;
    }

    /**
     * Liefert die Bewertung einer Teilmenge zur Entfernung aus der
     * übergegebenen partiellen Überdeckung. Dazu wird zufällig eines von zwei
     * Bewertungsverfahren ausgewählt.
     *
     * @param teilmenge     Die Teilmenge, die bewertet werden soll.
     * @param partialCover  Die partielle Überdeckung, in Bezug auf die die
     *                      Teilmenge bewertet werden soll.
     *
     * @return  Bewertung der Teilmenge, die kleiner oder gleich Null ist.
     *
     * @throws IllegalArgumentException  Die übergebene Teilmenge ist schon
     *                                   in <code>partialCover</code>
     *                                   enthalten.
     */
    public float wRmv(IndexTeilmenge teilmenge, ItmFamilie partialCover) {
        if (!(partialCover.enthaelt(teilmenge))) {
            throw new IllegalArgumentException("Teilmenge nicht enthalten");
        }

        float kosten = partialCover.kostenEntfernen(teilmenge);
        if (kosten == 0) {
            return -Float.MAX_VALUE;
        } else {
            if (rmvStrategyNr == 1) {
                return wRmv1(teilmenge, partialCover) / kosten;
            } else {
                return wRmv2(teilmenge, partialCover) / kosten;
            }
        }
    }
}

