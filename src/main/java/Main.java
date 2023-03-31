
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Main {

    public static void main(String[] args){
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        System.out.println(PrinterJob.getPrinterJob());
        printerJob.setPrintable(new PageImage());
        try {
            printerJob.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    static class PageImage implements Printable {

        public int print(Graphics g, PageFormat pf, int pageNumber)
                throws PrinterException {
            if ( pageNumber > 1 ) {
                return Printable.NO_SUCH_PAGE;
            } else {
                g.drawString("Page " + (pageNumber + 1), 150, 150);
            }
            return Printable.PAGE_EXISTS;
        }

    }
}

