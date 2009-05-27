/*
 * Dateiname      : NominalDatensaetze.java
 * Letzte �nderung: 04. Juni 2005
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2005
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

import java.util.ArrayList;

import lascer.problemdaten.AttributSammlung;
import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.attribute.NominalAttribut;

/**
 * Die Klasse enth�lt eine Reihe von konkreten Datens�tzen mit nominalen
 * Attributen.
 *
 * @author  Dietmar Lippold
 */
public class NominalDatensaetze {

    /**
     * Eine Liste der vorhandenen Datens�tze.
     */
    private static ArrayList datensaetze = new ArrayList();

    /**
     * Ein einzelner Datensatz. Wird nur bei der Initialisierung verwendet und
     * hat danach den Wert <CODE>null</CODE>.
     */
    private static Beispieldaten beispieldaten;

    /**
     * Die einheitlichen Werte aller nominalen Attribute.
     */
    private static String[] attributWerte = new String[] {"true", "false"};

    /**
     * Die Attribute.
     */
    private static AttributSammlung attributSammlung;

    /**
     * Die niht vorhandenen Werte der String-Attribute.
     */
    private static String[] stringWerte = new String[0];

    /**
     * Die Werte der nominalen Attribute. Wird nur bei der Initialisierung
     * verwendet und hat danach den Wert <CODE>null</CODE>.
     */
    private static String[] nominalWerte;

    /**
     * Die nicht vorhandenen Werte der nicht-ganzzahligen Attribute.
     */
    private static float[] realWerte = new float[0];

    /**
     * Die nicht vorhandenen Werte der ganzzahligen Attribute.
     */
    private static int[] intWerte = new int[0];

    /**
     * Ein Beispiel. Wird nur bei der Initialisierung verwendet und hat danach
     * den Wert <CODE>null</CODE>.
     */
    private static Beispiel beispiel;


