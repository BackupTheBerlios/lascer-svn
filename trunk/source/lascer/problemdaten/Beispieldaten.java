/*
 * Dateiname      : Beispieldaten.java
 * Letzte Änderung: 06. September 2006
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

import lascer.problemdaten.attribute.AbstraktesAttribut;
import lascer.problemdaten.attribute.StringAttribut;
import lascer.problemdaten.attribute.NominalAttribut;
import lascer.problemdaten.attribute.RealAttribut;
import lascer.problemdaten.attribute.IntAttribut;

/**
 * Repräsentiert eine Menge von Beispielen und zugehörigen Attributen.
 *
 * @author  Dietmar Lippold
 */
public class Beispieldaten implements Serializable {

    /**
     * Die diesen Daten zugrundeliegenden Attribute.
     */
    private AttributSammlung attributSammlung;

    /**
     * Die positiven Beispiele.
     */
    private ArrayList posBeispiele = new ArrayList();

    /**
     * Die negativen Beispiele.
     */
    private ArrayList negBeispiele = new ArrayList();

    /**
     * Der Name das Datensatzes.
     */
    private String name;

    /**
     * Gibt an, ob der Wert eines textuellen Attributs mindestens eines der
     * Beispiele unbekannt ist.
     */
    private boolean stringWertUnbekannt = false;

    /**
     * Gibt an, ob der Wert eines nominalen Attributs mindestens eines der
     * Beispiele unbekannt ist.
     */
    private boolean nominalWertUnbekannt = false;

    /**
     * Gibt an, ob der Wert eines nicht-ganzzahligen Attributs mindestens
     * eines der Beispiele unbekannt ist.
     */
    private boolean realWertUnbekannt = false;

    /**
     * Gibt an, ob der Wert eines ganzzahligen Attributs mindestens eines der
     * Beispiele unbekannt ist.
     */
    private boolean intWertUnbekannt = false;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param name              Der Name das Datensatzes.
     * @param attributSammlung  Die diesen Daten zugrundeliegenden Attribute.
     */
    public Beispieldaten(String name, AttributSammlung attributSammlung) {
        this.name = name;
        this.attributSammlung = attributSammlung;
    }

    /**
     * Liefert eine Kopie dieses Datensatzes, bei der die Klasse der Beispiele
     * optional invertiert werden kann. Die zugehörige Attributsammlung wird
     * als Instanz übernommen.
     *
     * @param invertiert  Gibt an, ob die Klasse jedes Beispiele invertiert
     *                    werden soll.
     *
     * @return  Eine Kopie dieses Datensatzes, gegebenenfalls mit invertierten
     *          Klassen der Beispiele.
     */
    public Beispieldaten kopie(boolean invertiert) {
        Beispieldaten kopie;

        kopie = new Beispieldaten(name, attributSammlung);

        if (invertiert) {
            kopie.posBeispiele = new ArrayList(negBeispiele);
            kopie.negBeispiele = new ArrayList(posBeispiele);
        } else {
            kopie.posBeispiele = new ArrayList(posBeispiele);
            kopie.negBeispiele = new ArrayList(negBeispiele);
        }

        kopie.stringWertUnbekannt = stringWertUnbekannt;
        kopie.nominalWertUnbekannt = nominalWertUnbekannt;
        kopie.realWertUnbekannt = realWertUnbekannt;
        kopie.intWertUnbekannt = intWertUnbekannt;

        return kopie;
    }

