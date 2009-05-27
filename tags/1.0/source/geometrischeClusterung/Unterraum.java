/*
 * Datei          : Unterraum.java
 * Letzte Änderung: 07. Juni 2005
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
 * Die Klasse repraesentiert einen Unterraum eines mehrdimensionalen Raums.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class Unterraum {

  /**
   * Ein im Unterraum enthaltener Punkt.
   */
  private Punkt enthaltenerPunkt;

  /**
   * Eine Basis des Unterraums.
   * jeder basicVektor ist eine Reihe
   */
  private Vektor[] basis;

  /**
   * Erzeugt eine neue Instanz aus einem Unterraum und einem neuen Punkt,
   * der nicht im Unterraum enthalten ist, indem die Basis des uebergebenen
   * Unterraums um einen Vektor von einem Punkt des Unterraums zum
   * uebergebenen Punkt ergaenzt wird. Ist der uebergebene Punkt im
   * Unterraum enthalten, wird eine <CODE>SubspaceInclusionException</CODE>
   * ausgeloesst.
   *
   * @exception SubspaceInclusionException  Der uebergebene Punkt ist im
   *                                        uebergebenen Unterraum enthalten.
   *
   * @param unterraum   Ein schon vorhandener Unterraum
   * @param neuerPunkt  Ein neuen Punkt
   */
  public Unterraum(Unterraum unterraum, Punkt neuerPunkt) {

    if (unterraum.enthaelt(neuerPunkt))
      throw new SubspaceInclusionException("Der neue Punkt ist im Unterraum enthalten");

    this.enthaltenerPunkt = unterraum.enthaltenerPunkt();
    int unterDim = unterraum.dimensionUnterraum();
    this.basis = new Vektor[unterDim + 1];

    for(int i = 0; i < unterDim; i++) {
      basis[i] =  unterraum.basis[i];
    }
    Vektor diffVektor = unterraum.enthaltenerPunkt.differenzVektor(neuerPunkt);
    basis[unterDim] =  diffVektor.multipliziertMitSkalar(1.0 / diffVektor.laenge());
  }

  /**
   * Erzeugt eine neue Instanz aus einer Menge von Vektoren und einem
   * enthaltenen Punkt. Dabei muss die Anzahl der Vektoren genau der
   * Dimension des von ihnen erzeugten Unterraums entsprechen, d.h. also
   * die Vektoren muessen linear unabhaengig sein. Anderenfalls wird eine
   * <CODE>LinearlyDependentException</CODE> ausgeloesst.
   *
   * @exception LinearlyDependentException  Die Dimension des erzeugten
   *                                        Unterraums ist kleiner als die
   *                                        Anzahl der ihn erzeugenden
   *                                        Vektoren.
   *
   * @param enthaltenerPunkt  Ein Punkt, der im Unterraum enthalten ist
   * @param erzeugendeVektoren  Die erzeugenden Vektoren des Unterraums
   */
  public Unterraum(Punkt enthaltenerPunkt, Vektor[] erzeugendeVektoren) {

    int vektorAnz = erzeugendeVektoren.length;
    this.enthaltenerPunkt = enthaltenerPunkt;
    this.basis = new Vektor[vektorAnz];

    VektorMenge vm = new VektorMenge(erzeugendeVektoren);
    if (vektorAnz != vm.numRang())
      throw new LinearlyDependentException("Die übergegebenen Vektoren sind lineal abhängig");

    for (int i = 0; i < vektorAnz; i++) {
      basis[i] = erzeugendeVektoren[i].multipliziertMitSkalar(1.0 / erzeugendeVektoren[i].laenge());
    }
  }

  /**
   * Erzeugen eine Instance aus einer Menge von Punkte, dabei muss die Anzahl
   * der Punkte genau um 1 groesse als die Dimension des Unterraums.dh, die
   * von dieser punkte erezugende Vektoren muesssen lineale unabhaengig sein.
   * Anderenfalls wird eine <CODE>LinearlyDependentException</CODE> ausgeloesst.
   *
   * @exception LinearlyDependentException  Die Dimension des erzeugten
   *                                        Unterraums ist kleiner als die
   *                                        Anzahl der ihn erzeugenden
   *                                        Vektoren.
   *
   * @param erzeugendePunkte  Die Punkte, die den Unterraum erzeugen
   */
  public Unterraum(ArrayList erzeugendePunkte) {

    int punkteAnz = erzeugendePunkte.size();
    this.enthaltenerPunkt = (Punkt)erzeugendePunkte.get(0);
    this.basis = new Vektor[punkteAnz - 1];

    if (punkteAnz > 1) {
      Punkt ersterPunkt = (Punkt)erzeugendePunkte.get(0);

      for(int i = 1; i < punkteAnz; i++){
        Vektor diffVektor = ersterPunkt.differenzVektor((Punkt)erzeugendePunkte.get(i));
        this.basis[i-1] = diffVektor.multipliziertMitSkalar(1.0 / diffVektor.laenge());
      }

      VektorMenge vm = new VektorMenge(basis);
      if (punkteAnz - 1 != vm.numRang()) {
        throw new LinearlyDependentException("Die übergegebenen Punkte spannen keinen Raum maximaler Dimension auf.");
      }
    }
  }

  /**
   * Liefert einen im Unterraum enthaltenen Punkt.
   *
   * @return  Den enthaltenen Punkt des Unterraums
   */
  public Punkt enthaltenerPunkt() {
    return enthaltenerPunkt;
  }

  /**
   * Liefert eine Basis des Unterraums.
   *
   * @return  Die Basis des Unterraums
   */
  public Vektor[] basis() {
    return basis;
  }

  /**
   * Liefert die Dimension des Unterraums.
   *
   * @return  Die Dimension des Unterraums
   */
  public int dimensionUnterraum() {
    return basis.length;
  }

  /**
   * Liefert die Dimension des Gesamtraums.
   *
   * @return  Die Dimension des Gesamtraums
   */
  public int dimensionGesamtraum() {
    return enthaltenerPunkt.dimension();
  }

  /**
   * Liefert den Abstand des uebergebenen Punktes vom Unterraum, d.h. die
   * Laenge des kuerzesten Vektors zwischen dem uebergebenen Punkt und
   * einem Punkt des Unterraums.
   *
   * @param punkt  Ein Punkt, von dem der Abstand ermittelt werden soll.
   * @return  Der Abstand vom Punkt zum Unterraum
   */
  public double abstand(Punkt punkt) {

    if(dimensionUnterraum()==0)
      return enthaltenerPunkt.abstand(punkt);
    else
      return punkt.abstand(projektion(punkt));
  }

  /**
   * Liefert die Projektion des uebergebenen Punktes auf den Unterraum.
   * Die Projektion ist der Schnittpunkt des Unterraums mit dem orthogonalen
   * Unterraum, der den übergebenen Punkt enthält. Von daher steht der
   * Differenzvektor zwischen dem übergebenen Punkt und der Projektion
   * senkrecht auf jeden Basisvektor des Unterraums. Außerdem ist die
   * Projektion der Punkt des Unterraums, der den geringsten Abstand zum
   * übergebenen Punkt hat.
   *
   * @param   punkt  Der Punkt, dessen Projektion ermittelt werden soll.
   *
   * @return  Die Projektion des uebergebenen Punktes auf den Unterraum
   */
  public Punkt projektion(Punkt punkt) {

    // Ermittlung und Test der Dimension des Unterraums
    int unterDim = basis.length;
    if (unterDim == 0) {
      return enthaltenerPunkt;
    }

    // Gleichungsystem ohne Spalte aus Konstanten
    double[][] glSystem = new double[unterDim][unterDim + 1];

    for (int z = 0; z < unterDim; z++) {
      for (int s = 0; s < unterDim; s++) {
        glSystem[z][s] = basis[z].skalarprodukt(basis[s]);
      }
    }

    // Konstruiere die Spalte aus Konstanten
    double[] konstantenSpalte = new double[unterDim];

    Vektor diffVektor = enthaltenerPunkt.differenzVektor(punkt);
    for(int z = 0; z < unterDim; z++){
      konstantenSpalte[z] = diffVektor.skalarprodukt(basis[z]);
    }

    // Zusammensetzen des Gleichungsystems
    for(int z = 0; z < unterDim; z++){
      glSystem[z][unterDim] = konstantenSpalte[z];
    }

    // Loesen des Gleichungsystems
    double[] loesung = new double[unterDim];
    loesung = LinearesGleichungsSystem.loesung(glSystem);

    // Summenvektor von den mit Faktoren multiplizierten Basis-Vektoren
    // ermitteln
    Vektor summenVektor = basis[0].multipliziertMitSkalar(loesung[0]);
    for (int i = 1; i < unterDim; i++){
      summenVektor = summenVektor.summe(basis[i].multipliziertMitSkalar(loesung[i]));
    }

    // Die Projektion ist der im Unterraum enthaltenen Punkt nach Verschiebung
    // um den Summenvektor
    return enthaltenerPunkt.nachVerschiebung(summenVektor);
  }

  /**
   * Liefert einen Punkt aus der übergebenen Menge, der maximalen Abstand zum
   * Unterraum hat. Wenn die Punktmenge leer ist, wird <CODE>NULL</CODE>
   * geliefert.
   *
   * @param punkte  Eine Menge von Punkten
   * @return  Einen Punkt aus einer Punktemenge, der maxmale Abstand zum
   *          Unterraum hat
   */
  public Punkt punktMitMaximalemAbstand(Collection punkte){

    Punkt maxPunkt = null;
    double maxAbstand = -1.0;

    Iterator punktIter = punkte.iterator();
    while (punktIter.hasNext()) {
      Punkt punkt = (Punkt)punktIter.next();
      double abstand = abstand(punkt);
      if (abstand > maxAbstand) {
        maxAbstand = abstand;
        maxPunkt = punkt;
      }
    }

    return maxPunkt;
  }

  /**
   * Ermittelt, ob der uebergebene Punkt im Unterraum enthalten ist. Wenn
   * der Abstand kleiner als Grenzwert ist, wird der Punkt als im Unterraum
   * enthalten betrachtet.
   *
   * @param punkt  Ein zu prüfender Punkt
   * @return  <CODE>true</CODE>, wenn der Punkt im Unterraum liegt, d.h. sein
   *          Abstand vom Unterraum kleiner als ein Grenzwert ist, sonst 
   *          <CODE>false</CODE>.
   */
  public boolean enthaelt(Punkt punkt) {
    return (abstand(punkt) <= Genauigkeit.maxAbstandFehler());
  }

  /**
   * Ermittelt, ob der uebergebene Punkt im Unterraum enthalten ist. Wenn
   * der Abstand kleiner als der angegebene Grenzwert ist, wird der Punkt als
   * im Unterraum enthalten betrachtet.
   *
   * @param punkt  Ein zu prüfender Punkt.
   * @param punkt  Die Ungenauigkeit, bis zu deren Abstand ein Punkt noch als
   *               im Unterraum enthalten gilt.
   *
   * @return  <CODE>true</CODE>, wenn der Punkt im Unterraum liegt, d.h. sein
   *          Abstand vom Unterraum kleiner als der angegebene Wert ist, sonst 
   *          <CODE>false</CODE>.
   */
  public boolean enthaelt(Punkt punkt, double ungenauigkeit) {
    return (abstand(punkt) <= ungenauigkeit);
  }

  /**
   * Liefert eine String-Darstellung der Instanz.
   *
   * @return  Ein String als Dastellung der Instanz.
   */
  public String toString() {

    StringBuffer text = new StringBuffer();

    text.append("@EnthaltenerPunkt von dem Unterraum" + "\n");
    text.append("  " + enthaltenerPunkt);
    text.append("\n");

    text.append("  @Basis von dem Unterraum" + "\n");

    int n =  dimensionUnterraum();
    for (int i = 0; i < n; i++){
      text.append("  " + basis[i]);
    }

    return text.toString();
  }
}

