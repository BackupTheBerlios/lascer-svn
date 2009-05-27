/*
 * Dateiname      : SolutionSetGeneration.java
 * Letzte �nderung: 13. Juli 2005
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


package mengenUeberdeckung.iteratedGreedyCovering;

import java.util.Collection;
import java.util.HashSet;

import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Liefert zu einer vorgegebenen L�sung eines �berdeckungsproblems eine Menge
 * weiterer L�sungen zu diesem �berdeckungsproblem.
 *
 * @author  Dietmar Lippold
 */
public interface SolutionSetGeneration {

    /**
     * Liefert zu einer vorgegebenen L�sung eines �berdeckungsproblems eine
     * Menge von Instanzen von <CODE>HashSet</CODE>, die weitere L�sungen
     * zum �berdeckungsproblem darstellen. Jedes HashSet enth�lt dabei die
     * Teilmengen einer <CODE>ItmFamilie</CODE>, die eine L�sung ist. Ein
     * HashSet mit den Teilmengen der �bergebenen <CODE>ItmFamilie</CODE> ist
     * in der Regel nicht enthalten.
     *
     * @param problemItms  Die Teilmengen des urspr�nglichen SCP-Problems.
     * @param actualCover  Die aktuelle L�sung.
     *
     * @return  Eine Menge Instanzen von <CODE>HashSet</CODE>, die weitere
     *          L�sungen zum �berdeckungsproblem darstellen.
     */
    public HashSet generatedSet(Collection problemItms, ItmFamilie actualCover);
}