    /**
     * Nimmt das übergebene Beispiel in die Liste der Beispiele auf.
     *
     * @param beispiel  Die aufzunehmende Beispiel.
     * @param positiv   Die Angabe, ob das Beispiel ein positives Beispiel
     *                  ist.
     *
     * @throws IllegalArgumentException  Wenn die Attribute des übergebenen
     *                                   und dieser Daten nicht gleich sind.
     */
    public void beispielAufnehmen(Beispiel beispiel, boolean positiv) {

        if (!beispiel.attributSammlung().equals(attributSammlung)) {
            throw new IllegalArgumentException("Attribute dieser Daten sind"
                                               + " ungleich zu denen des"
                                               + " Beispiels: " + beispiel);
        }

        if (positiv) {
            posBeispiele.add(beispiel);
        } else {
            negBeispiele.add(beispiel);
        }

        stringWertUnbekannt |= beispiel.stringWertUnbekannt();
        nominalWertUnbekannt |= beispiel.nominalWertUnbekannt();
        realWertUnbekannt |= beispiel.realWertUnbekannt();
        intWertUnbekannt |= beispiel.intWertUnbekannt();
    }

    /**
     * Entfernt das Beispiel mit der übergebenen Nummer aus der Liste der
     * Beispiele. Die Indices aller nachfolgenden Beispiele werden um Eins
     * verringert.
     *
     * @param beispielNummer  Die Nummer des zu entfernenden Beispiels.
     * @param positiv         Die Angabe, ob das zu entfernende Beispiel ein
     *                        positives Beispiel ist.
     *
     * @throws IllegalArgumentException  Wenn es kein Beispiel mit der
     *                                   angegebenen Nummer gibt.
     */
    public void beispielEntfernen(int beispielNummer, boolean positiv) {
        Beispiel naechstesBeispiel;

        if ((beispielNummer < 0)
            || (positiv && (beispielNummer >= posBeispiele.size()))
            || (!positiv && (beispielNummer >= negBeispiele.size()))) {

            throw new IllegalArgumentException("Nummer des Beispiels ist"
                                               + " unzulässig: " + beispielNummer);
        }

        if (positiv) {
            posBeispiele.remove(beispielNummer);
        } else {
            negBeispiele.remove(beispielNummer);
        }

        stringWertUnbekannt = false;
        nominalWertUnbekannt = false;
        realWertUnbekannt = false;
        intWertUnbekannt = false;

        for (int i = 0; i < posBeispiele.size(); i++) {
            naechstesBeispiel = (Beispiel) posBeispiele.get(i);
            stringWertUnbekannt |= naechstesBeispiel.stringWertUnbekannt();
            nominalWertUnbekannt |= naechstesBeispiel.nominalWertUnbekannt();
            realWertUnbekannt |= naechstesBeispiel.realWertUnbekannt();
            intWertUnbekannt |= naechstesBeispiel.intWertUnbekannt();
        }

        for (int i = 0; i < negBeispiele.size(); i++) {
            naechstesBeispiel = (Beispiel) negBeispiele.get(i);
            stringWertUnbekannt |= naechstesBeispiel.stringWertUnbekannt();
            nominalWertUnbekannt |= naechstesBeispiel.nominalWertUnbekannt();
            realWertUnbekannt |= naechstesBeispiel.realWertUnbekannt();
            intWertUnbekannt |= naechstesBeispiel.intWertUnbekannt();
        }
    }

    /**
     * Liefert den Namen des Datensatzes.
     *
     * @return  Den Namen des Datensatzes.
     */
    public String name() {
        return name;
    }

    /**
     * Liefert die diesen Daten zugrundeliegenden Attribute.
     *
     * @return  Die diesen Daten zugrundeliegenden Attribute.
     */
    public AttributSammlung attributSammlung() {
        return attributSammlung;
    }

    /**
     * Liefert die Anzahl der positiven Beispiele.
     *
     * @return  Die Anzahl der positiven Beispiele.
     */
    public int posBspAnz() {
        return posBeispiele.size();
    }

    /**
     * Liefert die Anzahl der negativen Beispiele.
     *
     * @return  Die Anzahl der negativen Beispiele.
     */
    public int negBspAnz() {
        return negBeispiele.size();
    }

    /**
     * Liefert eine Liste der positiven Beispiele.
     *
     * @return  Die eine Liste der positiven Beispiele.
     */
    public ArrayList posBeispiele() {
        return posBeispiele;
    }

