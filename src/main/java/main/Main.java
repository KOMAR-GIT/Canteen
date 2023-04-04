package main;

import main.services.MainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        MainService service = applicationContext.getBean(MainService.class);
        while (true) {
            service.getPrintablePerson(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            Thread.sleep(100);
        }
    }
}

