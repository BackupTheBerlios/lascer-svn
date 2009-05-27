/*
 * Dateiname      : Konstanten.java
 * Letzte Änderung: 18. Dezember 2007
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

import java.util.logging.Level;

/**
 * Enthält default-Werte für die Konfiguration der Klassen aus diesem Package.
 *
 * @author  Dietmar Lippold
 */
public class Konstanten {

    /**
     * Der Name vom Package mit den Klassen der int-Funktionen, die zur
     * Erzeugung von Prädikaten verwendet werden sollen.
     */
    public static final String INT_FUNK_ERZEUGER_PACKAGE = "lascer.intfunktionen.konkrete";

    /**
     * Die Namen der Klassen der zweistelligen int-Funktionen, die zur
     * Erzeugung von Prädikaten verwendet werden sollen. Die Namen sollten
     * nach der Komplexität ihrer Funktionen aufsteigend geordnet sein.
     */
    public static final String[] INT_ZWEI_FUNK_ERZEUGER_NAMEN
        = new String[] {"IntAdditionErzeugung", "IntSubtraktionErzeugung",
                        "IntAbsSubstErzeugung", "IntMultiplikationErzeugung"};

    /**
     * Die Namen der Klassen der einstelligen int-Funktionen, die zur
     * Erzeugung von Prädikaten verwendet werden sollen.
     */
    public static final String[] INT_EIN_FUNK_ERZEUGER_NAMEN
        = new String[] {};
//        = new String[] {"IntNeunMinusErzeugung"};

    /**
     * Die Namen der Klassen der nullstelligen int-Funktionen, die zur
     * Erzeugung von Prädikaten verwendet werden sollen.
     */
    public static final String[] INT_NULL_FUNK_ERZEUGER_NAMEN
        = new String[] {};
//        = new String[] {"IntKonstVierErzeugung", "IntKonstZweiErzeugung",
//                        "IntKonstEinsErzeugung"};

    /**
     * Der Name vom Package mit den Klassen der real-Funktionen, die zur
     * Erzeugung von Prädikaten verwendet werden sollen.
     */
    public static final String REAL_FUNK_ERZEUGER_PACKAGE = "lascer.realfunktionen.konkrete";

    /**
     * Die Namen der Klassen der zweistelligen real-Funktionen, die zur
     * Erzeugung von Prädikaten verwendet werden sollen. Die Namen sollten
     * nach der Komplexität ihrer Funktionen aufsteigend geordnet sein.
     */
    public static final String[] REAL_ZWEI_FUNK_ERZEUGER_NAMEN
        = new String[] {"RealAdditionErzeugung", "RealSubtraktionErzeugung",
                        "RealAbsSubstErzeugung", "RealMultiplikationErzeugung"};

    /**
     * Die Namen der Klassen der einstelligen real-Funktionen, die zur
     * Erzeugung von Prädikaten verwendet werden sollen.
     */
    public static final String[] REAL_EIN_FUNK_ERZEUGER_NAMEN
        = new String[] {};
//        = new String[] {"RealNeunMinusErzeugung"};

    /**
     * Die Namen der Klassen der nullstelligen real-Funktionen, die zur
     * Erzeugung von Prädikaten verwendet werden sollen.
     */
    public static final String[] REAL_NULL_FUNK_ERZEUGER_NAMEN
        = new String[] {};
//        = new String[] {"RealKonstVierErzeugung", "RealKonstZweiErzeugung",
//                        "RealKonstEinsErzeugung"};

    /**
     * Der zu verwendenden Logging-Level an.
     */
    public static final Level LOGGING_LEVEL = Level.CONFIG;

    /**
     * Der Text, der dem Namen eines nicht-ganzzahligen Attributs angehängt
     * wird, das aus einem ganzzahligen Attribut erzeugt wird.
     */
    public static final String INT_REAL_ATTRIB_ANHANG = "R";

