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
import java.util.Date;
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
    Boolean getPrintablePerson(Date timestamp) throws IOException {
        Optional<RegisterEvents> registerEvents = registerEventsRepository.findByLastTimestamp(timestamp);
        String staffName = "";
        if (registerEvents.isPresent()) {
            Integer staff_id = registerEvents.get().getStaffId();
            staffName = staffRepository.findById(staff_id).get().getShortFio();
        }
        sendToPrint(new Person("Имя", new Date()));
        return true;
    }

    boolean sendToPrint(Person person) throws IOException {

        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat format = job.defaultPage();
        Paper paper = new Paper();

        double paperWidth = 3.15 * 72.0; // Ширина бумаги в пунктах (1 дюйм = 72 пункта)
        double paperHeight = 10.0 * 72.0; // Высота бумаги в пунктах (1 дюйм = 72 пункта)

        paper.setSize(paperWidth, paperHeight);
        paper.setImageableArea(0.0, 0.0, paperWidth, paperHeight);

        File image = new File("src/main/resources/moika22_logo_w.png");
        BufferedImage bufferedImage = ImageIO.read(image);
        System.out.println(bufferedImage.toString());
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

                int imageWidth = bufferedImage.getWidth(null);
                int imageHeight = bufferedImage.getHeight(null);
                int x = (int) ((pf.getImageableWidth() - imageWidth) / 2);
                int y = (int) ((pf.getImageableHeight() - imageHeight) / 2);
                g2d.drawImage(bufferedImage, x, y, null);
                g2d.drawString(person.getName(), 0, 20);
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
