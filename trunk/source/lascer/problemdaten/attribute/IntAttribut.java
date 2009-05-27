/*
 * Dateiname      : IntAttribut.java
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
 * Definiert ein ganzzahliges Attribut.
 *
 * @author  Dietmar Lippold
 */
public class IntAttribut extends AbstraktesAttribut {

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param name  Der Name des Attributs.
     */
    public IntAttribut(String name) {
        super(name);
    }

    /**
     * Liefert eine Beschreibung des Attributs. Diese besteht aus dem Namen
     * und dem Typ des Attributs, wie er in einer arff-Datei angegeben ist,
     * getrennt durch ein Leerzeichen.
     *
     * @return  Eine Beschreibung des Attributs.
     */
    public String toString() {
        return (name() + " integer");
    }
}

