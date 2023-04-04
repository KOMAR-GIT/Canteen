package main;

import main.services.MainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.print.PrintException;
import java.io.IOException;
import java.util.Date;

@SpringBootApplication
public class Main  {

    public static void main(String[] args) throws IOException {
        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        MainService service = applicationContext.getBean(MainService.class);
        service.getPrintablePerson(new Date());
    }
}

