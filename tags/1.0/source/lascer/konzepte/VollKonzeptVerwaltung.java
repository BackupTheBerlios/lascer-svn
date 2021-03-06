/*
 * Dateiname      : VollKonzeptVerwaltung.java
 * Letzte �nderung: 21. November 2007
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2007
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

import java.util.Random;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import mathCollection.BitMathIntSet;

import lascer.praedikate.einzelne.BoolWertPraedikat;
import lascer.konzepte.einzelne.Literal;
import lascer.konzepte.mengen.VollstaendigeKonzepte;
import lascer.konzepte.mengen.AlgVollErzKonzepte;
import lascer.praedikate.Praedikat;
import lascer.problemdaten.Beispieldaten;

/**
 * Verwaltet eine Menge von vollst�ndigen Konzepte und steuert deren
 * Erzeugung. Die Menge stellt eine Auswahl der zur Aufnahme �bergebenen
 * Konzepte dar.
 *
 * @author  Dietmar Lippold
 */
public class VollKonzeptVerwaltung implements KonzeptVerwaltung, Serializable {

    /**
     * Der zu verwendende Zufallsgenerator.
     */
    private final Random random;

    /**
     * Die der Konzeptbildung zugrunde liegenden Beispieldaten.
     */
    private final Beispieldaten beispieldaten;

    /**
     * Die Menge der aufgenommenden vollst�ndigen Konzepte.
     */
    private transient VollstaendigeKonzepte vollstaendigeKonzepte;

    /**
     * Die Menge der aufgenommenden allgemeinen Konzepte zur Erzeugung
     * vollst�ndiger Konzepte.
     */
    private transient AlgVollErzKonzepte algVollErzKonzepte = null;

    /**
     * Faktor, mit dem die Summe der Kosten der ausgeschlossenen Indices
     * multipliziert wird, um die Kosten f�r einen nicht ausgeschlossenen
     * Index zu errechnen.
     */
    private float kostenFaktor;

    /**
     * Die gesch�tzte maximale Gesamt-Komplexit�t einer Formel. Dieser Wert
     * ist nur bei Vorgabe der Anzahl an Literalen, die maximal in einer
     * Disjunktion bzw. Konjunktion enthalten sein sollen, (Attribut
     * <CODE>maxLiteralAnz</CODE>) von Bedeutung.
     */
    private float gesamtKomplex;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * allgemeinen Konzeptmengen. Ein negativer Wert steht f�r eine
     * unbeschr�nkte Anzahl.
     */
    private int algItmAnz;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * speziellen, d.h. korrekten oder vollst�ndigen, Konzeptmengen. Ein
     * negativer Wert steht f�r eine unbeschr�nkte Anzahl.
     */
    private int spezItmAnz;

    /**
     * Die Anzahl der Literale, die maximal in einer Disjunktion bzw. in einer
     * Konjunktion enthalten sein sollen. Der Wert Null steht f�r eine
     * unbegrenzte Anzahl.
     */
    private int maxLiteralAnz;

    /**
     * Gibt an, in welchem Ausma� die Teilmengen besonders Speicher-effizient
     * aber dadurch weniger Laufzeit-effizient verwaltet werden sollen. Der
     * Wert ist gleich oder gr��er Null (maximale Laufzeit-Effizienz) und
     * kleiner oder gleich Zwei (maximale Speicher-Effizienz).
     */
    private int speicherEffizienz;

    /**
     * Gibt an, ob boolsche Attribute negiert werden sollen, d.h. invertierte
     * Literale dazu erzeugt werden sollen.
     */
    private boolean negBoolPraedErz;

    /**
     * Gibt an, ob bei der letzten Erzeugung der Menge f�r die allgemeinen
     * Konzepte, d.h. beim letzten Aufruf der Methode
     * <CODE>erzAlgKonzeptMenge</CODE>, die beste erzeugbare Formel Fehler
     * enthielt.
     */
    private boolean zuletztFehlerVorhanden = true;

    /**
     * Gibt an, ob es m�glich ist, da� alle allgemeinen Konzepte, die seit dem
     * letzten Aufruf von <CODE>erzAlgKonzeptMenge</CODE> hinzugef�gt wurden,
     * noch in der Auswahl enthalten sind.
     */
    private boolean algKonzEnthaltMoegl = false;

    /**
     * Liefert eine neue Instanz, die bis auf den enthaltenen Zufallsgenerator
     * identisch ist zur �bergebenen Instanz, d.h. zus�tzlich zum �bergebenen
     * Zufallsgenerator die identischen Attribut-Werte wie die �bergebene
     * Instanz besitzt.
     *
     * @param konzeptVerwaltung  Die Instanz, deren Attribute-Werte �bernommen
     *                           werden sollen.
     * @param random             Der Zufallsgenerator f�r die zu erzeugende
     *                           Instanz.
     */
    public VollKonzeptVerwaltung(VollKonzeptVerwaltung konzeptVerwaltung,
                                 Random random) {

        this.random = random;
        this.beispieldaten = konzeptVerwaltung.beispieldaten;
        this.vollstaendigeKonzepte = konzeptVerwaltung.vollstaendigeKonzepte;
        this.algVollErzKonzepte = konzeptVerwaltung.algVollErzKonzepte;
        this.kostenFaktor = konzeptVerwaltung.kostenFaktor;
        this.gesamtKomplex = konzeptVerwaltung.gesamtKomplex;
        this.algItmAnz = konzeptVerwaltung.algItmAnz;
        this.spezItmAnz = konzeptVerwaltung.spezItmAnz;
        this.maxLiteralAnz = konzeptVerwaltung.maxLiteralAnz;
        this.speicherEffizienz = konzeptVerwaltung.speicherEffizienz;
        this.negBoolPraedErz = konzeptVerwaltung.negBoolPraedErz;
        this.zuletztFehlerVorhanden = konzeptVerwaltung.zuletztFehlerVorhanden;
    }

