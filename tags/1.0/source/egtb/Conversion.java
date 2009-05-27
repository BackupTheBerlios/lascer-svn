/*
 * Filename   : Conversion.java
 * Last change: 22.05.2005 by Edgar Binder
 * Copyright  : Institut für Intelligente Systeme, Universität Stuttgart 
 *              (2005)
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


package egtb;

/**
 * Conversion is an utility class which converts an input
 * tablebase given by the first command line parameter to
 * an output tablebase given by the second command line
 * parameter.
 *
 * @author Edgar Binder
 */
public final class Conversion {

    /**
     * this code is returned to the caller if there's a problem.
     */
    public static final int ERROR_CODE = -1;

    /**
     * Contains the lowest meaningful ply count positions can have
     */
    public static final int DEFAULT_MIN_PLIES = Byte.MIN_VALUE;

    /**
     * Contains the highest meaningful ply count positions can have
     */
    public static final int DEFAULT_MAX_PLIES = Byte.MAX_VALUE;

    /**
     * Contains a list with all allowed switches whic can be
     * passed to {@link egtb.Conversion} as command line arguments
     */
    public static final String VALID_SWITCHES = "white black legal nodraws draws"
                                              + "nocaptures sort succ enumcoords"
                                              + "enumerate enumplies succ noconversions";

    /**
     * String containing the input tablebase's file name.
     */
    private static String inputFile = null;

    /**
     * String containing the output tablebase's file name.
     */
    private static String outputFile = null;

    /**
     * Contains the positions read from the front end in
     * possibly sorted order
     */
    private static Tablebase tablebase = null;

    /**
     * When set to <code>true</code> the method
     * {@link #convertTablebase} doesn't output
     * positions with pawns standing on the
     * conversion files
     */
    private static boolean noConversions = false;


    /**
     * When set to <code>true</code> the method
     * {@link #convertTablebase} also outputs
     * the successors of the position read from
     * the front end
     */
    private static boolean outputSuccessors = false;

    /**
     * When set to <code>true</code> the method
     * {@link #convertTablebase} doesn't
     * output positions where the black player is to move
     */
    private static boolean onlyWhite = false;

    /**
     * When set to <code>true</code> the method
     * {@link #convertTablebase} doesn't
     * output positions where the white player is to move
     */
    private static boolean onlyBlack = false;

    /**
     * When set to <code>true</code> the the coordinates of
     * the positions are sorted before output
     */
    private static boolean sortPositions = false;

    /**
     * When set to <code>true</code> only legal positions
     * will be sent to the back end
     */
    private static boolean onlyLegal = false;

    /**
     * When set to <code>true</code> drawn positions will not
     * be sent to the back end
     */
    private static boolean noDraws = false;

    /**
     * When set to <code>true</code> only drawn positions will
     * be sent to the back end
     */
    private static boolean draws = false;

    /**
     * When set to <code>true</code> positions with captures will not
     * be sent to the back end
     */
    private static boolean noCaptures = false;

    /**
     * When set to <code>true</code> the type
     * of the coordinate attributes is numeric,
     * otherwise it's nominal
     */
    private static boolean numericCoords = true;

    /**
     * When set to <code>true</code> the type
     * of the class attribute is numeric,
     * otherwise it's nominal
     */
    private static boolean numericPlies = true;

    /**
     * Contains the minimal ply count positions can have.
     *
     * @see egtb.BackEnd#BackEnd
     */
    private static int minPlies = DEFAULT_MIN_PLIES;

    /**
     * Contains the maximal ply count positions can have.
     *
     * @see egtb.BackEnd#BackEnd
     */
    private static int maxPlies = DEFAULT_MAX_PLIES;

    /**
     * Prevents instances of Conversion to be created.
     */
    private Conversion() {
    }

    /**
     * Checks if the given position passes the filters {@link #onlyWhite},
     * {@link #onlyBlack}, {@link #noDraws}, {@link #draws},
     * {@link #onlyLegal}, {@link #noCaptures}, {@link #noConversions},
     * {@link #minPlies} and {@link #maxPlies}
     *
     * @param position  the chess position to be checked
     *
     * @return  <code>true</code> if the position passes the filters,
     *          false otherwise
     */
    private static boolean passesFilters(Position position) {

        int pliesToMate = position.getPliesToMate();
        boolean allowedSide = (position.whiteToMove() && !onlyBlack)
                            | (!position.whiteToMove() && !onlyWhite);

        boolean allowedType = (!noDraws || !position.isDraw())
                            & (!draws || position.isDraw())
                            & (!onlyLegal || !position.isIllegal())
                            & (!noCaptures || !position.hasCaptures())
                            & (!noConversions || !position.hasConversions());

        boolean allowedPlies = (minPlies <= pliesToMate)
                             & (pliesToMate <= maxPlies);


        return allowedSide & allowedType & allowedPlies;
    }


