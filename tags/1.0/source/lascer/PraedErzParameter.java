/*
 * Dateiname      : PraedErzParameter.java
 * Letzte Änderung: 19. Dezember 2007
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


package lascer;

/**
 * Verwaltet eine Reihe von Parametern zur Steuerung der Erzeugung der
 * Praedikate bzw. von deren Erzeuger.
 *
 * @author  Dietmar Lippold
 */
public class PraedErzParameter {

    /**
     * Gibt die maximale Anzahl von Hyperebenen an, die für die Erzeugung der
     * entsprechenden Prädikate erzeugt werden sollen. Der Wert Null steht für
     * eine unbeschränkte Anzahl.
     */
    private int maxHypEbenAnz = Konstanten.MAX_HYP_EBENEN_ANZ;

    /**
     * Die maximale Anzahl der elementaren Funktionen in den zu erzeugenden
     * int-Funktionen.
     */
    private int maxIntFunkKomplex = Konstanten.MAX_INT_FUNK_KOMPLEX;

    /**
     * Die maximale Anzahl der elementaren Funktionen in den zu erzeugenden
     * real-Funktionen.
     */
    private int maxRealFunkKomplex = Konstanten.MAX_REAL_FUNK_KOMPLEX;

    /**
     * Gibt die Nummer des Wertes der boolschen Attribute an (Null oder Eins),
     * nur für den Prädikate, jedoch auch negierte, d.h. invertierte Literale,
     * erzeugt werden sollen. Wenn der Wert negativ ist, werden für beide
     * Werte Prädikate erzeugt, jedoch keine negierten.
     */
    private int boolWertPraedNummer = Konstanten.BOOL_WERT_PRAED_NUMMER;

    /**
     * Gibt an, ob aus int-Attributen zusätzlich real-Attribute erzeugt werden
     * sollen.
     */
    private boolean ergRealVonInt = Konstanten.ERG_REAL_VON_INT;

    /**
     * Gibt an, ob Prädikate zum Vergleich von nominalen Attributen
     * untereinander erzeugt werden sollen.
     */
    private boolean nomNomPraedErz = Konstanten.NOM_NOM_PRAED_ERZ;

    /**
     * Gibt an, ob Prädikate zum Vergleich von int-Attributen untereinander
     * erzeugt werden sollen.
     */
    private boolean intIntPraedErz = Konstanten.INT_INT_PRAED_ERZ;

    /**
     * Gibt an, ob Prädikate zum Vergleich von real-Attributen untereinander
     * erzeugt werden sollen.
     */
    private boolean realRealPraedErz = Konstanten.REAL_REAL_PRAED_ERZ;

    /**
     * Gibt an, ob bei Funktionen mit ganzzahligen Werten Prädikate nur zu
     * Halb-Intervallen erzeugt werden sollen. Falls nicht, werden Prädikate
     * zu Intervallen aller Arten erzeugt.
     */
    private boolean nurHalbIntItv = Konstanten.NUR_HALB_INT_ITV;

    /**
     * Gibt an, ob bei Funktionen mit Fließkomma-Werten Prädikate nur zu
     * Halb-Intervallen erzeugt werden sollen. Falls nicht, werden Prädikate
     * zu Intervallen aller Arten erzeugt.
     */
    private boolean nurHalbRealItv = Konstanten.NUR_HALB_REAL_ITV;

    /**
     * Gibt an, ob Prädikate mittels Hyperebenen zu den real-Attributen
     * erzeugt werden sollen.
     */
    private boolean hypEbenPraedErz = Konstanten.HYP_EBEN_PRAED_ERZ;

    /**
     * Legt die maximale Anzahl von Hyperebenen fest, die für die Erzeugung
     * der entsprechenden Attribute erzeugt werden sollen. Der Wert Null steht
     * für eine unbeschränkte Anzahl.
     *
     * @param wert  Der Wert, der die maximale Anzahl von Hyperebenen fest,
     *              die für die Erzeugung der entsprechenden Attribute erzeugt
     *              werden sollen.
     */
    void setMaxHypEbenAnz(int wert) {
        maxHypEbenAnz = wert;
    }

    /**
     * Legt die maximale Anzahl der elementaren Funktionen in den zu
     * erzeugenden int-Funktionen fest.
     *
     * @param wert  Die maximale Anzahl der elementaren Funktionen in den zu
     *              erzeugenden int-Funktionen.
     */
    void setMaxIntFunkKomplex(int wert) {
        maxIntFunkKomplex = wert;
    }