    /**
     * Liefert eine Liste der negativen Beispiele.
     *
     * @return  Die eine Liste der negativen Beispiele.
     */
    public ArrayList negBeispiele() {
        return negBeispiele;
    }

    /**
     * Ermittelt, ob der Wert eines textuellen Attributs mindestens eines der
     * Beispiele unbekannt ist.
     *
     * @return  <CODE>true</CODE>, ob der Wert eines textuellen Attributs
     *          mindestens eines der Beispiele unbekannt ist, anderenfalls
     *          <CODE>false</CODE>.
     */
    public boolean stringWertUnbekannt() {
        return stringWertUnbekannt;
    }

    /**
     * Ermittelt, ob der Wert eines nominalen Attributs mindestens eines der
     * Beispiele unbekannt ist.
     *
     * @return  <CODE>true</CODE>, ob der Wert eines nominalen Attributs
     *          mindestens eines der Beispiele unbekannt ist, anderenfalls
     *          <CODE>false</CODE>.
     */
    public boolean nominalWertUnbekannt() {
        return nominalWertUnbekannt;
    }

    /**
     * Ermittelt, ob der Wert eines nicht-ganzzahligen Attributs mindestens
     * eines der Beispiele unbekannt ist.
     *
     * @return  <CODE>true</CODE>, ob der Wert eines nicht-ganzzahligen
     *          Attributs mindestens eines der Beispiele unbekannt ist,
     *          anderenfalls <CODE>false</CODE>.
     */
    public boolean realWertUnbekannt() {
        return realWertUnbekannt;
    }

    /**
     * Ermittelt, ob der Wert eines ganzzahligen Attributs mindestens eines
     * der Beispiele unbekannt ist.
     *
     * @return  <CODE>true</CODE>, ob der Wert eines ganzzahligen Attributs
     *          mindestens eines der Beispiele unbekannt ist, anderenfalls
     *          <CODE>false</CODE>.
     */
    public boolean intWertUnbekannt() {
        return intWertUnbekannt;
    }

    /**
     * Nimmt das übergebene Attribut zu den textuellen Attributen hinzu und
     * weist die übergebenen Werte den Beispielen zu.
     *
     * @param attribut     Das aufzunehmende Attribut.
     * @param posBspWerte  Die Werte der positiven Beispiele für das
     *                     aufzunehmende Attribut.
     * @param negBspWerte  Die Werte der negativen Beispiele für das
     *                     aufzunehmende Attribut.
     *
     * @throws IllegalArgumentException  Wenn übergebene Attribut schon
     *                                   vorhanden ist oder die Anzahl der
     *                                   Werte nicht mit der Anzahl der
     *                                   Beispiele übereinstimmt.
     */
    public void addStringAttribut(StringAttribut attribut,
                                  String[] posBspWerte, String[] negBspWerte) {
        Beispiel beispiel;
        String   bspWert;
        int      attributNr;

        if (attributSammlung.attributNummer(attribut) != -1) {
            throw new IllegalArgumentException("Attribut schon vorhanden");
        }

        if ((posBspWerte.length != posBspAnz())
                || (negBspWerte.length != negBspAnz())) {
            throw new IllegalArgumentException("Anzahl der Beispiele stimmt"
                                               + " nicht");
        }

        attributSammlung.addStringAttribut(attribut);
        attributNr = attributSammlung.attributNummer(attribut);

        for (int bspNr = 0; bspNr < posBspWerte.length; bspNr++) {
            beispiel = (Beispiel) posBeispiele.get(bspNr);
            bspWert = posBspWerte[bspNr];
            beispiel.setStringWert(attributNr, bspWert);
            stringWertUnbekannt |= bspWert.equals(Konstanten.UNBEKANNT_STRING);
        }
        for (int bspNr = 0; bspNr < negBspWerte.length; bspNr++) {
            beispiel = (Beispiel) negBeispiele.get(bspNr);
            bspWert = negBspWerte[bspNr];
            beispiel.setStringWert(attributNr, bspWert);
            stringWertUnbekannt |= bspWert.equals(Konstanten.UNBEKANNT_STRING);
        }
    }

