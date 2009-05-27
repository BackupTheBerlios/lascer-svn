/*
 * Dateiname      : WekaClassifier.java
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Vector;
import java.util.Enumeration;
import java.io.StringReader;

import weka.core.OptionHandler;
import weka.core.Option;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;

import de.unistuttgart.commandline.ParameterParser;

import lascer.problemdaten.AttributSammlung;
import lascer.problemdaten.Beispiel;
import lascer.problemdaten.Beispieldaten;
import lascer.problemdaten.ArffDateiEinlesen;
import lascer.konzepte.Konzept;
import lascer.konzepte.KonzeptErzeugungFacade;
import lascer.konzepte.einzelne.Disjunktion;
import lascer.konzepte.einzelne.Konjunktion;

/**
 * Classifier for Weka by using Lascer.<P>
 *
 * ATTENTION: When using the standard package of weka, all integer attributes
 * are handled as real attributes.
 *
 * @author  Dietmar Lippold
 */
public class WekaClassifier extends Classifier implements OptionHandler {

    /**
     * Der Parser für die Kommandozeilen-Parameter.
     */
    private ParameterParser parser;

    /**
     * Der Datensatz, zu dem der Klassifizierer der positiven Beispiele
     * erzeugt wurde.
     */
    private Beispieldaten posDatensatz = null;

    /**
     * Der Datensatz, zu dem der Klassifizierer der negativen Beispiele
     * erzeugt wurde.
     */
    private Beispieldaten negDatensatz = null;

    /**
     * Eine Liste der Attribute des Datensatzes vom Typ
     * <CODE>weka.coreExtended.Attribute</CODE>.
     */
    private LinkedList attributListe = null;

    /**
     * Die zu den positiven Beispielen erzeugte Formel.
     */
    private Konzept posFormel = null;

    /**
     * Die zu den negativen Beispielen erzeugte Formel.
     */
    private Konzept negFormel = null;

    /**
     * Die Nummer der Klasse <EM>true</EM> im <CODE>Instances</CODE>-Objekt
     * von Weka. Wird in Methode <CODE>buildClassifier</CODE> gesetzt.
     */
    private double wekaClassTrue = -1;

    /**
     * Die Nummer der Klasse <EM>false</EM> im <CODE>Instances</CODE>-Objekt
     * von Weka. Wird in Methode <CODE>buildClassifier</CODE> gesetzt.
     */
    private double wekaClassFalse = -1;

    /**
     * Gibt an, ob der debug-Modus gesetzt ist.
     */
    private boolean debugMode = false;

    /**
     * Creates a new instance.
     */
    public WekaClassifier() {
        de.unistuttgart.commandline.Option formelnArtOption;
        de.unistuttgart.commandline.Option formelnKlasseOption;
        de.unistuttgart.commandline.Option lokalGrenzwertOption;
        de.unistuttgart.commandline.Option loggingSwitch;
        int paramCheck;

        // Parser erzeugen und Option zum Debugging aufnehmen.
        parser = Steuerung.parser();

        formelnArtOption = new de.unistuttgart.commandline.Option("formelArt");
        paramCheck = de.unistuttgart.commandline.Option.ONE_PARAMETER_CHECK;
        formelnArtOption.setParameterNumberCheck(paramCheck);
        formelnArtOption.setParamDescription("[dis|kon|beste]");
        formelnArtOption.setFullDescription("Gibt die Art der Formel an, die"
                                            + " für die Klassifikation erzeugt"
                                            + " und verwendet werden soll."
                                            + " (default: "
                                            + Konstanten.WEKA_FORMEL_ART + ")");
        parser.addOption(formelnArtOption);

        formelnKlasseOption = new de.unistuttgart.commandline.Option("formelKlasse");
        paramCheck = de.unistuttgart.commandline.Option.ONE_PARAMETER_CHECK;
        formelnKlasseOption.setParameterNumberCheck(paramCheck);
        formelnKlasseOption.setParamDescription("[pos|neg|beste|beide]");
        formelnKlasseOption.setFullDescription("Gibt die Klasse der Formel an,"
                                               + " die für die Klassifikation"
                                               + " erzeugt und verwendet werden"
                                               + " soll. (default: "
                                               + Konstanten.WEKA_FORMEL_KLASSE
                                               + ")");
        parser.addOption(formelnKlasseOption);

        lokalGrenzwertOption = new de.unistuttgart.commandline.Option("lokalGrenzwert");
        paramCheck = de.unistuttgart.commandline.Option.ONE_PARAMETER_CHECK;
        lokalGrenzwertOption.setParameterNumberCheck(paramCheck);
        lokalGrenzwertOption.setParamDescription("float");
        lokalGrenzwertOption.setFullDescription("Der Grenzwert, bis zu dem eine"
                                                + " Klasifikation lokal anhand"
                                                + " des Testbeispiels erfolgt."
                                                + " (default: "
                                                + Konstanten.LOKAL_GRENZWERT
                                                + ")");
        parser.addOption(lokalGrenzwertOption);

        loggingSwitch = new de.unistuttgart.commandline.Option("logging");
        paramCheck = de.unistuttgart.commandline.Option.ZERO_PARAMETERS_CHECK;
        loggingSwitch.setParameterNumberCheck(paramCheck);
        loggingSwitch.setFullDescription("Gibt bei der Erzeugung des"
                                         + " Klassifikatiors detailierte"
                                         + " Informationen auf der Konsole"
                                         + " aus.");
        parser.addOption(loggingSwitch);

        // Die Parameter für den Parser initialisieren.
        parser.setComandline(new String[0]);
    }