    /**
     * Legt die maximale Anzahl der elementaren Funktionen in den zu
     * erzeugenden real-Funktionen fest.
     *
     * @param wert  Die maximale Anzahl der elementaren Funktionen in den zu
     *              erzeugenden real-Funktionen.
     */
    void setMaxRealFunkKomplex(int wert) {
        maxRealFunkKomplex = wert;
    }

    /**
     * Legt die Nummer des Wertes der boolschen Attribute fest (Null oder
     * Eins), nur für den Prädikate, jedoch auch negierte, d.h. invertierte
     * Literale, erzeugt werden sollen. Wenn der Wert negativ ist, werden für
     * beide Werte Prädikate erzeugt, jedoch keine negierten.
     *
     * @param wert  Die Nummer des Wertes der boolschen Attribute (Null oder
     *              Eins), nur für den Prädikate erzeugt werden sollen, oder
     *              ein negativer Wert.
     */
    void setBoolWertPraedNummer(int wert) {
        boolWertPraedNummer = wert;
    }

    /**
     * Legt fest, ob aus int-Attributen zusätzlich real-Attribute erzeugt
     * werden sollen.
     *
     * @param wert  Der Wert, der angibt, ob aus int-Attributen zusätzlich
     *              real-Attribute erzeugt werden sollen.
     */
    void setErgRealVonInt(boolean wert) {
        ergRealVonInt = wert;
    }

    /**
     * Legt fest, ob Prädikate zum Vergleich von nominalen Attributen
     * untereinander erzeugt werden sollen.
     *
     * @param wert  Der Wert, der angibt, ob Prädikate zum Vergleich von
     *              nominalen Attributen untereinander erzeugt werden sollen.
     */
    void setNomNomPraedErz(boolean wert) {
        nomNomPraedErz = wert;
    }

    /**
     * Legt fest, ob Prädikate zum Vergleich von int-Attributen untereinander
     * erzeugt werden sollen.
     *
     * @param wert  Der Wert, der angibt, ob Prädikate zum Vergleich von
     *              int-Attributen untereinander erzeugt werden sollen.
     */
    void setIntIntPraedErz(boolean wert) {
        intIntPraedErz = wert;
    }

    /**
     * Legt fest, ob Prädikate zum Vergleich von real-Attributen untereinander
     * erzeugt werden sollen.
     *
     * @param wert  Der Wert, der angibt, ob Prädikate zum Vergleich von
     *              real-Attributen untereinander erzeugt werden sollen.
     */
    void setRealRealPraedErz(boolean wert) {
        realRealPraedErz = wert;
    }

    /**
     * Legt fest, ob nur zu Halb-Intervallen Prädikate von Funktionen mit
     * ganzzahligen Werten erzeugt werden sollen. Falls nicht, werden
     * Prädikate zu Intervalle aller Art erzeugt.
     *
     * @param wert  Der Wert, der angibt, ob nur zu Halb-Intervallen Prädikate
     *              von Funktionen mit ganzzahligen Werten erzeugt werden
     *              sollen. Falls nicht, werden Prädikate zu Intervalle aller
     *              Art erzeugt.
     */
    void setNurHalbIntItv(boolean wert) {
        nurHalbIntItv = wert;
    }

    /**
     * Legt fest, ob nur zu Halb-Intervallen Prädikate von Funktionen mit
     * Fließkomma-Werten erzeugt werden sollen. Falls nicht, werden Prädikate
     * zu Intervallen aller Art erzeugt.
     *
     * @param wert  Der Wert, der angibt, ob nur zu Halb-Intervallen Prädikate
     *              von Funktionen mit Fließkomma-Werten erzeugt werden
     *              sollen. Falls nicht, werden Prädikate zu Intervallen aller
     *              Art erzeugt.
     */
    void setNurHalbRealItv(boolean wert) {
        nurHalbRealItv = wert;
    }

    /**
     * Legt fest, ob Prädikate mittels Hyperebenen zu den real-Attributen
     * erzeugt werden sollen.
     *
     * @param wert  Der Wert, der angibt, ob Prädikate mittels Hyperebenen zu
     *              den real-Attributen erzeugt werden sollen.
     */
    void setHypEbenPraedErz(boolean wert) {
        hypEbenPraedErz = wert;
    }

    /**
     * Liefert die maximale Anzahl von Hyperebenen, die für die Erzeugung der
     * entsprechenden Attribute erzeugt werden sollen. Der Wert Null steht für
     * eine unbeschränkte Anzahl.
     *
     * @return  Den Wert, der die maximale Anzahl von Hyperebenen fest, die
     *          für die Erzeugung der entsprechenden Attribute erzeugt werden
     *          sollen.
     */
    public int getMaxHypEbenAnz() {
        return maxHypEbenAnz;
    }

