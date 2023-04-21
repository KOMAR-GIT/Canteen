package main;

import main.model.RegisterEvents;
import main.services.MainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException, PrinterException, ParseException {
        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        List<RegisterEvents> printedEvents = Collections.synchronizedList(new ArrayList<>());
        MainService service = applicationContext.getBean(MainService.class);

        // Создаем экземпляр Timer
        Timer timer = new Timer();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 18);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        // Задаем время первого запуска метода deleteRegs() (в 6 вечера сегодня)
        if (today.getTimeInMillis() < System.currentTimeMillis()) {
            today.add(Calendar.DAY_OF_MONTH, 1);
            System.out.println(today.getTime());
        }

        // Задаем период запуска метода deleteRegs() (24 часа)
        long period = 24 * 60 * 60 * 1000;

        // Запускаем метод deleteRegs() каждый день в 6 вечера
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                service.deleteOldEvents();
            }
        }, today.getTime(), period);

        while (true) {
            service.getPrintablePerson(printedEvents, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            if (printedEvents.size() > 20) printedEvents.clear();
        }
//        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        for (int i = 0; i < 2; i++) {
//            scheduledExecutorService.scheduleAtFixedRate(() -> {
//                while (true) {
//                    try {
//                        service.getPrintablePerson(printedEvents, new Timestamp(Calendar.getInstance().getTimeInMillis()));
//                        synchronized (printedEvents) {
//                            if (printedEvents.size() > 20) printedEvents.clear();
//                        }
//                    } catch (IOException | PrinterException | ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 0, 300, TimeUnit.MILLISECONDS);
//        }
    }
}

