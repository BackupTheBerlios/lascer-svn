/*
 * Dateiname      : ArffDateiEinlesen.java
 * Letzte Änderung: 24. August 2007
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2007
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


package lascer.problemdaten;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;
import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import weka.coreExtended.Instance;
import weka.coreExtended.Instances;
import weka.coreExtended.BasicAttribute;

import lascer.problemdaten.attribute.StringAttribut;
import lascer.problemdaten.attribute.NominalAttribut;
import lascer.problemdaten.attribute.RealAttribut;
import lascer.problemdaten.attribute.IntAttribut;

/**
 * Liest eine Datei im arff-Format ein.
 *
 * @author  Dietmar Lippold
 */
public class ArffDateiEinlesen {

    /**
     * Liefert das Beispiel zur übergebenen Instanz.
     *
     * @param instanz           Die Instanz, zu der das Beispiel geliefert
     *                          werden soll.
     * @param attribListe       Die Liste der Attribute des Beispiels.
     * @param attributSammlung  Die Attributsammlung zum Beispiel.
     *
     * @return  Das Beispiel zur übergebenen Instanz.
     */
    public static Beispiel instanzBeispiel(Instance instanz, List attribListe,
                                           AttributSammlung attributSammlung) {
        ArrayList      stringWertListe, nominalWertListe;
        ArrayList      realObjListe, intObjListe;
        Iterator       attribIter;
        String[]       stringWerte;
        String[]       nominalWerte;
        float[]        realWerte;
        int[]          intWerte;
        BasicAttribute basicAttrib;
        float          floatWert;
        int            intWert;

        stringWertListe = new ArrayList();
        nominalWertListe = new ArrayList();
        realObjListe = new ArrayList();
        intObjListe = new ArrayList();

        attribIter = attribListe.iterator();
        while (attribIter.hasNext()) {
            basicAttrib = (BasicAttribute) attribIter.next();

            if (basicAttrib.isString()) {

                if (instanz.isMissing(basicAttrib)) {
                    stringWertListe.add(Konstanten.UNBEKANNT_STRING);
                } else {
                    int wertIndex = (int) instanz.value(basicAttrib);
                    stringWertListe.add(basicAttrib.value(wertIndex));
                }

            } else if (basicAttrib.isNominal()) {

                if (instanz.isMissing(basicAttrib)) {
                    nominalWertListe.add(Konstanten.UNBEKANNT_NOMINAL);
                } else {
                    int wertIndex = (int) instanz.value(basicAttrib);
                    nominalWertListe.add(basicAttrib.value(wertIndex));
                }

            } else if (basicAttrib.isReal()) {

                if (instanz.isMissing(basicAttrib)) {
                    floatWert = Konstanten.UNBEKANNT_REAL;
                } else {
                    floatWert = (float) instanz.value(basicAttrib);
                }
                realObjListe.add(new Float(floatWert));

            } else if (basicAttrib.isInteger()) {

                if (instanz.isMissing(basicAttrib)) {
                    intWert = Konstanten.UNBEKANNT_INT;
                } else {
                    intWert = (int) instanz.value(basicAttrib);
                }
                intObjListe.add(new Integer(intWert));

            } else {

                throw new RuntimeException("Attribut von unbekanntem Typ: "
                                           + basicAttrib);

            }
        }

        stringWerte = (String[]) stringWertListe.toArray(new String[0]);
        nominalWerte = (String[]) nominalWertListe.toArray(new String[0]);

        realWerte = new float[realObjListe.size()];
        for (int i = 0; i < realWerte.length; i++) {
            realWerte[i] = ((Float) realObjListe.get(i)).floatValue();
        }

        intWerte = new int[intObjListe.size()];
        for (int i = 0; i < intWerte.length; i++) {
            intWerte[i] = ((Integer) intObjListe.get(i)).intValue();
        }

        return (new Beispiel(attributSammlung, stringWerte,
                             nominalWerte, realWerte, intWerte));
    }