    /**
     * Gibt an, welche Art von Formeln erzeugt und ausgegeben werden sollen.
     * Bei <CITE>dis</CITE> wird nur die disjunktive (eine korrekte) und bei
     * <CITE>kon</CITE> nur die konjunktive (eine vollständig) erzeugt und
     * ausgegeben. Bei <CITE>beste</CITE> und bei <CITE>beide</CITE> werden
     * beide Formeln erzeugt. Bei <CITE>beste</CITE> wird aber nur die bessere
     * von beiden ausgegeben, während bei <CITE>beide</CITE> beide Formeln
     * ausgegeben werden.
     */
    public static final String AUSGABE_FORMEL_ART = "beide";

    /**
     * Gibt an, für die Beispiele welcher Klasse eine Formel erzeugt und
     * ausgegeben werden soll. Bei <CITE>pos</CITE> wird eine Formel für die
     * positiven Beispiele erzeugt und ausgegeben, bei <CITE>neg</CITE> für
     * die negativen Beispiele und bei <CITE>beide</CITE> werden für die
     * Beispiele beider Klassen Formeln erzeugt und ausgegeben.
     */
    public static final String AUSGABE_FORMEL_KLASSE = "pos";

    /**
     * Gibt an, ob anstatt einer Formel das effizienteste einzelne Konzept
     * erzeugt und ausgegeben werden soll und für das Beispiel welcher Klasse
     * dies gegebenenfalls zutreffen muß bzw. nicht zutreffen darf. Bei
     * <CITE>nein</CITE> wird kein einzelnes Konzept erzeugt, bei
     * <CITE>pos</CITE> muß das Konzept für das letzte positive Beispiel
     * zutreffen, bei <CITE>neg</CITE> darf es nicht für das letzte negative
     * Beispiel zutreffen und bei <CITE>keine</CITE> gibt es kein Beispiel,
     * für das das Konzept zutreffen muß bzw. nicht zutreffen darf.
     */
    public static final String EINZEL_BSP_KLASSE = "nein";

    /**
     * Gibt an, welche Art von Formel bei Verwendung für Weka für die
     * Klassifikation erzeugt und verwendet werden soll. Bei <CITE>dis</CITE>
     * wird nur die disjunktive (eine korrekte) und bei <CITE>kon</CITE> nur
     * die konjunktive (eine vollständig) erzeugt und zur Klassifikation
     * verwendet. Bei <CITE>beste</CITE> werden beide Formeln erzeugt, aber es
     * wird nur die bessere von beiden zur Klassifikation verwendet.
     */
    public static final String WEKA_FORMEL_ART = "beste";

    /**
     * Gibt an, für die Beispiele welcher Klasse eine Formel bei der
     * Verwendung für Weka erzeugt und zur Klassifikation verwendet werden
     * soll. Bei <CITE>pos</CITE> wird nur die Formel für die positiven
     * Beispiele und bei <CITE>neg</CITE> nur für die negativen Beispiele
     * erzeugt. Bei <CITE>beste</CITE> und bei <CITE>beide</CITE> werden
     * Formeln für die Beispiele beider Klassen erzeugt. Bei
     * <CITE>beste</CITE> wird aber nur die bessere Formel verwendet. Bei
     * <CITE>beide</CITE> erfolgt die Klassifikation entsprechend der
     * Veränderung der Formeln bei Hinzunahme des zu klassifizierenden
     * Beispiels jeweils zu den Beispielen einer Klasse.
     */
    public static final String WEKA_FORMEL_KLASSE = "beste";

    /**
     * Für eine verteilte Berechnung der Name des Rechners des Dispatchers.
     * Optional kann die Angabe auch einen Port enthalten. Bei einer lokalen
     * Berechnung hat dieser Wert keine Bedeutung.
     */
    public static final String DISPATCHER = "localhost";

    /**
     * Für eine verteilte Berechnung der Name des Rechners des
     * Class-File-Servers. Bei einer lokalen Berechnung hat dieser Wert keine
     * Bedeutung.
     */
    public static final String CLASS_SERVER_NAME = "localhost";

    /**
     * Der Anteil der positiven Beispiele in Prozent, der vor der Erzeugung
     * der Formel bzw. des Konzepts entfernt werden soll.
     */
    public static final float POS_PRUNE_ANT = 0.0f;

