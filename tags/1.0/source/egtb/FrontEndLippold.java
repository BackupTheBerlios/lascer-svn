/*
 * Filename   : FrontEndLippold.java
 * Last change: 24.04.2005 by Edgar Binder
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

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Implements a specific front end for the lippold endgame tablebase format.
 * Can deal with both the Pascal (large) and the C-Style (small) formats.
 *
 * @author Edgar Binder
 * @see egtb.FrontEndEdwards
 */
public class FrontEndLippold extends FrontEnd {

    /**
     * Code for an illegal position.
     */
    public static final byte LPD_ILLEGAL = 0;

    /**
     * Code for positions that belong to other endgames,
     * for example the kind that appear after a pawn conversion
     */
    public static final byte LPD_NACHESP = 1;

    /**
     * Code for positions that weren't yet derived
     * (only used for generating the tablebase,
     * should never actually exist after then
     * generator finished)
     */
    public static final byte LPD_NICHTBEW = 2;

    /**
     * Code for a position where the curent side just mated.
     */
    public static final byte LPD_MATT = 3;

    /**
     * Code for a stalemate position.
     */
    public static final byte LPD_PATT = 4;

    /**
     * Code for a technically drawn position
     * (position where there are only the two kings left)
     */
    public static final byte LPD_TREMIS = 5;

    /**
     * Code for a position for wich white has a forced win.
     */
    public static final byte LPD_WGEWINN = 6;

    /**
     * Code for a position for wich black has a forced win.
     */
    public static final byte LPD_SGEWINN = 7;

    /**
     * Code for a drawn position.
     */
    public static final byte LPD_REMIS = 8;

    /**
     * The size of items in the Pascal generated tablebase
     */
    public static final int PASCAL_ITEM_SIZE = 4;

    /**
     * Contains the coordinates of the squares in the symmetry triangle
     * (weird initialisiation due to style checker).
     */
    public static final byte[] TRIANGLE_COORDINATES = toBytes("0 1 9 2 10 18 3 11 19 27");

    /**
     * The content of the tablebase is copied into this array after
     * connecting to the tablebase.
     */
    private byte[] tablebase = null;

    /**
     * See {@link egtb.BackEnd#pieces}
     */
    private String pieces = null;

    /**
     * Contains a mapping of the pieces from the index order
     * to the order used in {@link egtb.BackEnd#pieces}
     */
    private int[] mapping = null;

    /**
     * Encodes the address of the next item in the endgame tablebase.
     */
    private int index = 0;

    /**
     * Is true when the endgame has no pawns, false otherwise
     * (important because endgames without pawns have more symmetry).
     */
    private boolean isPawnless = true;


    /**
     * Reads a given number of items from the input stream
     * and returns them as a byte array.
     *
     * @param input     contains the input stream from wich we want to read
     * @param count     number of items to be read
     * @param itemSize  size of one item in bytes
     *
     * @return  an array of bytes containing the items
     *
     * @throws IOException  when an error occurs while reading the items
     *                      from the input stream
     */
    private byte[] readItemsIntoBytes(FileInputStream input, int count, int itemSize) throws IOException {
        byte[] result = new byte[count];

        if (itemSize == 1) {
            input.read(result);
        } else {
            byte[] buffer = new byte[count * itemSize];

            input.read(buffer);

            for (int i = 0; i < count; i++) {
                result[i] = buffer[itemSize * i];
            }
        }

        return result;
    }

    /**
     * Computes the size of the items in the input file
     * (Pascal generated tablebases always have sizes that are divisible by 4,
     *  while C generated tablebases always have odd sizes).
     *
     * @param input  the input stream containing the tablebase
     *
     * @return  an integer containing the size of the items in
     *          the file (either 1 or 4)
     *
     * @throws IOException  when there's some kind of error
     *                      in the input stream
     */
    private int getItemSize(FileInputStream input) throws IOException {
        if (input.available() % PASCAL_ITEM_SIZE == 0) {
            return PASCAL_ITEM_SIZE;
        } else {
            return 1;
        }
    }

