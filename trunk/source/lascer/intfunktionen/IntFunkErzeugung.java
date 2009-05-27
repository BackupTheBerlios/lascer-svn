/*
 * Dateiname      : IntFunkErzeugung.java
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


package lascer.intfunktionen;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;

import lascer.intfunktionen.arten.NullStellIntFunk;
import lascer.intfunktionen.arten.EinStellIntFunk;
import lascer.intfunktionen.arten.ZweiStellIntFunk;
import lascer.intfunktionen.konkrete.IntProjektFunk;

/**
 * Erzeugt Funktionen mit ganzzahligem Funktionswert. Dazu werden elementare
 * Funktionen zu so genannten Gesamtfunktionen verknüpft.
 *
 * @author  Dietmar Lippold
 */
public class IntFunkErzeugung implements Serializable {

    /**
     * Die Klasse repräsentiert einen Iterator zur Erzeugung von Funktionen,
     * die einen ganzzahligen Wert liefern.
     */
    class IntFunkIterator implements Iterator {

        /**
         * Die nächste zu liefernde Funktion.
         */
        private ErzeugbareIntFunk naechsteFunktion;

        /**
         * Erzeugt eine neue Instanz.
         */
        public IntFunkIterator() {

            if (maxElemFunkAnz == 0) {
                naechsteFunktion = null;
            } else {
                naechsteFunktion = ersteFunktion(1);
            }
        }

        /**
         * Ermittelt, ob eine weitere Funktion geliefert werden kann.
         *
         * @return  Den Wert <CODE>true</CODE>, wenn der Iterator eine weitere
         *          Funktion liefern kann, sonst <CODE>false</CODE>.
         */
        public boolean hasNext() {
            return (naechsteFunktion != null);
        }

        /**
         * Liefert die nächste Funktion.
         *
         * @return  Die nächste Funktion.
         *
         * @throws NoSuchElementException  Wenn keine Funktion mehr erzeugt
         *                                 werden konnte.
         */
        public Object next() {
            ErzeugbareIntFunk neueFunktion;
            int               aktuelleElemFunkAnz;

            if (naechsteFunktion == null) {
                // Es gibt keine weitere Funktion mehr.
                throw (new NoSuchElementException());
            }

            neueFunktion = naechsteFunktion;
            naechsteFunktion = naechsteFunktion(neueFunktion);
            aktuelleElemFunkAnz = neueFunktion.elementFunkAnz();
            while ((naechsteFunktion == null)
                   && (aktuelleElemFunkAnz + 1 <= maxElemFunkAnz)) {
                aktuelleElemFunkAnz++;
                naechsteFunktion = ersteFunktion(aktuelleElemFunkAnz);
            }

            return neueFunktion;
        }

        /**
         * Diese Methode ist nicht implementiert und darf nicht aufgerufen
         * werden.
         *
         * @throws UnsupportedOperationException  Bei jedem Aufruf.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Die Sammlung der erzeugbaren elementaren Funktionen.
     */
    private IntFunkSammlung intFunkSammlung;

    /**
     * Die maximale Anzahl elementarer Funktionen, aus denen eine
     * Gesamtfunktion bestehen darf.
     */
    private int maxElemFunkAnz;

    /**
     * Liefert eine neue Instanz.
     *
     * @param intFunkSammlung  Die Sammlung der erzeugbaren elementaren
     *                         Funktionen.
     * @param maxElemFunkAnz   Die maximale Anzahl elementarer Funktionen, aus
     *                         denen eine Gesamtfunktion bestehen darf.
     */
    public IntFunkErzeugung(IntFunkSammlung intFunkSammlung, int maxElemFunkAnz) {
        this.intFunkSammlung = intFunkSammlung;
        this.maxElemFunkAnz = maxElemFunkAnz;
    }

    /**
     * Liefert die nächste einstellige Gesamtfunktion, deren elementare
     * Funktion mindestens die übergebene Funktionsnummer hat.
     *
     * @param minFunkNr     Die minimale Funktionsnummer der neuen elementaren
     *                      Funktion.
     * @param elemFunkAnz   Die Anzahl der elementaren Funktionen der neuen
     *                      Gesamtfunktion.
     *
     * @return  Die die nächste einstellige Gesamtfunktion oder den Wert
     *          <CODE>null</CODE>, wenn es keine Funktion mehr gibt.
     */
    private EinStellIntFunk naechsteEinStellFunktion(int minFunkNr,
                                                     int elemFunkAnz) {
        ErzeugbareIntFunk argFunktion;
        EinStellIntFunk   funktion;
        int               funkNr;

        argFunktion = ersteFunktion(elemFunkAnz - 1);
        if (argFunktion == null) {
            return null;
        } else {
            funkNr = minFunkNr - 1;
            do {
                funkNr++;
                funktion = intFunkSammlung.einStelligeFunktion(funkNr, argFunktion);
            } while ((funktion != null)
                     && (funktion.maxElementFunkAnz() != 0)
                     && (funktion.maxElementFunkAnz() < elemFunkAnz));
            return funktion;
        }
    }

