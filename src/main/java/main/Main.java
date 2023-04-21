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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        List<RegisterEvents> printedEvents = Collections.synchronizedList(new ArrayList<>());
        MainService service = applicationContext.getBean(MainService.class);

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        for (int i = 0; i < 4; i++) {
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                while (true) {
                    try {
                        service.getPrintablePerson(printedEvents, new Timestamp(Calendar.getInstance().getTimeInMillis()));
                        synchronized (printedEvents) {
                            if (printedEvents.size() > 20) printedEvents.clear();
                        }
                    } catch (IOException | PrinterException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 300, TimeUnit.MILLISECONDS);
        }
    }
//        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
//        List<RegisterEvents> printedEvents = Collections.synchronizedList(new ArrayList<>());
//        MainService service = applicationContext.getBean(MainService.class);
//        Thread thread1 = new Thread(() -> {
//            try {
//                Thread.sleep(500);
//                service.getPrintablePerson(printedEvents, new Timestamp(Calendar.getInstance().getTimeInMillis()));
//            } catch (IOException | PrinterException | ParseException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
//
//        while (true) {
//            if (!thread1.isAlive())
//                thread1.start();
//            else{
//                thread1.join();
//            }
//        }
//    }
}

