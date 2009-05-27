/*
 * Dateiname      : KombiKonzept.java
 * Letzte Änderung: 01. November 2005
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


package lascer.konzepte;

import java.util.Collection;

/**
 * Definiert ein Konzept, das eine Kombination aus mehreren Teilkonzepten ist.
 *
 * @author  Dietmar Lippold
 */
public interface KombiKonzept extends Konzept {

    /**
     * Liefert eine flache Kopie dieses Konzepts.
     *
     * @return  Eine flache Kopie dieses Konzepts.
     */
    public Object clone();

    /**
     * Nimmt das übergebene Konzept als Teilkonzept in dieses Konzept auf.
     * Wenn es schon enthalten ist, ändert sich nichts.
     *
     * @param konzept  Das aufzunehmende Konzept.
     */
    public void aufnehmen(Konzept konzept);

    /**
     * Nimmt die übergebenen Konzepte als Teilkonzept in dieses Konzept auf,
     * soweit sie noch nicht enthalten sind.
     *
     * @param konzepte  Die Konzepten, die aufgenommen werden sollen.
     */
    public void aufnehmen(Collection konzepte);

    /**
     * Entfernt das übergebene Konzept als Teilkonzept aus diesem Konzept.
     * Wenn es nicht enthalten ist, ändert sich nichts.
     *
     * @param konzept  Das zu entfernende Konzept.
     */
    public void entfernen(Konzept konzept);
}

