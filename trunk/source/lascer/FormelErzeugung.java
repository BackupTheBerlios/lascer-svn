/*
 * Dateiname      : FormelErzeugung.java
 * Letzte Änderung: 17. Juli 2007
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
import java.io.FileNotFoundException;
import java.io.IOException;

import de.unistuttgart.commandline.Option;
import de.unistuttgart.commandline.ParameterParser;
import de.unistuttgart.commandline.ParameterParserException;

import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.ArffDateiEinlesen;
import lascer.problemdaten.UnsupportedDataException;
import lascer.konzepte.Konzept;
import lascer.konzepte.KonzeptErzeugungFacade;

/**
 * Klasse zur Erzeugung einer oder mehrerer Formeln.
 *
 * @author  Dietmar Lippold
 */
public class FormelErzeugung {

    /**
     * Erzeugt und liefert die beste Formel der angegebenen Art.
     *
     * @param datensatz          Der Datensatz, zu dem die Formel erzeugt
     *                           werden sollen.
     * @param praedErzParameter  Die Parameter zur Erzeugung der Prädikate.
     * @param konzErzParameter   Die Parameter zur Erzeugung der Konzepte.
     * @param formelArt          Die Art der Formel, die erzeugt werden soll.
     *
     * @return  Die erzeugte Formel.
     */
    private static Konzept erzeugteFormel(Beispieldaten datensatz,
                                          PraedErzParameter praedErzParameter,
                                          KonzErzParameter konzErzParameter,
                                          String formelArt) {
        KonzeptErzeugungFacade konzeptErzeugung;
        ArrayList              praedErzeuger;
        Konzept                besteFormel;

        // Erzeugung der Prädikat-Erzeuger.
        praedErzeuger = Steuerung.praedikatErzeuger(datensatz,
                                                    praedErzParameter);

        // Erzeugung des Konzept-Erzeugers.
        konzeptErzeugung = new KonzeptErzeugungFacade(datensatz,
                                                      konzErzParameter);

        // Erzeugung der Formel.
        if (formelArt.equals("dis")) {
            konzeptErzeugung.erzeugeKorrKonzepte(praedErzeuger);
            besteFormel = konzeptErzeugung.besteKorrFormel();
        } else if (formelArt.equals("kon")) {
            konzeptErzeugung.erzeugeVollKonzepte(praedErzeuger);
            besteFormel = konzeptErzeugung.besteVollFormel();
        } else if (formelArt.equals("beste")) {
            // Es soll die beste Formel geliefert werden.
            konzeptErzeugung.erzeugeKonzepte(praedErzeuger);
            besteFormel = konzeptErzeugung.besteFormel();
        } else {
            throw new IllegalArgumentException("Formel ist von unbekannter Art");
        }

        return besteFormel;
    }

    /**
     * Erzeugt und liefert die beste Formel der angegebenen Art, wobei vor der
     * Erzeugung der angegebene Anteil der positiven und negativen Beispiele
     * aus dem Datensatz entfernt wird.
     *
     * @param datensatz          Der Datensatz, zu dem die Formel erzeugt
     *                           werden sollen.
     * @param praedErzParameter  Die Parameter zur Erzeugung der Prädikate.
     * @param konzErzParameter   Die Parameter zur Erzeugung der Konzepte.
     * @param formelArt          Die Art der Formel, die erzeugt werden soll.
     * @param posPruneAnt        Der Anteil der positiven Beispiele, die vor
     *                           der Erzeugung des Konzepts entfernt werden
     *                           sollen.
     * @param negPruneAnt        Der Anteil der negativen Beispiele, die vor
     *                           der Erzeugung des Konzepts entfernt werden
     *                           sollen.
     *
     * @return  Die erzeugte Formel.
     */
    private static Konzept erzeugteRedFormel(Beispieldaten datensatz,
                                             PraedErzParameter praedErzParameter,
                                             KonzErzParameter konzErzParameter,
                                             String formelArt,
                                             float posPruneAnt, float negPruneAnt) {
        Beispieldaten pruneDatensatz;
        Pruning       pruning;
        Konzept       besteFormel;

        pruneDatensatz = datensatz;

        // Eventuelle Reduktion des Datensatzes nach Erzeugung einer Formel.
        if ((posPruneAnt > 0) || (negPruneAnt > 0)) {
            pruning = new Pruning();

            besteFormel = erzeugteFormel(datensatz, praedErzParameter,
                                         konzErzParameter, formelArt);
            pruneDatensatz = pruning.reduzierteDaten(datensatz, besteFormel,
                                                     posPruneAnt, negPruneAnt);
        }

        return erzeugteFormel(pruneDatensatz, praedErzParameter,
                              konzErzParameter, formelArt);
    }

