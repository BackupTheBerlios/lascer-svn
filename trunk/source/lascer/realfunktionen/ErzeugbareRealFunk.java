/*
 * Dateiname      : ErzeugbareRealFunk.java
 * Letzte Änderung: 25. Februar 2006
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


package lascer.realfunktionen;

import java.util.List;

/**
 * Definiert eine erzeugte real-Funktion, die Methoden besitzt, die nur für
 * die Erzeugung, nicht aber für die Benutzung der Funktion von Bedeutung
 * sind.
 *
 * @author  Dietmar Lippold
 */
public interface ErzeugbareRealFunk extends RealFunktion {

    /**
     * Gibt an, ob die Funktion sinnvoll ist.
     *
     * @return  Die Angabe, ob die Funktion sinnvoll ist.
     */
    public boolean istSinnvoll();

    /**
     * Liefert die Nummer der Funktion unter den Funktionen mit gleicher
     * Stelligkeit.
     *
     * @return  Die Nummer der Funktion.
     */
    public int nummer();

    /**
     * Liefert die Anzahl der elementaren Funktionen in der Gesamtfunktion.
     *
     * @return  Die Anzahl der elementaren Funktionen.
     */
    public int elementFunkAnz();

    /**
     * Liefert eine neu erzeugte Liste der von dieser Funktion verwendeten
     * Attribute.
     *
     * @return  Eine neu erzeugte Liste der von dieser Funktion verwendeten
     *          Attribute.
     */
    public List verwendeteAttribute();
}