    /**
     * Nimmt das übergebene Attribut zu den nominalen Attributen hinzu und
     * weist die übergebenen Werte den Beispielen zu.
     *
     * @param attribut     Das aufzunehmende Attribut.
     * @param posBspWerte  Die Werte der positiven Beispiele für das
     *                     aufzunehmende Attribut.
     * @param negBspWerte  Die Werte der negativen Beispiele für das
     *                     aufzunehmende Attribut.
     *
     * @throws IllegalArgumentException  Wenn übergebene Attribut schon
     *                                   vorhanden ist oder die Anzahl der
     *                                   Werte nicht mit der Anzahl der
     *                                   Beispiele übereinstimmt.
     */
    public void addNominalAttribut(NominalAttribut attribut,
                                   String[] posBspWerte, String[] negBspWerte) {
        Beispiel beispiel;
        String   bspWert;
        int      attributNr;

        if (attributSammlung.attributNummer(attribut) != -1) {
            throw new IllegalArgumentException("Attribut schon vorhanden");
        }

        if ((posBspWerte.length != posBspAnz())
                || (negBspWerte.length != negBspAnz())) {
            throw new IllegalArgumentException("Anzahl der Beispiele stimmt"
                                               + " nicht");
        }

        attributSammlung.addNominalAttribut(attribut);
        attributNr = attributSammlung.attributNummer(attribut);

        for (int bspNr = 0; bspNr < posBspWerte.length; bspNr++) {
            beispiel = (Beispiel) posBeispiele.get(bspNr);
            bspWert = posBspWerte[bspNr];
            beispiel.setNominalWert(attributNr, bspWert);
            nominalWertUnbekannt |= bspWert.equals(Konstanten.UNBEKANNT_NOMINAL);
        }
        for (int bspNr = 0; bspNr < negBspWerte.length; bspNr++) {
            beispiel = (Beispiel) negBeispiele.get(bspNr);
            bspWert = negBspWerte[bspNr];
            beispiel.setNominalWert(attributNr, bspWert);
            nominalWertUnbekannt |= bspWert.equals(Konstanten.UNBEKANNT_NOMINAL);
        }
    }

    /**
     * Nimmt das übergebene Attribut zu den nicht-ganzzahligen Attributen
     * hinzu und weist die übergebenen Werte den Beispielen zu.
     *
     * @param attribut     Das aufzunehmende Attribut.
     * @param posBspWerte  Die Werte der positiven Beispiele für das
     *                     aufzunehmende Attribut.
     * @param negBspWerte  Die Werte der negativen Beispiele für das
     *                     aufzunehmende Attribut.
     *
     * @throws IllegalArgumentException  Wenn übergebene Attribut schon
     *                                   vorhanden ist oder die Anzahl der
     *                                   Werte nicht mit der Anzahl der
     *                                   Beispiele übereinstimmt.
     */
    public void addRealAttribut(RealAttribut attribut,
                                float[] posBspWerte, float[] negBspWerte) {
        Beispiel beispiel;
        float    bspWert;
        int      attributNr;

        if (attributSammlung.attributNummer(attribut) != -1) {
            throw new IllegalArgumentException("Attribut schon vorhanden");
        }

        if ((posBspWerte.length != posBspAnz())
                || (negBspWerte.length != negBspAnz())) {
            throw new IllegalArgumentException("Anzahl der Beispiele stimmt"
                                               + " nicht");
        }

        attributSammlung.addRealAttribut(attribut);
        attributNr = attributSammlung.attributNummer(attribut);

        for (int bspNr = 0; bspNr < posBspWerte.length; bspNr++) {
            beispiel = (Beispiel) posBeispiele.get(bspNr);
            bspWert = posBspWerte[bspNr];
            beispiel.setRealWert(attributNr, bspWert);
            realWertUnbekannt |= (bspWert == Konstanten.UNBEKANNT_REAL);
        }
        for (int bspNr = 0; bspNr < negBspWerte.length; bspNr++) {
            beispiel = (Beispiel) negBeispiele.get(bspNr);
            bspWert = negBspWerte[bspNr];
            beispiel.setRealWert(attributNr, bspWert);
            realWertUnbekannt |= (bspWert == Konstanten.UNBEKANNT_REAL);
        }
    }

