/*
 * Dateiname      : Konzept.java
 * Letzte �nderung: 21. September 2006
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
import java.util.ArrayList;
import java.io.Serializable;
import mathCollection.BitMathIntSet;

import lascer.problemdaten.Beispiel;

/**
 * Definiert ein Konzept.
 *
 * @author  Dietmar Lippold
 */
public interface Konzept extends Serializable {

    /**
     * Liefert eine Menge der Teilkonzepte dieses Konzepts. Wenn dies ein
     * elementares Konzept (ein Literal) ist, wird eine leere Liste geliefert.
     *
     * @return  Eine Menge der Teilkonzepte dieses Konzepts.
     */
    public HashSet teilkonzepte();

    /**
     * Liefert eine Liste der elementaren Teilkonzepte (Literale) dieses
     * Konzepts. In dieser Liste k�nnen einige Literale auch mehrfach
     * vorkommen.
     *
     * @return  Eine Liste der elementaren Teilkonzepte dieses Konzepts.
     */
    public ArrayList elementareTeilkonzepte();

    /**
     * Liefert die Anzahl der Konjunktionen des Konzepts.
     *
     * @return  Die Anzahl der Konjunktionen des Konzepts.
     */
    public int konjunktionsAnz();

    /**
     * Liefert die Anzahl der Disjunktionen des Konzepts.
     *
     * @return  Die Anzahl der Disjunktionen des Konzepts.
     */
    public int disjunktionsAnz();

    /**
     * Liefert die Anzahl der einzelnen Literale des Konzepts, die also direkt
     * in diesem Konzept enthalten sind.
     *
     * @return  Die Anzahl der einzelnen Literale des Konzepts, die also
     *          direkt in diesem Konzept enthalten sind.
     */
    public int einzelLiteralAnz();

    /**
     * Liefert die Anzahl der positiven Literale des Konzepts insgesamt.
     *
     * @return  Die Anzahl der positiven Literale des Konzepts insgesamt.
     */
    public int posLiteralAnz();

    /**
     * Liefert die Anzahl der negativen (invertierten) Literale des Konzepts
     * insgesamt.
     *
     * @return  Die Anzahl der negativen (invertierten) Literale des Konzepts
     *          insgesamt.
     */
    public int negLiteralAnz();

    /**
     * Liefert die Komplexit�t des Konzepts.
     *
     * @return  Die Komplexit�t des Konzepts.
     */
    public float komplexitaet();

    /**
     * Liefert die Menge der Indices der vom Konzept erf�llten positiven
     * Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept erf�llten positiven
     *          Beispiele.
     */
    public BitMathIntSet posErfuelltBsp();

    /**
     * Liefert die Menge der Indices der vom Konzept erf�llten negativen
     * Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept erf�llten negativen
     *          Beispiele.
     */
    public BitMathIntSet negErfuelltBsp();

    /**
     * Liefert die Menge der Indices der vom Konzept richtiger Weise
     * abgedeckten positiven Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept abgedeckten
     *          positiven Beispiele.
     */
    public BitMathIntSet posRichtigBsp();

    /**
     * Liefert die Menge der Indices der vom Konzept richtiger Weise nicht
     * abgedeckten negativen Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept nicht abgedeckten
     *          negativen Beispiele.
     */
    public BitMathIntSet negRichtigBsp();

    /**
     * Liefert die Menge der Indices der vom Konzept f�lschlicher Weise nicht
     * abgedeckten positiven Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept nicht abgedeckten
     *          positiven Beispiele.
     */
    public BitMathIntSet posFalschBsp();

    /**
     * Liefert die Menge der Indices der vom Konzept f�lschlicher Weise
     * abgedeckten negativen Beispiele der zugrunde liegenden Daten.
     *
     * @return  Die Menge der Indices der vom Konzept abgedeckten negativen
     *          Beispiele.
     */
    public BitMathIntSet negFalschBsp();

    /**
     * Liefert die Anzahl der positiven Beispiele der zugrunde liegenden
     * Daten.
     *
     * @return  Die Gesamtanzahl der positiven Beispiele.
     */
    public int posGesamtAnz();

    /**
     * Liefert die Anzahl der negativen Beispiele der zugrunde liegenden
     * Daten.
     *
     * @return  Die Gesamtanzahl der negativen Beispiele.
     */
    public int negGesamtAnz();

    /**
     * Liefert die Anzahl der positiven Beispiele, f�r die das Konzept erf�llt
     * ist.
     *
     * @return  Die Anzahl der positiven Beispiele, f�r die das Konzept
     *          erf�llt ist.
     */
    public int posErfuelltAnz();