    /**
     * Liefert die erste sinnvolle Funktion mit der angegebenen Anzahl
     * elementarer Funktionen.
     *
     * @param elemFunkAnz  Die Anzahl der elementaren Funktionen der zu
     *                     liefernden Gesamtfunktion.
     *
     * @return  Die erste sinnvolle Gesamtfunktion mit der angegebenen Anzahl
     *          elementarer Funktionen oder den Wert <CODE>null</CODE>, wenn
     *          es keine solche Funktion gibt.
     */
    private ErzeugbareIntFunk ersteFunktion(int elemFunkAnz) {
        ErzeugbareIntFunk argFunktion1, argFunktion2;
        ErzeugbareIntFunk erzFunk;

        if (elemFunkAnz <= 0) {
            erzFunk = null;
        } else if (elemFunkAnz == 1) {
            erzFunk = intFunkSammlung.projektFunk(0);
        } else if (elemFunkAnz == 2) {
            erzFunk = naechsteEinStellFunktion(0, elemFunkAnz);
        } else {
            if (intFunkSammlung.zweiStellFunkAnz() > 0) {
                argFunktion1 = ersteFunktion(1);
                argFunktion2 = ersteFunktion(elemFunkAnz - 2);
                if (argFunktion2 != null) {
                    erzFunk = intFunkSammlung.zweiStelligeFunktion(0, argFunktion1,
                                                                   argFunktion2);
                } else {
                    erzFunk = naechsteEinStellFunktion(0, elemFunkAnz);
                }
            } else {
                erzFunk = naechsteEinStellFunktion(0, elemFunkAnz);
            }
        }

        if ((erzFunk != null) && !erzFunk.istSinnvoll()) {
            return naechsteFunktion(erzFunk);
        } else {
            return erzFunk;
        }
    }

    /**
     * Liefert die auf die übergebene Funktion folgende Gesamtfunktion mit
     * gleicher Anzahl elementarer Funktionen.
     *
     * @param funktion  Eine Projektions-Funktion, zu der die nächste
     *                  Gesamtfunktion geliefert werden soll.
     *
     * @return  Die auf die übergebenen Funktion folgende Gesamtfunktion oder
     *          den Wert <CODE>null</CODE>, wenn es keine solche Funktion
     *          gibt.
     */
    private ErzeugbareIntFunk naechsteFunkSpeziell(IntProjektFunk funktion) {
        int funkNr;

        funkNr = funktion.nummer();

        if (funkNr + 1 < intFunkSammlung.projektFunkAnz()) {
            return intFunkSammlung.projektFunk(funkNr + 1);
        } else {
            return intFunkSammlung.nullStelligeFunktion(0);
        }
    }

    /**
     * Liefert die auf die übergebenen Funktion folgende Gesamtfunktion mit
     * gleicher Anzahl elementarer Funktionen.
     *
     * @param funktion  Eine nullstellige Funktion, zu der die nächste
     *                  Gesamtfunktion geliefert werden soll.
     *
     * @return  Die auf die übergebenen Funktion folgende Gesamtfunktion oder
     *          den Wert <CODE>null</CODE>, wenn es keine solche Funktion
     *          gibt.
     */
    private ErzeugbareIntFunk naechsteFunkSpeziell(NullStellIntFunk funktion) {
        return intFunkSammlung.nullStelligeFunktion(funktion.nummer() + 1);
    }

    /**
     * Liefert die auf die übergebenen Funktion folgende Gesamtfunktion mit
     * gleicher Anzahl elementarer Funktionen.
     *
     * @param funktion  Eine einstellige Funktion, zu der die nächste
     *                  Gesamtfunktion geliefert werden soll.
     *
     * @return  Die auf die übergebenen Funktion folgende Gesamtfunktion oder
     *          den Wert <CODE>null</CODE>, wenn es keine solche Funktion
     *          gibt.
     */
    private ErzeugbareIntFunk naechsteFunkSpeziell(EinStellIntFunk funktion) {
        ErzeugbareIntFunk argFunktion, naechsteArgFunktion;
        int               elemFunkAnz;
        int               funkNr;

        funkNr = funktion.nummer();

        argFunktion = funktion.argumentFunktion();
        naechsteArgFunktion = naechsteFunktion(argFunktion);
        if (naechsteArgFunktion != null) {
            return intFunkSammlung.einStelligeFunktion(funkNr, naechsteArgFunktion);
        } else {
            if (funkNr + 1 < intFunkSammlung.einStellFunkAnz()) {
                elemFunkAnz = funktion.elementFunkAnz();
                return naechsteEinStellFunktion(funkNr + 1, elemFunkAnz);
            } else {
                return null;
            }
        }
    }

