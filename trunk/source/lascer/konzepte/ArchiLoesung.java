/*
 * Dateiname      : ArchiLoesung.java
 * Letzte �nderung: 05. Mai 2006
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
 * Realisiert einen Beh�lter f�r eine Teill�sung oder L�sung bei der
 * Verwendung von Architeuthis.
 *
 * @author  Dietmar Lippold
 */
public class ArchiLoesung implements Serializable {

    /**
     * Die Menge der erzeugten Konzepte aus der (Teil-)L�sung.
     */
    private HashSet erzeugteKonzepte;

    /**
     * Die beste Formel der (Teil-)L�sung.
     */
    private KombiKonzept besteFormel;

    /**
     * Die Angabe der (Teil-)L�sung, ob bei einem erneuter Aufruf eine
     * weitere Verbesserung m�glich ist.
     */
    private boolean weiterVerbesserMoeglich;

    /**
     * Erzeugt zu den �bergebenen Werten eine neue Instanz.
     *
     * @param erzeugteKonzepte         Die Menge der erzeugten Konzepte aus
     *                                 der (Teil-)L�sung.
     * @param besteFormel              Die beste Formel der (Teil-)L�sung.
     * @param weiterVerbesserMoeglich  Die Angabe der (Teil-)L�sung, ob bei
     *                                 einem erneuter Aufruf eine weitere
     *                                 Verbesserung m�glich ist.
     */
    public ArchiLoesung(HashSet erzeugteKonzepte, KombiKonzept besteFormel,
                        boolean weiterVerbesserMoeglich) {

        this.erzeugteKonzepte = erzeugteKonzepte;
        this.besteFormel = besteFormel;
        this.weiterVerbesserMoeglich = weiterVerbesserMoeglich;
    }

    /**
     * Liefert die Menge der erzeugten Konzepte aus der (Teil-)L�sung.
     *
     * @return  Die Menge der erzeugten Konzepte aus der (Teil-)L�sung.
     */
    public HashSet erzeugteKonzepte() {
        return erzeugteKonzepte;
    }

    /**
     * Liefert die beste Formel der (Teil-)L�sung.
     *
     * @return  Die beste Formel der (Teil-)L�sung.
     */
    public KombiKonzept besteFormel() {
        return besteFormel;
    }

    /**
     * Liefert die Angabe der (Teil-)L�sung, ob bei einem erneuter Aufruf eine
     * weitere Verbesserung m�glich ist.
     *
     * @return  Die Angabe der (Teil-)L�sung, ob bei einem erneuter Aufruf
     *          eine weitere Verbesserung m�glich ist.
     */
    public boolean weiterVerbesserMoeglich() {
        return weiterVerbesserMoeglich;
    }
}

