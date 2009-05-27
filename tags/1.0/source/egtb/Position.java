/*
 * Filename   : Position.java
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
 * Models a single chess position and provides methods to construct
 * the position, convert the position to a string and read single
 * attributes of the position (like piece coordinates, piece types
 * and so on).
 *
 * @author Edgar Binder
 */
public class Position implements Cloneable {

    /**
     * Code for a tablebase using no symmetry
     */
    public static final int SYMMETRY_NONE = 0;

    /**
     * Code for a tablebase using a triangle symmetry
     */
    public static final int SYMMETRY_TRIANGLE = 1;

    /**
     * Code for a tablebase using a rectangle symmetry
     */

    public static final int SYMMETRY_RECTANGLE = 2;

    /**
     * Contains the width of a standard chess board.
     */
    public static final int WIDTH = 8;

    /**
     * Code for a white colored chess piece
     */
    private static final int COLOR_WHITE = 0;

    /**
     * Code for a black colored chess piece
     */
    private static final int COLOR_BLACK = 1;

    /**
     * Maximal number of moves that
     */
    private static final int MAX_MOVES = 256;

    /**
     * Is <code>true</code> for chess positions where white has
     * the next move, otherwise <code>false</code>.
     */
    private boolean whiteToMove = true;

    /**
     * Is <code>true</code> for illegal positions,
     * otherwise <code>false</code>.
     */
    private boolean illegal = false;

    /**
     * Is <code>true</code> for drawn chess positions (including stalemates,
     * positions with insuficient material and so on).
     */
    private boolean draw = false;

    /**
     * If positive, it contains the minimal number of half-moves that
     * is needed to checkmate the oponent. If negative, it contains
     * the maximal number of half-moves one can play before getting
     * checkmated.
     */
    private int pliesToMate = 0;

    /**
     * See {@link egtb.BackEnd#pieces}.
     */
    private String pieces = "";

    /**
     * Contains the coordinates of the pieces, a piece
     * in the bottom-left corner has the coordinate 0.
     * The coordinate of a piece in the top-right corner is 63.
     */
    private byte[] coordinates = null;

    /**
     * Number of the piece which is used for position symmetry
     */
    private int symmetryPieceNum = -1;

    /**
     * Type of the used symmetry (either {@link #SYMMETRY_NONE},
     * {@link #SYMMETRY_TRIANGLE} or {@link #SYMMETRY_RECTANGLE})
     */
    private int symmetryType = 0;


