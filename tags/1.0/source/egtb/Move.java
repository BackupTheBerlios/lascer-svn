/*
 * Filename   : Move.java
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
 * Models a single chess move
 *
 * @author Edgar Binder
 */
public class Move {

    /**
     * X-coordinate of the start field
     */
    private byte oldX = 0;

    /**
     * Y-coordinate of the start field
     */
    private byte oldY = 0;

    /**
     * X-coordinate of the target field
     */
    private byte newX = 0;

    /**
     * Y-coordinate of the target field
     */
    private byte newY = 0;

    /**
     * Creates a new move from the (x, y) - coordinates
     * of the start field and the (x, y) - coordinates of
     * the target field.
     *
     * @param oldX  x - coordinate of the start field
     * @param oldY  y - coordinate of the start field
     * @param newX  x - coordinate of the target field
     * @param newY  y - coordinate of the target field
     */
    public Move(int oldX, int oldY, int newX, int newY) {
        this.oldX = (byte) oldX;
        this.oldY = (byte) oldY;
        this.newX = (byte) newX;
        this.newY = (byte) newY;
    }

    /**
     * Retrieves the x - coordinate of the start field
     *
     * @return  an int containing the x - coordinate
     */
    public int oldX() {
        return oldX;
    }

    /**
     * Retrieves the y - coordinate of the start field
     *
     * @return  an int containing the y - coordinate
     */
    public int oldY() {
        return oldY;
    }

    /**
     * Retrieves the x - coordinate of the target field
     *
     * @return  an int containing the x - coordinate
     */
    public int newX() {
        return newX;
    }

    /**
     * Retrieves the y - coordinate of the target field
     *
     * @return  an int containing the y - coordinate
     */
    public int newY() {
        return newY;
    }
}
