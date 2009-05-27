/*
 * Datei          : Hyperebene.java
 * Letzte Änderung: August 2002
 * Autoren        : Dietmar Lippold, Haiyi Peng, Jing Jing Wei, Yang Zhou
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2002
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


package geometrischeClusterung;

import java.util.*;

/**
 * Die Klasse repraesentiert eine Hyperebene im mehrdimensionalen Raum.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class Hyperebene extends Unterraum {

  /**
   * Der Normalen-Vektor der Hyperebene, d.h. ein Vektor der Laenge Eins,
   * der senkrecht auf die Hyperebene steht. Dieser gibt an auch, welche
   * Punkte des Gesamtraums oberhalb und welche unterhalb der Hyperebene
   * liegen. Dies gilt auch für eine Null-dimensionale Hyperebene.
   */
  private Vektor normalenVektor;

  /**
   * Liefert einen neu berechneten Normalenvektor.
   *
   * @return  Einen Normalenvektor
   */
  private Vektor berechneterNormalenVektor(Punkt referenzpunkt) {

    Punkt schnittPunkt, neuerRefPunkt;
    Vektor diffVektor, hilfsNormalenVektor;

    schnittPunkt = projektion(referenzpunkt);
    diffVektor = referenzpunkt.differenzVektor(schnittPunkt);
    hilfsNormalenVektor = diffVektor.multipliziertMitSkalar(1.0 / diffVektor.laenge());

    neuerRefPunkt = enthaltenerPunkt().nachVerschiebung(hilfsNormalenVektor);

    schnittPunkt = projektion(neuerRefPunkt);
    diffVektor = schnittPunkt.differenzVektor(neuerRefPunkt);
    return diffVektor.multipliziertMitSkalar(1.0 / diffVektor.laenge());
  }

  /**
   * Erzeugt eine neue Instanz aus einer Menge von Vektoren und einem
   * enthaltenen Punkt. Dabei muss die Anzahl der Vektoren genau
   * sein als die Dimension des Unterraums, d.h. genau der
   * Dimension der Hyperebene entsprechen. Ist die Anzahl kleiner, wird
   * eine <CODE>ToFewElementsException</CODE> ausgeloesst, ist sie
   * groesser, wird eine <CODE>ToManyElementsException</CODE> ausgeloesst.
   *
   * Der uebergebene Referenzpunkt legt die Orientierung des Normalenvektors
   * fest und zwar so, dass der Punkt eine negative Entferung von der
   * Hyperebene hat. Die Seite des Referenzpunktes wird als Unterseite der
   * Hyperebene betrachtet. Liegt der Referenzpunkt in der Hyperebene, wird
   * eine <CODE>SubspaceInclusionException</CODE> ausgeloesst.
   *
   * @exception ToFewElementsException   Die Anzahl der erzeugenden Vektoren
   *                                     ist kleiner als die Dimension des
   *                                     Unterraums .
   * @exception ToManyElementsException  Die Anzahl der erzeugenden Vektoren
   *                                     ist groesser als die Dimension des
   *                                     Unterraums .
   * @exception SubspaceInclusionException  Der uebergebene Referenzpunkt
   *                                        liegt in der Hyperebene.
   *
   * @param referenzpunkt  Ein Punkt, der unter der Hyperebene liegt
   * @param enthaltenerPunkt  Ein Punkt, der auf der Hyperebene liegt
   * @param erzeugendeVektoren  Die Vektoren, die die Hyperebene erzeugen
   */
  public Hyperebene(Punkt referenzpunkt,
                    Punkt enthaltenerPunkt, Vektor[] erzeugendeVektoren) {

    super(enthaltenerPunkt, erzeugendeVektoren);

    int unterDim = dimensionUnterraum();

    if(erzeugendeVektoren.length < unterDim)
      throw new ToFewElementsException("Die Anzahl der erzeugenden Vektoren ist zu klein.");

    if(erzeugendeVektoren.length > unterDim)
      throw new ToManyElementsException("Die Anzahl der erzeugenden Vektoren ist zu groß.");

    this.normalenVektor = berechneterNormalenVektor(referenzpunkt);

    if (enthaelt(referenzpunkt))
      throw new SubspaceInclusionException("Der uebergebene Referenzpunkt liegt in der Hyperebene");
  }

  /**
   * Erzeugt eine neue Instanz aus einer Menge von Punkten. Dabei muss die
   * Anzahl der Punkte genau der Dimension des Unterraums plus eins entsprechen, d.h.
   * genau um Eins groesser sein als die Dimension der Hyperebene. Ist die
   * Anzahl kleiner, wird eine <CODE>ToFewElementsException</CODE>
   * ausgeloesst, ist sie groesser, wird eine
   * <CODE>ToManyElementsException</CODE> ausgeloesst.
   *
   * Der uebergebene Referenzpunkt legt die Orientierung des Normalenvektors
   * fest und zwar so, dass der Punkt eine negative Entferung von der
   * Hyperebene hat. Die Seite des Referenzpunktes wird als Unterseite der
   * Hyperebene betrachtet. Liegt der Referenzpunkt in der Hyperebene, wird
   * eine <CODE>SubspaceInclusionException</CODE> ausgeloesst.
   *
   * @exception ToFewElementsException   Die Anzahl der erzeugenden Punkte
   *                                     ist kleiner als die Dimension des
   *                                     Unterraums plus eins.
   * @exception ToManyElementsException  Die Anzahl der erzeugenden Punkte
   *                                     ist groesser als die Dimension des
   *                                     Unterraums plus eins.
   * @exception SubspaceInclusionException  Der uebergebene Referenzpunkt
   *                                        liegt in der Hyperebene.
   * @param referenzpunkt  Ein Punkt,der unter auf der Hyperebene liegt
   * @param erzeugendePunkte  Die Punkte,die die Hyperebene erzeugen
   */
  public Hyperebene(Punkt referenzpunkt, ArrayList erzeugendePunkte) {

    super(erzeugendePunkte);

    int unterDim = dimensionUnterraum();

    if(erzeugendePunkte.size() < unterDim + 1)
      throw new ToFewElementsException("Die Anzahl der erzeugenden Vektoren ist zu klein");

    if(erzeugendePunkte.size() > unterDim + 1)
      throw new ToManyElementsException("Die Anzahl der erzeugenden Vektoren ist zu groß");

    this.normalenVektor = berechneterNormalenVektor(referenzpunkt);

    if (enthaelt(referenzpunkt)) {
      throw new SubspaceInclusionException("Der uebergebene Referenzpunkt liegt in der Hyperebene");
    }
  }

  /**
   * Liefert den Normalen-Vektor der Hyperebene, d.h. ein Vektor der Laenge
   * Eins, der senkrecht auf die Hyperebene steht.
   *
   * @return  Den Normalenvektor der Hyperebene
   */
  public Vektor normalenVektor() {
    return normalenVektor;
  }

  /**
   * Liefert den Abstand des uebergebenen Punktes von der Hyperebene, d.h.
   * die Laenge des kuerzesten Vektors zwischen dem uebergebenen Punkt und
   * einem Punkt der Hyperebene.
   *
   * @param punkt  Ein Punkt, von dem der Abstand ermittelt werden soll.
   * @return  Den Abstand des Punktes von der Hyperebene
   */
  public double abstand(Punkt punkt) {
    return Math.abs(entfernung(punkt));
  }

  /**
   * Liefert die Entfernung des uebergebenen Punktes von der Hyperebene.
   * Deren Betrag entspricht dem Abstand des Punktes von der Hyperebene.
   * Ihr Vorzeichen richtet sich jedoch danach, auf welcher Seite der
   * Hyperebene der Punkt liegt. Liegt er auf der gleichen Seite wie der
   * bei der Erzeugung angegebene Referenzpunkt, ist es negativ,
   * anderenfalls ist es positiv.Wenn der Betrag von der Entfernung kleiner
   * als die <CODE>ABWEICHUNG</CODE> ist,dann werde die Entfernung auf 0
   * gesetzt.
   *
   * @param punkt  Ein Punkt, von dem die Entfernung ermittelt werden soll.
   * @return  Die Entfernung des Punktes von der Hyperebene
   */
  public double entfernung(Punkt punkt) {

    Vektor diffVektor = enthaltenerPunkt().differenzVektor(punkt);
    return normalenVektor.skalarprodukt(diffVektor);
  }

  /**
   * Ermittelt, ob der uebergebene Punkt auf der Unterseite des Hyperebene
   * liegt, d.h. eine negative Entfernung von dieser hat.
   *
   * @param punkt  Ein uebergebener Punkt
   * @return  <CODE>true</CODE>, wenn der uebergebene Punkt auf der Unterseite
   *          des Hyperebene liegt, dh,eine negative Entfernung von der Hyperebene
   *          hat,anderenfalls <CODE>false</CODE>.
   */
  public boolean punktLiegtUnterhalb(Punkt punkt) {
    return (entfernung(punkt) < -1 * Genauigkeit.maxAbstandFehler());
  }

  /**
   * Ermittelt, ob der uebergebene Punkt auf der Oberseite des Hyperebene
   * liegt, d.h. eine positive Entfernung von dieser hat.
   *
   * @param punkt  Ein uebergebener Punkt
   * @return  <CODE>true</CODE>, wenn der uebergebene Punkt auf der Obenseite
   *          des Hyperebene liegt,dh,eine positiv Entfernung von der Hyperebene
   *          hat, anderenfalls <CODE>false</CODE>.
   */
  public boolean punktLiegtOberhalb(Punkt punkt) {
    return (entfernung(punkt) > Genauigkeit.maxAbstandFehler());
  }

  /**
   * Liefert eine String-Darstellung der Instanz.
   *
   * @return  Ein String als Dastellung der Instanz.
   */
  public String toString() {

    StringBuffer text = new StringBuffer();

    text.append("@Basis von der Hyperebene" + "\n");

    int n = dimensionUnterraum();
    for(int i = 0; i <n;  i++) {
      text.append("  " + basis()[i]);
    }
    text.append("\n");

    text.append("  "+"@enthaltenerPunkt von der Hyperebene" + "\n");
    text.append("  " + enthaltenerPunkt());
    text.append("\n");

    text.append("  "+"@normalenVektor von der Hyperebene" + "\n");
    text.append("  "+ normalenVektor);
    text.append("\n");

    return text.toString();
  }
}

