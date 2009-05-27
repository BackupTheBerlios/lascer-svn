/*
 * Dateiname      : IteratedGreedyStatistic.java
 * Letzte Änderung: 11. August 2006
 * Autoren        : Dietmar Lippold
 * Copyright (C)  : Institut für Intelligente Systeme Universität Stuttgart,
 *                  2006
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


package mengenUeberdeckung.tests.bib;

import java.util.Random;
import java.io.IOException;

import mengenUeberdeckung.tests.bib.parser.ParseException;
import mengenUeberdeckung.allgemein.UeberdeckungsOptimierung;
import mengenUeberdeckung.iteratedGreedyCovering.IterEnhancedGreedyHeuristic;

/**
 * Diese Klasse erzeugt eine Statistik mit dem Iterated-Greedy-Verfahren
 * zur Mengenüberdeckung zu einem Verzeichnis mit Problemdateien. Der Name des
 * Verzeichnisses, in dem die Problemdateien liegen, wird zusammen mit dem
 * Typ der Dateien und dem Namen der Ausgabedatei auf der Komandozeile
 * übergeben.
 *
 * @author  Dietmar Lippold
 */
public class IteratedGreedyStatistic {

    /**
     * Liefert eine Instanz vom Iterated-Greedy-Verfahren zur
     * Mengenüberdeckung.
     *
     * @param iterAnz  Die Anzahl der Iterationen im Iterated-Greedy
     *                 Verfahren. Wenn der Wert kleiner oder gleich Null ist,
     *                 wird ein Standard-Wert verwendet.
     *
     * @return  Ein Verfahren zur Erzeugung einer Mengennüberdeckung.
     */
    private static UeberdeckungsOptimierung scpAlgorithm(int iterAnz) {

        if (iterAnz <= 0) {
            return (new IterEnhancedGreedyHeuristic());
        } else {
            return (new IterEnhancedGreedyHeuristic(new Random(), iterAnz));
        }
    }

    /**
     * Entnimmt die Parameter aus dem übergebenen Array von Strings und
     * erzeugt eine Excel-Datei mit den Ergebnissen.<P>
     *
     * Auf der Komandozeile sind nacheinander folgende Parameter anzugeben,
     * wobei der letzte Parameter optional ist.
     *
     * <OL>
     * <LI>Der Name des Verzeichnisses mit den Problemdateien.
     * <LI>Der Typ der Problemdateien.
     * <LI>Der Name der Datei, in die die Excel-Tabelle geschrieben werden
     *     soll, oder <EM>stdout</EM>, wenn das Ergebnis auf der
     *     Standard-Ausgabe ausgegeben werden soll.
     * <LI>Die Anzahl der Iterationen im Iterated-Greedy Verfahren.
     * </OL>
     *
     * Wird die Anzahl der Iterationen angegeben, wird zusätzlich jeweils ein
     * Zufallsgenerator erzeugt, der bei jeder Ausführung dieses Programms
     * verschieden ist.<P>
     *
     * Als Typ sind folgende Angaben möglich:
     *
     * <DL>
     * <DT>or-bib</DT>
     * <DD>Die Dateien haben das Format der OR-Bibliothek.</DD>
     * <DT>rail-bib</DT>
     * <DD>Die Dateien haben das Format des Wettbewerbs mit den Daten der
     *     Eisenbahngesellschaft.</DD>
     * <DT>triple</DT>
     * <DD>Die Dateien haben das Format eines Steiner triple systems.</DD>
     * <DT>xu-bench</DT>
     * <DD>Die Dateien haben das Format der Benchmarks von Xu.</DD>
     * <DT>setOfSets</DT>
     * <DD>Die Dateien habe das Format, in dem eine Familie von der Methode
     *     <CODE>ItmFamilie.toLibFormat()</CODE> ausgegeben wird.
     * </DL>
     *
     * @param args  Ein Array von Strings, das den Namen des Verzeichnisses
     *              mit den Problemdateien, den Typ der Problemdateien, den
     *              Namen der Ausgabedatei und optional die Anzahl der
     *              Iterationen enthält.
     */
    public static void main(String[] args) {
        UeberdeckungsOptimierung scpVerfahren;

        if ((args.length < 3) || (args.length > 4)) {
            System.err.println("Keine drei oder vier Parameter angegeben");
            System.exit(-1);
        }

        try {
            if (args.length == 4) {
                scpVerfahren = scpAlgorithm(Integer.parseInt(args[3]));
            } else {
                scpVerfahren = scpAlgorithm(0);
            }
            ResultStatisticCreation.createStatistic(args[0], args[1],
                                                    args[2], scpVerfahren);
        } catch (IllegalArgumentException e) {
            System.err.println("Einer der Komandozeilenparameter war nicht"
                               + " korrekt.");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Eine der Dateien hatte nicht das angegebene"
                               + " Format.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Es ist ein Fehler beim Schreiben der Datei"
                               + " aufgetreten.");
            e.printStackTrace();
        }
    }
}