    /**
     * Nimmt das übergebene Attribut zu den ganzzahligen Attributen hinzu und
     * weist die übergebenen Werte den Beispielen zu.
     *
     * @param attribut     Das aufzunehmende Attribut.
     * @param posBspWerte  Die Werte der positiven Beispiele für das
     *                     aufzunehmende Attribut.
     * @param negBspWerte  Die Werte der negativen Beispiele für das
     *                     aufzunehmende Attribut.
     *
     * @throws IllegalArgumentException  Wenn übergebene Attribut schon
     *                                   vorhanden ist oder die Anzahl der
     *                                   Werte nicht mit der Anzahl der
     *                                   Beispiele übereinstimmt.
     */
    public void addIntAttribut(IntAttribut attribut,
                               int[] posBspWerte, int[] negBspWerte) {
        Beispiel beispiel;
        int      bspWert;
        int      attributNr;

        if (attributSammlung.attributNummer(attribut) != -1) {
            throw new IllegalArgumentException("Attribut schon vorhanden");
        }

        if ((posBspWerte.length != posBspAnz())
                || (negBspWerte.length != negBspAnz())) {
            throw new IllegalArgumentException("Anzahl der Beispiele stimmt"
                                               + " nicht");
        }

        attributSammlung.addIntAttribut(attribut);
        attributNr = attributSammlung.attributNummer(attribut);

        for (int bspNr = 0; bspNr < posBspWerte.length; bspNr++) {
            beispiel = (Beispiel) posBeispiele.get(bspNr);
            bspWert = posBspWerte[bspNr];
            beispiel.setIntWert(attributNr, bspWert);
            intWertUnbekannt |= (bspWert == Konstanten.UNBEKANNT_INT);
        }
        for (int bspNr = 0; bspNr < negBspWerte.length; bspNr++) {
            beispiel = (Beispiel) negBeispiele.get(bspNr);
            bspWert = negBspWerte[bspNr];
            beispiel.setIntWert(attributNr, bspWert);
            intWertUnbekannt |= (bspWert == Konstanten.UNBEKANNT_INT);
        }
    }

    /**
     * Liefert die Werte aller positiven oder negativen Beispiele für das
     * angegebene textuelle Attribut.
     *
     * @param attribut  Das Attribut, für das die Werte geliefert werden
     *                  sollen.
     * @param positiv   Die Angabe, ob die Werte der positiven Beispiel
     *                  geliefert werden sollen. Falls nein, werden die
     *                  Werte der negativen Beispiele geliefert.
     *
     * @return  Die Werte aller positiven oder negativen Beispiele für das
     *          angegebene textuelle Attribut.
     *
     * @throws IllegalArgumentException  Wenn das angegebene Attribut nicht
     *                                   vorhanden ist.
     */
    public String[] getStringWerte(StringAttribut attribut, boolean positiv) {
        String[] stringWerte;
        Beispiel beispiel;
        int      attributNr;
        int      bspAnz;

        attributNr = attributSammlung.attributNummer(attribut);
        if (attributNr == -1) {
            throw new IllegalArgumentException("Attribut nicht vorhanden");
        }

        if (positiv) {
            bspAnz = posBspAnz();
            stringWerte = new String[bspAnz];
            for (int bspNr = 0; bspNr < bspAnz; bspNr++) {
                beispiel = (Beispiel) posBeispiele.get(bspNr);
                stringWerte[bspNr] = beispiel.getStringWert(attributNr);
            }
        } else {
            bspAnz = negBspAnz();
            stringWerte = new String[bspAnz];
            for (int bspNr = 0; bspNr < bspAnz; bspNr++) {
                beispiel = (Beispiel) negBeispiele.get(bspNr);
                stringWerte[bspNr] = beispiel.getStringWert(attributNr);
            }
        }
        return stringWerte;
    }

