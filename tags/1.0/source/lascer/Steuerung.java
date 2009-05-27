/*
 * Dateiname      : Steuerung.java
 * Letzte Änderung: 19. Dezember 2007
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.unistuttgart.commandline.Option;
import de.unistuttgart.commandline.ParameterParser;
import de.unistuttgart.commandline.ParameterParserException;

import lascer.intfunktionen.IntFunkSammlung;
import lascer.intfunktionen.IntFunkErzeugung;
import lascer.intfunktionen.arten.ZweiStellIntFunkErz;
import lascer.intfunktionen.arten.EinStellIntFunkErz;
import lascer.intfunktionen.arten.NullStellIntFunkErz;
import lascer.realfunktionen.RealFunkSammlung;
import lascer.realfunktionen.RealFunkErzeugung;
import lascer.realfunktionen.arten.ZweiStellRealFunkErz;
import lascer.realfunktionen.arten.EinStellRealFunkErz;
import lascer.realfunktionen.arten.NullStellRealFunkErz;
import lascer.problemdaten.AttributSammlung;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.attribute.IntAttribut;
import lascer.problemdaten.attribute.RealAttribut;
import lascer.praedikate.erzeugung.NomWertPraedErzeugung;
import lascer.praedikate.erzeugung.NomNomPraedErzeugung;
import lascer.praedikate.erzeugung.HypEbenPraedErzeugung;
import lascer.praedikate.erzeugung.intpraed.IntWertPraedErzeugung;
import lascer.praedikate.erzeugung.intpraed.IntIntPraedErzeugung;
import lascer.praedikate.erzeugung.intpraed.IntFunkPraedErzeugung;
import lascer.praedikate.erzeugung.realpraed.RealWertPraedErzeugung;
import lascer.praedikate.erzeugung.realpraed.RealRealPraedErzeugung;
import lascer.praedikate.erzeugung.realpraed.RealFunkPraedErzeugung;

/**
 * Klasse zur Verarbeitung der Konfigurations-Parameter und zur Erzeugung der
 * Prädikate.
 *
 * @author  Dietmar Lippold
 */
public class Steuerung {