    /**
     * Class constructor which sets the object with a given chess position.
     *
     * @param whiteToMove    see {@link #whiteToMove}
     * @param illegal        see {@link #illegal}
     * @param draw           see {@link #draw}
     * @param pliesToMate    see {@link #pliesToMate}
     * @param pieces         see {@link egtb.BackEnd#pieces}
     * @param coordinates    see {@link #coordinates}
     * @param symmetryPiece  see {@link #symmetryPieceNum}
     * @param symmetryType   see {@link #symmetryType}
     */
    public Position(boolean whiteToMove, boolean illegal, boolean draw,
                    int pliesToMate, String pieces, byte[] coordinates,
                    char symmetryPiece, int symmetryType) {


        // check if the number of coordinates matches the number of pieces
        if (coordinates.length != pieces.length()) {
            throw new RuntimeException("invalid position!");
        }

        // check if all coordinates are in [0..63]
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i] < 0 || coordinates[i] >= WIDTH * WIDTH) {
                throw new RuntimeException("invalid position!");
            }
        }

        // set all position attributes
        this.whiteToMove = whiteToMove;
        this.illegal = illegal;
        this.draw = draw & !illegal;
        this.pieces = pieces;
        this.coordinates = coordinates;
        this.symmetryPieceNum = pieces.indexOf(symmetryPiece);
        this.symmetryType = symmetryType;

        // drawn or illegal positions shouldt have pliesToMate = 0
        if (illegal || draw) {
            this.pliesToMate = 0;
        } else {
            this.pliesToMate = pliesToMate;
        }
    }

    /**
     * Converts the number of moves of *one* playing side, to the
     * number of moves of both sides (plies). For example, if the
     * current side is white and does three moves both sides move
     * five time (white black white black white).
     *
     * @param movesToMate  the number of moves of that the current
     *                     side does
     *
     * @return  the number of moves that both sides make
     */
    public static int movesToPlies(int movesToMate) {
        if (movesToMate > 0) {
            return (movesToMate - 1) * 2 + 1;
        } else {
            return movesToMate * 2;
        }
    }

    /**
     * Returns a string containing the pieces of the endgame tablebase
     * given by filename.
     *
     * @param filename  name of the engame tablebase file (used to
     *                  extract the pieces)
     *
     * @return  a string containing the {@link egtb.BackEnd#pieces}
     */
    public static String getPiecesFromFilename(String filename) {
        String result = "";
        int i = 0;
        char ch = ' ';
        boolean white = false;

        // get rid of eventual paths in the filename and uppercase it
        filename = (filename.substring(filename.lastIndexOf("/") + 1)).toUpperCase();

        while ((i < filename.length())
            && ("KQBNRP".indexOf(filename.charAt(i)) > -1)) {

            ch = filename.charAt(i);

            // the king toggles the current color
            if (ch == 'K') {
                white = !white;
            }

            if (!white) {
                ch = Character.toLowerCase(ch);
            }

            result += ch;
            i++;
        }

        return result;
    }

    /**
     * Retrieves <code>true</code> when it's white turn to move,
     * <code>false</false> otherwise.
     *
     * @return  the attribute {@link #whiteToMove}
     */
    public boolean whiteToMove() {
        return whiteToMove;
    }

    /**
     * Retrieves <code>true</code> when the position is drawn,
     * <code>false</code> otherwise.
     *
     * @return  the attribute {@link #draw}
     */
    public boolean isDraw() {
        return draw;
    }

    /**
     * Retrieves <code>true</code> when the position is illegal,
     * <code>false</code> otherwise.
     *
     * @return  the attribute {@link #illegal}
     */
    public boolean isIllegal() {
        return illegal;
    }

    /**
     * Retrieves the number of plies until the side to move either
     * gets checkmated or wins by force.
     *
     * @return  the attribute {@link #pliesToMate}
     */
    public int getPliesToMate() {
        return pliesToMate;
    }

    /**
     * Retrieves the piece set of the position.
     *
     * @return  the attribute {@link #pieces}
     */
    public String getPieces() {
        return pieces;
    }

    /**
     * Retrieves the coordinates of the pieces.
     *
     * @return  the attribute {@link #coordinates}
     */
    public byte[] getCoordinates() {
        return coordinates;
    }

    /**
     * Converts the current position to a string that can be later
     * printed for example with System.out.println.
     *
     * @return  a string representing the chess position
     */
    public String toString() {
        String header = null;
        char[] board = ("........\n"
                      + "........\n"
                      + "........\n"
                      + "........\n"
                      + "........\n"
                      + "........\n"
                      + "........\n"
                      + "........\n").toCharArray();

        // used for computing the positions of the pieces
        int x = 0;
        int y = 0;

        if (illegal) {
            header = "Illegal position";
        } else {
            if (draw) {
                header = "Draw";
            } else {
                if (pliesToMate > 0) {
                    header = "Mate in " + pliesToMate + " plies";
                } else {
                    if (pliesToMate == 0) {
                        header = "Checkmate";
                    } else {
                        header = "Lost in " + (-pliesToMate) + " plies";
                    }
                }
            }
        }

        if (whiteToMove) {
            header += " (white to move):\n";
        } else {
            header += " (black to move):\n";
        }
        // places the pieces on the board
        for (int i = 0; i < coordinates.length; i++) {
            x = coordinates[i] % WIDTH;
            y = (WIDTH - 1) - (coordinates[i] / WIDTH);
            board[y * (WIDTH + 1) + x] = pieces.charAt(i);
        }

        return header + String.valueOf(board);
    }

    /**
     * Retrieves the color of a piece specified by its number
     * (position in {@link #pieces})
     *
     * @param piece  piece code (for example K, Q, ... )

     * @return  {@link #COLOR_WHITE} (if the piece is white),
     *          {@link #COLOR_BLACK} otherwise
     */
    private int getPieceColor(char piece) {
        if (piece < 'Z') {
            return COLOR_WHITE;
        } else {
            return COLOR_BLACK;
        }

    }

    /**
     * Retrieves a character representing the piece at the
     * coordinate given by <code>fieldCoordinate</code>.
     * If there's no piece on the field, a blank is returned.
     *
     * @param fieldCoordinate  coordinate of a field on the chess board
     *
     * @return  a character with the code of the piece standing on the
     *          field (for example 'K' if it's a white king). A blank
     *          is returned for empty fields
     */
    private char getPieceOnField(int fieldCoordinate) {

        // check if there's a piece on the field coordinate
        // if so, return that piece
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i] == fieldCoordinate) {
                return pieces.charAt(i);
            }
        }

        // otherwise return ' '
        return ' ';
    }

    /**
     * Checks whether the position has any captured pieces.
     *
     * @return  <code>true</code> if there are any captured pieces,
     *          <code>false</code> otherwise
     */
    public boolean hasCaptures() {

        // check for collisions
        for (int i = 0; i < coordinates.length; i++) {
            if (getPieceOnField(coordinates[i]) != pieces.charAt(i)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether the positions contains any conversions
     * (pawns on the promotion ranks)
     *
     * @return  <code>true</code> if there are conversions,
     *          <code>false</code> otherwise
     */
    public boolean hasConversions() {

        int y;
        String uppercasePieces = pieces.toUpperCase();

        // check for collisions
        for (int i = 0; i < coordinates.length; i++) {
            y = coordinates[i] / WIDTH;
            if ((y == 0 || y == WIDTH - 1) && (uppercasePieces.charAt(i) == 'P')) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the piece with the number <code>pieceNum</code> can be
     * moved to a field given by x + <code>dx</code> and
     * y + <code>dy</code> (where x, y are the current coordinates
     * of the piece). If the move is possible, then it's written
     * into the array <code>moveList</code> at the index given by
     * <code>nextMoveSlot</code>, and <code>nextMoveSlot</code>+1
     * is returned. If the move is not possible (for example because there's
     * already piece of the same color on the target field, or the field
     * isn't on the board) then <code>nextMoveSlot</code> is returned,
     * and no changes are  made to <code>moveList</code>
     *
     * @param moveList      an array containing moves
     * @param pieceNum      number of the piece which should be moved
     * @param dx            displacement between the x-coordinate of
     *                      the target field and the start field of
     *                      the move
     * @param dy            displacement between the y-coordinate of
     *                      the target field and the start field of
     *                      the move
     * @param nextMoveSlot  index in the array moveList where a
     *                      move can be written
     *
     * @return              index of the next free slot in the array
     *                      <code>moveList</code> after the (possible)
     *                      addition of the new move
     */
    private int addMove(Move[] moveList, int pieceNum, int dx, int dy, int nextMoveSlot) {

        int x = coordinates[pieceNum] % WIDTH;
        int y = coordinates[pieceNum] / WIDTH;
        int newX = x + dx;
        int newY = y + dy;

        int pieceColor = getPieceColor(pieces.charAt(pieceNum));

        if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < WIDTH) {

            int newCoord = (x + dx) + WIDTH * (y + dy);
            char targetFieldPiece = getPieceOnField(newCoord);

            // check if there's a piece of the same color at the target coordinates
            if (targetFieldPiece != ' ' && getPieceColor(targetFieldPiece) == pieceColor) {
                return nextMoveSlot;
            }

            // write the piece into move list
            moveList[nextMoveSlot] = new Move(x, y, newX, newY);
            nextMoveSlot++;
        }

        return nextMoveSlot;
    }

    /**
     * Adds moves to a "ray" of fields into the array moveList and
     * returns the updated free move slot position in moveList.
     * The direction of the ray is given by <code>dx</code> and
     * by <code>dy</code>, it starts at the position of the
     * piece given by <code>pieceNum</code> and it lasts until
     * the first obstacle (either the border of the chess board
     * or another piece).
     *
     * @param moveList      an array where the moves to the fields
     *                      on the ray are written to
     * @param pieceNum      number of the piece from where the ray starts
     * @param dx            the x-displacement of the ray
     * @param dy            the y-displacement of the ray
     * @param nextMoveSlot  position of the first free slot in
     *                      the array <code>moveList</code>
     *
     * @return  index of the next slot where a move can be written to
     *          <code>moveList</code>
     */
    private int addRay(Move[] moveList, int pieceNum, int dx, int dy, int nextMoveSlot) {
        int x = coordinates[pieceNum] % WIDTH;
        int y = coordinates[pieceNum] / WIDTH;
        int newX = x + dx;
        int newY = y + dy;
        int newCoord = 0;
        char targetFieldPiece = ' ';
        int pieceColor = getPieceColor(pieces.charAt(pieceNum));

        while (newX >= 0 && newX < WIDTH && newY >= 0 && newY < WIDTH) {

            newCoord = newX + WIDTH * newY;
            targetFieldPiece = getPieceOnField(newCoord);

            moveList[nextMoveSlot] = new Move(x, y, newX, newY);

            if (targetFieldPiece != ' ') {
                if (getPieceColor(targetFieldPiece) == pieceColor) {
                    return nextMoveSlot;
                } else {
                    return nextMoveSlot + 1;
                }
            }

            newX += dx;
            newY += dy;
            nextMoveSlot++;
        }

        return nextMoveSlot;
    }

    /**
     * Adds all moves a king can do to the move array
     * <code>moveList</code> and returns the updated next
     * move slot.
     *
     * @param moveList      an array where the king moves can be written to
     * @param pieceNum      number of a king piece
     * @param nextMoveSlot  position of the first free move slot in
     *                      <code>moveList</code>
     *
     * @return  the value of the first free move slot in the updated array
     *          <code>moveList</code>
     */
    private int addKingMoves(Move[] moveList, int pieceNum, int nextMoveSlot) {

        nextMoveSlot = addMove(moveList, pieceNum, -1, -1, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum,  0, -1, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum,  1, -1, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum,  1,  0, nextMoveSlot);

        nextMoveSlot = addMove(moveList, pieceNum,  1,  1, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum,  0,  1, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum, -1,  1, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum, -1,  0, nextMoveSlot);

        return nextMoveSlot;
    }

    /**
     * Adds all moves the pawns can do to the move array
     * <code>moveList</code> and returns the updated next
     * move slot.
     *
     * @param moveList      an array where the pawn moves can be written to
     * @param pieceNum      number of a pawn piece
     * @param nextMoveSlot  position of the first free move slot in
     *                      <code>moveList</code>
     *
     * @return  the value of the first free move slot in the updated array
     *          <code>moveList</code>
     */
    private int addPawnMoves(Move[] moveList, int pieceNum, int nextMoveSlot) {

        int x = coordinates[pieceNum] % WIDTH;
        int y = coordinates[pieceNum] / WIDTH;
        char piece = pieces.charAt(pieceNum);
        int color = getPieceColor(piece);
        int deltaY = 1;

        if (color == COLOR_BLACK) {
            deltaY = -1;
        }

        if (y != 0 && y != WIDTH - 1) {
            if (getPieceOnField((y + deltaY) * WIDTH + x) == ' ') {
                nextMoveSlot = addMove(moveList, pieceNum, 0, deltaY, nextMoveSlot);

                if ((y == 1 && color == COLOR_WHITE)
                    || (y == 1 && color == COLOR_BLACK)) {

                    if (getPieceOnField((y + 2 * deltaY) * WIDTH + x) == ' ') {
                        nextMoveSlot = addMove(moveList, pieceNum, 0, 2 * deltaY, nextMoveSlot);
                    }
                }
            }

            if (x > 0 && getPieceOnField((y + deltaY) * WIDTH + x - 1) != ' ') {
                nextMoveSlot = addMove(moveList, pieceNum, -1, deltaY, nextMoveSlot);
            }

            if (x < WIDTH - 1 && getPieceOnField((y + deltaY) * WIDTH + x + 1) != ' ') {
                nextMoveSlot = addMove(moveList, pieceNum,  1, deltaY, nextMoveSlot);
            }
        }

        return nextMoveSlot;
    }

    /**
     * Adds all moves a queen can do to the move array
     * <code>moveList</code> and returns the updated next
     * move slot.
     *
     * @param moveList      an array where the queen moves can be written to
     * @param pieceNum      number of a queen piece
     * @param nextMoveSlot  position of the first free move slot in
     *                      <code>moveList</code>
     *
     * @return  the value of the first free move slot in the updated array
     *          <code>moveList</code>
     */
    private int addQueenMoves(Move[] moveList, int pieceNum, int nextMoveSlot) {

        nextMoveSlot = addRay(moveList, pieceNum, -1, -1, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum,  0, -1, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum,  1, -1, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum,  1,  0, nextMoveSlot);

        nextMoveSlot = addRay(moveList, pieceNum,  1,  1, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum,  0,  1, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum, -1,  1, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum, -1,  0, nextMoveSlot);

        return nextMoveSlot;
    }

    /**
     * Adds all moves a rook can do to the move array
     * <code>moveList</code> and returns the updated next
     * move slot.
     *
     * @param moveList      an array where the rook moves can be written to
     * @param pieceNum      number of a rook piece
     * @param nextMoveSlot  position of the first free move slot in
     *                      <code>moveList</code>
     *
     * @return  the value of the first free move slot in the updated array
     *          <code>moveList</code>
     */
    private int addRookMoves(Move[] moveList, int pieceNum, int nextMoveSlot) {

        nextMoveSlot = addRay(moveList, pieceNum,  0, -1, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum,  1,  0, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum,  0,  1, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum, -1,  0, nextMoveSlot);

        return nextMoveSlot;
    }

    /**
     * Adds all moves a bishop can do to the move array
     * <code>moveList</code> and returns the updated next
     * move slot.
     *
     * @param moveList      an array where the bishop moves can be written to
     * @param pieceNum      number of a bishop piece
     * @param nextMoveSlot  position of the first free move slot in
     *                      <code>moveList</code>
     *
     * @return  the value of the first free move slot in the updated array
     *          <code>moveList</code>
     */
    private int addBishopMoves(Move[] moveList, int pieceNum, int nextMoveSlot) {

        nextMoveSlot = addRay(moveList, pieceNum, -1, -1, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum,  1, -1, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum,  1,  1, nextMoveSlot);
        nextMoveSlot = addRay(moveList, pieceNum, -1,  1, nextMoveSlot);

        return nextMoveSlot;
    }

    /**
     * Adds all moves a knight can do to the move array
     * <code>moveList</code> and returns the updated next
     * move slot.
     *
     * @param moveList      an array where the knight moves can be written to
     * @param pieceNum      number of a knight piece
     * @param nextMoveSlot  position of the first free move slot in
     *                      <code>moveList</code>
     *
     * @return  the value of the first free move slot in the updated array
     *          <code>moveList</code>
     */
    private int addKnightMoves(Move[] moveList, int pieceNum, int nextMoveSlot) {

        nextMoveSlot = addMove(moveList, pieceNum, -1, -2, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum,  1, -2, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum,  2, -1, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum,  2,  1, nextMoveSlot);

        nextMoveSlot = addMove(moveList, pieceNum,  1,  2, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum, -1,  2, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum, -2, -1, nextMoveSlot);
        nextMoveSlot = addMove(moveList, pieceNum, -2,  1, nextMoveSlot);

        return nextMoveSlot;
    }


    /**
     * Retrieves an array of moves containing all moves that
     * the side to move can do in the current position.
     *
     * @return  an array of moves with the moves of the side to move
     */
    public Move[] getMoveList() {

        Move[] result = null;
        Move[] moveList = new Move[MAX_MOVES];

        int nextMoveSlot = 0;
        char piece = ' ';

        // cycle through the list of piece coordinates and add their
        // legal move to the move list
        for (int i = 0; i < coordinates.length; i++) {
            piece = pieces.charAt(i);
            switch (Character.toUpperCase(piece)) {
            case 'K':
                if (whiteToMove == (piece == 'K')) {
                    nextMoveSlot = addKingMoves(moveList, i, nextMoveSlot);
                }
                break;
            case 'Q':
                if (whiteToMove == (piece == 'Q')) {
                    nextMoveSlot = addQueenMoves(moveList, i, nextMoveSlot);
                }
                break;
            case 'R':
                if (whiteToMove == (piece == 'R')) {
                    nextMoveSlot = addRookMoves(moveList, i, nextMoveSlot);
                }
                break;
            case 'N':
                if (whiteToMove == (piece == 'N')) {
                    nextMoveSlot = addKnightMoves(moveList, i, nextMoveSlot);
                }
                break;
            case 'B':
                if (whiteToMove == (piece == 'B')) {
                    nextMoveSlot = addBishopMoves(moveList, i, nextMoveSlot);
                }
                break;
            case 'P':
                if (whiteToMove == (piece == 'P')) {
                    nextMoveSlot = addPawnMoves(moveList, i, nextMoveSlot);
                }
                break;
            default:
                throw new RuntimeException("invalid position!");
            }
        }

        // write the moves into an array of the right side
        result = new Move[nextMoveSlot];
        for (int i = 0; i < nextMoveSlot; i++) {
            result[i] = moveList[i];
        }

        // and return it
        return result;
    }

    /**
     * Retrieves the position (as a new instance of {@link Position})
     * occuring after a move is made by the side to move. Should *never*
     * be called directly, use insted
     * {@link egtb.Tablebase#getPositionAfterMove}.
     *
     * @param move  a chess move of type {@link Move}
     *
     * @return  the position after the move is made
     */
    public Position getPositionAfterMove(Move move) {

        byte oldCoordinate = (byte) (move.oldX() + move.oldY() * WIDTH);
        byte newCoordinate = (byte) (move.newX() + move.newY() * WIDTH);

        byte[] updatedCoordinates = new byte[coordinates.length];

        for (int i = 0; i < coordinates.length; i++) {

            updatedCoordinates[i] = coordinates[i];
            if (updatedCoordinates[i] == oldCoordinate) {
                updatedCoordinates[i] = newCoordinate;
            }
        }

       return new Position(!whiteToMove, false, false, 0,
                           new String(pieces), updatedCoordinates,
                           pieces.charAt(symmetryPieceNum),
                           symmetryType);
    }

    /**
     *  Mirrors the current position in the x-coordinate.
     */
    public void mirrorX() {
        int x = 0;
        int y = 0;

        for (int i = 0; i < coordinates.length; i++) {
            x = coordinates[i] % WIDTH;
            y = coordinates[i] / WIDTH;
            coordinates[i] = (byte) ((WIDTH - 1 - x) + WIDTH * y);
        }
    }

    /**
     *  Mirrors the current position in the y-coordinate.
     */
    public void mirrorY() {
        int x = 0;
        int y = 0;

        for (int i = 0; i < coordinates.length; i++) {
            x = coordinates[i] % WIDTH;
            y = coordinates[i] / WIDTH;
            coordinates[i] = (byte) (x + WIDTH * (WIDTH - 1 - y));
        }
    }

    /**
     *  Rotates the curent position 90 degress to the right.
     */
    public void rotate() {
        int x = 0;
        int y = 0;

        for (int i = 0; i < coordinates.length; i++) {
            x = coordinates[i] % WIDTH;
            y = coordinates[i] / WIDTH;
            coordinates[i] = (byte) (y + WIDTH * x);
        }
    }

    /**
     * Mirrors and rotates the position in such a way,
     * that afterward the given piece is inside the
     * symmetry triangle (left, bottom of the board).
     *
     * @param pieceNum  number of piece wich is to be brought
     *                  in the symmetry triangle
     */
    public void getPieceInSymmetryTriangle(int pieceNum) {
        int x = 0;
        int y = 0;

        if (pieceNum == -1) {
            return;
        }

        x = coordinates[pieceNum] % WIDTH;
        y = coordinates[pieceNum] / WIDTH;

        if (x >= WIDTH / 2) {
            mirrorX();
        }

        if (y >= WIDTH / 2) {
            mirrorY();
        }

        x = coordinates[pieceNum] % WIDTH;
        y = coordinates[pieceNum] / WIDTH;

        if (y > x) {
            rotate();
        }
    }

    /**
     * Mirrors the position in the x - coordinate such that
     * the pawn given by pieceNum is on the left side of
     * the board.
     *
     * @param pieceNum  number of the pawn to be brought on
     *                  the left side of the board
     */
    public void getPieceInSymmetryRectangle(int pieceNum) {
        int x = 0;

        if (pieceNum == -1) {
            return;
        }

        x = coordinates[pieceNum] % WIDTH;

        if (x >= WIDTH / 2) {
            mirrorX();
        }
    }

    /**
     * Brings the position into an equivalent normal symmetric form.
     */
    public void normPosition() {
        if (symmetryType == SYMMETRY_TRIANGLE) {
            getPieceInSymmetryTriangle(symmetryPieceNum);
        }

        if (symmetryType == SYMMETRY_RECTANGLE) {
            getPieceInSymmetryRectangle(symmetryPieceNum);
        }
    }

    /**
     * Clones the current position.
     *
     * @return  a clone of the current position.
     */
    public Object clone() {
        return new Position(whiteToMove, illegal, draw, pliesToMate,
                new String(pieces), (byte[]) coordinates.clone(),
                pieces.charAt(symmetryPieceNum), symmetryType);
    }

    /**
     * Checks whether the pieces in given position have the
     * same coordinates like in <code>pos</code>.
     *
     * @param pos  a chess position
     *
     * @return  <code>true</code> if the current position has
     *          the same coordinates of the pieces like
     *          <code>pos</code>, <code>false</code> otherwise
     */
    public boolean hasSameCoordinates(Position pos) {
        byte[] coordinatesPos = pos.getCoordinates();

        if (coordinates.length != coordinatesPos.length) {
            return false;
        }

        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i] != coordinatesPos[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Sets the coordinates of the pieces of the current position.
     *
     * @param coordinates  the new coordinates.
     */
    public void setCoordinates(byte[] coordinates) {
        this.coordinates = coordinates;
    }
}
