/*
 * Dateiname      : AttributSammlung.java
 * Letzte Änderung: 03. Mai 2006
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


package lascer.problemdaten;

import java.util.ArrayList;
import java.io.Serializable;

import lascer.problemdaten.attribute.StringAttribut;
import lascer.problemdaten.attribute.NominalAttribut;
import lascer.problemdaten.attribute.RealAttribut;
import lascer.problemdaten.attribute.IntAttribut;

/**
 * Implementiert eine Sammulung von Attributen.
 *
 * @author  Dietmar Lippold
 */
public class AttributSammlung implements Serializable {

    /**
     * Die String-Attribute.
     */
    private ArrayList stringAttribute = new ArrayList();

    /**
     * Die nominalen Attribute.
     */
    private ArrayList nominalAttribute = new ArrayList();

    /**
     * Die nicht-ganzzahligen Attribute.
     */
    private ArrayList realAttribute = new ArrayList();

    /**
     * Die ganzzahligen Attribute.
     */
    private ArrayList intAttribute = new ArrayList();

    /**
     * Liefert die Anzahl der nominalen Attribute.
     *
     * @return  Die Anzahl der nominalen Attribute.
     */
    public int stringAttributAnz() {
        return stringAttribute.size();
    }

    /**
     * Liefert die Anzahl der nominalen Attribute.
     *
     * @return  Die Anzahl der nominalen Attribute.
     */
    public int nominalAttributAnz() {
        return nominalAttribute.size();
    }

    /**
     * Liefert die Anzahl der nicht-ganzzahligen Attribute.
     *
     * @return  Die Anzahl der nicht-ganzzahligen Attribute.
     */
    public int realAttributAnz() {
        return realAttribute.size();
    }

    /**
     * Liefert die Anzahl der ganzzahligen Attribute.
     *
     * @return  Die Anzahl der ganzzahligen Attribute.
     */
    public int intAttributAnz() {
        return intAttribute.size();
    }

    /**
     * Liefert die eindeutige Nummer des übergebenen String-Attributs. Die
     * kleinste Nummer ist Null und die Attribute werden fortlaufend
     * nummeriert.
     *
     * @param attribut  Das Attribut, dessen Nummer ermittelt werden soll.
     *
     * @return  Die eindeutige Nummer des Attributs innerhalb der
     *          String-Attribute. Wenn das Attribut nicht vorhanden ist, wird
     *          der Wert -1 geliefert.
     */
    public int attributNummer(StringAttribut attribut) {
        return stringAttribute.indexOf(attribut);
    }

    /**
     * Liefert die eindeutige Nummer des übergebenen nominalen Attributs.
     * Die kleinste Nummer ist Null und die Attribute werden fortlaufend
     * nummeriert.
     *
     * @param attribut  Das Attribut, dessen Nummer ermittelt werden soll.
     *
     * @return  Die eindeutige Nummer des Attributs innerhalb der nominalen
     *          Attribute. Wenn das Attribut nicht vorhanden ist, wird der
     *          Wert -1 geliefert.
     */
    public int attributNummer(NominalAttribut attribut) {
        return nominalAttribute.indexOf(attribut);
    }

    /**
     * Liefert die eindeutige Nummer des übergebenen nicht-ganzzahligen
     * Attributs. Die kleinste Nummer ist Null und die Attribute werden
     * fortlaufend nummeriert.
     *
     * @param attribut  Das Attribut, dessen Nummer ermittelt werden soll.
     *
     * @return  Die eindeutige Nummer des Attributs innerhalb der
     *          nicht-ganzzahligen Attribute. Wenn das Attribut nicht
     *          vorhanden ist, wird der Wert -1 geliefert.
     */
    public int attributNummer(RealAttribut attribut) {
        return realAttribute.indexOf(attribut);
    }

    /**
     * Liefert die eindeutige Nummer des übergebenen ganzzahligen Attributs.
     * Die kleinste Nummer ist Null und die Attribute werden fortlaufend
     * nummeriert.
     *
     * @param attribut  Das Attribut, dessen Nummer ermittelt werden soll.
     *
     * @return  Die eindeutige Nummer des Attributs innerhalb der
     *          ganzzahligen Attribute. Wenn das Attribut nicht vorhanden ist,
     *          wird der Wert -1 geliefert.
     */
    public int attributNummer(IntAttribut attribut) {
        return intAttribute.indexOf(attribut);
    }

