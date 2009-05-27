/*
 * Dateiname      : KorrKonzeptVerwaltung.java
 * Letzte Änderung: 21. November 2007
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
import lascer.konzepte.mengen.KorrekteKonzepte;
import lascer.konzepte.mengen.AlgKorrErzKonzepte;
import lascer.praedikate.Praedikat;
import lascer.problemdaten.Beispieldaten;

/**
 * Verwaltet eine Menge von korrekten Konzepte und steuert deren Erzeugung.
 * Die Menge stellt eine Auswahl der zur Aufnahme übergebenen Konzepte dar.
 *
 * @author  Dietmar Lippold
 */
public class KorrKonzeptVerwaltung implements KonzeptVerwaltung, Serializable {

    /**
     * Der zu verwendende Zufallsgenerator.
     */
    private final Random random;

    /**
     * Die der Konzeptbildung zugrunde liegenden Beispieldaten.
     */
    private final Beispieldaten beispieldaten;

    /**
     * Die Menge der aufgenommenden korrekten Konzepte.
     */
    private transient KorrekteKonzepte korrekteKonzepte;

    /**
     * Die Menge der aufgenommenden allgemeinen Konzepte zur Erzeugung
     * korrekter Konzepte.
     */
    private transient AlgKorrErzKonzepte algKorrErzKonzepte = null;

    /**
     * Faktor, mit dem die Summe der Kosten der überdeckten Indices
     * multipliziert wird, um die Kosten für einen nicht überdeckten Index zu
     * errechnen.
     */
    private float kostenFaktor;

    /**
     * Die geschätzte maximale Gesamt-Komplexität einer Formel. Dieser Wert
     * ist nur bei Vorgabe der Anzahl an Literalen, die maximal in einer
     * Disjunktion bzw. Konjunktion enthalten sein sollen, (Attribut
     * <CODE>maxLiteralAnz</CODE>) von Bedeutung.
     */
    private float gesamtKomplex;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * allgemeinen Konzeptmengen. Ein negativer Wert steht für eine
     * unbeschränkte Anzahl.
     */
    private int algItmAnz;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * speziellen, d.h. korrekten oder vollständigen, Konzeptmengen. Ein
     * negativer Wert steht für eine unbeschränkte Anzahl.
     */
    private int spezItmAnz;

    /**
     * Die Anzahl der Literale, die maximal in einer Disjunktion bzw. in einer
     * Konjunktion enthalten sein sollen. Der Wert Null steht für eine
     * unbegrenzte Anzahl.
     */
    private int maxLiteralAnz;

    /**
     * Gibt an, in welchem Ausmaß die Teilmengen besonders Speicher-effizient
     * aber dadurch weniger Laufzeit-effizient verwaltet werden sollen. Der
     * Wert ist gleich oder größer Null (maximale Laufzeit-Effizienz) und
     * kleiner oder gleich Zwei (maximale Speicher-Effizienz).
     */
    private int speicherEffizienz;

    /**
     * Gibt an, ob boolsche Attribute negiert werden sollen, d.h. invertierte
     * Literale dazu erzeugt werden sollen.
     */
    private boolean negBoolPraedErz;

    /**
     * Gibt an, ob bei der letzten Erzeugung der Menge für die allgemeinen
     * Konzepte, d.h. beim letzten Aufruf der Methode
     * <CODE>erzAlgKonzeptMenge</CODE>, die beste erzeugbare Formel Fehler
     * enthielt.
     */
    private boolean zuletztFehlerVorhanden = true;

    /**
     * Gibt an, ob es möglich ist, daß alle allgemeinen Konzepte, die seit dem
     * letzten Aufruf von <CODE>erzAlgKonzeptMenge</CODE> hinzugefügt wurden,
     * noch in der Auswahl enthalten sind.
     */
    private boolean algKonzEnthaltMoegl = false;

