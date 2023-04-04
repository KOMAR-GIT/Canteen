package main.services;

import main.model.Person;
import main.model.RegisterEvents;
import main.model.Staff;
import main.repositories.RegisterEventsRepository;
import main.repositories.StaffRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Service
public class MainService {

    private final RegisterEventsRepository registerEventsRepository;
    private final StaffRepository staffRepository;
    @Value("${validator_id}")
    private int validatorId;
//    private List<RegisterEvents> printedEvents;

    public MainService(StaffRepository staffRepository, RegisterEventsRepository registerEventsRepository) {
        this.registerEventsRepository = registerEventsRepository;
        this.staffRepository = staffRepository;
//        printedEvents = new ArrayList<>();
    }

    //Получаем данные человека, которому нужно распечатать чек
    public Boolean getPrintablePerson(Timestamp timestamp) throws IOException {
        List<Optional<RegisterEvents>> registerEvents =
                registerEventsRepository.getEvents(
                        validatorId,
                        getCorrectTimestamp(timestamp, false),
                        getCorrectTimestamp(timestamp, true));
        if (!registerEvents.isEmpty()) {
            Integer staff_id = registerEvents.get(0).get().getStaffId();
            String staffName = staffRepository.findById(staff_id).get().getShortFio();
//            sendToPrint(new Person(staffName, timestamp));
        }
        System.out.println("не печатаю. Время: " + timestamp);
        return true;
    }

    private String getCorrectTimestamp(Timestamp timestamp, boolean addOneSecond) {
        Timestamp finalTimestamp;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        if (addOneSecond) {
            calendar.add(Calendar.SECOND, 1);
        } else {
            calendar.add(Calendar.SECOND,-2);
        }
        finalTimestamp = new Timestamp(calendar.getTime().getTime());
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(finalTimestamp);
    }

    public boolean sendToPrint(Person person) throws IOException {

        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat format = job.defaultPage();
        Paper paper = new Paper();

        double paperWidth = 10 * 72.0; // Ширина бумаги в пунктах (1 дюйм = 72 пункта)
        double paperHeight = 10.0 * 72.0; // Высота бумаги в пунктах (1 дюйм = 72 пункта)

        paper.setSize(paperWidth, paperHeight);
        paper.setImageableArea(0.0, 0.0, paperWidth, paperHeight);

        format.setPaper(paper);

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
                if (page > 0) {
                    return NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pf.getImageableX(), pf.getImageableY());

                Font font = new Font("Arial", Font.PLAIN, 12);

                g2d.setFont(font);

                g2d.drawString(person.getName(), -10, 20);
                g2d.drawString(person.getDate().toString(), 0, 40);
                return PAGE_EXISTS;
            }
        });

        try {
            job.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
        return true;
    }

}
