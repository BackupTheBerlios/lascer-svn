/*
 * Dateiname      : RealFunkPraedErzeugung.java
 * Letzte Änderung: 28. August 2006
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

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.io.Serializable;

import lascer.realfunktionen.RealFunktion;
import lascer.realfunktionen.RealFunkErzeugung;
import lascer.praedikate.PraedikatErzeugung;
import lascer.praedikate.einzelne.realpraed.RealFunkWertPraedikat;
import lascer.praedikate.einzelne.realpraed.RealFunkHalbItvPraedikat;
import lascer.praedikate.einzelne.realpraed.RealFunkVollItvPraedikat;
import lascer.problemdaten.Beispieldaten;

/**
 * Implementiert die Methoden zur Erzeugung von Prädikaten aus Funktionen, die
 * einen Fließkomma-Wert liefern.
 *
 * @author  Dietmar Lippold
 */
public class RealFunkPraedErzeugung implements PraedikatErzeugung, Serializable {

    /**
     * Die Klasse repräsentiert einen Iterator zur Erzeugung von Prädikaten
     * aus Funktionen, die einen Fließkomma-Wert liefern.
     */
    class RealFunkPraedIterator implements Iterator {

        /**
         * Der Iterator zur Ermittlung neuer Funktionen.
         */
        private Iterator realFunkIterator = realFunkErzeugung.funkIterator();

        /**
         * Die zu den Funktionswerten erzeugten Intervalle.
         */
        private LinkedList intervalle = new LinkedList();

        /**
         * Die den vorhandenen Intervallen und Funktionswerten
         * zugrundeliegende Funktion.
         */
        private RealFunktion funktion = null;

        /**
         * Die Werte der Funktion für die positiven Beispiele.
         */
        private float[] posBspFunkWerte = null;

        /**
         * Die Werte der Funktion für die negativen Beispiele.
         */
        private float[] negBspFunkWerte = null;

        /**
         * Gibt an, ob die Prädikate möglichst viele positive Beispiele
         * abdecken sollen. Anderenfalls decken sie möglichst viele negative
         * Beispiele ab.
         */
        private boolean posBspPraedikate;

        /**
         * Gibt an, ob korrekte Prädikate erzeugt werden sollen.
         */
        private boolean korrPraedikate;

        /**
         * Gibt an, ob vollständige Prädikate erzeugt werden sollen.
         */
        private boolean vollPraedikate;

        /**
         * Erzeugt eine neue Instanz.
         *
         * @param posBspPraedikate  Gibt an, ob die Prädikate möglichst viele
         *                          positive Beispiele abdecken sollen.
         *                          Anderenfalls decken sie möglichst viele
         *                          negative Beispiele ab.
         * @param korrPraedikate    Gibt an, ob korrekte Prädikate erzeugt
         *                          werden sollen.
         * @param vollPraedikate    Gibt an, ob vollständige Prädikate erzeugt
         *                          werden sollen.
         *
         * @throws IllegalArgumentException  Wenn sowohl <CODE>korrPraedikate</CODE>
         *                                   wie <CODE>vollPraedikate</CODE>
         *                                   den Wert <CODE>true</CODE> haben.
         */
        public RealFunkPraedIterator(boolean posBspPraedikate,
                                     boolean korrPraedikate, boolean vollPraedikate) {

            if (korrPraedikate && vollPraedikate) {
                throw new IllegalArgumentException("Es können nicht geichzeitig"
                                                   + " korrekte und vollständige"
                                                   + " Prädikate erzeugt werden");
            }

            this.posBspPraedikate = posBspPraedikate;
            this.korrPraedikate = korrPraedikate;
            this.vollPraedikate = vollPraedikate;
        }