    /**
     * Gibt eine Formel der angegebenen Art aus.
     *
     * @param ausgabeFormel     Die auszugebende Formel.
     * @param ausgabeFormelArt  Die Art der auszugebenden Formel.
     * @param beispielKlasse    Die Klasse der Beispiele, zu denen die Formel
     *                          erzeugt wurde.
     */
    private static void formelAusgeben(Konzept ausgabeFormel,
                                       String ausgabeFormelArt,
                                       String beispielKlasse) {

        if (ausgabeFormelArt.equals("beste")) {
            System.out.println("Beste Formel insgesamt der "
                               + beispielKlasse + ". Beispiele :");
            System.out.println(ausgabeFormel.toString());
            System.out.println();
            System.out.println("Statistik :");
            System.out.println(ausgabeFormel.statistik());
            System.out.println();
        } else if (ausgabeFormelArt.equals("dis")) {
            System.out.println("Beste korrekte Formel der "
                               + beispielKlasse + ". Beispiele :");
            System.out.println(ausgabeFormel.toString());
            System.out.println();
            System.out.println("Statistik :");
            System.out.println(ausgabeFormel.statistik());
            System.out.println();
        } else if (ausgabeFormelArt.equals("kon")) {
            System.out.println("Beste vollständige Formel der "
                               + beispielKlasse + ". Beispiele :");
            System.out.println(ausgabeFormel.toString());
            System.out.println();
            System.out.println("Statistik :");
            System.out.println(ausgabeFormel.statistik());
            System.out.println();
        } else {
            throw new IllegalArgumentException("Formel ist von unbekannter Art");
        }
    }

    /**
     * Liefert das letzte positive Beispiel des Datensatzes.
     *
     * @param datensatz  Der Datensatz, von dem das Beispiel geliefert werden
     *                   soll.
     *
     * @return  Das letzte positive Beispiel des Datensatzes.
     */
    private static Beispiel letztesPosBeispiel(Beispieldaten datensatz) {
        return ((Beispiel) datensatz.posBeispiele().get(datensatz.posBspAnz() - 1));
    }

    /**
     * Liefert das letzte negative Beispiel des Datensatzes.
     *
     * @param datensatz  Der Datensatz, von dem das Beispiel geliefert werden
     *                   soll.
     *
     * @return  Das letzte negative Beispiel des Datensatzes.
     */
    private static Beispiel letztesNegBeispiel(Beispieldaten datensatz) {
        return ((Beispiel) datensatz.negBeispiele().get(datensatz.negBspAnz() - 1));
    }