    /**
     * Erzeugt eine neue Instanz.
     *
     * @param random             Der zu verwendende Zufallsgenerator.
     * @param beispieldaten      Die der Konzeptbildung zugrunde liegenden
     *                           Beispieldaten.
     * @param kostenFaktor       Der Faktor, mit dem die Summe der Kosten der
     *                           ausgeschlossenen Indices multipliziert wird,
     *                           um die Kosten f�r einen nicht
     *                           ausgeschlossenen Index zu errechnen.
     * @param gesamtKomplex      Die gesch�tzte maximale Gesamt-Komplexit�t
     *                           einer Formel.
     * @param algItmAnz          Die minimale Anzahl der zu speichernden
     *                           Teilmengen zur Auswahl der allgemeinen
     *                           Konzeptmengen. Ein negativer Wert steht f�r
     *                           eine unbeschr�nkte Anzahl.
     * @param spezItmAnz         Die minimale Anzahl der zu speichernden
     *                           Teilmengen zur Auswahl der speziellen
     *                           Konzeptmengen. Ein negativer Wert steht f�r
     *                           eine unbeschr�nkte Anzahl.
     * @param maxLiteralAnz      Die Anzahl der Literale, die maximal in einer
     *                           Disjunktion bzw. Konjunktion enthalten sein
     *                           sollen. Der Wert Null steht f�r eine
     *                           unbegrenzte Anzahl.
     * @param speicherEffizienz  Gibt an, in welchem Ausma� die Teilmengen
     *                           besonders Speicher-effizient aber dadurch
     *                           weniger Laufzeit-effizient verwaltet werden
     *                           sollen. Der Wert mu� zwischen Null und Zwei
     *                           liegen.
     * @param negBoolPraedErz    Gibt an, ob boolsche Attribute negiert werden
     *                           sollen, d.h. invertierte Literale dazu
     *                           erzeugt werden sollen.
     */
    public VollKonzeptVerwaltung(Random random, Beispieldaten beispieldaten,
                                 float kostenFaktor,  float gesamtKomplex,
                                 int algItmAnz, int spezItmAnz,
                                 int maxLiteralAnz, int speicherEffizienz,
                                 boolean negBoolPraedErz) {

        this.random = random;
        this.beispieldaten = beispieldaten;
        this.kostenFaktor = kostenFaktor;
        this.gesamtKomplex = gesamtKomplex;
        this.algItmAnz = algItmAnz;
        this.spezItmAnz = spezItmAnz;
        this.maxLiteralAnz = maxLiteralAnz;
        this.speicherEffizienz = speicherEffizienz;
        this.negBoolPraedErz = negBoolPraedErz;
        this.vollstaendigeKonzepte = new VollstaendigeKonzepte(random,
                                                               beispieldaten,
                                                               gesamtKomplex,
                                                               maxLiteralAnz,
                                                               spezItmAnz,
                                                               speicherEffizienz);
    }

    /**
     * Erzeugt eine neue Instanz. Der Zufallsgenerator wird mit einem festen
     * Wert initialisiert, die Anzahl der Literale in einer Disjunktion oder
     * Konjunktion ist nicht beschr�nkt und es erfolgt eine besonders
     * Laufzeit-Effiziente Verwaltung der Teilmengen.
     *
     * @param beispieldaten  Die der Konzeptbildung zugrunde liegenden
     *                       Beispieldaten.
     * @param algItmAnz      Die minimale Anzahl der zu speichernden
     *                       Teilmengen zur Auswahl der allgemeinen
     *                       Konzeptmengen. Ein negativer Wert steht f�r eine
     *                       unbeschr�nkte Anzahl.
     * @param spezItmAnz     Die minimale Anzahl der zu speichernden
     *                       Teilmengen zur Auswahl der speziellen
     *                       Konzeptmengen. Ein negativer Wert steht f�r eine
     *                       unbeschr�nkte Anzahl.
     */
    public VollKonzeptVerwaltung(Beispieldaten beispieldaten,
                                 int algItmAnz, int spezItmAnz) {

        this(new Random(Konstanten.SEED), beispieldaten,
             Konstanten.KOSTEN_FAKTOR_INIT, 0, algItmAnz, spezItmAnz, 0, 0,
             lascer.Konstanten.NEG_BOOL_PRAED_ERZ);
    }

