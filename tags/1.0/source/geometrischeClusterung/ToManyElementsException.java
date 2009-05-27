/*
 * Datei          : ToManyElementsException.java
 * Letzte �nderung: August 2002
 * Autoren        : Dietmar Lippold, Haiyi Peng, Jing Jing Wei, Yang Zhou
 * Copyright (C)  : Institut f�r Intelligente Systeme Universit�t Stuttgart,
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
 * Die Exception wird ausgeloest, wenn ein uebergebenes Array mehr Elemente
 * besitzt als es fuer den Aufruf einer Methode besitzen sollte.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class ToManyElementsException extends SizeMismatchException {

  /**
   * Erzeugt eine ToManyElementsException ohne weitere Angaben.
   */
  public ToManyElementsException() {
    super();
  }

  /**
   * Erzeugt eine ToManyElementsException mit dem angegebenen Text.
   *
   * @param msg a message
   */
  public ToManyElementsException(String msg) {
    super(msg);
  }
}

