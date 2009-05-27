/*
 * Dateiname      : UnsupportedDataException.java
 * Letzte Änderung: 29. Mai 2005
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


package lascer.problemdaten;

/**
 * Diese Exception wird ausgelöst, wenn die Daten der zu verarbeitenden
 * Datei nicht verwendet werden können. Dies kann insbesondere dann der
 * Fall sein, wenn nicht genau ein nominales Attribut mit dem Namen
 * <EM>class</EM> vorhanden ist oder wenn diesen nicht ausschließlich die
 * beide Werte <EM>true</EM> und <EM>false</EM> besitzt.
 *
 * @author  Dietmar Lippold
 */
public class UnsupportedDataException extends Exception {

    /**
     * Erzeugt eine Instanz ohne Angaben zu den Gründen.
     */
    public UnsupportedDataException() {
        super();
    }

    /**
     * Erzeugt eine Instanz mit dem angegebenen Text als Grund.
     *
     * @param msg  Eine Beschreibung des Fehlers.
     */
    public UnsupportedDataException(String msg) {
        super(msg);
    }
}

