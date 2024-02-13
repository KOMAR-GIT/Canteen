package com.moika22.canteen;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws IOException {
        Application.launch(JavaFxApplication.class, args);
    }
}