    /**
     * Erzeugt und liefert das effizienteste Konzept der angegebenen Art.
     * Dabei kann vorgegeben werden, daß das Konzept für ein positives
     * Beispiel zutreffen muß oder für ein negatives Beispiel nicht zutreffen
     * darf.
     *
     * @param datensatz          Der Datensatz, zu dem das Konzept erzeugt
     *                           werden sollen.
     * @param praedErzParameter  Die Parameter zur Erzeugung der Prädikate.
     * @param konzErzParameter   Die Parameter zur Erzeugung der Konzepte.
     * @param formelArt          Die Art der Formel, aus der das Konzept
     *                           erzeugt werden soll.
     * @param einzelBspKlasse    Die Klasse des Beispiels, für das das Konzept
     *                           zutreffen muß oder nicht zutreffen darf. Dies
     *                           gilt jeweils für das letzte Beispiel der
     *                           Klasse.
     *
     * @return  Das erzeugte Konzept.
     */
    private static Konzept erzeugtesKonzept(Beispieldaten datensatz,
                                            PraedErzParameter praedErzParameter,
                                            KonzErzParameter konzErzParameter,
                                            String formelArt,
                                            String einzelBspKlasse) {
        KonzeptErzeugungFacade konzeptErzeug;
        ArrayList              praedErzeuger;
        Konzept                bestesKonzept;
        int                    posBspIndex, negBspIndex;

        // Erzeugung der Prädikat-Erzeuger.
        praedErzeuger = Steuerung.praedikatErzeuger(datensatz,
                                                    praedErzParameter);
        // Erzeugung des Konzept-Erzeugers.
        konzeptErzeug = new KonzeptErzeugungFacade(datensatz, konzErzParameter);

        // Ermittlung der Indices der Beispiele, für die das Konzept zutreffen
        // muß oder nicht zutreffen darf.
        if (einzelBspKlasse.equals("pos")) {
            posBspIndex = datensatz.posBspAnz() - 1;
            negBspIndex = -1;
        } else if (einzelBspKlasse.equals("neg")) {
            posBspIndex = -1;
            negBspIndex = datensatz.negBspAnz() - 1;
        } else if (einzelBspKlasse.equals("keine")) {
            posBspIndex = -1;
            negBspIndex = -1;
        } else {
            throw new IllegalArgumentException("Konzpt hat unbekannte Klasse");
        }

        // Erzeugung des Konzepts.
        if (formelArt.equals("beste")) {
            bestesKonzept = konzeptErzeug.effizientKonzept(praedErzeuger,
                                                           posBspIndex,
                                                           negBspIndex);
        } else if (formelArt.equals("dis")) {
            bestesKonzept = konzeptErzeug.effizientKorrKonzept(praedErzeuger,
                                                               posBspIndex);
        } else if (formelArt.equals("kon")) {
            bestesKonzept = konzeptErzeug.effizientVollKonzept(praedErzeuger,
                                                               negBspIndex);
        } else {
            throw new IllegalArgumentException("Formel ist von unbekannter Art");
        }

        return bestesKonzept;
    }

