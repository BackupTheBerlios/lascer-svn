/*
 * Dateiname      : ArchiLoesung.java
 * Letzte Änderung: 05. Mai 2006
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


package lascer.konzepte;

import java.util.HashSet;
import java.io.Serializable;

/**
 * Realisiert einen Behälter für eine Teillösung oder Lösung bei der
 * Verwendung von Architeuthis.
 *
 * @author  Dietmar Lippold
 */
public class ArchiLoesung implements Serializable {

    /**
     * Die Menge der erzeugten Konzepte aus der (Teil-)Lösung.
     */
    private HashSet erzeugteKonzepte;

    /**
     * Die beste Formel der (Teil-)Lösung.
     */
    private KombiKonzept besteFormel;

    /**
     * Die Angabe der (Teil-)Lösung, ob bei einem erneuter Aufruf eine
     * weitere Verbesserung möglich ist.
     */
    private boolean weiterVerbesserMoeglich;

    /**
     * Erzeugt zu den übergebenen Werten eine neue Instanz.
     *
     * @param erzeugteKonzepte         Die Menge der erzeugten Konzepte aus
     *                                 der (Teil-)Lösung.
     * @param besteFormel              Die beste Formel der (Teil-)Lösung.
     * @param weiterVerbesserMoeglich  Die Angabe der (Teil-)Lösung, ob bei
     *                                 einem erneuter Aufruf eine weitere
     *                                 Verbesserung möglich ist.
     */
    public ArchiLoesung(HashSet erzeugteKonzepte, KombiKonzept besteFormel,
                        boolean weiterVerbesserMoeglich) {

        this.erzeugteKonzepte = erzeugteKonzepte;
        this.besteFormel = besteFormel;
        this.weiterVerbesserMoeglich = weiterVerbesserMoeglich;
    }

    /**
     * Liefert die Menge der erzeugten Konzepte aus der (Teil-)Lösung.
     *
     * @return  Die Menge der erzeugten Konzepte aus der (Teil-)Lösung.
     */
    public HashSet erzeugteKonzepte() {
        return erzeugteKonzepte;
    }

    /**
     * Liefert die beste Formel der (Teil-)Lösung.
     *
     * @return  Die beste Formel der (Teil-)Lösung.
     */
    public KombiKonzept besteFormel() {
        return besteFormel;
    }

    /**
     * Liefert die Angabe der (Teil-)Lösung, ob bei einem erneuter Aufruf eine
     * weitere Verbesserung möglich ist.
     *
     * @return  Die Angabe der (Teil-)Lösung, ob bei einem erneuter Aufruf
     *          eine weitere Verbesserung möglich ist.
     */
    public boolean weiterVerbesserMoeglich() {
        return weiterVerbesserMoeglich;
    }
}

