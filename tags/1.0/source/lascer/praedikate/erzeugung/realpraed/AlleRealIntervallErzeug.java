/*
 * Dateiname      : AlleRealIntervallErzeug.java
 * Letzte �nderung: 22. M�rz 2006
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


package lascer.praedikate.erzeugung.realpraed;

import java.util.LinkedList;

/**
 * Stellt Methoden zur Erzeugung von Intervallen aus geordneten
 * Flie�komma-Werten zur Verf�gung. Die Erzeugung erfolgt unter der
 * Voraussetzung, da� die Komplexit�t eines Einzelwert-Intervall-Pr�dikats
 * kleiner oder gleich ist wie die eines Halb-Intervall-Pr�dikats und diese
 * wiederum kleiner oder gleich ist wie die eines Voll-Intervall-Pr�dikats.
 *
 * @author  Dietmar Lippold
 */
public class AlleRealIntervallErzeug implements RealIntervallErzeugung {

    /**
     * Liefert eine Liste mit Intervallen, die alle inklusiven Werte aber
     * nicht alle exklusiven Werte enthalten, falls so eines existiert.
     *
     * @param inklusivWerte  Ein aufsteigend sortiertes Array der inklusiven
     *                       Werte.
     * @param exklusivWerte  Ein aufsteigend sortiertes Array der exklusiven
     *                       Werte.
     *
     * @return  Eine Liste mit den erzeugten vollst�ndigen Intervallen vom Typ
     *          <CODE>RealIntervall</CODE> oder eine leere Liste, wenn es kein
     *          sinnvolles vollst�ndiges Intervall gibt.
     */
    public LinkedList vollstaendigeIntervalle(float[] inklusivWerte,
                                              float[] exklusivWerte) {
        LinkedList intervalle;
        float      minInklWert, minExklWert, maxInklWert, maxExklWert;
        int        inklWertAnz, exklWertAnz;

        intervalle = new LinkedList();
        inklWertAnz = inklusivWerte.length;
        exklWertAnz = exklusivWerte.length;

        if (inklWertAnz == 0) {
            // Es gibt kein sinnvolles vollst�ndiges Intervall.
            return intervalle;
        } else if (exklWertAnz == 0) {
            // Es gibt keinen exklusiven Wert.
            minInklWert = inklusivWerte[0];
            maxInklWert = inklusivWerte[inklWertAnz - 1];
            if (minInklWert == maxInklWert) {
                // Das vollst�ndige Intervall besteht nur aus einem Wert.
                intervalle.add(new RealIntervall(minInklWert, minInklWert));
            } else {
                // Das vollst�ndige Intervall besteht aus mehreren Werten.
                intervalle.add(new RealIntervall(Konstanten.MIN_WERT, maxInklWert));
                intervalle.add(new RealIntervall(minInklWert, Konstanten.MAX_WERT));
            }
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
                if (minInklWert == maxInklWert) {
                    // Das vollst�ndige Intervall besteht nur aus einem Wert.
                    intervalle.add(new RealIntervall(minInklWert, minInklWert));
                } else if (minInklWert <= minExklWert) {
                    // Es braucht kein Intervall mit Untergrenze erzeugt zu
                    // werden.
                    intervalle.add(new RealIntervall(Konstanten.MIN_WERT, maxInklWert));
                } else if (maxExklWert <= maxInklWert) {
                    // Es braucht kein Intervall mit Obergrenze erzeugt zu
                    // werden.
                    intervalle.add(new RealIntervall(minInklWert, Konstanten.MAX_WERT));
                } else {
                    intervalle.add(new RealIntervall(Konstanten.MIN_WERT, maxInklWert));
                    intervalle.add(new RealIntervall(minInklWert, Konstanten.MAX_WERT));
                    intervalle.add(new RealIntervall(minInklWert, maxInklWert));
                }
            }
            return intervalle;
        }
    }

    /**
     * Lifert eine Liste aller Intervalle aus einzelnen Werten des
     * �bergebenen Arrays. Zu mehreren gleichen Werten wird nur ein Intervall
     * erzeugt.
     *
     * @param werte     Ein aufsteigend sortiertes Array der Werte, zu denen
     *                  teilweise ein Intervall aufgenommen werden soll.
     * @param minIndex  Der kleinste Index eines Wertes, zu dem ein
     *                  Intervall aufgenommen werden soll.
     * @param maxIndex  Der gr��te Index eines Wertes, zu dem ein Intervall
     *                  aufgenommen werden soll.
     *
     * @return  Eine Liste mit den erzeugten Einzelwert-Intervallen.
     */
    private LinkedList korrEwIntervalle(float[] werte,
                                        int minIndex, int maxIndex) {
        LinkedList intervalle;
        float      wert;
        int        index;

        intervalle = new LinkedList();
        index = minIndex;
        while (index <= maxIndex) {
            wert = werte[index];
            intervalle.add(new RealIntervall(wert, wert));
            do {
                index++;
            } while ((index <= maxIndex) && (werte[index] == wert));
        }
        return intervalle;
    }

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
     *          <CODE>RealIntervall</CODE>.
     */
    public LinkedList korrekteIntervalle(float[] inklusivWerte,
                                         float[] exklusivWerte) {
        LinkedList intervalle;
        float      minInklWert, maxInklWert;
        float      inklWert, exklWert;
        int        inklWertAnz, exklWertAnz;
        int        inklIndex, exklIndex, unterIndex;

        intervalle = new LinkedList();
        inklWertAnz = inklusivWerte.length;
        exklWertAnz = exklusivWerte.length;

        // Test, ob inklusivWerte oder exklusivWerte leer ist.
        if (inklWertAnz == 0) {
            return intervalle;
        } else if (exklWertAnz == 0) {
            minInklWert = inklusivWerte[0];
            maxInklWert = inklusivWerte[inklWertAnz - 1];
            if (minInklWert == maxInklWert) {
                intervalle.add(new RealIntervall(maxInklWert, maxInklWert));
            } else {
                intervalle.add(new RealIntervall(Konstanten.MIN_WERT, maxInklWert));
                intervalle.add(new RealIntervall(minInklWert, Konstanten.MAX_WERT));
                intervalle.addAll(korrEwIntervalle(inklusivWerte,
                                                   0, inklWertAnz - 1));
            }
            return intervalle;
        }

        // Alle Werte aus inklusivWerte, die kleiner sind als der kleinste
        // Wert aus exklusivWerte, zu einem Halb-Intervall zusammenfassen
        // und als Einzelwert-Intervalle aufnehmen.
        inklIndex = 0;
        exklIndex = 0;
        exklWert = exklusivWerte[0];
        while ((inklIndex + 1 < inklWertAnz)
               && (inklusivWerte[inklIndex + 1] < exklWert)) {
            inklIndex++;
        }
        if (inklusivWerte[inklIndex] < exklWert) {
            if (inklusivWerte[inklIndex] == inklusivWerte[0]) {
                // Das Intervall besteht nur aus einem Wert.
                intervalle.add(new RealIntervall(inklusivWerte[0],
                                                 inklusivWerte[0]));
            } else {
                // Das Intervall besteht aus mehreren Werten.
                intervalle.add(new RealIntervall(Konstanten.MIN_WERT,
                                                 inklusivWerte[inklIndex]));
                intervalle.addAll(korrEwIntervalle(inklusivWerte, 0, inklIndex));
            }
        }

        // Die Intervalle ermitteln, die oberhalb des kleinsten Wertes von
        // exklusivWerte liegen.
        while (inklIndex + 1 < inklWertAnz) {
            // Den n�chsten inkl-Wert suchen, der gr��er ist als exklWert.
            inklWert = inklusivWerte[inklIndex];
            while ((inklWert <= exklWert) && (inklIndex + 1 < inklWertAnz)) {
                inklIndex++;
                inklWert = inklusivWerte[inklIndex];
            }

            // Finde den n�chsten Wert aus exklusivWerte, der gleich oder
            // gr��er inklWert ist.
            while ((exklWert < inklWert) && (exklIndex + 1 < exklWertAnz)) {
                exklIndex++;
                exklWert = exklusivWerte[exklIndex];
            }

            if (inklWert != exklWert) {
                // Es wurde ein Wert aus inklusivWerte gefunden, der nicht
                // in exklusivWerte vorkommt.
                unterIndex = inklIndex;

                // Finde die Obergrenze des Intervalls.
                if (exklWert < inklWert) {
                    // Alle weiteren Werte geh�ren zu einem Intervall.
                    inklIndex = inklWertAnz - 1;
                    if (inklusivWerte[unterIndex] == inklusivWerte[inklIndex]) {
                        // Das Intervall besteht nur aus einem Wert.
                        intervalle.add(new RealIntervall(inklusivWerte[inklIndex],
                                                         inklusivWerte[inklIndex]));
                    } else {
                        // Das Intervall besteht aus mehreren Werten.
                        intervalle.add(new RealIntervall(inklusivWerte[unterIndex],
                                                         Konstanten.MAX_WERT));
                        intervalle.addAll(korrEwIntervalle(inklusivWerte,
                                                           unterIndex, inklIndex));
                    }
                } else {
                    // Finde den gr��ten Wert aus inklusivWerte, der kleiner
                    // ist als exklWert.
                    while ((inklIndex + 1 < inklWertAnz)
                           && (inklusivWerte[inklIndex + 1] < exklWert)) {
                        inklIndex++;
                    }
                    if (inklusivWerte[unterIndex] != inklusivWerte[inklIndex]) {
                        // Das Intervall besteht aus mehr als einem Wert.
                        intervalle.add(new RealIntervall(inklusivWerte[unterIndex],
                                                         inklusivWerte[inklIndex]));
                    }
                    intervalle.addAll(korrEwIntervalle(inklusivWerte,
                                                       unterIndex, inklIndex));
                }
            }
        }
        return intervalle;
    }

    /**
     * Liefert eine Liste aller Einzelwert-Intervalle, bei denen der Wert
     * sowohl in den inklusiv-Werten wie in den exklusiv-Werten enthalten ist.
     *
     * @param inklusivWerte  Ein aufsteigend sortiertes Array der inklusiven
     *                       Werte.
     * @param exklusivWerte  Ein aufsteigend sortiertes Array der exklusiven
     *                       Werte.
     *
     * @return  Eine Liste mit den erzeugten Einzelwert-Intervallen.
     */
    private LinkedList algEwIntervalle(float[] inklusivWerte,
                                       float[] exklusivWerte) {
        LinkedList intervalle;
        float      inklWert;
        int        inklWertAnz, exklWertAnz;
        int        inklIndex, exklIndex, letzterInklIndex;

        intervalle = new LinkedList();
        inklWertAnz = inklusivWerte.length;
        exklWertAnz = exklusivWerte.length;

        inklIndex = 0;
        exklIndex = 0;
        while (inklIndex < inklWertAnz) {
            // inklIndex erh�hen, bis der zugeh�rige inklusiv-Wert gleich
            // oder gr��er als exklusivWerte[exklIndex] ist.
            while ((inklIndex + 1 < inklWertAnz)
                   && (inklusivWerte[inklIndex] < exklusivWerte[exklIndex])) {
                inklIndex++;
            }
            inklWert = inklusivWerte[inklIndex];

            // exklIndex erh�hen, bis der zugeh�rige exklusiv-Wert gleich
            // oder gr��er als inklusivWerte[inklIndex] ist.
            while ((exklIndex + 1 < exklWertAnz)
                   && (exklusivWerte[exklIndex] < inklWert)) {
                exklIndex++;
            }

            // Wenn der inklusiv-Wert und der exklusiv-Wert gleich sind, das
            // zugeh�rige Einzelwert-Intervall aufnehmen.
            if (inklWert == exklusivWerte[exklIndex]) {
                intervalle.add(new RealIntervall(inklWert, inklWert));
            }

            // inklIndex erh�hen, bis sich der zugeh�rige Wert �ndert.
            letzterInklIndex = inklIndex;
            while ((inklIndex < inklWertAnz)
                   && (inklusivWerte[inklIndex] == inklusivWerte[letzterInklIndex])) {
                inklIndex++;
            }
        }

        return intervalle;
    }

    /**
     * Liefert alle m�glichen Intervalle der Werte aus
     * <CODE>inklusivWerte</CODE>, ohne die vollst�ndigen, die korrekten und
     * die nutzlosen Intervalle. Ein Intervall ist nutzlos, wenn es ein
     * anderes Intervall mit gleicher oder geringerer Komplexit�t gibt, das
     * gleich viele oder mehr Werte aus <CODE>inklusivWerte</CODE> und gleich
     * viele oder weniger Werte aus <CODE>exklusivWerte</CODE> enth�lt.
     *
     * @param inklusivWerte  Ein aufsteigend sortiertes Array der inklusiven
     *                       Werte.
     * @param exklusivWerte  Ein aufsteigend sortiertes Array der exklusiven
     *                       Werte.
     *
     * @return  Eine Liste der erzeugten allgemeinen Intervalle vom Typ
     *          <CODE>RealIntervall</CODE>.
     */
    public LinkedList allgemeineIntervalle(float[] inklusivWerte,
                                           float[] exklusivWerte) {
        LinkedList intervalle;
        float      inklUnterWert, exklUnterWert;
        float      inklOberWert, exklOberWert;
        int        inklWertAnz, exklWertAnz;
        int        inklUnterIndex, exklUnterIndex;
        int        inklOberIndex, exklOberIndex;

        intervalle = new LinkedList();
        inklWertAnz = inklusivWerte.length;
        exklWertAnz = exklusivWerte.length;

        if ((inklWertAnz == 0) || (exklWertAnz == 0)) {
            // Es gibt gar kein Intervall bzw. alle Intervalle sind korrekt.
            return intervalle;
        }

        if ((inklWertAnz == 1) || (exklWertAnz == 1)) {
            // Das Einzelwert-Intervall aus dem inklusiv-Wert ist vollst�ndig
            // bzw. alle Intervalle sind entweder korrekt oder nicht sinnvoll,
            // da sie alle exklusiv-Werte enthalten.
            return intervalle;
        }

        /*
         * Die Ermittlung der Intervalle wird von vier Werten gesteuert:
         *
         *  + inklUnterWert : Ein Wert aus inklusivWerte, der die Untergrenze
         *                    des aktuellen Intervalls angibt.
         *  + inklOberWert  : Ein Wert aus inklusivWerte, der die Obergrenze
         *                    des aktuellen Intervalls angibt.
         *  + exklUnterWert : Ein Wert aus exklusivWerte, der im aktuellen
         *                    Intervall enthalten sein soll.
         *  + exklOberWert  : Ein Wert aus exklusivWerte, der im aktuellen
         *                    Intervall nicht enthalten sein soll.
         *
         * F�r diese Werte gelten folgende Bedingungen:
         *
         *   inklUnterWert <= exklUnterWert <= inklOberWert < exklOberWert
         *
         * Dabei kann die Bedingung (inklOberWert < exklOberWert) verletzt
         * sein, wenn es keinen Wert in exklusivWerte gibt, der gr��er ist
         * als inklOberWert.
         *
         * F�r die Erzeugung der Intervalle wird zuerst inklUnterWert
         * festgelegt und dann werden zu diesem alle Werte f�r inklOberWert
         * ermittelt. F�r jede neue Obergrenze wird mindesten ein Wert aus
         * inklusivWerte, m�glichst mehr, und einer aus exklusivWerte (n�mlich
         * der letzte exklOberWert) zum letzten Intervall hinzugenommen. F�r
         * jede neue Untergrenze wird mindestens ein Wert aus inklusivWerte
         * und einer (n�mlich der letzte exklUnterWert), m�glichst mehr, aus
         * exklusivWerte ausgeschlossen.
         */

        inklUnterIndex = -1;
        exklUnterIndex = 0;
        inklUnterWert = Konstanten.MIN_WERT;
        exklUnterWert = exklusivWerte[0];

        while (inklUnterWert <= exklUnterWert) {
            // inklUnterIndex, exklUnterIndex, inklUnterWert und exklUnterWert
            // wurden bereits ermittelt.

            // Finde den kleinsten Wert f�r inklOberIndex, so da� inklOberWert
            // gerade gleich oder gr��er als exklUnterWert ist.
            inklOberIndex = inklUnterIndex;
            inklOberWert = inklUnterWert;
            while ((inklOberWert < exklUnterWert)
                   && (inklOberIndex + 1 < inklWertAnz)) {
                inklOberIndex++;
                inklOberWert = inklusivWerte[inklOberIndex];
            }

            if (inklOberWert >= exklUnterWert) {
                // Reduziere inklOberIndex, so da� gilt:
                // ((inklOberIndex == -1)
                //  || (inklusivWerte[inklOberIndex] < exklUnterWert))
                inklOberIndex--;
            }

            // Ermittle alle Obergrenzen zu inklUnterWert.
            exklOberIndex = exklUnterIndex;
            exklOberWert = exklUnterWert;
            while (inklOberIndex + 1 < inklWertAnz) {
                // Ermittle den n�chsten inklOberWert.
                inklOberIndex++;
                inklOberWert = inklusivWerte[inklOberIndex];

                // Ermittle den n�chsten exklOberWert.
                while ((exklOberIndex + 1 < exklWertAnz)
                       && (exklOberWert <= inklOberWert)) {
                    exklOberIndex++;
                    exklOberWert = exklusivWerte[exklOberIndex];
                }

                // Pr�fe, ob ein Wert f�r exklOberWert gefunden wurde.
                if (exklOberWert <= inklOberWert) {
                    // Der Wert wurde nicht gefunden. Alle restlichen Werte
                    // von inklusivWerte geh�ren zum Intervall.
                    inklOberIndex = inklWertAnz - 1;
                    if (inklusivWerte[inklOberIndex] < exklusivWerte[exklWertAnz - 1]) {
                        inklOberWert = inklusivWerte[inklOberIndex];
                    } else {
                        inklOberWert = Konstanten.MAX_WERT;
                    }
                } else {
                    // Finde den gr��ten Wert f�r inklOberWert, der kleiner
                    // ist als exklOberWert.
                    while ((inklOberIndex + 1 < inklWertAnz)
                           && (inklusivWerte[inklOberIndex + 1] < exklOberWert)) {
                        inklOberIndex++;
                    }
                    inklOberWert = inklusivWerte[inklOberIndex];
                }

                // Pr�fen, ob das Intervall nicht vollst�ndig ist.
                if ((inklUnterWert > inklusivWerte[0])
                        || (inklOberWert < inklusivWerte[inklWertAnz - 1])) {
                    // Das Intervall nur aufnehmen, wenn die Obergrenze nicht
                    // gleich der Untergrenze ist, da solche
                    // Einzelwert-Intervalle alle am Ende aufgenommen werden.
                    if (inklOberWert != inklUnterWert) {
                        intervalle.add(new RealIntervall(inklUnterWert,
                                                         inklOberWert));
                    }
                }
            }

            // Pr�fen, ob das druch inklUnterWert nach unten begrenzte
            // Halb-Intervall aufgenommen werden soll.
            if (inklusivWerte[inklWertAnz - 1] < exklusivWerte[exklWertAnz - 1]) {
                // Pr�fen, ob das Intervall nicht vollst�ndig ist.
                if (inklUnterWert > inklusivWerte[0]) {
                    intervalle.add(new RealIntervall(inklUnterWert,
                                                     Konstanten.MAX_WERT));
                }
            }

            // Ermittle den n�chsten Wert f�r inklUnterIndex und f�r
            // inklUnterWert.
            while ((inklUnterIndex + 1 < inklWertAnz)
                   && (inklUnterWert <= exklUnterWert)) {
                inklUnterIndex++;
                inklUnterWert = inklusivWerte[inklUnterIndex];
            }

            if (inklUnterWert > exklUnterWert) {
                // Es wurde ein neuer Wert f�r inklUnterWert gefunden.

                // Ermittle den n�chsten Wert f�r exklUnterIndex und f�r
                // exklUnterWert.
                while ((exklUnterIndex + 1 < exklWertAnz)
                       && (exklUnterWert < inklUnterWert)) {
                    exklUnterIndex++;
                    exklUnterWert = exklusivWerte[exklUnterIndex];
                }
            } else {
                // Es wurde kein neuer Wert f�r inklUnterWert gefunden.
                inklUnterWert = Konstanten.MAX_WERT;
                exklUnterWert = Konstanten.MIN_WERT;
            }
        }

        intervalle.addAll(algEwIntervalle(inklusivWerte, exklusivWerte));
        return intervalle;
    }

    /**
     * Liefert eine textuelle Darstellung des �bergebenen Arrays.
     *
     * @param werte  Ein Array, dessen textuelle Darstellung geliefert werden
     *               soll.
     *
     * @return  Eine textuelle Darstellung des �bergebenen Arrays.
     */
    public static String darstellung(float[] werte) {
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
        RealIntervallErzeugung intErz     = new AlleRealIntervallErzeug();
        float[]                inklWerte;
        float[]                exklWerte;

        inklWerte = new float[] {};
        exklWerte = new float[] {0, 3};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollst�ndige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new float[] {0, 1, 2, 3, 5};
        exklWerte = new float[] {};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollst�ndige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new float[] {0, 2, 2, 3};
        exklWerte = new float[] {0, 2, 3};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollst�ndige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new float[] {0, 1, 2, 3, 5};
        exklWerte = new float[] {0, 3, 3};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollst�ndige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new float[] {0, 1, 2, 3, 5};
        exklWerte = new float[] {2, 3, 3};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollst�ndige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new float[] {0, 1, 3, 5};
        exklWerte = new float[] {1, 2, 4, 6};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollst�ndige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new float[] {1, 3, 5, 7};
        exklWerte = new float[] {1, 2, 4, 6};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollst�ndige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new float[] {-5, -3, -3, -2, 1};
        exklWerte = new float[] {-6, -6, -2, -1, 1};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollst�ndige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new float[] {0, 1, 2, 3, 5};
        exklWerte = new float[] {6, 7, 7};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollst�ndige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();

        inklWerte = new float[] {1, 2, 3, 5};
        exklWerte = new float[] {0, 7, 7};
        System.out.println("inkl. Wert : " + darstellung(inklWerte));
        System.out.println("exkl. Wert : " + darstellung(exklWerte));
        System.out.println("Vollst�ndige Intervalle : "
                           + intErz.vollstaendigeIntervalle(inklWerte, exklWerte));
        System.out.println("Korrekte Intervalle : "
                           + intErz.korrekteIntervalle(inklWerte, exklWerte));
        System.out.println("Allgemeine Intervalle : "
                           + intErz.allgemeineIntervalle(inklWerte, exklWerte));
        System.out.println();
    }
}