    /**
     * Liest dieses Objekt aus dem �bergebenen Stream.
     *
     * @param stream  Der Stream, aus dem dieses Objekt gelesen werden soll.
     *
     * @throws IOException             Wenn bein Lesen des serialisierten
     *                                 Objekts eine Ausnahme auftrat.
     * @throws ClassNotFoundException  Wenn die Klasse dieser Instanz nicht
     *                                 gefunden werden konnte.
     */
    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException {

        HashSet vollKonzepte, algKonzepte;
        float[] ausschlussGuete;

        // Lesen der serialisierbaren Attribute.
        stream.defaultReadObject();

        // Rekonstruktion vom Wert des Attributs vollstaendigeKonzepte.
        vollKonzepte = (HashSet) stream.readObject();
        vollstaendigeKonzepte = new VollstaendigeKonzepte(random, beispieldaten,
                                                          gesamtKomplex,
                                                          maxLiteralAnz,
                                                          spezItmAnz,
                                                          speicherEffizienz);
        vollstaendigeKonzepte.aufnehmen(vollKonzepte);

        // Rekonstruktion vom Wert des Attributs algVollErzKonzepte.
        algKonzepte = (HashSet) stream.readObject();
        if (algKonzepte == null) {
            algVollErzKonzepte = null;
        } else {
            ausschlussGuete = vollstaendigeKonzepte.negAusschlussGuete();
            algVollErzKonzepte = new AlgVollErzKonzepte(random, beispieldaten,
                                                        ausschlussGuete, kostenFaktor,
                                                        algItmAnz, maxLiteralAnz,
                                                        speicherEffizienz);
            algVollErzKonzepte.aufnehmen(algKonzepte);
        }
    }

    /**
     * Schreibt dieses Objekt in den �bergebenen Stream.
     *
     * @param stream  Der Stream, in den dieses Objekt geschrieben werden
     *                soll.
     *
     * @throws IOException  Wenn bein Schreiben des serialisierten Objekts
     +                      eine Ausnahme auftrat.
     */
    private void writeObject(ObjectOutputStream stream)
        throws IOException {

        stream.defaultWriteObject();
        stream.writeObject(vollstaendigeKonzepte.enthalteneKonzepte());
        if (algVollErzKonzepte == null) {
            stream.writeObject(null);
        } else {
            stream.writeObject(algVollErzKonzepte.enthalteneKonzepte());
        }
    }

    /**
     * Liefert eine neue Instanz, die bis auf den enthaltenen Zufallsgenerator
     * identisch ist mit dieser Instanz, d.h. bis auf den Zufallsgenerator die
     * identischen Attribut-Werte besitzt.
     *
     * @param rand  Der Zufallsgenerator f�r die zu erzeugende Instanz.
     *
     * @return  Eine zu dieser Instanz identische Instanz, die aber den
     *          �bergebenen Zufallsgenerator besitzt.
     */
    public KonzeptVerwaltung flacheKopie(Random rand) {
        return (new VollKonzeptVerwaltung(this, rand));
    }

    /**
     * Liefert den Faktor, mit dem die Summe der Kosten der �berdeckten bzw.
     * ausgeschlossenen Indices multipliziert wird, um die Kosten f�r einen
     * nicht �berdeckten bzw. ausgeschlossenen Index zu errechnen.
     *
     * @return  Den enthaltenen Kostenfaktor.
     */
    public float gibKostenFaktor() {
        return kostenFaktor;
    }

