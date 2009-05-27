/*
 * Dateiname      : BoolWertPraedikat.java
 * Letzte Änderung: 21. Januar 2005
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


package lascer.praedikate.einzelne;

import lascer.problemdaten.attribute.NominalAttribut;
import lascer.praedikate.Praedikat;

/**
 * Definiert ein Prädikat, das den Wert eines boolschen Attributs mit einem
 * vorgegebenen Wert vergleicht. Ein boolsches Attribut ist ein nominales
 * Attribut, das genau zwei mögliche Werte besitzt.
 *
 * @author  Dietmar Lippold
 */
public class BoolWertPraedikat extends NomWertPraedikat implements Praedikat {

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param attribut  Das Attribut, dessen Wert verglichen werden soll.
     * @param wert      Der Wert, gegen den das Attribut verglichen werden
     *                  soll.
     */
    public BoolWertPraedikat(NominalAttribut attribut, String wert) {
        super(attribut, wert);
    }
}