    /**
     * Converts an "input" chess endgame tablebase given by
     * <code>frontEnd</code> to a a new tablebase bound to
     * <code>backEnd</code>. It expects both <code>frontEnd</code>
     * and <code>backEnd</code> to be already initialized.
     *
     * @param tablebase  the input tablebase (of type {@link egtb.Tablebase})
     * @param backEnd    the output tablabase
     *
     * @throws Exception  most likely an I/O exception
     *
     * @see Position
     * @see FrontEnd
     * @see FrontEnd#getCurrentPosition()
     * @see FrontEnd#gotoNextPosition()
     * @see BackEnd
     * @see BackEnd#writePosition(Position)
     */
    public static void convertTablebase(Tablebase tablebase, BackEnd backEnd) throws Exception {

        // we initialize "position" with the first
        // chess position that we get from the input tablebase
        Position position = null;
        int pliesToMate = 0;

        //int i = 0;

        // tell the user we're about to start the conversion
        System.out.print("converting to the " + backEnd.getFormat()
                         + " format...");


        position = tablebase.readPosition();

        // this loop does the following:
        // 1. checks if there is a position to process
        //    (this is the case if position != null)
        // 2. outputs this position through the back end
        // 3. reads a new position from the tablebase
        //    (null, if there aren't any positions left)
        while (position != null) {
            // the next commented code line is there for debug purposes
            // uncomment to get a text output of the processed positions
            // System.out.println(position.toString() + "\n");

            if (passesFilters(position)) {
                if (outputSuccessors) {
                    backEnd.writeSuccessors(tablebase, position);
                } else {
                    backEnd.writePosition(position);
                }
            }

            position = tablebase.readPosition();
        }

        // inform the user we're done converting
        System.out.println("done");
    }

    /**
     * Reads all position from the front end into the {@link #tablebase}
     *
     * @param frontEnd  represents the front end to be read from
     */
    private static void buildTablebase(FrontEnd frontEnd) {
        Position position = null;
        int posNum = 0;

        System.out.print("building tablebase...");
        tablebase = new Tablebase(sortPositions);

        position = frontEnd.getCurrentPosition();
        while (position != null) {

            tablebase.addPosition(position);
            posNum++;

            frontEnd.gotoNextPosition();
            position = frontEnd.getCurrentPosition();
        }

        System.out.println("done (" + posNum + " positions)");
    }