    /**
     * Nimmt das �bergebene Konzept in die passende Konzeptmenge auf. Wenn es
     * sich um ein allgemeines Konzept handelt und die Menge f�r die
     * allgemeinen Konzepte wurde noch nicht erzeugt, wird das Konzept nicht
     * aufgenommen.
     *
     * @param konzept  Das aufzunehmende Konzept.
     *
     * @return  <CODE>true</CODE>, wenn das Konzept in die Auswahl aufgenommen
     *          wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean konzeptAufnehmen(Konzept konzept) {
        boolean aufgenommen = false;

        if (konzept.istSinnvoll()) {
            if (konzept.istVollstaendig()) {
                aufgenommen = vollstaendigeKonzepte.aufnehmen(konzept);
            } else {
                if (algVollErzKonzepte != null) {
                    aufgenommen = algVollErzKonzepte.aufnehmen(konzept);
                }
            }
        }
        return aufgenommen;
    }

    /**
     * Nimmt die �bergebenen Konzepte in die passenden Konzeptmengen auf. Ein
     * allgemeines Konzept wird dabei nur aufgenommen, wenn die Menge f�r die
     * allgemeinen Konzepte schon erzeugt wurde.
     *
     * @param konzepte  Die aufzunehmenden Konzepte.
     *
     * @return  <CODE>true</CODE>, wenn mindestens eines der Konzept in die
     *          Auswahl aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean konzepteAufnehmen(Collection konzepte) {
        Iterator konzIter = konzepte.iterator();
        Konzept  konzept;
        boolean  aufgenommen = false;

        while (konzIter.hasNext()) {
            konzept = (Konzept) konzIter.next();
            aufgenommen |= konzeptAufnehmen(konzept);
        }
        return aufgenommen;
    }

    /**
     * Nimmt das �bergebene Pr�dikat in die passenden Konzeptmengen auf.
     * Wenn es ein allgemeines Konzept darstellt und die Mengen f�r die
     * allgemeinen Konzepte wurden noch nicht erzeugt, wird das Pr�dikat nicht
     * aufgenommen.
     *
     * @param praedikat   Das aufzunehmende Pr�dikat.
     * @param invertiert  Gibt an, ob das Pr�dikat bei der Aufnahme invertiert
     *                    werden soll.
     *
     * @return  <CODE>true</CODE>, wenn das Pr�dikat in die Auswahl
     *          aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean praedikatAufnehmen(Praedikat praedikat, boolean invertiert) {
        Literal konzept;

        if ((praedikat instanceof BoolWertPraedikat)
                && invertiert && !negBoolPraedErz) {
            return false;
        } else {
            konzept = new Literal(praedikat, beispieldaten, invertiert);
            return konzeptAufnehmen(konzept);
        }
    }

    /**
     * Nimmt das �bergebene Konzept in die passende Konzeptmenge auf, wenn es
     * das positive Beispiel mit dem angegebenen Index enth�lt und das
     * negative Beispiel mit dem angegebenen Index nicht enth�lt. Wenn es sich
     * um ein allgemeines Konzept handelt und die Menge f�r die allgemeinen
     * Konzepte wurde noch nicht erzeugt, wird das Konzept nicht aufgenommen.
     *
     * @param konzept   Das aufzunehmende Konzept.
     * @param posIndex  Der Index eines positiven Beispiels, das das Konzept
     *                  f�r die Aufnahme enthalten mu�. Ein negativer Wert
     *                  wird ignoriert.
     * @param negIndex  Der Index eines negativen Beispiels, das das Konzept
     *                  f�r die Aufnahme nicht enthalten darf. Ein negativer
     *                  Wert wird ignoriert.
     *
     * @return  <CODE>true</CODE>, wenn das Konzept in die Auswahl aufgenommen
     *          wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean konzeptAufnehmen(Konzept konzept,
                                    int posIndex, int negIndex) {

        if (((posIndex < 0) || konzept.posErfuelltBsp().contains(posIndex))
            && ((negIndex < 0) || !konzept.negErfuelltBsp().contains(negIndex))) {

            return konzeptAufnehmen(konzept);
        } else {
            return false;
        }
    }

    /**
     * Nimmt das �bergebene Pr�dikat in die passende Konzeptmenge auf, wenn es
     * das positive Beispiel mit dem angegebenen Index enth�lt und das
     * negative Beispiel mit dem angegebenen Index nicht enth�lt. Wenn es ein
     * allgemeines Konzept darstellt und die Menge f�r die allgemeinen
     * Konzepte wurde noch nicht erzeugt, wird das Pr�dikat nicht aufgenommen.
     *
     * @param praedikat   Das aufzunehmende Pr�dikat.
     * @param invertiert  Gibt an, ob das Pr�dikat bei der Aufnahme invertiert
     *                    werden soll.
     * @param posIndex    Der Index eines positiven Beispiels, das das
     *                    Pr�dikat f�r die Aufnahme enthalten mu�. Ein
     *                    negativer Wert wird ignoriert.
     * @param negIndex    Der Index eines negativen Beispiels, das das
     *                    Pr�dikat f�r die Aufnahme nicht enthalten darf. Ein
     *                    negativer Wert wird ignoriert.
     *
     * @return  <CODE>true</CODE>, wenn das Pr�dikat in die Auswahl
     *          aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean praedikatAufnehmen(Praedikat praedikat, boolean invertiert,
                                      int posIndex, int negIndex) {
        Literal konzept;

        if ((praedikat instanceof BoolWertPraedikat)
                && invertiert && !negBoolPraedErz) {
            return false;
        } else {
            konzept = new Literal(praedikat, beispieldaten, invertiert);
            return konzeptAufnehmen(konzept, posIndex, negIndex);
        }
    }

    /**
     * Nimmt das �bergebene Konzept unter bestimmten Bedingungen in die
     * Menge der vollst�ndigen Konzepte auf. Wenn es ein allgemeines Konzept
     * darstellt, wird das Konzept nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, da� das Konzept ein vollst�ndiges
     * Konzept einer Formel ersetzen kann. Die Fehler der um das vollst�ndige
     * Konzept reduzierten Formel werden dazu �bergeben.
     *
     * @param konzept           Das aufzunehmende Konzept.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele der Formel,
     *                          die durch die Entfernung des vollst�ndigen
     *                          Konzepts entstanden sind.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele der Formel,
     *                          die durch die Entfernung des vollst�ndigen
     *                          Konzepts entstanden sind.
     *
     * @return  <CODE>true</CODE>, wenn das Konzept in die Auswahl aufgenommen
     *          wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean konzeptAufnehmen(Konzept konzept,
                                    BitMathIntSet redKonzPosFehler,
                                    BitMathIntSet redKonzNegFehler) {

        if (konzept.istVollstaendig()
                && konzept.negFalschBsp().isDisjoint(redKonzNegFehler)) {
            return konzeptAufnehmen(konzept);
        } else {
            return false;
        }
    }

    /**
     * Nimmt das �bergebene Pr�dikat unter bestimmten Bedingungen in die
     * Menge der vollst�ndigen Konzepte auf. Wenn es ein allgemeines Konzept
     * darstellt, wird das Pr�dikat nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, da� das aus dem Pr�dikat erzeugte
     * Konzept ein vollst�ndiges Konzept einer Formel ersetzen kann. Die
     * Fehler der um das vollst�ndiges Konzept reduzierten Formel werden dazu
     * �bergeben.
     *
     * @param praedikat         Das aufzunehmende Pr�dikat.
     * @param invertiert        Gibt an, ob das Pr�dikat bei der Aufnahme
     *                          invertiert werden soll.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele der Formel,
     *                          die durch die Entfernung des vollst�ndigen
     *                          Konzepts entstanden sind.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele der Formel,
     *                          die durch die Entfernung des vollst�ndigen
     *                          Konzepts entstanden sind.
     *
     * @return  <CODE>true</CODE>, wenn das Pr�dikat in die Auswahl
     *          aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean praedikatAufnehmen(Praedikat praedikat, boolean invertiert,
                                      BitMathIntSet redKonzPosFehler,
                                      BitMathIntSet redKonzNegFehler) {
        Literal konzept;

        if ((praedikat instanceof BoolWertPraedikat)
                && invertiert && !negBoolPraedErz) {
            return false;
        } else {
            konzept = new Literal(praedikat, beispieldaten, invertiert);
            return konzeptAufnehmen(konzept, redKonzPosFehler, redKonzNegFehler);
        }
    }

    /**
     * Nimmt das �bergebene Konzept unter bestimmten Bedingungen in die
     * passende Konzeptmenge auf. Wenn es ein allgemeines Konzept darstellt
     * und die Menge f�r die allgemeinen Konzepte wurde noch nicht erzeugt,
     * wird das Pr�dikat nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, da� das Konzept ein Teilkonzept eines
     * vollst�ndiges Konzepts ersetzen kann. Die Fehler des um das Teilkonzept
     * reduzierten vollst�ndigen Konzepts werden zusammen mit der Komplexit�t
     * und der Anzahl der Fehler des Teilkonzepts �bergeben.
     *
     * @param konzept           Das aufzunehmende Konzept.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele des
     *                          vollst�ndigen Konzepts, aus dem das
     *                          Teilkonzept entfernt wurde.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele des
     *                          vollst�ndigen Konzepts, aus dem das
     *                          Teilkonzept entfernt wurde.
     * @param komplex           Die Komplexit�t des Teilkonzepts.
     * @param posFehlAnz        Die Anzahl der Fehler der positiven Beispiele
     *                          des Teilkonzepts.
     * @param negFehlAnz        Die Anzahl der Fehler der negativen Beispiele
     *                          des Teilkonzepts.
     *
     * @return  <CODE>true</CODE>, wenn das Konzet in die Auswahl aufgenommen
     *          wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean konzeptAufnehmen(Konzept konzept,
                                    BitMathIntSet redKonzPosFehler,
                                    BitMathIntSet redKonzNegFehler,
                                    float komplex, int posFehlAnz, int negFehlAnz) {

        if (((konzept.komplexitaet() < komplex)
             || (konzept.negFalschAnz() < negFehlAnz))
            && (konzept.posFalschBsp().isDisjoint(redKonzPosFehler))) {

            return konzeptAufnehmen(konzept);
        } else {
            return false;
        }
    }

    /**
     * Nimmt das �bergebene Pr�dikat unter bestimmten Bedingungen in die
     * passende Konzeptmenge auf. Wenn es ein allgemeines Konzept darstellt
     * und die Menge f�r die allgemeinen Konzepte wurde noch nicht erzeugt,
     * wird das Pr�dikat nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, da� das aus dem Pr�dikat erzeugte
     * Konzept ein Teilkonzept eines vollst�ndigen Konzepts ersetzen kann. Die
     * Fehler des um das Teilkonzept reduzierten vollst�ndigen Konzepts werden
     * zusammen mit der Komplexit�t und der Anzahl der Fehler des Teilkonzepts
     * �bergeben.
     *
     * @param praedikat         Das aufzunehmende Pr�dikat.
     * @param invertiert        Gibt an, ob das Pr�dikat bei der Aufnahme
     *                          invertiert werden soll.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele des
     *                          vollst�ndigen Konzepts, aus dem das
     *                          Teilkonzept entfernt wurde.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele des
     *                          vollst�ndigen Konzepts, aus dem das
     *                          Teilkonzept entfernt wurde.
     * @param komplex           Die Komplexit�t des Teilkonzepts.
     * @param posFehlAnz        Die Anzahl der Fehler der positiven Beispiele
     *                          des Teilkonzepts.
     * @param negFehlAnz        Die Anzahl der Fehler der negativen Beispiele
     *                          des Teilkonzepts.
     *
     * @return  <CODE>true</CODE>, wenn das Pr�dikat in die Auswahl
     *          aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean praedikatAufnehmen(Praedikat praedikat, boolean invertiert,
                                      BitMathIntSet redKonzPosFehler,
                                      BitMathIntSet redKonzNegFehler,
                                      float komplex, int posFehlAnz, int negFehlAnz) {
        Literal konzept;

        if ((praedikat instanceof BoolWertPraedikat)
                && invertiert && !negBoolPraedErz) {
            return false;
        } else {
            konzept = new Literal(praedikat, beispieldaten, invertiert);
            return konzeptAufnehmen(konzept, redKonzPosFehler, redKonzNegFehler,
                                    komplex, posFehlAnz, negFehlAnz);
        }
    }

    /**
     * Liefert eine neue Menge der allgemeinen Konzepte. Dies geschieht mit
     * Bezug auf den aktuellen Inhalt der Menge der vollst�ndigen Konzepte.
     * Wenn die Menge der allgemeiner Konzepte schon vorhanden ist, k�nnen
     * deren Konzepte in die neu erzeugte Menge �bernommen werden.
     *
     * @param konzepteUebernehmen  Gibt an, ob die vorhandenen allgemeinen
     *                             Konzepte in die neue Menge �bernommen
     *                             werden sollen.
     *
     * @return  Eine neue Menge der allgemeinen Konzepte.
     */
    private AlgVollErzKonzepte neueAlgVollKonzepte(boolean konzepteUebernehmen) {
        AlgVollErzKonzepte neuAlgVollKonz;
        float[]            ausschlussGuete;

        ausschlussGuete = vollstaendigeKonzepte.negAusschlussGuete();
        neuAlgVollKonz = new AlgVollErzKonzepte(random, beispieldaten,
                                                ausschlussGuete, kostenFaktor,
                                                algItmAnz, maxLiteralAnz,
                                                speicherEffizienz);
        if ((algVollErzKonzepte != null) && konzepteUebernehmen) {
            neuAlgVollKonz.aufnehmen(algVollErzKonzepte.enthalteneKonzepte());
        }
        return neuAlgVollKonz;
    }

