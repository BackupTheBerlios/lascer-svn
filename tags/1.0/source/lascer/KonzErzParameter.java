/*
 * Dateiname      : KonzErzParameter.java
 * Letzte �nderung: 21. November 2007
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2007
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


package lascer;

import java.io.Serializable;

/**
 * Verwaltet eine Reihe von Parametern zur Steuerung der Erzeugung der
 * Konzepte.
 *
 * @author  Dietmar Lippold
 */
public class KonzErzParameter implements Serializable {

    /**
     * F�r eine verteilte Berechnung der Name des Rechners des Dispatchers.
     * Optional kann die Angabe auch ein Port enthalten. Bei einer lokalen
     * Berechnung hat dieser Wert keine Bedeutung.
     */
    private String dispatcher = Konstanten.DISPATCHER;

    /**
     * F�r eine verteilte Berechnung der Name des Rechners des
     * Class-File-Servers. Bei einer lokalen Berechnung hat dieser Wert keine
     * Bedeutung.
     */
    private String classServerName = Konstanten.CLASS_SERVER_NAME;

    /**
     * Die Wahrscheinlichkeit, mit der bei einer parallelen Verarbeitung
     * mehrerer Teilprobleme ein nicht �berdecktes bzw. nicht ausgeschlossenes
     * Beispiel als �berdeckt bzw. ausgeschlossen behandelt werden soll. Wenn
     * nur ein Teilproblem erzeugt wird, ist die Wahrscheinlichkeit immer
     * Null.
     */
    private float parallelAenderWkt = Konstanten.PARALLEL_AENDER_WKT;

    /**
     * Gibt die initial gesch�tzte maximale Gesamt-Komplexit�t einer Formel
     * an. Dieser Wert ist nur bei Vorgabe der Anzahl an Literalen, die
     * maximal in einer Disjunktion bzw. Konjunktion enthalten sein sollen,
     * (Option maxLitAnz) von Bedeutung.
     */
    private float initGesamtKomplex = Konstanten.INIT_GESAMT_KOMPLEX;

    /**
     * F�r eine verteilte Berechnung der Port des Class-File-Servers. Bei
     * einer lokalen Berechnung hat dieser Wert keine Bedeutung.
     */
    private int classServerPort = Konstanten.CLASS_SERVER_PORT;

    /**
     * F�r eine lokale Berechnung die Anzahl der Teilprobleme, die dem Problem
     * vorgeschlagen werden soll. Bei einer verteilten Berechnung hat dieser
     * Wert keine Bedeutung.
     */
    private int lokaleTeilprobAnz = Konstanten.LOKALE_TEILPROB_ANZ;

    /**
     * Gibt an, in welchem Ausma� die Teilmengen besonders Speicher-effizient
     * aber dadurch weniger Laufzeit-effizient verwaltet werden sollen. Der
     * Wert ist gleich oder gr��er Null (maximale Laufzeit-Effizienz) und
     * kleiner oder gleich Zwei (maximale Speicher-Effizienz).
     */
    private int speicherEffizienz = Konstanten.SPEICHER_EFFIZIENZ;

    /**
     * Die Anzahl der Literale an, die maximal in einer Disjunktion bzw. in
     * einer Konjunktion enthalten sein sollen. Der Wert Null steht f�r eine
     * unbegrenzte Anzahl.
     */
    private int maxLitAnz = Konstanten.MAX_LITERAL_ANZ;

    /**
     * Die Anzahl der Iterationen des Gesamtverfahrens, also die Anzahl, wie
     * oft nacheinander ein bestes Konzept erzeugt werden soll. Der Wert mu�
     * positiv sein.
     */
    private int gesamtIterAnz = Konstanten.GESAMT_ITER_ANZ;

    /**
     * Die Anzahl der �u�eren Iterationen, in denen korrekte oder vollst�ndige
     * Konzepte aufgenommen werden und anschlie�end die mittlere Iteration
     * durchgef�hrt wird. Beim Wert Null wird iteriert, bis keine Verbesserung
     * mehr eintritt. Anderenfalls gibt der Absolutbetrag die Anzahl der
     * Iterationen an. Bei einem positiven Wert wird, wie beim Wert Null, in
     * jeder Iteration gepr�ft, ob nach einer Ver�nderung der Mengen
     * tats�chlich eine Verbesserung stattgefunden hat.
     */
    private int aeussereIterAnz = Konstanten.AEUSSERE_ITER_ANZ;