    /**
     * Nimmt das übergebene String-Attribut in diese Sammlung auf.
     *
     * @param attribut  Das Attribut, das aufgeommen werden soll.
     *
     * @throws IllegalArgumentException  Wenn das Attribut schon vorhanden
     *                                   ist.
     */
    public void addStringAttribut(StringAttribut attribut) {
        if (stringAttribute.contains(attribut)) {
            throw new IllegalArgumentException("Attribut schon vorhanden: "
                                               + attribut.toString());
        } else {
            stringAttribute.add(attribut);
        }
    }

    /**
     * Nimmt das übergebene nominale Attribut in diese Sammlung auf.
     *
     * @param attribut  Das Attribut, das aufgeommen werden soll.
     *
     * @throws IllegalArgumentException  Wenn das Attribut schon vorhanden
     *                                   ist.
     */
    public void addNominalAttribut(NominalAttribut attribut) {
        if (nominalAttribute.contains(attribut)) {
            throw new IllegalArgumentException("Attribut schon vorhanden: "
                                               + attribut.toString());
        } else {
            nominalAttribute.add(attribut);
        }
    }

    /**
     * Nimmt das übergebene nicht-ganzzahlige Attribut in diese Sammlung auf.
     *
     * @param attribut  Das Attribut, das aufgeommen werden soll.
     *
     * @throws IllegalArgumentException  Wenn das Attribut schon vorhanden
     *                                   ist.
     */
    public void addRealAttribut(RealAttribut attribut) {
        if (realAttribute.contains(attribut)) {
            throw new IllegalArgumentException("Attribut schon vorhanden: "
                                               + attribut.toString());
        } else {
            realAttribute.add(attribut);
        }
    }

    /**
     * Nimmt das übergebene ganzzahlige Attribut in diese Sammlung auf.
     *
     * @param attribut  Das Attribut, das aufgeommen werden soll.
     *
     * @throws IllegalArgumentException  Wenn das Attribut schon vorhanden
     *                                   ist.
     */
    public void addIntAttribut(IntAttribut attribut) {
        if (intAttribute.contains(attribut)) {
            throw new IllegalArgumentException("Attribut schon vorhanden: "
                                               + attribut.toString());
        } else {
            intAttribute.add(attribut);
        }
    }

    /**
     * Liefert das String-Attribut mit der angegebenen Nummer. Wenn es kein
     * String-Attribut mit der Nummer gibt, wird <CODE>null</CODE> geliefert.
     *
     * @param nummer  Die Nummer, zu der das Attribut ermittelt werden soll.
     *
     * @return  Das String-Attribut mit der angegebenen Nummer.
     */
    public StringAttribut getStringAttribut(int nummer) {
        return (StringAttribut) stringAttribute.get(nummer);
    }

    /**
     * Liefert das nominale Attribut mit der angegebenen Nummer. Wenn es
     * kein nominales Attribut mit der Nummer gibt, wird <CODE>null</CODE>
     * geliefert.
     *
     * @param nummer  Die Nummer, zu der das Attribut ermittelt werden soll.
     *
     * @return  Das nominale Attribut mit der angegebenen Nummer.
     */
    public NominalAttribut getNominalAttribut(int nummer) {
        return (NominalAttribut) nominalAttribute.get(nummer);
    }

    /**
     * Liefert das nicht-ganzzahlige Attribut mit der angegebenen Nummer.
     * Wenn es kein nicht-ganzzahliges Attribut mit der Nummer gibt, wird
     * <CODE>null</CODE> geliefert.
     *
     * @param nummer  Die Nummer, zu der das Attribut ermittelt werden soll.
     *
     * @return  Das nicht-ganzzahlige Attribut mit der angegebenen Nummer.
     */
    public RealAttribut getRealAttribut(int nummer) {
        return (RealAttribut) realAttribute.get(nummer);
    }

    /**
     * Liefert das ganzzahlige Attribut mit der angegebenen Nummer. Wenn es
     * kein ganzzahliges Attribut mit der Nummer gibt, wird <CODE>null</CODE>
     * geliefert.
     *
     * @param nummer  Die Nummer, zu der das Attribut ermittelt werden soll.
     *
     * @return  Das ganzzahlige Attribut mit der angegebenen Nummer.
     */
    public IntAttribut getIntAttribut(int nummer) {
        return (IntAttribut) intAttribute.get(nummer);
    }
}