    /**
     * Erzeugt die Menge f�r die allgemeinen Konzepte. Dies geschieht mit
     * Bezug auf den aktuellen Inhalt der Menge der vollst�ndigen Konzepte.
     * Wenn die Menge der allgemeiner Konzepte schon vorhanden ist, k�nnen
     * deren Konzepte in die neu erzeugte Menge �bernommen werden.
     *
     * @param konzepteUebernehmen  Gibt an, ob die vorhandenen allgemeinen
     *                             Konzepte in die neue Menge �bernommen
     *                             werden sollen.
     */
    public void erzAlgKonzeptMenge(boolean konzepteUebernehmen) {

        algKonzEnthaltMoegl = true;
        zuletztFehlerVorhanden = !vollstaendigeKonzepte.fehlerfreieFormelErzeugbar();
        algVollErzKonzepte = neueAlgVollKonzepte(konzepteUebernehmen);
    }

    /**
     * Gibt an, ob bei der letzten Erzeugung der Menge f�r die allgemeinen
     * Konzepte, d.h. beim letzten Aufruf der Methode
     * <CODE>erzAlgKonzeptMenge</CODE>, die beste erzeugbare Formel Fehler
     * enthielt. Wenn die Menge noch nicht erzeugt wurde, d.h. die Methode
     * noch nicht aufgerufen wurde, wird <CODE>true</CODE> geliefert.
     *
     * @return  <CODE>true</CODE>, wenn bei der letzten Erzeugung der Menge
     *          f�r die allgemeinen Konzepte die beste erzeugbare Formel
     *          Fehler enthielt, anderenfalls <CODE>false</CODE>.
     */
    public boolean zuletztFehlerVorhanden() {
        return zuletztFehlerVorhanden;
    }

