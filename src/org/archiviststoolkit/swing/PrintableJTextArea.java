package org.archiviststoolkit.swing;

/**
 * Created by IntelliJ IDEA.
 * User: Nathan Stevens
 * Date: Jul 23, 2008
 * Time: 8:57:32 PM
 *
 * Class used to create a JTextArea that can be printed
 * taken from http://forums.sun.com/thread.jspa?threadID=212065&messageID=2361244
 * by Author David J. Ward
 * To change this template use File | Settings | File Templates.
 */

import org.archiviststoolkit.dialog.ErrorDialog;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import javax.swing.JTextArea;

import javax.swing.text.BadLocationException;

public class PrintableJTextArea extends JTextArea implements Printable {

  /**
   * Holds value of property jobName.
   */
  private String jobName = "Print Job for " + System.getProperty("user.name");
  public int left_margin = inchesToPage(0.5);
  public int right_margin = inchesToPage(0.5);
  public int top_margin = inchesToPage(0.5);
  public int bottom_margin = inchesToPage(0.5);
  /**
   * Creates a new instance of PrintableJTextArea
   */
  public PrintableJTextArea() {
    super();
  }

  public PrintableJTextArea(String text) {
    super(text);
  }

  int inchesToPage(double inches) {
    return (int) (inches * 72.0);
  }
  
  // The superclass method JTextComponent.print() returns a boolean
  public boolean print() {

    // Create a printerJob object
    final PrinterJob printJob = PrinterJob.getPrinterJob();

    // Set the printable class to this one since we
    // are implementing the Printable interface
    printJob.setPrintable(this);
    printJob.setJobName(jobName);

    //Collect the print request attributes.
    final HashPrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
    attrs.add(OrientationRequested.PORTRAIT);
    attrs.add(Chromaticity.COLOR);
    attrs.add(Chromaticity.MONOCHROME);
    attrs.add(PrintQuality.NORMAL);
    attrs.add(PrintQuality.DRAFT);
    attrs.add(PrintQuality.HIGH);

    //Assume US Letter size, someone else can do magic for other paper formats
    attrs.add(new MediaPrintableArea(0.25f, 0.25f, 8.0f, 10.5f, MediaPrintableArea.INCH));

    // Show a print dialog to the user. If the user
    // clicks the print button, then print, otherwise
    // cancel the print job
    if (printJob.printDialog()) {
      Thread printerThread = new Thread(new Runnable() {
					public void run() {
            try {
              printJob.print(attrs);
            } catch (Exception PrintException) {
              new ErrorDialog("Error printing log message", PrintException).showDialog();
            }
					}
			}, "Log Printer Thread");
		  printerThread.start();

    // Added to implement the following contract for JTextComponent.print(): 
    // @return {@code true}, unless printing is canceled by the user
		  return true;
    } else { 
    	return false; 
    }
  }

  public int print(java.awt.Graphics graphics, java.awt.print.PageFormat pageFormat, int pageIndex) throws PrinterException {
    Graphics2D g2 = (Graphics2D) graphics;

    //Found it unwise to use the TextArea font's size,
    //We are just printing text so use a a font size that will
    //be generally useful.
    g2.setFont(getFont().deriveFont(10.0f));

    int bodyheight = (int) pageFormat.getImageableHeight();
    int bodywidth = (int) pageFormat.getImageableWidth();
    int lineheight = g2.getFontMetrics().getHeight() - (g2.getFontMetrics().getLeading() / 2);

    int lines_per_page = (bodyheight - top_margin - bottom_margin) / lineheight;

    int start_line = lines_per_page * pageIndex;

    if (start_line > getLineCount()) {
      return NO_SUCH_PAGE;
    }

    int page_count = (getLineCount() / lines_per_page) + 1;

    int end_line = start_line + lines_per_page;

    int linepos = top_margin;

    int lines = getLineCount();

    g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

    for (int line = start_line; line < end_line; line++) {
      try {
        String linetext = getText(
                getLineStartOffset(line),
                getLineEndOffset(line) - getLineStartOffset(line));
        g2.drawString(linetext, left_margin, linepos);
      } catch (BadLocationException e) {
        //never a bad location
      }

      linepos += lineheight;
      if (linepos > bodyheight - bottom_margin) {
        break;
      }
    }

    return Printable.PAGE_EXISTS;
  }
}