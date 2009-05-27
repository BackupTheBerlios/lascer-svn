/*
 * Dateiname      : KonkretesProblemAusgabe.java
 * Letzte �nderung: 19. Juli 2006
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


package mengenUeberdeckung.tests.konkret;

import mengenUeberdeckung.allgemein.ItmFamilie;

/**
 * Bietet Methoden zur Ausgabe eines konkreten Problems oder einer L�sung.
 *
 * @author  Dietmar Lippold
 */
public class KonkretesProblemAusgabe {

    /**
     * Gibt ein Problem aus.
     *
     * @param problem  Das auszugebende Problem.
     */
    public static void ausgabeProblem(ItmFamilie problem) {

        System.out.println("Ausgew�hltes Problem:");
        System.out.println(problem.toString());

        System.out.println();
        System.out.println();
        System.out.println("Statistik des Problems:");
        System.out.print(problem.statistik(1));
    }

    /**
     * Gibt eine �berdeckung aus.
     *
     * @param ueberdeckung  Die auszugebende �berdeckung.
     */
    public static void ausgabeUeberdeckung(ItmFamilie ueberdeckung) {

        System.out.println();
        System.out.println();
        System.out.println("Die erzeugte �berdeckung:");
        System.out.println(ueberdeckung.toString());

        System.out.println();
        System.out.println();
        System.out.println("Statistik der erzeugten �berdeckung:");
        System.out.print(ueberdeckung.statistik(1));
        System.out.println();
    }
}