    /**
     * Liefert eine Kopie des �bergebenen Arrays, wobei aber einige der Werte,
     * die Null waren, auf Eins ge�ndert wurden. Wenn das �bergebene Array
     * Null-Werte enth�lt, bleibt mindestens einer unver�ndert.
     *
     * @param werte      Das Array der Werte, in dessen Kopie einige
     *                   Null-Werte auf Eins ge�ndert werden sollen.
     * @param aenderWkt  Die Wahrscheinlichkeit, mit der ein Null-Werte
     +                   ge�ndert werden soll.
     *
     * @return  Die ge�nderte Kopie vom �bergebenen Array.
     */
    private float[] zufaelligGeaendert(float[] werte, float aenderWkt) {
        float[] neueWerte;
        int     nullAnzGeaendert, nullAnzUnveraendert;

        neueWerte = new float[werte.length];
        do {
            nullAnzGeaendert = 0;
            nullAnzUnveraendert = 0;
            for (int i = 0; i < werte.length; i++) {
                if (werte[i] == 0) {
                    if (random.nextFloat() < aenderWkt) {
                        neueWerte[i] = 1;
                        nullAnzGeaendert++;
                    } else {
                        neueWerte[i] = 0;
                        nullAnzUnveraendert++;
                    }
                } else {
                    neueWerte[i] = werte[i];
                }
            }
        } while ((nullAnzGeaendert > 0) && (nullAnzUnveraendert == 0));

        return neueWerte;
    }