    /**
     * Erzeugt und liefert das effizienteste Konzept der angegebenen Art,
     * wobei vor der Erzeugung der angegebene Anteil der positiven und
     * negativen Beispiele aus dem Datensatz entfernt wird. Außerdem kann
     * vorgegeben werden, daß das Konzept für ein positives Beispiel zutreffen
     * muß oder für ein negatives Beispiel nicht zutreffen darf.
     *
     * @param datensatz          Der Datensatz, zu dem das Konzept erzeugt
     *                           werden sollen.
     * @param praedErzParameter  Die Parameter zur Erzeugung der Prädikate.
     * @param konzErzParameter   Die Parameter zur Erzeugung der Konzepte.
     * @param formelArt          Die Art der Formel, aus der das Konzept
     *                           erzeugt werden soll.
     * @param einzelBspKlasse    Die Klasse des Beispiels, für das das Konzept
     *                           zutreffen muß oder nicht zutreffen darf. Dies
     *                           gilt jeweils für das letzte Beispiel der
     *                           Klasse.
     * @param posPruneAnt        Der Anteil der positiven Beispiele, die vor
     *                           der Erzeugung des Konzepts entfernt werden
     *                           sollen.
     * @param negPruneAnt        Der Anteil der negativen Beispiele, die vor
     *                           der Erzeugung des Konzepts entfernt werden
     *                           sollen.
     *
     * @return  Das erzeugte Konzept.
     */
    private static Konzept erzeugtesRedKonzept(Beispieldaten datensatz,
                                               PraedErzParameter praedErzParameter,
                                               KonzErzParameter konzErzParameter,
                                               String formelArt,
                                               String einzelBspKlasse,
                                               float posPruneAnt,
                                               float negPruneAnt) {
        Beispieldaten pruneDatensatz, entfDatensatz;
        Pruning       pruning;
        Konzept       besteFormel;
        Beispiel      entferntPosBsp, entferntNegBsp;

        pruneDatensatz = datensatz;

        // Eventuelle Reduktion des Datensatzes nach Erzeugung einer Formel.
        if ((posPruneAnt > 0) || (negPruneAnt > 0)) {
            pruning = new Pruning();
            entfDatensatz = datensatz.kopie(false);

            // Gegebenenfalls letztes positives Beispiel entfernen.
            if (einzelBspKlasse.equals("pos")) {
                entferntPosBsp = letztesPosBeispiel(entfDatensatz);
                entfDatensatz.beispielEntfernen(entfDatensatz.posBspAnz() - 1, true);
            } else {
                entferntPosBsp = null;
            }

            // Gegebenenfalls letztes negatives Beispiel entfernen.
            if (einzelBspKlasse.equals("neg")) {
                entferntNegBsp = letztesNegBeispiel(entfDatensatz);
                entfDatensatz.beispielEntfernen(entfDatensatz.negBspAnz() - 1, false);
            } else {
                entferntNegBsp = null;
            }

            // Erzeugung des neuen reduzierten Datensatzes.
            besteFormel = erzeugteFormel(entfDatensatz, praedErzParameter,
                                         konzErzParameter, formelArt);
            pruneDatensatz = pruning.reduzierteDaten(entfDatensatz, besteFormel,
                                                     posPruneAnt, negPruneAnt);

            // Gegebenenfalls entferntes positives Beispiel hinzufügen.
            if (entferntPosBsp != null) {
                pruneDatensatz.beispielAufnehmen(entferntPosBsp, true);
            }

            // Gegebenenfalls entferntes negatives Beispiel hinzufügen.
            if (entferntNegBsp != null) {
                pruneDatensatz.beispielAufnehmen(entferntNegBsp, false);
            }
        }

        return erzeugtesKonzept(pruneDatensatz, praedErzParameter,
                                konzErzParameter, formelArt, einzelBspKlasse);
    }

    /**
     * Gibt ein Konzept der angegebenen Art aus.
     *
     * @param datensatz          Der Datensatz, zu dem das Konzept erzeugt
     *                           wurde.
     * @param ausgabeKonzept     Das auszugebende Konzept.
     * @param einzelBspKlasse    Die Klasse des Beispiels, für das das Konzept
     *                           zutreffen muß oder nicht zutreffen darf. Dies
     *                           gilt jeweils für das letzte Beispiel der
     *                           Klasse.
     * @param ausgabeKonzeptArt  Die Art des auszugebenden Konzepts.
     * @param beispielKlasse     Die Klasse der Beispiele, zu denen das
     *                           Konzept erzeugt wurde.
     */
    private static void konzeptAusgeben(Beispieldaten datensatz,
                                        Konzept ausgabeKonzept,
                                        String einzelBspKlasse,
                                        String ausgabeKonzeptArt,
                                        String beispielKlasse) {

        // Ausgabe der Beispiele, für die das Konzept zutreffen muß oder nicht
        // zutreffen darf.
        if (einzelBspKlasse.equals("pos")) {
            System.out.println("Pos. Beispiel, für das das Konzept zutreffen"
                               + " muß: ");
            System.out.println(letztesPosBeispiel(datensatz));
            System.out.println();
        } else if (einzelBspKlasse.equals("neg")) {
            System.out.println("Neg. Beispiel, für das das Konzept nicht"
                               + " zutreffen darf: ");
            System.out.println(letztesNegBeispiel(datensatz));
            System.out.println();
        } else {
            System.out.println("Kein Beispiel für das Konzept vorgegeben.");
            System.out.println();
        }

        if (ausgabeKonzeptArt.equals("beste")) {
            System.out.println("Bestes Konzept insgesamt der "
                               + beispielKlasse + ". Beispiele :");
            System.out.println(ausgabeKonzept.toString());
            System.out.println();
            System.out.println("Statistik :");
            System.out.println(ausgabeKonzept.statistik());
            System.out.println();
        } else {
            if (ausgabeKonzeptArt.equals("dis") || ausgabeKonzeptArt.equals("beide")) {
                System.out.println("Bestes korrekte Konzept der "
                                   + beispielKlasse + ". Beispiele :");
                System.out.println(ausgabeKonzept.toString());
                System.out.println();
                System.out.println("Statistik :");
                System.out.println(ausgabeKonzept.statistik());
                System.out.println();
            }

            if (ausgabeKonzeptArt.equals("kon") || ausgabeKonzeptArt.equals("beide")) {
                System.out.println("Bestes vollständige Konzept der "
                                   + beispielKlasse + ". Beispiele :");
                System.out.println(ausgabeKonzept.toString());
                System.out.println();
                System.out.println("Statistik :");
                System.out.println(ausgabeKonzept.statistik());
                System.out.println();
            }
        }
    }