    /**
     * Die Anzahl der mittleren Iterationen, in denen allgemeine Konzepte
     * aufgenommen werden und anschlie�end die innere Iteration durchgef�hrt
     * wird. Beim Wert Null wird iteriert, bis keine Verbesserung mehr
     * eintritt. Anderenfalls gibt der Absolutbetrag die Anzahl der
     * Iterationen an. Bei einem positiven Wert wird, wie beim Wert Null, in
     * jeder Iteration gepr�ft, ob nach einer Ver�nderung der Mengen
     * tats�chlich eine Verbesserung stattgefunden hat.
     */
    private int mittlereIterAnz = Konstanten.MITTLERE_ITER_ANZ;

    /**
     * Die Anzahl der inneren Iterationen, in denen aus den allgemeinen
     * Konzepten korrekte oder vollst�ndige Konzepte erzeugt werden. Beim Wert
     * Null wird iteriert, bis keine Verbesserung mehr eintritt. Anderenfalls
     * gibt der Absolutbetrag die Anzahl der Iterationen an. Bei einem
     * positiven Wert wird, wie beim Wert Null, in jeder Iteration gepr�ft, ob
     * nach einer Ver�nderung der Mengen tats�chlich eine Verbesserung
     * stattgefunden hat.
     */
    private int innereIterAnz = Konstanten.INNERE_ITER_ANZ;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen von allgemeinen
     * Konzepten zur Erzeugung spezieller Konzepte. Ein negativer Wert steht
     * f�r eine unbeschr�nkte Anzahl und es findet keine Auswahl statt.
     */
    private int algSpezItmAnz = Konstanten.ALG_SPEZ_ITM_ANZ;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen von speziellen, d.h.
     * korrekten oder vollst�ndigen, Konzepten zur Erzeugung von Formeln. Ein
     * negativer Wert steht f�r eine unbeschr�nkte Anzahl und es findet keine
     * Auswahl statt.
     */
    private int spezFormItmAnz = Konstanten.SPEZ_FORM_ITM_ANZ;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * allgemeinen Konzepte bei der Optimierung eines speziellen Konzepts. Bei
     * einem negativen Wert werden alle Teilmengen aufgenommen und es findet
     * keine Auswahl statt. Beim Wert Null findet keine Optimierung statt.
     */
    private int optSpezItmAnz = Konstanten.OPT_SPEZ_ITM_ANZ;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * speziellen Konzepte bei der Optimierung eines finalen Konzepts. Bei
     * einem negativen Wert werden alle Teilmengen aufgenommen und es findet
     * keine Auswahl statt. Beim Wert Null findet keine Optimierung statt.
     */
    private int optFinItmAnz = Konstanten.OPT_FIN_ITM_ANZ;

    /**
     * Gibt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren bei
     * der Erzeugung eines speziellen Konzepts aus den allgemeinen Konzepten
     * an.
     */
    private int erzSpezScpIterAnz = Konstanten.ERZ_SPEZ_SCP_ITER_ANZ;

    /**
     * Gibt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren bei
     * der Erzeugung einer Formel aus den speziellen, d.h. aus den korrekten
     * oder vollst�ndigen, Konzepten an.
     */
    private int erzFormScpIterAnz = Konstanten.ERZ_FORM_SCP_ITER_ANZ;

    /**
     * Gibt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren bei
     * der Optimierung eines speziellen Konzepts an.
     */
    private int optSpezScpIterAnz = Konstanten.OPT_SPEZ_SCP_ITER_ANZ;

    /**
     * Gibt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren bei
     * der Optimierung einer finalen Formel an.
     */
    private int optFinScpIterAnz = Konstanten.OPT_FIN_SCP_ITER_ANZ;

    /**
     * Gibt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren bei
     * der Erzeugung einer finalen Formel aus den speziellen, d.h. aus den
     * korrekten oder vollst�ndigen, Konzepten an.
     */
    private int finFormScpIterAnz = Konstanten.FIN_FORM_SCP_ITER_ANZ;

    /**
     * Gibt an, ob der Zufallsgenerator bei jedem Lauf mit einem anderen Wert
     * initialisiert werden soll. Anderenfalls wird er jedes Mal mit dem
     * gleichen Wert initialisiert.
     */
    private boolean zufall = Konstanten.ZUFALL;

    /**
     * Gibt an, ob die Berechnungen mittels Architeuthis verteilt durchgef�hrt
     * werden sollen. Falls nein, wird eine lokale Berechnung durchgef�hrt.
     */
    private boolean verteilt = Konstanten.VERTEILT;

    /**
     * Gibt an, ob boolsche Attribute negiert werden sollen, d.h. invertierte
     * Literale dazu erzeugt werden sollen.
     */
    private boolean negBoolPraedErz = Konstanten.NEG_BOOL_PRAED_ERZ;