    /**
     * Liefert ein vollst�ndigen Konzept, das aus der Menge der allgemeinen
     * Konzepte erzeugt wurde. Dies geschieht mit Bezug auf den aktuellen
     * Inhalt der Menge der vollst�ndigen Konzepte, wobei aber einige der
     * bisher nicht ausgeschlossenen Beispiele ausgeschlossen behandelt
     * werden. Wenn die Menge der allgemeinen Konzepte noch nicht erzeugt
     * wurden, wird keines erzeugt.
     *
     * @param scpIterAnz        Die Anzahl der Iterationen im SCP-Verfahren.
     * @param aenderWkt         Die Wahrscheinlichkeit, mit der ein nicht
     *                          �berdecktes Beispiel als �berdeckt behandelt
     *                          werden sollen.
     * @param bekanntesKonzept  Ein bekanntes vollst�ndiges Konzept oder der
     *                          Wert <CODE>null</CODE>. Wenn kein besseres
     *                          Konzept erzeugt werden kann, wird dieses
     *                          zur�ck geliefert.
     *
     * @return  Das erzeugte vollst�ndige Konzept oder den Wert
     *          <CODE>null</CODE>, wenn keines erzeugt werden konnte.
     */
    private KombiKonzept erzeugtesKonzept(int scpIterAnz, float aenderWkt,
                                          Konzept bekanntesKonzept) {
        HashSet konzeptMenge;
        float[] negAusschlussGuete;

        if (algVollErzKonzepte != null) {
            // Die Menge der allgemeinen Konzepte wurde schon erzeugt.
            negAusschlussGuete = vollstaendigeKonzepte.negAusschlussGuete();
            if (aenderWkt > 0) {
                negAusschlussGuete = zufaelligGeaendert(negAusschlussGuete,
                                                        aenderWkt);
            }
            konzeptMenge = algVollErzKonzepte.vollstaendigeKonzepte(negAusschlussGuete,
                                                                    scpIterAnz, true,
                                                                    bekanntesKonzept);
            if (konzeptMenge.isEmpty()) {
                return null;
            } else {
                return ((KombiKonzept) konzeptMenge.toArray()[0]);
            }
        } else {
            return null;
        }
    }

    /**
     * Liefert ein vollst�ndigen Konzept, das aus der Menge der allgemeinen
     * Konzepte erzeugt wurde. Dies geschieht mit Bezug auf den aktuellen
     * Inhalt der Menge der vollst�ndigen Konzepte. Wenn die Menge der
     * allgemeinen Konzepte noch nicht erzeugt wurden, wird keines erzeugt.
     *
     * @param scpIterAnz        Die Anzahl der Iterationen im SCP-Verfahren.
     * @param bekanntesKonzept  Ein bekanntes vollst�ndiges Konzept oder der
     *                          Wert <CODE>null</CODE>. Wenn kein besseres
     *                          Konzept erzeugt werden kann, wird dieses
     *                          zur�ck geliefert.
     *
     * @return  Das erzeugte vollst�ndige Konzept oder den Wert
     *          <CODE>null</CODE>, wenn keines erzeugt werden konnte.
     */
    public KombiKonzept erzeugtesKonzept(int scpIterAnz,
                                         Konzept bekanntesKonzept) {
        return erzeugtesKonzept(scpIterAnz, 0, bekanntesKonzept);
    }

    /**
     * Liefert ein vollst�ndiges Konzept, das aus der Menge der allgemeinen
     * Konzepte erzeugt wurde. Dies geschieht mit Bezug auf den aktuellen
     * Inhalt der Menge der vollst�ndigen Konzepte, wobei aber einige der
     * bisher nicht ausgeschlossenen Beispiele ausgeschlossen behandelt
     * werden. Wenn die Menge der allgemeinen Konzepte noch nicht erzeugt
     * wurden, wird keines erzeugt.
     *
     * @param scpIterAnz    Die Anzahl der Iterationen im SCP-Verfahren.
     * @param aenderAnteil  Der Anteil der nicht ausgeschlossenen Beispiele,
     *                      die als ausgeschlossen behandelt werden sollen.
     *
     * @return  Das erzeugte vollst�ndige Konzept oder den Wert
     *          <CODE>null</CODE>, wenn keines erzeugt werden konnte.
     */
    public KombiKonzept erzeugtesKonzept(int scpIterAnz, float aenderAnteil) {
        return erzeugtesKonzept(scpIterAnz, aenderAnteil, null);
    }

    /**
     * Liefert ein vollst�ndigen Konzept, das aus der Menge der allgemeinen
     * Konzepte erzeugt wurde. Dies geschieht mit Bezug auf den aktuellen
     * Inhalt der Menge der vollst�ndigen Konzepte. Wenn die Menge der
     * allgemeinen Konzepte noch nicht erzeugt wurden, wird keines erzeugt.
     *
     * @param scpIterAnz  Die Anzahl der Iterationen im SCP-Verfahren.
     *
     * @return  Das erzeugte vollst�ndige Konzept oder den Wert
     *          <CODE>null</CODE>, wenn keines erzeugt werden konnte.
     */
    public KombiKonzept erzeugtesKonzept(int scpIterAnz) {
        return erzeugtesKonzept(scpIterAnz, 0, null);
    }

    /**
     * Liefert eine Menge von vollst�ndigen Konzepten, die aus der Menge der
     * allgemeinen Konzepte erzeugt wurden. Dies geschieht mit Bezug auf den
     * aktuellen Inhalt der Menge der vollst�ndigen Konzepte. Wenn die Menge
     * der allgemeinen Konzepte noch nicht erzeugt wurden, wird eine leere
     * Menge geliefert.
     *
     * @param scpIterAnz  Die Anzahl der Iterationen im SCP-Verfahren.
     *
     * @return  Die Menge der erzeugten vollst�ndigen Konzepte vom Typ
     *          <CODE>KombiKonzept</CODE>.
     */
    public HashSet erzeugteKonzepte(int scpIterAnz) {
        float[] negAusschlussGuete;

        if (algVollErzKonzepte != null) {
            // Die Menge der allgemeinen Konzepte wurde schon erzeugt.
            negAusschlussGuete = vollstaendigeKonzepte.negAusschlussGuete();
            return algVollErzKonzepte.vollstaendigeKonzepte(negAusschlussGuete,
                                                            scpIterAnz, false,
                                                            null);
        } else {
            return (new HashSet());
        }
    }

