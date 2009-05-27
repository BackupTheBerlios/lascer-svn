/*
 * Dateiname      : PraedikatErzeugung.java
 * Letzte �nderung: 28. August 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Dietmar Lippold, 2006
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


package lascer.praedikate;

import java.util.Iterator;
import java.io.Serializable;

/**
 * Definiert die Methoden zur Erzeugung von Pr�dikaten. Die
 * <CODE>Beispieldaten</CODE>, zu denen die Pr�dikate erzeugt werden, werden
 * dem Konsruktor der implementierenden Klasse �bergeben.<P>
 *
 * Ein vollst�ndiges Pr�dikat in Bezug auf eine Klasse ist eines, das f�r alle
 * Beispiele der Klasse erf�llt ist und insbesondere f�r kein Beispiel der
 * Klasse den unbekannten bzw. einen undefinierten Wert liefert. Ein korrektes
 * Pr�dikat in Bezug auf eine Klasse ist eines, das f�r kein Beispiel der
 * anderen Klasse erf�llt ist und insbesondere f�r kein Beispiel der anderen
 * Klasse den unbekannten bzw. einen undefinierten Wert liefert.
 *
 * @author  Dietmar Lippold
 *
 * @see  lascer.problemdaten.Beispieldaten
 */
public interface PraedikatErzeugung extends Serializable {

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die korrekt sind in
     * Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator �ber korrekte Pr�dikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posKorrPraedIter();

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die vollst�ndig sind
     * in Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator �ber vollst�ndige Pr�dikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posVollPraedIter();

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die korrekt sind in
     * Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator �ber korrekte Pr�dikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negKorrPraedIter();

    /**
     * Liefert einen Iterator �ber erzeugte Pr�dikate, die vollst�ndig sind
     * in Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator �ber vollst�ndige Pr�dikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negVollPraedIter();

    /**
     * Liefert einen Iterator �ber allgemeine erzeugte Pr�dikate. Diese
     * sollten weder vollst�ndig noch korrekt in Bezug auf die positiven oder
     * die negativen Beispiele sein. Zu einer bestimmten Menge negativer
     * Beispiele sollten m�glichst viele positive Beispiele von einem Pr�dikat
     * abgedeckt sein.
     *
     * @return  Einen Iterator �ber allgemeine Pr�dikate f�r positive
     *          Beispiele.
     */
    public Iterator posAlgPraedIter();

    /**
     * Liefert einen Iterator �ber allgemeine erzeugte Pr�dikate. Diese
     * sollten weder vollst�ndig noch korrekt in Bezug auf die positiven oder
     * die negativen Beispiele sein. Zu einer bestimmten Menge positiver
     * Beispiele sollten m�glichst viele negative Beispiele von einem Pr�dikat
     * abgedeckt sein.
     *
     * @return  Einen Iterator �ber allgemeine Pr�dikate f�r negative
     *          Beispiele.
     */
    public Iterator negAlgPraedIter();
}

