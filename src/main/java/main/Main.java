package main;

import main.services.MainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Calendar;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException {
        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        MainService service = applicationContext.getBean(MainService.class);
        int lastPrintedMinute = 0;
        boolean isOldEventsDeleted = false;

        while (true) {
            if (LocalTime.now().compareTo(LocalTime.of(19, 0)) == 0 && !isOldEventsDeleted) {
                isOldEventsDeleted = true;
                service.deleteOldEvents();
            }
            service.getPrintablePerson(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            int actualMinute = Calendar.getInstance().get(Calendar.MINUTE);
            if (actualMinute % 5 == 0 && actualMinute != lastPrintedMinute) {
                lastPrintedMinute = Calendar.getInstance().get(Calendar.MINUTE);
                System.out.println(new Timestamp(System.currentTimeMillis()));
            }
        }
    }
}

