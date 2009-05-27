/*
 * Dateiname      : SolutionSetGeneration.java
 * Letzte Änderung: 13. Juli 2005
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


package mengenUeberdeckung.iteratedGreedyCovering;

import java.util.Collection;
import java.util.HashSet;

import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Liefert zu einer vorgegebenen Lösung eines Überdeckungsproblems eine Menge
 * weiterer Lösungen zu diesem Überdeckungsproblem.
 *
 * @author  Dietmar Lippold
 */
public interface SolutionSetGeneration {

    /**
     * Liefert zu einer vorgegebenen Lösung eines Überdeckungsproblems eine
     * Menge von Instanzen von <CODE>HashSet</CODE>, die weitere Lösungen
     * zum Überdeckungsproblem darstellen. Jedes HashSet enthält dabei die
     * Teilmengen einer <CODE>ItmFamilie</CODE>, die eine Lösung ist. Ein
     * HashSet mit den Teilmengen der übergebenen <CODE>ItmFamilie</CODE> ist
     * in der Regel nicht enthalten.
     *
     * @param problemItms  Die Teilmengen des ursprünglichen SCP-Problems.
     * @param actualCover  Die aktuelle Lösung.
     *
     * @return  Eine Menge Instanzen von <CODE>HashSet</CODE>, die weitere
     *          Lösungen zum Überdeckungsproblem darstellen.
     */
    public HashSet generatedSet(Collection problemItms, ItmFamilie actualCover);
}

