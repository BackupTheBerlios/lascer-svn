/*
 * Datei          : Vektor.java
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

/**
 * Die Klasse repraesentiert einen Vektor im mehrdimensionalen Raum.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class Vektor {

  /**
   * Die Komponenten des Vektors, bezogen auf die
   * Standard-Orthonormal-Basis.
   */
  private double[] komponenten;

  /**
   * Erzeugt einen neuen Vektor aus den uebergebenen Komponenten-Werten,
   * wobei diese kopiert werden und keine Referenz uebernommen wird.
   *
   * @param werte eine double values als die Werte des Vektors
   */
  public Vektor(double[] werte) {
    komponenten = new double[werte.length];
    System.arraycopy(werte, 0, komponenten, 0, werte.length);
  }

  /**
   * Liefert die Dimension des Vektors.
   *
   * @return return die Dimension des Vektors
   */
  public int dimension() {
    return komponenten.length;
  }

  /**
   * Liefert vom Vektor die Komponente mit der uebergebenen Nummer. Diese
   * muss mindestens Null und maximal so gross sein wie die Dimension des
   * Vektors minus Eins.
   *
   * @param nummer eine integer Zahl zwischen 0 und die Dimension des Vektors minus Eins
   * @return return die Komponentwert, die die Vektor auf der Stelle nummer hat
   */
  public double komponente(int nummer) {
    return komponenten[nummer];
  }

  /**
   * Liefert die Laenge des Vektors.
   *
   * @return return die Laenge des Vektors
   */
  public double laenge() {
    return Math.sqrt(skalarprodukt(this));
  }

  /**
   * Liefert den Summenvektor aus dem uebergebenen Vektor und dem Vektor
   * selbst. Wenn die beiden Vektoren eine unterschiedliche Dimension
   * haben, wird eine <CODE>SizeMismatchException</CODE> ausgeloesst.
   *
   * @exception SizeMismatchException  Der uebergebene Vektor hat eine andere
   *                                   Dimension als der Vektor selbst.
   *
   * @param andererVektor ein andererVektor
   * @return  Die Summe vom Vektor selbst und dem übergebenen Vektor
   */
  public Vektor summe(Vektor andererVektor) {

    int dim = komponenten.length;
    if (dim != andererVektor.komponenten.length) {
      throw new SizeMismatchException("Die zu summierenden Vektoren haben unterschiedliche Dimension");
    }

    Vektor summe = new Vektor(komponenten);
    for (int knr = 0; knr < dim; knr++) {
      summe.komponenten[knr] += andererVektor.komponenten[knr];
    }
    return summe;
  }

  /**
   * Liefert den Differenzvektor der entsteht, wenn vom Vektor selbst der
   * uebergebene Vektor abgezogen wird. Wenn die beiden Vektoren eine
   * unterschiedliche Dimension haben, wird eine
   * <CODE>SizeMismatchException</CODE> ausgeloesst.
   *
   * @exception SizeMismatchException  Der uebergebene Vektor hat eine andere
   *                                   Dimension als der Vektor selbst.
   *
   * @param andererVektor ein andererVektor
   * @return  Die Differenz vom dem Vektor selbst und dem übergebenen Vektor
   */
  public Vektor differenz(Vektor andererVektor) {

    int dim = komponenten.length;
    if (dim != andererVektor.komponenten.length) {
      throw new SizeMismatchException("Die zu subtrahierenden Vektoren haben unterschiedliche Dimension");
    }

    Vektor differenz = new Vektor(komponenten);
    for (int knr = 0; knr < dim; knr++) {
      differenz.komponenten[knr] -= andererVektor.komponenten[knr];
    }
    return differenz;
  }

  /**
   * Liefert das Skalarprodukt aus dem uebergebenen Vektor und dem Vektor
   * selbst. Wenn die beiden Vektoren eine unterschiedliche Dimension
   * haben, wird ein <CODE>SizeMismatchException</CODE> ausgeloesst.
   *
   * @exception SizeMismatchException  Wenn der uebergebene Vektor eine
   *                                   andere Dimension hat also der Vektor
   *                                   selbst.
   * @param andererVektor ein andererVektor
   * @return return die Skalarprodukt zwischen dem Vektor selbst und einem gegebenen Vektor
   */
  public double skalarprodukt(Vektor andererVektor) {

    int n = dimension();
    double sum = 0;

    if (n != andererVektor.dimension()) {
      throw new SizeMismatchException("Die Vektoren haben eine unterschiedliche Laenge");
    }

    for(int i=0; i<n; i++){
      sum  += komponenten[i] * andererVektor.komponenten[i];
    }
    return sum;
  }

  /**
   * Liefert den Vektor, der durch Multiplikation von <CODE>this</CODE> mit
   * dem übergebenen Skalar-Wert entsteht.
   *
   * @param   faktor  Der Skalar-Wert, mit dem der Vektor selbst multipliziert
   *                  wird.
   *
   * @return  Der Vektor, der durch Multiplikation mit dem übergebenen
   *          Skalar-Wert entsteht.
   */
  public Vektor multipliziertMitSkalar(double faktor) {

    int n = dimension();
    Vektor result = new Vektor(this.komponenten);

    for(int i=0; i<n; i++){
       result.komponenten[i] = komponenten[i] * faktor;
    }
    return result;
  }

  /**
   * Liefert eine Darstellung des Vektors als Folge seiner Komponenten.
   *
   * @return  Eine Darstellung des Vektors als Folge seiner Komponenten.
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
   * @return return eine String Dastellung von der Instance
   */
  public String toString() {

    StringBuffer text = new StringBuffer();

    text.append("@komponenten von dem Vektor" + "\n");

    int n =  dimension();
    for (int i = 0; i < n; i++) {
      text.append("  " +  komponenten[i] + "  ");
    }
    text.append("\n");

    return text.toString();
  }
}

