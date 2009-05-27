/*
 * Dateiname      : NominalPraedTest.java
 * Letzte Änderung: 30. Mai 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2006
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


package lascer.tests;

import lascer.problemdaten.Beispieldaten;
import lascer.praedikate.erzeugung.NomWertPraedErzeugung;
import lascer.konzepte.Konzept;
import lascer.konzepte.KonzeptErzeugungFacade;

/**
 * Testet das System mit einen anzugebenden Datensatz mit nominalen
 * Attributen.
 *
 * @author  Dietmar Lippold
 */
public class NominalPraedTest {

    /**
     * Führt das Testprogramm aus.
     *
     * @param args  Ein Array dem Kommandozeilenparameter, der die Nummer des
     *              zu verwendenden Datensatzes angibt.
     */
    public static void main(String[] args) {
        KonzeptErzeugungFacade konzeptErzeugung;
        NomWertPraedErzeugung  praedErzeugung;
        Beispieldaten          datensatz;

        if (args.length != 1) {
            System.err.println("Fehler: Es wurde keine Nummer eines Datensatzes"
                               + " auf der Komandozeile angegeben");
            System.exit(-1);
        }

        // Datensatz holen und Konzeptverwaltung erzeugen.
        int datensatzNr = Integer.parseInt(args[0]);
        if (datensatzNr >= NominalDatensaetze.anzahlDatensaetze()) {
            System.out.println("Es gibt keinen Datensatz " + datensatzNr);
            System.exit(-1);
        }
        datensatz = NominalDatensaetze.gibDatensatz(datensatzNr);

        // Erzeugung der nominalen Prädikat-Erzeuger.
        praedErzeugung = new NomWertPraedErzeugung(datensatz, -1);

        // Erzeugung der Konzepte.
        konzeptErzeugung = new KonzeptErzeugungFacade(datensatz, 1);
        konzeptErzeugung.erzeugeKonzepte(praedErzeugung);

        Konzept ergebnis = konzeptErzeugung.besteFormel();
        System.out.println("Ergebnis : " + ergebnis.toString());
        System.out.println("Statistik:");
        System.out.println(ergebnis.statistik());
    }
}

