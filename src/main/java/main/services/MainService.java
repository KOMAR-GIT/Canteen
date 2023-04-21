package main.services;

import main.model.Person;
import main.model.RegisterEvents;
import main.repositories.RegisterEventsRepository;
import main.repositories.StaffRepository;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class MainService {

    private final RegisterEventsRepository registerEventsRepository;
    private final StaffRepository staffRepository;

    public MainService(StaffRepository staffRepository, RegisterEventsRepository registerEventsRepository) {
        this.registerEventsRepository = registerEventsRepository;
        this.staffRepository = staffRepository;
    }

    //Получаем данные человека, которому нужно распечатать чек
    public void getPrintablePerson(List<RegisterEvents> printedEvents, Timestamp timestamp) throws IOException, PrinterException, ParseException {
        List<RegisterEvents> localPrintedEvents = printedEvents;
        List<RegisterEvents> registerEvents = registerEventsRepository.getEvents();
        if (!registerEvents.isEmpty()) {
            registerEvents.sort(Comparator.comparingInt(RegisterEvents::getIdReg));
            registerEvents = registerEvents.subList(registerEvents.size() - 5, registerEvents.size());
            for (RegisterEvents registerEvent : registerEvents) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date parsedDate = dateFormat.parse(registerEvent.getLastTimestamp());
                Timestamp timestampFromDataBase = new Timestamp(parsedDate.getTime());
                boolean needToPrint = false;
                synchronized (localPrintedEvents) {
                    if (!localPrintedEvents.contains(registerEvent)) {
                        localPrintedEvents.add(registerEvent);
                        needToPrint = true;
                    }
                }
                if (needToPrint && timestampFromDataBase.after(getCorrectTimestamp(timestamp, true))) {
                    Integer staff_id = registerEvent.getStaffId();
                    String staffName = staffRepository.findById(staff_id).get().getShortFio();
                    sendToPrint(new Person(staffName, timestampFromDataBase));
                    System.out.println("Печатаю " + staffName);
                }
            }
        }
    }

    private Timestamp getCorrectTimestamp(Timestamp timestamp, boolean addTenSecond) {
        Timestamp finalTimestamp;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        if (addTenSecond) {
            calendar.add(Calendar.SECOND, -10);
        }
        finalTimestamp = new Timestamp(calendar.getTime().getTime());
        return finalTimestamp;
    }

    public boolean sendToPrint(Person person) throws IOException, PrinterException {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = printerJob.defaultPage();
        ClassLoader cl = getClass().getClassLoader();
        InputStream stream = cl.getResourceAsStream("/moika22_logo_w.png");
        if (stream == null) System.err.println("resource not found");
        BufferedImage bufferedImage = ImageIO.read(stream);
        Paper paper = pageFormat.getPaper();
        double width = 3.15 * 72; // ширина страницы в дюймах и перевод в пиксели
        double height = 11.69 * 72; // высота страницы в дюймах и перевод в пиксели
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        pageFormat.setPaper(paper);
        Printable printable = (graphics, pageFormat1, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat1.getImageableX(), pageFormat1.getImageableY());
            g2d.setFont(new Font("Courier New", Font.BOLD, 11));
            Image image1 = bufferedImage.getScaledInstance(200, 100, Image.SCALE_DEFAULT);
            int imageWidth = image1.getWidth(null);
            int imageHeight = image1.getHeight(null);
            int y = 100;
            g2d.drawImage(image1, 0, 0, imageWidth, imageHeight, null);
            g2d.drawString("Добро пожаловать, ", 0, y + 10);
            g2d.drawString(person.getName() + "!", 0, y + 20);
            g2d.drawString(person.getDate().toString(), 0, y + 30);
            return Printable.PAGE_EXISTS;
        };
        printerJob.setPrintable(printable, pageFormat); // установка параметров печати
        printerJob.print(); // запуск печати

        return true;
    }

}
