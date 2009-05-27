/*
 * Dateiname      : TeilmengenPuffer.java
 * Letzte �nderung: 12. August 2006
 * Autoren        : Edgar Binder, Dietmar Lippold
 * Copyright (C)  : Institut f�r Intelligente Systeme Universit�t Stuttgart,
 *                  2006
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


package mengenUeberdeckung.allgemein.itmVerwaltung;

import java.util.Arrays;

import mengenUeberdeckung.allgemein.IndexTeilmenge;

/**
 * Speichert eine begrenzte Menge von Teilmengen, wobei neuere Teilmengen
 * �ltere ersetzten.
 *
 * @author  Edgar Binder, Dietmar Lippold
 */
public class TeilmengenPuffer implements Cloneable {

    /**
     * Enth�lt die gespeicherten Teilmengen.
     */
    private IndexTeilmenge[] gespeicherteTeilmengen;

    /**
     * Erzeugt eine neue Instanz.
     */
    public TeilmengenPuffer() {

        gespeicherteTeilmengen = new IndexTeilmenge[Konstanten.GROESSE_TEILMENGEN_PUFFER];
        Arrays.fill(gespeicherteTeilmengen, null);
    }

    /**
     * Erzeugt eine neue Instanz, die die Teilmengen der �bergebenen Instanz
     * enth�lt.
     */
    public TeilmengenPuffer(TeilmengenPuffer andererPuffer) {
        IndexTeilmenge[] andereTeilmengen;

        andereTeilmengen = andererPuffer.gespeicherteTeilmengen;
        gespeicherteTeilmengen = (IndexTeilmenge[]) andereTeilmengen.clone();
    }

    /**
     * Liefert ein neues Objekt, das zu <CODE>this</CODE> gleich ist.
     *
     * @return  Ein zu diesem Objekt gleiches Objekt.
     */
    public Object clone() {
        return (new TeilmengenPuffer(this));
    }

    /**
     * L�scht den Teilmengenpuffer, d.h. setzt ihn auf den Zustand nach der
     * Erzeugung zur�ck.
     */
    public void clear() {
        Arrays.fill(gespeicherteTeilmengen, null);
    }

    /**
     * F�gt dem Puffer die �bergebene Teilmenge hinzu. Wenn die Teilmenge
     * schon enthalten ist, �ndert sich nichts. Wenn der Puffer bereits voll
     * ist, wird eine enthaltene Teilmenge entfernt und zur�ckgeliefert.
     *
     * @param teilmenge  Die Teilmenge, die <CODE>this</CODE> hinzuef�gt
     *                   werden soll.
     *
     * @return  Die Teilmenge, die entfernt werden mu�te, oder
     *          <CODE>null</CODE>, wenn keine entfernt werden brauchte.
     */
    public IndexTeilmenge teilmengeHinzufuegen(IndexTeilmenge teilmenge) {
        IndexTeilmenge entfernteTeilmenge;

        // Versuchen, die Teilmenge an einer unbelegten Position zu speichern.
        for (int i = 0; i < gespeicherteTeilmengen.length; i++) {
            if (gespeicherteTeilmengen[i] == null) {
                gespeicherteTeilmengen[i] = teilmenge;
                return null;
            }
        }

        // Die erste Teilmenge entfernen, alle anderen verschieben und die
        // neue Teilmenge an der letzten Position speichern.
        entfernteTeilmenge = gespeicherteTeilmengen[0];
        for (int i = 0; i < gespeicherteTeilmengen.length - 1; i++) {
            gespeicherteTeilmengen[i] = gespeicherteTeilmengen[i + 1];
        }
        gespeicherteTeilmengen[gespeicherteTeilmengen.length - 1] = teilmenge;
        return entfernteTeilmenge;
    }

    /**
     * Entfernt die �bergebenen Teilmenge aus der Puffer.
     *
     * @param teilmenge  Die Teilmenge, die aus <CODE>this</CODE> entfernt
     *                   werden soll.
     *
     * @return  <CODE>true</CODE>, wenn die Teilmenge entfernt werden konnte,
     *          anderenfalls <CODE>false</CODE>.
     */
    public boolean teilmengeEntfernen(IndexTeilmenge teilmenge) {

        for (int i = 0; i < gespeicherteTeilmengen.length; i++) {
            if (teilmenge.equals(gespeicherteTeilmengen[i])) {
                // Die nachfolgenden Teilmengen vorziehen.
                for (int j = i; j < gespeicherteTeilmengen.length - 1; j++) {
                    gespeicherteTeilmengen[j] = gespeicherteTeilmengen[j + 1];
                }
                gespeicherteTeilmengen[gespeicherteTeilmengen.length - 1] = null;

                return true;
            }
        }
        return false;
    }

    /**
     * Liefert eine Teilmenge, die den angegebenen Index enth�lt. Wenn es
     * keine solche Teilmenge gibt, wird <CODE>null</CODE> geliefert.
     *
     * @param index  Der Index, zu dem eine ihn enthaltende Teilmenge
     *               geliefert wird.
     *
     * @return  Eine Teilmenge, die den �bergebenen Index enth�lt.
     */
    public IndexTeilmenge enthaltendeTeilmenge(int index) {
        IndexTeilmenge teilmenge;

        for (int i = 0; i < gespeicherteTeilmengen.length; i++) {
            teilmenge = gespeicherteTeilmengen[i];
            if ((teilmenge != null) && teilmenge.indexIstEnthalten(index)) {
                return teilmenge;
            }
        }
        return null;
    }
}