    /**
     * Liefert die maximale Anzahl der elementaren Funktionen in den zu
     * erzeugenden int-Funktionen.
     *
     * @return  Die maximale Anzahl der elementaren Funktionen in den zu
     *          erzeugenden int-Funktionen.
     */
    public int getMaxIntFunkKomplex() {
        return maxIntFunkKomplex;
    }

    /**
     * Liefert die maximale Anzahl der elementaren Funktionen in den zu
     * erzeugenden real-Funktionen.
     *
     * @return  Die maximale Anzahl der elementaren Funktionen in den zu
     *          erzeugenden real-Funktionen.
     */
    public int getMaxRealFunkKomplex() {
        return maxRealFunkKomplex;
    }

    /**
     * Liefert die Nummer des Wertes der boolschen Attribute (Null oder Eins),
     * nur für den Prädikate, jedoch auch negierte, d.h. invertierte Literale,
     * erzeugt werden sollen. Wenn der Wert negativ ist, werden für beide
     * Werte Prädikate erzeugt, jedoch keine negierten.
     *
     * @return  Die Nummer des Wertes der boolschen Attribute (Null oder
     *          Eins), nur für den Prädikate erzeugt werden sollen, oder ein
     *          negativer Wert.
     */
    public int getBoolWertPraedNummer() {
        return boolWertPraedNummer;
    }

    /**
     * Gibt an, ob aus int-Attributen zusätzlich real-Attribute erzeugt werden
     * sollen.
     *
     * @return  Den Wert, der angibt, ob aus int-Attributen zusätzlich
     *          real-Attribute erzeugt werden sollen.
     */
    public boolean getErgRealVonInt() {
        return ergRealVonInt;
    }

    /**
     * Gibt an, ob Prädikate zum Vergleich von nominalen Attributen
     * untereinander erzeugt werden sollen.
     *
     * @return  Den Wert, der angibt, ob Prädikate zum Vergleich von
     *          nominalen Attributen untereinander erzeugt werden sollen.
     */
    public boolean getNomNomPraedErz() {
        return nomNomPraedErz;
    }

    /**
     * Gibt an, ob Prädikate zum Vergleich von int-Attributen untereinander
     * erzeugt werden sollen.
     *
     * @return  Den Wert, der angibt, ob Prädikate zum Vergleich von
     *          int-Attributen untereinander erzeugt werden sollen.
     */
    public boolean getIntIntPraedErz() {
        return intIntPraedErz;
    }

    /**
     * Gibt an, ob Prädikate zum Vergleich von real-Attributen untereinander
     * erzeugt werden sollen.
     *
     * @return  Den Wert, der angibt, ob Prädikate zum Vergleich von
     *          real-Attributen untereinander erzeugt werden sollen.
     */
    public boolean getRealRealPraedErz() {
        return realRealPraedErz;
    }

    /**
     * Gibt an, ob nur zu Halb-Intervallen Prädikate von Funktionen mit
     * ganzzahligen Werten erzeugt werden sollen. Falls nicht, werden
     * Prädikate zu Intervalle aller Art erzeugt.
     *
     * @return  Der Wert, der angibt, ob nur zu Halb-Intervallen Prädikate von
     *          Funktionen mit ganzzahligen Werten erzeugt werden sollen.
     *          Falls nicht, werden Prädikate zu Intervalle aller Art erzeugt.
     */
    public boolean getNurHalbIntItv() {
        return nurHalbIntItv;
    }

    /**
     * Gibt an, ob nur zu Halb-Intervallen Prädikate von Funktionen mit
     * Fließkomma-Werten erzeugt werden sollen. Falls nicht, werden Prädikate
     * zu Intervallen aller Art erzeugt.
     *
     * @return  Der Wert, der angibt, ob nur zu Halb-Intervallen Prädikate von
     *          Funktionen mit Fließkomma-Werten erzeugt werden sollen. Falls
     *          nicht, werden Prädikate zu Intervallen aller Art erzeugt.
     */
    public boolean getNurHalbRealItv() {
        return nurHalbRealItv;
    }

    /**
     * Gibt an, ob Prädikate mittels Hyperebenen zu den real-Attributen
     * erzeugt werden sollen.
     *
     * @return  Den Wert, der angibt, ob Prädikate mittels Hyperebenen zu den
     *          real-Attributen erzeugt werden sollen.
     */
    public boolean getHypEbenPraedErz() {
        return hypEbenPraedErz;
    }
}