    /**
     * Liefert die Anzahl der negativen Beispiele, f�r die das Konzept erf�llt
     * ist.
     *
     * @return  Die Anzahl der negativen Beispiele, f�r die das Konzept
     *          erf�llt ist.
     */
    public int negErfuelltAnz();

    /**
     * Liefert die Anzahl der positiven Beispiele, f�r die dieses Konzept
     * richtiger Weise erf�llt ist.
     *
     * @return  Die Anzahl der positiven Beispiele, f�r die dieses Konzept
     *          richtiger Weise erf�llt ist.
     */
    public int posRichtigAnz();

    /**
     * Liefert die Anzahl der negativen Beispiele, f�r die dieses Konzept
     * richtiger Weise nicht erf�llt ist.
     *
     * @return  Die Anzahl der negativen Beispiele, f�r die dieses Konzept
     *          richtiger Weise nicht erf�llt ist.
     */
    public int negRichtigAnz();

    /**
     * Liefert die Anzahl der positiven Beispiele, f�r die dieses Konzept
     * f�lschlicher Weise nicht erf�llt ist.
     *
     * @return  Die Anzahl der positiven Beispiele, f�r die dieses Konzept
     *          f�lschlicher Weise nicht erf�llt ist.
     */
    public int posFalschAnz();

    /**
     * Liefert die Anzahl der negativen Beispiele, f�r die dieses Konzept
     * f�lschlicher Weise erf�llt ist.
     *
     * @return  Die Anzahl der negativen Beispiele, f�r die dieses Konzept
     *          f�lschlicher Weise erf�llt ist.
     */
    public int negFalschAnz();

    /**
     * Ermittelt, ob dieses Konzept sinnvoll ist f�r die Erzeugung eines
     * insgesamt korrekten und vollst�ndigen Konzepts. Dazu mu� dieses Konzept
     * mindestens ein positives Element der zugrunde liegenden Daten abdecken
     * und ein negatives Element der Daten ausschlie�en, soweit es ein
     * positives bzw. ein negatives Beispiel gibt.
     *
     * @return  Die Angabe, ob dieses Konzept sinnvoll ist.
     */
    public boolean istSinnvoll();

    /**
     * Ermittelt, ob das Konzept korrekt ist, d.h. kein negatives Beispiel
     * der zugrunde liegenden Daten abdeckt.
     *
     * @return  Die Angabe, ob dieses Konzept korrekt ist.
     */
    public boolean istKorrekt();

    /**
     * Ermittelt, ob das Konzept vollst�ndig ist, d.h. alle positiven
     * Beispiele der zugrunde liegenden Daten abdeckt.
     *
     * @return  Die Angabe, ob dieses Konzept vollst�ndig ist.
     */
    public boolean istVollstaendig();

    /**
     * Ermittelt, ob dieses Konzept besser als das �bergebene Konzept ist. Das
     * ist der Fall, wann die Fehleranzahl dieses Konzepts geringer ist als
     * die des anderes Konzepts oder wenn die Fehleranzahl gleich ist und die
     * Komplexit�t dieses Konzepts geringer ist als die des anderen Konzepts.
     *
     * @param anderesKonzept  Ein anderes Konzept, mit dem dieses Konzept
     *                        verglichen wird.
     *
     * @return  Die Angabe, ob dieses Konzept besser als das �bergebene
     *          Konzept ist.
     */
    public boolean istBesser(Konzept anderesKonzept);

    /**
     * Ermittelt, ob dieses Konzept gleich gut wie das �bergebene Konzept ist.
     * Das ist der Fall, wann die Fehleranzahl und die Komplexit�t dieses
     * Konzepts gleich sind zur Fehleranzahl und die Komplexit�t des anderes
     * Konzepts.
     *
     * @param anderesKonzept  Ein anderes Konzept, mit dem dieses Konzept
     *                        verglichen wird.
     *
     * @return  Die Angabe, ob dieses Konzept gleich gut ist wie das
     *          �bergebene Konzept.
     */
    public boolean istGleichGut(Konzept anderesKonzept);

    /**
     * Ermitteln, ob das Konzept auf das �bergebene Beispiel zutrifft bzw. f�r
     * dieses erf�llt ist.
     *
     * @param beispiel  Ein Beispiel, f�r das ermittelt werden soll, ob das
     *                  Konzept darauf zutrifft.
     *
     * @return  Die Angabe, ob das Konzept auf das �bergebene Beispiel
     *          zutrifft.
     */
    public boolean trifftZu(Beispiel beispiel);

    /**
     * Liefert eine Beschreibung des Konzepts.
     *
     * @return  Eine Beschreibung des Konzepts.
     */
    public String toString();

    /**
     * Liefert eine Statistik zum Konzept.
     *
     * @return  Eine Statistik des Konzepts.
     */
    public String statistik();
}