    /**
     * Gibt an, ob nach Erzeugung einer korrekten und vollst�ndigen Formel
     * zus�tzlich zum jeweils optimalen Konzept weitere spezielle Konzepte
     * erzeugt und in die Auswahl der speziellen Konzepte aufgenommen werden
     * sollen.
     */
    private boolean zusatzKonzErz = Konstanten.ZUSATZ_KONZ_ERZ;

    /**
     * Legt f�r eine verteilte Berechnung den Namen des Rechners des
     * Dispatchers fest. Optional kann die Angabe auch ein Port enthalten. Bei
     * einer lokalen Berechnung hat dieser Wert keine Bedeutung.
     *
     * @param wert  F�r eine verteilte Berechnung der Namen des Rechners des
     *              Dispatchers. Optional kann die Angabe auch ein Port
     *              enthalten.
     */
    void setDispatcher(String wert) {
        dispatcher = wert;
    }

    /**
     * Legt f�r eine verteilte Berechnung den Namen des Rechners des
     * Class-File-Servers fest. Bei einer lokalen Berechnung hat dieser Wert
     * keine Bedeutung.
     *
     * @param wert  F�r eine verteilte Berechnung der Name des Rechners des
     *              Class-File-Servers.
     */
    void setClassServerName(String wert) {
        classServerName = wert;
    }

    /**
     * Legt die Wahrscheinlichkeit fest, mit der bei einer parallelen
     * Verarbeitung mehrerer Teilprobleme ein nicht �berdecktes bzw. nicht
     * ausgeschlossenes Beispiel als �berdeckt bzw. ausgeschlossen behandelt
     * werden soll.
     *
     * @param wert  Die Wahrscheinlichkeit fest, mit der bei einer parallelen
     *              Verarbeitung mehrerer Teilprobleme ein nicht �berdecktes
     *              bzw. nicht ausgeschlossenes Beispiel als �berdeckt bzw.
     *              ausgeschlossen behandelt werden soll.
     */
    void setParallelAenderWkt(float wert) {
        parallelAenderWkt = wert;
    }

    /**
     * Legt die initial gesch�tzte maximale Gesamt-Komplexit�t einer Formel
     * fest. Dieser Wert ist nur bei Vorgabe der Anzahl an Literalen, die
     * maximal in einer Disjunktion bzw. Konjunktion enthalten sein sollen,
     * (Attribut <CODE>maxLitAnz</CODE>) von Bedeutung.
     *
     * @param wert  Die initial gesch�tzte maximale Gesamt-Komplexit�t einer
     *              Formel.
     */
    void setInitGesamtKomplex(float wert) {
        initGesamtKomplex = wert;
    }

    /**
     * Legt f�r eine verteilte Berechnung den Port des Class-File-Servers
     * fest. Bei einer lokalen Berechnung hat dieser Wert keine Bedeutung.
     *
     * @param wert  F�r eine verteilte Berechnung der Port des
     *              Class-File-Servers.
     */
    void setClassServerPort(int wert) {
        classServerPort = wert;
    }

    /**
     * Legt f�r eine lokale Berechnung die Anzahl der Teilprobleme fest, die
     * dem Problem vorgeschlagen werden soll. Bei einer verteilten Berechnung
     * hat dieser Wert keine Bedeutung.
     *
     * @param wert  F�r eine lokale Berechnung die Anzahl der Teilprobleme,
     *              die dem Problem vorgeschlagen werden soll.
     */
    void setLokaleTeilprobAnz(int wert) {
        lokaleTeilprobAnz = wert;
    }

    /**
     * Legt fest, in welchem Ausma� die Teilmengen besonders
     * Speicher-effizient aber dadurch weniger Laufzeit-effizient verwaltet
     * werden sollen. Der Wert ist gleich oder gr��er Null (maximale
     * Laufzeit-Effizienz) und kleiner oder gleich Zwei (maximale
     * Speicher-Effizienz).
     *
     * @param wert  Der Wert, der angibt, in welchem Ausma� die Teilmengen
     *              besonders Speicher-effizient aber dadurch weniger
     *              Laufzeit-effizient verwaltet werden sollen.
     */
    void setSpeicherEffizienz(int wert) {
        speicherEffizienz = wert;
    }

