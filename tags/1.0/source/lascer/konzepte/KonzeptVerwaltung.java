/*
 * Dateiname      : KonzeptVerwaltung.java
 * Letzte Änderung: 09. Juli 2006
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

import java.util.Random;
import java.util.HashSet;
import java.util.Collection;
import mathCollection.BitMathIntSet;

import lascer.praedikate.Praedikat;

/**
 * Definiert die Verwaltung einer Menge von Konzepten und steuert deren
 * Erzeugung. Die Menge stellt eine Auswahl der zur Aufnahme übergebenen
 * Konzepte dar.
 *
 * @author  Dietmar Lippold
 */
public interface KonzeptVerwaltung {

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
    public KonzeptVerwaltung flacheKopie(Random rand);

    /**
     * Liefert den Faktor, mit dem die Summe der Kosten der überdeckten bzw.
     * ausgeschlossenen Indices multipliziert wird, um die Kosten für einen
     * nicht überdeckten bzw. ausgeschlossenen Index zu errechnen.
     *
     * @return  Den enthaltenen Kostenfaktor.
     */
    public float gibKostenFaktor();

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
    public boolean konzeptAufnehmen(Konzept konzept);

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
    public boolean konzepteAufnehmen(Collection konzepte);

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
    public boolean praedikatAufnehmen(Praedikat praedikat, boolean invertiert);

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
                                    int posIndex, int negIndex);

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
                                      int posIndex, int negIndex);

    /**
     * Nimmt das übergebene Konzept unter bestimmten Bedingungen in die
     * Menge der speziellen Konzepte auf. Wenn es ein allgemeines Konzept
     * darstellt, wird das Konzept nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, daß das Konzept ein spezielles Konzept
     * einer Formel ersetzen kann. Die Fehler der um das spezielle Konzept
     * reduzierten Formel werden dazu übergeben.
     *
     * @param konzept           Das aufzunehmende Konzept.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele der Formel,
     *                          die durch die Entfernung des speziellen
     *                          Konzepts entstanden sind.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele der Formel,
     *                          die durch die Entfernung des speziellen
     *                          Konzepts entstanden sind.
     *
     * @return  <CODE>true</CODE>, wenn das Konzept in die Auswahl aufgenommen
     *          wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean konzeptAufnehmen(Konzept konzept,
                                    BitMathIntSet redKonzPosFehler,
                                    BitMathIntSet redKonzNegFehler);

    /**
     * Nimmt das übergebene Prädikat unter bestimmten Bedingungen in die
     * Menge der speziellen Konzepte auf. Wenn es ein allgemeines Konzept
     * darstellt, wird das Prädikat nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, daß das aus dem Prädikat erzeugte
     * Konzept ein spezielles Konzept einer Formel ersetzen kann. Die Fehler
     * der um das spezielle Konzept reduzierten Formel werden dazu übergeben.
     *
     * @param praedikat         Das aufzunehmende Prädikat.
     * @param invertiert        Gibt an, ob das Prädikat bei der Aufnahme
     *                          invertiert werden soll.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele der Formel,
     *                          die durch die Entfernung des speziellen
     *                          Konzepts entstanden sind.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele der Formel,
     *                          die durch die Entfernung des speziellen
     *                          Konzepts entstanden sind.
     *
     * @return  <CODE>true</CODE>, wenn das Prädikat in die Auswahl
     *          aufgenommen wurde, anderenfalls <CODE>false</CODE>.
     */
    public boolean praedikatAufnehmen(Praedikat praedikat, boolean invertiert,
                                      BitMathIntSet redKonzPosFehler,
                                      BitMathIntSet redKonzNegFehler);

    /**
     * Nimmt das übergebene Konzept unter bestimmten Bedingungen in die
     * passende Konzeptmenge auf. Wenn es ein allgemeines Konzept darstellt
     * und die Menge für die allgemeinen Konzepte wurde noch nicht erzeugt,
     * wird das Prädikat nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, daß das Konzept ein Teilkonzept eines
     * speziellen Konzepts ersetzen kann. Die Fehler des um das Teilkonzept
     * reduzierten speziellen Konzepts werden zusammen mit der Komplexität und
     * der Anzahl der Fehler des Teilkonzepts übergeben.
     *
     * @param konzept           Das aufzunehmende Konzept.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele des
     *                          speziellen Konzepts, aus dem das Teilkonzept
     *                          entfernt wurde.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele des
     *                          speziellen Konzepts, aus dem das Teilkonzept
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
                                    float komplex, int posFehlAnz, int negFehlAnz);

    /**
     * Nimmt das übergebene Prädikat unter bestimmten Bedingungen in die
     * passende Konzeptmenge auf. Wenn es ein allgemeines Konzept darstellt
     * und die Menge für die allgemeinen Konzepte wurde noch nicht erzeugt,
     * wird das Prädikat nicht aufgenommen.<P>
     *
     * Die Bedingungen stellen sicher, daß das aus dem Prädikat erzeugte
     * Konzept ein Teilkonzept eines speziellen Konzepts ersetzen kann. Die
     * Fehler des um das Teilkonzept reduzierten speziellen Konzepts werden
     * zusammen mit der Komplexität und der Anzahl der Fehler des Teilkonzepts
     * übergeben.
     *
     * @param praedikat         Das aufzunehmende Prädikat.
     * @param invertiert        Gibt an, ob das Prädikat bei der Aufnahme
     *                          invertiert werden soll.
     * @param redKonzPosFehler  Die Fehler der positiven Beispiele des
     *                          speziellen Konzepts, aus dem das Teilkonzept
     *                          entfernt wurde.
     * @param redKonzNegFehler  Die Fehler der negativen Beispiele des
     *                          speziellen Konzepts, aus dem das Teilkonzept
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
                                      float komplex, int posFehlAnz, int negFehlAnz);

    /**
     * Erzeugt die Menge für die allgemeinen Konzepte. Dies geschieht mit
     * Bezug auf den aktuellen Inhalt der Mengen der korrekten oder
     * vollständigen Konzepte. Wenn schon Mengen allgemeiner Konzepte
     * vorhanden sind, können deren Konzepte in die neu erzeugten Mengen
     * übernommen werden.
     *
     * @param konzepteUebernehmen  Gibt an, ob die vorhandenen allgemeinen
     *                             Konzepte in die neue Menge übernommen
     *                             werden sollen.
     */
    public void erzAlgKonzeptMenge(boolean konzepteUebernehmen);

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
    public boolean zuletztFehlerVorhanden();

    /**
     * Liefert ein spezielles Konzept, das aus der Menge der allgemeinen
     * Konzepte erzeugt wurde. Dies geschieht mit Bezug auf den aktuellen
     * Inhalt der Menge der speziellen Konzepte. Wenn die Menge der
     * allgemeinen Konzepte noch nicht erzeugt wurden, wird keines erzeugt.
     *
     * @param scpIterAnz        Die Anzahl der Iterationen im SCP-Verfahren.
     * @param bekanntesKonzept  Ein bekanntes spezielles Konzept oder der Wert
     *                          <CODE>null</CODE>. Wenn kein besseres Konzept
     *                          erzeugt werden kann, wird dieses zurück
     *                          geliefert.
     *
     * @return  Das erzeugte spezielle Konzept oder den Wert <CODE>null</CODE>,
     *          wenn keines erzeugt werden konnte.
     */
    public KombiKonzept erzeugtesKonzept(int scpIterAnz,
                                         Konzept bekanntesKonzept);

    /**
     * Liefert ein spezielles Konzept, das aus der Menge der allgemeinen
     * Konzepte erzeugt wurde. Dies geschieht mit Bezug auf den aktuellen
     * Inhalt der Menge der speziellen Konzepte, wobei aber einige der bisher
     * nicht überdeckten bzw. ausgeschlossenen Beispiele als überdeckt bzw.
     * ausgeschlossen behandelt werden. Wenn die Menge der allgemeinen
     * Konzepte noch nicht erzeugt wurden, wird keines erzeugt.
     *
     * @param scpIterAnz  Die Anzahl der Iterationen im SCP-Verfahren.
     * @param aenderWkt   Die Wahrscheinlichkeit, mit der ein nicht
     *                    überdecktes bzw. nicht ausgeschlossenes Beispiel als
     *                    überdeckt bzw. ausgeschlossen behandelt werden soll.
     *
     * @return  Das erzeugte spezielle Konzept oder den Wert
     *          <CODE>null</CODE>, wenn keines erzeugt werden konnte.
     */
    public KombiKonzept erzeugtesKonzept(int scpIterAnz, float aenderWkt);

    /**
     * Liefert ein spezielles Konzept, das aus der Menge der allgemeinen
     * Konzepte erzeugt wurde. Dies geschieht mit Bezug auf den aktuellen
     * Inhalt der Menge der speziellen Konzepte. Wenn die Menge der
     * allgemeinen Konzepte noch nicht erzeugt wurden, wird keines erzeugt.
     *
     * @param scpIterAnz  Die Anzahl der Iterationen im SCP-Verfahren.
     *
     * @return  Das erzeugte spezielle Konzept oder den Wert
     *          <CODE>null</CODE>, wenn keines erzeugt werden konnte.
     */
    public KombiKonzept erzeugtesKonzept(int scpIterAnz);

    /**
     * Liefert eine Menge von speziellen Konzepten, die aus der Menge der
     * allgemeinen Konzepte erzeugt wurden. Dies geschieht mit Bezug auf den
     * aktuellen Inhalt der Menge der speziellen Konzepte. Wenn die Menge der
     * allgemeinen Konzepte noch nicht erzeugt wurden, wird eine leere Menge
     * geliefert.
     *
     * @param scpIterAnz  Die Anzahl der Iterationen im SCP-Verfahren.
     *
     * @return  Die Menge der erzeugten speziellen Konzepte vom Typ
     *          <CODE>KombiKonzept</CODE>.
     */
    public HashSet erzeugteKonzepte(int scpIterAnz);

    /**
     * Gibt an, ob mit Sicherheit alle hinzugefügten speziellen Konzepte in
     * der Auswahl enthalten sind.
     *
     * @return  Die Angabe, ob mit Sicherheit alle hinzugefügten speziellen
     *          Konzepte in der Auswahl enthalten sind.
     */
    public boolean alleSpezKonzepteEnthalten();

    /**
     * Liefert die Anzahl der enthaltenen speziellen Konzepte.
     *
     * @return  Die Anzahl der enthaltenen speziellen Konzepte.
     */
    public int enthalteneSpezKonzeptAnz();

    /**
     * Liefert die Menge der enthaltenen speziellen Konzepte.
     *
     * @return  Die Menge der enthaltenen speziellen Konzepte.
     */
    public HashSet enthalteneSpezKonzepte();

    /**
     * Gibt an, ob alle allgemeinen Konzepte, die seit dem letzten Aufruf von
     * <CODE>erzAlgKonzeptMenge</CODE> hinzugefügt wurden, mit Sicherheit noch
     * in der Auswahl enthalten sind.
     *
     * @return  Die Angabe, ob mit Sicherheit alle zuletzt hinzugefügten
     *          allgemeinen Konzepte in der Auswahl enthalten sind.
     */
    public boolean alleAlgKonzepteEnthalten();

    /**
     * Liefert die Anzahl der enthaltenen allgemeinen Konzepte.
     *
     * @return  Die Anzahl der enthaltenen allgemeinen Konzepte.
     */
    public int enthalteneAlgKonzeptAnz();

    /**
     * Liefert die Menge der enthaltenen allgemeinen Konzepte.
     *
     * @return  Die Menge der enthaltenen allgemeinen Konzepte.
     */
    public HashSet enthalteneAlgKonzepte();

    /**
     * Setzt den Faktor, mit dem die Summe der Kosten der überdeckten bzw.
     * ausgeschlossenen Indices multipliziert wird, um die Kosten für einen
     * nicht überdeckten bzw. ausgeschlossenen Index zu errechnen, auf einen
     * neuen Wert. Die enthaltenen allgemeinen Konzepte werden neu bewertet.
     *
     * @param neuerKostenFaktor  Der neue Wert für den Kostenfaktor.
     */
    public void setzeKostenFaktor(float neuerKostenFaktor);

    /**
     * Liefert die beste Formel. Das ist die, die primär die geringste Anzahl
     * Fehler und sekundär eine möglichst geringe Komplexität hat.
     *
     * @param scpIterAnz      Die Anzahl der Iterationen im SCP-Verfahren.
     * @param bekannteFormel  Eine bekannte Formel oder der Wert
     *                        <CODE>null</CODE>. Wenn keine bessere Formel
     *                        erzeugt werden kann, wird diese Formel zurück
     *                        geliefert.
     *
     * @return  Die beste korrekte oder vollständige Formel.
     */
    public KombiKonzept besteFormel(int scpIterAnz, Konzept bekannteFormel);

    /**
     * Liefert die beste Formel. Das ist die, die primär die geringste Anzahl
     * Fehler und sekundär eine möglichst geringe Komplexität hat.
     *
     * @param scpIterAnz  Die Anzahl der Iterationen im SCP-Verfahren.
     *
     * @return  Die beste korrekte oder vollständige Formel.
     */
    public KombiKonzept besteFormel(int scpIterAnz);
}

