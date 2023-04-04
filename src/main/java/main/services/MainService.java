package main.services;

import main.model.Person;
import main.model.RegisterEvents;
import main.model.Staff;
import main.repositories.RegisterEventsRepository;
import main.repositories.StaffRepository;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MainService {

    private final RegisterEventsRepository registerEventsRepository;
    private final StaffRepository staffRepository;

    public MainService(StaffRepository staffRepository, RegisterEventsRepository registerEventsRepository) {
        this.registerEventsRepository = registerEventsRepository;
        this.staffRepository = staffRepository;
    }

    //Получаем данные человека, которому нужно распечатать чек
    public Boolean getPrintablePerson(Date timestamp) throws IOException {
        Date date = new Date(2021, Calendar.APRIL,26,12,26,0);
        Timestamp timestamp1 = new Timestamp(121,3,26,12,26,0,0);
        Timestamp timestamp2 = new Timestamp(timestamp1.getTime()).setSeconds(timestamp1.getSeconds() + 1);timestamp1.setSeconds(timestamp1.getSeconds() + 1);
        List<Optional<RegisterEvents>> registerEvents = registerEventsRepository.getEvents(101310,);
        String staffName = "";
        if (registerEvents.get(0).isPresent()) {
            Integer staff_id = registerEvents.get(0).get().getStaffId();
            staffName = staffRepository.findById(staff_id).get().getShortFio();
            sendToPrint(new Person(staffName, timestamp));
        }
        return true;
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