    /**
     * Set debugging mode.
     *
     * @param debug  <CODE>true</CODE> if debug output shall be printed,
     *               otherwise <CODE>false</CODE>.
     */
    public void setDebug(boolean debug) {
        debugMode = debug;
    }

    /**
     * Get whether debugging is turned on.
     *
     * @return  <CODE>true</CODE> if debugging output is on, otherwise
     *          <CODE>false</CODE>.
     */
    public boolean getDebug() {
        return debugMode;
    }

    /**
     * Returns an enumeration of all the available options.
     *
     * @return  an enumeration of all available options.
     */
    public Enumeration listOptions() {
        de.unistuttgart.commandline.Option parserOption;
        Vector   optionVector;
        Iterator optionIter;
        Option   wekaOption;
        String   optionSynopsis;
        String   paramDescription;

        optionVector = new Vector();

        optionIter = parser.getOptionList().iterator();
        while (optionIter.hasNext()) {
            parserOption = (de.unistuttgart.commandline.Option) optionIter.next();
            optionSynopsis = parserOption.getPrefix() + parserOption.getKey();
            paramDescription = parserOption.getParamDescription();
            if (!paramDescription.equals("")) {
                optionSynopsis += " <" + paramDescription + ">";
            }
            wekaOption = new Option("\t" + parserOption.getFullDescription(),
                                    parserOption.getKey(),
                                    parserOption.maxParameterNumber(),
                                    optionSynopsis);
            optionVector.addElement(wekaOption);
        }

        return optionVector.elements();
    }

    /**
     * Sets the OptionHandler's options using the given list. All options
     * will be set (or reset) during this call (i.e. incremental setting
     * of options is not possible). Replaces options with empty strings.
     *
     * @param options  the list of options as an array of strings.
     *
     * @exception Exception  if an option is not supported.
     */
    public void setOptions(String[] options) throws Exception {
        String[] lascerOptions;
        int      lascerOptionAnz;
        int      lascerOptionNr;

        // Zählen, wie viele Optionen tatsächlich vorhanden sind.
        lascerOptionAnz = 0;
        for (int i = 0; i < options.length; i++) {
            if (!options[i].equals("")) {
                lascerOptionAnz++;
            }
        }

        // Die tatsächlich vorhandenen Optionen kopieren.
        lascerOptions = new String[lascerOptionAnz];
        lascerOptionNr = 0;
        for (int i = 0; i < options.length; i++) {
            if (!options[i].equals("")) {
                lascerOptions[lascerOptionNr] = options[i];
                lascerOptionNr++;
            }
        }

        parser.setComandline(lascerOptions);

        // Alle Optionen durch einen leeren String ersetzen.
        for (int i = 0; i < options.length; i++) {
            options[i] = "";
        }
    }

    /**
     * Gets the current option settings for the OptionHandler.
     *
     * @return the list of current option settings as an array of strings
     */
    public String[] getOptions() {
        de.unistuttgart.commandline.Option parserOption;
        ArrayList    options;
        Iterator     optionIter;
        Iterator     paramIter;
        StringBuffer optionString;

        try {
            Steuerung.parseArguments(parser);
        } catch (Exception e) {
            throw (new RuntimeException(e));
        }

        options = new ArrayList();

        optionIter = parser.getOptionList().iterator();
        while (optionIter.hasNext()) {
            parserOption = (de.unistuttgart.commandline.Option) optionIter.next();

            if (parserOption.isEnabled()) {
                optionString = new StringBuffer();
                optionString.append(parserOption.getPrefix()
                                    + parserOption.getKey());

                paramIter = parserOption.getParameterList().iterator();
                while (paramIter.hasNext()) {
                    optionString.append(" " + paramIter.next());
                }

                options.add(optionString.toString());
            }
        }

        return ((String[]) options.toArray(new String[0]));
    }