    // Definition der Datens�tze und Aufnahme in die entsprechende Liste.
    static {
        // Definition des ersten Datensatzes

        // Definition der nominalen Attribute
        attributSammlung = new AttributSammlung();
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt0", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt1", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt2", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt3", attributWerte));

        // Erzeugung des Datensatzes.
        beispieldaten = new Beispieldaten("testdatensatz0", attributSammlung);

        // Definition des ersten positiven Beispiels.
        nominalWerte = new String[] {"true", "true", "false", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des zweiten positiven Beispiels.
        nominalWerte = new String[] {"false", "true", "true", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des dritten positiven Beispiels.
        nominalWerte = new String[] {"false", "false", "true", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des ersten negativen Beispiels.
        nominalWerte = new String[] {"false", "false", "false", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Neuen Datensatz zur Liste der Datens�tze hinzuf�gen.
        datensaetze.add(beispieldaten);

        // Attribute l�schen.
        beispiel = null;
        nominalWerte = null;
        beispieldaten = null;

        // Definition des zweiten Datensatzes

        // Definition der nominalen Attribute
        attributSammlung = new AttributSammlung();
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt0", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt1", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt2", attributWerte));

        // Erzeugung des Datensatzes.
        beispieldaten = new Beispieldaten("testdatensatz1", attributSammlung);

        // Definition des ersten positiven Beispiels.
        nominalWerte = new String[] {"true", "false", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des zweiten positiven Beispiels.
        nominalWerte = new String[] {"true", "true", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des dritten positiven Beispiels.
        nominalWerte = new String[] {"false", "true", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des vierten positiven Beispiels.
        nominalWerte = new String[] {"false", "false", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des ersten negativen Beispiels.
        nominalWerte = new String[] {"false", "false", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Neuen Datensatz zur Liste der Datens�tze hinzuf�gen.
        datensaetze.add(beispieldaten);

        // Attribute l�schen.
        beispiel = null;
        nominalWerte = null;
        beispieldaten = null;

        // Definition des dritten Datensatzes

        // Definition der nominalen Attribute
        attributSammlung = new AttributSammlung();
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt0", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt1", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt2", attributWerte));

        // Erzeugung des Datensatzes.
        beispieldaten = new Beispieldaten("testdatensatz2", attributSammlung);

        // Definition des ersten positiven Beispiels.
        nominalWerte = new String[] {"true", "true", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des ersten negativen Beispiels.
        nominalWerte = new String[] {"true", "false", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Definition des zweiten negativen Beispiels.
        nominalWerte = new String[] {"true", "true", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Definition des dritten negativen Beispiels.
        nominalWerte = new String[] {"false", "true", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Definition des vierten negativen Beispiels.
        nominalWerte = new String[] {"false", "false", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Neuen Datensatz zur Liste der Datens�tze hinzuf�gen.
        datensaetze.add(beispieldaten);

        // Attribute l�schen.
        beispiel = null;
        nominalWerte = null;
        beispieldaten = null;

        // Definition des vierten Datensatzes

        // Definition der nominalen Attribute
        attributSammlung = new AttributSammlung();
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt0", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt1", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt2", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt3", attributWerte));

        // Erzeugung des Datensatzes.
        beispieldaten = new Beispieldaten("testdatensatz3", attributSammlung);

        // Definition des ersten positiven Beispiels.
        nominalWerte = new String[] {"false", "false", "true", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des zweiten positiven Beispiels.
        nominalWerte = new String[] {"false", "true", "false", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des dritten positiven Beispiels.
        nominalWerte = new String[] {"false", "true", "false", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des vierten positiven Beispiels.
        nominalWerte = new String[] {"true", "false", "true", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des ersten negativen Beispiels.
        nominalWerte = new String[] {"true", "false", "false", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Definition des zweiten negativen Beispiels.
        nominalWerte = new String[] {"true", "true", "false", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Definition des dritten negativen Beispiels.
        nominalWerte = new String[] {"true", "false", "false", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Definition des vierten negativen Beispiels.
        nominalWerte = new String[] {"false", "false", "false", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Neuen Datensatz zur Liste der Datens�tze hinzuf�gen.
        datensaetze.add(beispieldaten);

        // Attribute l�schen.
        beispiel = null;
        nominalWerte = null;
        beispieldaten = null;

        // Definition des f�nften Datensatzes

        // Definition der nominalen Attribute
        attributSammlung = new AttributSammlung();
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt0", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt1", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt2", attributWerte));
        attributSammlung.addNominalAttribut(new NominalAttribut("nomAtt3", attributWerte));

        // Erzeugung des Datensatzen.
        beispieldaten = new Beispieldaten("testdatensatz4", attributSammlung);

        // Definition des ersten positiven Beispiels.
        nominalWerte = new String[] {"true", "true", "true", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des zweiten positiven Beispiels.
        nominalWerte = new String[] {"true", "true", "true", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des dritten positiven Beispiels.
        nominalWerte = new String[] {"false", "false", "true", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des vierten positiven Beispiels.
        nominalWerte = new String[] {"false", "false", "true", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, true);

        // Definition des ersten negativen Beispiels.
        nominalWerte = new String[] {"true", "false", "true", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Definition des zweiten negativen Beispiels.
        nominalWerte = new String[] {"false", "false", "false", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Definition des dritten negativen Beispiels.
        nominalWerte = new String[] {"true", "false", "false", "true"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Definition des vierten negativen Beispiels.
        nominalWerte = new String[] {"true", "true", "false", "false"};
        beispiel = new Beispiel(attributSammlung, stringWerte, nominalWerte,
                                realWerte, intWerte);
        beispieldaten.beispielAufnehmen(beispiel, false);

        // Neuen Datensatz zur Liste der Datens�tze hinzuf�gen.
        datensaetze.add(beispieldaten);

        // Attribute l�schen.
        beispiel = null;
        nominalWerte = null;
        beispieldaten = null;
    }

    /**
     * Liefert die Anzahl der vorhandenen Datens�tze.
     *
     * @return  Die Anzahl der vorhandenen Datens�tze.
     */
    public static int anzahlDatensaetze() {
        return datensaetze.size();
    }

    /**
     * Liefert einen Datensatz mit einer bestimmten Nummer.
     *
     * @param nr  Die Nummer des Datensatzes, der geliefert werden soll. Sie
     *            mu� gleich oder gr��er Null und Eins kleiner als die Anzahl
     *            der vorhandenen Datens�tze sein.
     *
     * @return  Den Datensatz mit der angegebenen Nummer.
     *
     * @throws IndexOutOfBoundsException  Ein Datensatz mit der angegebenen
     *                                    Nummer ist nicht vorhanden.
     */
    public static Beispieldaten gibDatensatz(int nr) {
        return ((Beispieldaten) datensaetze.get(nr));
    }

    /**
     * Gibt die Anzahl der vorhandenen Datens�tze aus.
     *
     * @param args  Ein Array ohne Kommandozeilenparameter.
     */
    public static void main(String[] args) {
        System.out.println("Es sind " + datensaetze.size() + " Datens�tze"
                           + " vorhanden.");
    }
}

