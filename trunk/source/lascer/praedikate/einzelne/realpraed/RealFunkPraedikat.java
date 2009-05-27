/*
 * Dateiname      : RealFunkPraedikat.java
 * Letzte �nderung: 06. September 2006
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


package lascer.praedikate.einzelne.realpraed;

import lascer.problemdaten.Beispieldaten;
import lascer.realfunktionen.RealFunktion;
import lascer.praedikate.Praedikat;

/**
 * Definiert ein abstraktes Pr�dikat, das die Flie�komma-Werte einer Funktion
 * gegen vorgegebene Werte vergleicht.
 *
 * @author  Dietmar Lippold
 */
public abstract class RealFunkPraedikat implements Praedikat {

    /**
     * Die Daten, zu deren Attribut-Sammlung das Pr�dikat erzeugt wurde und
     * zu deren Beispielen die Funktionswerte �bergeben wurden.
     */
    private final Beispieldaten bspdaten;

    /**
     * Die Funktion, deren Werte f�r Beispiele gegen vorgegebene Werte
     * verglichen werden.
     */
    private final RealFunktion funktion;

    /**
     * Die Werte der Funktion f�r die positiven Beispiele.
     */
    private float[] posBspFunkWerte;

    /**
     * Die Werte der Funktion f�r die negativen Beispiele.
     */
    private float[] negBspFunkWerte;

    /**
     * Die Komplexit�t des Pr�dikats.
     */
    private final float komplexitaet;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param funktion          Die Funktion, deren Wert f�r ein Beispiel in
     *                          Bezug auf das vorgegebene Intervall gepr�ft
     *                          wird.
     * @param beispieldaten     Daten, zu deren Attribut-Sammlung das Pr�dikat
     *                          erzeugt wird und zu deren Beispielen die
     *                          �bergeben Funktionswerte geh�ren.
     * @param posBspFunkWerte   Die Werte der Funktion f�r die positiven
     *                          Beispiele.
     * @param negBspFunkWerte   Die Werte der Funktion f�r die negativen
     *                          Beispiele.
     * @param spezKomplexitaet  Die spezielle Komplexit�t des Pr�dikats. F�r
     *                          die Gesamtkomplexit�t wird noch die
     *                          Komplexit�t der Funktion addiert.
     */
    public RealFunkPraedikat(RealFunktion funktion, Beispieldaten beispieldaten,
                             float[] posBspFunkWerte, float[] negBspFunkWerte,
                             float spezKomplexitaet) {
        this.funktion = funktion;
        this.bspdaten = beispieldaten;
        this.posBspFunkWerte = posBspFunkWerte;
        this.negBspFunkWerte = negBspFunkWerte;
        this.komplexitaet = funktion.komplexitaet() + spezKomplexitaet;
    }

    /**
     * Liefert die Funktion des Pr�dikats.
     *
     * @return  Die Funktion des Pr�dikats.
     */
    protected RealFunktion funktion() {
        return funktion;
    }

    /**
     * Liefert die Komplexit�t des Pr�dikats.
     *
     * @return  Die Komplexit�t des Pr�dikats.
     */
    public float komplexitaet() {
        return komplexitaet;
    }

    /**
     * Ermittelt f�r jeden der �bergebenen Werte, ob dieser gleich ist zu
     * einem der vorgegebenen Werte des Pr�dikats.
     *
     * @param funkWerte  Die Werte, f�r die ermittelt wird, ob sie zu einem
     *                   der vorgegebenen Werte gleich sind.
     *
     * @return  Ein neu erzeugtes Array mit Werten die angeben, ob die
     *          �bergebenen Werte zu einem der vorgegebenen Werte gleich sind.
     */
    protected abstract boolean[] bspWerte(float[] funkWerte);

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Pr�dikats auf die �bergeben Funktionswerte der Beispiele ergeben.
     *
     * @param funkWerte      Die Funktionswerte der Beispiele.
     * @param invertiert     Die Angabe, ob die Werte des Pr�dikats invertiert
     *                       werden sollen.
     * @param wertUnbekannt  Die Angabe, ob mindestens einer der
     *                       Funktionswerte unbekannt ist.
     * @param unbekWert      Der Wert, der einem Beispiel mit unbekanntem Wert
     *                       zugewiesen werden soll.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Pr�dikats auf die Funktionswerte ergeben, gegebenenfalls
     *          invertiert.
     */
    private boolean[] praedikatWerte(float[] funkWerte, boolean invertiert,
                                     boolean wertUnbekannt, boolean unbekWert) {
        boolean[] praedikatWerte;

        praedikatWerte = bspWerte(funkWerte);

        if (invertiert) {
            for (int i = 0; i < funkWerte.length; i++) {
                praedikatWerte[i] = !praedikatWerte[i];
            }
        }

        if (wertUnbekannt) {
            for (int i = 0; i < praedikatWerte.length; i++) {
                if (funkWerte[i] == Konstanten.UNBEKANNT_WERT) {
                    praedikatWerte[i] = unbekWert;
                }
            }
        }

        return praedikatWerte;
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Pr�dikats auf die positiven Beispiele der �bergebenen Beispieldaten
     * ergeben.
     *
     * @param beispieldaten  Die Beispieldaten, f�r deren positive Beispiele
     *                       die Werte des Pr�dikats ermittelt werden sollen.
     * @param invertiert     Die Angabe, ob die Werte des Pr�dikats invertiert
     *                       werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Pr�dikats auf die positiven Beispiele ergeben.
     */
    public boolean[] posBspWerte(Beispieldaten beispieldaten, boolean invertiert) {
        float[] funkWerte;

        // Die Werte zu den Beispielen ermitteln.
        if (beispieldaten.posBeispiele().equals(bspdaten.posBeispiele())) {
            funkWerte = posBspFunkWerte;
        } else {
            funkWerte = funktion.posBspWerte(beispieldaten);
        }

        return praedikatWerte(funkWerte, invertiert,
                              beispieldaten.realWertUnbekannt(), false);
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Pr�dikats auf die negativen Beispiele der �bergebenen Beispieldaten
     * ergeben.
     *
     * @param beispieldaten  Die Beispieldaten, f�r deren negativen Beispiele
     *                       die Werte des Pr�dikats ermittelt werden sollen.
     * @param invertiert     Die Angabe, ob die Werte des Pr�dikats invertiert
     *                       werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Pr�dikats auf die negativen Beispiele ergeben.
     */
    public boolean[] negBspWerte(Beispieldaten beispieldaten, boolean invertiert) {
        float[] funkWerte;

        // Die Werte zu den Beispielen ermitteln.
        if (beispieldaten.negBeispiele().equals(bspdaten.negBeispiele())) {
            funkWerte = negBspFunkWerte;
        } else {
            funkWerte = funktion.negBspWerte(beispieldaten);
        }

        return praedikatWerte(funkWerte, invertiert,
                              beispieldaten.realWertUnbekannt(), true);
    }
}