    /**
     * Returns a new generated formula.
     *
     * @param erzeugungsDatensatz  The data, for which the formula shall be
     *                             generated.
     * @param praedErzParameter    The parameter for the generation of the
     *                             predicates.
     * @param konzErzParameter     The parameter for the generation of the
     *                             concepts.
     * @param formelArt            The type of concept, which shall be
     *                             generated.
     *
     * @return  The generated formula.
     */
    private Konzept generatedFormula(Beispieldaten erzeugungsDatensatz,
                                     PraedErzParameter praedErzParameter,
                                     KonzErzParameter konzErzParameter,
                                     String formelArt) {
        KonzeptErzeugungFacade konzeptErzeugung;
        ArrayList              praedErzeuger;

        // Erzeugung der Prädikat-Erzeuger.
        praedErzeuger = Steuerung.praedikatErzeuger(erzeugungsDatensatz,
                                                    praedErzParameter);

        // Erzeugung des Konzept-Erzeugers.
        konzeptErzeugung = new KonzeptErzeugungFacade(erzeugungsDatensatz,
                                                      konzErzParameter);

        // Erzeugung der Formel.
        if (formelArt.equals("dis")) {
            konzeptErzeugung.erzeugeKorrKonzepte(praedErzeuger);
            return konzeptErzeugung.besteKorrFormel();
        } else if (formelArt.equals("kon")) {
            konzeptErzeugung.erzeugeVollKonzepte(praedErzeuger);
            return konzeptErzeugung.besteVollFormel();
        } else {
            // Es soll die beste Formel geliefert werden.
            konzeptErzeugung.erzeugeKonzepte(praedErzeuger);
            return konzeptErzeugung.besteFormel();
        }
    }

