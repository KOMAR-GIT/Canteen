package com.moika22.canteen.service;

import com.moika22.canteen.ApplicationProperties;
import com.moika22.canteen.ReportBuilder;
import com.moika22.canteen.api.LastEmployee;
import com.moika22.canteen.model.RegisterEvent;
import com.moika22.canteen.model.Staff;
import com.moika22.canteen.repository.RegisterEventsRepository;
import com.moika22.canteen.repository.StaffRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.List;
import java.util.*;

@Service
@Slf4j
public class MainService {

    private final RegisterEventsRepository registerEventsRepository;
    private final StaffRepository staffRepository;
    private Integer personsCount;
    private final List<RegisterEvent> printedEvents;
    private final Map<String, Integer> persons;
    private boolean isProgramRunning;
    private final ApplicationProperties applicationProperties;
    private String lastTimestamp;
    private final double WIDTH; // ширина страницы в дюймах и перевод в пиксели
    private final double HEIGHT;// высота страницы в дюймах и перевод в пиксели

    private LastEmployee lastEmployee;

    public MainService(StaffRepository staffRepository,
                       RegisterEventsRepository registerEventsRepository,
                       ApplicationProperties applicationProperties) {
        this.registerEventsRepository = registerEventsRepository;
        this.staffRepository = staffRepository;
        this.persons = Collections.synchronizedMap(new HashMap<>());
        this.applicationProperties = applicationProperties;
        personsCount = 0;
        printedEvents = new ArrayList<>();
        isProgramRunning = false;
        getStartTimestamp();
        WIDTH = 226.8;
        HEIGHT = 841.68;
        lastEmployee = new LastEmployee("None", 0, "00:00");
    }


    @Async("taskExecutor")
    public void getPrintablePerson() {
        while (isProgramRunning) {
            //Получение списка последних приложивших карточку к валидатору в столовой
            List<RegisterEvent> registerEvents;
            try {
                registerEvents = registerEventsRepository.getEvents(lastTimestamp);
            } catch (Exception e) {
                log.error(Arrays.toString(e.getStackTrace()));
                log.error("Не удалось получить данные об ивентах из бд");
                continue;
            }

            for (RegisterEvent registerEvent : registerEvents) {
                String staffName = "Неизвестный сотрудник";
                Integer staffId = registerEvent.getStaffId(); //Получение id сотрудника

                if (staffId != null) {
                    Optional<Staff> employee = staffRepository.findById(staffId);
                    if (employee.isPresent()) {
                        staffName = employee.get().getShortFio(); // Поиск имени сотрудника в бд по его id
                    }
                }

                // Добавление единицы к счетчику посетителей за сегодня
                personsCount++;
                lastEmployee = new LastEmployee(staffName, personsCount, registerEvent.getLastTimestamp());
                if (sendToPrint(lastEmployee)) { // печатаем чек

                    printedEvents.add(registerEvent); //добавляем в список распечатанных

                    if (persons.containsKey(lastEmployee.getName())) {
                        persons.merge(lastEmployee.getName(), 1, Integer::sum);
                    } else {
                        persons.put(lastEmployee.getName(), 1);
                    }

                    lastTimestamp = registerEvent.getLastTimestamp();
                }
                if (printedEvents.size() > 10) printedEvents.remove(0);
            }
        }
    }

    private String getCorrectTimestamp(Timestamp timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
    }

    public void createReport(String date) {
        ReportBuilder reportBuilder = new ReportBuilder();
        String reportPath = applicationProperties.getProperty("reports.path") + "/" + date + ".xlsx";
        reportBuilder.createReport(persons, reportPath);
    }

    public void deleteOldEvents() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        String time = getCorrectTimestamp(new Timestamp(calendar.getTimeInMillis()));
        log.info("Удаление ивентов до {}", time);
        registerEventsRepository.clearOldEvents(time);
    }

    private boolean sendToPrint(LastEmployee person) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PageFormat pageFormat = printerJob.defaultPage();
        ClassLoader cl = getClass().getClassLoader();

        // Попытка получить эмблему отеля
        BufferedImage bufferedImage;
        try {
            InputStream stream = cl.getResourceAsStream("static/img/moika22_logo_w.png");
            bufferedImage = ImageIO.read(stream);
        } catch (Exception e) {
            log.warn("Логотип не найден");
            return false;
        }

        //Настройка размеров листа печати
        Paper paper = pageFormat.getPaper();
        paper.setSize(WIDTH, HEIGHT);
        paper.setImageableArea(0, 0, WIDTH, HEIGHT);
        pageFormat.setPaper(paper);

        printerJob.setPrintable(getPage(bufferedImage, person), pageFormat); // установка параметров печати
        try {
            printerJob.print(); // запуск печати
        } catch (PrinterException e) {
            log.error("Ошибка печати");
            return false;
        }
        log.info("Печатается чек для {}. {} посетитель за сегодня", person.getName(), personsCount);
        return true;
    }

    //Создание страницы
    private Printable getPage(BufferedImage logo, LastEmployee person) {
        return (graphics, pageFormat1, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat1.getImageableX(), pageFormat1.getImageableY());
            g2d.setFont(new Font("Courier New", Font.BOLD, 11));
            Image image1 = logo.getScaledInstance(200, 100, Image.SCALE_DEFAULT);
            int imageWidth = image1.getWidth(null);
            int imageHeight = image1.getHeight(null);
            int y = 100;
            g2d.drawImage(image1, 0, 0, imageWidth, imageHeight, null);
            g2d.drawString("Добро пожаловать, ", 0, y + 10);
            g2d.drawString(person.getName() + "!", 0, y + 20);
            g2d.drawString(person.getTime(), 0, y + 30);
            g2d.drawString("Вы " + personsCount + " посетитель", 0, y + 40);
            return Printable.PAGE_EXISTS;
        };
    }

    public boolean isProgramRunning() {
        return isProgramRunning;
    }

    public void setProgramRunning(boolean programRunning) {
        isProgramRunning = programRunning;
    }

    public LocalTime getStartTime() {
        return LocalTime.parse(applicationProperties.getProperty("time.start"));
    }

    public LocalTime getEndTime() {
        return LocalTime.parse(applicationProperties.getProperty("time.stop"));
    }

    public String getDeleteOldEventsTime() {
        return applicationProperties.getProperty("time.old-events");
    }

    public void getStartTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -30);
        lastTimestamp = getCorrectTimestamp(new Timestamp(calendar.getTimeInMillis()));
    }

    public LastEmployee getLastEmployee() {
        return lastEmployee;
    }

    public void clear() {
        personsCount = 0;
        printedEvents.clear();
        getStartTimestamp();
    }
}
