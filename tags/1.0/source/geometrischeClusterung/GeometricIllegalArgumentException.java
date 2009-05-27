/*
 * Datei          : GeometricIllegalArgumentException.java
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
 * Die Exception wird ausgelöst, wenn einer Methode einer Klasse vom Package
 * <CODE>geometrischeClusterung</CODE> ein unzulaessiges Argument uebergeben
 * wurde.
 *
 * @author  Dietmar Lippold
 * @author  Haiyi Peng
 * @author  Jing Jing Wei
 * @author  Yang Zhou
 */
public class GeometricIllegalArgumentException extends IllegalArgumentException {
  /**
   * Erzeugt eine GeometricIllegalArgumentException ohne weitere Angaben.
   */
  public GeometricIllegalArgumentException() {
    super();
  }

  /**
   * Erzeugt eine GeometricIllegalArgumentException mit dem angegebenen
   * Text.
   *
   * @param msg eine message
   */
  public GeometricIllegalArgumentException(String msg) {
    super(msg);
  }
}