        /**
         * Liefert ein neu erzeugtes Array, das alle Werte des übergebenen
         * Arrays bis auf den unbekannten Wert enthält.
         *
         * @param werte  Die Werte, die bis auf den unbekannten Wert zurück
         *               gegeben werden sollen.
         *
         * @return  Ein neu erzeugtes Array, das alle Werte des übergebenen
         *          Arrays bis auf den unbekannten Wert enthält.
         */
        private float[] bekannteWerte(float[] werte) {
            float[] bekannteWerte;
            int     anzBekannteWerte;
            int     nummerBekannterWert;

            // Die Anzahl der bekannten Werte ermitteln.
            anzBekannteWerte = 0;
            for (int i = 0; i < werte.length; i++) {
                if (werte[i] != Konstanten.UNBEKANNT_WERT) {
                    anzBekannteWerte++;
                }
            }

            // Die bekannten Werte kopieren.
            bekannteWerte = new float[anzBekannteWerte];
            nummerBekannterWert = 0;
            for (int i = 0; i < werte.length; i++) {
                if (werte[i] != Konstanten.UNBEKANNT_WERT) {
                    bekannteWerte[nummerBekannterWert] = werte[i];
                    nummerBekannterWert++;
                }
            }

            return bekannteWerte;
        }

        /**
         * Erzeugt, wenn notwendig und möglich, neue Intervalle. Notwendig ist
         * das, wenn die Liste der Intervalle leer ist. In dem Fall wird, wenn
         * möglich, eine neue Funktion erzeugt, mit der neue Intervalle
         * erzeugt werden können.
         */
        private void erzeugeIntervalle() {
            float[] inklBspFunkWerte;
            float[] exklBspFunkWerte;
            float[] inklBspWerte;
            float[] exklBspWerte;

            while (intervalle.isEmpty() && realFunkIterator.hasNext()) {
                funktion = (RealFunktion) realFunkIterator.next();

                posBspFunkWerte = funktion.posBspWerte(beispieldaten);
                negBspFunkWerte = funktion.negBspWerte(beispieldaten);

                if (posBspPraedikate) {
                    inklBspFunkWerte = posBspFunkWerte;
                    exklBspFunkWerte = negBspFunkWerte;
                } else {
                    inklBspFunkWerte = negBspFunkWerte;
                    exklBspFunkWerte = posBspFunkWerte;
                }

                if (beispieldaten.realWertUnbekannt()) {
                    inklBspWerte = bekannteWerte(inklBspFunkWerte);
                    exklBspWerte = bekannteWerte(exklBspFunkWerte);
                } else {
                    inklBspWerte = (float[]) inklBspFunkWerte.clone();
                    exklBspWerte = (float[]) exklBspFunkWerte.clone();
                }

                Arrays.sort(inklBspWerte);
                Arrays.sort(exklBspWerte);

                if (korrPraedikate) {
                    if (!RealWertPraedErzeugung.unbekWertEnthalten(exklBspFunkWerte)) {
                        intervalle = realItvErzeugung.korrekteIntervalle(inklBspWerte,
                                                                         exklBspWerte);
                    } else {
                        intervalle = new LinkedList();
                    }
                } else if (vollPraedikate) {
                    if (!RealWertPraedErzeugung.unbekWertEnthalten(inklBspFunkWerte)) {
                        intervalle = realItvErzeugung.vollstaendigeIntervalle(inklBspWerte,
                                                                              exklBspWerte);
                    } else {
                        intervalle = new LinkedList();
                    }
                } else {
                    intervalle = realItvErzeugung.allgemeineIntervalle(inklBspWerte,
                                                                       exklBspWerte);
                }
            }
        }

        /**
         * Ermittelt, ob ein weiteres Prädikat geliefert werden kann.
         *
         * @return  Den Wert <CODE>true</CODE>, wenn der Iterator ein weiteres
         *          Prädikat liefern kann, sonst <CODE>false</CODE>.
         */
        public boolean hasNext() {
            erzeugeIntervalle();

            return !intervalle.isEmpty();
        }