    /**
     * Liefert den Parser für die Konfigurations-Parameter.
     *
     * @return  Den Parser für die Konfigurations-Parameter.
     */
    public static ParameterParser parser() {
        ParameterParser parser;
        Option          configOption;
        Option          unbekWertBspOption;
        Option          posPruneAntOption, negPruneAntOption;
        Option          verteiltOption, aenderWktOption;
        Option          teilprobAnzOption, dispatcherOption;
        Option          classServerNameOption, classServerPortOption;
        Option          zufallOption, speicherEffiOption;
        Option          boolWertPraedNrOption;
        Option          nomNomOption, intIntOption, realRealOption;
        Option          halbIntItvOption, halbRealItvOption;
        Option          hypEbenOption;
        Option          ergRealVonIntOption, zusatzKonzErzOption;
        Option          maxHypEbenAnzOption;
        Option          maxIntFunkKomplOption, maxRealFunkKomplOption;
        Option          gesamtIterAnzOption, aeussereIterAnzOption;
        Option          mittlereIterAnzOption, innereIterAnzOption;
        Option          algSpezItmAnzOption, spezFormItmAnzOption;
        Option          optSpezItmAnzOption, optFinItmAnzOption;
        Option          erzSpezScpIterAnzOption, erzFormScpIterAnzOption;
        Option          optSpezScpIterAnzOption, optFinScpIterAnzOption;
        Option          finFormScpIterAnzOption;
        Option          maxLitAnzOption, initGesKomplexOption;

        parser = new ParameterParser();

        unbekWertBspOption = new Option("unbekWertBsp");
        unbekWertBspOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        unbekWertBspOption.setParamDescription("[ja|nein]");
        unbekWertBspOption.setFullDescription("Gibt an, ob eingelesene Beispiele"
                                              + " mit unbekannten Attributwerten"
                                              + " verwendet werden sollen."
                                              + " (default: "
                                              + Konstanten.UNBEK_WERT_BSP + ")");
        parser.addOption(unbekWertBspOption);

        posPruneAntOption = new Option("posPruneAnt");
        posPruneAntOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        posPruneAntOption.setParamDescription("float");
        posPruneAntOption.setFullDescription("Der Prozent-Anteil der positiven"
                                             + " Beispiele, der vor der Erzeugung"
                                             + " einer Formel entfernt wird."
                                             + " (default: "
                                             + Konstanten.POS_PRUNE_ANT + ")");
        parser.addOption(posPruneAntOption);

        negPruneAntOption = new Option("negPruneAnt");
        negPruneAntOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        negPruneAntOption.setParamDescription("float");
        negPruneAntOption.setFullDescription("Der Prozent-Anteil der negativen"
                                             + " Beispiele, der vor der Erzeugung"
                                             + " einer Formel entfernt wird."
                                             + " (default: "
                                             + Konstanten.NEG_PRUNE_ANT + ")");
        parser.addOption(negPruneAntOption);

        zufallOption = new Option("zufall");
        zufallOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        zufallOption.setParamDescription("[ja|nein]");
        zufallOption.setFullDescription("Initialisiert den Zufallsgenerator"
                                        + " bei jedem Lauf mit einem anderen Wert."
                                        + " (default: " + Konstanten.ZUFALL
                                        + ")");
        parser.addOption(zufallOption);

        verteiltOption = new Option("verteilt");
        verteiltOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        verteiltOption.setParamDescription("[ja|nein]");
        verteiltOption.setFullDescription("Gibt an, ob die Berechnung mittels"
                                          + " Architeuthis verteilt durchgeführt"
                                          + " werden soll. (default: "
                                          + Konstanten.VERTEILT + ")");
        parser.addOption(verteiltOption);

        aenderWktOption = new Option("parallelAenderWkt");
        aenderWktOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        aenderWktOption.setParamDescription("float");
        aenderWktOption.setFullDescription("Die Wahrscheinlichkeit, mit der bei"
                                           + " einer parallelen Verarbeitung"
                                           + " mehrerer Teilprobleme die jeweilige"
                                           + " Bewertung der Beispiele modifiziert"
                                           + " werden soll. (default: "
                                           + Konstanten.PARALLEL_AENDER_WKT + ")");
        parser.addOption(aenderWktOption);

        teilprobAnzOption = new Option("lokaleTeilprobAnz");
        teilprobAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        teilprobAnzOption.setParamDescription("integer");
        teilprobAnzOption.setFullDescription("Die Anzahl der Teilprobleme, die"
                                             + " dem Problem bei einer lokalen"
                                             + " Berechnung vorgeschlagen"
                                             + " werden soll. (default: "
                                             + Konstanten.LOKALE_TEILPROB_ANZ
                                             + ")");
        parser.addOption(teilprobAnzOption);

        dispatcherOption = new Option("dispatcher");
        dispatcherOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        dispatcherOption.setParamDescription("hostname");
        dispatcherOption.setFullDescription("Für eine verteilte Berechnung der"
                                            + " Name des Rechners des"
                                            + " Dispatchers. (default: "
                                            + Konstanten.DISPATCHER + ")");
        parser.addOption(dispatcherOption);

        classServerNameOption = new Option("classServerName");
        classServerNameOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        classServerNameOption.setParamDescription("hostname");
        classServerNameOption.setFullDescription("Für eine verteilte Berechnung"
                                                 + " der Name des Rechners des"
                                                 + " Class-File-Servers."
                                                 + " (default: "
                                                 + Konstanten.CLASS_SERVER_NAME
                                                 + ")");
        parser.addOption(classServerNameOption);

        classServerPortOption = new Option("classServerPort");
        classServerPortOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        classServerPortOption.setParamDescription("integer");
        classServerPortOption.setFullDescription("Für eine verteilte Berechnung"
                                                 + " der Port des Class-File-Servers."
                                                 + " (default: "
                                                 + Konstanten.CLASS_SERVER_PORT
                                                 + ")");
        parser.addOption(classServerPortOption);

        boolWertPraedNrOption = new Option("boolWertPraedNummer");
        boolWertPraedNrOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        boolWertPraedNrOption.setParamDescription("[-1|0|1]");
        boolWertPraedNrOption.setFullDescription("Die Nummer des Wertes eines"
                                                 + " boolschen Attributs, nur"
                                                 + " der zur Erzeugung von"
                                                 + " Prädikaten verdendet werden"
                                                 + " soll, oder -1. (default: "
                                                 + Konstanten.BOOL_WERT_PRAED_NUMMER
                                                 + ")");
        parser.addOption(boolWertPraedNrOption);

        nomNomOption = new Option("nomNomPraedErz");
        nomNomOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        nomNomOption.setParamDescription("[ja|nein]");
        nomNomOption.setFullDescription("Gibt an, ob Prädikate zum Vergleich von"
                                        + " nominalen Attributen untereinander"
                                        + " erzeugt werden sollen. (default: "
                                        + Konstanten.NOM_NOM_PRAED_ERZ + ")");
        parser.addOption(nomNomOption);

        intIntOption = new Option("intIntPraedErz");
        intIntOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        intIntOption.setParamDescription("[ja|nein]");
        intIntOption.setFullDescription("Gibt an, ob Prädikate zum Vergleich von"
                                        + " int-Attributen untereinander erzeugt"
                                        + " werden sollen. (default: "
                                        + Konstanten.INT_INT_PRAED_ERZ + ")");
        parser.addOption(intIntOption);

        realRealOption = new Option("realRealPraedErz");
        realRealOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        realRealOption.setParamDescription("[ja|nein]");
        realRealOption.setFullDescription("Gibt an, ob Prädikate zum Vergleich"
                                          + " von real-Attributen untereinander"
                                          + " erzeugt werden sollen. (default: "
                                          + Konstanten.REAL_REAL_PRAED_ERZ + ")");
        parser.addOption(realRealOption);

        halbIntItvOption = new Option("nurHalbIntItv");
        halbIntItvOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        halbIntItvOption.setParamDescription("[ja|nein]");
        halbIntItvOption.setFullDescription("Gibt an, ob Prädikate bei"
                                            + " Funktionen mit ganzzahligen"
                                            + " Werten nur zu Halb-Intervallen"
                                            + " erzeugt werden sollen."
                                            + " (default: "
                                            + Konstanten.NUR_HALB_INT_ITV + ")");
        parser.addOption(halbIntItvOption);

        halbRealItvOption = new Option("nurHalbRealItv");
        halbRealItvOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        halbRealItvOption.setParamDescription("[ja|nein]");
        halbRealItvOption.setFullDescription("Gibt an, ob Prädikate bei"
                                             + " Funktionen mit Fließkomma-Werten"
                                             + " nur zu Halb-Intervallen"
                                             + " erzeugt werden sollen."
                                             + " (default: "
                                             + Konstanten.NUR_HALB_REAL_ITV + ")");
        parser.addOption(halbRealItvOption);

        hypEbenOption = new Option("hypEbenPraedErz");
        hypEbenOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        hypEbenOption.setParamDescription("[ja|nein]");
        hypEbenOption.setFullDescription("Gibt an, ob Prädikate mittels"
                                         + " Hyperebenen zu den real-Attributen"
                                         + " erzeugt werden sollen. (default: "
                                         + Konstanten.HYP_EBEN_PRAED_ERZ + ")");
        parser.addOption(hypEbenOption);

        ergRealVonIntOption = new Option("ergRealVonInt");
        ergRealVonIntOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        ergRealVonIntOption.setParamDescription("[ja|nein]");
        ergRealVonIntOption.setFullDescription("Gibt an, ob aus int-Attributen"
                                               + " zusätzlich real-Attribute"
                                               + " erzeugt werden sollen."
                                               + " (default: "
                                               + Konstanten.ERG_REAL_VON_INT
                                               + ")");
        parser.addOption(ergRealVonIntOption);

        zusatzKonzErzOption = new Option("zusatzKonzErz");
        zusatzKonzErzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        zusatzKonzErzOption.setParamDescription("[ja|nein]");
        zusatzKonzErzOption.setFullDescription("Gibt an, zusätzlich zum jeweils"
                                               + " optimalen Konzept weitere"
                                               + " spezielle Konzepte erzeugt"
                                               + " werden sollen. (default: "
                                               + Konstanten.ZUSATZ_KONZ_ERZ + ")");
        parser.addOption(zusatzKonzErzOption);

        speicherEffiOption = new Option("speicherEffizienz");
        speicherEffiOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        speicherEffiOption.setParamDescription("[0|1|2]");
        speicherEffiOption.setFullDescription("Gibt an, in welchem Ausmaß die"
                                              + " Teilmengen besonders"
                                              + " Speicher-effizient aber dadurch"
                                              + " weniger Laufzeit-effizient"
                                              + " verwaltet werden sollen."
                                              + " (default: "
                                              + Konstanten.SPEICHER_EFFIZIENZ
                                              + ")");
        parser.addOption(speicherEffiOption);

        maxHypEbenAnzOption = new Option("maxHypEbenAnz");
        maxHypEbenAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        maxHypEbenAnzOption.setParamDescription("integer");
        maxHypEbenAnzOption.setFullDescription("Gibt die maximale Anzahl von"
                                               + " zu erzeugender Hyperebenen"
                                               + " an. (default: "
                                               + Konstanten.MAX_HYP_EBENEN_ANZ
                                               + ")");
        parser.addOption(maxHypEbenAnzOption);

        maxIntFunkKomplOption = new Option("maxIntFunkKompl");
        maxIntFunkKomplOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        maxIntFunkKomplOption.setParamDescription("integer");
        maxIntFunkKomplOption.setFullDescription("Die maximale Anzahl der"
                                                 + " elementaren Funktionen in"
                                                 + " den zu erzeugenden"
                                                 + " int-Funktionen. (default: "
                                                 + Konstanten.MAX_INT_FUNK_KOMPLEX
                                                 + ")");
        parser.addOption(maxIntFunkKomplOption);

        maxRealFunkKomplOption = new Option("maxRealFunkKompl");
        maxRealFunkKomplOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        maxRealFunkKomplOption.setParamDescription("integer");
        maxRealFunkKomplOption.setFullDescription("Die maximale Anzahl der"
                                                  + " elementaren Funktionen in"
                                                  + " den zu erzeugenden"
                                                  + " real-Funktionen. (default: "
                                                  + Konstanten.MAX_REAL_FUNK_KOMPLEX
                                                  + ")");
        parser.addOption(maxRealFunkKomplOption);

        gesamtIterAnzOption = new Option("gesamtIterAnz");
        gesamtIterAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        gesamtIterAnzOption.setParamDescription("integer");
        gesamtIterAnzOption.setFullDescription("Die Anzahl der Iterationen des"
                                               + " Gesamtverfahrens. (default: "
                                               + Konstanten.GESAMT_ITER_ANZ
                                               + ")");
        parser.addOption(gesamtIterAnzOption);

        aeussereIterAnzOption = new Option("aeussereIterAnz");
        aeussereIterAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        aeussereIterAnzOption.setParamDescription("integer");
        aeussereIterAnzOption.setFullDescription("Die Anzahl der äußeren"
                                                 + " Iterationen mit Vorzeichen"
                                                 + " oder Null. (default: "
                                                 + Konstanten.AEUSSERE_ITER_ANZ
                                                 + ")");
        parser.addOption(aeussereIterAnzOption);

        mittlereIterAnzOption = new Option("mittlereIterAnz");
        mittlereIterAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        mittlereIterAnzOption.setParamDescription("integer");
        mittlereIterAnzOption.setFullDescription("Die Anzahl der mittleren"
                                                 + " Iterationen mit Vorzeichen"
                                                 + " oder Null. (default: "
                                                 + Konstanten.MITTLERE_ITER_ANZ
                                                 + ")");
        parser.addOption(mittlereIterAnzOption);

        innereIterAnzOption = new Option("innereIterAnz");
        innereIterAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        innereIterAnzOption.setParamDescription("integer");
        innereIterAnzOption.setFullDescription("Die Anzahl der inneren"
                                               + " Iterationen mit Vorzeichen"
                                               + " oder Null. (default: "
                                               + Konstanten.INNERE_ITER_ANZ
                                               + ")");
        parser.addOption(innereIterAnzOption);

        algSpezItmAnzOption = new Option("algSpezItmAnz");
        algSpezItmAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        algSpezItmAnzOption.setParamDescription("integer");
        algSpezItmAnzOption.setFullDescription("Die minimale Anzahl der zu"
                                               + " speichernden Teilmengen von"
                                               + " allgemeinen Konzepten zur"
                                               + " Erzeugung spezieller Konzepte."
                                               + " (default: "
                                               + Konstanten.ALG_SPEZ_ITM_ANZ
                                               + ")");
        parser.addOption(algSpezItmAnzOption);

        spezFormItmAnzOption = new Option("spezFormItmAnz");
        spezFormItmAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        spezFormItmAnzOption.setParamDescription("integer");
        spezFormItmAnzOption.setFullDescription("Die minimale Anzahl der zu"
                                                + " speichernden Teilmengen"
                                                + " von speziellen Konzepten"
                                                + " zur Erzeugung von Formeln."
                                                + " (default: "
                                                + Konstanten.SPEZ_FORM_ITM_ANZ
                                                + ")");
        parser.addOption(spezFormItmAnzOption);

        optSpezItmAnzOption = new Option("optSpezItmAnz");
        optSpezItmAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        optSpezItmAnzOption.setParamDescription("integer");
        optSpezItmAnzOption.setFullDescription("Die minimale Anzahl der zu"
                                               + " speichernden Teilmengen bei"
                                               + " der Optimierung eines"
                                               + " speziellen Konzepts."
                                               + " (default: "
                                               + Konstanten.OPT_SPEZ_ITM_ANZ
                                               + ")");
        parser.addOption(optSpezItmAnzOption);

        optFinItmAnzOption = new Option("optFinItmAnz");
        optFinItmAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        optFinItmAnzOption.setParamDescription("integer");
        optFinItmAnzOption.setFullDescription("Die minimale Anzahl der zu"
                                              + " speichernden Teilmengen bei"
                                              + " der Optimierung eines"
                                              + " finalen Konzepts. (default: "
                                              + Konstanten.OPT_FIN_ITM_ANZ
                                              + ")");
        parser.addOption(optFinItmAnzOption);

        erzSpezScpIterAnzOption = new Option("erzSpezScpIterAnz");
        erzSpezScpIterAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        erzSpezScpIterAnzOption.setParamDescription("integer");
        erzSpezScpIterAnzOption.setFullDescription("Die Anzahl der durchzuführenden"
                                                   + " Iterationen bei der Erzeugung"
                                                   + " eines speziellen Konzepts."
                                                   + " (default: "
                                                   + Konstanten.ERZ_SPEZ_SCP_ITER_ANZ
                                                   + ")");
        parser.addOption(erzSpezScpIterAnzOption);

        erzFormScpIterAnzOption = new Option("erzFormScpIterAnz");
        erzFormScpIterAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        erzFormScpIterAnzOption.setParamDescription("integer");
        erzFormScpIterAnzOption.setFullDescription("Die Anzahl der durchzuführenden"
                                                   + " Iterationen bei der Erzeugung"
                                                   + " einer Formel. (default: "
                                                   + Konstanten.ERZ_FORM_SCP_ITER_ANZ
                                                   + ")");
        parser.addOption(erzFormScpIterAnzOption);

        optSpezScpIterAnzOption = new Option("optSpezScpIterAnz");
        optSpezScpIterAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        optSpezScpIterAnzOption.setParamDescription("integer");
        optSpezScpIterAnzOption.setFullDescription("Die Anzahl der durchzuführenden"
                                                   + " Iterationen bei der"
                                                   + " Optimierung eines speziellen"
                                                   + " Konzepts. (default: "
                                                   + Konstanten.OPT_SPEZ_SCP_ITER_ANZ
                                                   + ")");
        parser.addOption(optSpezScpIterAnzOption);

        optFinScpIterAnzOption = new Option("optFinScpIterAnz");
        optFinScpIterAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        optFinScpIterAnzOption.setParamDescription("integer");
        optFinScpIterAnzOption.setFullDescription("Die Anzahl der durchzuführenden"
                                                  + " Iterationen bei der"
                                                  + " Optimierung einer finalen"
                                                  + " Formel. (default: "
                                                  + Konstanten.OPT_FIN_SCP_ITER_ANZ
                                                  + ")");
        parser.addOption(optFinScpIterAnzOption);

        finFormScpIterAnzOption = new Option("finFormScpIterAnz");
        finFormScpIterAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        finFormScpIterAnzOption.setParamDescription("integer");
        finFormScpIterAnzOption.setFullDescription("Die Anzahl der durchzuführenden"
                                                   + " Iterationen bei der"
                                                   + " erzeugung einer finalen"
                                                   + " Formel. (default: "
                                                   + Konstanten.FIN_FORM_SCP_ITER_ANZ
                                                   + ")");
        parser.addOption(finFormScpIterAnzOption);

        maxLitAnzOption = new Option("maxLitAnz");
        maxLitAnzOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        maxLitAnzOption.setParamDescription("integer");
        maxLitAnzOption.setFullDescription("Die Anzahl der Literale, die maximal"
                                           + " in einer Disjunktion bzw. Konjunktion"
                                           + " enthalten sein sollen. (default: "
                                           + Konstanten.MAX_LITERAL_ANZ + ")");
        parser.addOption(maxLitAnzOption);

        initGesKomplexOption = new Option("initGesamtKomplex");
        initGesKomplexOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        initGesKomplexOption.setParamDescription("float");
        initGesKomplexOption.setFullDescription("Die initial geschätzte maximale"
                                                + " Gesamt-Komplexität einer"
                                                + " Formel. (default: "
                                                + Konstanten.INIT_GESAMT_KOMPLEX
                                                + ")");
        parser.addOption(initGesKomplexOption);

        configOption = new Option("configParam");
        configOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        configOption.setParamDescription("filename");
        configOption.setFullDescription("Der Name einer Datei mit Parametern"
                                        + " zur Konfiguration.");
        parser.addOption(configOption);

        return parser;
    }