    /**
     * Liefert die Werte aller positiven oder negativen Beispiele für das
     * angegebene nominale Attribut.
     *
     * @param attribut  Das Attribut, für das die Werte geliefert werden
     *                  sollen.
     * @param positiv   Die Angabe, ob die Werte der positiven Beispiel
     *                  geliefert werden sollen. Falls nein, werden die
     *                  Werte der negativen Beispiele geliefert.
     *
     * @return  Die Werte aller positiven oder negativen Beispiele für das
     *          angegebene nominale Attribut.
     *
     * @throws IllegalArgumentException  Wenn das angegebene Attribut nicht
     *                                   vorhanden ist.
     */
    public String[] getNominalWerte(NominalAttribut attribut, boolean positiv) {
        String[] nominalWerte;
        Beispiel beispiel;
        int      attributNr;
        int      bspAnz;

        attributNr = attributSammlung.attributNummer(attribut);
        if (attributNr == -1) {
            throw new IllegalArgumentException("Attribut nicht vorhanden");
        }

        if (positiv) {
            bspAnz = posBspAnz();
            nominalWerte = new String[bspAnz];
            for (int bspNr = 0; bspNr < bspAnz; bspNr++) {
                beispiel = (Beispiel) posBeispiele.get(bspNr);
                nominalWerte[bspNr] = beispiel.getNominalWert(attributNr);
            }
        } else {
            bspAnz = negBspAnz();
            nominalWerte = new String[bspAnz];
            for (int bspNr = 0; bspNr < bspAnz; bspNr++) {
                beispiel = (Beispiel) negBeispiele.get(bspNr);
                nominalWerte[bspNr] = beispiel.getNominalWert(attributNr);
            }
        }
        return nominalWerte;
    }

    /**
     * Liefert die Werte aller positiven oder negativen Beispiele für das
     * angegebene nicht-ganzzahlige Attribut.
     *
     * @param attribut  Das Attribut, für das die Werte geliefert werden
     *                  sollen.
     * @param positiv   Die Angabe, ob die Werte der positiven Beispiel
     *                  geliefert werden sollen. Falls nein, werden die
     *                  Werte der negativen Beispiele geliefert.
     *
     * @return  Ein neu erzeugtes Array mit den Werte aller positiven oder
     *          negativen Beispiele für das angegebene nicht-ganzzahlige
     *          Attribut.
     *
     * @throws IllegalArgumentException  Wenn das angegebene Attribut nicht
     *                                   vorhanden ist.
     */
    public float[] getRealWerte(RealAttribut attribut, boolean positiv) {
        float[]  realWerte;
        Beispiel beispiel;
        int      attributNr;
        int      bspAnz;

        attributNr = attributSammlung.attributNummer(attribut);
        if (attributNr == -1) {
            throw new IllegalArgumentException("Attribut nicht vorhanden");
        }

        if (positiv) {
            bspAnz = posBspAnz();
            realWerte = new float[bspAnz];
            for (int bspNr = 0; bspNr < bspAnz; bspNr++) {
                beispiel = (Beispiel) posBeispiele.get(bspNr);
                realWerte[bspNr] = beispiel.getRealWert(attributNr);
            }
        } else {
            bspAnz = negBspAnz();
            realWerte = new float[bspAnz];
            for (int bspNr = 0; bspNr < bspAnz; bspNr++) {
                beispiel = (Beispiel) negBeispiele.get(bspNr);
                realWerte[bspNr] = beispiel.getRealWert(attributNr);
            }
        }
        return realWerte;
    }