    /**
     * Der Anteil der negativen Beispiele in Prozent, der vor der Erzeugung
     * der Formel bzw. des Konzepts entfernt werden soll.
     */
    public static final float NEG_PRUNE_ANT = 0.0f;

    /**
     * Die Wahrscheinlichkeit, mit der bei einer parallelen Verarbeitung
     * mehrerer Teilprobleme ein nicht überdecktes bzw. nicht ausgeschlossenes
     * Beispiel als überdeckt bzw. ausgeschlossen behandelt werden soll. Wenn
     * nur ein Teilproblem erzeugt wird, ist die Wahrscheinlichkeit immer
     * Null.
     */
    public static final float PARALLEL_AENDER_WKT = 0.3f;

    /**
     * Gibt die initial geschätzte maximale Gesamt-Komplexität einer Formel
     * an. Dieser Wert ist nur bei Vorgabe der Anzahl an Literalen, die
     * maximal in einer Disjunktion bzw. Konjunktion enthalten sein sollen,
     * (Konstante <CODE>MAX_LIT_ANZ</CODE>) von Bedeutung.
     */
    public static final float INIT_GESAMT_KOMPLEX = 0.0f;

    /**
     * Der Grenzwert der Differenz der Komplexitäten zweier Formeln, bis zu
     * dem (exklusiv) bei der Klassifikation eine lokale Klassifikation anhand
     * des Testbeispiels erfolgt. Die Formeln ergeben sich nach Hinzunahme des
     * Testbeispiels zum einen als positives und zum anderen als negatives
     * Beispiel.
     */
    public static final float LOKAL_GRENZWERT = 0.0f;

    /**
     * Für eine verteilte Berechnung der Port des Class-File-Servers. Bei
     * einer lokalen Berechnung hat dieser Wert keine Bedeutung.
     */
    public static final int CLASS_SERVER_PORT = 1855;

    /**
     * Für eine lokale Berechnung die Anzahl der Teilprobleme, die dem Problem
     * vorgeschlagen werden soll. Bei einer verteilten Berechnung hat dieser
     * Wert keine Bedeutung.
     */
    public static final int LOKALE_TEILPROB_ANZ = 1;

    /**
     * Gibt an, in welchem Ausmaß die Teilmengen besonders Speicher-effizient
     * aber dadurch weniger Laufzeit-effizient verwaltet werden sollen. Der
     * Wert ist gleich oder größer Null (maximale Laufzeit-Effizienz) und
     * kleiner oder gleich Zwei (maximale Speicher-Effizienz).
     */
    public static final int SPEICHER_EFFIZIENZ = 0;

    /**
     * Gibt die Nummer des Wertes der boolschen Attribute an (Null oder Eins),
     * nur für den Prädikate, jedoch auch negierte, d.h. invertierte Literale,
     * erzeugt werden sollen. Wenn der Wert negativ ist, werden für beide
     * Werte Prädikate erzeugt, jedoch keine negierten.
     */
    public static final int BOOL_WERT_PRAED_NUMMER = -1;

    /**
     * Gibt die Anzahl der Literale an, die maximal in einer Disjunktion bzw.
     * in einer Konjunktion enthalten sein sollen. Der Wert Null steht für
     * eine unbegrenzte Anzahl.
     */
    public static final int MAX_LITERAL_ANZ = 0;

    /**
     * Die maximale Anzahl der elementaren Funktionen in den zu erzeugenden
     * int-Funktionen.
     */
    public static final int MAX_INT_FUNK_KOMPLEX = 4;

    /**
     * Die maximale Anzahl der elementaren Funktionen in den zu erzeugenden
     * real-Funktionen.
     */
    public static final int MAX_REAL_FUNK_KOMPLEX = 4;

    /**
     * Die Anzahl der Iterationen des Gesamtverfahrens, also die Anzahl, wie
     * oft nacheinander ein bestes Konzept erzeugt werden soll. Der Wert muß
     * positiv sein.
     */
    public static final int GESAMT_ITER_ANZ = 1;

