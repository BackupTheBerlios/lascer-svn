/*
 * Datei          : ToFewElementsException.java
 * Letzte Änderung: August 2002
 * Autoren        : Dietmar Lippold, Haiyi Peng, Jing Jing Wei, Yang Zhou
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2002
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


package geometrischeClusterung;

/**
 * Die Exception wird ausgeloest, wenn ein uebergebenes Array wenige Elemente
 * besitzt als es fuer den Aufruf einer Methode besitzen sollte.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class ToFewElementsException extends SizeMismatchException {

  /**
   * Erzeugt eine ToFewElementsException ohne weitere Angaben.
   */
  public ToFewElementsException() {
    super();
  }

  /**
   * Erzeugt eine ToFewElementsException mit dem angegebenen Text.
   *
   * @param msg a message
   */
  public ToFewElementsException(String msg) {
    super(msg);
  }
}