    /**
     * Generates the classifier.
     *
     * @param data  the data to be used.
     *
     * @exception Exception  if the classifier can't built successfully.
     */
    public void buildClassifier(Instances data) throws Exception {
        weka.coreExtended.Instances        extendedInstances;
        weka.coreExtended.BasicInstance    extInst;
        weka.coreExtended.BasicAttribute   classAttribut;
        de.unistuttgart.commandline.Option formelnArtOption;
        de.unistuttgart.commandline.Option formelnKlasseOption;
        de.unistuttgart.commandline.Option loggingSwitch;
        Instance          readInst;
        Beispieldaten     invDatensatz;
        StringReader      stringReader;
        Enumeration       instEnum;
        Enumeration       attribEnum;
        PraedErzParameter praedErzParameter = null;
        KonzErzParameter  konzErzParameter  = null;
        Pruning           pruning;
        String            formelArt;
        String            formelKlasse;
        String            optionWert;
        float             posPruneAnt, negPruneAnt;
        int               instNumber;
        boolean           unbekannteWertBsp;

        Steuerung.parseArguments(parser);

        formelArt = Konstanten.WEKA_FORMEL_ART;
        formelnArtOption = parser.getOption("formelArt");
        if (parser.isEnabled(formelnArtOption)) {
            optionWert = parser.getParameter(formelnArtOption);
            if (!optionWert.equals("dis") && !optionWert.equals("kon")
                && !optionWert.equals("beste")) {

                System.err.println("Wert der Option formelArt unzulässig");
                System.err.println("Zulässig: " + formelnArtOption.toString());
                throw (new RuntimeException("Wert von Option unzulässig."));
            }
            formelArt = optionWert;
        }

        formelKlasse = Konstanten.WEKA_FORMEL_KLASSE;
        formelnKlasseOption = parser.getOption("formelKlasse");
        if (parser.isEnabled(formelnKlasseOption)) {
            optionWert = parser.getParameter(formelnKlasseOption);
            if (!optionWert.equals("pos") && !optionWert.equals("neg")
                && !optionWert.equals("beste") && !optionWert.equals("beide")) {

                System.err.println("Wert der Option formelKlasse unzulässig");
                System.err.println("Zulässig: " + formelnKlasseOption.toString());
                throw (new RuntimeException("Wert von Option unzulässig."));
            }
            formelKlasse = optionWert;
        }

        loggingSwitch = parser.getOption("logging");
        if (debugMode || parser.isEnabled(loggingSwitch)) {
            Steuerung.setLogLevel(Konstanten.LOGGING_LEVEL);
        }

        // Ermittlung der Parameter.
        unbekannteWertBsp = Steuerung.unbekannteWertBeispiele(parser);
        posPruneAnt = Steuerung.posPruneAnteil(parser);
        negPruneAnt = Steuerung.negPruneAnteil(parser);
        praedErzParameter = Steuerung.praedErzParameter(parser);
        konzErzParameter = Steuerung.konzErzParameter(parser);

        // Einlesen der Daten und Erzeugung des Instanzen-Objekts.
        instNumber = data.numInstances();
        stringReader = new StringReader(data.toString());
        extendedInstances = new weka.coreExtended.Instances(stringReader,
                                                            instNumber);
        instEnum = data.enumerateInstances();
        while (instEnum.hasMoreElements()) {
            readInst = (Instance) instEnum.nextElement();
            extInst = new weka.coreExtended.BasicInstance(readInst.weight(),
                                                          readInst.toDoubleArray());
            extendedInstances.addBasicInstance(extInst);
        }

        // Erzeugung der Datensätze.
        posDatensatz = ArffDateiEinlesen.beispieldaten(extendedInstances,
                                                       unbekannteWertBsp);
        negDatensatz = posDatensatz.kopie(true);

        // Erzeugung der Liste der Attribute.
        attributListe = new LinkedList();
        attribEnum = extendedInstances.enumerateBasicAttributes();
        while (attribEnum.hasMoreElements()) {
            attributListe.add(attribEnum.nextElement());
        }

        // Ermittlung der Werte der Klassifikation.
        classAttribut = extendedInstances.basicClassAttribute();
        wekaClassTrue = classAttribut.indexOfValue("true");
        wekaClassFalse = classAttribut.indexOfValue("false");

        // Die Formel zur Klasse der positiven Beispiele erzeugen.
        if (formelKlasse.equals("pos")
            || formelKlasse.equals("beste")
            || formelKlasse.equals("beide")) {

            posFormel = generatedFormula(posDatensatz, praedErzParameter,
                                         konzErzParameter, formelArt);
        }

        // Die Formel zur Klasse der negativen Beispiele erzeugen.
        if (formelKlasse.equals("neg")
            || formelKlasse.equals("beste")
            || formelKlasse.equals("beide")) {

            negFormel = generatedFormula(negDatensatz, praedErzParameter,
                                         konzErzParameter, formelArt);
        }

        if (formelKlasse.equals("beste")) {
            // Die schlechtere Formel löschen.
            if (negFormel.istBesser(posFormel)) {
                posFormel = null;
            } else {
                negFormel = null;
            }
        }

        if ((posPruneAnt > 0) || (negPruneAnt > 0)) {
            pruning = new Pruning();

            if (posFormel != null) {
                posDatensatz = pruning.reduzierteDaten(posDatensatz, posFormel,
                                                       posPruneAnt, negPruneAnt);
                posFormel = generatedFormula(posDatensatz, praedErzParameter,
                                             konzErzParameter, formelArt);
            }

            if (negFormel != null) {
                negDatensatz = pruning.reduzierteDaten(negDatensatz, negFormel,
                                                       negPruneAnt, posPruneAnt);
                negFormel = generatedFormula(negDatensatz, praedErzParameter,
                                             konzErzParameter, formelArt);
            }
        }
    }

    /**
     * Returns a new generated formula after adding the test example to a copy
     * of the data set.
     *
     * @param erzeugungsDatensatz  The data, for which the formula shall be
     *                             generated.
     * @param praedErzParameter    The parameter for the generation of the
     *                             predicates.
     * @param konzErzParameter     The parameter for the generation of the
     *                             concepts.
     * @param vorhandeneFormel     The always generated formula.
     * @param testbeispiel         The example which shall be added to a copy
     *                             of the data set.
     * @param posTestBeispiel      <CODE>true</CODE>, if the test example
     *                             shall be added as a positive example.
     *
     * @return  The generated formula.
     */
    private Konzept generatedAddFormula(Beispieldaten erzeugungsDatensatz,
                                        PraedErzParameter praedErzParameter,
                                        KonzErzParameter konzErzParameter,
                                        Konzept  vorhandeneFormel,
                                        Beispiel testbeispiel,
                                        boolean posTestBeispiel) {
        Beispieldaten datensatzKopie;
        String        formelArt;

        // Die Art der erzeugten Formel ermitteln.
        if (vorhandeneFormel instanceof Disjunktion) {
            formelArt = "dis";
        } else if (vorhandeneFormel instanceof Konjunktion) {
            formelArt = "kon";
        } else {
            throw new RuntimeException("Art der Formel unbekannt: "
                                       + vorhandeneFormel);
        }

        datensatzKopie = erzeugungsDatensatz.kopie(false);
        datensatzKopie.beispielAufnehmen(testbeispiel, posTestBeispiel);

        return generatedFormula(datensatzKopie, praedErzParameter,
                                konzErzParameter, formelArt);
    }