    /**
     * Die Anzahl der äußeren Iterationen, in denen korrekte oder vollständige
     * Konzepte aufgenommen werden und anschließend die mittlere Iteration
     * durchgeführt wird. Beim Wert Null wird iteriert, bis keine Verbesserung
     * mehr eintritt. Anderenfalls gibt der Absolutbetrag die Anzahl der
     * Iterationen an. Bei einem positiven Wert wird, wie beim Wert Null, in
     * jeder Iteration geprüft, ob nach einer Veränderung der Mengen
     * tatsächlich eine Verbesserung stattgefunden hat.
     */
    public static final int AEUSSERE_ITER_ANZ = -1;

    /**
     * Die Anzahl der mittleren Iterationen, in denen allgemeine Konzepte
     * aufgenommen werden und anschließend die innere Iteration durchgeführt
     * wird. Beim Wert Null wird iteriert, bis keine Verbesserung mehr
     * eintritt. Anderenfalls gibt der Absolutbetrag die Anzahl der
     * Iterationen an. Bei einem positiven Wert wird, wie beim Wert Null, in
     * jeder Iteration geprüft, ob nach einer Veränderung der Mengen
     * tatsächlich eine Verbesserung stattgefunden hat.
     */
    public static final int MITTLERE_ITER_ANZ = -1;

    /**
     * Die Anzahl der inneren Iterationen, in denen aus den allgemeinen
     * Konzepten korrekte oder vollständige Konzepte erzeugt werden. Beim Wert
     * Null wird iteriert, bis keine Verbesserung mehr eintritt. Anderenfalls
     * gibt der Absolutbetrag die Anzahl der Iterationen an. Bei einem
     * positiven Wert wird, wie beim Wert Null, in jeder Iteration geprüft, ob
     * nach einer Veränderung der Mengen tatsächlich eine Verbesserung
     * stattgefunden hat.
     */
    public static final int INNERE_ITER_ANZ = 0;

    /**
     * Gibt die maximale Anzahl von Hyperebenen an, die für die Erzeugung der
     * entsprechenden Prädikate erzeugt werden sollen. Der Wert Null steht für
     * eine unbeschränkte Anzahl.
     */
    public static final int MAX_HYP_EBENEN_ANZ = 20000;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen von allgemeinen
     * Konzepten zur Erzeugung spezieller Konzepte. Ein negativer Wert steht
     * für eine unbeschränkte Anzahl und es findet keine Auswahl statt.
     */
    public static final int ALG_SPEZ_ITM_ANZ = -1;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen von speziellen, d.h.
     * korrekten oder vollständigen, Konzepten zur Erzeugung von Formeln. Ein
     * negativer Wert steht für eine unbeschränkte Anzahl und es findet keine
     * Auswahl statt.
     */
    public static final int SPEZ_FORM_ITM_ANZ = -1;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * allgemeinen Konzepte bei der Optimierung eines speziellen Konzepts. Bei
     * einem negativen Wert werden alle Teilmengen aufgenommen und es findet
     * keine Auswahl statt. Beim Wert Null findet keine Optimierung statt.
     */
    public static final int OPT_SPEZ_ITM_ANZ = -1;

    /**
     * Die minimale Anzahl der zu speichernden Teilmengen zur Auswahl der
     * speziellen Konzepte bei der Optimierung eines finalen Konzepts. Bei
     * einem negativen Wert werden alle Teilmengen aufgenommen und es findet
     * keine Auswahl statt. Beim Wert Null findet keine Optimierung statt.
     */
    public static final int OPT_FIN_ITM_ANZ = -1;

    /**
     * Gibt die Anzahl der durchzuführenden Iterationen beim SCP-Verfahren bei
     * der Erzeugung eines speziellen Konzepts aus den allgemeinen Konzepten
     * an. Der Wert muß größer als Null sein.
     */
    public static final int ERZ_SPEZ_SCP_ITER_ANZ = 15;

    /**
     * Gibt die Anzahl der durchzuführenden Iterationen beim SCP-Verfahren bei
     * der Erzeugung einer Formel aus den speziellen, d.h. aus den korrekten
     * oder vollständigen, Konzepten an. Der Wert muß größer als Null sein.
     */
    public static final int ERZ_FORM_SCP_ITER_ANZ = 15;

