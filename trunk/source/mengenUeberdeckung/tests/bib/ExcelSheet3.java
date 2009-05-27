/*
 * Dateiname      : ExcelSheet3.java
 * Letzte �nderung: 2. Januar 2005
 * Autoren        : Dietmar Lippold, Michael Wohlfart
 * Copyright (C)  : Institut f�r Intelligente Systeme Universit�t Stuttgart,
 *                  2005
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

import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Diese Klasse erzeugt eine Tabelle zu Datens�tzen aus drei Werten.
 *
 * @author  Dietmar Lippold, Michael Wohlfart
 */
public class ExcelSheet3 {

    /**
     * Der Name dieser Tabelle.
     */
    private String tableName;

    /**
     * Die erste Zeile der �berschrift f�r die Tabelle.
     */
    private String header1;

    /**
     * Die zweite Zeile der �berschrift f�r die Tabelle.
     */
    private String header2;

    /**
     * Nummer der Zeile, in die als n�chstes geschrieben wird (Z�hlung der
     * Zeilen und Spalten beginnt bei 0).
     */
    private short line;

    /**
     * Tabelle in die geschrieben wird.
     */
    private HSSFSheet selectedSheet;

    /**
     * Workbook (Menge aus Sheets/Tabellen).
     */
    private HSSFWorkbook workbook;

    /**
     * Erzeugt eine Instanz zu den �bergebenen Werten.
     *
     * @param tableName  Der Name dieser Tabelle.
     * @param header1    Der Text, der in der ersten Zeile des Headers
     *                   ausgegeben werden soll.
     * @param header2    Der Text, der in der zweiten Zeile des Headers
     *                   ausgegeben werden soll.
     */
    public ExcelSheet3(String tableName, String header1, String header2) {
        this.tableName = tableName;
        this.header1 = header1;
        this.header2 = header2;
    }

    /**
     * Diese Methode setzt den Tabellennamen und die �berschriften f�r die
     * Spalten einer Excel-Tabelle und initialisiert die Attribute
     * <CODE>workbook</CODE>, <CODE>selectedSheet</CODE> und <CODE>line</CODE>.
     */
    private void initSheet() {
        HSSFFont      boldFont, italicFont;  // verschiedene Schriftarten
        HSSFRow       row;                   // Zeile
        HSSFCellStyle style;                 // Zellenattribute als style Objekt
        HSSFCell      cell;                  // einzelnen Zelle

        // Zeilennummer auf Anfang setzen.
        line = 0;

        // Sheet anlegen.
        // Ein workbook scheint eine Menge von Excel-Sheets zu sein.
        workbook = new HSSFWorkbook();

        // Name im Excel Tab angeben.
        selectedSheet = workbook.createSheet(tableName);

        // Initialisierung der Schriftarten.
        boldFont = workbook.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        italicFont = workbook.createFont();
        italicFont.setItalic(true);

        // Erste Header-Zeile in bold eintragen.
        row = selectedSheet.createRow(line);
        style = workbook.createCellStyle();
        style.setFont(boldFont);

        cell = row.createCell((short)1);
        cell.setCellStyle(style);
        cell.setCellValue(header1);

        line++;

        // Zweite Header-Zeile in bold eintragen.
        row = selectedSheet.createRow(line);
        style = workbook.createCellStyle();
        style.setFont(boldFont);

        cell = row.createCell((short)1);
        cell.setCellStyle(style);
        cell.setCellValue(header2);

        line++;

        // Leerzeile in Tabelle aufnehmen.
        row = selectedSheet.createRow(line);

        line++;

        // Spaltentitel in italic eintragen.
        row = selectedSheet.createRow(line);
        style = workbook.createCellStyle();
        style.setFont(italicFont);

        cell = row.createCell((short)0);
        cell.setCellStyle(style);
        cell.setCellValue("problemname");

        cell = row.createCell((short)1);
        cell.setCellStyle(style);
        cell.setCellValue("runtime");

        cell = row.createCell((short)2);
        cell.setCellStyle(style);
        cell.setCellValue("solutionCost");

        line++;
    }

    /**
     * Diese Methode scheibt eine Zeile mit Messergebnissen an die durch den
     * Parameter <CODE>row</CODE> vorgegebene Zeile.
     *
     * @param result  Menge mit Messergebnissen.
     * @param row     Zeile, in die geschrieben werden soll.
     */
    private void setRow(ResultSet3 result, HSSFRow row) {
        HSSFFont plainFont;
        HSSFCellStyle style;
        HSSFCell cell;

        plainFont = workbook.createFont();
        style = workbook.createCellStyle();
        style.setFont(plainFont);

        cell = row.createCell((short)0);
        cell.setCellStyle(style);
        cell.setCellValue(result.getProblemName());

        cell = row.createCell((short)1);
        cell.setCellStyle(style);
        cell.setCellValue(result.getRuntime());

        cell = row.createCell((short)2);
        cell.setCellStyle(style);
        cell.setCellValue(result.getSolutionCost());
    }

    /**
     * Liefert zu den �bergebenen Datens�tzen ein workbook, das die
     * zugeh�rige Excel-Tabelle enth�lt.
     *
     * @param rows      Eine Liste von Datens�tzen. Die Elemente sind vom Typ
     *                  <CODE>ResultSet3</CODE>.
     *
     * @return  Das workbook mit der erzeugten Tabelle.
     */
    public HSSFWorkbook generatedSheet(List rows) {
        ResultSet3 result;
        HSSFRow    row;
        Iterator   iterator;

        initSheet();

        // Messdaten eintragen.
        iterator = rows.iterator();
        while (iterator.hasNext()) {
            row = selectedSheet.createRow(line);
            result = (ResultSet3) iterator.next();
            setRow(result, row);
            line++;
        }

        return workbook;
    }
}

