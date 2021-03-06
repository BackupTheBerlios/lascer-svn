/*
 * Dateiname      : IntFunkSammlung.java
 * Letzte �nderung: 03. Mai 2006
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

import java.util.ArrayList;
import java.io.Serializable;

import lascer.problemdaten.AttributSammlung;
import lascer.problemdaten.attribute.IntAttribut;
import lascer.intfunktionen.arten.NullStellIntFunkErz;
import lascer.intfunktionen.arten.EinStellIntFunkErz;
import lascer.intfunktionen.arten.ZweiStellIntFunkErz;
import lascer.intfunktionen.arten.NullStellIntFunk;
import lascer.intfunktionen.arten.EinStellIntFunk;
import lascer.intfunktionen.arten.ZweiStellIntFunk;
import lascer.intfunktionen.konkrete.IntProjektFunk;

/**
 * Verwaltet elementare int-Funktionen.
 *
 * @author  Dietmar Lippold
 */
public class IntFunkSammlung implements Serializable {

    /**
     * Die gespeicherten Projektions-Funktionen.
     */
    private ArrayList projektFunktionen = new ArrayList();

    /**
     * Die gespeicherten nullstelligen Funktionen.
     */
    private ArrayList nullStellErzeuger = new ArrayList();

    /**
     * Die gespeicherten einstelligen Funktionen.
     */
    private ArrayList einStellErzeuger = new ArrayList();

    /**
     * Die gespeicherten zweistelligen Funktionen.
     */
    private ArrayList zweiStellErzeuger = new ArrayList();

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param attributSammlung  Die Sammlung der Attribute, zu denen die
     *                          Funktionen verwaltet werden sollen.
     */
    public IntFunkSammlung(AttributSammlung attributSammlung) {
        IntAttribut attribut;
        int         intAttributAnz;

        intAttributAnz = attributSammlung.intAttributAnz();
        for (int nummer = 0; nummer < intAttributAnz; nummer++) {
            attribut = attributSammlung.getIntAttribut(nummer);
            projektFunktionen.add(new IntProjektFunk(nummer, attribut));
        }
    }

    /**
     * Nimmt einen Erzeuger nullstelliger Funktionen in die Verwaltung auf.
     *
     * @param erzeuger  Der Erzeuger nullstelliger Funktionen, der in die
     *                  Verwaltung aufgenommen werden soll.
     */
    public void nullStellErzeugerAufnehmen(NullStellIntFunkErz erzeuger) {
        nullStellErzeuger.add(erzeuger);
    }

    /**
     * Nimmt einen Erzeuger einstelliger Funktionen in die Verwaltung auf.
     *
     * @param erzeuger  Der Erzeuger einstelliger Funktionen, der in die
     *                  Verwaltung aufgenommen werden soll.
     */
    public void einStellErzeugerAufnehmen(EinStellIntFunkErz erzeuger) {
        einStellErzeuger.add(erzeuger);
    }

    /**
     * Nimmt einen Erzeuger zweistelliger Funktionen in die Verwaltung auf.
     *
     * @param erzeuger  Der Erzeuger zweistelliger Funktionen, der in die
     *                  Verwaltung aufgenommen werden soll.
     */
    public void zweiStellErzeugerAufnehmen(ZweiStellIntFunkErz erzeuger) {
        zweiStellErzeuger.add(erzeuger);
    }

    /**
     * Liefert die Anzahl unterschiedlicher Projektions-Funktionen, die
     * erzeugt werden k�nnen.
     *
     * @return  Die Anzahl unterschiedlicher Projektions-Funktionen, die
     *          erzeugt werden k�nnen.
     */
    public int projektFunkAnz() {
        return projektFunktionen.size();
    }

    /**
     * Liefert die Anzahl unterschiedlicher elementarer nullstelliger
     * Funktionen, die erzeugt werden k�nnen.
     *
     * @return  Die Anzahl unterschiedlicher elementarer nullstelliger
     *          Funktionen, die erzeugt werden k�nnen.
     */
    public int nullStellFunkAnz() {
        return nullStellErzeuger.size();
    }

    /**
     * Liefert die Anzahl unterschiedlicher elementarer einstelliger
     * Funktionen, die erzeugt werden k�nnen.
     *
     * @return  Die Anzahl unterschiedlicher elementarer einstelliger
     *          Funktionen, die erzeugt werden k�nnen.
     */
    public int einStellFunkAnz() {
        return einStellErzeuger.size();
    }

