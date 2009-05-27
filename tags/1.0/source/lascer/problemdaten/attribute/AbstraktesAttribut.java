/*
 * Dateiname      : AbstraktesAttribut.java
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

import java.io.Serializable;

/**
 * Implementiert ein abstraktes Attribut.
 *
 * @author  Dietmar Lippold
 */
public abstract class AbstraktesAttribut implements Serializable {

    /**
     * Den Namen des Attributs.
     */
    private final String name;

    /**
     * Der berechnete hashCode dieses Objekts. Wenn der Wert Null ist, gilt
     * er als noch nicht berechnet.
     */
    private final int hashCode;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param name  Der Name des Attributs.
     */
    public AbstraktesAttribut(String name) {
        this.name = name;
        hashCode = name.hashCode();
    }

    /**
     * Liefert den hashCode dieses Objekts.
     *
     * @return  Den hashCode dieses Objekts.
     */
    public int hashCode() {
        return hashCode();
    }

    /**
     * Ermittelt, ob das übergebene Objekt gleich ist zu diesem Objekt.
     *
     * @param objekt  Das Objekt, das mit diesem Objekt verglichen werden
     *                soll.
     *
     * @return  <CODE>true</CODE> genau dann, wenn das übergebene Objekt
     *          gleich ist zu diesem Objekt, ansonsten <CODE>false</CODE>.
     */
    public boolean equals(Object objekt) {
        if (objekt == null) {
            return false;
        }
        if (objekt == this) {
            return true;
        }
        if (!objekt.getClass().equals(this.getClass())) {
            return false;
        }
        AbstraktesAttribut anderesAttribut = (AbstraktesAttribut) objekt;
        return anderesAttribut.name.equals(name);
    }

    /**
     * Liefert den Namen des Attributs.
     *
     * @return  Den Namen des Attributs.
     */
    public String name() {
        return name;
    }

    /**
     * Liefert eine Beschreibung des Attributs. Diese besteht aus dem Namen
     * und dem Typ des Attributs, wie er in einer arff-Datei angegeben ist,
     * getrennt durch ein Leerzeichen.
     *
     * @return  Eine Beschreibung des Attributs.
     */
    public abstract String toString();
}