    /**
     * Sets the attributes {@link #inputFile}, {@link #outputFile},
     * {@link #onlyWhite}, {@link #onlyBlack}, {@link #sortPositions},
     * {@link #numericCoords}, {@link #numericPlies}, {@link #minPlies},
     * {@link #maxPlies}
     *
     * @param args  an array of Strings containing the command line parameters
     */
    private static void parseCommandLineParams(String[] args) {
        String argument = null;
        int index = 0;

        boolean foundOutputFile = false;

        for (int i = 0; i < args.length; i++) {
            argument = args[i];

            // check if we're dealing with a switch (starting with "-")
            // or with a file name
            if (argument.startsWith("-")) {

                // format the switch into a nice form
                argument = argument.substring(1).toLowerCase();

                index = VALID_SWITCHES.indexOf(argument);

                // check if the switch is a valid one
                if (index > -1
                    && (VALID_SWITCHES.substring(index)).split(" ")[0].startsWith(argument)) {

                    // set the corresponding flags
                    onlyWhite |= argument.equals("white");
                    onlyBlack |= argument.equals("black");
                    onlyLegal |= argument.equals("legal");
                    draws |= argument.equals("draws");
                    noDraws |= argument.equals("nodraws");
                    noCaptures |= argument.equals("nocaptures");
                    noCaptures |= argument.equals("succ");
                    noConversions |= argument.equals("succ");
                    noConversions |= argument.equals("noconversions");
                    sortPositions |= argument.equals("sort");
                    sortPositions |= argument.equals("succ");


                    numericCoords &= !argument.equals("enumcoords");
                    numericCoords &= !argument.equals("enumerate");
                    numericPlies &= !argument.equals("enumplies");
                    numericPlies &= !argument.equals("enumerate");
                    outputSuccessors |= argument.equals("succ");
                } else {
                    try {
                        if (argument.startsWith("min")) {
                            minPlies = Integer.parseInt(argument.substring("min".length()));
                        } else if (argument.startsWith("max")) {
                            maxPlies = Integer.parseInt(argument.substring("max".length()));
                        } else {
                            System.out.println("unknown switch: -" + argument);
                            System.exit(ERROR_CODE);
                        }
                    } catch (Exception e) {
                        System.out.print("can't read " + argument);
                        System.exit(ERROR_CODE);
                    }
                }
            } else {
                if (inputFile == null) {

                    String succ = "";
                    if (outputSuccessors) {
                        succ = "_succ";
                    }

                    // set the name of input file
                    inputFile = argument;

                    // we choose a nice default name for the output file
                    if (FrontEnd.getExtension(argument).equals("lpd")) {
                        outputFile = BackEnd.removeExtension(argument)
                                   + "_lippold" + succ + ".arff";
                    }

                    if (FrontEnd.getExtension(argument).equals("tbw")) {
                        outputFile = BackEnd.removeExtension(argument)
                                   + "_edwards_wtm" + succ + ".arff";
                    }

                    if (FrontEnd.getExtension(argument).equals("tbb")) {
                        outputFile = BackEnd.removeExtension(argument)
                                   + "_edwards_btm" + succ + ".arff";
                    }
                } else {
                    // check if we already set the name of the output file
                    // if no, the set it. If already set, then there's
                    // something wrong about the command line parameters
                    // because the output file shouldn't be set twice
                    if (!foundOutputFile) {
                        foundOutputFile = true;
                        outputFile = argument;
                    } else {
                        System.out.print("too many parameters!");
                        System.exit(ERROR_CODE);
                    }
                }
            }
        }
    }

    /**
     * Outputs a help screen that should help new users
     */
    public static void printHelpScreen() {
        System.out.print("Syntax: java egtb.Conversion [switches] ");
        System.out.print("inputfile [outputfile]\n\n");
        System.out.print("Only following extensions are allowed ");
        System.out.print("for the input file:\n");
        System.out.print("   tbw (Edwards format, white to move)\n");
        System.out.print("   tbb (Edwards format, black to move)\n");
        System.out.print("   lpd (Lippold format)\n\n");
        System.out.print("The output file must have the extension arff\n");
        System.out.print("(Attribute Relationship File Format). Both the\n");
        System.out.print("input and the output file must begin with a\n");
        System.out.print("prefix describing the used piece set, white\n");
        System.out.print("pieces first starting with the white king and then\n");
        System.out.print("the black pieces (starting with the black king)\n\n");
        System.out.print("Switches:\n");
        System.out.print("   -succ            outputs positions with their successors\n");
        System.out.print("   -white           output only positions with white to move\n");
        System.out.print("   -black           output only positions with black to move\n");
        System.out.print("   -sort            sort the positions\n");
        System.out.print("   -legal           output only legal positions\n");
        System.out.print("   -draws           output only drawn positions\n");
        System.out.print("   -nodraws         output only positions which aren't drawn\n");
        System.out.print("   -nocaptures      output only positions with no captures\n");
        System.out.print("   -noconversions   output only positions with no converted pawns\n");
        System.out.print("   -enumerate       use enumeration attributes for coordinates and plies\n");
        System.out.print("   -enumcoords      use enumeration attributes for coordinates\n");
        System.out.print("   -enumplies       use an enumeration attribute for the ply count\n");
        System.out.print("   -minN            sets the min ply count to N\n");
        System.out.print("   -maxN            sets the max ply count to N\n\n");

        System.out.println("Examples:");
        System.out.println("    java egtb.Conversion "
                + "KRK.lpd");
        System.out.println("    java egtb.Conversion "
                + "KPK.tbb KPK_edwards_btm.arff");
        System.out.println("    java egtb.Conversion "
                + "-enumerate KQK.tbw");
        System.out.println("    java egtb.Conversion "
                + "-sort -white KQKR.lpd KQKR_lippold.arff");
    }

