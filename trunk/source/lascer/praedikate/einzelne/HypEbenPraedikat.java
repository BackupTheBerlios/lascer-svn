/*
 * Dateiname      : HypEbenPraedikat.java
 * Letzte Änderung: 12. September 2006
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


package lascer.praedikate.einzelne;

import java.util.ArrayList;
import geometrischeClusterung.Unterraum;
import geometrischeClusterung.Punkt;
import geometrischeClusterung.Vektor;

import lascer.problemdaten.AttributSammlung;
import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.attribute.RealAttribut;
import lascer.praedikate.Praedikat;

/**
 * Definiert ein Prädikat, das prüft, ob ein Beispiel, charakterisiert durch
 * seine real-Werte, in einem Unterraum auf einer bestimmten Seite einer
 * Hyperebene liegt.
 *
 * @author  Dietmar Lippold
 */
public class HypEbenPraedikat implements Praedikat {

    /**
     * Die Daten, zu deren Attribut-Sammlung das Prädikat erzeugt wurde und
     * aus deren Beispielen die Punkte in <CODE>posBspPunkte</CODE> und
     * <CODE>negBspPunkte</CODE> erzeugt wurden.
     */
    private Beispieldaten bspdaten;

    /**
     * Punkte der positiven Beispiele, die bei der Erzeugung des Prädikats
     * vorhanden waren.
     */
    private ArrayList posBspPunkte = null;

    /**
     * Punkte der negativen Beispiele, die bei der Erzeugung des Prädikats
     * vorhanden waren.
     */
    private ArrayList negBspPunkte = null;

    /**
     * Der Unterraum, zu dem geprüft wird, ob ein Beispiel darin enthalten
     * ist. Wenn der Unterraum kein echter Unterraum ist, ist der Wert
     * <CODE>null</CODE>.
     */
    private Unterraum unterraum;

    /**
     * Ein Punkt der Hyperebene.
     */
    private Punkt enthaltenerPunkt;

    /**
     * Der Normalenvektor der Hyperebene.
     */
    private Vektor normalenVektor;

    /**
     * Der Mindestabstand, den ein Punkt von der Hyperebene haben muß, um
     * auf der richtigen Seite zu liegen.
     */
    private double minAbstand;

    /**
     * Ein Vektor der Namen der nicht-ganzzahligen Attribute eines Beispiels.
     */
    private String attribVektor;

    /**
     * Liefert einen Punkt, der die Werte der nicht-ganzzahligen Attribute
     * des Beispiels besitzt.
     *
     * @param attributSammlung  Die zum Beispiel gehörenden Attribute.
     * @param beispiel          Das Beispiel, zu dem der Punkt erzeugt werden
     *                          soll.
     *
     * @return  Einen Punkt, der die Werte der nicht-ganzzahligen Attribute
     *          des Beispiels besitzt.
     */
    public static Punkt punkt(AttributSammlung attributSammlung, Beispiel beispiel) {
        RealAttribut attribut;
        double[]     werte;

        werte = new double[attributSammlung.realAttributAnz()];
        for (int aNr = 0; aNr < werte.length; aNr++) {
            attribut = attributSammlung.getRealAttribut(aNr);
            werte[aNr] = beispiel.getRealWert(attribut);
        }
        return (new Punkt(werte));
    }

    /**
     * Liefert die Punkte zu den übergebenen Beispielen. Dabei wird ein Punkt
     * aus den Werten der nicht-ganzzahligen Attribute der Beispiele erzeugt.
     *
     * @param attributSammlung  Die zum Beispiel gehörenden Attribute.
     * @param beispiele         Die Beispiel, zu denen die Punkte erzeugt
     *                          werden sollen.
     *
     * @return  Die Punkte zu den übergebenen Beispielen.
     */
    public static ArrayList punkte(AttributSammlung attributSammlung,
                                   ArrayList beispiele) {
        ArrayList punkte;
        Beispiel  beispiel;

        punkte = new ArrayList(beispiele.size());
        for (int bspNr = 0; bspNr < beispiele.size(); bspNr++) {
            beispiel = (Beispiel) beispiele.get(bspNr);
            punkte.add(punkt(attributSammlung, beispiel));
        }
        return punkte;
    }

