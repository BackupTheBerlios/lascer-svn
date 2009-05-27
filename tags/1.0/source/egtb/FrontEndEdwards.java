/*
 * Filename   : FrontEndEdwards.java
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
 * Implements a specific front end for the edwards tablebase format.
 * Can't handle correctly endgames with pawns and wasn't
 * yet tested on four man endgame tablebases.
 *
 * @author  Edgar Binder
 * @see  egtb.FrontEndLippold
 */
public class FrontEndEdwards extends FrontEnd {

    /**
     * Contains the coordinates of the squares in the symmetry triangle
     * (the weird initialisation is used for passing the style check).
     */
    public static final byte[] TRIANGLE_COORDINATES = toBytes("0 1 2 3 9 10 11 18 19 27");

    /**
     * The code for an illegal position in edwards endgame tablebases
     */
    private static final byte ILLEGAL_POSITION = 127;

    /**
     * Used for adjusting the steps to mate in loosing situations
     */
    private static final int EDWARDS_DELTA = 3;

    /**
     * The content of the tablebase is copied into this array after
     * connecting to the tablebase
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
    private int[] order = null;

    /**
     * Encodes the address of the next item in the endgame tablebase
     */
    private int index = 0;

    /**
     * Is true when the endgame has no pawns, false otherwise
     * (important because endgames without pawns have more symmetry)
     */
    private boolean isPawnless = true;

    /**
     * Is true if the white player is about to move, false otherwise
     */
    private boolean whiteToMove = true;

    /**
     * Loads the endgame tablebase in <code>tablebase</code>
     * and sets the attributes {$link #pieces},
     * {$link #isPawnless} and {$link #whiteToMove}
     *
     * @param filename  the engame tablebase file to be opened
     *
     * @throws IOException  if the filename doesn't exist
     */
    public FrontEndEdwards(String filename) throws IOException {
        FileInputStream input = new FileInputStream(filename);
        String orderedPieces = null;
        String tmpPieces = null;
        int len = 0;
        int pos = 0;

        // endgames where white ist to move are saved
        // in files ending with ".tbw"
        whiteToMove = (getExtension(filename).equals("tbw"));

        pieces = Position.getPiecesFromFilename(filename);
        len = pieces.length();

        orderedPieces = new String(pieces);
        if (!whiteToMove) {
            orderedPieces.replace('K', ' ');
            orderedPieces.replace('k', 'K');
            orderedPieces.replace(' ', 'k');
            //orderedPieces = pieces.substring(pos) + pieces.substring(0, pos);
        }

        // compute the order in which the pieces are saved in the index
        tmpPieces = new String(pieces);
        order = new int[tmpPieces.length()];
        for (int i = 0; i < order.length; i++) {
            pos = tmpPieces.indexOf(orderedPieces.charAt(i));
            order[i] = pos;
            tmpPieces.replaceFirst("" + orderedPieces.charAt(i), " ");
        }

        // pawnless endgames don't have a P in the filename
        isPawnless = (pieces.toUpperCase().indexOf("P") < 0);

        tablebase = new byte[input.available()];
        input.read(tablebase);
        input.close();
    }

    /**
     * Retrieves the chess position given by <code>index</code>
     *
     * @return  an object of type {$link egtb.Position}
     *          representing the desired chess position
     */
    public Position getCurrentPosition() {
        if (index >= tablebase.length) {
            return null;
        } else {
            byte tablebaseElement = tablebase[index];

            // edwards tablebase denotes illegal positions with 127
            boolean illegal = (tablebaseElement == ILLEGAL_POSITION);

            // draws have the value 0
            boolean draw = (tablebaseElement == 0);

            // this is how the number of steps to mate is saved
            int stepsToMate = ILLEGAL_POSITION - ((int) (tablebaseElement) + BYTE_VALUES) % BYTE_VALUES;
            if (stepsToMate < 0) {
                stepsToMate += EDWARDS_DELTA;
            }

            byte[] coordinates = new byte[pieces.length()];
            int len = coordinates.length;
            int bkIndex = order[len - 1];

            // used for computing the coordinates of the
            // king in the symmetry rectangle
            int x = 0;
            int y = 0;

            // reading the coordinates from the index
            for (int i = 0; i < len; i++) {
                coordinates[order[i]] = (byte) ((index >> (COORDINATE_BITS * i)) % BOARD_SIZE);
            }

            // the king gives the symmetry
            if (isPawnless) {
                coordinates[bkIndex] = TRIANGLE_COORDINATES[coordinates[bkIndex]];
            } else {
                x = coordinates[bkIndex] % (BOARD_WIDTH / 2);
                y = (coordinates[bkIndex] / (BOARD_WIDTH / 2));
                coordinates[bkIndex] = (byte) (x + y * BOARD_WIDTH);
            }

            return new Position(whiteToMove, illegal, draw,
                                Position.movesToPlies(stepsToMate),
                                pieces, coordinates,
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
     * position in the endgame tablebase
     */
    public void gotoNextPosition() {
        index++;
    }

    /**
     * Retrieves the full name of the used tablebase format.
     *
     * @return  a string with the full name of the used format
     */
    public String getFormat() {
        return "edwards";
    }

    /**
     * Retrieves the piece which lives in the symmetry triangle/rectangle
     *
     * @return  a character with an abbreviation of the piece
     */
    public char getSymmetryPiece() {
        if (whiteToMove) {
            return 'k';
        } else {
            return 'K';
        }
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