    /**
     * Shows a short description and copyright message.
     * Initializes the front and the back end and then
     * attempts to convert the input tablebase. Also
     * outputs information about what's going on
     * at each phase of the conversion.<p>
     *
     * The input and output files are specified through
     * command like parameters. If there's no input or
     * no output file it shows a "help" screen
     * and terminates.
     *
     * @param args  the command line parameters
     *
     * @throws Exception  most likely an I/O Exception
     *
     * @see FrontEnd
     * @see BackEnd
     * @see BackEnd#BackEnd
     * @see BackEnd#close()
     * @see #convertTablebase
     */
    public static void main(String[] args) throws Exception {

        // frontEnd will be used to parse a given tablebase,
        // and backEnd will be used to write into a new tablebase
        FrontEnd frontEnd = null;
        BackEnd backEnd = null;

        parseCommandLineParams(args);

        // output a brief description of the program
        // and the copyright text
        System.out.println("tablebase converter, version 1.2");
        System.out.println("copyright: Institut für Intelligente Systeme, "
                           + "Universität Stuttgart (2005)");
        System.out.println("           http://www.iis.uni-stuttgart.de");
        System.out.println("");

        // check if there's a valid input file and a
        // valid output file name. If not, odds are
        // the user sees the program for the first time,
        // and we should teach him how to use it
        if (inputFile == null || outputFile == null) {
            printHelpScreen();
            return;
        }

        // here we initialize the front end, that means we:
        // - tell the user what we're doing
        // - call the constructor of the front end object,
        //   whic connects to the tablebase file and
        //   eventually loads its content in a buffer
        //   (we chose between to the Edwards and the Lippold format,
        //    based on the extension of the input file)
        // - detect and report failures (a possible cause
        //   for a failure could be for example a missing
        //   input file)
        System.out.print("loading " + inputFile + "...");
        try {
            String extension = FrontEnd.getExtension(inputFile).toUpperCase();

            if (extension.equals("TBB") || extension.equals("TBW")) {
                if (outputSuccessors) {
                    System.out.println("the switch -succ doesn't work with the edwards format!");
                    System.exit(ERROR_CODE);
                }
                frontEnd = new FrontEndEdwards(inputFile);
            } else {
                if (extension.equals("LPD")) {
                    frontEnd = new FrontEndLippold(inputFile);
                } else {
                    System.out.println("unknown format!");
                    System.exit(ERROR_CODE);
                }
            }

            // check if both kings are there
            if (frontEnd.getPieces().indexOf('K') < 0) {
                System.out.println("the white king is missing!");
                System.exit(ERROR_CODE);
            }

            if (frontEnd.getPieces().indexOf('k') < 0) {
                System.out.println("the black king is missing!");
                System.exit(ERROR_CODE);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            System.exit(ERROR_CODE);
        }
        System.out.println("done (used piece set: " + frontEnd.getPieces() + ")");

        // create the tablebase in the internal format
        buildTablebase(frontEnd);

        // here we initialize the back end, that means we:
        // - tell the user what we're doing
        // - call the constructor of the back end object,
        //   that opens a file in which the converted
        //   tablebase will be written to
        // - detect and report failures (a possible cause
        //   for a failure could be for example not enough
        //   disk space)
        System.out.print("opening " + outputFile + "...");
        try {
            String extension = FrontEnd.getExtension(outputFile).toUpperCase();

            if (extension.equals("ARFF")) {
                backEnd = new BackEnd(outputFile, numericCoords, numericPlies,
                                      minPlies, maxPlies, outputSuccessors);
            } else {
                System.out.println("unknown format!");
                System.exit(ERROR_CODE);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            System.exit(ERROR_CODE);
        }
        System.out.println("done (used piece set: " + backEnd.getPieces() + ")");

        // check if the pieces in the input tablebase
        // match the pieces in the output tablebase
        if (!frontEnd.getPieces().equals(backEnd.getPieces())) {
            System.out.println("the pieces in the input tablebase don't "
                               + "match those in the output tablebase");
            System.exit(ERROR_CODE);
        }

        // now that we have a working front and back end
        // we can convert the tablebase
        convertTablebase(tablebase, backEnd);

        // the back end needs "closing" in order
        // to flush its output buffer. Failing
        // to close would result in missing data
        // near the end of the tablebase
        backEnd.close();
    }
}