    /**
     * Erzeugt eine neue Instanz. Die Dimensionalität des Raumes der
     * Hyperebene muß mindestens 1 sein.
     *
     * @param beispieldaten     Daten, zu deren Attribut-Sammlung das Prädikat
     *                          erzeugt wird und zu deren Beispielen die
     *                          übergeben Punkte gehören.
     * @param posBspPunkte      Die Punkte der positiven Beispiele.
     * @param negBspPunkte      Die Punkte der negativen Beispiele.
     * @param unterraum         Der Unterraum, zu dem geprüft wird, ob ein
     *                          Beispiel darin enthalten ist.
     * @param enthaltenerPunkt  Ein Punkt der Hyperebene.
     * @param normalenVektor    Der Normalenvektor der Hyperebene.
     * @param minAbstand        Der Mindestabstand, den ein Punkt von der
     *                          Hyperebene haben muß, um auf der richtigen
     *                          Seite zu liegen.
     */
    public HypEbenPraedikat(Beispieldaten beispieldaten,
                            ArrayList posBspPunkte, ArrayList negBspPunkte,
                            Unterraum unterraum, Punkt enthaltenerPunkt,
                            Vektor normalenVektor, double minAbstand) {
        AttributSammlung attributSammlung;
        StringBuffer     namensvektor;
        RealAttribut     attribut;

        if (unterraum.dimensionUnterraum() == enthaltenerPunkt.dimension()) {
            // Der Unterraum ist der Gesamtraum. Der Punkt eines Beispiels
            // liegt daher immer im Unterraum.
            this.unterraum = null;
        } else {
            // Der Unterraum ist ein echter Unterraum.
            this.unterraum = unterraum;
        }

        this.bspdaten = beispieldaten;
        this.posBspPunkte = posBspPunkte;
        this.negBspPunkte = negBspPunkte;
        this.enthaltenerPunkt = enthaltenerPunkt;
        this.normalenVektor = normalenVektor;
        this.minAbstand = minAbstand;

        attributSammlung = beispieldaten.attributSammlung();
        namensvektor = new StringBuffer();
        namensvektor.append("<");
        for (int aNr = 0; aNr < attributSammlung.realAttributAnz(); aNr++) {
            attribut = attributSammlung.getRealAttribut(aNr);
            namensvektor.append(attribut.name() + ", ");
        }
        namensvektor.deleteCharAt(namensvektor.length() - 1);
        namensvektor.deleteCharAt(namensvektor.length() - 1);
        namensvektor.append(">");
        attribVektor = namensvektor.toString();
    }

    /**
     * Liefert die Komplexität des Prädikats.
     *
     * @return  Die Komplexität des Prädikats.
     */
    public float komplexitaet() {
        return Konstanten.HYP_EBEN_KOMPLEX_FAKTOR * enthaltenerPunkt.dimension();
    }

    /**
     * Liefert die Entfernung des uebergebenen Punktes von der Hyperebene
     * dieses Prädikats. Deren Betrag entspricht dem Abstand des Punktes von
     * der Hyperebene. Ihr Vorzeichen richtet sich nach danach, auf welcher
     * Seite der Hyperebene der Punkt liegt.
     *
     * @param punkt  Ein Punkt, von dem die Entfernung zur Hyperebene dieses
     *               Prädikats ermittelt werden soll.
     *
     * @return  Die Entfernung des Punktes von der Hyperebene dieses
     *          Prädikats.
     */
    private double entfernung(Punkt punkt) {
        double differenz, summe;
        int    dim = punkt.dimension();

        summe = 0;
        for (int k = 0; k < dim; k++) {
            differenz = punkt.komponente(k) - enthaltenerPunkt.komponente(k);
            summe += normalenVektor.komponente(k) * differenz;
        }

        return summe;
    }

    /**
     * Ermittelt, ob der übergebene Punkt auf der Oberseite der Hyperebene
     * dieses Prädikats liegt, d.h. eine positive Entfernung von dieser hat.
     *
     * @param punkt  Der Punkt, dessen Lage ermittelt werden soll.
     *
     * @return  <CODE>true</CODE>, wenn der übergebene Punkt auf der
     *          Oberseite der Hyperebene liegt, anderenfalls
     *          <CODE>false</CODE>.
     */
    private boolean punktLiegtOberhalb(Punkt punkt) {
      return (entfernung(punkt) > minAbstand);
    }

    /**
     * Ermitteln, ob das Prädikat für das Beispiel zum übergebenen Punkt
     * erfüllt ist.
     *
     * @param punkt  Der Punkt eines Beispiels, für das ermittelt werden soll,
     *               ob das Prädikat dafür erfüllt ist.
     *
     * @return  Die Angabe, ob das Prädikat für  den übergebenen Punkt
     *          erfüllt ist.
     */
    private boolean wert(Punkt punkt) {

        if (unterraum == null) {
            return (!punktLiegtOberhalb(punkt));
        } else {
            return (unterraum.enthaelt(punkt, minAbstand)
                    && !punktLiegtOberhalb(punkt));
        }
    }