    /**
     * Returns a new generated concept after adding the test example to a copy
     * of the data set.
     *
     * @param erzeugungsDatensatz  The data, for which the concept shall be
     *                             generated.
     * @param praedErzParameter    The parameter for the generation of the
     *                             predicates.
     * @param konzErzParameter     The parameter for the generation of the
     *                             concepts.
     * @param vorhandeneFormel     The always generated formula.
     * @param testbeispiel         The example which shall be added to a copy
     *                             of the data set.
     * @param posTestBeispiel      <CODE>true</CODE>, if the test example
     *                             shall be added as a positive example.
     *
     * @return  The generated concept.
     */
    private Konzept generatedAddConcept(Beispieldaten erzeugungsDatensatz,
                                        PraedErzParameter praedErzParameter,
                                        KonzErzParameter konzErzParameter,
                                        Konzept  vorhandeneFormel,
                                        Beispiel testbeispiel,
                                        boolean posTestBeispiel) {
        KonzeptErzeugungFacade konzeptErzeugung;
        Beispieldaten          datensatzKopie;
        ArrayList              praedErzeuger;
        int                    beispielNr;

        // Testbeispiel in Kopie vom Datensatz aufnehmen.
        datensatzKopie = erzeugungsDatensatz.kopie(false);
        datensatzKopie.beispielAufnehmen(testbeispiel, posTestBeispiel);

        // Erzeugung der Prädikat-Erzeuger.
        praedErzeuger = Steuerung.praedikatErzeuger(datensatzKopie,
                                                    praedErzParameter);

        // Erzeugung des Konzept-Erzeugers.
        konzeptErzeugung = new KonzeptErzeugungFacade(datensatzKopie,
                                                      konzErzParameter);

        if (vorhandeneFormel instanceof Disjunktion) {
            if (posTestBeispiel) {
                beispielNr = datensatzKopie.posBspAnz() - 1;
                return konzeptErzeugung.effizientKorrKonzept(praedErzeuger,
                                                             beispielNr);
            } else {
                return konzeptErzeugung.effizientKorrKonzept(praedErzeuger, -1);
            }
        } else if (vorhandeneFormel instanceof Konjunktion) {
            if (posTestBeispiel) {
                return konzeptErzeugung.effizientVollKonzept(praedErzeuger, -1);
            } else {
                beispielNr = datensatzKopie.negBspAnz() - 1;
                return konzeptErzeugung.effizientVollKonzept(praedErzeuger,
                                                             beispielNr);
            }
        } else {
            throw new RuntimeException("Art der Formel unbekannt: "
                                       + vorhandeneFormel);
        }
    }