    /**
     * Liefert eine neue Instanz, die bis auf den enthaltenen Zufallsgenerator
     * identisch ist zur übergebenen Instanz, d.h. zusätzlich zum übergebenen
     * Zufallsgenerator die identischen Attribut-Werte wie die übergebene
     * Instanz besitzt.
     *
     * @param konzeptVerwaltung  Die Instanz, deren Attribute-Werte übernommen
     *                           werden sollen.
     * @param random             Der Zufallsgenerator für die zu erzeugende
     *                           Instanz.
     */
    public KorrKonzeptVerwaltung(KorrKonzeptVerwaltung konzeptVerwaltung,
                                 Random random) {

        this.random = random;
        this.beispieldaten = konzeptVerwaltung.beispieldaten;
        this.korrekteKonzepte = konzeptVerwaltung.korrekteKonzepte;
        this.algKorrErzKonzepte = konzeptVerwaltung.algKorrErzKonzepte;
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
     *                           überdeckten Indices multipliziert wird, um
     *                           die Kosten für einen nicht überdeckten Index
     *                           zu errechnen.
     * @param gesamtKomplex      Die geschätzte maximale Gesamt-Komplexität
     *                           einer Formel.
     * @param algItmAnz          Die minimale Anzahl der zu speichernden
     *                           Teilmengen zur Auswahl der allgemeinen
     *                           Konzeptmengen. Ein negativer Wert steht für
     *                           eine unbeschränkte Anzahl.
     * @param spezItmAnz         Die minimale Anzahl der zu speichernden
     *                           Teilmengen zur Auswahl der speziellen
     *                           Konzeptmengen. Ein negativer Wert steht für
     *                           eine unbeschränkte Anzahl.
     * @param maxLiteralAnz      Die Anzahl der Literale, die maximal in einer
     *                           Disjunktion bzw. Konjunktion enthalten sein
     *                           sollen. Der Wert Null steht für eine
     *                           unbegrenzte Anzahl.
     * @param speicherEffizienz  Gibt an, in welchem Ausmaß die Teilmengen
     *                           besonders Speicher-effizient aber dadurch
     *                           weniger Laufzeit-effizient verwaltet werden
     *                           sollen. Der Wert muß zwischen Null und Zwei
     *                           liegen.
     * @param negBoolPraedErz    Gibt an, ob boolsche Attribute negiert werden
     *                           sollen, d.h. invertierte Literale dazu
     *                           erzeugt werden sollen.
     */
    public KorrKonzeptVerwaltung(Random random, Beispieldaten beispieldaten,
                                 float kostenFaktor, float gesamtKomplex,
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
        this.korrekteKonzepte = new KorrekteKonzepte(random, beispieldaten,
                                                     gesamtKomplex, maxLiteralAnz,
                                                     spezItmAnz, speicherEffizienz);
    }

    /**
     * Erzeugt eine neue Instanz. Der Zufallsgenerator wird mit einem festen
     * Wert initialisiert, die Anzahl der Literale in einer Disjunktion oder
     * Konjunktion ist nicht beschränkt und es erfolgt eine besonders
     * Laufzeit-Effiziente Verwaltung der Teilmengen.
     *
     * @param beispieldaten  Die der Konzeptbildung zugrunde liegenden
     *                       Beispieldaten.
     * @param algItmAnz      Die minimale Anzahl der zu speichernden
     *                       Teilmengen zur Auswahl der allgemeinen
     *                       Konzeptmengen. Ein negativer Wert steht für eine
     *                       unbeschränkte Anzahl.
     * @param spezItmAnz     Die minimale Anzahl der zu speichernden
     *                       Teilmengen zur Auswahl der speziellen
     *                       Konzeptmengen. Ein negativer Wert steht für eine
     *                       unbeschränkte Anzahl.
     */
    public KorrKonzeptVerwaltung(Beispieldaten beispieldaten,
                                 int algItmAnz, int spezItmAnz) {

        this(new Random(Konstanten.SEED), beispieldaten,
             Konstanten.KOSTEN_FAKTOR_INIT, 0, algItmAnz, spezItmAnz, 0, 0,
             lascer.Konstanten.NEG_BOOL_PRAED_ERZ);
    }

    /**
     * Liest dieses Objekt aus dem übergebenen Stream.
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

        HashSet korrKonzepte, algKonzepte;
        float[] ueberdeckGuete;

        // Lesen der serialisierbaren Attribute.
        stream.defaultReadObject();

        // Rekonstruktion vom Wert des Attributs korrekteKonzepte.
        korrKonzepte = (HashSet) stream.readObject();
        korrekteKonzepte = new KorrekteKonzepte(random, beispieldaten,
                                                gesamtKomplex, maxLiteralAnz,
                                                spezItmAnz, speicherEffizienz);
        korrekteKonzepte.aufnehmen(korrKonzepte);

        // Rekonstruktion vom Wert des Attributs algKorrErzKonzepte.
        algKonzepte = (HashSet) stream.readObject();
        if (algKonzepte == null) {
            algKorrErzKonzepte = null;
        } else {
            ueberdeckGuete = korrekteKonzepte.posUeberdeckGuete();
            algKorrErzKonzepte = new AlgKorrErzKonzepte(random, beispieldaten,
                                                        ueberdeckGuete, kostenFaktor,
                                                        algItmAnz, maxLiteralAnz,
                                                        speicherEffizienz);
            algKorrErzKonzepte.aufnehmen(algKonzepte);
        }
    }

    /**
     * Schreibt dieses Objekt in den übergebenen Stream.
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
        stream.writeObject(korrekteKonzepte.enthalteneKonzepte());
        if (algKorrErzKonzepte == null) {
            stream.writeObject(null);
        } else {
            stream.writeObject(algKorrErzKonzepte.enthalteneKonzepte());
        }
    }

    /**
     * Liefert eine neue Instanz, die bis auf den enthaltenen Zufallsgenerator
     * identisch ist mit dieser Instanz, d.h. bis auf den Zufallsgenerator die
     * identischen Attribut-Werte besitzt.
     *
     * @param rand  Der Zufallsgenerator für die zu erzeugende Instanz.
     *
     * @return  Eine zu dieser Instanz identische Instanz, die aber den
     *          übergebenen Zufallsgenerator besitzt.
     */
    public KonzeptVerwaltung flacheKopie(Random rand) {
        return (new KorrKonzeptVerwaltung(this, rand));
    }

    /**
     * Liefert den Faktor, mit dem die Summe der Kosten der überdeckten bzw.
     * ausgeschlossenen Indices multipliziert wird, um die Kosten für einen
     * nicht überdeckten bzw. ausgeschlossenen Index zu errechnen.
     *
     * @return  Den enthaltenen Kostenfaktor.
     */
    public float gibKostenFaktor() {
        return kostenFaktor;
    }

    /**
     * Nimmt das übergebene Konzept in die passende Konzeptmenge auf. Wenn es
     * sich um ein allgemeines Konzept handelt und die Menge für die
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
            if (konzept.istKorrekt()) {
                aufgenommen = korrekteKonzepte.aufnehmen(konzept);
            } else {
                if (algKorrErzKonzepte != null) {
                    aufgenommen = algKorrErzKonzepte.aufnehmen(konzept);
                }
            }
        }
        return aufgenommen;
    }

    /**
     * Nimmt die übergebenen Konzepte in die passenden Konzeptmengen auf. Ein
     * allgemeines Konzept wird dabei nur aufgenommen, wenn die Menge für die
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
     * Nimmt das übergebene Prädikat in die passende Konzeptmenge auf. Wenn es
     * ein allgemeines Konzept darstellt und die Menge für die allgemeinen
     * Konzepte wurde noch nicht erzeugt, wird das Prädikat nicht aufgenommen.
     *
     * @param praedikat   Das aufzunehmende Prädikat.
     * @param invertiert  Gibt an, ob das Prädikat bei der Aufnahme invertiert
     *                    werden soll.
     *
     * @return  <CODE>true</CODE>, wenn das Prädikat in die Auswahl
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
     * Nimmt das übergebene Konzept in die passende Konzeptmenge auf, wenn es
     * das positive Beispiel mit dem angegebenen Index enthält und das
     * negative Beispiel mit dem angegebenen Index nicht enthält. Wenn es sich
     * um ein allgemeines Konzept handelt und die Menge für die allgemeinen
     * Konzepte wurde noch nicht erzeugt, wird das Konzept nicht aufgenommen.
     *
     * @param konzept   Das aufzunehmende Konzept.
     * @param posIndex  Der Index eines positiven Beispiels, das das Konzept
     *                  für die Aufnahme enthalten muß. Ein negativer Wert
     *                  wird ignoriert.
     * @param negIndex  Der Index eines negativen Beispiels, das das Konzept
     *                  für die Aufnahme nicht enthalten darf. Ein negativer
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
     * Nimmt das übergebene Prädikat in die passende Konzeptmenge auf, wenn es
     * das positive Beispiel mit dem angegebenen Index enthält und das
     * negative Beispiel mit dem angegebenen Index nicht enthält. Wenn es ein
     * allgemeines Konzept darstellt und die Menge für die allgemeinen
     * Konzepte wurde noch nicht erzeugt, wird das Prädikat nicht aufgenommen.
     *
     * @param praedikat   Das aufzunehmende Prädikat.
     * @param invertiert  Gibt an, ob das Prädikat bei der Aufnahme invertiert
     *                    werden soll.
     * @param posIndex    Der Index eines positiven Beispiels, das das
     *                    Prädikat für die Aufnahme enthalten muß. Ein
     *                    negativer Wert wird ignoriert.
     * @param negIndex    Der Index eines negativen Beispiels, das das
     *                    Prädikat für die Aufnahme nicht enthalten darf. Ein
     *                    negativer Wert wird ignoriert.
     *
     * @return  <CODE>true</CODE>, wenn das Prädikat in die Auswahl
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
     * Nimmt das übergebene Konzept unter bestimmten Bedingungen in die
     * Menge der korrekten Konzepte auf. Wenn es ein allgemeines Konzept
     * darstellt, wird das Konzept nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, daß das Konzept ein korrektes Konzept
     * einer Formel ersetzen kann. Die Fehler der um das korrekte Konzept
     * reduzierten Formel werden dazu übergeben.
     *
     * @param konzept           Das aufzunehmende Konzept.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele der Formel,
     *                          die durch die Entfernung des korrekten
     *                          Konzepts entstanden sind.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele der Formel,
     *                          die durch die Entfernung des korrekten
     *                          Konzepts entstanden sind.
     *
     * @return  <CODE>true</CODE>, wenn das Konzept in die Auswahl aufgenommen
     *          wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean konzeptAufnehmen(Konzept konzept,
                                    BitMathIntSet redKonzPosFehler,
                                    BitMathIntSet redKonzNegFehler) {

        if (konzept.istKorrekt()
                && konzept.posFalschBsp().isDisjoint(redKonzPosFehler)) {
            return konzeptAufnehmen(konzept);
        } else {
            return false;
        }
    }

    /**
     * Nimmt das übergebene Prädikat unter bestimmten Bedingungen in die
     * Menge der korrekten Konzepte auf. Wenn es ein allgemeines Konzept
     * darstellt, wird das Prädikat nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, daß das aus dem Prädikat erzeugte
     * Konzept ein korrektes Konzept einer Formel ersetzen kann. Die Fehler
     * der um das korrekte Konzept reduzierten Formel werden dazu übergeben.
     *
     * @param praedikat         Das aufzunehmende Prädikat.
     * @param invertiert        Gibt an, ob das Prädikat bei der Aufnahme
     *                          invertiert werden soll.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele der Formel,
     *                          die durch die Entfernung des korrekten
     *                          Konzepts entstanden sind.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele der Formel,
     *                          die durch die Entfernung des korrekten
     *                          Konzepts entstanden sind.
     *
     * @return  <CODE>true</CODE>, wenn das Prädikat in die Auswahl
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
     * Nimmt das übergebene Konzept unter bestimmten Bedingungen in die
     * passende Konzeptmenge auf. Wenn es ein allgemeines Konzept darstellt
     * und die Menge für die allgemeinen Konzepte wurde noch nicht erzeugt,
     * wird das Prädikat nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, daß das Konzept ein Teilkonzept eines
     * korrektes Konzepts ersetzen kann. Die Fehler des um das Teilkonzept
     * reduzierten korrekten Konzepts werden zusammen mit der Komplexität und
     * der Anzahl der Fehler des Teilkonzepts übergeben.
     *
     * @param konzept           Das aufzunehmende Konzept.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele des
     *                          korrekten Konzepts, aus dem das Teilkonzept
     *                          entfernt wurde.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele des
     *                          korrekten Konzepts, aus dem das Teilkonzept
     *                          entfernt wurde.
     * @param komplex           Die Komplexität des Teilkonzepts.
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
             || (konzept.posFalschAnz() < posFehlAnz))
            && (konzept.negFalschBsp().isDisjoint(redKonzNegFehler))) {

            return konzeptAufnehmen(konzept);
        } else {
            return false;
        }
    }

    /**
     * Nimmt das übergebene Prädikat unter bestimmten Bedingungen in die
     * passende Konzeptmenge auf. Wenn es ein allgemeines Konzept darstellt
     * und die Menge für die allgemeinen Konzepte wurde noch nicht erzeugt,
     * wird das Prädikat nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, daß das aus dem Prädikat erzeugte
     * Konzept ein Teilkonzept eines korrekten Konzepts ersetzen kann. Die
     * Fehler des um das Teilkonzept reduzierten korrekten Konzepts werden
     * zusammen mit der Komplexität und der Anzahl der Fehler des Teilkonzepts
     * übergeben.
     *
     * @param praedikat         Das aufzunehmende Prädikat.
     * @param invertiert        Gibt an, ob das Prädikat bei der Aufnahme
     *                          invertiert werden soll.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele des
     *                          korrekten Konzepts, aus dem das Teilkonzept
     *                          entfernt wurde.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele des
     *                          korrekten Konzepts, aus dem das Teilkonzept
     *                          entfernt wurde.
     * @param komplex           Die Komplexität des Teilkonzepts.
     * @param posFehlAnz        Die Anzahl der Fehler der positiven Beispiele
     *                          des Teilkonzepts.
     * @param negFehlAnz        Die Anzahl der Fehler der negativen Beispiele
     *                          des Teilkonzepts.
     *
     * @return  <CODE>true</CODE>, wenn das Prädikat in die Auswahl
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
     * Bezug auf den aktuellen Inhalt der Menge der korrekten Konzepte. Wenn
     * die Menge der allgemeiner Konzepte schon vorhanden ist, können deren
     * Konzepte in die neu erzeugte Menge übernommen werden.
     *
     * @param konzepteUebernehmen  Gibt an, ob die vorhandenen allgemeinen
     *                             Konzepte in die neue Menge übernommen
     *                             werden sollen.
     *
     * @return  Eine neue Menge der allgemeinen Konzepte.
     */
    private AlgKorrErzKonzepte neueAlgKorrKonzepte(boolean konzepteUebernehmen) {
        AlgKorrErzKonzepte neuAlgKorrKonz;
        float[]            ueberdeckGuete;

        ueberdeckGuete = korrekteKonzepte.posUeberdeckGuete();
        neuAlgKorrKonz = new AlgKorrErzKonzepte(random, beispieldaten,
                                                ueberdeckGuete, kostenFaktor,
                                                algItmAnz, maxLiteralAnz,
                                                speicherEffizienz);
        if ((algKorrErzKonzepte != null) && konzepteUebernehmen) {
            neuAlgKorrKonz.aufnehmen(algKorrErzKonzepte.enthalteneKonzepte());
        }
        return neuAlgKorrKonz;
    }

    /**
     * Erzeugt die Menge für die allgemeinen Konzepte. Dies geschieht mit
     * Bezug auf den aktuellen Inhalt der Menge der korrekten Konzepte. Wenn
     * die Menge der allgemeiner Konzepte schon vorhanden ist, können deren
     * Konzepte in die neu erzeugte Menge übernommen werden.
     *
     * @param konzepteUebernehmen  Gibt an, ob die vorhandenen allgemeinen
     *                             Konzepte in die neue Menge übernommen
     *                             werden sollen.
     */
    public void erzAlgKonzeptMenge(boolean konzepteUebernehmen) {

        algKonzEnthaltMoegl = true;
        zuletztFehlerVorhanden = !korrekteKonzepte.fehlerfreieFormelErzeugbar();
        algKorrErzKonzepte = neueAlgKorrKonzepte(konzepteUebernehmen);
    }

    /**
     * Gibt an, ob bei der letzten Erzeugung der Menge für die allgemeinen
     * Konzepte, d.h. beim letzten Aufruf der Methode
     * <CODE>erzAlgKonzeptMenge</CODE>, die beste erzeugbare Formel Fehler
     * enthielt. Wenn die Menge noch nicht erzeugt wurde, d.h. die Methode
     * noch nicht aufgerufen wurde, wird <CODE>true</CODE> geliefert.
     *
     * @return  <CODE>true</CODE>, wenn bei der letzten Erzeugung der Menge
     *          für die allgemeinen Konzepte die beste erzeugbare Formel
     *          Fehler enthielt, anderenfalls <CODE>false</CODE>.
     */
    public boolean zuletztFehlerVorhanden() {
        return zuletztFehlerVorhanden;
    }

    /**
     * Liefert eine Kopie des übergebenen Arrays, wobei aber einige der Werte,
     * die Null waren, auf Eins geändert wurden. Wenn das übergebene Array
     * Null-Werte enthält, bleibt mindestens einer unverändert.
     *
     * @param werte      Das Array der Werte, in dessen Kopie einige
     *                   Null-Werte auf Eins geändert werden sollen.
     * @param aenderWkt  Die Wahrscheinlichkeit, mit der ein Null-Werte
     +                   geändert werden soll.
     *
     * @return  Die geänderte Kopie vom übergebenen Array.
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
     * Liefert ein korrektes Konzept, das aus der Menge der allgemeinen
     * Konzepte erzeugt wurde. Dies geschieht mit Bezug auf den aktuellen
     * Inhalt der Menge der korrekten Konzepte, wobei aber einige der bisher
     * nicht überdeckten Beispiele als überdeckt behandelt werden. Wenn die
     * Menge der allgemeinen Konzepte noch nicht erzeugt wurden, wird keines
     * erzeugt.
     *
     * @param scpIterAnz        Die Anzahl der Iterationen im SCP-Verfahren.
     * @param aenderWkt         Die Wahrscheinlichkeit, mit der ein nicht
     *                          überdecktes Beispiel als überdeckt behandelt
     *                          werden sollen.
     * @param bekanntesKonzept  Ein bekanntes korrektes Konzept oder der Wert
     *                          <CODE>null</CODE>. Wenn kein besseres Konzept
     *                          erzeugt werden kann, wird dieses zurück
     *                          geliefert.
     *
     * @return  Das erzeugte korrekte Konzept oder den Wert <CODE>null</CODE>,
     *          wenn keines erzeugt werden konnte.
     */
    private KombiKonzept erzeugtesKonzept(int scpIterAnz, float aenderWkt,
                                          Konzept bekanntesKonzept) {
        HashSet konzeptMenge;
        float[] posUeberdeckGuete;

        if (algKorrErzKonzepte != null) {
            // Die Menge der allgemeinen Konzepte wurde schon erzeugt.
            posUeberdeckGuete = korrekteKonzepte.posUeberdeckGuete();
            if (aenderWkt > 0) {
                posUeberdeckGuete = zufaelligGeaendert(posUeberdeckGuete,
                                                       aenderWkt);
            }
            konzeptMenge = algKorrErzKonzepte.korrekteKonzepte(posUeberdeckGuete,
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
     * Liefert ein korrektes Konzept, das aus der Menge der allgemeinen
     * Konzepte erzeugt wurde. Dies geschieht mit Bezug auf den aktuellen
     * Inhalt der Menge der korrekten Konzepte. Wenn die Menge der
     * allgemeinen Konzepte noch nicht erzeugt wurden, wird keines erzeugt.
     *
     * @param scpIterAnz        Die Anzahl der Iterationen im SCP-Verfahren.
     * @param bekanntesKonzept  Ein bekanntes korrektes Konzept oder der Wert
     *                          <CODE>null</CODE>. Wenn kein besseres Konzept
     *                          erzeugt werden kann, wird dieses zurück
     *                          geliefert.
     *
     * @return  Das erzeugte korrekte Konzept oder den Wert <CODE>null</CODE>,
     *          wenn keines erzeugt werden konnte.
     */
    public KombiKonzept erzeugtesKonzept(int scpIterAnz,
                                         Konzept bekanntesKonzept) {
        return erzeugtesKonzept(scpIterAnz, 0, bekanntesKonzept);
    }

    /**
     * Liefert ein korrektes Konzept, das aus der Menge der allgemeinen
     * Konzepte erzeugt wurde. Dies geschieht mit Bezug auf den aktuellen
     * Inhalt der Menge der korrektes Konzepte, wobei aber einige der bisher
     * nicht überdeckten Beispiele als überdeckt behandelt werden. Wenn die
     * Menge der allgemeinen Konzepte noch nicht erzeugt wurden, wird keines
     * erzeugt.
     *
     * @param scpIterAnz    Die Anzahl der Iterationen im SCP-Verfahren.
     * @param aenderAnteil  Der Anteil der nicht überdeckten Beispiele, die
     *                      als überdeckt behandelt werden sollen.
     *
     * @return  Das erzeugte korrekte Konzept oder den Wert <CODE>null</CODE>,
     *          wenn keines erzeugt werden konnte.
     */
    public KombiKonzept erzeugtesKonzept(int scpIterAnz, float aenderAnteil) {
        return erzeugtesKonzept(scpIterAnz, aenderAnteil, null);
    }

    /**
     * Liefert ein korrektes Konzept, das aus der Menge der allgemeinen
     * Konzepte erzeugt wurde. Dies geschieht mit Bezug auf den aktuellen
     * Inhalt der Menge der korrekten Konzepte. Wenn die Menge der
     * allgemeinen Konzepte noch nicht erzeugt wurden, wird keines erzeugt.
     *
     * @param scpIterAnz  Die Anzahl der Iterationen im SCP-Verfahren.
     *
     * @return  Das erzeugte korrekte Konzept oder den Wert <CODE>null</CODE>,
     *          wenn keines erzeugt werden konnte.
     */
    public KombiKonzept erzeugtesKonzept(int scpIterAnz) {
        return erzeugtesKonzept(scpIterAnz, 0, null);
    }

    /**
     * Liefert eine Menge von korrekten Konzepten, die aus der Menge der
     * allgemeinen Konzepte erzeugt wurden. Dies geschieht mit Bezug auf den
     * aktuellen Inhalt der Menge der korrekten Konzepte. Wenn die Menge der
     * allgemeinen Konzepte noch nicht erzeugt wurden, wird eine leere
     * Menge geliefert.
     *
     * @param scpIterAnz  Die Anzahl der Iterationen im SCP-Verfahren.
     *
     * @return  Die Menge der erzeugten korrekten Konzepte vom Typ
     *          <CODE>KombiKonzept</CODE>.
     */
    public HashSet erzeugteKonzepte(int scpIterAnz) {
        float[] posUeberdeckGuete;

        if (algKorrErzKonzepte != null) {
            // Die Menge der allgemeinen Konzepte wurde schon erzeugt.
            posUeberdeckGuete = korrekteKonzepte.posUeberdeckGuete();
            return algKorrErzKonzepte.korrekteKonzepte(posUeberdeckGuete,
                                                       scpIterAnz, false, null);
        } else {
            return (new HashSet());
        }
    }

    /**
     * Gibt an, ob mit Sicherheit alle hinzugefügten korrekten Konzepte in der
     * Auswahl enthalten sind.
     *
     * @return  Die Angabe, ob mit Sicherheit alle hinzugefügten korrekten
     *          Konzepte in der Auswahl enthalten sind.
     */
    public boolean alleSpezKonzepteEnthalten() {
        return korrekteKonzepte.alleKonzepteEnthalten();
    }

    /**
     * Liefert die Anzahl der enthaltenen korrekten Konzepte.
     *
     * @return  Die Anzahl der enthaltenen korrekten Konzepte.
     */
    public int enthalteneSpezKonzeptAnz() {
        return korrekteKonzepte.enthalteneKonzeptAnz();
    }

    /**
     * Liefert die Menge der enthaltenen korrekten Konzepte.
     *
     * @return  Die Menge der enthaltenen korrekten Konzepte.
     */
    public HashSet enthalteneSpezKonzepte() {
        return korrekteKonzepte.enthalteneKonzepte();
    }

    /**
     * Gibt an, ob alle allgemeinen Konzepte, die seit dem letzten Aufruf von
     * <CODE>erzAlgKonzeptMenge</CODE> hinzugefügt wurden, mit Sicherheit noch
     * in der Auswahl enthalten sind.
     *
     * @return  Die Angabe, ob mit Sicherheit alle zuletzt hinzugefügten
     *          allgemeinen Konzepte in der Auswahl enthalten sind.
     */
    public boolean alleAlgKonzepteEnthalten() {
        return (algKonzEnthaltMoegl
                && algKorrErzKonzepte.alleKonzepteEnthalten());
    }

    /**
     * Liefert die Anzahl der enthaltenen allgemeinen Konzepte.
     *
     * @return  Die Anzahl der enthaltenen allgemeinen Konzepte.
     */
    public int enthalteneAlgKonzeptAnz() {
        if (algKorrErzKonzepte == null) {
            return 0;
        } else {
            return algKorrErzKonzepte.enthalteneKonzeptAnz();
        }
    }

    /**
     * Liefert die Menge der enthaltenen allgemeinen Konzepte.
     *
     * @return  Die Menge der enthaltenen allgemeinen Konzepte.
     */
    public HashSet enthalteneAlgKonzepte() {
        if (algKorrErzKonzepte == null) {
            return (new HashSet(1));
        } else {
            return algKorrErzKonzepte.enthalteneKonzepte();
        }
    }

    /**
     * Setzt den Faktor, mit dem die Summe der Kosten der überdeckten Indices
     * multipliziert wird, um die Kosten für einen nicht überdeckten Index zu
     * errechnen, auf einen neuen Wert. Die enthaltenen allgemeinen Konzepte
     * werden neu bewertet.
     *
     * @param neuerKostenFaktor  Der neue Wert für den Kostenfaktor.
     */
    public void setzeKostenFaktor(float neuerKostenFaktor) {
        kostenFaktor = neuerKostenFaktor;
        if (algKorrErzKonzepte != null) {
            algKonzEnthaltMoegl &= algKorrErzKonzepte.alleKonzepteEnthalten();
            algKorrErzKonzepte = neueAlgKorrKonzepte(true);
        }
    }

    /**
     * Liefert die beste korrekte Formel. Das ist die, die primär die
     * geringste Anzahl positiver Fehler und sekundär eine möglichst geringe
     * Komplexität hat.
     *
     * @param scpIterAnz      Die Anzahl der Iterationen im SCP-Verfahren.
     * @param bekannteFormel  Eine bekannte korrekte Formel oder der Wert
     *                        <CODE>null</CODE>. Wenn keine bessere Formel
     *                        erzeugt werden kann, wird diese Formel zurück
     *                        geliefert.
     *
     * @return  Die beste korrekte Formel.
     */
    public KombiKonzept besteFormel(int scpIterAnz, Konzept bekannteFormel) {
        return korrekteKonzepte.besteFormel(scpIterAnz, bekannteFormel);
    }

    /**
     * Liefert die beste korrekte Formel. Das ist die, die primär die
     * geringste Anzahl positiver Fehler und sekundär eine möglichst geringe
     * Komplexität hat.
     *
     * @param scpIterAnz  Die Anzahl der Iterationen im SCP-Verfahren.
     *
     * @return  Die beste korrekte Formel.
     */
    public KombiKonzept besteFormel(int scpIterAnz) {
        return besteFormel(scpIterAnz, null);
    }
}