    /**
     * Ermitteln, ob das Prädikat für das übergebene Beispiel erfüllt ist.
     *
     * @param beispiel    Ein Beispiel, für das ermittelt werden soll, ob das
     *                    Prädikat dafür erfüllt ist.
     * @param invertiert  Die Angabe, ob der Wert des Prädikats invertiert
     *                    werden sollen.
     *
     * @return  Die Angabe, ob das Prädikat für  das übergebene Beispiel
     *          erfüllt ist.
     */
    public boolean wert(Beispiel beispiel, boolean invertiert) {

        if (beispiel.realWertUnbekannt()) {
            return false;
        } else if (invertiert) {
            return !wert(punkt(bspdaten.attributSammlung(), beispiel));
        } else {
            return wert(punkt(bspdaten.attributSammlung(), beispiel));
        }
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Prädikats auf die Beispiele zu den übergebenen Punkten ergeben.
     *
     * @param punkte      Die Punkte, die die Beispiele repräsentieren, für
     *                    die die Werte des Prädikats ermittelt werden sollen.
     * @param invertiert  Die Angabe, ob die Werte des Prädikats invertiert
     *                    werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Prädikats auf die Beispiele der Punkte ergeben.
     */
    private boolean[] bspWerte(ArrayList punkte, boolean invertiert) {
        boolean[] bspWerte;
        Punkt     punkt;

        // Jetzt die Werte für die Punkte errechnen.
        bspWerte = new boolean[punkte.size()];
        for (int pNr = 0; pNr < punkte.size(); pNr++) {
            punkt = (Punkt) punkte.get(pNr);
            if (invertiert) {
                bspWerte[pNr] = !wert(punkt);
            } else {
                bspWerte[pNr] = wert(punkt);
            }
        }

        return bspWerte;
    }

    /**
     * Ändert die übergebenen Werte des Prädikats zu allen Beispiele, die
     * einen unbekannten real-Wert besitzen, auf den übergebenen Wert.
     *
     * @param praedWerte  Die Werte des Prädikats.
     * @param beispiele   Die Liste der Beispiele, für die geprüft wird, ob
     *                    sie einen unbekannten real-Wert besitzen.
     * @param neuerWert   Der Wert, der bei einem Beispiel mit unbekannten
     *                    Wert zugewiesen werden soll.
     */
    private void aenderUnbekWerte(boolean[] praedWerte, ArrayList beispiele,
                                  boolean neuerWert) {
        Beispiel beispiel;

        for (int wertNr = 0; wertNr < praedWerte.length; wertNr++) {
            beispiel = (Beispiel) beispiele.get(wertNr);
            if (beispiel.realWertUnbekannt()) {
                praedWerte[wertNr] = neuerWert;
            }
        }
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Prädikats auf die positiven Beispiele der übergebenen Beispieldaten
     * ergeben.
     *
     * @param beispieldaten  Die Beispieldaten, für deren positive Beispiele
     *                       die Werte des Prädikats ermittelt werden sollen.
     * @param invertiert     Die Angabe, ob die Werte des Prädikats invertiert
     *                       werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Prädikats auf die positiven Beispiele ergeben.
     */
    public boolean[] posBspWerte(Beispieldaten beispieldaten, boolean invertiert) {
        ArrayList punkte;
        boolean[] praedWerte;

        // Die Punkte zu den Beispielen ermitteln.
        if (beispieldaten.posBeispiele().equals(bspdaten.posBeispiele())) {
            punkte = posBspPunkte;
        } else {
            punkte = punkte(bspdaten.attributSammlung(), beispieldaten.posBeispiele());
        }

        praedWerte = bspWerte(punkte, invertiert);
        if (beispieldaten.realWertUnbekannt()) {
            aenderUnbekWerte(praedWerte, beispieldaten.posBeispiele(), false);
        }

        return praedWerte;
    }

    /**
     * Liefert ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     * des Prädikats auf die negativen Beispiele der übergebenen Beispieldaten
     * ergeben.
     *
     * @param beispieldaten  Die Beispieldaten, für deren negativen Beispiele
     *                       die Werte des Prädikats ermittelt werden sollen.
     * @param invertiert     Die Angabe, ob die Werte des Prädikats invertiert
     *                       werden sollen.
     *
     * @return  Ein neu erzeugtes Array mit den Werten, die sich bei Anwendung
     *          des Prädikats auf die negativen Beispiele ergeben.
     */
    public boolean[] negBspWerte(Beispieldaten beispieldaten, boolean invertiert) {
        ArrayList punkte;
        boolean[] praedWerte;

        // Die Punkte zu den Beispielen ermitteln.
        if (beispieldaten.negBeispiele().equals(bspdaten.negBeispiele())) {
            punkte = negBspPunkte;
        } else {
            punkte = punkte(bspdaten.attributSammlung(), beispieldaten.negBeispiele());
        }

        praedWerte = bspWerte(punkte, invertiert);
        if (beispieldaten.realWertUnbekannt()) {
            aenderUnbekWerte(praedWerte, beispieldaten.negBeispiele(), true);
        }

        return praedWerte;
    }

    /**
     * Liefert eine Beschreibung des Prädikats.
     *
     * @return  Eine Beschreibung des Prädikats.
     */
    public String toString() {
        StringBuffer beschreibung = new StringBuffer();

        beschreibung.append("((" + attribVektor + " - "
                            + enthaltenerPunkt.komponentenDarstellung() + ") * "
                            + normalenVektor.komponentenDarstellung()
                            + " <= " + minAbstand + ")");

        if (unterraum == null) {
            return beschreibung.toString();
        } else {
            return ("(" + attribVektor + " in Unterraum) AND "
                    + beschreibung.toString());
        }
    }
}