    /**
     * Liefert die Werte aller positiven oder negativen Beispiele für das
     * angegebene ganzzahlige Attribut.
     *
     * @param attribut  Das Attribut, für das die Werte geliefert werden
     *                  sollen.
     * @param positiv   Die Angabe, ob die Werte der positiven Beispiel
     *                  geliefert werden sollen. Falls nein, werden die
     *                  Werte der negativen Beispiele geliefert.
     *
     * @return  Ein neu erzeugtes Array mit den Werte aller positiven oder
     *          negativen Beispiele für das angegebene ganzzahlige Attribut.
     *
     * @throws IllegalArgumentException  Wenn das angegebene Attribut nicht
     *                                   vorhanden ist.
     */
    public int[] getIntWerte(IntAttribut attribut, boolean positiv) {
        int[]    intWerte;
        Beispiel beispiel;
        int      attributNr;
        int      bspAnz;

        attributNr = attributSammlung.attributNummer(attribut);
        if (attributNr == -1) {
            throw new IllegalArgumentException("Attribut nicht vorhanden");
        }

        if (positiv) {
            bspAnz = posBspAnz();
            intWerte = new int[bspAnz];
            for (int bspNr = 0; bspNr < bspAnz; bspNr++) {
                beispiel = (Beispiel) posBeispiele.get(bspNr);
                intWerte[bspNr] = beispiel.getIntWert(attributNr);
            }
        } else {
            bspAnz = negBspAnz();
            intWerte = new int[bspAnz];
            for (int bspNr = 0; bspNr < bspAnz; bspNr++) {
                beispiel = (Beispiel) negBeispiele.get(bspNr);
                intWerte[bspNr] = beispiel.getIntWert(attributNr);
            }
        }
        return intWerte;
    }

    /**
     * Liefert eine Beschreibung dieser Daten im arff-Format.
     *
     * @return  Eine Beschreibung dieser Daten im arff-Format.
     */
    public String toString() {
        StringBuffer       beschreibung = new StringBuffer();
        AbstraktesAttribut attribut;
        int                attribAnz;

        // Den Namen in die Beschreibung aufnehmen.
        beschreibung.append("@relation " + name + "\n\n");

        // Die String-Attribute in die Beschreibung aufnehmen.
        attribAnz = attributSammlung.stringAttributAnz();
        for (int i = 0; i < attribAnz; i++) {
            attribut = attributSammlung.getStringAttribut(i);
            beschreibung.append("@attribute " + attribut + "\n");
        }

        // Die nominalen Attribute in die Beschreibung aufnehmen.
        attribAnz = attributSammlung.nominalAttributAnz();
        for (int i = 0; i < attribAnz; i++) {
            attribut = attributSammlung.getNominalAttribut(i);
            beschreibung.append("@attribute " + attribut + "\n");
        }

        // Die nicht-ganzzahligen Attribute in die Beschreibung aufnehmen.
        attribAnz = attributSammlung.realAttributAnz();
        for (int i = 0; i < attribAnz; i++) {
            attribut = attributSammlung.getRealAttribut(i);
            beschreibung.append("@attribute " + attribut + "\n");
        }

        // Die ganzzahligen Attribute in die Beschreibung aufnehmen.
        attribAnz = attributSammlung.intAttributAnz();
        for (int i = 0; i < attribAnz; i++) {
            attribut = attributSammlung.getIntAttribut(i);
            beschreibung.append("@attribute " + attribut + "\n");
        }

        // Das class-Attribut in die Beschreibung aufnehmen.
        beschreibung.append("@attribute class {false, true}\n");

        // Den Datenteil in die Beschreibung aufnehmen.
        beschreibung.append("\n");
        beschreibung.append("@data\n");
        for (int i = 0; i < negBeispiele.size(); i++) {
            beschreibung.append(negBeispiele.get(i) + ", false\n");
        }
        for (int i = 0; i < posBeispiele.size(); i++) {
            beschreibung.append(posBeispiele.get(i) + ", true\n");
        }

        return beschreibung.toString();
    }
}