    /**
     * Gibt an, ob mit Sicherheit alle hinzugef�gten vollst�ndigen Konzepte in
     * der Auswahl enthalten sind.
     *
     * @return  Die Angabe, ob mit Sicherheit alle hinzugef�gten vollst�ndigen
     *          Konzepte in der Auswahl enthalten sind.
     */
    public boolean alleSpezKonzepteEnthalten() {
        return vollstaendigeKonzepte.alleKonzepteEnthalten();
    }

    /**
     * Liefert die Anzahl der enthaltenen vollst�ndigen Konzepte.
     *
     * @return  Die Anzahl der enthaltenen vollst�ndigen Konzepte.
     */
    public int enthalteneSpezKonzeptAnz() {
        return vollstaendigeKonzepte.enthalteneKonzeptAnz();
    }

    /**
     * Liefert die Menge der enthaltenen vollst�ndigen Konzepte.
     *
     * @return  Die Menge der enthaltenen vollst�ndigen Konzepte.
     */
    public HashSet enthalteneSpezKonzepte() {
        return vollstaendigeKonzepte.enthalteneKonzepte();
    }

    /**
     * Gibt an, ob alle allgemeinen Konzepte, die seit dem letzten Aufruf von
     * <CODE>erzAlgKonzeptMenge</CODE> hinzugef�gt wurden, mit Sicherheit noch
     * in der Auswahl enthalten sind.
     *
     * @return  Die Angabe, ob mit Sicherheit alle zuletzt hinzugef�gten
     *          allgemeinen Konzepte in der Auswahl enthalten sind.
     */
    public boolean alleAlgKonzepteEnthalten() {
        return (algKonzEnthaltMoegl
                && algVollErzKonzepte.alleKonzepteEnthalten());
    }

    /**
     * Liefert die Anzahl der enthaltenen allgemeinen Konzepte.
     *
     * @return  Die Anzahl der enthaltenen allgemeinen Konzepte.
     */
    public int enthalteneAlgKonzeptAnz() {
        if (algVollErzKonzepte == null) {
            return 0;
        } else {
            return algVollErzKonzepte.enthalteneKonzeptAnz();
        }
    }

    /**
     * Liefert die Menge der enthaltenen allgemeinen Konzepte.
     *
     * @return  Die Menge der enthaltenen allgemeinen Konzepte.
     */
    public HashSet enthalteneAlgKonzepte() {
        if (algVollErzKonzepte == null) {
            return (new HashSet(1));
        } else {
            return algVollErzKonzepte.enthalteneKonzepte();
        }
    }

    /**
     * Setzt den Faktor, mit dem die Summe der Kosten der ausgeschlossenen
     * Indices multipliziert wird, um die Kosten f�r einen nicht
     * ausgeschlossenen Index zu errechnen, auf einen neuen Wert. Die
     * enthaltenen allgemeinen Konzepte werden neu bewertet.
     *
     * @param neuerKostenFaktor  Der neue Wert f�r den Kostenfaktor.
     */
    public void setzeKostenFaktor(float neuerKostenFaktor) {
        kostenFaktor = neuerKostenFaktor;
        if (algVollErzKonzepte != null) {
            algKonzEnthaltMoegl &= algVollErzKonzepte.alleKonzepteEnthalten();
            algVollErzKonzepte = neueAlgVollKonzepte(true);
        }
    }

    /**
     * Liefert die beste vollst�ndige Formel. Das ist die, die prim�r die
     * geringste Anzahl negativer Fehler und sekund�r eine m�glichst geringe
     * Komplexit�t hat.
     *
     * @param scpIterAnz      Die Anzahl der Iterationen im SCP-Verfahren.
     * @param bekannteFormel  Eine bekannte vollst�ndige Formel oder der Wert
     *                        <CODE>null</CODE>. Wenn keine bessere Formel
     *                        erzeugt werden kann, wird diese Formel zur�ck
     *                        geliefert.
     *
     * @return  Die beste vollst�ndige Formel.
     */
    public KombiKonzept besteFormel(int scpIterAnz, Konzept bekannteFormel) {
        return vollstaendigeKonzepte.besteFormel(scpIterAnz, bekannteFormel);
    }

    /**
     * Liefert die beste vollst�ndige Formel. Das ist die, die prim�r die
     * geringste Anzahl negativer Fehler und sekund�r eine m�glichst geringe
     * Komplexit�t hat.
     *
     * @param scpIterAnz  Die Anzahl der Iterationen im SCP-Verfahren.
     *
     * @return  Die beste vollst�ndige Formel.
     */
    public KombiKonzept besteFormel(int scpIterAnz) {
        return besteFormel(scpIterAnz, null);
    }
}

