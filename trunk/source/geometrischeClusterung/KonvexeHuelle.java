/*
 * Datei          : KonvexeHuelle.java
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
 * Die Klasse repraesentiert eine konvexe Huelle von Punkten in einem
 * Unterraum eines mehrdimensionalen Raums.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class KonvexeHuelle {

  /**
   * Ein Punkt, der in der konvexen Huelle enthalten ist, d.h. im Unterraum
   * enthalten ist und unterhalb jeder der Hyperebenen der konvenxen Huelle
   * liegt.
   */
  private Punkt enthaltenerPunkt;

  /**
   * Der Unterraum, in dem die konvexe Huelle enthalten ist.
   */
  private Unterraum unterraum;

  /**
   * Eine Liste von Elementen vom Typ <CODE>facetten</CODE>, deren
   * Schnitte mit dem Unterraum in diesem alle gegebenen Punkte
   * umschliessen.
   * @associates <{geometrischeClusterung.Facette}>
   * @supplierCardinality 0..*
   */
  private ArrayList facetten;

  /**
   * Erzeugt eine konvexe Hülle aus der uebergebenen Liste von Punkten.<P>
   *
   * Da die Grenzwerte in der Klasse Genauigkeit static-Attribute sind, wird
   * die gesamte Ausführung synchronisiert. Der Konstruktor wird also nicht
   * von zwei Thread gleichzeitig durchlaufen.
   *
   * @param punkte  Eine Liste von Elementen vom Typ <CODE>Punkt</CODE>,
   *                zu denen die konvexe Huelle erzeugt werden soll.
   */
  public KonvexeHuelle(ArrayList punkte) {
    this(punkte, 0);
  }

  /**
   * Erzeugt eine konvexe Hülle aus einer Teilmenge der uebergebenen Punkten,
   * indem die Anzahl der Facetten, aus denen die konvexe Hülle besteht,
   * beschränkt ist. Die tatsächliche Anzahl kann kleiner oder auch etwas
   * größer sein. Der Unterraum der erzeugten konvexen Hülle stimmt aber
   * mit dem Unterraum der konvexen Hülle aller Punkte überein.<P>
   *
   * Da die Grenzwerte in der Klasse Genauigkeit static-Attribute sind, wird
   * die gesamte Ausführung synchronisiert. Der Konstruktor wird also nicht
   * von zwei Thread gleichzeitig durchlaufen.
   *
   * @param punkte  Eine Liste von Elementen vom Typ <CODE>Punkt</CODE>,
   *                zu denen die konvexe Huelle erzeugt werden soll.
   * @param maxFacettenAnz  Die Anzahl der Facetten, aus denen die konvexe
   *                        Hülle maximal bestehen sollte. Ist der Wert Null,
   *                        ist die Anzahl unbeschränkt.
   */
  public KonvexeHuelle(ArrayList punkte, int maxFacettenAnz) {

    synchronized (getClass()) {

      // Erzeugen der passenden Konstanten
      Genauigkeit.setzeGrenzwerte(punkte);

      // Erzeugen der initialen Punkte
      ArrayList initialePunkte = initialePunktMenge(punkte);

      // Enthaltenen Punkt erzeugen
      this.enthaltenerPunkt = Punkt.schwerpunkt(initialePunkte);

      // Unterraum erzeugen, in dem die konvexe Huelle liegt
      this.unterraum = new Unterraum(initialePunkte);

      // Facetten erzeugen
      if(unterraum.dimensionUnterraum() == 0) {

        this.facetten = new ArrayList(0);

      } else if (unterraum.dimensionUnterraum() == 1) {

        int punkteAnz = initialePunkte.size();
        this.facetten = new ArrayList(punkteAnz);
        for(int i = 0; i < punkteAnz; i++){
          ArrayList erzeugendePunkt = new ArrayList(1);
          erzeugendePunkt.add(initialePunkte.get(i));
          this.facetten.add(new Facette(enthaltenerPunkt, erzeugendePunkt));
        }

      } else {

        ArrayList initialeFacetten = initialeFacetten(initialePunkte, punkte);
        this.facetten = erweiterung(initialeFacetten, maxFacettenAnz);

      }
    }
  }

  /**
   * Erzeugt eine Menge von Punkten, deren Raum die gleiche Dimension hat
   * wie die Menge aller Punkte und die einen möglichst großen Abstand
   * untereinander haben. Aus ihnen kann eine intiale konvexe Hülle erzeugt
   * werden.
   *
   * @param punkte  Eine Liste von Elementen vom Typ <CODE>Punkt</CODE>,
   *                zu denen die konvexe Huelle erzeugt werden soll.
   * @return  Eine Menge von Punkten, aus denen eine initiale konvexe Hülle
   *          erzeugt werden kann.
   */
  private ArrayList initialePunktMenge(ArrayList punkte) {

    Punkt bezugspunkt  = Punkt.schwerpunkt(punkte);

    // Berechnen des Anfangspunktes, der den groeßten Abstand zu bezugspunkt hat
    Punkt anfangsPunkt = bezugspunkt.punktMitMaximalemAbstand(punkte);
    ArrayList erzeugendePunkte = new ArrayList();
    erzeugendePunkte.add(anfangsPunkt);

    Unterraum extendRaum = new Unterraum(erzeugendePunkte);

    Punkt weitererPunkt = extendRaum.punktMitMaximalemAbstand(punkte);

    while(! extendRaum.enthaelt(weitererPunkt)) {
      erzeugendePunkte.add(weitererPunkt);
      extendRaum = new Unterraum(extendRaum, weitererPunkt);
      weitererPunkt = extendRaum.punktMitMaximalemAbstand(punkte);
    }

    return erzeugendePunkte;
  }

  /**
   * Ermittelt die initialen Facetten.
   *
   * @param initialePunkte  Die Punkte, die die initiale konvexe Huelle erzeugen
   * @param gesamtPunkte  Alle Punkte der konvexen Huelle
   * @return  Die Facetten der initialen konvex Huelle
   */
  private ArrayList initialeFacetten(ArrayList initialePunkte, ArrayList gesamtPunkte) {

    int initPunktAnz = initialePunkte.size();
    ArrayList initialeFacetten = new ArrayList(initPunktAnz);

    // In alleGrenzEbenen wird zu jedem initialen Punkt die aus den anderen
    // Punkten erzeugte Facette gespeichert, um diese spaeter als angrenzende
    // Facetten zu verwenden
    HashMap alleGrenzEbenen = new HashMap(initPunktAnz);

    for (int i = 0; i < initPunktAnz; i++) {
      // Erzeugen einer Teilmenge der Menge der erzeugenden Punkte
      ArrayList erzeugendePunkte = (ArrayList)initialePunkte.clone();
      Punkt delPunkt = (Punkt)erzeugendePunkte.get(i);
      erzeugendePunkte.remove(i);

      // Erzeugen der Facetten aus den Punkten
      Facette newFacette = new Facette(enthaltenerPunkt,erzeugendePunkte);
      initialeFacetten.add(newFacette);
      alleGrenzEbenen.put(delPunkt,newFacette);
    }

    // Die angrenzenden Ebenen fuer jede initialen Facette erzeugen
    for (int i = 0; i < initPunktAnz; i++) {
      Facette geholtFacette = (Facette)initialeFacetten.get(i);
      for (int j = 0; j < initPunktAnz - 1; j++) {
        Punkt geholtPunkt = (Punkt)geholtFacette.erzeugendePunkte().get(j);
        Facette angrenzendeFacette = (Facette)alleGrenzEbenen.get(geholtPunkt);
        geholtFacette.angrenzendeFacetteHinzufuegen(geholtPunkt,angrenzendeFacette);
      }
    }

    // Die über den Facetten liegenden Punkte ermitteln
    int gesamtPunktAnz = gesamtPunkte.size();
    for (int i = 0; i < initPunktAnz; i++){
      Facette bezugsFacette = (Facette)initialeFacetten.get(i);
      for (int j = 0; j < gesamtPunktAnz; j++) {

        // Wenn ein Punkt über einer Facetten liegt, muss diese Facette
        // unter dem Punkt gespeichert werden
        Punkt aPunkt = (Punkt)gesamtPunkte.get(j);
        if (bezugsFacette.oberhalbPunktHinzufuegen(aPunkt)) {
          if (! aPunkt.unterhalbFacetteHinzufuegen(bezugsFacette)) {
            throw new RuntimeException("Facette schon in Menge vorhanden");
          }
        }
      }
    }
    return initialeFacetten;
  }

  /**
   * Erweitert die konvexe Hülle, die aus den übergebenen Facetten besteht,
   * um die übergebenen Punkte.
   *
   * @param initialeFacetten  Die initialen Facetten, die eine konvexe Hülle
   *                          für eine Teilmenge der Punkte bilden.
   * @param maxFacettenAnz  Die Anzahl der Facetten, aus denen die konvexe
   *                        Hülle maximal bestehen soll. Ist der Wert Null,
   *                        ist die Anzahl unbeschränkt.
   * @return  Die Facetten, die die konvexe Hülle bilden
   */
  private ArrayList erweiterung(ArrayList initialeFacetten, int maxFacettenAnz) {

    HashSet aktuelleFacetten;
    if (maxFacettenAnz == 0) {
      aktuelleFacetten = new HashSet();
    } else {
      aktuelleFacetten = new HashSet(maxFacettenAnz);
    }
    aktuelleFacetten.addAll(initialeFacetten);
    LinkedList alleFacetten = new LinkedList(initialeFacetten);

    Punkt weitererPunkt = naechsterPunkt(aktuelleFacetten, alleFacetten);
    while ((weitererPunkt != null)
      && ((maxFacettenAnz == 0) || (aktuelleFacetten.size() < maxFacettenAnz))) {
      addWeiterenPunkt(aktuelleFacetten, alleFacetten, weitererPunkt);
      weitererPunkt = naechsterPunkt(aktuelleFacetten, alleFacetten);
    }

    return (new ArrayList(aktuelleFacetten));
  }

  /**
   * Liefert den Punkt, der oberhalb der nächsten Facette liegt und unter
   * allen oberhalb von ihr liegenden Punkten den größten Abstand hat. Die
   * Facetten werden dabei in der Reihenfolge ihrer Erzeugung überprüft. Wenn
   * es keinen oberhalb einer Facette liegenden Punkt gibt, wird
   * <CODE>null</CODE> zurückgeliefert.
   *
   * @param aktuelleFacetten  Die Facetten, aus denen die konvexe Huelle aktuell
   *                          besteht
   * @param alleFacetten  Eine Liste von Facetten, zu der alle neuen Facetten
   *                      hinzugefügt wurden.
   * @return  Einen Punkt, der oberhalb einer Facette aus der übergebenen
   *          Menge liegt
   */
  private Punkt naechsterPunkt(HashSet aktuelleFacetten, LinkedList alleFacetten) {

    while (! alleFacetten.isEmpty()) {
      Facette nextFacette = (Facette)alleFacetten.removeFirst();
      if (aktuelleFacetten.contains(nextFacette)) {
        Punkt nextPunkt = nextFacette.punktMitMaximalemAbstand(nextFacette.oberhalbPunkte());
        if (nextPunkt != null) {
          return nextPunkt;
        }
      }
    }
    return null;
  }

  /**
   * Hinzufügen eines weiteren Punktes zu der bisher entstandenen konvexen
   * Hülle. Nach dem Hinzufügen ist wieder eine konvexe Hülle vorhanden.
   *
   * @param aktuelleFacetten  Die Facetten, aus denen die konvexe Huelle aktuell
   *                          besteht
   * @param alleFacetten  Eine Liste von Facetten, zu der alle neuen Facetten
   *                      hinzuzufügen sind.
   * @param weitererPunkt  Ein weiterer Punkt, der zu der konven Huelle hinzugefuegt
   *                       werden soll
   */
  private void addWeiterenPunkt(HashSet aktuelleFacetten, LinkedList alleFacetten,
                                Punkt weitererPunkt) {

    // Da innerhalb der folgenden Schleife die unterhalbFacetten von Punkten
    // verändert werden, wird für die Schleifendurchlauf eine Kopie der
    // unterhalbFacetten des übergebenen Punktes erstellt.
    Collection unterhalbFacetten = (Collection)weitererPunkt.unterhalbFacetten().clone();
    ArrayList alleNewFacetten = new ArrayList();

    // Erzeugen der neuen Facetten zu einer unter dem Punkt liegenen Facette
    Iterator ebenenIter = unterhalbFacetten.iterator();
    while (ebenenIter.hasNext()) {
      Facette altFacette = (Facette)ebenenIter.next();
      ArrayList erzeugendePunkte = altFacette.erzeugendePunkte();

      int punktAnz = erzeugendePunkte.size();

      // In neueGrenzFacetten werden angrenzenden Facetten gespeichert,
      // um diese nachher den jeweils anderen erzeugten Facetten hinzuzufügen
      HashMap neueGrenzFacetten = new HashMap(punktAnz);
      Facette[] neueFacetten = new Facette[punktAnz];

      // ersetztePunkte sind alle Punkte, die jeweils aus erzeugendePunkte der
      // altFacette gelöscht werden. Das sind auch die Punkte, die als Referenzpunkt
      // für die neu erzeugte Facette benutzt worden sind.
      Punkt[] ersetztePunkte = new Punkt[punktAnz];
      for(int pnr = 0; pnr < punktAnz; pnr++) {
        ArrayList neueErzeugendePunkte = (ArrayList)erzeugendePunkte.clone();

        // Erzeugende Punkt der neuen Facetten ermitteln
        ersetztePunkte[pnr] = (Punkt)neueErzeugendePunkte.get(pnr);
        neueErzeugendePunkte.remove(pnr);
        neueErzeugendePunkte.add(weitererPunkt);

        // Erzeugen einer neuen Facetten
        neueFacetten[pnr] = new Facette(ersetztePunkte[pnr], neueErzeugendePunkte);
        neueGrenzFacetten.put(ersetztePunkte[pnr], neueFacetten[pnr]);
        alleNewFacetten.add(neueFacetten[pnr]);
      }

      // Speichern der angenzenden Facetten zu den neu erzeugten Facetten
      for(int fnr = 0; fnr < punktAnz; fnr++) {
        Facette neueFacette = (Facette)neueFacetten[fnr];
        Facette angrenzendeFacette;

        // Zu jedem Punkt der neuen Facette, außer zu weitererPunkt, wird die
        // Facette als angrenzende Facette gespeichert, die diesen Punkt nicht
        // enthält
        for(int pnr = 0; pnr < punktAnz; pnr++) {
          Punkt erzeugenderPunkt = (Punkt)(neueFacette.erzeugendePunkte().get(pnr));
          if(erzeugenderPunkt != weitererPunkt) {
            angrenzendeFacette = (Facette)neueGrenzFacetten.get(erzeugenderPunkt);
            neueFacette.angrenzendeFacetteHinzufuegen(erzeugenderPunkt, angrenzendeFacette);
          }
        }

        // Zu weitererPunkt wird die in altFacette angrenzende Facette gespeichert
        angrenzendeFacette = altFacette.angrenzendeFacette(ersetztePunkte[fnr]);
        neueFacette.angrenzendeFacetteHinzufuegen(weitererPunkt, angrenzendeFacette);
      }

      // Hinzufügen aller Punkte zu oberhalbPunkte der neuen Facetten und
      // Hinzufügen der neuen Facetten zu den unterhalbFacetten der
      // entsprechenden Punkte
      for(int fnr = 0; fnr < punktAnz; fnr++) {
        Facette neueFacette = (Facette)neueFacetten[fnr];

        // Iteration über die Punkte, die über der angrenzenden Facette liegen
        Facette altNachbarFacette = neueFacette.angrenzendeFacette(weitererPunkt);
        Iterator nachbarIter = altNachbarFacette.oberhalbPunkte().iterator();
        while(nachbarIter.hasNext()) {
          Punkt oberhalbPunkt = (Punkt)nachbarIter.next();
          if(neueFacette.oberhalbPunktHinzufuegen(oberhalbPunkt)){
            if (! oberhalbPunkt.unterhalbFacetteHinzufuegen(neueFacette)) {
              throw new RuntimeException("Facette schon in Menge vorhanden");
            }
          }
        }

        // Iteration über die Punkte, die über altFacette liegen
        Iterator altIter = altFacette.oberhalbPunkte().iterator();
        while(altIter.hasNext()) {
          Punkt oberhalbPunkt = (Punkt)altIter.next();
          if(neueFacette.oberhalbPunktHinzufuegen(oberhalbPunkt)){
            if (! oberhalbPunkt.unterhalbFacetteHinzufuegen(neueFacette)) {
              throw new RuntimeException("Facette schon in Menge vorhanden");
            }
          }
        }
      }

      // Bevor altFacette aus der Gesamtmenge der Facetten gelöscht wird,
      // wird sie aus unterhalbFacetten ihrer oberhalbPunkte gelöscht
      Iterator punktIter = altFacette.oberhalbPunkte().iterator();
      while(punktIter.hasNext()) {
        if (! ((Punkt)punktIter.next()).unterhalbFacetteLoeschen(altFacette)) {
          throw new RuntimeException("Facette nicht in Menge vorhanden");
        }
      }

      // Löschen der altFacette aus der Gesamtmenge der Facetten
      aktuelleFacetten.remove(altFacette);
    }

    // ersteFacetten enthält alle Facetten aus alleNewFacetten, zu denen
    // es (noch) keine gleiche Facetten in ersteFacetten gibt, d.h zu zwei
    // gleichen Facetten aus alleNewFacetten enthält sie jeweils eine.
    HashMap ersteFacetten = new HashMap();

    // zweiteFacetten enthält alle zweiten Facetten aus alleNewFacetten,
    // zu denen eine gleiche Facette in ersteFacetten gespeichert ist.
    ArrayList zweiteFacetten = new ArrayList();

    for(int fnr=0; fnr < alleNewFacetten.size(); fnr++) {
      Facette neueFacette = (Facette)alleNewFacetten.get(fnr);
      if(! ersteFacetten.containsKey(neueFacette)) {
        ersteFacetten.put(neueFacette, neueFacette);
      }
      else {
        zweiteFacetten.add(neueFacette);
      }
    }

    // Von den doppelten Facetten ist jeweils ein Exemplar in zweiteFacetten
    // gespeichert. Das andere befindet sich in ersteFacetten.
    Iterator zweiteFacettenIter = zweiteFacetten.iterator();
    while (zweiteFacettenIter.hasNext()) {
      Facette facette1 = (Facette)zweiteFacettenIter.next();
      Facette facette2 = (Facette)ersteFacetten.remove(facette1);

      ArrayList erzeugendePunkte = facette1.erzeugendePunkte();
      int size = erzeugendePunkte.size();
      for(int pnr = 0; pnr < size; pnr++) {
        Punkt pa = (Punkt)erzeugendePunkte.get(pnr);

        // Schritt 1
        Facette facette3 = facette1.angrenzendeFacette(pa);
        Facette facette4 = facette2.angrenzendeFacette(pa);

        // Schritt 2
        if((facette3 != facette2) && (facette4 == facette1)
           || (facette3 == facette2) && (facette4 != facette1)) {
          throw new RuntimeException("Facetten sind ungleich");
        }

        if(facette3 != facette2) {
          // Schritt 3
          Punkt pb = facette3.angrenzendeFacettePunkt(facette1);
          Punkt pc = facette4.angrenzendeFacettePunkt(facette2);

          // Schritt 4
          facette3.angrenzendeFacetteHinzufuegen(pb, facette4);
          facette4.angrenzendeFacetteHinzufuegen(pc, facette3);
        }
      }

      // facette1 und facette2 aus den unterhalbFacetten ihrer OberhalbPunkte 
      // löschen.
      Iterator punktIter;
      punktIter = facette1.oberhalbPunkte().iterator();
      while(punktIter.hasNext()) {
        if (! ((Punkt)punktIter.next()).unterhalbFacetteLoeschen(facette1)) {
          throw new RuntimeException("Facette nicht in Menge vorhanden");
        }
      }
      punktIter = facette2.oberhalbPunkte().iterator();
      while(punktIter.hasNext()) {
        if (! ((Punkt)punktIter.next()).unterhalbFacetteLoeschen(facette2)) {
          throw new RuntimeException("Facette nicht in Menge vorhanden");
        }
      }
    }

    // Nachdem aus ersteFacetten alle Facetten gelöscht wurden, zu denen
    // eine gleiche Facette in zweiteFacetten vorhanden war, werden jetzt
    // die restlichen Facetten zu den aktuellen und zu der Liste aller Facetten
    // hinzugefügt.
    aktuelleFacetten.addAll(ersteFacetten.keySet());
    alleFacetten.addAll(ersteFacetten.keySet());
  }

  /**
   * Liefert einen einen enthaltenen Punkt der konvexen Huelle, d.h.
   * einen Punkt, im Unterraum der konvenxen Huelle enthalten ist und
   * unterhalb jeder der Hyperebenen der konvenxen Huelle liegt.
   *
   * @return  Einen in der konvexen Huelle enthaltenen Punkt.
   */
  public Punkt enthaltenerPunkt() {
    return enthaltenerPunkt;
  }

  /**
   * Liefert den Unterraum, in dem die konvexe Huelle enthalten ist.
   *
   * @return  Den Unterraum, in dem die konvexe Huelle enthalten ist.
   */
  public Unterraum unterraum() {
    return unterraum;
  }

  /**
   * Liefert eine Liste von Elementen vom Typ <CODE>Facette</CODE>, die
   * zusammen mit dem Unterraum eine konvexe Hülle für eine Teilmenge
   * (eventuell auch die ganze Menge) der im Konstruktor übergegebenen Punkte
   * bilden.
   *
   * @return  Eine Liste von Elementen vom Typ <CODE>Facette</CODE>.
   */
  public ArrayList facetten() {
    return facetten;
  }

  /**
   * Liefert eine Liste von Elementen vom Typ <CODE>Hyperebene</CODE> (oder
   * teilweise der Unterklasse <CODE>Facette</CODE>), die zusammen mit dem
   * Unterraum eine konvexe Umgebung für die Menge aller im Konstruktor
   * übergebenen Punkte bilden. Wenn die konvexe Hülle schon alle diese Punkte
   * enthält, liefert die Methode die gleiche Menge wie <CODE>facetten()</CODE>.
   *
   * @return  Eine Liste von Elementen vom Typ <CODE>Hyperebene</CODE>.
   */
  public ArrayList umschliessendeHyperebenen() {
    ArrayList hyperebenen = new ArrayList(facetten.size());

    for (int fnr = 0; fnr < facetten.size(); fnr++) {
      Facette facette = (Facette)facetten.get(fnr);
      Punkt punkt = facette.punktMitMaximalemAbstand(facette.oberhalbPunkte());
      if (punkt == null) {
        // Es gibt keinen Punkt, der über facette liegt
        hyperebenen.add(facette);
      } else {
        // enthaltenerPunkt stellt für alle Hyperebene einen Referenzpunkt dar
        hyperebenen.add(new Hyperebene(enthaltenerPunkt, punkt, facette.basis()));
      }
    }
    return hyperebenen;
  }

  /**
   * Ermittelt, ob der uebergebene Punkt im Unterraum der konvexen Huelle
   * enthalten ist.
   *
   * @param punkt  Ein Punkt, von dem geprueft wird, ob er im Unterraum
   *               der konvexen Huelle enthalten ist.
   *
   * @return  <CODE>true</CODE>, wenn der uebergebene Punkt im Unterraum
   *          der konvexen Huelle enthalten ist, anderenfalls
   *          <CODE>false</CODE>.
   */
  public boolean enthaeltImUnterraum(Punkt punkt) {
    return unterraum.enthaelt(punkt);
  }

  /**
   * Ermittelt, ob der uebergebene Punkt in der konvexen Huelle (einer
   * Teilmenge der im Konstruktor übergebenen Punkt) enthalten ist, d.h.
   * sowohl im Unterraum der konvexen Huelle enthalten ist wie auch
   * unterhalb jeder Hyperebene, d.h. auf der gleichen Seite wie der
   * Referenzpunkt, liegt.
   *
   * @param punkt  Ein Punkt, von dem geprueft wird, ob er in der konvexen
   *               Huelle enthalten ist.
   *
   * @return  <CODE>true</CODE>, wenn der uebergebene Punkt in der konvexen
   *          Huelle enthalten ist, anderenfalls <CODE>false</CODE>.
   */
  public boolean enthaelt(Punkt punkt) {

    if(! unterraum.enthaelt(punkt)) {
      return false;
    } else{
      for(int i = 0; i < facetten.size(); i++) {
        Facette facette = (Facette)facetten.get(i);
        if (facette.punktLiegtOberhalb(punkt)) {
          return false;
        }
      }
      return true;
    }
  }

  /**
   * Ermittelt, ob alle Punkte, die im Konstruktor übegeben wurden und zu
   * deren Teilmenge die konvexe Hülle erzeugt wurde, in der konvexen Huelle
   * enthalten sind.
   *
   * @return  <CODE>true</CODE>, wenn alle im Konstruktor uebergebenen Punkte
   *          in der konvexen Huelle enthalten sind, anderenfalls
   *          <CODE>false</CODE>.
   */
  public boolean enthaeltAlleErzeugendenPunkte() {
    for (int i = 0; i < facetten.size(); i++) {
      Facette facette = (Facette)facetten.get(i);
      if (facette.oberhalbPunkte().size() > 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Liefert die Anzahl der übergebenen Punkte, die in der konvexen Hülle
   * enthalten sind.
   *
   * @param punkte  Die Punkte, von denen ermittelt werden soll, wie viele in
   *                der konvexen Hülle enthalten sind.
   *
   * @return  Die Anzhal der übergebenen Punkte, die in der konvexen Hülle
   *          enthalten sind.
   */
  public int anzEnthaltenerPunkte(ArrayList punkte) {
    int anzahl = 0;

    for (int i = 0; i < punkte.size(); i++) {
      Punkt punkt = (Punkt)punkte.get(i);
      if (enthaelt(punkt)) {
        anzahl++;
      }
    }
    return anzahl;
  }

  /**
   * Returns a string representation of this Instance
   *
   * @return return eine String Dastellung der Instance
   */
  public final String toString() {

    StringBuffer text = new StringBuffer();

    text.append("@KonvexeHuelle" + "\n");
    text.append("@Enthaltener Punkt der KonvexeHuelle" + "\n");
    text.append("  " + enthaltenerPunkt+ "\n");

    text.append("@Unterraum ,in dem die konvexeHuelle enthalten ist" + "\n");
    text.append("  " + unterraum + "\n");

    text.append("@Facetten" + "\n");
    for(int i = 0; i < facetten.size() ; i++) {
      text.append("  " + "@Facette[" + i + "]" + "\n");
      text.append("  " + facetten.get(i) + "\n");
    }
    text.append("\n");

    return text.toString();
  }
}

