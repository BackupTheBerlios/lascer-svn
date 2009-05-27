/*
 * Filename   : BackEnd.java
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

import java.io.PrintStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/**
 * BackEnd provides methods to open, write to and close a chess tablebase in
 * the <a href="http://www.cs.waikato.ac.nz/~ml/weka/arff.html">ARFF</a>
 * Format (Attribute Relation File Format).
 *
 * @author Edgar Binder
 */
public class BackEnd {

    /**
     * Contains the width of a standard chess board.
     */
    private static final int BOARD_WIDTH = 8;

    /**
     * Used for writing text in the target tablebase.
     */
    private PrintStream output = null;

    /**
     * Contains the pieces used in the endgame<br>
     * White pieces are denoted "K", "Q", "B", "N", "R", "P"<br>
     * (King, Queen, Bishop, Knight, Rook, Pawn)<br>
     * Black pieces are denoted "k", "q", "b", "n", "r", "p".<p>
     *
     * The order of the pieces determines how positions
     * are coded into the tablebase.
     */
    private String pieces = null;

    /**
     * Is most likely thrown because the pieces of a given position
     * don't match with the pieces of the tablebase
     * (see {@link #writePosition}).<p>
     *
     * Can also be thrown when a tablebase with a wrong filename is openend.
     */
    private RuntimeException invalidPieces = new RuntimeException("invalid pieces");

    /**
     * Opens a new file (overwrites existing files), and then attempts
     * to write a relation header into this file.<p>
     *
     * @param filename            gives the name of the output file
     * @param numericCoordinates  if true, the datatype of the coordinate
     *                            attributes is numeric (integer) otherwise
     *                            nominal: {1, 2, 3, 4, 5, 6, 7, 8}
     * @param numericPlies        if true, the datatype of the ply
     *                            attribute is numeric (integer) otherwise
     *                            nominal: {minPlies, ..., maxPlies}
     * @param minPlies            lowest ply count that a position can have
     * @param maxPlies            highest ply count that a position can have
     * @param successors          if true, then also successor positions will
     *                            be outputed by the back end
     *
     * @throws FileNotFoundException  will never actually be thrown
     */
    public BackEnd(String filename,
                   boolean numericCoordinates,
                   boolean numericPlies,
                   int minPlies,
                   int maxPlies,
                   boolean successors) throws FileNotFoundException {

        BufferedOutputStream file = new BufferedOutputStream(new FileOutputStream(filename));
        output = new PrintStream(file);

        // output the relation's name
        output.println("@relation " + removeExtension(filename));

        // The weka comment block
        output.println("");
        output.println("% for all files: 1 <= rank <= 8, from bottom to top");
        output.println("% for all ranks: 1 <= file <= 8, from left to right");

        pieces = Position.getPiecesFromFilename(filename);
        outputCoordinateAttributes(numericCoordinates, "");

        if (successors) {
            outputCoordinateAttributes(numericCoordinates, "Target");
        }

        // here we output the "sideToMove", "illegal" and "draw" attributes
        output.println("");
        output.println("@attribute sideToMove {white, black}");

        if (!successors) {
            output.println("@attribute illegal {false, true}");
            output.println("@attribute draw {false, true}");
        }

        output.println("");

        if (successors) {
            output.println("% bad = the move changes the expected outcome of the game");
            output.println("% good = the move doesn't change the outcome, but it's not optimal");
            output.println("% perfect = optimal move");
            output.println("@attribute class {bad, good, perfect}");
        } else {
            // also the class attribute, together with a comment
            output.println("% the number of plys to end the game");
            output.println("% a position with a negative value will be lost for the side to move");
            outputPliesAttribute(numericPlies, minPlies, maxPlies, "plies");
        }

        // finally we can output the data
        output.println("");
        output.println("@data");
    }

    /**
     * Writes the plies attribute to the ARFF-header. The type of this
     * attribute is either integer or enumeration.
     *
     * @param numericPlies  when <code>true</code>, the type of the attribute
     *                      is integer, otherwise {<code>minPlies</code>, ...,
     *                      <code>maxPlies</code>}
     * @param minPlies      contains the lowest element of the enumeration
     * @param maxPlies      contains the highest element of the enumeration
     * @param name          contains the name of the attribute
     *                      (normally "plies")
     */
    public void outputPliesAttribute(boolean numericPlies,
                                     int minPlies,
                                     int maxPlies,
                                     String name) {

        output.print("@attribute " + name + " ");
        if (numericPlies) {
            output.println("integer");
        } else {

            boolean firstValue = true;

            // output the value interval of plies
            output.print("{");
            for (int i = minPlies; i <= maxPlies; i++) {
                if ((i <= 0 && (i % 2 == 0)) || (i > 0 && (i % 2 == 1))) {

                    if (!firstValue) {
                        output.print(", ");
                    }

                    output.print(i);
                    firstValue = false;
                }
            }

            output.println("}");
        }
    }