    /**
     * Gibt die Anzahl der durchzuführenden Iterationen beim SCP-Verfahren bei
     * der Optimierung eines speziellen Konzepts an. Der Wert muß größer als
     * Null sein.
     */
    public static final int OPT_SPEZ_SCP_ITER_ANZ = 20;

    /**
     * Gibt die Anzahl der durchzuführenden Iterationen beim SCP-Verfahren bei
     * der Erzeugung einer finalen Formel aus den speziellen, d.h. aus den
     * korrekten oder vollständigen, Konzepten an. Der Wert muß größer als
     * Null sein.
     */
    public static final int FIN_FORM_SCP_ITER_ANZ = 40;

    /**
     * Gibt die Anzahl der durchzuführenden Iterationen beim SCP-Verfahren bei
     * der Optimierung einer finalen Formel an. Der Wert muß größer als Null
     * sein.
     */
    public static final int OPT_FIN_SCP_ITER_ANZ = 40;

    /**
     * Gibt an, ob eingelesene Beispiele mit unbekannten Attributwerten
     * verwendet werden sollen. Falls nicht, werden Beispiele mit solchen
     * Werten verworfen.
     */
    public static final boolean UNBEK_WERT_BSP = true;

    /**
     * Gibt an, ob der Zufallsgenerator bei jedem Lauf mit einem anderen Wert
     * initialisiert werden soll. Anderenfalls wird er jedes Mal mit dem
     * gleichen Wert initialisiert.
     */
    public static final boolean ZUFALL = false;

    /**
     * Gibt an, ob die Berechnungen mittels Architeuthis verteilt durchgeführt
     * werden sollen. Falls nein, wird eine lokale Berechnung durchgeführt.
     */
    public static final boolean VERTEILT = false;

    /**
     * Gibt an, ob boolsche Attribute negiert werden sollen, d.h. invertierte
     * Literale dazu erzeugt werden sollen.
     */
    public static final boolean NEG_BOOL_PRAED_ERZ = (BOOL_WERT_PRAED_NUMMER >= 0);

    /**
     * Gibt an, ob nach Erzeugung einer korrekten und vollständigen Formel
     * zusätzlich zum jeweils optimalen Konzept weitere spezielle Konzepte
     * erzeugt und in die Auswahl der speziellen Konzepte aufgenommen werden
     * sollen.
     */
    public static final boolean ZUSATZ_KONZ_ERZ = false;

    /**
     * Gibt an, ob Prädikate zum Vergleich von nominalen Attributen
     * untereinander erzeugt werden sollen.
     */
    public static final boolean NOM_NOM_PRAED_ERZ = false;

    /**
     * Gibt an, ob Prädikate zum Vergleich von int-Attributen untereinander
     * erzeugt werden sollen.
     */
    public static final boolean INT_INT_PRAED_ERZ = false;

    /**
     * Gibt an, ob Prädikate zum Vergleich von real-Attributen untereinander
     * erzeugt werden sollen.
     */
    public static final boolean REAL_REAL_PRAED_ERZ = false;

    /**
     * Gibt an, ob nur zu Halb-Intervallen Prädikate von Funktionen mit
     * ganzzahligen Werten erzeugt werden sollen. Falls nicht, werden
     * Prädikate zu Intervalle aller Art erzeugt.
     */
    public static final boolean NUR_HALB_INT_ITV = false;

    /**
     * Gibt an, ob nur zu Halb-Intervallen Prädikate von Funktionen mit
     * Fließkomma-Werten erzeugt werden sollen. Falls nicht, werden Prädikate
     * zu Intervallen aller Art erzeugt.
     */
    public static final boolean NUR_HALB_REAL_ITV = false;

    /**
     * Gibt an, ob Prädikate mittels Hyperebenen zu den real-Attributen
     * erzeugt werden sollen.
     */
    public static final boolean HYP_EBEN_PRAED_ERZ = false;

    /**
     * Gibt an, ob aus int-Attributen zusätzlich real-Attribute erzeugt werden
     * sollen.
     */
    public static final boolean ERG_REAL_VON_INT = false;
}

