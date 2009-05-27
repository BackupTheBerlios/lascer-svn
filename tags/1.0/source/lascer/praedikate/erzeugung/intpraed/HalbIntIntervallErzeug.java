/*
 * Dateiname      : HalbIntIntervallErzeug.java
 * Letzte Änderung: 22. März 2006
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

/**
 * Stellt Methoden zur Erzeugung von Intervallen aus geordneten ganzzahligen
 * Werten zur Verfügung, wobei nur Halb-Intervalle erzeugt werden.
 *
 * @author  Dietmar Lippold
 */
public class HalbIntIntervallErzeug implements IntIntervallErzeugung {

    /**
     * Liefert eine Liste mit Halb-Intervallen, die alle inklusiven Werte aber
     * nicht alle exklusiven Werte enthalten, falls so eines existiert.
     *
     * @param inklusivWerte  Ein aufsteigend sortiertes Array der inklusiven
     *                       Werte.
     * @param exklusivWerte  Ein aufsteigend sortiertes Array der exklusiven
     *                       Werte.
     *
     * @return  Eine Liste mit den erzeugten vollständigen Halb-Intervallen
     *          vom Typ <CODE>IntIntervall</CODE> oder eine leere Liste, wenn
     *          es kein sinnvolles vollständiges Halb-Intervall gibt.
     */
    public LinkedList vollstaendigeIntervalle(int[] inklusivWerte,
                                              int[] exklusivWerte) {
        LinkedList intervalle;
        int        minInklWert, minExklWert, maxInklWert, maxExklWert;
        int        inklWertAnz, exklWertAnz;

        intervalle = new LinkedList();
        inklWertAnz = inklusivWerte.length;
        exklWertAnz = exklusivWerte.length;

        if (inklWertAnz == 0) {
            // Es gibt kein sinnvolles vollständiges Intervall.
            return intervalle;
        } else if (exklWertAnz == 0) {
            // Es gibt keinen exklusiven Wert.
            minInklWert = inklusivWerte[0];
            maxInklWert = inklusivWerte[inklWertAnz - 1];
            intervalle.add(new IntIntervall(Konstanten.MIN_WERT, maxInklWert));
            intervalle.add(new IntIntervall(minInklWert, Konstanten.MAX_WERT));
            return intervalle;
        } else {
            // Es gibt sowohl inklusive wie exklusive Werte. Nun ermitteln,
            // welche Arten von Intervallen zu erzeugen sind.
            minInklWert = inklusivWerte[0];
            minExklWert = exklusivWerte[0];
            maxInklWert = inklusivWerte[inklWertAnz - 1];
            maxExklWert = exklusivWerte[exklWertAnz - 1];

            // Es existiert nur dann ein sinnvolles Intervall, wenn nicht alle
            // exklusiv-Werte im Intervall der inklusiv-Werte enthalten sind.
            if ((minExklWert < minInklWert) || (maxInklWert < maxExklWert)) {
                if (minInklWert <= minExklWert) {
                    // Es braucht kein Halb-Intervall mit Untergrenze erzeugt
                    // zu werden.
                    intervalle.add(new IntIntervall(Konstanten.MIN_WERT, maxInklWert));
                } else if (maxExklWert <= maxInklWert) {
                    // Es braucht kein Halb-Intervall mit Obergrenze erzeugt
                    // zu werden.
                    intervalle.add(new IntIntervall(minInklWert, Konstanten.MAX_WERT));
                } else {
                    intervalle.add(new IntIntervall(Konstanten.MIN_WERT, maxInklWert));
                    intervalle.add(new IntIntervall(minInklWert, Konstanten.MAX_WERT));
                }
            }
            return intervalle;
        }
    }