    /**
     * Liefert die auf die übergebenen Funktion folgende Gesamtfunktion mit
     * gleicher Anzahl elementarer Funktionen.
     *
     * @param funktion  Eine zweistellige Funktion, zu der die nächste
     *                  Gesamtfunktion geliefert werden soll.
     *
     * @return  Die auf die übergebenen Funktion folgende Gesamtfunktion oder
     *          den Wert <CODE>null</CODE>, wenn es keine solche Funktion
     *          gibt.
     */
    private ErzeugbareIntFunk naechsteFunkSpeziell(ZweiStellIntFunk funktion) {
        ErzeugbareIntFunk argFunktion1, argFunktion2;
        ErzeugbareIntFunk naechsteFunktion1, naechsteFunktion2;
        ErzeugbareIntFunk neueFunktion1, neueFunktion2;
        int               argElemFunkAnz1, argElemFunkAnz2, elemFunkAnz;
        int               funkNr;

        funkNr = funktion.nummer();

        argFunktion1 = funktion.argumentFunktion1();
        argFunktion2 = funktion.argumentFunktion2();
        naechsteFunktion2 = naechsteFunktion(argFunktion2);
        if (naechsteFunktion2 != null) {
            return intFunkSammlung.zweiStelligeFunktion(funkNr,
                                                        argFunktion1,
                                                        naechsteFunktion2);
        } else {
            naechsteFunktion1 = naechsteFunktion(argFunktion1);
            if (naechsteFunktion1 != null) {
                argElemFunkAnz1 = argFunktion1.elementFunkAnz();
                argElemFunkAnz2 = argFunktion2.elementFunkAnz();
                if (funktion.istKommutativ()
                        && (argElemFunkAnz1 == argElemFunkAnz2)) {
                    // Die Funktion ist kommutativ und die beiden
                    // Argument-Funktionen haben die gleiche Anzahl
                    // elementarer Funktionen.
                    neueFunktion2 = naechsteFunktion(argFunktion1);
                } else {
                    // Die Funktion ist nicht kommutativ oder die beiden
                    // Argument-Funktionen haben eine unterschiedliche
                    // Anzahl elementarer Funktionen.
                    neueFunktion2 = ersteFunktion(argElemFunkAnz2);
                }
                return intFunkSammlung.zweiStelligeFunktion(funkNr,
                                                            naechsteFunktion1,
                                                            neueFunktion2);
            } else if (funkNr + 1 < intFunkSammlung.zweiStellFunkAnz()) {
                // Es gibt noch eine weitere zweistellige Funktion.
                elemFunkAnz = funktion.elementFunkAnz();
                neueFunktion1 = ersteFunktion(1);
                neueFunktion2 = ersteFunktion(elemFunkAnz - 2);
                return intFunkSammlung.zweiStelligeFunktion(funkNr + 1,
                                                            neueFunktion1,
                                                            neueFunktion2);
            } else {
                // Es wird eine einstellige Funktion geliefert.
                elemFunkAnz = funktion.elementFunkAnz();
                return naechsteEinStellFunktion(0, elemFunkAnz);
            }
        }
    }

    /**
     * Liefert die auf die übergebenen Funktion folgende sinnvolle
     * Gesamtfunktion mit gleicher Komplexität.
     *
     * @param funktion  Die Funktion, zu der die nächste Gesamtfunktion
     *                  geliefert werden soll.
     *
     * @return  Die auf die übergebenen Funktion folgende sinnvolle
     *          Gesamtfunktion oder den Wert <CODE>null</CODE>, wenn es keine
     *          solche Funktion gibt.
     */
    private ErzeugbareIntFunk naechsteFunktion(ErzeugbareIntFunk funktion) {
        ErzeugbareIntFunk altFunk, neuFunk;

        altFunk = funktion;

        do {
            if (funktion instanceof IntProjektFunk) {
                neuFunk = naechsteFunkSpeziell((IntProjektFunk) funktion);
            } else if (funktion instanceof NullStellIntFunk) {
                neuFunk = naechsteFunkSpeziell((NullStellIntFunk) funktion);
            } else if (funktion instanceof EinStellIntFunk) {
                neuFunk = naechsteFunkSpeziell((EinStellIntFunk) funktion);
            } else if (funktion instanceof ZweiStellIntFunk) {
                neuFunk = naechsteFunkSpeziell((ZweiStellIntFunk) funktion);
            } else {
                throw new IllegalArgumentException("Funktion ist von"
                                                   + " unbekannter Art : "
                                                   + funktion.toString());
            }
        } while ((neuFunk != null) && !neuFunk.istSinnvoll());

        return neuFunk;
    }

    /**
     * Liefert einen Iterator über erzeugte Instanzen der Klasse
     * <CODE>IntFunktion</CODE>.
     *
     * @return  Einen Iterator über erzeugte int-Funktionen.
     */
    public Iterator funkIterator() {
        return new IntFunkIterator();
    }
}