    /**
     * Loads the endgame tablebase in <code>tablebase</tablebase>
     * and sets the attributes {@link #pieces} and {@link #isPawnless}
     *
     * @param filename  the engame tablebase file to be opened
     *
     * @throws IOException  if the filename doesn't exist
     */
    public FrontEndLippold(String filename) throws IOException {

        FileInputStream input = new FileInputStream(filename);
        int itemSize = getItemSize(input);
        int numPieces = readItemsIntoBytes(input, 1, itemSize)[0];
//        byte[] header = new byte[2 * (numPieces - 2) + 2];
        byte[] header = readItemsIntoBytes(input, 2 * (numPieces - 1), itemSize);

        String whitePieces = "K";
        String blackPieces = "k";
        String tmpPieces = null;

        // will contain pieces in the same order as in the index order
        // (see comment of the method getCurrentPosition)
        String orderedPieces = "Kk";

        byte color = 0;
        char piece = 'K';
        int pieceIndex = 0;

//        input.read(header);

        // get the white and the black piece set
        // also determine the correct piece order.
        for (int i = 0; i < numPieces - 2; i++) {

            color = header[i * 2];
            piece = germanToEnglish(header[i * 2 + 1], color);

            if (header[i * 2] == 0) {
                whitePieces += piece;
            } else {
                blackPieces += piece;
            }

            orderedPieces = piece + orderedPieces;
        }

        // the used piece set is composed of the white and the black pieces
        // (white pieces first). The first white (black) piece must be the white
        // (black) king. The remaining white (black) pieces don't have to
        // have any special order.
        pieces = whitePieces + blackPieces;

        // compute a mapping from index order to the order of the
        // pieces in the string "pieces"
        tmpPieces = new String(pieces);
        mapping = new int[tmpPieces.length()];
        for (int i = 0; i < mapping.length; i++) {
            pieceIndex = tmpPieces.indexOf(orderedPieces.charAt(i));
            mapping[i] = pieceIndex;
            tmpPieces.replaceFirst("" + orderedPieces.charAt(i), " ");
        }

        // pawnless endgames don't have a P in the pieces
        isPawnless = (pieces.toUpperCase().indexOf("P") < 0);

        //tablebase = new byte[input.available()];
        //input.read(tablebase);
        tablebase = readItemsIntoBytes(input, input.available() / itemSize, itemSize);
        input.close();
    }

    /**
     * Converts a piece given by a german code to the native
     * format (K -> K, D -> Q, L -> B, T -> R, x -> P). Black
     * pieces are converted to lowercase letters, white pieces
     * to uppercase leters.
     *
     * @param germanPiece  ASCII code of the german abbreviation
     *                     of a chess pieces (King -> 'K', Queen -> 'D',
     *                     Bishop -> 'L', Knight -> 'S', Pawn -> 'x')
     *
     * @param color  0 means white, 1 means black
     *
     * @return  english abbreviation of the piece as a char
     */
    private static char germanToEnglish(byte germanPiece, byte color) {
        char piece = "KQBNRPPPPPPPPP".charAt("KDLSTxabcdefgh".indexOf((char) germanPiece));
        if (color == 0) {
            return piece;
        } else {
            return Character.toLowerCase(piece);
        }
    }

