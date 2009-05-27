/*
 * Dateiname      : Beispiel.java
 * Letzte Änderung: 09. September 2006
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

import java.util.Arrays;
import java.io.Serializable;

import lascer.problemdaten.attribute.StringAttribut;
import lascer.problemdaten.attribute.NominalAttribut;
import lascer.problemdaten.attribute.RealAttribut;
import lascer.problemdaten.attribute.IntAttribut;

/**
 * Repräsentiert ein Beispiel.
 *
 * @author  Dietmar Lippold
 */
public class Beispiel implements Serializable {

    /**
     * Die diesem Beispiel zugrundeliegenden Attribute.
     */
    private AttributSammlung attributSammlung;

    /**
     * Die Werte der String-Attribute des Beispiels.
     */
    private String[] stringWerte;

    /**
     * Die Werte der nominalen Attribute des Beispiels.
     */
    private String[] nominalWerte;

    /**
     * Die Werte der nicht-ganzzahligen Attribute des Beispiels.
     */
    private float[] realWerte;

    /**
     * Die Werte der ganzzahligen Attribute des Beispiels.
     */
    private int[] intWerte;

    /**
     * Erzeugt eine neue Instanz, die noch keine Werte besitzt.
     *
     * @param attributSammlung  Die diesem Beispiel zugrundeliegenden
     *                          Attribute.
     */
    Beispiel(AttributSammlung attributSammlung) {
        this.attributSammlung = attributSammlung;
        stringWerte = new String[0];
        nominalWerte = new String[0];
        realWerte = new float[0];
        intWerte = new int[0];
    }

    /**
     * Erzeugt eine neue Instanz, die die übergebenen Werte besitzt.
     *
     * @param attributSammlung  Die diesem Beispiel zugrundeliegenden
     *                          Attribute.
     * @param stringWerte       Die Werte der String-Attribute.
     * @param nominalWerte      Die anfänglichen Werte der nominalen Attribute
     *                          dieses Beispiels.
     * @param realWerte         Die anfänglichen Werte der nicht-ganzzahligen
     *                          Attribute dieses Beispiels.
     * @param intWerte          Die anfänglichen Werte der ganzzahligen
     *                          Attribute dieses Beispiels.
     *
     * @throws IllegalArgumentException  Der Wert eines nominalen Attributs
     *                                   gehört nicht zum Attribut.
     */
    public Beispiel(AttributSammlung attributSammlung, String[] stringWerte,
                    String[] nominalWerte, float[] realWerte, int[] intWerte) {
        NominalAttribut nomAttribut;

        this.attributSammlung = attributSammlung;
        this.stringWerte = stringWerte;
        this.realWerte = realWerte;
        this.intWerte = intWerte;

        // Anstatt der übergebenen Werte der nominalen Attribute die gleichen
        // Werte der Attribute speichern.
        this.nominalWerte = new String[nominalWerte.length];
        for (int wNr = 0; wNr < nominalWerte.length; wNr++) {
            if (nominalWerte[wNr].equals(Konstanten.UNBEKANNT_NOMINAL)) {
                this.nominalWerte[wNr] = Konstanten.UNBEKANNT_NOMINAL;
            } else {
                nomAttribut = attributSammlung.getNominalAttribut(wNr);
                this.nominalWerte[wNr] = nomAttribut.wert(nominalWerte[wNr]);
                if (this.nominalWerte[wNr] == null) {
                    throw new IllegalArgumentException("Wert des Attributs "
                                                        + nomAttribut.name()
                                                        + " ist unzulässig: "
                                                        + nominalWerte[wNr]);
                }
            }
        }
    }

    /**
     * Liefert die diesem Beispiel zugrundeliegenden Attribute.
     *
     * @return  Die diesem Beispiel zugrundeliegenden Attribute.
     */
    public AttributSammlung attributSammlung() {
        return attributSammlung;
    }