    /**
     * Liefert die Anzahl unterschiedlicher elementarer zweistelliger
     * Funktionen, die erzeugt werden k�nnen.
     *
     * @return  Die Anzahl unterschiedlicher elementarer zweistelliger
     *          Funktionen, die erzeugt werden k�nnen.
     */
    public int zweiStellFunkAnz() {
        return zweiStellErzeuger.size();
    }

    /**
     * Liefert eine Instanz der Projektions-Funktion mit der angegebenen
     * Nummer.
     *
     * @param nummer  Die Nummer der Projektions-Funktion, die geliefert
     *                werden soll. Die kleinste Nummer ist Null, die gr��te
     *                ist die um Eins verminderte Anzahl der
     *                Projektions-Funktionen.
     *
     * @return  Die Projektions-Funktion mit der angegebenen Nummer oder den
     *          Wert <CODE>null</CODE>, wenn es keine solche Funktion gibt.
     */
    public IntProjektFunk projektFunk(int nummer) {
        if (nummer < projektFunktionen.size()) {
            return ((IntProjektFunk) projektFunktionen.get(nummer));
        } else {
            return null;
        }
    }

    /**
     * Liefert eine Instanz der nullstelligen elementaren Funktion mit der
     * angegebenen Nummer.
     *
     * @param nummer  Die Nummer der zu liefernden nullstelligen elementaren
     *                Funktion. Die kleinste Nummer ist Null, die gr��te ist
     *                die um Eins verminderte Anzahl der nullstelligen
     *                Funktionen.
     *
     * @return  Die nullstellige elementare Funktion mit der angegebenen
     *          Nummer oder den Wert <CODE>null</CODE>, wenn es keine solche
     *          Funktion gibt.
     */
    public NullStellIntFunk nullStelligeFunktion(int nummer) {
        NullStellIntFunkErz erzeuger;

        if (nummer < nullStellErzeuger.size()) {
            erzeuger = (NullStellIntFunkErz) nullStellErzeuger.get(nummer);
            return erzeuger.nullStelligeFunktion(nummer);
        } else {
            return null;
        }
    }

    /**
     * Liefert eine Instanz der einstelligen elementaren Funktion mit der
     * angegebenen Nummer.
     *
     * @param nummer       Die Nummer der zu liefernden einstelligen
     *                     elementaren Funktion. Die kleinste Nummer ist Null,
     *                     die gr��te ist die um Eins verminderte Anzahl der
     *                     einstelligen Funktionen.
     * @param argFunktion  Die Funktion, die als Argument der zu erzeugenden
     *                     einstelligen Funktion dienen soll.
     *
     * @return  Die einstellige elementare Funktion mit der angegebenen
     *          Nummer oder den Wert <CODE>null</CODE>, wenn es keine solche
     *          Funktion gibt.
     */
    public EinStellIntFunk einStelligeFunktion(int nummer,
                                               ErzeugbareIntFunk argFunktion) {
        EinStellIntFunkErz erzeuger;

        if (nummer < einStellErzeuger.size()) {
            erzeuger = (EinStellIntFunkErz) einStellErzeuger.get(nummer);
            return erzeuger.einStelligeFunktion(nummer, argFunktion);
        } else {
            return null;
        }
    }

    /**
     * Liefert eine Instanz der zweistelligen elementaren Funktion mit der
     * angegebenen Nummer.
     *
     * @param nummer        Die Nummer der zu liefernden zweistelligen
     *                      elementaren Funktion. Die kleinste Nummer ist
     *                      Null, die gr��te ist die um Eins verminderte
     *                      Anzahl der zweistelligen Funktionen.
     * @param argFunktion1  Die Funktion, die als ersten Argument der zu
     *                      erzeugenden zweistelligen Funktion dienen soll.
     * @param argFunktion2  Die Funktion, die als zweites Argument der zu
     *                      erzeugenden zweistelligen Funktion dienen soll.
     *
     * @return  Die zweistellige elementare Funktion mit der angegebenen
     *          Nummer oder den Wert <CODE>null</CODE>, wenn es keine solche
     *          Funktion gibt.
     */
    public ZweiStellIntFunk zweiStelligeFunktion(int nummer,
                                                 ErzeugbareIntFunk argFunktion1,
                                                 ErzeugbareIntFunk argFunktion2) {
        ZweiStellIntFunkErz erzeuger;

        if (nummer < zweiStellErzeuger.size()) {
            erzeuger = (ZweiStellIntFunkErz) zweiStellErzeuger.get(nummer);
            return erzeuger.zweiStelligeFunktion(nummer, argFunktion1,
                                                 argFunktion2);
        } else {
            return null;
        }
    }
}

