/*
 * Dateiname      : NominalAttribut.java
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


package lascer.problemdaten.attribute;

/**
 * Implementiert ein nominales Attribut.
 *
 * @author  Dietmar Lippold
 */
public class NominalAttribut extends AbstraktesAttribut {

    /**
     * Die möglichen Werte des Attributs.
     */
    private String[] werte;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param name   Der Name des Attributs.
     * @param werte  Die möglichen Werte des Attributs.
     */
    public NominalAttribut(String name, String[] werte) {
        super(name);

        this.werte = (String[]) werte.clone();
    }

    /**
     * Liefert den Wert des Attributs, der gleich ist zum übergebenen Wert.
     *
     * @param wert  Der Wert, zu dem der gleiche vorhandene Wert geliefert
     *              werden soll.
     *
     * @return  Den Wert des Attributs, der gleich ist zum übergebenen Wert.
     *          Falls es einen solchen nicht gibt, wird <CODE>null</CODE>
     *          geliefert.
     */
    public String wert(String wert) {

        for (int wNr = 0; wNr < werte.length; wNr++) {
            if (werte[wNr].equals(wert)) {
                return werte[wNr];
            }
        }
        return null;
    }

    /**
     * Liefert die möglichen Werte des Attributs.
     *
     * @return  Die möglichen Werte des Attributs.
     */
    public String[] werte() {
        return werte;
    }

    /**
     * Liefert eine Beschreibung des Attributs. Diese besteht aus dem Namen
     * und dem Typ des Attributs, wie er in einer arff-Datei angegeben ist,
     * getrennt durch ein Leerzeichen.
     *
     * @return  Eine Beschreibung des Attributs.
     */
    public String toString() {
        StringBuffer beschreibung = new StringBuffer();

        beschreibung.append(name());
        beschreibung.append(" {");
        for (int wNr = 0; wNr < werte.length - 1; wNr++) {
            beschreibung.append(werte[wNr] + ", ");
        }
        beschreibung.append(werte[werte.length - 1] + "}");

        return beschreibung.toString();
    }
}