    /**
     * Ermittelt, ob einer der Werte der textuellen Attribute unbekannt ist.
     *
     * @return  <CODE>true</CODE>, wenn einer der Werte der textuellen
     *          Attribute unbekannt ist, anderenfalls <CODE>false</CODE>.
     */
    public boolean stringWertUnbekannt() {

        for (int i = 0; i < stringWerte.length; i++) {
            if (stringWerte[i].equals(Konstanten.UNBEKANNT_STRING)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ermittelt, ob einer der Werte der nominalen Attribute unbekannt ist.
     *
     * @return  <CODE>true</CODE>, wenn einer der Werte der nominalen
     *          Attribute unbekannt ist, anderenfalls <CODE>false</CODE>.
     */
    public boolean nominalWertUnbekannt() {

        for (int i = 0; i < nominalWerte.length; i++) {
            if (nominalWerte[i].equals(Konstanten.UNBEKANNT_NOMINAL)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ermittelt, ob einer der Werte der nicht-ganzzahligen Attribute
     * unbekannt ist.
     *
     * @return  <CODE>true</CODE>, wenn einer der Werte der nicht-ganzzahligen
     *          Attribute unbekannt ist, anderenfalls <CODE>false</CODE>.
     */
    public boolean realWertUnbekannt() {

        for (int i = 0; i < realWerte.length; i++) {
            if (realWerte[i] == Konstanten.UNBEKANNT_REAL) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ermittelt, ob einer der Werte der ganzzahligen Attribute unbekannt ist.
     *
     * @return  <CODE>true</CODE>, wenn einer der Werte der ganzzahligen
     *          Attribute unbekannt ist, anderenfalls <CODE>false</CODE>.
     */
    public boolean intWertUnbekannt() {

        for (int i = 0; i < intWerte.length; i++) {
            if (intWerte[i] == Konstanten.UNBEKANNT_INT) {
                return true;
            }
        }
        return false;
    }

    /**
     * Liefert den Wert für das textuelle Attribut mit der angegebenen Nummer.
     *
     * @param attributNr  Die Nummer des textuellen Attributs, für das der
     *                    Wert geliefert werden soll.
     *
     * @return  Den Wert für das textuelle Attribut mit der angegebenen
     *          Nummer.
     */
    String getStringWert(int attributNr) {
        return stringWerte[attributNr];
    }

    /**
     * Liefert den Wert für das nominale Attribut mit der angegebenen Nummer.
     *
     * @param attributNr  Die Nummer des nominalen Attributs, für das der Wert
     *                    geliefert werden soll.
     *
     * @return  Den Wert für das nominale Attribut mit der angegebenen Nummer.
     */
    String getNominalWert(int attributNr) {
        return nominalWerte[attributNr];
    }

    /**
     * Liefert den Wert für das nicht-ganzzahlige Attribut mit der angegebenen
     * Nummer.
     *
     * @param attributNr  Die Nummer des nicht-ganzzahligen Attributs, für das
     *                    der Wert geliefert werden soll.
     *
     * @return  Den Wert für das nicht-ganzzahlige Attribut mit der
     *          angegebenen Nummer.
     */
    float getRealWert(int attributNr) {
        return realWerte[attributNr];
    }

    /**
     * Liefert den Wert für das ganzzahlige Attribut mit der angegebenen
     * Nummer.
     *
     * @param attributNr  Die Nummer des ganzzahligen Attributs, für das der
     *                    Wert geliefert werden soll.
     *
     * @return  Den Wert für das ganzzahlige Attribut mit der angegebenen
     *          Nummer.
     */
    int getIntWert(int attributNr) {
        return intWerte[attributNr];
    }

    /**
     * Liefert den Wert für das textuelle Attribut mit der angegebenen Nummer.
     *
     * @param attribut  Das textuelle Attribut, für das der Wert geliefert
     *                  werden soll.
     *
     * @return  Den Wert für das übergebene textuelle Attribut.
     *
     * @throws IllegalArgumentException  Wenn das angegebene Attribut nicht
     *                                   vorhanden ist.
     */
    public String getStringWert(StringAttribut attribut) {
        int attributNr;

        attributNr = attributSammlung.attributNummer(attribut);
        if (attributNr == -1) {
            throw new IllegalArgumentException("Attribut nicht vorhanden: "
                                               + attribut.toString());
        }

        return stringWerte[attributNr];
    }

    /**
     * Liefert den Wert für das nominale Attribut mit der angegebenen Nummer.
     *
     * @param attribut  Das nominale Attribut, für das der Wert geliefert
     *                  werden soll.
     *
     * @return  Den Wert für das übergebene nominale Attribut.
     *
     * @throws IllegalArgumentException  Wenn das angegebene Attribut nicht
     *                                   vorhanden ist.
     */
    public String getNominalWert(NominalAttribut attribut) {
        int attributNr;

        attributNr = attributSammlung.attributNummer(attribut);
        if (attributNr == -1) {
            throw new IllegalArgumentException("Attribut nicht vorhanden: "
                                               + attribut.toString());
        }

        return nominalWerte[attributNr];
    }

    /**
     * Liefert den Wert für das nicht-ganzzahlige Attribut mit der angegebenen
     * Nummer.
     *
     * @param attribut  Das nicht-ganzzahligen Attribut, für das der Wert
     *                  geliefert werden soll.
     *
     * @return  Den Wert für das übergebene nicht-ganzzahlige Attribut.
     *
     * @throws IllegalArgumentException  Wenn das angegebene Attribut nicht
     *                                   vorhanden ist.
     */
    public float getRealWert(RealAttribut attribut) {
        int attributNr;

        attributNr = attributSammlung.attributNummer(attribut);
        if (attributNr == -1) {
            throw new IllegalArgumentException("Attribut nicht vorhanden: "
                                               + attribut.toString());
        }

        return realWerte[attributNr];
    }

    /**
     * Liefert den Wert für das ganzzahlige Attribut mit der angegebenen
     * Nummer.
     *
     * @param attribut  Das ganzzahlige Attribut, für das der Wert geliefert
     *                  werden soll.
     *
     * @return  Den Wert für das übergebene ganzzahlige Attribut.
     *
     * @throws IllegalArgumentException  Wenn das angegebene Attribut nicht
     *                                   vorhanden ist.
     */
    public int getIntWert(IntAttribut attribut) {
        int attributNr;

        attributNr = attributSammlung.attributNummer(attribut);
        if (attributNr == -1) {
            throw new IllegalArgumentException("Attribut nicht vorhanden: "
                                               + attribut.toString());
        }

        return intWerte[attributNr];
    }

    /**
     * Setzt den übergebenen Wert für das textuelle Attribut mit der
     * angegebenen Nummer. Wenn die Anzahl der bisherigen Attribute kleiner
     * ist als die übergebene Nummer, wird das Array der Attributwerte
     * erweitert.
     *
     * @param attributNr  Die Nummer des textuellen Attributs, für das der
     *                    Wert gesetzt werden soll.
     * @param neuerWert   Der zu setzende Wert.
     */
    void setStringWert(int attributNr, String neuerWert) {

        if (attributNr > attributSammlung.stringAttributAnz()) {
            throw new IllegalArgumentException("Attributnummer zu groß: "
                                               + attributNr);
        }

        if (attributNr >= stringWerte.length) {
            // Das Array der Attributwerte muß erweitert werden
            String[] stringWerteNeu = new String[attributNr + 1];
            Arrays.fill(stringWerteNeu, "");
            System.arraycopy(stringWerte, 0, stringWerteNeu, 0, stringWerte.length);
            stringWerte = stringWerteNeu;
        }

        stringWerte[attributNr] = neuerWert;
    }

    /**
     * Setzt den übergebenen Wert für das nominale Attribut mit der
     * angegebenen Nummer. Wenn die Anzahl der bisherigen Attribute kleiner
     * ist als die übergebene Nummer, wird das Array der Attributwerte
     * erweitert.
     *
     * @param attributNr  Die Nummer des nominalen Attributs, für das der Wert
     *                    gesetzt werden soll.
     * @param neuerWert   Der zu setzende Wert.
     */
    void setNominalWert(int attributNr, String neuerWert) {

        if (attributNr > attributSammlung.nominalAttributAnz()) {
            throw new IllegalArgumentException("Attributnummer zu groß: "
                                               + attributNr);
        }

        if (attributNr >= nominalWerte.length) {
            // Das Array der Attributwerte muß erweitert werden
            String[] nominalWerteNeu = new String[attributNr + 1];
            Arrays.fill(nominalWerteNeu, "");
            System.arraycopy(nominalWerte, 0, nominalWerteNeu, 0, nominalWerte.length);
            nominalWerte = nominalWerteNeu;
        }

        nominalWerte[attributNr] = neuerWert;
    }

    /**
     * Setzt den übergebenen Wert für das nicht-ganzzahlige Attribut mit der
     * angegebenen Nummer. Wenn die Anzahl der bisherigen Attribute kleiner
     * ist als die übergebene Nummer, wird das Array der Attributwerte
     * erweitert.
     *
     * @param attributNr  Die Nummer des nicht-ganzzahlige Attributs, für das
     *                    der Wert gesetzt werden soll.
     * @param neuerWert   Der zu setzende Wert.
     */
    void setRealWert(int attributNr, float neuerWert) {

        if (attributNr > attributSammlung.realAttributAnz()) {
            throw new IllegalArgumentException("Attributnummer zu groß: "
                                               + attributNr);
        }

        if (attributNr >= realWerte.length) {
            // Das Array der Attributwerte muß erweitert werden
            float[] realWerteNeu = new float[attributNr + 1];
            Arrays.fill(realWerteNeu, 0);
            System.arraycopy(realWerte, 0, realWerteNeu, 0, realWerte.length);
            realWerte = realWerteNeu;
        }

        realWerte[attributNr] = neuerWert;
    }

    /**
     * Setzt den übergebenen Wert für das ganzzahlige Attribut mit der
     * angegebenen Nummer. Wenn die Anzahl der bisherigen Attribute kleiner
     * ist als die übergebene Nummer, wird das Array der Attributwerte
     * erweitert.
     *
     * @param attributNr  Die Nummer des ganzzahlige Attributs, für das der
     *                    Wert gesetzt werden soll.
     * @param neuerWert   Der zu setzende Wert.
     */
    void setIntWert(int attributNr, int neuerWert) {

        if (attributNr > attributSammlung.intAttributAnz()) {
            throw new IllegalArgumentException("Attributnummer zu groß: "
                                               + attributNr);
        }

        if (attributNr >= intWerte.length) {
            // Das Array der Attributwerte muß erweitert werden
            int[] intWerteNeu = new int[attributNr + 1];
            Arrays.fill(intWerteNeu, 0);
            System.arraycopy(intWerte, 0, intWerteNeu, 0, intWerte.length);
            intWerte = intWerteNeu;
        }

        intWerte[attributNr] = neuerWert;
    }

    /**
     * Liefert eine Beschreibung des Beispiels mit dessen Werten. Die Werte
     * werden durch Kommata getrennt ausgegeben, zuerst die der textuellen
     * Attribute, dann die der nominalen Attribute, dann die der
     * nicht-ganzzahlige Attribute und schließlich die der ganzzahligen
     * Attribute.
     *
     * @return  Eine Beschreibung des Beispiels mit dessen Werten.
     */
    public String toString() {
        StringBuffer beschreibung = new StringBuffer();

        for (int i = 0; i < stringWerte.length; i++) {
            if (stringWerte[i].equals(Konstanten.UNBEKANNT_NOMINAL)) {
                beschreibung.append(Konstanten.UNBEKANNT_BESCHREIB + ", ");
            } else {
                beschreibung.append(stringWerte[i] + ", ");
            }
        }
        for (int i = 0; i < nominalWerte.length; i++) {
            if (nominalWerte[i].equals(Konstanten.UNBEKANNT_NOMINAL)) {
                beschreibung.append(Konstanten.UNBEKANNT_BESCHREIB + ", ");
            } else {
                beschreibung.append(nominalWerte[i] + ", ");
            }
        }
        for (int i = 0; i < realWerte.length; i++) {
            if (realWerte[i] == Konstanten.UNBEKANNT_REAL) {
                beschreibung.append(Konstanten.UNBEKANNT_BESCHREIB + ", ");
            } else {
                beschreibung.append(realWerte[i] + ", ");
            }
        }
        for (int i = 0; i < intWerte.length; i++) {
            if (intWerte[i] == Konstanten.UNBEKANNT_INT) {
                beschreibung.append(Konstanten.UNBEKANNT_BESCHREIB + ", ");
            } else {
                beschreibung.append(intWerte[i] + ", ");
            }
        }
        beschreibung.deleteCharAt(beschreibung.length() - 1);
        beschreibung.deleteCharAt(beschreibung.length() - 1);

        return beschreibung.toString();
    }
}