    /**
     * Liest die Werte der übergebenen Parameter ein. Das Array der Parameter
     * (die Kommandozeile) muß beim übergebenen Parser bereits gesetzt worden
     * sein.
     *
     * @param parser  Der zum Einlesen der Parameter verwendete Parser.
     *
     * @throws ParameterParserException  Bei einem Fehler beim Parsen der
     *                                   Parameter.
     * @throws FileNotFoundException     Wenn die als Option angegebene
     *                                   Properties-Datei nicht existiert.
     * @throws IOException               Wenn die als Option angegebene
     *                                   Properties-Datei nicht gelesen werden
     *                                   kann.
     */
    public static void parseArguments(ParameterParser parser)
        throws ParameterParserException, FileNotFoundException, IOException {

        Properties props;
        HashMap    propsHashMap;
        Option     configOption;
        String     configDatei = "configDatei";

        // Option von config-Datei ermitteln.
        configOption = parser.getOption("configParam");
        parser.parseOption(configOption);
        if (parser.isEnabled(configOption)) {
            try {
                // Es wird ein vorgegebenes properties file verwendet.
                configDatei = parser.getParameter(configOption);

                // Daten vom properties file laden
                props = new Properties();
                props.load(new FileInputStream(configDatei));

                // ... und im Kommandozeilenparser zusammen mit den
                // Kommandozeilen-Parametern parsen.
                propsHashMap = new HashMap(props);
                parser.setProperties(propsHashMap);
                parser.parseAll();
            } catch (FileNotFoundException e) {
                throw (new FileNotFoundException("Datei " + configDatei
                                                 + " nicht gefunden"));
            }
        } else {
            // Kommandozeilenparametern ohne properties-Datei parsen.
            parser.parseAll();
        }
    }

