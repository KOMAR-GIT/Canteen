package main;

import main.services.MainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.sql.Timestamp;
<<<<<<< HEAD
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
=======
import java.text.ParseException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
>>>>>>> d02ba7edaec2a8e9a40854a0a0b0d536b7537e70

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException {
        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        MainService service = applicationContext.getBean(MainService.class);
        int lastPrintedMinute = 0;

        // Создаем экземпляр Timer
        Timer timer = new Timer();
<<<<<<< HEAD
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 19);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        // Задаем время первого запуска метода deleteRegs() (в 6 вечера сегодня)
        if (today.getTimeInMillis() < System.currentTimeMillis()) {
            today.add(Calendar.DAY_OF_MONTH, 1);
            System.out.println(today.getTime());
        }


        // Задаем период запуска метода deleteRegs() (24 часа)
        long period = 24 * 60 * 60 * 1000;
=======
        LocalTime timeToRun = LocalTime.of(18,0);
>>>>>>> d02ba7edaec2a8e9a40854a0a0b0d536b7537e70

        // Запускаем метод deleteRegs() каждый день в 6 вечера
       TimerTask task = new TimerTask() {
            @Override
            public void run() {
                service.deleteOldEvents();
            }
        };

        while (true) {
<<<<<<< HEAD
            service.getPrintablePerson(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            int actualMinute = Calendar.getInstance().get(Calendar.MINUTE);
            if (actualMinute % 5 == 0 && actualMinute != lastPrintedMinute) {
                lastPrintedMinute = Calendar.getInstance().get(Calendar.MINUTE);
                System.out.println(new Timestamp(System.currentTimeMillis()));
            }

=======
            LocalTime now = LocalTime.now();
            service.getPrintablePerson(printedEvents, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            if (printedEvents.size() > 20) printedEvents.clear();
>>>>>>> d02ba7edaec2a8e9a40854a0a0b0d536b7537e70
        }
    }
}

