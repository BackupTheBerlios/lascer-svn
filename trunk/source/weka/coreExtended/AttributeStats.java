/*
 * Filename     : AttributeStats.java
 * Last change  : 28. May 2005 by Dietmar Lippold
 * Authors      : Len Trigg (len@webmind.com)
 *                Yang Zhou
 * Copyright (C): Len Trigg, 1999
 *                Image Understanding Department, University of Stuttgart, 2002
 *
 * This file was developed on basis of the weka package
 * (http://sourceforge.net/projects/weka).
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


package weka.coreExtended;

/**
 * This class is equal with class weka.core.AttributeStats but makes
 * it possible to use the method addDistinct(double, int) in the package
 * weka.coreExtended .
 *
 * @author  <a href="mailto:len@webmind.com">Len Trigg</a>
 * @author  Yang Zhou
 *
 * @version  8. Jul. 2002
 */
public class AttributeStats extends weka.core.AttributeStats {

  /**
   * Updates the counters for one more observed distinct value.
   *
   * @param value the value that has just been seen
   * @param count the number of times the value appeared
   */
  protected void addDistinct(double value, int count) {
    super.addDistinct(value, count);
  }
}