        /**
         * Liefert das nächste Prädikat.
         *
         * @return  Das nächste Prädikat.
         *
         * @throws NoSuchElementException  Wenn kein Prädikat mehr erzeugt
         *                                 werden konnte.
         */
        public Object next() {
            RealIntervall intervall;
            float         untergrenze, obergrenze;

            // Falls notwendig und möglich, neue Intervalle erzeugen.
            erzeugeIntervalle();

            if (intervalle.isEmpty()) {
                // Es gibt kein Prädikat mehr.
                throw (new NoSuchElementException());
            }

            intervall = (RealIntervall) intervalle.removeFirst();
            untergrenze = intervall.untergrenze();
            obergrenze = intervall.obergrenze();
            if (untergrenze == obergrenze) {
                return (new RealFunkWertPraedikat(funktion, untergrenze,
                                                  beispieldaten,
                                                  posBspFunkWerte,
                                                  negBspFunkWerte));
            } else if (untergrenze == Konstanten.MIN_WERT) {
                return (new RealFunkHalbItvPraedikat(funktion, obergrenze,
                                                     true, beispieldaten,
                                                     posBspFunkWerte,
                                                     negBspFunkWerte));
            } else if (obergrenze == Konstanten.MAX_WERT) {
                return (new RealFunkHalbItvPraedikat(funktion, untergrenze,
                                                     false, beispieldaten,
                                                     posBspFunkWerte,
                                                     negBspFunkWerte));
            } else {
                return (new RealFunkVollItvPraedikat(funktion, untergrenze,
                                                     obergrenze, beispieldaten,
                                                     posBspFunkWerte,
                                                     negBspFunkWerte));
            }
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
     * Die Beispieldaten, zu denen die Prädikate erzeugt werden sollen.
     */
    private Beispieldaten beispieldaten;

    /**
     * Die Instanz zur Erzeugung von Funktionen.
     */
    private RealFunkErzeugung realFunkErzeugung;

    /**
     * Die Instanz zur Erzeugung von Intervallen.
     */
    private RealIntervallErzeugung realItvErzeugung;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param beispieldaten      Die Beispieldaten, zu denen die Prädikate
     *                           erzeugt werden sollen.
     * @param realFunkErzeugung  Die Erzeuger der Funktionen.
     * @param nurHalbIntervalle  Gibt an, ob nur Halb-Intervalle anstatt aller
     *                           Arten von Intervallen erzeugt werden sollen.
     */
    public RealFunkPraedErzeugung(Beispieldaten beispieldaten,
                                  RealFunkErzeugung realFunkErzeugung,
                                  boolean nurHalbIntervalle) {

        this.beispieldaten = beispieldaten;
        this.realFunkErzeugung = realFunkErzeugung;
        if (nurHalbIntervalle) {
            this.realItvErzeugung = new HalbRealIntervallErzeug();
        } else {
            this.realItvErzeugung = new AlleRealIntervallErzeug();
        }
    }

    /**
     * Liefert einen Iterator über Prädikate, die korrekt sind in Bezug auf
     * die positiven Beispiele.
     *
     * @return  Einen Iterator über korrekte Prädikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posKorrPraedIter() {
        return (new RealFunkPraedIterator(true, true, false));
    }

    /**
     * Liefert einen Iterator über Prädikate, die korrekt sind in Bezug auf
     * die negativen Beispiele.
     *
     * @return  Einen Iterator über korrekte Prädikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negKorrPraedIter() {
        return (new RealFunkPraedIterator(false, true, false));
    }

    /**
     * Liefert einen Iterator über Prädikate, die vollständig sind in Bezug
     * auf die positiven Beispiele.
     *
     * @return  Einen Iterator über vollständige Prädikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posVollPraedIter() {
        return (new RealFunkPraedIterator(true, false, true));
    }

    /**
     * Liefert einen Iterator über Prädikate, die vollständig sind in Bezug
     * auf die negativen Beispiele.
     *
     * @return  Einen Iterator über vollständige Prädikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negVollPraedIter() {
        return (new RealFunkPraedIterator(false, false, true));
    }

    /**
     * Liefert einen Iterator über allgemeine Prädikate. Diese sind weder
     * vollständig noch korrekt in Bezug auf die positiven oder die negativen
     * Beispiele. Zu einer bestimmten Menge negativer Beispiele werden
     * möglichst viele positive Beispiele von einem Prädikat abgedeckt.
     *
     * @return  Einen Iterator über allgemeine Prädikate für positive
     *          Beispiele.
     */
    public Iterator posAlgPraedIter() {
        return (new RealFunkPraedIterator(true, false, false));
    }

    /**
     * Liefert einen Iterator über allgemeine Prädikate. Diese sind weder
     * vollständig noch korrekt in Bezug auf die positiven oder die negativen
     * Beispiele. Zu einer bestimmten Menge positiver Beispiele werden
     * möglichst viele negative Beispiele von einem Prädikat abgedeckt.
     *
     * @return  Einen Iterator über allgemeine Prädikate für negative
     *          Beispiele.
     */
    public Iterator negAlgPraedIter() {
        return (new RealFunkPraedIterator(false, false, false));
    }
}