    /**
     * Outputs the attributes representing the coordinates of the pieces on
     * the chess board to the ARFF-header. The type of the attributes is
     * either integer or the enumeration {1, 2, 3, 4, 5, 6, 7, 8}.
     *
     * @param numericCoordinates  when <code>true</code> the type of the
     *                            coordinate attributes is integer, otherwise
     *                            it's {1, 2, 3, 4, 5, 6, 7, 8}
     *
     * @param suffix              contains a suffix to be added to the
     *                            name of the attributes (like for example
     *                            "Target", when outputing coordinate
     *                            attributes for target (successor) position)
     */
    public void outputCoordinateAttributes(boolean numericCoordinates,
                                           String suffix) {

        // piecesInitials and piecesNames *must* match
        String pieceInitials = "KQBNRPkqbnrp";
        String[] pieceNames = {"whiteKing", "whiteQueen", "whiteBishop",
                               "whiteKnight", "whiteRook", "whitePawn",
                               "blackKing", "blackQueen", "blackBishop",
                               "blackKnight", "blackRook", "blackPawn"};
        char currentPiece = ' ';
        String pieceName = null;
        int index = -1;

        // we output here the attributes that
        // represent the coordinates of the pieces
        for (int i = 0; i < pieces.length(); i++) {
            currentPiece = pieces.charAt(i);
            index = pieceInitials.indexOf(currentPiece);

            if (index < 0) {
                throw invalidPieces;
            }

            pieceName = pieceNames[index];

            output.print("@attribute " + pieceName + "File" + suffix + " ");
            if (numericCoordinates) {
                output.println("integer");
            } else {
                output.println("{1, 2, 3, 4, 5, 6, 7, 8}");
            }

            output.print("@attribute " + pieceName + "Rank" + suffix + " ");
            if (numericCoordinates) {
                output.println("integer");
            } else {
                output.println("{1, 2, 3, 4, 5, 6, 7, 8}");
            }

        }
    }

    /**
     * Retrieves the used endgame piece set
     * @return  string containing the pieces
     */
    public String getPieces() {
        return pieces;
    }

    /**
     * Writes the given coordinates to the ARFF-file.
     *
     * @param coordinates  contains the {@link egtb.Position#coordinates}
     *                     that are going to be written to the chosen
     *                     output file
     */
    public void outputCoordinates(byte[] coordinates) {
        for (int i = 0; i < coordinates.length; i++) {
            output.print("12345678".charAt(coordinates[i] % BOARD_WIDTH));
            output.print(',');
            output.print("12345678".charAt(coordinates[i] / BOARD_WIDTH));
            output.print(',');
        }
    }

    /**
     * Writes the values of the position flags
     * {@link egtb.Position#whiteToMove}, {@link egtb.Position#isIllegal} and
     * {@link egtb.Position#isDraw} if <code>outputWhiteToMove</code>
     * <code>outputIsIllegal</code> and <code>outputIsDraw</code> are set
     * accordingly.
     *
     * @param outputWhiteToMove  masks {@link egtb.Position#whiteToMove}
     * @param outputIsIllegal    masks {@link egtb.Position#isIllegal}
     * @param outputIsDraw       masks {@link egtb.Position#isDraw}
     * @param position           contains a chess position whose flags
     *                           are to be outputed
     */
    public void outputPositionFlags(boolean outputWhiteToMove,
                                    boolean outputIsIllegal,
                                    boolean outputIsDraw,
                                    Position position) {
        if (outputWhiteToMove) {
            if (position.whiteToMove()) {
                output.print("white,");
            } else {
                output.print("black,");
            }
        }

        if (outputIsIllegal) {
            if (position.isIllegal()) {
                output.print("true,");
            } else {
                output.print("false,");
            }
        }

        if (outputIsDraw) {
            if (position.isDraw()) {
                output.print("true,");
            } else {
                output.print("false,");
            }
        }
    }

    /**
     * Writes a chess position into the tablebase. The set
     * of pieces used in this position must match the
     * set of pieces of the tablebase.
     *
     * @param position  contains the description of the position to be written
     */
    public void writePosition(Position position) {
        byte[] coordinates = position.getCoordinates();

        if (!position.getPieces().equals(pieces)) {
            throw invalidPieces;
        }

        // format:
        // - coordinates
        // - position illegal?
        // - position drawn?
        // - steps to mate
        outputCoordinates(coordinates);

        outputPositionFlags(true, true, true, position);

        output.println(position.getPliesToMate());
    }


    /**
     * Writes a chess position and one of it's direct successors along with
     * an evaluation to the ARFF file.
     *
     * @param position    contains a chess position
     * @param successor   contains the successor position
     * @param evaluation  evaluation of the successor (should be
     *                    either "perfect", "good" or "bad")
     */
    private void writePositionAndSuccessor(Position position,
                                          Position successor,
                                          String evaluation) {

        int pliesToMate = position.getPliesToMate();
        int pliesToMateSucc = successor.getPliesToMate();

        if (!position.getPieces().equals(pieces)) {
            throw invalidPieces;
        }

        // format:
        // - coordinates of the pieces in the current position
        // - coordinates of the pieces in the successor position
        // - position illegal?
        // - position drawn?
        // - steps to mate
        // - evaluation of the move
        outputCoordinates(position.getCoordinates());
        outputCoordinates(successor.getCoordinates());

        outputPositionFlags(true, false, false, position);

        output.println(evaluation);
    }