    /**
     * Legt die Anzahl der Literale fest, die maximal in einer Disjunktion
     * bzw. in einer Konjunktion enthalten sein sollen. Der Wert Null steht
     * f�r eine unbegrenzte Anzahl.
     *
     * @param wert  Die Anzahl der Literale, die maximal in einer Disjunktion
     *              bzw. in einer Konjunktion enthalten sein sollen.
     */
    void setMaxLitAnz(int wert) {
        maxLitAnz = wert;
    }

    /**
     * Legt die Anzahl der Iterationen des Gesamtverfahrens fest, also die
     * Anzahl, wie oft nacheinander ein bestes Konzept erzeugt werden soll.
     *
     * @param wert  Die Anzahl der Iterationen des Gesamtverfahrens, also die
     *              Anzahl, wie oft nacheinander ein bestes Konzept erzeugt
     *              werden soll. Der Wert mu� positiv sein.
     */
    void setGesamtIterAnz(int wert) {
        gesamtIterAnz = wert;
    }

    /**
     * Legt die Anzahl der �u�eren Iterationen fest, in denen korrekte oder
     * vollst�ndige Konzepte aufgenommen werden und anschlie�end die mittlere
     * Iteration durchgef�hrt wird. Beim Wert Null wird iteriert, bis keine
     * Verbesserung mehr eintritt. Anderenfalls gibt der Absolutbetrag die
     * Anzahl der Iterationen an. Bei einem positiven Wert wird, wie beim Wert
     * Null, in jeder Iteration gepr�ft, ob nach einer Ver�nderung der Mengen
     * tats�chlich eine Verbesserung stattgefunden hat.
     *
     * @param wert  Die Anzahl der �u�eren Iterationen.
     */
    void setAeussereIterAnz(int wert) {
        aeussereIterAnz = wert;
    }

    /**
     * Legt die Anzahl der mittleren Iterationen fest, in denen allgemeine
     * Konzepte aufgenommen werden und anschlie�end die innere Iteration
     * durchgef�hrt wird. Beim Wert Null wird iteriert, bis keine Verbesserung
     * mehr eintritt. Anderenfalls gibt der Absolutbetrag die Anzahl der
     * Iterationen an. Bei einem positiven Wert wird, wie beim Wert Null, in
     * jeder Iteration gepr�ft, ob nach einer Ver�nderung der Mengen
     * tats�chlich eine Verbesserung stattgefunden hat.
     *
     * @param wert  Die Anzahl der mittleren Iterationen.
     */
    void setMittlereIterAnz(int wert) {
        mittlereIterAnz = wert;
    }

    /**
     * Legt die Anzahl der inneren Iterationen fest, in denen aus den
     * allgemeinen Konzepten korrekte oder vollst�ndige Konzepte erzeugt
     * werden. Beim Wert Null wird iteriert, bis keine Verbesserung mehr
     * eintritt. Anderenfalls gibt der Absolutbetrag die Anzahl der
     * Iterationen an. Bei einem positiven Wert wird, wie beim Wert Null, in
     * jeder Iteration gepr�ft, ob nach einer Ver�nderung der Mengen
     * tats�chlich eine Verbesserung stattgefunden hat.
     *
     * @param wert  Die Anzahl der inneren Iterationen.
     */
    void setInnereIterAnz(int wert) {
        innereIterAnz = wert;
    }

    /**
     * Legt die minimale Anzahl der zu speichernden Teilmengen von allgemeinen
     * Konzepten zur Erzeugung spezieller Konzepte fest. Ein negativer Wert
     * steht f�r eine unbeschr�nkte Anzahl und es findet keine Auswahl statt.
     *
     * @param wert  Die minimale Anzahl der zu speichernden Teilmengen von
     *              allgemeinen Konzepten zur Erzeugung spezieller Konzepte.
     */
    void setAlgSpezItmAnz(int wert) {
        algSpezItmAnz = wert;
    }

    /**
     * Legt die minimale Anzahl der zu speichernden Teilmengen von speziellen,
     * d.h. korrekten oder vollst�ndigen, Konzepten zur Erzeugung von Formeln
     * fest. Ein negativer Wert steht f�r eine unbeschr�nkte Anzahl und es
     * findet keine Auswahl statt.
     *
     * @param wert  Die minimale Anzahl der zu speichernden Teilmengen von
     *              speziellen, d.h. korrekten oder vollst�ndigen, Konzepten
     *              zur Erzeugung von Formeln.
     */
    void setSpezFormItmAnz(int wert) {
        spezFormItmAnz = wert;
    }

