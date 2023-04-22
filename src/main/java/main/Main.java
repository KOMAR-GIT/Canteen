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
import java.time.LocalTime;
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
        LocalTime timeToRun = LocalTime.of(18,0);

        // Запускаем метод deleteRegs() каждый день в 6 вечера
       TimerTask task = new TimerTask() {
            @Override
            public void run() {
                service.deleteOldEvents();
            }
        };

        while (true) {
            LocalTime now = LocalTime.now();
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

