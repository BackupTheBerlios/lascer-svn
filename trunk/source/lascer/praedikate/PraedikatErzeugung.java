/*
 * Dateiname      : PraedikatErzeugung.java
 * Letzte Änderung: 28. August 2006
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
 * Definiert die Methoden zur Erzeugung von Prädikaten. Die
 * <CODE>Beispieldaten</CODE>, zu denen die Prädikate erzeugt werden, werden
 * dem Konsruktor der implementierenden Klasse übergeben.<P>
 *
 * Ein vollständiges Prädikat in Bezug auf eine Klasse ist eines, das für alle
 * Beispiele der Klasse erfüllt ist und insbesondere für kein Beispiel der
 * Klasse den unbekannten bzw. einen undefinierten Wert liefert. Ein korrektes
 * Prädikat in Bezug auf eine Klasse ist eines, das für kein Beispiel der
 * anderen Klasse erfüllt ist und insbesondere für kein Beispiel der anderen
 * Klasse den unbekannten bzw. einen undefinierten Wert liefert.
 *
 * @author  Dietmar Lippold
 *
 * @see  lascer.problemdaten.Beispieldaten
 */
public interface PraedikatErzeugung extends Serializable {

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die korrekt sind in
     * Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator über korrekte Prädikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posKorrPraedIter();

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die vollständig sind
     * in Bezug auf die positiven Beispiele.
     *
     * @return  Einen Iterator über vollständige Prädikate in Bezug auf die
     *          positiven Beispiele.
     */
    public Iterator posVollPraedIter();

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die korrekt sind in
     * Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator über korrekte Prädikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negKorrPraedIter();

    /**
     * Liefert einen Iterator über erzeugte Prädikate, die vollständig sind
     * in Bezug auf die negativen Beispiele.
     *
     * @return  Einen Iterator über vollständige Prädikate in Bezug auf die
     *          negativen Beispiele.
     */
    public Iterator negVollPraedIter();

    /**
     * Liefert einen Iterator über allgemeine erzeugte Prädikate. Diese
     * sollten weder vollständig noch korrekt in Bezug auf die positiven oder
     * die negativen Beispiele sein. Zu einer bestimmten Menge negativer
     * Beispiele sollten möglichst viele positive Beispiele von einem Prädikat
     * abgedeckt sein.
     *
     * @return  Einen Iterator über allgemeine Prädikate für positive
     *          Beispiele.
     */
    public Iterator posAlgPraedIter();

    /**
     * Liefert einen Iterator über allgemeine erzeugte Prädikate. Diese
     * sollten weder vollständig noch korrekt in Bezug auf die positiven oder
     * die negativen Beispiele sein. Zu einer bestimmten Menge positiver
     * Beispiele sollten möglichst viele negative Beispiele von einem Prädikat
     * abgedeckt sein.
     *
     * @return  Einen Iterator über allgemeine Prädikate für negative
     *          Beispiele.
     */
    public Iterator negAlgPraedIter();
}

