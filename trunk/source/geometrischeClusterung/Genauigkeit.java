/*
 * Datei          : Genauigkeit.java
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

import java.util.ArrayList;

/**
 * Beinhaltet Werte und Methoden zur Berechnung der Genauigkit für alle Klassen
 * das packages. Vor der ersten Benutzung der Grenzwerte muß die Methode
 * <CODE>setzeGrenzwerte(ArrayList)</CODE> oder die Methode
 * <CODE>aktualisiereGrenzwerte(ArrayList)</CODE> aufgerufen werden.<P>
 *
 * Achtung: Da die Grenzwerte in static-Attributen gespeichert werden, sind
 * diese für mehrere gleichzeitige Berechnungen der konvexen Hülle (in
 * mehreren Threads) von verschiedenen Punktmengen gleich. Für den Fall ist
 * die Methode <CODE>aktualisiereGrenzwerte(ArrayList)</CODE> zu verwenden.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class Genauigkeit {

  /**
   * Gibt an, ob die Werte der Grenzwerte berechnet wurden.
   */
  private static boolean berechnet = false;

  /**
   * Die Genauigkeit der Darstellung einer double-Zahl, d.h. die kleinste
   * Stelle, die noch exakt dargestelt wird.
   */
  private static final double WERTGENAUIGKEIT = 1.0E-15;

  /**
   * Ein Faktor, der die zulässige Ungenauigkeit für die Berechnung des
   * Abstands eines Punktes von einer Facette festlegt.
   */
  private static final double UNGENAUIGKEIT = 0.5;

  /**
   * Der maximale Fehler für ein Diagonalelement im Gauß-Verfahren.
   */
  private static double maxDiagonalFehler = 0;

  /**
   * Der maximale Fehler für ein Diagonalelement im Gauß-Verfahren. Er kann
   * als Untergrenze für den Wert verwendet werden, den ein Diagonalelement
   * bei der Berechnung einer oberen Dreiecksmatrix mittels des Gauß-Verfahrens
   * haben muß. Wenn der Wert gleich oder kleiner ist, wird er als Null
   * betrachtet. Bei einem Gleichungssystem ist dieses dann nicht lösbar.
   */
  public static final double maxDiagonalFehler() {
    if (! berechnet) {
      throw new RuntimeException("Grenzwerte wurden noch nicht berechnet.");
    } else {
      return maxDiagonalFehler;
    }
  }

  /**
   * Der maximale Fehler bei der Berechnung des Abstands aufgrund der
   * Wert- und Rechenungenauigkeit.
   */
  private static double maxAbstandFehler = 0;

  /**
   * Der maximale Fehler bei der Berechnung des Abstands aufgrund der
   * Wert- und Rechenungenauigkeit.
   */
  public static final double maxAbstandFehler() {
    if (! berechnet) {
      throw new RuntimeException("Grenzwerte wurden noch nicht berechnet.");
    } else {
      return maxAbstandFehler;
    }
  }

  /**
   * Liefert das Maximum der Maximum-Norm aller Differenzvektoren der
   * übergebenen Punkte. Wenn die Liste der übergebenen Punkte keinen
   * Punkt enthält, wird der Wert Null geliefert.
   */
  private static final double maxDiffMaxNorm(ArrayList punkte) {
    if (punkte.size() == 0) {
      return 0;
    }

    Punkt punkt = (Punkt)punkte.get(0);
    double[] minWerte = new double[punkt.dimension()];
    double[] maxWerte = new double[punkt.dimension()];

    for (int pnr = 1; pnr < punkte.size(); pnr++) {
      punkt = (Punkt)punkte.get(pnr);
      for (int knr = 0; knr < punkt.dimension(); knr++) {
        minWerte[knr] = Math.min(minWerte[knr], punkt.komponente(knr));
        maxWerte[knr] = Math.max(maxWerte[knr], punkt.komponente(knr));
      }
    }

    double maxDiff = 0;
    for (int knr = 0; knr < maxWerte.length; knr++) {
      maxDiff = Math.max(maxDiff, maxWerte[knr] - minWerte[knr]);
    }
    return maxDiff;
  }

  /**
   * Berechnet die Grenzwerte aufgrund der Punkte, aus denen die konvexe
   * Hüelle erzeugt werden soll.
   */
  private static final synchronized void berechneGrenzwerte(ArrayList punkte, boolean neu) {
    if (punkte.size() > 0) {
      double maxMaxNorm = maxDiffMaxNorm(punkte);
      Punkt punkt = (Punkt)punkte.get(0);
      int dimension = punkt.dimension();

      // Der maximale Fehler für ein Diagonalelement im Gauß-Verfahren ist
      // das Produkt aus der Wertgenauigkeit und der Dimension der Vektoren.
      maxDiagonalFehler = WERTGENAUIGKEIT * dimension;

      // Der minimale Abstand wird als zulässige Ungenauigkeit bei der
      // Abstandsberechnung aus der Wertgenauigkeit, dem Faktor der
      // zulässigen Ungenauigkeit, der maximalen Größe der Norm der
      // Differenzvektoren und dem Quadrat ihrer Dimension berechnet.
      double maxFehler = WERTGENAUIGKEIT * UNGENAUIGKEIT * dimension * maxMaxNorm * maxMaxNorm;
      if (neu) {
        maxAbstandFehler = maxFehler;
      } else {
        maxAbstandFehler = Math.max(maxAbstandFehler, maxFehler);
      }

      berechnet = true;
    }
  }

  /**
   * Berechnet die Grenzwerte aufgrund der Punkte, aus denen die konvexe
   * Hüelle erzeugt werden soll. Da die Grenzwerte bei jedem Aufruf neu
   * berechnet und in static-Attributen gespeichert werden, darf diese
   * Methode nicht verwendet werden, wenn gleichzeitig mehrere konvexe
   * Hüllen für verschiedene Punktmengen erzeugt werden. In dem Fall ist
   * die Methode <CODE>aktualisiereGrenzwerte(ArrayList)</CODE> zu
   * verwenden.
   */
  public static final void setzeGrenzwerte(ArrayList punkte) {
    berechneGrenzwerte(punkte, true);
  }

  /**
   * Aktualisiert die Grenzwerte aufgrund der Punkte, aus denen die konvexe
   * Hüelle erzeugt werden soll. Dabei wird den Grenzwerte der sicherste
   * Wert aus allen Aufrufen zugewiesen, wodurch es aber mit höherer
   * Wahrscheinlichkeit passieren kann, daß ein Punkt, der nur etwas
   * außerhalb der konvexen Hülle liegt, als in der konvexen Hülle enthalten
   * betrachtet wird.
   */
  public static final void aktualisiereGrenzwerte(ArrayList punkte) {
    berechneGrenzwerte(punkte, false);
  }
}

