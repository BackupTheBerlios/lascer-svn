/*
 * Datei          : Facette.java
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
 * Die Klasse repraesentiert einen durch Punkte abgegrenzten Teilbereich einer
 * Facette im mehrdimensionalen Raum, wobei die Facette aus den Punkten
 * erzeugt wird.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class Facette extends Hyperebene {
  /**
   * Die Menge der Punkte, aus denen das Objekt erzeugt wurde.
   * @associates <{geometrischeClusterung.Punkt}>
   */
  private ArrayList erzeugendePunkte;

  /**
   * Die Menge der Punkte, die oberhalb der Facette liegen
   * @associates <{geometrischeClusterung.Punkt}>
   */
  private HashSet oberhalbPunkte;

  /**
   * Zu jeder Kante der Facette ist die angrenzenden Facetten gespeichert. Eine
   * Kante ist eine Menge von Punkten, in denen genau ein Punkt der erzeugenden
   * Punkte der Facette fehlt. Sie wird repräsentiert durch den fehlenden
   * Punkt.<P>
   *
   * Die Keys der HashMap sind vom Typ <CODE>Punkt</CODE>, die Values vom Typ
   * <CODE>Facette</CODE>.
   */
  private HashMap angrenzendeFacetten;

  /**
   * Der HashCode der Facette.
   */
  private int hashCode;

  /**
   * Erzeugt eine neue Instanz aus einer Menge von Punkten. Dabei muss die
   * Anzahl der Punkte genau der Dimension des Gesamtraums entsprechen,
   * d.h. genau um Eins groesser sein als die Dimension der zu erzeugenden
   * Facette. Ist die Anzahl kleiner, wird eine
   * <CODE>ToFewElementsException</CODE> ausgeloesst, ist sie groesser,
   * wird eine <CODE>ToManyElementsException</CODE> ausgeloesst. Ausserdem
   * muss die Dimension des durch die Punkte erzeugten Unterraums genau um
   * Eins kleiner sein als die Dimension des Gesamtraums. Anderenfalls wird
   * eine <CODE>DimensionToLowException</CODE> ausgeloesst.<P>
   *
   * Der uebergebene Referenzpunkt legt die Orientierung des Normalenvektors
   * fest und zwar so, dass der Punkt eine negative Entferung von der
   * Facetten hat. Die Seite des Referenzpunktes wird als
   * Unterseite der Facette betrachtet. Liegt der Referenzpunkt in der
   * Facette, wird eine <CODE>SubspaceInclusionException</CODE>
   * ausgeloesst.
   *
   * @exception ToFewElementsException   Die Anzahl der erzeugenden Punkte
   *                                     ist kleiner als die Dimension des
   *                                     Unterraums plus eins.
   * @exception ToManyElementsException  Die Anzahl der erzeugenden Punkte
   *                                     ist groesser als die Dimension des
   *                                     Unterraums plus eins.
   * @exception SubspaceInclusionException  Der uebergebene Referenzpunkt
   *                                        liegt in der Facetten
   * @param referenzpunkt eine auf der unterseite der Facette liegt Punkt
   * @param erzeugendePunkte eine menge von Punkte , die die Facette erzeugen
   */
  public Facette(Punkt referenzpunkt, ArrayList erzeugendePunkte){
    super(referenzpunkt, erzeugendePunkte);
    this.erzeugendePunkte = (ArrayList)erzeugendePunkte.clone();
    this.angrenzendeFacetten = new HashMap();
    this.oberhalbPunkte = new HashSet();
    this.hashCode = berechneterHashCode();

    // Überprüfung der Genauigkeit der Abstandsberechnung
    for (int i = 0; i < erzeugendePunkte.size(); i++) {
      Punkt punkt = (Punkt)erzeugendePunkte.get(i);
      if (! enthaelt(punkt)) {
        throw new RuntimeException("Fehler durch Rechenungenauigkeit");
      }
    }
  }

  /**
   * Liefert die Menge der Punkte, aus denen das Objekt erzeugt wurde.
   *
   * @return  Die erzeugenden Punkte der Facette
   */
  public ArrayList erzeugendePunkte(){
    return erzeugendePunkte;
  }

  /**
   * Liefert die Menge der Punkte, die oberhalb der Facette liegen.
   *
   * @return  Alle Punkte, die oberhalb der Facette liegen
   */
  public HashSet oberhalbPunkte(){
    return oberhalbPunkte;
  }

  /**
   * Liefert die angrenzende Facette zurm übergebenen Punkt (Kante).
   *
   * @param kante  Ein Punkt
   * @return  Die Facette, die zum Punkt (zur Kante) gespeichert ist
   */
  public Facette angrenzendeFacette(Punkt kante){
    return (Facette)angrenzendeFacetten.get(kante);
  }

  /**
   * Liefert den Punkt (die Kante) zur angegebenen Facette.
   *
   * @param value  Eine Facette
   * @return  Den Punkt (die Kante), der zu der übergebenen, angrenzenden
   *          Facette gespeichert ist. Wenn keine solche Facette gespeichert
   *          ist, wird <CODE>null</CODE> geliefert.
   */
  public Punkt angrenzendeFacettePunkt(Facette value){

    Set aSet = angrenzendeFacetten.keySet();
    Iterator iter = aSet.iterator();
    Punkt aKey = null;
    while(iter.hasNext()){
      aKey = (Punkt)iter.next();
      if(value.equals(angrenzendeFacette(aKey))){
        return aKey;
      }
    }
    return aKey ;
  }

  /**
   * Ermittelt, ob die übergebene Facette zu <CODE>this</CODE> gleich ist.
   *
   * @param andereFacette  Eine zu vergleichende Facette.
   * @return  <CODE>true</CODE>, wenn die uebergebene Facette die gleichen
   *          erzeugenden Punkte wie der Facette selbst hat, anderenfalls
   *          wird <CODE>false</CODE> geliefert.
   */
  public boolean equals(Object andereFacette){

    if(andereFacette == this){
      return true;
    }

    if(! (andereFacette instanceof Facette)){
      return false;
    }

    if(andereFacette.hashCode() != hashCode){
      return false;
    }

    int n = ((Facette)andereFacette).erzeugendePunkte().size();
    int m = erzeugendePunkte.size();
    if(m!=n){
      return false;
    }
    else{
      HashSet punkteMenge = new HashSet(n);

      for(int i=0; i<n; i++){
        punkteMenge.add(((Facette)andereFacette).erzeugendePunkte.get(i));
      }

      for(int i=0; i<n; i++){
        if(!punkteMenge.contains(erzeugendePunkte.get(i))){
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Prueft, ob der uebergebene Punkt oberhalb der Facette liegt und wenn
   * dies so ist, wird der Punkt dem Attribut oberhalbPunkte hinzugefuegt.
   *
   * @param punkt einen gegebenen Punkt
   * @return  <CODE>true</CODE>, wenn der Punkt tatsächlich zur Menge
   *          hinzugefügt wurde, anderenfalls <CODE>false</CODE>.
   */
  public boolean oberhalbPunktHinzufuegen(Punkt punkt){

    if(oberhalbPunkte.contains(punkt)) {
      return false;
    }
    if(punktLiegtOberhalb(punkt)) {
      return oberhalbPunkte.add(punkt);
    }
    else {
      return false;
    }
  }

  /**
   * Loescht den uebergebenen Punkt aus dem Attribut oberhalbPunkte.
   *
   * @param punkt einen gegebenen Punkt
   * @return  <CODE>true</CODE>,wenn der übergegebene Punkt tatsächlich aus
   *          der entsprechenden Menge gelöscht wurde, d.h. vorher darin
   *          enthalten war, anderenfalls <CODE>false</CODE>.
   */
  public boolean oberhalbPunktLoeschen(Punkt punkt){
    return oberhalbPunkte.remove(punkt);
  }

  /**
   * Fuegt Facette als Wert zum Schluessel punkt dem Attribut
   * angrenzendeFacette hinzu.
   *
   * @param punkt    Der Punkt (die Kante) zur angrenzenden Facette
   * @param facette  Eine Facette
   */
  public void angrenzendeFacetteHinzufuegen(Punkt punkt, Facette facette){
    angrenzendeFacetten.put(punkt,facette);
  }

  /**
   * Liefert einen neu berechneten hashCode der Facette.
   *
   * @return  Einen neu berechneten hashCode der Instanz.
   */
  private int berechneterHashCode() {

    long summe;
    int anzahlPunkte = erzeugendePunkte.size();

    summe = 0;
    for (int i = 0; i < anzahlPunkte; i++) {
      summe += ((Punkt)erzeugendePunkte.get(i)).hashCode();
    }

    return (int)(summe / anzahlPunkte);
  }

  /**
   * Liefert den gespeicherten hashCode der Facette.
   *
   * @return  Den hashCode der Instanz.
   */
  public int hashCode() {
    return hashCode;
  }

  /**
   * Liefert eine String-Darstellung der Instanz.
   *
   * @return  Ein String als Dastellung der Instanz.
   */
  public String toString() {

    StringBuffer text = new StringBuffer();

    text.append("@ErzeugendePunkte der Facette" + "\n");

    int n = erzeugendePunkte.size();
    for (int i = 0; i < n; i++) {
      text.append("  " + erzeugendePunkte.get(i));
    }

    return text.toString();
  }
}