    /**
     * Legt die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * allgemeinen Konzepte bei der Optimierung eines speziellen Konzepts
     * fest. Bei einem negativen Wert werden alle Teilmengen aufgenommen und
     * es findet keine Auswahl statt. Beim Wert Null findet keine Optimierung
     * statt.
     *
     * @param wert  Die minimale Anzahl der zu speichernden Teilmengen zur
     *              Auswahl der allgemeinen Konzepte bei der Optimierung eines
     *              speziellen Konzepts.
     */
    void setOptSpezItmAnz(int wert) {
        optSpezItmAnz = wert;
    }

    /**
     * Legt die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * speziellen Konzepte bei der Optimierung eines finalen Konzepts fest.
     * Bei einem negativen Wert werden alle Teilmengen aufgenommen und es
     * findet keine Auswahl statt. Beim Wert Null findet keine Optimierung
     * statt.
     *
     * @param wert  Die minimale Anzahl der zu speichernden Teilmengen zur
     *              Auswahl der speziellen Konzepte bei der Optimierung eines
     *              finalen Konzepts.
     */
    void setOptFinItmAnz(int wert) {
        optFinItmAnz = wert;
    }

    /**
     * Legt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren bei
     * der Erzeugung eines speziellen Konzepts aus den allgemeinen Konzepten
     * fest.
     *
     * @param wert  Die Anzahl der durchzuf�hrenden Iterationen beim
     *              SCP-Verfahren bei der Erzeugung eines speziellen Konzepts
     *              aus den allgemeinen Konzepten.
     */
    void setErzSpezScpIterAnz(int wert) {
        erzSpezScpIterAnz = wert;
    }

    /**
     * Legt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren bei
     * der Erzeugung einer Formel aus den speziellen, d.h. aus den korrekten
     * oder vollst�ndigen, Konzepten fest.
     *
     * @param wert  Die Anzahl der durchzuf�hrenden Iterationen beim
     *              SCP-Verfahren bei der Erzeugung einer Formel aus den
     *              speziellen Konzepten.
     */
    void setErzFormScpIterAnz(int wert) {
        erzFormScpIterAnz = wert;
    }

    /**
     * Legt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren bei
     * der Optimierung eines speziellen Konzepts fest.
     *
     * @param wert  Die Anzahl der durchzuf�hrenden Iterationen beim
     *              SCP-Verfahren bei der Optimierung eines speziellen
     *              Konzepts.
     */
    void setOptSpezScpIterAnz(int wert) {
        optSpezScpIterAnz = wert;
    }

    /**
     * Legt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren bei
     * der Optimierung einer finalen Formel fest.
     *
     * @param wert  Die Anzahl der durchzuf�hrenden Iterationen beim
     *              SCP-Verfahren bei der Optimierung einer finalen Formel.
     */
    void setOptFinScpIterAnz(int wert) {
        optFinScpIterAnz = wert;
    }

    /**
     * Legt die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren bei
     * der Erzeugung einer finalen Formel aus den speziellen, d.h. aus den
     * korrekten oder vollst�ndigen, Konzepten fest.
     *
     * @param wert  Die Anzahl der durchzuf�hrenden Iterationen beim
     *              SCP-Verfahren bei der Erzeugung einer finalen Formel aus
     *              den speziellen Konzepten.
     */
    void setFinFormScpIterAnz(int wert) {
        finFormScpIterAnz = wert;
    }

    /**
     * Legt fest, ob der Zufallsgenerator bei jedem Lauf mit einem anderen
     * Wert initialisiert werden soll. Anderenfalls wird er jedes Mal mit dem
     * gleichen Wert initialisiert.
     *
     * @param wert  Der Wert, der angibt, ob der Zufallsgenerator bei jedem
     *              Lauf mit einem anderen Wert initialisiert werden soll.
     */
    void setZufall(boolean wert) {
        zufall = wert;
    }

    /**
     * Legt fest, ob die Berechnungen mittels Architeuthis verteilt
     * durchgef�hrt werden sollen. Falls nein, wird eine lokale Berechnung
     * durchgef�hrt.
     *
     * @param wert  Der Wert, der angibt, ob die Berechnungen mittels
     *              Architeuthis verteilt durchgef�hrt werden sollen. Falls
     *              nein, wird eine lokale Berechnung durchgef�hrt.
     */
    void setVerteilt(boolean wert) {
        verteilt = wert;
    }

    /**
     * Legt fest, ob boolsche Attribute negiert werden sollen, d.h.
     * invertierte Literale dazu erzeugt werden sollen.
     *
     * @param wert  Der Wert, der angibt, ob boolsche Attribute negiert werden
     *              sollen, d.h. invertierte Literale dazu erzeugt werden
     *              sollen.
     */
    void setNegBoolPraedErz(boolean wert) {
        negBoolPraedErz = wert;
    }

