/*
 * Datei          : SizeMismatchException.java
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

import java.lang.IllegalArgumentException;

/**
 * Die Exception wird ausgelöst, wenn die Größe des übergebenen Objekts
 * unterschiedlich ist zur Größe des Objekts <CODE>this</CODE>.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class SizeMismatchException extends IllegalArgumentException {

  /**
   * Erzeugt eine SizeMismatchException ohne weitere Angaben.
   */
  public SizeMismatchException() {
    super();
  }

  /**
   * Erzeugt eine SizeMismatchException mit dem angegebenen Text.
   *
   * @param msg a message
   */
  public SizeMismatchException(String msg) {
    super(msg);
  }
}

