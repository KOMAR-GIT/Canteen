package main.services;

import javafx.scene.control.Button;
import main.api.LastEmployee;
import main.model.Person;
import main.model.RegisterEvents;
import main.repositories.RegisterEventsRepository;
import main.repositories.StaffRepository;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterStateReason;
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
import java.util.Optional;

@Service
public class MainService {

    private final RegisterEventsRepository registerEventsRepository;
    private final StaffRepository staffRepository;
    private Integer personsCount;
    private final List<RegisterEvents> printedEvents;
    private boolean isProgramRunning;

    public MainService(StaffRepository staffRepository, RegisterEventsRepository registerEventsRepository) {
        this.registerEventsRepository = registerEventsRepository;
        this.staffRepository = staffRepository;
        personsCount = 0;
        printedEvents = new ArrayList<>();
        isProgramRunning = true;
    }

    public LastEmployee getPrintablePerson(Timestamp timestamp) throws IOException {
        LastEmployee lastEmployee = null;
        //получаем список приложивших карточку к валидатору в столовой за последние 4 секунды
        List<RegisterEvents> registerEvents = registerEventsRepository.getEvents(getCorrectTimestamp(timestamp, true));
        if (!registerEvents.isEmpty()) {
            for (RegisterEvents registerEvent : registerEvents) {
                //Если записи нет в printedEvents, значит печатаем
                if (!printedEvents.contains(registerEvent)
                        && printedEvents.stream().noneMatch(n -> n.getStaffId().equals(registerEvent.getStaffId()))) {
                    printedEvents.add(registerEvent); //добавляем в список распечатанных
                    Integer staffId = registerEvent.getStaffId(); //получаем id сотрудника
                    String staffName = staffId != null
                            ? staffRepository.findById(staffId).get().getShortFio()
                            : "Неизвестный сотрудник"; // находим в бд по id имя сотрудника
                    Person person = new Person(staffName, registerEvent.getLastTimestamp());
                    // Добавляем единицу к счетчику посетителей за сегодня
                    personsCount++;
//                    sendToPrint(person); // печатаем чек
                    lastEmployee = new LastEmployee(person.getName(), personsCount, new SimpleDateFormat("HH:mm").format(registerEvent.getLastTimestamp()));
                    System.out.println("Печатаю " + person.getName() + " " + personsCount + " посетитель за сегодня");
                }
            }
        }
        if (printedEvents.size() > 10) printedEvents.clear();
        return lastEmployee;
    }

    public void deleteOldEvents() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        System.out.println("Удаление записей до: " +
                getCorrectTimestamp(new Timestamp(calendar.getTimeInMillis()), false));
        registerEventsRepository.clearOldEvents(
                getCorrectTimestamp(new Timestamp(calendar.getTimeInMillis()), false));
    }

    private String getCorrectTimestamp(Timestamp timestamp, boolean subtractSomeSeconds) {
        Timestamp finalTimestamp;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        if (subtractSomeSeconds) {
            calendar.add(Calendar.SECOND, -4);
        }
        finalTimestamp = new Timestamp(calendar.getTime().getTime());
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(finalTimestamp);
    }

    public void sendToPrint(Person person) throws IOException {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.getPrintService().getAttributes().get(PrinterStateReason.class).toString();
        PageFormat pageFormat = printerJob.defaultPage();
        ClassLoader cl = getClass().getClassLoader();

        // пытаемся получить эмблему отеля
        InputStream stream = cl.getResourceAsStream("/moika22_logo_w.png");
        if (stream == null) System.err.println("resource not found");
        assert stream != null;
        BufferedImage bufferedImage = ImageIO.read(stream);

        //Выставляем размеры листа печати
        Paper paper = pageFormat.getPaper();
        // ширина страницы в дюймах и перевод в пиксели
        double WIDTH = 226.8;
        // высота страницы в дюймах и перевод в пиксели
        double HEIGHT = 841.68;
        paper.setSize(WIDTH, HEIGHT);
        paper.setImageableArea(0, 0, WIDTH, HEIGHT);
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
    }

    public boolean isProgramRunning() {
        return isProgramRunning;
    }

    public void setProgramRunning(boolean programRunning) {
        isProgramRunning = programRunning;
    }

//    public void changeStartButton(Button startButton) {
//        if(isProgramRunning){
//            isProgramRunning = false;
//            startButton.setText("Запустить");
//            startButton.setStyle("-fx-background-color: #89d960");
//        }else{
//            isProgramRunning = true;
//            startButton.setText("Остановить");
//            startButton.setStyle("-fx-background-color:  #f7746a");
//        }
//    }
}