    /**
     * Legt fest, ob nach Erzeugung einer korrekten und vollst�ndigen Formel
     * zus�tzlich zum jeweils optimalen Konzept weitere spezielle Konzepte
     * erzeugt und in die Auswahl der speziellen Konzepte aufgenommen werden
     * sollen.
     *
     * @param wert  Der Wert, der angibt, ob nach Erzeugung einer korrekten
     *              und vollst�ndigen Formel zus�tzliche spezielle Konzepte
     *              erzeugt werden sollen.
     */
    void setZusatzKonzErz(boolean wert) {
        zusatzKonzErz = wert;
    }

    /**
     * Liefert f�r eine verteilte Berechnung den Namen des Rechners des
     * Dispatchers. Optional kann die Angabe auch ein Port enthalten. Bei
     * einer lokalen Berechnung hat dieser Wert keine Bedeutung.
     *
     * @return  F�r eine verteilte Berechnung den Namen des Rechners des
     *          Dispatchers. Die Angabe kann auch einen Port enthalten.
     */
    public String getDispatcher() {
        return dispatcher;
    }

    /**
     * Liefert f�r eine verteilte Berechnung den Namen des Rechners des
     * Class-File-Servers. Bei einer lokalen Berechnung hat dieser Wert
     * keine Bedeutung.
     *
     * @return  F�r eine verteilte Berechnung den Namen des Rechners des
     *          Class-File-Servers.
     */
    public String getClassServerName() {
        return classServerName;
    }

    /**
     * Liefert die Wahrscheinlichkeit, mit der bei einer parallelen
     * Verarbeitung mehrerer Teilprobleme ein nicht �berdecktes bzw. nicht
     * ausgeschlossenes Beispiel als �berdeckt bzw. ausgeschlossen behandelt
     * werden soll.
     *
     * @return  Die Wahrscheinlichkeit fest, mit der bei einer parallelen
     *          Verarbeitung mehrerer Teilprobleme ein nicht �berdecktes bzw.
     *          nicht ausgeschlossenes Beispiel als �berdeckt bzw.
     *          ausgeschlossen behandelt werden soll.
     */
    public float getParallelAenderWkt() {
        return parallelAenderWkt;
    }

    /**
     * Liefert die initial gesch�tzte maximale Gesamt-Komplexit�t einer
     * Formel. Dieser Wert ist nur bei Vorgabe der Anzahl an Literalen, die
     * maximal in einer Disjunktion bzw. Konjunktion enthalten sein sollen,
     * (Attribut <CODE>maxLitAnz</CODE>) von Bedeutung.
     *
     * @return  Die initial gesch�tzte maximale Gesamt-Komplexit�t einer
     *          Formel.
     */
    public float getInitGesamtKomplex() {
        return initGesamtKomplex;
    }

    /**
     * Liefert f�r eine verteilte Berechnung den Port des Class-File-Servers.
     * Bei einer lokalen Berechnung hat dieser Wert keine Bedeutung.
     *
     * @return  F�r eine verteilte Berechnung den Port des Class-File-Servers.
     */
    public int getClassServerPort() {
        return classServerPort;
    }

    /**
     * Liefert f�r eine lokale Berechnung die Anzahl der Teilprobleme, die
     * dem Problem vorgeschlagen werden soll. Bei einer verteilten Berechnung
     * hat dieser Wert keine Bedeutung.
     *
     * @return  F�r eine lokale Berechnung die Anzahl der Teilprobleme, die
     *          dem Problem vorgeschlagen werden soll.
     */
    public int getLokaleTeilprobAnz() {
        return lokaleTeilprobAnz;
    }

    /**
     * Liefert einen Wert, der angibt, in welchem Ausma� die Teilmengen
     * besonders Speicher-effizient aber dadurch weniger Laufzeit-effizient
     * verwaltet werden sollen. Der Wert ist gleich oder gr��er Null (maximale
     * Laufzeit-Effizienz) und kleiner oder gleich Zwei (maximale
     * Speicher-Effizienz).
     *
     * @return  Den Wert, der angibt, in welchem Ausma� die Teilmengen
     *          besonders Speicher-effizient aber dadurch weniger
     *          Laufzeit-effizient verwaltet werden sollen.
     */
    public int getSpeicherEffizienz() {
        return speicherEffizienz;
    }

    /**
     * Liefert die Anzahl der Literale, die maximal in einer Disjunktion bzw.
     * in einer Konjunktion enthalten sein sollen. Der Wert Null steht f�r
     * eine unbegrenzte Anzahl.
     *
     * @return  Die Anzahl der Literale, die maximal in einer Disjunktion bzw.
     *          in einer Konjunktion enthalten sein sollen.
     */
    public int getMaxLitAnz() {
        return maxLitAnz;
    }