    /**
     * Liefert alle Halb-Intervalle, die maximal viele inklusive Werte aber
     * keinen exklusiven Wert enthalten. Wenn es keinen inklusiven Wert gibt,
     * wird eine leere Liste geliefert.
     *
     * @param inklusivWerte  Ein aufsteigend sortiertes Array der inklusiven
     *                       Werte.
     * @param exklusivWerte  Ein aufsteigend sortiertes Array der exklusiven
     *                       Werte.
     *
     * @return  Eine Liste der erzeugten korrekten Halb-Intervalle vom Typ
     *          <CODE>IntIntervall</CODE>.
     */
    public LinkedList korrekteIntervalle(int[] inklusivWerte,
                                         int[] exklusivWerte) {
        LinkedList intervalle;
        int        minInklWert, maxInklWert;
        int        minExklWert, maxExklWert;
        int        inklWertAnz, exklWertAnz;
        int        inklIndex;

        intervalle = new LinkedList();
        inklWertAnz = inklusivWerte.length;
        exklWertAnz = exklusivWerte.length;

        // Test, ob inklusivWerte oder exklusivWerte leer ist.
        if (inklWertAnz == 0) {
            return intervalle;
        } else if (exklWertAnz == 0) {
            minInklWert = inklusivWerte[0];
            maxInklWert = inklusivWerte[inklWertAnz - 1];
            intervalle.add(new IntIntervall(Konstanten.MIN_WERT, maxInklWert));
            intervalle.add(new IntIntervall(minInklWert, Konstanten.MAX_WERT));
            return intervalle;
        }

        // Alle Werte aus inklusivWerte, die kleiner sind als der kleinste
        // Wert aus exklusivWerte, zu einem Halb-Intervall zusammenfassen.
        inklIndex = 0;
        minExklWert = exklusivWerte[0];
        while ((inklIndex + 1 < inklWertAnz)
               && (inklusivWerte[inklIndex + 1] < minExklWert)) {
            inklIndex++;
        }
        if (inklusivWerte[inklIndex] < minExklWert) {
            intervalle.add(new IntIntervall(Konstanten.MIN_WERT,
                                            inklusivWerte[inklIndex]));
        }

        // Alle Werte aus inklusivWerte, die größer sind als der größte Wert
        // aus exklusivWerte, zu einem Halb-Intervall zusammenfassen.
        inklIndex = inklWertAnz - 1;
        maxExklWert = exklusivWerte[exklWertAnz - 1];
        while ((inklIndex - 1 >= 0)
               && (inklusivWerte[inklIndex - 1] > maxExklWert)) {
            inklIndex--;
        }
        if (inklusivWerte[inklIndex] > maxExklWert) {
            intervalle.add(new IntIntervall(inklusivWerte[inklIndex],
                                            Konstanten.MAX_WERT));
        }

        return intervalle;
    }

    /**
     * Liefert alle möglichen Halb-Intervalle der Werte aus
     * <CODE>inklusivWerte</CODE>, ohne die vollständigen, die korrekten und
     * die nutzlosen Intervalle. Ein Halb-Intervall ist nutzlos, wenn es ein
     * anderes Halb-Intervall gibt, das gleich viele oder mehr Werte aus
     * <CODE>inklusivWerte</CODE> und gleich viele oder weniger Werte aus
     * <CODE>exklusivWerte</CODE> enthält.
     *
     * @param inklusivWerte  Ein aufsteigend sortiertes Array der inklusiven
     *                       Werte.
     * @param exklusivWerte  Ein aufsteigend sortiertes Array der exklusiven
     *                       Werte.
     *
     * @return  Eine Liste der erzeugten allgemeinen Halb-Intervalle vom Typ
     *          <CODE>IntIntervall</CODE>.
     */
    public LinkedList allgemeineIntervalle(int[] inklusivWerte,
                                           int[] exklusivWerte) {
        LinkedList intervalle;
        int        minExklWert, maxExklWert;
        int        inklWertAnz, exklWertAnz;
        int        inklIndex, exklIndex;
        boolean    intervallVorhanden;

        intervalle = new LinkedList();
        inklWertAnz = inklusivWerte.length;
        exklWertAnz = exklusivWerte.length;

        if ((inklWertAnz == 0) || (exklWertAnz == 0)) {
            // Es gibt gar kein Intervall bzw. alle Intervalle sind korrekt.
            return intervalle;
        }

        if ((inklWertAnz == 1) || (exklWertAnz == 1)) {
            // Die Halb-Intervalle sind vollständig bzw. sind sie entweder
            // korrekt oder nicht sinnvoll, da sie alle exklusiv-Werte (den
            // einen vorhandenen) enthalten.
            return intervalle;
        }

        minExklWert = exklusivWerte[0];
        maxExklWert = exklusivWerte[exklWertAnz - 1];

        // Halb-Intervalle mit Untergrenze erzeugen.
        inklIndex = 0;
        exklIndex = 0;

        intervallVorhanden = true;
        while (intervallVorhanden) {
            /*
             * Es gilt entweder (exklIndex == 0) oder es gelten folgende
             * Bedingungen:
             *  + inklusivWerte[inklIndex] <= maxExklWert
             *  + inklusivWerte[inklIndex] > exklusivWerte[exklIndex]
             *  + inklusivWerte[inklIndex] <= exklusivWerte[exklIndex + 1]
             */

            // Nächsten exklIndex ermitteln, so daß der exklusiv-Wert gleich
            // oder größer dem inklusiv-Wert ist.
            while ((exklIndex < exklWertAnz - 1)
                   && (exklusivWerte[exklIndex] < inklusivWerte[inklIndex])) {
                exklIndex++;
            }

            if (exklusivWerte[exklIndex] < inklusivWerte[inklIndex]) {
                // Es gibt keinen weiteren exklIndex.
                intervallVorhanden = false;
            } else {
                // Nächsten inklIndex ermitteln, so daß der inklusiv-Wert
                // größer als der exklusiv-Wert ist.
                while ((inklIndex < inklWertAnz - 1)
                       && (inklusivWerte[inklIndex] <= exklusivWerte[exklIndex])) {
                    inklIndex++;
                }

                if (inklusivWerte[inklIndex] <= exklusivWerte[exklIndex]) {
                    // Es gibt keinen weiteren inklIndex.
                    intervallVorhanden = false;
                } else if (inklusivWerte[inklIndex] > maxExklWert) {
                    // Es gibt nur noch ein korrektes Intervall.
                    intervallVorhanden = false;
                } else {
                    // Das neue Intervall aufnehmen.
                    intervalle.add(new IntIntervall(inklusivWerte[inklIndex],
                                                    Konstanten.MAX_WERT));
                }
            }
        }

        // Halb-Intervalle mit Obergrenze erzeugen.
        inklIndex = inklWertAnz - 1;
        exklIndex = exklWertAnz - 1;

        intervallVorhanden = true;
        while (intervallVorhanden) {
            /*
             * Es gilt entweder (exklIndex == exklWertAnz - 1) oder es gelten
             * folgende Bedingungen:
             *  + inklusivWerte[inklIndex] >= minExklWert
             *  + inklusivWerte[inklIndex] < exklusivWerte[exklIndex]
             *  + inklusivWerte[inklIndex] >= exklusivWerte[exklIndex - 1]
             */

            // Nächsten exklIndex ermitteln, so daß der exklusiv-Wert gleich
            // oder kleiner dem inklusiv-Wert ist.
            while ((exklIndex > 0)
                   && (exklusivWerte[exklIndex] > inklusivWerte[inklIndex])) {
                exklIndex--;
            }

            if (exklusivWerte[exklIndex] > inklusivWerte[inklIndex]) {
                // Es gibt keinen weiteren exklIndex.
                intervallVorhanden = false;
            } else {
                // Nächsten inklIndex ermitteln, so daß der inklusiv-Wert
                // kleiner als der exklusiv-Wert ist.
                while ((inklIndex > 0)
                       && (inklusivWerte[inklIndex] >= exklusivWerte[exklIndex])) {
                    inklIndex--;
                }

                if (inklusivWerte[inklIndex] >= exklusivWerte[exklIndex]) {
                    // Es gibt keinen weiteren inklIndex.
                    intervallVorhanden = false;
                } else if (inklusivWerte[inklIndex] < minExklWert) {
                    // Es gibt nur noch ein korrektes Intervall.
                    intervallVorhanden = false;
                } else {
                    // Das neue Intervall aufnehmen.
                    intervalle.add(new IntIntervall(Konstanten.MIN_WERT,
                                                    inklusivWerte[inklIndex]));
                }
            }
        }

        return intervalle;
    }

