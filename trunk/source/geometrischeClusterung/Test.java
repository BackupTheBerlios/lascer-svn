/*
 * Datei          : Test.java
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
 * Testet die Klassen des Packages.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class Test {

  private static Random punktRandom = new Random();

  /**
   * Der maximale Wert der Komponenten der zufälig erzeugten Punkte.
   */
  private static final int MAX_KOMPONENTEN_WERT = 200;

  /**
   * Erzeugt Instanzen der Klassen des Packages, ruft verschiedene ihrer
   * Methoden auf und gibt die Ergebnisse auf der Standardausgabe aus.
   *
   * @param args some arguments
   */
  public static void main(String[] args) {
/*
    nullDimInEinDimTest();
    einDimInEinDimTest();
    nullDimInZweiDimTest();
    einDimInZweiDimTest();
    zweiDimInZweiDimTest();
    nullDimInDreiDimTest();
    einDimInDreiDimTest();
    zweiDimInDreiDimTest();
    dreiDimInDreiDimTest();
*/
    zufaelligerPunktTest();
  }

  /**
   * Erzeugt eine Folge von Facetten aus Mengen zufälliger Punkte.
   */
  public static void zufaelligerPunktTest() {

    // Vorgabe eines Wertes für den Zufallsgenerator zur Wiederholbarkeit
    // der Erzeugung der Datensätze
    punktRandom.setSeed(7);

    // Erzeugung einer Folge von konvexen Hüllen
    for(int i = 0; i < 10; i++) {
      System.out.println("Schleifendurchlauf " + i);
      ArrayList punkte = randomPunkte(100, 5);

      KonvexeHuelle  kh = new KonvexeHuelle(punkte);
//      KonvexeHuelle  kh = new KonvexeHuelle(punkte, 1000);
      System.out.println("Anzahl der Facetten = " + kh.facetten().size());
//      System.out.println("Anzahl enthaltener Punkte = " + kh.anzEnthaltenerPunkte(punkte));
    }
  }

  /**
   * Erzeugt eine Menge von zufälligen Punkten.
   *
   * @param anzahl  Die Anzahl der zu erzeugenden Punkte
   * @param dimension  Die Dimension der zu erzeugenden Punkte
   * @return  Die erzeugten Punkte
   */
  public static ArrayList randomPunkte(int anzahl, int dimension){

    ArrayList result = new ArrayList(anzahl);

    for (int i = 0; i < anzahl; i++){
      result.add(randomPunkt(dimension));
    }

    return result;
  }

  /**
   * Erzeugt einen zufälligen Punkt.
   *
   * @param dimension Die dimension des zu erzeugende Punkt
   * @return return einen randomPunkt
   */
  public static Punkt randomPunkt(int dimension) {

    double[] komponenten = new double[dimension];

    for(int i=0; i<dimension; i++){
      komponenten[i] = MAX_KOMPONENTEN_WERT * punktRandom.nextDouble();
    }

    return new Punkt(komponenten);
  }

  /**
   * Test mit einem nulldimensionalen Unterraum aus eindimensionalen Punkten.
   */
  public static void nullDimInEinDimTest() {

    double[]  a1 = {1};
    double[]  a2 = {1};

    Punkt p1  = new  Punkt(a1);
    Punkt p2  = new  Punkt(a2);

    Punkt[] punkteArray = {p1,p2};

    ArrayList punkte = new ArrayList();
    int n = punkteArray.length;

    for(int i=0; i<n; i++){
      punkte.add(punkteArray[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.print(kh);
  }

  /**
   * Test mit einem eindimensionalen Unterraum aus eindimensionalen Punkten.
   */
  public static void einDimInEinDimTest() {

    double[]  a1 = {1};
    double[]  a2 = {1};
    double[]  a3 = {2};
    double[]  a4 = {5};

    Punkt p1  = new  Punkt(a1);
    Punkt p2  = new  Punkt(a2);
    Punkt p3  = new  Punkt(a3);
    Punkt p4  = new  Punkt(a4);

    Punkt[] punkteArray = {p1,p2,p3,p4};

    ArrayList punkte = new ArrayList();
    int n = punkteArray.length;

    for(int i=0; i<n; i++){
      punkte.add(punkteArray[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.print(kh);
  }

  /**
   * Test mit einem nulldimensionalen Unterraum aus zweidimensionalen Punkten.
   */
  public static void nullDimInZweiDimTest() {

    double[]  a1 = {1,1};
    double[]  a2 = {1,1};

    Punkt p1  = new  Punkt(a1);
    Punkt p2  = new  Punkt(a2);

    Punkt[] punkteArray = {p1,p2};

    ArrayList punkte = new ArrayList();
    int n = punkteArray.length;

    for(int i=0; i<n; i++){
      punkte.add(punkteArray[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.print(kh);
  }

  /**
   * Test mit einem eindimensionalen Unterraum aus zweidimensionalen Punkten.
   */
  public static void einDimInZweiDimTest() {

    double[]  a1 = {1,1};
    double[]  a2 = {1,1};
    double[]  a3 = {2,2};
    double[]  a4 = {3,3};

    Punkt p1  = new  Punkt(a1);
    Punkt p2  = new  Punkt(a2);
    Punkt p3  = new  Punkt(a3);
    Punkt p4  = new  Punkt(a4);

    Punkt[] punkteArray = {p1,p2,p3,p4};

    ArrayList punkte = new ArrayList();
    int n = punkteArray.length;

    for(int i=0; i<n; i++){
      punkte.add(punkteArray[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.print(kh);
  }

  /**
   * Test mit einem zweidimensionalen Unterraum aus zweidimensionalen Punkten.
   */
  public static void zweiDimInZweiDimTest() {

    double[]  a1 = {1,1};
    double[]  a2 = {1,1};
    double[]  a3 = {2,2};
    double[]  a4 = {3,4};
    double[]  a5 = {-3,1};
    double[]  a6 = {-2,3};
    double[]  a7 = {-4,-4};
    double[]  a8 = {2,-2};
    double[]  a9 = {3,-5};

    Punkt p1  = new  Punkt(a1);
    Punkt p2  = new  Punkt(a2);
    Punkt p3  = new  Punkt(a3);
    Punkt p4  = new  Punkt(a4);
    Punkt p5  = new  Punkt(a5);
    Punkt p6  = new  Punkt(a6);
    Punkt p7  = new  Punkt(a7);
    Punkt p8  = new  Punkt(a8);
    Punkt p9  = new  Punkt(a9);

    Punkt[] punkteArray = {p1,p2,p3,p4,p5,p6,p7,p8,p9};

    ArrayList punkte = new ArrayList();
    int n = punkteArray.length;
    for(int i=0; i<n; i++){
      punkte.add(punkteArray[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.print(kh);
  }

  /**
   * Test mit einem nulldimensionalen Unterraum aus dreidimensionalen Punkten.
   */
  public static void nullDimInDreiDimTest() {

    double[]  a1 = {1,1,1};
    double[]  a2 = {1,1,1};

    Punkt p1  = new  Punkt(a1);
    Punkt p2  = new  Punkt(a2);

    Punkt[] punkteArray = {p1,p2};

    ArrayList punkte = new ArrayList() ;
    int n = punkteArray.length;
    for(int i=0; i<n; i++){
      punkte.add(punkteArray[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.print(kh);
  }

  /**
   * Test mit einem eindimensionalen Unterraum aus dreidimensionalen Punkten.
   */
  public static void einDimInDreiDimTest() {

    double[]  a1 = {1,1,1};
    double[]  a2 = {1,1,1};
    double[]  a3 = {2,2,2};
    double[]  a4 = {5,5,5};

    Punkt p1  = new  Punkt(a1);
    Punkt p2  = new  Punkt(a2);
    Punkt p3  = new  Punkt(a3);
    Punkt p4  = new  Punkt(a4);

    Punkt[] punkteArray = {p1,p2,p3,p4};

    ArrayList punkte = new ArrayList() ;
    int n = punkteArray.length;
    for(int i=0; i<n; i++){
      punkte.add(punkteArray[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.print(kh);
  }

  /**
   * Test mit einem zweidimensionalen Unterraum aus dreidimensionalen Punkten.
   */
  public static void zweiDimInDreiDimTest() {

    double[]  a1 = {0,0,0};
    double[]  a2 = {0,0,0};
    double[]  a3 = {1,0,1};
    double[]  a4 = {1,1,0};
    double[]  a5 = {2,1,1};

    Punkt p1  = new  Punkt(a1);
    Punkt p2  = new  Punkt(a2);
    Punkt p3  = new  Punkt(a3);
    Punkt p4  = new  Punkt(a4);
    Punkt p5  = new  Punkt(a5);

    Punkt[] punkteArray = {p1,p2,p3,p4,p5};

    ArrayList punkte = new ArrayList() ;
    int n = punkteArray.length;
    for(int i=0; i<n; i++){
      punkte.add(punkteArray[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.print(kh);
  }

  /**
   * Test mit einem dreidimensionalen Unterraum aus dreidimensionalen Punkten.
   */
  public static void dreiDimInDreiDimTest() {

    double[]  a1 = {0,0,0};
    double[]  a2 = {0,0,0};
    double[]  a3 = {0,0,1};
    double[]  a4 = {0,1,0};
    double[]  a5 = {0,1,1};
    double[]  a6 = {1,0,0};
    double[]  a7 = {1,0,1};
    double[]  a8 = {1,1,0};
    double[]  a9 = {1,1,1};

    Punkt p1  = new  Punkt(a1);
    Punkt p2  = new  Punkt(a2);
    Punkt p3  = new  Punkt(a3);
    Punkt p4  = new  Punkt(a4);
    Punkt p5  = new  Punkt(a5);
    Punkt p6  = new  Punkt(a6);
    Punkt p7  = new  Punkt(a7);
    Punkt p8  = new  Punkt(a8);
    Punkt p9  = new  Punkt(a9);

    Punkt[] punkteArray = {p1,p2,p3,p4,p5,p6,p7,p8,p9};

    ArrayList punkte = new ArrayList() ;
    int n = punkteArray.length;
    for(int i=0; i<n; i++){
      punkte.add(punkteArray[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.print(kh);
  }
}

