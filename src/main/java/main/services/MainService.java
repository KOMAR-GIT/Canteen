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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class MainService {

    private final RegisterEventsRepository registerEventsRepository;
    private final StaffRepository staffRepository;
    private Integer personsCount;
    private final List<RegisterEvents> printedEvents;

    public MainService(StaffRepository staffRepository, RegisterEventsRepository registerEventsRepository) {
        this.registerEventsRepository = registerEventsRepository;
        this.staffRepository = staffRepository;
        personsCount = 0;
        printedEvents = new ArrayList<>();
    }

    public void getPrintablePerson(Timestamp timestamp) throws IOException {
        //получаем список приложивших карточку к валидатору в столовой за последние 4 секунды
        List<RegisterEvents> registerEvents = registerEventsRepository.getEvents(getCorrectTimestamp(timestamp, true));
        if (!registerEvents.isEmpty()) {
            for (RegisterEvents registerEvent : registerEvents) {
                //Если записи нет в printedEvents, значит печатаем
                if (!printedEvents.contains(registerEvent)) {
                    printedEvents.add(registerEvent); //добавляем в список распечатанных
                    Integer staff_id = registerEvent.getStaffId(); //получаем id сотрудника
                    String staffName = staffRepository.findById(staff_id).get().getShortFio(); // находим в бд по id имя сотрудника
                    Person person = new Person(staffName, registerEvent.getLastTimestamp());
                    personsCount++; // Добавляем единицу к счетчику посетителей за сегодня
                    sendToPrint(person); // печатаем чек
                    System.out.println("Печатаю " + person.getName() + ". Он " + personsCount + " за сегодня");
                }
            }
        }
        if (printedEvents.size() > 20) printedEvents.clear();
    }

    public void deleteOldEvents() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        System.out.println("Удаление записей до: " +
                getCorrectTimestamp(new Timestamp(calendar.getTimeInMillis()), false));
        registerEventsRepository.clearOldEvents(
                getCorrectTimestamp(new Timestamp(calendar.getTimeInMillis()), false));
    }

    private String getCorrectTimestamp(Timestamp timestamp, boolean addTenSecond) {
        Timestamp finalTimestamp;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        if (addTenSecond) {
            calendar.add(Calendar.SECOND, -4);
        }
        finalTimestamp = new Timestamp(calendar.getTime().getTime());
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(finalTimestamp);
    }

    public boolean sendToPrint(Person person) throws IOException {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = printerJob.defaultPage();
        ClassLoader cl = getClass().getClassLoader();

        // пытаемся получить эмблему отеля
        InputStream stream = cl.getResourceAsStream("/moika22_logo_w.png");
        if (stream == null) System.err.println("resource not found");
        BufferedImage bufferedImage = ImageIO.read(stream);

        //Выставляем размеры листа печати
        Paper paper = pageFormat.getPaper();
        double width = 3.15 * 72; // ширина страницы в дюймах и перевод в пиксели
        double height = 11.69 * 72; // высота страницы в дюймах и перевод в пиксели
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        pageFormat.setPaper(paper);

        //Прописываем, что и как печатаем на листе
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
            g2d.drawString(person.getDate(), 0, y + 30);
            g2d.drawString("Вы " + personsCount + " посетитель", 0, y + 40);
            return Printable.PAGE_EXISTS;
        };
        printerJob.setPrintable(printable, pageFormat); // установка параметров печати
        try {
            printerJob.print(); // запуск печати
        } catch (PrinterException e) {
            System.out.println("Ошибка печати");
        }

        return true;
    }

}