    /**
     * Liefert eine textuelle Darstellung des übergebenen Arrays.
     *
     * @param werte  Ein Array, dessen textuelle Darstellung geliefert werden
     *               soll.
     *
     * @return  Eine textuelle Darstellung des übergebenen Arrays.
     */
    public static String darstellung(int[] werte) {
        StringBuffer text = new StringBuffer();

        if (werte.length > 0) {
            for (int i = 0; i < werte.length; i++) {
                text.append(werte[i] + ", ");
            }

            text.deleteCharAt(text.length() - 1);
            text.deleteCharAt(text.length() - 1);
        }
        return text.toString();
    }

    /**
     * Testet die Methoden mit einigen Testdaten.
     *
     * @param args  Ein leeres Array der Kommandozeilen-Argumente.
     */
    public static void main(String[] args) {
        IntIntervallErzeugung intErz     = new HalbIntIntervallErzeug();
        int[]                 inklWerte;
        int[]                 exklWerte;

        inklWerte = new int[] {};
        exklWerte = new int[] {0, 3};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollständige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new int[] {0, 1, 2, 3, 5};
        exklWerte = new int[] {};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollständige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new int[] {0, 2, 2, 3};
        exklWerte = new int[] {0, 2, 3};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollständige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new int[] {0, 1, 2, 3, 5};
        exklWerte = new int[] {0, 3, 3};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollständige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new int[] {0, 1, 2, 3, 5};
        exklWerte = new int[] {2, 3, 3};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollständige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new int[] {0, 1, 3, 5};
        exklWerte = new int[] {1, 2, 4, 6};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollständige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new int[] {1, 3, 5, 7};
        exklWerte = new int[] {1, 2, 4, 6};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollständige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new int[] {-5, -3, -3, -2, 1};
        exklWerte = new int[] {-6, -6, -2, -1, 1};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollständige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new int[] {0, 1, 2, 3, 5};
        exklWerte = new int[] {6, 7, 7};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollständige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new int[] {1, 2, 3, 5};
        exklWerte = new int[] {0, 7, 7};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollständige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();
    }
}

