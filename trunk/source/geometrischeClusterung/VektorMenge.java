/*
 * Datei          : VektorMenge.java
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
 * Die Klasse enthält Methoden für eine Menge von Vektoren.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class VektorMenge{

  /**
   * Die VektorMatrix von VektorMenge
   */
  private double[][] vektorMatrix ;

  /**
   * Der Rang von der Vektoren
   */
  private int rang;

  /**
   * Berechnet zu einer Menge von Vektoren deren Rang. Es wird vorausgesetzt,
   * daß alle Vektoren die gleiche Dimension haben.
   *
   * @param vektoren  Eine Menge von Vektoren
   */
  public VektorMenge(Vektor[] vektoren){

    int vektorAnz = vektoren.length;
    int vektorDim = vektoren[0].dimension();

    this.vektorMatrix = new double[vektorAnz][vektorDim];
    for (int i = 0; i < vektorAnz; i++) {
      for (int j = 0; j < vektorDim; j++) {
        vektorMatrix[i][j] = vektoren[i].komponente(j);
      }
    }
    berechneNumRang();
  }

  /**
   * Berechnet zu einer Menge von Punkten den Rang ihrer Differenzvektoren.
   * Die Menge der übergebenen Punkte darf nicht leer sein und es wird
   * vorausgesetzt, daß alle Vektoren die gleiche Dimension haben.
   *
   * @param punkteArray  Eine Menge von Punkten
   */
  public VektorMenge(ArrayList punkteArray){

    int punkteAnz = punkteArray.size();
    Punkt ersterPunkt = (Punkt)punkteArray.get(0);
    int punkteDim = ersterPunkt.dimension();

    Vektor[] vektorMenge = new Vektor[punkteAnz - 1];

    for(int i = 1; i < punkteAnz; i++){
      vektorMenge[i-1] = ersterPunkt.differenzVektor((Punkt)punkteArray.get(i));
    }

    this.vektorMatrix = new double[punkteAnz - 1][punkteDim];
    for (int i = 0; i < punkteAnz - 1; i++) {
      for (int j = 0; j < punkteDim; j++) {
        this.vektorMatrix[i][j] = vektorMenge[i].komponente(j);
      }
    }
    berechneNumRang();
  }

  /**
   * Berechnen des Rangs der VektorMatrix.
   */
  private void berechneNumRang() {

    double[][] mat = obereDreiecksmatrix();

    int zeilenAnz  = mat.length;
    int spaltenAnz = mat[0].length;
    boolean zero = true;
    int numRang = 0;

    for (int i = 0; i < zeilenAnz; i++) {
      zero = true;
      for (int j = 0; j < spaltenAnz; j++) {
        if (mat[i][j] != 0)
          zero = false;
      }
      if (zero == false)
        numRang = numRang + 1;
    }
    this.rang = numRang;
  }

  /**
   * Berechnet die obere Dreiecksmatrix zu vektorMatrix mittels des
   * Gauß-Verfahrens. Wenn eine Spalte nur aus Werten besteht, die als Null
   * betrachtet werden, wid das entsprechende Diagonalelement übergangen und
   * mit dem nächsten Diagonalelement fortgefahren.
   *
   * @return  Eine obere Dreiecksmatrix
   */
  private double[][] obereDreiecksmatrix() {

    int zeilenAnz  = vektorMatrix.length;
    int spaltenAnz = vektorMatrix[0].length;
    int quadGroesse = Math.max(zeilenAnz, spaltenAnz);
    double quadMatrix[][] = new double[quadGroesse][quadGroesse];

     // Kopieren die Elemente der Matrix in eine quadratische Matrix
    for (int z = 0; z < zeilenAnz; z++) {
      for (int s = 0; s < spaltenAnz; s++) {
        quadMatrix[z][s] = vektorMatrix[z][s];
      }
    }

    int pZeile;
    double[] zeilensumme = new double[quadGroesse];

    // Berechnen der Zeilensummen
    for (int zeile = 0; zeile < quadGroesse; zeile++) {
      zeilensumme[zeile] = 0;
      for (int spalte = 0; spalte < quadGroesse; spalte++) {
        zeilensumme[zeile] += Math.abs(quadMatrix[zeile][spalte]);
      }
    }

    for (int i = 0; i < quadGroesse; i++) {

      // Bestimmen des Pivot-Element der Spalte als das Element, das nach
      // Division durch die Zeilensumme am größten ist
      pZeile = i;
      double pivotWert = Math.abs(quadMatrix[pZeile][i]) / zeilensumme[pZeile];
      for (int z = i + 1; z < quadGroesse; z++) {
        if (Math.abs(quadMatrix[z][i] / zeilensumme[z]) > pivotWert) {
          pZeile = z;
          pivotWert = Math.abs(quadMatrix[pZeile][i]) / zeilensumme[pZeile];
        }
      }

      if (i != pZeile) {
        // Die Zeilen i und pZeile vertauschen
        double temp;
        for (int s = i; s < quadGroesse; s++) {
          temp = quadMatrix[i][s];
          quadMatrix[i][s] = quadMatrix[pZeile][s];
          quadMatrix[pZeile][s] = temp;
        }
        temp = zeilensumme[i];
        zeilensumme[i] = zeilensumme[pZeile];
        zeilensumme[pZeile] = temp;
      }

      // Wenn das Diagonalelement betragsmäßig größer als ein Mindestwert ist,
      // werden die Elemente unter diesem durch Subtraktion von Zeilen
      // eleminiert. Ansonsten wird zur nächsten Zeile übergegangen.
      if (Math.abs(quadMatrix[i][i]) > Genauigkeit.maxDiagonalFehler()) {

        // GaussElimination
        for (int z = i + 1; z < quadGroesse; z++) {
          double quotient = quadMatrix[z][i] / quadMatrix[i][i];
          quadMatrix[z][i] = 0;
          for (int s = i + 1; s < quadGroesse; s++) {
            quadMatrix[z][s] -= quadMatrix[i][s] * quotient;
          }
        }
      }
    }

    return quadMatrix;
  }

  /**
   * @return  Den Rang der VektorMatrix
   */
  public int numRang() {
    return rang;
  }
}

