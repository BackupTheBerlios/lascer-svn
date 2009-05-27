/*
 * Datei          : LinearesGleichungsSystem.java
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

/**
 * Liefert die Lösung zu einem linearen Gleichungssystem.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class LinearesGleichungsSystem {

  /**
   * Erzeugt aus einer oberen Dreiecksmatrix die Lösung des Gleichungssystems,
   * indem von unten nach oben die Werte für die Variablen ermittelt und dann
   * in die weiteren Gleichunen eingesetzt werden. Die Werte der Variablen
   * steht am Ende auf der rechten Seite, d.h. in der Spalte ganz rechts.
   *
   * @param matrix  Eine obere Dreiecksmatrix.
   * @exception RuntimeException  Wenn es keine Lösung oder unendliche
   *                              Lösungen gibt
   */
  private static void substitution(double[][] matrix) {

    int anz = matrix.length;  // Anzahl der Gleichungen und Variablen

    for(int z = anz - 1; z >= 0; z--) {
      if(Math.abs(matrix[z][z]) <= Genauigkeit.maxDiagonalFehler()) {
        System.err.println("Wert kleiner als Genauigkeit.minGaussWert(): " + matrix[z][z]);
        throw new RuntimeException("Fehler beim Lösen des Gleichungssystems. Wert ist zu klein.");
      }
      for(int s = z + 1; s < anz; s++){
        matrix[z][anz] -= matrix[z][s] * matrix[s][anz];
      }
      matrix[z][anz] /= matrix[z][z];
    }
  }

  /**
   * Stellt durch geeignete Subtraktionen von Zeilen eine obere Dreiecksmatrix
   * her.
   *
   * @param matrix  Die erweiterte Matrix, die gelost werden soll.
   * @exception RuntimeException  Wenn es keine Lösung oder unendlich viele
   *                              Lösungen gibt.
   */
 private static void gaussElimination(double matrix[][]) {

    int pZeile;
    int n = matrix.length;
    double[] zeilensumme = new double[n];

    // Berechnen der Zeilensummen
    for (int zeile = 0; zeile < n; zeile++) {
      zeilensumme[zeile] = 0;
      for (int spalte = 0; spalte < n; spalte++) {
        zeilensumme[zeile] += Math.abs(matrix[zeile][spalte]);
      }
    }

    for (int i = 0; i < n - 1; i++) {

      // Bestimmen des Pivot-Element der Spalte als das Element, das nach
      // Division durch die Zeilensumme am größten ist
      pZeile = i;
      double pivotWert = Math.abs(matrix[pZeile][i]) / zeilensumme[pZeile];
      for (int z = i + 1; z < n; z++) {
        if (Math.abs(matrix[z][i]/zeilensumme[z]) > pivotWert) {
          pZeile = z;
          pivotWert = Math.abs(matrix[pZeile][i]) / zeilensumme[pZeile];
        }
      }

      if (i != pZeile) {
        // Die Zeilen i und pZeile vertauschen
        double temp;
        for(int s = i; s < n + 1; s++) {
          temp = matrix[i][s];
          matrix[i][s] = matrix[pZeile][s];
          matrix[pZeile][s] = temp;
        }
        temp = zeilensumme[i];
        zeilensumme[i] = zeilensumme[pZeile];
        zeilensumme[pZeile] = temp;
      }

      if(Math.abs(matrix[i][i]) <= Genauigkeit.maxDiagonalFehler()) {
        System.err.println("Wert kleiner als Genauigkeit.minGaussWert(): " + matrix[i][i]);
        throw new RuntimeException("Fehler beim Lösen des Gleichungssystems. Wert ist zu klein.");
      }

      // GaussElimination
      for (int z = i + 1; z < n; z++) {
        double quotient = matrix[z][i] / matrix[i][i];
        matrix[z][i] = 0;
        for (int s = i + 1; s < n + 1; s++) {
          matrix[z][s] -= matrix[i][s] * quotient;
        }
      }
    }
  }

  /**
   * Ermittelt die Lösung zu einem Gleichungssystem mit einer Spalte auf
   * der rechten Seite.
   *
   * @param matrix die gleichungsmatrix
   * @return return the last column als solution
   */
  public static double[] loesung(double[][] matrix) {

    int zeilenAnz = matrix.length;
    int spaltenAnz = matrix[0].length;

    if (zeilenAnz != spaltenAnz - 1) {
      throw new RuntimeException("Gleichungsmatrix hat nicht das richtige Format");
    }

    double[][] matrixKopie = new double[zeilenAnz][spaltenAnz];
    for (int z = 0; z < zeilenAnz; z++) {
      for (int s = 0; s < spaltenAnz; s++) {
        matrixKopie[z][s] = matrix[z][s];
      }
    }

    // Erzeugung einer oberen Dreiecksmatrix
    gaussElimination(matrixKopie);

    // Auflösung der Gleichungen durch iterierte Substitution
    substitution(matrixKopie);

    double[] result = new double[zeilenAnz];
    for(int z = 0; z < zeilenAnz; z++) {
      result[z] = matrixKopie[z][spaltenAnz - 1];
    }

    return result;
  }

  /**
   * Ermittlung die Norm des Fehlers der Lösung des linearen Gleichungssystems.
   *
   * @param matrix  Die Gleichungsmatrix
   * @param solution  Eine Lösung des Gleichungssystems
   * @return  Die Norm des Fehlers der Lösung
   */
  public static double quadratFehlerNorm(double[][] matrix, double[] solution) {

    int varAnz = solution.length;
    double quadratSumme = 0;

    // Ermittelung der Differenzen zwischen vorgegebenen und der errechneten
    // rechten Seite des Gleichungssystems
    for (int zeile = 0; zeile < varAnz; zeile++) {
      double summe = 0;
      for (int spalte = 0; spalte < varAnz; spalte++) {
        summe += matrix[zeile][spalte] * solution[spalte];
      }
      double differenz = matrix[zeile][varAnz] - summe;
      quadratSumme += differenz * differenz;
    }

    return Math.sqrt(quadratSumme);
  }
}