    /**
     * Classifies a given example.
     *
     * @param datensatz            The data, for which the concept shall be
     *                             generated.
     * @param praedErzParameter    The parameter for the generation of the
     *                             predicates.
     * @param konzErzParameter     The parameter for the generation of the
     *                             concepts.
     * @param vorhandFormel        The always generated formula.
     * @param testbeispiel         The example to be classified.
     * @param lokalGrenzwert       The value for the difference of the
     *                             complexities of the two formulas, upto that
     *                             a local classification will be done.
     * @param trueClassValue       The value to return, when the example is
     *                             positive classified.
     * @param falseClassValue      The value to return, when the example is
     *                             negative classified.
     *
     * @return  the classification of the example.
     */
    private double relativeCassification(Beispieldaten datensatz,
                                         PraedErzParameter praedErzParameter,
                                         KonzErzParameter konzErzParameter,
                                         Konzept vorhandFormel,
                                         Beispiel testbeispiel,
                                         float lokalGrenzwert,
                                         double trueClassValue,
                                         double falseClassValue) {
        Konzept formelTrue, formelFalse;
        Konzept konzeptTrue, konzeptFalse;
        float   fehlAnzTrue, fehlAnzFalse;
        float   komplexTrue, komplexFalse, komplexDiff;
        float   bewertTrue, bewertFalse;

        // Eine Formel für die Beispiele des Datensatzes inklusive des
        // Testbeispiels erzeugen.
        if (vorhandFormel.trifftZu(testbeispiel)) {
            formelTrue = vorhandFormel;
            formelFalse = generatedAddFormula(datensatz, praedErzParameter,
                                              konzErzParameter, vorhandFormel,
                                              testbeispiel, false);
        } else {
            formelFalse = vorhandFormel;
            formelTrue = generatedAddFormula(datensatz, praedErzParameter,
                                             konzErzParameter, vorhandFormel,
                                             testbeispiel, true);
        }

        // Die Fehleranzahl der Formeln ermitteln.
        fehlAnzTrue = formelTrue.posFalschAnz() + formelTrue.negFalschAnz();
        fehlAnzFalse = formelFalse.posFalschAnz() + formelFalse.negFalschAnz();

        // Die Komplexität der Formeln ermitteln.
        komplexTrue = formelTrue.komplexitaet();
        komplexFalse = formelFalse.komplexitaet();
        komplexDiff = komplexTrue - komplexFalse;

        if (fehlAnzTrue < fehlAnzFalse) {
            return trueClassValue;
        } else if (fehlAnzTrue > fehlAnzFalse) {
            return falseClassValue;
        } else {
            if (Math.abs(komplexDiff) >= lokalGrenzwert) {
                // Klassifikation entsprechend der Komplexität.
                if (komplexTrue < komplexFalse) {
                    return trueClassValue;
                } else {
                    return falseClassValue;
                }
            } else {
                // Lokale Klassifikation.

                konzeptTrue = generatedAddConcept(datensatz, praedErzParameter,
                                                  konzErzParameter, vorhandFormel,
                                                  testbeispiel, true);
                konzeptFalse = generatedAddConcept(datensatz, praedErzParameter,
                                                   konzErzParameter, vorhandFormel,
                                                   testbeispiel, false);

                // Die Bewertungen der Konzepte ermitteln.
                bewertTrue = KonzeptErzeugungFacade.konzeptEffizienz(konzeptTrue);
                bewertFalse = KonzeptErzeugungFacade.konzeptEffizienz(konzeptFalse);

                if (bewertTrue >= bewertFalse) {
                    return trueClassValue;
                } else {
                    return falseClassValue;
                }
            }
        }
    }