    /**
     * Ermittet anhand der Parameter des übergebenen Parsers, ob eingelesene
     * Beispiele mit unbekannten Attributwerten verwendet werden sollen.
     *
     * @param parser  Der zum Einlesen der Parameter verwendete Parser.
     *
     * @return  <CODE>true</CODE>, wenn eingelesene Beispiele mit unbekannten
     *          Attributwerten verwendet werden sollen, anderenfalls
     *          <CODE>false</CODE>.
     *
     * @throws IllegalArgumentException  Wenn der Parameter einen unzulässigen
     *                                   Wert hat.
     */
    public static boolean unbekannteWertBeispiele(ParameterParser parser)
        throws IllegalArgumentException {

        Option  unbekWertBspOption;
        String  unbekWertBspText;
        String  message;
        boolean unbekannteWertBsp;

        unbekannteWertBsp = Konstanten.UNBEK_WERT_BSP;
        unbekWertBspOption = parser.getOption("unbekWertBsp");
        if (parser.isEnabled(unbekWertBspOption)) {
            unbekWertBspText = parser.getParameter(unbekWertBspOption);
            if (unbekWertBspText.equalsIgnoreCase("ja")) {
                unbekannteWertBsp = true;
            } else if (unbekWertBspText.equalsIgnoreCase("nein")) {
                unbekannteWertBsp = false;
            } else {
                message = "Wert der Option unbekWertBsp unzulässig\n";
                message += "Zulässig: " + unbekWertBspOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        return unbekannteWertBsp;
    }

    /**
     * Ermittet anhand der Parameter des übergebenen Parsers den Anteil der
     * positiven Beispiele in Prozent, der vor der Erzeugung der Formel bzw.
     * des Konzepts entfernt werden soll.
     *
     * @param parser  Der zum Einlesen der Parameter verwendete Parser.
     *
     * @return  Den Anteil der positiven Beispiele in Prozent, der vor der
     *          Erzeugung der Formel bzw. des Konzepts entfernt werden soll.
     */
    public static float posPruneAnteil(ParameterParser parser) {
        Option posPruneAntOption;
        float  posPruneAnteil;

        posPruneAnteil = Konstanten.POS_PRUNE_ANT;
        posPruneAntOption = parser.getOption("posPruneAnt");
        if (parser.isEnabled(posPruneAntOption)) {
            posPruneAnteil = parser.getParameterAsFloat(posPruneAntOption);
        }

        return posPruneAnteil;
    }

    /**
     * Ermittet anhand der Parameter des übergebenen Parsers den Anteil der
     * negativen Beispiele in Prozent, der vor der Erzeugung der Formel bzw.
     * des Konzepts entfernt werden soll.
     *
     * @param parser  Der zum Einlesen der Parameter verwendete Parser.
     *
     * @return  Den Anteil der negativen Beispiele in Prozent, der vor der
     *          Erzeugung der Formel bzw. des Konzepts entfernt werden soll.
     */
    public static float negPruneAnteil(ParameterParser parser) {
        Option negPruneAntOption;
        float  negPruneAnteil;

        negPruneAnteil = Konstanten.NEG_PRUNE_ANT;
        negPruneAntOption = parser.getOption("negPruneAnt");
        if (parser.isEnabled(negPruneAntOption)) {
            negPruneAnteil = parser.getParameterAsFloat(negPruneAntOption);
        }

        return negPruneAnteil;
    }

    /**
     * Liefert die eingelesenen Parameter für die Erzeugung der Prädikate.
     *
     * @param parser  Der zum Einlesen der Parameter verwendete Parser.
     *
     * @return  Die eingelesenen Parameter für die Erzeugung der Prädikate.
     *
     * @throws IllegalArgumentException  Wenn einer der Parameter einen
     *                                   unzulässigen Wert hat.
     */
    public static PraedErzParameter praedErzParameter(ParameterParser parser)
        throws IllegalArgumentException {

        PraedErzParameter praedErzParameter = new PraedErzParameter();
        Option            boolWertPraedNrOption;
        Option            nomNomOption;
        Option            intIntOption;
        Option            realRealOption;
        Option            halbIntItvOption;
        Option            halbRealItvOption;
        Option            hypEbenOption;
        Option            ergRealVonIntOption;
        Option            maxHypEbenAnzOption;
        Option            maxIntFunkKomplOption;
        Option            maxRealFunkKomplOption;
        String            message;
        String            optionWert;

        nomNomOption = parser.getOption("nomNomPraedErz");
        if (parser.isEnabled(nomNomOption)) {
            optionWert = parser.getParameter(nomNomOption);
            if (optionWert.equalsIgnoreCase("ja")) {
                praedErzParameter.setNomNomPraedErz(true);
            } else if (optionWert.equalsIgnoreCase("nein")) {
                praedErzParameter.setNomNomPraedErz(false);
            } else {
                message = "Wert der Option nomNomPraedErz unzulässig\n";
                message += "Zulässig: " + nomNomOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        intIntOption = parser.getOption("intIntPraedErz");
        if (parser.isEnabled(intIntOption)) {
            optionWert = parser.getParameter(intIntOption);
            if (optionWert.equalsIgnoreCase("ja")) {
                praedErzParameter.setIntIntPraedErz(true);
            } else if (optionWert.equalsIgnoreCase("nein")) {
                praedErzParameter.setIntIntPraedErz(false);
            } else {
                message = "Wert der Option intIntPraedErz unzulässig\n";
                message += "Zulässig: " + intIntOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        realRealOption = parser.getOption("realRealPraedErz");
        if (parser.isEnabled(realRealOption)) {
            optionWert = parser.getParameter(realRealOption);
            if (optionWert.equalsIgnoreCase("ja")) {
                praedErzParameter.setRealRealPraedErz(true);
            } else if (optionWert.equalsIgnoreCase("nein")) {
                praedErzParameter.setRealRealPraedErz(false);
            } else {
                message = "Wert der Option realRealPraedErz unzulässig\n";
                message += "Zulässig: " + realRealOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        halbIntItvOption = parser.getOption("nurHalbIntItv");
        if (parser.isEnabled(halbIntItvOption)) {
            optionWert = parser.getParameter(halbIntItvOption);
            if (optionWert.equalsIgnoreCase("ja")) {
                praedErzParameter.setNurHalbIntItv(true);
            } else if (optionWert.equalsIgnoreCase("nein")) {
                praedErzParameter.setNurHalbIntItv(false);
            } else {
                message = "Wert der Option nurHalbIntItv unzulässig\n";
                message += "Zulässig: " + halbIntItvOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        halbRealItvOption = parser.getOption("nurHalbRealItv");
        if (parser.isEnabled(halbRealItvOption)) {
            optionWert = parser.getParameter(halbRealItvOption);
            if (optionWert.equalsIgnoreCase("ja")) {
                praedErzParameter.setNurHalbRealItv(true);
            } else if (optionWert.equalsIgnoreCase("nein")) {
                praedErzParameter.setNurHalbRealItv(false);
            } else {
                message = "Wert der Option nurHalbRealItv unzulässig\n";
                message += "Zulässig: " + halbRealItvOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        hypEbenOption = parser.getOption("hypEbenPraedErz");
        if (parser.isEnabled(hypEbenOption)) {
            optionWert = parser.getParameter(hypEbenOption);
            if (optionWert.equalsIgnoreCase("ja")) {
                praedErzParameter.setHypEbenPraedErz(true);
            } else if (optionWert.equalsIgnoreCase("nein")) {
                praedErzParameter.setHypEbenPraedErz(false);
            } else {
                message = "Wert der Option hypEbenOption unzulässig\n";
                message += "Zulässig: " + hypEbenOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        ergRealVonIntOption = parser.getOption("ergRealVonInt");
        if (parser.isEnabled(ergRealVonIntOption)) {
            optionWert = parser.getParameter(ergRealVonIntOption);
            if (optionWert.equalsIgnoreCase("ja")) {
                praedErzParameter.setErgRealVonInt(true);
            } else if (optionWert.equalsIgnoreCase("nein")) {
                praedErzParameter.setErgRealVonInt(false);
            } else {
                message = "Wert der Option ergRealVonInt unzulässig\n";
                message += "Zulässig: " + ergRealVonIntOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        boolWertPraedNrOption = parser.getOption("boolWertPraedNummer");
        if (parser.isEnabled(boolWertPraedNrOption)) {
            int boolWertPraedNr = parser.getParameterAsInt(boolWertPraedNrOption);
            if ((boolWertPraedNr >= -1) && (boolWertPraedNr <= 1)) {
                praedErzParameter.setBoolWertPraedNummer(boolWertPraedNr);
            } else {
                message = "Wert der Option boolWertPraedNummer unzulässig\n";
                message += "Zulässig: " + boolWertPraedNrOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        maxHypEbenAnzOption = parser.getOption("maxHypEbenAnz");
        if (parser.isEnabled(maxHypEbenAnzOption)) {
            int maxHypEbenAnz = parser.getParameterAsInt(maxHypEbenAnzOption);
            praedErzParameter.setMaxHypEbenAnz(maxHypEbenAnz);
        }

        maxIntFunkKomplOption = parser.getOption("maxIntFunkKompl");
        if (parser.isEnabled(maxIntFunkKomplOption)) {
            int maxIntFunkKomplex = parser.getParameterAsInt(maxIntFunkKomplOption);
            praedErzParameter.setMaxIntFunkKomplex(maxIntFunkKomplex);
        }

        maxRealFunkKomplOption = parser.getOption("maxRealFunkKompl");
        if (parser.isEnabled(maxRealFunkKomplOption)) {
            int maxRealFunkKomplex = parser.getParameterAsInt(maxRealFunkKomplOption);
            praedErzParameter.setMaxRealFunkKomplex(maxRealFunkKomplex);
        }

        return praedErzParameter;
    }

    /**
     * Liefert die eingelesenen Parameter für die Erzeugung der Konzpte.
     *
     * @param parser  Der zum Einlesen der Parameter verwendete Parser.
     *
     * @return  Die eingelesenen Parameter für die Erzeugung der Konzpte.
     *
     * @throws IllegalArgumentException  Wenn einer der Parameter einen
     *                                   unzulässigen Wert hat.
     */
    public static KonzErzParameter konzErzParameter(ParameterParser parser)
        throws IllegalArgumentException {

        KonzErzParameter konzErzParameter = new KonzErzParameter();
        Option           verteiltOption;
        Option           zufallOption;
        Option           boolWertPraedNrOption;
        Option           zusatzKonzErzOption;
        Option           speicherEffiOption;
        Option           aenderWktOption;
        Option           initGesKomplexOption;
        Option           teilprobAnzOption;
        Option           dispatcherOption;
        Option           classServerNameOption;
        Option           classServerPortOption;
        Option           maxLitAnzOption;
        Option           gesamtIterAnzOption;
        Option           aeussereIterAnzOption;
        Option           mittlereIterAnzOption;
        Option           innereIterAnzOption;
        Option           algSpezItmAnzOption;
        Option           spezFormItmAnzOption;
        Option           optSpezItmAnzOption;
        Option           optFinItmAnzOption;
        Option           erzSpezScpIterAnzOption;
        Option           erzFormScpIterAnzOption;
        Option           optSpezScpIterAnzOption;
        Option           optFinScpIterAnzOption;
        Option           finFormScpIterAnzOption;
        String           message;
        String           optionWert;

        verteiltOption = parser.getOption("verteilt");
        if (parser.isEnabled(verteiltOption)) {
            optionWert = parser.getParameter(verteiltOption);
            if (optionWert.equalsIgnoreCase("ja")) {
                konzErzParameter.setVerteilt(true);
            } else if (optionWert.equalsIgnoreCase("nein")) {
                konzErzParameter.setVerteilt(false);
            } else {
                message = "Wert der Option verteilt unzulässig\n";
                message += "Zulässig: " + verteiltOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        zufallOption = parser.getOption("zufall");
        if (parser.isEnabled(zufallOption)) {
            optionWert = parser.getParameter(zufallOption);
            if (optionWert.equalsIgnoreCase("ja")) {
                konzErzParameter.setZufall(true);
            } else if (optionWert.equalsIgnoreCase("nein")) {
                konzErzParameter.setZufall(false);
            } else {
                message = "Wert der Option zufall unzulässig\n";
                message += "Zulässig: " + zufallOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        zusatzKonzErzOption = parser.getOption("zusatzKonzErz");
        if (parser.isEnabled(zusatzKonzErzOption)) {
            optionWert = parser.getParameter(zusatzKonzErzOption);
            if (optionWert.equalsIgnoreCase("ja")) {
                konzErzParameter.setZusatzKonzErz(true);
            } else if (optionWert.equalsIgnoreCase("nein")) {
                konzErzParameter.setZusatzKonzErz(false);
            } else {
                message = "Wert der Option zusatzKonzErz unzulässig\n";
                message += "Zulässig: " + zusatzKonzErzOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        speicherEffiOption = parser.getOption("speicherEffizienz");
        if (parser.isEnabled(speicherEffiOption)) {
            int speicherEffizienz = parser.getParameterAsInt(speicherEffiOption);
            if ((speicherEffizienz >= 0) && (speicherEffizienz <= 2)) {
                konzErzParameter.setSpeicherEffizienz(speicherEffizienz);
            } else {
                message = "Wert der Option speicherEffizienz unzulässig\n";
                message += "Zulässig: " + speicherEffiOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        boolWertPraedNrOption = parser.getOption("boolWertPraedNummer");
        if (parser.isEnabled(boolWertPraedNrOption)) {
            int boolWertPraedNr = parser.getParameterAsInt(boolWertPraedNrOption);
            if ((boolWertPraedNr >= -1) && (boolWertPraedNr <= 1)) {
                konzErzParameter.setNegBoolPraedErz(boolWertPraedNr >= 0);
            } else {
                message = "Wert der Option boolWertPraedNummer unzulässig\n";
                message += "Zulässig: " + boolWertPraedNrOption.toString() + "\n";
                throw (new IllegalArgumentException(message));
            }
        }

        aenderWktOption = parser.getOption("parallelAenderWkt");
        if (parser.isEnabled(aenderWktOption)) {
            float parallelAenderWkt = parser.getParameterAsFloat(aenderWktOption);
            konzErzParameter.setParallelAenderWkt(parallelAenderWkt);
        }

        initGesKomplexOption = parser.getOption("initGesamtKomplex");
        if (parser.isEnabled(initGesKomplexOption)) {
            float initGesamtKomplex = parser.getParameterAsFloat(initGesKomplexOption);
            konzErzParameter.setInitGesamtKomplex(initGesamtKomplex);
        }

        teilprobAnzOption = parser.getOption("lokaleTeilprobAnz");
        if (parser.isEnabled(teilprobAnzOption)) {
            int lokaleTeilprobAnz = parser.getParameterAsInt(teilprobAnzOption);
            konzErzParameter.setLokaleTeilprobAnz(lokaleTeilprobAnz);
        }

        dispatcherOption = parser.getOption("dispatcher");
        if (parser.isEnabled(dispatcherOption)) {
            String dispatcher = parser.getParameter(dispatcherOption);
            konzErzParameter.setDispatcher(dispatcher);

        }

        classServerNameOption = parser.getOption("classServerName");
        if (parser.isEnabled(classServerNameOption)) {
            String classServerName = parser.getParameter(classServerNameOption);
            konzErzParameter.setClassServerName(classServerName);

        }

        classServerPortOption = parser.getOption("classServerPort");
        if (parser.isEnabled(classServerPortOption)) {
            int classServerPort = parser.getParameterAsInt(classServerPortOption);
            konzErzParameter.setClassServerPort(classServerPort);

        }

        maxLitAnzOption = parser.getOption("maxLitAnz");
        if (parser.isEnabled(maxLitAnzOption)) {
            int maxLitAnz = parser.getParameterAsInt(maxLitAnzOption);
            konzErzParameter.setMaxLitAnz(maxLitAnz);
        }

        gesamtIterAnzOption = parser.getOption("gesamtIterAnz");
        if (parser.isEnabled(gesamtIterAnzOption)) {
            int gesamtIterAnz = parser.getParameterAsInt(gesamtIterAnzOption);
            konzErzParameter.setGesamtIterAnz(gesamtIterAnz);
        }

        aeussereIterAnzOption = parser.getOption("aeussereIterAnz");
        if (parser.isEnabled(aeussereIterAnzOption)) {
            int aeussereIterAnz = parser.getParameterAsInt(aeussereIterAnzOption);
            konzErzParameter.setAeussereIterAnz(aeussereIterAnz);
        }

        mittlereIterAnzOption = parser.getOption("mittlereIterAnz");
        if (parser.isEnabled(mittlereIterAnzOption)) {
            int mittlereIterAnz = parser.getParameterAsInt(mittlereIterAnzOption);
            konzErzParameter.setMittlereIterAnz(mittlereIterAnz);
        }

        innereIterAnzOption = parser.getOption("innereIterAnz");
        if (parser.isEnabled(innereIterAnzOption)) {
            int innereIterAnz = parser.getParameterAsInt(innereIterAnzOption);
            konzErzParameter.setInnereIterAnz(innereIterAnz);
        }

        algSpezItmAnzOption = parser.getOption("algSpezItmAnz");
        if (parser.isEnabled(algSpezItmAnzOption)) {
            int algSpezItmAnz = parser.getParameterAsInt(algSpezItmAnzOption);
            konzErzParameter.setAlgSpezItmAnz(algSpezItmAnz);
        }

        spezFormItmAnzOption = parser.getOption("spezFormItmAnz");
        if (parser.isEnabled(spezFormItmAnzOption)) {
            int spezFormItmAnz = parser.getParameterAsInt(spezFormItmAnzOption);
            konzErzParameter.setSpezFormItmAnz(spezFormItmAnz);
        }

        optSpezItmAnzOption = parser.getOption("optSpezItmAnz");
        if (parser.isEnabled(optSpezItmAnzOption)) {
            int optSpezItmAnz = parser.getParameterAsInt(optSpezItmAnzOption);
            konzErzParameter.setOptSpezItmAnz(optSpezItmAnz);
        }

        optFinItmAnzOption = parser.getOption("optFinItmAnz");
        if (parser.isEnabled(optFinItmAnzOption)) {
            int optFinItmAnz = parser.getParameterAsInt(optFinItmAnzOption);
            konzErzParameter.setOptFinItmAnz(optFinItmAnz);
        }

        erzSpezScpIterAnzOption = parser.getOption("erzSpezScpIterAnz");
        if (parser.isEnabled(erzSpezScpIterAnzOption)) {
            int erzSpezScpIterAnz = parser.getParameterAsInt(erzSpezScpIterAnzOption);
            if (erzSpezScpIterAnz > 0) {
                konzErzParameter.setErzSpezScpIterAnz(erzSpezScpIterAnz);
            } else {
                message = "Wert der Option erzSpezScpIterAnz unzulässig\n";
                message += "Wert muß größer als Null sein.\n";
                throw (new IllegalArgumentException(message));
            }
        }

        erzFormScpIterAnzOption = parser.getOption("erzFormScpIterAnz");
        if (parser.isEnabled(erzFormScpIterAnzOption)) {
            int erzFormScpIterAnz = parser.getParameterAsInt(erzFormScpIterAnzOption);
            if (erzFormScpIterAnz > 0) {
                konzErzParameter.setErzFormScpIterAnz(erzFormScpIterAnz);
            } else {
                message = "Wert der Option erzFormScpIterAnz unzulässig\n";
                message += "Wert muß größer als Null sein.\n";
                throw (new IllegalArgumentException(message));
            }
        }

        optSpezScpIterAnzOption = parser.getOption("optSpezScpIterAnz");
        if (parser.isEnabled(optSpezScpIterAnzOption)) {
            int optSpezScpIterAnz = parser.getParameterAsInt(optSpezScpIterAnzOption);
            if (optSpezScpIterAnz > 0) {
                konzErzParameter.setOptSpezScpIterAnz(optSpezScpIterAnz);
            } else {
                message = "Wert der Option optSpezScpIterAnz unzulässig\n";
                message += "Wert muß größer als Null sein.\n";
                throw (new IllegalArgumentException(message));
            }
        }

        optFinScpIterAnzOption = parser.getOption("optFinScpIterAnz");
        if (parser.isEnabled(optFinScpIterAnzOption)) {
            int optFinScpIterAnz = parser.getParameterAsInt(optFinScpIterAnzOption);
            if (optFinScpIterAnz > 0) {
                konzErzParameter.setOptFinScpIterAnz(optFinScpIterAnz);
            } else {
                message = "Wert der Option optFinScpIterAnz unzulässig\n";
                message += "Wert muß größer als Null sein.\n";
                throw (new IllegalArgumentException(message));
            }
        }

        finFormScpIterAnzOption = parser.getOption("finFormScpIterAnz");
        if (parser.isEnabled(finFormScpIterAnzOption)) {
            int finFormScpIterAnz = parser.getParameterAsInt(finFormScpIterAnzOption);
            if (finFormScpIterAnz > 0) {
                konzErzParameter.setFinFormScpIterAnz(finFormScpIterAnz);
            } else {
                message = "Wert der Option finFormScpIterAnz unzulässig\n";
                message += "Wert muß größer als Null sein.\n";
                throw (new IllegalArgumentException(message));
            }
        }

        return konzErzParameter;
    }

    /**
     * Setzt den Logging-Level für den ConsoleHandler am root-Logger und für
     * den root-Logger selbst.
     *
     * @param level  Der Logging-Level, auf den der ConsoleHandler am
     *               root-Logger und für den root-Logger selbst gesetzt werden
     *               sollen.
     */
    public static void setLogLevel(Level level) {
        Handler[]      handlers;
        Logger         rootLogger;
        ConsoleHandler consoleHandler;

        consoleHandler = null;

        // Den DefaultHandler ermitteln, der am root-Logger hängt.
        rootLogger = Logger.getLogger("");
        handlers = rootLogger.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            if (handlers[i] instanceof ConsoleHandler) {
                consoleHandler = (ConsoleHandler) handlers[i];
            }
        }

        // Wenn kein ConsoleHandler am root-Logger vorhanden war, selbst einen
        // erzeugen und anhängen.
        if (consoleHandler == null) {
            consoleHandler = new ConsoleHandler();
            Logger.getLogger("").addHandler(consoleHandler);
        }

        consoleHandler.setLevel(level);
        rootLogger.setLevel(level);
    }

    /**
     * Erzeugt aus den ganzzahligen Attributen das übergebenen Datensatzes
     * nicht-ganzzahlige Attribute und fügt diese dem Datensatz hinzu, wobei
     * die Daten der Beispiele entsprechend ergänzt werden.
     *
     * @param beispieldaten  Der Datensatz, aus dem die ganzzahligen Attribute
     *                       entnommen und zu dem die nicht-ganzzahligen
     *                       Attribute hinzugefügt werden.
     */
    private static void ergRealVonInt(Beispieldaten beispieldaten) {
        AttributSammlung attributSammlung;
        RealAttribut     realAttrib;
        IntAttribut      intAttrib;
        float[]          posRealWerte, negRealWerte;
        int[]            posIntWerte, negIntWerte;

        posRealWerte = new float[beispieldaten.posBspAnz()];
        negRealWerte = new float[beispieldaten.negBspAnz()];

        attributSammlung = beispieldaten.attributSammlung();
        for (int aNr = 0; aNr < attributSammlung.intAttributAnz(); aNr++) {
            intAttrib = attributSammlung.getIntAttribut(aNr);
            realAttrib = new RealAttribut(intAttrib.toString()
                                          + Konstanten.INT_REAL_ATTRIB_ANHANG);

            posIntWerte = beispieldaten.getIntWerte(intAttrib, true);
            for (int i = 0; i < posIntWerte.length; i++) {
                posRealWerte[i] = posIntWerte[i];
            }

            negIntWerte = beispieldaten.getIntWerte(intAttrib, false);
            for (int i = 0; i < negIntWerte.length; i++) {
                negRealWerte[i] = negIntWerte[i];
            }

            beispieldaten.addRealAttribut(realAttrib, posRealWerte, negRealWerte);
        }
    }

    /**
     * Liefert eine Sammlung der int-Funktionen, die zur Erzeugung von
     * Prädikaten verwendet werden sollen.
     *
     * @param attributSammlung       Der Sammung der Attribute, zu denen die
     *                               Funktionen erzeugt werden sollen.
     * @param zweiFunkErzeugerNamen  Die Namen der Erzeugerklassen der
     *                               zweistelligen int-Funktionen.
     * @param einFunkErzeugerNamen   Die Namen der Erzeugerklassen der
     *                               einstelligen int-Funktionen.
     * @param nullFunkErzeugerNamen  Die Namen der Erzeugerklassen der
     *                               nullstelligen int-Funktionen.
     *
     * @return  Eine Sammlung der int-Funktionen, die zur Erzeugung von
     *          Prädikaten verwendet werden sollen.
     */
    private static IntFunkSammlung intFunkSammlung(AttributSammlung attributSammlung,
                                                   String[] zweiFunkErzeugerNamen,
                                                   String[] einFunkErzeugerNamen,
                                                   String[] nullFunkErzeugerNamen) {
        IntFunkSammlung      funkSammlung;
        ZweiStellIntFunkErz  zweiStellErzeuger;
        EinStellIntFunkErz   einStellErzeuger;
        NullStellIntFunkErz  nullStellErzeuger;
        Class                zweiStellErzKlasse;
        Class                einStellErzKlasse;
        Class                nullStellErzKlasse;
        String               klassenName;

        funkSammlung = new IntFunkSammlung(attributSammlung);

        // Erzeugung der Erzeuger der zweistelligen Funktionen
        for (int funkNr = 0; funkNr < zweiFunkErzeugerNamen.length; funkNr++) {
            klassenName = Konstanten.INT_FUNK_ERZEUGER_PACKAGE
                          + "." + zweiFunkErzeugerNamen[funkNr];
            try {
                zweiStellErzKlasse = Class.forName(klassenName);
                zweiStellErzeuger = (ZweiStellIntFunkErz) zweiStellErzKlasse.newInstance();
                funkSammlung.zweiStellErzeugerAufnehmen(zweiStellErzeuger);
            } catch (Exception e) {
                System.err.println("Fehler beim Erzeugen der Klasse "
                                   + zweiFunkErzeugerNamen[funkNr]);
            }
        }

        // Erzeugung der Erzeuger der einstelligen Funktionen
        for (int funkNr = 0; funkNr < einFunkErzeugerNamen.length; funkNr++) {
            klassenName = Konstanten.INT_FUNK_ERZEUGER_PACKAGE
                          + "." + einFunkErzeugerNamen[funkNr];
            try {
                einStellErzKlasse = Class.forName(klassenName);
                einStellErzeuger = (EinStellIntFunkErz) einStellErzKlasse.newInstance();
                funkSammlung.einStellErzeugerAufnehmen(einStellErzeuger);
            } catch (Exception e) {
                System.err.println("Fehler beim Erzeugen der Klasse "
                                   + einFunkErzeugerNamen[funkNr]);
            }
        }

        // Erzeugung der Erzeuger der nullstelligen Funktionen
        for (int funkNr = 0; funkNr < nullFunkErzeugerNamen.length; funkNr++) {
            klassenName = Konstanten.INT_FUNK_ERZEUGER_PACKAGE
                          + "." + nullFunkErzeugerNamen[funkNr];
            try {
                nullStellErzKlasse = Class.forName(klassenName);
                nullStellErzeuger = (NullStellIntFunkErz) nullStellErzKlasse.newInstance();
                funkSammlung.nullStellErzeugerAufnehmen(nullStellErzeuger);
            } catch (Exception e) {
                System.err.println("Fehler beim Erzeugen der Klasse "
                                   + nullFunkErzeugerNamen[funkNr]);
            }
        }

        return funkSammlung;
    }

    /**
     * Liefert eine Sammlung der real-Funktionen, die zur Erzeugung von
     * Prädikaten verwendet werden sollen.
     *
     * @param attributSammlung       Der Sammung der Attribute, zu denen die
     *                               Funktionen erzeugt werden sollen.
     * @param zweiFunkErzeugerNamen  Die Namen der Erzeugerklassen der
     *                               zweistelligen rea-Funktionen.
     * @param einFunkErzeugerNamen   Die Namen der Erzeugerklassen der
     *                               einstelligen real-Funktionen.
     * @param nullFunkErzeugerNamen  Die Namen der Erzeugerklassen der
     *                               nullstelligen real-Funktionen.
     *
     * @return  Eine Sammlung der real-Funktionen, die zur Erzeugung von
     *          Prädikaten verwendet werden sollen.
     */
    private static RealFunkSammlung realFunkSammlung(AttributSammlung attributSammlung,
                                                     String[] zweiFunkErzeugerNamen,
                                                     String[] einFunkErzeugerNamen,
                                                     String[] nullFunkErzeugerNamen) {
        RealFunkSammlung      funkSammlung;
        ZweiStellRealFunkErz  zweiStellErzeuger;
        EinStellRealFunkErz   einStellErzeuger;
        NullStellRealFunkErz  nullStellErzeuger;
        Class                 zweiStellErzKlasse;
        Class                 einStellErzKlasse;
        Class                 nullStellErzKlasse;
        String                klassenName;

        funkSammlung = new RealFunkSammlung(attributSammlung);

        // Erzeugung der Erzeuger der zweistelligen Funktionen
        for (int funkNr = 0; funkNr < zweiFunkErzeugerNamen.length; funkNr++) {
            klassenName = Konstanten.REAL_FUNK_ERZEUGER_PACKAGE
                          + "." + zweiFunkErzeugerNamen[funkNr];
            try {
                zweiStellErzKlasse = Class.forName(klassenName);
                zweiStellErzeuger = (ZweiStellRealFunkErz) zweiStellErzKlasse.newInstance();
                funkSammlung.zweiStellErzeugerAufnehmen(zweiStellErzeuger);
            } catch (Exception e) {
                System.err.println("Fehler beim Erzeugen der Klasse "
                                   + zweiFunkErzeugerNamen[funkNr]);
            }
        }

        // Erzeugung der Erzeuger der einstelligen Funktionen
        for (int funkNr = 0; funkNr < einFunkErzeugerNamen.length; funkNr++) {
            klassenName = Konstanten.REAL_FUNK_ERZEUGER_PACKAGE
                          + "." + einFunkErzeugerNamen[funkNr];
            try {
                einStellErzKlasse = Class.forName(klassenName);
                einStellErzeuger = (EinStellRealFunkErz) einStellErzKlasse.newInstance();
                funkSammlung.einStellErzeugerAufnehmen(einStellErzeuger);
            } catch (Exception e) {
                System.err.println("Fehler beim Erzeugen der Klasse "
                                   + einFunkErzeugerNamen[funkNr]);
            }
        }

        // Erzeugung der Erzeuger der nullstelligen Funktionen
        for (int funkNr = 0; funkNr < nullFunkErzeugerNamen.length; funkNr++) {
            klassenName = Konstanten.REAL_FUNK_ERZEUGER_PACKAGE
                          + "." + nullFunkErzeugerNamen[funkNr];
            try {
                nullStellErzKlasse = Class.forName(klassenName);
                nullStellErzeuger = (NullStellRealFunkErz) nullStellErzKlasse.newInstance();
                funkSammlung.nullStellErzeugerAufnehmen(nullStellErzeuger);
            } catch (Exception e) {
                System.err.println("Fehler beim Erzeugen der Klasse "
                                   + nullFunkErzeugerNamen[funkNr]);
            }
        }

        return funkSammlung;
    }

    /**
     * Liefert eine Liste mit Prädikat-Erzeugern.
     *
     * @param datensatz  Die der Erzeugung der Prädikat-Erzeuger zugrunde
     *                   liegenden Daten.
     * @param parameter  Die Parameter, die die Erzeugung der
     *                   Prädikat-Erzeuger konfigurieren.
     *
     * @return  Eine Liste mit Prädikat-Erzeugern.
     */
    public static ArrayList praedikatErzeuger(Beispieldaten datensatz,
                                              PraedErzParameter parameter) {
        ArrayList         praedErzeuger             = new ArrayList();
        IntFunkSammlung   intFunkSammlung;
        RealFunkSammlung  realFunkSammlung;
        IntFunkErzeugung  intFunkErzeugung;
        RealFunkErzeugung realFunkErzeugung;
        String[]          intZweiFunkErzeugerNamen;
        String[]          intEinFunkErzeugerNamen;
        String[]          intNullFunkErzeugerNamen;
        String[]          realZweiFunkErzeugerNamen;
        String[]          realEinFunkErzeugerNamen;
        String[]          realNullFunkErzeugerNamen;
        int               boolWertPraedNr;

        if (parameter.getErgRealVonInt()) {
            ergRealVonInt(datensatz);
        }

        // Erzeugung von Erzeugern von Prädikaten zum Vergleich nominaler
        // Attribute mit Werten.
        boolWertPraedNr = parameter.getBoolWertPraedNummer();
        praedErzeuger.add(new NomWertPraedErzeugung(datensatz, boolWertPraedNr));

        // Erzeugung von Erzeugern von Prädikaten zum Vergleich von
        // int-Attribute mit Werten. Dies braucht nur zu geschehen, wenn keine
        // int-Funktionen erzeugt werden.
        if (parameter.getMaxIntFunkKomplex() == 0) {
            praedErzeuger.add(new IntWertPraedErzeugung(datensatz));
        }

        // Erzeugung von Erzeugern von Prädikaten zum Vergleich
        // real-Attribute mit Werten. Dies braucht nur zu geschehen, wenn
        // keine real-Funktionen erzeugt werden.
        if (parameter.getMaxRealFunkKomplex() == 0) {
            praedErzeuger.add(new RealWertPraedErzeugung(datensatz));
        }

        if (parameter.getNomNomPraedErz()) {
            // Erzeugung von Erzeugern von Prädikaten zum Vergleich zweier
            // nominaler Attribute.
            praedErzeuger.add(new NomNomPraedErzeugung(datensatz));
        }

        if (parameter.getIntIntPraedErz()) {
            // Erzeugung von Erzeugern von Prädikaten zum Vergleich zweier
            // int-Attribute.
            praedErzeuger.add(new IntIntPraedErzeugung(datensatz));
        }

        if (parameter.getRealRealPraedErz()) {
            // Erzeugung von Erzeugern von Prädikaten zum Vergleich zweier
            // real-Attribute.
            praedErzeuger.add(new RealRealPraedErzeugung(datensatz));
        }

        if (parameter.getHypEbenPraedErz()) {
            // Erzeugung von Erzeugern von Prädikaten mittels Hyperebenen.
            praedErzeuger.add(new HypEbenPraedErzeugung(datensatz,
                                                        parameter.getMaxHypEbenAnz()));
        }

        // Erzeugung der int-Funktionssammlung.
        intZweiFunkErzeugerNamen = Konstanten.INT_ZWEI_FUNK_ERZEUGER_NAMEN;
        intEinFunkErzeugerNamen = Konstanten.INT_EIN_FUNK_ERZEUGER_NAMEN;
        intNullFunkErzeugerNamen = Konstanten.INT_NULL_FUNK_ERZEUGER_NAMEN;
        intFunkSammlung = intFunkSammlung(datensatz.attributSammlung(),
                                          intZweiFunkErzeugerNamen,
                                          intEinFunkErzeugerNamen,
                                          intNullFunkErzeugerNamen);

        // Erzeugung des Erzeugers der int-Gesamtfunktionen.
        intFunkErzeugung = new IntFunkErzeugung(intFunkSammlung,
                                                parameter.getMaxIntFunkKomplex());

        // Erzeugung des Erzeugers der Prädikate der int-Funktionen.
        praedErzeuger.add(new IntFunkPraedErzeugung(datensatz, intFunkErzeugung,
                                                    parameter.getNurHalbIntItv()));

        // Erzeugung der real-Funktionssammlung.
        realZweiFunkErzeugerNamen = Konstanten.REAL_ZWEI_FUNK_ERZEUGER_NAMEN;
        realEinFunkErzeugerNamen = Konstanten.REAL_EIN_FUNK_ERZEUGER_NAMEN;
        realNullFunkErzeugerNamen = Konstanten.REAL_NULL_FUNK_ERZEUGER_NAMEN;
        realFunkSammlung = realFunkSammlung(datensatz.attributSammlung(),
                                            realZweiFunkErzeugerNamen,
                                            realEinFunkErzeugerNamen,
                                            realNullFunkErzeugerNamen);

        // Erzeugung des Erzeugers der real-Gesamtfunktionen.
        realFunkErzeugung = new RealFunkErzeugung(realFunkSammlung,
                                                  parameter.getMaxRealFunkKomplex());

        // Erzeugung des Erzeugers der Prädikate der real-Funktionen.
        praedErzeuger.add(new RealFunkPraedErzeugung(datensatz, realFunkErzeugung,
                                                     parameter.getNurHalbRealItv()));

        return praedErzeuger;
    }
}

