/*
 * Filename   : PositionComparator.java
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
 * Stateless class which contains a single method for comparing
 * two chess positions.
 *
 * @author Edgar Binder
 */
public class PositionComparator implements java.util.Comparator {

    /**
     * Compares two special objects <code>obj1</code> and
     * <code>obj2</code> which represent chess positions
     * (see {@link Position}). If the two positions are
     * equal, the comparision result is 0, otherwise it's
     * -1 if the first position is "smaller" and 1 if the
     * first position is greater. The following criterions
     * are used with decreasing priority to determine which
     * position is smaller:<p>
     *
     * - color of the side to move (white is smaller)<br>
     * - lexical orders of the used piece sets<br>
     * - the coordinates of the pieces
     *
     * @param obj1  first chess position
     * @param obj2  second chess position
     *
     * @return  0 if the positions are equal, -1 if the first
     *          position is smaller and 1 otherwise.
     */
    public int compare(Object obj1, Object obj2) {

        int i = 0;
        Position pos1 = (Position) obj1;
        Position pos2 = (Position) obj2;
        byte[] coords1 = pos1.getCoordinates();
        byte[] coords2 = pos2.getCoordinates();
        String pieces1 = pos1.getPieces();
        String pieces2 = pos2.getPieces();
        int pieceComparison = pieces1.compareTo(pieces2);

        if (pos1.whiteToMove() != pos2.whiteToMove()) {
            if (pos1.whiteToMove()) {
                return -1;
            } else {
                return 1;
            }
        }

        if (pieceComparison < 0) {
            return -1;
        }

        if (pieceComparison > 0) {
            return 1;
        }

        // here we have equal piece sets, thus equal sized coordinate arrays
        while (i < coords1.length && coords1[i] == coords2[i]) {
            i++;
        }

        if (i < coords1.length) {
            if (coords1[i] < coords2[i]) {
                return -1;
            } else {
                return 1;
            }
        }

        return 0;
    }
}