    /**
     * Liefert die Anzahl der Iterationen des Gesamtverfahrens, also die
     * Anzahl, wie oft nacheinander ein bestes Konzept erzeugt werden soll.
     *
     * @return  Die Anzahl der Iterationen des Gesamtverfahrens, also die
     *          Anzahl, wie oft nacheinander ein bestes Konzept erzeugt werden
     *          soll. Der Wert mu� positiv sein.
     */
    public int getGesamtIterAnz() {
        return gesamtIterAnz;
    }

    /**
     * Liefert die Anzahl der �u�eren Iterationen, in denen korrekte oder
     * vollst�ndige Konzepte aufgenommen werden und anschlie�end die mittlere
     * Iteration durchgef�hrt wird. Beim Wert Null wird iteriert, bis keine
     * Verbesserung mehr eintritt. Anderenfalls gibt der Absolutbetrag die
     * Anzahl der Iterationen an. Bei einem positiven Wert wird, wie beim Wert
     * Null, in jeder Iteration gepr�ft, ob nach einer Ver�nderung der Mengen
     * tats�chlich eine Verbesserung stattgefunden hat.
     *
     * @return  Die Anzahl der �u�eren Iterationen.
     */
    public int getAeussereIterAnz() {
        return aeussereIterAnz;
    }

    /**
     * Liefert die Anzahl der mittleren Iterationen, in denen allgemeine
     * Konzepte aufgenommen werden und anschlie�end die innere Iteration
     * durchgef�hrt wird. Beim Wert Null wird iteriert, bis keine Verbesserung
     * mehr eintritt. Anderenfalls gibt der Absolutbetrag die Anzahl der
     * Iterationen an. Bei einem positiven Wert wird, wie beim Wert Null, in
     * jeder Iteration gepr�ft, ob nach einer Ver�nderung der Mengen
     * tats�chlich eine Verbesserung stattgefunden hat.
     *
     * @return  Die Anzahl der mittleren Iterationen.
     */
    public int getMittlereIterAnz() {
        return mittlereIterAnz;
    }

    /**
     * Liefert die Anzahl der inneren Iterationen, in denen aus den
     * allgemeinen Konzepten korrekte oder vollst�ndige Konzepte erzeugt
     * werden. Beim Wert Null wird iteriert, bis keine Verbesserung mehr
     * eintritt. Anderenfalls gibt der Absolutbetrag die Anzahl der
     * Iterationen an. Bei einem positiven Wert wird, wie beim Wert Null, in
     * jeder Iteration gepr�ft, ob nach einer Ver�nderung der Mengen
     * tats�chlich eine Verbesserung stattgefunden hat.
     *
     * @return  Die Anzahl der inneren Iterationen.
     */
    public int getInnereIterAnz() {
        return innereIterAnz;
    }

    /**
     * Liefert die minimale Anzahl der zu speichernden Teilmengen von
     * allgemeinen Konzepten zur Erzeugung spezieller Konzepte. Ein negativer
     * Wert steht f�r eine unbeschr�nkte Anzahl und es findet keine Auswahl
     * statt.
     *
     * @return  Die minimale Anzahl der zu speichernden Teilmengen von
     *          allgemeinen Konzepten zur Erzeugung spezieller Konzepte.
     */
    public int getAlgSpezItmAnz() {
        return algSpezItmAnz;
    }

    /**
     * Liefert die minimale Anzahl der zu speichernden Teilmengen von
     * speziellen, d.h. korrekten oder vollst�ndigen, Konzepten zur Erzeugung
     * von Formeln. Ein negativer Wert steht f�r eine unbeschr�nkte Anzahl und
     * es findet keine Auswahl statt.
     *
     * @return  Die minimale Anzahl der zu speichernden Teilmengen von
     *          speziellen, d.h. korrekten oder vollst�ndigen, Konzepten zur
     *          Erzeugung von Formeln.
     */
    public int getSpezFormItmAnz() {
        return spezFormItmAnz;
    }

    /**
     * Liefert die minimale Anzahl der zu speichernden Teilmengen zur Auswahl
     * der allgemeinen Konzepte bei der Optimierung eines speziellen Konzepts.
     * Bei einem negativen Wert werden alle Teilmengen aufgenommen und es
     * findet keine Auswahl statt. Beim Wert Null findet keine Optimierung
     * statt.
     *
     * @return  Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl
     *          der allgemeinen Konzepte bei der Optimierung eines speziellen
     *          Konzepts.
     */
    public int getOptSpezItmAnz() {
        return optSpezItmAnz;
    }