    /**
     * Erzeugt zu der angegebenen Datei mit Daten eine möglichst einfache
     * Formel, die die Beispiele möglichst korrekt und vollständig
     * klassifiziert.
     *
     * @param args  Als Komandozeilen-Argument ist der Name der Datei, in dem
     *              die Problem-Daten gespeichert sind, anzugeben und es
     *              können weitere Parameter angegeben werden, die bei Angabe
     *              des Parameters <KBD>-help</KBD> genannt werden.
     */
    public static void main(String[] args) {
        KonzeptErzeugungFacade konzeptErzeugung;
        ParameterParser   parser;
        Beispieldaten     posDatensatz      = null;
        Beispieldaten     negDatensatz      = null;
        PraedErzParameter praedErzParameter = null;
        KonzErzParameter  konzErzParameter  = null;
        ArrayList         praedErzeuger;
        Konzept           besteFormel;
        Konzept           bestesKonzept;
        Option            helpSwitch, logSwitch;
        Option            ausgabeFormelArtOption;
        Option            ausgabeFormelKlasseOption;
        Option            einzelBspKlasseOption;
        String            datensatzDatei;
        String            ausgabeFormelArt;
        String            ausgabeFormelKlasse;
        String            einzelBspKlasse;
        float             posPruneAnt       = 0;
        float             negPruneAnt       = 0;
        boolean           unbekannteWertBsp = false;

        parser = Steuerung.parser();

        ausgabeFormelArtOption = new Option("ausgabeFormelArt");
        ausgabeFormelArtOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        ausgabeFormelArtOption.setParamDescription("[dis|kon|beste|beide]");
        ausgabeFormelArtOption.setFullDescription("Gibt die Art der Formeln an, "
                                                  + " die erzeugt und ausgegeben"
                                                  + " werden sollen. (default: "
                                                  + Konstanten.AUSGABE_FORMEL_ART
                                                  + ")");
        parser.addOption(ausgabeFormelArtOption);

        ausgabeFormelKlasseOption = new Option("ausgabeFormelKlasse");
        ausgabeFormelKlasseOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        ausgabeFormelKlasseOption.setParamDescription("[pos|neg|beide]");
        ausgabeFormelKlasseOption.setFullDescription("Gibt die Klasse der Beispiele"
                                                     + " an, für die eine Formel"
                                                     + " erzeugt und ausgegeben"
                                                     + " werden soll. (default: "
                                                     + Konstanten.AUSGABE_FORMEL_KLASSE
                                                     + ")");
        parser.addOption(ausgabeFormelKlasseOption);

        einzelBspKlasseOption = new Option("einzelBspKlasse");
        einzelBspKlasseOption.setParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        einzelBspKlasseOption.setParamDescription("[nein|pos|neg|keine]");
        einzelBspKlasseOption.setFullDescription("Gibt an, ob anstatt einer"
                                                 + " Formel ein Konzept"
                                                 + " erzeugt werden soll und"
                                                 + " für ein Beispiel welcher"
                                                 + " Klasse dies gültig sein"
                                                 + " soll. (default: "
                                                 + Konstanten.EINZEL_BSP_KLASSE
                                                 + ")");
        parser.addOption(einzelBspKlasseOption);

        logSwitch = new Option("logging");
        logSwitch.setParameterNumberCheck(Option.ZERO_PARAMETERS_CHECK);
        logSwitch.setFullDescription("Gibt bei der Erzeugung der Formeln"
                                     + " detailierte Informationen auf der"
                                     + " Konsole aus.");
        parser.addOption(logSwitch);

        helpSwitch = new Option("help");
        helpSwitch.setParameterNumberCheck(Option.ZERO_PARAMETERS_CHECK);
        helpSwitch.setFullDescription("Gibt Hinweise zur Benutzung aus.");
        parser.addOption(helpSwitch);

        // Der Name der Datensatz-Datei wird als freier Parameter am Ende der
        // Kommandozeilenparameter angegeben.
        parser.setFreeParameterNumberCheck(Option.ONE_PARAMETER_CHECK);
        parser.setFreeParameterPosition(ParameterParser.END);
        parser.setFreeParameterDescription("filename");

        try {
            // Option von help ermitteln.
            parser.setComandline(args);
            parser.parseOption(helpSwitch);

            if (parser.isEnabled(helpSwitch)) {
                System.out.println("Benutzung:");
                System.out.println("lascer.FormelErzeugung " + parser.toString());
                System.exit(0);
            } else {
                try {
                    Steuerung.parseArguments(parser);
                } catch (FileNotFoundException e) {
                    System.err.println(e.getMessage());
                    System.exit(-1);
                } catch (IOException e) {
                    System.err.println("Fehler beim Einlesen der"
                                       + " config-Datei");
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        } catch (ParameterParserException e) {
            System.err.println("Fehler bei der Angabe der Parameter.");
            System.err.println(e);
            System.err.println();
            System.err.println("Mögliche und notwendige Parameter sind:");
            System.err.println(parser);
            System.exit(-1);
        }

        if (parser.isEnabled(logSwitch)) {
            Steuerung.setLogLevel(Konstanten.LOGGING_LEVEL);
        }

        try {
            unbekannteWertBsp = Steuerung.unbekannteWertBeispiele(parser);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        try {
            posPruneAnt = Steuerung.posPruneAnteil(parser);
            negPruneAnt = Steuerung.negPruneAnteil(parser);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        try {
            praedErzParameter = Steuerung.praedErzParameter(parser);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        try {
            konzErzParameter = Steuerung.konzErzParameter(parser);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        ausgabeFormelArt = Konstanten.AUSGABE_FORMEL_ART;
        if (parser.isEnabled(ausgabeFormelArtOption)) {
            ausgabeFormelArt = parser.getParameter(ausgabeFormelArtOption);
        }

        if (!ausgabeFormelArt.equals("dis") && !ausgabeFormelArt.equals("kon")
            && !ausgabeFormelArt.equals("beste")
            && !ausgabeFormelArt.equals("beide")) {

            System.err.println("Wert der Option ausgabeFormelArt unzulässig");
            System.err.println("Zulässig: " + ausgabeFormelArtOption.toString());
            System.exit(-1);
        }

        ausgabeFormelKlasse = Konstanten.AUSGABE_FORMEL_KLASSE;
        if (parser.isEnabled(ausgabeFormelKlasseOption)) {
            ausgabeFormelKlasse = parser.getParameter(ausgabeFormelKlasseOption);
        }

        if (!ausgabeFormelKlasse.equals("pos")
            && !ausgabeFormelKlasse.equals("neg")
            && !ausgabeFormelKlasse.equals("beide")) {

            System.err.println("Wert der Option ausgabeFormelKlasse unzulässig");
            System.err.println("Zulässig: " + ausgabeFormelKlasseOption.toString());
            System.exit(-1);
        }

        einzelBspKlasse = Konstanten.EINZEL_BSP_KLASSE;
        if (parser.isEnabled(einzelBspKlasseOption)) {
            einzelBspKlasse = parser.getParameter(einzelBspKlasseOption);
        }

        if (!einzelBspKlasse.equals("nein")
            && !einzelBspKlasse.equals("pos")
            && !einzelBspKlasse.equals("neg")
            && !einzelBspKlasse.equals("keine")) {

            System.err.println("Wert der Option einzelBspKlasse unzulässig");
            System.err.println("Zulässig: " + einzelBspKlasseOption.toString());
            System.exit(-1);
        }

        datensatzDatei = parser.getFreeParameter();

        try {
            posDatensatz = ArffDateiEinlesen.beispieldaten(datensatzDatei,
                                                           unbekannteWertBsp);
            negDatensatz = posDatensatz.kopie(true);
        } catch (FileNotFoundException e) {
            System.err.println("Datei " + datensatzDatei + " nicht gefunden");
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("Fehler beim Einlesen der Daten");
            e.printStackTrace();
            System.exit(-1);
        } catch (UnsupportedDataException e) {
            System.err.println("Die Art der Daten wird nicht unterstützt: "
                               + e.toString());
            System.exit(-1);
        }

        if (ausgabeFormelKlasse.equals("pos") || ausgabeFormelKlasse.equals("beide")) {
            // Erzeugung und Ausgabe der Formeln zu den positiven Beispielen.

            if (einzelBspKlasse.equals("nein")) {
                // Formeln erzeugen und ausgeben.

                if (ausgabeFormelArt.equals("beste")) {
                    // Insgesamt beste Formel erzeugen und ausgeben.
                    besteFormel = erzeugteRedFormel(posDatensatz, praedErzParameter,
                                                    konzErzParameter, "beste",
                                                    posPruneAnt, negPruneAnt);
                    formelAusgeben(besteFormel, "beste", "pos");
                } else {
                    // Beste disjunktive Formel erzeugen und ausgeben.
                    if (ausgabeFormelArt.equals("dis")
                        || ausgabeFormelArt.equals("beide")) {

                        besteFormel = erzeugteRedFormel(posDatensatz, praedErzParameter,
                                                        konzErzParameter, "dis",
                                                        posPruneAnt, negPruneAnt);
                        formelAusgeben(besteFormel, "dis", "pos");
                    }

                    // Beste konjunktive Formel erzeugen und ausgeben.
                    if (ausgabeFormelArt.equals("kon")
                        || ausgabeFormelArt.equals("beide")) {

                        besteFormel = erzeugteRedFormel(posDatensatz, praedErzParameter,
                                                        konzErzParameter, "kon",
                                                        posPruneAnt, negPruneAnt);
                        formelAusgeben(besteFormel, "kon", "pos");
                    }
                }

            } else {
                // Einzelne Konzepte erzeugen und ausgeben.

                if (ausgabeFormelArt.equals("beste")) {
                    // Insgesamt bestes Konzept erzeugen und ausgeben.
                    bestesKonzept = erzeugtesRedKonzept(posDatensatz,
                                                        praedErzParameter,
                                                        konzErzParameter, "beste",
                                                        einzelBspKlasse,
                                                        posPruneAnt, negPruneAnt);
                    konzeptAusgeben(posDatensatz, bestesKonzept,
                                    einzelBspKlasse, "beste", "pos");
                } else {
                    // Bestes Konzept einer disjunktives Formel erzeugen und
                    // ausgeben.
                    if (ausgabeFormelArt.equals("dis")
                        || ausgabeFormelArt.equals("beide")) {

                        bestesKonzept = erzeugtesRedKonzept(posDatensatz,
                                                            praedErzParameter,
                                                            konzErzParameter, "dis",
                                                            einzelBspKlasse,
                                                            posPruneAnt, negPruneAnt);
                        konzeptAusgeben(posDatensatz, bestesKonzept,
                                        einzelBspKlasse, "dis", "pos");
                    }

                    // Bestes Konzept einer konjunktives Formel erzeugen und
                    // ausgeben.
                    if (ausgabeFormelArt.equals("kon")
                        || ausgabeFormelArt.equals("beide")) {

                        bestesKonzept = erzeugtesRedKonzept(posDatensatz,
                                                            praedErzParameter,
                                                            konzErzParameter, "kon",
                                                            einzelBspKlasse,
                                                            posPruneAnt, negPruneAnt);
                        konzeptAusgeben(posDatensatz, bestesKonzept,
                                        einzelBspKlasse, "kon", "pos");
                    }
                }
            }
        }

        if (ausgabeFormelKlasse.equals("neg") || ausgabeFormelKlasse.equals("beide")) {
            // Erzeugung und Ausgabe der Formeln zu den negativen Beispielen.

            if (einzelBspKlasse.equals("nein")) {
                // Formeln erzeugen und ausgeben.

                if (ausgabeFormelArt.equals("beste")) {
                    // Insgesamt beste Formel erzeugen und ausgeben.
                    besteFormel = erzeugteRedFormel(negDatensatz, praedErzParameter,
                                                    konzErzParameter, "beste",
                                                    negPruneAnt, posPruneAnt);
                    formelAusgeben(besteFormel, "beste", "neg");
                } else {
                    // Beste disjunktive Formel erzeugen und ausgeben.
                    if (ausgabeFormelArt.equals("dis")
                        || ausgabeFormelArt.equals("beide")) {

                        besteFormel = erzeugteRedFormel(negDatensatz, praedErzParameter,
                                                        konzErzParameter, "dis",
                                                        negPruneAnt, posPruneAnt);
                        formelAusgeben(besteFormel, "dis", "neg");
                    }

                    // Beste konjunktive Formel erzeugen und ausgeben.
                    if (ausgabeFormelArt.equals("kon")
                        || ausgabeFormelArt.equals("beide")) {

                        besteFormel = erzeugteRedFormel(negDatensatz, praedErzParameter,
                                                        konzErzParameter, "kon",
                                                        negPruneAnt, posPruneAnt);
                        formelAusgeben(besteFormel, "kon", "neg");
                    }
                }

            } else {
                // Einzelne Konzepte erzeugen und ausgeben.

                if (ausgabeFormelArt.equals("beste")) {
                    // Insgesamt bestes Konzept erzeugen und ausgeben.
                    bestesKonzept = erzeugtesRedKonzept(negDatensatz,
                                                        praedErzParameter,
                                                        konzErzParameter, "beste",
                                                        einzelBspKlasse,
                                                        negPruneAnt, posPruneAnt);
                    konzeptAusgeben(negDatensatz, bestesKonzept,
                                    einzelBspKlasse, "beste", "neg");
                } else {
                    // Bestes Konzept einer disjunktives Formel erzeugen und
                    // ausgeben.
                    if (ausgabeFormelArt.equals("dis")
                        || ausgabeFormelArt.equals("beide")) {

                        bestesKonzept = erzeugtesRedKonzept(negDatensatz,
                                                            praedErzParameter,
                                                            konzErzParameter, "dis",
                                                            einzelBspKlasse,
                                                            negPruneAnt, posPruneAnt);
                        konzeptAusgeben(negDatensatz, bestesKonzept,
                                        einzelBspKlasse, "dis", "neg");
                    }

                    // Bestes Konzept einer konjunktives Formel erzeugen und
                    // ausgeben.
                    if (ausgabeFormelArt.equals("kon")
                        || ausgabeFormelArt.equals("beide")) {

                        bestesKonzept = erzeugtesRedKonzept(negDatensatz,
                                                            praedErzParameter,
                                                            konzErzParameter, "kon",
                                                            einzelBspKlasse,
                                                            negPruneAnt, posPruneAnt);
                        konzeptAusgeben(negDatensatz, bestesKonzept,
                                        einzelBspKlasse, "kon", "neg");
                    }
                }
            }
        }
    }
}