    /**
     * Retrieves the chess position given by <code>index</code>.
     * Here's a description of the index used by the three
     * pieces lippold format:<P>
     *
     * <pre>
     * 0 1 2 3 4 5 6 7 8 9 10 11 12  13 14 15 16 17
     * | ----------- --------------  --------------
     * | | y  |  x | |  y  |   x  |  the id of the
     * | ----------- --------------  black king's
     * | field pos.  field pos. of   field in the
     * | of the 3rd  the white king  symmetry triangle/rectangle
     * | piece                       (depending whether the 3rd
     * |                             piece is a pawn)
     * |
     * -----> the parity bit gives the color (0 white, 1 black)
     * </pre>
     *
     * @return  an object of type {@link egtb.Position}
     *          representing the desired chess position
     */
    public Position getCurrentPosition() {
        if (index * 2 + 1 >= tablebase.length) {
            return null;
        } else {
            byte type = tablebase[index * 2];
            byte value = tablebase[index * 2 + 1];
            int coordinate = 0;

            //STELLART = (illegal,nachesp,nichtbew,matt,patt,tremis,wgewinn,sgewinn,remis);
            boolean illegal = (type == LPD_ILLEGAL);
            boolean draw = (type == LPD_PATT || type == LPD_TREMIS || type == LPD_REMIS);
            boolean whiteToMove = (index % 2 == 0);

            // this is how the number of steps to mate is saved
            int pliesToMate = value;

            if (whiteToMove && (type == LPD_SGEWINN) || (!whiteToMove && (type == LPD_WGEWINN))) {
                pliesToMate = -pliesToMate;
            }

            byte[] coordinates = new byte[pieces.length()];
            int numPieces = coordinates.length;

            // mapping for the black king (index order to used piece set order)
            int bkMapping = mapping[numPieces - 1];

            // used for computing the position of the king
            int x = 0;
            int y = 0;

            // decoding the coordinates from the index
            for (int i = 0; i < numPieces; i++) {

                // decode the i-th coordinate from the index
                // (see method comment)
                coordinate = (index >> ((COORDINATE_BITS * i) + 1)) % BOARD_SIZE;

                // decode the x, y values from the coordinate
                x = coordinate / BOARD_WIDTH;
                y = coordinate % BOARD_WIDTH;

                // save the coordinate with help from mapping
                // (mapping maps a the coordinate address from the
                //  the index order to the order in the used piece set)
                coordinates[mapping[i]] = (byte) coordinate;

                // the lippold - format has the x, y values swaped,
                // so if the current piece isn't the king we correct this.
                if (mapping[i] != bkMapping) {
                    coordinates[mapping[i]] = (byte) (x + BOARD_WIDTH * y);
                }
            }

            // the black king lives in the symmetry triangle/rectangle
            if (isPawnless) {
                coordinates[bkMapping] = TRIANGLE_COORDINATES[coordinates[bkMapping]];
            } else {
                x = (coordinates[bkMapping] / BOARD_WIDTH);
                y = (coordinates[bkMapping] % BOARD_WIDTH);
                coordinates[bkMapping] = (byte) (x + y * BOARD_WIDTH);
            }

            return new Position(whiteToMove, illegal, draw,
                                pliesToMate, pieces, coordinates,
                                getSymmetryPiece(), getSymmetryType());
        }
    }

    /**
     * Retrieves the used piece set in the endgame tablebase.
     *
     * @return  the pieces used in the openened endgame tablebase
     */
    public String getPieces() {
        return pieces;
    }

    /**
     * Increases the index to a new value so that it points to the next
     * position in the endgame tablebase.
     */
    public void gotoNextPosition() {
        index += 1;
    }

    /**
     * Retrieves the full name of the used tablebase format.
     *
     * @return  a string with the full name of the used format
     */
    public String getFormat() {
        return "lippold";
    }

    /**
     * Retrieves the piece which lives in the symmetry triangle/rectangle
     *
     * @return  a character with an abbreviation of the piece
     */
    public char getSymmetryPiece() {
       return 'k';
    }

    /**
     * Retrieves the type of symmetry used in the tablebase
     *
     * @return  one of the values {@link egtb.Position#SYMMETRY_NONE},
     *          {@link egtb.Position#SYMMETRY_TRIANGLE},
     *          {@link egtb.Position#SYMMETRY_RECTANGLE}
     */
    public int getSymmetryType() {
        if (isPawnless) {
            return Position.SYMMETRY_TRIANGLE;
        } else {
            return Position.SYMMETRY_RECTANGLE;
        }
    }

}