    /**
     * Classifies a given instance.
     *
     * @param inst  the instance to be classified.
     *
     * @return  the classification of the instance.
     */
    public double classifyInstance(Instance inst) {
        weka.coreExtended.Instance         extendedInstance;
        de.unistuttgart.commandline.Option lokalGrenzwertOption;
        AttributSammlung  attributSammlung;
        PraedErzParameter praedErzParameter = null;
        KonzErzParameter  konzErzParameter  = null;
        Konzept           posFormelTrue, posFormelFalse;
        Konzept           negFormelTrue, negFormelFalse;
        Konzept           posKonzeptTrue, posKonzeptFalse;
        Konzept           negKonzeptTrue, negKonzeptFalse;
        Beispiel          testbeispiel;
        float             fehlAnzPosTrue, fehlAnzPosFalse, fehlAnzPosDiff;
        float             fehlAnzNegTrue, fehlAnzNegFalse, fehlAnzNegDiff;
        float             komplexPosTrue, komplexPosFalse, komplexPosDiff;
        float             komplexNegTrue, komplexNegFalse, komplexNegDiff;
        float             bewertPosTrue, bewertPosFalse, bewertPosDiff;
        float             bewertNegTrue, bewertNegFalse, bewertNegDiff;
        float             lokalGrenzwert;

        extendedInstance = new weka.coreExtended.BasicInstance(inst.weight(),
                                                               inst.toDoubleArray());
        attributSammlung = posDatensatz.attributSammlung();
        testbeispiel = ArffDateiEinlesen.instanzBeispiel(extendedInstance,
                                                         attributListe,
                                                         attributSammlung);

        lokalGrenzwert = Konstanten.LOKAL_GRENZWERT;
        lokalGrenzwertOption = parser.getOption("lokalGrenzwert");
        if (parser.isEnabled(lokalGrenzwertOption)) {
            lokalGrenzwert = parser.getParameterAsFloat(lokalGrenzwertOption);
        }

        if ((posFormel != null) && (negFormel != null) || (lokalGrenzwert > 0)) {
            // Ermittlung der Parameter.
            praedErzParameter = Steuerung.praedErzParameter(parser);
            konzErzParameter = Steuerung.konzErzParameter(parser);
        }

        if ((posFormel == null) && (negFormel == null)) {

            throw (new RuntimeException("Keine Formel vorhanden."));

        } else if (negFormel == null) {
            // Es ist nur eine Formel für die positiven Beispiele vorhanden.

            if (lokalGrenzwert == 0) {
                // Die Klassifikation erfolgt nur mittels der vorhandenen
                // Formel.
                if (posFormel.trifftZu(testbeispiel)) {
                    return wekaClassTrue;
                } else {
                    return wekaClassFalse;
                }
            } else {
                // Die Klassifikation erfolgt nach Erzeugung von Formeln und
                // gegebenenfalls Konzepten nach Hinzunahme des Testbeispiels
                // zum Datensatz.
                return relativeCassification(posDatensatz, praedErzParameter,
                                             konzErzParameter, posFormel,
                                             testbeispiel, lokalGrenzwert,
                                             wekaClassTrue, wekaClassFalse);
            }

        } else if (posFormel == null) {
            // Es ist nur eine Formel für die negativen Beispiele vorhanden.

            if (lokalGrenzwert == 0) {
                // Die Klassifikation erfolgt nur mittels der vorhandenen
                // Formel.
                if (negFormel.trifftZu(testbeispiel)) {
                    return wekaClassFalse;
                } else {
                    return wekaClassTrue;
                }
            } else {
                // Die Klassifikation erfolgt nach Erzeugung von Formeln und
                // gegebenenfalls Konzepten nach Hinzunahme des Testbeispiels
                // zum Datensatz.
                return relativeCassification(negDatensatz, praedErzParameter,
                                             konzErzParameter, negFormel,
                                             testbeispiel, lokalGrenzwert,
                                             wekaClassFalse, wekaClassTrue);
            }

        } else {
            // Es ist sowohl eine Formel für die positiven wie für negativen
            // Beispiele vorhanden. Es findet daher eine Klassifikation
            // entsprechend der relativen Veränderung der Komplexität der
            // Formeln bei Berücksichtigung des zu klassifizierenden Beispiels
            // statt.

            // Eine Formel für die positiven Beispiele inklusive des
            // Testbeispiels erzeugen.
            if (posFormel.trifftZu(testbeispiel)) {
                posFormelTrue = posFormel;
                posFormelFalse = generatedAddFormula(posDatensatz, praedErzParameter,
                                                     konzErzParameter, posFormel,
                                                     testbeispiel, false);
            } else {
                posFormelFalse = posFormel;
                posFormelTrue = generatedAddFormula(posDatensatz, praedErzParameter,
                                                    konzErzParameter, posFormel,
                                                    testbeispiel, true);
            }

            // Eine Formel für die negativen Beispiele inklusive des
            // Testbeispiels erzeugen.
            if (negFormel.trifftZu(testbeispiel)) {
                negFormelTrue = negFormel;
                negFormelFalse = generatedAddFormula(negDatensatz, praedErzParameter,
                                                     konzErzParameter, negFormel,
                                                     testbeispiel, false);
            } else {
                negFormelFalse = negFormel;
                negFormelTrue = generatedAddFormula(negDatensatz, praedErzParameter,
                                                    konzErzParameter, negFormel,
                                                    testbeispiel, true);
            }

            // Die Fehleranzahl der Formeln ermitteln.
            fehlAnzPosTrue = (posFormelTrue.posFalschAnz()
                              + posFormelTrue.negFalschAnz());
            fehlAnzPosFalse = (posFormelFalse.posFalschAnz()
                               + posFormelFalse.negFalschAnz());
            fehlAnzNegTrue = (negFormelTrue.posFalschAnz()
                              + negFormelTrue.negFalschAnz());
            fehlAnzNegFalse = (negFormelFalse.posFalschAnz()
                               + negFormelFalse.negFalschAnz());
            fehlAnzPosDiff = fehlAnzPosTrue - fehlAnzPosFalse;
            fehlAnzNegDiff = fehlAnzNegTrue - fehlAnzNegFalse;

            // Die Komplexität der Formeln ermitteln.
            komplexPosTrue = posFormelTrue.komplexitaet();
            komplexPosFalse = posFormelFalse.komplexitaet();
            komplexNegTrue = negFormelTrue.komplexitaet();
            komplexNegFalse = negFormelFalse.komplexitaet();
            komplexPosDiff = komplexPosTrue - komplexPosFalse;
            komplexNegDiff = komplexNegTrue - komplexNegFalse;

            // Vergleich der Veränderungen der Formeln.
            if ((fehlAnzPosDiff < 0) && (fehlAnzNegDiff > 0)) {

                return wekaClassTrue;

            } else if ((fehlAnzPosDiff > 0) && (fehlAnzNegDiff < 0)) {

                return wekaClassFalse;

            } else if (Math.abs(fehlAnzPosDiff) > Math.abs(fehlAnzNegDiff)) {

                // fehlAnzPosDiff ist nicht Null.
                if (fehlAnzPosDiff < 0) {
                    return wekaClassTrue;
                } else {
                    return wekaClassFalse;
                }

            } else if (Math.abs(fehlAnzPosDiff) < Math.abs(fehlAnzNegDiff)) {

                // fehlAnzNegDiff ist nicht Null.
                if (fehlAnzNegDiff > 0) {
                    return wekaClassTrue;
                } else {
                    return wekaClassFalse;
                }

            } else {
                // Die Veränderung der Fehleranzahl bezüglich der Klassse des
                // Testbeispiels ist für beide Formeln aller Beispiel der
                // jeweiligen Klasse gleich.

                if ((Math.abs(komplexPosDiff - komplexNegDiff) >= 2 * lokalGrenzwert)
                    && (komplexPosDiff != komplexNegDiff)) {

                    // Klassifikation nach den Komplexitäten.
                    if (Math.abs(komplexPosDiff) > Math.abs(komplexNegDiff)) {

                        // komplexPosDiff ist nicht Null.
                        if (komplexPosDiff < 0) {
                            return wekaClassTrue;
                        } else {
                            return wekaClassFalse;
                        }

                    } else {

                        // komplexNegDiff ist betragsmäßig größer als
                        // komplexPosDiff und nicht Null.
                        if (komplexNegDiff > 0) {
                            return wekaClassTrue;
                        } else {
                            return wekaClassFalse;
                        }

                    }

                } else if (lokalGrenzwert > 0) {
                    // Eine lokale Klassifikation vornehmen.

                    // Erzeugung der effizientesten Konzepte nach Hinzunahme
                    // des Testbeispiels.
                    posKonzeptTrue = generatedAddConcept(posDatensatz,
                                                         praedErzParameter,
                                                         konzErzParameter, posFormel,
                                                         testbeispiel, true);
                    posKonzeptFalse = generatedAddConcept(posDatensatz,
                                                          praedErzParameter,
                                                          konzErzParameter, posFormel,
                                                          testbeispiel, false);
                    negKonzeptTrue = generatedAddConcept(negDatensatz,
                                                         praedErzParameter,
                                                         konzErzParameter, negFormel,
                                                         testbeispiel, true);
                    negKonzeptFalse = generatedAddConcept(negDatensatz,
                                                          praedErzParameter,
                                                          konzErzParameter, negFormel,
                                                          testbeispiel, false);

                    // Die Bewertungen der Konzepte ermitteln.
                    bewertPosTrue = KonzeptErzeugungFacade.konzeptEffizienz(posKonzeptTrue);
                    bewertPosFalse = KonzeptErzeugungFacade.konzeptEffizienz(posKonzeptFalse);
                    bewertNegTrue = KonzeptErzeugungFacade.konzeptEffizienz(negKonzeptTrue);
                    bewertNegFalse = KonzeptErzeugungFacade.konzeptEffizienz(negKonzeptFalse);
                    bewertPosDiff = bewertPosTrue - bewertPosFalse;
                    bewertNegDiff = bewertNegTrue - bewertNegFalse;

                    if (Math.abs(bewertPosDiff) >= Math.abs(bewertNegDiff)) {
                        if (bewertPosDiff >= 0) {
                            return wekaClassTrue;
                        } else {
                            return wekaClassFalse;
                        }
                    } else {
                        if (bewertNegDiff < 0) {
                            return wekaClassTrue;
                        } else {
                            return wekaClassFalse;
                        }
                    }

                } else {
                    // Die Veränderung der Komplexität bezüglich der Klassse
                    // des Testbeispiels ist für beide Formeln aller Beispiel
                    // der jeweiligen Klasse gleich und eine lokale
                    // Klassifikation soll nicht erfolgen. Daher nach der
                    // Anzahl der Beispiele der Klassen klassifizieren.

                    if (posDatensatz.posBspAnz() >= posDatensatz.negBspAnz()) {
                        return wekaClassTrue;
                    } else {
                        return wekaClassFalse;
                    }
                }
            }
        }
    }

    /**
     * Returns a description of the classifier.
     *
     * @return  a description of the classifier as a string.
     */
    public String toString() {

        if ((posFormel != null) && (negFormel != null)) {
            return ("{pos: " + posFormel.toString()
                    + "; neg: " + negFormel.toString() + "}");
        } else if (posFormel != null) {
            return "pos: " + posFormel.toString();
        } else if (negFormel != null) {
            return "neg: " + negFormel.toString();
        } else {
            return "";
        }
    }

    /**
     * Evaluates the classifier.
     *
     * @param args  an array with the options for the evaluation.
     */
    public static void main(String[] args) {

        try {
            System.out.println(Evaluation.evaluateModel(new WekaClassifier(), args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

