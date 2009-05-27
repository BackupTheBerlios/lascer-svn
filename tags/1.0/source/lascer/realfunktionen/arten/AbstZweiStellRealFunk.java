/*
 * Dateiname      : AbstZweiStellRealFunk.java
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
 * Abstrakte zweistellige int-Funktion.
 *
 * @author  Dietmar Lippold
 */
public abstract class AbstZweiStellRealFunk implements ZweiStellRealFunk {

    /**
     * Die erste Argumentfunktion dieser Funktion.
     */
    private ErzeugbareRealFunk argFunktion1;

    /**
     * Die zweite Argumentfunktion dieser Funktion.
     */
    private ErzeugbareRealFunk argFunktion2;

    /**
     * Die Nummer dieser Funktion unter allen zweistelligen Funktionen.
     */
    private int nummer;

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param nummer        Die Nummer der zu erzeugenden Funktion unter allen
     *                      zweistelligen Funktionen.
     * @param argFunktion1  Die erste Argumentfunktion der zu erzeugenden
     *                      Funktion.
     * @param argFunktion2  Die zweite Argumentfunktion der zu erzeugenden
     *                      Funktion.
     */
    public AbstZweiStellRealFunk(int nummer, ErzeugbareRealFunk argFunktion1,
                                 ErzeugbareRealFunk argFunktion2) {
        this.nummer = nummer;
        this.argFunktion1 = argFunktion1;
        this.argFunktion2 = argFunktion2;
    }

    /**
     * Gibt an, ob die Funktion sinnvoll ist.
     *
     * @return  Die Angabe, ob die Funktion sinnvoll ist.
     */
    public boolean istSinnvoll() {
        return (argFunktion1.istSinnvoll() && argFunktion2.istSinnvoll());
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
        return (argFunktion1.elementFunkAnz() + argFunktion2.elementFunkAnz() + 1);
    }

    /**
     * Liefert eine neu erzeugte Liste der von dieser Funktion verwendeten
     * Attribute.
     *
     * @return  Eine neu erzeugte Liste der von dieser Funktion verwendeten
     *          Attribute.
     */
    public List verwendeteAttribute() {
        List verwendeteAttribute;

        verwendeteAttribute = argFunktion1.verwendeteAttribute();
        verwendeteAttribute.addAll(argFunktion2.verwendeteAttribute());
        return verwendeteAttribute;
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
        return (argFunktion1.komplexitaet() + argFunktion2.komplexitaet()
                + eigeneKomplexitaet());
    }

    /**
     * Liefert die erste Argument-Funktion.
     *
     * @return  Die erste Argument-Funktion.
     */
    public ErzeugbareRealFunk argumentFunktion1() {
        return argFunktion1;
    }

    /**
     * Liefert die zweite Argument-Funktion.
     *
     * @return  Die zweite Argument-Funktion.
     */
    public ErzeugbareRealFunk argumentFunktion2() {
        return argFunktion2;
    }

    /**
     * Liefert den Wert der Funktion für die übergebenen Werte der
     * Argumentfunktionen.
     *
     * @param funkWert1  Der Wert der ersten Argumentfunktion.
     * @param funkWert2  Der Wert der zweiten Argumentfunktion.
     *
     * @return  Den Wert der Funktion für die übergebenen Werte der
     *          Argumentfunktionen.
     */
    protected abstract float funkWert(float funkWert1, float funkWert2);

    /**
     * Liefert den Wert der Funktion für das übergebene Beispiel.
     *
     * @param beispiel  Ein Beispiel, für das der Wert der Funktion ermittelt
     *                  werden soll.
     *
     * @return  Den Wert der Funktion für das übergebene Beispiel.
     */
    public float wert(Beispiel beispiel) {
        float wert1, wert2;

        wert1 = argFunktion1.wert(beispiel);
        wert2 = argFunktion2.wert(beispiel);

        if ((wert1 == Konstanten.UNBEKANNT_WERT)
                || (wert2 == Konstanten.UNBEKANNT_WERT)) {
            return Konstanten.UNBEKANNT_WERT;
        } else {
            return funkWert(wert1, wert2);
        }
    }

    /**
     * Liefert ein Array mit den Werten der Funktion für die übergebenen
     * Werte der Argumentfunktionen.
     *
     * @param funkWerte1  Die Werte der ersten Argumentfunktion.
     * @param funkWerte2  Die Werte der zweiten Argumentfunktion.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          der Funktion auf die Werte der Argumentfunktionen ergeben.
     */
    protected abstract float[] funkWerte(float[] funkWerte1, float[] funkWerte2);

    /**
     * Ersetzt zu allen Beispielen, zu denen einer der beiden Argument-Werte
     * unbekannt ist, den Ergebnis-Wert ebenfalls durch die Konstante für den
     * unbekannten Wert.
     *
     * @param argWerte1      Die einen Argument-Werte.
     * @param argWerte2      Die anderen Argument-Werte.
     * @param ergebnisWerte  Die Ergebnis-Werte, die verändert werden.
     */
    private void korrekturUnbekWerte(float[] ergebnisWerte,
                                     float[] argWerte1, float[] argWerte2) {

        for (int i = 0; i < ergebnisWerte.length; i++) {
            if ((argWerte1[i] == Konstanten.UNBEKANNT_WERT)
                    || (argWerte2[i] == Konstanten.UNBEKANNT_WERT)) {
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
        float[] funkWerte1;
        float[] funkWerte2;
        float[] ergebnisWerte;

        funkWerte1 = argFunktion1.posBspWerte(beispieldaten);
        funkWerte2 = argFunktion2.posBspWerte(beispieldaten);

        if (funkWerte1.length != funkWerte2.length) {
            throw new Error("Anzahl der Funktionswerte ist ungleich");
        }

        ergebnisWerte = funkWerte(funkWerte1, funkWerte2);
        korrekturUnbekWerte(ergebnisWerte, funkWerte1, funkWerte2);

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
        float[] funkWerte1;
        float[] funkWerte2;
        float[] ergebnisWerte;

        funkWerte1 = argFunktion1.negBspWerte(beispieldaten);
        funkWerte2 = argFunktion2.negBspWerte(beispieldaten);

        if (funkWerte1.length != funkWerte2.length) {
            throw new Error("Anzahl der Funktionswerte ist ungleich");
        }

        ergebnisWerte = funkWerte(funkWerte1, funkWerte2);
        korrekturUnbekWerte(ergebnisWerte, funkWerte1, funkWerte2);

        return ergebnisWerte;
    }
}

