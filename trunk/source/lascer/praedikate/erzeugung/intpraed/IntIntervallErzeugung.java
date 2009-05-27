/*
 * Dateiname      : IntIntervallErzeugung.java
 * Letzte Änderung: 03. Mai 2006
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


package lascer.praedikate.erzeugung.intpraed;

import java.util.LinkedList;
import java.io.Serializable;

/**
 * Definiert Methoden zur Erzeugung von Intervallen aus geordneten
 * ganzzahligen Werten.
 *
 * @author  Dietmar Lippold
 */
public interface IntIntervallErzeugung extends Serializable {

    /**
     * Liefert eine Liste mit Intervallen, die alle inklusiven Werte aber
     * nicht alle exklusiven Werte enthalten, falls so eines existiert.
     *
     * @param inklusivWerte  Ein aufsteigend sortiertes Array der inklusiven
     *                       Werte.
     * @param exklusivWerte  Ein aufsteigend sortiertes Array der exklusiven
     *                       Werte.
     *
     * @return  Eine Liste mit den erzeugten vollständigen Intervallen vom Typ
     *          <CODE>IntIntervall</CODE> oder eine leere Liste, wenn es kein
     *          sinnvolles vollständiges Intervall gibt.
     */
    public LinkedList vollstaendigeIntervalle(int[] inklusivWerte,
                                              int[] exklusivWerte);

    /**
     * Liefert alle Intervalle, die maximal viele inklusive Werte aber keinen
     * exklusiven Wert enthalten. Wenn es keinen inklusiven Wert gibt, wird
     * eine leere Liste geliefert.
     *
     * @param inklusivWerte  Ein aufsteigend sortiertes Array der inklusiven
     *                       Werte.
     * @param exklusivWerte  Ein aufsteigend sortiertes Array der exklusiven
     *                       Werte.
     *
     * @return  Eine Liste der erzeugten korrekten Intervalle vom Typ
     *          <CODE>IntIntervall</CODE>.
     */
    public LinkedList korrekteIntervalle(int[] inklusivWerte,
                                         int[] exklusivWerte);

    /**
     * Liefert alle möglichen Intervalle der Werte aus
     * <CODE>inklusivWerte</CODE>, ohne die vollständigen, die korrekten und
     * die nutzlosen Intervalle. Ein Intervall ist nutzlos, wenn es ein
     * anderes Intervall mit gleicher oder geringerer Komplexität gibt, das
     * gleich viele oder mehr Werte aus <CODE>inklusivWerte</CODE> und gleich
     * viele oder weniger Werte aus <CODE>exklusivWerte</CODE> enthält.
     *
     * @param inklusivWerte  Ein aufsteigend sortiertes Array der inklusiven
     *                       Werte.
     * @param exklusivWerte  Ein aufsteigend sortiertes Array der exklusiven
     *                       Werte.
     *
     * @return  Eine Liste der erzeugten allgemeinen Intervalle vom Typ
     *          <CODE>IntIntervall</CODE>.
     */
    public LinkedList allgemeineIntervalle(int[] inklusivWerte,
                                           int[] exklusivWerte);
}