    /**
     * Given a chess position and a successor, this method estimates
     * how good the move was, wich produced this successor position.
     *
     * @param position   contains a chess position
     * @param successor  contains a successor of the position
     *
     * @return  the evaluation of the move producing the successor, wich
     *          should be either "perfect", "good", "bad" or "error".
     *          The "error" evaluation is returned for example, when
     *          plies to mate count of the successor is lower than expected
     */
    private String evaluateMove(Position position, Position successor) {

        int plies = position.getPliesToMate();
        int succPlies = successor.getPliesToMate();

        boolean draw = position.isDraw();
        boolean succDraw = successor.isDraw();

        int diff = plies + succPlies;
        int sign = sgn(plies);

        if ((!draw && !succDraw && diff == sign) || (draw && succDraw)) {
            return "perfect";
        }

        if (draw && !succDraw && (succPlies < 0)
            || (!draw && (plies < 0) && (succDraw || succPlies < 0))
            || (!draw && (plies > 0) && (succPlies < 0 &&  -succPlies < plies - 1))) {

            return "error";
        }

        if ((draw && !succDraw)
            || (!draw && (plies > 0) && ((succPlies > 0) || succDraw))) {
            return "bad";
        }

        return "good";
    }

    /**
     * Writes all legal successor positions that can be generated
     * with one half-move starting from position given by the
     * parameter <code>position</code>. In case of error, it outputs
     * to the console the offending position and all its successors.
     *
     * @param tablebase  contains a collection with positions wich have
     *                   a precomputed evaluation
     * @param position   contains the chess position whose successors are
     *                   to be written to the ARFF - file
     */
    public void writeSuccessors(Tablebase tablebase, Position position) {

        Move[] moves = position.getMoveList();
        Position successor = null;
        String evaluation = null;
        int foundRightSuccessor = 0;

        if (position.isIllegal()) {
            return;
        }

        for (int i = 0; i < moves.length; i++) {
            successor = tablebase.getPositionAfterMove(position, moves[i]);

            if (successor == null) {
                successor = position.getPositionAfterMove(moves[i]);
                System.out.println(successor);

                successor.normPosition();
                System.out.println(successor);

                System.exit(1);
            }

            if (successor != null && !successor.isIllegal()) {
                evaluation = evaluateMove(position, successor);
                writePositionAndSuccessor(position, successor, evaluation);

                if (foundRightSuccessor == 0 && evaluation.equals("perfect")) {
                    foundRightSuccessor = 1;
                }

                if (evaluation.equals("error")) {
                    foundRightSuccessor = -1;
                }
            }
        }

        if (foundRightSuccessor < 1 &&  position.getPliesToMate() != 0) {
            System.out.println("error while processing the following position:");
            System.out.println(position.toString());

            for (int i = 0; i < moves.length; i++) {
                successor = tablebase.getPositionAfterMove(position, moves[i]);

                if (successor != null /* && !successor.isIllegal()*/) {
                    evaluation = evaluateMove(position, successor);
                    System.out.println(successor.toString());
                }
            }
        }
    }

    /**
     * Computes the signum function
     *
     * @param x  contains an integer number
     *
     * @return  1, when x > 0<br>
     *          0, when x = 0<br>
     *          -1, when x < 0
     */
    public static int sgn(int x) {
        if (x == 0) {
            return 0;
        }

        if (x < 0) {
            return -1;
        } else {
            return 1;
        }

    }

    /**
     * Replaces the method {@link Math#abs}. Should be a lot faster.
     *
     * @param x  contains an integer number
     *
     * @return  -x, when x < 0<br>
     *           x, when x >= 0
     */
    public static int abs(int x) {
        if (x < 0) {
            return -x;
        } else {
            return x;
        }
    }

    /**
     * Removes the extension of a given file name and returns it.
     * For example, if called with "KQK.tbw" it returns "KQK".
     *
     * @param filename   contains a filename as a string
     *
     * @return  string containing the filename without the extension
     */
    public static String removeExtension(String filename) {
        filename = filename.substring(filename.lastIndexOf("/") + 1);
        filename = filename.substring(filename.lastIndexOf("\\") + 1);

        int index = filename.lastIndexOf(".");

        if (index < 0) {
            return filename;
        } else {
            return filename.substring(0, index);
        }
    }

    /**
     * Flushes the content of the buffers and the closes the output file
     */
    public void close() {
        output.println();
        output.close();
    }

    /**
     * Retrieves the full name of the used tablebase format.
     *
     * @return  a string with the full name of the used format
     */
    public String getFormat() {
        return "attribute relation file";
    }

}
