/*
 * Dateiname      : AbstEinStellRealFunk.java
 * Letzte Änderung: 12. Juni 2006
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


package lascer.realfunktionen.arten;

import java.util.List;

import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;
import lascer.realfunktionen.ErzeugbareRealFunk;

/**
 * Abstrakte einstellige real-Funktion.
 *
 * @author  Dietmar Lippold
 */
public abstract class AbstEinStellRealFunk implements EinStellRealFunk {

    /**
     * Die Argumentfunktion dieser Funktion.
     */
    private ErzeugbareRealFunk argFunktion;

    /**
     * Die Nummer dieser Funktion unter allen einstelligen Funktionen.
     */
    private int nummer;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param nummer       Die Nummer der zu erzeugenden Funktion unter allen
     *                     einstelligen Funktionen.
     * @param argFunktion  Die Argumentfunktion der zu erzeugenden Funktion.
     */
    public AbstEinStellRealFunk(int nummer, ErzeugbareRealFunk argFunktion) {
        this.nummer = nummer;
        this.argFunktion = argFunktion;
    }

    /**
     * Gibt an, ob die Funktion sinnvoll ist.
     *
     * @return  Die Angabe, ob die Funktion sinnvoll ist.
     */
    public boolean istSinnvoll() {
        return argFunktion.istSinnvoll();
    }

    /**
     * Liefert die Nummer der Funktion unter den Funktionen mit gleicher
     * Stelligkeit.
     *
     * @return  Die Nummer der Funktion.
     */
    public int nummer() {
        return nummer;
    }

    /**
     * Liefert die Anzahl der elementaren Funktionen in der Gesamtfunktion.
     *
     * @return  Die Anzahl der elementaren Funktionen.
     */
    public int elementFunkAnz() {
        return (argFunktion.elementFunkAnz() + 1);
    }

    /**
     * Liefert eine neu erzeugte Liste der von dieser Funktion verwendeten
     * Attribute.
     *
     * @return  Eine neu erzeugte Liste der von dieser Funktion verwendeten
     *          Attribute.
     */
    public List verwendeteAttribute() {
        return argFunktion.verwendeteAttribute();
    }

    /**
     * Liefert die eigene Komplexität der elementaren Funktion.
     *
     * @return  Die eigene Komplexität der elementaren Funktion.
     */
    protected abstract float eigeneKomplexitaet();

    /**
     * Liefert die Komplexität der Gesamtfunktion.
     *
     * @return  Die Komplexität der Gesamtfunktion.
     */
    public float komplexitaet() {
        return (argFunktion.komplexitaet() + eigeneKomplexitaet());
    }

    /**
     * Liefert die Argument-Funktion.
     *
     * @return  Die Argument-Funktion.
     */
    public ErzeugbareRealFunk argumentFunktion() {
        return argFunktion;
    }

    /**
     * Liefert den Wert der Funktion für den übergebenen Wert der
     * Argumentfunktion.
     *
     * @param funkWert  Der Wert der Argumentfunktion.
     *
     * @return  Den Wert der Funktion für den übergebenen Wert der
     *          Argumentfunktion.
     */
    protected abstract float funkWert(float funkWert);

    /**
     * Liefert den Wert der Funktion für das übergebene Beispiel.
     *
     * @param beispiel  Ein Beispiel, für das der Wert der Funktion ermittelt
     *                  werden soll.
     *
     * @return  Den Wert der Funktion für das übergebene Beispiel.
     */
    public float wert(Beispiel beispiel) {
        float wert;

        wert = argFunktion.wert(beispiel);

        if (wert == Konstanten.UNBEKANNT_WERT) {
            return Konstanten.UNBEKANNT_WERT;
        } else {
            return funkWert(wert);
        }
    }

    /**
     * Liefert ein Array mit den Werten der Funktion für die übergebenen
     * Werte der Argumentfunktion.
     *
     * @param argFunkWerte  Die Werte der Argumentfunktion.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          der Funktion auf die Werte der Argumentfunktion ergeben.
     */
    protected abstract float[] funkWerte(float[] argFunkWerte);

    /**
     * Ersetzt zu allen Beispielen, zu denen der Argument-Wert unbekannt ist,
     * den Ergebnis-Wert ebenfalls durch die Konstante für den unbekannten
     * Wert.
     *
     * @param argWerte       Die Argument-Werte.
     * @param ergebnisWerte  Die Ergebnis-Werte, die verändert werden.
     */
    private void korrekturUnbekWerte(float[] ergebnisWerte, float[] argWerte) {

        for (int i = 0; i < ergebnisWerte.length; i++) {
            if (argWerte[i] == Konstanten.UNBEKANNT_WERT) {
                ergebnisWerte[i] = Konstanten.UNBEKANNT_WERT;
            }
        }
    }

    /**
     * Liefert ein Array mit den Werten der Funktion für die positiven
     * Beispiele der übergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, für deren positive Beispiele
     *                       die Werte der Funktion ermittelt werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          der Funktion auf die positiven Beispiele ergeben.
     */
    public float[] posBspWerte(Beispieldaten beispieldaten) {
        float[] argFunkWerte;
        float[] ergebnisWerte;

        argFunkWerte = argFunktion.posBspWerte(beispieldaten);
        ergebnisWerte = funkWerte(argFunkWerte);
        korrekturUnbekWerte(ergebnisWerte, argFunkWerte);

        return ergebnisWerte;
    }

    /**
     * Liefert ein Array mit den Werten der Funktion für die negativen
     * Beispiele der übergebenen Beispieldaten.
     *
     * @param beispieldaten  Die Beispieldaten, für deren negative Beispiele
     *                       die Werte der Funktion ermittelt werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          der Funktion auf die negativen Beispiele ergeben.
     */
    public float[] negBspWerte(Beispieldaten beispieldaten) {
        float[] argFunkWerte;
        float[] ergebnisWerte;

        argFunkWerte = argFunktion.negBspWerte(beispieldaten);
        ergebnisWerte = funkWerte(argFunkWerte);
        korrekturUnbekWerte(ergebnisWerte, argFunkWerte);

        return ergebnisWerte;
    }
}

