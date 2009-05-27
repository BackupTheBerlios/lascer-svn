/*
 * Filename   : FrontEnd.java
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

/**
 * This abstract class provides methods for opening a endgame tablebase,
 * reading the current chess position, go to the next position,
 * read the pieces of the endgame and finally a static method
 * that returns to file extension of a given filename.
 *
 * @author Edgar Binder
 */
public abstract class FrontEnd {

    /**
     * Contains the number of different values that can be saved in a byte.
     */
    public static final int BYTE_VALUES = 256;

    /**
     * Contains the size of a standard chess board.
     */
    public static final int BOARD_SIZE = 64;

    /**
     * Contains the width of a standard chess board.
     */
    public static final int BOARD_WIDTH = 8;

    /**
     * Contains the number of bits used to encode a single coordinate.
     */
    public static final int COORDINATE_BITS = 6;

    /**
     * Takes a file name and returns its extension.
     *
     * @param filename  the name of the file whose extension
     *                  should be returned
     *
     * @return  the extension of the file
     */
    public static final String getExtension(String filename) {
        int pos = filename.lastIndexOf(".");
        if (pos < 0) {
            return "";
        } else {
            return filename.substring(pos + 1);
        }
    }

    /**
     * Converts the values in the input string to an array of bytes.
     *
     * @param input  string that contains values in the range 0..255
     *               separated by blanks
     *
     * @return  an a array of bytes containing the values of the
     *          numbers in the input string
     */
    public static final byte[] toBytes(String input) {
        String[] strings = input.split(" ");
        byte[] result = new byte[strings.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = Byte.parseByte(strings[i]);
        }

        return result;
    }

    /**
     * Retrieves the used piece set in the endgame tablebase.
     *
     * @return  the pieces used in the openened endgame tablebase
     */
    public abstract String getPieces();

    /**
     * Retrieves the chess position given by <code>index</code>.
     *
     * @return  an object of type {@link egtb.Position}
     *          representing the desired chess position
     */
    public abstract Position getCurrentPosition();

    /**
     * Increases the index to a new value so that it points to the next
     * position in the endgame tablebase.
     */
    public abstract void gotoNextPosition();

    /**
     * Retrieves the full name of the used tablebase format.
     *
     * @return  a string with the full name of the used format
     */
    public abstract String getFormat();

    /**
     * Retrieves the piece which lives in the symmetry triangle/rectangle
     *
     * @return  a character with an abbreviation of the piece
     */
    public abstract char getSymmetryPiece();

    /**
     * Retrieves the type of symmetry used in the tablebase
     *
     * @return  one of the values {@link egtb.Position#SYMMETRY_NONE},
     *          {@link egtb.Position#SYMMETRY_TRIANGLE},
     *          {@link egtb.Position#SYMMETRY_RECTANGLE}
     */
    public abstract int getSymmetryType();

}
