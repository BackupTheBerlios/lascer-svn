/*
 * Dateiname      : ResultSet3.java
 * Letzte Änderung: 1. Januar 2005
 * Autoren        : Dietmar Lippold, Michael Wohlfart
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


package mengenUeberdeckung.tests.bib;

/**
 * Diese Klasse repräsentiert einen Datensatz aus drei Werten zur Darstellung
 * in einer Excel-Tabelle.
 *
 * @author  Dietmar Lippold, Michael Wohlfart
 */
public class ResultSet3 {

    /**
     * Der Name des Problems, d.h. in der Regel der entsprechenden Datei,
     * zu dem die angegebenen Werte gehören.
     */
    private String problemName;

    /**
     * Die Kosten der Lösung zum angegebenen Problem.
     */
    private float solutionCost;

    /**
     * Die Laufzeit in Millisekunden zur Ermittlung der Lösung.
     */
    private long runtime;

    /**
     * Erzeugt eine Instanz zu den übergebenen Werten.
     *
     * @param problemName   Der Name des Problems bzw. des Datensatzes.
     * @param solutionCost  Die Kosten der ermittelten Lösung des Problems.
     * @param runtime       Die Laufzeit in Millisekunden zur Ermittlung der
     *                      Lösung.
     */
    public ResultSet3(String problemName, float solutionCost, long runtime) {
        this.problemName = problemName;
        this.solutionCost = solutionCost;
        this.runtime = runtime;
    }

    /**
     * Liefert den Problemnamen.
     *
     * @return  Name des Problems.
     */
    public String getProblemName() {
        return problemName;
    }

    /**
     *Liefert die Kosten der Lösung.
     *
     * @return  Kosten der Lösung.
     */
    public float getSolutionCost() {
        return solutionCost;
    }

    /**
     * Liefert die Laufzeit in Millisekunden.
     *
     * @return  Laufzeit in Millisekunden.
     */
    public long getRuntime()  {
        return runtime;
    }
}

