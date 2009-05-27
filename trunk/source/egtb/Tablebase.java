/*
 * Filename   : Tablebase.java
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

import java.util.TreeSet;
import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;

/**
 * Models a (possibly sorted) collection of chess positions which can be
 * added and retrieved. The mode of operation (unsorted or sorted) can
 * be chosen through the constructor. When working in the sorted mode
 * the tablebase supports very fast retrieving of positions (but
 * the addition of new ones is slower)
 *
 * @author Edgar Binder
 */
public class Tablebase {

    /**
     * This collection contains all chess positions stored in the tablebase
     */
    private Collection positions = null;

    /**
     * The comparator is used to compare a chess position to another one
     */
    private PositionComparator comparator = new PositionComparator();

    /**
     * Used for retrieving the chess positions stored in the tablebase
     */
    private Iterator iterator = null;

    /**
     * Constructs a new tablebase object. If <code>sorted</code>
     * is true, then the tablebase sorts its collection of
     * positions.
     *
     * @param sorted  when true, the tablebase keeps the stored
     *                chess positions sorted
     */
    public Tablebase(boolean sorted) {

        if (sorted) {
            positions = new TreeSet(comparator);
        } else {
            positions = new Vector();
        }
    }

    /**
     * Adds a new chess position to the tablebase and sets
     * the iterator to null.
     *
     * @param pos  contains the chess position to be added to the tablebase
     * @see #readPosition()
     */
    public void addPosition(Position pos) {
        positions.add(pos);
        iterator = null;
    }

    /**
     * Searches the tablebase for the position given by <code>pos</code>.
     * If found, it returns the full position (including evaluation),
     * otherwise it returns <code>null</code>.
     *
     * @param pos  position to be searched
     *
     * @return  <code>null</code> if the position is not found,
     *          otherwise the same position including evaluation.
     */
    public Position findPosition(Position pos) {
        TreeSet set = (TreeSet) ((TreeSet) positions).tailSet(pos);
        Position found = null;
        int i;

        if (set.iterator().hasNext()) {
            found = (Position) set.first();

            if (!pos.hasSameCoordinates(found)) {
                return null;
            }

            return (Position) found.clone();

        } else {
            return null;
        }
    }

    /**
     * Gives back the position which arises after a move
     * is made on the position <code>pos</code>.
     *
     * @param pos   start position
     * @param move  the move to be made
     *
     * @return  the position after the move has been made,
     *          including the evaluation of this position.
     */
    public Position getPositionAfterMove(Position pos, Move move) {
        byte[] coordinates = null;
        Position succ = pos.getPositionAfterMove(move);

        coordinates = (byte[]) succ.getCoordinates().clone();

        succ.normPosition();
        succ = findPosition(succ);

        if (succ != null) {
            succ.setCoordinates(coordinates);
        }

        return succ;
    }

    /**
     * Retrieves the next position from the tablebase. If the iterator
     * is null, the it retrieves a new iterator. If there aren't any
     * positions left, <code>null</code> is returned.
     *
     * @return  the next position given by {$link iterator}
     */
    public Position readPosition() {

        if (iterator == null) {
            iterator = positions.iterator();
        }

        if (iterator.hasNext()) {
            return (Position) ((Position) iterator.next()).clone();
        } else {
            return null;
        }
    }
}
