/*
 * Datei          : Punkt.java
 * Letzte Änderung: 09. Juni 2005
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
 * Die Klasse repraesentiert einen Punkt im mehrdimensionalen Raum.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class Punkt {

  /**
   * Die Komponenten des Punktes, bezogen auf die
   * Standard-Orthonormal-Basis.
   */
  private double[] komponenten;

  /**
   * Die unterhalb des Punktes liegenden Facetten, d.h. die Facetten, bei
   * denen der Punkt oberhalb liegt.
   */
  private HashSet unterhalbFacetten;

  /**
   * Die HashCode des Punktes. Wird vor der ersten Verwendung berechnet.
   */
  private int hashCode = 0;

  /**
   * Erzeugt einen neuen Punkt aus den uebergebenen Komponenten-Werten,
   * wobei diese kopiert werden und keine Referenz uebernommen wird.
   *
   * @param werte die Werte des Punktes
   */
  public Punkt(double[] werte) {
    komponenten = new double[werte.length];
    System.arraycopy(werte, 0, komponenten, 0, werte.length);
    unterhalbFacetten = new HashSet();
  }

  /**
   * Liefert die Dimension des Raums, in dem der Punkt enthalten ist.
   *
   * @return return die Dimension des Gesamtraums
   */
  public int dimension() {
    return komponenten.length;
  }

  /**
   * Liefert die unterhalbFacetten des Punktes
   *
   * @return return alle Facette ,die unterhalb vom Punkt liegen
   */
  public HashSet unterhalbFacetten() {
    return unterhalbFacetten;
  }

  /**
   * Liefert vom Punkt die Komponente mit der uebergebenen Nummer. Diese
   * muss mindestens Null und maximal so gross sein wie die Dimension des
   * Punktes minus Eins.
   *
   * @param nummer eine integer Zahle zwischen 0 und die Dimension des Punktes
   *  minus Eins
   * @return return die Komponentewert, die der Punkt auf der Stelle nummer hat
   */
  public double komponente(int nummer) {
    return komponenten[nummer];
  }

  /**
   * Liefert den Abstand des uebergebenen Punktes vom Punkt selbst.
   * Wenn die zwei Punkt unterschiedliche Dimension haben,werde die
   * <CODE>SizeMismatchException</CODE> ausgeloesst
   *
   * @exception SizeMismatchException  Die zwei Punkt haben unterschiedliche
   *                                   Dimension
   *
   * @param andererPunkt  Eine anderer Punkt
   * @return  Der Abstand zwischen dem Punkt selbst und dem übergebenen Punkt
   */
  public double abstand(Punkt andererPunkt) {

    int n = dimension();

    if(n != andererPunkt.dimension())
      throw new SizeMismatchException("Die beiden Punkte haben verschiedene Dimension");

    return differenzVektor(andererPunkt).laenge();
  }

  /**
   * Liefert einen Punkt aus der punkteMenge, der maxmale Abstand zu punkt hat,
   * wenn es mehr as einen solchen Punkt gibt, wird nur einen davon ausgegeben
   * wenn die gegebene PunkteMenge leer ist, wird NULL ausgegeben
   *
   * @param punkte  Eine Menge von Punkten
   * @return  Einen punkt aus der PunkteMenge, der maximalen Abstand zu dem
   *          Punkt selbst hat
   */
  public Punkt punktMitMaximalemAbstand(ArrayList punkte){

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
   * Liefert den Vektor, der vom Punkt selbst zum uebergebenen Punkt fuehrt.
   * Wenn die zwei Punkt unterschiedliche Dimension haben,werde die
   * <CODE>SizeMismatchException</CODE> ausgeloesst
   *
   * @exception SizeMismatchException  Die zwei Punkt haben unterschiedliche
   *                                   Dimension
   *
   * @param andererPunkt  Einen andererPunkt
   * @return  Der Differenzvektor zwischen den beiden Punkte
   */
  public Vektor differenzVektor(Punkt andererPunkt) {

    int dim = komponenten.length;
    if(dim != andererPunkt.dimension()){
      throw new SizeMismatchException("Die zwei Punkte haben unterschiedliche Dimension");
    }

    double[] diffKomp = new double[dim];

    for(int i = 0; i < dim; i++){
      diffKomp[i] = andererPunkt.komponenten[i] - komponenten[i];
    }

    return new Vektor(diffKomp);
  }

  /**
   * Liefert den Punkt, der nach Verschiebung des Punktes selbst um den
   * uebergebenen Vektor entsteht. Wenn die Dimension vom Punkt und vom Vektor
   * unterschiedlich sit,wird eine <CODE>SizeMismatchException</CODE>
   * ausgeloesst.
   *
   * @exception SizeMismatchException  Der Punkt und der Vektor haben eine
   *                                   unterschiedliche Dimension.
   *
   * @param vektor  Der Vektor, um den der Punkt verschoben wird.
   * @return  Den Punkt, der nach der Verschiebung entsteht.
   */
  public Punkt nachVerschiebung(Vektor vektor) {

    if (komponenten.length != vektor.dimension()) {
      throw new SizeMismatchException("Der Punkt und der Vektor haben eine unterschiedliche Dimension.");
    }

    Punkt neuerPunkt = new Punkt(komponenten);
    for (int knr = 0; knr < komponenten.length; knr++) {
      neuerPunkt.komponenten[knr] += vektor.komponente(knr);
    }
    return neuerPunkt;
  }

  /**
   * Liefert den hashCode für den Punkt. Falls er noch nicht berechnet
   * wurde, wird er zuvor berechnet.
   *
   * @return   Den hashCode des Punktes
   */
  public int hashCode(){
    if (hashCode == 0) {
      hashCode = berechnerHashCode();
    }
    return hashCode;
  }

  /**
   * Liefert einen berechneten Hashcode für den Punkt selbst.
   *
   * @return  Ein Hashcode für <CODE>this</CODE>
   */
  private int berechnerHashCode() {
    return toString().hashCode();
  }

  /**
   * Ueberschreibt die entsprechende Methode der Klasse Object.
   *
   * @param object  Eine Objekt
   * @return  <CODE>true</CODE>, wenn der uebergebene Punkt identische
   *          mit dem Punkt selbst ist, anderenfalls <CODE>false</CODE>.
   */
  public boolean equals(Object object){

    if(object == this) {
      return true;
    }

    if(! (object instanceof Punkt)) {
      return false;
    }

    if(object.hashCode() != hashCode()) {
      return false;
    }

    return Arrays.equals(((Punkt)object).komponenten,komponenten);
  }

  /**
   * Fügt die übergebene Facette der Menge unterhalbFacetten hinzu.
   *
   * @param facette  Die hinzuzufügende Facette
   * @return  <CODE>true</CODE>, wenn die Facette der Menge echt hinzugefügt
   *          wurde, d.h. in ihr noch nicht enthalten war, anderenfalls
   *          <CODE>false</CODE>
   */
  public boolean unterhalbFacetteHinzufuegen(Facette facette){
    return unterhalbFacetten.add(facette);
  }

  /**
   * Loescht die übergebene Facette aus der Menge unterhalbFacetten.
   *
   * @param facette  Die zu löschende Facette
   * @return  <CODE>true</CODE>, wenn die Facette aus der Menge tatsächlich
   *          gelöscht wurde, d.h. in ihr enthalten war, anderenfalls
   *          <CODE>false</CODE>
   */
  public boolean unterhalbFacetteLoeschen(Facette facette){
    return unterhalbFacetten.remove(facette);
  }

  /**
   * Ermittln den schwerpunkt aus einer punkteMenge
   * Es wird angenommen,dass alle Punkte die gleiche
   * Dimension haben
   *
   * @param punkteMenge eine Menge von Punkt
   * @return return den schwerpunkt von den gegebenen punktmenge
   */
  public static Punkt schwerpunkt(ArrayList punkteMenge){

    int n = punkteMenge.size();
    int m = ((Punkt)punkteMenge.get(0)).dimension();
    double[] komponenten = new double[m];

    for(int i=0; i<m; i++){
      for(int j=0; j<n; j++)
        komponenten[i] = ((Punkt)punkteMenge.get(j)).komponente(i) + komponenten[i];
    }

    for(int i=0; i<m; i++){
      komponenten[i] = komponenten[i] / n;
    }

    return new Punkt(komponenten);
  }

  /**
   * Liefert eine Darstellung des Punktes als Folge seiner Komponenten.
   *
   * @return  Eine Darstellung des Punktes als Folge seiner Komponenten.
   */
  public String komponentenDarstellung() {
    StringBuffer darstellung = new StringBuffer();

    darstellung.append("<");
    for(int i = 0; i < dimension(); i++) {
      darstellung.append(komponente(i) + ", ");
    }
    if (dimension() > 0) {
      darstellung.deleteCharAt(darstellung.length() - 1);
      darstellung.deleteCharAt(darstellung.length() - 1);
    }
    darstellung.append(">");

    return darstellung.toString();
  }

  /**
   * Returns a string representation of this Instance
   *
   * @return return eine String Dastellung der Instance
   */
  public String toString() {

    StringBuffer text = new StringBuffer();

    text.append("@komponenten von dem Punkt" + "\n");

    int n =   dimension();
    for(int i = 0; i < n; i++) {
      text.append("  " + komponente(i) + "  ");
    }
    text.append("\n");

    return text.toString();
  }
}

