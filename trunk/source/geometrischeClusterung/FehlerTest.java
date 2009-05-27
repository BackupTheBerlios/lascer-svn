/*
 * Datei          : FehlerTest.java
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
 * Teste die Klassen zur Erzeugung einer konvexen Hülle mit einigen
 * vorgegebenen Datensätzen.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class FehlerTest {

  public static void main(String[] args) {

    //abstandTest();
    facettenUngleich1();
    //facettenUngleich2();
    //indexGleichMinusEins();
    //endlosSchleife();
  }

  public static void abstandTest() {

    double[] a1 = {0, 0, 0, 0, 0};
    double[] a2 = {9.0,    183.0,    24.0,    121.0,    181.0};
    double[] a3 = {155.0,    69.0,   193.0,    37.0,    55.0};
    double[] a4 = {74.0,    6.0,    84.0,    192.0,    2.0};
    double[] a5 = {177.0,    21.0,    63.0,    18.0,    75.0};
    double[] a6 = {77.0,    23.0,    159.0,    20.0,    94.0};

    Punkt referenzpunkt = new Punkt(a1);
    Punkt[] erzeug = new Punkt[5];
    erzeug[0] = new Punkt(a2);
    erzeug[1] = new Punkt(a3);
    erzeug[2] = new Punkt(a4);
    erzeug[3] = new Punkt(a5);
    erzeug[4] = new Punkt(a6);

    ArrayList erzeugendePunkte = new ArrayList(erzeug.length);
    ArrayList allePunkte = new ArrayList(erzeug.length + 1);
    for(int i = 0; i < erzeug.length; i++) {
      erzeugendePunkte.add(erzeug[i]);
      allePunkte.add(erzeug[i]);
    }
    allePunkte.add(referenzpunkt);

    Genauigkeit.setzeGrenzwerte(allePunkte);

    Facette testFacette = new Facette(referenzpunkt, erzeugendePunkte);
    for(int size=0; size<testFacette.erzeugendePunkte().size(); size++)
           System.out.println("erzeugendePunkt von altFacette " + size + "=" + testFacette.erzeugendePunkte()
                               .get(size));
    System.out.println("Enthaltener Punkt = " + testFacette.enthaltenerPunkt());
    System.out.println("Projektion = " + testFacette.projektion(referenzpunkt));
    Punkt schnittPunkt = testFacette.projektion(referenzpunkt);
    System.out.println("Abstand Projektion zu Unterraum = " + testFacette.abstand(schnittPunkt));
    System.out.println();
    Vektor diffVektor = referenzpunkt.differenzVektor(schnittPunkt);
    System.out.println("Differenzvektor Referenzpunkt zu Schnittpunkt = " + diffVektor);
    System.out.println("1 / Laege vom Differenzvektor = " + (1.0 / diffVektor.laenge()));
    System.out.println();
    System.out.println("Normalenvektor =" + testFacette.normalenVektor());
    System.out.println("Abstand zwischen referenzpunkt und der Facette = " + testFacette.abstand(referenzpunkt));
    for (int pnr = 0; pnr < erzeug.length; pnr++) {
      System.out.println("Abstand zu einer enthaltenden Punkt " + pnr + " = " + testFacette.abstand(erzeug[pnr]));
    }
  }

  /**
   * Testprogramm 1 zur Fehlermeldung "Faceten sind ungleich"
   */
  public static void facettenUngleich1() {

    double[]  a1 = {64.0 ,   175.0 ,   54.0,    24.0 ,   135.0};
    double[]  a2 = {77.0 ,   23.0 ,   159.0 ,   20.0 ,   94.0 };
    double[]  a3 = {9.0  ,  183.0  ,  24.0  ,  121.0 ,   181.0  };
    double[]  a4 = {182.0 ,   36.0 ,   86.0  ,  195.0 ,   121.0  };
    double[]  a5 = {81.0 ,   182.0 ,   75.0 ,   42.0  ,  96.0  };
    double[]  a6 = {74.0,    6.0,    84.0 ,   192.0 ,   2.0  };
    double[]  a7 = {137.0  ,  12.0 ,   159.0  ,  154.0 ,   171.0 };
    double[]  a8 = {122.0  ,  69.0 ,   83.0  ,  68.0  ,  152.0  };
    double[]  a9 = {111.0   , 60.0 ,   42.0  ,  87.0 ,   43.0  };
    double[]  a10 = {136.0  ,  158.0,    142.0  ,  101.0  ,  39.0};
    double[]  a11 = {8.0  ,  136.0  ,  192.0  ,  12.0   , 87.0  };
    double[]  a12 = {151.0  ,  41.0  ,  64.0 ,   189.0  ,  131.0 };
    double[]  a13 = {112.0  ,  197.0  ,  51.0  ,  35.0  ,  88.0  };
    double[]  a14 = {175.0  ,  170.0  ,  183.0  ,  79.0 ,   151.0};
    double[]  a15 = {123.0  ,  45.0  ,  182.0  ,  158.0 ,   37.0  };
    double[]  a16 = {28.0  ,  199.0  ,  51.0  ,  17.0   , 59.0 } ;
    double[]  a17 = {162.0  ,  61.0 ,   174.0 ,   175.0  ,  183.0};
    double[]  a18 = {155.0  ,  69.0 ,   193.0   , 37.0  ,  55.0  };
    double[]  a19 = {177.0  ,  21.0 ,   63.0 ,   18.0  ,  75.0  };
    double[]  a20 = {125.0  ,  30.0  ,  44.0 ,   58.0  ,  168.0};

    Punkt[] p = new  Punkt[21];

    p[1]  = new  Punkt(a1);
    p[2]  = new  Punkt(a2);
    p[3]  = new  Punkt(a3);
    p[4]  = new  Punkt(a4);
    p[5]  = new  Punkt(a5);
    p[6]  = new  Punkt(a6);
    p[7]  = new  Punkt(a7);
    p[8]  = new  Punkt(a8);
    p[9]  = new  Punkt(a9);

    p[10]  = new  Punkt(a10);
    p[11]  = new  Punkt(a11);
    p[12]  = new  Punkt(a12);
    p[13]  = new  Punkt(a13);
    p[14]  = new  Punkt(a14);
    p[15]  = new  Punkt(a15);
    p[16]  = new  Punkt(a16);
    p[17]  = new  Punkt(a17);
    p[18]  = new  Punkt(a18);
    p[19]  = new  Punkt(a19);

    p[20]  = new  Punkt(a20);

    ArrayList punkte = new ArrayList();
    for(int i=1; i<21; i++){
      punkte.add(p[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.println();
    System.out.println();
    System.out.println("Ausgabe der Methode facettenUngleich1");
    System.out.println("Anzahl der Facetten = " + kh.facetten().size());
  }

  /**
   * Testprogramm 2 zur Fehlermeldung "Faceten sind ungleich"
   */
  public static void facettenUngleich2() {

    double[] a1 = {117.0 ,   175.0  ,  90.0  ,  123.0 ,   153.0 ,   46.0};
    double[] a2 = {66.0  ,  121.0   , 193.0  ,  76.0  ,  92.0   , 27.0};
    double[] a3 = {166.0 ,   135.0  ,  111.0 ,   23.0 ,   126.0 ,   70.0};
    double[] a4 = {127.0 ,   78.0   , 146.0  ,  19.0  ,  61.0   , 150.0};
    double[] a5 = {44.0  ,  157.0   , 133.0  ,  121.0 ,   152.0 ,   22.0};
    double[] a6 = {39.0  ,  138.0   , 163.0  ,  31.0  ,  186.0  ,  57.0};
    double[] a7 = {126.0 ,   74.0   , 162.0  ,  79.0  ,  94.0   , 191.0};
    double[] a8 = {198.0 ,   154.0  ,  97.0  ,  75.0  ,  21.0   , 177.0};
    double[] a9 = {106.0 ,   144.0  ,  67.0  ,  183.0 ,   1.0   , 102.0};
    double[] a10 = {19.0 ,   2.0    ,102.0   , 177.0  ,  160.0  ,  122.0};
    double[] a11 = {142.0,    78.0  ,  192.0 ,   29.0 ,   47.0  ,  26.0};
    double[] a12 = {77.0 ,   4.0    ,2.0   , 137.0   , 157.0   , 175.0};
    double[] a13 = {97.0 ,   15.0   , 75.0 ,   178.0  ,  149.0 ,   196.0};
    double[] a14 = {116.0,    87.0  ,  78.0,    109.0 ,   179.0 ,   42.0};
    double[] a15 = {32.0 ,   103.0  ,  57.0,    21.0  ,  65.0   , 144.0};
    double[] a16 = {80.0 ,   20.0   , 117.0,    137.0 ,   175.0 ,   156.0};
    double[] a17 = {6.0  ,  168.0   , 147.0 ,   148.0 ,   70.0  ,  137.0};
    double[] a18 = {169.0,    71.0  ,  32.0  ,  84.0  ,  135.0  ,  136.0};
    double[] a19 = {110.0,    152.0 ,  125.0 ,  149.0 ,   122.0 ,   196.0};
    double[] a20 = {31.0 ,   171.0  ,  156.0 , 172.0  ,  148.0  ,  128.0};

    Punkt[] p = new  Punkt[21];

    p[1]  = new  Punkt(a1);
    p[2]  = new  Punkt(a2);
    p[3]  = new  Punkt(a3);
    p[4]  = new  Punkt(a4);
    p[5]  = new  Punkt(a5);
    p[6]  = new  Punkt(a6);
    p[7]  = new  Punkt(a7);
    p[8]  = new  Punkt(a8);
    p[9]  = new  Punkt(a9);
    p[10]  = new  Punkt(a10);
    p[11]  = new  Punkt(a11);
    p[12]  = new  Punkt(a12);
    p[13]  = new  Punkt(a13);
    p[14]  = new  Punkt(a14);
    p[15]  = new  Punkt(a15);
    p[16]  = new  Punkt(a16);
    p[17]  = new  Punkt(a17);
    p[18]  = new  Punkt(a18);
    p[19]  = new  Punkt(a19);
    p[20]  = new  Punkt(a20);

    ArrayList punkte = new ArrayList();
    for(int i=1; i<21; i++){
      punkte.add(p[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.println();
    System.out.println();
    System.out.println("Ausgabe der Methode facettenUngleich2");
    System.out.println("Anzahl der Facetten = " + kh.facetten().size());
  }

  /**
   * Testprogramm zum Fehler "index == -1".
   */
  public static void indexGleichMinusEins() {

    double[] a1= {153.32709682632034 ,   172.95130799778767 ,  9.601814738289915  ,  108.0705369328382  };
    double[] a2= {198.73299616014745  ,  48.82135427925809   ,  112.78831347677927 ,   179.6025723558459  };
    double[] a3= {174.2990135609494   , 10.16288667470917    , 142.04577806981854  ,  114.82855557310636  };
    double[] a4= {44.62559334474301   , 126.8798390748793    ,54.11733278307405    , 14.760321956807276  };
    double[] a5= {113.33639655426435  ,  91.88361467093245   , 168.65831370501223  ,  186.40569670626255  };
    double[] a6= {180.14286232008533  ,  135.71710118227747  ,  78.77463557359256  ,  64.9594395437484  };
    double[] a7= {17.247988685921946  ,  180.0933648977092   , 69.18459790845921   , 152.57307414113313  };
    double[] a8= {28.425462951219593  ,  21.273143778708725  ,  54.65679826174035  ,  92.85031211235923  };
    double[] a9= {16.805636741659914  ,  17.212922585056045  ,  67.19699915391047  ,  161.34758591201393  };
    double[] a10= {6.345176839135314  ,  41.673382611883156  ,  192.52716372492728 ,   119.74210159320515  };
    double[] a11= {37.1525024648832   , 163.985668858889     , 184.7202306492259   , 154.96253691737675  };
    double[] a12= {186.2864117582115  ,  14.930849036580529  ,  172.42440732569128 ,   104.46035271870315  };
    double[] a13= { 153.3406880750783  ,  107.19565703254554  ,  110.93117044760305 ,   154.93255735863602  };
    double[] a14= {194.072713086436   , 93.12547892100353    ,  131.41143801651802 ,   94.18946886670571  };
    double[] a15= {185.52946372638633 ,   63.00337944774945  ,  91.96465377879952  ,  194.49395034246345  };
    double[] a16= {162.56956693180672 ,   113.84166791127987 ,   44.69591122239023 ,   110.58129541495411  };
    double[] a17= {178.37542221540667 ,   151.6804608602558  ,  198.85649231516905  ,  186.7018262726169  };
    double[] a18= {10.494693976644488 ,   29.64539647710438  ,  82.15353855295808  ,  84.41107770781294  };
    double[] a19= {59.14576778307128  ,  69.52451213023814   , 180.65490430681666  ,  66.46519166744275  };
    double[] a20= {165.9671500356645  ,  166.38214247983535  ,  172.28548742964182 ,   2.062034659663503  };
    double[] a21= {19.08600322490046  , 33.7354174655581     , 159.73170417800858  ,  194.801306815649  };
    double[] a22= {104.3264099977945  ,  111.07686233429467  ,  55.64112571501576  ,  77.11977188300038  };
    double[] a23= { 12.573238545757782 ,   26.03431132972156  ,  76.3244191278645   , 63.59049927801912  };
    double[] a24= { 119.75274071677259 ,   80.21725999570238  ,  159.73734929548812  ,  10.357836460369851  };
    double[] a25= { 18.58644050771101  ,  10.806466108596947  ,  39.34060235131418 ,   189.11273125638667  };
    double[] a26= { 0.22008869434639866,    69.20159951102028 ,   77.5057193924197  ,  51.39561787668465  };
    double[] a27= { 159.02478521235614 ,   53.54934515758409  ,  190.4556341029834   , 132.6471748927651  };
    double[] a28= {  125.31274490773792 ,   133.73234742136125 ,   81.2986907660636   , 25.309862958383466  };
    double[] a29= { 18.405413694701522 ,   115.4071153389991  ,  138.5352161449984  ,  36.415993682173344  };
    double[] a30= { 156.4370141153121  ,  3.93682773904942    ,154.48620378106816   , 137.48106149003326  };
    double[] a31= {  159.47185846821418 ,   149.5728066161379  ,  71.41381056823852  ,  113.15360939402599  };
    double[] a32= { 138.95916440315906 ,   81.99621303888986  ,  72.6738007985199   , 19.95594800310141  };
    double[] a33= {  79.24431731848682  ,  175.33939940273905  , 102.06030750418032  ,  177.58275252340798  };
    double[] a34= { 85.80090069780427  ,  124.06403289711743  ,  116.57363076035226  ,  132.60735405575406  };
    double[] a35= { 100.04312126128212 ,   55.581100249998585 ,   56.16212950349579  ,  158.0784549676972  };
    double[] a36= { 162.0235798932291  ,  157.2555157670029   , 48.559489020051096   , 82.37267586991675  };
    double[] a37= {  140.0165625316003  ,  153.44604931695403  ,  158.15906156889608  ,  176.80129636090018  };
    double[] a38= { 39.93249625403455  ,  32.488961915862944  ,  34.937224993045014   , 73.85221697642812  };
    double[] a39= { 157.70438034569423 ,   94.04166507209423  ,  122.75802504919602  ,  36.581141737318305  };
    double[] a40= {  44.23238768770421  ,  63.96041433862063   , 45.96171385454302   , 154.5486334816199  };
    double[] a41= { 115.16814033813594 ,  106.89396652955998  ,  8.960062030349402  ,  2.8326298028457986 };
    double[] a42= {  156.76547813675555 ,   56.0328953081201   , 115.63166674367868  ,  168.02875203696627  };
    double[] a43= { 154.21976518272314 ,   74.96203227419198  ,  43.63077674263644  ,  18.78360329191704  };
    double[] a44= {  0.3633035412828667 ,   68.32143524420724  ,  93.29350401393025  ,  178.28898365478793 };
    double[] a45= { 130.27943590805305 ,   65.98890826658528  ,  71.64549316917275  ,  142.29784350641665 };
    double[] a46= {  171.34353976513268 ,   119.75515113096702 ,   169.08560920204266  ,  178.60561388246674  };
  //  double[] a47= { 98  ,  171  , 139  ,  42  };
    double[] a47= { 98.39416761015111  ,  171.9701698760062   , 139.6403470627149  ,  42.57323643957711  };
    double[] a48= {  140.18310824842203 ,   82.39762649610338  ,  105.25409437810592 ,   148.15122377059456  };
    double[] a49= { 195.94826864216938 ,  132.5214337410537   , 170.40061178906734  ,  44.99671022943519  };
    double[] a50= { 189.89879119068496 ,  65.80684571150417   , 155.37146756470517  ,  169.65842230973223  };

    Punkt[] p = new  Punkt[51];

    p[1]  = new  Punkt(a1);
    p[2]  = new  Punkt(a2);
    p[3]  = new  Punkt(a3);
    p[4]  = new  Punkt(a4);
    p[5]  = new  Punkt(a5);
    p[6]  = new  Punkt(a6);
    p[7]  = new  Punkt(a7);
    p[8]  = new  Punkt(a8);
    p[9]  = new  Punkt(a9);

    p[10]  = new  Punkt(a10);
    p[11]  = new  Punkt(a11);
    p[12]  = new  Punkt(a12);
    p[13]  = new  Punkt(a13);
    p[14]  = new  Punkt(a14);
    p[15]  = new  Punkt(a15);
    p[16]  = new  Punkt(a16);
    p[17]  = new  Punkt(a17);
    p[18]  = new  Punkt(a18);
    p[19]  = new  Punkt(a19);

    p[20]  = new  Punkt(a20);
    p[21]  = new  Punkt(a21);
    p[22]  = new  Punkt(a22);
    p[23]  = new  Punkt(a23);
    p[24]  = new  Punkt(a24);
    p[25]  = new  Punkt(a25);
    p[26]  = new  Punkt(a26);
    p[27]  = new  Punkt(a27);
    p[28]  = new  Punkt(a28);
    p[29]  = new  Punkt(a29);

    p[30]  = new  Punkt(a30);
    p[31]  = new  Punkt(a31);
    p[32]  = new  Punkt(a32);
    p[33]  = new  Punkt(a33);
    p[34]  = new  Punkt(a34);
    p[35]  = new  Punkt(a35);
    p[36]  = new  Punkt(a36);
    p[37]  = new  Punkt(a37);
    p[38]  = new  Punkt(a38);
    p[39]  = new  Punkt(a39);

    p[40]  = new  Punkt(a40);
    p[41]  = new  Punkt(a41);
    p[42]  = new  Punkt(a42);
    p[43]  = new  Punkt(a43);
    p[44]  = new  Punkt(a44);
    p[45]  = new  Punkt(a45);
    p[46]  = new  Punkt(a46);
    p[47]  = new  Punkt(a47);
    p[48]  = new  Punkt(a48);
    p[49]  = new  Punkt(a49);

    p[50]  = new  Punkt(a50);

    ArrayList punkte = new ArrayList();
    for(int i=1; i<51; i++){
      punkte.add(p[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.println();
    System.out.println();
    System.out.println("Ausgabe der Methode indexGleichMinusEins");
    System.out.println("Anzahl der Facetten = " + kh.facetten().size());
  }

  /**
   * Methode zur Dokumentation des endlosen Laufs des Programms.
   */
  public static void endlosSchleife() {

    double[]  a1 = {174.0,    130.0,    47.0,    77.0};
    double[]  a2 = {121.0,    130.0,    148.0,    136.0};
    double[]  a3 = {106.0,    145.0,    20.0,    108.0};
    double[]  a4 = {0.0,    156.0,    17.0,    136.0};
    double[]  a5 = {163.0,    145.0,    96.0,    180.0};
    double[]  a6 = {89.0,    7.0,    37.0,    166.0};
    double[]  a7 = {44.0,    164.0,    178.0,    97.0};
    double[]  a8 = {109.0,    197.0,    147.0,    45.0};
    double[]  a9 = {46.0,    15.0,    79.0,    44.0};
    double[]  a10 = {38.0,    30.0,    158.0,    92.0};
    double[]  a11 = {77.0,    13.0,    80.0,    33.0};
    double[]  a12 = {1.0,    140.0,    139.0,    170.0};
    double[]  a13 = {56.0,    36.0,    173.0,    55.0};
    double[]  a14 = {48.0,    53.0,    75.0,    54.0};
    double[]  a15 = {183.0,    56.0,    37.0,    151.0};
    double[]  a16 = {55.0,    32.0,    186.0,    58.0};
    double[]  a17 = {26.0,    137.0,    44.0,    37.0};
    double[]  a18 = {183.0,    138.0,    113.0,    93.0};
    double[]  a19 = {189.0,    134.0,    45.0,    6.0};
    double[]  a20 = {30.0,    50.0,    50.0,    193.0};
    double[]  a21 = {22.0,    198.0,    139.0,    46.0};
    double[]  a22 = {7.0,    77.0,    155.0,    184.0};
    double[]  a23 = {38.0,    14.0,    53.0,    186.0};
    double[]  a24 = {182.0,    28.0,    188.0,    17.0};
    double[]  a25 = {179.0,    171.0,    59.0,    127.0};
    double[]  a26 = {19.0,    43.0,    27.0,    167.0};
    double[]  a27 = {121.0,    109.0,    149.0,    47.0};
    double[]  a28 = {151.0,    170.0,    117.0,    97.0};
    double[]  a29 = {43.0,    119.0,    86.0,    153.0};
    double[]  a30 = {124.0,    86.0,    174.0,    84.0};
    double[]  a31 = {153.0,    157.0,    164.0,    170.0};
    double[]  a32 = {138.0,    44.0,    129.0,    141.0};
    double[]  a33 = {109.0,    54.0,    184.0,    26.0};
    double[]  a34 = {33.0,    154.0,    125.0,    196.0};
    double[]  a35 = {105.0,    156.0,    189.0,    40.0};
    double[]  a36 = {48.0,    163.0,    39.0,    167.0};
    double[]  a37 = {75.0,    101.0,    159.0,    158.0};
    double[]  a38 = {86.0,    157.0,    58.0,    17.0};
    double[]  a39 = {91.0,    62.0,    110.0,    52.0};
    double[]  a40 = {125.0,    48.0,    77.0,    107.0};
    double[]  a41 = {89.0,    56.0,    97.0,    124.0};
    double[]  a42 = {10.0,    160.0,    144.0,    34.0};
    double[]  a43 = {113.0,    150.0,    64.0,    133.0};
    double[]  a44 = {155.0,    84.0,    23.0,    174.0};
    double[]  a45 = {108.0,    67.0,    153.0,    184.0};
    double[]  a46 = {102.0,    17.0,    197.0,    25.0};
    double[]  a47 = {152.0,    127.0,    99.0,    94.0};
    double[]  a48 = {155.0,    53.0,    84.0,    61.0};
    double[]  a49 = {54.0,    114.0,    23.0,    184.0};
    double[]  a50 = {145.0,    137.0,    97.0,    84.0};

    Punkt[] p = new  Punkt[51];

    p[1]  = new  Punkt(a1);
    p[2]  = new  Punkt(a2);
    p[3]  = new  Punkt(a3);
    p[4]  = new  Punkt(a4);
    p[5]  = new  Punkt(a5);
    p[6]  = new  Punkt(a6);
    p[7]  = new  Punkt(a7);
    p[8]  = new  Punkt(a8);
    p[9]  = new  Punkt(a9);

    p[10]  = new  Punkt(a10);
    p[11]  = new  Punkt(a11);
    p[12]  = new  Punkt(a12);
    p[13]  = new  Punkt(a13);
    p[14]  = new  Punkt(a14);
    p[15]  = new  Punkt(a15);
    p[16]  = new  Punkt(a16);
    p[17]  = new  Punkt(a17);
    p[18]  = new  Punkt(a18);
    p[19]  = new  Punkt(a19);

    p[20]  = new  Punkt(a20);
    p[21]  = new  Punkt(a21);
    p[22]  = new  Punkt(a22);
    p[23]  = new  Punkt(a23);
    p[24]  = new  Punkt(a24);
    p[25]  = new  Punkt(a25);
    p[26]  = new  Punkt(a26);
    p[27]  = new  Punkt(a27);
    p[28]  = new  Punkt(a28);
    p[29]  = new  Punkt(a29);

    p[30]  = new  Punkt(a30);
    p[31]  = new  Punkt(a31);
    p[32]  = new  Punkt(a32);
    p[33]  = new  Punkt(a33);
    p[34]  = new  Punkt(a34);
    p[35]  = new  Punkt(a35);
    p[36]  = new  Punkt(a36);
    p[37]  = new  Punkt(a37);
    p[38]  = new  Punkt(a38);
    p[39]  = new  Punkt(a39);

    p[40]  = new  Punkt(a40);
    p[41]  = new  Punkt(a41);
    p[42]  = new  Punkt(a42);
    p[43]  = new  Punkt(a43);
    p[44]  = new  Punkt(a44);
    p[45]  = new  Punkt(a45);
    p[46]  = new  Punkt(a46);
    p[47]  = new  Punkt(a47);
    p[48]  = new  Punkt(a48);
    p[49]  = new  Punkt(a49);

    p[50]  = new  Punkt(a50);

    ArrayList punkte = new ArrayList();
    for(int i=1; i<51; i++){
      punkte.add(p[i]);
    }

    KonvexeHuelle kh = new KonvexeHuelle(punkte);
    System.out.println();
    System.out.println();
    System.out.println("Ausgabe der Methode endlosSchleife");
    System.out.println("Anzahl der Facetten = " + kh.facetten().size());
  }
}

