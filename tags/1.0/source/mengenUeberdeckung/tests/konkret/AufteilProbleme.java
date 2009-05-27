/*
 * Dateiname      : AufteilProbleme.java
 * Letzte Änderung: 06. Januar 2005
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


package mengenUeberdeckung.tests.konkret;

import java.util.ArrayList;

import mengenUeberdeckung.allgemein.ItmFamilie;
import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Die Klasse enthält eine Reihe von konkreten Aufteilungsproblemen.
 *
 * @author  Dietmar Lippold
 */
public class AufteilProbleme {

    /**
     * Eine Liste der vorhandenen Probleme.
     */
    private static ArrayList probleme = new ArrayList();

    /**
     * Ein einzelnes Problem. Wird nur bei der Initialisierung verwendet und
     * hat danach den Wert <CODE>null</CODE>.
     */
    private static ItmFamilie problem;

    /**
     * Eine einzelne Teilmenge. Wird nur bei der Initialisierung verwendet und
     * hat danach den Wert <CODE>null</CODE>.
     */
    private static IndexTeilmenge teilmenge;

    // Definition der Probleme und Aufnahme in die Problemliste.
    static {
        // Neues Problem.
        problem = new ItmFamilie(4);

        teilmenge = new IndexTeilmenge(4, 2);
        teilmenge.indicesAufnehmen(new int[] {0, 2, 3});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(4, 3);
        teilmenge.indicesAufnehmen(new int[] {1});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(4, 6);
        teilmenge.indicesAufnehmen(new int[] {2, 3});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(4, 1);
        teilmenge.indicesAufnehmen(new int[] {0, 1});
        problem.teilmengeHinzufuegen(teilmenge);

        probleme.add(problem);

        // Neues Problem.
        problem = new ItmFamilie(6);

        teilmenge = new IndexTeilmenge(6, 32);
        teilmenge.indicesAufnehmen(new int[] {0, 1, 2, 3, 4});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(6, 4);
        teilmenge.indicesAufnehmen(new int[] {0, 1, 2});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(6, 2);
        teilmenge.indicesAufnehmen(new int[] {3, 5});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(6, 1);
        teilmenge.indicesAufnehmen(new int[] {0, 2});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(6, 24);
        teilmenge.indicesAufnehmen(new int[] {2, 3, 4, 5});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(6, 45);
        teilmenge.indicesAufnehmen(new int[] {0, 1, 2, 3, 4, 5});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(6, 4);
        teilmenge.indicesAufnehmen(new int[] {3, 4, 5});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(6, 2);
        teilmenge.indicesAufnehmen(new int[] {0, 1});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(6, 28);
        teilmenge.indicesAufnehmen(new int[] {0, 1, 2, 3});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(6, 35);
        teilmenge.indicesAufnehmen(new int[] {1, 3, 4, 5});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(6, 1);
        teilmenge.indicesAufnehmen(new int[] {3, 4});
        problem.teilmengeHinzufuegen(teilmenge);

        teilmenge = new IndexTeilmenge(6, 40);
        teilmenge.indicesAufnehmen(new int[] {1, 2, 3, 4, 5});
        problem.teilmengeHinzufuegen(teilmenge);

        probleme.add(problem);

        // Attribute löschen.
        teilmenge = null;
        problem = null;
    }

    /**
     * Liefert die Anzahl der vorhandenen Probleme.
     *
     * @return  Die Anzahl der vorhandenen Probleme.
     */
    public static int anzahlProbleme() {
        return probleme.size();
    }

    /**
     * Liefert ein Problem mit einer bestimmten Nummer.
     *
     * @param nr  Die Nummer des Problems, das geliefert werden soll. Sie
     *            muß gleich oder größer Null und Eins kleiner als die Anzahl
     *            der vorhandenen Probleme sein.
     *
     * @return  Das Problem mit der angegebenen Nummer.
     *
     * @throws IndexOutOfBoundsException  Ein Problem mit der angegebenen
     *                                    Nummer ist nicht vorhanden.
     */
    public static ItmFamilie gibProblem(int nr) {
        return ((ItmFamilie) probleme.get(nr));
    }

    /**
     * Gibt die Anzahl der vorhandenen Probleme aus.
     *
     * @param args  Ein Array ohne Kommandozeilenparameter.
     */
    public static void main(String[] args) {
        System.out.println("Es sind " + probleme.size() + " Probleme"
                           + " vorhanden.");
    }
}