    /**
     * Liefert zum übergebenen Instanzen-Objekt die enthaltenen Daten.
     *
     * @param instDaten          Das zu verarbeitende Instanzen-Objekt.
     * @param unbekannteWertBsp  Gibt an, ob eingelesene Beispiele mit
     *                           unbekannten Attributwerten verwendet werden
     *                           sollen.
     *
     * @return  Die Daten des Instanzen-Objekts.
     *
     * @throws UnsupportedDataException  Die Art der Daten wird nicht
     *                                   unterstützt.
     */
    public static Beispieldaten beispieldaten(Instances instDaten,
                                              boolean unbekannteWertBsp)
        throws UnsupportedDataException {

        Beispieldaten    beispieldaten;
        AttributSammlung attributSammlung;
        ArrayList        attributListe;
        ArrayList        nominalArffAttribute;
        BasicAttribute   basicAttrib;
        BasicAttribute   classAttrib;
        Instance         instanz;
        Beispiel         beispiel;
        String           attribName;

        // Datei-Header verarbeiten.
        attributListe = new ArrayList();
        nominalArffAttribute = new ArrayList();
        attributSammlung = new AttributSammlung();
        Enumeration attribEnum = instDaten.enumerateBasicAttributes();
        while (attribEnum.hasMoreElements()) {
            basicAttrib = (BasicAttribute) attribEnum.nextElement();
            attributListe.add(basicAttrib);
            attribName = basicAttrib.name();
            if (basicAttrib.isInteger()) {
                attributSammlung.addIntAttribut(new IntAttribut(attribName));
            } else if (basicAttrib.isReal()) {
                attributSammlung.addRealAttribut(new RealAttribut(attribName));
            } else if (basicAttrib.isNominal()) {
                nominalArffAttribute.add(basicAttrib);
            } else if (basicAttrib.isString()) {
                attributSammlung.addStringAttribut(new StringAttribut(attribName));
            } else {
                System.err.println("WARNUNG: Unbekanntes Attribut: "
                                   + basicAttrib);
            }
        }

        // class Attribut ermitteln.
        classAttrib = null;
        for (int i = 0; i < nominalArffAttribute.size(); i++) {
            basicAttrib = (BasicAttribute) nominalArffAttribute.get(i);
            if (basicAttrib.name().equalsIgnoreCase("class")) {
                if (classAttrib != null) {
                    throw new UnsupportedDataException("Es ist mehr als ein"
                                                       + " class Attribut vorhanden");
                } else {
                    if (basicAttrib.numValues() != 2) {
                        throw new UnsupportedDataException("Das class Attribut hat"
                                                           + " nicht die Werte"
                                                           + " true und false");
                    } else {
                        String attribWert0 = basicAttrib.value(0);
                        String attribWert1 = basicAttrib.value(1);
                        if (!attribWert0.equals("true") && !attribWert0.equals("false")
                            || !attribWert1.equals("true") && !attribWert1.equals("false")) {

                            throw new UnsupportedDataException("Das class Attribut hat"
                                                               + " nicht die Werte"
                                                               + " true und false");
                        } else {
                            classAttrib = basicAttrib;
                        }
                    }
                }
            }
        }
        if (classAttrib == null) {
            throw new UnsupportedDataException("Es ist kein class Attribut vorhanden");
        }
        nominalArffAttribute.remove(classAttrib);
        attributListe.remove(classAttrib);

        // Nach der Entfernung des class-Attributs die nominalen Attribute
        // aufnehmen.
        for (int aNr = 0; aNr < nominalArffAttribute.size(); aNr++) {
            basicAttrib = (BasicAttribute) nominalArffAttribute.get(aNr);
            attribName = basicAttrib.name();
            int werteAnz = basicAttrib.numValues();
            String[] werte = new String[werteAnz];
            for (int i = 0; i < werteAnz; i++) {
                werte[i] = basicAttrib.value(i);
            }
            attributSammlung.addNominalAttribut(new NominalAttribut(attribName, werte));
        }
        beispieldaten = new Beispieldaten(instDaten.relationName(), attributSammlung);

        // Alle Beispiele ohne Klassenangabe löschen.
        instDaten.setBasicClass(classAttrib);
        instDaten.deleteWithMissingClass();

        if (!unbekannteWertBsp) {
            // Alle Beispiele mit unbekanntem Attributwert löschen.
            for (int aNr = 0; aNr < instDaten.numBasicAttributes(); aNr++) {
                instDaten.deleteWithMissingBasicAttribute(aNr);
            }
        }

        // Beispiele verarbeiten.
        Enumeration bspEnum = instDaten.enumerateBasicInstances();
        while (bspEnum.hasMoreElements()) {
            instanz = (Instance) bspEnum.nextElement();

            // Beispiel erzeugen.
            beispiel = instanzBeispiel(instanz, attributListe, attributSammlung);

            // Klasse des Beispiels ermitteln.
            int wertIndex = (int) instanz.value(classAttrib);
            boolean positiv = classAttrib.value(wertIndex).equals("true");

            beispieldaten.beispielAufnehmen(beispiel, positiv);
        }

        return beispieldaten;
    }

    /**
     * Liefert zur Datei mit dem übergebenen Namen die enthaltenen Daten.
     *
     * @param dateiname          Der Name der einzulesenden Datei.
     * @param unbekannteWertBsp  Gibt an, ob eingelesene Beispiele mit
     *                           unbekannten Attributwerten verwendet werden
     *                           sollen.
     *
     * @return  Die Daten der eingelesenen Datei.
     *
     * @throws FileNotFoundException     Eine Datei mit dem angegebenen Namen
     *                                   wurde nicht gefunden.
     * @throws IOException               Die Datei konnte nicht ganz gelesen
     *                                   werden.
     * @throws UnsupportedDataException  Die Art der Daten wird nicht
     *                                   unterstützt.
     */
    public static Beispieldaten beispieldaten(String dateiname,
                                              boolean unbekannteWertBsp)
        throws FileNotFoundException, IOException, UnsupportedDataException {

        Reader    reader;
        Instances datDaten = null;

        // Datei einlesen.
        reader = new BufferedReader(new FileReader(dateiname));
        datDaten = new Instances(reader);

        return beispieldaten(datDaten, unbekannteWertBsp);
    }
}