    /**
     * Liefert die minimale Anzahl der zu speichernden Teilmengen zur Auswahl
     * der speziellen Konzepte bei der Optimierung eines finalen Konzepts. Bei
     * einem negativen Wert werden alle Teilmengen aufgenommen und es findet
     * keine Auswahl statt. Beim Wert Null findet keine Optimierung statt.
     *
     * @return  Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl
     *          der speziellen Konzepte bei der Optimierung eines finalen
     *          Konzepts.
     */
    public int getOptFinItmAnz() {
        return optFinItmAnz;
    }

    /**
     * Liefert die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     * bei der Erzeugung eines speziellen Konzepts aus den allgemeinen
     * Konzepten.
     *
     * @return  Die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     *          bei der Erzeugung eines speziellen Konzepts aus den
     *          allgemeinen Konzepten.
     */
    public int getErzSpezScpIterAnz() {
        return erzSpezScpIterAnz;
    }

    /**
     * Liefert die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     * bei der Erzeugung einer Formel aus den speziellen, d.h. aus den
     * korrekten oder vollst�ndigen, Konzepten.
     *
     * @return  Die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     *          bei der Erzeugung einer Formel aus den speziellen, d.h. aus
     *          den korrekten oder vollst�ndigen, Konzepten.
     */
    public int getErzFormScpIterAnz() {
        return erzFormScpIterAnz;
    }

    /**
     * Liefert die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     * bei der Optimierung eines speziellen Konzepts.
     *
     * @return  Die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     *          bei der Optimierung eines speziellen Konzepts.
     */
    public int getOptSpezScpIterAnz() {
        return optSpezScpIterAnz;
    }

    /**
     * Liefert die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     * bei der Optimierung einer finalen Formel.
     *
     * @return  Die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     *          bei der Optimierung einer finalen Formel.
     */
    public int getOptFinScpIterAnz() {
        return optFinScpIterAnz;
    }

    /**
     * Liefert die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     * bei der Erzeugung einer finalen Formel aus den speziellen, d.h. aus den
     * korrekten oder vollst�ndigen, Konzepten.
     *
     * @return  Die Anzahl der durchzuf�hrenden Iterationen beim SCP-Verfahren
     *          bei der Erzeugung einer finalen Formel aus den speziellen,
     *          d.h. aus den korrekten oder vollst�ndigen, Konzepten.
     */
    public int getFinFormScpIterAnz() {
        return finFormScpIterAnz;
    }

    /**
     * Gibt an, ob der Zufallsgenerator bei jedem Lauf mit einem anderen Wert
     * initialisiert werden soll. Anderenfalls wird er jedes Mal mit dem
     * gleichen Wert initialisiert.
     *
     * @return  Den Wert, der angibt, ob der Zufallsgenerator bei jedem Lauf
     *          mit einem anderen Wert initialisiert werden soll.
     */
    public boolean getZufall() {
        return zufall;
    }

    /**
     * Gibt an, ob die Berechnungen mittels Architeuthis verteilt
     * durchgef�hrt werden sollen. Falls nein, wird eine lokale Berechnung
     * durchgef�hrt.
     *
     * @return  Den Wert, der angibt, ob die Berechnungen mittels Architeuthis
     *          verteilt durchgef�hrt werden sollen. Falls nein, wird eine
     *          lokale Berechnung durchgef�hrt.
     */
    public boolean getVerteilt() {
        return verteilt;
    }

    /**
     * Gibt an, ob boolsche Attribute negiert werden sollen, d.h. invertierte
     * Literale dazu erzeugt werden sollen.
     *
     * @return  Der Wert, der angibt, ob boolsche Attribute negiert werden
     *          sollen, d.h. invertierte Literale dazu erzeugt werden sollen.
     */
    public boolean getNegBoolPraedErz() {
        return negBoolPraedErz;
    }

    /**
     * Gibt an, ob nach Erzeugung einer korrekten und vollst�ndigen Formel
     * zus�tzlich zum jeweils optimalen Konzept weitere spezielle Konzepte
     * erzeugt und in die Auswahl der speziellen Konzepte aufgenommen werden
     * sollen.
     *
     * @return  Den Wert, der angibt, ob nach Erzeugung einer korrekten und
     *          vollst�ndigen Formel zus�tzliche spezielle Konzepte erzeugt
     *          werden sollen.
     */
    public boolean getZusatzKonzErz() {
        return zusatzKonzErz;
    }
}

